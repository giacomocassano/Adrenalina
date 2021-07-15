package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Map;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.EmpowerCard;
import it.polimi.ingsw.model.cards.EmpowerType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class EmpowerCardTest {

    Game game = null;
    List<Player> players = null;
    Map map = null;

    @Before
    public void b4Test(){
        Player p1 = new Player("AAA", Color.BLUE, false);
        Player p2 = new Player("BBB", Color.GREEN, false);
        Player p3 = new Player("CCC", Color.YELLOW, false);
        Player p4 = new Player("DDD", Color.GREY, false);
        Player p5 = new Player("EEE", Color.PURPLE, false);
        this.players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        players.add(p5);
        this.map = new Map(1);
        this.game = new Game(players, p1, map, true, false);
        game.startDecksMapsFromXML();
    }

    @Test
    public void empowerTest1Teleporter(){
        /*
        testing if a player can teleport
         */
        game.assignEmpower(players.get(0),Color.BLUE, EmpowerType.TELEPORTER);
        map.getSquare(0,0).addPlayer(players.get(0));
        EmpowerCard emp=players.get(0).getEmpowers().get(0);
        emp.setUser(players.get(0));
        emp.setPosition(map.getSquare(2,3));
        Assert.assertTrue(emp.isValidEmpowerUse(map,players.get(0)));
        emp.performEmpower(map,players.get(0),game.getEmpowerStack());
        Assert.assertEquals(map.getSquare(2,3),map.getPlayerPosition(players.get(0)));
        /*
        if i set user to null expected a false in isValidUse
         */
        emp.setUser(null);
        map.getSquare(0,0).addPlayer(players.get(0));
        Assert.assertFalse(emp.isValidEmpowerUse(map,players.get(0)));
        emp.performEmpower(map,players.get(0),game.getEmpowerStack());
        Assert.assertEquals(map.getSquare(0,0),map.getPlayerPosition(players.get(0)));
    }

    @Test
    public void tagBackGranadeTest(){
        Player player1= players.get(0);
        Player player2=players.get(1);
        player1.sufferDamage(map,player2,3,0);
        map.getSquare(0,0).addPlayer(player1);
        map.getSquare(0,0).addPlayer(player2);
        game.assignEmpower(player1,Color.BLUE,EmpowerType.TAGBACK_GRANADE);
        game.getActiveTurn().setActivePlayer(player2);
        EmpowerCard emp=player1.getEmpowers().get(0);
        emp.setUser(player1);
        emp.setTarget(player2);
        Assert.assertTrue(emp.isValidEmpowerUse(map,player2));
        emp.performEmpower(map,player2,game.getEmpowerStack());
        /*
        Same but changing position so p1 can't see p2
         */
        map.getSquare(0,0).removePlayer(player2);
        map.getSquare(0,0).addPlayer(player1);
        map.getSquare(2,3).addPlayer(player2);
        game.assignEmpower(player1,Color.BLUE,EmpowerType.TAGBACK_GRANADE);
        game.getActiveTurn().setActivePlayer(player2);
         emp=player1.getEmpowers().get(0);
        emp.setUser(player1);
        emp.setTarget(player2);
        Assert.assertFalse(emp.isValidEmpowerUse(map,player2));
    }


    @Test
    public void targetingScopeTest(){
        Player player1= players.get(0);
        Player player2=players.get(1);
        player1.sufferDamage(map,player2,3,0);
        game.assignEmpower(player2,Color.BLUE,EmpowerType.TARGETING_SCOPE);
        game.getActiveTurn().setActivePlayer(player2);
        EmpowerCard emp=player2.getEmpowers().get(0);
        emp.setUser(player2);
        /* without target and cube color
        */
        Assert.assertFalse(emp.isValidEmpowerUse(map,player2));
        /*
        with target
         */
        emp.setTarget(player1);
        Assert.assertFalse(emp.isValidEmpowerUse(map,player2));
        /*
        with everything now it must work
         */
        emp.setCubeColor(Color.BLUE);
        Assert.assertTrue(emp.isValidEmpowerUse(map,player2));


        player2.getAmmoCubes().resetToZero();
        player2.getEmpowers().clear();
        Assert.assertFalse(emp.isValidEmpowerUse(map,player2));

        Assert.assertFalse(emp.performEmpower(map,player2,game.getEmpowerStack()));

    }


}
