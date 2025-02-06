package it.univaq.disim.lpo.risiko.core;

/**
 * Classe di avvio del gioco.
 * Questa classe fornisce il punto d'ingresso (main) del programma, limitandosi 
 * a richiamare il metodo statico di avvio del gioco nella classe Runner.
 * In questo modo, la logica di esecuzione del principale del gioco rimane nella 
 * classe Runner, mentre StartGame funge solo da "bootstrap" dell'applicazione.
 */
public class StartGame {
    public static void main(String[] args) {
        try {
            Runner.startRunner();
        } catch (Exception e) {
            System.err.println("Errore anomalo: " + e.getMessage());
            e.printStackTrace();
        }     
    }
}