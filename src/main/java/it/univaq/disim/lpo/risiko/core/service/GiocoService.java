package it.univaq.disim.lpo.risiko.core.service;

import it.univaq.disim.lpo.risiko.core.model.Gioco;
import it.univaq.disim.lpo.risiko.core.model.Giocatore;
import java.io.IOException;
import it.univaq.disim.lpo.risiko.core.RisikoException;
import java.util.List;

/**
 * Interfaccia per i servizi relativi alla gestione della partita.
 */
public interface GiocoService {

    /**
     * Inizializza una nuova partita.
     *
     * @return un oggetto Gioco che rappresenta la nuova partita.
     * @throws InizializzaPartitaException se si verifica un errore durante l'inizializzazione.
     */
    Gioco inizializzaPartita() throws InizializzaPartitaException;

    /**
     * Carica una partita salvata da un file.
     *
     * @param fileName il nome del file da cui caricare la partita.
     * @return l'oggetto Gioco caricato dal file.
     * @throws IOException            se si verifica un errore di I/O.
     * @throws ClassNotFoundException se la classe del gioco non viene trovata.
     */
    Gioco caricaGioco(String fileName) throws IOException, ClassNotFoundException;

    /**
     * Ottiene l'ordine dei giocatori nella partita.
     *
     * @param gioco l'oggetto Gioco di cui ottenere l'ordine dei giocatori.
     * @return una lista di giocatori nell'ordine di gioco.
     */
    List<Giocatore> getOrdineGiocatori(Gioco gioco);

    /**
     * Esegue il turno di un giocatore.
     *
     * @param giocatore il giocatore il cui turno deve essere eseguito.
     * @param gioco     l'oggetto Gioco corrente.
     * @return true se il turno è stato completato con successo, false se la partita deve terminare o tornare al menu.
     * @throws RisikoException se si verifica un errore durante il turno.
     */
    boolean turnoGiocatore(Giocatore giocatore, Gioco gioco) throws RisikoException;

}