package it.polimi.ingsw.events.client_to_server.to_server;

import it.polimi.ingsw.events.visitors.ConfigVisitor;

import java.io.Serializable;

public class SelectedName extends ConfigEvent implements Serializable {

    private String name;

    public SelectedName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void acceptConfigVisitor(ConfigVisitor visitor) {
        visitor.visit(this);
    }


}
