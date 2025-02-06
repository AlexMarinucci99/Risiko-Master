package it.univaq.disim.lpo.risiko.core.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MazzoDiCarte implements Serializable {
    private static final long serialVersionUID = 1L;
    private LinkedList<Carta> carte;

    public MazzoDiCarte(List<Territorio> territori) {
        carte = new LinkedList<>();
        // Creare le carte per ogni territorio
        for (Territorio territorio : territori) {
            // Assegna un tipo casuale tra CANNONE , FANTE , CAVAGLIERE
            Carta.TipoCarta tipo = Carta.TipoCarta.values()[new Random().nextInt(3)];
            carte.add(new Carta(tipo, territorio));
        }
        // Aggiungi le carte Jolly
        carte.add(new Carta(Carta.TipoCarta.JOLLY, null));
        carte.add(new Carta(Carta.TipoCarta.JOLLY, null));

        Collections.shuffle(carte);
    }

    public Carta pescaCarta() {
        if (carte.isEmpty()) {
            return null;
        }
        return carte.removeFirst();
    }

    public void restituisciCarte(List<Carta> carteDaRestituire) {
        carte.addAll(carteDaRestituire);
        Collections.shuffle(carte);
    }

}
