package it.polimi.ingsw.events.visitors;

import it.polimi.ingsw.events.client_to_server.to_controller.*;

/**
 * Interface of visitor patter of Controller events
 */
public interface ControllerVisitor {
    /**
     * Visitor pattern method.
     * @param event to visit.
     */
    void visit(EmpowerResponse event);

    /**
     * Visitor pattern method.
     * @param event to visit.
     */
    void visit(MoveResponse event);

    /**
     * Visitor pattern method.
     * @param event to visit.
     */
    void visit(WeaponToShootResponse event);

    /**
     * Visitor pattern method.
     * @param event to visit.
     */
    void visit(FirstSpawnResponse event);

    /**
     * Visitor pattern method.
     * @param event to visit.
     */
    void visit(MoveExceeded event);

    /**
     * Visitor pattern method.
     * @param event to visit.
     */
    void visit(WeaponToGrabResponse event);

    /**
     * Visitor pattern method.
     * @param event to visit.
     */
    void visit(BasicEffectResponse event);

    /**
     * Visitor pattern method.
     * @param event to visit.
     */
    void visit(BasicEffectUseResponse event);

    /**
     * Visitor pattern method.
     * @param event to visit.
     */
    void visit(EmpowerUseExceeded event);

    /**
     * Visitor pattern method.
     * @param event to visit.
     */
    void visit(SquareToMoveResponse event);

    /**
     * Visitor pattern method.
     * @param event to visit.
     */
    void visit(WeaponsToReloadResponse event);

    /**
     * Visitor pattern method.
     * @param event to visit.
     */
    void visit(UltraEffectUseResponse event);

    /**
     * Visitor pattern method.
     * @param event to visit.
     */
    void visit(UltraEffectResponse event);

    /**
     * Visitor pattern method.
     * @param event to visit.
     */
    void visit(DeathTimeExceeded event);

    /**
     * Visitor pattern method.
     * @param event to visit.
     */
    void visit(RespawnResponse event);

    /**
     * Visitor pattern method.
     * @param response to visit.
     */
    void visit(EmpowerUseResponse response);

    /**
     * Visitor pattern method.
     * @param response to visit.
     */
    void visit(ShooterMovementResponse response);

    /**
     * Visitor pattern method.
     * @param response to visit.
     */
    void visit(GrabAmmoResponse response);
}
