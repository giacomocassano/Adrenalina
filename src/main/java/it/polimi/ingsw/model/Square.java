package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.AmmunitionCard;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Square in the map.
 * Map is composed by some squares, a square can be a respawn point or
 * a normal square. If square is a respawn point is possible to find some weapons
 * in the other case, you can find an ammo card.
 */

public class Square {

    private List<Player> players;
    private Color color;
    private boolean[] bounds; //[T, R, L, B]
    private final Position position;
    private boolean isRespawnPoint;
    private AmmunitionCard ammos;
    private List<WeaponCard> weapons;

    private static final int MAX_WEAPONS = 3;
    private static final int TOP = 0;
    private static final int RIGHT = 1;
    private static final int LEFT = 2;
    private static final int BOTTOM = 3;

    /**
     *
     * @param color is square's colour
     * @param isRespawnPoint if is true, a player can respawn in it
     * @param row represents Map's row where square is located
     * @param column represents Map's column where square is located
     * @param top if true square has another square on her top
     * @param right if true square has another square on her right
     * @param left if true square has another square on her left
     * @param bottom if true square has another square on her bottom
     */
    public Square(Color color, boolean isRespawnPoint, int row, int column, boolean top, boolean right, boolean left, boolean bottom) {
        this.color = color;
        this.position = new Position(row, column);
        players = new ArrayList<>();
        weapons = new ArrayList<>();
        ammos = null;
        bounds = new boolean[4];
        bounds[TOP] = top;
        bounds[RIGHT] = right;
        bounds[LEFT] = left;
        bounds[BOTTOM] = bottom;
        this.isRespawnPoint = isRespawnPoint;
    }

    /**
     * Method to add a Player to a square
     * @param p is player
     * @return true if player is successfully added to square.
     */
    public boolean addPlayer(Player p) {
        if(p == null || players.contains(p))
            return false;
        players.add(p);
        return true;
    }
    /**
     * Method to remove a Player from square
     * @param p is player
     * @return true if player is successfully removed from a  square.
     */
    public boolean removePlayer(Player p) {
        if(p == null || !players.contains(p))
            return false;
        return players.remove(p);
    }

    /**
     * Method to see if a square contains a Player
     * @param player is the player that we want to know position
     * @return true if player is in the square, else, false.
     */
    public boolean containsPlayer(Player player) {
        return players.contains(player);
    }

    /**
     * Color setter
     * @param color is square's color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Players getter
     * @return a list of players in the square.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Color getter
     * @return square's color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Respawn Point getter
     * @return true if square is a spawnpoint
     */
    public boolean isRespawnPoint() {
        return isRespawnPoint;
    }

    /**
     * Top Square getter.
     * @return true if square has a top square with no walls
     */
    public boolean hasTop() {
        return bounds[TOP];
    }

    /**
     * Right Square getter.
     * @return true if square has a right square with no walls
     */
    public boolean hasRight() {
        return bounds[RIGHT];
    }

    /**
     * Left Square getter.
     * @return true if square has a left square with no walls
     */
    public boolean hasLeft() {
        return bounds[LEFT];
    }

    /**
     * Bottom Square getter.
     * @return true if square has a bottom square with no walls
     */
    public boolean hasBottom() {
        return bounds[BOTTOM];
    }

    /**
     * Row getter
     * @return square's row in Map
     */
    public int getRow() {
        return position.getRow();
    }

    /**
     * Column getter
     * @return square's column in Map
     */
    public int getColumn() {
        return position.getColumn();
    }

    /**
     * Position getter
     * @return square's position in Map
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @param weapon is weapon that we want to see if is in the square
     * @return true if weapon is in the square.
     */

    public boolean containsWeapon(WeaponCard weapon) {
        return weapons.contains(weapon);
    }

    /**
     * @param weapon is the weapon card that has to be added
     * @return true if is successfully addedd, else false
     */
    public boolean addWeapon(WeaponCard weapon) {
        if(weapons.size() >= MAX_WEAPONS || !isRespawnPoint)
            return false;
        weapons.add(weapon);
        return true;
    }

    /**
     * @param weapon is the weapon card that has to be removed
     * @return true if is successfully removed, else false
     */
    public boolean removeWeapon(WeaponCard weapon) {
        if(!weapons.contains(weapon))
            return false;
        weapons.remove(weapon);
        return true;
    }

    /**
     * @return num of weapons in the square.
     */
    public int getWeaponsSize() {
        return this.weapons.size();
    }

    /**
     * Ammo-Card Setter
     * @param ammos is an Ammo Card
     * @return true if ammo is successfully added to the square, else false.
     */
    public boolean setAmmos(AmmunitionCard ammos) {
        if(this.ammos != null || isRespawnPoint)
            return false;
        this.ammos = ammos;
        return true;
    }

    /**
     * Weapon card Setter
     * @param weapons is a List of weapon card
     * @return true if weapons are successfully added to the square, else false.
     */
    public boolean setWeapons(List<WeaponCard> weapons) {
        if(this.isRespawnPoint) {
            this.weapons = weapons;
            return true;
        }
        else return false;
    }
    /**
     * Takes Ammo Card from the square. After there is no more an ammo card
     * @return AmmoCard in the square.
     */
    public AmmunitionCard pickAmmos() {
        AmmunitionCard ammoCard = ammos;
        ammos = null;
        return ammoCard;
    }

    /**
     * Weapons getter
     * @return weapons in the square
     */
    public List<WeaponCard> getWeapons() {
        return weapons;
    }

    /**
     * Ammos getter
     * @return ammos in the square
     */
    public AmmunitionCard getAmmos() {
        return ammos;
    }

}