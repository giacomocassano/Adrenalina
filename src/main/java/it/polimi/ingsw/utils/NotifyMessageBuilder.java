package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.AmmunitionCard;
import it.polimi.ingsw.model.cards.EmpowerCard;
import it.polimi.ingsw.model.cards.WeaponCard;
import it.polimi.ingsw.model.cards.effects.Effect;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Useful class to compose message used during client-server communication.
 */
public class NotifyMessageBuilder {

    private static final String RED_CUBES=" cubi rossi, ";
    private static final String YELLOW_CUBES=" cubi gialli";
    private static final String BLUE_CUBES=" cubi blu, ";
    private static final String EMPOWER=" e un potenziamento.";
    private static final String HAS_GRABBED_MESSAGE=" ha raccolto ";
    private static final String HAS_BUYED_MESSAGE=" ha comprato ";
    private static final String CHANGING_MESSAGE=" scambiandola con ";
    private static final String DEAD_PLAYERS_MESSAGE="Alcuni giocatori sono morti. Stanno per rinascere.";
    private static final String HAS_RESPAWNED_MESSAGE=" è rinato nel punto di respawn ";
    private static final String HAS_SPAWNED_MESSAGE=" sta per nascere.";
    private static final String HAS_RELOADED_MESSAGE="ha ricaricato ";
    private static final String HAS_SHOT_MESSAGE=" ha sparato a ";
    private static final String HAS_USED_MESSAGE=" ha usato ";

    /**
     * Default constructor
     */
    private NotifyMessageBuilder(){}

    /**
     * Builds a message to send to other players and make them aware about a player that has grabbed an ammo card.
     * @param player is the player that has grabbed an ammo card
     * @param ammunitionCard is the ammo card that player has grabbed.
     * @return a string that is a message
     */
    public static String buildGrabAmmoMessage(Player player, AmmunitionCard ammunitionCard) {
        String message = player.getName() + HAS_GRABBED_MESSAGE+ ammunitionCard.getAmmoCubes().getRed() + RED_CUBES +ammunitionCard.getAmmoCubes().getBlue() + BLUE_CUBES+
                ammunitionCard.getAmmoCubes().getYellow() + YELLOW_CUBES;
        if(ammunitionCard.hasEmpower())
            message += EMPOWER;
        else
            message += ".";
        return message;
    }

    /**
     * Builds a message to send to other players and make them aware about a player that has grabbed an weapon card.
     * @param player is the player that has grabbed a weapon card
     * @param dropped is the weapon card that player has dropped.
     * @param gained is the weapon grabbed.
     * @return a string that is a message
     */
    public static String buildGrabWeaponMessage(Player player, WeaponCard gained, WeaponCard dropped) {
        String message = player.getName() + HAS_BUYED_MESSAGE + gained.getName();
        if(dropped != null)
            message += CHANGING_MESSAGE+dropped.getName();
        return message;
    }

    /**
     * Builds a message about a player that has shot to other players
     * @param shooter is the player that has shot
     * @param players are shooter targets
     * @param weaponCard is the weapon that player has used
     * @return a String that is a message.
     */
    public static String buildShootMessage(Player shooter, List<Player> players, WeaponCard weaponCard) {
        List<String> effects = weaponCard.getActiveEffects().stream().map(Effect::getName).collect(Collectors.toList());
        List<String> targets = players.stream().filter(Player::isJustShot).map(Player::getName).collect(Collectors.toList());
        String message = shooter.getName() + HAS_SHOT_MESSAGE;
        for(String t: targets) message += t + " ";
        message += "con " +weaponCard.getName() + " usando ";
        for(String e: effects) message += e +", ";
        return message;
    }

    /**
     * Builds a message about a player that used an empower
     * @param user is empower's user
     * @param empowerCard is the empower that player has used
     * @return a string that is the message
     */
    public static String buildEmpowerUseMessage(Player user, EmpowerCard empowerCard) {
        return user.getName() + HAS_USED_MESSAGE + empowerCard.getName() + " " +empowerCard.getColor().getDescription()+".";
    }

    /**
     * Builds a message about a player that has respawned
     * @param respawned is the respawned player
     * @param dropped is the empower that he has dropped
     * @return a string that is the message
     */
    public static String buildRespawnMessage(Player respawned, EmpowerCard dropped) {
        return respawned.getName() + HAS_RESPAWNED_MESSAGE+dropped.getColor().getDescription() +".";
    }

    /**
     * builds a message about a player that has reload a weapon
     * @param player is the player that reloads the weapon
     * @param weaponCards is the reloaded weapon
     * @return a string that is a message
     */
    public static String buildReloadMessage(Player player, List<WeaponCard> weaponCards) {
        List<String> weapons = weaponCards.stream().map(WeaponCard::getName).collect(Collectors.toList());
        String message = player.getName() + HAS_RELOADED_MESSAGE;
        for(String w: weapons) message += w +" ";
        return message;
    }

    /**
     * builds a message about a player that is dead
     * @return a string that is the message
     */
    public static String buildDeathMessage() {
        return DEAD_PLAYERS_MESSAGE;
    }

    /**
     * builds a message about a player that is spawning
     * @param player is the player that is spawning
     * @return a string that is the message
     */
    public static String buildSpawnMessage(Player player) {
        return player.getName() + HAS_SPAWNED_MESSAGE;
    }

    /**
     * builds a message about a player that is doing a move
     * @param player is the moving player
     * @param moveNumber is the number of his move in his action
     * @param actionNumber is the number of his action in his turn
     * @return a string that is the message
     */
    public static String buildMoveMessage(Player player, int moveNumber, int actionNumber) {
        return "Tocca a " + player.getName()+"! Deve effettuare la "+moveNumber+"° mossa della "+actionNumber+"° azione del suo turno.";
    }
}
