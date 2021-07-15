package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static it.polimi.ingsw.model.Color.*;

public class KillShootTrackTest {

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
    public void creatingKillShotTest(){
        //with no deaths and kills and others
        Assert.assertEquals(8,game.getKillshotTrack().getRemainingKills());

        //adding kills in killShot

        game.getKillshotTrack().addKill(players.get(0),true);
        game.getKillshotTrack().addKill(players.get(0),true);
        game.getKillshotTrack().addKill(players.get(0),true);
        game.getKillshotTrack().addKill(players.get(0),true);
        game.getKillshotTrack().addKill(players.get(0),true);
        game.getKillshotTrack().addKill(players.get(0),true);
        List<Color> killShotRep=game.getKillshotTrack().getKillShotRep();
        Assert.assertEquals(killShotRep.size(),8-game.getKillshotTrack().getRemainingKills());

        Assert.assertEquals(killShotRep.get(0).getDescription(),players.get(0).getColor().getDescription());

    }

    /**
     * See if num kills is 0 after 8 deaths.
     */
    @Test
    public void killShotTest2(){
        game.getKillshotTrack().addKill(players.get(0),true);
        game.getKillshotTrack().addKill(players.get(0),true);
        game.getKillshotTrack().addKill(players.get(0),true);
        game.getKillshotTrack().addKill(players.get(0),true);
        game.getKillshotTrack().addKill(players.get(0),true);
        game.getKillshotTrack().addKill(players.get(0),true);
        game.getKillshotTrack().addKill(players.get(0),true);
        game.getKillshotTrack().addKill(players.get(0),true);
        Assert.assertEquals(0,game.getKillshotTrack().getRemainingKills());
    }

    @Test
    public void killShotTest3(){
        game.getKillshotTrack().addKill(players.get(0),true);
        game.getKillshotTrack().addKill(players.get(0),true);
        game.getKillshotTrack().addKill(players.get(0),true);
        game.getKillshotTrack().addKill(players.get(0),true);
        game.getKillshotTrack().addKill(players.get(1),true);
        game.getKillshotTrack().addKill(players.get(1),true);
        game.getKillshotTrack().addKill(players.get(1),true);
        game.getKillshotTrack().addKill(players.get(1),true);

    }
    @Test
    public void addFinalPointsTest(){

        List<Player>players=new ArrayList<>();
        Player anto = new Player("Antonio", Color.YELLOW, false);
        Player maldini = new Player("Maldini", Color.PURPLE, false);
        Player baresi = new Player("Baresi", Color.WHITE, false);
        Player marquez = new Player("Marquez", Color.RED, false);

        players.add(anto);
        players.add(maldini);
        players.add(baresi);
        players.add(marquez);

        Game game=new Game(players,8,2);


        game.getKillshotTrack().addKill(maldini,false);
        game.getKillshotTrack().addKill(maldini,false);
        game.getKillshotTrack().addKill(maldini,false);

        game.getKillshotTrack().addKill(anto,false);
        game.getKillshotTrack().addKill(anto,false);

        game.getKillshotTrack().addKill(marquez,false);
        game.getKillshotTrack().addKill(marquez,false);
        game.getKillshotTrack().addKill(marquez,false);

        game.getKillshotTrack().addFinalPoints();

        Assert.assertEquals(4,anto.getPoints());
        Assert.assertEquals(8,maldini.getPoints());
        Assert.assertEquals(6,marquez.getPoints());
    }

    @Test
    public void addFinalPointsTest2(){

        List<Player>players=new ArrayList<>();
        Player anto = new Player("Antonio", Color.YELLOW, false);
        Player maldini = new Player("Maldini", Color.PURPLE, false);
        Player baresi = new Player("Baresi", Color.WHITE, false);
        Player marquez = new Player("Marquez", Color.RED, false);

        players.add(anto);
        players.add(maldini);
        players.add(baresi);
        players.add(marquez);

        Game game=new Game(players,8,2);


        game.getKillshotTrack().addKill(marquez,false);
        game.getKillshotTrack().addKill(marquez,false);
        game.getKillshotTrack().addKill(marquez,false);

        game.getKillshotTrack().addKill(anto,false);
        game.getKillshotTrack().addKill(anto,false);

        game.getKillshotTrack().addKill(maldini,false);
        game.getKillshotTrack().addKill(maldini,false);
        game.getKillshotTrack().addKill(maldini,false);

        game.getKillshotTrack().addFinalPoints();

        Assert.assertEquals(4,anto.getPoints());
        Assert.assertEquals(6,maldini.getPoints());
        Assert.assertEquals(8,marquez.getPoints());
    }
}