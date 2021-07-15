package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.exceptions.EffectException;
import it.polimi.ingsw.exceptions.ShootException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.WeaponCard;
import it.polimi.ingsw.model.cards.effects.RecoilMovement;
import it.polimi.ingsw.model.cards.effects.ShootEffect;
import it.polimi.ingsw.model.cards.effects.UltraDamage;
import it.polimi.ingsw.utils.XMLHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WeaponCardTest {

    Map map1, map2, map3, map4, readyMap;
    Player jack, silva, anto, maldini, baresi, marquez, hamilton;
    List<WeaponCard> deck;
    XMLHelper XMLHelper=new XMLHelper();
    //Helper method that return the card from cards sstack with name = weaponName
    private WeaponCard getWeapon(String weaponName) {
        //Getting weapon
        WeaponCard weapon = null;
        for(WeaponCard w: deck) {
            if(w.getName().equals(weaponName))
                weapon = w;
        }
        return weapon;
    }

    @Before
    public void beforeEachTest() {
        //Set up maps
        map1 = new Map(1);
        map2 = new Map(2);
        map3 = new Map(3);
        map4 = new Map(4);
        readyMap = new Map(1);

        //Set up players
        jack = new Player("Giacomo", Color.BLUE, false);
        silva = new Player("Silvano", Color.RED, false);
        anto = new Player("Antonio", Color.YELLOW, false);
        maldini = new Player("Maldini", Color.PURPLE, false);
        baresi = new Player("Baresi", Color.WHITE, false);
        marquez = new Player("Marquez", Color.PURPLE, false);
        hamilton = new Player("Hamilton", Color.WHITE, false);

        deck = XMLHelper.forgeWeaponDeck();

        readyMap.getSquare(0,0).addPlayer(jack);
        readyMap.getSquare(1,2).addPlayer(silva);
        readyMap.getSquare(0,2).addPlayer(anto);
        readyMap.getSquare(2,3).addPlayer(maldini);
        readyMap.getSquare(2,2).addPlayer(baresi);
    }

    @Test
    public void testDistruttore() throws EffectException , ShootException{

        //Basic Effect
        map1.getSquare(0,0).addPlayer(jack);
        map1.getSquare(1,2).addPlayer(silva);
        map1.getSquare(0,2).addPlayer(anto);
        map1.getSquare(2,3).addPlayer(maldini);
        map1.getSquare(2,2).addPlayer(baresi);

        WeaponCard weapon = this.getWeapon("DISTRUTTORE");

        List<Player> targets = new ArrayList<>();
        targets.add(silva);
        ShootEffect basic = weapon.getBasicEffects().get(0);
        basic.setActive(true);
        basic.setTargets(targets);
        jack.addWeapon(weapon);
        jack.shoot(map1, weapon);

        Assert.assertTrue(silva.getNumDamages() == 2 && silva.getNumMarks() == 1);
        weapon.reload();
        basic.setActive(true);
        basic.setTargets(targets);
        jack.shoot(map1, weapon);
        weapon.reload();
        targets.remove(silva);
        targets.add(anto);
        basic.setActive(true);
        basic.setTargets(targets);
        jack.shoot(map1, weapon);

        //Optional Effect
        targets.removeAll(targets);
        jack.getWeapons().get(0).reload();
        jack.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        jack.getWeapons().get(0).getBasicEffects().get(0).getUltraDamage().setActive(true);

        targets.add(anto);
        jack.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);
        jack.getWeapons().get(0).getBasicEffects().get(0).getUltraDamage().addNewTarget(anto);

        jack.shoot(map1, weapon);

        Assert.assertEquals(5, anto.getNumDamages());
        Assert.assertEquals(2, anto.getNumMarks());
        Assert.assertEquals(5, anto.getNumDamages(jack));
        Assert.assertEquals(2, anto.getNumMarks(jack));



    }

    @Test
    public void mitragliatriceTest() throws EffectException , ShootException{

        //Basic Effect

        map4.getSquare(1, 2).addPlayer(jack);
        map4.getSquare(0, 3).addPlayer(silva);
        map4.getSquare(0, 2).addPlayer(anto);
        map4.getSquare(1, 2).addPlayer(marquez);
        map4.getSquare(0, 1).addPlayer(hamilton);


        WeaponCard weapon = this.getWeapon("MITRAGLIATRICE");

        anto.addWeapon(weapon);
        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);

        List<Player> targets =new ArrayList<>();


        targets.add(marquez);

        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        anto.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);

        Assert.assertTrue(anto.isValidShoot(map4, weapon));
        anto.shoot(map4,anto.getWeapons().get(0));

        Assert.assertEquals(1, marquez.getNumDamages());
        Assert.assertEquals(0, marquez.getNumMarks());
        Assert.assertEquals(1, marquez.getNumDamages(anto));
        Assert.assertEquals(0, marquez.getNumMarks(anto));

        //Another Test
        targets.add(silva);
        anto.getWeapons().get(0).reload();
        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        anto.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets); //marquez and silva
        Assert.assertTrue(anto.isValidShoot(map4, weapon));
        anto.shoot(map4, anto.getWeapons().get(0));


        Assert.assertEquals(2, marquez.getNumDamages());
        Assert.assertEquals(0, marquez.getNumMarks());
        Assert.assertEquals(2, marquez.getNumDamages(anto));
        Assert.assertEquals(0, marquez.getNumMarks(anto));

        Assert.assertEquals(1, silva.getNumDamages());
        Assert.assertEquals(0, silva.getNumMarks());
        Assert.assertEquals(1, silva.getNumDamages(anto));
        Assert.assertEquals(0, silva.getNumMarks(anto));

        //First Optional Effect
        anto.getWeapons().get(0).reload();
        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        anto.getWeapons().get(0).getBasicEffects().get(0).getUltraDamage().setActive(true);

        anto.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets); //silva and marquenz
        anto.getWeapons().get(0).getBasicEffects().get(0).getUltraDamage().addNewTarget(silva);

        Assert.assertTrue(anto.isValidShoot(map4, weapon));
        anto.shoot(map4,anto.getWeapons().get(0));

        Assert.assertEquals(3, silva.getNumDamages());
        Assert.assertEquals(0, silva.getNumMarks());
        Assert.assertEquals(3, silva.getNumDamages(anto));
        Assert.assertEquals(0, silva.getNumMarks(anto));

        Assert.assertEquals(3, marquez.getNumDamages());
        Assert.assertEquals(0, marquez.getNumMarks());
        Assert.assertEquals(3, marquez.getNumDamages(anto));
        Assert.assertEquals(0, marquez.getNumMarks(anto));

        //Second Optional Effect

        anto.getWeapons().get(0).reload();
        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        anto.getWeapons().get(0).getBasicEffects().get(0).getUltraDamage().setActive(true);
        anto.getWeapons().get(0).getBasicEffects().get(0).getUltraDamage().getUltraDamage().setActive(true);

        anto.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);//silva and marquenz
        anto.getWeapons().get(0).getBasicEffects().get(0).getUltraDamage().addNewTarget(silva);
        anto.getWeapons().get(0).getBasicEffects().get(0).getUltraDamage().getUltraDamage().addNewTarget(marquez);

        Assert.assertTrue(anto.isValidShoot(map4, weapon));
        anto.shoot(map4,anto.getWeapons().get(0));

        Assert.assertEquals(5, silva.getNumDamages());
        Assert.assertEquals(0, silva.getNumMarks());
        Assert.assertEquals(5, silva.getNumDamages(anto));
        Assert.assertEquals(0, silva.getNumMarks(anto));

        Assert.assertEquals(5, marquez.getNumDamages());
        Assert.assertEquals(0, marquez.getNumMarks());
        Assert.assertEquals(5, marquez.getNumDamages(anto));
        Assert.assertEquals(0, marquez.getNumMarks(anto));
    }


    /*
    @Test
    public void testTorpedine() throws ShootException, EffectException {
        //Getting weapon to jack player
        WeaponCard weapon = this.getWeapon("TORPEDINE");
        jack.addWeapon(weapon);

        //Getting effects
        ShootEffect basic = weapon.getBasicEffects().get(0);
        UltraDamage ultra1 = basic.getUltraDamage();
        UltraDamage ultra2 = ultra1.getUltraDamage();

        //Setting up effects
        basic.setActive(true);
        basic.addTarget(anto);
        ultra1.setActive(true);
        ultra1.addNewTarget(silva);
        ultra2.setActive(true);
        ultra2.addNewTarget(maldini);

        Assert.assertTrue(jack.isValidShoot(readyMap, weapon));
        jack.shoot(readyMap, weapon);

    }
    */
    @Test
    public void FucileAlPlasmaTest() throws ShootException, EffectException {
        //Getting weapon to jack player
        WeaponCard weapon = this.getWeapon("FUCILE AL PLASMA");
        jack.addWeapon(weapon);

        //Getting effects
        ShootEffect basic = weapon.getBasicEffects().get(0);
        UltraDamage ultra1 = basic.getUltraDamage();
        UltraDamage ultra2 = ultra1.getUltraDamage();

        //Setting up effects
        basic.setActive(true);
        basic.addTarget(anto);
        ultra1.setActive(true);
        ultra1.addNewTarget(anto);

        Assert.assertTrue(jack.isValidShoot(readyMap, weapon));
        jack.shoot(readyMap, weapon);
    }

    @Test
    public void testFucileDiPrecisione() throws EffectException, ShootException {
        map1.getSquare(0,0).addPlayer(jack);
        map1.getSquare(1,2).addPlayer(silva);
        map1.getSquare(0,2).addPlayer(anto);
        map1.getSquare(2,3).addPlayer(maldini);
        map1.getSquare(2,2).addPlayer(baresi);

        WeaponCard weapon = this.getWeapon("FUCILE DI PRECISIONE");

        List<Player> targets = new ArrayList<>();
        targets.add(silva);
        ShootEffect basic = weapon.getBasicEffects().get(0);
        basic.setActive(true);
        basic.setTargets(targets);
        jack.addWeapon(weapon);
        jack.shoot(map1, weapon);

        Assert.assertTrue(silva.getNumDamages() == 3 && silva.getNumMarks() == 1);
        System.out.println();
    }

    @Test
    public void falceProtonicaTest() throws EffectException , ShootException{
        map4.getSquare(1, 2).addPlayer(jack);
        map4.getSquare(1, 2).addPlayer(silva);
        map4.getSquare(1, 2).addPlayer(anto);
        map4.getSquare(1, 2).addPlayer(marquez);
        map4.getSquare(1, 2).addPlayer(hamilton);

        WeaponCard weaponCard = this.getWeapon("FALCE PROTONICA");

        anto.addWeapon(weaponCard);
        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);


        List<Square> squares=new ArrayList<>();
        squares.add(map4.getPlayerPosition(anto));
        anto.getWeapons().get(0).getBasicEffects().get(0).setSquares(squares);

        anto.shoot(map4, anto.getWeapons().get(0));

        Assert.assertEquals(1, marquez.getNumDamages());
        Assert.assertEquals(0, marquez.getNumMarks());
        Assert.assertEquals(1, marquez.getNumDamages(anto));
        Assert.assertEquals(0, marquez.getNumMarks(anto));

        Assert.assertEquals(1, silva.getNumDamages());
        Assert.assertEquals(0, silva.getNumMarks());
        Assert.assertEquals(1, silva.getNumDamages(anto));
        Assert.assertEquals(0, silva.getNumMarks(anto));

        Assert.assertEquals(1, jack.getNumDamages());
        Assert.assertEquals(0, jack.getNumMarks());
        Assert.assertEquals(1, jack.getNumDamages(anto));
        Assert.assertEquals(0, jack.getNumMarks(anto));

        Assert.assertEquals(1, hamilton.getNumDamages());
        Assert.assertEquals(0, hamilton.getNumMarks());
        Assert.assertEquals(1, hamilton.getNumDamages(anto));
        Assert.assertEquals(0, hamilton.getNumMarks(anto));

        //Alternative Effect
        squares.removeAll(squares);
        squares.add(map4.getPlayerPosition(anto));

        anto.getWeapons().get(0).getBasicEffects().get(1).setActive(true);
        anto.getWeapons().get(0).getBasicEffects().get(1).setSquares(squares);
        anto.getWeapons().get(0).reload();

        anto.shoot(map4, anto.getWeapons().get(0));

        Assert.assertEquals(3, marquez.getNumDamages());
        Assert.assertEquals(0, marquez.getNumMarks());
        Assert.assertEquals(3, marquez.getNumDamages(anto));
        Assert.assertEquals(0, marquez.getNumMarks(anto));

        Assert.assertEquals(3, silva.getNumDamages());
        Assert.assertEquals(0, silva.getNumMarks());
        Assert.assertEquals(3, silva.getNumDamages(anto));
        Assert.assertEquals(0, silva.getNumMarks(anto));

        Assert.assertEquals(3, jack.getNumDamages());
        Assert.assertEquals(0, jack.getNumMarks());
        Assert.assertEquals(3, jack.getNumDamages(anto));
        Assert.assertEquals(0, jack.getNumMarks(anto));

        Assert.assertEquals(3, hamilton.getNumDamages());
        Assert.assertEquals(0, hamilton.getNumMarks());
        Assert.assertEquals(3, hamilton.getNumDamages(anto));
        Assert.assertEquals(0, hamilton.getNumMarks(anto));
    }

    @Test
    public void raggioTraenteTest() throws EffectException, ShootException{
        //Getting weapon to jack player
        WeaponCard weapon = this.getWeapon("RAGGIO TRAENTE");
        jack.addWeapon(weapon);

        //Getting effects
        ShootEffect basic = weapon.getBasicEffects().get(0);
        ShootEffect alter = weapon.getBasicEffects().get(1);

        //Setting up effects
        basic.setActive(true);
        basic.addTarget(maldini);
        basic.addSquare(readyMap.getSquare(0, 1));

        //Maldini is too far from the vortex square
        Assert.assertFalse(jack.isValidShoot(readyMap, weapon));

        basic.resetTargets();
        basic.addTarget(silva);
        Assert.assertTrue(jack.isValidShoot(readyMap, weapon));
        jack.shoot(readyMap, weapon);
        Assert.assertEquals(readyMap.getSquare(0, 1), readyMap.getPlayerPosition(silva));
        Assert.assertEquals(1, silva.getNumDamages());

        //Assert true when the target is not visible but the vortex square is visible
        weapon.reload();
        basic.setActive(true);
        basic.addTarget(baresi);
        basic.addSquare(readyMap.getSquare(1, 1));

        Assert.assertTrue(jack.isValidShoot(readyMap, weapon));
        jack.shoot(readyMap, weapon);
        Assert.assertEquals(readyMap.getSquare(1, 1), readyMap.getPlayerPosition(baresi));
        Assert.assertEquals(1, baresi.getNumDamages());

        //Testing alternative effect
        weapon.reload();
        alter.setActive(true);
        alter.addTarget(anto);
        //Is not necessary set the vortex square, bc in this effect the vortex square is the shooter square

        Assert.assertTrue(jack.isValidShoot(readyMap, weapon));
        jack.shoot(readyMap, weapon);
        Assert.assertEquals(readyMap.getSquare(0,0), readyMap.getPlayerPosition(anto));
        Assert.assertEquals(3, anto.getNumDamages());
    }

    @Test
    public void cannoneVortexTest() throws ShootException, EffectException {
        //Getting weapon to baresi player
        WeaponCard weapon = this.getWeapon("CANNONE VORTEX");
        baresi.addWeapon(weapon);

        //Getting effects
        ShootEffect basic = weapon.getBasicEffects().get(0);
        UltraDamage ultra = basic.getUltraDamage();

        //Setting up effects
        basic.setActive(true);
        basic.addTarget(silva);
        basic.addSquare(readyMap.getSquare(1, 3));
        ultra.setActive(true);
        ultra.addNewTarget(maldini);

        Assert.assertTrue(baresi.isValidShoot(readyMap, weapon));
        baresi.shoot(readyMap, weapon);
        Assert.assertEquals(readyMap.getSquare(1, 3), readyMap.getPlayerPosition(silva));
        Assert.assertEquals(readyMap.getSquare(1, 3), readyMap.getPlayerPosition(maldini));
        Assert.assertEquals(2, silva.getNumDamages());
        Assert.assertEquals(1, maldini.getNumDamages());
    }

    @Test
    public void vulcanizzatoreTest() throws EffectException , ShootException{
        map4.getSquare(2, 2).addPlayer(jack);
        map4.getSquare(1, 3).addPlayer(silva);
        map4.getSquare(0, 3).addPlayer(anto);
        map4.getSquare(2, 3).addPlayer(marquez);
        map4.getSquare(1, 2).addPlayer(hamilton);

        WeaponCard weaponCard = this.getWeapon("VULCANIZZATORE");

        anto.addWeapon(weaponCard);
        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);

        anto.getWeapons().get(0).getBasicEffects().get(0).setTargets(map4.getPlayerInRoom(Color.YELLOW));
        anto.getWeapons().get(0).getBasicEffects().get(0).setSquares(map4.getRoomSquares(Color.YELLOW));
        anto.shoot(map4, anto.getWeapons().get(0));

        Assert.assertEquals(1, marquez.getNumDamages());
        Assert.assertEquals(0, marquez.getNumMarks());
        Assert.assertEquals(1, marquez.getNumDamages(anto));
        Assert.assertEquals(0, marquez.getNumMarks(anto));

        Assert.assertEquals(1, hamilton.getNumDamages());
        Assert.assertEquals(0, hamilton.getNumMarks());
        Assert.assertEquals(1, hamilton.getNumDamages(anto));
        Assert.assertEquals(0, hamilton.getNumMarks(anto));

        Assert.assertEquals(1, jack.getNumDamages());
        Assert.assertEquals(0, jack.getNumMarks());
        Assert.assertEquals(1, jack.getNumDamages(anto));
        Assert.assertEquals(0, jack.getNumMarks(anto));

        Assert.assertEquals(1, silva.getNumDamages());
        Assert.assertEquals(0, silva.getNumMarks());
        Assert.assertEquals(1, silva.getNumDamages(anto));
        Assert.assertEquals(0, silva.getNumMarks(anto));

        //Alternative Effect

        List<Square> squares = new ArrayList<>();
        anto.getWeapons().get(0).getBasicEffects().get(1).setActive(true);
        squares.add(map4.getSquare(1, 3));
        anto.getWeapons().get(0).getBasicEffects().get(1).setSquares(squares);
        anto.getWeapons().get(0).reload();

        anto.shoot(map4, anto.getWeapons().get(0));

        Assert.assertEquals(2, silva.getNumDamages());
        Assert.assertEquals(1, silva.getNumMarks());
        Assert.assertEquals(2, silva.getNumDamages(anto));
        Assert.assertEquals(1, silva.getNumMarks(anto));

    }

    @Test
    public void razzoTermicoTest() throws EffectException , ShootException{
        map4.getSquare(0,0).addPlayer(jack);
        map4.getSquare(0,0).addPlayer(silva);
        map4.getSquare(0,0).addPlayer(anto);
        map4.getSquare(2,3).addPlayer(maldini);
        map4.getSquare(2,2).addPlayer(baresi);

        WeaponCard weaponCard = this.getWeapon("RAZZO TERMICO");

        List<Player> targets = new ArrayList<>();
        targets.add(maldini);
        anto.addWeapon(weaponCard);
        ShootEffect basic = weaponCard.getBasicEffects().get(0);
        basic.setActive(true);
        basic.setTargets(targets);
        anto.shoot(map4, anto.getWeapons().get(0));
        Assert.assertEquals(3,maldini.getNumDamages());
    }

    @Test
    public void testRaggioSolare() throws EffectException , ShootException{
        map1.getSquare(0,0).addPlayer(jack);
        map1.getSquare(0,2).addPlayer(silva);
        map1.getSquare(0,2).addPlayer(anto);
        map1.getSquare(0,2).addPlayer(maldini);
        map1.getSquare(0,2).addPlayer(baresi);

        WeaponCard weapon = this.getWeapon("RAGGIO SOLARE");

        List<Player> targets = new ArrayList<>();
        targets.add(silva);
        ShootEffect basic = weapon.getBasicEffects().get(0);
        basic.setActive(true);
        basic.setTargets(targets);
        jack.addWeapon(weapon);
        jack.shoot(map1, weapon);
        Assert.assertTrue(silva.getNumDamages() == 1 && silva.getNumMarks() == 1
                && anto.getNumMarks() == 1 && maldini.getNumMarks() == 1 && baresi.getNumMarks() == 1 && baresi.getNumDamages() == 0);
    }

    @Test
    public void lanciafiammeBasicEffectTest() throws EffectException , ShootException{

        //Basic Effect
        map2.getSquare(2,1).addPlayer(jack);
        map2.getSquare(1,1).addPlayer(silva);
        map2.getSquare(0,1).addPlayer(anto);
        map2.getSquare(2,2).addPlayer(marquez);
        map2.getSquare(2,3).addPlayer(hamilton);

        WeaponCard lanciafiamme = this.getWeapon("LANCIAFIAMME");

        jack.addWeapon(lanciafiamme);
        //Test upper direction
        List<Player> targets = new ArrayList<>();
        targets.add(silva);
        targets.add(anto);
        ShootEffect basic = lanciafiamme.getBasicEffects().get(0);
        basic.setActive(true);
        basic.setTargets(targets);

        Assert.assertTrue(jack.canShoot(map2, lanciafiamme));
        jack.shoot(map2, lanciafiamme);

        Assert.assertEquals(1, silva.getNumDamages());
        Assert.assertEquals(0, silva.getNumMarks());
        Assert.assertEquals(1, anto.getNumDamages());
        Assert.assertEquals(0, anto.getNumMarks());
        lanciafiamme.reload();

        //Test right direction
        targets.clear();
        targets.add(marquez);
        targets.add(hamilton);
        basic = lanciafiamme.getBasicEffects().get(0);
        basic.setActive(true);
        basic.setTargets(targets);

        Assert.assertTrue(jack.canShoot(map2, lanciafiamme));
        jack.shoot(map2, lanciafiamme);

        Assert.assertEquals(1, marquez.getNumDamages());
        Assert.assertEquals(0, marquez.getNumMarks());
        Assert.assertEquals(1, hamilton.getNumDamages());
        Assert.assertEquals(0, hamilton.getNumMarks());
        lanciafiamme.reload();

        //Test left direction
        hamilton.addWeapon(lanciafiamme);
        jack.removeWeapons(lanciafiamme);
        targets.clear();
        targets.add(marquez);
        targets.add(jack);
        basic = lanciafiamme.getBasicEffects().get(0);
        basic.setActive(true);
        basic.setTargets(targets);

        Assert.assertTrue(hamilton.canShoot(map2, lanciafiamme));
        hamilton.shoot(map2, lanciafiamme);

        Assert.assertEquals(2, marquez.getNumDamages());
        Assert.assertEquals(0, marquez.getNumMarks());
        Assert.assertEquals(1, jack.getNumDamages());
        Assert.assertEquals(0, jack.getNumMarks());
        lanciafiamme.reload();

        //Test down direction
        anto.addWeapon(lanciafiamme);
        hamilton.removeWeapons(lanciafiamme);
        targets.clear();
        targets.add(silva);
        targets.add(jack);
        basic = lanciafiamme.getBasicEffects().get(0);
        basic.setActive(true);
        basic.setTargets(targets);

        Assert.assertTrue(anto.canShoot(map2, lanciafiamme));
        anto.shoot(map2, lanciafiamme);

        Assert.assertEquals(2, silva.getNumDamages());
        Assert.assertEquals(0, silva.getNumMarks());
        Assert.assertEquals(2, jack.getNumDamages());
        Assert.assertEquals(0, jack.getNumMarks());
        lanciafiamme.reload();
    }

    @Test
    public void alternativeEffectLanciafiammeTest() throws ShootException, EffectException {
        //Basic Effect
        map2.getSquare(2,1).addPlayer(jack);
        map2.getSquare(1,1).addPlayer(silva);
        map2.getSquare(0,1).addPlayer(anto);
        map2.getSquare(2,2).addPlayer(marquez);
        map2.getSquare(2,3).addPlayer(hamilton);

        WeaponCard lanciafiamme = this.getWeapon("LANCIAFIAMME");

        jack.addWeapon(lanciafiamme);
        //Test upper direction
        List<Square> squares = new ArrayList<>();
        squares.add(map2.getSquare(0, 1));
        squares.add(map2.getSquare(1, 1));
        ShootEffect basic = lanciafiamme.getBasicEffects().get(1);
        basic.setActive(true);
        basic.setSquares(squares);

        Assert.assertTrue(jack.canShoot(map2, lanciafiamme));
        jack.shoot(map2, lanciafiamme);

        Assert.assertEquals(2, silva.getNumDamages());
        Assert.assertEquals(0, silva.getNumMarks());
        Assert.assertEquals(1, anto.getNumDamages());
        Assert.assertEquals(0, anto.getNumMarks());
        lanciafiamme.reload();

        //Test right direction
        squares.clear();
        squares.add(map2.getSquare(2, 2));
        squares.add(map2.getSquare(2, 3));

        basic = lanciafiamme.getBasicEffects().get(1);
        basic.setActive(true);
        basic.setSquares(squares);

        Assert.assertTrue(jack.canShoot(map2, lanciafiamme));
        jack.shoot(map2, lanciafiamme);

        Assert.assertEquals(2, marquez.getNumDamages());
        Assert.assertEquals(0, marquez.getNumMarks());
        Assert.assertEquals(1, hamilton.getNumDamages());
        Assert.assertEquals(0, hamilton.getNumMarks());
        lanciafiamme.reload();

        //Test left direction
        hamilton.addWeapon(lanciafiamme);
        jack.removeWeapons(lanciafiamme);
        squares.clear();
        squares.add(map2.getSquare(2, 1));
        squares.add(map2.getSquare(2, 2));
        basic = lanciafiamme.getBasicEffects().get(1);
        basic.setActive(true);
        basic.setSquares(squares);

        Assert.assertTrue(hamilton.canShoot(map2, lanciafiamme));
        hamilton.shoot(map2, lanciafiamme);

        Assert.assertEquals(4, marquez.getNumDamages());
        Assert.assertEquals(0, marquez.getNumMarks());
        Assert.assertEquals(1, jack.getNumDamages());
        Assert.assertEquals(0, jack.getNumMarks());
        lanciafiamme.reload();

        //Test down direction
        anto.addWeapon(lanciafiamme);
        hamilton.removeWeapons(lanciafiamme);
        squares.clear();
        squares.add(map2.getSquare(1, 1));
        squares.add(map2.getSquare(2, 1));

        basic = lanciafiamme.getBasicEffects().get(1);
        basic.setActive(true);
        basic.setSquares(squares);

        Assert.assertTrue(anto.canShoot(map2, lanciafiamme));
        anto.shoot(map2, lanciafiamme);

        Assert.assertEquals(4, silva.getNumDamages());
        Assert.assertEquals(0, silva.getNumMarks());
        Assert.assertEquals(2, jack.getNumDamages());
        Assert.assertEquals(0, jack.getNumMarks());
        lanciafiamme.reload();
    }

    @Test
    public void otherLanciafiammeTest() throws ShootException, EffectException {
        map4.movePlayer(jack, map4.getSquare(0, 0));
        map4.movePlayer(silva, map4.getSquare(0, 1));
        map4.movePlayer(anto, map4.getSquare(0, 2));

        WeaponCard weapon = this.getWeapon("LANCIAFIAMME");
        ShootEffect basic = weapon.getBasicEffects().get(0);
        basic.setActive(true);
        List<Player> targets = basic.getPossibleTargets(map4, jack);
        Assert.assertEquals(2, targets.size());

        basic.setTargets(targets);

        jack.addWeapon(weapon);
        jack.shoot(map4, weapon);

        Assert.assertEquals(1, silva.getNumDamages());
        Assert.assertEquals(1, anto.getNumDamages());

    }

    /*
    @Test
    public void lanciaGranateTest() throws ShootException, EffectException {
        readyMap.movePlayer(baresi, readyMap.getSquare(1, 2));
        readyMap.movePlayer(maldini, readyMap.getSquare(0,0));
        //Getting weapon to jack player
        WeaponCard weapon = this.getWeapon("LANCIAGRANATE");
        jack.addWeapon(weapon);

        //Getting effects
        ShootEffect basic = weapon.getBasicEffects().get(0);
        UltraDamage ultra = basic.getUltraDamage();
        RecoilMovement recoil = basic.getRecoil();

        //Setting up effects
        basic.setActive(true);
        basic.addTarget(anto);
        recoil.setPlayer(anto);
        recoil.setPosition(readyMap.getSquare(1, 2));
        ultra.setActive(true);
        ultra.addSquare(readyMap.getSquare(0,0 ));

        Assert.assertTrue(jack.isValidShoot(readyMap, weapon));
        jack.shoot(readyMap, weapon);

        Assert.assertEquals(1, anto.getNumDamages());
        Assert.assertEquals(0, silva.getNumDamages());
        Assert.assertEquals(1, maldini.getNumDamages());
        Assert.assertEquals(0, baresi.getNumDamages());
        Assert.assertEquals(0, jack.getNumDamages()); //jack doesn't suffer damage because he's the shooter
    }
    */

    @Test
    public void lanciarazziTest() throws EffectException , ShootException{

        //Basic Effect
        map4.getSquare(0,1).addPlayer(jack);
        map4.getSquare(0,3).addPlayer(silva);
        map4.getSquare(0,2).addPlayer(anto);
        map4.getSquare(2,3).addPlayer(marquez);
        map4.getSquare(2,2).addPlayer(hamilton);

        WeaponCard weaponCard = this.getWeapon("LANCIARAZZI");

        anto.addWeapon(weaponCard);


        List<Player> targets = new ArrayList<>();
        targets.add(marquez);
        RecoilMovement recoil=anto.getWeapons().get(0).getBasicEffects().get(0).getRecoil();
        recoil.setPlayer(marquez);
        recoil.setPosition(map4.getSquare(2,2));

        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        anto.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);

        Assert.assertTrue(anto.isValidShoot(map4, anto.getWeapons().get(0)));
        anto.shoot(map4,anto.getWeapons().get(0));

        Assert.assertEquals(2,marquez.getNumDamages());
        Assert.assertEquals(0,marquez.getNumMarks());
        Assert.assertEquals(2,marquez.getNumDamages(anto));
        Assert.assertEquals(0,marquez.getNumMarks(anto));

        Assert.assertEquals(map4.getSquare(2,2),map4.getPlayerPosition(marquez));



    }

    @Test
    public void fucileLaserTest() throws EffectException , ShootException{
        map4.getSquare(2,2).addPlayer(jack);
        map4.getSquare(1,3).addPlayer(silva);
        map4.getSquare(1,2).addPlayer(anto);
        map4.getSquare(1,1).addPlayer(marquez);
        map4.getSquare(1,0).addPlayer(hamilton);

        WeaponCard weaponCard = this.getWeapon("FUCILE LASER");

        anto.addWeapon(weaponCard);
        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);

        List<Player> targets = new ArrayList<>();
        targets.add(marquez);

        anto.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);
        anto.shoot(map4,anto.getWeapons().get(0));

        Assert.assertEquals(3,marquez.getNumDamages());
        Assert.assertEquals(0,marquez.getNumMarks());
        Assert.assertEquals(3,marquez.getNumDamages(anto));
        Assert.assertEquals(0,marquez.getNumMarks(anto));

        //another test
        targets.remove(marquez);
        targets.add(hamilton);
        anto.getWeapons().get(0).reload();
        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        anto.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);
        anto.shoot(map4,anto.getWeapons().get(0));

        Assert.assertEquals(3,hamilton.getNumDamages());
        Assert.assertEquals(0,hamilton.getNumMarks());
        Assert.assertEquals(3,hamilton.getNumDamages(anto));
        Assert.assertEquals(0,hamilton.getNumMarks(anto));


        //alternative Effect
        targets.removeAll(targets);

        targets.add(marquez);
        targets.add(hamilton);

        anto.getWeapons().get(0).reload();
        anto.getWeapons().get(0).getBasicEffects().get(1).setActive(true);
        anto.getWeapons().get(0).getBasicEffects().get(1).setTargets(targets);
        anto.shoot(map4,anto.getWeapons().get(0));

        Assert.assertEquals(5,hamilton.getNumDamages());
        Assert.assertEquals(0,hamilton.getNumMarks());
        Assert.assertEquals(5,hamilton.getNumDamages(anto));
        Assert.assertEquals(0,hamilton.getNumMarks(anto));

        Assert.assertEquals(5,marquez.getNumDamages());
        Assert.assertEquals(0,marquez.getNumMarks());
        Assert.assertEquals(5,marquez.getNumDamages(anto));
        Assert.assertEquals(0,marquez.getNumMarks(anto));
    }

    @Test
    public void spadaFotonicaTest() throws EffectException , ShootException{
        map4.getSquare(1, 3).addPlayer(jack);
        map4.getSquare(1, 2).addPlayer(silva);
        map4.getSquare(1, 2).addPlayer(anto);
        map4.getSquare(1, 2).addPlayer(marquez);
        map4.getSquare(1, 2).addPlayer(hamilton);

        WeaponCard weaponCard = this.getWeapon("SPADA FOTONICA");
        anto.addWeapon(weaponCard);
        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);

        List<Player> targets = new ArrayList<>();
        targets.add(marquez);

        anto.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);
        anto.shoot(map4,anto.getWeapons().get(0));

        Assert.assertEquals(2, marquez.getNumDamages());
        Assert.assertEquals(0, marquez.getNumMarks());
        Assert.assertEquals(2, marquez.getNumDamages(anto));
        Assert.assertEquals(0, marquez.getNumMarks(anto));
    }

    @Test
    public  void ZX2() throws EffectException , ShootException{
        map1.getSquare(0,0).addPlayer(jack);
        map1.getSquare(2,3).addPlayer(silva);
        map1.getSquare(1,2).addPlayer(anto);
        map1.getSquare(1,0).addPlayer(marquez);
        map1.getSquare(2,1).addPlayer(hamilton);

        WeaponCard weaponCard = this.getWeapon("ZX-2");
        anto.addWeapon(weaponCard);
        List<Player> targets = new ArrayList<>();
        targets.add(marquez);

        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        anto.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);

        anto.shoot(map1,anto.getWeapons().get(0));

        Assert.assertEquals(1,marquez.getNumDamages());
        Assert.assertEquals(2,marquez.getNumMarks());
        Assert.assertEquals(1,marquez.getNumDamages(anto));
        Assert.assertEquals(2,marquez.getNumMarks(anto));

        anto.getWeapons().get(0).reload();
        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        anto.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);

        anto.shoot(map1,anto.getWeapons().get(0));
        Assert.assertEquals(4,marquez.getNumDamages());
        Assert.assertEquals(2,marquez.getNumMarks());
        Assert.assertEquals(4,marquez.getNumDamages(anto));
        Assert.assertEquals(2,marquez.getNumMarks(anto));

        //reload
        anto.removeWeapons(weaponCard);
        jack.addWeapon(weaponCard);
        jack.getWeapons().get(0).reload();


        jack.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        jack.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);

        jack.shoot(map1,jack.getWeapons().get(0));

        Assert.assertEquals(5,marquez.getNumDamages());
        Assert.assertEquals(4,marquez.getNumMarks());
        Assert.assertEquals(4,marquez.getNumDamages(anto));
        Assert.assertEquals(2,marquez.getNumMarks(anto));
        Assert.assertEquals(2,marquez.getNumMarks(jack));
        Assert.assertEquals(1,marquez.getNumDamages(jack));



        //Alternative effect test



        jack.removeWeapons(weaponCard);

        jack = new Player("Giacomo", Color.BLUE, true);
        silva = new Player("Silvano", Color.RED, false);
        anto = new Player("Antonio", Color.YELLOW, false);
        marquez = new Player("Marquez", Color.PURPLE, false);
        hamilton = new Player("Hamilton", Color.WHITE, false);

        map1.getSquare(0,0).addPlayer(jack);
        map1.getSquare(2,3).addPlayer(silva);
        map1.getSquare(1,2).addPlayer(anto);
        map1.getSquare(1,0).addPlayer(marquez);
        map1.getSquare(2,1).addPlayer(hamilton);

        anto.addWeapon(weaponCard);

        targets.removeAll(targets);
        targets.add(jack);

        anto.getWeapons().get(0).getBasicEffects().get(1).setActive(true);
        anto.getWeapons().get(0).getBasicEffects().get(1).setTargets(targets);

        anto.shoot(map1,anto.getWeapons().get(0));
        Assert.assertEquals(0,jack.getNumDamages());
        Assert.assertEquals(1,jack.getNumMarks());
        Assert.assertEquals(1,jack.getNumMarks(anto));

        anto.getWeapons().get(0).reload();
        targets.add(silva);
        anto.getWeapons().get(0).getBasicEffects().get(1).setTargets(targets);
        anto.getWeapons().get(0).getBasicEffects().get(1).setActive(true);

        anto.shoot(map1,anto.getWeapons().get(0));

        Assert.assertEquals(0,jack.getNumDamages());
        Assert.assertEquals(2,jack.getNumMarks());
        Assert.assertEquals(2,jack.getNumMarks(anto));

        Assert.assertEquals(0,silva.getNumDamages());
        Assert.assertEquals(1,silva.getNumMarks());
        Assert.assertEquals(1,silva.getNumMarks(anto));

        anto.getWeapons().get(0).reload();
        targets.add(marquez);
        anto.getWeapons().get(0).getBasicEffects().get(1).setTargets(targets);
        anto.getWeapons().get(0).getBasicEffects().get(1).setActive(true);

        anto.shoot(map1,anto.getWeapons().get(0));

        Assert.assertEquals(0,jack.getNumDamages());
        Assert.assertEquals(3,jack.getNumMarks());
        Assert.assertEquals(3,jack.getNumMarks(anto));

        Assert.assertEquals(0,silva.getNumDamages());
        Assert.assertEquals(2,silva.getNumMarks());
        Assert.assertEquals(2,silva.getNumMarks(anto));

        Assert.assertEquals(0,marquez.getNumDamages());
        Assert.assertEquals(1,marquez.getNumMarks());
        Assert.assertEquals(1,marquez.getNumMarks(anto));
    }

    @Test
    public void fucileAPompaTest() throws EffectException , ShootException{
        map4.getSquare(2,2).addPlayer(jack);
        map4.getSquare(1,3).addPlayer(silva);
        map4.getSquare(0,0).addPlayer(anto);
        map4.getSquare(0,0).addPlayer(marquez);
        map4.getSquare(0,1).addPlayer(hamilton);

        WeaponCard weaponCard = this.getWeapon("FUCILE A POMPA");

        //Adding weapon to the shooter
        anto.addWeapon(weaponCard);

        //Setting the basic effect configuration
        ShootEffect basicEffect = weaponCard.getBasicEffects().get(0);
        basicEffect.setActive(true);
        List<Player> targets = new ArrayList<>();
        targets.add(marquez);
        basicEffect.setTargets(targets);

        //Setting the recoil configuration
        RecoilMovement recoil = basicEffect.getRecoil();
        recoil.setPlayer(marquez);
        recoil.setPosition(map4.getSquare(0, 1));

        Assert.assertTrue(anto.isValidShoot(map4, weaponCard));
        anto.shoot(map4,anto.getWeapons().get(0));

        //Asserting the damages
        Assert.assertEquals(3,marquez.getNumDamages());
        Assert.assertEquals(0,marquez.getNumMarks());
        Assert.assertEquals(0,marquez.getNumMarks(anto));
        Assert.assertEquals(3,marquez.getNumDamages(anto));
        //Asserting the recoil
        Assert.assertEquals(map4.getSquare(0,1),map4.getPlayerPosition(marquez));

        //Test alternative effect
        weaponCard.reload();
        //Setting the alternative effect configuration
        ShootEffect alternativeEffect = weaponCard.getBasicEffects().get(1);
        alternativeEffect.setActive(true);
        targets.removeAll(targets);
        targets.add(hamilton);
        alternativeEffect.setTargets(targets);
        Assert.assertTrue(anto.isValidShoot(map4, weaponCard));
        anto.shoot(map4,anto.getWeapons().get(0));

        Assert.assertEquals(2,hamilton.getNumDamages());
        Assert.assertEquals(0,hamilton.getNumMarks());
        Assert.assertEquals(2,hamilton.getNumDamages(anto));
        Assert.assertEquals(0,hamilton.getNumMarks(anto));

    }

    @Test
    public void testCyberguanto() throws EffectException , ShootException{
        map1.getSquare(0,0).addPlayer(jack);
        map1.getSquare(0,1).addPlayer(silva);
        map1.getSquare(0,2).addPlayer(anto);
        map1.getSquare(2,3).addPlayer(maldini);
        map1.getSquare(2,2).addPlayer(baresi);

        WeaponCard weapon = this.getWeapon("CYBERGUANTO");

        List<Player> targets = new ArrayList<>();
        targets.add(silva);
        ShootEffect basic = weapon.getBasicEffects().get(0);
        basic.setActive(true);
        basic.setTargets(targets);
        jack.addWeapon(weapon);
        jack.shoot(map1, weapon);
        Assert.assertTrue(map1.getPlayerPosition(jack) == map1.getSquare(0,1));

        map1.movePlayer(jack, map1.getSquare(0,0));
        weapon.reload();
        weapon.getBasicEffects().get(0).setActive(false);
        weapon.getBasicEffects().get(1).setActive(true);
        weapon.getBasicEffects().get(1).setTargets(targets);
        targets.add(anto);
        jack.shoot(map1, weapon);
        Assert.assertTrue( map1.getPlayerPosition(jack) == map1.getPlayerPosition(anto));
    }

    @Test
    public void ondaDurtoTest() throws EffectException , ShootException{
        //Basic Effect
        map3.getSquare(0,2).addPlayer(jack);
        map3.getSquare(1,3).addPlayer(silva);
        map3.getSquare(1,2).addPlayer(anto);
        map3.getSquare(2,2).addPlayer(marquez);
        map3.getSquare(2,2).addPlayer(hamilton);

        WeaponCard weaponCard = this.getWeapon("ONDA D'URTO");
        anto.addWeapon(weaponCard);
        List<Player> targets = new ArrayList<>();
        targets.add(silva);
        targets.add(jack);
        targets.add(marquez);
        ShootEffect basic = weaponCard.getBasicEffects().get(0);
        basic.setActive(true);
        basic.setTargets(targets);

        anto.shoot(map3,anto.getWeapons().get(0));
        Assert.assertEquals(1,silva.getNumDamages());
        Assert.assertEquals(0,silva.getNumMarks());
        Assert.assertEquals(1,silva.getNumDamages(anto));

        Assert.assertEquals(1,jack.getNumDamages());
        Assert.assertEquals(0,jack.getNumMarks());
        Assert.assertEquals(1,jack.getNumDamages(anto));


        Assert.assertEquals(1,marquez.getNumDamages());
        Assert.assertEquals(0,marquez.getNumMarks());
        Assert.assertEquals(1,marquez.getNumDamages(anto));

        Assert.assertEquals(0,hamilton.getNumDamages());
        Assert.assertEquals(0,hamilton.getNumMarks());

        //Test alternative effect
        map3.movePlayer(anto, map3.getSquare(1, 3));
        map3.movePlayer(silva, map3.getSquare(1, 2));
        anto.removeWeapons(weaponCard);
        silva.addWeapon(weaponCard);
        weaponCard.reload();
        ShootEffect altEffect = weaponCard.getBasicEffects().get(1);
        altEffect.setActive(true);
        Assert.assertTrue(silva.isValidShoot(map3, weaponCard));
        silva.shoot(map3, weaponCard);
        Assert.assertEquals(1, anto.getNumDamages(silva));
        Assert.assertEquals(1, jack.getNumDamages(silva));
        Assert.assertEquals(1, hamilton.getNumDamages(silva));
        Assert.assertEquals(1, marquez.getNumDamages(silva));

    }

    @Test
    public void martelloIonicoTest() throws EffectException , ShootException{
        map4.getSquare(2,2).addPlayer(jack);
        map4.getSquare(1,3).addPlayer(silva);
        map4.getSquare(0,3).addPlayer(anto);
        map4.getSquare(0,3).addPlayer(marquez);
        map4.getSquare(1,0).addPlayer(hamilton);

        WeaponCard weaponCard = this.getWeapon("MARTELLO IONICO");
        anto.addWeapon(weaponCard);
        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);

        List<Player> targets = new ArrayList<>();
        targets.add(marquez);

        anto.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);
        anto.shoot(map4,anto.getWeapons().get(0));

        Assert.assertEquals(2,marquez.getNumDamages());
        Assert.assertEquals(0,marquez.getNumMarks());
        Assert.assertEquals(2,marquez.getNumDamages(anto));
        Assert.assertEquals(0,marquez.getNumMarks(anto));

        weaponCard.reload();
        //Alternative Effect
        ShootEffect altEff = weaponCard.getBasicEffects().get(1);
        altEff.setActive(true);
        RecoilMovement rec = altEff.getRecoil();
        rec.setPlayer(marquez);
        rec.setPosition(map4.getSquare(0,1));
        altEff.setTargets(targets);

        Assert.assertTrue(anto.isValidShoot(map4, weaponCard));
        anto.shoot(map4,anto.getWeapons().get(0));

        Assert.assertEquals(5,marquez.getNumDamages());
        Assert.assertEquals(0,marquez.getNumMarks());
        Assert.assertEquals(5,marquez.getNumDamages(anto));
        Assert.assertEquals(0,marquez.getNumMarks(anto));
        Assert.assertEquals(map4.getSquare(0,1),map4.getPlayerPosition(marquez));
    }
}


