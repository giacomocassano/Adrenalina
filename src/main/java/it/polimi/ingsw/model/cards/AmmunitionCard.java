package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.AmmoCubes;

import java.io.Serializable;

/**
 * This class is used to represent a game Ammunition Card.
 * An ammo cards gives player the faculty to grab some cubes and
 * in some cases an empower.
 */
public class AmmunitionCard extends Card implements Serializable {

    private AmmoCubes ammoCubes;
    private static final String CARD_NAME="Carta munizioni";
    private static final String CARD_FORMAT="Cubi rossi: %d\nCubi blu: %d\nCubi gialli: %d\n";
    private static final String EMPOWER_MESSAGE="La carta offre la possibilità di pescare un potenziamento";
    private boolean empower;

    /**
     * Constructor
     * @param ammoCubes are cubes that player can earn from a card
     * @param id is the id of the card
     * @param empower is true if with card you can pick an empower.
     */
    public AmmunitionCard(AmmoCubes ammoCubes, int id, boolean empower) {
        super(CARD_NAME,id);
        this.ammoCubes = ammoCubes;
        this.empower = empower;
    }

    /**
     *
     * @return the description of the card
     */
    public String getDescription() {
        String description = String.format(CARD_FORMAT,
                ammoCubes.getRed(), ammoCubes.getBlue(), ammoCubes.getYellow());
        if(empower)
            description=description.concat(EMPOWER_MESSAGE);
        return description;
    }

    /**
     *
     * @return ammo cubes that you can earn picking the card
     */
    public AmmoCubes getAmmoCubes() {
        return ammoCubes;
    }

    /**
     *
     * @return true if ammo card has an empower
     */
    public boolean hasEmpower() {
        return empower;
    }
}