package it.polimi.ingsw.client.user_interfaces.gui;

import it.polimi.ingsw.client.user_interfaces.model_representation.*;
import it.polimi.ingsw.model.AmmoCubes;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.cards.AmmunitionCard;
import javafx.application.Platform;
import it.polimi.ingsw.utils.WriterHelper;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class used to draw game sections (map, players sections, messages, alert windows) in GUI
 */
public class GameSession {

    private static final String FINAL_FRENZY_LOGO = "/images/OtherStuff/FinalFrenzyLogo.png";
    private static final String BACKGROUND_STYLE_FF = "backgroundRootFF";
    private static final String ERROR_MESSAGE = "Errore nello sleep del message thread";
    private static final int SECOND = 1000;
    private static final int ONE_SECOND = SECOND;
    private static final int NUM_POINTS_BOARD = 6;
    private static final int MY_PLAYER_SKULL_BOX_TRASL_X = 100;
    private static final int MY_PLAYER_SKULL_BOX_TRASL_Y = 117;
    private static final int MY_PLAYER_SKULL_BOX_PREF_WIDTH = 212;
    private static final String FINAL_FRENZY_SUFFIX = "FF";
    private static final int MY_PLAYER_MARKS_TRASL_X = -130;
    private static final int MY_PLAYER_MARKS_TRASL_Y = 10;
    private static final double MY_SKULL_BOX_SPACING = -4.5;
    private static final double MY_DMG_BOX_SPACING = -1.5;
    private static final int MY_DMG_BOX_TRASL_X = 54;
    private static final int MY_DMG_BOX_TRASL_Y = 55;
    private static final int PLAYER_IMG_SIZE = 45;
    private static final int IMG_INSIDE_MAP_TRASL_X = 10;
    private static final int ENEMY_EMP_WEAP_SPACING = 8;
    private static final String ENEMY_NAME_STYLE = "enemyName";
    private static final String GOLDEN_COLOR = "-fx-text-fill:rgba(202,186,0,0.99);";
    private static final int MY_PLAYER_NAME_TRASL_X = 15;
    private static final int MY_PLAYER_NAME_TRASL_Y = -80;
    private static final int MY_PLAYER_BOARD_TRASL_X = 15;
    private static final int MY_PLAYER_BOARD_WIDTH = 504;
    private static final int MY_PLAYER_BOARD_HEIGHT = 120;
    private static final int ENEMY_PLAYER_SKULLBOX_TRASL_X = 77;
    private static final int ENEMY_PLAYER_SKULLBOX_TRASL_Y = 65;
    private static final int ENEMY_SKULLBOX_SPACING = -9;
    private static final int ENEMY_MARKBOX_TRASL_X = -100;
    private static final double ENEMY_DMGBOX_SPACING = -0.8;
    private static final int ENEMY_DMGBOX_TRASL_Y = 33;
    private static final double ENEMY_DMGBOX_TRASL_X = 30.6;
    private static final int MAP_UPPERPANE_MAX_HEIGHT = 134;
    private static final int MAP_UPPERPANE_PREF_HEIGHT = 130;
    private static final int LATERAL_PANE_MAP_WIDTH = 124;
    private static final int TEXT_STACK_PANE_MAX_WIDTH = 300;
    private static final double TEXT_STACK_PANE_MAX_HEIGHT = 67.57;
    private static final int TEXT_WRAPPING_WIDTH = 370;
    private static final int TEXT_TRASL_X = 305;
    private static final int TEXT_TRASL_Y = -15;
    private static final int MYPLAYER_BOX_SPACING = 20;
    private static final String MAP_SECTION_BORDER_STYLE = "-fx-border-color: grey;-fx-border-width: 3; -fx-border-style: solid;";
    private static final double KILLSHOT_SPACING = -0.97;
    private static final int KILLSHOT_MAX_WIDTH = 100;
    private static final int KILLSHOT_IMAGE_SIZE = 35;
    private static final int KILLSHOT_CELLS_NUMBER = 8;
    private static final int KILLSHOT_TRASL_Y = 37;
    private static final double KILLSHOT_TRASL_X = 50.8;
    private static final int MAP_ADJUST_TRASL_X_CONST = 140;
    private static final int MAP_ADJUST_TRASL_Y_CONST = 50;
    private static final int MYPLAYERBOX_ADJUST_TRASL_X_CONST = 500;
    private static final int ENEMIES_ADJUST_TRASL_Y_CONST = 25;
    private static final int ENEMIES_ADJUST_SPACING_CONST = 90;
    private static final double ENEMIES_SECTION_WIDTH_CONST = 0.25;
    private static final double ENEMY_TRASL_X_ADJUST_CONST = -0.2;
    private static final int ENEMY_SKULL_BOX_FF_SACING = -9;
    private static final int ENEMY_SKULL_BOX_TRASL_Y = 65;
    private static final int ENEMY_SKULL_BOX_TRASL_X = 105;
    private static final int MYPL_SKULLBOX_FF_TRASL_Y = 100;
    private static final int MYPL_SKULLBOX_FF_TRASL_X = 150;
    private static final int MYPL_SKULLBOX_FF_PREF_WIDTH = 212;
    private static final int FF_LOGO_TRASL_Y = 46;
    private static final int FF_LOGO_TRASL_X = 50;
    private static final int FF_LOGO_WIDTH = 300;
    private static final int FF_LOGO_HEIGHT = 150;
    private static final int MYPL_PREF_HEIGHT_ADJUST_CONST = 6;


    private BorderPane primarySections;
    private VBox enemiesVBox;
    private BorderPane mapBorderPane;
    private StackPane mapStackPane;
    private List<Color> enemyColors;
    private Color myPlayerColor;
    private boolean finalFrenzy;


    private static List<ImageView> myWeapons=new ArrayList<>();
    private static List<ImageView> myEmpowers=new ArrayList<>();
    private static List<StackPane> enemiesBoards=new ArrayList<>();
    private static List<HBox> enemiesBoxes=new ArrayList<>();
    private static List<ImageView> blueCards=new ArrayList<>();
    private static List<ImageView> redCards=new ArrayList<>();
    private static List<ImageView> yellowCards=new ArrayList<>();
    private GridPane mapPane;
    private StackPane myPlayerPane;
    private HBox myPlayerBox;
    private StackPane gameStackPaneRoot;
    private StackPane stackPaneRoot;
    private Text messages;
    private HBox killShotTrack;
    private StackPane finalFrenzyPane;

    private static final String EMPOWERS_PATH="/images/Cards/Empowers/emp";
    private static final String WEAPONS_PATH="/images/Cards/weapons/weapon";
    private static final String AMMO_PATH="/images/Cards/ammoCards/ammo";
    private static final String PLAYERS_IMAGE_PATH="/images/Players/";
    private static final String PNG_EXTENSION=".png";

    private static final int MIN_MESSAGE_TIME = 1;

    private static final int CARD_WIDTH=50;
    private static final int CARD_HEIGHT=70;
    private static final int BOARD_WIDTH=400;
    private static final int BOARD_HEIGHT=96;
    private static final String EMP_BACK="/images/Cards/empBack.png";
    private static final String BLU_BOARD="/images/playerBoards/landscape/Plancia_blu.png";
    private static final String PURPLE_BOARD="/images/playerBoards/landscape/Plancia_viola.png";
    private static final String GREEN_BOARD="/images/playerBoards/landscape/Plancia_verde.png";
    private static final String YELLOW_BOARD="/images/playerBoards/landscape/Plancia_gialla.png";
    private static final String GREY_BOARD="/images/playerBoards/landscape/Plancia_grigia.png";
    private static final String BLUE_CUBE="/images/Cubes/blueCube.png";
    private static final String RED_CUBE="/images/Cubes/redCube.png";
    private static final String YELLOW_CUBE="/images/Cubes/yellowCube.png";
    private static final String YELLOW_DAMAGE="/images/damages/yellowDmg.png";
    private static final String GREEN_DAMAGE= "/images/damages/greenDmg.png";
    private static final String BLUE_DAMAGE= "/images/damages/blueDmg.png";
    private static final String PURPLE_DAMAGE="/images/damages/purpleDmg.png";
    private static final String GREY_DAMAGE="/images/damages/greyDmg.png";
    private static final String SKULL="/images/OtherStuff/redSkull.png";
    private static final String CSS_PATH="Style.css";
    private static final String BACKGROUND_STYLE_IMAGE="backgroundRoot";
    private static final String GAME_BOARD_BACKGROUND="backgroundGameBoard";

    private static final int MIN_WINDOW_WIDTH_SIZE=1300;
    private static final int MIN_WINDOW_HEIGHT_SIZE=806;
    private static final int MAP_HEIGTH=669;
    private static final int MAP_GRID_COLUMN_WIDTH=140;
    private static final int MAP_GRID_ROW_HEIGTH=156;
    private static final int MAP_ROW_NUMBER=3;
    private static final int NUM_POINTS_BOARD_FF = 4;
    private static final int MAP_COLUMN_NUMBER=4;
    private static final String FONT_PATH="/font/Ethnocentric.ttf";
    private static final int ENEMY_AMMO_BOX_CELL_SIZE=30;
    private static final int MY_AMMO_BOX_CELL_SIZE=40;
    private static final int ROTATION_ANGLE=90;
    private static final int MY_CARDS_HEIGTH=138;
    private static final int MY_WEAPON_CARD_WIDTH=100;
    private static final int MY_EMPOWER_CARD_WIDTH=95;
    private static final int BLUE_WEAPONS_WIDTH=72;
    private static final int BLUE_WEAPONS_HEIGTH=117;
    private static final int LATERAL_MAP_WEAPONS_WIDTH=82;
    private static final int LATERAL_MAP_WEAPONS_HEIGTH=110;
    private static final double GLOW_EFFECT_COSTANT=0.4;
    private static final int DAMAGE_IMAGE_SIZE=24;
    private static final int ZERO=0;
    private static final int FIRST_BLUE_CARD_TRASL_Y=305;
    private static final int BLUE_CARD_TRASL_GAP=89;
    private static final int RED_CARDS_TRASLATE_X=13;
    private static final int SKULL_AMMO_DAMAGE_SIZE=30;


    public GameSession(StackPane stackPaneRoot) {
        this.gameStackPaneRoot = new StackPane();
        this.primarySections=new BorderPane();
        this.stackPaneRoot = stackPaneRoot;
        this.enemiesVBox=new VBox();
        this.mapBorderPane=new BorderPane();
        this.finalFrenzy=false;
    }

    /**
     *
     * @param mapRepresentation is used to create players positions
     * @param gameBoardRepresentation is used to create enemies damages, marks and deaths
     * @param myPlayerRepresentation is used to create my player damages, marks and deaths
     * @param otherPlayersRep  is used to create my player damages, marks and deaths
     *
     * creates the game window;
     */

    public void createGame(MapRepresentation mapRepresentation,GameBoardRepresentation gameBoardRepresentation,MyPlayerRepresentation myPlayerRepresentation,List<PlayerRepresentation> otherPlayersRep) {

        stackPaneRoot.getChildren().add(gameStackPaneRoot);
        gameStackPaneRoot.getChildren().add(primarySections);
        mapBorderPane=returnMapPane(mapRepresentation.getMapType());
        mapBorderPane.setStyle(MAP_SECTION_BORDER_STYLE);
        mapStackPane=new StackPane();
        mapStackPane.getChildren().add(mapBorderPane);
        mapStackPane.setAlignment(mapBorderPane,Pos.TOP_LEFT);
        primarySections.setCenter(mapStackPane);
        enemyColors=new ArrayList<>();
        for(int i=ZERO;i<otherPlayersRep.size();i++) {
            enemyColors.add(otherPlayersRep.get(i).getColor());
            enemiesVBox.getChildren().add(startEnemyPlayerBoard(otherPlayersRep.get(i).getColor(), otherPlayersRep.get(i).getName()));
            enemiesVBox.getChildren().add(startEnemyPlayerEmpowersAndWeapons());
        }
        primarySections.setRight(enemiesVBox);
        this.gameStackPaneRoot.prefWidthProperty().bind(stackPaneRoot.widthProperty());
        this.gameStackPaneRoot.prefHeightProperty().bind(stackPaneRoot.heightProperty());
        enemiesVBox.setAlignment(Pos.TOP_RIGHT);
        primarySections.getStylesheets().add(CSS_PATH);
        primarySections.getStyleClass().add(BACKGROUND_STYLE_IMAGE);
        myPlayerColor=myPlayerRepresentation.getColor();
        myPlayerBox=new HBox(MYPLAYER_BOX_SPACING);
        myPlayerBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        primarySections.setBottom(myPlayerBox);
        primarySections.setBottom(myPlayerBox);
        StackPane myPlayerStackPane=startMYPlayerBoard(myPlayerRepresentation.getColor(), myPlayerRepresentation.getName());
        myPlayerPane=myPlayerStackPane;
        myPlayerBox.getChildren().add(myPlayerStackPane);
        myPlayerBox.setAlignment(Pos.CENTER_LEFT);
        startMyPlayerEmpowersAndWeapons();
        updateGame(mapRepresentation,otherPlayersRep,myPlayerRepresentation,gameBoardRepresentation.getKillshotTrackRep());
        setMapWeaponsClickable(mapRepresentation);
        setAllPlayersCardsClickable(myPlayerRepresentation);
    }

    /**
     *
     * @param map is used to update players positions
     * @param enemies is used to update enemies damages, marks and deaths
     * @param myPlayer is used to update my player damages, marks and deaths
     * @param killShotTrack is used to update killShootTrack
     *
     */

    public void updateGame(MapRepresentation map, List<PlayerRepresentation> enemies,MyPlayerRepresentation myPlayer,List<Color> killShotTrack){
        updateMap(map,enemies,myPlayer);
        updateEnemyPlayers(enemiesBoards,enemies,enemiesBoxes);
        updateMyPlayer(myPlayer);
        setMapWeaponsClickable(map);
        setAllPlayersCardsClickable(myPlayer);
        updateKillShotTrack(killShotTrack);
    }

    public StackPane getGameStackPaneRoot() {
        return gameStackPaneRoot;
    }

    /**
     *
     * @param mapNumber is used to selct map type
     * @returns map Border Pane divided in 5 sections:
     *          top(weaponsand killshootTrack), left(weapons), right(weapons),
     *          center(map) , bottom(messages space)
     *
     */
    private BorderPane returnMapPane(int mapNumber) {
        BorderPane mapBase=new BorderPane();
        mapBase.setMaxHeight(MAP_HEIGTH);
        mapBase.setMinHeight(MAP_HEIGTH);
        mapBase.setMaxWidth(MIN_WINDOW_HEIGHT_SIZE);
        mapBase.setMinWidth(MIN_WINDOW_HEIGHT_SIZE);

        mapBase.getStylesheets().add(CSS_PATH);
        mapBase.getStyleClass().add(GAME_BOARD_BACKGROUND+mapNumber);
        GridPane grid=new GridPane();
        mapPane =grid;
        grid.setAlignment(Pos.TOP_CENTER);
        grid.getColumnConstraints().add(new ColumnConstraints(MAP_GRID_COLUMN_WIDTH));//first column
        grid.getColumnConstraints().add(new ColumnConstraints(MAP_GRID_COLUMN_WIDTH));//second column
        grid.getColumnConstraints().add(new ColumnConstraints(MAP_GRID_COLUMN_WIDTH));//third column
        grid.getColumnConstraints().add(new ColumnConstraints(MAP_GRID_COLUMN_WIDTH));//fourth column
        grid.getRowConstraints().add(new RowConstraints(MAP_GRID_ROW_HEIGTH));
        grid.getRowConstraints().add(new RowConstraints(MAP_GRID_ROW_HEIGTH));
        grid.getRowConstraints().add(new RowConstraints(MAP_GRID_ROW_HEIGTH));
        for (int i=ZERO;i<MAP_COLUMN_NUMBER;i++)
            for (int j=ZERO;j<MAP_ROW_NUMBER;j++) {
                grid.add(createLittleGrid(grid.getColumnConstraints().get(ZERO),grid.getRowConstraints().get(ZERO)), i, j);
            }
        mapBase.setCenter(grid);
        StackPane upperPane=new StackPane();
        upperPane.setAlignment(Pos.TOP_CENTER);
        upperPane.setMaxHeight(MAP_UPPERPANE_MAX_HEIGHT);
        upperPane.setPrefHeight(MAP_UPPERPANE_PREF_HEIGHT);
        mapBase.setTop(upperPane);
        StackPane rightStackPane=new StackPane();
        rightStackPane.setMaxWidth(LATERAL_PANE_MAP_WIDTH);
        rightStackPane.setPrefWidth(LATERAL_PANE_MAP_WIDTH);
        mapBase.setRight(rightStackPane);
        StackPane leftStackPane=new StackPane();
        rightStackPane.setAlignment(Pos.CENTER);
        leftStackPane.setMaxWidth(LATERAL_PANE_MAP_WIDTH);
        leftStackPane.setPrefWidth(LATERAL_PANE_MAP_WIDTH);
        mapBase.setLeft(leftStackPane);
        addBlueCards(upperPane);
        addKillShotTrack(upperPane);
        addRedCards(leftStackPane);
        addYellowCards(rightStackPane);
        StackPane textStackPane=new StackPane();
        textStackPane.setMaxWidth(TEXT_STACK_PANE_MAX_WIDTH);
        textStackPane.setMaxHeight(TEXT_STACK_PANE_MAX_HEIGHT);
        textStackPane.setAlignment(Pos.CENTER);
        messages=new Text();
        messages.setFill(javafx.scene.paint.Color.WHITE);
        messages.setFont(Font.loadFont(getClass().getResourceAsStream(FONT_PATH),12));
        messages.setWrappingWidth(TEXT_WRAPPING_WIDTH);
        textStackPane.setAlignment(messages,Pos.CENTER_RIGHT);
        textStackPane.getChildren().add(messages);
        mapBase.setBottom(textStackPane);
        messages.setTranslateX(TEXT_TRASL_X);
        messages.setTranslateY(TEXT_TRASL_Y);
        finalFrenzyPane=upperPane;
        messages.setStyle("-fx-text-fill:white;");
        return mapBase;
    }


    /**
     *
     * @param upperPane is used to put blue cards in the map
     * creates blue card's image view in map secton
     *
     */

    private void addBlueCards(StackPane upperPane){
        ImageView firstBlueCard=new ImageView();
        firstBlueCard.setFitWidth(BLUE_WEAPONS_WIDTH);
        firstBlueCard.setFitHeight(BLUE_WEAPONS_HEIGTH);
        firstBlueCard.setTranslateX(-FIRST_BLUE_CARD_TRASL_Y);
        firstBlueCard.setOnMouseEntered(e->firstBlueCard.setEffect(new Glow(GLOW_EFFECT_COSTANT)));
        firstBlueCard.setOnMouseExited(e->firstBlueCard.setEffect(new Glow(ZERO)));


        ImageView secBlueCard=new ImageView();
        secBlueCard.setFitWidth(BLUE_WEAPONS_WIDTH);
        secBlueCard.setFitHeight(BLUE_WEAPONS_HEIGTH);
        secBlueCard.setTranslateX(-FIRST_BLUE_CARD_TRASL_Y+BLUE_CARD_TRASL_GAP);
        secBlueCard.setOnMouseEntered(e->secBlueCard.setEffect(new Glow(GLOW_EFFECT_COSTANT)));
        secBlueCard.setOnMouseExited(e->secBlueCard.setEffect(new Glow(ZERO)));

        ImageView thirdBlueCard=new ImageView();
        thirdBlueCard.setFitWidth(BLUE_WEAPONS_WIDTH);
        thirdBlueCard.setFitHeight(BLUE_WEAPONS_HEIGTH);
        thirdBlueCard.setTranslateX(-FIRST_BLUE_CARD_TRASL_Y+2*BLUE_CARD_TRASL_GAP);
        thirdBlueCard.setOnMouseEntered(e->thirdBlueCard.setEffect(new Glow(GLOW_EFFECT_COSTANT)));
        thirdBlueCard.setOnMouseExited(e->thirdBlueCard.setEffect(new Glow(ZERO)));

        blueCards=new ArrayList<>();
        blueCards.add(firstBlueCard);
        blueCards.add(secBlueCard);
        blueCards.add(thirdBlueCard);

        upperPane.getChildren().addAll(firstBlueCard,secBlueCard,thirdBlueCard);
        upperPane.setAlignment(firstBlueCard,Pos.TOP_RIGHT);
        upperPane.setAlignment(secBlueCard,Pos.TOP_RIGHT);
        upperPane.setAlignment(thirdBlueCard,Pos.TOP_RIGHT);
    }

    /**
     *
     * @param leftPane is used to put red cards in the map
     * creates red card's image view in map secton
     *
     */
    private void addRedCards(StackPane leftPane){
        ImageView firstRedCard=new ImageView();
        firstRedCard.setFitHeight(LATERAL_MAP_WEAPONS_HEIGTH);
        firstRedCard.setFitWidth(LATERAL_MAP_WEAPONS_WIDTH);
        firstRedCard.setTranslateY(96);
        firstRedCard.setTranslateX(RED_CARDS_TRASLATE_X);
        firstRedCard.setOnMouseEntered(e->firstRedCard.setEffect(new Glow(GLOW_EFFECT_COSTANT)));
        firstRedCard.setOnMouseExited(e->firstRedCard.setEffect(new Glow(ZERO)));
        ImageView secRedCard=new ImageView();
        secRedCard.setFitHeight(LATERAL_MAP_WEAPONS_HEIGTH);
        secRedCard.setFitWidth(LATERAL_MAP_WEAPONS_WIDTH);
        secRedCard.setTranslateY(193);
        secRedCard.setTranslateX(RED_CARDS_TRASLATE_X);
        secRedCard.setOnMouseEntered(e->secRedCard.setEffect(new Glow(GLOW_EFFECT_COSTANT)));
        secRedCard.setOnMouseExited(e->secRedCard.setEffect(new Glow(ZERO)));
        ImageView thirdRedCard=new ImageView();
        thirdRedCard.setFitHeight(LATERAL_MAP_WEAPONS_HEIGTH);
        thirdRedCard.setFitWidth(LATERAL_MAP_WEAPONS_WIDTH);
        thirdRedCard.setTranslateY(292);
        thirdRedCard.setTranslateX(RED_CARDS_TRASLATE_X);
        thirdRedCard.setOnMouseEntered(e->thirdRedCard.setEffect(new Glow(GLOW_EFFECT_COSTANT)));
        thirdRedCard.setOnMouseExited(e->thirdRedCard.setEffect(new Glow(ZERO)));
        redCards=new ArrayList<>();
        redCards.add(firstRedCard);
        redCards.add(secRedCard);
        redCards.add(thirdRedCard);

        leftPane.getChildren().addAll(firstRedCard,secRedCard,thirdRedCard);
        leftPane.setAlignment(firstRedCard,Pos.TOP_LEFT);
        leftPane.setAlignment(secRedCard,Pos.TOP_LEFT);
        leftPane.setAlignment(thirdRedCard,Pos.TOP_LEFT);
        redCards.get(0).rotateProperty().setValue(ROTATION_ANGLE);
        redCards.get(1).rotateProperty().setValue(ROTATION_ANGLE);
        redCards.get(2).rotateProperty().setValue(ROTATION_ANGLE);
    }


    /**
     *
     * @param rightPane is used to put yellow cards in the map
     * creates yellow card's image view in map secton
     *
     */
    private void addYellowCards(StackPane rightPane){

        ImageView firstYellowCard=new ImageView();
        firstYellowCard.setFitHeight(LATERAL_MAP_WEAPONS_HEIGTH);
        firstYellowCard.setFitWidth(LATERAL_MAP_WEAPONS_WIDTH);
        firstYellowCard.setTranslateY(234);
        firstYellowCard.setTranslateX(-15);
        firstYellowCard.setOnMouseEntered(e->firstYellowCard.setEffect(new Glow(GLOW_EFFECT_COSTANT)));
        firstYellowCard.setOnMouseExited(e->firstYellowCard.setEffect(new Glow(ZERO)));

        ImageView secYellowCard=new ImageView();
        secYellowCard.setFitHeight(LATERAL_MAP_WEAPONS_HEIGTH);
        secYellowCard.setFitWidth(LATERAL_MAP_WEAPONS_WIDTH);
        secYellowCard.setTranslateY(326);
        secYellowCard.setTranslateX(-13);
        secYellowCard.setOnMouseEntered(e->secYellowCard.setEffect(new Glow(GLOW_EFFECT_COSTANT)));
        secYellowCard.setOnMouseExited(e->secYellowCard.setEffect(new Glow(ZERO)));

        ImageView thirdYellowCard=new ImageView();
        thirdYellowCard.setFitHeight(LATERAL_MAP_WEAPONS_HEIGTH);
        thirdYellowCard.setFitWidth(LATERAL_MAP_WEAPONS_WIDTH);
        thirdYellowCard.setTranslateY(428);
        thirdYellowCard.setTranslateX(-13);
        thirdYellowCard.setOnMouseEntered(e->thirdYellowCard.setEffect(new Glow(GLOW_EFFECT_COSTANT)));
        thirdYellowCard.setOnMouseExited(e->thirdYellowCard.setEffect(new Glow(ZERO)));

        yellowCards=new ArrayList<>();
        yellowCards.add(firstYellowCard);
        yellowCards.add(secYellowCard);
        yellowCards.add(thirdYellowCard);

        rightPane.getChildren().addAll(firstYellowCard,secYellowCard,thirdYellowCard);
        rightPane.setAlignment(firstYellowCard,Pos.TOP_RIGHT);
        rightPane.setAlignment(secYellowCard,Pos.TOP_RIGHT);
        rightPane.setAlignment(thirdYellowCard,Pos.TOP_RIGHT);
        yellowCards.get(ZERO).rotateProperty().setValue(ROTATION_ANGLE);
        yellowCards.get(1).rotateProperty().setValue(ROTATION_ANGLE);
        yellowCards.get(2).rotateProperty().setValue(ROTATION_ANGLE);

    }



    /**
     *
     * @param map is used to get map information from the server
     * @param enemies is used to get enemies information from the server
     * @param myplayer is used to get myPlayer information from the server
     *
     *  updates Map section
     */
    public void updateMap(MapRepresentation map,List<PlayerRepresentation> enemies,MyPlayerRepresentation myplayer){
        updateWeapons(map.getYellowWeapons(),map.getRedWeapons(),map.getBlueWeapons());
        updateMapAmmoCards(mapPane,map.getAmmosInPosition());
        updateMapPlayers(mapPane,enemies,myplayer);
    }

    /**
     *
     * @param mapPane is used to get exact position of the screen where insert ammutinations card
     * @param map is used to get ammutination card's game square
     *
     *  updates ammo cards in the map
     */

    public void updateMapAmmoCards(GridPane mapPane, Map<Position, AmmunitionCard> map){
        GridPane smallGrid;
        for (Node children:mapPane.getChildren()) {
            ((GridPane) children).getChildren().clear();
        }
        for(Map.Entry<Position, AmmunitionCard> pos_ammo : map.entrySet()) {
            int x = pos_ammo.getKey().getRow();
            int y = pos_ammo.getKey().getColumn();

            smallGrid = (GridPane)getNodeByRowColumnIndex(x,y,mapPane);
            if(pos_ammo.getValue()!=null){
                smallGrid.add(getAmmoCardImage(pos_ammo.getValue().getId()),ZERO,ZERO);

            }
        }
    }

    /**
     *
     * @param row is the row of gridPane where search something
     * @param column is the column of gridPane where search something
     * @param gridPane is the GridPane where search elements
     *
     * @return node in the row and column of gridpane
     */
    private Node getNodeByRowColumnIndex (int row,int column, GridPane gridPane) {
        Node result = null;
        List<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    /**
     *
     * @param mapPane is the map section in the window where update players position
     * @param enemies is used to get enemies map position
     * @param myplayer is used to get myplayer map position
     *
     * updates all players position
     */

    public void updateMapPlayers(GridPane mapPane,List<PlayerRepresentation> enemies,MyPlayerRepresentation myplayer) {
        GridPane smallGrid;
        int i;
        int j;
        Boolean completed=false;
        for (PlayerRepresentation p : enemies) {
            Position playerPos = p.getPosition();
            if (playerPos != null) {
                int x = playerPos.getRow();
                int y = playerPos.getColumn();
                smallGrid = (GridPane)getNodeByRowColumnIndex(x,y,mapPane);
                for (i=ZERO;i<3&&!completed;i++) {
                    for (j = ZERO; j < 3&&!completed; j++)
                        if (getNodeByRowColumnIndex(i,j,smallGrid)==null&&(i!=ZERO||j!=ZERO)) {
                            smallGrid.add(getPlayerImage(p.getColor()),j,i);
                            completed=true;
                        }
                }
                completed=false;
            }
        }
        if (myplayer.getPosition() != null) {
            int x = myplayer.getPosition().getRow();
            int y = myplayer.getPosition().getColumn();
            smallGrid = (GridPane) getNodeByRowColumnIndex (x,y,mapPane);
            completed=false;
            for (i=1;i<3&&!completed;i++) {
                for (j = 1; j < 3&&!completed; j++)
                    if (getNodeByRowColumnIndex(i, j, smallGrid) == null&&(i!=ZERO||j!=ZERO)) {
                        smallGrid.add(getPlayerImage(myplayer.getColor()), j, i);
                        completed=true;
                    }
            }
        }
    }


    /**
     *
     * @param blueWeapons is used to update map's blue weapons images
     *
     *
     * updates all images of blue weapons
     */
    private void updateBlueWeapons(List<WeaponInfo> blueWeapons){
        if(!blueWeapons.isEmpty())
            blueCards.get(ZERO).setImage(new Image(getClass().getResourceAsStream(WEAPONS_PATH+blueWeapons.get(0).getId()+PNG_EXTENSION)));
        else
            blueCards.get(ZERO).setImage(null);
        if(blueWeapons.size()>1)
            blueCards.get(1).setImage(new Image(getClass().getResourceAsStream(WEAPONS_PATH+blueWeapons.get(1).getId()+PNG_EXTENSION)));
        else
            blueCards.get(1).setImage(null);
        if(blueWeapons.size()>2)
            blueCards.get(2).setImage(new Image(getClass().getResourceAsStream(WEAPONS_PATH+blueWeapons.get(2).getId()+PNG_EXTENSION)));
        else
            blueCards.get(2).setImage(null);
    }

    /**
     *
     * @param redWeapons is used to update map's red weapons images
     *
     *
     * updates all images of red weapons
     */
    private void updateRedWeapons(List<WeaponInfo> redWeapons){
        if(!redWeapons.isEmpty())
            redCards.get(ZERO).setImage(new Image(getClass().getResourceAsStream(WEAPONS_PATH+redWeapons.get(ZERO).getId()+PNG_EXTENSION)));
        else
            redCards.get(ZERO).setImage(null);
        if(redWeapons.size()>1)
            redCards.get(1).setImage(new Image(getClass().getResourceAsStream(WEAPONS_PATH+redWeapons.get(1).getId()+PNG_EXTENSION)));
        else
            redCards.get(1).setImage(null);
        if(redWeapons.size()>2)
            redCards.get(2).setImage(new Image(getClass().getResourceAsStream(WEAPONS_PATH+redWeapons.get(2).getId()+PNG_EXTENSION)));
        else
            redCards.get(2).setImage(null);
    }

    /**
     * @param yellowWeapons is used to update map's yellowWeapons weapons images
     * updates all images of yellowWeapons weapons
     */

    private void updateYellowWeapons(List<WeaponInfo> yellowWeapons){
        if(!yellowWeapons.isEmpty())
            yellowCards.get(ZERO).setImage(new Image(getClass().getResourceAsStream(WEAPONS_PATH+yellowWeapons.get(ZERO).getId()+PNG_EXTENSION)));
        else
            yellowCards.get(ZERO).setImage(null);
        if(yellowWeapons.size()>1)
            yellowCards.get(1).setImage(new Image(getClass().getResourceAsStream(WEAPONS_PATH+yellowWeapons.get(1).getId()+PNG_EXTENSION)));
        else
            yellowCards.get(1).setImage(null);
        if(yellowWeapons.size()>2)
            yellowCards.get(2).setImage(new Image(getClass().getResourceAsStream(WEAPONS_PATH+yellowWeapons.get(2).getId()+PNG_EXTENSION)));
        else
            yellowCards.get(2).setImage(null);
    }

    /**
     *
     * @param yellowWeapons is used to update map's yellowWeapons weapons images
     *
     * @param redWeapons is used to update map's red weapons images
     *
     * @param blueWeapons is used to update map's blue weapons images
     *
     * updates all weapon's images
     *
     */


    private void updateWeapons(List<WeaponInfo> yellowWeapons,List<WeaponInfo> redWeapons,List<WeaponInfo> blueWeapons){
        updateBlueWeapons(blueWeapons);
        updateYellowWeapons(yellowWeapons);
        updateRedWeapons(redWeapons);
    }


    /**
     *
     * @param col is used to get col's size
     *
     * @param row is used to get row's size
     *
     * creates a little grid inside a cell of big Map grid
     *            in order to insert ammos and players avatar inside
     *
     */

    private GridPane createLittleGrid(ColumnConstraints col,RowConstraints row){


        GridPane littleGrid=new GridPane();



        littleGrid.getColumnConstraints().add(new ColumnConstraints(col.getPrefWidth()/3));
        littleGrid.getColumnConstraints().add(new ColumnConstraints(col.getPrefWidth()/3));
        littleGrid.getColumnConstraints().add(new ColumnConstraints(col.getPrefWidth()/3));

        littleGrid.getRowConstraints().add(new RowConstraints(row.getPrefHeight()/3));
        littleGrid.getRowConstraints().add(new RowConstraints(row.getPrefHeight()/3));
        littleGrid.getRowConstraints().add(new RowConstraints(row.getPrefHeight()/3));


        return  littleGrid;

    }

    /**
     *
     * @param playerBoard is used to get playerBoard StackPane where insert damages
     *
     *
     * creates a box where put enemy's damages
     */

    private void addEnemyPlayerDamageBox(StackPane playerBoard){
        HBox dmgBox=new HBox();
        dmgBox.setTranslateX(ENEMY_DMGBOX_TRASL_X);
        dmgBox.setTranslateY(ENEMY_DMGBOX_TRASL_Y);
        dmgBox.setSpacing(ENEMY_DMGBOX_SPACING);
        playerBoard.getChildren().add(dmgBox);
    }

    /**
     *
     * @param playerBoard is used to get playerBoard StackPane where insert marks
     *
     *
     * creates a box where put enemy's marks
     */

    private void addEnemyPlayerMarksBox(StackPane playerBoard){
        HBox markBox=new HBox();
        markBox.setTranslateX(ENEMY_MARKBOX_TRASL_X);
        markBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        playerBoard.getChildren().add(markBox);
    }

    /**
     *
     * @param playerBoard is used to get playerBoard StackPane where insert
     *                    death's skulls
     *
     *
     * creates a box where put enemy's skulls
     */
    private void addEnemyPlayerSkull(StackPane playerBoard) {
        HBox skullbox = new HBox();
        skullbox.setSpacing(ENEMY_SKULLBOX_SPACING);
        playerBoard.getChildren().add(skullbox);
        skullbox.setTranslateY(ENEMY_PLAYER_SKULLBOX_TRASL_Y);
        skullbox.setTranslateX(ENEMY_PLAYER_SKULLBOX_TRASL_X);
        for (int i = ZERO; i < NUM_POINTS_BOARD; i++) {
            skullbox.getChildren().add(new ImageView());

        }
    }



    /**
     *
     * @param color is used to set playerBoard's color
     * @param playerName is used to set enemy's name
     *
     * creates an enemy's player board
     */
    private StackPane startEnemyPlayerBoard(Color color, String playerName){
        StackPane playerBoard=new StackPane();
        ImageView boardImage = new ImageView(new Image(getClass().getResourceAsStream(getRelativeBoard(color))));
        playerBoard.setMaxWidth(boardImage.getFitWidth());
        playerBoard.setMaxHeight(boardImage.getFitHeight());
        playerBoard.getChildren().add(boardImage);
        boardImage.setFitHeight(BOARD_HEIGHT);
        boardImage.setFitWidth(BOARD_WIDTH);
        Label name=new Label(playerName);
        name.setFont(Font.loadFont(getClass().getResourceAsStream(FONT_PATH),21));
        name.getStyleClass().add(ENEMY_NAME_STYLE);
        name.setTextFill(javafx.scene.paint.Color.WHITE);
        name.setTextAlignment(TextAlignment.RIGHT);
        name.setTextOverrun(OverrunStyle.ELLIPSIS);
        name.setStyle(GOLDEN_COLOR);

        name.maxWidthProperty().bind(stackPaneRoot.widthProperty().subtract(1250));
        name.translateXProperty().bind(stackPaneRoot.widthProperty().divide(-5));
        playerBoard.getChildren().add(name);
        name.setAlignment(Pos.CENTER_LEFT);
        addEnemyPlayerAmmoBox(playerBoard);
        addEnemyPlayerDamageBox(playerBoard);
        addEnemyPlayerMarksBox(playerBoard);
        addEnemyPlayerSkull(playerBoard);
        enemiesBoards.add(playerBoard);
        return playerBoard;
    }

    /**
     *
     * @param color is used to set playerBoard's color
     * @param playerName is used to set my player's name
     *
     * creates my player's player board
     */

    private StackPane startMYPlayerBoard(Color color, String playerName){
        StackPane playerBoard=new StackPane();
        ImageView boardImage = new ImageView(new Image(getClass().getResourceAsStream(getRelativeBoard(color))));
        playerBoard.getChildren().add(boardImage);
        boardImage.setFitHeight(MY_PLAYER_BOARD_HEIGHT);
        boardImage.setFitWidth(MY_PLAYER_BOARD_WIDTH);
        playerBoard.setAlignment(Pos.CENTER_LEFT);
        boardImage.setTranslateX(MY_PLAYER_BOARD_TRASL_X);
        Label name=new Label(playerName);
        playerBoard.getChildren().add(name);
        playerBoard.setAlignment(name,Pos.CENTER_LEFT);
        name.setTranslateY(MY_PLAYER_NAME_TRASL_Y);
        name.setTranslateX(MY_PLAYER_NAME_TRASL_X);
        name.getStyleClass().add(ENEMY_NAME_STYLE);
        name.setStyle(GOLDEN_COLOR);
        name.setAlignment(playerBoard.getAlignment());
        name.setTextFill(javafx.scene.paint.Color.WHITE);
        name.setFont(Font.loadFont(getClass().getResourceAsStream(FONT_PATH),24));
        addMyPlayerAmmoBox(playerBoard);
        addMyPlayerDamageBox(playerBoard);
        addMyPlayerMarksBox(playerBoard);
        addMyPlayerSkull(playerBoard);
        myPlayerPane=playerBoard;
        return playerBoard;
    }

    /**
     *
     * @param playerBoard is used to get the exact position where create
     *                    an ammo section
     *
     *
     * creates enemy's ammo section
     */
    private void addEnemyPlayerAmmoBox(StackPane playerBoard){
        GridPane ammobox=new GridPane();
        ammobox.getColumnConstraints().add(new ColumnConstraints(ENEMY_AMMO_BOX_CELL_SIZE));
        ammobox.getColumnConstraints().add(new ColumnConstraints(ENEMY_AMMO_BOX_CELL_SIZE));
        ammobox.getColumnConstraints().add(new ColumnConstraints(ENEMY_AMMO_BOX_CELL_SIZE));
        ammobox.getRowConstraints().add(new RowConstraints(ENEMY_AMMO_BOX_CELL_SIZE));
        ammobox.getRowConstraints().add(new RowConstraints(ENEMY_AMMO_BOX_CELL_SIZE));
        ammobox.getRowConstraints().add(new RowConstraints(ENEMY_AMMO_BOX_CELL_SIZE));
        ammobox.setAlignment(Pos.CENTER_RIGHT);
        playerBoard.getChildren().add(ammobox);
    }

    /**
     *
     * @param ammoBox is used to get ammo section reference
     * @param numRed is used to get red's ammos number
     * @param numBlue is used to get blue's ammos number
     * @param numYellow is used to get yellow's ammos number
     *
     * updates enemy's ammo section
     */
    private void updateEnemyPlayerAmmoCube(GridPane ammoBox,int numRed,int numBlue,int numYellow){
        ammoBox.getChildren().clear();
        for(int i=ZERO;i<numBlue;i++) {
            ammoBox.add(getAmmoImage(BLUE_CUBE), i, ZERO);
        }
        for(int i=ZERO;i<numYellow;i++)
            ammoBox.add(getAmmoImage(YELLOW_CUBE),i,1);
        for(int i=ZERO;i<numRed;i++)
            ammoBox.add(getAmmoImage(RED_CUBE),i,2);
    }


    /**
     *
     * @param id is weapon's identifier
     *
     * @return imageView with the correct weapon searched in resources
     */
    private ImageView getWeaponImage(int id){
        return new ImageView(new Image(getClass().getResourceAsStream(WEAPONS_PATH+id+PNG_EXTENSION)));
    }

    /**
     *
     * @param color is board's color
     *
     * @return the correct color string in order to
     * search correctly board's image
     */
    private String getRelativeBoard(it.polimi.ingsw.model.Color color){
        switch (color){
            case YELLOW: return YELLOW_BOARD;
            case BLUE: return BLU_BOARD;
            case PURPLE: return PURPLE_BOARD;
            case GREEN: return GREEN_BOARD;
            case GREY:return GREY_BOARD;
            default:return null;
        }
    }

    /**
     *
     * @param color is damage's color
     *
     * @return the correct color string in order to search
     * correctly damage's image
     */
    private String getRelativeDrop(it.polimi.ingsw.model.Color color){
        switch (color){
            case YELLOW: return YELLOW_DAMAGE;
            case BLUE: return BLUE_DAMAGE;
            case PURPLE: return PURPLE_DAMAGE;
            case GREEN: return GREEN_DAMAGE;
            case GREY:return GREY_DAMAGE;
            default:return null;
        }
    }



    /**
     *
     * @param playerSkullsBox is skulls' section
     * @param numDeaths are enemy's deaths
     *
     * updates enemy's skulls section
     */
    private void updateEnemyPlayerSkulls(HBox playerSkullsBox, int numDeaths){
        if(!finalFrenzy) {
            if (numDeaths <= NUM_POINTS_BOARD)
                for (int i = ZERO; i < numDeaths; i++) {
                    playerSkullsBox.getChildren().set(i, getSkullImage());
                }
            else {
                for (int i = ZERO; i < NUM_POINTS_BOARD; i++) {
                    playerSkullsBox.getChildren().set(i, getSkullImage());
                }
            }
        }else {
            if (numDeaths <= NUM_POINTS_BOARD_FF)
                for (int i = ZERO; i < numDeaths; i++) {
                    playerSkullsBox.getChildren().set(i, getSkullImage());
                }
            else {
                for (int i = ZERO; i < NUM_POINTS_BOARD_FF; i++) {
                    playerSkullsBox.getChildren().set(i, getSkullImage());
                }
            }
        }

    }

    /**
     *
     * @param playerDamageBox is damages' section
     * @param playerDamages is enemy's damages list
     *
     * updates enemy's damages section
     */
    private void updateEnemyPlayerDamage(HBox playerDamageBox,List<Color> playerDamages) {
        playerDamageBox.getChildren().clear();
        ImageView image;
        for (int i = ZERO; i < playerDamages.size(); i++) {
            image = new ImageView();
            image.setFitWidth(DAMAGE_IMAGE_SIZE);
            image.setFitHeight(DAMAGE_IMAGE_SIZE);
            image.setImage(new Image(getClass().getResourceAsStream(getRelativeDrop(playerDamages.get(i)))));
            playerDamageBox.getChildren().add(image);
        }
    }

    /**
     *
     * @param player is player's server representation
     * @param playerPane is enemy's board section
     * @param playerBox enemy player section
     *
     * updates enemy player's section
     */
    private void updateEnemyPlayer(PlayerRepresentation player,StackPane playerPane,HBox playerBox){
        updateEnemyPlayerAmmoCube((GridPane)playerPane.getChildren().get(2),
                player.getAmmoCubes().getRed(),
                player.getAmmoCubes().getBlue(),player.getAmmoCubes().getYellow());
        updateEnemyPlayerDamage((HBox)playerPane.getChildren().get(3),player.getDamages());
        updateEnemyPlayerSkulls((HBox)playerPane.getChildren().get(5),player.getDeaths());

        updateEnemyPlayerDamage((HBox)playerPane.getChildren().get(4),player.getMarks());
        updateEnemyPlayerCardsAndEmpowers(playerBox,player);

    }


    private void updateEnemyPlayers(List<StackPane> playersPane,List<PlayerRepresentation> players,List<HBox> playersBox) {

        for (int i = ZERO; i < playersPane.size(); i++) {
            updateEnemyPlayer(players.get(i), playersPane.get(i), playersBox.get(i));
        }

    }

    /**
     *
     * @param myPlayer is my player server representation
     *
     * updates my player empowers and weapons
     */
    private void updateMyPlayerEmpowersAndWeapons(MyPlayerRepresentation myPlayer){
        int i;
        for(i=ZERO;i<myWeapons.size();i++)
            myWeapons.get(i).setImage(null);
        for(i=ZERO;i<myPlayer.getWeapons().size();i++)
           myWeapons.get(i).setImage(new Image(getClass().getResourceAsStream(WEAPONS_PATH+myPlayer.getWeapons().get(i).getId()+PNG_EXTENSION)));
        for(i=ZERO;i<myEmpowers.size();i++)
            myEmpowers.get(i).setImage(null);
        for(i=ZERO;i<myPlayer.getEmpowers().size();i++)
            myEmpowers.get(i).setImage(new Image(getClass().getResourceAsStream(EMPOWERS_PATH+myPlayer.getEmpowers().get(i).getId()+PNG_EXTENSION)));

    }

    /**
     *
     * @param player is enemy player server representation
     * @param playerBox is the enemy's section
     *
     * updates enemy's empowers and weapons
     */

    private void updateEnemyPlayerCardsAndEmpowers(HBox playerBox, PlayerRepresentation player){

        int playerWeapons=player.getNumWeapons();
        int others;
        int i;
        playerBox.getChildren().clear();
        ImageView image=new ImageView();
        image.setImage(new Image(getClass().getResourceAsStream(EMP_BACK)));
        image.setFitHeight(CARD_HEIGHT);
        image.setFitWidth(CARD_WIDTH);
        playerBox.getChildren().add(image);
        for ( i = ZERO; i <player.getUnloadedWeapons().size(); i++) {
            image = getWeaponImage(player.getUnloadedWeapons().get(i).getId());
            image.setFitWidth(CARD_WIDTH);
            image.setFitHeight(CARD_HEIGHT);
            playerBox.getChildren().add(image);
        }
        others=i;
        for(int j=ZERO;j<playerWeapons-others;j++) {
            image = getWeaponImage(ZERO);
            image.setFitWidth(CARD_WIDTH);
            image.setFitHeight(CARD_HEIGHT);
            playerBox.getChildren().add(image);
        }
    }

    /**
     *
     * creates enemy's image views of weapons and empowers
     */
    private HBox startEnemyPlayerEmpowersAndWeapons(){
        HBox empweapBox=new HBox();
        empweapBox.setSpacing(ENEMY_EMP_WEAP_SPACING);
        ImageView emp1 = new ImageView(new Image(getClass().getResourceAsStream(EMP_BACK)));
        emp1.setFitWidth(CARD_WIDTH);
        emp1.setFitHeight(CARD_HEIGHT);
        ImageView weap1 = new ImageView();
        weap1.setFitWidth(CARD_WIDTH);
        weap1.setFitHeight(CARD_HEIGHT);
        ImageView weap2 = new ImageView();
        weap2.setFitWidth(CARD_WIDTH);
        weap2.setFitHeight(CARD_HEIGHT);
        ImageView weap3 = new ImageView();
        weap3.setFitWidth(CARD_WIDTH);
        weap3.setFitHeight(CARD_HEIGHT);
        empweapBox.getChildren().add(emp1);
        empweapBox.getChildren().add(weap1);
        empweapBox.getChildren().add(weap2);
        empweapBox.getChildren().add(weap3);
        empweapBox.setAlignment(Pos.CENTER_RIGHT);
        enemiesBoxes.add(empweapBox);
        return empweapBox;
    }


    /**
     *
     * creates my player's image views of weapons and empowers
     */
    private void startMyPlayerEmpowersAndWeapons(){
        ImageView weap1 = new ImageView();
        weap1.setFitWidth(MY_WEAPON_CARD_WIDTH);
        weap1.setFitHeight(MY_CARDS_HEIGTH);
        weap1.setOnMouseEntered(e->weap1.setEffect(new Glow(GLOW_EFFECT_COSTANT)));
        weap1.setOnMouseExited(e->weap1.setEffect(new Glow(ZERO)));


        ImageView weap2 = new ImageView();
        weap2.setFitWidth(MY_WEAPON_CARD_WIDTH);
        weap2.setFitHeight(MY_CARDS_HEIGTH);
        weap2.setOnMouseEntered(e->weap2.setEffect(new Glow(GLOW_EFFECT_COSTANT)));
        weap2.setOnMouseExited(e->weap2.setEffect(new Glow(ZERO)));

        ImageView weap3 = new ImageView();
        weap3.setFitWidth(MY_WEAPON_CARD_WIDTH);
        weap3.setFitHeight(MY_CARDS_HEIGTH);
        weap3.setOnMouseEntered(e->weap3.setEffect(new Glow(GLOW_EFFECT_COSTANT)));
        weap3.setOnMouseExited(e->weap3.setEffect(new Glow(ZERO)));

        ImageView emp1 = new ImageView();
        emp1.setFitWidth(MY_EMPOWER_CARD_WIDTH);
        emp1.setFitHeight(MY_CARDS_HEIGTH);
        emp1.setOnMouseEntered(e->emp1.setEffect(new Glow(GLOW_EFFECT_COSTANT)));
        emp1.setOnMouseExited(e->emp1.setEffect(new Glow(ZERO)));

        ImageView emp2 = new ImageView();
        emp2.setFitWidth(MY_EMPOWER_CARD_WIDTH);
        emp2.setFitHeight(MY_CARDS_HEIGTH);
        emp2.setOnMouseEntered(e->emp2.setEffect(new Glow(GLOW_EFFECT_COSTANT)));
        emp2.setOnMouseExited(e->emp2.setEffect(new Glow(ZERO)));

        ImageView emp3 = new ImageView();
        emp3.setFitWidth(MY_EMPOWER_CARD_WIDTH);
        emp3.setFitHeight(MY_CARDS_HEIGTH);
        emp3.setOnMouseEntered(e->emp3.setEffect(new Glow(GLOW_EFFECT_COSTANT)));
        emp3.setOnMouseExited(e->emp3.setEffect(new Glow(ZERO)));

        myPlayerBox.getChildren().addAll(weap1,weap2,weap3,emp1,emp2,emp3);
        myWeapons.add(weap1);
        myWeapons.add(weap2);
        myWeapons.add(weap3);
        myEmpowers.add(emp1);
        myEmpowers.add(emp2);
        myEmpowers.add(emp3);

    }

    /**
     *
     * @param id is ammo's identifier
     *
     * @return imageView with the correct ammo searched in resources
     */

    private ImageView getAmmoCardImage(int id){
        ImageView ammo=new ImageView(new Image(getClass().getResourceAsStream(AMMO_PATH+id+PNG_EXTENSION)));
        ammo.setFitHeight(SKULL_AMMO_DAMAGE_SIZE);
        ammo.setFitWidth(SKULL_AMMO_DAMAGE_SIZE);
        ammo.setTranslateX(IMG_INSIDE_MAP_TRASL_X);
        return ammo;
    }

    /**
     *
     * @param color is player's color
     *
     * @return imageview of the player's avatar
     */
    private ImageView getPlayerImage(Color color){

        ImageView player=new ImageView(new Image(getClass().getResourceAsStream(PLAYERS_IMAGE_PATH+color.getDescription()+PNG_EXTENSION)));
        player.setFitHeight(PLAYER_IMG_SIZE);
        player.setFitWidth(PLAYER_IMG_SIZE);
        player.setTranslateX(IMG_INSIDE_MAP_TRASL_X);
        return player;
    }

    /**
     *
     *
     *
     * @return skull's imageview
     */
    private ImageView getSkullImage(){
        ImageView image=new ImageView(new Image(getClass().getResourceAsStream(SKULL)));
        image.setFitHeight(SKULL_AMMO_DAMAGE_SIZE);
        image.setFitWidth(SKULL_AMMO_DAMAGE_SIZE);
        return image;
    }


    /**
     *
     * @param path is the path of ammo's image
     *
     * @return ammo's imageview
     */
    private ImageView getAmmoImage(String path){
        ImageView image=new ImageView(new Image(getClass().getResourceAsStream(path)));
        image.setFitHeight(SKULL_AMMO_DAMAGE_SIZE-5);
        image.setFitWidth(SKULL_AMMO_DAMAGE_SIZE-5);
        return image;
    }

    /**
     *
     * @param weapons is list of weapons where search description
     * @param id is the weapon's identifier
     *
     * @return a list of string composed by weapon's name and weapon's description
     */
    private List<String> searchWeaponDescription(List<WeaponInfo> weapons,int id){
        List<String> info=new ArrayList<>();
        for (WeaponInfo weaponInfo:weapons){
            if (weaponInfo.getId()==id){
                info.add(weaponInfo.getName());
                info.add(weaponInfo.getWeaponDescription());
                return info;
            }
        }
        return info;
    }

    /**
     *
     * @param empowers is list of empowers where search description
     * @param id is the empower's identifier
     *
     * @return a list of string composed by empower's name and empower's description
     */
    private List<String> searchEmpowerDescription(List<EmpowerInfo> empowers,int id){
        List<String> info=new ArrayList<>();
        for (EmpowerInfo empowerInfo:empowers){
            if (empowerInfo.getId()==id){
                info.add(empowerInfo.getName());
                info.add(empowerInfo.getDescription());
                return info;
            }
        }
        return info;
    }

    /**
     *
     * @param mapRep is the server Map representation
     *
     * sets clickable all weapon's cards of the map (in order to zoom them in a new window)
     */
    private void setMapWeaponsClickable(MapRepresentation mapRep){
        for (WeaponInfo weapon: mapRep.getBlueWeapons()){
            int id=weapon.getId();
            String name =searchWeaponDescription(mapRep.getBlueWeapons(),id).get(ZERO);
            String description=searchWeaponDescription(mapRep.getBlueWeapons(),id).get(1);
            blueCards.get(mapRep.getBlueWeapons().indexOf(weapon)).setOnMouseClicked(e-> AlertBox.zoomCard(blueCards.get(mapRep.getBlueWeapons().indexOf(weapon)),description,name));
        }
        for (WeaponInfo weapon: mapRep.getRedWeapons()){
            int id=weapon.getId();
            String name=searchWeaponDescription(mapRep.getRedWeapons(),id).get(ZERO);
            String description=searchWeaponDescription(mapRep.getRedWeapons(),id).get(1);
            redCards.get(mapRep.getRedWeapons().indexOf(weapon)).setOnMouseClicked(e-> AlertBox.zoomCard(redCards.get(mapRep.getRedWeapons().indexOf(weapon)),description,name));
        }
        for (WeaponInfo weapon: mapRep.getYellowWeapons()){
            int id=weapon.getId();
            String name=searchWeaponDescription(mapRep.getYellowWeapons(),id).get(ZERO);
            String description=searchWeaponDescription(mapRep.getYellowWeapons(),id).get(1);
            yellowCards.get(mapRep.getYellowWeapons().indexOf(weapon)).setOnMouseClicked(e-> AlertBox.zoomCard(yellowCards.get(mapRep.getYellowWeapons().indexOf(weapon)),description,name));
        }
    }

    /**
     *
     * @param myPlayerRepresentation is my player's server representation
     *
     * sets clickable all player's cards of the map (in order to zoom them in a new window)
     */
    private void setAllPlayersCardsClickable(MyPlayerRepresentation myPlayerRepresentation){
        /*
        for (HBox weaponSection:ENEMIES_BOX){
            for (Node card: weaponSection.getChildren()){
                card.setOnMouseClicked(e->AlertBox.zoomCard((ImageView)card));
            }
        }
        */
        for (WeaponInfo weapon:myPlayerRepresentation.getWeapons()){
                int id = weapon.getId();
                String name =searchWeaponDescription(myPlayerRepresentation.getWeapons(), id).get(0);
                String description = searchWeaponDescription(myPlayerRepresentation.getWeapons(), id).get(1);
                myWeapons.get(myPlayerRepresentation.getWeapons().indexOf(weapon)).setOnMouseClicked(e -> AlertBox.zoomCard(myWeapons.get(myPlayerRepresentation.getWeapons().indexOf(weapon)), description,name));
            }

        for (EmpowerInfo empowerInfo:myPlayerRepresentation.getEmpowers()) {
            int id = empowerInfo.getId();
            String name = searchEmpowerDescription(myPlayerRepresentation.getEmpowers(), id).get(0);
            String description = searchEmpowerDescription(myPlayerRepresentation.getEmpowers(), id).get(1);
            myEmpowers.get(myPlayerRepresentation.getEmpowers().indexOf(empowerInfo)).setOnMouseClicked(e -> AlertBox.zoomCard(myEmpowers.get(myPlayerRepresentation.getEmpowers().indexOf(empowerInfo)), description, name));
        }

    }

    /**
     *
     * @param myDamages are my player damages
     * @param myPlayerBox is the my player's section
     *
     * updates my player damages
     */
    private void updateMyPlayerDamage(HBox myPlayerBox,List<Color> myDamages) {
        myPlayerBox.getChildren().clear();
        ImageView image;
        for (int i = ZERO; i < myDamages.size(); i++) {
            image = new ImageView();
            image.setFitWidth(SKULL_AMMO_DAMAGE_SIZE);
            image.setFitHeight(SKULL_AMMO_DAMAGE_SIZE);
            image.setImage(new Image(getClass().getResourceAsStream(getRelativeDrop(myDamages.get(i)))));
            myPlayerBox.getChildren().add(image);
        }
    }

    /**
     * @param playerBoard is my player board section
     * creates my player damages' section
     */
    private void addMyPlayerDamageBox(StackPane playerBoard){
        HBox dmgBox=new HBox();
        playerBoard.getChildren().add(dmgBox);
        dmgBox.setTranslateY(MY_DMG_BOX_TRASL_Y);
        dmgBox.setTranslateX(MY_DMG_BOX_TRASL_X);
        dmgBox.setSpacing(MY_DMG_BOX_SPACING);
    }

    /**
     * @param playerBoard is my player board section
     * creates my player marks' section
     */
    private void addMyPlayerMarksBox(StackPane playerBoard){
        HBox markBox=new HBox();
        markBox.setTranslateX(MY_PLAYER_MARKS_TRASL_X);
        markBox.setTranslateY(MY_PLAYER_MARKS_TRASL_Y);
        markBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        playerBoard.getChildren().add(markBox);
    }

    /**
     * @param playerBoard is my player board section
     * creates my player skulls' section
     */
    private void addMyPlayerSkull(StackPane playerBoard) {
        HBox skullbox = new HBox();
        skullbox.setAlignment(Pos.TOP_LEFT);
        skullbox.setSpacing(MY_SKULL_BOX_SPACING);
        playerBoard.getChildren().add(skullbox);
        playerBoard.setAlignment(skullbox,Pos.CENTER_LEFT);
        skullbox.setTranslateY(MY_PLAYER_SKULL_BOX_TRASL_X);
        skullbox.setTranslateX(MY_PLAYER_SKULL_BOX_TRASL_Y);
        skullbox.setPrefWidth(MY_PLAYER_SKULL_BOX_PREF_WIDTH);
        skullbox.setPrefHeight(SKULL_AMMO_DAMAGE_SIZE);
        for (int i = ZERO; i< NUM_POINTS_BOARD; i++){
            skullbox.getChildren().add(new ImageView());
        }
    }

    /**
     * @param playerBoard is my player board section
     * creates my player ammos' section
     */
    private void addMyPlayerAmmoBox(StackPane playerBoard){
        GridPane ammobox=new GridPane();
        ammobox.setGridLinesVisible(true);
        ammobox.getColumnConstraints().add(new ColumnConstraints(MY_AMMO_BOX_CELL_SIZE));
        ammobox.getColumnConstraints().add(new ColumnConstraints(MY_AMMO_BOX_CELL_SIZE));
        ammobox.getColumnConstraints().add(new ColumnConstraints(MY_AMMO_BOX_CELL_SIZE));
        ammobox.getRowConstraints().add(new RowConstraints(MY_AMMO_BOX_CELL_SIZE));
        ammobox.getRowConstraints().add(new RowConstraints(MY_AMMO_BOX_CELL_SIZE));
        ammobox.getRowConstraints().add(new RowConstraints(MY_AMMO_BOX_CELL_SIZE));
        ammobox.setAlignment(Pos.CENTER_RIGHT);
        ammobox.setTranslateX(SKULL_AMMO_DAMAGE_SIZE);
        playerBoard.getChildren().add(ammobox);


    }
    /**
     * @param ammoBox is my player ammos' section
     * @param myAmmo is my player ammos' representation
     * updates my player ammos' section
     */
    private void updateMyPlayerAmmoCube(GridPane ammoBox, AmmoCubes myAmmo){
        int numBlue,numYellow,numRed;
        numBlue=myAmmo.getBlue();
        numRed=myAmmo.getRed();
        numYellow=myAmmo.getYellow();
        ammoBox.getChildren().clear();
        for(int i=ZERO;i<numBlue;i++) {
            ammoBox.add(getAmmoImage(BLUE_CUBE), i, ZERO);
        }
        for(int i=ZERO;i<numYellow;i++)
            ammoBox.add(getAmmoImage(YELLOW_CUBE),i,1);

        for(int i=ZERO;i<numRed;i++)
            ammoBox.add(getAmmoImage(RED_CUBE),i,2);
    }

    /**
     * @param myPlayer is my player's representation
     * updates all my player sections
     */
    private void updateMyPlayer(MyPlayerRepresentation myPlayer){
        updateMyPlayerDamage((HBox)myPlayerPane.getChildren().get(3),myPlayer.getDamages());
        updateMyPlayerDamage((HBox)myPlayerPane.getChildren().get(4),myPlayer.getMarks());
        updateMyPlayerAmmoCube((GridPane)myPlayerPane.getChildren().get(2),myPlayer.getAmmoCubes());
        updateMyPlayerEmpowersAndWeapons(myPlayer);
        updateMyPlayerSkulls((HBox)myPlayerPane.getChildren().get(5),myPlayer.getDeaths());

    }

    /**
     *
     * @param playerSkulls is my player skulls' section
     * @param numDeaths are my player deaths
     *
     * updates my player's points
     */
    private void updateMyPlayerSkulls(HBox playerSkulls, int numDeaths){
        if (!finalFrenzy) {
            if (numDeaths <= NUM_POINTS_BOARD)
                for (int i = ZERO; i < numDeaths; i++) {
                    playerSkulls.getChildren().set(i, getSkullImage());
                }
            else
                for (int i = ZERO; i < NUM_POINTS_BOARD; i++) {
                    playerSkulls.getChildren().set(i, getSkullImage());
                }
        }else{
            if (numDeaths <= NUM_POINTS_BOARD_FF)
                for (int i = ZERO; i < numDeaths; i++) {
                    playerSkulls.getChildren().set(i, getSkullImage());
                }
            else
                for (int i = ZERO; i < NUM_POINTS_BOARD_FF; i++) {
                    playerSkulls.getChildren().set(i, getSkullImage());
                }
        }

    }

    /**
     * @param upperpane is the top secion of map's sections
     * creates killshotTrack' section
     */
    private void addKillShotTrack(StackPane upperpane){
        killShotTrack=new HBox(KILLSHOT_SPACING);
        killShotTrack.setMaxWidth(KILLSHOT_MAX_WIDTH);
        upperpane.getChildren().add(killShotTrack);
        Image image=new Image(getClass().getResourceAsStream(SKULL),KILLSHOT_IMAGE_SIZE,KILLSHOT_IMAGE_SIZE,true,true);
        for (int i = ZERO; i< KILLSHOT_CELLS_NUMBER; i++){
            killShotTrack.getChildren().add(new ImageView(image));
        }
        upperpane.setAlignment(killShotTrack,Pos.TOP_LEFT);
        killShotTrack.setTranslateY(KILLSHOT_TRASL_Y);
        killShotTrack.setTranslateX(KILLSHOT_TRASL_X);


    }

    /**
     * @param deaths is the kills' list of killshotTrack
     * updates killshotTrack' section
     */
    private void updateKillShotTrack(List<Color> deaths){
        int i=ZERO;
        for (Color damage:deaths){
            ImageView image=((ImageView)killShotTrack.getChildren().get(i));
            image.setImage(new Image(getClass().getResourceAsStream(getRelativeDrop(damage))));
            image.setFitWidth(KILLSHOT_IMAGE_SIZE);
            image.setFitHeight(KILLSHOT_IMAGE_SIZE);
            i++;
        }
    }

    /**
     *
     * adjusts window's dimensions basing on the screen and constants
     * @param height is window height
     * @param primaryStage is the stage
     * @param width is window width
     */
    public void adjustDimensions(double height, double width, Stage primaryStage){
        mapStackPane.setTranslateX(width/ MAP_ADJUST_TRASL_X_CONST);
        mapStackPane.setTranslateY(height/ MAP_ADJUST_TRASL_Y_CONST);
        myPlayerBox.setTranslateX(width/ MYPLAYERBOX_ADJUST_TRASL_X_CONST);
        myPlayerBox.setPrefHeight(height/ MYPL_PREF_HEIGHT_ADJUST_CONST);
        enemiesVBox.translateXProperty().bind(primaryStage.widthProperty().subtract(MIN_WINDOW_WIDTH_SIZE).multiply(ENEMY_TRASL_X_ADJUST_CONST));
        enemiesVBox.setTranslateY(height/ ENEMIES_ADJUST_TRASL_Y_CONST);
        enemiesVBox.setSpacing(height/ ENEMIES_ADJUST_SPACING_CONST);
        enemiesVBox.prefWidthProperty().bind(primaryStage.widthProperty().multiply(ENEMIES_SECTION_WIDTH_CONST));

    }

    /**
     *
     * @param  message is message that has to be showed on the base of the map
     *
     * shows a message for some seconds at the base of the map
     */

    public void setMessages(String message) {
        new Thread(() -> {
            try {
                Thread.sleep(MIN_MESSAGE_TIME * ONE_SECOND);
            } catch (InterruptedException e) {
                WriterHelper.printErrorMessage(ERROR_MESSAGE);
            }
            messages.setText(message);
        }).start();
    }

    /**
     *
     * @param  playerPane is enemy player playerboard section
     *
     * creates a new skullBox for final frenzy mode
     */
    private void createEnemySkullBoxFinalFrenzy(StackPane playerPane){
        HBox skullbox = new HBox();
        skullbox.setSpacing(ENEMY_SKULL_BOX_FF_SACING);
        playerPane.getChildren().set(5,skullbox);
        skullbox.setTranslateY(ENEMY_SKULL_BOX_TRASL_Y);
        skullbox.setTranslateX(ENEMY_SKULL_BOX_TRASL_X);
        for (int i = ZERO; i < NUM_POINTS_BOARD_FF; i++) {
            skullbox.getChildren().add(new ImageView());
        }

    }

    /**
     *
     * @param  playerPane is my player playerboard section
     *
     * creates a new skullBox for final frenzy mode
     */

    private void createMyPlayerSkullBoxFinalFrenzy(StackPane playerPane){
        HBox skullbox = new HBox();
        skullbox.setAlignment(Pos.TOP_LEFT);
        skullbox.setSpacing(MY_SKULL_BOX_SPACING);
        playerPane.getChildren().set(5,skullbox);
        playerPane.setAlignment(skullbox,Pos.CENTER_LEFT);
        skullbox.setTranslateY(MYPL_SKULLBOX_FF_TRASL_Y);
        skullbox.setTranslateX(MYPL_SKULLBOX_FF_TRASL_X);
        skullbox.setPrefWidth(MYPL_SKULLBOX_FF_PREF_WIDTH);
        skullbox.setPrefHeight(SKULL_AMMO_DAMAGE_SIZE);
        for (int i = ZERO; i < NUM_POINTS_BOARD_FF; i++) {
            skullbox.getChildren().add(new ImageView());
        }

    }

    /**
     *
     * changes player boards' images with final frenzy player board,
     * creates a new skullBox,adjusts the traslations,
     * inserts the final frenzy logo and changes background style
     */

    public void startFinalFrenzy(){
        if (!finalFrenzy) {
            for (int i = 0; i < enemiesBoards.size(); i++) {
                if (!isDamaged((HBox) enemiesBoards.get(i).getChildren().get(3))) {
                    String url = getRelativeBoard(enemyColors.get(i));
                    url = url.replaceAll(PNG_EXTENSION, FINAL_FRENZY_SUFFIX);
                    url = url.concat(PNG_EXTENSION);
                    ((ImageView) enemiesBoards.get(i).getChildren().get(0)).setImage(new Image(getClass().getResourceAsStream(url)));
                    StackPane enemyStackPane = enemiesBoards.get(i);
                    Platform.runLater(() -> createEnemySkullBoxFinalFrenzy(enemyStackPane));
                    double translation = enemiesBoards.get(i).getChildren().get(3).getTranslateX();
                    enemiesBoards.get(i).getChildren().get(3).setTranslateX(translation * 1.15);
                    ((HBox) enemiesBoards.get(i).getChildren().get(3)).setSpacing(-2);
                }
            }
            if (!isDamaged((HBox) myPlayerPane.getChildren().get(3))) {
                String url = getRelativeBoard(myPlayerColor);
                url = url.replaceAll(PNG_EXTENSION, FINAL_FRENZY_SUFFIX);
                url = url.concat(PNG_EXTENSION);
                ((ImageView) myPlayerPane.getChildren().get(0)).setImage(new Image(getClass().getResourceAsStream(url)));
                Platform.runLater(() -> createMyPlayerSkullBoxFinalFrenzy(myPlayerPane));
                double translation = myPlayerPane.getChildren().get(3).getTranslateX();
                myPlayerPane.getChildren().get(3).setTranslateX(translation * 1.15);
                ((HBox) myPlayerPane.getChildren().get(3)).setSpacing(-2);
            }
        }
        finalFrenzy = true;
        primarySections.getStyleClass().add(BACKGROUND_STYLE_FF);
        Platform.runLater(()->addFinalFrenzyLogo(finalFrenzyPane));
    }

    /**
     *
     * @param upperpane is the upper field of the map section
     * inserts a image logo of final frenzy mode
     */
    private void addFinalFrenzyLogo(StackPane upperpane){

        ImageView finalFrenzyLogo=new ImageView(new Image(getClass().getResourceAsStream(FINAL_FRENZY_LOGO), FF_LOGO_WIDTH, FF_LOGO_HEIGHT,true,true));
        upperpane.getChildren().add(finalFrenzyLogo);
        finalFrenzyLogo.setTranslateY(FF_LOGO_TRASL_Y);
        finalFrenzyLogo.setTranslateX(FF_LOGO_TRASL_X);
        upperpane.setAlignment(finalFrenzyLogo,Pos.CENTER_LEFT);


    }

    /**
     *
     * @param damages is the damages Section of the player
     * @return true if player is damaged
     */

    private boolean isDamaged(HBox damages){
        if((damages.getChildren().isEmpty()))
            return false;
        return true;
    }

}
