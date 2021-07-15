package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.cards.AmmunitionCard;
import it.polimi.ingsw.model.cards.EmpowerCard;
import it.polimi.ingsw.model.cards.WeaponCard;
import it.polimi.ingsw.model.cards.effects.*;
import it.polimi.ingsw.utils.XMLHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class XMLhelperTest {
XMLHelper XMLHelper=new XMLHelper();

@Test
    public void read24Empowers(){
    List<EmpowerCard> mazzo=new ArrayList<>();
    mazzo= XMLHelper.forgeEmpowerDeck();
    Assert.assertEquals(24,mazzo.size());
}

@Test
    public void read36Ammos(){
        List<AmmunitionCard> mazzo=new ArrayList<>();
        mazzo= XMLHelper.forgeAmmoDeck();
        Assert.assertEquals(36,mazzo.size());
    }

@Test
    public void read21Weapons(){

        List<WeaponCard> mazzo=new ArrayList<>();
        mazzo=XMLHelper.forgeWeaponDeck();
        Assert.assertEquals(21,mazzo.size());
}


@Test


    public void showWeapons(){
        List<WeaponCard> mazzo=new ArrayList<>();
        int i=1;
        mazzo=XMLHelper.forgeWeaponDeck();
        for(WeaponCard carta: mazzo) {
            System.out.println("\t\t"+carta.getName() + "\nColor:" + carta.getColor() + "\nCost: (R: " +
                    +carta.getBuyCost().getRed() + ", B: " + carta.getBuyCost().getBlue() + ", Y: " +
                    +carta.getBuyCost().getYellow()+")");
            for(ShootEffect effetto: carta.getBasicEffects()) {
                System.out.println("\tEffect:");
                System.out.println(effetto.getName());
                System.out.println(effetto.getDescription());
                System.out.println("Damages: " + effetto.getnDamages());
                System.out.println("Marks: " + effetto.getnMarks());
                System.out.println("Cost: (R: " + effetto.getCost().getRed() + ", B: " + effetto.getCost().getBlue() + ", Y: " + effetto.getCost().getYellow() + ")");
                if(effetto instanceof NormalShoot) {
                    NormalShoot casted = (NormalShoot) effetto;
                    System.out.println("NORMALSHOOT");
                    System.out.println("Visible targets: " + casted.isShootVisible());
                    System.out.println("Max targets: " + casted.getMaxTarget());
                    if (effetto instanceof RangedShoot) {
                        RangedShoot subcasted = (RangedShoot) effetto;
                        System.out.println("RANGEDSHOOT");
                        System.out.println("Min Range: " + subcasted.getMinRange());
                        System.out.println("Max Range: " + subcasted.getMaxRange());
                    }
                } else if (effetto instanceof DirectionalShoot) {
                    System.out.println("DIRECTIONALSHOOT");
                    System.out.println("Decreased:" + ((DirectionalShoot) effetto).isDecreased());
                    System.out.println("Cyber: " + ((DirectionalShoot) effetto).isCyber());
                    System.out.println("MaxTargets: " + ((DirectionalShoot) effetto).getMaxTargets());
                    System.out.println("MaxRange: " + ((DirectionalShoot) effetto).getMaxRange());
                    System.out.println(" ");
                } else if (effetto instanceof ExplosiveShoot) {
                    System.out.println("EXPLOSIVESHOOT");
                    ExplosiveShoot casted = (ExplosiveShoot) effetto;
                    System.out.println("Area damages: " + casted.getAreaMarks());
                    System.out.println("Area marks: " + casted.getAreaMarks());
                } else if (effetto instanceof LaserShoot) {
                    LaserShoot casted = (LaserShoot) effetto;
                    System.out.println("LASERSHOOT");
                    System.out.println("Max Targets: " + casted.getMaxTargets());
                } else if (effetto instanceof WaveShoot) {
                    WaveShoot casted = (WaveShoot) effetto;
                    System.out.println("WAVESHOOT");
                    System.out.println("MaxTargets: " + casted.getMaxTargets());
                } else if (effetto instanceof WaveMultipleShoot) {
                    System.out.println("WAVEMULTIPLESHOOT");
                } else if (effetto.getName().equals("")) {
                    MultipleDirectionalShoot casted = (MultipleDirectionalShoot) effetto;
                    System.out.println("MULTIPLEDIRECTIONALSHOOT");
                    System.out.println("Decreased:" + casted.isDecreased());
                    System.out.println("MaxRange: " + casted.getMaxRange());
                } else if (effetto instanceof MultipleShoot) {
                    MultipleShoot casted = (MultipleShoot) effetto;
                    System.out.println("MULTIPLESHOOT");
                    System.out.println("Room: " + casted.isRoom());
                    System.out.println("Min Range: " + casted.getMinRange());
                    System.out.println("Max Range: " + casted.getMaxRange());
                } else if (effetto instanceof VortexShoot) {
                    VortexShoot casted = (VortexShoot) effetto;
                    System.out.println("VORTEXSHOOT");
                    System.out.println("Min Range: " + casted.getMinRange());
                    System.out.println("Max Range: " + casted.getMaxRange());
                    System.out.println("Target steps: " + casted.getTargetSteps());
                }
                if(effetto.getUltraDamage()!= null) {
                    UltraDamage ultra = effetto.getUltraDamage();
                    while(ultra != null) {
                        System.out.println("\n\t UltraDamage");
                        System.out.println("Name: "+ultra.getName());
                        System.out.println(ultra.getDescription());
                        System.out.println("Cost: (R: " + ultra.getCost().getRed() + ", B: " + ultra.getCost().getBlue() + ", Y: " + ultra.getCost().getYellow() + ")");
                        System.out.println("Damages: " + ultra.getnDamages());
                        System.out.println("Marks: " + ultra.getnMarks());
                        System.out.println("Chain: "+ultra.isChain());
                        System.out.println("Old target shootable: "+ultra.areOldTargetsShootable());
                        System.out.println("Vortex: " + ultra.isVortex());
                        System.out.println("Area: "+ultra.isArea());
                        ultra = ultra.getUltraDamage();
                    }
                }
            }

            System.out.println("\n\n\n");
        }
        Assert.assertTrue(true);
    }


}
