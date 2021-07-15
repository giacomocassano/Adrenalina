package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EffectException;
import it.polimi.ingsw.exceptions.MoveException;
import it.polimi.ingsw.exceptions.ShootException;
import it.polimi.ingsw.model.cards.AmmunitionCard;
import it.polimi.ingsw.model.cards.EmpowerCard;
import it.polimi.ingsw.model.cards.Stack;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a move. a move is an atomic "action" that player can do.
 * An example of a move is: move 1 square, reload, shoot.
 */
public class Move {
    private MoveType type;
    private Square square;
    private EmpowerCard empower;
    private WeaponCard[] weapons;
    private Player player;
    private Map map;
    private static final int NUM_MIN_WEAPONS=0;
    private static final int NUM_MAX_WEAPONS=3;

    /**
     * Creates a Move object
     * @param player is player that is doing his move
     * @param map is the playing map
     * @param type is the move type.
     */
    public Move(Player player, Map map, MoveType type) {
        this.type = type;
        this.player = player;
        this.map = map;
        weapons = new WeaponCard[NUM_MAX_WEAPONS];
    }


    /**
     * type getter
     * @param type is the type of move
     * @return true if move.type=type
     */
    public boolean hasType(MoveType type) {
        return (this.type == type);
    }

    /**
     * move type setter
     * @param type is move's type
     */
    public void setType(MoveType type) {
        this.type = type;
    }

    /**
     * Square setter
     * @param square is the square if player has to move.
     */
    public void setSquare(Square square) {
        this.square = square;
    }

    /**
     * empower's setter
     * @param empower is empower that player wants to use
     */
    public void setEmpower(EmpowerCard empower) {
        this.empower = empower;
    }

    /**
     * sets gained weapon
     * @param weapon is gained weapon
     */
    public void setGainedWeapon(WeaponCard weapon) {
        weapons[0] = weapon;
    }

    /**
     *
     * @return move type
     */
    public MoveType getType() {
        return type;
    }

    /**
     *
     * @return square if player has to move.
     */
    public Square getSquare() {
        return square;
    }

    /**
     *
     * @return empower if player has to use one.
     */
    public EmpowerCard getEmpower() {
        return empower;
    }

    /**
     *
     * gets gained weapon.
     * @return the gained weapon
     */
    public WeaponCard getGainedWeapon() {
        return weapons[0];
    }

    /**
     * dropped weapon getter
     * @return the weapon that player decide to drop.
     */
    public WeaponCard getDroppedWeapon() {
        return weapons[1];
    }

    /**
     * dropped weapon setter
     * @param droppedWeapon is the weapon that player has dropped.
     */
    public void setDroppedWeapon(WeaponCard droppedWeapon) {
        this.weapons[1] = droppedWeapon;
    }

    /**
     * Shooting weapon getter
     * @return shooting weapon
     */
    public WeaponCard getShootWeapon() {
        return this.weapons[0];
    }

    /**
     * set weapon to shoot
     * @param weapon is the shooting weapon
     */
    public void setShootWeapon(WeaponCard weapon) {
        this.weapons[0] = weapon;
    }

    /**
     * @param index is index of the weapon
     * @param weaponToReload is the weapon to reload.
     */
    public void setReloadWeapon(int index, WeaponCard weaponToReload) {
        this.weapons[index] = weaponToReload;
    }

    /**
     *
     * @param index is the index of the needed weapon
     * @return null if index is not valid or the weapon in that index.
     */
    public WeaponCard getWeapon(int index) {
        if(index < NUM_MIN_WEAPONS || index >= NUM_MAX_WEAPONS)
            return null;
        return weapons[index];
    }

    /**
     * sets every weapon to null
     */
    public void resetWeapons() {
        weapons[0] = null;
        weapons[1] = null;
        weapons[2] = null;
    }

    /**
     * Checks if move is valid.
     * @return true if move is valid.
     */
    public boolean isValidMove() {
        if(map == null || player == null) return false;
        switch (getType()){
            case END_ACTION:
                return true;
            case RUN:
                Square oldPosition = map.getPlayerPosition(player);
                Square newPosition = getSquare();
                if(map.getSquareDistance(oldPosition, newPosition) > 1)
                    return false;
                break;
            case GRAB:
                Square position = map.getPlayerPosition(player);
                if((!position.isRespawnPoint() && position.getAmmos() == null) || (position.isRespawnPoint() && !player.canGrabWeapon(position, getGainedWeapon(), getDroppedWeapon())))
                    return false;
                break;
            case SHOOT:
                WeaponCard weapon = getShootWeapon();
                Player shooter = player;
                AmmoCubes cost = weapon.getActiveEffectsCost();
                if(!shooter.canPay(cost)) return false;
                break;
            case RELOAD:
                List<WeaponCard> weaponsToReload = new ArrayList<>();
                AmmoCubes totalCost = new AmmoCubes(0,0,0);
                for(int i=0; i<3; i++){
                    WeaponCard w = getWeapon(i);
                    if(w != null) {
                        weaponsToReload.add(w);
                        totalCost.addAmmoCubes(w.getReloadCost());
                    }
                }
                if(!player.canPay(totalCost)) return false;
                break;
        }
        return true;
    }

    /**
     * Performs the move
     * @param empowersStack is empower's stack
     * @param ammosStack is ammo's stack
     * @throws ShootException if player can't shoot
     * @throws MoveException if player can't move
     * @throws EffectException if player can't use effect
     */
    public void performMove(Stack<EmpowerCard> empowersStack, Stack<AmmunitionCard> ammosStack) throws ShootException, MoveException, EffectException {
        if(!isValidMove()) throw new MoveException("Mossa non valida");
        switch (getType()){
            case RUN:
                Square newPosition = getSquare();
                map.movePlayer(player, newPosition);
                break;
            case GRAB:
                Square position = map.getPlayerPosition(player);
                if(position.isRespawnPoint()) {
                    AmmoCubes weaponCost = getGainedWeapon().getBuyCost();
                    player.grabWeapon(position, getGainedWeapon(), getDroppedWeapon());
                    player.payCost(weaponCost, empowersStack);
                }else{
                    player.grabAmmos(position, empowersStack, ammosStack);
                }
                break;
            case SHOOT:
                WeaponCard weapon = getShootWeapon();
                Player shooter = player;
                AmmoCubes cost = weapon.getActiveEffectsCost();
                shooter.shoot(map, weapon);
                shooter.payCost(cost, empowersStack);
                break;
            case RELOAD:
                List<WeaponCard> weaponsToReload = new ArrayList<>();
                AmmoCubes totalCost = new AmmoCubes(0,0,0);
                for(int i=0; i<3; i++){
                    WeaponCard w = getWeapon(i);
                    if(w != null) {
                        weaponsToReload.add(w);
                        totalCost.addAmmoCubes(w.getReloadCost());
                    }
                }
                player.reloadWeapons(weaponsToReload);
                player.payCost(totalCost, empowersStack);
                break;
        }
    }

    /**
     *
     * @return the  player that is doing the move
     */
    public Player getPlayer() {
        return player;
    }
}