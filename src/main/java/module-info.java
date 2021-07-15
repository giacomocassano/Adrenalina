module it.polimi.ingsw {

    requires javafx.controls;
    requires java.rmi;
    requires java.xml;
    requires gson;

    exports it.polimi.ingsw.client;
    exports it.polimi.ingsw.server to java.rmi;
}