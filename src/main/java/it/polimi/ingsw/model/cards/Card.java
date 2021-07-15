package it.polimi.ingsw.model.cards;

import java.io.Serializable;

/**
 * This is an abstract class that represents every card.
 * every card has an ID and a Name
 */
public abstract class Card implements Serializable {

    private String name;
    private int id;

    /**
     * This is the constructor
     * @param name is the name of the card
     * @param id is the card id
     */
    public Card(String name, int id){
        this.name=name;
        this.id=id;
    }

    /**
     *
     * @return the name of the card
     */
    public String getName() { return this.name; }

    /**
     *
     * @return the id of the card.
     */
    public int getId(){return this.id; }

}