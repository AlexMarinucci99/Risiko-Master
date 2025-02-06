package it.univaq.disim.lpo.risiko.core.model;

import java.io.Serializable;

public class TurnoGioco implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum TurnPhase {
        START_TURN,
        DISTRIBUTE_ARMIES,
        MENU,
        END_TURN
    }

    private TurnPhase currentPhase = TurnPhase.START_TURN;
    private int armateDaDistribuire = 0;
    private boolean turnoTerminato = false;
    private int armateTotali; // Totale delle armate ricevute ad inizio turno
    private int numeroTerritori; // Numero di territori controllati all'inizio del turno
    private int numeroContinenti; // Numero di continenti controllati all'inizio del turno

    public TurnPhase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(TurnPhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public int getArmateDaDistribuire() {
        return armateDaDistribuire;
    }

    public void setArmateDaDistribuire(int armateDaDistribuire) {
        this.armateDaDistribuire = armateDaDistribuire;
    }

    public boolean isTurnoTerminato() {
        return turnoTerminato;
    }

    public void setTurnoTerminato(boolean turnoTerminato) {
        this.turnoTerminato = turnoTerminato;
    }

    public int getArmateTotali() {
        return armateTotali;
    }

    public void setArmateTotali(int armateTotali) {
        this.armateTotali = armateTotali;
    }

    public int getNumeroTerritori() {
        return numeroTerritori;
    }

    public void setNumeroTerritori(int numeroTerritori) {
        this.numeroTerritori = numeroTerritori;
    }

    public int getNumeroContinenti() {
        return numeroContinenti;
    }

    public void setNumeroContinenti(int numeroContinenti) {
        this.numeroContinenti = numeroContinenti;
    }

}
