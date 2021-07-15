package it.polimi.ingsw.model;

import it.polimi.ingsw.client.user_interfaces.model_representation.MapRepresentation;
import it.polimi.ingsw.client.user_interfaces.model_representation.WeaponInfo;
import it.polimi.ingsw.model.cards.AmmunitionCard;
import it.polimi.ingsw.model.cards.Stack;
import it.polimi.ingsw.model.cards.WeaponCard;
import it.polimi.ingsw.utils.XMLHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Map class represents the Game board map.
 * The map has a type, can be 1 to 4 and is composed by some squares.
 */

public class Map {

    private Square[][] grid;
    private final int type;

    //Defines.
    private static final int MAP_ROWS = 3;
    private static final int MAP_COLUMNS = 4;
    private static final int WEAPON_SIZE = 3;

    /**
     * Costructor, takes a grid[][] from the XML builder
     * @param type is map type.
     */
    public Map(int type) {
        this.type = type;
        XMLHelper xmlHelper=new XMLHelper();
        this.grid = xmlHelper.loadMap(type);
    }

    /**
     * @return yellow point of Respawn. Expected Square[2][3] in every map type
     */
    public Square getRespawnYellow() {
        return this.grid[2][3];
    }
    /**
     * @return blue point of Respawn. Expected Square[0][2] in every map type
     */
    public Square getRespawnBlue() {
        return this.grid[0][2];
    }
    /**
     * @return red point of Respawn. Expected Square[0][1] in every map type
     */
    public Square getRespawnRed() {
        return this.grid[1][0];
    }

    /***
     *
     * @param color of the raspwawn point choosen.
     * @return the corrisponding respawn point.
     */
    public Square getRespawnPoint(Color color) {
        if(color == Color.RED)
            getRespawnRed();
        else if(color == Color.BLUE)
            getRespawnBlue();
        else if(color == Color.YELLOW)
            getRespawnYellow();
        return null;
    }

    /**
     * @return the Square with these coordinates
     * @param col is square column in map
     * @param row is square row in map
     */
    public Square getSquare(int row, int col) {
       return grid[row][col];
    }

    /**
     * Is used to Refill map with ammunition card and weapon card from their stacks.
     * @param ammoStack is the stack of ammunitions card
     * @param weaponsStack is the stack of weapons card
     */
    public void refill(Stack<AmmunitionCard> ammoStack, Stack<WeaponCard> weaponsStack) {
        for(int r = 0; r< MAP_ROWS; r++) {
            for (int c = 0; c < MAP_COLUMNS; c++) {
                if(grid[r][c] != null) {
                    Square square = grid[r][c];
                    if (!square.isRespawnPoint()) {
                        if (square.getAmmos() == null)
                            square.setAmmos((AmmunitionCard) ammoStack.pickCard());
                    } else {
                        while(square.getWeaponsSize() != WEAPON_SIZE && !weaponsStack.getCards().isEmpty())
                                square.addWeapon((WeaponCard) weaponsStack.pickCard());
                    }
                }
            }
        }
    }

    /**
     *
     * @param roomColor is used to get every player in the room
     * @return a list with players in the room.
     */
    public List<Player> getPlayerInRoom(Color roomColor) {
        List<Player> players = new ArrayList<>();
        for(int r = 0; r< MAP_ROWS; r++)
            for(int c = 0; c< MAP_COLUMNS; c++)
                if(grid[r][c] != null && grid[r][c].getColor() == roomColor)
                    players.addAll(grid[r][c].getPlayers());
        return players;
    }

    /**
     * @param color is the color of the room
     * @return a list of squares that compose the room
     */
    public List<Square> getRoomSquares(Color color) {
        List<Square> squares = new ArrayList<>();
        for(int r = 0; r< MAP_ROWS; r++)
            for(int c = 0; c< MAP_COLUMNS; c++)
                if(grid[r][c] != null && grid[r][c].getColor() == color)
                    squares.add(grid[r][c]);
        return squares;
    }

    /**
     * Moves a player in a new positiion
     * @param player is the player that has to be moved
     * @param newPosition is the new position
     */
    public void movePlayer(Player player, Square newPosition) {
        Square oldPosition = this.getPlayerPosition(player);
        if(oldPosition != null)
            oldPosition.removePlayer(player);
        newPosition.addPlayer(player);
    }

    /**
     * Respawns a player in a respawn point
     * @param player is the player that has to be respawned
     * @param respawnColor is the color of the spawn point
     */
    public void respawnPlayer(Player player, Color respawnColor) {
        if(respawnColor == Color.BLUE)
            getRespawnBlue().addPlayer(player);
        else if(respawnColor == Color.RED)
            getRespawnRed().addPlayer(player);
        else if(respawnColor == Color.YELLOW)
            getRespawnYellow().addPlayer(player);
    }

    /**
     * remove from map a dead player
     * @param dead is the player that is dead
     */
    public void removeDeadPlayer(Player dead) {
        Square position = this.getPlayerPosition(dead);
        if(position != null)
            position.removePlayer(dead);
    }

    /**
     *
     * @param player is the player that you want to know position
     * @return player's position
     */
    public Square getPlayerPosition(Player player) {
        for(int r = 0; r< MAP_ROWS; r++)
            for(int c = 0; c< MAP_COLUMNS; c++)
                if(grid[r][c] != null && grid[r][c].containsPlayer(player)) return grid[r][c];
        return  null;
    }

    /**
     *
     * @param player that is want to know the color
     * @return the color of the room where player is.
     */
    public Color getPlayerRoomColor(Player player) {
        for(int r = 0; r< MAP_ROWS; r++)
            for(int c = 0; c< MAP_COLUMNS; c++)
                if(grid[r][c] != null && grid[r][c].containsPlayer(player)) return grid[r][c].getColor();
        return  null;
    }

    /**
     * Checks that there are no walls between a target and a shooter. returns true if there are NO WALLS, else false.
     * @param shooter is the shooter
     * @param target is the target
     * @return TRUE if there are no walls, else false
     */
    public boolean checkWalls(Player shooter, Player target) {
        Square shPos = this.getPlayerPosition(shooter);
        Square tarPos = this.getPlayerPosition(target);
        return checkWalls(shPos, tarPos);
    }

    /**
     * Checks that there are no walls between two positions.
     * @param shPos is the shooter position
     * @param tarPos is the target position
     * @return true if there are no walls.
     */
    public boolean checkWalls(Square shPos, Square tarPos) {
        if(shPos.getRow() == tarPos.getRow()) { //sono sulla stessa linea
            if(shPos.getColumn() < tarPos.getColumn()){
                Square tmp = shPos;
                while(tmp != tarPos) {
                    if(!tmp.hasRight()) return false;
                    tmp = grid[tmp.getRow()][tmp.getColumn()+1];
                }
                return true;
            }else{
                Square tmp = shPos;
                while(tmp != tarPos) {
                    if(!tmp.hasLeft()) return false;
                    tmp = grid[tmp.getRow()][tmp.getColumn()-1];
                }
                return true;
            }
        }else if(shPos.getColumn() == tarPos.getColumn()) {
            if(shPos.getRow() < tarPos.getRow()) {
                Square tmp = shPos;
                while(tmp != tarPos) {
                    if(!tmp.hasBottom()) return false;
                    tmp = grid[tmp.getRow()+1][tmp.getColumn()];
                }
                return true;
            }else{
                Square tmp = shPos;
                while(tmp != tarPos) {
                    if(!tmp.hasTop()) return false;
                    tmp = grid[tmp.getRow()-1][tmp.getColumn()];
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if every target is aligned with the shooter between a min range and a max one.
     * @param shooter is the shooter
     * @param targets are the targets
     * @param rangeMin is the minimum range
     * @param rangeMax is the maximum range
     * @return true if all of them are aligned.
     */
    public boolean checkTheLine(Player shooter, List<Player> targets, int rangeMin, int rangeMax) {
        int rowShooter = getPlayerPosition(shooter).getRow();
        int colShooter = getPlayerPosition(shooter).getColumn();
        boolean top = true, bottom = true, left = true, right = true;
        int distance = 0;
        for(Player target: targets) {
            int rowTarget = getPlayerPosition(target).getRow();
            int colTarget = getPlayerPosition(target).getColumn();
            if ((rowShooter != rowTarget) || (colShooter != colTarget)) {
                if (colTarget != colShooter) { //se non sono sulla stessa colonna
                    top = bottom = false;
                    if (rowTarget != rowShooter) return false; //devono essere sulla stessa riga
                    if (colTarget < colShooter) right = false; //se lo shooter è a destra del target
                    if (colTarget > colShooter) left = false; //se lo shooter è a sinistra del target
                } else { //se sono sulla stessa colonna
                    right = left = false;
                    if (rowTarget < rowShooter) bottom = false; //se lo shooter è sotto il target
                    if (rowTarget > rowShooter) top = false; //se lo shooter è sopra il target
                }
            }
            if(!top && !bottom && !right && !left) return false;

            /* Controlla che siano tutti a una distanza minore del range massimo e maggiore del range minimo */
            if(getPlayersDistance(shooter, target) > distance)
                distance = getPlayersDistance(shooter, target);
            if(distance > rangeMax || distance < rangeMin) return false;
        }
        return true;
    }
    /**
     * Is same as checkTheLine but with positions.
     * Checks if every target is aligned with the shooter between a min range and a max one.
     * @param shPos is the shooter
     * @param targetsPos are the targets
     * @param rangeMin is the minimum range
     * @param rangeMax is the maximum range
     * @return true if all of them are aligned.
     */
    public boolean checkTheLine(Square shPos, List<Square> targetsPos, int rangeMin, int rangeMax) {
        int rowShooter = shPos.getRow();
        int colShooter = shPos.getColumn();
        boolean top = true, bottom = true, left = true, right = true;
        int distance = 0;

        for(Square target: targetsPos){
            int rowTarget = target.getRow();
            int colTarget = target.getColumn();
            if ((rowShooter != rowTarget) || (colShooter != colTarget)) {
                if (colTarget != colShooter) { //se non sono sulla stessa colonna
                    top = bottom = false;
                    if (rowTarget != rowShooter) return false; //devono essere sulla stessa riga
                    if (colTarget < colShooter) right = false; //se lo shooter è a destra del target
                    if (colTarget > colShooter) left = false; //se lo shooter è a sinistra del target
                } else { //se sono sulla stessa colonna
                    right = left = false;
                    if (rowTarget < rowShooter) bottom = false; //se lo shooter è sotto il target
                    if (rowTarget > rowShooter) top = false; //se lo shooter è sopra il target
                }
            }
            if(!top && !bottom && !right && !left) return false;

            /* Controlla che siano tutti a una distanza minore del range massimo e maggiore del range minimo */
            if(getSquareDistance(shPos, target) > distance)
                distance = getSquareDistance(shPos, target);
            if(distance > rangeMax || distance < rangeMin) return false;
        }
        return true;
    }

    /**
     * @return a list of colors of the rooms seen by a player
     * @param watcher is the player
     */
    public List<Color> getVisibleRoom(Player watcher) {
        Square position = this.getPlayerPosition(watcher);
        return getVisibleRoom(position);
    }

    /**
     *@return a list of colors of the rooms seen by a certain position
     * @param position is the position analized
     *
     */
    public List<Color> getVisibleRoom(Square position) {
        List<Color> colors = new ArrayList<>();
        colors.add(position.getColor());
        if(position.hasTop() && !colors.contains(grid[position.getRow()-1][position.getColumn()].getColor()))
            colors.add(grid[position.getRow()-1][position.getColumn()].getColor());
        if(position.hasRight() && !colors.contains(grid[position.getRow()][position.getColumn()+1].getColor()))
            colors.add(grid[position.getRow()][position.getColumn()+1].getColor());
        if(position.hasLeft() && !colors.contains(grid[position.getRow()][position.getColumn()-1].getColor()))
            colors.add(grid[position.getRow()][position.getColumn()-1].getColor());
        if(position.hasBottom() && !colors.contains(grid[position.getRow()+1][position.getColumn()].getColor()))
            colors.add(grid[position.getRow()+1][position.getColumn()].getColor());
        return colors;
    }

    /**
     *
     * @param watcher is the player
     * @param visible is true if players wanted are visible from the watcher
     * @return a list of visible or not visible from the watcher
     */
    public List<Player> getOtherPlayers(Player watcher, boolean visible) {
        List<Player> others = new ArrayList<>();
        List<Color> colors = this.getVisibleRoom(watcher);
        for(int r = 0; r< MAP_ROWS; r++)
            for(int c = 0; c< MAP_COLUMNS; c++)
                if(grid[r][c] != null)
                    if((visible && colors.contains(grid[r][c].getColor())) || (!visible && !colors.contains(grid[r][c].getColor())))
                        others.addAll(grid[r][c].getPlayers());
        if(others.contains(watcher))
            others.remove(watcher);
        return others;
    }

    public List<Square> getOtherSquare(Square position, boolean visible) {
        List<Square> others = new ArrayList<>();
        List<Color> colors = this.getVisibleRoom(position);
        for(int r = 0; r< MAP_ROWS; r++)
            for(int c = 0; c< MAP_COLUMNS; c++)
                if(grid[r][c] != null)
                    if((visible && colors.contains(grid[r][c].getColor())) || (!visible && !colors.contains(grid[r][c].getColor())))
                        others.add(grid[r][c]);
        return others;
    }

    public List<Color> getNearRooms(Player player) {
        List<Color> colors = this.getVisibleRoom(player);
        colors.remove(this.getPlayerPosition(player).getColor());
        return colors;
    }

    /**
     *
     * @param square is the analyzed one
     * @return a list of squares near the first one
     */
    public List<Square> getNearSquares(Square square) {
        List<Square> squares = new ArrayList<>();
        int row = square.getRow();
        int col = square.getColumn();
        if(square.hasTop()) squares.add(grid[row-1][col]);
        if(square.hasBottom()) squares.add(grid[row+1][col]);
        if(square.hasRight()) squares.add(grid[row][col+1]);
        if(square.hasLeft()) squares.add(grid[row][col-1]);
        return squares;
    }

    /**
     * @param watcher is the player that is watching
     * @param watched is the watched player
     * @return true if the second player is watched by the first wan
     */
    public boolean isVisible(Player watcher, Player watched) {
        if(watcher == null || watched == null) return false;
        List<Player> watcheds = this.getOtherPlayers(watcher, true);
        return watcheds.contains(watched); //watcheds can't be null
    }

    public boolean isVisible(Square s1, Square s2) {
        if(s1 == null || s2 == null) return false;
        List<Square> watcheds = this.getOtherSquare(s1, true);
        return watcheds.contains(s2); //watcheds can't be null
    }

    /**
     * Calculates min distance from two player
     * @param p1 is player 1
     * @param p2 is player 2
     * @return min distance from players
     */
    public int getPlayersDistance(Player p1, Player p2) {
        Square start = this.getPlayerPosition(p1);
        Square arrive = this.getPlayerPosition(p2);
        return distance(start, arrive, new ArrayList<Square>(), 100, -1);
    }

    /**
     * using @distance calculate the min distance from
     * @param s1 the first square
     * @param s2 the second one
     * @return the min distance
     */
    public int getSquareDistance(Square s1, Square s2) {
        return distance(s1, s2, new ArrayList<Square>(), 100, -1);
    }

    /**
     * Calculates the distance from two squares using an BFS recursive algorithm.
     * @param actual is the starting square
     * @param arrive is the arriving square
     * @param visitated is a list of visitated squares (for recursion)
     * @param minDistance is the min distance (for recursion)
     * @param actDistance is the actual distance (for recursion)
     * @return the distance from two squares
     */
    private int distance(Square actual, Square arrive, List<Square> visitated, int minDistance, int actDistance) {
        actDistance++;
        if(visitated.contains(actual) || actDistance > minDistance) return minDistance;
        if(actual == arrive) return actDistance;
        visitated.add(actual);
        if(actual.hasTop()) {
            List<Square> copyList = new ArrayList<>();
            copyList.addAll(visitated);
            int dis = distance(grid[actual.getRow()-1][actual.getColumn()], arrive, copyList, minDistance, actDistance);
            if(dis<minDistance) minDistance = dis;
        }
        if(actual.hasBottom()) {
            List<Square> copyList = new ArrayList<>();
            copyList.addAll(visitated);
            int dis = distance(grid[actual.getRow()+1][actual.getColumn()], arrive, copyList, minDistance, actDistance);
            if(dis<minDistance) minDistance = dis;
        }
        if(actual.hasRight()) {
            List<Square> copyList = new ArrayList<>();
            copyList.addAll(visitated);
            int dis = distance(grid[actual.getRow()][actual.getColumn()+1], arrive, copyList, minDistance, actDistance);
            if(dis<minDistance) minDistance = dis;
        }
        if(actual.hasLeft()) {
            List<Square> copyList = new ArrayList<>();
            copyList.addAll(visitated);
            int dis = distance(grid[actual.getRow()][actual.getColumn()-1], arrive, copyList, minDistance, actDistance);
            if(dis<minDistance) minDistance = dis;
        }
        return minDistance;
    }

    /**
     * Returns a list of possible squares far max steps from a player
     * @param player is the player
     * @param maxSteps is the max distance
     * @return a list o possible squares.
     */
    public List<Square> getPossibleMovementSquares(Player player, int maxSteps) {
        List<Square> recoilSquares = new ArrayList<>();
        Square pos = this.getPlayerPosition(player);
        if(pos != null) {
           recoilSquares.add(pos);
           Square tmp = pos;
           if(pos.hasTop()) {
               for (int i = 1; i <= maxSteps && tmp.hasTop(); i++) {
                   tmp = this.getSquare(pos.getRow() - i, pos.getColumn());
                   recoilSquares.add(tmp);
               }
           }
           if(pos.hasBottom()) {
               tmp = pos;
               for (int i = 1; i <= maxSteps && tmp.hasBottom(); i++) {
                   tmp = this.getSquare(pos.getRow() + i, pos.getColumn());
                   recoilSquares.add(tmp);
               }
           }
           if(pos.hasLeft()) {
               tmp = pos;
               for (int i = 1; i <= maxSteps && tmp.hasLeft(); i++) {
                   tmp = this.getSquare(pos.getRow(), pos.getColumn() - i);
                   recoilSquares.add(tmp);
               }
           }
           if(pos.hasRight()) {
               tmp = pos;
               for (int i = 1; i <= maxSteps && tmp.hasRight(); i++) {
                   tmp = this.getSquare(pos.getRow(), pos.getColumn() + i);
                   recoilSquares.add(tmp);
               }
           }
        }
        return recoilSquares;
    }

    private boolean areInSameSquare(List<Player> players) {
        if(players == null || players.isEmpty()) return false;
        Square position = getPlayerPosition(players.get(0));
        for(Player p: players)
            if(position != getPlayerPosition(p)) return false;
        return true;
    }

    private boolean areInTheSameRoom(List<Player> players) {
        if(players == null || players.isEmpty()) return false;
        Color roomCol = getPlayerRoomColor(players.get(0));
        for(Player p: players)
            if(roomCol != getPlayerRoomColor(p)) return false;
        return true;
    }

    /**
     *
     * @return player that are on map in a certain time.
     */
    public List<Player> getPlayersOnMap() {
        List<Player> players = new ArrayList<>();
        for(Square square: getEverySquares())
            if(square.getPlayers() != null && !square.getPlayers().isEmpty())
                players.addAll(square.getPlayers());
        return players;
    }

    /**
     *
     * @return map type.
     */
    public int getType() {
        return type;
    }

    /**
     * @return every square from map which is not null.
     *
     */
    public List<Square> getEverySquares() {
        List<Square> squares = new ArrayList<>();
        for(int r=0; r<MAP_ROWS; r++)
            for(int c=0; c<MAP_COLUMNS; c++)
                if(grid[r][c] != null)
                    squares.add(grid[r][c]);
        return squares;
    }

    /**
     * @param position is the position that we want to get square
     * @return a square from a position
     */
    public Square getSquare(Position position) {
        for(Square square: this.getEverySquares())
            if(square.getPosition().equals(position))
                return square;
        return null;
    }

    /**
     * Creates a map representation of the map
     * @return a Map Representation.
     */
    public MapRepresentation getMapRepresentation() {
        MapRepresentation info = new MapRepresentation();
        info.setMapType(type);
        info.setAmmosInPosition(getAmmoInPositions());
        List<WeaponInfo> blueWeapons = this.getRespawnBlue().getWeapons().stream().map(WeaponCard::getWeaponInfo).collect(Collectors.toList());
        List<WeaponInfo> redWeapons = this.getRespawnRed().getWeapons().stream().map(WeaponCard::getWeaponInfo).collect(Collectors.toList());;
        List<WeaponInfo> yelWeapons = this.getRespawnYellow().getWeapons().stream().map(WeaponCard::getWeaponInfo).collect(Collectors.toList());;
        info.setRedWeapons(redWeapons);
        info.setBlueWeapons(blueWeapons);
        info.setYellowWeapons(yelWeapons);
        return info;
    }

    public java.util.Map<Position, AmmunitionCard> getAmmoInPositions() {
        java.util.Map<Position, AmmunitionCard> ammoInPos = new HashMap<>();
        for(Square s: this.getEverySquares()) {
            if(!s.isRespawnPoint())
                ammoInPos.put(s.getPosition(), s.getAmmos());
        }
        return ammoInPos;
    }

    /**
     *
     * @param position is the interested position
     * @param maxDistance is the the distance
     * @return a list of squares which are further away than a certain distance from a certain position
     */
    public List<Square> getSquareAtMaxDistance(Square position, int maxDistance) {
        return getEverySquares().stream().filter(s -> getSquareDistance(s, position) <= maxDistance).collect(Collectors.toList());
    }

}