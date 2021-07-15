package it.polimi.ingsw.model;

/**
 * This enum is  used to describe a Move-Type.
 * There are 5 types of Move: run,grab,shoot,reload,end action.
 */
public enum MoveType {

    RUN("CORRI: spostati di una casella"),
    GRAB("RACCOGLI: raccogli una carta munizione o una carta arma"),
    SHOOT("SPARA: seleziona un'arma con cui sparare"),
    RELOAD("RICARICA: seleziona quali armi vuoi riacaricare"),
    END_ACTION("TERMINA: concludi l'azione");

    private String description;

    MoveType(String description) {
        this.description = description;
    }

    /**
     * @return Move's description.
     */
    public String getDescription() {
        return description;
    }

}