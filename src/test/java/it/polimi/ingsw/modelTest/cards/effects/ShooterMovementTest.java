package it.polimi.ingsw.modelTest.cards.effects;

import it.polimi.ingsw.exceptions.EffectException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.effects.ShooterMovement;
import org.junit.Assert;
import org.junit.Test;

public class ShooterMovementTest {

    @Test
    public void testingShooterMovement(){
        Map map=new Map(1);
        Player p1=new Player("carlo", Color.YELLOW,false);
        ShooterMovement shooterMovement=new ShooterMovement("move",null,new AmmoCubes(0,0,0),3);
        shooterMovement.setPlayer(p1);
        map.getRespawnRed().addPlayer(p1);
        shooterMovement.setPosition(map.getSquare(0,0));
        Assert.assertTrue(shooterMovement.isValidEffect(map));
        try {
            shooterMovement.performEffect(map);
        }catch (EffectException e){
            e.toString();
        }
        Assert.assertEquals(map.getSquare(0,0),map.getPlayerPosition(p1));
        /*
        Now with just 1 step
         */
        ShooterMovement shooterMovement1=new ShooterMovement("move",null,new AmmoCubes(0,0,0),1);
        shooterMovement1.setPlayer(p1);
        map.getRespawnRed().addPlayer(p1);
        shooterMovement1.isValidEffect(map);
        shooterMovement1.setPosition(map.getSquare(2,3));
        Assert.assertFalse(shooterMovement1.isValidEffect(map));
        //everything null
        ShooterMovement shooterMovement2=new ShooterMovement(null,null,null,1);
        Assert.assertFalse(shooterMovement2.isValidEffect(null));
        //map true but player nor
        shooterMovement2.setPlayer(null);
        Assert.assertFalse(shooterMovement2.isValidEffect(map));

        //player true and map also. but player is not in a position
        map.removeDeadPlayer(p1);
        shooterMovement2.setPlayer(p1);
        Assert.assertFalse(shooterMovement2.isValidEffect(map));
        try {
            shooterMovement2.performEffect(map);
            //doesn't enter in this assert because it throws the exception
            Assert.assertTrue(false);
        }catch (EffectException e){
            Assert.assertTrue(true);
        }

    }


}
