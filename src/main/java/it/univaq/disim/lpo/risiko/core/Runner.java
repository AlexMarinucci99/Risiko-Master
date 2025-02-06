package it.univaq.disim.lpo.risiko.core;

import it.univaq.disim.lpo.risiko.core.model.*;
import it.univaq.disim.lpo.risiko.core.service.*;
import it.univaq.disim.lpo.risiko.core.service.impl.*;
import it.univaq.disim.lpo.risiko.core.utils.*;
import org.fusesource.jansi.AnsiConsole;
import java.util.List;

/**
 * Classe che coordina l'intero ciclo di vita del gioco.
 * La classe Runner si occupa di:
 * Inizializzare il gioco (nuova partita e caricamento di partite salvate)
 * Eseguire il loop principale dei turni di gioco, gestendo il turno di ogni 
 * giocatore fino alla fine della partita o al ritorno al menù.
 * Visualizzare messaggi e loggare gli eventi principali.
 * il metodo startRunner() viene richiamato dalla classe StartGame.
 * Rimane in esecuzione finchè la partita non termina o finchè l'utente non
 * decide di interrompere.
 */
public class Runner {
    public static void startRunner() {
        // Installa AnsiConsole per supportare i colori ANSI nella console
        AnsiConsole.systemInstall();

        // Creazione delle istanze dei servizi necessari
        GiocoServiceImpl giocoService = new GiocoServiceImpl();
        GiocatoreService giocatoreService = new GiocatoreServiceImpl();
        
        // Flag per controllare il ciclo principale del gioco
        boolean running = true; 
        
        // Ciclo principale del gioco
        while (running) {
            try {
                // Inizializza la partita (nuova o caricata)
                Gioco gioco = giocoService.inizializzaPartita();

                // Distribuisce le armate iniziali solo se non sono già state distribuite
                if (!gioco.isArmateDistribuite()) {
                    int numeroGiocatori = gioco.getOrdineGiocatori().size();
                    int armatePerGiocatore = giocatoreService.calcolaArmatePerGiocatore(numeroGiocatori);
                    giocatoreService.distribuzioneInizialeArmate(gioco.getOrdineGiocatori(), armatePerGiocatore);

                    // Imposta il flag per evitare la ridistribuzione
                    gioco.setArmateDistribuite(true);                
                }

                // Ciclo interno del gioco: esegue i turni dei giocatori.
                while (gioco.isPartitaInCorso() && !gioco.isRitornaAlMenu()) {
                    List<Giocatore> ordineGiocatori = gioco.getOrdineGiocatori();

                    // Ottiene il giocatore corrente.
                    Giocatore giocatore = ordineGiocatori.get(gioco.getCurrentPlayerIndex());

                    // Esegue il turno del giocatore.
                    boolean continua = giocoService.turnoGiocatore(giocatore, gioco);
                    if (!continua || !gioco.isPartitaInCorso() || gioco.isRitornaAlMenu()) {
                        if (gioco.isPartitaInCorso()) {
                            // Logga la fine del turno del giocatore.
                            FileServiceImpl.getInstance().writeLog("Turno di " + giocatore.getNome().toUpperCase() + "terminato.");
                            FileServiceImpl.getInstance().writeLog("");
                        } else {
                            FileServiceImpl.getInstance().writeLog("GIOCO TERMINATO");
                        }
                        break; // Esce dal ciclo interno se il gioco è terminato o si torna al menù.
                    }

                    // Aggiorna l'indice del giocatore corrente.
                    int nextPlayerIndex = (gioco.getCurrentPlayerIndex() + 1) % ordineGiocatori.size();
                    gioco.setCurrentPlayerIndex(nextPlayerIndex);

                    // Controlla se un round è stato completato(Tutti i giocatori hanno giocato).
                    if (nextPlayerIndex == 0) {
                        gioco.incrementRoundCount(); // Incrementa il contatore dei round.

                        // Mostra messaggio di completamento del round.
                        OutputUtils.println("\n══════════════════", OutputUtils.ANSI_BRIGHT_GREEN, OutputUtils.ANSI_BOLD);
                        OutputUtils.println("ROUND " + (gioco.getRoundCount() - 1) + " COMPLETATO", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);
                        OutputUtils.println("══════════════════", OutputUtils.ANSI_BRIGHT_GREEN, OutputUtils.ANSI_BOLD);
                        try {
                            Thread.sleep(2000);   // Pausa per migliorare la leggibilità.
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } 
                    }
                }

                // Controlla se il player ha scelto di tornare al menù.
                if(gioco.isRitornaAlMenu()) {
                    continue;  // Torna all'inizio del ciclo principale.
                }

                // Controlla se la partita è terminata.
                if(!gioco.isPartitaInCorso()) {
                    OutputUtils.print("La partita è terminata, vuoi tornare al menù iniziale? (S/N): ", OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BOLD);
                    String risposta = InputManagerSingleton.getInstance().readString();
                    if (risposta.equalsIgnoreCase("n")){
                        System.out.println();
                        running = false; // Termina il gioco.
                    }  
                    // Se l'utente sceglie "s", il gioco riparte.              
                }
            
            } catch (InizializzaPartitaException e) {
                System.out.println("Errore durante l'inizializzazione della partita: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Si è verificato un errore: " + e.getMessage());
            }
        }
        // Disinstalla AnsiConsole al termine del gioco
        AnsiConsole.systemUninstall();
    }
}