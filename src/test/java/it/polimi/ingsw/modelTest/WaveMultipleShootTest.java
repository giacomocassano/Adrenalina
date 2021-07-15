package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Map;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.WeaponCard;
import it.polimi.ingsw.model.cards.effects.WaveMultipleShoot;
import it.polimi.ingsw.utils.XMLHelper;
import org.junit.Before;

import java.util.List;

public class WaveMultipleShootTest {

    Map map1, map2, map3, map4, readyMap;
    Player jack, silva, anto, maldini, baresi, marquez, hamilton;
    List<WeaponCard> deck;
    it.polimi.ingsw.utils.XMLHelper XMLHelper=new XMLHelper();

    @Before
    public void beforeEachTest() {
        //Set up maps
        map1 = new Map(1);
        map2 = new Map(2);
        map3 = new Map(3);
        map4 = new Map(4);
        readyMap = new Map(1);

        //Set up players
        jack = new Player("Giacomo", Color.BLUE, false);
        silva = new Player("Silvano", Color.RED, false);
        anto = new Player("Antonio", Color.YELLOW, false);
        maldini = new Player("Maldini", Color.PURPLE, false);
        baresi = new Player("Baresi", Color.WHITE, false);
        marquez = new Player("Marquez", Color.PURPLE, false);
        hamilton = new Player("Hamilton", Color.WHITE, false);

        deck = XMLHelper.forgeWeaponDeck();

        readyMap.getSquare(0,0).addPlayer(jack);
        readyMap.getSquare(1,2).addPlayer(silva);
        readyMap.getSquare(0,2).addPlayer(anto);
        readyMap.getSquare(2,3).addPlayer(maldini);
        readyMap.getSquare(2,2).addPlayer(baresi);
    }



}
