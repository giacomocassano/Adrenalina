package it.polimi.ingsw.events.client_to_server.to_controller;

import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.events.visitors.ControllerVisitor;
import it.polimi.ingsw.model.Position;

import java.io.Serializable;
import java.util.List;

/**
 * This is a shooterMovement response, sent from client to server after a
 * ShooterMovementRequest. Client tells server if he wants to use the shooter movement
 * effect and a position where he wants to move and if he wants to use it
 * before or after the basic effect
 */
public class ShooterMovementResponse extends EventFromView implements Serializable {

    private boolean wantUse;
    private Position position;
    private boolean beforeBasicEffect; //false if after
    private List<EmpowerInfo> payingEmpower;


    /**
     * This event is sent when a player use a weapon that allows him to move.
     * @param player is client's player
     * @param wantUse is true if player wants to use the effect
     * @param position is where he wants to move
     * @param beforeBasicEffect is true if he wants to move before base effect
     * @param payingEmpower are empowers that he want to use to pay the effect
     */
    public ShooterMovementResponse(String player, boolean wantUse, Position position, boolean beforeBasicEffect, List<EmpowerInfo> payingEmpower) {
        super(player);
        this.wantUse = wantUse;
        this.position = position;
        this.beforeBasicEffect = beforeBasicEffect;
        this.payingEmpower = payingEmpower;
    }

    /**
     *
     * @return true if player want to use the movement-effect. Is like an abort.
     */
    public boolean isWantUse() {
        return wantUse;
    }

    /**
     *
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     *
     * @return true if player wants to move before basic effect, else false
     */
    public boolean isBeforeBasicEffect() {
        return beforeBasicEffect;
    }

    /**
     *
     * @return paying empowers.
     */
    public List<EmpowerInfo> getPayingEmpower() {
        return payingEmpower;
    }

    @Override
    public void acceptControllerVisitor(ControllerVisitor visitor) {
        visitor.visit(this);
    }
}
