package it.polimi.ingsw.events.client_to_server.to_controller;

import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.events.visitors.ControllerVisitor;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Position;

import java.io.Serializable;

/**
 * This response is sent from client to server after an EmpowerUseRequest
 * Client tells server how he wants to use an empower, which target to hit, which position to move in,
 * if empower has a cost, how he wants to pay it and other infos.
 */
public class EmpowerUseResponse extends EventFromView implements Serializable {

    private EmpowerInfo empower;
    private String target;
    private Position position;
    private Color payingColor;
    private EmpowerInfo payingEmp;
    private boolean isAborted;

    /**
     *
     * @param player is client name
     * @param empower is empower that player wants to use
     * @param target are selected by player if empowers is used against an enemy
     * @param position is selected by player if empowers allows to move
     * @param payingColor is the cost payed by player to use empower
     * @param payingEmp is the cost payed by a player expressed in empowers
     * @param isAborted is true if player want to abort the empower use action.
     */
    public EmpowerUseResponse(String player, EmpowerInfo empower, String target, Position position, Color payingColor, EmpowerInfo payingEmp, boolean isAborted) {
        super(player);
        this.empower = empower;
        this.target = target;
        this.position = position;
        this.payingColor = payingColor;
        this.payingEmp = payingEmp;
        this.isAborted = isAborted;
    }

    /**
     *
     * @return the empower.
     */
    public EmpowerInfo getEmpower() {
        return empower;
    }

    /**
     *
     * @param empower is empower that is used.
     */
    public void setEmpower(EmpowerInfo empower) {
        this.empower = empower;
    }

    /**
     *
     * @return target selected by player
     */
    public String getTarget() {
        return target;
    }

    /**
     *
     * @param target is the enemy selected by player if empowers is used against someone.
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     *
     * @return position if empower is used to move.
     */
    public Position getPosition() {
        return position;
    }

    /**
     *
     * @param position is where client
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     *
     * @return the paying color
     */
    public Color getPayingColor() {
        return payingColor;
    }

    /**
     *
     * @return paying empowers used by player
     */
    public EmpowerInfo getPayingEmp() {
        return payingEmp;
    }

    /**
     *
     * @return true if actions has been aborted.
     */
    public boolean isAbort() {
        return isAborted;
    }


    @Override
    public void acceptControllerVisitor(ControllerVisitor visitor) {
        visitor.visit(this);
    }
}
