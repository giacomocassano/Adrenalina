package it.polimi.ingsw.client.user_interfaces.cli;

import it.polimi.ingsw.client.user_interfaces.model_representation.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.AmmunitionCard;
import it.polimi.ingsw.utils.ANSIColors;
import it.polimi.ingsw.utils.WriterHelper;

import java.util.List;
import java.util.Map;

/**
 * Class used to print the show and print the GameBoard in the
 * command line interface.
 *
 */
public class GameBoard {

    private static final int WINDOW_LENGTH = 158;
    private static final int WINDOW_HEIGHT = 53;
    private static final int ENEMY_PLAYER_LENGTH = 48;
    private static final int ENEMY_PLAYER_HEIGHT = 10;
    private static final int SQUARE_HEIGHT = 9;
    private static final int SQUARE_LENGTH = 18;
    private static final int MAP_HEIGHT = SQUARE_HEIGHT * 3 + 1;
    private static final int MAP_LENGTH = SQUARE_LENGTH * 4 + 1;
    private static final int MY_PLAYER_LENGTH = 55;
    private static final int MY_PLAYER_HEIGHT = 9;
    private static final int FIRST_ENEMY_X = 1;
    private static final int FIRST_ENEMY_Y = 110;
    private static final int WEAPONS_LENGTH =22;
    private static final int X_FOR_MAP=14;
    private static final int Y_FOR_MAP=23;
    private static final int KILL_SHOT_TRACK_X =0;
    private static final int KILL_SHOT_TRACK_Y =75;
    private static final int SCOREBOARD_LEN=20;
    private static final int X_RED_WEAPONS=0;
    private static final int Y_RED_WEAPONS=WEAPONS_LENGTH;
    private static final int X_BLUE_WEAPONS=0;
    private static final int Y_BLUE_WEAPONS=0;
    private static final int X_YELLOW_WEAPONS=0;
    private static final int Y_YELLOW_WEAPONS=WEAPONS_LENGTH*2;
    private static final int SCOREBOARD_X=14;
    private static final int SCOREBOARD_Y=0;
    private static int X_MY_WEAPONS =44;
    private static int Y_MY_WEAPONS =30;
    private static int MY_EMPOWER_X =44;
    private static int MY_EMPOWER_Y =0;
    private static int MY_PLAYER_X =43;
    private static int MY_PLAYER_Y =103;
    private static final int MY_WEAPONS_LENGTH=55;
    private static final int MY_EMPOWERS_LENGTH=18;
    private static final String KILL_SHOT_TRACK ="KILLSHOT TRACK:";
    private static final String REMAINING_EMPOWERS="REMAINING EMPOWERS:";
    private static final String REMAINING_WEAPONS="REMAINING WEAPONS:";
    private static final String REMAINING_AMMOS="REMAINING AMMOS:";
    private static final String TWO_DAMAGES_MESSAGE="can move x2 b4 grab";
    private static final String FIVE_DAMAGES_MESSAGE="can move x2 b4 grab,x1 b4 shoot!";
    private static final String MY_EMPOWERS="MY EMPOWERS:";
    private static final String SCOREBOARD="SCOREBOARD";
    private static final String DAMAGES = "DAMAGES:";
    private static final String MARKS = "MARKS:";
    private static final String AMMO_CUBE_RED = "RED CUBES:";
    private static final String AMMO_CUBE_YELLOW = "YELLOW CUBES:";
    private static final String AMMO_CUBE_BLUE = "BLUE CUBES:";
    private static final String EMPOWERS = "NUM.EMPOWERS:";
    private static final String DEATHS = "NUM.DEATHS:";
    private static final String LIFE_POINTS = "LIFE POINTS:";
    private static final String UNLOADED_WEAPONS = "NUM WEAPONS:";
    private static final String SCORES="SCORES:";
    private static final String SPAWN="SPAWN";
    private static final String BLUE_WEAPONS="BLUE WEAPONS:";
    private static final String RED_WEAPONS="RED WEAPONS:";
    private static final String YELLOW_WEAPONS="YELLOW WEAPONS:";
    private static final String COST="COST:";
    private static final String MY_WEAPONS="MY WEAPONS:";
    private static final String RELOAD_COST="COSTO DI RICARICA:";
    private static final String MY_LIFE_POINTS="MY LIFE POINTS:";
    private static final String MY_DAMAGES="MY DAMAGES:";
    private static final String MY_MARKS="MY MARKS:";
    private static final String MY_DEATHS="MY DEATHS:";
    private static final String TIPS="TIPS:";
    private static final String DAMAGED_MESSAGE_1="1)You can move twice before grab.";
    private static final String DAMAGED_MESSAGE_2="2)You can move once before shoot.";

    private static char cube ='X';
    private static char heart ='O';
    private static char mark ='O';

    private static final char LONG_MAP_PIECE='|';
    private static final char LEFT_UP_ANGLE_MAP_PIECE='+';
    private static final char LEFT_DOWN_ANGLE_MAP_PIECE='+';
    private static final char LEFT_WALL_MAP_PIECE='+';
    private static final char RIGHT_WALL_MAP_PIECE='+';
    private static final char LONG_MAP_PIECE_HORIZZONTAL='-';
    private static final char CROSS_MAP_PIECE='+';
    private static final char MAP_PIECE_BORDER_UP='+';
    private static final char MAP_PIECE_BORDER_DOWN='+';
    private static final char RIGHT_ANGLE_MAP_PIECE= '+';
    private static final char RIGHT_DOWN_ANGLE_MAP_PIECE= '+';



    private static char[][] board;


    /**
     * Default constructor
     */
    public GameBoard(){
        board=new char[WINDOW_HEIGHT][WINDOW_LENGTH];
        setAllBlank();

    }

    /**
     * This method resizes the gameboard if num Players is less then 3
     * @param enemies are enemies
     */
    private static void shiftPositions(List<PlayerRepresentation> enemies){
        if(enemies.size()==2){
            MY_PLAYER_X = 24;
            MY_PLAYER_Y =FIRST_ENEMY_Y - 7 ;
            Y_MY_WEAPONS =FIRST_ENEMY_Y- 7;
            X_MY_WEAPONS = MY_PLAYER_X + 9;
            MY_EMPOWER_Y =FIRST_ENEMY_Y- 7;
            MY_EMPOWER_X = X_MY_WEAPONS + 7;
        }
    }

    /**
     * This method writes the layout of a respawn-point's weapon box. Is used in writeMapWeapons().
     * @param x is the x of the box in the window
     * @param y is the y of the box in the window
     * @param weapons is a List of a spawn point's weapons
     * @param name is the name of weapons
     */
    private static void writeWeaponLayout(int x,int y, List<WeaponInfo> weapons,String name) {
        if (weapons != null) {
            writeBoards(x, y, weapons.size() * 3 + 3, WEAPONS_LENGTH);
            for (int i = 0; i < name.length(); i++)
                board[x + 1][y + 1 + i] = name.charAt(i);
            writeWeaponStats(weapons, x, y);
        }
    }

    /**
     * This method writes every spawn point's weapon box.
     * @param bluweap are blue spawn point's weapons
     * @param redweap are red spawn point's weapons
     * @param yellowweap are yellow spawn point's weapons
     */
    private static void writeMapWeapons(List<WeaponInfo> bluweap,List<WeaponInfo> redweap,List<WeaponInfo> yellowweap){
        writeWeaponLayout(X_BLUE_WEAPONS,Y_BLUE_WEAPONS,bluweap,BLUE_WEAPONS);
        writeWeaponLayout(X_RED_WEAPONS,Y_RED_WEAPONS,redweap,RED_WEAPONS);
        writeWeaponLayout(X_YELLOW_WEAPONS,Y_YELLOW_WEAPONS,yellowweap,YELLOW_WEAPONS);
    }

    /**
     * This method fills a weapon box with weapon's stats.
     * @param weapons are the weapons in a respawn point.
     * @param x is the x of the weapon box
     * @param y is the y of the weapon box
     */
    private static void writeWeaponStats(List<WeaponInfo> weapons,int x, int y){
        int weaponX=3;
        int weaponY=1;
        for(WeaponInfo w: weapons){
            for(int i=0;i<w.getName().length();i++)
                board[x+weaponX][y+weaponY+i]=w.getName().charAt(i);
            for(int i=0;i<COST.length();i++)
                board[x+weaponX+1][y+weaponY+i]=COST.charAt(i);
            board[x+weaponX+1][y+weaponY+7]='R';
            board[x+weaponX+1][y+weaponY+8]=':';
            board[x+weaponX+1][y+weaponY+9]=String.valueOf(w.getBuyCost().getRed()).charAt(0);
            board[x+weaponX+1][y+weaponY+12]='B';
            board[x+weaponX+1][y+weaponY+13]=':';
            board[x+weaponX+1][y+weaponY+14]=String.valueOf(w.getBuyCost().getBlue()).charAt(0);
            board[x+weaponX+1][y+weaponY+17]='Y';
            board[x+weaponX+1][y+weaponY+18]=':';
            board[x+weaponX+1][y+weaponY+19]=String.valueOf(w.getBuyCost().getYellow()).charAt(0);
            weaponX=weaponX+3;
        }
    }

    /**
     * This method creates a rectangle with characters.
     * @param x x is the x in the window where the box has to be written
     * @param y y is the y in the window where the box has to be written
     * @param height is the height of the box.
     * @param length is le length of the box
     */
    private static void writeBoards(int x, int y, int height, int length) {

        board[x][y] =LEFT_UP_ANGLE_MAP_PIECE;
        for (int c = 1; c < length - 1; c++) {
            board[x][y + c] = LONG_MAP_PIECE_HORIZZONTAL;
        }
        board[x][y + length - 1] =  RIGHT_ANGLE_MAP_PIECE;
        for (int r = 1; r < height - 1; r++) {
            board[x + r][y] = LONG_MAP_PIECE;
            for (int c = 1; c < length - 1; c++) {
                board[x + r][y + c] = ' ';
            }
            board[x + r][y + length - 1] = LONG_MAP_PIECE;
        }
        board[x + height - 1][y] =LEFT_DOWN_ANGLE_MAP_PIECE ;
        for (int c = 1; c < length - 1; c++) {
            board[x + height - 1][y + c] = LONG_MAP_PIECE_HORIZZONTAL;
        }
        board[x + height - 1][y + length - 1] = RIGHT_DOWN_ANGLE_MAP_PIECE;

    }

    /**
     * This method creates the map.
     * @param type is map type
     */
    private static void writeMap(int type) {

        writeBoards(X_FOR_MAP, Y_FOR_MAP, MAP_HEIGHT, MAP_LENGTH);
        board[X_FOR_MAP + SQUARE_HEIGHT][Y_FOR_MAP] = LEFT_WALL_MAP_PIECE;
        board[X_FOR_MAP + SQUARE_HEIGHT * 2][Y_FOR_MAP] = LEFT_WALL_MAP_PIECE;
        for (int c = 1; c < MAP_LENGTH; c++) {
            board[X_FOR_MAP + SQUARE_HEIGHT][Y_FOR_MAP + c] = LONG_MAP_PIECE_HORIZZONTAL;
            board[X_FOR_MAP + SQUARE_HEIGHT * 2][Y_FOR_MAP + c] = LONG_MAP_PIECE_HORIZZONTAL;
        }
        board[X_FOR_MAP + SQUARE_HEIGHT][Y_FOR_MAP + MAP_LENGTH - 1] = RIGHT_WALL_MAP_PIECE;
        board[X_FOR_MAP + SQUARE_HEIGHT * 2][Y_FOR_MAP + MAP_LENGTH - 1] = RIGHT_WALL_MAP_PIECE;
        writeVerticalCells( 3);
        board[X_FOR_MAP + SQUARE_HEIGHT][Y_FOR_MAP + SQUARE_LENGTH] = CROSS_MAP_PIECE;
        board[X_FOR_MAP + SQUARE_HEIGHT][Y_FOR_MAP + SQUARE_LENGTH * 2] = CROSS_MAP_PIECE;
        board[X_FOR_MAP + SQUARE_HEIGHT][Y_FOR_MAP + SQUARE_LENGTH * 3] = CROSS_MAP_PIECE;
        board[X_FOR_MAP + SQUARE_HEIGHT * 2][Y_FOR_MAP + SQUARE_LENGTH] = CROSS_MAP_PIECE;
        board[X_FOR_MAP + SQUARE_HEIGHT * 2][Y_FOR_MAP + SQUARE_LENGTH * 2] = CROSS_MAP_PIECE;
        board[X_FOR_MAP + SQUARE_HEIGHT * 2][Y_FOR_MAP + SQUARE_LENGTH * 3] = CROSS_MAP_PIECE;
        mapOpenDoors();
        setMapBorders(type);
        setWalls(type);
        writeSpawns();

    }



    /**
     * Writes in the map where are spawn points.
     */
    private static void writeSpawns(){

        for(int i=0;i<SPAWN.length();i++)
            board[X_FOR_MAP+SQUARE_HEIGHT+2+i][Y_FOR_MAP+1]=SPAWN.charAt(i);
        for(int i=0;i<SPAWN.length();i++)
            board[X_FOR_MAP+2*SQUARE_HEIGHT+2+i][Y_FOR_MAP+MAP_LENGTH-2]=SPAWN.charAt(i);
        for(int i=0;i<SPAWN.length();i++)
            board[X_FOR_MAP+1][Y_FOR_MAP+SQUARE_LENGTH*2+3+i]=SPAWN.charAt(i);

    }

    /**
     * Writes vertical cells in the map.
     * @param numCells is number of columns.
     */
    private static void writeVerticalCells(int numCells) {

        board[X_FOR_MAP][Y_FOR_MAP + SQUARE_LENGTH] = MAP_PIECE_BORDER_UP;
        board[X_FOR_MAP][Y_FOR_MAP + SQUARE_LENGTH * 2] = MAP_PIECE_BORDER_UP;
        board[X_FOR_MAP][Y_FOR_MAP + SQUARE_LENGTH * 3] = MAP_PIECE_BORDER_UP;
        for (int r = 1; r < MAP_HEIGHT - 1; r++) {
            for (int j = 1; j <= numCells; j++)
                board[X_FOR_MAP + r][Y_FOR_MAP + SQUARE_LENGTH * j] = LONG_MAP_PIECE;
        }
        board[X_FOR_MAP + MAP_HEIGHT - 1][Y_FOR_MAP + SQUARE_LENGTH] = MAP_PIECE_BORDER_DOWN;
        board[X_FOR_MAP + MAP_HEIGHT - 1][Y_FOR_MAP + SQUARE_LENGTH * 2] = MAP_PIECE_BORDER_DOWN;
        board[X_FOR_MAP + MAP_HEIGHT - 1][Y_FOR_MAP + SQUARE_LENGTH * 3] = MAP_PIECE_BORDER_DOWN;

    }

    /**
     * This method is used during the creation of map with characters.
     * It deletes some characters to create doors between rooms.
     */
    private static void mapOpenDoors() {
        for (int i = SQUARE_LENGTH; i < MAP_LENGTH - 1; i = i + SQUARE_LENGTH) {
            for (int j = 3; j <= 6; j++) {
                board[X_FOR_MAP + j][Y_FOR_MAP + i] = ' ';
                board[X_FOR_MAP + j + SQUARE_HEIGHT][Y_FOR_MAP + i] = ' ';
                board[X_FOR_MAP + j + SQUARE_HEIGHT * 2][Y_FOR_MAP + i] = ' ';
            }
        }
        for (int i = SQUARE_HEIGHT; i < MAP_HEIGHT - 1; i = i + SQUARE_HEIGHT) {
            for (int j = 6; j <= 12; j++) {
                board[X_FOR_MAP + i][Y_FOR_MAP + j] = ' ';
                board[X_FOR_MAP + i][Y_FOR_MAP + j + SQUARE_LENGTH] = ' ';
                board[X_FOR_MAP + i][Y_FOR_MAP + j + SQUARE_LENGTH * 2] = ' ';
                board[X_FOR_MAP + i][Y_FOR_MAP + j + SQUARE_LENGTH * 3] = ' ';
            }
        }
    }


    /**
     * This creates or deletes borders of map. It is based on map type.
     * @param type is tha type of the map.
     */
    private static void setMapBorders(int type) {
        switch (type) {
            case 1:
                for (int c = SQUARE_LENGTH * 3 + 1; c < MAP_LENGTH - 1; c++)
                    board[X_FOR_MAP][Y_FOR_MAP + c] = ' ';
                for (int r = 0; r < SQUARE_HEIGHT; r++)
                    board[X_FOR_MAP + r][Y_FOR_MAP + MAP_LENGTH - 1] = ' ';
                board[X_FOR_MAP][Y_FOR_MAP + SQUARE_LENGTH * 3] =RIGHT_ANGLE_MAP_PIECE;
                board[X_FOR_MAP + SQUARE_HEIGHT][Y_FOR_MAP + MAP_LENGTH - 1] = RIGHT_ANGLE_MAP_PIECE;
                for (int r = SQUARE_HEIGHT * 2 + 1; r < MAP_HEIGHT - 1; r++)
                    board[X_FOR_MAP + r][Y_FOR_MAP] = ' ';
                for (int c = 0; c < SQUARE_LENGTH; c++)
                    board[MAP_HEIGHT + X_FOR_MAP - 1][Y_FOR_MAP + c] = ' ';
                board[X_FOR_MAP + SQUARE_HEIGHT * 2][Y_FOR_MAP] = LEFT_DOWN_ANGLE_MAP_PIECE;
                board[X_FOR_MAP + SQUARE_HEIGHT * 3][Y_FOR_MAP + SQUARE_LENGTH] =LEFT_DOWN_ANGLE_MAP_PIECE;

                for (int r = 3; r <= 6; r++)
                    board[X_FOR_MAP + r][Y_FOR_MAP + SQUARE_LENGTH * 3] = LONG_MAP_PIECE;
                for (int c = 1; c <= 7; c++)
                    board[X_FOR_MAP + SQUARE_HEIGHT][Y_FOR_MAP + SQUARE_LENGTH * 3 + 5 + c] = LONG_MAP_PIECE_HORIZZONTAL;
                for (int r = 3; r <= 6; r++)
                    board[X_FOR_MAP + r + SQUARE_HEIGHT * 2][Y_FOR_MAP + SQUARE_LENGTH] = LONG_MAP_PIECE;
                for (int c = 3 + 1; c < SQUARE_LENGTH - 3; c++)
                    board[X_FOR_MAP + SQUARE_HEIGHT * 2][Y_FOR_MAP + c] = LONG_MAP_PIECE_HORIZZONTAL;
                break;
            case 2:
                for (int c = SQUARE_LENGTH * 3 + 1; c < MAP_LENGTH - 1; c++)
                    board[X_FOR_MAP][Y_FOR_MAP + c] = ' ';
                for (int r = 0; r < SQUARE_HEIGHT; r++)
                    board[X_FOR_MAP + r][Y_FOR_MAP + MAP_LENGTH - 1] = ' ';
                board[X_FOR_MAP][Y_FOR_MAP + SQUARE_LENGTH * 3] = RIGHT_ANGLE_MAP_PIECE;
                board[X_FOR_MAP + SQUARE_HEIGHT][Y_FOR_MAP + MAP_LENGTH - 1] = RIGHT_ANGLE_MAP_PIECE;
                for (int r = 3; r <= 6; r++)
                    board[X_FOR_MAP + r][Y_FOR_MAP + SQUARE_LENGTH * 3] = LONG_MAP_PIECE;
                for (int c = SQUARE_LENGTH * 3 + 1; c < SQUARE_LENGTH * 4 - 3; c++)
                    board[X_FOR_MAP + SQUARE_HEIGHT][Y_FOR_MAP + c] = LONG_MAP_PIECE_HORIZZONTAL;
                break;
            case 3:
                for (int r = SQUARE_HEIGHT * 2 + 1; r < MAP_HEIGHT - 1; r++)
                    board[X_FOR_MAP + r][Y_FOR_MAP] = ' ';
                for (int c = 0; c < SQUARE_LENGTH; c++)
                    board[MAP_HEIGHT + X_FOR_MAP - 1][Y_FOR_MAP + c] = ' ';
                board[X_FOR_MAP + SQUARE_HEIGHT * 2][Y_FOR_MAP] = LEFT_DOWN_ANGLE_MAP_PIECE;
                board[X_FOR_MAP + SQUARE_HEIGHT * 3][Y_FOR_MAP + SQUARE_LENGTH] = LEFT_DOWN_ANGLE_MAP_PIECE;
                for (int r = 3; r <= 6; r++)
                    board[X_FOR_MAP + r + SQUARE_HEIGHT * 2][Y_FOR_MAP + SQUARE_LENGTH] = LONG_MAP_PIECE;
                for (int c = 3 + 1; c < SQUARE_LENGTH - 3; c++)
                    board[X_FOR_MAP + SQUARE_HEIGHT * 2][Y_FOR_MAP + c] = LONG_MAP_PIECE_HORIZZONTAL;
                break;
            case 4:
                break;
            default:
                break;
        }

    }

    /**
     * This create walls is the map.
     * @param type is map type.
     */
    private static void setWalls(int type) {
        switch (type) {
            case 1:
                for (int c = 0; c <= 10; c++)
                    board[X_FOR_MAP + SQUARE_HEIGHT][Y_FOR_MAP + SQUARE_LENGTH + 2 + c] = LONG_MAP_PIECE_HORIZZONTAL;
                for (int c = 0; c <= 10; c++)
                    board[X_FOR_MAP + SQUARE_HEIGHT * 2][Y_FOR_MAP + SQUARE_LENGTH * 2 + 2 + c] = LONG_MAP_PIECE_HORIZZONTAL;
                break;
            case 2:
                for (int r = 0; r <= 5; r++)
                    board[2 + X_FOR_MAP + SQUARE_HEIGHT + r][Y_FOR_MAP + SQUARE_LENGTH] = LONG_MAP_PIECE;
                for (int c = 0; c <= 10; c++)
                    board[X_FOR_MAP + SQUARE_HEIGHT * 2][Y_FOR_MAP + SQUARE_LENGTH * 2 + 2 + c] = LONG_MAP_PIECE_HORIZZONTAL;
                break;
            case 3:
                for (int c = 0; c <= 10; c++)
                    board[X_FOR_MAP + SQUARE_HEIGHT][Y_FOR_MAP + SQUARE_LENGTH + 2 + c] = LONG_MAP_PIECE_HORIZZONTAL;
                for (int r = 0; r <= 5; r++)
                    board[2 + X_FOR_MAP + SQUARE_HEIGHT + r][Y_FOR_MAP + SQUARE_LENGTH * 2] = LONG_MAP_PIECE;
                break;
            case 4:
                for (int r = 0; r <= 5; r++)
                    board[2 + X_FOR_MAP + SQUARE_HEIGHT + r][Y_FOR_MAP + SQUARE_LENGTH] =LONG_MAP_PIECE;
                for (int r = 0; r <= 5; r++)
                    board[2 + X_FOR_MAP + SQUARE_HEIGHT + r][Y_FOR_MAP + SQUARE_LENGTH * 2] =LONG_MAP_PIECE;
                break;
            default:
                break;
        }
    }

    /**
     * This method writes an enemy player's layout in the window.
     * @param num is the number of the player that has to be written.
     * @param x x is the x in the window where the box has to be written
     * @param y y is the y in the window where the box has to be written
     */
    private static void writeLayoutEnemyPlayer(int num, int x, int y) {
        for (int i = 0; i < ENEMY_PLAYER_LENGTH; i++)
            board[x][y + i] = LONG_MAP_PIECE_HORIZZONTAL;
        for (int i = 0; i < ENEMY_PLAYER_LENGTH; i++)
            board[x + ENEMY_PLAYER_HEIGHT][y + i] = LONG_MAP_PIECE_HORIZZONTAL;
        for (int i = 1; i < ENEMY_PLAYER_HEIGHT; i++)
            board[x + i][y] = LONG_MAP_PIECE;
        for (int i = 0; i < LIFE_POINTS.length(); i++)
            board[x + 2][1 + y + i] = LIFE_POINTS.charAt(i);
        for (int i = 0; i < DAMAGES.length(); i++)
            board[x + 6][1 + y + i + 25] = DAMAGES.charAt(i);
        for (int i = 0; i < MARKS.length(); i++)
            board[x + 6][1 + y + i] = MARKS.charAt(i);
        for (int i = 0; i < EMPOWERS.length(); i++)
            board[x + 3][1 + y + i ] = EMPOWERS.charAt(i);
        for (int i = 0; i < UNLOADED_WEAPONS.length(); i++)
            board[x + 8][1 + y + i] = UNLOADED_WEAPONS.charAt(i);
        for (int i = 0; i < DEATHS.length(); i++)
            board[x + 4][1 + y + i ] = DEATHS.charAt(i);
        for (int i = 0; i < AMMO_CUBE_RED.length(); i++)
            board[x + 2][1 + y + 30 + i] = AMMO_CUBE_RED.charAt(i);
        for (int i = 0; i < AMMO_CUBE_BLUE.length(); i++)
            board[x + 3][1 + y + 30 + i] = AMMO_CUBE_BLUE.charAt(i);
        for (int i = 0; i < AMMO_CUBE_YELLOW.length(); i++)
            board[x + 4][1 + y + 30 + i] = AMMO_CUBE_YELLOW.charAt(i);
        board[x + ENEMY_PLAYER_HEIGHT][y] = LEFT_DOWN_ANGLE_MAP_PIECE;
        if (num == 0)
            board[x][y] = LEFT_UP_ANGLE_MAP_PIECE;
        else
            board[x][y] = LEFT_WALL_MAP_PIECE;
    }

    /**
     *This method writes the Scoreboard and fills her with players stats.
     * @param x x is the x in the window where the box has to be written
     * @param y y is the y in the window where the box has to be written
     * @param players are enemy players.
     * @param myplayer is your player.
     */
    private static void writeScoreboard(int x, int y, List<PlayerRepresentation> players, MyPlayerRepresentation myplayer) {
        writeBoards(x, y, players.size() * 3 + 6, SCOREBOARD_LEN);
        for(int i =0;i<SCOREBOARD.length();i++)
            board[x+1][y+i+1]=SCOREBOARD.charAt(i);
        int z = 3;
        for (PlayerRepresentation player : players) {
            for (int i = 0; i < player.getName().length(); i++)
                board[x + z][i + y + 1] = player.getName().charAt(i);

            for (int i = 0; i < SCORES.length(); i++)
                board[x + z + 1][i + y + 1] = SCORES.charAt(i);
            board[x + z + 1][y + 8] = String.valueOf(player.getPoints()).charAt(0);
            if (player.getPoints() >= 10)
                board[x + z + 1][y + 9] = String.valueOf(player.getPoints()).charAt(1);
            if (player.getPoints() > 100)
                board[x + z + 1][y + 10] = String.valueOf(player.getPoints()).charAt(2);
            z=z+3;
        }
        for (int i = 0; i < myplayer.getName().length(); i++)
            board[x + z][i + y + 1] = myplayer.getName().charAt(i);
        for (int i = 0; i < SCORES.length(); i++)
            board[x + z + 1][i + y + 1] = SCORES.charAt(i);
        board[x + z + 1][y + 8] = String.valueOf(myplayer.getPoints()).charAt(0);
        if (myplayer.getPoints() >= 10)
            board[x + z + 1][y + 9] = String.valueOf(myplayer.getPoints()).charAt(1);
        if (myplayer.getPoints() > 100)
            board[x + z + 1][y + 10] = String.valueOf(myplayer.getPoints()).charAt(2);
    }

    /**
     * This method uses writeLayoutEnemyPlayer() for every enemy player.
     * @param players are enemy players
     */
    private static void writeEnemiesLayout(List<PlayerRepresentation> players) {
        int x = FIRST_ENEMY_X;
        int num = 0;
        for (PlayerRepresentation p : players) {
            writeLayoutEnemyPlayer(num, x, FIRST_ENEMY_Y);
            x = x + ENEMY_PLAYER_HEIGHT;
            num++;
        }
    }

    /**
     * This method fills enemy players layouts with their stats.
     * @param players are enemy players.
     */
    private static void writeEnemiesStats(List<PlayerRepresentation> players) {
        int x = FIRST_ENEMY_X;
        int y = FIRST_ENEMY_Y;
        for (PlayerRepresentation player : players) {
            updatePlayerInfo(x, y, player.getName(), player.getAmmoCubes().getRed(), player.getAmmoCubes().getYellow(),
                    player.getAmmoCubes().getBlue(),player.getMarks().size(), player.getDamages().size(), player.getNumEmpowers(),
                    player.getDeaths(), player.getNumWeapons(),player.getDeathPoints());
            updateEnemyPlayerWeapons(player.getUnloadedWeapons(),x,y);
            x = x + ENEMY_PLAYER_HEIGHT;
        }
    }

    /**
     * This updates an enemy weapons
     * @param weaponInfos are enemy weapons.
     * @param x x is the x in the window where the box has to be written
     * @param y y is the y in the window where the box has to be written
     */
    private static void updateEnemyPlayerWeapons(List<WeaponInfo> weaponInfos, int x, int y) {
        int c = 0;
        int length=0;
        for (WeaponInfo weaponInfo : weaponInfos) {
            if(weaponInfo.getName().length()>4)
                length=weaponInfo.getName().length()-4;
            else
                length=weaponInfo.getName().length();
            for (int i = 0; i < length; i++, c++)
                board[x + 9][y + 1 + c] = weaponInfo.getName().charAt(i);
            board[x + 9][y + 1 + c] = '-';
            length=0;
            c++;
        }
    }

    /**
     * This method clears the window. Set all char to blank.
     */
    private static void setAllBlank() {

        for (int r = 0; r < WINDOW_HEIGHT; r++)
            for (int c = 0; c < WINDOW_LENGTH; c++)
                board[r][c] = ' ';
    }

    /**
     * This method updates stats of a player
     * @param x is the x of player's layout in the window.
     * @param y is the y of player's layout in the window.
     * @param name is player's name.
     * @param redCubes is the number of player's red cubes.
     * @param yellowCubes is the number of player's yellow cubes.
     * @param blueCubes is the number of player's blue cubes.
     * @param marks is the number of player's marks.
     * @param damages is the number of player's damages.
     * @param numempowers is the number of player's empowers.
     * @param numdeaths is the number of player's deaths
     * @param numweapons is the number of player's weapons
     * @param points are player's points.
     */
    private static void updatePlayerInfo(int x, int y, String name, int redCubes, int yellowCubes,
                                         int blueCubes, int marks,int damages, int numempowers, int numdeaths, int numweapons,int points) {
        int s = 15;
        for (int i = 0; i < name.length(); i++) {
            board[x + 1][y + i + 1] = name.charAt(i);
            s = i;
        }
        s=s+3;
        for (int i = 0; i < redCubes; i++)
            board[x + 2][y + i + 40 + 1] = cube;
        for (int i = 0; i < blueCubes; i++)
            board[x + 3][y + i + 41 + 1] = cube;
        for (int i = 0; i < yellowCubes; i++)
            board[x + 4][y + i + 43 + 1] = cube;
        for (int i = 0; i < damages; i++)
            board[x + 7][y + i + 1 + 25] = heart;
        for (int i = 0; i < marks; i++)
            board[x + 7][y + i + 1] = mark;
        board[x + 2][y + 15] = String.valueOf(points).charAt(0);
        board[x + 3][y + 15] = String.valueOf(numempowers).charAt(0);
        board[x + 8][y + 15] = String.valueOf(numweapons).charAt(0);
        board[x + 4][y + 15] = String.valueOf(numdeaths).charAt(0);
        if (damages > 2) {
            int i;
            for (i = 0; i < TWO_DAMAGES_MESSAGE.length(); i++)
                board[x + 1][y + i + s] = TWO_DAMAGES_MESSAGE.charAt(i);
        }
        if (damages > 5)
            for (int i = 0; i < FIVE_DAMAGES_MESSAGE.length(); i++)
                board[x + 1][y + i + s] = FIVE_DAMAGES_MESSAGE.charAt(i);

    }

    /**
     * This method from a Model color returns an ANSICOlor.
     * @return the ANSIColor matched with the color.
     */
    private static ANSIColors colorToAnsi(Color color){
        switch (color){
            case YELLOW:return ANSIColors.YELLOW;
            case GREEN:return ANSIColors.GREEN;
            case PURPLE:return ANSIColors.MAGENTA;
            case BLUE:return ANSIColors.BLUE;
            case GREY:return ANSIColors.WHITE;
            case RED:return ANSIColors.RED;
            default: return null;
        }
    }
    /**
     * This method from a Model color returns an ANSICOlor.
     * @return the ANSIColorBold matched with the color.
     */
    private static ANSIColors colorToAnsiBold(Color color){
        switch (color){
            case YELLOW:return ANSIColors.YELLOW_BOLD;
            case GREEN:return ANSIColors.GREEN_BOLD;
            case PURPLE:return ANSIColors.MAGENTA_BOLD;
            case BLUE:return ANSIColors.BLUE_BOLD;
            case GREY:return ANSIColors.WHITE_BOLD;
            case RED: return ANSIColors.RED_BOLD;
            default: return null;
        }
    }

    /**
     * This is the main method, it takes the Game Reresentation and creates the window.
     * @param game is the game representation
     */
    public void startBoard(GameRepresentation game) {
        setAllBlank();
         shiftPositions(game.getOtherPlayerRapresentation());
        MapRepresentation map = game.getMapRepresentation();
        writeMap(map.getMapType());
        writeMapWeapons(map.getBlueWeapons(), map.getRedWeapons(), map.getYellowWeapons());
        writeAmmosInMap(map.getAmmosInPosition());
        writeKillShotTrack(game.getGameBoardRepresentation().getEmpowerStackSize(),game.getGameBoardRepresentation().getWeaponsStackSize(),game.getGameBoardRepresentation().getAmmoStackSize());
        writeEnemiesLayout(game.getOtherPlayerRapresentation());
        writeEnemiesStats(game.getOtherPlayerRapresentation());
        writeScoreboard(SCOREBOARD_X, SCOREBOARD_Y, game.getOtherPlayerRapresentation(),game.getMyPlayerRepresentation());
        writePersonalInfo();
        MyPlayerRepresentation myPlayer=game.getMyPlayerRepresentation();
        writeMapPlayers(game.getOtherPlayerRapresentation(),game.getMyPlayerRepresentation());
        updatePersonalInfo(myPlayer.getAmmoCubes().getRed(),myPlayer.getAmmoCubes().getBlue(),
                myPlayer.getAmmoCubes().getYellow(),myPlayer.getDamages().size(),
                myPlayer.getMarks().size(),myPlayer.getDeaths(),myPlayer.getDeathPoints());
        writeMyWeapons(game.getMyPlayerRepresentation().getWeapons());
        writeMyEmpowers(game.getMyPlayerRepresentation().getEmpowers());
    }

    /**
     * This method writes ammunition cards in the map.
     * @param map is the playing map.
     */
    private static void writeAmmosInMap(Map<Position,AmmunitionCard> map) {
        int h;
        for (Map.Entry<Position, AmmunitionCard> pos_ammo : map.entrySet()) {
            int x = pos_ammo.getKey().getRow();
            int y = pos_ammo.getKey().getColumn();
            boolean wrote = false;
            h = 0;
            if (pos_ammo.getValue() != null) {
                for (int i = 0; i < pos_ammo.getValue().getAmmoCubes().getRed(); i++) {
                    board[X_FOR_MAP + x * SQUARE_HEIGHT + 1][Y_FOR_MAP + y * SQUARE_LENGTH + 1 + i] = 'R';
                    wrote = true;
                }
                if (wrote) {
                    h++;
                    wrote = false;
                }
                for (int i = 0; i < pos_ammo.getValue().getAmmoCubes().getBlue(); i++) {
                    board[X_FOR_MAP + x * SQUARE_HEIGHT + 1 + h][Y_FOR_MAP + y * SQUARE_LENGTH + 1 + i] = 'B';
                    wrote = true;
                }
                if (wrote) {
                    h++;
                    wrote = false;
                }
                for (int i = 0; i < pos_ammo.getValue().getAmmoCubes().getYellow(); i++) {
                    board[X_FOR_MAP + x * SQUARE_HEIGHT + 1 + h][Y_FOR_MAP + y * SQUARE_LENGTH + 1 + i] = 'Y';
                    wrote = true;
                }
                if (wrote) {
                    h++;
                }
                if (pos_ammo.getValue().hasEmpower())
                    board[X_FOR_MAP + x * SQUARE_HEIGHT + 1 + h][Y_FOR_MAP + y * SQUARE_LENGTH + 1] = 'P';
            }
        }
    }

    /**
     * This method writes some stats like Killshot Track, remaining weapons,ammuinition cards and empower cards.
     * @param emps are remaining empowers (in empowers deck)
     * @param weapons are remaining weapons (in weapons deck)
     * @param ammos are remaining ammo cards (in ammo deck)
     */
    private static void writeKillShotTrack(int emps,int weapons,int ammos){
        int i;
        for(i=0; i< KILL_SHOT_TRACK.length(); i++)
            board[KILL_SHOT_TRACK_X][KILL_SHOT_TRACK_Y +i]= KILL_SHOT_TRACK.charAt(i);
        for(int h=0;h<8;h++) {
            board[KILL_SHOT_TRACK_X][KILL_SHOT_TRACK_Y + i] = heart;
            i++;
        }
        for(i=0;i<REMAINING_EMPOWERS.length();i++)
            board[KILL_SHOT_TRACK_X +2][KILL_SHOT_TRACK_Y +i]=REMAINING_EMPOWERS.charAt(i);
        board[KILL_SHOT_TRACK_X +2][KILL_SHOT_TRACK_Y +i]=String.valueOf(emps).charAt(0);
        i++;
        if(emps>9)
            board[KILL_SHOT_TRACK_X +2][KILL_SHOT_TRACK_Y +i]=String.valueOf(emps).charAt(1);

        for(i=0;i<REMAINING_WEAPONS.length();i++)
            board[KILL_SHOT_TRACK_X +3][KILL_SHOT_TRACK_Y +i]=REMAINING_WEAPONS.charAt(i);
        board[KILL_SHOT_TRACK_X +3][KILL_SHOT_TRACK_Y +i]=String.valueOf(weapons).charAt(0);
        i++;
        if(weapons>9)
            board[KILL_SHOT_TRACK_X +3][KILL_SHOT_TRACK_Y +i]=String.valueOf(weapons).charAt(1);

        for(i=0;i<REMAINING_AMMOS.length();i++)
            board[KILL_SHOT_TRACK_X +4][KILL_SHOT_TRACK_Y +i]=REMAINING_AMMOS.charAt(i);
        board[KILL_SHOT_TRACK_X +4][KILL_SHOT_TRACK_Y +i]=String.valueOf(ammos).charAt(0);
        i++;
        if(ammos>9)
            board[KILL_SHOT_TRACK_X +4][KILL_SHOT_TRACK_Y +i]=String.valueOf(ammos).charAt(1);
    }

    /**
     *Write "yourPlayer" weapon's panel in the window.
     * @param myWeapons are your player weapons
     */
    private static void writeMyWeapons(List<WeaponInfo> myWeapons){
        writeBoards(X_MY_WEAPONS, Y_MY_WEAPONS,3+myWeapons.size(),MY_WEAPONS_LENGTH);
        int z=1;
        for(int i=0;i<MY_WEAPONS.length();i++)
            board[X_MY_WEAPONS +z][Y_MY_WEAPONS +1+i]=MY_WEAPONS.charAt(i);
        z++;
        for(WeaponInfo we: myWeapons){
            for(int i=0;i<we.getName().length();i++)
                board[X_MY_WEAPONS +z][Y_MY_WEAPONS +1+i]=we.getName().charAt(i);
            for(int i=0;i<RELOAD_COST.length();i++)
                board[X_MY_WEAPONS +z][Y_MY_WEAPONS +22+i]=RELOAD_COST.charAt(i);
            board[X_MY_WEAPONS +z][Y_MY_WEAPONS +40]='R';
            board[X_MY_WEAPONS +z][Y_MY_WEAPONS +41]=':';
            board[X_MY_WEAPONS +z][Y_MY_WEAPONS +42]=String.valueOf(we.getReloadCost().getRed()).charAt(0);
            board[X_MY_WEAPONS +z][Y_MY_WEAPONS +44]='B';
            board[X_MY_WEAPONS +z][Y_MY_WEAPONS +45]=':';
            board[X_MY_WEAPONS +z][Y_MY_WEAPONS +46]=String.valueOf(we.getReloadCost().getBlue()).charAt(0);
            board[X_MY_WEAPONS +z][Y_MY_WEAPONS +48]='Y';
            board[X_MY_WEAPONS +z][Y_MY_WEAPONS +49]=':';
            board[X_MY_WEAPONS +z][Y_MY_WEAPONS +50]=String.valueOf(we.getReloadCost().getYellow()).charAt(0);
            if(we.isLoaded())
                board[X_MY_WEAPONS +z][Y_MY_WEAPONS +52]='C';
            else
                board[X_MY_WEAPONS +z][Y_MY_WEAPONS +52]='S';
            z++;
        }

    }

    /**
     * Write "your Player" empower's panel in the window
     * @param myEmpowers are "Your player" empowers
     */

    private static void writeMyEmpowers(List<EmpowerInfo> myEmpowers){
        writeBoards(MY_EMPOWER_X, MY_EMPOWER_Y,3+myEmpowers.size(),MY_EMPOWERS_LENGTH);
        int z=1;
        for(int i=0;i<MY_EMPOWERS.length();i++)
            board[MY_EMPOWER_X +z][MY_EMPOWER_Y +1+i]=MY_EMPOWERS.charAt(i);
        z++;
        for(EmpowerInfo emp: myEmpowers){
            for(int i=0;i<emp.getName().length();i++) {
                board[MY_EMPOWER_X + z][MY_EMPOWER_Y + 1 + i] = emp.getName().charAt(i);
            }
            z++;
        }
    }


    /**
     * Write Players in map panel
     * @param players are enemy players
     * @param myplayer is "your player"
     */
    private static void writeMapPlayers(List<PlayerRepresentation> players, MyPlayerRepresentation myplayer) {
        int r;
        int c;
        int j = 5;
        int i = 3;
        for (PlayerRepresentation player : players) {
            if (player.getPosition() != null) {
                r = player.getPosition().getRow();
                c = player.getPosition().getColumn();
                for(int l=0;l<player.getName().length();l++)
                    board[X_FOR_MAP + r * SQUARE_HEIGHT + i][Y_FOR_MAP + c * SQUARE_LENGTH + j+l] = player.getName().charAt(l);
                i++;
            }

        }
        if (myplayer.getPosition() != null) {
            r = myplayer.getPosition().getRow();
            c = myplayer.getPosition().getColumn();
            board[X_FOR_MAP + r * SQUARE_HEIGHT + i][Y_FOR_MAP + c * SQUARE_LENGTH + j] = 'T';
            board[X_FOR_MAP + r * SQUARE_HEIGHT + i][Y_FOR_MAP + c * SQUARE_LENGTH + j+1] = 'U';
        }
    }

    /**
     * Write everything stats important about "you player".
     */
    private static void writePersonalInfo(){
        writeBoards(MY_PLAYER_X, MY_PLAYER_Y,MY_PLAYER_HEIGHT,MY_PLAYER_LENGTH);
        for(int r=0;r<MY_PLAYER_HEIGHT;r++)
            board[MY_PLAYER_X +r][MY_PLAYER_LENGTH+ MY_PLAYER_Y -1]=' ';
        for (int i = 0; i < MY_LIFE_POINTS.length(); i++)
            board[MY_PLAYER_X + 1][1 + MY_PLAYER_Y + i] = MY_LIFE_POINTS.charAt(i);
        for (int i = 0; i < MY_DAMAGES.length(); i++)
            board[MY_PLAYER_X + 6][1 + MY_PLAYER_Y + i + 25] = MY_DAMAGES.charAt(i);
        for (int i = 0; i <MY_MARKS.length(); i++)
            board[MY_PLAYER_X + 6][1 + MY_PLAYER_Y + i] = MY_MARKS.charAt(i);
        for (int i = 0; i <MY_EMPOWERS.length(); i++)
            board[MY_PLAYER_X + 4][1 + MY_PLAYER_Y + i ] = MY_EMPOWERS.charAt(i);
        for (int i = 0; i < MY_DEATHS.length(); i++)
            board[MY_PLAYER_X + 2][1 + MY_PLAYER_Y + i ] = MY_DEATHS.charAt(i);
        for (int i = 0; i < AMMO_CUBE_RED.length(); i++)
            board[MY_PLAYER_X + 1][1 + MY_PLAYER_Y + 30 + i] = AMMO_CUBE_RED.charAt(i);
        for (int i = 0; i < AMMO_CUBE_BLUE.length(); i++)
            board[MY_PLAYER_X + 2][1 + MY_PLAYER_Y + 30 + i] = AMMO_CUBE_BLUE.charAt(i);
        for (int i = 0; i < AMMO_CUBE_YELLOW.length(); i++)
            board[MY_PLAYER_X + 3][1 + MY_PLAYER_Y + 30 + i] = AMMO_CUBE_YELLOW.charAt(i);

    }

    /**
     * This update "your player" 's personal infos.
     * @param redCubes is num of red cubes
     * @param blueCubes is num of blue cubes
     * @param yellowCubes is num of yellow cubes
     * @param damages is number of damages
     * @param marks is number of marks
     * @param numDeaths is number of deaths
     * @param points are player's points
     */

    private static void updatePersonalInfo(int redCubes, int blueCubes, int yellowCubes, int damages, int marks,int numDeaths,int points){
        for (int i = 0; i < redCubes; i++)
            board[MY_PLAYER_X + 1][MY_PLAYER_Y + i + 41] = cube;
        for (int i = 0; i < blueCubes; i++)
            board[MY_PLAYER_X + 2][MY_PLAYER_Y + i + 42] = cube;
        for (int i = 0; i < yellowCubes; i++)
            board[MY_PLAYER_X + 3][MY_PLAYER_Y + i + 44] = cube;
        for (int i = 0; i < damages; i++)
            board[MY_PLAYER_X + 7][MY_PLAYER_Y + i + 26] = heart;
        for (int i = 0; i < marks; i++)
            board[MY_PLAYER_X +7][MY_PLAYER_Y + i + 1] = mark;
        board[MY_PLAYER_X + 2][MY_PLAYER_Y + 15] = String.valueOf(numDeaths).charAt(0);
        board[MY_PLAYER_X + 1][MY_PLAYER_Y + 18] = String.valueOf(points).charAt(0);

        if(damages>2){
            for(int i=0;i<TIPS.length();i++)
                board[KILL_SHOT_TRACK_X +7][KILL_SHOT_TRACK_Y +i]=TIPS.charAt(i);
            for(int i=0;i<DAMAGED_MESSAGE_1.length();i++)
                board[KILL_SHOT_TRACK_X +8][KILL_SHOT_TRACK_Y +i]=DAMAGED_MESSAGE_1.charAt(i);
            if(damages>4)
                for(int i=0;i<DAMAGED_MESSAGE_2.length();i++)
                    board[KILL_SHOT_TRACK_X +9][KILL_SHOT_TRACK_Y +i]=DAMAGED_MESSAGE_2.charAt(i);
        }
    }

    /**
     * This method print "your panel" to the window
     * @param player is "your player" representation
     * @param a is the x of your player panel in the window
     * @param b is the y of you player panel in the window
     */
    private static void printMyPlayer(MyPlayerRepresentation player,int a, int b) {
        if (a < 5 && b < 30) {
            WriterHelper.printColored(colorToAnsiBold(player.getColor()));
        } else if (a == 1 && b > 30) {
            WriterHelper.printColored(ANSIColors.RED_BOLD);
        }else if(a==2&&b>30){
            WriterHelper.printColored(ANSIColors.BLUE_BOLD);
        }else if(a==3&&b>30){
            WriterHelper.printColored(ANSIColors.YELLOW_BOLD);
        }else if(a==6||a>=8){
            WriterHelper.printColored(colorToAnsiBold(player.getColor()));
        }else if(a==7&&b>0&&b<25){
            printMyPlayerMarks(player,b-1);
        }else if(a==7&&b>25){
            printMyPlayerDamage(player,b-26);
        }
    }

    /**
     * Prints in the window "your player" marks box.
     */
    private static void printMyPlayerMarks(MyPlayerRepresentation player,int b){
        if(b<player.getMarks().size())
            WriterHelper.printColored(colorToAnsi(player.getMarks().get(b)));
    }

    /**
     * Prints in the window "your player" damages box
     */
    private static void printMyPlayerDamage(MyPlayerRepresentation player,int b){
        if(b<player.getDamages().size())
            WriterHelper.printColored(colorToAnsi(player.getDamages().get(b)));
    }

    /**
     * Prints in the window the scoreboard
     * @param x is the x of the scoreboard in the window
     * @param players are enemy players representation
     * @param myplayer *is "your player" reprensentation
     */
    private static void printScoreboard(int x,List<PlayerRepresentation> players,MyPlayerRepresentation myplayer){

        if(x==SCOREBOARD_X+1)
            WriterHelper.printColored(ANSIColors.CYAN_BOLD);
        if((x==SCOREBOARD_X+3||x==SCOREBOARD_X+4))
            WriterHelper.printColored(colorToAnsiBold(players.get(0).getColor()));
        if((x==SCOREBOARD_X+6||x==SCOREBOARD_X+7))
            WriterHelper.printColored(colorToAnsiBold(players.get(1).getColor()));
        if(players.size()>2){
            if((x==SCOREBOARD_X+9||x==SCOREBOARD_X+10))
                WriterHelper.printColored(colorToAnsiBold(players.get(2).getColor()));
            if(players.size()>3) {
                if ((x == SCOREBOARD_X + 15 || x == SCOREBOARD_X + 16))
                    WriterHelper.printColored(colorToAnsiBold(myplayer.getColor()));
                if ((x == SCOREBOARD_X + 12 || x == SCOREBOARD_X + 13))
                    WriterHelper.printColored(colorToAnsiBold(players.get(3).getColor()));
            } else
            if((x==SCOREBOARD_X+12||x==SCOREBOARD_X+13))
                WriterHelper.printColored(colorToAnsiBold(myplayer.getColor()));
        }
        else{
            if((x==SCOREBOARD_X+9||x==SCOREBOARD_X+10))
                WriterHelper.printColored(colorToAnsiBold(myplayer.getColor()));
        }
    }

    /**
     *
     * @param player is an enemy player
     * @param b is the x position of player's marks in player's panel
     */
    private static void printPlayerMarks(PlayerRepresentation player,int b){
        if(b<player.getMarks().size())
            WriterHelper.printColored(colorToAnsi(player.getMarks().get(b)));
    }

    /**
     *
     * @param player is an enemy player
     * @param b is the x position of player's damages in player's panel
     */
    private static void printPlayerDamage(PlayerRepresentation player,int b){
        if(b<player.getDamages().size())
            WriterHelper.printColored(colorToAnsi(player.getDamages().get(b)));
    }

    /**
     *
     * @param player is an enemy player that has to printed
     * @param a is the x position of player's panel in the window
     * @param b is the y position of player's panel in the window
     */
    private static void printPlayer(PlayerRepresentation player,int a, int b) {
        if (a < 5 && b < 30 ||a==1) {
            WriterHelper.printColored(colorToAnsiBold(player.getColor()));
        } else if (a == 2 && b > 30) {
            WriterHelper.printColored(ANSIColors.RED_BOLD);
        }else if(a==3&&b>30){
            WriterHelper.printColored(ANSIColors.BLUE_BOLD);
        }else if(a==4&&b>30){
            WriterHelper.printColored(ANSIColors.YELLOW_BOLD);
        }else if(a==6||a>=8){
            WriterHelper.printColored(colorToAnsiBold(player.getColor()));
        }else if(a==7&&b>0&&b<25){
            printPlayerMarks(player,b-1);
        }else if(a==7&&b>25){
            printPlayerDamage(player,b-26);
        }
    }

    /**
     * This method uses printPlayer() for every Player. Look printPlayer() for more infos.
     * @param players are enemy players
     */
    private static void printPlayers(List<PlayerRepresentation> players,int x,int y) {
        if (x > FIRST_ENEMY_X && x < FIRST_ENEMY_X + ENEMY_PLAYER_HEIGHT && y > FIRST_ENEMY_Y && y < FIRST_ENEMY_Y + ENEMY_PLAYER_LENGTH)
            printPlayer(players.get(0), x - FIRST_ENEMY_X, y - FIRST_ENEMY_Y);
        if (x > FIRST_ENEMY_X + ENEMY_PLAYER_HEIGHT && x < FIRST_ENEMY_X + ENEMY_PLAYER_HEIGHT * 2 && y > FIRST_ENEMY_Y && y < FIRST_ENEMY_Y + ENEMY_PLAYER_LENGTH)
            printPlayer(players.get(1), x-FIRST_ENEMY_X-ENEMY_PLAYER_HEIGHT, y - FIRST_ENEMY_Y);
        if (players.size() >= 3){
            if (x > FIRST_ENEMY_X + ENEMY_PLAYER_HEIGHT * 2 && x < FIRST_ENEMY_X + ENEMY_PLAYER_HEIGHT * 3 && y > FIRST_ENEMY_Y && y < FIRST_ENEMY_Y + ENEMY_PLAYER_LENGTH)
                printPlayer(players.get(2), x-FIRST_ENEMY_X-ENEMY_PLAYER_HEIGHT*2, y - FIRST_ENEMY_Y);
        } else WriterHelper.printColored(ANSIColors.RESET);
        if (players.size() >= 4) {
            if (x > FIRST_ENEMY_X + ENEMY_PLAYER_HEIGHT * 3 && x < FIRST_ENEMY_X + ENEMY_PLAYER_HEIGHT * 4 && y > FIRST_ENEMY_Y && y < FIRST_ENEMY_Y + ENEMY_PLAYER_LENGTH)
                printPlayer(players.get(3), x-FIRST_ENEMY_X- ENEMY_PLAYER_HEIGHT*3, y - FIRST_ENEMY_Y);
        }else WriterHelper.printColored(ANSIColors.RESET);
    }


    /**
     * This method is used to decide the color to print the map because every square has a different color.
     * @param x is x of the square.
     * @param y is y of the square.
     * @param type is map Type.
     */
    private static void printMaps(int x,int y,int type){

        WriterHelper.printColored(ANSIColors.WHITE_BOLD);

        if(x>X_FOR_MAP&&x<SQUARE_HEIGHT+X_FOR_MAP&&y>Y_FOR_MAP&&y<SQUARE_LENGTH+Y_FOR_MAP&&(type==1||type==3)) {
            WriterHelper.printColored(ANSIColors.BLUE_BACKGROUND);
            WriterHelper.printColored(ANSIColors.WHITE_BOLD);
        }
        if(x>X_FOR_MAP&&x<SQUARE_HEIGHT+X_FOR_MAP&&y>Y_FOR_MAP+SQUARE_LENGTH&&y<SQUARE_LENGTH*2+Y_FOR_MAP&&(type==1||type==3)) {
            WriterHelper.printColored(ANSIColors.BLUE_BACKGROUND);
            WriterHelper.printColored(ANSIColors.WHITE_BOLD);
        }
        if((type==1||type==3)&&(x>X_FOR_MAP+SQUARE_HEIGHT&&x<X_FOR_MAP+SQUARE_HEIGHT*2&&y>Y_FOR_MAP&&y<SQUARE_LENGTH+Y_FOR_MAP)) {
            WriterHelper.printColored(ANSIColors.RED_BACKGROUND);
            WriterHelper.printColored(ANSIColors.WHITE_BOLD);
        }
        if((type==1||type==3)&&(x>X_FOR_MAP+SQUARE_HEIGHT&&x<X_FOR_MAP+SQUARE_HEIGHT*2&&y>Y_FOR_MAP+SQUARE_LENGTH&&y<SQUARE_LENGTH*2+Y_FOR_MAP)) {
            WriterHelper.printColored(ANSIColors.RED_BACKGROUND);
            WriterHelper.printColored(ANSIColors.WHITE_BOLD);
        }
        if((type==1||type==3)&&(x>X_FOR_MAP+SQUARE_HEIGHT*2&&x<X_FOR_MAP+SQUARE_HEIGHT*3&&y>Y_FOR_MAP+SQUARE_LENGTH&&y<Y_FOR_MAP+SQUARE_LENGTH*2)) {
            WriterHelper.printColored(ANSIColors.BLACK_BOLD);
            WriterHelper.printColored(ANSIColors.WHITE_BACKGROUND);
        }
        if(x>X_FOR_MAP&&x<SQUARE_HEIGHT+X_FOR_MAP&&y>Y_FOR_MAP&&y<SQUARE_LENGTH+Y_FOR_MAP&&(type==2||type==4)) {
            WriterHelper.printColored(ANSIColors.RED_BACKGROUND);
            WriterHelper.printColored(ANSIColors.WHITE_BOLD);
        }
        if(x>X_FOR_MAP+SQUARE_HEIGHT&&x<2*SQUARE_HEIGHT+X_FOR_MAP&&y>Y_FOR_MAP&&y<SQUARE_LENGTH+Y_FOR_MAP&&(type==2||type==4)) {
            WriterHelper.printColored(ANSIColors.RED_BACKGROUND);
            WriterHelper.printColored(ANSIColors.WHITE_BOLD);
        }

        if(x>X_FOR_MAP&&x<SQUARE_HEIGHT+X_FOR_MAP&&y>Y_FOR_MAP+SQUARE_LENGTH&&y<2*SQUARE_LENGTH+Y_FOR_MAP&&(type==2||type==4)) {
            WriterHelper.printColored(ANSIColors.BLUE_BACKGROUND);
            WriterHelper.printColored(ANSIColors.WHITE_BOLD);
        }
        if(x>X_FOR_MAP+SQUARE_HEIGHT&&x<2*SQUARE_HEIGHT+X_FOR_MAP&&y>Y_FOR_MAP+SQUARE_LENGTH&&y<2*SQUARE_LENGTH+Y_FOR_MAP&&(type==2||type==4)) {
            WriterHelper.printColored(ANSIColors.MAGENTA_BACKGROUND);
            WriterHelper.printColored(ANSIColors.WHITE_BOLD);
        }
        if((type==2||type==4)&&(x>X_FOR_MAP+SQUARE_HEIGHT*2&&x<X_FOR_MAP+SQUARE_HEIGHT*3&&y>Y_FOR_MAP&&y<Y_FOR_MAP+SQUARE_LENGTH)) {
            WriterHelper.printColored(ANSIColors.WHITE_BACKGROUND);
            WriterHelper.printColored(ANSIColors.BLACK_BOLD);
        }
        if((type==2||type==4)&&(x>X_FOR_MAP+SQUARE_HEIGHT*2&&x<X_FOR_MAP+SQUARE_HEIGHT*3&&y>Y_FOR_MAP+SQUARE_LENGTH&&y<Y_FOR_MAP+SQUARE_LENGTH*2)) {
            WriterHelper.printColored(ANSIColors.WHITE_BACKGROUND);
            WriterHelper.printColored(ANSIColors.BLACK_BOLD);
        }
        // 4 && 3
        if(x>X_FOR_MAP&&x<SQUARE_HEIGHT+X_FOR_MAP&&y>Y_FOR_MAP+SQUARE_LENGTH*2&&y<3*SQUARE_LENGTH+Y_FOR_MAP&&(type==3||type==4)) {
            WriterHelper.printColored(ANSIColors.BLUE_BACKGROUND);
            WriterHelper.printColored(ANSIColors.WHITE_BOLD);
        }
        if(x>X_FOR_MAP&&x<SQUARE_HEIGHT+X_FOR_MAP&&y>Y_FOR_MAP+SQUARE_LENGTH*3&&y<4*SQUARE_LENGTH+Y_FOR_MAP&&(type==3||type==4)) {
            WriterHelper.printColored(ANSIColors.GREEN_BACKGROUND);
            WriterHelper.printColored(ANSIColors.WHITE_BOLD);
        }
        if(x>X_FOR_MAP+SQUARE_HEIGHT&&x<2*SQUARE_HEIGHT+X_FOR_MAP&&y>Y_FOR_MAP+SQUARE_LENGTH*2&&y<3*SQUARE_LENGTH+Y_FOR_MAP&&(type==3||type==4)) {
            WriterHelper.printColored(ANSIColors.YELLOW_BACKGROUND);
            WriterHelper.printColored(ANSIColors.WHITE_BOLD);
        }
        if(x>X_FOR_MAP+SQUARE_HEIGHT&&x<2*SQUARE_HEIGHT+X_FOR_MAP&&y>Y_FOR_MAP+SQUARE_LENGTH*3&&y<4*SQUARE_LENGTH+Y_FOR_MAP&&(type==3||type==4)) {
            WriterHelper.printColored(ANSIColors.YELLOW_BACKGROUND);
            WriterHelper.printColored(ANSIColors.WHITE_BOLD);
        }
        if(x>X_FOR_MAP+SQUARE_HEIGHT*2&&x<3*SQUARE_HEIGHT+X_FOR_MAP&&y>Y_FOR_MAP+SQUARE_LENGTH*2&&y<3*SQUARE_LENGTH+Y_FOR_MAP&&(type==3||type==4)) {
            WriterHelper.printColored(ANSIColors.YELLOW_BACKGROUND);
            WriterHelper.printColored(ANSIColors.WHITE_BOLD);
        }
        if(x>X_FOR_MAP+SQUARE_HEIGHT*2&&x<3*SQUARE_HEIGHT+X_FOR_MAP&&y>Y_FOR_MAP+SQUARE_LENGTH*3&&y<4*SQUARE_LENGTH+Y_FOR_MAP&&(type==3||type==4)) {
            WriterHelper.printColored(ANSIColors.YELLOW_BACKGROUND);
            WriterHelper.printColored(ANSIColors.WHITE_BOLD);
        }
        //1 && 2
        if(x>X_FOR_MAP&&x<SQUARE_HEIGHT+X_FOR_MAP&&y>Y_FOR_MAP+SQUARE_LENGTH*2&&y<3*SQUARE_LENGTH+Y_FOR_MAP&&(type==1||type==2)) {
            WriterHelper.printColored(ANSIColors.BLUE_BACKGROUND);
            WriterHelper.printColored(ANSIColors.WHITE_BOLD);
        }
        if(x>X_FOR_MAP+SQUARE_HEIGHT&&x<3*SQUARE_HEIGHT+X_FOR_MAP&&y>Y_FOR_MAP+SQUARE_LENGTH*3&&y<4*SQUARE_LENGTH+Y_FOR_MAP&&(type==1||type==2)) {
            WriterHelper.printColored(ANSIColors.YELLOW_BACKGROUND);
            WriterHelper.printColored(ANSIColors.WHITE_BOLD);
        }
        if(x>X_FOR_MAP+SQUARE_HEIGHT*2&&x<3*SQUARE_HEIGHT+X_FOR_MAP&&y>Y_FOR_MAP+SQUARE_LENGTH*2&&y<3*SQUARE_LENGTH+Y_FOR_MAP&&(type==1||type==2)) {
            WriterHelper.printColored(ANSIColors.WHITE_BACKGROUND);
            WriterHelper.printColored(ANSIColors.BLACK_BOLD);
        }
        if(x>X_FOR_MAP+SQUARE_HEIGHT&&x<SQUARE_HEIGHT*2+X_FOR_MAP&&y>Y_FOR_MAP+SQUARE_LENGTH*2&&y<3*SQUARE_LENGTH+Y_FOR_MAP&&type==2) {
            WriterHelper.printColored(ANSIColors.MAGENTA_BACKGROUND);
            WriterHelper.printColored(ANSIColors.WHITE_BOLD);
        }
        if(x>X_FOR_MAP+SQUARE_HEIGHT&&x<SQUARE_HEIGHT*2+X_FOR_MAP&&y>Y_FOR_MAP+SQUARE_LENGTH*2&&y<3*SQUARE_LENGTH+Y_FOR_MAP&&type==1) {
            WriterHelper.printColored(ANSIColors.RED_BACKGROUND);
            WriterHelper.printColored(ANSIColors.WHITE_BOLD);
        }
    }

    /**
     * This method plots the window in the screen.
     * @param players are enemy players
     * @param me is my player
     * @param type is map type
     * @param killShotColors are colors for killshot track
     */
    public void plot(List<PlayerRepresentation> players,MyPlayerRepresentation me,int type,List<Color> killShotColors) {

        int high=WINDOW_HEIGHT;
        if(players.size()==2)
            high=MAP_HEIGHT+X_FOR_MAP+4;

        for(int i=0;i<10;i++) {
            WriterHelper.printlnOnConsole("");
            for (int j = 0; j < WINDOW_LENGTH; j++)
                WriterHelper.printOnConsole(" ");
        }
        for (int r = 0; r < high; r++) {
            WriterHelper.printlnOnConsole("");
            for (int c = 0; c < WINDOW_LENGTH; c++) {
                printDecider(r,c,players,me,type,killShotColors);
                System.out.print(board[r][c]);
                WriterHelper.printColored(ANSIColors.RESET);
            }
        }
        WriterHelper.printlnOnConsole("");
        WriterHelper.printlnOnConsole("");
    }

    /**
     * This method
     * @param x is the x of the window that has to be printed.
     * @param y is the y of the window that has to be printed.
     * @param players are representation of enemy players
     * @param me is "Your Player" representation
     * @param type is map type
     * @param killshotColors is the representation of the killShot Track.
     */
    private static void printDecider(int x, int y,List<PlayerRepresentation> players,MyPlayerRepresentation me, int type,List<Color> killshotColors){
        if(x>=0&&x<12&&y>=0&&y<WEAPONS_LENGTH)
            WriterHelper.printColored(ANSIColors.BLUE_BOLD);
        if(x>=0&&x<12&&y>=WEAPONS_LENGTH&&y<WEAPONS_LENGTH+ WEAPONS_LENGTH)
            WriterHelper.printColored(ANSIColors.RED_BOLD);
        if(x>=0&&x<12&&y>= 2*WEAPONS_LENGTH &&y<WEAPONS_LENGTH+2* WEAPONS_LENGTH)
            WriterHelper.printColored(ANSIColors.YELLOW_BOLD);
        if(x>=X_FOR_MAP&&x<=X_FOR_MAP+SQUARE_HEIGHT*3&&y>=Y_FOR_MAP&&y<=Y_FOR_MAP+SQUARE_LENGTH*4)
            printMaps(x,y,type);
        if(x> MY_PLAYER_X &&x< MY_PLAYER_X +MY_PLAYER_HEIGHT-1&&y> MY_PLAYER_Y &&y< MY_PLAYER_Y +MY_PLAYER_LENGTH)
            printMyPlayer(me,x- MY_PLAYER_X,y- MY_PLAYER_Y);
        if(x>SCOREBOARD_X&&y>SCOREBOARD_Y&&y<SCOREBOARD_Y+SCOREBOARD_LEN-1)
            printScoreboard(x,players,me);
        if(x== KILL_SHOT_TRACK_X &&y>= KILL_SHOT_TRACK_Y &&y< KILL_SHOT_TRACK_Y +15)
            WriterHelper.printColored(ANSIColors.BLACK_BOLD);
        if(x== KILL_SHOT_TRACK_X &&y>= KILL_SHOT_TRACK_Y +15&&y< KILL_SHOT_TRACK_Y +15+8)
            printKillshot(y,killshotColors);
        if(x> KILL_SHOT_TRACK_X &&x<= KILL_SHOT_TRACK_X +4&&y>= KILL_SHOT_TRACK_Y &&y<= KILL_SHOT_TRACK_Y +32)
            WriterHelper.printColored(ANSIColors.BLACK_BOLD);
        if(x> KILL_SHOT_TRACK_X +5&&x<= KILL_SHOT_TRACK_X +9&&y>= KILL_SHOT_TRACK_Y &&y<= KILL_SHOT_TRACK_Y +32)
            WriterHelper.printColored(colorToAnsiBold(me.getColor()));
        if (x > FIRST_ENEMY_X && x < FIRST_ENEMY_X + ENEMY_PLAYER_HEIGHT && y > FIRST_ENEMY_Y && y < FIRST_ENEMY_Y + ENEMY_PLAYER_LENGTH&&players.get(0)!=null)
            printPlayer(players.get(0), x - FIRST_ENEMY_X, y - FIRST_ENEMY_Y);
        if (x > FIRST_ENEMY_X + ENEMY_PLAYER_HEIGHT && x < FIRST_ENEMY_X + ENEMY_PLAYER_HEIGHT * 2 && y > FIRST_ENEMY_Y && y < FIRST_ENEMY_Y + ENEMY_PLAYER_LENGTH)
            printPlayer(players.get(1), x-FIRST_ENEMY_X-ENEMY_PLAYER_HEIGHT, y - FIRST_ENEMY_Y);
        if (players.size() >= 3){
            if (x > FIRST_ENEMY_X + ENEMY_PLAYER_HEIGHT * 2 && x < FIRST_ENEMY_X + ENEMY_PLAYER_HEIGHT * 3 && y > FIRST_ENEMY_Y && y < FIRST_ENEMY_Y + ENEMY_PLAYER_LENGTH)
                printPlayer(players.get(2), x-FIRST_ENEMY_X-ENEMY_PLAYER_HEIGHT*2, y - FIRST_ENEMY_Y);
        }
        if (players.size() >= 4) {
            if (x > FIRST_ENEMY_X + ENEMY_PLAYER_HEIGHT * 3 && x < FIRST_ENEMY_X + ENEMY_PLAYER_HEIGHT * 4 && y > FIRST_ENEMY_Y && y < FIRST_ENEMY_Y + ENEMY_PLAYER_LENGTH)
                printPlayer(players.get(3), x-FIRST_ENEMY_X- ENEMY_PLAYER_HEIGHT*3, y - FIRST_ENEMY_Y);
        }
        if(x> X_MY_WEAPONS &&x< X_MY_WEAPONS +5&&y> Y_MY_WEAPONS &&y< Y_MY_WEAPONS +MY_WEAPONS_LENGTH-1)
            printmyWeapons(x,me);

        if(x> MY_EMPOWER_X &&x< MY_EMPOWER_X +5&&y> MY_EMPOWER_Y &&y< MY_EMPOWER_Y +MY_EMPOWERS_LENGTH-1)
            printmyEmpowers(x,me);
    }


    /**
     * This method is used to print "Your Player" empowers in the screen.
     * @param x is the x in the screen where are empowers.
     * @param me is the representation of "Your Player"
     */
    private static void printmyEmpowers(int x, MyPlayerRepresentation me){
        if(x== MY_EMPOWER_X +1) {
            WriterHelper.printColored(colorToAnsiBold(me.getColor()));
        }else
            WriterHelper.printColored(ANSIColors.RESET);
        if(x== MY_EMPOWER_X +2&&!me.getEmpowers().isEmpty())
            WriterHelper.printColored(colorToAnsiBold(me.getEmpowers().get(0).getColor()));
        else WriterHelper.printColored(ANSIColors.RESET);
        if(x== MY_EMPOWER_X +3&&me.getEmpowers().size()>1)
            WriterHelper.printColored(colorToAnsiBold(me.getEmpowers().get(1).getColor()));
        else WriterHelper.printColored(ANSIColors.RESET);
        if(x== MY_EMPOWER_X +4&&me.getEmpowers().size()>2)
            WriterHelper.printColored(colorToAnsiBold(me.getEmpowers().get(2).getColor()));
        else WriterHelper.printColored(ANSIColors.RESET);

    }

    /**
     * This method is used to print KillShot-Track.
     * @param y is the y position of Killshot-Track in the screen
     * @param colors are from KillShot representation to represent kills.
     */
    private static void printKillshot(int y,List<Color> colors){
        int a=y- KILL_SHOT_TRACK_Y -15;
        if(a>=0&&a<8&&colors!=null){
            switch (a){
                case 0:
                    if(!colors.isEmpty()) {
                        WriterHelper.printColored(colorToAnsiBold(colors.get(0)));

                    }else WriterHelper.printColored(ANSIColors.RED_BOLD);
                    break;
                case 1:
                    if(colors.size()>1) {
                        WriterHelper.printColored(colorToAnsiBold(colors.get(1)));

                    }
                    else WriterHelper.printColored(ANSIColors.RED_BOLD);
                    break;
                case 2:
                    if(colors.size()>2) {
                        WriterHelper.printColored(colorToAnsiBold(colors.get(2)));

                    }
                    else WriterHelper.printColored(ANSIColors.RED_BOLD);
                    break;
                case 3:
                    if(colors.size()>3)
                        WriterHelper.printColored(colorToAnsiBold(colors.get(3)));
                    else WriterHelper.printColored(ANSIColors.RED_BOLD);
                    break;
                case 4:
                    if(colors.size()>4)
                        WriterHelper.printColored(colorToAnsiBold(colors.get(4)));
                    else WriterHelper.printColored(ANSIColors.RED_BOLD);
                    break;
                case 5:
                    if(colors.size()>5)
                        WriterHelper.printColored(colorToAnsiBold(colors.get(5)));
                    else WriterHelper.printColored(ANSIColors.RED_BOLD);
                    break;
                case 6:
                    if(colors.size()>6)
                        WriterHelper.printColored(colorToAnsiBold(colors.get(6)));
                    else WriterHelper.printColored(ANSIColors.RED_BOLD);
                    break;
                case 7:
                    if(colors.size()>7)
                        WriterHelper.printColored(colorToAnsiBold(colors.get(7)));
                    else WriterHelper.printColored(ANSIColors.RED_BOLD);
                    break;
            }
        }else WriterHelper.printColored(ANSIColors.RED_BOLD);
    }


    /**
     * This method is used to print on screen colors of "your player's" weapons.
     * @param x is the position of weapons.
     * @param me is the representation of your player.
     */
    private static void printmyWeapons(int x, MyPlayerRepresentation me){

        if(x== X_MY_WEAPONS +1) {
            WriterHelper.printColored(colorToAnsiBold(me.getColor()));
        }else
            WriterHelper.printColored(ANSIColors.RESET);
        if(x== X_MY_WEAPONS +2&&!me.getWeapons().isEmpty()) {
            if (me.getWeapons().get(0).isLoaded())
                WriterHelper.printColored(colorToAnsiBold(me.getWeapons().get(0).getColor()));
            }
        else WriterHelper.printColored(ANSIColors.RESET);
        if(x== X_MY_WEAPONS +3&&me.getWeapons().size()>1) {
            if (me.getWeapons().get(1).isLoaded())
                WriterHelper.printColored(colorToAnsiBold(me.getWeapons().get(1).getColor()));
            }
        else WriterHelper.printColored(ANSIColors.RESET);
        if(x== X_MY_WEAPONS +4&&me.getWeapons().size()>2){
            if (me.getWeapons().get(2).isLoaded())
                WriterHelper.printColored(colorToAnsiBold(me.getWeapons().get(2).getColor()));
            }
        else WriterHelper.printColored(ANSIColors.RESET);
    }

}