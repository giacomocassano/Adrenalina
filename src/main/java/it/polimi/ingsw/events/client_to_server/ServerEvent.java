package it.polimi.ingsw.events.client_to_server;

import it.polimi.ingsw.events.visitors.ServerVisitor;

import java.io.Serializable;

public abstract class ServerEvent implements Serializable {
    public abstract void acceptServerVisitor(ServerVisitor visitor);
}
