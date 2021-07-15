package it.polimi.ingsw.events.client_to_server.to_server;

import it.polimi.ingsw.events.visitors.ConfigVisitor;

public class PingServer extends ConfigEvent{

    public final static int OK = 1;
    public final static int ERR = -1;

    private final int response;

    public PingServer(int response) {
        this.response = response;
    }

    public int getResponse() {
        return response;
    }

    @Override
    public void acceptConfigVisitor(ConfigVisitor visitor) {
        visitor.visit(this);
    }
}
