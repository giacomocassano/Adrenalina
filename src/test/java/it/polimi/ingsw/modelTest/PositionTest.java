package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.Position;
import org.junit.Assert;
import org.junit.Test;

public class PositionTest {

    @Test
    public void posTest(){

        Position pos=new Position();
        Assert.assertEquals(0,pos.getRow());
        Assert.assertEquals(0,pos.getColumn());

        Position pos2=new Position(2,3);
        Assert.assertEquals(2,pos2.getRow());
        Assert.assertEquals(3,pos2.getColumn());

        Assert.assertFalse(pos2.equals(pos));

    }
}
