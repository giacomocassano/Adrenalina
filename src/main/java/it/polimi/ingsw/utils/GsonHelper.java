package it.polimi.ingsw.utils;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.*;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to Serialize and Deserialize the game status in a Json File to implements
 * persistence functionality.
 */
public class GsonHelper{

    private static final String BLU_WEAPONS="BluWeapons";
    private static final String RED_WEAPONS="RedWeapons";
    private static final String YELLOW_WEAPONS="YellowWeapons";
    private static final String MAP="MAP";
    private static final String MAP_TYPE="mapType";
    private static final String MAP_AMMO="mapAmmunition";
    private static final String EMP_STACK="EmpowerStack";
    private static final String DISC_EMP_STACK="DiscardedEmpowerStack";
    private static final String WEAP_STACK="WeaponsStack";
    private static final String DISC_WEAP_STACK="DiscardedWeaponsStack";
    private static final String AMMO_STACK="AmmoStack";
    private static final String DISC_AMMO_STACK="DiscardedAmmoStack";
    private static final String PATH="src/resources/lastGame.json";
    private static final String NAME="name";
    private static final String COLOR="color";
    private static final String POINTS="points";
    private static final String NUM_DEATHS="numDeaths";
    private static final String FIRST_PLAYER="firstPlayer";
    private static final String BEFORE_FIRST_PLAYER="beforeFirstPlayer";
    private static final String FINAL_FRENZY="finalFrenzy";
    private static final String JUST_SHOT="justShot";
    private static final String AMMO_CUBES="AmmoCubes";
    private static final String AMMO_BLUE="ammoBlue";
    private static final String AMMO_RED="ammoRed";
    private static final String AMMO_YELLOW="ammoYellow";
    private static final String POSITION="position";
    private static final String ROW="row";
    private static final String COLUMN="column";
    private static final String WEAPONS="weapons";
    private static final String EMPOWERS="empowers";
    private static final String PLAYERS_DMGS_MARKS="PLAYERS_DAMAGES_AND_MARKS:";
    private static final String KILLSHOT_TRACK="KILLSHOT_TRACK:";
    private static final String PLAYERS="PLAYERS";
    private static final String DAMAGES="DAMAGES:";
    private static final String MARKS="MARKS:";
    private static final String GAME_INFOS="GAME INFOS:";
    private static final String FIRST_ROUND="FIRST ROUND:";
    private static final String LAST_ROUND="LAST ROUND:";
    private static final String ACTIVE_PLAYER="ACTIVE PLAYER";
    private static final int MAP_ROWS = 3;
    private static final int MAP_COLUMNS = 4;
    private static final String red="RED";
    private static final String blue="BLUE";
    private static final String grey="GREY";
    private static final String purple="PURPLE";
    private static final String white="WHITE";
    private static final String green="GREEN";
    private static final String yellow="YELLOW";


    /**
     * Writes in a json file the game, so players, decks, killshot-track and other infos.
     * @param game is the last played game.
     * @param path is the path of the file to write
     */
    public static void writeGameStatus(Game game, String path){
        try{
            JsonWriter writer = new JsonWriter(new FileWriter(path));
            writer.setIndent(" ");
            writer.beginObject();
            writeMapAndDecks(game,writer);
            writePlayers(writer,game.getPlayers(),game.getMap());
            writePlayersDamage(writer,game.getPlayers());
            writeGameInfos(writer,game);
            writeKillShotTrack(writer,game.getKillshotTrack());
            writer.endObject();
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes every player in the json file
     * @param writer is the Gson Writer
     * @param players are players from last played game.
     * @param map is the map in the last played game.
     * @throws IOException is something goes wrong during file writing.
     */
    public static void writePlayers(JsonWriter writer,List<Player> players,Map map)throws IOException{
        writer.name(PLAYERS);
        writer.beginArray();
        for(Player p: players)
            writePlayer(p,map,writer);
        writer.endArray();
    }

    /**
     * Writes every player's damage (from last game) in the json file.
     * @param writer is the Gson Writer
     * @param players are players from last played game.
     * @throws IOException if there is an error during file writing
     */
    private static void writePlayersDamage(JsonWriter writer,List<Player> players)throws IOException{
        writer.name(PLAYERS_DMGS_MARKS);
        writer.beginArray();
        for(Player p: players) {
            writer.beginObject();
            writePlayerDamagesAndMarks(p, writer);
            writer.endObject();
        }
        writer.endArray();
    }

    /**
     * Reads killshot track from the xml file and creates an instance of a new killshot track.
     * @param reader is the Gson reader
     * @param players are players read from json file
     * @param game is the new game just read from json file
     * @return a copy of last played game's killshot track
     * @throws IOException if something goes wrong during file reading.
     */
    public static KillShotTrack readKillShotTrack(JsonReader reader,List<Player> players,Game game)throws IOException{
        boolean stop=false;
        KillShotTrack killShotTrack=new KillShotTrack(8,game);
        reader.nextName();
        reader.beginArray();
        while (reader.hasNext()) {
            String playerName = reader.nextString();
            for (int i = 0; i < players.size() && !stop; i++) {
                if (players.get(i).getName().equals(playerName)){
                    killShotTrack.addKill(players.get(i), reader.nextBoolean());
                    stop=true;
                }
            }
            stop=false;
        }
        reader.endArray();
        return killShotTrack;
    }

    /**
     * writes to json file infos about last game, like if games stopped in the first or last round
     * the active player when game stopped..
     * @param writer is the Gson writer
     * @param game is last played game
     * @throws IOException if there is an error during file writing.
     */
    private static void writeGameInfos(JsonWriter writer,Game game)throws IOException{
        writer.name(GAME_INFOS);
        writer.beginObject();
        writer.name(FIRST_ROUND);
        writer.value(game.isFirstRound());
        writer.name(LAST_ROUND);
        writer.value(game.isLastRound());
        writer.name(ACTIVE_PLAYER);
        writer.value(game.getActivePlayer().getName());
        writer.endObject();
    }

    /**
     * reads from json file infos about last game, like if game stopped in first or last round and others
     * and creates a new instance of last played game.
     * @param reader is the Gson Reader
     * @param players are  players read from file and ready to be added to new game.
     * @param map is the map read from file and ready to be added to new game
     * @return a new instance of last played game
     * @throws IOException if there is an error during file reading
     */
    public static Game readGameInfosAndStartGame(JsonReader reader,List<Player> players,Map map)throws IOException{
        Player activePlayer=players.get(0);
        reader.nextName();
        reader.beginObject();
        reader.nextName();
        Boolean firstRound=reader.nextBoolean();
        reader.nextName();
        Boolean lastRound=reader.nextBoolean();
        reader.nextName();
        String playerName=reader.nextString();
        for(Player p: players)
            if (p.getName().equals(playerName)) {
                activePlayer = p;
                break;
            }
        reader.endObject();
        return new Game(players,activePlayer,map,firstRound,lastRound);
    }

    /**
     * Writes every info of last played game's killshotTrack in a json file
     * @param writer is a Gson Writer
     * @param killShotTrack is last played game's killshotTrack
     * @throws IOException if there is an error during file writing.
     */
    private static void writeKillShotTrack(JsonWriter writer,KillShotTrack killShotTrack)throws IOException{
        writer.name(KILLSHOT_TRACK);
        writer.beginArray();
        for(Damage d: killShotTrack.getMarkers()) {
            writer.value(d.getShooter().getName());
            writer.value(d.isInfertDamage());
        }
        writer.endArray();
    }

    /**
     * Writes in a json file every info about a player's damages and marks
     * @param player is the interested player
     * @param writer is a Json Writer
     * @throws IOException if there is an error during file writing.
     */
    private static void writePlayerDamagesAndMarks(Player player,JsonWriter writer)throws IOException{
        writer.name(NAME);
        writer.value(player.getName());
        writer.name(DAMAGES);
        writer.beginArray();
        writer.setIndent("");
        for (Damage d: player.getDamages()) {
            writer.value(d.getShooter().getName());
            writer.value(d.isInfertDamage());
        }
        writer.endArray();
        writer.setIndent(" ");
        writer.name(MARKS);
        writer.beginArray();
        writer.setIndent("");
        for (Damage m: player.getMarks()) {
            writer.value(m.getShooter().getName());
        }
        writer.endArray();
        writer.setIndent(" ");
    }

    /**
     * Writes in a json file every info about last played game's map and decks
     * @param game is last played game
     * @param writer is a Gson Writer
     * @throws IOException if there is an error during file writing
     */
    private static void writeMapAndDecks(Game game,JsonWriter writer)throws IOException{
            writer.name(MAP);
            writer.beginObject();
            writer.name(MAP_TYPE).value(game.getMap().getType());
            writer.name(MAP_AMMO);
            writeMapAmmos(game.getMap(), writer);
            writeMapWeapons(game.getMap(), writer);
            writer.name(EMP_STACK);
            writeEmpowersID(game.getEmpowerStack().getCards(), writer);
            writer.name(DISC_EMP_STACK);
            writeEmpowersID(game.getEmpowerStack().getDiscardeds(), writer);
            writer.name(WEAP_STACK);
            writeWeaponsID(game.getWeaponStack().getCards(), writer);
            writer.name(DISC_WEAP_STACK);
            writeWeaponsID(game.getWeaponStack().getDiscardeds(), writer);
            writer.name(AMMO_STACK);
            writeAmmunitionID(game.getAmmoStack().getCards(), writer);
            writer.name(DISC_AMMO_STACK);
            writeAmmunitionID(game.getAmmoStack().getDiscardeds(), writer);
            writer.endObject();
    }

    /**
     * Takes a color and turns it in a string. Not usable Color.ToString because write it in italian
     * @param c is the interested color
     * @return a string.
     */
    private static String colorToString(Color c) {
        switch (c) {
            case RED: return red;
            case BLUE: return blue;
            case GREY: return grey;
            case PURPLE: return purple;
            case WHITE: return white;
            case GREEN: return green;
            case YELLOW: return yellow;
        }
        return null;
    }

    /**
     * Reads all the infos about last game.
     *
     */
    public static void readGameStatus(){
        Stack<WeaponCard> weaponCardStack = new Stack<>(XMLHelper.forgeWeaponDeck());
        Stack<EmpowerCard> empowerCardStack = new Stack<>(XMLHelper.forgeEmpowerDeck());
        Stack<AmmunitionCard> ammunitionCardStack = new Stack<>(XMLHelper.forgeAmmoDeck());
        List<Player> players;
        try {
            JsonReader reader = new JsonReader(new FileReader(PATH));
            reader.beginObject();
            Map map=readMap(reader, weaponCardStack,empowerCardStack,ammunitionCardStack);
            players=readPlayers(reader,map,empowerCardStack,weaponCardStack,ammunitionCardStack);
            readPlayersDamagesAndMarks(reader,players);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Reads from json file every player damages and marks calling readPlayerDamagesAndMarks()
     * @param reader is the Gson Reader
     * @param players is last played game's players
     * @throws IOException if there is an error during file reading.
     */
    public static void readPlayersDamagesAndMarks(JsonReader reader,List<Player> players)throws IOException {
        reader.nextName();
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            reader.nextName();
            Player player=getPlayer(players,reader.nextString());
            if(player!=null)
                readPlayerDamagesAndMarks(reader,player,players);
            reader.endObject();
        }
        reader.endArray();
    }

    /**
     * Reads from a json file player's damages and marks and then add them to interested player
     * @param reader is Gson Reader
     * @param player is the interested player
     * @param players is a list of players
     * @throws IOException if there is an error during file reading
     */
    private static void readPlayerDamagesAndMarks(JsonReader reader,Player player,List<Player> players)throws IOException{
        reader.nextName();
        reader.beginArray();
        while (reader.hasNext()) {
            player.getDamages().add(new Damage(getPlayer(players,reader.nextString()),reader.nextBoolean()));
        }
        reader.endArray();
        reader.nextName();
        reader.beginArray();
        while (reader.hasNext()) {
            player.getMarks().add(new Damage(getPlayer(players,reader.nextString()),false));
        }
        reader.endArray();
    }


    /**
     *
     * @param players is a list of players
     * @param name is player name
     * @return a player from a list of players.
     */
    private static Player getPlayer(List<Player> players,String name){
            for(Player p: players)
                if(p.getName().equals(name))
                    return p;
                return null;
    }

    /**
     * reads from json file the weapons stack and orders it like last game played weapons stack
     * @param reader is the json reader
     * @param cardStack is a new weapons stack that has to be ordered
     * @throws IOException if there is an error during file reading.
     */
    private static void readAndOrderWeaponStack(JsonReader reader,Stack<WeaponCard> cardStack)throws IOException{
        int id;
        reader.nextName();
        reader.beginArray();
        WeaponCard w;
        while (reader.hasNext()) {
            id = reader.nextInt();
            w=cardStack.getCardById(id);
            cardStack.removeCard(w);
            cardStack.addCard(w);
            }
        reader.endArray();
    }

    /**
     * reads from json file the empowers stack and orders it like last game played empowers stack
     * @param reader is the json reader
     * @param cardStack is a new empowers stack that has to be ordered
     * @throws IOException if there is an error during file reading.
     */
    private static void readAndOrderEmpStack(JsonReader reader,Stack<EmpowerCard> cardStack)throws IOException{
        int id;
        reader.nextName();
        reader.beginArray();
        EmpowerCard e;
        while (reader.hasNext()) {
            id = reader.nextInt();
            e=cardStack.getCardById(id);
            cardStack.removeCard(e);
            cardStack.addCard(e);
        }
        reader.endArray();
    }

    /**
     * reads from json file the ammo stack and orders it like last game played ammo stack
     * @param reader is the json reader
     * @param cardStack is a new ammo stack that has to be ordered
     * @throws IOException if there is an error during file reading.
     */
    private static void readAndOrderAmmoStack(JsonReader reader,Stack<AmmunitionCard> cardStack)throws IOException{
        int id;
        reader.nextName();
        reader.beginArray();
        AmmunitionCard a;
        while (reader.hasNext()) {
            id = reader.nextInt();
            a=cardStack.getCardById(id);
            cardStack.removeCard(a);
            cardStack.addCard(a);
        }
        reader.endArray();
    }

    /**
     * Reads from json file the discarded weapon stack and creates an instance of it.
     * @param reader is the json reader
     * @param cardStack is a new stack to take cards and put them in the discarded list.
     * @throws IOException is there is an error during file reading
     */
    private static void readDiscardedWeaponStack(JsonReader reader,Stack<WeaponCard> cardStack)throws IOException{
        int id;
        reader.nextName();
        reader.beginArray();
        WeaponCard w;
        while (reader.hasNext()) {
            id = reader.nextInt();
            w=cardStack.getCardById(id);
            cardStack.moveInDiscardedCard(w);
        }
        reader.endArray();
    }

    /**
     * Reads from json file the discarded empower stack and creates an instance of it.
     * @param reader is the json reader
     * @param cardStack is a new stack to take cards and put them in the discarded list.
     * @throws IOException is there is an error during file reading
     */
    private static void readDiscardedEmpStack(JsonReader reader,Stack<EmpowerCard> cardStack)throws IOException{
        int id;
        reader.nextName();
        reader.beginArray();
        EmpowerCard e;
        while (reader.hasNext()) {
            id = reader.nextInt();
            e=cardStack.getCardById(id);
            cardStack.moveInDiscardedCard(e);
        }
        reader.endArray();
    }

    /**
     * Reads from json file the discarded ammo stack and creates an instance of it.
     * @param reader is the json reader
     * @param cardStack is a new stack to take cards and put them in the discarded list.
     * @throws IOException is there is an error during file reading
     */
    private static void readDiscardedAmmoStack(JsonReader reader,Stack<AmmunitionCard> cardStack)throws IOException{
        int id;
        reader.nextName();
        reader.beginArray();
        AmmunitionCard a;
        while (reader.hasNext()) {
            id = reader.nextInt();
            a=cardStack.getCardById(id);
            cardStack.moveInDiscardedCard(a);
        }
        reader.endArray();
    }

    /**
     * Method to read from json file map infos, then deserialize those infos and creates an instance of the last played game's
     * map
     * @param reader is the Gson Reader
     * @param weaponCardStack is a weapon cards stack
     * @param empowerCardStack is an empower cards stack
     * @param ammunitionCardStack is an ammo cards stack
     * @return the last played map
     * @throws IOException if there is an error during file reading.
     */
    public static Map readMap(JsonReader reader,Stack<WeaponCard> weaponCardStack,Stack<EmpowerCard> empowerCardStack,Stack<AmmunitionCard> ammunitionCardStack) throws IOException {
        reader.nextName();
        reader.beginObject();
        reader.nextName();
        int type = reader.nextInt();
        Map map = new Map(type);
        reader.nextName();
        List<AmmunitionCard> mapAmmunition = readAmmoCards(reader, ammunitionCardStack.getCards());
        List<WeaponCard> mapBlueWeapons = readWeaponCards(reader, weaponCardStack.getCards());
        List<WeaponCard> mapRedWeapons = readWeaponCards(reader, weaponCardStack.getCards());
        List<WeaponCard> mapYellowWeapons = readWeaponCards(reader, weaponCardStack.getCards());
        map.getRespawnBlue().setWeapons(mapBlueWeapons);
        map.getRespawnRed().setWeapons(mapRedWeapons);
        map.getRespawnYellow().setWeapons(mapYellowWeapons);
        placeAmmosInMap(map,mapAmmunition);
        readAndOrderEmpStack(reader,empowerCardStack);
        readDiscardedEmpStack(reader,empowerCardStack);
        readAndOrderWeaponStack(reader,weaponCardStack);
        readDiscardedWeaponStack(reader,weaponCardStack);
        readAndOrderAmmoStack(reader,ammunitionCardStack);
        readDiscardedAmmoStack(reader,ammunitionCardStack);
        reader.endObject();
        return map;
    }

    /**
     * Method to place ammo cards (read from json file) in the map
     * @param map is the deserialized map.
     * @param mapAmmo are deserialized ammunition cards.
     */
    private static void placeAmmosInMap(Map map,List<AmmunitionCard> mapAmmo){
        int i=0;
        for(Square s: map.getEverySquares()){
            if(!s.isRespawnPoint()){
                s.setAmmos(mapAmmo.get(i));
                i++;
            }
        }
    }

    /**
     * Method to read from a json file a list of weapon cards, then find those cards in a stack, removes them
     * and creates a new stack.
     * @param reader is the Gson Reader
     * @param cards are cards from the original stack.
     * @return a list of weapon cards with just few cards from the original stack
     * @throws IOException if there is an error during file reading
     */
    private static List<WeaponCard> readWeaponCards(JsonReader reader, List<WeaponCard> cards) throws IOException {
        int id;
        List<WeaponCard> newCards = new ArrayList<>();
        reader.nextName();
        reader.beginArray();
        while (reader.hasNext()) {
            id = reader.nextInt();
            for (int i = 0; i < cards.size(); i++) {
                if (cards.get(i).getId() == id) {
                    newCards.add(cards.get(i));
                    cards.remove(cards.get(i));
                }
            }
        }
        reader.endArray();
        return newCards;
    }


    /**
     * Method to read from a json file a list of weapon cards, then find those cards in a stack, removes them
     * and creates a new stack.
     * This is used to read Player cards because a player can have an unloaded weapon
     * @param reader is the Gson Reader
     * @param cards are cards from the original stack.
     * @return a list of weapon cards with just few cards from the original stack
     * @throws IOException if there is an error during file reading
     */
    private static List<WeaponCard> readPlayerWeaponCards(JsonReader reader, List<WeaponCard> cards) throws IOException {
        int id;
        boolean load;
        List<WeaponCard> newCards = new ArrayList<>();
        reader.nextName();
        reader.beginArray();
        while (reader.hasNext()) {
            id = reader.nextInt();
            load=reader.nextBoolean();
            for (int i = 0; i < cards.size(); i++) {
                if (cards.get(i).getId() == id) {
                    newCards.add(cards.get(i));
                    if(!load)
                        cards.get(i).unload();
                    cards.remove(cards.get(i));
                }
            }
        }
        reader.endArray();
        return newCards;
    }



    /**
     * Method to read from a json file a list of empower cards, then find those cards in a stack, removes them
     * and creates a new stack.
     * @param reader is the Gson Reader
     * @param cards are cards from the original stack.
     * @return a list of empower card with just few cards from the original stack
     * @throws IOException if there is an error during file reading
     */
    private static List<EmpowerCard> readEmpowerCards(JsonReader reader, List<EmpowerCard> cards) throws IOException {
        int idEmp;
        List<EmpowerCard> newCards = new ArrayList<>();
        reader.nextName();
        reader.beginArray();
        while (reader.hasNext()) {
            idEmp= reader.nextInt();
            for (int i = 0; i < cards.size(); i++) {
                if (cards.get(i).getId() == idEmp) {
                    newCards.add(cards.get(i));
                    cards.remove(cards.get(i));
                }
            }
        }
        reader.endArray();
        return newCards;
    }


    /**
     * Method to read from a json file a list of ammo cards, then find those cards in a stack, removes them
     * and creates a new stack.
     * @param reader is the Gson Reader
     * @param cards are cards from the original stack.
     * @return a list of ammo card with just few cards from the original stack
     * @throws IOException if there is an error during file reading
     */
    private static List<AmmunitionCard> readAmmoCards(JsonReader reader, List<AmmunitionCard> cards) throws IOException {
        int id;
        List<AmmunitionCard> newCards = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            id = reader.nextInt();
            for (int i = 0; i < cards.size(); i++) {
                if (cards.get(i).getId() == id) {
                    newCards.add(cards.get(i));
                    cards.remove(cards.get(i));
                }
            }
        }
        reader.endArray();
        return newCards;
    }

    /**
     * Used to read players from a Json file to create a new instance of last game.
     * @param reader is the Gson Reader
     * @param map is the just loaded map.
     * @param empowerCardStack is the just loaded empower card stack
     * @param weaponCardStack is the just loaded weapon card stack
     * @param ammunitionCardStack is the just loaded ammo card stack.
     * @return a list of players
     * @throws IOException if there is a problem during file reading
     */
    public static List<Player> readPlayers(JsonReader reader, Map map,Stack<EmpowerCard> empowerCardStack,
                                           Stack<WeaponCard> weaponCardStack,
                                           Stack<AmmunitionCard> ammunitionCardStack) throws IOException {
        List<Player> players=new ArrayList<>();
        reader.nextName();
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            reader.nextName();
            String name = reader.nextString();
            reader.nextName();
            String color = reader.nextString();
            reader.nextName();
            boolean firstPlayer = reader.nextBoolean();
            reader.nextName();
            boolean firstFinalFrenzy= reader.nextBoolean();
            reader.nextName();
            int points = reader.nextInt();
            reader.nextName();
            reader.beginObject();
            reader.nextName();
            int numBlue=reader.nextInt();
            reader.nextName();
            int numYellow=reader.nextInt();
            reader.nextName();
            int numRed=reader.nextInt();
            reader.endObject();
            List<WeaponCard> playerWeapons=readPlayerWeaponCards(reader,weaponCardStack.getCards());
            List<EmpowerCard> playerEmpowers=readEmpowerCards(reader,empowerCardStack.getCards());
            reader.nextName();
            int numDeaths = reader.nextInt();
            reader.nextName();
            boolean isB4firstPlayer = reader.nextBoolean();
            reader.nextName();
            boolean justShot = reader.nextBoolean();
            reader.nextName();
            reader.beginObject();
            reader.nextName();
            int row=reader.nextInt();
            reader.nextName();
            int col=reader.nextInt();
            reader.endObject();
            reader.endObject();
            Player player=new Player(name,XMLHelper.colorReader(color),firstPlayer);
            AmmoCubes playerAmmoCubes=new AmmoCubes(numRed,numBlue,numYellow);
            player.setAmmoCubes(playerAmmoCubes);
            player.addPoints(points);
            player.setFirstFinalFrenzy(firstFinalFrenzy);
            player.setJustShot(justShot);
            player.setBeforeFirstPlayer(isB4firstPlayer);
            player.setDeaths(numDeaths);
            player.setEmpowers(playerEmpowers);
            player.addWeapons(playerWeapons);
            players.add(player);
            map.getSquare(row,col).addPlayer(player);
        }
        reader.endArray();
    return players;
    }

    /**
     * Writes every information about a player in a json file.
     * @param p is interested player
     * @param map is the map (used to take player's position)
     * @param writer is the Gson Writer
     * @throws IOException if there is an error during file writing
     */
    private static void writePlayer(Player p, Map map,JsonWriter writer) throws IOException {
        writer.setIndent(" ");
        writer.beginObject();
        writer.name(NAME).value(p.getName());
        writer.name(COLOR).value(colorToString(p.getColor()));
        writer.name(FIRST_PLAYER).value(p.isFirstPlayer());
        writer.name(FINAL_FRENZY).value(p.isFirstFinalFrenzy());
        writer.name(POINTS).value(p.getPoints());
        writer.name(AMMO_CUBES);
        writer.beginObject();
        writer.name(AMMO_BLUE).value(p.getAmmoCubes().getBlue());
        writer.name(AMMO_YELLOW).value(p.getAmmoCubes().getYellow());
        writer.name(AMMO_RED).value(p.getAmmoCubes().getRed());
        writer.endObject();
        writer.name(WEAPONS);
        writeWeaponsIDPlayer(p.getWeapons(), writer);
        writer.name(EMPOWERS);
        writeEmpowersID(p.getEmpowers(), writer);
        writer.name(NUM_DEATHS).value(p.getDeaths());
        writer.name(BEFORE_FIRST_PLAYER).value(p.isBeforeFirstPlayer());
        writer.name(JUST_SHOT).value(p.isJustShot());
        writer.name(POSITION);
        writer.beginObject();
        if(map.getPlayerPosition(p)!=null) {
            writer.name(ROW).value(map.getPlayerPosition(p).getRow());
            writer.name(COLUMN).value(map.getPlayerPosition(p).getColumn());
        }
        writer.endObject();
        writer.endObject();
    }

    /**
     * Writes a list of weapons Id in the json file
     * @param weapons are interested weapon cards.
     * @param writer is the Gson writer
     * @throws IOException if there is a problem during file-writing.
     */
    private static void writeWeaponsID(List<WeaponCard> weapons, JsonWriter writer) throws IOException {
        writer.beginArray();
        writer.setIndent("");
        for (WeaponCard w : weapons) {
            writer.value(w.getId());
        }
        writer.endArray();
        writer.setIndent(" ");
    }


    /**
     * Writes a list of weapons Id in the json file for a player
     * @param weapons are interested weapon cards.
     * @param writer is the Gson writer
     * @throws IOException if there is a problem during file-writing.
     */
    private static void writeWeaponsIDPlayer(List<WeaponCard> weapons, JsonWriter writer) throws IOException {
        writer.beginArray();
        writer.setIndent("");
        for (WeaponCard w : weapons) {
            writer.value(w.getId());
            writer.value(w.isLoaded());
        }
        writer.endArray();
        writer.setIndent(" ");
    }
    /**
     * Writes a list of empowers Id in the json file
     * @param empowers are interested empower cards.
     * @param writer is the Gson writer
     * @throws IOException if there is a problem during file-writing.
     */
    private static void writeEmpowersID(List<EmpowerCard> empowers, JsonWriter writer) throws IOException {
        writer.beginArray();
        writer.setIndent("");
        for (EmpowerCard e : empowers) {
            writer.value(e.getId());
        }
        writer.endArray();
        writer.setIndent(" ");
    }

    /**
     * Writes a list of  Ammo Id in the json file
     * @param ammos are interested ammo cards.
     * @param writer is the Gson writer
     * @throws IOException if there is a problem during file-writing.
     */
    private static void writeAmmunitionID(List<AmmunitionCard> ammos, JsonWriter writer) throws IOException {
        writer.beginArray();
        writer.setIndent("");
        for (AmmunitionCard a : ammos) {
            writer.value(a.getId());
        }
        writer.endArray();
        writer.setIndent(" ");
    }

    /**
     * Method to write in the json file all the Ammunition Cards in the Map
     * @param map is the map in the game
     * @param writer is the Json Writer
     * @throws IOException if there is a problem during file-writing
     */
    private static void writeMapAmmos(Map map, JsonWriter writer) throws IOException {

        writer.beginArray();
        writer.setIndent("");
        for (int i = 0; i < MAP_ROWS; i++)
            for (int j = 0; j < MAP_COLUMNS; j++)
                if (map.getSquare(i, j) != null && !map.getSquare(i, j).isRespawnPoint())
                    writer.value(map.getSquare(i, j).getAmmos().getId());

        writer.endArray();
        writer.setIndent(" ");
    }

    /**
     * Writes in Json File map weapons
     * @param map is the map
     * @param writer is the writer
     * @throws IOException because a file has to be opened.
     */
    private static void writeMapWeapons(Map map, JsonWriter writer) throws IOException {

        writer.name(BLU_WEAPONS);
        writer.beginArray();
        writer.setIndent("");
        for (int h = 0; h < map.getRespawnBlue().getWeapons().size(); h++)
            writer.value(map.getRespawnBlue().getWeapons().get(h).getId());
        writer.endArray();
        writer.setIndent("");
        writer.name(RED_WEAPONS);
        writer.beginArray();
        writer.setIndent("");
        for (int h = 0; h < map.getRespawnRed().getWeapons().size(); h++)
            writer.value(map.getRespawnRed().getWeapons().get(h).getId());
        writer.endArray();
        writer.setIndent("");
        writer.name(YELLOW_WEAPONS);
        writer.beginArray();
        writer.setIndent("");
        for (int h = 0; h < map.getRespawnYellow().getWeapons().size(); h++)
            writer.value(map.getRespawnYellow().getWeapons().get(h).getId());
        writer.endArray();
        writer.setIndent(" ");
    }

}