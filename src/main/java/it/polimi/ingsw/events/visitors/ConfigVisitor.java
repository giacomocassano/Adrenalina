package it.polimi.ingsw.events.visitors;

import it.polimi.ingsw.events.client_to_server.to_server.PingServer;
import it.polimi.ingsw.events.client_to_server.to_server.SelectedName;

/**
 * Interface of visitor patter for configuration events.
 */
public interface ConfigVisitor {

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(SelectedName event);

    /**
     * Visitor pattern method.
     * @param ping is the event to visit.
     */
    void visit(PingServer ping);
}
