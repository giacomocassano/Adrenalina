package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.cards.Stack;
import it.polimi.ingsw.model.cards.WeaponCard;
import it.polimi.ingsw.utils.XMLHelper;
import org.junit.Assert;
import org.junit.Test;

public class TurnTest {


    @Test
    public void test1(){
        /*
        See if setActiveWeapons works.
         */
        Turn turn=new Turn();
        Player player=new Player("Silvano", Color.BLUE,true);
        Stack<WeaponCard> weaponCardStack=new Stack<WeaponCard>(XMLHelper.forgeWeaponDeck());
        player.addWeapon(weaponCardStack.getCardById(1));
        turn.setActivePlayer(player);
        turn.setActiveWeapon(player.getWeapons().get(0));
        Assert.assertEquals(1,turn.getActiveWeapon().getId());
        /*
        See if setActive Effect works.
         */
        turn.setActiveEffect(player.getWeapons().get(0).getBasicEffects().get(0));
        Assert.assertEquals(weaponCardStack.getCardById(1).getBasicEffects().get(0),turn.getActiveEffect());


    }


}
