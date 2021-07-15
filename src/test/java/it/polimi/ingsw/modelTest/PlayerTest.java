package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.exceptions.MoveException;
import it.polimi.ingsw.model.AmmoCubes;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Stack;
import it.polimi.ingsw.model.cards.WeaponCard;
import it.polimi.ingsw.utils.XMLHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PlayerTest {
    @Test
    public void grabAmmosTest() {
        Player pippo = new Player("pippo", Color.BLUE, false);

        Player jack = new Player("Giacomo", Color.BLUE, false);
        Player anto = new Player("Antonio", Color.GREEN, false);
        Player silva = new Player("Silvano", Color.RED, false);
        Player pierino = new Player("Pierino", Color.GREY, false);

        List<Player> playersList = new ArrayList<>();
        playersList.add(jack);
        playersList.add(anto);
        playersList.add(pierino);
        playersList.add(pippo);
        playersList.add(silva);
        Game game = new Game(playersList, 2, 2);
        game.startDecksMapsFromXML();

        Assert.assertTrue(pippo.grabAmmos(game.getMap().getSquare(0, 0), game.getEmpowerStack(), game.getAmmoStack()));

    }

    @Test
    public void grabAmmosTest2() {
        Player pippo = new Player("pippo", Color.BLUE, false);

        Player jack = new Player("Giacomo", Color.BLUE, false);
        Player anto = new Player("Antonio", Color.GREEN, false);
        Player silva = new Player("Silvano", Color.RED, false);
        Player pierino = new Player("Pierino", Color.GREY, false);

        List<Player> playersList = new ArrayList<>();
        playersList.add(jack);
        playersList.add(anto);
        playersList.add(pierino);
        playersList.add(pippo);
        playersList.add(silva);
        Game game = new Game(playersList, 2, 2);
        game.startDecksMapsFromXML();

        Assert.assertFalse(pippo.grabAmmos(game.getMap().getSquare(1, 0), game.getEmpowerStack(), game.getAmmoStack()));

    }

    @Test
    public void canGrabWeaponTest() {

        Player pippo = new Player("pippo", Color.BLUE, false);
        Player jack = new Player("Giacomo", Color.BLUE, false);
        Player anto = new Player("Antonio", Color.GREEN, false);
        Player silva = new Player("Silvano", Color.RED, false);
        Player pierino = new Player("Pierino", Color.GREY, false);

        List<Player> playersList = new ArrayList<>();
        playersList.add(jack);
        playersList.add(anto);
        playersList.add(pierino);
        playersList.add(pippo);
        playersList.add(silva);
        Game game = new Game(playersList, 2, 2);
        game.startDecksMapsFromXML();

        pippo.addWeapon(forgeWeaponById(2));

        Assert.assertFalse(pippo.canGrabWeapon(game.getMap().getSquare(0, 0), game.getWeaponStack().getCards().get(0), pippo.getWeapons().get(0)));
    }

    @Test
    public void canGrabWeaponTest2() {
        Player pippo = new Player("pippo", Color.BLUE, false);
        Player jack = new Player("Giacomo", Color.BLUE, false);
        Player anto = new Player("Antonio", Color.GREEN, false);
        Player silva = new Player("Silvano", Color.RED, false);
        Player pierino = new Player("Pierino", Color.GREY, false);

        List<Player> playersList = new ArrayList<>();
        playersList.add(jack);
        playersList.add(anto);
        playersList.add(pierino);
        playersList.add(pippo);
        playersList.add(silva);
        Game game = new Game(playersList, 2, 2);
        game.startDecksMapsFromXML();

        pippo.setAmmoCubes(new AmmoCubes(3,3,3));
        pippo.addWeapon(forgeWeaponById(2));

        Assert.assertTrue(pippo.canGrabWeapon(game.getMap().getSquare(1, 0), game.getMap().getSquare(1,0).getWeapons().get(0), pippo.getWeapons().get(0)));
    }

    @Test
    public void grabWeaponTest(){
        Player pippo = new Player("pippo", Color.BLUE, false);
        Player jack = new Player("Giacomo", Color.BLUE, false);
        Player anto = new Player("Antonio", Color.GREEN, false);
        Player silva = new Player("Silvano", Color.RED, false);
        Player pierino = new Player("Pierino", Color.GREY, false);

        List<Player> playersList = new ArrayList<>();
        playersList.add(jack);
        playersList.add(anto);
        playersList.add(pierino);
        playersList.add(pippo);
        playersList.add(silva);
        Game game = new Game(playersList, 2, 2);
        game.startDecksMapsFromXML();
        pippo.setAmmoCubes(new AmmoCubes(3,3,3));
        boolean test;
        try{
            test=pippo.grabWeapon(game.getMap().getSquare(1,0),game.getMap().getSquare(1,0).getWeapons().get(2),null);
        }catch (MoveException e){
            test=false;
        }

        Assert.assertTrue(test);

    }
    @Test
    public void controlPayment(){
        Player pippo = new Player("pippo", Color.BLUE, false);
        pippo.setAmmoCubes(new AmmoCubes(3,3,3));
        Assert.assertTrue(pippo.canPay(new AmmoCubes(3,3,3)));
    }
    @Test
    public void controlPayment2(){
        Player pippo = new Player("pippo", Color.BLUE, false);
        pippo.setAmmoCubes(new AmmoCubes(0,0,0));
        Assert.assertFalse(pippo.canPay(new AmmoCubes(3,3,3)));
    }
    @Test
    public void controlPayment3(){
        Player pippo = new Player("pippo", Color.BLUE, false);
        pippo.setAmmoCubes(new AmmoCubes(3,3,2));
        Assert.assertFalse(pippo.canPay(new AmmoCubes(3,3,3)));
    }

    @Test
    public void reloadWeaponsTest(){
        Player pippo = new Player("pippo", Color.BLUE, false);
        Player jack = new Player("Giacomo", Color.BLUE, false);
        Player anto = new Player("Antonio", Color.GREEN, false);
        Player silva = new Player("Silvano", Color.RED, false);
        Player pierino = new Player("Pierino", Color.GREY, false);

        List<Player> playersList = new ArrayList<>();
        playersList.add(jack);
        playersList.add(anto);
        playersList.add(pierino);
        playersList.add(pippo);
        playersList.add(silva);
        Game game = new Game(playersList, 2, 2);
        game.startDecksMapsFromXML();
        pippo.setAmmoCubes(new AmmoCubes(3,3,3));
        pippo.addWeapon(game.getMap().getSquare(1,0).getWeapons().get(0));
        pippo.addWeapon(game.getMap().getSquare(1,0).getWeapons().get(1));

        List<WeaponCard>weaponCards=new ArrayList<>();
        weaponCards.add(pippo.getWeapons().get(0));
        weaponCards.add(pippo.getWeapons().get(1));
        pippo.getWeapons().get(0).unload();
        pippo.getWeapons().get(1).unload();
        pippo.reloadWeapons(weaponCards);
        for (WeaponCard weaponCard:weaponCards)
            Assert.assertTrue(weaponCard.isLoaded());
    }
    private WeaponCard forgeWeaponById(int id) {
        Stack<WeaponCard> weaponCardStack = new Stack<>(XMLHelper.forgeWeaponDeck());
        return weaponCardStack.getCardById(id);
    }
}