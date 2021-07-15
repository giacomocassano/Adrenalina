package it.polimi.ingsw.utils;

import it.polimi.ingsw.client.user_interfaces.model_representation.EffectInfo;
import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.client.user_interfaces.model_representation.WeaponInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.cards.EmpowerCard;
import it.polimi.ingsw.model.cards.WeaponCard;
import it.polimi.ingsw.model.cards.effects.ShootEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * This class helps find an object from its corresponding representation
 */
public class RepresentationHelper {

    /**
     *
     * takes a list of weapon cards and a list of weapon info,
     * finds the common elements and returns the corresponding weapon cards
     * @param weaponCards is the list of weapon cards
     * @param weaponInfos is the list of weapon infos
     * @return a list of weapon cards
     */
    public static List<WeaponCard> weaponsMapper(List<WeaponCard> weaponCards, List<WeaponInfo> weaponInfos) {
        if(weaponCards == null || weaponInfos == null) return new ArrayList<>();
        List<WeaponCard> mapped = new ArrayList<>();
        for(WeaponCard w: weaponCards)
            for(WeaponInfo i: weaponInfos)
                if(w.getId() == i.getId())
                    mapped.add(w);
        return mapped;
    }
    /**
     *
     * takes a list of empower cards and a list of empower info,
     * finds the common elements and returns the corresponding empower cards
     * @param empowerCards is the list of empower cards
     * @param empowerInfos is the list of empower infos
     * @return a list of empower cards
     */
    public static List<EmpowerCard> empowersMapper(List<EmpowerCard> empowerCards, List<EmpowerInfo> empowerInfos) {
        if(empowerCards == null || empowerInfos == null) return new ArrayList<>();
        List<EmpowerCard> mapped = new ArrayList<>();
        for(EmpowerCard e: empowerCards)
            for(EmpowerInfo i: empowerInfos)
                if(e.getId() == i.getId())
                    mapped.add(e);
        return mapped;
    }

    /**
     *
     * find the corresponding empower card from a empower info
     * @param empowerCards is a list of empower cards
     * @param empowerInfo is the empower info
     * @return the corresponding empower card from a empower info
     */
    public static EmpowerCard empowerMapper(List<EmpowerCard> empowerCards, EmpowerInfo empowerInfo) {
        if(empowerCards == null || empowerInfo == null) return null;
        for(EmpowerCard e: empowerCards)
            if(e.getId() == empowerInfo.getId())
                return e;
        return null;
    }

    /**
     *
     * find the corresponding weapon card from a weapon info
     * @param weaponCards is a list of weapon cards
     * @param weaponInfo is the weapon info
     * @return the corresponding weapon card from a weapon info
     */
    public static WeaponCard weaponMapper(List<WeaponCard> weaponCards, WeaponInfo weaponInfo) {
        if(weaponCards == null || weaponInfo == null) return null;
        for(WeaponCard w: weaponCards)
            if(w.getId() == weaponInfo.getId())
                return w;
        return null;
    }

    /**
     *
     * find the corresponding shoot Effect from a list of effect info
     * @param shootEffects is a list of effects
     * @param effectInfo is the effect info
     * @return the corresponding shootEffect from effectInfo
     */
    public static ShootEffect shootEffectMapper(List<ShootEffect> shootEffects, EffectInfo effectInfo) {
        if(shootEffects == null || effectInfo == null) return null;
        for(ShootEffect shootEffect: shootEffects)
            if(shootEffect.getName().equals(effectInfo.getName()))
                return shootEffect;
        return null;
    }

    /**
     * find the corresponding players from a list of players names.
     * @param players is a list of players
     * @param names is a list of names
     * @return the corresponding players from names
     */
    public static List<Player> playersMapper(List<Player> players, List<String> names) {
        if(players == null ||  names == null) return new ArrayList<>();
        List<Player> playerList = new ArrayList<>();
        for(Player p: players)
            for(String name: names)
                if(p.getName().equals(name))
                    playerList.add(p);
        return playerList;
    }

    /**
     * Finds the correspondent player from a string that is player's name
     * @param players is a list of players
     * @param player is the name of player searched
     * @return a player with that name
     */
    public static Player playerMapper(List<Player> players, String player) {
        if(players == null || player == null) return null;
        for(Player p: players)
            if(p.getName().equals(player))
                return p;
        return null;
    }

    /**
     * Finds the common elements between two lists, one of squares and the other one of
     * positions.
     * @param squares is a list of squares
     * @param positions is a list of positions
     * @return the list of squares correspondent from the positions
     */
    public static List<Square> squaresMapper(List<Square> squares, List<Position> positions) {
        if(squares == null || positions == null) return new ArrayList<>();
        List<Square> squareList = new ArrayList<>();
        for(Square s: squares)
            for(Position p: positions)
                if(s.getRow() == p.getRow() && s.getColumn() == p.getColumn())
                    squareList.add(s);
        return squareList;
    }

    /**
     * Finds a square from a certain position
     * @param squares is a list of squares
     * @param pos is a certain position
     * @return the correspondent square from the position.
     */
    public static Square squareMapper(List<Square> squares, Position pos) {
        if(squares == null || pos == null) return null;
        for(Square s: squares)
            if(s.getRow() == pos.getRow() && s.getColumn() == pos.getColumn())
                return s;
        return null;
    }
}
