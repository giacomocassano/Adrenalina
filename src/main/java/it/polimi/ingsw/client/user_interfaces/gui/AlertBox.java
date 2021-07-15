package it.polimi.ingsw.client.user_interfaces.gui;

import it.polimi.ingsw.client.ServerConnection;
import it.polimi.ingsw.client.user_interfaces.model_representation.EffectInfo;
import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.client.user_interfaces.model_representation.WeaponInfo;
import it.polimi.ingsw.model.AmmoCubes;
import it.polimi.ingsw.model.MoveType;
import it.polimi.ingsw.model.Position;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class used to create Alert windows as response to events received
 * by GUIController
 */
class AlertBox {

    private static final int BUTTONS_SECTION_PREF_HEIGHT = 80;
    private static final String BACKGROUND_COLOR_TRANSPARENT_STYLE = "-fx-background-color: transparent;";
    private static final String MESSAGE_DIALOG_TITLE = "Messaggio";
    private static final int ZERO = 0;
    private static final int WINNING_ALERT_WIDTH = 700;
    private static final int WINNING_ALERT_HEIGHT = 500;
    private static final String AVATARS_FOLDER_PATH = "/images/OtherStuff/";
    private static final String GAME_OVER_IMG_PATH = "/images/OtherStuff/Game_Over,_Man!.png";
    private static final String AVATAR_IMG_SUFFIX = "Robot.png";
    private static final String WINNING_MESSAGE = " complimenti, hai vinto!";
    private static final String LOOSER_MESSAGE = " mi dispiace, non hai vinto\necco l'avatar del vincitore:";
    private static final String GOLDEN_COLOR_STYLE = "-fx-text-fill:rgba(202,186,0,0.99);";
    private static final int AVATAR_IMG_WINNING_WIDTH = 350;
    private static final int GAME_OVER_IMG_WIDTH = 500;
    private static final int WINNER_ADVISE_TRASL_X = -20;
    private static final int AVATAR_TRASL_X = -20;
    private static final int RANKING_SPACING = 40;
    private static final int RANKING_TRASL_X = 40;
    private static final String ASK_SPAWN_MESSAGE = " \nScegli dove nascere";
    private static final String GRAB_AMMO_INFORMATION_MESSAGE = "Hai scelto di raccogliere una carta munizione";
    private static final String CONFIRM_MESSAGE = "Confermi?";
    private static final String YES_TEXT = "Si";
    private static final String NO_TEXT = "No";
    private static final String LEFT_STRING = "SINISTRA";
    private static final String RIGHT_STRING = "DESTRA";
    private static final String UP_STRING = "SOPRA";
    private static final String BOTTOM_STRING = "SOTTO";
    private static final String STAY_IN_YOUR_CELL_STRING = "STAI FERMO";
    private static final String MOVE_QUESTION_MESSAGE = "In quale cella vuoi muoverti?";
    private static final String ASK_EMPOWER_MESSAGE = "Vuoi usare un potenziamento?";
    private static final String ASK_WEAPON_SHOT_MESSAGE = "Con quale arma vuoi sparare?";
    private static final String ASK_RESPAWN_MESSAGE = "Quale potenziamento vuoi scartare per rinascere?";
    private static final String DROP_WEAPON_MESSAGE = "Quale arma vuoi lasciare?";
    private static final String ASK_BASIC_EFFECT_QUESTION = "Quale effetto base vuoi usare?";
    private static final String RECOIL_MOVEMENT_MESSAGE = "Dove vuoi rinculare il bersaglio?";
    private static final String RELOAD_WEAPON_QUESTION = "Quale armi vuoi ricaricare?";
    private static final String NOT_RECHARGE_TEXT = "Non ricaricare";
    private static final String BEFORE_TEXT = "Prima";
    private static final String AFTER_TEXT = "Dopo";
    private static final String ASK_SHOOTER_MOVEMENT_QUESTION = "Vuoi usare l'effetto primo o dopo l'effetto base";
    private static final String MOVEMENT_QUESTION = "Dove vorresti muoverti?";
    private static final String ULTRA_EFFECT_QUESTION = "Quale ultra effetto vorresti usare?";
    private static final String PAYING_COLOR_QUESTION = "Con che colore vorresti pagare?";
    private static final String TARGET_SELECTION_MESSAGE = ", chi vorresti bersagliare?";
    private static final String USING_EMPOWER_FREFIX_STRING = "Hai scelto di usare ";
    private static final String TRANSPORTER_QUESTION = "Dove vorresti teletrasportarti?";
    private static final String TAGBACK_GRENADE_MESSAGE_INFORMATION = "Hai scelto di usare GRANATA VENOM.\nPuoi colpire chi ti ha sparato:";
    private static final String TAGBACK_GRENADE_MOVE_QUESTION = "Chi vuoi spostare?";
    private static final String TAGBACK_GRENADE_MOVE_QUESTION2 = "Dove vuoi spostarlo?";
    private static final String SERVER_ERROR_ALERT_TITLE = "Errore del server";
    private static final String SERVER_ERROR_HEADER_TEXT = "Server al momento non disponibile";
    private static final String SERVER_ERROR_ALERT_TEXT = "Si è verificato un errore interno. Riprovare a ricconnettersi più tardi";
    private static final String RANKING_POINTS_SUFFIX = " punti";
    private static final double TITLE_TRANSL_Y_CONST_PROPERTY = 0.08;
    private static final int MAX_WIDTH_CONST_WINDOW_ZOOM = 2;
    private static final double CARD_ZOOMER_FIT_WIDTH_PROPERTY_CONST = 0.3;
    private static final double CARD_ZOOMED_TRANSL_X_PROPERTY_CONST = 0.016;
    private static final int TITLE_ZOOM_WIDTH_TRANSL_Y = 10;
    private static ServerConnection connection;
    private static Stage window;
    private static Stage windowZoom;
    private static VBox layout1;
    private static VBox layout2;
    private static HBox buttons;
    private static Label title1;
    private static Label title2;
    private static Button confirmButton;
    private static Button exitButton;
    private static List<CheckBox> checkBoxes1;
    private static List<CheckBox> checkBoxes2;
    private static List<CheckBox> checkBoxes3;

    private static Alert alert;
    private static boolean isAlertActive;
    private static final String CSS_FILE_NAME="Style.css";
    private static final String WINDOW_BACKGROUND_STYLE="backgroundRoot";
    private static final String CONFIRM_BUTTON_TEXT="Conferma";
    private static final String ABORT_BUTTON_TEXT="Annulla";
    private static final int PREF_WINDOW_WIDTH=400;
    private static final int PREF_WINDOW_HEIGHT=600;
    private static final int PREF_VBOX_SPACING=50;
    private static final int PREF_HBOX_SPACING=20;
    private static final String WINDOW_TITLE="Adrenalina";
    private static final String FONT_PATH="/font/Ethnocentric.ttf";
    private static final double THREE_PERCENT=0.03;
    private static final double NINETY_PERCENT=0.9;
    private static final int BUTTONS_PREF_HEIGHT=60;
    private static final String DO_NOT_USE_MESSAGE="Non usare";
    private static final String SELECTION_QUESTION="Chi vorresti colpire?";
    private static final String PAYING_EMPOWERS_QUESTION="Con quali potenziamenti vuoi pagare?";
    private static final String SELECT_WEAPON_QUESTION="Quale arma vuoi Raccogliere?";
    private static final String SELECT_SQUARES_SHOOT_QUESTION="Quali celle vorresti colpire?";

    private AlertBox() {
    }

    /**
     *
     * @param conn is the server connection
     * @param question is the question that has to be shown on window
     * @param options is a list of answers to be displayed in order to answer to the question
     * @param maxOptions are max allowed option that could be selected
     *
     * shows a window with a set of checkboxes
     */

    private static void initWindowComponents(ServerConnection conn, String question, List<String> options, int maxOptions) {
        connection = conn;
        window = new Stage();

        //Block events to other windows
        window.initModality(Modality.NONE);
        window.initStyle(StageStyle.UTILITY);
        window.setAlwaysOnTop(true);
        window.setTitle(WINDOW_TITLE);
        window.setWidth(PREF_WINDOW_WIDTH);
        window.setHeight(PREF_WINDOW_HEIGHT);

        layout1 = new VBox(PREF_VBOX_SPACING);

        buttons =new HBox(PREF_HBOX_SPACING);
        Font font12=Font.loadFont(AlertBox.class.getResourceAsStream(FONT_PATH),12);
        title1=new Label(question);
        title1.setTextFill(Color.WHITE);
        title1.setWrapText(true);
        title1.setFont(font12);
        title1.setTextAlignment(TextAlignment.CENTER);
        confirmButton=new Button(CONFIRM_BUTTON_TEXT);
        confirmButton.setDisable(true);
        confirmButton.setFont(font12);
        exitButton=new Button(ABORT_BUTTON_TEXT);
        exitButton.setFont(font12);

        exitButton.setOnMouseClicked(e->window.close());

        buttons.getChildren().addAll(confirmButton,exitButton);

        layout1.getChildren().add(title1);

        checkBoxes1=new ArrayList<>();

        for (String option:options) {

            CheckBox tmp=new CheckBox(option);
            checkBoxes1.add(tmp);
            tmp.setWrapText(true);
            tmp.setFont(font12);
            tmp.setTextAlignment(TextAlignment.CENTER);
            layout1.getChildren().add(tmp);
            tmp.setTextFill(Color.WHITE);
        }

        for (CheckBox checkFirst:checkBoxes1){
            checkFirst.setOnAction(e->{
                if (getNumSelectedCheckboxes(checkBoxes1)==maxOptions) {
                    confirmButton.setDisable(false);
                    for (CheckBox check:checkBoxes1){
                        if(!check.isSelected())
                            check.setDisable(true);
                    }
                }
                else{
                    confirmButton.setDisable(true);
                    for (CheckBox check:checkBoxes1){
                            check.setDisable(false);
                    }
                }
            });
        }

        BorderPane borderPane=new BorderPane();

        borderPane.setBottom(buttons);

        ScrollPane layoutRoot=new ScrollPane(layout1);



        StackPane stackPane=new StackPane();

        stackPane.getChildren().add(layoutRoot);

        stackPane.setAlignment(layoutRoot,Pos.CENTER);

        borderPane.setCenter(stackPane);

        buttons.setAlignment(Pos.CENTER);

        buttons.setPrefHeight(BUTTONS_SECTION_PREF_HEIGHT);

        layout1.setAlignment(Pos.CENTER);

        layoutRoot.setStyle(BACKGROUND_COLOR_TRANSPARENT_STYLE);
        layoutRoot.setFitToHeight(true);
        layoutRoot.setFitToWidth(true);
        layoutRoot.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        layoutRoot.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        borderPane.getStylesheets().add(CSS_FILE_NAME);
        borderPane.getStyleClass().add(WINDOW_BACKGROUND_STYLE);

        layout1.getStyleClass().add(WINDOW_BACKGROUND_STYLE);


        Scene scene = new Scene(borderPane);
        window.setScene(scene);
    }

    /**
     *
     * @param message is the message that has to be shown on screen
     *
     * creates an information's window
     */

    public static void showMessageDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(WINDOW_TITLE);
        alert.setHeaderText(MESSAGE_DIALOG_TITLE);
        alert.initStyle(StageStyle.UTILITY);
        alert.initStyle(StageStyle.UTILITY);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     *
     * @param checkBoxes is the list of checkboxes
     * @return number of selected checkboxes in checkboxes
     */
    private static int getNumSelectedCheckboxes(List<CheckBox> checkBoxes) {
        int cont = ZERO;
        for(CheckBox box: checkBoxes)
            if(box.isSelected())
                cont++;
        return cont;
    }

    /**
     * Gets a sublist of a given list. The sublist contains the elements of the list at the indexes indicated in the list of integer.
     * @param list is the given list
     * @param indexes is the list of indexes
     * @return the sublist
     */
    private static List getSublist(List list, List<Integer> indexes) {
        List sublist = new ArrayList();
        for(int i: indexes)
            sublist.add(list.get(i));
        return sublist;
    }

    /**
     * Asks to user the move he wants to perform
     * @param conn is the server connection
     * @param player is the player name
     * @param moveTypes are the possible move types
     * @param actionNum is the action number of the turn
     * @param moveNum is the move number of the action
     */
    public static void askMove(ServerConnection conn, String player, List<MoveType> moveTypes, int actionNum, int moveNum){
        initWindowComponents(conn, player+ " devi fare la " +moveNum+"° mossa della "+actionNum+"° azione del tuo turno.\nCosa vorresti fare?",
                moveTypes.stream().map(MoveType::getDescription).collect(Collectors.toList()), 1);
        confirmButton.setOnAction(e -> {
            List<MoveType> selected = getSublist(moveTypes, getCheckBoxSelection(checkBoxes1));
            connection.notifyMoveResponse(player, selected.get(ZERO));
            window.close();
        });
        window.showAndWait();
    }

    /**
     * Ask to the user which empower he wants to drop to spawn for the first time.
     * @param conn is the server connection
     * @param player is the player name
     * @param emps are the possible empowers to select
     */
    public static void askSpawn(ServerConnection conn, String player, List<EmpowerInfo> emps) {
        initWindowComponents(conn, player+ ASK_SPAWN_MESSAGE, emps.stream().map(e -> e.getName() + " - " + e.getColor().getDescription()).collect(Collectors.toList()), 1);
        confirmButton.setOnAction(e -> {
            EmpowerInfo selected = (EmpowerInfo) getSublist(emps, getCheckBoxSelection(checkBoxes1)).get(ZERO);
            emps.remove(selected);
            connection.notifyFirstSpawnResponse(player, selected, emps);
            window.close();
        });
        window.showAndWait();
    }

    /**
     * Asks to user the confirm that he wants to grab a ammunition card.
     * @param conn is the server connection
     * @param player is the player name
     */
    public static void askGrabAmmo(ServerConnection conn, String player) {
        isAlertActive = true;
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(WINDOW_TITLE);
        alert.initStyle(StageStyle.UTILITY);
        alert.setHeaderText(GRAB_AMMO_INFORMATION_MESSAGE);
        alert.setContentText(CONFIRM_MESSAGE);


        ButtonType buttonTypeOne = new ButtonType(YES_TEXT);
        ButtonType buttonTypeTwo = new ButtonType(NO_TEXT);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeOne){
            // ... user chose "Si"
            connection.notifyGrabAmmoResponse(player, false);
        } else if (result.isPresent() && result.get() == buttonTypeTwo) {
            // ... user chose "No"
            connection.notifyGrabAmmoResponse(player, true);
        }
        isAlertActive = false;
    }


    /**
     * Asks to user which square he wants to move in.
     * @param conn is the server connection
     * @param player is the player name
     * @param positions are the possible positions to select
     * @param intialPosition is the initial position of the user
     */
    public static void askSquareToMove(ServerConnection conn, String player, List<Position> positions, Position intialPosition) {
        //Set position list
        List<String> positionOptions = new ArrayList<>();
        for(Position pos: positions){
            if(pos.getRow() == intialPosition.getRow()){
                if(pos.getColumn() == intialPosition.getColumn()-1)
                    positionOptions.add(LEFT_STRING);
                else if(pos.getColumn() == intialPosition.getColumn()+1)
                    positionOptions.add(RIGHT_STRING);
            }else if(pos.getColumn() == intialPosition.getColumn()){
                if(pos.getRow()== intialPosition.getRow()-1)
                    positionOptions.add(UP_STRING);
                else if(pos.getRow() == intialPosition.getRow()+1)
                    positionOptions.add(BOTTOM_STRING);
            }
            if(intialPosition.equals(pos))
                positionOptions.add(STAY_IN_YOUR_CELL_STRING);
        }
        //Init window components
        initWindowComponents(conn, MOVE_QUESTION_MESSAGE, positionOptions, 1);
        confirmButton.setOnAction( e -> {
            Position selected = (Position) getSublist(positions, getCheckBoxSelection(checkBoxes1)).get(ZERO);
            connection.notifySquareToMoveResponse(player, selected, false);
            window.close();
        });
        exitButton.setOnAction(e -> {
            connection.notifySquareToMoveResponse(player, null, true);
            window.close();
        });
        window.showAndWait();
    }

    /**
     * Asks to user which empower he wants to use.
     * @param conn is the server connection
     * @param player is the player name
     * @param emps are the possible empowers to select
     */
    public static void askEmpower(ServerConnection conn, String player, List<EmpowerInfo> emps) {
        initWindowComponents(conn, ASK_EMPOWER_MESSAGE, emps.stream().map(e -> e.getName() + " - "+e.getColor()).collect(Collectors.toList()), 1);
        confirmButton.setOnAction(e -> {
            EmpowerInfo selected = (EmpowerInfo) getSublist(emps, getCheckBoxSelection(checkBoxes1)).get(ZERO);
            connection.notifyEmpowerResponse(player, selected, true);
            window.close();
        });
        exitButton.setText(DO_NOT_USE_MESSAGE);
        exitButton.setOnAction(e -> {
            connection.notifyEmpowerResponse(player, null, false);
            window.close();
        });
        window.showAndWait();
    }

    /**
     * Asks to user which weapon he wants to shoot with
     * @param connection is the server connection
     * @param player is the player name
     * @param weapons are the possible weapons to select
     */
    public static void askWeaponToShoot(ServerConnection connection, String player, List<WeaponInfo> weapons) {
        initWindowComponents(connection, ASK_WEAPON_SHOT_MESSAGE, weapons.stream().map(WeaponInfo::getName).collect(Collectors.toList()), 1);
        confirmButton.setOnAction(e -> {
            WeaponInfo selected = (WeaponInfo) getSublist(weapons, getCheckBoxSelection(checkBoxes1)).get(ZERO);
            connection.notifyWeaponToShootResponse(player, selected, false);
            window.close();
        });
        exitButton.setOnAction(e -> {
            connection.notifyWeaponToShootResponse(player, null, true);
            window.close();
        });
        window.showAndWait();
    }

    /**
     * Asks to user which empower he wants to drop to respawn.
     * @param connection is the server connection
     * @param player is the player name
     * @param empowers are the possible empower that player can drop
     */
    public static void askRespawn(ServerConnection connection, String player, List<EmpowerInfo> empowers) {
        initWindowComponents(connection, ASK_RESPAWN_MESSAGE, empowers.stream().map(e -> e.getName() +" - "+e.getColor()).collect(Collectors.toList()), 1);
        confirmButton.setOnAction(e -> {
            EmpowerInfo selected = (EmpowerInfo) getSublist(empowers, getCheckBoxSelection(checkBoxes1)).get(ZERO);
            connection.notifyRespawnResponse(player, selected);
            window.close();
        });
        window.showAndWait();
    }

    /**
     * Gets a list of integer where each integer is the index of the selected checkboxes
     * @param checkBoxes are the checkboxes
     * @return the list of integer where each integer is the index of the selected checkboxes
     */
    private static List<Integer> getCheckBoxSelection(List<CheckBox> checkBoxes) {
        List<Integer> selections = new ArrayList<>();
        if(checkBoxes != null && !checkBoxes.isEmpty())
            for(int i = ZERO; i<checkBoxes.size(); i++)
                if(checkBoxes.get(i).isSelected())
                    selections.add(i);
        return selections;
    }

    /**
     * Asks to user which weapon he wants to grab and which weapon he want to drop if he already has 3 weapons
     * @param connection is the server connection
     * @param player is the player name
     * @param weapons are the possible weapons to buy
     * @param drops is the possible weapons to drop
     * @param payingEmpowers are the possible paying empowers
     */
    public static void askWeaponToGrab(ServerConnection connection, String player,List<WeaponInfo> weapons, List<WeaponInfo> drops, List<EmpowerInfo> payingEmpowers){
        List<String> gainedWeapons = weapons.stream().map(WeaponInfo::getName).collect(Collectors.toList());
        List<String> payingEmps = payingEmpowers.stream().map(e -> e.getName() + " - " + e.getColor()).collect(Collectors.toList());
        if(drops == null || drops.isEmpty()) {
            initDoubleWindowComponents(connection, SELECT_WEAPON_QUESTION, PAYING_EMPOWERS_QUESTION,
                    gainedWeapons, payingEmps,1, 1, ZERO, payingEmpowers.size());
            confirmButton.setOnAction(e -> {
                WeaponInfo selected = (WeaponInfo) getSublist(weapons, getCheckBoxSelection(checkBoxes1)).get(ZERO);
                List<EmpowerInfo> payings = getSublist(payingEmpowers, getCheckBoxSelection(checkBoxes2));
                connection.notifyWeaponToGrabResponse(player, selected, null, payings, false);
                window.close();
            });
        }else{
            List<String> droppedWeapons = drops.stream().map(WeaponInfo::getName).collect(Collectors.toList());
            initTripleWindowComponents(connection, SELECT_WEAPON_QUESTION, DROP_WEAPON_MESSAGE, PAYING_EMPOWERS_QUESTION,
                    gainedWeapons, droppedWeapons, payingEmps, 1,1,1,1, ZERO, payingEmpowers.size());
            confirmButton.setOnAction(e -> {
                WeaponInfo selected = (WeaponInfo) getSublist(weapons, getCheckBoxSelection(checkBoxes1)).get(ZERO);
                WeaponInfo dropped = (WeaponInfo) getSublist(drops, getCheckBoxSelection(checkBoxes2)).get(ZERO);
                List<EmpowerInfo> payings = getSublist(payingEmpowers, getCheckBoxSelection(checkBoxes3));
                connection.notifyWeaponToGrabResponse(player, selected, dropped, payings, false);
                window.close();
            });
        }
        exitButton.setOnAction(e -> {
            connection.notifyWeaponToGrabResponse(player, null, null, null, true);
            window.close();
        });
        window.showAndWait();
    }

    /**
     * Ask to user which basic effect he want to use
     * @param connection is the server connection
     * @param player is the player name
     * @param effects are the possible basic effects
     * @param payingEmpowers are the possible paying empowers
     */
    public static void askBasicEffect(ServerConnection connection, String player, List<EffectInfo> effects, List<EmpowerInfo> payingEmpowers) {
        initDoubleWindowComponents(connection, ASK_BASIC_EFFECT_QUESTION, PAYING_EMPOWERS_QUESTION,
                effects.stream().map(EffectInfo::getName).collect(Collectors.toList()),
                payingEmpowers.stream().map(e->e.getName() +" - "+e.getColor()).collect(Collectors.toList()),
                1,1, ZERO, payingEmpowers.size());
        confirmButton.setOnAction(e -> {
            EffectInfo selected = (EffectInfo) getSublist(effects, getCheckBoxSelection(checkBoxes1)).get(ZERO);
            List<EmpowerInfo> payings = getSublist(payingEmpowers, getCheckBoxSelection(checkBoxes2));
            connection.notifyBasicEffectResponse(player, false,selected, payings);
            window.close();
        });
        exitButton.setOnAction(e -> {
            connection.notifyBasicEffectResponse(player, true, null, null);
            window.close();
        });
        window.showAndWait();
    }

    /**
     * Asks to user the basic effect settings.
     * @param connection is the server connection
     * @param playerName is the player name
     * @param posTargets are the possible target player names
     * @param posPositions are the possible target positions
     * @param posRecoils are the possible recoil positions if effect has recoil
     * @param maaxPlayers is the maximum number of target players
     * @param maxSquares is maximum number of target positions
     */
    public static void askBasicEffectUse(ServerConnection connection, String playerName, List<String> posTargets, List<Position> posPositions, List<Position> posRecoils, int maaxPlayers, int maxSquares) {
        List<String> posStrings = posPositions.stream().map(p -> p.getRow() + ", "+p.getColumn()).collect(Collectors.toList());
        List<String> recStrings = posRecoils.stream().map(p -> p.getRow() + ", "+p.getColumn()).collect(Collectors.toList());
        if(!posTargets.isEmpty() && posPositions.isEmpty() && posRecoils.isEmpty()) {
            initWindowComponents(connection, SELECTION_QUESTION, posTargets, maaxPlayers);
            confirmButton.setOnAction(e -> {
                List<String> targets = getSublist(posTargets, getCheckBoxSelection(checkBoxes1));
                connection.notifyBasicEffectUseResponse(false, playerName, targets, new ArrayList<>(), null);
                window.close();
            });
        }else if(posTargets.isEmpty() && !posPositions.isEmpty() && posRecoils.isEmpty()) {
            initWindowComponents(connection, SELECT_SQUARES_SHOOT_QUESTION, posStrings, maxSquares);
            confirmButton.setOnAction(e -> {
                List<Position> selected = getSublist(posPositions, getCheckBoxSelection(checkBoxes1));
                connection.notifyBasicEffectUseResponse(false, playerName, new ArrayList<>(), selected, null);
                window.close();
            });
        }else if(!posTargets.isEmpty() && !posPositions.isEmpty() && posRecoils.isEmpty()) {
            initDoubleWindowComponents(connection, SELECTION_QUESTION, SELECT_SQUARES_SHOOT_QUESTION,
                    posTargets, posStrings, 1, maaxPlayers, 1, maxSquares);
            confirmButton.setOnAction(e -> {
                List<String> targets = getSublist(posTargets, getCheckBoxSelection(checkBoxes1));
                List<Position> squares = getSublist(posPositions, getCheckBoxSelection(checkBoxes2));
                connection.notifyBasicEffectUseResponse(false, playerName, targets, squares, null);
                window.close();
            });
        }else if(!posTargets.isEmpty() && posPositions.isEmpty() && !posRecoils.isEmpty()) {
            initDoubleWindowComponents(connection, SELECTION_QUESTION, RECOIL_MOVEMENT_MESSAGE,
                    posTargets, recStrings, 1, maaxPlayers, 1, 1);
            confirmButton.setOnAction(e -> {
                List<String> targets = getSublist(posTargets, getCheckBoxSelection(checkBoxes1));
                Position rec = (Position) getSublist(posRecoils, getCheckBoxSelection(checkBoxes2)).get(ZERO);
                connection.notifyBasicEffectUseResponse(false, playerName, targets, new ArrayList<>(), rec);
                window.close();
            });
        }
        exitButton.setOnAction(e -> {
            connection.notifyBasicEffectUseResponse(true, playerName, new ArrayList<>(), new ArrayList<>(), null);
            window.close();
        });
        window.showAndWait();
    }

    /**
     * Asks to the user the ultra effect settings.
     * @param connection is the server connection
     * @param player is the player name
     * @param possibleTargets are the possible target names
     * @param possiblePositions are the possible target positions
     * @param maxTargets is the maximum number of target players
     * @param maxPositions is the maximum number of target positions
     */
    public static void askUltraEffectUse(ServerConnection connection, String player, List<String> possibleTargets, List<Position> possiblePositions, int maxTargets, int maxPositions) {
        List<String> posStrings = possiblePositions.stream().map(p -> p.getRow() + ", "+p.getColumn()).collect(Collectors.toList());
        if(maxTargets > ZERO && maxPositions > ZERO) {
            initDoubleWindowComponents(connection, SELECTION_QUESTION, SELECT_SQUARES_SHOOT_QUESTION, possibleTargets, posStrings, 1, maxTargets, 1, maxPositions);
            confirmButton.setOnAction(e -> {
                List<String> targets = getSublist(possibleTargets, getCheckBoxSelection(checkBoxes1));
                Position position = (Position) getSublist(possiblePositions, getCheckBoxSelection(checkBoxes2)).get(ZERO);
                connection.notifyUltraEffectUseResponse(false, player, targets, position);
                window.close();
            });
        }else if(maxTargets > ZERO && maxPositions == ZERO) {
            initWindowComponents(connection, SELECTION_QUESTION, possibleTargets, maxTargets);
            confirmButton.setOnAction(e -> {
                List<String> targets = getSublist(possibleTargets, getCheckBoxSelection(checkBoxes1));
                connection.notifyUltraEffectUseResponse(false, player, targets, null);
                window.close();
            });
        }else if(maxTargets == ZERO && maxPositions > ZERO) {
            initWindowComponents(connection, SELECT_SQUARES_SHOOT_QUESTION, posStrings, maxPositions);
            confirmButton.setOnAction(e -> {
                Position position = (Position) getSublist(possiblePositions, getCheckBoxSelection(checkBoxes2)).get(ZERO);
                connection.notifyUltraEffectUseResponse(false, player, new ArrayList<>(), position);
                window.close();
            });
        }
        exitButton.setOnAction(e -> {
            connection.notifyUltraEffectUseResponse(true, player, new ArrayList<>(), null);
            window.close();
        });
        window.showAndWait();
    }

    /**
     * Asks to user which weapons he wants to reload and how he pays them.
     * @param connection is the server connection
     * @param player is the player name
     * @param weapons are the possible weapons to reload
     * @param payingEmpowers are the possible paying empowers
     */
    public static void askWeaponToReload(ServerConnection connection, String player, List<WeaponInfo> weapons, List<EmpowerInfo> payingEmpowers) {
        initDoubleWindowComponents(connection, RELOAD_WEAPON_QUESTION, PAYING_EMPOWERS_QUESTION,
                weapons.stream().map(WeaponInfo::getName).collect(Collectors.toList()),
                payingEmpowers.stream().map(e->e.getName() +" - "+e.getColor()).collect(Collectors.toList()),
                1, weapons.size(), ZERO, payingEmpowers.size());
        confirmButton.setOnAction(e -> {
            List<WeaponInfo> selected = getSublist(weapons, getCheckBoxSelection(checkBoxes1));
            List<EmpowerInfo> payings = getSublist(payingEmpowers, getCheckBoxSelection(checkBoxes2));
            connection.notifyWeaponToReloadResponse(player, selected, payings, false);
            window.close();
        });
        exitButton.setText(NOT_RECHARGE_TEXT);
        exitButton.setOnAction(e -> {
            connection.notifyWeaponToReloadResponse(player, null, null, true);
            window.close();
        });
        window.showAndWait();
    }

    /**
     * Asks to user how he wants to use te shooter movement effect.
     * @param connection is the server connection
     * @param playerName is the player name
     * @param movement is the shooter movement effect
     * @param positions are the possible positions to choose
     * @param payings are the possible paying empowers
     */
    public static void askShooterMovement(ServerConnection connection, String playerName, EffectInfo movement, List<Position> positions, List<EmpowerInfo> payings) {
        List<String> beforeAfter = new ArrayList<>();
        beforeAfter.add(BEFORE_TEXT);
        beforeAfter.add(AFTER_TEXT);
        List<String> payingStrs = payings.stream().map(e -> e.getName() +" - "+e.getColor()).collect(Collectors.toList());
        List<String> posStrings = positions.stream().map(p -> p.getRow() + ", "+p.getColumn()).collect(Collectors.toList());
        initTripleWindowComponents(connection, ASK_SHOOTER_MOVEMENT_QUESTION, MOVEMENT_QUESTION, PAYING_EMPOWERS_QUESTION,
                beforeAfter, posStrings, payingStrs, 1, 1, 1, 1, ZERO, payings.size());
        exitButton.setText(DO_NOT_USE_MESSAGE);
        exitButton.setOnAction(e -> {
            connection.notifyShooterMovementResponse(playerName, true, null, false, new ArrayList<>());
            window.close();
        });
        confirmButton.setOnAction(e -> {
            boolean before = false;
            if(checkBoxes1.get(ZERO).isSelected())
                before = true;
            Position position = (Position) getSublist(positions, getCheckBoxSelection(checkBoxes2)).get(ZERO);
            List<EmpowerInfo> payingEmps = getSublist(payings, getCheckBoxSelection(checkBoxes3));
            connection.notifyShooterMovementResponse(playerName, false, position, before, payingEmps);
            window.close();
        });
        window.showAndWait();
    }

    /**
     * Asks to user if he wants to use the ultra effect.
     * @param connection is the server connection
     * @param player is the player
     * @param ultras are the ultra effects that user can choose
     * @param payings are the possible paying empowers
     */
    public static void askUltraEffect(ServerConnection connection, String player, List<EffectInfo> ultras, List<EmpowerInfo> payings) {
        List<String> payingStrs = payings.stream().map(e -> e.getName() +" - "+e.getColor()).collect(Collectors.toList());
        List<String> ultraStrs = ultras.stream().map(u -> u.getName() + ": "+u.getDescription()).collect(Collectors.toList());
        initDoubleWindowComponents(connection, ULTRA_EFFECT_QUESTION, PAYING_EMPOWERS_QUESTION,
                ultraStrs, payingStrs, 1, 1, ZERO, payings.size());
        confirmButton.setOnAction(e -> {
            List<EmpowerInfo> selPayings = getSublist(payings, getCheckBoxSelection(checkBoxes2));
            connection.notifylUltraEffectResponse(player, false, true, selPayings);
            window.close();
        });
        exitButton.setText(DO_NOT_USE_MESSAGE);
        exitButton.setOnAction(e -> {
            connection.notifylUltraEffectResponse(player, false, false, new ArrayList<>());
            window.close();
        });
        window.showAndWait();
    }

    /**
     * Asks to user how he wants to use the empower.
     * @param connection
     * @param playerName
     * @param empower
     * @param enemies
     * @param positions
     * @param empowers
     * @param ammoCubes
     * @param payable
     */
    public static void askEmpowerUse(ServerConnection connection, String playerName, EmpowerInfo empower, List<String> enemies, List<Position> positions,
                                     List<EmpowerInfo> empowers, AmmoCubes ammoCubes, boolean payable) {
        List<String> payingStrs = new ArrayList<>();
        if(empowers != null)
            payingStrs = empowers.stream().map(e -> e.getName() +" - "+e.getColor()).collect(Collectors.toList());
        List<it.polimi.ingsw.model.Color> cubeColors = new ArrayList<>();
        List<String> posStrings = new ArrayList<>();
        if(positions != null)
            posStrings = positions.stream().map(p -> p.getRow() + ", "+p.getColumn()).collect(Collectors.toList());
        switch (empower.getType()) {
            case TARGETING_SCOPE:
                if(ammoCubes.getRed() > ZERO) cubeColors.add(it.polimi.ingsw.model.Color.RED);
                if(ammoCubes.getBlue() > ZERO) cubeColors.add(it.polimi.ingsw.model.Color.BLUE);
                if(ammoCubes.getYellow() > ZERO) cubeColors.add(it.polimi.ingsw.model.Color.YELLOW);
                List<String> colorsStrings = cubeColors.stream().map(it.polimi.ingsw.model.Color::getDescription).collect(Collectors.toList());
                initTripleWindowComponents(connection, USING_EMPOWER_FREFIX_STRING +empower.getName()+ TARGET_SELECTION_MESSAGE, PAYING_COLOR_QUESTION, PAYING_EMPOWERS_QUESTION, enemies, colorsStrings, payingStrs, 1, 1, 1, 1, ZERO, 1);
                confirmButton.setOnAction(e -> {
                    String target = (String) getSublist(enemies, getCheckBoxSelection(checkBoxes1)).get(ZERO);
                    it.polimi.ingsw.model.Color color = (it.polimi.ingsw.model.Color) getSublist(cubeColors, getCheckBoxSelection(checkBoxes2)).get(ZERO);
                    EmpowerInfo paying = null;
                    if(!getCheckBoxSelection(checkBoxes3).isEmpty())
                         paying = (EmpowerInfo) getSublist(empowers, getCheckBoxSelection(checkBoxes3)).get(ZERO);
                    connection.notifyEmpowerUseResponse(playerName, empower, target, null, color, paying, false);
                    window.close();
                });
                break;
            case TELEPORTER:
                initWindowComponents(connection, TRANSPORTER_QUESTION, posStrings, 1);
                confirmButton.setOnAction(e -> {
                    Position teleporterPosition = (Position) getSublist(positions, getCheckBoxSelection(checkBoxes1)).get(ZERO);
                    connection.notifyEmpowerUseResponse(playerName, empower, null, teleporterPosition, null, null, false);
                    window.close();
                });
                break;
            case TAGBACK_GRANADE:
                initWindowComponents(connection, TAGBACK_GRENADE_MESSAGE_INFORMATION, enemies, 1);
                confirmButton.setOnAction(e -> {
                    String target = (String) getSublist(enemies, getCheckBoxSelection(checkBoxes1)).get(ZERO);
                    connection.notifyEmpowerUseResponse(playerName, empower, target, null, null, null, false);
                    window.close();
                });
                break;
            case NEWTON:
                initDoubleWindowComponents(connection, TAGBACK_GRENADE_MOVE_QUESTION, TAGBACK_GRENADE_MOVE_QUESTION2, enemies, posStrings, 1,1,1,1);
                confirmButton.setOnAction(e -> {
                    String target = (String) getSublist(enemies, getCheckBoxSelection(checkBoxes1)).get(ZERO);
                    Position position = (Position) getSublist(positions, getCheckBoxSelection(checkBoxes2)).get(ZERO);
                    connection.notifyEmpowerUseResponse(playerName, empower, target, position, null, null, false);
                    window.close();
                });
                break;
        }
        exitButton.setText(DO_NOT_USE_MESSAGE);
        exitButton.setOnAction(e -> {
            connection.notifyEmpowerUseResponse(playerName, empower, null, null, null, null, true);
            window.close();
        });
        window.showAndWait();
    }

    /**
     * closes the opened window
     */
    public static void closeAlertBox() {
        if(isAlertActive){
            alert.close();
            isAlertActive = false;
        }else
            window.close();
    }

    /**
     * shows an error for the server connection
     */
    public static void showServerErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(SERVER_ERROR_ALERT_TITLE);
        alert.initStyle(StageStyle.UTILITY);
        alert.setHeaderText(SERVER_ERROR_HEADER_TEXT);
        alert.setContentText(SERVER_ERROR_ALERT_TEXT);

        alert.showAndWait();
        System.exit(1);
    }

    /**
     *
     * @param title is the title of the error window
     * @param headerTest   is the error type
     * @param contentText   is the error description
     * displays a window with error description
     */
    public static void showErrorDialog(String title, String headerTest, String contentText) {
        isAlertActive = true;
        alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle(title);
        alert.setHeaderText(headerTest);
        alert.setContentText(contentText);

        alert.showAndWait();
        isAlertActive = false;
    }

    /**
     *
     * @param winnerName is the name of the winner
     * @param rankingPositions  is the final ranking of the game
     * @param winnerColor   is the color of the winner
     * @param myPlayerName  is name of my player
     *
     * creates a window with the ranking of the game and the image of the winner
     */
    public static void createWinningAlert(String winnerName, Map<String,Integer> rankingPositions, it.polimi.ingsw.model.Color winnerColor,String myPlayerName){
        Stage stage=new Stage();
        stage.setTitle(WINDOW_TITLE);
        stage.setWidth(WINNING_ALERT_WIDTH);
        stage.setHeight(WINNING_ALERT_HEIGHT);
        BorderPane window=new BorderPane();
        window.getStylesheets().add(CSS_FILE_NAME);
        window.getStyleClass().add(WINDOW_BACKGROUND_STYLE);
        stage.initStyle(StageStyle.UTILITY);
        Font font14=Font.loadFont(AlertBox.class.getResourceAsStream(FONT_PATH),14);

        Scene scene=new Scene(window);

        ImageView yourAvatar=new ImageView(new Image(AlertBox.class.getResourceAsStream(AVATARS_FOLDER_PATH +winnerColor+ AVATAR_IMG_SUFFIX)));
        ImageView gameOver=new ImageView(new Image(AlertBox.class.getResourceAsStream(GAME_OVER_IMG_PATH)));
        yourAvatar.setFitWidth(AVATAR_IMG_WINNING_WIDTH);
        gameOver.setFitWidth(GAME_OVER_IMG_WIDTH);
        yourAvatar.setTranslateY(-BUTTONS_SECTION_PREF_HEIGHT);
        gameOver.setPreserveRatio(true);
        yourAvatar.setPreserveRatio(true);
        VBox right=new VBox(BUTTONS_SECTION_PREF_HEIGHT);
        Label winnerAdvice=new Label();
        if(winnerName!=null) {
            if (myPlayerName.equals(winnerName))
                winnerAdvice.setText(winnerName + WINNING_MESSAGE);
            else
                winnerAdvice.setText(myPlayerName + LOOSER_MESSAGE);
        }
        else
            winnerAdvice.setText("Non c'è un vincitore, c'è stato un pareggio");
        winnerAdvice.setFont(font14);
        winnerAdvice.setTextFill(Color.WHITE);
        winnerAdvice.setTranslateX(WINNER_ADVISE_TRASL_X);
        right.getChildren().addAll(winnerAdvice,yourAvatar);
        right.setAlignment(Pos.TOP_CENTER);
        StackPane top=new StackPane();
        top.getChildren().addAll(gameOver);
        yourAvatar.setTranslateX(AVATAR_TRASL_X);

        VBox left=new VBox(RANKING_SPACING);
        List<Label> ranking=new ArrayList<>();

        int i= ZERO;
        for (Map.Entry<String,Integer> map:rankingPositions.entrySet()){
            Label label=new Label(i+1+") "+map+ RANKING_POINTS_SUFFIX);
            label.setTextFill(Color.WHITE);
            label.setFont(font14);
            label.setAlignment(Pos.CENTER);
            if (i== ZERO)
                label.setStyle(GOLDEN_COLOR_STYLE);
            ranking.add(label);
            left.getChildren().add(ranking.get(i));
            i++;
        }
        left.setTranslateX(RANKING_TRASL_X);
        window.setLeft(left);
        window.setTop(top);
        window.setRight(right);
        stage.setScene(scene);
        stage.setOnCloseRequest(e->System.exit(1));
        stage.show();
    }

    /**
     *
     * @param card  is the ImageView of the card that has to be zoomed
     * @param description   is the description of showed card
     * @param name  is the name of the card
     * creates a window with the card and it's description
     */

    public static void zoomCard(ImageView card,String description,String name) {

        windowZoom = new Stage();

        windowZoom.initStyle(StageStyle.UTILITY);

        //Block events to other windows
        windowZoom.initModality(Modality.NONE);
        windowZoom.setAlwaysOnTop(true);
        windowZoom.setTitle(WINDOW_TITLE);
        windowZoom.setWidth(PREF_WINDOW_WIDTH*2);
        windowZoom.setHeight(PREF_WINDOW_WIDTH);
        Font font12=Font.loadFont(AlertBox.class.getResourceAsStream(FONT_PATH),12);
        Font font16=Font.loadFont(AlertBox.class.getResourceAsStream(FONT_PATH),16);


        Label descr = new Label();
        descr.setText(description);
        descr.setMaxWidth(PREF_WINDOW_WIDTH);
        descr.setWrapText(true);
        descr.setFont(font12);
        descr.setTextFill(Color.WHITE);

        Label title=new Label(name);
        title.setMaxWidth(PREF_WINDOW_WIDTH);
        title.setWrapText(true);
        title.setFont(font16);
        title.setStyle(GOLDEN_COLOR_STYLE);



        ImageView cardZoomed=new ImageView(card.getImage());

        cardZoomed.setFitHeight(windowZoom.getHeight()*NINETY_PERCENT);
        cardZoomed.maxWidth(PREF_WINDOW_WIDTH/2);

        cardZoomed.preserveRatioProperty().setValue(true);


        StackPane layout = new StackPane();
        layout.getChildren().addAll(title,descr,cardZoomed);
        layout.setAlignment(title,Pos.TOP_RIGHT);
        title.setTranslateY(TITLE_ZOOM_WIDTH_TRANSL_Y);
        layout.setAlignment(descr,Pos.CENTER_RIGHT);
        layout.setAlignment(cardZoomed,Pos.CENTER_LEFT);

        cardZoomed.setTranslateX(windowZoom.getWidth()*CARD_ZOOMED_TRANSL_X_PROPERTY_CONST);
        title.setTranslateX(-windowZoom.getWidth()*THREE_PERCENT);
        descr.setTranslateX(-windowZoom.getWidth()*THREE_PERCENT);



        windowZoom.widthProperty().addListener(e->{
            cardZoomed.setTranslateX(windowZoom.getWidth()* CARD_ZOOMED_TRANSL_X_PROPERTY_CONST);
            cardZoomed.maxWidth(windowZoom.getWidth()/MAX_WIDTH_CONST_WINDOW_ZOOM);
            title.setTranslateX(-windowZoom.getWidth()*THREE_PERCENT);
            title.setMaxWidth(windowZoom.getWidth()/MAX_WIDTH_CONST_WINDOW_ZOOM);
            descr.setTranslateX(-windowZoom.getWidth()*THREE_PERCENT);
            descr.setMaxWidth(windowZoom.getWidth()/MAX_WIDTH_CONST_WINDOW_ZOOM);
            cardZoomed.setFitWidth(windowZoom.getWidth()* CARD_ZOOMER_FIT_WIDTH_PROPERTY_CONST);
        });


        windowZoom.heightProperty().addListener(e->{
            cardZoomed.setFitHeight(windowZoom.getHeight()*NINETY_PERCENT);
            cardZoomed.maxWidth(windowZoom.getWidth()/ MAX_WIDTH_CONST_WINDOW_ZOOM);
            title.setTranslateY(windowZoom.getHeight()* TITLE_TRANSL_Y_CONST_PROPERTY);
            descr.setMaxWidth(windowZoom.getWidth()/MAX_WIDTH_CONST_WINDOW_ZOOM);
        });

        layout.getStylesheets().add(CSS_FILE_NAME);
        layout.getStyleClass().add(WINDOW_BACKGROUND_STYLE);


        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);

        windowZoom.setScene(scene);
        windowZoom.showAndWait();
    }

    /**
     *
     * @param conn is server connection
     * @param question1 is the first question that has to be shown
     * @param question2 is the second question that has to be shown
     * @param options1  is the list of answers for question1
     * @param options2 is the list of answers for question2
     * @param minOptions1 is min number of answers that have to be selected in question1
     * @param maxOptions1 is max number of answers that have to be selected in question1
     * @param minOptions2 is min number of answers that have to be selected in question2
     * @param maxOptions2 is max number of answers that have to be selected in question2
     *
     * creates a double window with 2 section filled with 2 questions
     */

    private static void initDoubleWindowComponents(ServerConnection conn, String question1,String question2,List<String> options1, List<String> options2, int minOptions1, int maxOptions1, int minOptions2, int maxOptions2) {
        connection = conn;
        window = new Stage();
        window.setAlwaysOnTop(true);

        Font font12=Font.loadFont(AlertBox.class.getResourceAsStream(FONT_PATH),12);

        //Block events to other windows
        window.initModality(Modality.NONE);
        window.initStyle(StageStyle.UTILITY);
        window.setTitle(WINDOW_TITLE);
        window.setWidth(PREF_WINDOW_WIDTH*2);
        window.setHeight(PREF_WINDOW_HEIGHT);

        layout1 = new VBox(PREF_VBOX_SPACING);
        layout2=new VBox(PREF_VBOX_SPACING);

        buttons =new HBox(PREF_HBOX_SPACING);

        title1=new Label(question1);
        title1.setTextFill(Color.WHITE);
        title1.setWrapText(true);
        title1.setTextAlignment(TextAlignment.CENTER);
        title1.setFont(font12);
        title2=new Label(question2);
        title2.setTextFill(Color.WHITE);
        title2.setWrapText(true);
        title2.setTextAlignment(TextAlignment.CENTER);
        title2.setFont(font12);


        confirmButton=new Button(CONFIRM_BUTTON_TEXT);
        confirmButton.setFont(font12);
        confirmButton.setDisable(true);
        exitButton=new Button(ABORT_BUTTON_TEXT);
        exitButton.setFont(font12);
        exitButton.setOnMouseClicked(e->window.close());
        buttons.getChildren().addAll(confirmButton,exitButton);

        layout1.getChildren().add(title1);
        layout2.getChildren().add(title2);

        checkBoxes1=new ArrayList<>();
        checkBoxes2=new ArrayList<>();

        for (String option:options1) {
            CheckBox tmp=new CheckBox(option);
            tmp.setWrapText(true);
            tmp.setTextAlignment(TextAlignment.CENTER);
            tmp.setFont(font12);
            checkBoxes1.add(tmp);
            layout1.getChildren().add(tmp);
            tmp.setTextFill(Color.WHITE);
        }
        for (String option:options2) {
            CheckBox tmp=new CheckBox(option);
            tmp.setWrapText(true);
            tmp.setTextAlignment(TextAlignment.CENTER);
            tmp.setFont(font12);
            checkBoxes2.add(tmp);
            layout2.getChildren().add(tmp);
            tmp.setTextFill(Color.WHITE);
        }

        for (CheckBox checkFirst:checkBoxes1){
            checkFirst.setOnAction(e->{
                if (getNumSelectedCheckboxes(checkBoxes1)>=minOptions1&&getNumSelectedCheckboxes(checkBoxes1)<=maxOptions1) {
                    if (getNumSelectedCheckboxes(checkBoxes2)>=minOptions2&&getNumSelectedCheckboxes(checkBoxes2)<=maxOptions2)
                        confirmButton.setDisable(false);
                    for (CheckBox check:checkBoxes1){
                        if(!check.isSelected())
                            check.setDisable(true);
                    }
                }
                else{
                    confirmButton.setDisable(true);
                    for (CheckBox check:checkBoxes1){
                        check.setDisable(false);
                    }
                }
            });
        }

        for (CheckBox checkFirst:checkBoxes2){
            checkFirst.setOnAction(e->{
                if (getNumSelectedCheckboxes(checkBoxes2)>=minOptions2&&getNumSelectedCheckboxes(checkBoxes2)<=maxOptions2) {
                    if (getNumSelectedCheckboxes(checkBoxes1)>=minOptions1&&getNumSelectedCheckboxes(checkBoxes1)<=maxOptions1)
                        confirmButton.setDisable(false);
                    for (CheckBox check:checkBoxes2){
                        if(!check.isSelected()&&getNumSelectedCheckboxes(checkBoxes2)<minOptions2)
                            check.setDisable(true);
                    }
                }
                else{
                    confirmButton.setDisable(true);
                    for (CheckBox check:checkBoxes2){
                        check.setDisable(false);
                    }
                }
            });
        }
        BorderPane layoutRoot=new BorderPane();
        SplitPane dividedLayout=new SplitPane();
        ScrollPane scrollPane1=new ScrollPane(layout1);
        ScrollPane scrollPane2=new ScrollPane(layout2);

        dividedLayout.getItems().addAll(scrollPane1,scrollPane2);

        scrollPane1.setStyle(BACKGROUND_COLOR_TRANSPARENT_STYLE);
        scrollPane1.setFitToHeight(true);
        scrollPane1.setFitToWidth(true);

        scrollPane2.setStyle(BACKGROUND_COLOR_TRANSPARENT_STYLE);
        scrollPane2.setFitToHeight(true);
        scrollPane2.setFitToWidth(true);


        layoutRoot.setCenter(dividedLayout);

        layoutRoot.setBottom(buttons);

        buttons.setAlignment(Pos.CENTER);
        buttons.setPrefHeight(BUTTONS_PREF_HEIGHT);
        title1.setAlignment(Pos.CENTER);
        title2.setAlignment(Pos.CENTER);

        layout1.setAlignment(Pos.CENTER);
        layout2.setAlignment(Pos.CENTER);

        layout1.getStyleClass().add(WINDOW_BACKGROUND_STYLE);
        layout2.getStyleClass().add(WINDOW_BACKGROUND_STYLE);

        layoutRoot.getStylesheets().add(CSS_FILE_NAME);
        layoutRoot.getStyleClass().add(WINDOW_BACKGROUND_STYLE);

        dividedLayout.getStylesheets().add(CSS_FILE_NAME);
        dividedLayout.getStyleClass().add(WINDOW_BACKGROUND_STYLE);

        Scene scene = new Scene(layoutRoot);
        window.setScene(scene);
    }

    /**
     *
     * @param conn is server connection
     * @param question1 is the first question that has to be shown
     * @param question2 is the second question that has to be shown
     * @param question3 is the third question that has to be shown
     * @param options1  is the list of answers for question1
     * @param options2 is the list of answers for question2
     * @param options3 is the list of answers for question3
     * @param minOptions1 is min number of answers that have to be selected in question1
     * @param maxOptions1 is max number of answers that have to be selected in question1
     * @param minOptions2 is min number of answers that have to be selected in question2
     * @param maxOptions2 is max number of answers that have to be selected in question2
     * @param minOptions3 is min number of answers that have to be selected in question3
     * @param maxOptions3 is max number of answers that have to be selected in question3
     *
     * creates a triple window with 3 section filled with 3 questions
     */
    private static void initTripleWindowComponents(ServerConnection conn, String question1,String question2,String question3,List<String> options1, List<String> options2,List<String> options3, int minOptions1, int maxOptions1, int minOptions2, int maxOptions2,int minOptions3, int maxOptions3){
        connection = conn;
        window = new Stage();
        window.setAlwaysOnTop(true);
        Font font12=Font.loadFont(AlertBox.class.getResourceAsStream(FONT_PATH),12);

        //Block events to other windows
        window.initModality(Modality.NONE);
        window.initStyle(StageStyle.UTILITY);
        window.setTitle(WINDOW_TITLE);
        window.setWidth(PREF_WINDOW_WIDTH*3);
        window.setHeight(PREF_WINDOW_HEIGHT);

        layout1 = new VBox(PREF_VBOX_SPACING);
        layout1.setMaxWidth(PREF_WINDOW_WIDTH);
        layout2.setMaxWidth(PREF_WINDOW_WIDTH);
        layout2=new VBox(PREF_VBOX_SPACING);
        VBox layout3=new VBox(PREF_VBOX_SPACING);
        layout3.setMaxWidth(PREF_WINDOW_WIDTH);
        buttons =new HBox(PREF_HBOX_SPACING);

        title1=new Label(question1);
        title1.setTextFill(Color.WHITE);
        title1.setWrapText(true);
        title1.setTextAlignment(TextAlignment.CENTER);
        title1.setFont(font12);
        title2=new Label(question2);
        title2.setTextFill(Color.WHITE);
        title2.setWrapText(true);
        title2.setTextAlignment(TextAlignment.CENTER);
        title2.setFont(font12);
        Label title3=new Label(question3);
        title3.setTextFill(Color.WHITE);
        title3.setWrapText(true);
        title3.setTextAlignment(TextAlignment.CENTER);
        title3.setFont(font12);




        confirmButton=new Button(CONFIRM_BUTTON_TEXT);
        confirmButton.setFont(font12);
        confirmButton.setDisable(true);
        exitButton=new Button(ABORT_BUTTON_TEXT);
        exitButton.setFont(font12);
        exitButton.setOnMouseClicked(e->window.close());
        buttons.getChildren().addAll(confirmButton,exitButton);

        layout1.getChildren().add(title1);
        layout2.getChildren().add(title2);
        layout3.getChildren().add(title3);


        checkBoxes1=new ArrayList<>();
        checkBoxes2=new ArrayList<>();
        checkBoxes3=new ArrayList<>();

        for (String option:options1) {
            CheckBox tmp=new CheckBox(option);
            tmp.setWrapText(true);
            tmp.setFont(font12);
            tmp.setTextAlignment(TextAlignment.CENTER);
            checkBoxes1.add(tmp);
            layout1.getChildren().add(tmp);
            tmp.setTextFill(Color.WHITE);
        }
        for (String option:options2) {
            CheckBox tmp=new CheckBox(option);
            tmp.setWrapText(true);
            tmp.setFont(font12);
            tmp.setTextAlignment(TextAlignment.CENTER);
            checkBoxes2.add(tmp);
            layout2.getChildren().add(tmp);
            tmp.setTextFill(Color.WHITE);
        }
        for (String option:options3) {
            CheckBox tmp=new CheckBox(option);
            tmp.setWrapText(true);
            tmp.setFont(font12);
            tmp.setTextAlignment(TextAlignment.CENTER);
            checkBoxes3.add(tmp);
            layout3.getChildren().add(tmp);
            tmp.setTextFill(Color.WHITE);
        }

        for (CheckBox checkFirst:checkBoxes1){
            checkFirst.setOnAction(e->{
                if (getNumSelectedCheckboxes(checkBoxes1)>=minOptions1&&getNumSelectedCheckboxes(checkBoxes1)<=maxOptions1) {
                    if (getNumSelectedCheckboxes(checkBoxes2)>=minOptions2&&getNumSelectedCheckboxes(checkBoxes2)<=maxOptions2&&getNumSelectedCheckboxes(checkBoxes3)>=minOptions3&&getNumSelectedCheckboxes(checkBoxes3)<=maxOptions3)
                        confirmButton.setDisable(false);
                    for (CheckBox check:checkBoxes1){
                        if(!check.isSelected())
                            check.setDisable(true);
                    }
                }
                else{
                    confirmButton.setDisable(true);
                    for (CheckBox check:checkBoxes1){
                        check.setDisable(false);
                    }
                }
            });
        }

        for (CheckBox checkFirst:checkBoxes2){
            checkFirst.setOnAction(e->{
                if (getNumSelectedCheckboxes(checkBoxes2)>=minOptions2&&getNumSelectedCheckboxes(checkBoxes2)<=maxOptions2) {
                    if (getNumSelectedCheckboxes(checkBoxes1)>=minOptions1&&getNumSelectedCheckboxes(checkBoxes1)<=maxOptions1&&getNumSelectedCheckboxes(checkBoxes3)>=minOptions3&&getNumSelectedCheckboxes(checkBoxes3)<=maxOptions3)
                        confirmButton.setDisable(false);
                    for (CheckBox check:checkBoxes2){
                        if(!check.isSelected()&&getNumSelectedCheckboxes(checkBoxes2)<minOptions2)
                            check.setDisable(true);
                    }
                }
                else{
                    confirmButton.setDisable(true);
                    for (CheckBox check:checkBoxes2){
                        check.setDisable(false);
                    }
                }
            });
        }

        for (CheckBox checkFirst:checkBoxes3){
            checkFirst.setOnAction(e->{
                if (getNumSelectedCheckboxes(checkBoxes3)>=minOptions3&&getNumSelectedCheckboxes(checkBoxes3)<=maxOptions3) {
                    if (getNumSelectedCheckboxes(checkBoxes1)>=minOptions1&&getNumSelectedCheckboxes(checkBoxes1)<=maxOptions1&&getNumSelectedCheckboxes(checkBoxes2)>=minOptions2&&getNumSelectedCheckboxes(checkBoxes2)<=maxOptions2)
                        confirmButton.setDisable(false);
                    for (CheckBox check:checkBoxes3){
                        if(!check.isSelected()&&getNumSelectedCheckboxes(checkBoxes3)<minOptions3)
                            check.setDisable(true);
                    }
                }
                else{
                    confirmButton.setDisable(true);
                    for (CheckBox check:checkBoxes3){
                        check.setDisable(false);
                    }
                }
            });
        }

        BorderPane layoutRoot=new BorderPane();
        SplitPane dividedLayout=new SplitPane();

        dividedLayout.getItems().addAll(layout1,layout2,layout3);

        layoutRoot.setCenter(dividedLayout);

        layoutRoot.setBottom(buttons);

        buttons.setAlignment(Pos.CENTER);
        buttons.setPrefHeight(BUTTONS_PREF_HEIGHT);
        title1.setAlignment(Pos.CENTER);
        title2.setAlignment(Pos.CENTER);
        title3.setAlignment(Pos.CENTER);

        layout1.setAlignment(Pos.CENTER);
        layout2.setAlignment(Pos.CENTER);
        layout3.setAlignment(Pos.CENTER);


        layoutRoot.getStylesheets().add(CSS_FILE_NAME);
        layoutRoot.getStyleClass().add(WINDOW_BACKGROUND_STYLE);

        dividedLayout.getStylesheets().add(CSS_FILE_NAME);
        dividedLayout.getStyleClass().add(WINDOW_BACKGROUND_STYLE);


        Scene scene = new Scene(layoutRoot);
        window.setScene(scene);
    }
}
