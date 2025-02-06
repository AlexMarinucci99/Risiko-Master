package it.univaq.disim.lpo.risiko.core.model;

import java.io.Serializable;

public class CartaObiettivo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String descrizione;

    public CartaObiettivo(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

}
