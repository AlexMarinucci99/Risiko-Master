package it.univaq.disim.lpo.risiko.core.service.impl;

import it.univaq.disim.lpo.risiko.core.service.CartaObiettivoService;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import it.univaq.disim.lpo.risiko.core.model.CartaObiettivo;
import it.univaq.disim.lpo.risiko.core.model.Giocatore;

/**
 * Implementazione dell'interfaccia CartaObiettivoService.
 * Gestisce la creazione e l'assegnazione delle carte obiettivo.
 */
public class CartaObiettivoServiceImpl implements CartaObiettivoService {

	/**
	 * Genera una lista di carte obiettivo casuali.
	 *
	 * @param numeroObiettivi il numero di carte obiettivo da generare.
	 * @return una lista di carte obiettivo selezionate casualmente.
	 */
	public List<CartaObiettivo> generaObiettiviCasuali(int numeroObiettivi) {
		List<CartaObiettivo> obiettiviDisponibili = Arrays.asList(
			/*
			 * Questo obiettivo è stato usato per eseguire dei test veloci
			 * durante la fase di sviluppo, inutile per il gameplay effettivo.
			 */
			// new CartaObiettivo("Conquistare 1 territorio")
			new CartaObiettivo("Conquistare la totalità dell'America del Nord e dell'Africa"),
			new CartaObiettivo("Conquistare la totalità dell'America del Nord e dell'Oceania"),
			new CartaObiettivo("Conquistare la totalità dell'Asia e del Sud America"),
			new CartaObiettivo("Conquistare la totalità dell'Asia e dell'Africa"),
			new CartaObiettivo("Conquistare 18 territori presidiandoli con almeno due armate ciascuno"),
			new CartaObiettivo("Conquistare 24 territori"),
			new CartaObiettivo("Conquistare la totalità dell'Europa, del Sud America e di un terzo continente a scelta"),
			new CartaObiettivo("Conquistare la totalità dell'Europa, dell'Oceania e di un terzo continente a scelta"));
		
        // Mescola la lista per randomizzare gli obiettivi
		Collections.shuffle(obiettiviDisponibili);
		
        // Assicura che il numero richiesto di obiettivi non superi quelli disponibili
		int numeroDaAssegnare = Math.min(numeroObiettivi, obiettiviDisponibili.size());
		return obiettiviDisponibili.subList(0, numeroDaAssegnare);
	}

	/**
	 * Assegna casualmente le carte obiettivo a ciascun giocatore.
	 *
	 * @param giocatori la lista dei giocatori.
	 * @param obiettivi la lista degli obiettivi disponibili.
	 */
	@Override
	public void assegnaObiettiviCasuali(List<Giocatore> giocatori, List<CartaObiettivo> obiettivi) {
		// Mescola gli obiettivi per randomizzare l'assegnazione
		Collections.shuffle(obiettivi);

		// Assegna gli obiettivi ai giocatori
		for (int i = 0; i < giocatori.size(); i++) {
			Giocatore giocatore = giocatori.get(i);
			CartaObiettivo obiettivoAssegnato = obiettivi.get(i);
			giocatore.setObiettivo(obiettivoAssegnato);
		}
	}

}