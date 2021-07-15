package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.AmmoCubes;
import org.junit.Assert;
import org.junit.Test;

public class AmmoCubesTest {


    @Test
    public void defaultConstructorTest(){

        AmmoCubes ammoCubes=new AmmoCubes();
        Assert.assertTrue(ammoCubes.getBlue()==0&&ammoCubes.getRed()==0&&ammoCubes.getBlue()==0);
    }


    @Test
    public void noDefaultConstructorTest(){

        AmmoCubes ammoCubes=new AmmoCubes(1,2,3);
        Assert.assertTrue(ammoCubes.getBlue()==2&&ammoCubes.getRed()==1&&ammoCubes.getYellow()==3);
    }

    @Test
    public void changeAmmoTest(){

        AmmoCubes ammoCubes=new AmmoCubes(1,2,3);
        AmmoCubes ammoCubes1=new AmmoCubes(0,0,0);
        ammoCubes1.addAmmoCubes(ammoCubes);
        Assert.assertTrue(ammoCubes.getBlue()==2&&ammoCubes.getRed()==1&&ammoCubes.getYellow()==3);
    }

    @Test
    public void addAmmoRed(){

        AmmoCubes ammoCubes=new AmmoCubes(0,2,3);
        ammoCubes.addAmmoCubesRed(3);
        Assert.assertEquals(3,ammoCubes.getRed());
        Assert.assertTrue(ammoCubes.addAmmoCubesRed(33));
        Assert.assertEquals(3,ammoCubes.getRed());

    }

    @Test
    public void addAmmoBlue(){

        AmmoCubes ammoCubes=new AmmoCubes(0,2,3);
        Assert.assertEquals(2,ammoCubes.getBlue());
        ammoCubes.addAmmoCubesBlue(3);
        Assert.assertEquals(3,ammoCubes.getBlue());
        Assert.assertTrue(ammoCubes.addAmmoCubesBlue(113));
        Assert.assertEquals(3,ammoCubes.getBlue());

    }

    @Test
    public void addAmmoBlue2(){

        AmmoCubes ammoCubes=new AmmoCubes(0,2,3);
        Assert.assertEquals(2,ammoCubes.getBlue());
        Assert.assertFalse(ammoCubes.addAmmoCubesBlue(-4));

    }

    @Test
    public void addAmmoBlue3(){

        AmmoCubes ammoCubes=new AmmoCubes(-1,-2,-3);
        AmmoCubes ammoCubes1=new AmmoCubes();
        Assert.assertFalse(ammoCubes1.addAmmoCubes(ammoCubes));

    }
    @Test
    public void testIsLowerThan(){

        AmmoCubes ammoCubes=new AmmoCubes(1,2,3);
        AmmoCubes ammoCubes1=new AmmoCubes(0,0,0);
        Assert.assertTrue(ammoCubes1.isLowerThan(ammoCubes));

    }

    @Test
    public void testDecreasingAmmos(){
        AmmoCubes ammoCubes=new AmmoCubes(1,2,3);
        ammoCubes.decreaseAmmoCubesBlue(2);
        Assert.assertEquals(0,ammoCubes.getBlue());
        Assert.assertFalse(ammoCubes.decreaseAmmoCubesBlue(-1));


        AmmoCubes ammoCubes1=new AmmoCubes(1,2,3);
        ammoCubes1.decreaseAmmoCubesYellow(3);
        Assert.assertEquals(0,ammoCubes1.getYellow());
        Assert.assertFalse(ammoCubes1.decreaseAmmoCubesYellow(-1));

    }

    @Test
    public void resetToZeroTest(){
        AmmoCubes ammoCubes=new AmmoCubes(1,2,3);
        ammoCubes.resetToZero();
        Assert.assertEquals(0,ammoCubes.getYellow());
        Assert.assertEquals(0,ammoCubes.getBlue());
        Assert.assertEquals(0,ammoCubes.getRed());

    }



}
