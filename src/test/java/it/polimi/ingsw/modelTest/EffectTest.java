package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.exceptions.EffectException;
import it.polimi.ingsw.exceptions.ShootException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Map;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Stack;
import it.polimi.ingsw.model.cards.WeaponCard;
import it.polimi.ingsw.model.cards.effects.ShootEffect;
import it.polimi.ingsw.utils.XMLHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class EffectTest {
    Game game = null;
    List<Player> players = null;
    Map map = null;

    @Before
    public void beforeEachActionTest() {
        players = new ArrayList<>();
        players.add(new Player("Giacomo", Color.BLUE, false));
        players.add(new Player("Silvano", Color.RED, false));
        players.add(new Player("Antonio", Color.YELLOW, false));
        players.add(new Player("Maldini", Color.PURPLE, false));
        players.add(new Player("Baresi", Color.WHITE, false));

        game = new Game(players, 8, new Random().nextInt(3) + 1);
        map = game.getMap();
        game.startDecksMapsFromXML();
        game.getMap().getSquare(0,0).addPlayer(players.get(0));
        game.getMap().getSquare(0,2).addPlayer(players.get(1));
        game.getMap().getSquare(0,2).addPlayer(players.get(2));
        game.getMap().getSquare(2,3).addPlayer(players.get(3));
        game.getMap().getSquare(2,2).addPlayer(players.get(4));
    }

    @Test
    public void recoilTest() throws ShootException, EffectException {
        Player anto = players.get(2);
        Player silva = players.get(1);
        Stack<WeaponCard> stack = new Stack<>(XMLHelper.forgeWeaponDeck());
        List<WeaponCard> weapons = stack.getCards();
        WeaponCard weapon = null;
        for (WeaponCard w : weapons)
            if (w.getName().equals("FUCILE A POMPA")) {
                weapon = w;
            }
        if (weapon != null) {
            anto.addWeapon(weapon);
            ShootEffect effect = weapon.getBasicEffects().get(0);

            effect.setActive(true);
            List<Player> targets = new ArrayList<>();
            targets.add(silva);
            effect.setTargets(targets);
            effect.getRecoil().setPlayer(silva);
            effect.getRecoil().setPosition(map.getSquare(0, 2));
            effect.setShooter(anto);

            Assert.assertTrue(effect.isValidEffect(map));
            anto.shoot(map, weapon);
            Assert.assertEquals(3, silva.getNumDamages(anto));
            Assert.assertEquals(map.getSquare(0, 2), map.getPlayerPosition(silva));
        }
    }

    @Test
    public void otherRecoilTest() throws ShootException, EffectException {
        Player anto = players.get(2);
        Player silva = players.get(1);
        Stack<WeaponCard> stack = new Stack<>(XMLHelper.forgeWeaponDeck());
        List<WeaponCard> weapons = stack.getCards();
        WeaponCard weapon = null;
        for(WeaponCard w: weapons)
            if(w.getName().equals("FUCILE A POMPA")){
                weapon = w;
            }

        anto.addWeapon(weapon);
        ShootEffect effect = weapon.getBasicEffects().get(0);
        effect.setActive(true);
        List<Player> targets = new ArrayList<>();
        targets.add(silva);
        effect.setTargets(targets);
        effect.getRecoil().setPlayer(silva);
        effect.getRecoil().setPosition(map.getSquare(0, 0));
        effect.setShooter(anto);

        Assert.assertFalse(effect.isValidEffect(map));
    }

}