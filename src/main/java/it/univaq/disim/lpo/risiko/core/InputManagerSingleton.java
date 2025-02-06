package it.univaq.disim.lpo.risiko.core;

import java.io.Closeable;
import java.io.IOException;
import java.util.Scanner;
import it.univaq.disim.lpo.risiko.core.utils.OutputUtils;

/**
 * Classe Singleton per gestire l'input dell'utente tramite Scanner.
 */
public class InputManagerSingleton implements Closeable {

    private Scanner scanner;
    private static InputManagerSingleton instance = null;

    /**
     * Costruttore privato per prevenire l'instanziazione.
     * Inizializza lo Scanner.
     */
    private InputManagerSingleton() {
        scanner = new Scanner(System.in);
    }

    /**
     * Restituisce l'unica istanza di SingletonMain.
     *
     * @return l'istanza di SingletonMain.
     */
    public static InputManagerSingleton getInstance() {
        if (instance == null) {
            synchronized (InputManagerSingleton.class) {
                if (instance == null) {
                    instance = new InputManagerSingleton();
                }
            }
        }
        return instance;
    }

    /**
     * Ottiene l'istanza dello Scanner.
     *
     * @return l'istanza dello Scanner.
     */
    public Scanner getScanner() {
        return scanner;
    }

    /**
     * Chiude la risorsa Scanner.
     */
    public void disposeScanner() {
        scanner.close();
    }

    /**
     * Chiude la risorsa quando non è più necessaria.
     *
     * @throws IOException se si verifica un errore di I/O.
     */
    @Override
    public void close() throws IOException {
        scanner.close();
    }

    /**
     * Legge un intero dalla console.
     * Gestisce input non validi chiedendo nuovamente all'utente.
     *
     * @return l'intero letto dall'utente.
     */
    public Integer readInteger() {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                OutputUtils.println("\nInput non valido. Inserisci un numero.", OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
                OutputUtils.print("\nScelta: ", OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BOLD);
            }
        }
    }

    /**
     * Legge un intero dalla console finché non corrisponde a uno dei valori
     * possibili.
     *
     * @param possibleValues un array di valori interi validi.
     * @return l'intero inserito dall'utente che corrisponde a uno dei valori
     *         possibili.
     */
    public Integer readIntegerUntilPossibleValue(Integer[] possibleValues) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine());
                for (int possibleValue : possibleValues) {
                    if (value == possibleValue) {
                        return value;
                    }
                }
                OutputUtils.println("\nValore non valido. Riprova.", OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
                OutputUtils.print("\nScelta: ", OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BOLD);
            } catch (NumberFormatException e) {
                OutputUtils.println("\nInput non valido. Inserisci un numero.", OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
                OutputUtils.print("\nScelta: ", OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BOLD);
            }
        }
    }

    /**
     * Legge una stringa non vuota dalla console.
     * Gestisce input vuoti chiedendo nuovamente all'utente.
     *
     * @return la stringa letta dall'utente.
     */
    public String readString() {
        try {
            String input = scanner.nextLine();
            while (input == null || input.trim().isEmpty()) {
                OutputUtils.print("\nInput vuoto. Per favore, inserisci un valore: ", OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
                input = scanner.nextLine();
            }
            return input;
        } catch (Exception e) {
            OutputUtils.println("\nErrore durante la lettura dell'input: " + e.getMessage(), OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
            return null;
        }
    }

}