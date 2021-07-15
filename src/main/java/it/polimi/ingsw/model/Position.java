package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * Position Class is useful.
 * Is used to describe a position, like player, ammo-card, or square Position in Map.
 * Implements serializable because is sent through sockets.
 */
public class Position implements Serializable  {

    private int row;
    private int column;

    /**
     * Default constructor.
     */
    public Position() {
    }

    /**
     *
     * @param row is the row in Map
     * @param column is the column in Map
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Row getter
     * @return position's row
     */
    public int getRow() {
        return row;
    }

    /**
     * Column getter
     * @return position's column
     */
    public int getColumn() {
        return column;
    }


    /**
     *  equals Override
     * @param position is the position that has to be compared
     * @return true if position is the same.
     */

    public boolean equals(Position position) {
        return position.getRow() == row && position.getColumn() == column;
    }
}
