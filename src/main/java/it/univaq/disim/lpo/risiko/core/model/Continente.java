package it.univaq.disim.lpo.risiko.core.model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Continente implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nome;
    private List<Territorio> territori;
    private List<Territorio> confini;

    public Continente(String nome, List<Territorio> territori, List<Territorio> confini) {
        this.nome = nome;
        this.territori = territori;
        this.confini = confini;
    }

    public Continente(String nome, List<Territorio> territori) {
        this(nome, territori, new ArrayList<>());
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Territorio> getTerritori() {
        return this.territori;
    }

    public void setTerritori(List<Territorio> territori) {
        this.territori = territori;
    }

    public List<Territorio> getConfini() {
        return this.confini;
    }

    public void setConfini(List<Territorio> confini) {
        this.confini = confini;
    }

}
