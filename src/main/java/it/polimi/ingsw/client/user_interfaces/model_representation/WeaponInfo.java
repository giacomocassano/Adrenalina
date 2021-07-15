package it.polimi.ingsw.client.user_interfaces.model_representation;

import it.polimi.ingsw.model.AmmoCubes;
import it.polimi.ingsw.model.Color;

import java.io.Serializable;
import java.util.List;

/**
 * WeaponInfo class is used during communication between Server and Clients.
 * This class is a small representation of a WeaponCard. Look in package Model for other infos.
 */
public class WeaponInfo implements Serializable {

    private int id;
    private String name;
    private Color color;
    private AmmoCubes buyCost;
    private AmmoCubes reloadCost;
    private boolean loaded;
    private List<EffectInfo> basicEffects;
    private List<EffectInfo> ultraEffects;

    /**
     * This is the base constructor.
     */

    public WeaponInfo() {
        //look javadoc for infos..
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public AmmoCubes getBuyCost() {
        return buyCost;
    }

    public void setBuyCost(AmmoCubes buyCost) {
        this.buyCost = buyCost;
    }

    public AmmoCubes getReloadCost() {
        return reloadCost;
    }

    public void setReloadCost(AmmoCubes reloadCost) {
        this.reloadCost = reloadCost;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public List<EffectInfo> getBasicEffects() {
        return basicEffects;
    }

    public void setBasicEffects(List<EffectInfo> basicEffects) {
        this.basicEffects = basicEffects;
    }

    public List<EffectInfo> getUltraEffects() {
        return ultraEffects;
    }

    public void setUltraEffects(List<EffectInfo> ultraEffects) {
        this.ultraEffects = ultraEffects;
    }

    public String getWeaponDescription() {
        String description = "";
        for(EffectInfo effect: this.getBasicEffects()) {
            description = description + effect.getName()+"\n\n"+effect.getDescription()+"\n\n\n";
        }

        for(EffectInfo effect: this.getUltraEffects()){
            description = description + effect.getName()+"\n\n"+effect.getDescription()+"\n\n\n";
        }
        return description;
    }
}
