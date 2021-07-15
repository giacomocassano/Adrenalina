package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exceptions.EffectException;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Multiple shoot is a weapon shoot that allows shooter to hit some targets
 * between a min range and a max range or hits in a room.
 */
public class MultipleShoot extends ShootEffect{

    private static final String NOT_VALID_EFFECT_MESSAGE = "L'effetto non è valido";
    private static int MAX_ROOM_SQUARES = 12;

    private boolean room;
    private int minRange;
    private int maxRange;

    /**
     *
     * @param name is effect's name
     * @param description is effect's description
     * @param cost is effect's cost based on ammocubes
     * @param nDamages is max number of damage that effect deals
     * @param nMarks is max number of marks that effect deals
     * @param ultraDamage is the ultra effect related to this one
     * @param minRange is the minimum distance that enemies must be to be hit
     * @param maxRange is the max distance that enemies must be to be hit
     * @param room is true if effect is room based.
     */
    public MultipleShoot(String name, String description, AmmoCubes cost, int nDamages, int nMarks, UltraDamage ultraDamage, int minRange, int maxRange, boolean room) {
        super(name, description, cost, true, nDamages, nMarks, false, true, null, ultraDamage);
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.room = room;
    }

    /**
     * Checks if effect is valid
     * checks if every square selected is in the same room, if room is visible and makes other controls
     * @param map is the playing map. Is used by the method to see if effect can be performed.
     * @return true if effect is valid and ready to be performed.
     */
    @Override
    public boolean isValidEffect(Map map) {
        if(getShooter() == null)
            return false;
        if(room){
            if(getSquares() == null || getSquares().isEmpty() || getShooter() == null)
                return false;
            Color roomColor = getSquares().get(0).getColor();
            List<Square> roomSquares = map.getRoomSquares(roomColor);

            if(roomSquares.size() != getSquares().size() || !roomSquares.containsAll(getSquares()))
                return false;

            if(!map.getVisibleRoom(getShooter()).contains(roomColor)) return false;
        }else{

            if(minRange == 0 && maxRange == 0) {
                List<Square> squares = new ArrayList<>();
                squares.add(map.getPlayerPosition(getShooter()));
            }else if(getSquares() == null || getSquares().isEmpty())
                return false;

            if(getSquares().size() != 1 && !map.getOtherSquare(map.getPlayerPosition(getShooter()), true).containsAll(getSquares())) return false;

            for(Square square: getSquares()){
                int distance = map.getSquareDistance(map.getPlayerPosition(getShooter()), square);
                if(distance < getMinRange() || distance > getMaxRange()) return false;
            }
        }
        return true;
    }

    /**
     * Performs the effect adding damage to targets
     * @param map is the playing map and is used to perform the effect.
     * @throws EffectException if there is an error during the perform of the effect
     */
    @Override
    public void performEffect(Map map) throws EffectException {
        if(!isValidEffect(map)) throw new EffectException(NOT_VALID_EFFECT_MESSAGE);
        for(Square square: getSquares()) {
            getTargets().addAll(square.getPlayers());
            for(Player target: square.getPlayers())
                if(target != getShooter())
                    target.sufferDamage(map, getShooter(), getnDamages(), getnMarks());
        }
        if(getUltraDamage() != null) {
            getUltraDamage().setShooter(getShooter());
            getUltraDamage().setTargets(getTargets());
        }
    }

    /**
     *
     * @return if effect is room based
     */
    public boolean isRoom() {
        return room;
    }

    /**
     *
     * @return min range of the effect
     */
    public int getMinRange() {
        return minRange;
    }

    /**
     *
     * @return max range of the effect
     */
    public int getMaxRange() {
        return maxRange;
    }

    /**
     * Gets possible targets
     * @param map is the playing map
     * @param shooter is the player that is using the effect
     * @return a list of possible targets that shooter can hit using this effect.
     */
    @Override
    public List<Player> getPossibleTargets(Map map, Player shooter) {
        return new ArrayList<>();
    }

    /**
     *
     * @param map is the playing map
     * @param shooter is the player that is using the effect
     * @return a list of possible targets that shooter can hit with the effect
     */
    @Override
    public List<Square> getPossibleSquares(Map map, Player shooter) {
        List<Square> squares = new ArrayList<>();
        Square shPos = map.getPlayerPosition(shooter);
        if(!room) {
            squares = map.getOtherSquare(shPos, true);
            squares = squares.stream().filter(s -> map.getSquareDistance(shPos, s) <= maxRange)
                    .filter(s -> map.getSquareDistance(shPos, s) >= minRange)
                    .collect(Collectors.toList());
        }else{
            List<Color> rooms = map.getNearRooms(shooter);
            for(Color room: rooms)
                squares.addAll(map.getRoomSquares(room));
        }
        return squares;
    }

    /**
     *
     * @return the number of targets that shooter has to select
     */
    @Override
    public int getNumTargetsToSelect() {
        return 0;
    }

    /**
     *
     * @return the number of squares that shooter has to select
     */
    @Override
    public int getNumSquaresToSelect() {
        if(!room)
            return 1;
        else
            return MAX_ROOM_SQUARES;
    }
}
