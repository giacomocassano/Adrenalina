package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EffectException;
import it.polimi.ingsw.exceptions.MoveException;
import it.polimi.ingsw.exceptions.ShootException;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.Stack;
import it.polimi.ingsw.model.cards.effects.RecoilMovement;
import it.polimi.ingsw.model.cards.effects.ShootEffect;
import it.polimi.ingsw.model.cards.effects.ShooterMovement;
import it.polimi.ingsw.model.cards.effects.UltraDamage;
import it.polimi.ingsw.utils.*;
import it.polimi.ingsw.utils.ModelObserver;

import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.model.MoveType.*;

/**
 * Main class of all model package.
 * All parts of model are accessible from this class.
 * It represent the match and contains all handle methods called from controller to handle the match.
 */
public class Game {

    private static final int MAX_DAMAGE = 12;
    private static final int MAX_NUM_PLAYER = 5;
    private static final String FINAL_FRENZY_MESSAGE1 = "Frenesia finale attivata!!!";
    private static final String FINAL_FRENZY_MESSAGE2 = "FRENESIA FINALE ATTIVATA";
    private static final String SHOT_NOT_VALID_MESSAGE1 = " ha provato a sparare in modo non valido!\n";
    private static final String SHOT_NOT_VALID_MESSAGE2 = " hai sparato in un modo non valido!";
    private static final String NOT_ENOUGH_RESOURCES_TO_RECHARGE_MESSAGE = " hai provato a ricaricare un'arma ma non potevi pagarla!";
    private static final String SETTING_EMPOWERS_ERROR_MESSAGE = "Attenzione! Non hai settato bene il tuo potenziamento!";
    private static final String NO_EMPOWERS_MESSAGE = "Non hai potenziamenti che puoi utilizzare. Attendi gli altri giocatori.";
    private final List<Player> players;
    private List<Player> deadPlayers;
    private Map map;
    private KillShotTrack killshotTrack;
    private Stack<AmmunitionCard> ammoStack;
    private Stack<EmpowerCard> empowerStack;
    private Stack<WeaponCard> weaponStack;
    private boolean isOver;
    private boolean isFirstRound;
    private Turn activeTurn;
    private boolean isPlayerShooting;
    private boolean isLastRound;
    private List<ModelObserver> observers;

    /**
     * This is Game Costructor.
     * @param players are players that are playing the game.
     * @param skulls is the number of skulls
     * @param mapType is map type.
     */
    public Game(List<Player> players, int skulls, int mapType) {
        this.players = players;
        this.activeTurn = new Turn();
        this.activeTurn.setActivePlayer(players.get(0));
        this.activeTurn.getActivePlayer().setFirstPlayer(true);
        this.deadPlayers = new ArrayList<>();
        this.killshotTrack = new KillShotTrack(skulls, this);
        this.isOver = false;
        this.activeTurn.setRemainingActions(2);
        this.isFirstRound = true;
        this.map = new Map(mapType);
        isPlayerShooting = false;
        isLastRound = false;
        activeTurn.setActiveEmpowerPlayers(new ArrayList<>());
        observers = new ArrayList<>();
        WriterHelper.printWithTag("GAME", "I giocatori sono " + players.get(0).getName() + ", " + players.get(1).getName() + " e " + players.get(2).getName() + "\n");
    }

    /**
     * Game constructor called when server is reloading and existing match.
     * @param players are players that are playing the game.
     * @param activePlayer is the actual active player.
     * @param map is game map.
     * @param isFirstRound is true if game is reloaded from first round.
     * @param isLastRound is true if game is reloaded from last round.
     */
    public Game(List<Player> players, Player activePlayer,Map map,Boolean isFirstRound,Boolean isLastRound) {
        this.players = players;
        this.activeTurn = new Turn();
        this.activeTurn.setActivePlayer(activePlayer);
        this.activeTurn.getActivePlayer().setFirstPlayer(true);
        this.deadPlayers = new ArrayList<>();
        this.isOver = false;
        this.activeTurn.setRemainingActions(2);
        this.map = map;
        isPlayerShooting = false;
        this.isFirstRound = isFirstRound;
        this.isLastRound = isLastRound;
        activeTurn.setActiveEmpowerPlayers(new ArrayList<>());
        observers = new ArrayList<>();
        WriterHelper.printWithTag("GAME", "I giocatori sono " + players.get(0).getName() + ", " + players.get(1).getName() + " e " + players.get(2).getName() + "\n");
    }

    /**
     * Method called from server to reload stacks.
     * @param ammoStack is the stack of ammunition cards.
     * @param empowerStack is the stack of empower cards.
     * @param weaponStack is the stack of weapon cards.
     */
    public void reloadStacks(Stack<AmmunitionCard> ammoStack, Stack<EmpowerCard> empowerStack, Stack<WeaponCard> weaponStack) {
        this.ammoStack = ammoStack;
        this.empowerStack = empowerStack;
        this.weaponStack = weaponStack;
    }

    /**
     * Method used in fisrt round to add to the active player two empowers before his first spawn.
     */
    private void addFirstSpawnEmpowers() {
        EmpowerCard e1 = (EmpowerCard) empowerStack.pickCard();
        EmpowerCard e2 = (EmpowerCard) empowerStack.pickCard();
        getActivePlayer().addEmpower(e1);
        getActivePlayer().addEmpower(e2);
    }

    /**
     * Loads ths 3 game stack from XML file and it shuffles them.
     */
    public void startDecksMapsFromXML(){
        XMLHelper xmlHelper=new XMLHelper();
        this.ammoStack = new Stack<>(xmlHelper.forgeAmmoDeck());
        this.empowerStack = new Stack<>(xmlHelper.forgeEmpowerDeck());
        this.weaponStack = new Stack<>(xmlHelper.forgeWeaponDeck());
        ammoStack.shuffle();
        weaponStack.shuffle();
        empowerStack.shuffle();
        refillMap();
    }

    /**
     * Adds empowers and ask to born to player.
     */
    public void firstBorn() {
        addFirstSpawnEmpowers();
        messageOtherPlayer(NotifyMessageBuilder.buildSpawnMessage(getActivePlayer()));
        observers.forEach(o -> o.notifySpawnRequest(getActivePlayer()));
    }

    /**
     * Updates the active player at the end of the turn.
     * If player is disconnected it jumps to next player.
     */
    public void updateActivePlayer() {
        //Update the active player
        int activeIndex = players.indexOf(activeTurn.getActivePlayer());
        if (activeIndex == (players.size() - 1)) {
            if (isFirstRound) {
                isFirstRound = false;
            }
            activeTurn.setActivePlayer(players.get(0));
        } else {
            activeTurn.setActivePlayer(players.get(activeIndex + 1));
        }
        //If active player is disconnected, also update
        if(!activeTurn.getActivePlayer().isConnected()){
            if(isFirstRound) {
                addFirstSpawnEmpowers();
                this.respawnDisconnectedPlayer(getActivePlayer());
                WriterHelper.printlnColored(ANSIColors.YELLOW_BOLD, getActivePlayer().getName() + " prima nascita casuale");
            }
            updateActivePlayer();
        }
    }

    /**
     * Method called after each move, it notify the request of the move
     */
    public void nextMove() {
        observers.forEach(o -> o.notifyMoveRequest(getActivePlayer(), isFinalFrenzy(), activeTurn.getActiveAction().getValidMoves(), 3 - activeTurn.getRemainingActions(), activeTurn.getActiveAction().getMoveCounter()+1));
        messageOtherPlayer(NotifyMessageBuilder.buildMoveMessage(getActivePlayer(), activeTurn.getActiveAction().getMoveCounter()+1, 3-activeTurn.getRemainingActions()));
    }

    /**
     * Performs the first spawn of a player.
     * Player spawns on the respawn point with dropped empower card color.
     * @param empToDrop is the empower dropped by active player.
     */
    public void performFirstSpawn(EmpowerCard empToDrop) {
        //Drop the empower
        empowerStack.discard(empToDrop);
        activeTurn.getActivePlayer().removeEmpower(empToDrop);
        //Perform first spawn
        Color respawnColor = empToDrop.getColor();
        map.respawnPlayer(activeTurn.getActivePlayer(), respawnColor);
        updateGameRepContext(false);
    }

    /**
     * Sets the next action with right player.
     */
    public void nextAction() {
        activeTurn.setActiveAction(new Action(map, activeTurn.getActivePlayer(), killshotTrack.isFinalFrenzy(), empowerStack, ammoStack));
    }

    /**
     * Called when the client answers to the request of the move.
     * It ask the use of the chosen move type to active player.
     * @param type is the type of chosen move.
     */
    public void handleMove(MoveType type) {
        Square playerPosition = map.getPlayerPosition(activeTurn.getActivePlayer());
        switch (type) {
            case RUN:
                observers.forEach(o -> o.notifySquareToMoveRequest(getActivePlayer(), map));
                break;
            case GRAB:
                //If the player is not on a respawn point, he grabs the ammunition card on his position
                if (!playerPosition.isRespawnPoint()) {
                    observers.forEach(o -> o.notifyGrabAmmoRequest(getActivePlayer(), map));
                } else { //If the player is on a respawn point
                    observers.forEach(o -> o.notifyGrabWeaponRequest(getActivePlayer(), map));
                }
                break;
            case SHOOT:
                observers.forEach(o -> o.notifyShootWeaponRequest(getActivePlayer()));
                break;
            case RELOAD:
                observers.forEach(o -> o.notifyWeaponsToReload(getActivePlayer()));
                break;
            case END_ACTION:
                //End the action
                activeTurn.getActiveAction().endAction();
                messageOtherPlayer(getActivePlayer().getName() + " ha concluso l'azione.");
                break;
        }

    }

    /**
     * Method called after each action, it notifies the client with the moves they can do.
     * @param players are players that can use empowers.
     */
    public void requestEmpowerUses(List<Player> players) {
        activeTurn.setActiveEmpowerPlayers(new ArrayList<>());
        for (Player player : players) {
            List<EmpowerType> valids = this.getValidEmpowerTypes(player);
            //Notify the client
            if (!valids.isEmpty()) {
                activeTurn.getActiveEmpowerPlayers().add(player);
                ModelObserver observer = getObserver(player);
                if(observer != null)
                    observer.notifyEmpowerRequest(player, valids);
            } else {
                ModelObserver observer = getObserver(player);
                if(observer != null)
                    observer.notifyMessage(NO_EMPOWERS_MESSAGE);
            }
        }
    }

    /**
     * Method that build the list of empower types that player can use.
     * @param player is the player that can use empowers
     * @return the list of empower types that player can use.
     */
    public List<EmpowerType> getValidEmpowerTypes(Player player) {
        List<Player> justShotPlayers = players.stream().filter(Player::isJustShot).collect(Collectors.toList());
        List<EmpowerType> valids = new ArrayList<>();
        List<EmpowerType> emps = player.getEmpowers().stream().map(EmpowerCard::getType).collect(Collectors.toList());
        if (player == activeTurn.getActivePlayer()) {
            //If the active player has just shooted, he can use targeting scope
            if (emps.contains(EmpowerType.TARGETING_SCOPE) && !justShotPlayers.isEmpty())
                valids.add(EmpowerType.TARGETING_SCOPE);
            //The active player can use newton
            if (emps.contains(EmpowerType.NEWTON) && map.getPlayersOnMap().size() != 1)
                valids.add(EmpowerType.NEWTON);
            //The active player can use teleporter
            if (emps.contains(EmpowerType.TELEPORTER))
                valids.add(EmpowerType.TELEPORTER);
        } else { //If there are player that has just been shooted, they can use tagback granade
            if (emps.contains(EmpowerType.TAGBACK_GRANADE) && justShotPlayers.contains(player))
                valids.add(EmpowerType.TAGBACK_GRANADE);
        }
        return valids;
    }

    /**
     * Gets the chosen empower from player and request the use of it.
     * @param player is the player who uses the empower.
     * @param empowerCard is the empower to use.
     */
    public void handleEmpower(Player player, EmpowerCard empowerCard) {
        ModelObserver observer = getObserver(player);
        if(observer != null)
            observer.notifyEmpowerUseRequest(getActivePlayer(), player, map, map.getPlayersOnMap(), empowerCard);
    }

    /**
     * Control is the empower use is valid. If is not valid signals the error to client.
     * @param user is the player who uses the empowers.
     * @param emp is the empower to control
     * @param target is the target player selected
     * @param position is the target position selected
     * @return true if the use is valid.
     */
    public boolean isValidEmpowerUse(Player user, EmpowerCard emp, Player target, Square position) {
        emp.setActive(true);
        emp.setUser(user);
        emp.setTarget(target);
        emp.setPosition(position);
        boolean isValid = emp.isValidEmpowerUse(map, getActivePlayer());
        if (!isValid){
            ModelObserver observer = getObserver(user);
            if(observer != null)
                observer.notifyErrorMessage(SETTING_EMPOWERS_ERROR_MESSAGE);
        }
        return isValid;
    }

    /**
     * Performs the use of selected empower card.
     * @param user if the player that use empower
     * @param emp is the used empower.
     * @param target is the target player.
     * @param position is the target position.
     */
    public void handleEmpowerUse(Player user, EmpowerCard emp, Player target, Square position) {
        emp.setActive(true);
        emp.setUser(user);
        emp.setTarget(target);
        emp.setPosition(position);
        emp.performEmpower(map, getActivePlayer(), empowerStack);
        //remove empower from player empowers
        user.removeEmpower(emp);
        empowerStack.discard(emp);
        //Notify players
        messageOtherPlayer(NotifyMessageBuilder.buildEmpowerUseMessage(user, emp));
        updateGameRepContext(false);
    }

    /**
     * Controls if there are dead player. If a player is death,
     * it removes him from map and it adds the dead player to the dead list.
     * Also it add a death in killshot track, It assigns the dead point
     * to other player and it add a death on player board.
     */
    public void checkDeaths() {
        int countDeaths =0;
        for (Player p : players) {
            //if the player is dead
            if (p.isDead()) {
                countDeaths++;
                this.deadPlayers.add(p);
                if(!isLastRound) {
                    if (p.getNumDamages() == MAX_DAMAGE) {
                        killshotTrack.addKill(p.getDamages().get(MAX_DAMAGE - 1).getShooter(), true);
                    } else if (p.getNumDamages() == MAX_DAMAGE - 1)
                        killshotTrack.addKill(p.getDamages().get(MAX_DAMAGE - 2).getShooter(), false);
                }
                if(!isLastRound)
                    p.addDeath();
                int max = 0;
                int tmp;
                Player bestPlayer = null;
                if(!isLastRound)
                    p.firstDamage().getShooter().addPoints(1);
                for (int i = 0; i < players.size(); i++) {
                    for (Player player : players) {
                        tmp = p.getNumDamages(player);
                        if (tmp >= max && tmp != 0) {
                            if (tmp == max)
                                bestPlayer = solveParity(p, tmp);
                            else {
                                max = tmp;
                                bestPlayer = player;
                            }
                        }
                    }
                    if (max != 0 && bestPlayer != null) {
                        if(!isLastRound)
                            bestPlayer.addPoints(p.removeAvaiblePoints());
                        else
                            bestPlayer.addPoints(p.removeFrenzyPoints());
                        p.removeDamageByPlayer(bestPlayer);
                    }
                    max = 0;
                }
            }
        }
        if (countDeaths>1){
            getActivePlayer().addPoints(1);
        }
        updateGameRepContext(false);
    }


    /**
     * Notifies dead players with a respawn request and add to them a empower card as rules say.
     */
    public void handleDeaths() {
        if (!deadPlayers.isEmpty()) {
            observers.stream().filter(o -> !deadPlayers.contains(o.getPlayer())).forEach(o -> o.notifyMessage(NotifyMessageBuilder.buildDeathMessage()));
            for (Player dead : new ArrayList<>(deadPlayers)) {
                EmpowerCard picked = (EmpowerCard) empowerStack.pickCard();
                dead.getEmpowers().add(picked);
                if (dead.isConnected()) {
                    ModelObserver observer = getObserver(dead);
                    if (observer != null)
                        observer.notifyRespawnRequest(dead);
                } else {
                    this.respawnDisconnectedPlayer(dead);
                    WriterHelper.printlnColored(ANSIColors.YELLOW_BOLD, dead.getName() + " respawnato casualmente");
                }
            }
        }
    }

    /**
     * Perform the spawn of a player. Player are respawned in the respawn point having the color of the dropped empower card.
     * @param player is the player to respawn
     * @param dropped is the dropped empower card
     */
    public void performRespawn(Player player, EmpowerCard dropped) {
        //Remove player from the dead players list
        deadPlayers.remove(player);
        //Remove the dropped card from the player deck and add it to the discarded empowers stack
        player.getEmpowers().remove(dropped);
        empowerStack.discard(dropped);
        //Remove all damages from player
        player.resetDamages();
        //Put the player in the respawn point
        map.respawnPlayer(player, dropped.getColor());
        //Notify players
        messageOtherPlayer(NotifyMessageBuilder.buildRespawnMessage(player, dropped));
        updateGameRepContext(false);
    }

    /**
     * Abort the action.
     */
    public void abortAction() {
        nextMove();
    }

    /**
     * Perform the player movement. Player moves one square.
     * @param position is the position where player moves.
     * @throws MoveException if move is not valid.
     * @throws ShootException if the shoot is not valid. (not here)
     * @throws EffectException if an effect is not valid (not here)
     */
    public void performMoveSingleSquarePlayer(Square position) throws MoveException, ShootException, EffectException {
        Move move = new Move(activeTurn.getActivePlayer(), map, RUN);
        move.setSquare(position);
        activeTurn.getActiveAction().performMove(move);
        updateGameRepContext(false);
        messageOtherPlayer(activeTurn.getActivePlayer().getName() + " si è spostato nella cella ("
                + position.getRow() + ",  " + position.getColumn() + ")");
    }

    /**
     * Control tha grab move and ask the confir to the player
     * @throws MoveException if move is not valid.
     * @throws ShootException if the shoot is not valid. (not here)
     * @throws EffectException if an effect is not valid (not here)
     */
    public void handleGrabAmmos() throws MoveException, ShootException, EffectException {
        AmmunitionCard ammoCard = map.getPlayerPosition(getActivePlayer()).getAmmos();
        activeTurn.getActiveAction().performMove(new Move(activeTurn.getActivePlayer(), map, GRAB));
        //Notify grab
        messageOtherPlayer(NotifyMessageBuilder.buildGrabAmmoMessage(getActivePlayer(), ammoCard));
        updateGameRepContext(false);
    }

    /**
     * Perform the grab of weapon.
     * Notifies error if player can't pay the weapon.
     * @param weaponToGrab is the grabbed weapon.
     * @param weaponToDrop is the dropped weapon if player had 3 weapons yet.
     * @throws MoveException if the move is not valid
     * @throws ShootException if shoot is not valid (not here)
     * @throws EffectException if effect is not valid (not here)
     */
    public void handleGrabWeapon(WeaponCard weaponToGrab, WeaponCard weaponToDrop) throws MoveException, ShootException, EffectException {
        Move move = new Move(activeTurn.getActivePlayer(), map, GRAB);
        move.setGainedWeapon(weaponToGrab);
        move.setDroppedWeapon(weaponToDrop);
        //Control weapon payment
        AmmoCubes cost = weaponToGrab.getBuyCost();
        if(getActivePlayer().controlPayment(cost)) {
            activeTurn.getActiveAction().performMove(move);
            messageOtherPlayer(NotifyMessageBuilder.buildGrabWeaponMessage(getActivePlayer(), weaponToGrab, weaponToDrop));
        }else{
            ModelObserver observer = getObserver(getActivePlayer());
            if(observer != null)
                observer.notifyMessage(getActivePlayer().getName() + " hai provato a compare un'arma ma non potevi pagarla!");
        }
        updateGameRepContext(false);
    }

    /**
     * Called after the user selects the weapon card to perform a shoot
     * @param weaponCard are the loaded weapons of player.
     */
    public void handleWeaponToShoot(WeaponCard weaponCard) {
        activeTurn.setActiveWeapon(weaponCard);
        //If weapon has a shooter movement and the player can pay it
        ShooterMovement shooterMovement = weaponCard.getShooterMovement();
        if (shooterMovement != null && getActivePlayer().canPay(shooterMovement.getCost())) {
            isPlayerShooting = true;
            observers.forEach(o -> o.notifyShooterMovementRequest(getActivePlayer(), map, shooterMovement));
        } else {
            observers.forEach(o -> o.notifyBasicEffectRequest(getActivePlayer(), activeTurn.getActiveWeapon()));
        }
    }

    /**
     * Called after user selects the basic effect. It asks the basic effect use to player.
     * @param effect is the selected basic effect.
     */
    public void handleBasicEffect(ShootEffect effect) {
        activeTurn.setActiveEffect(effect);
        effect.setActive(true);
        observers.forEach(o -> o.notifyBasicEffectUseRequest(getActivePlayer(), map, effect));
    }

    /**
     * Handles basic effect use setting targets and other effect info. If effect has an ultra effect that player can pay,
     * it request the use of the effect to the player, else it performs the shoot.
     * @param targets are the target players
     * @param squares are the target squares
     * @param recoilSquare is the recoil square if effect has a recoil
     * @throws MoveException if move is not valid
     * @throws ShootException if shoot is not valid
     * @throws EffectException if effect is not valid
     */
    public void handleBasicEffectUse(List<Player> targets, List<Square> squares, Square recoilSquare) throws MoveException, ShootException, EffectException {
        //Set effect as active, set shooter, target and squares
        ShootEffect basic = activeTurn.getActiveEffect();
        basic.setActive(true);
        basic.setShooter(activeTurn.getActivePlayer());
        basic.setTargets(targets);
        basic.setSquares(squares);
        //Control if recoil movement exists
        RecoilMovement recoil = activeTurn.getActiveEffect().getRecoil();
        if (recoil != null) {
            recoil.setPosition(recoilSquare);
            recoil.setPlayer(targets.get(0));
            recoil.setActive(true);
        }
        //Se l'effetto ha un effetto ultra allora chiedo se vuole usarlo, altrimenti sparo
        UltraDamage ultra = basic.getUltraDamage();
        if (ultra != null && activeTurn.getActivePlayer().canPay(ultra.getCost())) {
            isPlayerShooting = true;
            activeTurn.setActiveUltra(ultra);
            ultra.setShooter(activeTurn.getActivePlayer());
            ultra.setTargets(targets);
            ultra.setSquares(squares);
            observers.forEach(o -> o.notifyUltraEffectRequest(getActivePlayer(), ultra));
        } else {
            isPlayerShooting = false;
            //Shoot
            controlAndShoot();
        }
    }

    /**
     * It assigns points accumulated on playerboards at the end
     * of the game to other players.
     */
    public void giveFinalPlayersPoints(){
        for (Player p : players) {
            int max = 0;
            int tmp;
            Player bestPlayer = null;
            if(!isLastRound)
                p.firstDamage().getShooter().addPoints(1);
            for (int i = 0; i < players.size(); i++) {
                for (Player player : players) {
                    tmp = p.getNumDamages(player);
                    if (tmp >= max && tmp != 0) {
                        if (tmp == max)
                            bestPlayer = solveParity(p, tmp);
                        else {
                            max = tmp;
                            bestPlayer = player;
                        }
                    }
                }
                if (max != 0 && bestPlayer != null) {
                    if(!isLastRound)
                        bestPlayer.addPoints(p.removeAvaiblePoints());
                    else
                        bestPlayer.addPoints(p.removeFrenzyPoints());
                    p.removeDamageByPlayer(bestPlayer);
                }
                max = 0;
            }
        }
    }

    /**
     * Handle the shooter movement setting all shooter movement use parameters.
     * Then ask basic effect use.
     * @param shooterMovement is the shooter movement effect.
     * @param wantUse is true if player want to use the effect
     * @param position is the position where player wants to move.
     * @param order is 0 if player want to move before basic effect, else 1.
     * @throws MoveException if the move is not valid.
     * @throws ShootException if the shoot is not valid.
     * @throws EffectException if effect is not valid.
     */
    public void handleShooterMovement(ShooterMovement shooterMovement, boolean wantUse, Square position, int order) throws MoveException, ShootException, EffectException {
        if (wantUse) {
            shooterMovement.setActive(true);
            shooterMovement.setPlayer(getActivePlayer());
            shooterMovement.setPosition(position);
            shooterMovement.setOrder(order);
            if(order == 0)
                map.movePlayer(getActivePlayer(), position);
        }
        //Ask basic effect
        observers.forEach(o -> o.notifyBasicEffectRequest(getActivePlayer(), activeTurn.getActiveWeapon()));
    }

    /**
     * Controls if player wants to use the ultra effect and if he wants to use it, method asks the use to the player.
     * Else it performs the shoot.
     * @param isActive is true if player wants to use the effect.
     * @throws MoveException if move is not valid
     * @throws ShootException is shoot is not valid
     * @throws EffectException is effect is not valid
     */
    public void handleUltraEffect(boolean isActive) throws MoveException, ShootException, EffectException {
        if (isActive) {
            UltraDamage ultra = activeTurn.getActiveUltra();
            ultra.setActive(true);
            observers.forEach(o -> o.notifyUltraEffectUseRequest(getActivePlayer(), map, ultra));
        } else {
            //Shoot
            controlAndShoot();
        }
    }

    /**
     * Sets the ultra effect use parameters. If ultra effect has another ultra effect, methods ask to player if him wants to use the effect.
     * Else it performs the shoot.
     * @param targets are the target players.
     * @param square are the target square.
     * @throws MoveException if move is not valid.
     * @throws ShootException is shoot is not valid.
     * @throws EffectException is effect is not valid.
     */
    public void handleUltraEffectUse(List<Player> targets, Square square) throws MoveException, ShootException, EffectException {
        UltraDamage ultra = activeTurn.getActiveUltra();
        ultra.setShooter(activeTurn.getActivePlayer());
        ultra.setNewTargets(targets);
        ultra.setDamageSquare(square);
        //Se l'ultra ha un altro ultra, allora ripetere il procedimento ricordandosi la differenza tra target e new target
        if (ultra.getUltraDamage() != null && activeTurn.getActivePlayer().canPay(ultra.getUltraDamage().getCost())) {
            isPlayerShooting = true;
            ultra = ultra.getUltraDamage();
            activeTurn.setActiveUltra(ultra);
            ultra.setShooter(activeTurn.getActivePlayer());
            ultra.setTargets(targets);
            observers.forEach(o -> o.notifyUltraEffectRequest(getActivePlayer(), activeTurn.getActiveUltra()));
        } else {
            isPlayerShooting = false;
            //Shoot
            controlAndShoot();
        }
    }

    /**
     * Sets the reload move parameters.
     * @param weaponsToReload are the weapons that player want to reload.
     * @throws MoveException if move is not valid
     * @throws ShootException if shoot is not valid (not here)
     * @throws EffectException if effect is not valid (not here)
     */
    public void handleReload(List<WeaponCard> weaponsToReload) throws MoveException, ShootException, EffectException {
        Move move = new Move(activeTurn.getActivePlayer(), map, RELOAD);
        AmmoCubes cost = new AmmoCubes();
        for (int i = 0; i < weaponsToReload.size(); i++) {
            move.setReloadWeapon(i, weaponsToReload.get(i));
            cost.addAmmoCubes(weaponsToReload.get(i).getReloadCost());
        }
        //Control payment
        if (getActivePlayer().controlPayment(cost)){
            activeTurn.getActiveAction().performMove(move);
            messageOtherPlayer(NotifyMessageBuilder.buildReloadMessage(getActivePlayer(), weaponsToReload));
        }else{
            ModelObserver observer = getObserver(getActivePlayer());
            if(observer != null)
                observer.notifyMessage(getActivePlayer().getName() + NOT_ENOUGH_RESOURCES_TO_RECHARGE_MESSAGE);
        }
        updateGameRepContext(false);
    }

    /**
     * Set the move in case active player wants to shoot.
     * @return the move setted.
     */
    private Move setShootMove() {
        Move move = new Move(activeTurn.getActivePlayer(), map, SHOOT);
        move.setShootWeapon(activeTurn.getActiveWeapon());
        return move;
    }

    /**
     * @param player is player that has been killed and has
     *               a parity instance in his playerboard
     * @param num   is the number of players' damages  that
     *              caused the parity instance
     * @return player who has to get the points after solving the parity
     */
    private Player solveParity(Player player, int num) {
        int min = MAX_NUM_PLAYER;
        int tmp;
        List<Player> parityPlayers = new ArrayList<>();
        Player bestPlayer = null;
        for (Damage damage : player.getDamages()) {
            if (player.getNumDamages(damage.getShooter()) == num)
                parityPlayers.add(damage.getShooter());
        }
        for (Damage damage : player.getDamages()) {
            if (parityPlayers.contains(damage.getShooter())) {
                tmp = player.getDamages().indexOf(damage);
                if (tmp < min) {
                    min = tmp;
                    bestPlayer = damage.getShooter();
                }
            }
        }
        return bestPlayer;
    }

    /**
     * Refill the map with ammunition cards and weapons cards at the end of the turn.
     */
    public void refillMap() {
        this.map.refill(ammoStack, weaponStack);
    }

    /**
     * Updates the game rep context.
     * @param gameStarting is true if game is starting.
     */
    public void updateGameRepContext(boolean gameStarting) {
        observers.forEach(o -> o.notifyGameRepContext(o.getPlayer(), this, gameStarting));
    }

    /**
     * Signals time exceeded to players.
     * @param players are the player to notify.
     */
    public void timeExceeded(List<Player> players) {
        for(Player p: players) {
            ModelObserver o = getObserver(p);
            if(o != null)
                o.notifyTimeExceeded("Il tempo è scaduto!");
        }
    }

    /**
     * Respawns the disconnected player picking a causal empower card from his deck.
     * @param disconnected is the disconnected player
     */
    public void respawnDisconnectedPlayer(Player disconnected) {
        EmpowerCard emp = disconnected.getEmpowers().get(new Random().nextInt(disconnected.getEmpowers().size()));
        this.performRespawn(disconnected, emp);
    }

    /**
     * Starts the final frenzy.
     */
    public void startFinalFrenzy() {
        //Set before or after fist player attribute in Player class
        int actualIndex = players.indexOf(activeTurn.getActivePlayer());
        for (int index = 0; index < players.size(); index++) {
            boolean beforeFirstPlayer = (actualIndex > index);
            players.get(index).setBeforeFirstPlayer(beforeFirstPlayer);
        }
        //Set first player final frenzy
        getActivePlayer().setFirstFinalFrenzy(true);
        //Remove all deaths from player board
        players.forEach(p -> p.setDeaths(0));
        //Notify final frenzy
        messageOtherPlayer(FINAL_FRENZY_MESSAGE1);
        WriterHelper.printlnColored(ANSIColors.YELLOW_BOLD, FINAL_FRENZY_MESSAGE2);
    }

    /**
     * Reset shooter player attrivute in players at the end of the action.
     */
    public void resetShootedPlayers() {
        players.forEach(p -> p.setJustShot(false));
    }

    /**
     * Ends the game when the last round is ended.
     * Notifies the raking to the clients.
     */
    public void endGame() {
        //Set game as over
        this.isOver = true;
        //Add final killshot track points
        killshotTrack.addFinalPoints();
        //Add final game boards points
        this.giveFinalPlayersPoints();
        //Check if a player win or is tie
        List<Player> winners = getWinner();
        Player winner = null;
        if(winners.size() == 1) {
            winner = winners.get(0);
            winners.clear();
        }
        //Observers
        for(ModelObserver observer: observers)
            observer.notifyEndGame(observer.getPlayer(), winner, winners, getRanking(this.players));
    }

    /**
     * Calculate the winner player.
     * @return the winner player
     */
    private List<Player> getWinner() {
        List<Player> winnerPlayers = new ArrayList<>();
        if (isOver) {
            Player winner = players.get(0);
            for (Player player : players)
                if (player.getPoints() > winner.getPoints() || (player.getPoints() == winner.getPoints() && killshotTrack.getKillShotTrackPlayerPoints(player) > killshotTrack.getKillShotTrackPlayerPoints(winner))) {
                    winner = player;
                    winnerPlayers.clear();
                    winnerPlayers.add(player);
                }else if(player.getPoints() == winner.getPoints() && killshotTrack.getKillShotTrackPlayerPoints(player) == killshotTrack.getKillShotTrackPlayerPoints(winner)){
                    winnerPlayers.add(player);
                }
        }
        return winnerPlayers;
    }

    /**
     * Build the ranking.
     * @param players are the game players.
     * @return a Map that is the final scoreboard
     */
    public java.util.Map<String, Integer> getRanking(List<Player> players) {
        java.util.Map<String, Integer> ranking = new LinkedHashMap<>();
        List<Player> playersCopy = new ArrayList<>(players);
        while(!playersCopy.isEmpty()) {
            Player maxPlayer = playersCopy.get(0);
            for (Player p : playersCopy) {
                if(p.getPoints() > maxPlayer.getPoints())
                    maxPlayer = p;
            }
            ranking.put(maxPlayer.getName(), maxPlayer.getPoints());
            playersCopy.remove(maxPlayer);
        }
        return ranking;
    }

    /**
     * Control if the shoot is valid and performs it.
     * @throws MoveException if move is not valid
     * @throws ShootException if the shoot action is not valid
     * @throws EffectException if the weapon effect is not valid
     */
    public void controlAndShoot() throws MoveException, ShootException, EffectException {
        if (getActivePlayer().isValidShoot(map, getActiveTurn().getActiveWeapon())) {
            activeTurn.getActiveAction().performMove(setShootMove());
            //Notify to client
            messageOtherPlayer(NotifyMessageBuilder.buildShootMessage(getActivePlayer(), players, activeTurn.getActiveWeapon()));
        } else {
            WriterHelper.printErrorMessage(getActivePlayer().getName() + SHOT_NOT_VALID_MESSAGE1);
            ModelObserver observer = getObserver(getActivePlayer());
            if(observer != null)
                observer.notifyMessage(getActivePlayer().getName() + SHOT_NOT_VALID_MESSAGE2);
            getActiveTurn().getActiveWeapon().reset();
        }
        updateGameRepContext(false);
    }

    /**
     * Send a message to all player excluded the active one.
     * @param message is the messsage to send.
     */
    private void messageOtherPlayer(String message) {
        observers.stream().filter(o -> o.getPlayer() != getActivePlayer()).forEach(o -> o.notifyMessage(message));
    }

    /**
     * Get the corresponding observer of a player.
     * @param player is the player to map with his observer
     * @return the corresponding observer
     */
    private ModelObserver getObserver(Player player) {
        for(ModelObserver observer: observers)
            if(observer.getPlayer() == player)
                return observer;
        return null;
    }

    /**
     * Register a new model observer (view)
     * @param observer is the view to registry
     */
    public void registerObserver(ModelObserver observer) {
        this.observers.add(observer);
    }

    public void setKillshotTrack(KillShotTrack killshotTrack){ this.killshotTrack=killshotTrack;}


    //Getter methods
    public List<Player> getPlayers() {
        return players;
    }

    public List<Player> getDeadPlayers() {
        return deadPlayers;
    }

    public Player getActivePlayer() {
        return activeTurn.getActivePlayer();
    }

    public Map getMap() {
        return map;
    }

    public KillShotTrack getKillshotTrack() {
        return killshotTrack;
    }

    public Stack<AmmunitionCard> getAmmoStack() {
        return ammoStack;
    }

    public Stack<EmpowerCard> getEmpowerStack() {
        return empowerStack;
    }

    public Stack<WeaponCard> getWeaponStack() {
        return weaponStack;
    }

    public boolean isPlayerShooting() {
        return isPlayerShooting;
    }

    public boolean isFinalFrenzy() {
        return killshotTrack.isFinalFrenzy();
    }

    public boolean isFirstRound() {
        return isFirstRound;
    }

    public boolean isLastRound() {
        return isLastRound;
    }

    public void setLastRound(boolean lastRound) {
        isLastRound = lastRound;
    }

    public Turn getActiveTurn() {
        return activeTurn;
    }

    //THESE TWO METHODS ARE USED ONLY IN TEST CLASSES TO ASSIGN A WEAPON OR A EMPOWER TO A PLAYER

    /**
     * This method is used only in test package. It's used only for assign a weapon to a player in the project tests.
     * @param player is the player to assign weapon
     * @param name is the weapon name
     */
    public void assignWeapon(Player player, String name) {
        WeaponCard weapon = null;
        for (WeaponCard w : this.weaponStack.getCards()) {
            if (w.getName().equals(name))
                weapon = w;
        }
        if (weapon != null) {
            weaponStack.removeCard(weapon);
            player.addWeapon(weapon);
        }
    }

    /**
     * This method is used only in test package. It's used only for assign a weapon to a player in the project tests.
     * @param player is the player to assign empower
     * @param empColor is the color of empower to assign
     * @param empType is the type of the empower
     */
    public void assignEmpower(Player player, Color empColor, EmpowerType empType) {
        EmpowerCard emp = null;
        for (EmpowerCard e : this.empowerStack.getCards()) {
            if (e.getColor() == empColor && e.getType() == empType) {
                emp = e;
                break;
            }
        }
        if (emp != null) {
            empowerStack.removeCard(emp);
            player.getEmpowers().add(emp);
        }
    }
}