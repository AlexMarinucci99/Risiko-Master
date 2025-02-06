package it.univaq.disim.lpo.risiko.core;

/**
 * Eccezione personalizzata per il gioco Risiko.
 * 
 * Viene sollevata quando si verificano condizioni di errore specifiche
 * all'interno del flusso di esecuzione del gioco, consentendo di gestire
 * in modo concentrato problemi imprevisti o situazioni anomale.
 */

public class RisikoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RisikoException() {
        super();
    }

    public RisikoException(String message) {
        super(message);
    }

    public RisikoException(String message, Throwable cause) {
        super(message, cause);
    }

    public RisikoException(Throwable cause) {
        super(cause);
    }

}