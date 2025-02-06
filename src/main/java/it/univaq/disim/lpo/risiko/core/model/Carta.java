package it.univaq.disim.lpo.risiko.core.model;

import java.io.Serializable;

public class Carta implements Serializable {
    private static final long serialVersionUID = 1L;
    private TipoCarta tipo;
    private Territorio territorio;

    public enum TipoCarta {
        CANNONE,
        FANTE,
        CAVALIERE,
        JOLLY
    }

    public Carta(TipoCarta tipo, Territorio territorio) {
        this.tipo = tipo;
        this.territorio = territorio;
    }

    public TipoCarta getTipo() {
        return tipo;
    }

    public Territorio getTerritorio() {
        return territorio;
    }

}
