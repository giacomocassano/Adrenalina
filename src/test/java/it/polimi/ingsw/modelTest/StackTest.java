package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.cards.EmpowerCard;
import it.polimi.ingsw.model.cards.Stack;
import it.polimi.ingsw.utils.XMLHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class StackTest {
    XMLHelper XMLHelper=new XMLHelper();

    @Test
    public void pickCardTest(){
        //test for Empower cards
        Stack stack=new Stack(XMLHelper.forgeEmpowerDeck());
        Stack temp=new Stack(stack.getCards());

        for (int i=0;i<24;i++){
           Assert.assertEquals(temp.getCards().get(i),stack.pickCard());
        }

        //test for ammoCards cards
        stack=new Stack(XMLHelper.forgeAmmoDeck());

        temp=new Stack(stack.getCards());
        for(int i=0;i<36;i++)
            Assert.assertEquals(temp.getCards().get(i),stack.pickCard());

        //test for Weapon Cards
        stack=new Stack(XMLHelper.forgeWeaponDeck());
        temp=new Stack(stack.getCards());
        for(int i=0;i<21;i++)
            Assert.assertEquals(temp.getCards().get(i),stack.pickCard());
    }
    @ Test
    public void testFillStack(){
        Stack stack,deletedStack,tmpStack;
        stack=new Stack(XMLHelper.forgeEmpowerDeck());
        for(EmpowerCard card:(ArrayList<EmpowerCard>)stack.getCards())
            System.out.println(card.getName() + " " + card.getColor()+" "+card.getType());
        System.out.println("-------------------------------");
        deletedStack=new Stack();
        for (int i=0;i<24;i++){
            deletedStack.addCard(stack.pickCard());
        }
        tmpStack=new Stack(deletedStack.getCards());
        stack.fillStack(deletedStack);
        for(EmpowerCard card: (ArrayList<EmpowerCard>)stack.getCards())
            System.out.println(card.getName() + " " + card.getColor());

        Assert.assertEquals(tmpStack.getCards(),stack.getCards());
    }

}
