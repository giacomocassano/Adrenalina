package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.Damage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Map;
import it.polimi.ingsw.model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static it.polimi.ingsw.model.Color.*;

public class DamageTest {

    Game game = null;
    List<Player> players = null;
    Map map = null;

    @Before
    public void beforeEachActionTest() {
        players = new ArrayList<>();
        players.add(new Player("Giacomo", BLUE, false));
        players.add(new Player("Silvano", RED, false));
        players.add(new Player("Antonio", YELLOW, false));
        players.add(new Player("Maldini", PURPLE, false));
        players.add(new Player("Baresi", WHITE, false));

        game = new Game(players, 8, new Random().nextInt(3) + 1);
        map = game.getMap();
        game.startDecksMapsFromXML();
        game.getMap().getSquare(0,0).addPlayer(players.get(0));
        game.getMap().getSquare(1,2).addPlayer(players.get(1));
        game.getMap().getSquare(0,2).addPlayer(players.get(2));
        game.getMap().getSquare(2,3).addPlayer(players.get(3));
        game.getMap().getSquare(2,2).addPlayer(players.get(4));

    }

    @Test
    public void testDamage1(){
    players.get(0).sufferDamage(map,players.get(1),4,0);
    Assert.assertEquals(players.get(1),players.get(0).getDamages().get(0).getShooter());
    players.get(0).getDamages().add(new Damage(players.get(1),true));
    Assert.assertTrue(players.get(0).getDamages().get(4).isInfertDamage());

    }

    @Test
    public void getShooterTest(){
        players.get(0).sufferDamage(map,players.get(3),4,0);
        Assert.assertEquals(players.get(3),players.get(0).getDamages().get(0).getShooter());


    }


}
