package it.univaq.disim.lpo.risiko.core.service;

import java.util.List;
import it.univaq.disim.lpo.risiko.core.model.Giocatore;
import it.univaq.disim.lpo.risiko.core.model.Gioco;
import it.univaq.disim.lpo.risiko.core.model.Mappa;
import it.univaq.disim.lpo.risiko.core.model.Territorio;

/**
 * Interfaccia per i servizi relativi ai giocatori.
 */

public interface GiocatoreService {

	 /**
     * Aggiunge un territorio alla lista dei territori controllati dal giocatore.
     *
     * @param giocatore il giocatore a cui aggiungere il territorio.
     * @param territorio il territorio da aggiungere.
     */
	void aggiungiTerritorio(Giocatore giocatore, Territorio territorio);
	
	/**
     * Rimuove un territorio dalla lista dei territori controllati dal giocatore.
     *
     * @param giocatore il giocatore da cui rimuovere il territorio.
     * @param territorio il territorio da rimuovere.
     */
	void rimuoviTerritorio(Giocatore giocatore,Territorio territorio);
	
	/**
     * Aggiunge armate al giocatore.
     *
     * @param giocatore il giocatore a cui aggiungere le armate.
     * @param armate    il numero di armate da aggiungere.
     */
	void aggiungiArmate(Giocatore giocatore, int armate);
	
	 /**
     * Rimuove armate dal giocatore.
     *
     * @param giocatore il giocatore da cui rimuovere le armate.
     * @param armate    il numero di armate da rimuovere.
     */
	void rimuoviArmate(Giocatore giocatore, int armate);
	
	/**
     * Distribuisce le armate iniziali tra i giocatori.
     *
     * @param giocatori          la lista dei giocatori.
     * @param armatePerGiocatore il numero di armate che ogni giocatore deve distribuire.
     */
	void distribuzioneInizialeArmate(List<Giocatore> giocatori, int armatePerGiocatore);
	
	/**
     * Crea i giocatori per la partita.
     *
     * @param numeroGiocatori il numero di giocatori da creare.
     * @return una lista di giocatori creati.
     */
	List<Giocatore> creaGiocatori(int numeroGiocatori);
	
	 /**
     * Determina l'ordine di gioco lanciando i dadi per ciascun giocatore.
     *
     * @param giocatori la lista dei giocatori.
     * @return una lista di giocatori ordinata in base ai risultati dei dadi.
     */
	List<Giocatore> lancioDadiPerPrimoGiocatore(List<Giocatore> giocatori);
	
	 /**
     * Calcola il numero di armate iniziali per ciascun giocatore in base al numero totale di giocatori.
     *
     * @param numeroGiocatori il numero totale di giocatori.
     * @return il numero di armate per giocatore.
     */
	int calcolaArmatePerGiocatore(int numeroGiocatori);
	
	/**
     * Consente ai giocatori di scegliere i colori delle loro armate.
     *
     * @param giocatori la lista dei giocatori.
     */
	void scegliColoriGiocatori(List<Giocatore> giocatori);
	
	/**
     * Distribuisce i territori tra i giocatori in modo casuale.
     *
     * @param giocatori la lista dei giocatori.
     * @param mappa     la mappa del gioco contenente i territori.
     */
	void distribuzioneTerritori(List<Giocatore> giocatori,Mappa mappa);
	
	/**
     * Permette al giocatore di scambiare carte per ottenere armate aggiuntive.
     *
     * @param giocatore il giocatore che vuole scambiare le carte.
     * @param gioco     l'oggetto Gioco corrente.
     * @return il numero di armate ottenute dallo scambio.
     */
	int scambiaCartePerArmate(Giocatore giocatore, Gioco gioco);
	
	 /**
     * Verifica se il giocatore possiede almeno una combinazione valida di carte per effettuare uno scambio.
     *
     * @param giocatore il giocatore da verificare.
     * @return true se possiede una combinazione valida, false altrimenti.
     */
	boolean possiedeAlmenoUnaCombinazioneValida(Giocatore giocatore);

}