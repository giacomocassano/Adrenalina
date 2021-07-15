package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.exceptions.EffectException;
import it.polimi.ingsw.exceptions.MoveException;
import it.polimi.ingsw.exceptions.ShootException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.utils.WriterHelper;
import it.polimi.ingsw.utils.XMLHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameTest {

    private static int WEAP_DECK_SIZE=21;
    private static int AMMO_DECK_SIZE=36;
    private static int EMP_DECK_SIZE=24;

    private Game game;
    private List<Player> players;
    private Player jack, anto, silva;
    private EmpowerCard teleporter, targetingScope, tagbackGranade, newton;
    private Map map;
    private AmmunitionCard ammo;

    private static final int SKULLS = 8;
    private static final int MAP_TYPE = 4;

    @Before
    public void setGame() {
        //Set up players
        jack = new Player("JACK", Color.BLUE, true);
        anto = new Player("ANTO", Color.YELLOW, false);
        silva = new Player("SILVA", Color.PURPLE, false);
        jack.setAmmoCubes(new AmmoCubes(3,3,3));
        players = new ArrayList<>();
        players.add(jack);
        players.add(anto);
        players.add(silva);
        //Set up game
        game = new Game(players, SKULLS, MAP_TYPE);
        game.startDecksMapsFromXML();
        map = game.getMap();
        //Set up empowers
        teleporter = new EmpowerCard(EmpowerType.TELEPORTER, "TELETRASPORTO", 0, "", Color.BLUE);;
        tagbackGranade = new EmpowerCard(EmpowerType.TAGBACK_GRANADE, "GRANATA VENOM", 0, "", Color.YELLOW);
        targetingScope = new EmpowerCard(EmpowerType.TARGETING_SCOPE, "MIRINO", 0, "", Color.RED);
        newton = new EmpowerCard(EmpowerType.NEWTON, "GRANATA VENOM", 0, "", Color.BLUE);
        //Set up ammo card
        ammo = new AmmunitionCard(new AmmoCubes(0, 1, 2), 88, true);
    }

    @Test
    public void testRanking() {
        Player p1 = new Player("AAA", Color.BLUE, false);
        Player p2 = new Player("BBB", Color.RED, false);
        Player p3 = new Player("CCC", Color.YELLOW, false);
        Player p4 = new Player("DDD", Color.GREY, false);
        Player p5 = new Player("EEE", Color.PURPLE, false);
        p1.addPoints(5);
        p2.addPoints(10);
        p3.addPoints(1);
        p4.addPoints(7);
        p5.addPoints(50);
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        players.add(p5);
        Game game = new Game(players, 8, 2);
        java.util.Map<String, Integer> ranking = game.getRanking(players);
        WriterHelper.printRanking(ranking);
        List<String> rank = ranking.keySet().stream().collect(Collectors.toList());
        Assert.assertEquals("EEE", rank.get(0));
        Assert.assertEquals("BBB", rank.get(1));
        Assert.assertEquals("DDD", rank.get(2));
        Assert.assertEquals("AAA", rank.get(3));
        Assert.assertEquals("CCC", rank.get(4));
    }

    @Test
    public void addFirstEmpowers(){
        Player p1 = new Player("AAA", Color.BLUE, false);
        Player p2 = new Player("BBB", Color.GREEN, false);
        Player p3 = new Player("CCC", Color.YELLOW, false);
        Player p4 = new Player("DDD", Color.GREY, false);
        Player p5 = new Player("EEE", Color.PURPLE, false);
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        players.add(p5);
        Map map=new Map(1);
        Game game=new Game(players,p1,map,true,false);
        game.startDecksMapsFromXML();
        game.firstBorn();
        Assert.assertEquals(2,p1.getEmpowers().size());
    }

    @Test
    public void startDecksXML(){
        Player p1 = new Player("AAA", Color.BLUE, false);
        Player p2 = new Player("BBB", Color.GREEN, false);
        Player p3 = new Player("CCC", Color.YELLOW, false);
        Player p4 = new Player("DDD", Color.GREY, false);
        Player p5 = new Player("EEE", Color.PURPLE, false);
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        players.add(p5);
        Map map=new Map(1);
        Game game=new Game(players,p1,map,true,false);
        Assert.assertNull(game.getEmpowerStack());
        Assert.assertNull(game.getAmmoStack());
        Assert.assertNull(game.getWeaponStack());
        game.startDecksMapsFromXML();
        Assert.assertEquals(WEAP_DECK_SIZE-9,game.getWeaponStack().getSize());
        Assert.assertEquals(AMMO_DECK_SIZE-7,game.getAmmoStack().getSize());
        Assert.assertEquals(EMP_DECK_SIZE,game.getEmpowerStack().getSize());
    }

    @Test
    public void assignWeaponTest(){
        Player p1 = new Player("AAA", Color.BLUE, false);
        Player p2 = new Player("BBB", Color.GREEN, false);
        Player p3 = new Player("CCC", Color.YELLOW, false);
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        Map map=new Map(1);
        Game game=new Game(players,p1,map,true,false);
        game.startDecksMapsFromXML();
        game.assignWeapon(p1,"DISTRUTTORE");
        Assert.assertEquals(0,p2.getWeapons().size());
        Assert.assertEquals(0,p3.getWeapons().size());
    }

    @Test
    public void assignEmpTest(){
        Player p1 = new Player("AAA", Color.BLUE, false);
        Player p2 = new Player("BBB", Color.GREEN, false);
        Player p3 = new Player("CCC", Color.YELLOW, false);
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        Map map=new Map(1);
        Game game=new Game(players,p1,map,true,false);
        game.startDecksMapsFromXML();
        game.assignEmpower(p1,Color.BLUE, EmpowerType.TELEPORTER);
        Assert.assertEquals(1,p1.getEmpowers().size());
    }

    @Test
    public void finalFrenzyTest(){
        /*
        Creating a game with 3 players
         */
        Player p1 = new Player("AAA", Color.BLUE, true);
        Player p2 = new Player("BBB", Color.GREEN, false);
        Player p3 = new Player("CCC", Color.YELLOW, false);
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        Map map=new Map(1);
        Game game=new Game(players,p1,map,true,false);
        game.startDecksMapsFromXML();
        p1.addDeath();
        p1.addDeath();
        p1.addDeath();
        p2.addDeath();
        p3.addDeath();
        p3.addDeath();
        p3.addDeath();
        p3.addDeath();
        Assert.assertEquals(3,p1.getDeaths());
        Assert.assertEquals(1,p2.getDeaths());
        Assert.assertEquals(4,p3.getDeaths());
        /*
        p1 first player
        p2 second
        p3 third
        Setting active p2
         */
        game.getActiveTurn().setActivePlayer(p2);
        game.startFinalFrenzy();
        /*
        Now player deaths are 0 after final frenzy mode.
         */
        Assert.assertEquals(0,p1.getDeaths());
        Assert.assertEquals(0,p2.getDeaths());
        Assert.assertEquals(0,p3.getDeaths());
        /*
         */
        Assert.assertFalse(p1.isFirstFinalFrenzy());
        Assert.assertTrue(p2.isFirstFinalFrenzy());
        Assert.assertFalse(p3.isFirstFinalFrenzy());

    }


    @Test
    public void performFirstSpawnTest(){
        Player p1 = new Player("AAA", Color.BLUE, true);
        Player p2 = new Player("BBB", Color.GREEN, false);
        Player p3 = new Player("CCC", Color.YELLOW, false);
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        Map map=new Map(1);
        Game game=new Game(players,p1,map,true,false);
        game.startDecksMapsFromXML();
        game.assignEmpower(p1,Color.RED,EmpowerType.TELEPORTER);
        game.performFirstSpawn(p1.getEmpowers().get(0));
        Assert.assertEquals(map.getRespawnRed().getRow(),map.getPlayerPosition(p1).getRow());
        Assert.assertEquals(map.getRespawnRed().getColumn(),map.getPlayerPosition(p1).getColumn());
    }

    @Test
    public void getWinnerTest(){
        Player p1 = new Player("AAA", Color.BLUE, true);
        Player p2 = new Player("BBB", Color.GREEN, false);
        Player p3 = new Player("CCC", Color.YELLOW, false);
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        Map map=new Map(4);
        Game game=new Game(players,p1,map,true,false);
        game.startDecksMapsFromXML();
        p1.addPoints(100);
        p2.addPoints(200);
    }

    @Test
    public void testChangeOfActivePlayer() {
        Assert.assertEquals(jack, game.getActivePlayer());
        game.updateActivePlayer();
        Assert.assertEquals(anto, game.getActivePlayer());
        game.updateActivePlayer();
        Assert.assertEquals(silva, game.getActivePlayer());
        game.updateActivePlayer();
        Assert.assertEquals(jack, game.getActivePlayer());
    }

    @Test
    public void testFirstSpawn() {
        jack.addEmpower(teleporter);
        game.performFirstSpawn(teleporter);
        Assert.assertEquals(game.getMap().getRespawnBlue(), game.getMap().getPlayerPosition(jack));
        Assert.assertFalse(jack.getEmpowers().contains(teleporter));
    }

    @Test
    public void testValidTeleporter() {
        //Add teleporter to two players
        jack.addEmpower(teleporter);
        anto.addEmpower(teleporter);
        //Jack is the active player and he can use teleporter
        Assert.assertTrue(game.getValidEmpowerTypes(jack).contains(EmpowerType.TELEPORTER));
        //Anto is not the active player and he can't use teleporter
        Assert.assertFalse(game.getValidEmpowerTypes(anto).contains(teleporter));
    }


    /**
     * Context: ack is the first player and he has shoot to silva, so silva can use tagback granade
     */
    @Test
    public void testValidTagbackGranade() {
        //Add tagback granade to a player and set it to just shooted
        silva.addEmpower(tagbackGranade);
        silva.setJustShot(true);
        //Control that silva can use tagback granade
        Assert.assertTrue(game.getValidEmpowerTypes(silva).contains(EmpowerType.TAGBACK_GRANADE));
    }

    @Test
    public void testDeaths() {
        map.getRespawnRed().addPlayer(jack);
        jack.sufferDamage(map, silva, 12, 0);
        //Check deaths, the dead player must be in the dead list
        game.checkDeaths();
        //Control jack is dead
        Assert.assertTrue(game.getDeadPlayers().contains(jack));
        Assert.assertFalse(game.getDeadPlayers().contains(silva));
        Assert.assertEquals(1, game.getDeadPlayers().size());
    }


    /**
     * Context: jack is dead in the respawn point. He is removed from map from checkDeaths method.
     * Control his respawn in red respawn point.
     */
    @Test
    public void testRespawn() {
        map.getRespawnRed().addPlayer(jack);
        jack.sufferDamage(map, silva, 12, 0);
        jack.addEmpower(targetingScope);
        //Check deaths, the dead player must be in the dead list
        game.checkDeaths();
        //Perform a respawn
        game.performRespawn(jack, targetingScope);
        //Control jack is respawned in right point
        Assert.assertEquals(map.getRespawnRed(), map.getPlayerPosition(jack));
        Assert.assertTrue(game.getDeadPlayers().isEmpty());
        Assert.assertEquals(0, jack.getNumDamages());
    }

    /**
     * Context: jack is not on a respawn point and he grabs an ammunition card.
     * Jack has not ammo cubes.
     */
    @Test
    public void testGrabAmmo() throws MoveException, ShootException, EffectException {
        //Change action
        game.nextAction();
        //Set context
        Square jackPosition = map.getSquare(2, 2);
        jackPosition.addPlayer(jack);
        jack.setAmmoCubes(new AmmoCubes(0, 0, 0));
        game.handleMove(MoveType.GRAB);
        //Control that it is not a respawn point
        Assert.assertFalse(jackPosition.isRespawnPoint());
        //Change ammo card in the position
        AmmunitionCard ammoCard = jackPosition.getAmmos();
        //Jack grabs ammo
        game.handleGrabAmmos();
        //Control jack has grabbed the ammo card
        Assert.assertEquals(ammoCard.getAmmoCubes().getRed(), jack.getAmmoCubes().getRed());
        Assert.assertEquals(ammoCard.getAmmoCubes().getBlue(), jack.getAmmoCubes().getBlue());
        Assert.assertEquals(ammoCard.getAmmoCubes().getYellow(), jack.getAmmoCubes().getYellow());
        if(ammoCard.hasEmpower())
            Assert.assertEquals(1, jack.getEmpowers().size());
        else
            Assert.assertTrue(jack.getEmpowers().isEmpty());
    }

    /**
     * Context: it is the end of the turn. Map must be refilled.
     */
    @Test
    public void testMapRefill() {
        map.getRespawnRed().setWeapons(new ArrayList<>());
        map.getRespawnBlue().setWeapons(new ArrayList<>());
        map.getRespawnYellow().setWeapons(new ArrayList<>());
        Assert.assertEquals(0, map.getRespawnRed().getWeaponsSize());
        Assert.assertEquals(0, map.getRespawnBlue().getWeaponsSize());
        Assert.assertEquals(0, map.getRespawnYellow().getWeaponsSize());
        //Refill map
        map.refill(game.getAmmoStack(), game.getWeaponStack());
        //Control the refill
        Assert.assertEquals(3, map.getRespawnRed().getWeaponsSize());
        Assert.assertEquals(3, map.getRespawnBlue().getWeaponsSize());
        Assert.assertEquals(3, map.getRespawnYellow().getWeaponsSize());
    }

    /**
     * Context: Silva is disconnected and he is not on map.
     * He must respawn.
     */
    @Test
    public void testDisconnectedPlayerRespawn() {
        silva.addEmpower(teleporter);
        Assert.assertFalse(map.getPlayersOnMap().contains(silva));
        //Respawn silva
        game.respawnDisconnectedPlayer(silva);
        //Test he is respawned in blu respawn point
        Assert.assertTrue(map.getPlayersOnMap().contains(silva));
        Assert.assertEquals(map.getRespawnBlue(), map.getPlayerPosition(silva));
    }

    /**
     * Context: Jack is the active player and he want to grab a weapon in red respawn point.
     */
    @Test
    public void testGrabWeapon() throws MoveException, ShootException, EffectException {
        //Set up context
        WeaponCard grabbed = map.getRespawnRed().getWeapons().get(0);
        map.getRespawnRed().addPlayer(jack);
        game.nextAction();
        game.handleMove(MoveType.GRAB);
        //Grab first weapon of respawn point
        game.handleGrabWeapon(grabbed, null);
        //Control grab
        Assert.assertEquals(grabbed, jack.getWeapons().get(0));
        Assert.assertEquals(2, map.getRespawnRed().getWeaponsSize());
    }

    /**
     * Context jack wants to selects Distruttore to shoot.
     * Jack and Silva are on yellow respawn point.
     */
    @Test
    public void testSelectionWeapon() {
        //Set up context
        map.getRespawnYellow().addPlayer(jack);
        map.getRespawnYellow().addPlayer(silva);
        WeaponCard weapon = forgeWeaponById(1);
        jack.addWeapon(weapon);
        game.nextAction();
        game.handleMove(MoveType.SHOOT);
        //Jack choose Distruttore
        game.handleWeaponToShoot(weapon);
        //Assert that distruttore is the active weapon
        Assert.assertEquals(weapon, game.getActiveTurn().getActiveWeapon());
    }

    /**
     * Context jack wants to shoot with his Distruttore to Silva.
     * Jack and Silva are on yellow respawn point.
     */
    @Test
    public void testBasicShoot() throws MoveException, ShootException, EffectException {
        //Set up context
        map.getRespawnYellow().addPlayer(jack);
        map.getRespawnYellow().addPlayer(silva);
        WeaponCard weapon = forgeWeaponById(1);
        jack.addWeapon(weapon);
        game.nextAction();
        game.handleMove(MoveType.SHOOT);
        //Jack choose Distruttore
        game.handleWeaponToShoot(weapon);
        //Jack want to use basic effect
        game.handleBasicEffect(weapon.getBasicEffects().get(0));
        //Set basic effect use
        List<Player> targets = new ArrayList<>();
        targets.add(silva);
        game.handleBasicEffectUse(targets, new ArrayList<>(), null);
        //Don't use ultra effect
        game.handleUltraEffect(false);
        //Control the shoot
        Assert.assertFalse(weapon.isLoaded());
        Assert.assertEquals(2, silva.getNumDamages());
    }

    /**
     * Context jack wants to shoot with his Distruttore to Silva.
     * Jack, Anto and Silva are on yellow respawn point.
     */
    @Test
    public void testUltraShoot() throws MoveException, ShootException, EffectException {
        //Set up context
        map.getRespawnYellow().addPlayer(jack);
        map.getRespawnYellow().addPlayer(silva);
        map.getRespawnYellow().addPlayer(anto);
        WeaponCard weapon = forgeWeaponById(1);
        jack.addWeapon(weapon);
        game.nextAction();
        game.handleMove(MoveType.SHOOT);
        //Jack choose Distruttore
        game.handleWeaponToShoot(weapon);
        //Jack want to use basic effect
        game.handleBasicEffect(weapon.getBasicEffects().get(0));
        //Set basic effect use
        List<Player> targets = new ArrayList<>();
        targets.add(silva);
        game.handleBasicEffectUse(targets, new ArrayList<>(), null);
        //Set ultra effect
        game.handleUltraEffect(true);
        List<Player> ultraTargets = new ArrayList<>();
        ultraTargets.add(anto);
        Assert.assertEquals(weapon.getBasicEffects().get(0).getUltraDamage(), game.getActiveTurn().getActiveUltra());
        game.handleUltraEffectUse(ultraTargets, null);
        //Control the shoot
        Assert.assertFalse(weapon.isLoaded());
        Assert.assertEquals(2, silva.getNumDamages());
        Assert.assertEquals(1, anto.getNumMarks());
    }

    /**
     * Context: jack has just shooted and he wants to reload the weapon
     */
    @Test
    public void testReload() throws MoveException, ShootException, EffectException {
        //Set up context
        map.getRespawnYellow().addPlayer(jack);
        WeaponCard weapon = forgeWeaponById(1);
        jack.addWeapon(weapon);
        weapon.unload();
        game.nextAction();
        //Select weapons to reload
        List<WeaponCard> reloads = new ArrayList<>();
        reloads.add(weapon);
        game.handleReload(reloads);
        //Control the reload
        Assert.assertTrue(weapon.isLoaded());
    }

    /**
     * Context: two players are dead but they are disconnected.
     * Game must respawn them.
     */
    @Test
    public void handleDisconnectedRespawn() {
        map.getRespawnYellow().addPlayer(jack);
        map.getRespawnRed().addPlayer(silva);
        map.getRespawnBlue().addPlayer(anto);
        silva.sufferDamage(map, jack, 11, 0);
        anto.sufferDamage(map, jack, 11, 0);
        anto.setConnected(false);
        silva.setConnected(false);
        //Now check and handle deaths
        game.checkDeaths();
        game.handleDeaths();
        //Control if disconnect player are respawned
        Assert.assertTrue(map.getPlayersOnMap().contains(anto));
        Assert.assertTrue(map.getPlayersOnMap().contains(silva));
    }

    /**
     * Context: Jack want to use the teleporter.
     * Jack is on red respawn point and he wants to move on yellow respawn point.
     */
    @Test
    public void testEmpowerUse() {
        //Set up context
        game.nextAction();
        map.getRespawnRed().addPlayer(jack);
        jack.addEmpower(teleporter);
        game.handleMove(MoveType.END_ACTION);
        //Use empower
        game.handleEmpower(jack, teleporter);
        Assert.assertTrue(game.isValidEmpowerUse(jack, teleporter, null, map.getRespawnYellow()));
        game.handleEmpowerUse(jack, teleporter, null, map.getRespawnYellow());
        Assert.assertEquals(map.getRespawnYellow(), map.getPlayerPosition(jack));
    }

    @Test
    public void testStacksReload() {
        game.reloadStacks(new Stack<>(), new Stack<>(), new Stack<>());
        Assert.assertEquals(0, game.getAmmoStack().getSize());
        Assert.assertEquals(0, game.getWeaponStack().getSize());
        Assert.assertEquals(0, game.getEmpowerStack().getSize());
        //Now forge new stacks and add them in game
        Stack<AmmunitionCard> ammoStack = new Stack<>(XMLHelper.forgeAmmoDeck());
        Stack<WeaponCard> weaponStack = new Stack<>(XMLHelper.forgeWeaponDeck());
        Stack<EmpowerCard> empowerStack = new Stack<>(XMLHelper.forgeEmpowerDeck());
        game.reloadStacks(ammoStack, empowerStack, weaponStack);
        Assert.assertEquals(ammoStack.getSize(), game.getAmmoStack().getSize());
        Assert.assertEquals(weaponStack.getSize(), game.getWeaponStack().getSize());
        Assert.assertEquals(empowerStack.getSize(), game.getEmpowerStack().getSize());

    }

    private WeaponCard forgeWeaponById(int id) {
        Stack<WeaponCard> weaponCardStack = new Stack<>(XMLHelper.forgeWeaponDeck());
        return weaponCardStack.getCardById(id);
    }


    @Test
    public void checkDeathsTest() throws EffectException, ShootException {

        List<Player>players=new ArrayList<>();
        Player anto = new Player("Antonio", Color.YELLOW, false);
        Player maldini = new Player("Maldini", Color.PURPLE, false);
        Player baresi = new Player("Baresi", Color.WHITE, false);
        Player marquez = new Player("Marquez", Color.RED, false);

        players.add(anto);
        players.add(maldini);
        players.add(baresi);
        players.add(marquez);

        Game game=new Game(players,5,2);
        Stack stack=new Stack(XMLHelper.forgeWeaponDeck());


        List<Player> targets=new ArrayList<>();
        targets.add(baresi);
        maldini.addWeapon(getWeapon("TORPEDINE"));
        anto.addWeapon(getWeapon("FUCILE AL PLASMA"));
        marquez.addWeapon(getWeapon("SPADA FOTONICA"));

        game.getMap().getSquare(0,0).addPlayer(anto);
        game.getMap().getSquare(0,2).addPlayer(maldini);
        game.getMap().getSquare(0,2).addPlayer(baresi);
        game.getMap().getSquare(0,2).addPlayer(marquez);

        //Maldini Shoots
        maldini.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        maldini.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);
        maldini.shoot(game.getMap(),maldini.getWeapons().get(0));

        //Antonio Shoots
        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        anto.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);
        anto.shoot(game.getMap(),anto.getWeapons().get(0));

        //Antonio Shoots
        anto.getWeapons().get(0).reload();
        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        anto.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);
        anto.shoot(game.getMap(),anto.getWeapons().get(0));

        //Antonio Shoots
        anto.getWeapons().get(0).reload();
        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        anto.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);
        anto.shoot(game.getMap(),anto.getWeapons().get(0));

        //Antonio Shoots
        anto.getWeapons().get(0).reload();
        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        anto.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);
        anto.shoot(game.getMap(),anto.getWeapons().get(0));

        //Marquez Shoots
        marquez.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        marquez.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);
        marquez.shoot(game.getMap(),marquez.getWeapons().get(0));


        game.checkDeaths();

        Assert.assertEquals(0,marquez.getNumDamages());
        Assert.assertEquals(8,anto.getPoints());
        Assert.assertEquals(7,maldini.getPoints());
        Assert.assertEquals(4,marquez.getPoints());
        Assert.assertEquals(4,game.getKillshotTrack().getRemainingKills());
    }

    // checks if point assignment in final frenzy mode is correct
    @Test
    public void checkDeathsFFTest() throws EffectException, ShootException {

        List<Player>players=new ArrayList<>();
        Player anto = new Player("Antonio", Color.YELLOW, false);
        Player maldini = new Player("Maldini", Color.PURPLE, false);
        Player baresi = new Player("Baresi", Color.WHITE, false);
        Player marquez = new Player("Marquez", Color.RED, false);

        players.add(anto);
        players.add(maldini);
        players.add(baresi);
        players.add(marquez);

        Game game=new Game(players,anto,new Map(2),false,true);
        Stack stack=new Stack(XMLHelper.forgeWeaponDeck());


        List<Player> targets=new ArrayList<>();
        targets.add(baresi);
        maldini.addWeapon(getWeapon("TORPEDINE"));
        anto.addWeapon(getWeapon("FUCILE AL PLASMA"));
        marquez.addWeapon(getWeapon("SPADA FOTONICA"));

        game.getMap().getSquare(0,0).addPlayer(anto);
        game.getMap().getSquare(0,2).addPlayer(maldini);
        game.getMap().getSquare(0,2).addPlayer(baresi);
        game.getMap().getSquare(0,2).addPlayer(marquez);

        //Maldini Shoots
        maldini.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        maldini.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);
        maldini.shoot(game.getMap(),maldini.getWeapons().get(0));

        //Antonio Shoots
        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        anto.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);
        anto.shoot(game.getMap(),anto.getWeapons().get(0));

        //Antonio Shoots
        anto.getWeapons().get(0).reload();
        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        anto.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);
        anto.shoot(game.getMap(),anto.getWeapons().get(0));

        //Antonio Shoots
        anto.getWeapons().get(0).reload();
        anto.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        anto.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);
        anto.shoot(game.getMap(),anto.getWeapons().get(0));

        //Marquez Shoots
        marquez.getWeapons().get(0).reload();
        marquez.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        marquez.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);
        marquez.shoot(game.getMap(),marquez.getWeapons().get(0));

        //Marquez Shoots
        marquez.getWeapons().get(0).reload();
        marquez.getWeapons().get(0).getBasicEffects().get(0).setActive(true);
        marquez.getWeapons().get(0).getBasicEffects().get(0).setTargets(targets);
        marquez.shoot(game.getMap(),marquez.getWeapons().get(0));


        game.checkDeaths();

        Assert.assertEquals(2,anto.getPoints());
        Assert.assertEquals(1,maldini.getPoints());
        Assert.assertEquals(1,marquez.getPoints());
    }

    @Test
    public void giveFinalPlayersPointsTest(){
        List<Player>players=new ArrayList<>();
        Player anto = new Player("Antonio", Color.YELLOW, false);
        Player maldini = new Player("Maldini", Color.PURPLE, false);
        Player baresi = new Player("Baresi", Color.WHITE, false);
        Player marquez = new Player("Marquez", Color.RED, false);

        players.add(anto);
        players.add(maldini);
        players.add(baresi);
        players.add(marquez);

        Game game=new Game(players,anto,new Map(2),false,true);
        Stack stack=new Stack(XMLHelper.forgeWeaponDeck());

        anto.getDamages().add(new Damage(maldini));
        anto.getDamages().add(new Damage(maldini));
        anto.getDamages().add(new Damage(maldini));
        anto.getDamages().add(new Damage(baresi));
        anto.getDamages().add(new Damage(marquez));



        maldini.getDamages().add(new Damage(anto));
        maldini.getDamages().add(new Damage(anto));
        maldini.getDamages().add(new Damage(anto));
        maldini.getDamages().add(new Damage(anto));
        maldini.getDamages().add(new Damage(anto));
        maldini.getDamages().add(new Damage(anto));

        game.giveFinalPlayersPoints();

        Assert.assertEquals(2,anto.getPoints());
        Assert.assertEquals(2,maldini.getPoints());
        Assert.assertEquals(1,marquez.getPoints());


    }



    private WeaponCard getWeapon(String weaponName) {
        //Getting weapon
        Stack stack=new Stack(XMLHelper.forgeWeaponDeck());
        WeaponCard weapon = null;
        for(WeaponCard w: (List<WeaponCard>)stack.getCards()) {
            if(w.getName().equals(weaponName))
                weapon = w;
        }
        return weapon;
    }
}