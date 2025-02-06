package it.univaq.disim.lpo.risiko.core.model;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

public class Mappa implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Continente> continenti;

    public Mappa() {
        this.continenti = new ArrayList<>();
    }

    public Mappa(List<Continente> continenti) {
        this.continenti = continenti;
    }

    public List<Continente> getContinenti() {
        return this.continenti;
    }

    public void setContinenti(List<Continente> continenti) {
        this.continenti = continenti;
    }

    public Continente getContinente(String nomeContinente) {
        for (Continente continente : continenti) {
            if (continente.getNome().equalsIgnoreCase(nomeContinente)) {
                return continente;
            }
        }
        return null;
    }

    // Metodo per aggiungere un continente
    public void aggiungiContinente(Continente continente) {
        this.continenti.add(continente);
    }

    public List<Territorio> getTerritori() {
        List<Territorio> tuttiTerritori = new ArrayList<>();
        for (Continente continente : this.continenti) {
            tuttiTerritori.addAll(continente.getTerritori());
        }
        return tuttiTerritori;
    }

}
