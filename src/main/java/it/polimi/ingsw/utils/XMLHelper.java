package it.polimi.ingsw.utils;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.AmmunitionCard;
import it.polimi.ingsw.model.cards.EmpowerCard;
import it.polimi.ingsw.model.cards.EmpowerType;
import it.polimi.ingsw.model.cards.WeaponCard;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import it.polimi.ingsw.model.cards.effects.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to read from XML files maps, cards and configurations.
 */

public class XMLHelper{

    private static final int MAP_ROWS = 3;
    private static final int MAP_COLUMNS = 4;
    private static final String COLOR="color";
    private static final String ID="id";
    private static final String DESCRIPTION="description";
    private static final String TYPE="type";
    private static final String NAME="nameNotConfirmed";
    private static final String MAP_1_PATH="map1.xml";
    private static final String MAP_2_PATH="map2.xml";
    private static final String MAP_3_PATH="map3.xml";
    private static final String MAP_4_PATH="map4.xml";
    private static final String TAGBACK_GRANADE="TAGBACK GRANADE";
    private static final String TELEPORTER="TELEPORTER";
    private static final String TARGETING_SCOPE="TARGETING SCOPE";
    private static final String NEWTON="NEWTON";
    private static final String AMMO_FILE_PATH="AmmoCards.xml";
    private static final String AMMO ="ammo";
    private static final String RIGHT="hasRight";
    private static final String TOP="hasTop";
    private static final String LEFT="hasLeft";
    private static final String BOTTOM="hasBottom";
    private static final String SPAWN="spawn";
    private static final String RED="RED";
    private static final String YELLOW="YELLOW";
    private static final String BLUE="BLUE";
    private static final String GREEN="GREEN";
    private static final String WHITE="WHITE";
    private static final String NUM_YELLOW="numYellow";
    private static final String NUM_RED="numRed";
    private static final String NUM_BLUE="numBlue";
    private static final String REC_NUM_YELLOW="recnumYellow";
    private static final String REC_NUM_RED="recnumRed";
    private static final String REC_NUM_BLUE="recnumBlue";
    private static final String MOVE_YELLOW="moveYellow";
    private static final String MOVE_RED="moveRed";
    private static final String MOVE_BLUE="moveBlue";
    private static final String YELLOW_LOWER="Yellow";
    private static final String RED_LOWER="Red";
    private static final String BLUE_LOWER="Blue";
    private static final String WEAPON_FILE_PATH="Weapons.xml";
    private static final String WEAPON="weapon";
    private static final String TRUE="true";
    private static final String EMPOWERS_PATH="Empowers.xml";
    private static final String EMPOWER="empower";
    private static final String EFFECT="effect";
    private static final String MOVEMENT_NAME="movementName";
    private static final String MOVEMENT_DESCRIPTION="movementDescription";
    private static final String MOVEMENT_STEPS="movementMaxSteps";
    private static final String MOVE_EFFECT="hasMoveEffect";
    private static final String AREA_DAMAGE="AreaDamage";
    private static final String AREA_MARKS="AreaMarks";
    private static final String TARGETS_STEPS="TargetSteps";
    private static final String MIN_RANGE="MinRange";
    private static final String MAX_RANGE="MaxRange";
    private static final String DECREASED="Decreased";
    private static final String MAX_TARGETS="MaxTargets";
    private static final String HAS_POWERUP="hasPowerup";
    private static final String NAME2="Name";
    private static final String ULTRA2="ultra2";
    private static final String HAS_ANOTHER="hasAnother";
    private static final String DESCRIPTION_UP="Description";
    private static final String DAMAGE="Damage";
    private static final String MARK="Mark";
    private static final String CHAIN="Chain";
    private static final String OLD_TARGETS_SHOOT="OldTargetsShootable";
    private static final String VORTEX="Vortex";
    private static final String DURING_BASE="DuringBaseEffect";
    private static final String CONFIGURATION_PATH="Configurations.xml";
    private static final String SOCKET_PORT="SocketPort";
    private static final String RMI_PORT="RMIPort";
    private static final String RESPAWN_SECONDS="RespawnSeconds";
    private static final String MOVE_SECONDS="MoveSeconds";
    private static final String EMPOWER_SECONDS="EmpowerSeconds";
    private static final String PING_SECONDS="PingSeconds";
    private static final String PING_WAITING_SECONDS="PingWaitingSeconds";
    private static final String WAITING_ROOM_SECONDS="WaitingRoomSeconds";
    private static final String IP_SERVER="ipServer";
    private static final String ROOM="Room";
    private static final String CYBER="Cyber";
    private static final String STEPS="Steps";
    private static final String SHOOT_VISIBLE="ShootVisible";
    private static final String RECOIL="Recoil";
    private static final String CONFIGURATIONS="Configurations";
    private static final String RELOAD_GAME="reloadGame";

    /**
     * This method reads from XML the map and returns a MATRIX of squares.
     *
     * @param type is map's type.
     * @return a matrix of Squares
     */
    public  Square[][] loadMap(int type) {

        Square[][] grid = new Square[MAP_ROWS][MAP_COLUMNS];
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream mapFile;
        try {
            switch (type) {
                case 1:
                    mapFile=cl.getResourceAsStream(MAP_1_PATH);
                    break;
                case 2:
                    mapFile=cl.getResourceAsStream(MAP_2_PATH);
                    break;
                case 3:
                    mapFile=cl.getResourceAsStream(MAP_3_PATH);
                    break;
                case 4:
                    mapFile=cl.getResourceAsStream(MAP_4_PATH);
                    break;
                default:
                    mapFile=cl.getResourceAsStream(MAP_4_PATH);
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(mapFile);
            int cont = 0;
            String hasTop, hasLeft, hasRight, hasBottom, colour, spawn;
            for (int i = 0; i < MAP_ROWS; i++)
                for (int j = 0; j < MAP_COLUMNS; j++, cont++) {
                    hasTop = doc.getElementsByTagName(TOP).item(cont).getTextContent();
                    hasLeft = doc.getElementsByTagName(LEFT).item(cont).getTextContent();
                    hasRight = doc.getElementsByTagName(RIGHT).item(cont).getTextContent();
                    hasBottom = doc.getElementsByTagName(BOTTOM).item(cont).getTextContent();
                    colour = doc.getElementsByTagName(COLOR).item(cont).getTextContent();
                    spawn = doc.getElementsByTagName(SPAWN).item(cont).getTextContent();
                    grid[i][j] = new Square(colorReader(colour), boolReader(spawn), i, j, boolReader(hasTop), boolReader(hasRight), boolReader(hasLeft), boolReader(hasBottom));
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (type) {
            case 1:
                grid[0][3] = null;
                grid[2][0] = null;
                break;
            case 2:
                grid[0][3] = null;
                break;
            case 3:
                grid[2][0] = null;
            default:
        }
        return grid;
    }

    /**
     * This method reads from an XML file empower-cards   and returns a List of Empower Cards
     * @return a list of empower card to create an Empower Stack
     */
    public static List<EmpowerCard> forgeEmpowerDeck() {
        ArrayList<EmpowerCard> empowerDeck = new ArrayList<EmpowerCard>();
        try {
            ClassLoader cl = XMLHelper.class.getClassLoader();
            InputStream empowerFile;
            empowerFile=cl.getResourceAsStream(EMPOWERS_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document empowerdoc = dBuilder.parse(empowerFile);
            empowerdoc.getDocumentElement().normalize();
            NodeList nodeList = empowerdoc.getElementsByTagName(EMPOWER);
            for (int i = 0; i < nodeList.getLength(); i++)
                empowerDeck.add(getEmpower(nodeList.item(i)));
            return empowerDeck;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return empowerDeck;
    }

    /**
     * Method to read Starting configuration from XMl file.
     * @return starting configuration object
     * @throws Exception if there are file issues
     */
    public static StartingConfiguration readConfigs() throws Exception {
        ClassLoader cl = XMLHelper.class.getClassLoader();
        InputStream configFile;
        configFile=cl.getResourceAsStream(CONFIGURATION_PATH);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document configDoc = dBuilder.parse(configFile);
        configDoc.getDocumentElement().normalize();
        NodeList nodeList = configDoc.getElementsByTagName(CONFIGURATIONS);
        Element element = (Element) nodeList.item(0);
        String ipServer = element.getElementsByTagName(IP_SERVER).item(0).getTextContent();
        String socketPort = element.getElementsByTagName(SOCKET_PORT).item(0).getTextContent();
        String rmiPort = element.getElementsByTagName(RMI_PORT).item(0).getTextContent();
        return new StartingConfiguration(ipServer,Integer.valueOf(socketPort),Integer.valueOf(rmiPort));
    }

    /**
     * Returns an empowerCard from XML file.
     * @param node from Xml-file.
     * @return EmpowerCard
     */
    private static EmpowerCard getEmpower(Node node){
        EmpowerCard card = null;
        if (node.getNodeType() == node.ELEMENT_NODE) {
            Element element = (Element) node;
            String name = element.getElementsByTagName(NAME).item(0).getTextContent();
            String id=element.getElementsByTagName(ID).item(0).getTextContent();
            String desc = element.getElementsByTagName(DESCRIPTION).item(0).getTextContent();
            String col = element.getElementsByTagName(COLOR).item(0).getTextContent();
            String type=element.getElementsByTagName(TYPE).item(0).getTextContent();
            card = new EmpowerCard(emptypeReader(type),name,Integer.valueOf(id),desc,colorReader(col));
        }
        return card;
    }

    /**
     * Takes a type and returns an EmpowerType
     * @param type is emp's type
     * @return empowerType associated.
     */
    private static EmpowerType emptypeReader(String type){

        switch (type){
            case TAGBACK_GRANADE:
                return EmpowerType.TAGBACK_GRANADE;
            case TELEPORTER:
                return EmpowerType.TELEPORTER;
            case TARGETING_SCOPE:
                return EmpowerType.TARGETING_SCOPE;
            case NEWTON:
                return EmpowerType.NEWTON;
            default:
                return null;
        }
    }


    /**
     * This PUBLIC STATIC method reads from FILE ammocard.xml
     * every Ammunition Card and returns a deck of those cards.
     * @return a list of ammunition card
     */
    public static List<AmmunitionCard> forgeAmmoDeck() {
        List<AmmunitionCard> ammoDeck = new ArrayList<AmmunitionCard>();
        try {
            ClassLoader cl = XMLHelper.class.getClassLoader();
            InputStream ammoFile;
            ammoFile=cl.getResourceAsStream(AMMO_FILE_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document ammodoc = dBuilder.parse(ammoFile);
            ammodoc.getDocumentElement().normalize();
            NodeList nodeList = ammodoc.getElementsByTagName(AMMO);
            for (int i = 0; i < nodeList.getLength(); i++)
                ammoDeck.add(getAmmunition(nodeList.item(i)));
            return ammoDeck;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return ammoDeck;
    }

    /**
     * This PRIVATE STATIC Method is used by forgeAmmoDeck() to create a AmmoCubes Object
     * and uses it for creating an AmmunitionCard.
     */
    private static AmmunitionCard getAmmunition(Node node) {
        AmmunitionCard card = null;
        AmmoCubes ammo;
        if (node.getNodeType() == node.ELEMENT_NODE) {
            Element element = (Element) node;
            String id = element.getElementsByTagName(ID).item(0).getTextContent();
            String numYellow = element.getElementsByTagName(NUM_YELLOW).item(0).getTextContent();
            String numRed = element.getElementsByTagName(NUM_RED).item(0).getTextContent();
            String numBlue = element.getElementsByTagName(NUM_BLUE).item(0).getTextContent();
            String hasPowerup = element.getElementsByTagName(HAS_POWERUP).item(0).getTextContent();
            ammo = new AmmoCubes(Integer.valueOf(numRed), Integer.valueOf(numBlue), Integer.valueOf(numYellow));
            if (boolReader(hasPowerup))
                card = new AmmunitionCard(ammo,Integer.valueOf(id), true);
            else
                card = new AmmunitionCard(ammo,Integer.valueOf(id), false);
        }
        return card;
    }

    /**
     * This method gets a weapon's ultra effect from XML file
     * @param node is a node in XML file
     * @return an Ultra Damage that is an Ultra effect.
     */
    private static UltraDamage getUltraDamage(Node node) {
        UltraDamage ultraDamage = null;
        UltraDamage ultraultraDamage = null;
        AmmoCubes cost = null;
        if (node.getNodeType() == node.ELEMENT_NODE) {
            Element element = (Element) node;
            String hasAnother = element.getElementsByTagName(HAS_ANOTHER).item(0).getTextContent();
            if (boolReader(hasAnother)) {
                NodeList nodoultra = element.getElementsByTagName(ULTRA2);
                ultraultraDamage = getUltraDamage(nodoultra.item(0));
            }
            String nameread = element.getElementsByTagName(NAME2).item(0).getTextContent();
            cost = getAmmoCube(node, 2);
            String descriptionread = element.getElementsByTagName(DESCRIPTION_UP).item(0).getTextContent();
            String dmgread = element.getElementsByTagName(DAMAGE).item(0).getTextContent();
            String markread = element.getElementsByTagName(MARK).item(0).getTextContent();
            String maxTargetsread = element.getElementsByTagName(MAX_TARGETS).item(0).getTextContent();
            String chainread = element.getElementsByTagName(CHAIN).item(0).getTextContent();
            String oldTargetsread = element.getElementsByTagName(OLD_TARGETS_SHOOT).item(0).getTextContent();
            String arearead = element.getElementsByTagName(AREA_DAMAGE).item(0).getTextContent();
            String vortexread = element.getElementsByTagName(VORTEX).item(0).getTextContent();
            String duringBase=  element.getElementsByTagName(DURING_BASE).item(0).getTextContent();
            ultraDamage = new UltraDamage(nameread, descriptionread, cost, Integer.valueOf(dmgread), Integer.valueOf(markread),
                    ultraultraDamage, Integer.valueOf(maxTargetsread), boolReader(chainread), boolReader(oldTargetsread),
                    boolReader(arearead), boolReader(vortexread),boolReader(duringBase));
        }
        return ultraDamage;
    }

    /**
     * This gets a weapon's effect from the XML file.
     * @param node is a node from XML file.
     * @return a ShootEffect
     */
    public static ShootEffect getEffect(Node node) {

        ShootEffect effect = null;
        if (node.getNodeType() == node.ELEMENT_NODE) {
            Element element = (Element) node;
            UltraDamage ultra = null;
            AmmoCubes cost;
            String name = element.getElementsByTagName(NAME2).item(0).getTextContent();
            String effectread = element.getElementsByTagName("typeEffect").item(0).getTextContent();
            String descriptionread = element.getElementsByTagName(DESCRIPTION_UP).item(0).getTextContent();
            String dmgread = element.getElementsByTagName(DAMAGE).item(0).getTextContent();
            String markread = element.getElementsByTagName(MARK).item(0).getTextContent();
            String numYellow = element.getElementsByTagName(YELLOW_LOWER).item(0).getTextContent();
            String numRed = element.getElementsByTagName(RED_LOWER).item(0).getTextContent();
            String numBlue = element.getElementsByTagName(BLUE_LOWER).item(0).getTextContent();
            cost = new AmmoCubes(Integer.valueOf(numRed), Integer.valueOf(numBlue), Integer.valueOf(numYellow));
            String weaponType = element.getElementsByTagName("weaponType").item(0).getTextContent();
            if (weaponType.equals("OptionalEffect")) {
                NodeList ultraList = element.getElementsByTagName("ultra");
                ultra = getUltraDamage(ultraList.item(0));
            }
            switch (effectread) {
                case "NormalShoot":
                    effect = getNormalShot(element, name, descriptionread, dmgread, markread, cost, ultra);
                    break;
                case "RangedShoot":
                    effect=getRangedShot(element, name, descriptionread, dmgread, markread, cost, ultra);
                    break;
                case "DirectionalShoot":
                    effect=getDirectionalShot(element, name, descriptionread, dmgread, markread, cost, ultra);
                    break;
                case "LaserShoot":
                    effect=getLaserShot(element, name, descriptionread, dmgread, markread, cost, ultra);
                    break;
                case "MultipleShoot":
                    effect=getMultipleShot(element, name, descriptionread, dmgread, markread, cost, ultra);
                    break;
                case "MultipleDirectionalShoot":
                    effect=getMultipleDirectionalShoot(element, name, descriptionread, dmgread, markread, cost, ultra);
                    break;
                case "WaveShoot":
                    effect=getWaveShoot(element, name, descriptionread, dmgread, markread, cost, ultra);
                    break;
                case "WaveMultipleShoot":
                    effect=getWaveMultipleShoot(name, descriptionread, dmgread, markread, cost, ultra);
                    break;
                case "VortexShoot":
                    effect=getVortexShoot(element, name, descriptionread, dmgread, markread, cost, ultra);
                    break;
                case "ExplosiveShoot":
                    effect=getExplosiveShoot(element, name, descriptionread, dmgread, markread, cost, ultra);
                    break;
                default:
                    break;
            }
        }
        return effect;
    }

    /**
     *This method is used in getEffect and returns a NormalShoot effect.
     **/
    private static NormalShoot getNormalShot(Element element, String name, String desc, String damage, String mark, AmmoCubes cost, UltraDamage ultra) {
        String shootVis = element.getElementsByTagName(SHOOT_VISIBLE).item(0).getTextContent();
        String maxTargets = element.getElementsByTagName(MAX_TARGETS).item(0).getTextContent();
        String recoil = element.getElementsByTagName(RECOIL).item(0).getTextContent();
        RecoilMovement recoilMovement = null;
        if (boolReader(recoil)) {
            String steps = element.getElementsByTagName(STEPS).item(0).getTextContent();
            recoilMovement = new RecoilMovement(Integer.valueOf(steps));
        }
        return new NormalShoot(name, desc, cost, Integer.valueOf(damage), Integer.valueOf(mark), recoilMovement, ultra, boolReader(shootVis), Integer.valueOf(maxTargets));
    }

    /**
     *This method is used in getEffect and returns a RangedShoot effect
     **/
    private static RangedShoot getRangedShot(Element element, String name, String description, String damage, String mark, AmmoCubes cost, UltraDamage ultra) {
        String shootVis = element.getElementsByTagName(SHOOT_VISIBLE).item(0).getTextContent();
        String maxTargets = element.getElementsByTagName(MAX_TARGETS).item(0).getTextContent();
        String minRange = element.getElementsByTagName(MIN_RANGE).item(0).getTextContent();
        String maxRange = element.getElementsByTagName(MAX_RANGE).item(0).getTextContent();
        String recoil = element.getElementsByTagName(RECOIL).item(0).getTextContent();
        RecoilMovement recoilMovement=null;
        if (boolReader(recoil)) {
            String steps = element.getElementsByTagName(STEPS).item(0).getTextContent();
            recoilMovement = new RecoilMovement(Integer.valueOf(steps));
        }
        return new RangedShoot(name, description, cost, Integer.valueOf(damage),
                Integer.valueOf(mark), recoilMovement, ultra, boolReader(shootVis), Integer.valueOf(maxTargets),
                Integer.valueOf(minRange), Integer.valueOf(maxRange));
    }

    /**
     *This method is used in getEffect and returns a DirectionalShoot effect
     **/
    private static DirectionalShoot getDirectionalShot(Element element, String name, String description, String damage, String mark, AmmoCubes cost, UltraDamage ultra){
        RecoilMovement recoilMovement=null;

        String decreased = element.getElementsByTagName(DECREASED).item(0).getTextContent();
        String cyber = element.getElementsByTagName(CYBER).item(0).getTextContent();
        String maxTargets = element.getElementsByTagName(MAX_TARGETS).item(0).getTextContent();
        String maxRange = element.getElementsByTagName(MAX_RANGE).item(0).getTextContent();

        return  new DirectionalShoot(name, description, cost, Integer.valueOf(damage), Integer.valueOf(mark), recoilMovement,
                ultra, Integer.valueOf(maxTargets), Integer.valueOf(maxRange), boolReader(decreased), boolReader(cyber));
    }

    /**
     *This method is used in getEffect and returns a LaserShoot effect
     **/
    private static LaserShoot getLaserShot(Element element, String name, String description, String damage, String mark, AmmoCubes cost, UltraDamage ultra){

        RecoilMovement recoilMovement=null;
        String maxTargets = element.getElementsByTagName(MAX_TARGETS).item(0).getTextContent();
        return new LaserShoot(name, description, cost, Integer.valueOf(damage),
                Integer.valueOf(mark), recoilMovement, ultra, Integer.valueOf(maxTargets));
    }

    /**
     *This method is used in getEffect and returns a MultipleShoot effect
     **/
    private static MultipleShoot getMultipleShot(Element element, String name, String description, String damage, String mark, AmmoCubes cost, UltraDamage ultra){
        String minRange = element.getElementsByTagName(MIN_RANGE).item(0).getTextContent();
        String maxRange = element.getElementsByTagName(MAX_RANGE).item(0).getTextContent();
        String room = element.getElementsByTagName(ROOM).item(0).getTextContent();
        return new MultipleShoot(name, description, cost, Integer.valueOf(damage),
                Integer.valueOf(mark), ultra, Integer.valueOf(minRange), Integer.valueOf(maxRange),
                boolReader(room));
    }
    /**
     *This method is used in getEffect and returns a WaveSHoot effect
     **/
    private static WaveShoot getWaveShoot(Element element, String name, String description, String damage, String mark, AmmoCubes cost, UltraDamage ultra) {
        String maxTargets = element.getElementsByTagName(MAX_TARGETS).item(0).getTextContent();
        return new WaveShoot(name, description, cost, Integer.valueOf(damage),
                Integer.valueOf(mark), ultra, Integer.valueOf(maxTargets));

    }


    /**
     *This method is used in getEffect and returns a WaveMultipleShoot effect
     **/
    private static WaveMultipleShoot getWaveMultipleShoot(String name, String description, String damage, String mark, AmmoCubes cost, UltraDamage ultra) {
        return new WaveMultipleShoot(name, description, cost, Integer.valueOf(damage),
                Integer.valueOf(mark), ultra);
    }


    /**
     *This method is used in getEffect and returns a MultipleDirectionalShoot effect
     **/
    private static MultipleDirectionalShoot getMultipleDirectionalShoot(Element element, String name, String description, String damage, String mark, AmmoCubes cost, UltraDamage ultra){
        String maxRange = element.getElementsByTagName(MAX_RANGE).item(0).getTextContent();
        String decreased = element.getElementsByTagName(DECREASED).item(0).getTextContent();
        return new MultipleDirectionalShoot(name, description, cost, Integer.valueOf(damage),
                Integer.valueOf(mark), ultra, Integer.valueOf(maxRange), boolReader(decreased));
    }
    /**
     *This method is used in getEffect and returns an VortexShoot effect
     **/
    private static VortexShoot getVortexShoot(Element element, String name, String description, String damage, String mark, AmmoCubes cost, UltraDamage ultra){
        String tarsteps = element.getElementsByTagName(TARGETS_STEPS).item(0).getTextContent();
        String minRange = element.getElementsByTagName(MIN_RANGE).item(0).getTextContent();
        String maxRange = element.getElementsByTagName(MAX_RANGE).item(0).getTextContent();
        return new VortexShoot(name, description, cost, Integer.valueOf(damage), Integer.valueOf(mark),
                ultra, Integer.valueOf(minRange), Integer.valueOf(maxRange), Integer.valueOf(tarsteps));
    }


    /**
     *This method is used in getEffect and returns an ExplosiveShoot effect
     **/
    private static ExplosiveShoot getExplosiveShoot(Element element, String name, String description, String damage, String mark, AmmoCubes cost, UltraDamage ultra){
        String areadmg = element.getElementsByTagName(AREA_DAMAGE).item(0).getTextContent();
        String areamarks = element.getElementsByTagName(AREA_MARKS).item(0).getTextContent();
        return new ExplosiveShoot(name, description, cost, Integer.valueOf(damage),
                Integer.valueOf(mark), Integer.valueOf(areadmg), Integer.valueOf(areamarks), ultra);
    }

    /**
     *This Method is called by forgeWeaponDeck and reads every weapon from XML-file
     **/
    private static WeaponCard getWeapon(Node node) {
        WeaponCard card = null;
        List<ShootEffect> effects = new ArrayList<>();
        AmmoCubes ammobuy;
        AmmoCubes ammorec;
        AmmoCubes ammomove;
        ShooterMovement movement = null;

        if (node.getNodeType() == node.ELEMENT_NODE) {
            Element element = (Element) node;
            String name = element.getElementsByTagName(NAME).item(0).getTextContent();
            String id = element.getElementsByTagName(ID).item(0).getTextContent();
            String color = element.getElementsByTagName(COLOR).item(0).getTextContent();
            ammobuy = getAmmoCube(node, 0);
            ammorec = getAmmoCube(node, 1);
            String allowsMovement = element.getElementsByTagName(MOVE_EFFECT).item(0).getTextContent();
            NodeList effectsList = element.getElementsByTagName(EFFECT);
            for (int i = 0; i < effectsList.getLength(); i++)
                effects.add(getEffect(effectsList.item(i)));
            if (boolReader(allowsMovement)) {
                String nameMovement = element.getElementsByTagName(MOVEMENT_NAME).item(0).getTextContent();
                String descMovement = element.getElementsByTagName(MOVEMENT_DESCRIPTION).item(0).getTextContent();
                String stepsMovement = element.getElementsByTagName(MOVEMENT_STEPS).item(0).getTextContent();
                ammomove = getAmmoCube(node, 3);
                movement = new ShooterMovement(nameMovement, descMovement, ammomove, Integer.valueOf(stepsMovement));
            }
            card = new WeaponCard(name,Integer.valueOf(id),colorReader(color), ammobuy, ammorec, effects, movement);
        }
        return card;
    }

    /**
     *This method returns a List of Weapon-Cards reading an XML FILE.
     * @return a list of weapon card
     **/
    public static List<WeaponCard> forgeWeaponDeck() {
        List<WeaponCard> weaponDeck = new ArrayList<WeaponCard>();
        try {
            ClassLoader cl = XMLHelper.class.getClassLoader();
            InputStream weaponFile;
            weaponFile=cl.getResourceAsStream(WEAPON_FILE_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document weapondoc = dBuilder.parse(weaponFile);
            weapondoc.getDocumentElement().normalize();
            NodeList nodeList = weapondoc.getElementsByTagName(WEAPON);
            for (int i = 0; i < nodeList.getLength(); i++)
                weaponDeck.add(getWeapon(nodeList.item(i)));
            return weaponDeck;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return weaponDeck;
    }

    /**
     *
     * This method takes a string and returns a COLOR
     * @param color is a String that we read
     * @return a Color.
     */
    public static Color colorReader(String color) {
        if (color.equals(RED))
            return Color.RED;
        else if (color.equals(YELLOW))
            return Color.YELLOW;
        else if (color.equals(BLUE))
            return Color.BLUE;
        else if (color.equals(GREEN))
            return Color.GREEN;
        else if (color.equals("GREY"))
            return Color.GREY;
        else if(color.equals(WHITE))
            return Color.WHITE;
        else
            return Color.PURPLE;

    }

    /**
     * Takes a String. if string equals "true" returns true
     * @param value is the string
     * @return true or false.
     */
    private static boolean boolReader(String value) {
        if (value.equals(TRUE)) return true;
        else return false;
    }
    /**
     * Takes a Node and a Cube Type and returns an AmmoCube Configurations
     * @param node is xml node
     * @param type is AMMO cube type
     * @return an AmmoCube
     */
    private static AmmoCubes getAmmoCube(Node node, int type) {

        AmmoCubes ammo = null;
        String numRed = null;
        String numYellow = null;
        String numBlue = null;
        if (node.getNodeType() == node.ELEMENT_NODE) {
            Element element = (Element) node;
            if (type == 0) {
                numYellow = element.getElementsByTagName(NUM_YELLOW).item(0).getTextContent();
                numRed = element.getElementsByTagName(NUM_RED).item(0).getTextContent();
                numBlue = element.getElementsByTagName(NUM_BLUE).item(0).getTextContent();
            } else if (type == 1) {
                numYellow = element.getElementsByTagName(REC_NUM_YELLOW).item(0).getTextContent();
                numRed = element.getElementsByTagName(REC_NUM_RED).item(0).getTextContent();
                numBlue = element.getElementsByTagName(REC_NUM_BLUE).item(0).getTextContent();
            } else if (type == 2) {
                numYellow = element.getElementsByTagName(YELLOW_LOWER).item(0).getTextContent();
                numRed = element.getElementsByTagName(RED_LOWER).item(0).getTextContent();
                numBlue = element.getElementsByTagName(BLUE_LOWER).item(0).getTextContent();
            } else if (type == 3) {
                numYellow = element.getElementsByTagName(MOVE_YELLOW).item(0).getTextContent();
                numRed = element.getElementsByTagName(MOVE_RED).item(0).getTextContent();
                numBlue = element.getElementsByTagName(MOVE_BLUE).item(0).getTextContent();
            }
            ammo = new AmmoCubes(Integer.valueOf(numRed), Integer.valueOf(numBlue), Integer.valueOf(numYellow));
        }
        return ammo;
    }
}