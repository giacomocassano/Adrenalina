package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.*;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapTest {


    private static final int MAX_NUMBER_OF_PLAYERS=5;

    @Test
    public void testVisibilityFunctions(){
        Map map = new Map(1);
        Player jack = new Player("Giacomo", Color.BLUE, false);
        Player anto = new Player("Antonio", Color.GREEN, false);
        Player silva = new Player("Silvano", Color.RED, false);
        Player pierino = new Player("Pierino", Color.GREY, false);

        map.getSquare(0,0).addPlayer(jack);
        map.getSquare(1,2).addPlayer(silva);
        map.getSquare(0,2).addPlayer(anto);
        map.getSquare(2,3).addPlayer(pierino);

        List<Player> visibleFromJack = map.getOtherPlayers(jack, true);
        Assert.assertTrue(visibleFromJack.contains(anto) && visibleFromJack.contains(silva) && visibleFromJack.size() == 2);
        List<Player> visibleFromAnto = map.getOtherPlayers(anto, true);
        Assert.assertTrue(visibleFromAnto.contains(jack) && visibleFromAnto.contains(silva) && visibleFromAnto.size() == 2);
        List<Player> visibleFromSilva = map.getOtherPlayers(silva, true);
        Assert.assertTrue(visibleFromSilva.contains(jack) && visibleFromSilva.contains(anto) && visibleFromSilva.contains(pierino) && visibleFromSilva.size() == 3);
        List<Player> visibleFromPierino = map.getOtherPlayers(pierino, true);
        Assert.assertTrue(visibleFromPierino.size() == 0);

        List<Player> notVisibleFromJack = map.getOtherPlayers(jack, false);
        Assert.assertTrue(notVisibleFromJack.contains(pierino) && notVisibleFromJack.size() == 1);
        List<Player> notVisibleFromAnto = map.getOtherPlayers(anto, false);
        Assert.assertTrue(notVisibleFromAnto.contains(pierino) && notVisibleFromAnto.size() == 1);
        List<Player> notVisibleFromSilva = map.getOtherPlayers(silva, false);
        Assert.assertTrue(notVisibleFromSilva.size() == 0);
        List<Player> notVisibleFromPierino = map.getOtherPlayers(pierino, false);
        Assert.assertTrue(notVisibleFromPierino.contains(jack) && notVisibleFromPierino.contains(silva) && notVisibleFromPierino.contains(anto) && notVisibleFromPierino.size() == 3);
    }
    @Test
    public void testVisibilityFunctions4() {
        Map map = new Map(4);
        Player jack = new Player("Giacomo", Color.BLUE, false);
        Player anto = new Player("Antonio", Color.GREEN, false);
        Player silva = new Player("Silvano", Color.RED, false);
        Player pierino = new Player("Pierino", Color.GREY, false);


        map.getSquare(0, 0).addPlayer(jack);
        map.getSquare(0, 3).addPlayer(silva);
        map.getSquare(2, 3).addPlayer(anto);
        map.getSquare(1, 1).addPlayer(pierino);
        map.getSquare(1,2).addPlayer(silva);
        List<Player> visibleFromJack = map.getOtherPlayers(jack, true);
        List<Player> visibleFromPierino= map.getOtherPlayers(pierino,true);
        Assert.assertFalse(visibleFromJack.contains(silva)&&visibleFromJack.contains(anto)&&visibleFromJack.contains(pierino));
        Assert.assertFalse(visibleFromPierino.contains(silva)&&visibleFromPierino.contains(anto));
    }


    @Test
    public void testDistanceFunction() {
        Map map = new Map(1);
        Player[] players = { new Player("Giacomo", Color.BLUE, false),
                new Player("Silvano", Color.RED, false),
                new Player("Antonio", Color.YELLOW, false),
                new Player("Maldini", Color.PURPLE, false),
                new Player("Baresi", Color.WHITE, false)};

        map.getSquare(0,0).addPlayer(players[0]);
        map.getSquare(1,2).addPlayer(players[1]);
        map.getSquare(0,2).addPlayer(players[2]);
        map.getSquare(2,3).addPlayer(players[3]);
        map.getSquare(2,2).addPlayer(players[4]);

        int[] distances = {0, 3, 2, 5, 4, 3, 0, 1, 2, 3, 2, 1, 0, 3, 4, 5, 2, 3, 0, 1, 4, 3, 4, 1, 0};

        for(int i=0; i<MAX_NUMBER_OF_PLAYERS; i++) {
            for (int j = 0; j <MAX_NUMBER_OF_PLAYERS; j++) {
                System.out.println("Distance from " + players[i].getName() + " to " + players[j].getName() + ": " + map.getPlayersDistance(players[i], players[j]));
                Assert.assertTrue(map.getPlayersDistance(players[i], players[j]) == distances[i*5+j]);
            }
            System.out.println();
        }
    }

    //testing the  Method checkTheLine in map.
    @Test
    public void checkthetest(){

        Map map= new Map(4);
        Player jack = new Player("Giacomo", Color.BLUE, false);
        Player anto = new Player("Antonio", Color.GREEN, false);
        Player silva = new Player("Silvano", Color.RED, false);
        Player pierino = new Player("Pierino", Color.GREY, false);
        Player maradona =new Player("Diego Armando", Color.YELLOW,false);

        map.getSquare(0,0).addPlayer(jack);
        map.getSquare(0,1).addPlayer(silva);
        map.getSquare(0,2).addPlayer(anto);
        map.getSquare(0,3).addPlayer(pierino);

        List<Player> targets=new ArrayList<>();
        targets.add(silva);
        targets.add(anto);
        Assert.assertTrue(map.checkTheLine(jack,targets,1,2));
        targets.add(pierino);
        Assert.assertTrue(map.checkTheLine(jack,targets,1,3));

        map.getSquare(0,3).removePlayer(pierino);
        map.getSquare(1,0).addPlayer(pierino);
        Assert.assertFalse(map.checkTheLine(jack,targets,1,3));
        //now i remove every player from the precedent position
        map.getSquare(0,0).removePlayer(jack);
        map.getSquare(0,1).removePlayer(silva);
        map.getSquare(0,2).removePlayer(anto);
        map.getSquare(1,0).removePlayer(pierino);
        //put them all in the same square
        map.getSquare(1,1).addPlayer(jack);
        map.getSquare(1,1).addPlayer(silva);
        map.getSquare(1,1).addPlayer(anto);
        map.getSquare(1,1).addPlayer(pierino);
        //shooter now is Diego Armando
        targets.add(jack);
        //now targets have 4 players
        map.getSquare(1,1).addPlayer(maradona);
        Assert.assertFalse(map.checkTheLine(maradona,targets,1,1));
        //now i change ranges and shooter position.
        map.getSquare(1,1).removePlayer(maradona);
        map.getSquare(2,1).addPlayer(maradona);
        Assert.assertTrue(map.checkTheLine(maradona,targets,1,1));
    }

    /*
    *Testing the method checkWall in map.
    **/
    @Test
    public void testTheWall(){

        Map map= new Map(4);
        Player totti = new Player("Francesco", Color.RED, false);
        Player buffon = new Player("Gigi", Color.YELLOW, false);

        map.getSquare(0,1).addPlayer(totti);
        map.getSquare(1,3).addPlayer(buffon);
        //two players on different row/col no problem
        Assert.assertFalse(map.checkWalls(totti,buffon));
        map.getSquare(0,1).removePlayer(totti);
        map.getSquare(1,3).removePlayer(buffon);
        //no problem
        map.getSquare(0,1).addPlayer(totti);
        map.getSquare(2,1).addPlayer(buffon);
        Assert.assertTrue(map.checkWalls(totti,buffon));
        //no problem expected wall.
        map.getSquare(1,0).addPlayer(totti);
        map.getSquare(1,3).addPlayer(buffon);
        Assert.assertFalse(map.checkWalls(totti,buffon));

        map=new Map(2);
        map.getSquare(1,0).addPlayer(totti);
        map.getSquare(1,3).addPlayer(buffon);
        Assert.assertFalse(map.checkWalls(totti,buffon));

        map.getSquare(1,0).removePlayer(totti);
        map.getSquare(1,3).removePlayer(buffon);
        map.getSquare(0,2).addPlayer(totti);
        map.getSquare(2,2).addPlayer(buffon);
        Assert.assertFalse(map.checkWalls(totti,buffon));

        //Now I change the map.
        map=new Map(1);
        //testing the first wall. passed.
        map.getSquare(0,1).addPlayer(totti);
        map.getSquare(2,1).addPlayer(buffon);
        Assert.assertFalse(map.checkWalls(totti,buffon));

        //testing the second wall
        map.getSquare(0,1).removePlayer(totti);
        map.getSquare(2,1).removePlayer(buffon);
        map.getSquare(0,2).addPlayer(totti);
        map.getSquare(2,2).addPlayer(buffon);
        Assert.assertFalse(map.checkWalls(totti,buffon));

        //testing with assertTrue
        map.getSquare(0,2).removePlayer(totti);
        map.getSquare(2,2).removePlayer(buffon);
        map.getSquare(1,0).addPlayer(totti);
        map.getSquare(1,3).addPlayer(buffon);
        Assert.assertTrue(map.checkWalls(totti,buffon));

        //map type 2.
        map=new Map(2);
        map.getSquare(1,1).addPlayer(totti);
        map.getSquare(1,2).addPlayer(buffon);
        Assert.assertTrue(map.checkWalls(totti,buffon));
        map.getSquare(1,1).removePlayer(totti);
        map.getSquare(1,2).removePlayer(buffon);
        map.getSquare(1,0).addPlayer(totti);
        map.getSquare(1,1).addPlayer(buffon);
        Assert.assertFalse(map.checkWalls(totti,buffon));

        //map type 3.
    }

    /**
     * Test for getPlayerinRoom and getVisibleRoom and getOtherPlayers.
     */
    @Test
    public void testVisibility() {

        Map map = new Map(4);
        Player Goku = new Player("Son", Color.BLUE, false);
        Player Cell = new Player("Ma", Color.GREEN, false);
        Player Boo = new Player("Majin", Color.PURPLE, false);
        Player Vegeta = new Player("ha un nome vegeta?", Color.WHITE, false);

        //now testing getPlayerinRoom

        List<Player> enemies = new ArrayList<>();
        map.getSquare(2, 2).addPlayer(Goku);
        map.getSquare(1, 2).addPlayer(Vegeta);
        map.getSquare(2, 3).addPlayer(Boo);
        map.getSquare(1, 3).addPlayer(Cell);
        enemies = map.getPlayerInRoom(Color.YELLOW);
        Assert.assertTrue(enemies.contains(Goku) && enemies.contains(Vegeta) && enemies.contains(Boo) && enemies.contains(Cell));

        map.getSquare(2, 2).removePlayer(Goku);
        map.getSquare(1, 2).removePlayer(Vegeta);
        map.getSquare(2, 3).removePlayer(Boo);
        map.getSquare(1, 3).removePlayer(Cell);

        map.getSquare(1, 1).addPlayer(Goku);
        map.getSquare(0, 0).addPlayer(Vegeta);
        map.getSquare(2, 0).addPlayer(Boo);
        map.getSquare(0, 3).addPlayer(Cell);
        enemies=map.getPlayerInRoom(Color.PURPLE);
        Assert.assertFalse(enemies.contains(Vegeta)||enemies.contains(Boo)||enemies.contains(Cell));
        enemies=map.getPlayerInRoom(Color.RED);
        Assert.assertTrue(enemies.contains(Vegeta));
        Assert.assertFalse(enemies.contains(Cell));

        //changing the map
        map=new Map(3);

        map.getSquare(0, 1).addPlayer(Goku);
        map.getSquare(0, 0).addPlayer(Vegeta);
        map.getSquare(0, 2).addPlayer(Boo);
        map.getSquare(0, 3).addPlayer(Cell);
        enemies=map.getPlayerInRoom(Color.BLUE);
        Assert.assertTrue(enemies.contains(Goku)&&enemies.contains(Vegeta)&&enemies.contains(Boo));
        Assert.assertFalse(enemies.contains(Cell));
        map.getSquare(0, 1).removePlayer(Goku);
        map.getSquare(0, 0).removePlayer(Vegeta);
        map.getSquare(0, 2).removePlayer(Boo);
        map.getSquare(0, 3).removePlayer(Cell);

    }

    @Test
    public void testRecoilsSquares() {
        Map map = new Map(1);
        Player jack = new Player("Giacomo", Color.BLUE, false);
        Player anto = new Player("Antonio", Color.GREEN, false);
        Player silva = new Player("Silvano", Color.RED, false);
        Player pierino = new Player("Pierino", Color.GREY, false);

        map.getSquare(0,0).addPlayer(jack);
        map.getSquare(1,2).addPlayer(silva);
        map.getSquare(0,2).addPlayer(anto);
        map.getSquare(2,3).addPlayer(pierino);
/*
        List<Square> squares = map.getPossibleMovementSquares(silva, 1);
        List<Position> positions = squares.stream().map(Square::getPosition).collect(Collectors.toList());
        Assert.assertEquals(4, positions.size());

        squares = map.getPossibleMovementSquares(silva, 2);
        positions = squares.stream().map(Square::getPosition).collect(Collectors.toList());
        Assert.assertEquals(5, positions.size());

 */
    }
}
