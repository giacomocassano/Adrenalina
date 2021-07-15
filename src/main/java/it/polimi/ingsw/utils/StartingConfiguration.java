package it.polimi.ingsw.utils;


/**
 * Contains every information about timers and configurations to let server starts
 * in different ways with different timers
 */

public class StartingConfiguration {

    private String ipServer;
    private int socketPort;
    private int rmiPort;

    /**
     * Constructor
     * @param ipServer is the ip of the server
     * @param socketPort is the socket port
     * @param rmiPort is the rmi port
     */
    public StartingConfiguration(String ipServer, int socketPort, int rmiPort) {
        this.ipServer = ipServer;
        this.socketPort = socketPort;
        this.rmiPort = rmiPort;
    }

    /**
     * Server ip getter
     * @return ip of the server
     */
    public String getIpServer() {
        return ipServer;
    }

    /**
     * Socket port getter
     * @return socket port
     */
    public int getSocketPort() {
        return socketPort;
    }

    /**
     * Rmi port geter
     * @return rmi port
     */
    public int getRmiPort() {
        return rmiPort;
    }
}
