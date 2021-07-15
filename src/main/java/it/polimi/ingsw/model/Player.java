package it.polimi.ingsw.model;

import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.client.user_interfaces.model_representation.MyPlayerRepresentation;
import it.polimi.ingsw.client.user_interfaces.model_representation.PlayerRepresentation;
import it.polimi.ingsw.client.user_interfaces.model_representation.WeaponInfo;
import it.polimi.ingsw.exceptions.EffectException;
import it.polimi.ingsw.exceptions.MoveException;
import it.polimi.ingsw.exceptions.ShootException;
import it.polimi.ingsw.model.cards.AmmunitionCard;
import it.polimi.ingsw.model.cards.EmpowerCard;
import it.polimi.ingsw.model.cards.Stack;
import it.polimi.ingsw.model.cards.WeaponCard;
import it.polimi.ingsw.model.cards.effects.ShootEffect;
import it.polimi.ingsw.model.cards.effects.ShooterMovement;
import it.polimi.ingsw.model.cards.effects.UltraDamage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.model.Color.*;

/**
 * Player class represents a Player character in the game.
 *A player has a name, a color, can be the first player or not,
 * during the game he gains points and he can die some times,
 * he has some weapons and some empowers and cubes.
 */


public class Player {

    private final String name;
    private final Color color;
    private boolean firstPlayer;
    private boolean isFirstFinalFrenzy;
    private boolean isConnected;
    private int points;
    private AmmoCubes ammoCubes;
    private List<WeaponCard> weapons;
    private List<EmpowerCard> payingEmpowers;
    private List<EmpowerCard> empowers;
    private List<Damage> damages;
    private List<Damage> marks;
    private int deaths;
    private List<Integer> availPoints;
    private List<Integer> frenzyPoints;
    private boolean justShot;
    private boolean isBeforeFirstPlayer;

    //Defines
    private static final  int MAX_WEAPON_CARDS = 3;
    private static final  int MAX_EMP_CARDS = 3;
    private static final  int DAMAGES_SIZE = 12;
    private static final  int DEATH_LIMIT = 10;
    private static final int MAX_MARK_FROM_PLAYER = 3;
    private static final int FIRST_DEATH_POINTS=8;
    private static final int SECOND_DEATH_POINTS=6;
    private static final int THIRD_DEATH_POINTS=4;
    private static final int FOURTH_DEATH_POINTS=2;
    private static final int LAST_DEATHS_POINTS=1;
    private static final int STARTING_POINTS=0;
    private static final int STARTING_DEATHS=0;
    private static final int STARTING_CUBES=1;

    /**
     * Constructor.
     * @param name is the name (nickname) of the Player
     * @param color is the color of his character
     * @param firstPlayer is true if is the first player. Game starts with him
     */

    public Player(String name, Color color, boolean firstPlayer) {
        this.name = name;
        this.color = color;
        this.firstPlayer = firstPlayer;
        this.points = STARTING_POINTS;
        this.ammoCubes = new AmmoCubes(STARTING_CUBES, STARTING_CUBES, STARTING_CUBES);
        frenzyPoints=new ArrayList<>();
        frenzyPoints.add(FOURTH_DEATH_POINTS);
        frenzyPoints.add(LAST_DEATHS_POINTS);
        frenzyPoints.add(LAST_DEATHS_POINTS);
        frenzyPoints.add(LAST_DEATHS_POINTS);
        availPoints=new ArrayList<>();
        availPoints.add(FIRST_DEATH_POINTS);
        availPoints.add(SECOND_DEATH_POINTS);
        availPoints.add(THIRD_DEATH_POINTS);
        availPoints.add(FOURTH_DEATH_POINTS);
        availPoints.add(LAST_DEATHS_POINTS);
        availPoints.add(LAST_DEATHS_POINTS);
        weapons = new ArrayList<>();
        empowers = new ArrayList<>();
        payingEmpowers = new ArrayList<>();
        damages = new ArrayList<>();
        marks = new ArrayList<>();
        deaths = STARTING_DEATHS;
        justShot = false;
        isConnected = true;
        isFirstFinalFrenzy = false;
    }

    /**
     *
     * @param position is Player's position.
     * @param stack is empower's stack,  is used if Player grabs and ammoCard with an Empower in it. so he has to pick an empower from the deck
     * @param ammoStack is ammo cards stack. If the card is successfully grabbed is replaced in discarded stack.
     * @return true if player can grab an Ammocard.
     */
    public boolean grabAmmos(Square position, Stack<EmpowerCard> stack, Stack<AmmunitionCard> ammoStack) {
        if(position == null || stack == null || ammoStack == null || position.isRespawnPoint() || position.getAmmos() == null) return false;
        AmmunitionCard ammunitionCard = position.pickAmmos(); //pick ammos from position
        ammoStack.discard(ammunitionCard);
        this.ammoCubes.addAmmoCubes(ammunitionCard.getAmmoCubes());
        if(this.empowers.size() < MAX_EMP_CARDS && ammunitionCard.hasEmpower())
            this.empowers.add((EmpowerCard) stack.pickCard());
        return true;
    }

    /**
     * This method checks if a player can grab a Weapon. Used in grabWeapon(){}
     * @param position is Player's position
     * @param gained is the weapon that Player want to get.
     * @param dropped is the weapon that player want to drop
     * @return true if player can grab the weapon.
     */
    public boolean canGrabWeapon(Square position, WeaponCard gained, WeaponCard dropped) {
        if(position == null || gained == null || !position.isRespawnPoint() || !position.containsWeapon(gained))
            return false;
        if(this.weapons.size() >= MAX_WEAPON_CARDS){
            if(!weapons.contains(dropped))
                return false;
        }
        if(ammoCubes.isLowerThan(gained.getBuyCost())) return false;
        return true;
    }

    /**
     * @param position is Player's position.
     * @param gained is the weapon that it has to be taken.
     * @param dropped is the weapon that it has to be dropped if player already has 3 weapons.
     * @return true if player grabbed the weapon.
     * @throws MoveException if player can't grab that weapon. Look canGrabWeapon(){}
     */
    public boolean grabWeapon(Square position, WeaponCard gained, WeaponCard dropped) throws MoveException {
        if(!canGrabWeapon(position, gained, dropped)) throw new MoveException("Il giocatore non può raccogliere questa arma");
        if(this.weapons.size() >= MAX_WEAPON_CARDS){
            if(!weapons.contains(dropped))
                return false;
            weapons.remove(dropped);
            position.addWeapon(dropped);
        }
        position.removeWeapon(gained);
        this.weapons.add(gained);
        return true;
    }

    /**
     * Checks if Player can shoot with a weapon.
     * @param map is playing map.
     * @param weapon is the weapon that Player wants to use.
     * @return true if player can shoot with that weapon.
     */
    public boolean isValidShoot(Map map, WeaponCard weapon) {
        Square initialSquare = map.getPlayerPosition(this);
        if(!weapon.isLoaded() || !weapons.contains(weapon)) return false;
        //Get the active effect
        ShootEffect effect = null;
        for(ShootEffect e: weapon.getBasicEffects()) {
            if(e.isActive())
                effect = e;
        }
        if(effect==null) return false;
        effect.setShooter(this);
        //Move player if shooter movement is before basic effect
        if(!effect.isValidEffect(map)) return false;
        //Control ultra damages
        UltraDamage ultra = effect.getUltraDamage();
        List<Player> targets = effect.getTargets();
        while(ultra != null && ultra.isActive()) {
            ultra.setShooter(this);
            ultra.setTargets(targets);
            if(!ultra.isValidEffect(map)) return false;
            targets = ultra.getNewTargets();
            ultra = ultra.getUltraDamage();
        }
        //Control recoil effect
        if(effect.getRecoil() != null && !effect.getRecoil().isValidEffect(map)) return false;
        //Control payment
        AmmoCubes cost = weapon.getActiveEffectsCost();
        if(!this.controlPayment(cost)) return false;
        return true;
    }

    /**
     * Is used to perform a Shoot Move.
     * @param map is the playing map
     * @param weapon is the weapon that player want to use.
     * @throws ShootException if weapon is not loaded or player doesn't have weapons
     * @throws EffectException if effect can't be performed, like some value is null or other errors.
     */
    public void shoot(Map map, WeaponCard weapon) throws ShootException, EffectException {
        //Control weapon is loaded and the player has the weapon
        if(!weapon.isLoaded()) throw new ShootException("L'arma non è carica!");
        if(!weapons.contains(weapon)) throw new ShootException("Il giocatore non possiede l'arma!");
        //Get the active effect
        ShootEffect effect = null;
        for(ShootEffect e: weapon.getBasicEffects()) {
            if(e.isActive())
                effect = e;
        }
        if(effect == null) throw new ShootException("Nessun effetto è attivo!");
        //Perform the effect
        effect.setShooter(this);
        effect.performEffect(map);
        //Perform ultra damages
        UltraDamage ultra = effect.getUltraDamage();
        List<Player> targets = effect.getTargets();
        while(ultra != null && ultra.isActive()) {
            ultra.setShooter(this);
            ultra.setTargets(targets);
            ultra.performEffect(map);
            targets = ultra.getNewTargets();
            ultra = ultra.getUltraDamage();
        }
        //Control shooter movement
        ShooterMovement shooterMovement = weapon.getShooterMovement();
        if(shooterMovement != null && shooterMovement.isActive() && shooterMovement.getOrder() == 1 )
            map.movePlayer(this, shooterMovement.getPosition());
        //Reset weapon
        weapon.reset();
        //Unload the weapon after shoot
        weapon.unload();
    }

    /**
     * Checks if player can shot
     * @param map is playing map
     * @param weapon is weapon that player wants to use
     * @return true if player can shoot with that weapon.
     */
    public boolean canShoot(Map map, WeaponCard weapon) {
        //Control weapon is loaded and the player has the weapon
        if(!weapon.isLoaded() || !weapons.contains(weapon)) return false;
        //Get the active effect
        ShootEffect effect = null;
        for(ShootEffect e: weapon.getBasicEffects()) {
            if(e.isActive())
                effect = e;
        }
        if(effect == null) return false;
        //Control is shooter movement is active and before basic effect
        ShooterMovement movement = weapon.getShooterMovement();
        if(movement != null && movement.isActive() && movement.getOrder() == 0) {
            movement.setPlayer(this);
            if(!movement.isValidEffect(map)) return false;
        }
        //Control the effect
        effect.setShooter(this);
        if(!effect.isValidEffect(map)) return false;
        //Control if shooter movement is active and after basic effect
        if(movement != null && movement.isActive() && movement.getOrder() == 1) {
            movement.setPlayer(this);
            if(!movement.isValidEffect(map)) return false;
        }
        //Control ultra damages
        UltraDamage ultra = effect.getUltraDamage();
        List<Player> targets = effect.getTargets();
        while(ultra != null && ultra.isActive()) {
            ultra.setShooter(this);
            ultra.setTargets(targets);
            if(!ultra.isValidEffect(map)) return false;
            targets = ultra.getNewTargets();
            ultra = ultra.getUltraDamage();
        }
        return true;
    }

    /**
     * Checks if player can pay with his cubes
     * @param cost is the cost that player has to afford
     * @return true if player can pay
     */
    public boolean controlPayment(AmmoCubes cost) {
        AmmoCubes ammoCubes = new AmmoCubes(this.ammoCubes.getRed(), this.ammoCubes.getBlue(), this.ammoCubes.getYellow());
        for(EmpowerCard emp: this.getPayingEmpowers())
            if(emp.getColor() == RED)
                ammoCubes.addAmmoCubesRed(1);
            else if(emp.getColor() == BLUE)
                ammoCubes.addAmmoCubesBlue(1);
            else if(emp.getColor() == YELLOW)
                ammoCubes.addAmmoCubesYellow(1);
        if(ammoCubes.isLowerThan(cost)) return false;
        return true;
    }

    /**
     * payCost is the cost that player has to pay
     * @param cost is cost expressed in cubes
     * @param empowersStack are empowers that player wants to use to pay
     */
    public void payCost(AmmoCubes cost, Stack<EmpowerCard> empowersStack) {
        List<EmpowerCard> usedPayingEmps = new ArrayList<>();
        AmmoCubes tmpCost = new AmmoCubes(cost.getRed(), cost.getBlue(), cost.getYellow());
        //Decrease the cost with the paying empower cards
        for(EmpowerCard payingEmp: this.payingEmpowers) {
            if(payingEmp.getColor() == RED && tmpCost.getRed() > 0) {
                usedPayingEmps.add(payingEmp);
                tmpCost.decreaseAmmoCubesRed(1);
            }else if(payingEmp.getColor() == BLUE && tmpCost.getBlue() > 0) {
                usedPayingEmps.add(payingEmp);
                tmpCost.decreaseAmmoCubesBlue(1);
            }else if(payingEmp.getColor() == YELLOW && tmpCost.getYellow() > 0) {
                usedPayingEmps.add(payingEmp);
                tmpCost.decreaseAmmoCubesYellow(1);
            }
        }
        //Remove the empowers
        this.empowers.removeAll(usedPayingEmps);
        empowersStack.discardAll(usedPayingEmps);
        this.payingEmpowers.clear();
        //Now use the ammoCards cubes
        this.ammoCubes.decreaseAmmoCubes(tmpCost);

    }

    /**
     * Method to reload player's weapons.
     * @param weaponsToReload are weapons that player wants to reload
     * @return true if weapons are successfully reloaded
     */
    public boolean reloadWeapons(List<WeaponCard> weaponsToReload) {
        if(weaponsToReload == null || !this.weapons.containsAll(weaponsToReload))
            return false;
        for(WeaponCard weapon: weaponsToReload)
            weapon.reload();
        return true;
    }

    /**
     * This method id used when player is shot. Damages are added, if there are marks are removed..
     * @param map is playing map
     * @param shooter is the player that is shooting
     * @param damages are how much damages player has to take
     * @param marks are how much marks player has to take.
     */
    public void sufferDamage(Map map, Player shooter, int damages, int marks) {
        if(damages > 0) {
            List<Damage> marksToDamage = this.marks.stream().filter(d -> d.getShooter() == shooter).collect(Collectors.toList());
            this.marks.removeAll(marksToDamage);
            addDamages(marksToDamage);
            this.justShot = true;
        }
        for(int i=0; i<damages; i++)
            addDamageBy(shooter);
        for(int i=0; i<marks; i++)
            addMarkBy(shooter);
        if(getNumDamages() == DAMAGES_SIZE) {
            Player marked = this.damages.get(DAMAGES_SIZE-1).getShooter();
            marked.sufferDamage(map, this, 0, 1);
        }
        if(isDead())
            map.removeDeadPlayer(this);
    }

    /**
     * Add a damage from a Player to another one
     * @param shooter is the player that is giving damage.
     */
    private void addDamageBy(Player shooter) {
        if(damages.size() < DAMAGES_SIZE) {
            damages.add(new Damage(shooter));
        }
    }
    /**
     * Add some damages to a Player
     * @param damagesToAdd is a List of damages
     */
    private void addDamages(List<Damage> damagesToAdd) {
        for(Damage d: damagesToAdd) {
            if(damages.size() < DAMAGES_SIZE){
                damages.add(d);
            }
        }
    }
    /**
     * Add a mark from a Player to another one
     * @param marker is the player that is giving damage.
     */
    private void addMarkBy(Player marker) {
        if(getNumMarks(marker) < MAX_MARK_FROM_PLAYER)
            marks.add(new Damage(marker));
    }

    /**
     * Adds an empower
     * @param empowerCard is the emp that has to be added.
     */
    public void addEmpower(EmpowerCard empowerCard) {
        this.empowers.add(empowerCard);
    }

    /**
     * Creates a new list of damages.
     */
    public void resetDamages() {
        this.damages = new ArrayList<>();
    }

    /**
     * Checks if player is dead
     * @return true if is dead, of course.
     */
    public boolean isDead() {
        return getNumDamages() > DEATH_LIMIT;
    }

    /**
     * @return player's num of damages
     */
    public int getNumDamages() {
        return damages.size();
    }

    /**
     * @return player's num of marks
     */
    public int getNumMarks() {
        return marks.size();
    }

    /**
     * @param shooter is the player that is want to know how much damage dealt
     * @return player's num of damages made by another Player.
     */
    public int getNumDamages(Player shooter) {
        int cont = 0;
        for(Damage damage: damages)
            if(damage.getShooter() == shooter)
                cont++;
        return cont;
    }

    /**
     * @param shooter is the player that is want to know how much marks he inflicted
     * @return player's num of marks made by a Player.
     */
    public int getNumMarks(Player shooter) {
        int cont = 0;
        for(Damage mark: marks)
            if(mark.getShooter() == shooter)
                cont++;
        return cont;
    }

    /**
     * @return a list of every player that has shot to a Player.
     */
    public List<Player> getShooters() {
        List<Player> shooters = new ArrayList<>();
        for(Damage d: damages)
            if(!shooters.contains(d.getShooter()))
                shooters.add(d.getShooter());
        return shooters;
    }

    /**
     * Compare which player has shot first.
     * @param p1 is the first player
     * @param p2 is the second player
     * @return the player that has shoot first
     */
    public Player getFirstShooter(Player p1, Player p2) {
        for(Damage d: damages)
            if(d.getShooter() == p1 || d.getShooter() == p2)
                return d.getShooter();
        return null;
    }

    /**
     * @return player's loaded weapons.
     */
    public List<WeaponCard> getLoadedWeapons() {
        return this.weapons.stream().filter(WeaponCard::isLoaded).collect(Collectors.toList());
    }

    /**
     * Updates paying empowers list.
     * @param empowerCards is the list of empower cards to add
     */
    public void addPayingEmpowers(List<EmpowerCard> empowerCards) {
        payingEmpowers.addAll(empowerCards);
    }

    /**
     * Remove paying empowers
     * @param empowerCards is a list of empower cards
     */
    public void removePayingEmpowers(List<EmpowerCard> empowerCards) {
        payingEmpowers.removeAll(empowerCards);
    }

    /**
     * Remove every empower in empowerCards from a player.
     * @param empowerCards are empowers to remove
     */
    public void removeEmpowers(List<EmpowerCard> empowerCards) {
        empowers.removeAll(empowerCards);
    }

    /**
     *Checks if player can afford a cost.
     * @param cost is the cost that player have to pay
     * @return true if player can buy something.
     */
    public boolean canPay(AmmoCubes cost) {
        AmmoCubes cubes = new AmmoCubes(ammoCubes.getRed(), ammoCubes.getBlue(), ammoCubes.getYellow());
        for(EmpowerCard emp: this.empowers) {
            if(emp.getColor() == RED)
                cubes.addAmmoCubesRed(1);
            else if(emp.getColor() == BLUE)
                cubes.addAmmoCubesBlue(1);
            else if(emp.getColor() == YELLOW)
                cubes.addAmmoCubesYellow(1);
        }
        return !cubes.isLowerThan(cost);
    }
    /**
     * Paying emps Getter
     * @return player's paying empowers
     */
    public List<EmpowerCard> getPayingEmpowers() {
        return this.payingEmpowers;
    }

    public int getNumPayingEmpowers(Color color) {
        int cont = 0;
        for(EmpowerCard ec: payingEmpowers)
            if(ec.getColor() == color)
                cont++;
        return cont;
    }

    /**
     * Add points to a player
     * @param points are points that have to be added
     */
    public void addPoints(int points) {
        this.points += points;
    }

    /**
     * Add a weapon from player's weapons
     * @param weapon is the weapon to add
     * @return true if weapon is successfully added
     */
    public boolean addWeapon(WeaponCard weapon) {
        if(this.weapons.size() >= MAX_WEAPON_CARDS) return false;
        this.weapons.add(weapon);
        return true;
    }

    /**
     * Remove a weapon from player's weapons
     * @param weapon is the weapon to remove
     */
    public void removeWeapons(WeaponCard weapon){
        this.weapons.get(this.weapons.indexOf(weapon)).reload();
        this.weapons.remove(weapon);

    }

    /**
     * Remove an empower
     * @param empower is the empower to remove.
     */
    public void removeEmpower(EmpowerCard empower) {
        this.empowers.remove(empower);
    }

    /**
     * Empowers Setter
     * @param empowers are player's empowers
     */
    public void setEmpowers(List<EmpowerCard> empowers) {
        this.empowers = empowers;
    }
    /**
     * Name Getter
     * @return player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Color Getter
     * @return player's color
     */
    public Color getColor() {
        return color;
    }

    /**
     * FirstPlayer Setter
     * @param firstPlayer is true if player is the first one
     */
    public void setFirstPlayer(boolean firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    /**
     * @return true if player is the first one
     */
    public boolean isFirstPlayer() {
        return firstPlayer;
    }

    /**
     * connected Getter.
     * @return true if player is connected.
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Connected setter
     * @param connected is true if player is connected
     */
    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    /**
     * Marks Getter
     * @return player's marks
     */
    public List<Damage> getMarks() {
        return marks;
    }

    /**
     * @return player's number of points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Points Getter
     * @return player's points
     */
    public AmmoCubes getAmmoCubes() {
        return ammoCubes;
    }

    /**
     * Weapons Getter
     * @return player's weapons
     */
    public List<WeaponCard> getWeapons() {
        return weapons;
    }

    /**
     * Empowers Getter
     * @return player's emps.
     */
    public List<EmpowerCard> getEmpowers() {
        return empowers;
    }

    /**
     * @return player's number of deaths
     */
    public int getDeaths() {
        return deaths;
    }

    /**
     * Remove the first element of Aviable points
     * @return value
     */
    public int removeAvaiblePoints(){
        return this.availPoints.remove(0);
    }

    /**
     * Remove the first element of frenzy points
     * @return value
     */
    public int removeFrenzyPoints(){
        return this.frenzyPoints.remove(0);
    }


    /**
     * Remove every damage from a Player made by another one.
     * @param player is the another one.
     */
    public void removeDamageByPlayer(Player player){
        int size=this.damages.size();
        for (int i=0;i<size;i++){
            if(this.damages.get(i).getShooter().equals(player)){
                this.damages.remove(i);
                i--;
                size--;
            }
        }
    }

    /**
     * Damages Getter
     * @return damages taken by player
     */
    public List<Damage> getDamages() { return damages; }

    /**
     * add Empowers to list of paying one.
     * @param payingEmp are empowers to add
     */
    public void addPayingEmpower(EmpowerCard payingEmp) {
        this.payingEmpowers.add(payingEmp);
    }

    /**
     * @return FirstFinalFrenzy. true if player is first in FF mode.
     */
    public boolean isFirstFinalFrenzy() {
        return isFirstFinalFrenzy;
    }

    /**
     * first Final Frenzy setter
     * @param firstFinalFrenzy is true if player is first in FF mode.
     */
    public void setFirstFinalFrenzy(boolean firstFinalFrenzy) {
        isFirstFinalFrenzy = firstFinalFrenzy;
    }

    /**
     * Add a death to the player
     */
    public void addDeath(){ this.deaths+=1; }

    /**
     * @return first damage took by player.
     */
    public Damage firstDamage(){ return this.damages.get(0); }

    /**
     * Just Shot getter
     * @return true if player is just shot
     */
    public boolean isJustShot() {
        return justShot;
    }

    /**
     *
     * @param justShot is true if player has just been shot.
     */
    public void setJustShot(boolean justShot) {
        this.justShot = justShot;
    }

    /**
     * before first player Getter
     * @return true if Player is before the first one.
     */
    public boolean isBeforeFirstPlayer() {
        return isBeforeFirstPlayer;
    }

    /**
     * Before first player setter
     * @param beforeFirstPlayer is if player is before the first one. Used in final frenzy
     */
    public void setBeforeFirstPlayer(boolean beforeFirstPlayer) {
        isBeforeFirstPlayer = beforeFirstPlayer;
    }

    /**
     * Is used to get a "myplayerRepresentation" that is similar to "PlayerRepresentation" but with more informations.
     * @param map is used to get player position.
     * @return a "MyPlayerRepresentation" object.
     */
    public MyPlayerRepresentation getMyPlayerRepresentation(Map map) {
        MyPlayerRepresentation info = new MyPlayerRepresentation();
        info.setName(name);
        info.setColor(color);
        info.setFirstPlayer(firstPlayer);
        info.setActive(isConnected);
        Square pos = map.getPlayerPosition(this);
        if(pos != null)
            info.setPosition(pos.getPosition());
        info.setAmmoCubes(ammoCubes);
        List<WeaponInfo> wInfos = this.getWeapons().stream().map(WeaponCard::getWeaponInfo).collect(Collectors.toList());
        info.setWeapons(wInfos);
        List<EmpowerInfo> eInfos = this.getEmpowers().stream().map(EmpowerCard::getEmpowerInfo).collect(Collectors.toList());
        info.setEmpowers(eInfos);
        List<Color> damColors = this.getDamages().stream().map(d -> d.getShooter().getColor()).collect(Collectors.toList());
        info.setDamages(damColors);
        List<Color> marColors = this.getMarks().stream().map(d -> d.getShooter().getColor()).collect(Collectors.toList());
        info.setMarks(marColors);
        info.setPoints(points);
        info.setDeaths(deaths);
        if(!availPoints.isEmpty())
            info.setDeathPoints(this.availPoints.get(0));
        return info;
    }

    /**
     * Creates a Player Representation
     * @param map is used to take player positions.
     * @return the representation of the Player, only basic information used in communication.
     */
    public PlayerRepresentation getPlayerRepresentation(Map map) {
        PlayerRepresentation info = new PlayerRepresentation();
        info.setName(name);
        info.setColor(color);
        info.setFirstPlayer(firstPlayer);
        info.setActive(isConnected);
        Square pos = map.getPlayerPosition(this);
        if(pos != null)
            info.setPosition(pos.getPosition());
        info.setAmmoCubes(ammoCubes);
        List<WeaponInfo> wInfos = this.getWeapons().stream().filter(w -> !w.isLoaded()).map(WeaponCard::getWeaponInfo).collect(Collectors.toList());
        info.setUnloadedWeapons(wInfos);
        info.setNumWeapons(weapons.size());
        info.setNumEmpowers(this.empowers.size());
        List<Color> damColors = this.getDamages().stream().map(d -> d.getShooter().getColor()).collect(Collectors.toList());
        info.setDamages(damColors);
        List<Color> marColors = this.getMarks().stream().map(d -> d.getShooter().getColor()).collect(Collectors.toList());
        info.setMarks(marColors);
        info.setPoints(points);
        info.setDeaths(deaths);
        if(!availPoints.isEmpty())
            info.setDeathPoints(this.availPoints.get(0));
        return info;
    }

    /**
     *
     * @return player's final frenzy available points
     */
    public List<Integer> getFrenzyPoints() {
        return frenzyPoints;
    }

    /**
     * Sets player's ammocubes.
     * @param ammoCubes are player's cubes.
     */
    public void setAmmoCubes(AmmoCubes ammoCubes) {
        this.ammoCubes = ammoCubes;
    }

    /**
     * Adds to a player some deaths
     * @param deaths are player's deaths
     */
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    /**
     * Adds to a player some weapons
     * @param weapons are player's weapons.
     */
    public void addWeapons(List<WeaponCard> weapons){
        if(weapons.size()<=3)
            this.weapons=weapons;
    }

    /**
     * Adds to a player some empowers
     * @param empowers are player's empowers.
     */
    public void addEmpowers(List<EmpowerCard> empowers) {
        if (empowers.size() <= 3) {
            this.empowers = empowers;
            this.payingEmpowers=empowers;
        }
    }
}