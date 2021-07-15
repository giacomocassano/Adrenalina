package it.polimi.ingsw.utils;

/**
 * Class that build and set the default client configuration.
 * It sets: user interface method (cli or gui), communication method with server (socket or RMI),
 * the server ip, server socket port and server RMI port.
 */
public class Config {
    private String uiMethod;
    private String communicationMethod;
    private String serverIp;
    private int socPort;
    private int rmiPort;

    private static final String UI_CMD = "-ui";
    private static final String COM_CMD = "-com";
    private static final String IP_CMD = "-ip";
    private static final String SP_CMD = "-sp";
    private static final String RP_CMD = "-rp";
    private static final String INFO_CMD = "-info";
    private static final String CLI="cli";
    private static final String GUI="gui";
    private static final String SOC="soc";
    private static final String RMI="rmi";

    private static final String defaultIp = "localhost";
    private static final int defaultRMIPort = 12333;
    private static final int defaultSocPort = 12345;

    /**
     * Default constructor.
     */
    public Config() {
        uiMethod = null;
        communicationMethod = null;
        setDefaultConfiguration();
    }

    /**
     * Sets the default parameters of server ip, server socket and rmi ports.
     */
    private void setDefaultConfiguration() {
        serverIp = defaultIp;
        socPort = defaultSocPort;
        rmiPort = defaultRMIPort;
    }

    /**
     * Loads the parameters from configuration file.
     */
    public void loadFromConfigurationFile() {
        try {
            StartingConfiguration startingConfiguration = XMLHelper.readConfigs();
            this.serverIp = startingConfiguration.getIpServer();
            this.socPort = startingConfiguration.getSocketPort();
            this.rmiPort = startingConfiguration.getRmiPort();
        } catch (Exception e) {
            //Use default configuration
            setDefaultConfiguration();
        }
    }

    /**
     * Builds configuration from program arguments.
     * @param args are the program arguments.
     * @return the built configuration.
     */
    public static Config buildConfig(String[] args) {
        Config config = new Config();
        config.loadFromConfigurationFile();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case UI_CMD:
                    if(i+1 < args.length && (args[i+1].equals(CLI) || args[i+1].equals(GUI)))
                        config.setUiMethod(args[i+1]);
                    break;
                case COM_CMD:
                    if(i+1 < args.length && (args[i+1].equals(SOC) || args[i+1].equals(RMI)))
                        config.setCommunicationMethod(args[i+1]);
                    break;
                case IP_CMD:
                    if(i+1 < args.length)
                        config.setServerIp(args[i+1]);
                    break;
                case SP_CMD:
                    if(i+1 < args.length)
                        config.setSocPort(Integer.parseInt(args[i+1]));
                    break;
                case RP_CMD:
                    if(i+1 < args.length)
                        config.setRmiPort(Integer.parseInt(args[i+1]));
                    break;
                case INFO_CMD:
                    WriterHelper.printAdrenalinaTitle();
                    WriterHelper.printCommandsInfoTable();
                    System.exit(0);
                    break;
            }
        }
        return config;
    }


    /**
     * Setter.
     * @param uiMethod is the ui method to set.
     */
    public void setUiMethod(String uiMethod) {
        this.uiMethod = uiMethod;
    }

    /**
     * Setter.
     * @param communicationMethod is the communication method to set.
     */
    public void setCommunicationMethod(String communicationMethod) {
        this.communicationMethod = communicationMethod;
    }

    /**
     * Setter.
     * @param serverIp is the server ip address to set.
     */
    private void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    /**
     * Setter.
     * @param socPort is the socket port to set.
     */
    private void setSocPort(int socPort) {
        this.socPort = socPort;
    }

    /**
     * Setter.
     * @param rmiPort is the rmi port to set.
     */
    private void setRmiPort(int rmiPort) {
        this.rmiPort = rmiPort;
    }

    /**
     * Getter.
     * @return the ui method.
     */
    public String getUiMethod() {
        return uiMethod;
    }

    /**
     * Getter.
     * @return the communication method.
     */
    public String getCommunicationMethod() {
        return communicationMethod;
    }

    /**
     * Getter.
     * @return the server ip address.
     */
    public String getServerIp() {
        return serverIp;
    }

    /**
     * Getter.
     * @return socket port.
     */
    public int getSocPort() {
        return socPort;
    }

    /**
     * Getter.
     * @return the rmi port.
     */
    public int getRmiPort() {
        return rmiPort;
    }
}
