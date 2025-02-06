package it.univaq.disim.lpo.risiko.core.service;

import java.io.IOException;
import it.univaq.disim.lpo.risiko.core.model.Gioco;

/**
 * Interfaccia per i servizi relativi alle operazioni sui file
 */

public interface FileService {

    /**
     * Salva lo stato della partita su un file.
     * 
     * @param gioco    l'oggetto Gioco da salvare.
     * @param fileName il nome del file su cui salvare il gioco.
     * @throws IOException se si verifica un errore durante la scrittura del file.
     */
    void salvaGioco(Gioco gioco, String fileName) throws IOException;

    /**
     * Carica lo stato di una partita da un file.
     * 
     * @param fileName il nome del file da cui caricare il gioco.
     * @return l'oggetto Gioco caricato dal file.
     * @throws IOException            se si verifica un errore di I/O durante il
     *                                caricamento.
     * @throws ClassNotFoundException se la classe del gioco non viene trovata.
     */
    Gioco caricaGioco(String fileName) throws IOException, ClassNotFoundException;

    /**
     * Legge i dati sa un file
     * 
     * @param fileName il nome del file da cui leggere i dati.
     * @return una stringa contenente i dati letti dal file.
     */
    String readData(String fileName);

    /**
     * Scrive i dati su un file
     * 
     * @param fileName il nome del file su cui scrivere i dati.
     * @param data     una stringa contenente i dati da scrivere sul file.
     */
    void writeData(String fileName, String data);

}
