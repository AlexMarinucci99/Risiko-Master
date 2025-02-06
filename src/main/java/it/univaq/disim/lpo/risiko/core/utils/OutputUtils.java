package it.univaq.disim.lpo.risiko.core.utils;

import it.univaq.disim.lpo.risiko.core.model.Giocatore;

/**
 * Classe di utilità per la gestione dell'output con codici ANSI.
 * Questa classe fornisce costanti per i codici ANSI che possono essere utilizzati
 * per modificare il colore del testo e dello sfondo nella console.
 */
public class OutputUtils {

    // Codici ANSI per i colori del testo
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_UNDERLINE = "\u001B[4m";
    public static final String ANSI_BLINK = "\u001B[5m";
    public static final String ANSI_REVERSE = "\u001B[7m";
    public static final String ANSI_HIDDEN = "\u001B[8m";

    // Codici ANSI per i colori del testo
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BRIGHT_RED = "\u001B[91m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BRIGHT_GREEN = "\u001B[92m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BRIGHT_YELLOW = "\u001B[93m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_BRIGHT_BLUE = "\u001B[94m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_BRIGHT_PURPLE = "\u001B[95m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_BRIGHT_CYAN = "\u001B[96m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BRIGHT_WHITE = "\u001B[97m";

    // Codici ANSI per i colori di sfondo
    public static final String ANSI_BACKGROUND_BLACK = "\u001B[40m";
    public static final String ANSI_BACKGROUND_RED = "\u001B[41m";
    public static final String ANSI_BACKGROUND_GREEN = "\u001B[42m";
    public static final String ANSI_BACKGROUND_YELLOW = "\u001B[43m";
    public static final String ANSI_BACKGROUND_BLUE = "\u001B[44m";
    public static final String ANSI_BACKGROUND_PURPLE = "\u001B[45m";
    public static final String ANSI_BACKGROUND_CYAN = "\u001B[46m";
    public static final String ANSI_BACKGROUND_WHITE = "\u001B[47m";

    // Metodi per stampare testo con effetti
    public static void print(String text, String... ansiCodes) {
        StringBuilder sb = new StringBuilder();
        for (String code : ansiCodes) {
            sb.append(code);
        }
        sb.append(text);
        sb.append(ANSI_RESET);
        System.out.print(sb.toString());
    }

    public static void println(String text, String... ansiCodes) {
        print(text + "\n", ansiCodes);
    }

    public static void printTurnHeader(Giocatore giocatore) {
        println("\n══════════════════════", ANSI_BRIGHT_PURPLE, ANSI_BOLD);
        println("E' IL TURNO DI " + giocatore.getNome().toUpperCase(), ANSI_BOLD);
        println("══════════════════════", ANSI_BRIGHT_PURPLE, ANSI_BOLD);
    }

}