package it.univaq.disim.lpo.risiko.core.service;

import java.util.List;
import it.univaq.disim.lpo.risiko.core.model.CartaObiettivo;
import it.univaq.disim.lpo.risiko.core.model.Giocatore;

/**
 * Interfaccia per i servizi relativi alle carte obiettivo.
 */
public interface CartaObiettivoService {

    /**
     * Genera una lista di carte obiettivo casuali.
     * 
     * @param numeroObiettivi il numero di carte obiettivo da generare.
     * @return una lista di carte obiettivo generate casualmente.
     */
    List<CartaObiettivo> generaObiettiviCasuali(int numeroObiettivi);

    /**
     * Assegna casualmente un obiettivo ai giocatori.
     * 
     * @param giocatori        la lista dei giocatori a cui assegnare gli obiettivi.
     * @param obiettiviCasuali la lista degli obiettivi da assegnare.
     */
    void assegnaObiettiviCasuali(List<Giocatore> giocatori, List<CartaObiettivo> obiettiviCasuali);

}
