package it.univaq.disim.lpo.risiko.core.model;

import java.io.Serializable;

public class CartaTerritorio implements Serializable {

    private static final long serialVersionUID = 1L;
    private Territorio territorio;
    private String figura;

    public CartaTerritorio(Territorio territorio, String figura) {
        this.territorio = territorio;
        this.figura = figura;
    }

    public Territorio getTerritorio() {
        return territorio;
    }

    public void setTerritorio(Territorio territorio) {
        this.territorio = territorio;
    }

    public String getFigura() {
        return figura;
    }

    public void setFigura(String figura) {
        this.figura = figura;
    }

}
