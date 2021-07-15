package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EffectException;
import it.polimi.ingsw.exceptions.MoveException;
import it.polimi.ingsw.exceptions.ShootException;
import it.polimi.ingsw.model.cards.AmmunitionCard;
import it.polimi.ingsw.model.cards.EmpowerCard;
import it.polimi.ingsw.model.cards.Stack;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.model.MoveType.*;
/**
 * Action Class is used to describe an action made by player.
 * An Action is a group of moves.
 * Player normally can do two action each turn
 */
public class Action {
    private static final int ZERO = 0;
    private final Player activePlayer;
    private List<Move> moves;
    private int moveCounter;
    private final boolean finalFrenzy;
    private List<MoveType> validMoves;
    private boolean isActive;
    private final Map map;
    private Stack<EmpowerCard> empowersStack;
    private Stack<AmmunitionCard> ammosStack;

    /**
     * Constructor
     * @param map is playing map
     * @param activePlayer is the active player that is doing the action
     * @param finalFrenzy is true if is a frenzy action
     * @param empowersStack is the stack of empowers
     * @param ammosStack is the stack of the ammos.
     */
    public Action(Map map, Player activePlayer, boolean finalFrenzy, Stack<EmpowerCard> empowersStack, Stack<AmmunitionCard> ammosStack) {
        this.activePlayer = activePlayer;
        this.finalFrenzy = finalFrenzy;
        moves = new ArrayList<>();
        validMoves = new ArrayList<>();
        moveCounter = ZERO;
        isActive = true;
        this.map = map;
        this.empowersStack = empowersStack;
        this.ammosStack = ammosStack;
        setValidMoves();
    }

    /**
     * @return moves done.
     */
    public List<Move> getMoves() {
        return moves;
    }


    /**
     *Sets valid moves if the action is normal.
     * Valid moves are moves that player can do normally each turn
     *
     */
    private void setValidNormalMoves() {
        Move previous = null;
        switch (moveCounter) {
            case ZERO:
                validMoves.addAll(Arrays.asList(RUN, GRAB, SHOOT, RELOAD, END_ACTION));
                break;
            case 1:
                previous = moves.get(ZERO);
                if (previous.hasType(RUN)) {
                    validMoves.addAll(Arrays.asList(RUN, GRAB, RELOAD, END_ACTION));
                    if (activePlayer.getNumDamages() > 5)
                        validMoves.add(SHOOT);
                }else if (previous.hasType(GRAB) || previous.hasType(SHOOT)) {
                    validMoves = new ArrayList<>();
                    validMoves.add(RELOAD);
                    validMoves.add(END_ACTION);
                }else{
                    validMoves = new ArrayList<>();
                    validMoves.add(END_ACTION);
                }
                break;
            case 2:
                previous = moves.get(1);
                if(previous.hasType(RUN)) {
                    validMoves.addAll(Arrays.asList(RUN, RELOAD, END_ACTION));
                    if(activePlayer.getNumDamages() > 2)
                        validMoves.add(GRAB);
                }else if(previous.hasType(GRAB) || previous.hasType(SHOOT)){
                    validMoves = new ArrayList<>();
                    validMoves.add(RELOAD);
                    validMoves.add(END_ACTION);
                }else{
                    validMoves = new ArrayList<>();
                    validMoves.add(END_ACTION);
                }
                break;
            case 3:
                previous = moves.get(2);
                if(previous.hasType(RUN) || previous.hasType(GRAB)) {
                    validMoves = new ArrayList<>();
                    validMoves.add(RELOAD);
                    validMoves.add(END_ACTION);
                }else{
                    validMoves = new ArrayList<>();
                    validMoves.add(END_ACTION);
                }
                break;
            default:
                validMoves = new ArrayList<>();
                break;
        }
    }

    /**
     * This sets moves that player can do if he is before the first player during
     * final frenzy mode.
     */
    private void setValidFinalFrenzyBeforeFirstPlayerMoves() {
        Move previous = null;
        switch (moveCounter) {
            case ZERO:
                validMoves.addAll(Arrays.asList(RUN, GRAB, SHOOT, RELOAD));
                validMoves.add(END_ACTION);
                break;
            case 1:
                previous = moves.get(ZERO);
                if (previous.hasType(RUN)) {
                    validMoves.addAll(Arrays.asList(RUN, GRAB, SHOOT, RELOAD));
                    validMoves.add(END_ACTION);
                } else if (previous.hasType(RELOAD)) {
                    validMoves = new ArrayList<>();
                    validMoves.add(SHOOT);
                    validMoves.add(END_ACTION);
                }else{
                    validMoves = new ArrayList<>();
                    validMoves.add(END_ACTION);
                }
                break;
            case 2:
                previous = moves.get(1);
                if(previous.hasType(RUN)) {
                    validMoves.addAll(Arrays.asList(RUN, GRAB, END_ACTION));
                }else if(previous.hasType(RELOAD)){
                    validMoves = new ArrayList<>();
                    validMoves.add(SHOOT);
                    validMoves.add(END_ACTION);
                }else{
                    validMoves = new ArrayList<>();
                    validMoves.add(END_ACTION);
                }
                break;
            case 3:
                previous = moves.get(2);
                if(previous.hasType(RUN)) {
                    validMoves = new ArrayList<>();
                    validMoves.add(RUN);
                    validMoves.add(END_ACTION);
                }else{
                    validMoves = new ArrayList<>();
                    validMoves.add(END_ACTION);
                }
                break;
            default:
                validMoves = new ArrayList<>();
                break;
        }
    }

    /**
     * This sets final frenzy possible move if player is after first player
     */
    private void setValidFinalFrenzyAfterFirstPlayerMoves() {
        Move previous = null;
        switch (moveCounter) {
            case ZERO:
                validMoves.addAll(Arrays.asList(RUN, GRAB, SHOOT, RELOAD, END_ACTION));
                break;
            case 1:
                previous = moves.get(ZERO);
                if (previous.hasType(RUN)) {
                    validMoves.addAll(Arrays.asList(RUN, GRAB, SHOOT, RELOAD, END_ACTION));
                } else if (previous.hasType(RELOAD)) {
                    validMoves = new ArrayList<>();
                    validMoves.add(SHOOT);
                    validMoves.add(END_ACTION);
                }else{
                    validMoves = new ArrayList<>();
                    validMoves.add(END_ACTION);
                }
                break;
            case 2:
                previous = moves.get(1);
                if(previous.hasType(RUN)) {
                    validMoves.addAll(Arrays.asList(RUN, GRAB, SHOOT, RELOAD, END_ACTION));
                }else if(previous.hasType(RELOAD)){
                    validMoves = new ArrayList<>();
                    validMoves.add(SHOOT);
                    validMoves.add(END_ACTION);
                }else{
                    validMoves = new ArrayList<>();
                    validMoves.add(END_ACTION);
                }
                break;
            case 3:
                previous = moves.get(2);
                if(previous.hasType(RUN)) {
                    validMoves = new ArrayList<>();
                    validMoves.add(GRAB);
                    validMoves.add(END_ACTION);
                }else if(previous.hasType(RELOAD)){
                    validMoves = new ArrayList<>();
                    validMoves.add(SHOOT);
                    validMoves.add(END_ACTION);
                }else{
                    validMoves = new ArrayList<>();
                    validMoves.add(END_ACTION);
                }
                break;
            default:
                validMoves = new ArrayList<>();
                break;
        }
    }

    /**
     *
     * @param move is a move
     * @return true if move is valid, so move is in validMoves
     */
    public boolean isValidMove(Move move) {
        if( move != null && move.getPlayer() == activePlayer && isActive && validMoves.contains(move.getType()) && move.isValidMove())
            return true;
        return false;
    }

    /**
     * This performs the move,
     * @param move is move that has to be performed
     * @throws MoveException if move is not valid
     * @throws ShootException if move is a shoot move and player can't shoot
     * @throws EffectException if an effect is used during move and isn't usable.
     */
    public void performMove(Move move) throws MoveException, ShootException, EffectException {
        if (move.hasType(END_ACTION)){
            endAction();
        }else {
            if (!isValidMove(move)) throw new MoveException("La mossa non è valida.");
            move.performMove(empowersStack, ammosStack);
            moves.add(move);
            moveCounter++;
            setValidMoves();
        }
        if(validMoves.isEmpty() || (validMoves.contains(END_ACTION) && validMoves.size() == 1))
            endAction();
    }

    /**
     * Sets ValidMoves.
     * is based on the action status. If final frenzy is true and player is before
     * first player, so valid moves are from setFinalFrenzyBeforeFirstPlayerMoves()...
     */
    private void setValidMoves() {
        validMoves = new ArrayList<>();
        if(finalFrenzy) {
            if(activePlayer.isBeforeFirstPlayer())
                setValidFinalFrenzyBeforeFirstPlayerMoves();
            else
                setValidFinalFrenzyAfterFirstPlayerMoves();
        }else{
            setValidNormalMoves();
        }
        //If the player doesn't have loaded weapons, he can't shoot
        List<WeaponCard> loadedWeapons = activePlayer.getLoadedWeapons();
        if(validMoves.contains(SHOOT) && loadedWeapons.isEmpty())
            validMoves.remove(SHOOT);
        //If there aren't other players on map player can't shoot
        if(validMoves.contains(SHOOT) && map.getPlayersOnMap().size() == 1)
            validMoves.remove(SHOOT);
        //If player stays in a position where he can't grab
        Square position = map.getPlayerPosition(activePlayer);
        if(position != null) {
            if (validMoves.contains(GRAB) && ((position.isRespawnPoint() && map.getPlayerPosition(activePlayer).getWeaponsSize() == ZERO)
                    || (!position.isRespawnPoint() && map.getPlayerPosition(activePlayer).getAmmos() == null)))
                validMoves.remove(GRAB);
            //If all weapons are loaded,player cant't reload
            if (validMoves.contains(RELOAD) && loadedWeapons.containsAll(activePlayer.getWeapons()))
                validMoves.remove(RELOAD);
        }
    }

    /**
     *
     * @return valid Moves
     */
    public List<MoveType> getValidMoves() {
        return validMoves;
    }

    /**
     *
     * @return true if action is active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * ends the action
     */
    public void endAction() {
        this.isActive = false;
        validMoves = new ArrayList<>();
    }

    /**
     *
     * @return move counter
     */
    public int getMoveCounter() {
        return moveCounter;
    }
}