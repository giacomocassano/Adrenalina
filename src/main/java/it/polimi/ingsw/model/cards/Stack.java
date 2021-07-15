package it.polimi.ingsw.model.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Modelize all types of card's stack
 */
public class Stack<T extends Card> {

    private List<T> cards;
    public List<T> getDiscardeds() {
        return discardeds;
    }

    private List<T> discardeds;

    /**
     * Default constructor
     */
    public Stack(){
        this.cards = new ArrayList<>();
        this.discardeds = new ArrayList<>();
    }

    /**
     * Constructor
     * @param cards is a list of cards.
     */
    public Stack(List<T> cards) {
        this.cards=new ArrayList<>();
        this.cards.addAll(cards);
        this.discardeds = new ArrayList<>();
    }

    /**
     * Shuffles the normal deck.
     */
    public void shuffle() {
        Random random=new Random();
        for(int i=0;i<this.getSize();i++) {
            T tempCard=this.cards.get(i);
            int randomIndex=i+random.nextInt((this.getSize()-i));
            this.cards.set(i,this.cards.get(randomIndex));
            this.cards.set(randomIndex,tempCard);
        }
    }

    /**
     * @return normal deck's size
     */
    public int getSize() {
        return this.cards.size();
    }

    /**
     * adds a card to the deck
     * @param card is the card that has to be added.
     */
    public void addCard(T card) {
        this.cards.add(card);
    }


    public void addAllCards(List<T> newCards) {
        cards.addAll(newCards);
    }

    /**
     * @return first card of the stack and updates the stack
     */
    public Card pickCard() {
        if(cards.isEmpty())
            refill();
        if(!cards.isEmpty())
            return cards.remove(0);
        else
            return null;
    }

    /**
     * Used when there is a empty stack and a full discarded-stack
     * It substitutes the first with the second and shuffles the product
     * @param stack is the stack to fill
     */
    public void fillStack(Stack<T> stack) {
        this.cards.addAll(stack.getCards());
        stack.getCards().removeAll(stack.getCards());
    }

    /**
     * @return the normal deck
     */
    public List<T> getCards() {
        return this.cards;
    }

    /**
     * discards a card
     * @param discarded is a card that has to be discarded.
     */
    public void discard(T discarded) {
        this.discardeds.add(discarded);
    }

    /**
     * Discard a list of cards
     * @param discardeds are all the discarded card.
     */
    public void discardAll(List<T> discardeds) {
        this.discardeds.addAll(discardeds);
    }

    /**
     * refill the deck with the discarded one
     */
    private void refill() {
        cards.addAll(discardeds);
        discardeds.clear();
        shuffle();
    }

    /**
     * Removes a card from the normal deck
     * @param c is the card that has to be removed
     */
    public void removeCard(T c) {
        this.cards.remove(c);
    }

    /**
     * Returns a card from an id
     * @param id is the searched id
     * @return a card with that id
     */
    public T getCardById(int id) {
        for(T card: cards)
            if(card.getId() == id)
                return card;
        return null;
    }

    /**
     * Takes a card from the normal deck and puts it into discarded one
     * @param discarded is the card
     */
    public void moveInDiscardedCard(T discarded) {
        if(cards.contains(discarded))
            cards.remove(discarded);
        discardeds.add(discarded);

    }

}