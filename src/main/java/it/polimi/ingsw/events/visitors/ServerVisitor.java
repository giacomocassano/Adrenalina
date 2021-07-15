package it.polimi.ingsw.events.visitors;

import it.polimi.ingsw.events.client_to_server.to_server.ConfigEvent;
import it.polimi.ingsw.events.client_to_server.to_controller.*;
import it.polimi.ingsw.events.client_to_server.to_server.SelectedName;

/**
 * Interface of visitor pattern.
 */
public interface ServerVisitor {
    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(EventFromView event);

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(ConfigEvent event);
}
