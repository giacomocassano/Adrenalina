package it.polimi.ingsw.client.user_interfaces.gui;

import it.polimi.ingsw.client.user_interfaces.UserInterface;
import it.polimi.ingsw.client.user_interfaces.model_representation.EffectInfo;
import it.polimi.ingsw.events.client_to_server.to_server.SelectedName;
import it.polimi.ingsw.events.model_to_view.*;
import it.polimi.ingsw.events.visitors.ViewVisitor;
import it.polimi.ingsw.utils.Config;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to handle events from server in Graphical User Interface
 */
public class GUIcontroller extends UserInterface implements ViewVisitor {

    private static final String LOGIN_BUTTONS_STYLE = "-fx-background-color: #2f2f2fa3;-fx-background-radius:100 ;-fx-text-fill:white;";
    private static final String LOGIN_MESSAGES_STYLE = "-fx-background-color: rgba(255,0,23,0.61);-fx-background-radius:100 ;-fx-text-fill:white;";
    private static final int NO_NAME_INSERTED_LABEL_HEIGHT = 40;
    private static final int WAITING_ROOM_MAP_PREVIEW_SIZE = 200;
    private static final String NO_CONNECTION_SELECTED_MESSAGE = "Per favore scegli il tipo di connessione!";
    private static final String NO_USERNAME_ISERTED_MESSAGE = "Per favore inserisci un username!";
    private static final String BACKGROUND_STYLE = "backgroundRoot";
    private static final int INFO_CONNECTIONS_PREF_HEIGHT = 100;
    private static final int TIME_TO_WAIT_TRANSL_X = 80;
    private static final int LOADING_GIF_TRANSL_Y = 40;
    private static final int CONNECTIONS_TRANS_X = 20;
    private static final int CONNECTIONS_TRANS_Y = 90;
    private static final int CONNECTIONS_MAX_WIDTH = 500;
    private static final int CONNECTION_PREF_WIDTH = 300;
    private static final int PREVIEW_MAP_TRANSL_X = -50;
    private static final int PREVIEW_MAP_TRASL_Y = 35;
    private static final int ZERO = 0;
    private static final int GRID_WAITING_ROOM_ROW_COL = 2;
    private static final int PREVIEW_MAP_PREF_WIDTH = 449;
    private static final int PREVIEW_MAP_PREF_HEIGHT = 400;
    private static final int HGAP_PREVIEW_MAP = 40;
    private static final int HBOX_PREF_HEIGHT_WAITING_ROOM = 120;
    private static final int NAME_ALREADY_INSERTED_PREF_HEIGHT = 40;
    private static final int NAME_ALREADY_INSERTED_TRANSL_Y = 120;
    private static final int NO_USERNAME_INSERTED_TRANSL_Y = 130;
    private static final int USERNAME_TRANSL_Y = 80;
    private static final int CHECK_BOXES_TRASL_Y = 40;
    private static final int SOC_CHECK_TRASL_X = -40;
    private static final int RMI_CHECK_TRASL_X = 50;
    private static final int USERNAME_MAX_WIDTH = 200;
    private static final int LOGIN_BUTTON_MAX_WIDTH = 100;
    private static final int LOGIN_BUTTON_MAX_HEIGHT = 20;
    private static final String LOGIN_BUTTON_TEXT = "Login";
    private static final String NAME_ALREADY_INSERTED_TEXT = "Il nome inserito è già stato scelto da un altro giocatore";
    private static final String CLIENT_DISCONNECTED_MESSAGE = "[Client] Il Client si è disconnesso";
    private static final int PRIMARY_STAGE_MIN_WIDTH = 1300;
    private static final String CONNECTION_TITLE_TEXT = "Giocatori Connessi";
    private static final int PRIMARY_STAGE_MIN_HEIGHT = 820;
    private static final String PRIMARY_STAGE_TITLE = "Adrenalina";
    private static final String RECONNECTION_MESSAGE = " si è riconnesso.";
    private static final String DISCONNECTION_MESSAGE = " si è disconnesso.";
    private static final String GRAB_AMMO_MESSAGE = " sta per raccogliere una carta munizioni.";
    private static final String GAME_STARTING_MESSAGE = "La partita sta per cominciare";
    private static final String CLIENT_CONNECTION_MESSAGE = "[Client] Il Client si è connesso con il nome di ";
    private static final String NAME_ALREADY_IN_USE_SUFFIX_MESSAGE = " è stato già scelto da un altro giocatore";
    private static final String NAME_ALREADY_IN_USE_PREFIX_MESSAGE = "[Client] Il nome ";
    private static final String CLIENT_CONNECTING_MESSAGE = "[Client] Il client sta per connettersi";
    private static final String ERROR = "Errore";
    private static final String TIME_EXCEEDED_MESSAGE = "Tempo scaduto";
    private static final String GRAB_WEAPON_MESSAGE = " sta comprando un'arma.";
    private static final String RELOAD_WEAPON_MESSAGE = " sta per ricaricare le sue armi.";
    private static final String SETTING_EFFECT_MESSAGE = " sta settando l'effetto aggiuntivo.";
    private static final String SELECTING_EFFECT_MESSAGE = " sta scegliendo l'effetto aggiuntivo.";
    private static final String SELECTING_MOVE_MESSAGE = " sta scegliendo dove muoversi.";
    private static final String SELECTING_WEAPON_SHOOT_MESSAGE = " sta scegliendo l'arma con cui sparare.";
    private static final String SPAWN_MESSAGE = " sta per nascere.";
    private static final String USING_EFFECT_MESSAGE = " sta per usare l'effetto base.";
    private static final String SELECTING_BASE_EFFECT_MESSAGE = " sta scegliendo l'effetto base.";
    private static final String SERVER_NOT_CONNECTED_MESSAGE = "Il server non è ancora disponibile ad accettare connessioni";
    private static final String SERVER_NOT_CONNECTED_LABEL_TEXT = "Il server non è ancora pronto,\nprova tra poco";
    private Stage primaryStage;
    private StackPane stackPaneRoot;
    private String playerName;
    private StackPane gameStackPaneRoot;
    private StackPane loginStackPane;
    private BorderPane waitingRoomBorderPane;
    private ListView<Label> loggedList;
    private Label infoConnections;
    private TextField username;
    private RadioButton socCheck;
    private RadioButton rmiCheck;
    private Label noUsernameInserted;
    private Label nameAlreadyInserted;
    private GameSession gameSession;
    private Boolean connectionSelected;
    private Button loginButton;
    private ImageView loadingGif;
    private Label timeToWait;
    private Label waitingRoomInfo;
    private ImageView background;
    private boolean gameStarted;
    private boolean nameErrorAlreadyShowed;
    private static final String USERNAME_DEFAULT_STRING="Username";
    private static final String CSS_PATH="Style.css";
    private static final String LOGIN_PAGE_BACKGROUND_IMAGE_PATH="/images/OtherStuff/adrenalina.png";
    private static final String PNG_EXTENSION=".png";
    private static final String MAPS_IMAGES_PATH="/images/maps/Mappa";
    private static final String YELLOW_ROBOT_IMAGE_PATH= "/images/OtherStuff/YELLOWRobot.png";
    private static final String LOADING_GIF_PATH="/images/OtherStuff/loading.gif";
    private static final String FONT_PATH="/font/Ethnocentric.ttf";
    private static final String SOCKET="Socket";
    private static final String RMI="RMI";
    private static final String CONNECTION_MESSAGE="Uno o più giocatori si è disconnesso...";
    private static final String WAITING_PLAYERS_MESSAGE="In attesa di altri giocatori...";


    /**
     * GuiController constructor
     * @param config is the configuration object used to config user interface
     */
    public GUIcontroller(Config config) {
        super(config);
        this.gameStarted = false;
        this.nameErrorAlreadyShowed=false;
        this.primaryStage = null;
        this.stackPaneRoot = new StackPane();
        this.playerName = null;
        this.loginStackPane =new StackPane();
        this.waitingRoomBorderPane =new BorderPane();
        this.loggedList=new ListView<>();
        this.username=new TextField();
        this.noUsernameInserted = new Label();
        this.socCheck=new RadioButton(SOCKET);
        this.rmiCheck=new RadioButton(RMI);
        this.nameAlreadyInserted=new Label();
        this.connectionSelected=false;
        this.infoConnections=new Label(CONNECTION_MESSAGE);
        this.loadingGif=new ImageView(new Image(getClass().getResourceAsStream(LOADING_GIF_PATH)));
        this.timeToWait=new Label();
        this.waitingRoomInfo=new Label(WAITING_PLAYERS_MESSAGE);
    }

    /**
     * Method that visit an event recived from model. Override from User Interface.
     * @param event is event recived from model
     */
    @Override
    public void receiveEventFromModel(EventFromModel event) {
        //Visitor acceptControllerVisitor the event and, in the method, the event call the corresponding visitor method
        event.acceptViewVisitor(this);
    }

    /**
     * Prints an error message if server is not available
     */
    @Override
    public void showServerNotAvailableError() {
        if(gameStarted){
            Platform.runLater(() -> AlertBox.showServerErrorDialog());
        }else {
            noUsernameInserted.setVisible(true);
            noUsernameInserted.setText(SERVER_NOT_CONNECTED_LABEL_TEXT);
            System.out.println(SERVER_NOT_CONNECTED_MESSAGE);
        }
    }

    /**
     * Visit method of BasicEffectRequest
     * @param event is a basic effect request sent by server
     */
    @Override
    public void visit(BasicEffectRequest event) {
        if(event.getPlayer().equals(playerName))
            Platform.runLater(() -> AlertBox.askBasicEffect(getServerConnection(), getPlayerName(), event.getBasicEffects(), event.getPayingEmpowers()));
        else
            Platform.runLater(() -> gameSession.setMessages(event.getPlayer() + SELECTING_BASE_EFFECT_MESSAGE));

    }

    /**
     * Visit method of BasicEffectUse Request
     * @param event is a basic effect use request sent by server
     */
    @Override
    public void visit(BasicEffectUseRequest event) {
        if(event.getPlayer().equals(playerName))
            Platform.runLater(() -> AlertBox.askBasicEffectUse(getServerConnection(), getPlayerName(), event.getPlayerNames(), event.getSquarePositions(), event.getRecoilPositions(), event.getMaxPlayers(), event.getMaxSquares()));
        else
            Platform.runLater(() -> gameSession.setMessages(event.getPlayer() + USING_EFFECT_MESSAGE));
    }

    /**
     * Visit method of Spawn Request event.
     * @param event is a Spawn Request message sent from server
     */
    @Override
    public void visit(SpawnRequest event) {
        if(event.getPlayer().equals(playerName))
            Platform.runLater(() -> AlertBox.askSpawn(getServerConnection(), event.getPlayer(), event.getEmpowers()));
        else
            Platform.runLater(() -> gameSession.setMessages(event.getPlayer() + SPAWN_MESSAGE));
    }

    /**
     * Visit method of EmpowerRequest
     * @param event is an empower request sent by server
     */
    @Override
    public void visit(EmpowerRequest event) {
        if(event.getPlayer().equals(playerName))
            Platform.runLater(() -> AlertBox.askEmpower(getServerConnection(), getPlayerName(), event.getValidEmpowers()));
    }

    /**
     * Visit method of MoveRequest
     * @param event is a Move request sent by server
     */
    @Override
    public void visit(MoveRequest event) {
        if(event.getPlayer().equals(playerName)) {
            Platform.runLater(() -> AlertBox.askMove(this.getServerConnection(), getPlayerName(), event.getValidMoves(), event.getActionNumber(), event.getMoveNumber()));
        }if (event.isFinalFrenzy()) {
                gameSession.startFinalFrenzy();
        }

    }

    /**
     * Visit method of SpawnRequest
     * @param event is a spawn request sent by server
     */
    @Override
    public void visit(RespawnRequest event) {
        if(event.getPlayer().equals(playerName))
            Platform.runLater(() -> AlertBox.askRespawn(getServerConnection(), getPlayerName(), event.getEmpowers()));
    }

    /**
     * Visit method of ShootWeaponRequest
     * @param event is a ShootWeapon request sent by server
     */
    @Override
    public void visit(ShootWeaponRequest event) {
        if(event.getPlayer().equals(playerName))
            Platform.runLater(() -> AlertBox.askWeaponToShoot(getServerConnection(), getPlayerName(), event.getWeapons()));
        else
            Platform.runLater(() -> gameSession.setMessages(event.getPlayer() + SELECTING_WEAPON_SHOOT_MESSAGE));
    }

    /**
     * Visit method of SquareMoveRequest
     * @param event is a SquareMove request sent by server
     */
    @Override
    public void visit(SquareMoveRequest event) {
        if(event.getPlayer().equals(playerName))
            Platform.runLater(() -> AlertBox.askSquareToMove(getServerConnection(), getPlayerName(), event.getPositions(), event.getInitialPosition()));
        else
            Platform.runLater(() -> gameSession.setMessages(event.getPlayer() + SELECTING_MOVE_MESSAGE));
    }

    /**
     * Visit method of UltraEffectRequest
     * @param event is an ultra effect request sent by server
     */
    @Override
    public void visit(UltraEffectRequest event) {
        if(event.getPlayer().equals(playerName)) {
            List<EffectInfo> ultras = new ArrayList<>();
            ultras.add(event.getUltra());
            Platform.runLater(() -> AlertBox.askUltraEffect(getServerConnection(), getPlayerName(), ultras, event.getPayingEmpowers()));
        }else
            Platform.runLater(() -> gameSession.setMessages(event.getPlayer() + SELECTING_EFFECT_MESSAGE));
    }

    /**
     * Visit method of UltraEffectUseRequest
     * @param event is an ultra effect use request sent by server
     */
    @Override
    public void visit(UltraEffectUseRequest event) {
        if(event.getPlayer().equals(playerName))
            Platform.runLater(() -> AlertBox.askUltraEffectUse(getServerConnection(), getPlayerName(), event.getPlayerNames(), event.getSquarePositions(), event.getMaxPlayers(), event.getMaxSquares()));
        else
            Platform.runLater(() -> gameSession.setMessages(event.getPlayer() + SETTING_EFFECT_MESSAGE));
    }

    /**
     * Visit method of WeaponsToReload
     * @param event is a weapons to reload request sent by server
     */
    @Override
    public void visit(WeaponsToReloadRequest event) {
        if(event.getPlayer().equals(playerName))
            Platform.runLater(() -> AlertBox.askWeaponToReload(getServerConnection(), getPlayerName(), event.getWeaponsToReload(), event.getPayingEmpowers()));
        else
            Platform.runLater(() -> gameSession.setMessages(event.getPlayer() + RELOAD_WEAPON_MESSAGE));
    }

    /**
     * Visit method of WeaponToGrabRequest
     * @param event is an weapon to grab request sent by server
     */
    @Override
    public void visit(WeaponToGrabRequest event) {
        if(event.getPlayer().equals(playerName))
            Platform.runLater(()->AlertBox.askWeaponToGrab(getServerConnection(),getPlayerName(),event.getWeaponsToGrab(), event.getWeaponsToDrop(), event.getPayingEmpowers()));
        else
            Platform.runLater(() -> gameSession.setMessages(event.getPlayer() + GRAB_WEAPON_MESSAGE));
    }
    /**
     * Visit method of a Time Exceeded Notification
     * @param event is a Time exceeded notification
     */

    @Override
    public void visit(TimeExceededNotification event) {
        Platform.runLater(() -> AlertBox.closeAlertBox());
        Platform.runLater(() -> gameSession.setMessages(TIME_EXCEEDED_MESSAGE));
    }

    /**
     * Visit method of messageNotification event.
     * @param event is a messageNotification event used to give some infos to client.
     */
    @Override
    public void visit(MessageNotification event) {
        if(event.isInfoMessage())
            Platform.runLater(() -> gameSession.setMessages(event.getMessage()));
        else if(event.isErrorMessage()) {
            Platform.runLater(() -> AlertBox.showErrorDialog(ERROR, ERROR, event.getMessage()));
        }
    }

    /**
     * Visit-method of GameRepUpdate
     * @param event is a Game Representation update event
     * More infos in package @events, same class.
     */
    @Override
    public void visit(GameRepUpdate event) {
        Platform.runLater(()->{
            this.playerName = event.getMyPlayerRep().getName();
            if(event.isGameStarting()) {
                gameSession.createGame(event.getMapRep(),event.getGameBoardRepresentation(),event.getMyPlayerRep(),event.getOtherPlayersReps());
                waitingRoomBorderPane.setVisible(false);
                gameStackPaneRoot.setVisible(true);
                adjustDimensions();
            }
            else {
                gameSession.updateGame(event.getMapRep(), event.getOtherPlayersReps(), event.getMyPlayerRep(), event.getGameBoardRepresentation().getKillshotTrackRep());
            }
        });
    }


    /**
     * Visit method of Login Request event.
     * @param event is a loginRequest event.
     */
    @Override
    public void visit(LoginRequest event) {
        noUsernameInserted.setVisible(false);
        System.out.println(CLIENT_CONNECTING_MESSAGE);
        Platform.runLater(()->getServerConnection().asyncSendEvent(new SelectedName(playerName)));
    }

    /**
     * Visit method of LoginSuccess event
     * @param event is a login success event.
     */
    @Override
    public void visit(LoginSuccess event) {
        getServerConnection().startPingThread();
        loginStackPane.setVisible(false);
        waitingRoomBorderPane.setVisible(true);
        this.gameStarted = true;
        nameErrorAlreadyShowed=false;
        System.out.println(CLIENT_CONNECTION_MESSAGE +playerName);
    }

    /**
     * Visit method of LoginError event
     * @param event is a login error event
     */
    @Override
    public void visit(LoginError event) {
        Platform.runLater(()->{
            nameAlreadyInserted.setVisible(true);
            noUsernameInserted.setVisible(false);
            if(!nameErrorAlreadyShowed) {
                System.out.println(NAME_ALREADY_IN_USE_PREFIX_MESSAGE + playerName + NAME_ALREADY_IN_USE_SUFFIX_MESSAGE);
                nameErrorAlreadyShowed=true;
            }
            Platform.runLater(() -> getServerConnection().asyncSendEvent(new SelectedName(playerName)));


        });

    }

    /**
     * Visit-method of EmpowerUseRequest
     * More at EmpowerUseRequest in the package @events.
     * @param request is an empower use request
     */
    @Override
    public void visit(EmpowerUseRequest request) {
        Platform.runLater(() -> AlertBox.askEmpowerUse(getServerConnection(), getPlayerName(), request.getEmpower(), request.getPossibleTargets(), request.getPossiblePositions(), request.getPayingEmpowers(), request.getAmmoCubes(), request.isPayable()));
    }

    /**
     * WaitingRoom update visit method.
     * @param update is a WaitingRoomUpdate, more at the same class in @events package
     */
    @Override
    public void visit(WaitingRoomUpdate update) {
        Platform.runLater(()->{
            infoConnections.setVisible(false);
            if(update.isStarting()) {
                timeToWait.setVisible(true);
                loadingGif.setVisible(true);
                waitingRoomInfo.setText(GAME_STARTING_MESSAGE);
                timeToWait.setText(String.valueOf(update.getTimer()));
            }
            if(update.getDisconnectedPlayer()!=null) {
                infoConnections.setText("Il giocatore " + update.getDisconnectedPlayer() + DISCONNECTION_MESSAGE);
                infoConnections.setVisible(true);
            }
            loggedList.getItems().clear();
            for(String name:update.getWaitingPlayers()){
                if (playerName.equalsIgnoreCase(name)){
                    Label label =new Label(playerName+" (tu)");
                    label.setFont(Font.loadFont(getClass().getResourceAsStream(FONT_PATH),16));
                    loggedList.getItems().add(label);
                }
                else{
                    Label label =new Label(name);
                    label.setFont(Font.loadFont(getClass().getResourceAsStream(FONT_PATH),16));
                    loggedList.getItems().add(label);
                }
        }});
    }

    /**
     * Visit of ShooterMovementRequest. More at ShooterMovementRequest class in events.
     * @param request is a shooter mov. request
     */
    @Override
    public void visit(ShooterMovementRequest request) {
        if(request.getPlayer().equals(playerName))

            Platform.runLater(() -> AlertBox.askShooterMovement(getServerConnection(), getPlayerName(), request.getMovement(), request.getPossiblePositions(), request.getPayingEmpowers()));
    }

    /**
     * Visit-method of GrabAmmo request
     * More at GrabAmmoRequest in the package @events.
     * @param event is a grab ammo request
     */
    @Override
    public void visit(GrabAmmoRequest event) {
        if(event.getPlayer().equals(playerName))
            Platform.runLater(() -> AlertBox.askGrabAmmo(getServerConnection(), getPlayerName()));
        else
            Platform.runLater(() -> gameSession.setMessages(event.getPlayer() + GRAB_AMMO_MESSAGE));
    }

    /**
     * Visit method of GameOverNotification event.
     * @param notification is a GameOverNotification, more at GameOverNotification.
     */
    @Override
    public void visit(GameOverNotification notification) {
        Platform.runLater(()->AlertBox.createWinningAlert(notification.getWinner(),notification.getRanking(),notification.getWinnerColor(),playerName));
    }

    /**
     * Visit method of DisconnectionNotification. More at @DisconnectionNotification
     * @param notification is the DisconnectionNotification message sent by server
     */
    @Override
    public void visit(DisconnectionNotification notification) {
        if(gameStarted)
            Platform.runLater(() -> gameSession.setMessages(notification.getDisconnectedPlayer() + DISCONNECTION_MESSAGE));
    }

    /**
     * Visit method of ReconnectionNotification. More at @ReconnectionNotification
     * @param notification is the ReconnectionNotification message sent by server
     */
    @Override
    public void visit(ReconnectionNotification notification) {
        if(gameStarted)
            Platform.runLater(() -> {
                if (!playerName.equals(notification.getReconnectedPlayer()))
                    gameSession.setMessages(notification.getReconnectedPlayer() + RECONNECTION_MESSAGE);
            });
    }
    /**
     * Visit of Ping client event. More infos at PingClient in events
     * @param event is the ping client event.
     */
    @Override
    public void visit(PingClient event) {
        getServerConnection().pingReceived();
        new Thread(() -> getServerConnection().pingServer()).start();
    }

    /**
     * adjusts window's dimensions basing on the screen and constants
     */
    private void adjustDimensions(){
        gameSession.adjustDimensions(primaryStage.getHeight(),primaryStage.getWidth(),primaryStage);
    }

    /**
     *
     * @param primaryStage is the primaryStage created in LaunchClient
     * Drows all section of login session, waiting session and game session and
     * sets visible only loginSession
     */
    public void start(Stage primaryStage){
        this.gameSession=new GameSession(this.stackPaneRoot);
        this.primaryStage=primaryStage;
        primaryStage.setTitle(PRIMARY_STAGE_TITLE);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(YELLOW_ROBOT_IMAGE_PATH)));

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setMinWidth(PRIMARY_STAGE_MIN_WIDTH);
        primaryStage.setMinHeight(PRIMARY_STAGE_MIN_HEIGHT);
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        createLogin();
        createWaitingRoom();
        this.gameStackPaneRoot=gameSession.getGameStackPaneRoot();


        Scene scene=new Scene(stackPaneRoot,primaryStage.getWidth(),primaryStage.getHeight());

        loginStackPane.setVisible(true);
        waitingRoomBorderPane.setVisible(false);
        gameStackPaneRoot.setVisible(false);

        primaryStage.setScene(scene);
        primaryStage.show();


        primaryStage.setOnCloseRequest(e-> {
            System.out.println(CLIENT_DISCONNECTED_MESSAGE);
            Platform.exit();
            System.exit(ZERO);
        });
    }

    /**
     * creates login window and sets all the properties of username textField and login button
     */
    private void createLogin() {

        stackPaneRoot.getChildren().add(loginStackPane);

        Font font14=Font.loadFont(getClass().getResourceAsStream(FONT_PATH),14);

        Image image = new Image(getClass().getResourceAsStream(LOGIN_PAGE_BACKGROUND_IMAGE_PATH), primaryStage.getWidth(), primaryStage.getHeight(), false, false);



        background = new ImageView(image);

        background.fitHeightProperty().bind(primaryStage.heightProperty());
        background.fitWidthProperty().bind(primaryStage.widthProperty());


        noUsernameInserted.setTextFill(Color.BLACK);
        noUsernameInserted.setStyle(LOGIN_MESSAGES_STYLE);
        noUsernameInserted.setPrefHeight(NO_NAME_INSERTED_LABEL_HEIGHT);
        noUsernameInserted.setFont(font14);


        nameAlreadyInserted.setText(NAME_ALREADY_INSERTED_TEXT);


        loginButton = new Button(LOGIN_BUTTON_TEXT);
        loginButton.setFont(font14);

        loginButton.setStyle(LOGIN_BUTTONS_STYLE);

        username.setText(USERNAME_DEFAULT_STRING);

        username.setStyle(LOGIN_BUTTONS_STYLE);
        username.setFont(font14);


        Font font12=Font.loadFont(getClass().getResourceAsStream(FONT_PATH),12);
        socCheck.setTextFill(Color.WHITE);
        socCheck.setFont(font12);
        rmiCheck.setTextFill(Color.WHITE);
        rmiCheck.setFont(font12);


        loginStackPane.getChildren().add(background);
        loginStackPane.getChildren().add(loginButton);
        loginButton.setMaxSize(LOGIN_BUTTON_MAX_WIDTH, LOGIN_BUTTON_MAX_HEIGHT);
        username.setMaxWidth(USERNAME_MAX_WIDTH);

        loginStackPane.getChildren().add(username);
        loginStackPane.getChildren().add(noUsernameInserted);
        loginStackPane.getChildren().add(nameAlreadyInserted);
        loginStackPane.getChildren().addAll(socCheck,rmiCheck);

        loginStackPane.setAlignment(Pos.CENTER);


        socCheck.setTranslateY(CHECK_BOXES_TRASL_Y);
        socCheck.setTranslateX(SOC_CHECK_TRASL_X);
        rmiCheck.setTranslateY(CHECK_BOXES_TRASL_Y);
        rmiCheck.setTranslateX(RMI_CHECK_TRASL_X);


        username.setTranslateY(USERNAME_TRANSL_Y);

        noUsernameInserted.setTranslateY(NO_USERNAME_INSERTED_TRANSL_Y);
        nameAlreadyInserted.setTranslateY(NAME_ALREADY_INSERTED_TRANSL_Y);

        nameAlreadyInserted.setVisible(false);
        nameAlreadyInserted.setStyle(LOGIN_MESSAGES_STYLE);
        nameAlreadyInserted.setPrefHeight(NAME_ALREADY_INSERTED_PREF_HEIGHT);
        nameAlreadyInserted.setFont(font14);
        loginButton.setDisable(true);

        setLoginActions();

    }

    /**
     * creates waitingRoom window
     */
    private void createWaitingRoom(){

        stackPaneRoot.getChildren().add(waitingRoomBorderPane);

        Font font40=Font.loadFont(getClass().getResourceAsStream(FONT_PATH),40);


        StackPane center=new StackPane();
        HBox hBox=new HBox();
        waitingRoomInfo.setTextFill(Color.WHITE);
        waitingRoomInfo.setFont(font40);
        hBox.getChildren().add(waitingRoomInfo);



        hBox.setAlignment(Pos.CENTER);
        hBox.setPrefHeight(HBOX_PREF_HEIGHT_WAITING_ROOM);
        GridPane previewMaps=new GridPane();

        previewMaps.setHgap(HGAP_PREVIEW_MAP);
        previewMaps.setPrefHeight(PREVIEW_MAP_PREF_HEIGHT);
        previewMaps.setPrefWidth(PREVIEW_MAP_PREF_WIDTH);


        previewMaps.getColumnConstraints().add(new ColumnConstraints(WAITING_ROOM_MAP_PREVIEW_SIZE,WAITING_ROOM_MAP_PREVIEW_SIZE,WAITING_ROOM_MAP_PREVIEW_SIZE));
        previewMaps.getColumnConstraints().add(new ColumnConstraints(WAITING_ROOM_MAP_PREVIEW_SIZE,WAITING_ROOM_MAP_PREVIEW_SIZE,WAITING_ROOM_MAP_PREVIEW_SIZE));
        previewMaps.getRowConstraints().add(new RowConstraints(WAITING_ROOM_MAP_PREVIEW_SIZE,WAITING_ROOM_MAP_PREVIEW_SIZE,WAITING_ROOM_MAP_PREVIEW_SIZE));
        previewMaps.getRowConstraints().add(new RowConstraints(WAITING_ROOM_MAP_PREVIEW_SIZE,WAITING_ROOM_MAP_PREVIEW_SIZE,WAITING_ROOM_MAP_PREVIEW_SIZE));

        int mapNumber=1;
        for (int i = ZERO; i< GRID_WAITING_ROOM_ROW_COL; i++)
            for (int j = ZERO; j<GRID_WAITING_ROOM_ROW_COL; j++) {
                String url=MAPS_IMAGES_PATH+mapNumber+PNG_EXTENSION;
                Image image=new Image(getClass().getResourceAsStream(url),WAITING_ROOM_MAP_PREVIEW_SIZE,WAITING_ROOM_MAP_PREVIEW_SIZE,true,true);
                previewMaps.add(new ImageView(image),i,j);
                mapNumber++;
            }

        previewMaps.setAlignment(Pos.CENTER_RIGHT);
        previewMaps.setTranslateY(PREVIEW_MAP_TRASL_Y);
        previewMaps.setTranslateX(PREVIEW_MAP_TRANSL_X);


        TitledPane connections=new TitledPane();


        Font font14=Font.loadFont(getClass().getResourceAsStream(FONT_PATH),14);

        connections.setPrefWidth(CONNECTION_PREF_WIDTH);
        connections.setMaxWidth(CONNECTIONS_MAX_WIDTH);
        connections.setContent(loggedList);
        connections.setText(CONNECTION_TITLE_TEXT);
        connections.setTranslateY(CONNECTIONS_TRANS_Y);
        connections.setTranslateX(CONNECTIONS_TRANS_X);
        connections.setFont(font14);

        waitingRoomBorderPane.setLeft(connections);
        waitingRoomBorderPane.setRight(previewMaps);


        center.getChildren().add(loadingGif);
        loadingGif.setTranslateY(LOADING_GIF_TRANSL_Y);
        loadingGif.setVisible(false);
        timeToWait.setVisible(false);
        center.getChildren().add(timeToWait);
        timeToWait.setTranslateY(TIME_TO_WAIT_TRANSL_X);
        timeToWait.setTextFill(Color.WHITE);

        timeToWait.setFont(font14);

        waitingRoomBorderPane.setTop(hBox);
        waitingRoomBorderPane.setCenter(center);

        infoConnections.setPrefHeight(INFO_CONNECTIONS_PREF_HEIGHT);
        infoConnections.setPrefWidth(primaryStage.getWidth());
        infoConnections.setAlignment(Pos.CENTER_RIGHT);
        infoConnections.setTextFill(Color.RED);
        infoConnections.setFont(font40);
        waitingRoomBorderPane.setBottom(infoConnections);
        infoConnections.setVisible(false);

        waitingRoomBorderPane.getStylesheets().add(CSS_PATH);
        waitingRoomBorderPane.getStyleClass().add(BACKGROUND_STYLE);


    }


    /**
     * sets all the functions of interactives
     * elements of login page and sets all the restriction
     * for name inserted and connection selected
     */

    private void setLoginActions(){
        socCheck.setOnAction(e-> {
            if(rmiCheck.isSelected()) {
                rmiCheck.setSelected(false);
                loginButton.setDisable(false);
            }
            noUsernameInserted.setVisible(false);
        });

        rmiCheck.setOnAction(e->{
            if(socCheck.isSelected()) {
                socCheck.setSelected(false);
                loginButton.setDisable(false);
            }
            noUsernameInserted.setVisible(false);
        });

        loginButton.setOnAction(e -> {
            if (!username.getText().equals("") && !username.getText().equals(USERNAME_DEFAULT_STRING)) {
                playerName = username.getText();
                Platform.runLater(()->{
                    if(!connectionSelected) {
                    if (socCheck.isSelected()) {
                        startSocketClient();
                    } else if (rmiCheck.isSelected())
                        startRmiClient();
                    if(super.getServerConnection()!=null)
                        connectionSelected=true;
                }});
            }
            else {
                noUsernameInserted.setText("");
                noUsernameInserted.setText(NO_USERNAME_ISERTED_MESSAGE);
            }
        });

        username.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (!username.getText().equals("") && !username.getText().equals(USERNAME_DEFAULT_STRING)&&(rmiCheck.isSelected()||socCheck.isSelected())) {
                    playerName=username.getText();
                    Platform.runLater(()->{
                        if(!connectionSelected) {
                            if (socCheck.isSelected()) {
                                startSocketClient();
                            } else if (rmiCheck.isSelected())
                                startRmiClient();
                            if(super.getServerConnection()!=null)
                                connectionSelected=true;
                        }
                    });
                } else {
                    noUsernameInserted.setVisible(true);
                    noUsernameInserted.setText("");
                    noUsernameInserted.setText(NO_CONNECTION_SELECTED_MESSAGE);
                }
            }
        });

        username.textProperty().addListener((obs, oldText, newText)->loginButton.setDisable(newText.isEmpty()||(!socCheck.isSelected()&&!rmiCheck.isSelected())));

        socCheck.selectedProperty().addListener((obs,oldChoise,newChoise)->  loginButton.setDisable(!newChoise));

        rmiCheck.selectedProperty().addListener((obs,oldChoise,newChoise)->  loginButton.setDisable(!newChoise));

        background.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2) {
                if (!primaryStage.isFullScreen()) {
                    primaryStage.setFullScreen(true);
                } else {
                    primaryStage.setFullScreen(false);
                }
            }
        });

        username.setOnMouseClicked(e -> {
            if(username.getText().equals(USERNAME_DEFAULT_STRING))
                username.clear();
        });
    }

    public String getPlayerName() { return playerName; }
}


