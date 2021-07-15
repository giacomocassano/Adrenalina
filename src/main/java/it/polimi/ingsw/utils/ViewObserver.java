package it.polimi.ingsw.utils;

import it.polimi.ingsw.events.client_to_server.to_controller.EventFromView;

/**
 * Interface that represents the observer of the view.
 * It's a active part of Observer pattern.
 */
public interface ViewObserver {

    /**
     * Update the view observer with an event from view.
     * @param eventFromView is the update event.
     */
    void update(EventFromView eventFromView);
}
