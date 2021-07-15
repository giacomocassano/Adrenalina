package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.client.user_interfaces.model_representation.WeaponInfo;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.events.client_to_server.to_controller.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.utils.XMLHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerTest {

    private Controller controller;
    private Game game;
    private Map map;
    private List<Player> players;
    private Player jack, anto, silva;
    private EmpowerCard teleporter, targetingScope, tagbackGranade, newton;


    private static final int SKULLS = 8;
    private static final int MAP_TYPE = 4;

    @Before
    public void setController() {
        //Set up players
        jack = new Player("JACK", Color.BLUE, true);
        anto = new Player("ANTO", Color.YELLOW, false);
        silva = new Player("SILVA", Color.PURPLE, false);
        jack.setAmmoCubes(new AmmoCubes(3,3,3));
        players = new ArrayList<>();
        players.add(jack);
        players.add(anto);
        players.add(silva);
        //Set up empowers
        teleporter = new EmpowerCard(EmpowerType.TELEPORTER, "TELETRASPORTO", 0, "", Color.BLUE);;
        tagbackGranade = new EmpowerCard(EmpowerType.TAGBACK_GRANADE, "GRANATA VENOM", 0, "", Color.YELLOW);
        targetingScope = new EmpowerCard(EmpowerType.TARGETING_SCOPE, "MIRINO", 0, "", Color.RED);
        newton = new EmpowerCard(EmpowerType.NEWTON, "GRANATA VENOM", 0, "", Color.BLUE);
        //Set up game
        game = new Game(players, SKULLS, MAP_TYPE);
        game.startDecksMapsFromXML();
        map = game.getMap();
        //Set up controller
        controller = new Controller(game);
    }

    @Test
    public void testStartOfTheGame() {
        controller.startGame();
        //Sleep half second
        sleepSomeMillis();
        //Control if controller is waiting for a move
        Assert.assertTrue(controller.isWaitingSpawn());
    }

    @Test
    public void testStartingFirstPlayer() {
        controller.startGame();
        Assert.assertEquals(jack, game.getActivePlayer());
    }

    @Test
    public void testFirstSpawn() {
        controller.startGame();
        sleepSomeMillis();
        spawnPlayer();
        sleepSomeMillis();
        Assert.assertFalse(controller.isWaitingSpawn());
        Assert.assertTrue(controller.isWaitingMove());
    }

    /**
     * Context: first turn is ended.
     * Controller is waiting for a new first spawn.
     */
    @Test
    public void testTheEndOfTurn() {
        controller.startGame();
        sleepSomeMillis();
        //Spawn player
        spawnPlayer();
        sleepSomeMillis();
        //End first action
        endAction();
        sleepSomeMillis();
        dontUseEmpower();
        sleepSomeMillis();
        //End second action
        endAction();
        sleepSomeMillis();
        dontUseEmpower();
        sleepSomeMillis();
        //Control new active player and new spawn request
        Assert.assertTrue(controller.isWaitingSpawn());
        Assert.assertFalse(controller.isWaitingEmpower());
        Assert.assertFalse(controller.isWaitingMove());
        Assert.assertEquals(anto, game.getActivePlayer());
    }

    @Test
    public void testDeathThread() {
        controller.startGame();
        sleepSomeMillis();
        //Spawn player
        spawnPlayer();
        sleepSomeMillis();
        //End first action
        endAction();
        sleepSomeMillis();
        dontUseEmpower();
        sleepSomeMillis();
        //Kill player
        anto.sufferDamage(game.getMap(), jack, 11, 0);
        //End second action
        endAction();
        sleepSomeMillis();
        dontUseEmpower();
        sleepSomeMillis();
        //Control dead thread is active
        Assert.assertFalse(controller.isWaitingSpawn());
        Assert.assertFalse(controller.isWaitingEmpower());
        Assert.assertFalse(controller.isWaitingMove());
        Assert.assertTrue(controller.isWaitingRespawn());
    }

    /**
     * Context: anto and jack are dead.
     * Controller have to see jack and anto as dead.
     */
    @Test
    public void controlDeadPlayers() {
        controller.startGame();
        sleepSomeMillis();
        //Spawn player
        spawnPlayer();
        sleepSomeMillis();
        //End first action
        endAction();
        sleepSomeMillis();
        dontUseEmpower();
        sleepSomeMillis();
        //Kill two players
        anto.sufferDamage(game.getMap(), jack, 11, 0);
        jack.sufferDamage(game.getMap(), silva, 12, 0);
        //End second action
        endAction();
        sleepSomeMillis();
        dontUseEmpower();
        sleepSomeMillis();
        //Control dead thread is active
        Assert.assertTrue(game.getDeadPlayers().contains(jack));
        Assert.assertTrue(game.getDeadPlayers().contains(anto));
        Assert.assertFalse(game.getDeadPlayers().contains(silva));
    }

    /**
     * Context: anto and jack are dead. They respawn.
     */
    @Test
    public void controlRespawn() {
        controller.startGame();
        sleepSomeMillis();
        //Spawn player
        spawnPlayer();
        sleepSomeMillis();
        //End first action
        endAction();
        sleepSomeMillis();
        dontUseEmpower();
        sleepSomeMillis();
        //Kill two players
        anto.sufferDamage(game.getMap(), jack, 11, 0);
        jack.sufferDamage(game.getMap(), silva, 12, 0);
        //End second action
        endAction();
        sleepSomeMillis();
        dontUseEmpower();
        sleepSomeMillis();
        //Control respawns
        controller.update(new RespawnResponse(jack.getName(), jack.getEmpowers().get(0).getEmpowerInfo()));
        Assert.assertTrue(map.getPlayersOnMap().contains(jack));
        Assert.assertFalse(map.getPlayersOnMap().contains(anto));
        controller.update(new RespawnResponse(anto.getName(), anto.getEmpowers().get(0).getEmpowerInfo()));
        Assert.assertTrue(map.getPlayersOnMap().contains(jack));
        Assert.assertTrue(map.getPlayersOnMap().contains(anto));
    }

    @Test
    public void testPlayerMovement() {
        controller.startGame();
        sleepSomeMillis();
        //Spawn player
        spawnPlayer();
        sleepSomeMillis();
        //Move player
        Square playerPosition = game.getMap().getPlayerPosition(game.getActivePlayer());
        Position upperPosition = game.getMap().getSquare(playerPosition.getRow() - 1, playerPosition.getColumn()).getPosition();
        controller.update(new SquareToMoveResponse(game.getActivePlayer().getName(), upperPosition, false));
        //Control position
        Assert.assertEquals(upperPosition, game.getMap().getPlayerPosition(game.getActivePlayer()).getPosition());
    }

    /**
     * Context: jack want to use teleporter
     */
    @Test
    public void testEmpowerUse() {
        //Setup context;
        controller.startGame();
        jack.addEmpower(teleporter);
        sleepSomeMillis();
        //Spawn player
        spawnPlayer();
        sleepSomeMillis();
        //End first action
        endAction();
        sleepSomeMillis();
        //Control jack can use teleporter
        controller.update(new EmpowerResponse(jack.getName(), teleporter.getEmpowerInfo(), true));
        controller.update(new EmpowerUseResponse(jack.getName(), teleporter.getEmpowerInfo(), null, game.getMap().getRespawnYellow().getPosition(),
                null, null, false));
        Assert.assertTrue(game.getMap().getRespawnYellow().getPlayers().contains(jack));

    }

    /**
     * Context: jack is the active player and he wants to grab the first weapon card in
     * red respawn point.
     */
    @Test
    public void testGrabWeapon() {
        //Setup context;
        controller.startGame();
        sleepSomeMillis();
        //Spawn player
        spawnPlayer();
        game.getMap().movePlayer(jack, game.getMap().getRespawnRed());
        sleepSomeMillis();
        //Grab
        WeaponCard weaponCard  =map.getRespawnRed().getWeapons().get(0);
        controller.update(new WeaponToGrabResponse(jack.getName(), weaponCard.getWeaponInfo(), null, new ArrayList<>(), false));
        Assert.assertTrue(jack.getWeapons().contains(weaponCard));
    }

    /**
     * Context: jack is the active player and he wants to grab ammo.
     */
    @Test
    public void testGrabAmmo() {
        //Setup context;
        controller.startGame();
        sleepSomeMillis();
        jack.setAmmoCubes(new AmmoCubes(0,0,0));
        //Spawn player
        spawnPlayer();
        jack.getEmpowers().clear(); //to control the number of emps later
        game.getMap().movePlayer(jack, game.getMap().getSquare(2, 2));
        AmmunitionCard ammoCard = map.getPlayerPosition(jack).getAmmos();
        sleepSomeMillis();
        //Grab
        controller.update(new GrabAmmoResponse(jack.getName(), false));
        //Control jack has grabbed the ammo card
        Assert.assertEquals(ammoCard.getAmmoCubes().getRed(), jack.getAmmoCubes().getRed());
        Assert.assertEquals(ammoCard.getAmmoCubes().getBlue(), jack.getAmmoCubes().getBlue());
        Assert.assertEquals(ammoCard.getAmmoCubes().getYellow(), jack.getAmmoCubes().getYellow());
        if(ammoCard.hasEmpower())
            Assert.assertEquals(1, jack.getEmpowers().size());
        else
            Assert.assertTrue(jack.getEmpowers().isEmpty());
        Assert.assertNull(map.getPlayerPosition(jack).getAmmos());
    }

    /**
     * Context: jack wants to shoot with distruttore.
     */
    @Test
    public void testSelectedWeapon() {
        WeaponCard distruttore = forgeWeaponById(1);
        jack.addWeapon(distruttore);
        map.getRespawnYellow().addPlayer(silva);
        map.getRespawnYellow().addPlayer(anto);
        //Setup context
        controller.startGame();
        sleepSomeMillis();
        //Spawn player
        spawnPlayer();
        sleepSomeMillis();
        map.movePlayer(jack, map.getRespawnYellow());
        //Select weapon
        controller.update(new WeaponToShootResponse(jack.getName(), distruttore.getWeaponInfo(), false));
        Assert.assertEquals(distruttore, game.getActiveTurn().getActiveWeapon());
    }

    /**
     * Context: jack wants to shoot with distruttore basic effect to silva.
     */
    @Test
    public void testBasicEffectShoot() {
        WeaponCard distruttore = forgeWeaponById(1);
        jack.addWeapon(distruttore);
        map.getRespawnYellow().addPlayer(silva);
        map.getRespawnYellow().addPlayer(anto);
        List<String> basicTargets = new ArrayList<>();
        basicTargets.add(silva.getName());
        //Setup context
        controller.startGame();
        sleepSomeMillis();
        //Spawn player
        spawnPlayer();
        sleepSomeMillis();
        map.movePlayer(jack, map.getRespawnYellow());
        //Select weapon
        controller.update(new WeaponToShootResponse(jack.getName(), distruttore.getWeaponInfo(), false));
        controller.update(new BasicEffectResponse(jack.getName(), false, distruttore.getBasicEffects().get(0).getEffectInfo(),
                new ArrayList<>()));
        controller.update(new BasicEffectUseResponse(false, jack.getName(), basicTargets, new ArrayList<>(), null));
        controller.update(new UltraEffectResponse(jack.getName(), false, false, new ArrayList<>()));
        //Control damages
        Assert.assertEquals(2, silva.getNumDamages());
        Assert.assertFalse(distruttore.isLoaded());
    }

    /**
     * Context: jack wants to shoot with distruttore basic effect to silva
     * and with ultra effect to anto.
     */
    @Test
    public void testUltraEffectShoot() {
        WeaponCard distruttore = forgeWeaponById(1);
        jack.addWeapon(distruttore);
        map.getRespawnYellow().addPlayer(silva);
        map.getRespawnYellow().addPlayer(anto);
        List<String> basicTargets = new ArrayList<>();
        basicTargets.add(silva.getName());
        List<String> ultraTargets = new ArrayList<>();
        ultraTargets.add(anto.getName());
        //Setup context
        controller.startGame();
        sleepSomeMillis();
        //Spawn player
        spawnPlayer();
        sleepSomeMillis();
        map.movePlayer(jack, map.getRespawnYellow());
        //Select weapon
        controller.update(new WeaponToShootResponse(jack.getName(), distruttore.getWeaponInfo(), false));
        controller.update(new BasicEffectResponse(jack.getName(), false, distruttore.getBasicEffects().get(0).getEffectInfo(),
                new ArrayList<>()));
        controller.update(new BasicEffectUseResponse(false, jack.getName(), basicTargets, new ArrayList<>(), null));
        controller.update(new UltraEffectResponse(jack.getName(), false, true, new ArrayList<>()));
        controller.update(new UltraEffectUseResponse(false, jack.getName(), ultraTargets, null));
        //Control damages
        Assert.assertEquals(2, silva.getNumDamages());
        Assert.assertEquals(1, anto.getNumMarks());
        Assert.assertFalse(distruttore.isLoaded());
    }


    /**
     * Test an abort.
     * The number of the move must be the same after the abort.
     * The remaining action number must be the same after the abort.
     */
    @Test
    public void testAbort() {
        controller.startGame();
        sleepSomeMillis();
        //Spawn player
        spawnPlayer();
        sleepSomeMillis();
        //Test abort
        int moveNumber1 = game.getActiveTurn().getActiveAction().getMoveCounter();
        int remainingActions1 = game.getActiveTurn().getRemainingActions();
        controller.update(new SquareToMoveResponse(jack.getName(), null, true));
        int moveNumber2 = game.getActiveTurn().getActiveAction().getMoveCounter();
        int remainingActions2 = game.getActiveTurn().getRemainingActions();
        //Control numbers
        Assert.assertEquals(moveNumber1, moveNumber2);
        Assert.assertEquals(remainingActions1, remainingActions2);
    }

    /**
     * Context: controller is waiting for jack's move.
     * Move time exceeded.
     */
    @Test
    public void testMoveTimeExceeded() {
        controller.startGame();
        sleepSomeMillis();
        //Spawn player
        spawnPlayer();
        sleepSomeMillis();
        jack.addEmpower(teleporter);
        //Move time exceeded
        Assert.assertTrue(controller.isWaitingMove());
        controller.update(new MoveExceeded());
        sleepSomeMillis();
        Assert.assertTrue(controller.isWaitingEmpower());
    }

    /**
     * Context: controller is waiting for jack empowers use.
     * Empower time exceed.
     */
    @Test
    public void testEmpowerTimeExceeded() {
        controller.startGame();
        sleepSomeMillis();
        //Spawn player
        spawnPlayer();
        sleepSomeMillis();
        jack.addEmpower(teleporter);
        //End first action
        endAction();
        sleepSomeMillis();
        //Empower time exceed
        Assert.assertTrue(controller.isWaitingEmpower());
        controller.update(new EmpowerUseExceeded());
        sleepSomeMillis();
        Assert.assertTrue(controller.isWaitingMove());
    }

    /**
     * Context: anto is dead. Time to respawn exceed.
     * Controller respawn it.
     */
    @Test
    public void testDeathTimeExceeded() {
        controller.startGame();
        sleepSomeMillis();
        //Spawn player
        spawnPlayer();
        sleepSomeMillis();
        //End first action
        endAction();
        sleepSomeMillis();
        dontUseEmpower();
        sleepSomeMillis();
        //Kill player
        anto.sufferDamage(game.getMap(), jack, 11, 0);
        //End second action
        endAction();
        sleepSomeMillis();
        dontUseEmpower();
        sleepSomeMillis();
        //Death time exceed
        Assert.assertTrue(controller.isWaitingRespawn());
        controller.update(new DeathTimeExceeded());
        sleepSomeMillis();
        Assert.assertTrue(controller.isWaitingSpawn());
    }


    /**
     * Context: jack has a unloaded weapon and he wants to reload his weapon.
     */
    @Test
    public void testReloadWeapon() {
        //Setup context;
        WeaponCard distruttore = forgeWeaponById(1);
        distruttore.unload();
        controller.startGame();
        jack.addWeapon(distruttore);
        sleepSomeMillis();
        //Spawn player
        spawnPlayer();
        sleepSomeMillis();
        List<WeaponInfo> reloads = new ArrayList<>();
        reloads.add(distruttore.getWeaponInfo());
        controller.update(new WeaponsToReloadResponse(jack.getName(), reloads, null, false));
        Assert.assertTrue(distruttore.isLoaded());
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            System.out.println("Errore nello sleep di test.");
        }
    }

    private void sleepSomeMillis() {
        sleep(50);
    }

    private void spawnPlayer() {
        String playerName = game.getActivePlayer().getName();
        EmpowerCard empToDrop = new EmpowerCard(EmpowerType.TELEPORTER, "TELETRASPORTO", 0, "", Color.YELLOW);
        game.getActivePlayer().addEmpower(empToDrop);
        List<EmpowerInfo> others = game.getActivePlayer().getEmpowers().stream()
                .map(EmpowerCard::getEmpowerInfo).filter(e -> e != empToDrop.getEmpowerInfo()).collect(Collectors.toList());
        controller.update(new FirstSpawnResponse(playerName, empToDrop.getEmpowerInfo(), others));
    }

    private void endAction() {
        controller.update(new MoveResponse(game.getActivePlayer().getName(), MoveType.END_ACTION));
    }

    private void dontUseEmpower() {
        controller.update(new EmpowerResponse(game.getActivePlayer().getName(), null, false));
    }

    private WeaponCard forgeWeaponById(int id) {
        Stack<WeaponCard> weaponCardStack = new Stack<>(XMLHelper.forgeWeaponDeck());
        return weaponCardStack.getCardById(id);
    }
}
