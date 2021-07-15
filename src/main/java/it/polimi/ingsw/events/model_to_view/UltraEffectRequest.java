package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.client.user_interfaces.model_representation.EffectInfo;
import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.events.visitors.ViewVisitor;

import java.io.Serializable;
import java.util.List;


/**
 * This message is the request sent by the server to a client after he used a basic effect with a
 * weapon that allows the possibility to use an ultra effect."
 * Client can choose to use it or not.
 */
public class UltraEffectRequest extends EventFromModel implements Serializable {

    private EffectInfo ultra;
    private List<EmpowerInfo> payingEmpowers;

    /**
     *
     * @param player is player's name
     * @param ultra is the ultra effect that player can use
     * @param payingEmpowers are player's empowers to pay the ultra effect
     */
    public UltraEffectRequest(String player, EffectInfo ultra, List<EmpowerInfo> payingEmpowers) {
        super(player);
        this.ultra = ultra;
        this.payingEmpowers = payingEmpowers;
    }

    public EffectInfo getUltra() {
        return ultra;
    }

    public List<EmpowerInfo> getPayingEmpowers() {
        return payingEmpowers;
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
