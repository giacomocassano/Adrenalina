package it.polimi.ingsw.client.user_interfaces.model_representation;

import it.polimi.ingsw.model.AmmoCubes;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Position;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * This class represent a small representation of enemies characters in-Game.
 * This class contains only few infos because every character has not to know about his enemies.
 * Implements Serializable because is sent through sockets.
 */

public class PlayerRepresentation implements Serializable {

    private String name;
    private Color color;
    private boolean isFirstPlayer;
    private boolean isActive;
    private Position position;
    private AmmoCubes ammoCubes;
    private List<WeaponInfo> unloadedWeapons;
    private int numWeapons;
    private int numEmpowers;
    private List<Color> damages;
    private List<Color> marks;
    private int points;
    private int deaths;
    private int deathPoints;

    public PlayerRepresentation() {
        isFirstPlayer = false;
        position = null;
        this.unloadedWeapons = new ArrayList<>();
        damages = new ArrayList<>();
        marks = new ArrayList<>();
        numEmpowers = numWeapons = 0;
        points = deaths = 0;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public AmmoCubes getAmmoCubes() {
        return ammoCubes;
    }

    public void setAmmoCubes(AmmoCubes ammoCubes) {
        this.ammoCubes = ammoCubes;
    }

    public List<WeaponInfo> getUnloadedWeapons() {
        return unloadedWeapons;
    }

    public void setUnloadedWeapons(List<WeaponInfo> unloadedWeapons) {
        this.unloadedWeapons = unloadedWeapons;
    }

    public int getNumWeapons() {
        return numWeapons;
    }

    public void setNumWeapons(int numWeapons) {
        this.numWeapons = numWeapons;
    }

    public int getNumEmpowers() {
        return numEmpowers;
    }

    public void setNumEmpowers(int numEmpowers) {
        this.numEmpowers = numEmpowers;
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
