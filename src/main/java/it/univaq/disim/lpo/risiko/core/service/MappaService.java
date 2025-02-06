package it.univaq.disim.lpo.risiko.core.service;

import it.univaq.disim.lpo.risiko.core.model.Continente;
import it.univaq.disim.lpo.risiko.core.model.Territorio;
import it.univaq.disim.lpo.risiko.core.model.Mappa;
import java.util.List;

/*
 * Interfaccia per i servizi relativi alla mappa del gioco.
 */
public interface MappaService {

    /**
     * Inizializza i continenti e i territori del gioco.
     *
     * @return una lista di continenti con i loro rispettivi territori inizializzati.
     */
    List<Continente> inizializzaContinentiETerritori();

    /**
     * Recupera tutti i territori di un determinato continente.
     *
     * @param nomeContinente il nome del continente di cui ottenere i territori.
     * @return una lista di territori appartenenti al continente specificato.
     */
    List<Territorio> getTuttiITerritori(String nomeContinente);

    /**
     * Ottiene la mappa corrente del gioco.
     *
     * @return l'oggetto Mappa che rappresenta la mappa del gioco.
     */
    Mappa getMappa();

    /**
     * Inizializza la mappa del gioco con i continenti forniti.
     *
     * @param continenti una lista di continenti da includere nella mappa.
     * @return l'oggetto Mappa inizializzato.
     * @throws InizializzaPartitaException se si verifica un errore durante l'inizializzazione.
     */
    Mappa inizializzaMappa(List<Continente> continenti) throws InizializzaPartitaException;

}