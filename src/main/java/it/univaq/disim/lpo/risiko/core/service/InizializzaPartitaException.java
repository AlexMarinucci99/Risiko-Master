package it.univaq.disim.lpo.risiko.core.service;

import it.univaq.disim.lpo.risiko.core.RisikoException;

/**
 * Eccezione lanciata quando si verifica un errore durante l'inizializzazione della partita.
 */
public class InizializzaPartitaException extends RisikoException {

    private static final long serialVersionUID = 1L;

    /**
     * Costruttore senza parametri.
     */
    public InizializzaPartitaException() {
        super();
    }

    /**
     * Costruttore con messaggio di dettaglio.
     *
     * @param messaggio il messaggio dettagliato dell'eccezione.
     */
    public InizializzaPartitaException(String messaggio) {
        super(messaggio);
    }

    /**
     * Costruttore con messaggio di dettaglio e causa.
     *
     * @param messaggio il messaggio dettagliato dell'eccezione.
     * @param cause     la causa dell'eccezione.
     */
    public InizializzaPartitaException(String messaggio, Throwable cause) {
        super(messaggio,cause);
    }

    /**
     * Costruttore con causa.
     *
     * @param cause la causa dell'eccezione.
     */
    public InizializzaPartitaException(Throwable cause) {
        super(cause);
    }

}