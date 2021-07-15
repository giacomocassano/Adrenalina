package it.polimi.ingsw.client.user_interfaces.model_representation;

import it.polimi.ingsw.model.AmmoCubes;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Position;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a small representation of Client's character-in Game.
 * This class contains more infos then PlayerRepresentation Class.
 * Implements Serializable because is sent through sockets.
 */
public class MyPlayerRepresentation implements Serializable {

    private String name;
    private Color color;
    private boolean isFirstPlayer;
    private boolean isActive;
    private Position position; //if it is null, the player is dead
    private AmmoCubes ammoCubes;
    private List<WeaponInfo> weapons;
    private List<EmpowerInfo> empowers;
    private List<Color> damages;
    private List<Color> marks;
    private int points;
    private int deaths;
    private int deathPoints;

    public MyPlayerRepresentation() {
        isFirstPlayer = false;
        position = null;
        this.weapons = new ArrayList<>();
        this.empowers = new ArrayList<>();
        damages = new ArrayList<>();
        marks = new ArrayList<>();
        points = deaths = deathPoints = 0;
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

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        isFirstPlayer = firstPlayer;
    }

    public AmmoCubes getAmmoCubes() {
        return ammoCubes;
    }

    public void setAmmoCubes(AmmoCubes ammoCubes) {
        this.ammoCubes = ammoCubes;
    }

    public boolean isActive() {
        return isActive;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<WeaponInfo> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<WeaponInfo> weapons) {
        this.weapons = weapons;
    }

    public List<EmpowerInfo> getEmpowers() {
        return empowers;
    }

    public void setEmpowers(List<EmpowerInfo> empowers) {
        this.empowers = empowers;
    }

    public List<Color> getDamages() {
        return damages;
    }

    public void setDamages(List<Color> damages) {
        this.damages = damages;
    }

    public List<Color> getMarks() {
        return marks;
    }

    public void setMarks(List<Color> marks) {
        this.marks = marks;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getDeathPoints() {
        return deathPoints;
    }

    public void setDeathPoints(int deathPoints) {
        this.deathPoints = deathPoints;
    }
}
