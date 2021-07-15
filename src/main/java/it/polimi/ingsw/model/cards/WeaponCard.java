package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.client.user_interfaces.model_representation.EffectInfo;
import it.polimi.ingsw.client.user_interfaces.model_representation.WeaponInfo;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.effects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This Class is used to represent a weapon card in the game.
 * A weapon has a NAME, an ID, has a buying cost and a reloading one.
 * A weapon can be loaded or unloaded, has some effects and can have
 * a movement effect
 */

public class WeaponCard extends Card {

    private Color color;
    private AmmoCubes buyCost;
    private AmmoCubes reloadCost;
    private boolean loaded;
    private transient List<ShootEffect> basicEffects;
    private transient ShooterMovement shooterMovement;

    /**
     * This is the constructor
     * @param name is the name of the weapon
     * @param id is the ID of the weapon
     * @param color is the color of the weapon
     * @param buyCost is the cost that a player has to afford buying this weapon
     * @param reloadCost is the cost that a player has to afford reloading this weapon
     * @param basicEffects are basic effects of the weapon
     * @param shooterMovement is the movement effect related to the weapon
     */
    public WeaponCard(String name,int id,Color color, AmmoCubes buyCost, AmmoCubes reloadCost, List<ShootEffect> basicEffects, ShooterMovement shooterMovement) {
        super(name,id);
        this.color=color;
        this.buyCost=buyCost;
        this.reloadCost=reloadCost;
        this.basicEffects=basicEffects;
        this.shooterMovement = shooterMovement;
        this.loaded=true;
    }

    /**
     *
     * @return true if weapon is loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * reloads the weapon
     */
    public void reload() {
        this.loaded = true;
    }

    /**
     * unloads the weapon
     */
    public void unload() {
        this.loaded = false;
    }

    /**
     *
     * @return the cost of the active effects.
     * (cost is expressed in ammo cubes)
     */
    public AmmoCubes getActiveEffectsCost() {
        AmmoCubes totalCost = new AmmoCubes(0,0,0);
        for(ShootEffect basic: basicEffects){
            if(basic.isActive()) {
                //Add active basic effect cost
                totalCost.addAmmoCubes(basic.getCost());
                //Add shooter movement cost
                if(shooterMovement != null)
                    totalCost.addAmmoCubes(shooterMovement.getCost());
                //Add active ultra damages cost
                UltraDamage ultra = basic.getUltraDamage();
                while (ultra != null && ultra.isActive()) {
                    totalCost.addAmmoCubes(ultra.getCost());
                    ultra = ultra.getUltraDamage();
                }
            }
        }
        return totalCost;
    }

    /**
     * Resets every flag of the card and also
     * the effects.
     * This method is called when a weapon is dropped or something.
     */
    public void reset() {
        //Reset of active flags, shooter and targets or square.
        for(ShootEffect basic: getBasicEffects()) {
            //Reset basic effect
            basic.setActive(false);
            basic.setShooter(null);
            basic.setTargets(new ArrayList<Player>());
            basic.setSquares(new ArrayList<Square>());
            //Reset shooter movement
            if(shooterMovement != null) {
                shooterMovement.setActive(false);
            }
            //Reset recoil
            RecoilMovement recoil = basic.getRecoil();
            if(recoil != null) {
                recoil.setPosition(null);
                recoil.setPlayer(null);
                recoil.setActive(false);
            }
            //Reset ultra effects
            UltraDamage ultra = basic.getUltraDamage();
            while (ultra != null) {
                ultra.setActive(false);
                ultra.setShooter(null);
                ultra.setTargets(new ArrayList<Player>());
                ultra.setNewTargets(new ArrayList<Player>());
                ultra.setSquares(new ArrayList<Square>());
                ultra = ultra.getUltraDamage();
            }
        }
    }

    /**
     *
     * @return weapon's active effects.
     */
    public List<Effect> getActiveEffects() {
        List<Effect> effects = new ArrayList<>();
        for(ShootEffect basic: basicEffects){
            if(basic.isActive()) {
                //Add active basic effect
                effects.add(basic);
                //Add shooter movement
                if(shooterMovement != null)
                    effects.add(shooterMovement);
                //Add active ultra damages cost
                UltraDamage ultra = basic.getUltraDamage();
                while (ultra != null && ultra.isActive()) {
                    effects.add(ultra);
                    ultra = ultra.getUltraDamage();
                }
            }
        }
        return effects;
    }

    /**
     *
     * @return weapon's color
     */
    public Color getColor() {
        return color;
    }

    /**
     *
     * @return weapon's cost of buying
     */
    public AmmoCubes getBuyCost() {
        return buyCost;
    }

    /**
     *
     * @return weapon's cost of reloading
     */
    public AmmoCubes getReloadCost() {
        return reloadCost;
    }

    /**
     *
     * @return weapon's basic effects
     */
    public List<ShootEffect> getBasicEffects() {
        return basicEffects;
    }

    /**
     *
     * @return weapon's shooter movement effects
     */
    public ShooterMovement getShooterMovement() {
        return shooterMovement;
    }

    /**
     * Creates a weaponInfo object starting from WeaponCard.
     * More at @WeaponInfo
     * @return the weaponInfo correspondent from WeaponCard
     */
    public WeaponInfo getWeaponInfo() {
        WeaponInfo info = new WeaponInfo();
        info.setId(this.getId());
        info.setName(this.getName());
        info.setColor(this.getColor());
        info.setBuyCost(this.getBuyCost());
        info.setReloadCost(this.getReloadCost());
        info.setLoaded(this.isLoaded());
        List<EffectInfo> basics = this.getBasicEffects().stream().map(Effect::getEffectInfo).collect(Collectors.toList());
        info.setBasicEffects(basics);
        List<EffectInfo> ultras = new ArrayList<>();
        for(ShootEffect basic: basicEffects) {
            UltraDamage ultra = basic.getUltraDamage();
            while(ultra != null) {
                ultras.add(ultra.getEffectInfo());
                ultra = ultra.getUltraDamage();
            }
        }
        info.setUltraEffects(ultras);
        return info;
    }
 }