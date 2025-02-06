package it.univaq.disim.lpo.risiko.core.service;

import it.univaq.disim.lpo.risiko.core.model.Territorio;

/**
 * Interfaccia per i servizi relativi ai territori.
 */
public interface TerritorioService {

	/**
     * Aggiunge un numero specificato di armate a un territorio.
     *
     * @param territorio il territorio a cui aggiungere le armate
     * @param armate il numero di armate da aggiungere
     */
	void aggiungiArmate(Territorio territorio, int armate);

	/**
     * Rimuove un numero specificato di armate da un territorio.
     *
     * @param territorio il territorio da cui rimuovere le armate
     * @param armate il numero di armate da rimuovere
     */
	void rimuoviArmate(Territorio territorio, int armate);
	
}