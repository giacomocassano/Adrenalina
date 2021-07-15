package it.polimi.ingsw.events.visitors;

import it.polimi.ingsw.events.model_to_view.*;

/**
 * Interface of visitor pattern.
 */
public interface ViewVisitor {

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(BasicEffectRequest event);

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(BasicEffectUseRequest event);

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(SpawnRequest event);

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(EmpowerRequest event);

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(MoveRequest event);

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(RespawnRequest event);

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(ShootWeaponRequest event);

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(SquareMoveRequest event);

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(UltraEffectRequest event);

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(UltraEffectUseRequest event);

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(WeaponsToReloadRequest event);

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(WeaponToGrabRequest event);

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(TimeExceededNotification event);

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(MessageNotification event);

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(GameRepUpdate event);

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(LoginRequest event);

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(LoginSuccess event);

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(LoginError event);

    /**
     * Visitor pattern method.
     * @param request is the event to visit.
     */
    void visit(EmpowerUseRequest request);

    /**
     * Visitor pattern method.
     * @param update is the event to visit.
     */
    void visit(WaitingRoomUpdate update);

    /**
     * Visitor pattern method.
     * @param request is the event to visit.
     */
    void visit(ShooterMovementRequest request);

    /**
     * Visitor pattern method.
     * @param request is the event to visit.
     */
    void visit(GrabAmmoRequest request);

    /**
     * Visitor pattern method.
     * @param notification is the event to visit.
     */
    void visit(GameOverNotification notification);

    /**
     * Visitor pattern method.
     * @param notification is the event to visit.
     */
    void visit(DisconnectionNotification notification);

    /**
     * Visitor pattern method.
     * @param notification is the event to visit.
     */
    void visit(ReconnectionNotification notification);

    /**
     * Visitor pattern method.
     * @param event is the event to visit.
     */
    void visit(PingClient event);
}
