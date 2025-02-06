package it.univaq.disim.lpo.risiko.core.service.impl;

import it.univaq.disim.lpo.risiko.core.model.Territorio;
import it.univaq.disim.lpo.risiko.core.service.TerritorioService;

/**
 * Implementazione dell'interfaccia TerritorioService.
 * Fornisce metodi per modificare il numero di armate in un territorio.
 */
public class TerritorioServiceImpl implements TerritorioService {
	
	/**
     * Aggiunge armate al territorio specificato.
     *
     * @param territorio il territorio a cui aggiungere le armate.
     * @param armate     il numero di armate da aggiungere.
     */
	@Override
	public void aggiungiArmate(Territorio territorio, int armate) {
		territorio.setArmate(territorio.getArmate() + armate);
	}

	/**
     * Rimuove armate dal territorio specificato.
     *
     * @param territorio il territorio da cui rimuovere le armate.
     * @param armate     il numero di armate da rimuovere.
     */
	@Override
	public void rimuoviArmate(Territorio territorio, int armate) {
	     territorio.setArmate((territorio.getArmate() - armate));
	}

}