package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * This class represents a game Ammo-Cube.
 *
 */

public class AmmoCubes implements Serializable {

    private int red;
    private int blue;
    private int yellow;

    private static final int MAX_CUBES = 3;
    private static final int MIN_CUBES=0;


    /**
     *This is the standard constructor that sets every value to 0.
     */
    public AmmoCubes() {
        this.red = 0;
        this.blue = 0;
        this.yellow = 0;
    }


    /**
     *This constructor takes: @red, @blue and @yellow number of cubes.
     * @param red is num of red cubes
     * @param blue is num of blue cubes
     * @param yellow is num of yellow cubes
     */
    public AmmoCubes(int red, int blue, int yellow) {
        this.red = red;
        this.blue = blue;
        this.yellow = yellow;
    }

    /**
     * Red cubes getter.
     * @return number of red cubes.
     */
    public int getRed() {
        return red;
    }

    /**
     * Blue cubes getter.
     * @return number of blue cubes.
     */
    public int getBlue() {
        return blue;
    }

    /**
     * Yellow cubes getter.
     * @return number of yellow cubes.
     */
    public int getYellow() {
        return yellow;
    }

    /**
     *
     * @param ac is another ammoCubes
     * @return true if cubes have been added correctly.
     */
    public boolean addAmmoCubes(AmmoCubes ac) {
        return this.addAmmoCubesRed(ac.getRed()) && this.addAmmoCubesBlue(ac.getBlue()) && this.addAmmoCubesYellow(ac.getYellow());
    }

    /**
     * Method to add ammo cubes red
     * @param red can't be less the MIN_CUBES
     * @return true if there are no errors.
     */
    public boolean addAmmoCubesRed(int red){
        if(red<MIN_CUBES) return false;
        if(this.red+red>MAX_CUBES)
            this.red = MAX_CUBES;
        else
            this.red+=red;
        return true;
    }

    /**
     * Method to add ammo cubes blue
     * @param blue can't be less the MIN_CUBES
     * @return true if there are no errors.
     */

    public boolean addAmmoCubesBlue(int blue){
        if(blue<MIN_CUBES) return false;
        if(this.blue+blue>MAX_CUBES)
            this.blue = MAX_CUBES;
        else
            this.blue+=blue;
        return true;
    }

    /**
     * Method to add ammo cubes yellow
     * @param yellow can't be less the MIN_CUBES
     * @return true if there are no errors.
     */

    public boolean addAmmoCubesYellow(int yellow){
        if(yellow<MIN_CUBES) return false;
        if(this.yellow+yellow>MAX_CUBES)
            this.yellow = MAX_CUBES;
        else
            this.yellow+=yellow;
        return true;
    }
    /**
     * Method to decrease ammo cubes yellow
     * @param red can't be greater then player red cubes.**
     * @return true if this** condition is true.
     */
    public boolean decreaseAmmoCubesRed(int red){
        if(red<MIN_CUBES) return false;
        if(this.red-red<MIN_CUBES) {
            return false;
        }
        this.red-=red;
        return true;
    }

    /**
     * Method to decrease ammo cubes yellow
     * @param blue can't be greater then player blue cubes.**
     * @return true if this** condition is true.
     */
    public boolean decreaseAmmoCubesBlue(int blue){
        if(blue<MIN_CUBES) return false;
        if(this.blue-blue<MIN_CUBES) {
            return false;
        }
        this.blue-=blue;
        return true;
    }

    /**
     * Method to decrease ammo cubes yellow
     * @param yellow can't be greater then player yellow cubes.**
     * @return true if this** condition is true.
     */
    public boolean decreaseAmmoCubesYellow(int yellow){
        if(yellow<MIN_CUBES) return false;
        if(this.yellow-yellow<MIN_CUBES) {
            return false;
        }
        this.yellow-=yellow;
        return true;
    }

    /**
     * This decrease Yellow, red and blue cubes.
     * @param cost is how much it has to be decreased
     * @return true if cubes are successfully decreased
     */
    public boolean decreaseAmmoCubes(AmmoCubes cost) {
        return  this.decreaseAmmoCubesRed(cost.getRed()) &&
                this.decreaseAmmoCubesBlue(cost.getBlue()) &&
                this.decreaseAmmoCubesYellow(cost.getYellow());
    }

    /**
     * sets every value to zero.
     */
    public void resetToZero(){
        this.blue=MIN_CUBES;
        this.red=MIN_CUBES;
        this.yellow=MIN_CUBES;
    }

    /**
     * This method is used to see if yellow cost is greater then yellow, same for red and blue.
     *
     * @param cost is the ammo cube to compare
     * @return true if is greater.
     *
     */
    public boolean isLowerThan(AmmoCubes cost) {
        if(this.red < cost.getRed() || this.blue < cost.getBlue() || this.yellow < cost.getYellow())
            return true;
        return false;
    }

}