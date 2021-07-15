package it.polimi.ingsw.events.client_to_server.to_server;

import it.polimi.ingsw.events.client_to_server.ServerEvent;
import it.polimi.ingsw.events.visitors.ConfigVisitor;
import it.polimi.ingsw.events.visitors.ServerVisitor;

import java.io.Serializable;

public abstract class ConfigEvent extends ServerEvent implements Serializable {

    @Override
    public void acceptServerVisitor(ServerVisitor visitor) {
        visitor.visit(this);
    }

    public abstract void acceptConfigVisitor(ConfigVisitor visitor);

}
