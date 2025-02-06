package it.univaq.disim.lpo.risiko.core.service.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Arrays;
import java.util.Collections;
import org.fusesource.jansi.AnsiConsole;

import it.univaq.disim.lpo.risiko.core.InputManagerSingleton;
import it.univaq.disim.lpo.risiko.core.model.*;
import it.univaq.disim.lpo.risiko.core.service.*;
import it.univaq.disim.lpo.risiko.core.utils.OutputUtils;

/**
 * Implementazione dell'interfaccia GiocoService.
 * Gestisce l'inizializzazione e il flusso della partita.
 */
public class GiocoServiceImpl implements GiocoService {

    private final FileService fileservice = FileServiceImpl.getInstance();
    private final GiocatoreService giocatoreService = new GiocatoreServiceImpl();
    private final CartaObiettivoService obiettivoService = new CartaObiettivoServiceImpl();
    private final MappaService mappaService;

    /**
     * Costruttore che inizializza il servizio della mappa.
     * Gestisce eventuali eccezioni durante l'inizializzazione.
     */
    public GiocoServiceImpl() {
        try {
            this.mappaService = new MappaServiceImpl();
        } catch (InizializzaPartitaException e) {
            throw new RuntimeException("Errore nell'inizializzazione della mappa", e);
        }
    }

    /**
     * Restituisce l'ordine dei giocatori nella partita.
     *
     * @param gioco l'oggetto Gioco corrente.
     * @return la lista ordinata dei giocatori.
     */
    public List<Giocatore> getOrdineGiocatori(Gioco gioco) {
        return gioco.getOrdineGiocatori();
    }

    /**
     * Inizializza una nuova partita o carica una partita esistente.
     * Presenta all'utente un menu per scegliere l'opzione desiderata.
     *
     * @return l'oggetto Gioco inizializzato.
     * @throws InizializzaPartitaException se si verifica un errore durante
     *                                     l'inizializzazione.
     */
    public Gioco inizializzaPartita() throws InizializzaPartitaException {
        // Titolo con ASCII art per l'intestazione del gioco
        String titoloFiglet = "\r\n" + //
                "           _____   _____   _____  _____  _  __ ____       _    _         _                 ____             \r\n"
                + //
                "          |  __ \\ |_   _| / ____||_   _|| |/ // __ \\     | |  | |       (_)         /\\    / __ \\            \r\n"
                + //
                "          | |__) |  | |  | (___    | |  | ' /| |  | |    | |  | | _ __   _ __   __ /  \\  | |  | |           \r\n"
                + //
                "          |  _  /   | |   \\___ \\   | |  |  < | |  | |    | |  | || '_ \\ | |\\ \\ / // /\\ \\ | |  | |           \r\n"
                + //
                "          | | \\ \\  _| |_  ____) | _| |_ | . \\| |__| |    | |__| || | | || | \\ V // ____ \\| |__| |           \r\n"
                + //
                "          |_|  \\_\\|_____||_____/ |_____||_|\\_\\\\____/      \\____/ |_| |_||_|  \\_//_/    \\_\\\\___\\_\\           \r\n"
                + //
                "  _                 __  __             _____  _______  ______  _____   __  __  _____  _   _  _____    _____ \r\n"
                + //
                " | |               |  \\/  |    /\\     / ____||__   __||  ____||  __ \\ |  \\/  ||_   _|| \\ | ||  __ \\  / ____|\r\n"
                + //
                " | |__   _   _     | \\  / |   /  \\   | (___     | |   | |__   | |__) || \\  / |  | |  |  \\| || |  | || (___  \r\n"
                + //
                " | '_ \\ | | | |    | |\\/| |  / /\\ \\   \\___ \\    | |   |  __|  |  _  / | |\\/| |  | |  | . ` || |  | | \\___ \\ \r\n"
                + //
                " | |_) || |_| |    | |  | | / ____ \\  ____) |   | |   | |____ | | \\ \\ | |  | | _| |_ | |\\  || |__| | ____) |\r\n"
                + //
                " |_.__/  \\__, |    |_|  |_|/_/    \\_\\|_____/    |_|   |______||_|  \\_\\|_|  |_||_____||_| \\_||_____/ |_____/ \r\n"
                + //
                "          __/ |                                                                                             \r\n"
                + //
                "         |___/                                                                                              \r\n"
                + //
                "";
        // Pulizia dello schermo e stampa del titolo
        AnsiConsole.out().print("\033[H\033[2J");
        AnsiConsole.out().flush();

        OutputUtils.println(titoloFiglet, OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BLINK, OutputUtils.ANSI_BOLD);
        System.out.println();
        System.out.println();

        // Menù iniziale per scegliere tra nuova partita o caricamento
        OutputUtils.println("Seleziona un'opzione:", OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BOLD);
        System.out.println();
        OutputUtils.println("1 - Avvia una Nuova Partita", OutputUtils.ANSI_GREEN, OutputUtils.ANSI_BOLD);
        OutputUtils.println("2 - Carica una Partita Esistente", OutputUtils.ANSI_GREEN, OutputUtils.ANSI_BOLD);
        System.out.println();
        OutputUtils.print("\nScelta: ", OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BOLD);
        Integer modo = InputManagerSingleton.getInstance().readIntegerUntilPossibleValue(new Integer[] { 1, 2 });

        switch (modo) {
            case 1:
                return avviaNuovaPartita();
            case 2:
                return caricaPartitaEsistente();
            default:
                throw new InizializzaPartitaException("Opzione non valida.");
        }
    }

    /**
     * Avvia una nuova partita.
     *
     * @return l'oggetto Gioco inizializzato.
     * @throws InizializzaPartitaException se si verifica un errore durante l'inizializzazione
     *
     */
    private Gioco avviaNuovaPartita() throws InizializzaPartitaException {
        // Pulizia dello schermo
        AnsiConsole.out().print("\033[H\033[2J");
        AnsiConsole.out().flush();

        // Step 1: Inizializzazione del numero di giocatori
        OutputUtils.println("\n——————————————————————————", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);
        OutputUtils.println("IMPOSTAZIONE DELLA PARTITA", OutputUtils.ANSI_BOLD);
        OutputUtils.println("——————————————————————————", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);
        OutputUtils.print("\nInserisci il numero di giocatori (2-6): ", OutputUtils.ANSI_BOLD);
        Integer numeroGiocatori = InputManagerSingleton.getInstance().readIntegerUntilPossibleValue(new Integer[] { 2, 3, 4, 5, 6 });

        // Step 2: Creazione dei giocatori
        List<Giocatore> giocatori = giocatoreService.creaGiocatori(numeroGiocatori);
        if (giocatori == null || giocatori.isEmpty()) {
            throw new InizializzaPartitaException("Errore nella creazione dei giocatori. La lista dei giocatori è null o vuota.");
        }

        // Step 3: Generazione e assegnazione degli obiettivi
        List<CartaObiettivo> obiettivi = obiettivoService.generaObiettiviCasuali(giocatori.size());
        obiettivoService.assegnaObiettiviCasuali(giocatori, obiettivi);
        Mappa mappa = mappaService.getMappa();

        // Step 4: Determinazione dell'ordine dei giocatori
        List<Giocatore> ordineGiocatori = giocatoreService.lancioDadiPerPrimoGiocatore(giocatori);
        OutputUtils.println("\nIl giocatore " + ordineGiocatori.get(0).getNome().toUpperCase() + " inizia per primo!", OutputUtils.ANSI_BRIGHT_YELLOW, OutputUtils.ANSI_BOLD);

        // Step 5: Creazione del gioco con l'ordine dei giocatori
        int gameNumber = FileServiceImpl.getInstance().getNextGameNumber();
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        // Crea il nome del file di log nella cartella "logs"
        String logFileName = "partita_" + gameNumber + "_" + currentDate + ".log";

        Gioco gioco = new Gioco("Inizio", ordineGiocatori, mappa, 6, new ArrayList<>(), new ArrayList<>());
        gioco.setLogFileName(logFileName);

        // Imposta il file di log corrente nel FileServiceImpl
        FileServiceImpl.getInstance().setCurrentLogFileName(logFileName);

        // Logging dell'inizio del gioco e assegnazione degli obiettivi
        FileServiceImpl.getInstance().writeLog("GIOCO AVVIATO");
        FileServiceImpl.getInstance().writeLog(""); // Riga vuota per separare le azioni
        for (Giocatore giocatore : giocatori) {
            FileServiceImpl.getInstance().writeLog("Il giocatore " + giocatore.getNome().toUpperCase() + " ha ricevuto l'obiettivo: " + giocatore.getObiettivo().getDescrizione());
            FileServiceImpl.getInstance().writeLog(""); // Riga vuota per separare le azioni
        }

        // Step 6: Assegna l'ordine dei giocatori al gioco
        gioco.setOrdineGiocatori(ordineGiocatori);

        // Step 7: I giocatori scelgono i colori
        giocatoreService.scegliColoriGiocatori(ordineGiocatori);

        // Step 8: Distribuzione delle armate iniziali
        OutputUtils.println("\r\n"
                + "   _____ _                                _       _     _                    _                            _   _ _        _ \r\n"
                + "  / ____| |                              (_)     (_)   (_)                  | |                          | | (_) |      | |\r\n"
                + " | (___ | |_ __ _     _ __   ___ _ __     _ _ __  _ _____  __ _ _ __ ___    | | __ _     _ __   __ _ _ __| |_ _| |_ __ _| |\r\n"
                + "  \\___ \\| __/ _` |   | '_ \\ / _ \\ '__|   | | '_ \\| |_  / |/ _` | '__/ _ \\   | |/ _` |   | '_ \\ / _` | '__| __| | __/ _` | |\r\n"
                + "  ____) | || (_| |   | |_) |  __/ |      | | | | | |/ /| | (_| | | |  __/   | | (_| |   | |_) | (_| | |  | |_| | || (_| |_|\r\n"
                + " |_____/ \\__\\__,_|   | .__/ \\___|_|      |_|_| |_|_/___|_|\\__,_|_|  \\___|   |_|\\__,_|   | .__/ \\__,_|_|   \\__|_|\\__\\__,_(_)\r\n"
                + "                     | |                                                                | |                                \r\n"
                + "                     |_|                                                                |_|                                \r\n"
                + "", OutputUtils.ANSI_BRIGHT_YELLOW, OutputUtils.ANSI_BOLD);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Pulizia dello schermo e distribuzione dei territori
        AnsiConsole.out().print("\033[H\033[2J");
        AnsiConsole.out().flush();
        giocatoreService.distribuzioneTerritori(ordineGiocatori, mappa);

        // Inizializza il mazzo di carte
        MazzoDiCarte mazzoDiCarte = new MazzoDiCarte(mappa.getTerritori());
        gioco.setMazzoDiCarte(mazzoDiCarte);

        return gioco;
    }

    /**
     * Carica una partita salvata in precedenza.
     *
     * @return l'oggetto Gioco caricato.
     * @throws InizializzaPartitaException se si verifica un errore durante il
     *                                     caricamento.
     */
    private Gioco caricaPartitaEsistente() throws InizializzaPartitaException {
        while (true) {
            // Pausa per migliorare l'esperienza utente
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Pulizia dello schermo
            AnsiConsole.out().print("\033[H\033[2J");
            AnsiConsole.out().flush();

            // Lista dei salvataggi disponibili
            OutputUtils.println("\nSalvataggi disponibili:", OutputUtils.ANSI_BOLD);
            File saveFolder = new File("saves/");
            if (saveFolder.exists() && saveFolder.isDirectory()) {
                String[] files = saveFolder.list();
                if (files != null && files.length > 0) {
                    for (String fileName : files) {
                        if (!fileName.equals(".gitkeep")) { // Escludi il file .gitkeep
                            OutputUtils.println("- " + fileName, OutputUtils.ANSI_BOLD);
                        }
                    }
                } else {
                    OutputUtils.println("Nessun file di salvataggio trovato.", OutputUtils.ANSI_BOLD);
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return inizializzaPartita(); // Ritorna al menu principale
                }
            } else {
                OutputUtils.println("Nessun file di salvataggio trovato.", OutputUtils.ANSI_BOLD);
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return inizializzaPartita(); // Ritorna al menu principale
            }

            // Richiesta del nome del file da caricare
            OutputUtils.print("\nInserisci il nome del file da caricare: ", OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BOLD);
            String valore = InputManagerSingleton.getInstance().readString();
            System.out.println();

            if (!valore.endsWith(".save")) {
                valore += ".save";
            }

            try {
                Gioco giocoCaricato = fileservice.caricaGioco(valore);

                // Gestione del nome del file di log associato al gioco caricato
                String logFileName = giocoCaricato.getLogFileName();
                if (logFileName == null) {
                    FileServiceImpl.getInstance().setCurrentLogFileName(logFileName);
                } else {
                    // Se il gioco caricato non ha un logFileName, imposta un nome di default o chiedi all'utente
                    logFileName = valore.replace(".save", ".log");
                    giocoCaricato.setLogFileName(logFileName);
                    FileServiceImpl.getInstance().setCurrentLogFileName(logFileName);
                }

                // Verifica e ripristino di eventuali errori mancanti
                if (giocoCaricato.getRoundCount() < 1) {
                    giocoCaricato.setRoundCount(1);
                }
                if (giocoCaricato.getCurrentTurnState() == null) {
                    giocoCaricato.setCurrentTurnState(new TurnoGioco());
                }

                giocoCaricato.setLoadedGame(true);
                return giocoCaricato;
            } catch (IOException | ClassNotFoundException e) {
                OutputUtils.println("\nErrore nel caricamento del file: " + e.getMessage(), OutputUtils.ANSI_BRIGHT_RED, OutputUtils.ANSI_BOLD);
                return null;
            }
        }
    }

    /**
     * Carica un gioco da un file specificato.
     *
     * @param fileName il nome del file da cui caricare il gioco.
     * @return l'oggetto Gioco caricato.
     * @throws IOException            se si verifica un errore di I/O durante il caricamento.
     *
     * @throws ClassNotFoundException se la classe Gioco non viene trovata durante la deserializzazione.
     *
     */
    public Gioco caricaGioco(String fileName) throws IOException, ClassNotFoundException {
        return fileservice.caricaGioco(fileName);
    }

    /**
     * Verifica se il giocatore ha soddisfatto le condizioni di vittoria.
     * @param giocatore il giocatore corrente.
     * @param gioco     l'oggetto Gioco corrente.
     * @return  true se il giocatore ha vinto, false altrimenti.
     */
    public boolean verificaVittoria(Giocatore giocatore, Gioco gioco) {
        CartaObiettivo obiettivo = giocatore.getObiettivo();

        switch (obiettivo.getDescrizione()) {
            /* Questo è un case che è stato usato per eseguire dei test veloci
               durante la fase di sviluppo, inutile per il gameplay effettivo.
            case "Conquistare 1 territorio":
                if (giocatore.getTerritori_controllati().size() >= 1) {
                    dichiaraVittoria(giocatore, gioco);
                    return true;
                }
            break;
            */

            case "Conquistare la totalità dell'America del Nord e dell'Africa":
                if (haConquistatoContinente(giocatore, "America del Nord", gioco) && haConquistatoContinente(giocatore, "Africa", gioco)) {
                    dichiaraVittoria(giocatore, gioco);
                    return true;
                }
            break;
            
            case "Conquistare la totalità dell'America del Nord e dell'Oceania":
                if (haConquistatoContinente(giocatore, "America del Nord", gioco) && haConquistatoContinente(giocatore, "Oceania", gioco)) {
                    dichiaraVittoria(giocatore, gioco);
                    return true;
                }
            break;

            case "Conquistare la totalità dell'Asia e dell'America del Sud":
                if (haConquistatoContinente(giocatore, "Asia", gioco) && haConquistatoContinente(giocatore, "America del Sud", gioco)) {
                    dichiaraVittoria(giocatore, gioco);
                    return true;
                }
            break;

            case "Conquistare la totalità dell'Asia e dell'Africa":
                if (haConquistatoContinente(giocatore, "Asia", gioco) && haConquistatoContinente(giocatore, "Africa", gioco)) {
                    dichiaraVittoria(giocatore, gioco);
                    return true;
                }
            break;

            case "Conquistare 18 territori presidiandoli con almeno due armate ciascuno":
                long territoriConAlmenoDueArmate = giocatore.getTerritori_controllati().stream().filter(t -> t.getNumeroArmate() >= 2).count();
                if (territoriConAlmenoDueArmate >= 18) {
                    dichiaraVittoria(giocatore, gioco);
                    return true;
                }
            break;

            case "Conquistare 24 territori":
                if  (giocatore.getTerritori_controllati().size() >= 24) {
                    dichiaraVittoria(giocatore, gioco);
                    return true;
                }

            case "Conquistare la totalità dell'Europa, dell'America del Sud e di un terzo continente a scelta":
               if (haConquistatoContinente(giocatore, "Europa", gioco)
                    && haConquistatoContinente(giocatore, "America del Sud", gioco)
                    && haConquistatoUnAltroContinente(giocatore, Arrays.asList("Europa", "America del Sud"), gioco)) {
                        dichiaraVittoria(giocatore, gioco);
                        return true;
                }
            break;

            case "Conquistare la totalità dell'Europa, dell'Oceania e di un terzo continente a scelta":
               if (haConquistatoContinente(giocatore, "Europa", gioco)
                    && haConquistatoContinente(giocatore, "Oceania", gioco)
                    && haConquistatoUnAltroContinente(giocatore, Arrays.asList("Europa", "Oceania"), gioco)) {
                        dichiaraVittoria(giocatore, gioco);
                        return true;
                }
            break;

            default:
                System.out.println("Obiettivo non riconosciuto.");
                break;
        }

        return false;
    }

    /**
     * Verifica se il giocatore ha conquistato tutti i territori di un continente.
     * 
     * @param giocatore il giocatore da verificare.
     * @param nomeContinente il nome del continente.
     * @param gioco l'oggetto Gioco corrente.
     * @return  true se il giocatore controlla tutti i territori del continete, false altrimenti.
     */
    private boolean haConquistatoContinente(Giocatore giocatore, String nomeContinente, Gioco gioco) {
        Continente continente = gioco.getMappa().getContinente(nomeContinente);
        if (continente == null) {
            System.out.println("Il continente " + nomeContinente + "non esiste nella mappa.");
            return false;
        }
        return giocatore.getTerritori_controllati().containsAll(continente.getTerritori());
    }

    /**
     * Verifica se il giocatore ha conquistato un altro continente diverso da quelli specificati.
     * 
     * @param giocatore il giocatore da verificare.
     * @param esclusi la lista dei nomi dei continenti da escludere.
     * @param gioco  l'oggetto Gioco corrente.
     * @return true se il giocatore ha conquistato un altro continente, false altrimenti.
     */
    private boolean haConquistatoUnAltroContinente(Giocatore giocatore, List<String> esclusi, Gioco gioco) {
        for (Continente continente : gioco.getMappa().getContinenti()) {
            String nomeContinente = continente.getNome();
            if (!esclusi.contains(nomeContinente) && haConquistatoContinente(giocatore, nomeContinente, gioco)) {
                return true;
            }   
        }
        return false;
    }
    
    /**
     * Dichiara la vittoria del giocatore.
     *
     * @param giocatore il giocatore che ha vinto.
     * @param gioco     l'oggetto Gioco corrente.
     */
    private void dichiaraVittoria(Giocatore giocatore, Gioco gioco) {
        // Pausa
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Pulizia schermo
        AnsiConsole.out().print("\033[H\033[2J");
        AnsiConsole.out().flush();

        OutputUtils.println("\n\r\n" + //
                "  _____           _   _ _            _                      _             _          \r\n" + //
                " |  __ \\         | | (_| |          | |                    (_)           | |         \r\n" + //
                " | |__) __ _ _ __| |_ _| |_ __ _    | |_ ___ _ __ _ __ ___  _ _ __   __ _| |_ __ _   \r\n" + //
                " |  ___/ _` | '__| __| | __/ _` |   | __/ _ | '__| '_ ` _ \\| | '_ \\ / _` | __/ _` |  \r\n" + //
                " | |  | (_| | |  | |_| | || (_| |   | ||  __| |  | | | | | | | | | | (_| | || (_| |_ \r\n" + //
                " |_|   \\__,_|_|   \\__|_|\\__\\__,_|    \\__\\___|_|  |_| |_| |_|_|_| |_|\\__,_|\\__\\__,_(_)\r\n" + //
                "                                                                                     \r\n" + //
                "                                                                                     \r\n" + //
                "", OutputUtils.ANSI_BRIGHT_GREEN, OutputUtils.ANSI_BOLD);
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        OutputUtils.print("\nCongratulazioni " + giocatore.getNome().toUpperCase() + "! Hai completato il tuo obiettivo e...", OutputUtils.ANSI_YELLOW, OutputUtils.ANSI_BOLD);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        OutputUtils.println("\r\n" + //
                "  _    _          _____    __      _______ _   _ _______ ____      _                   _____        _____ _______ _____ _______       _ \r\n"
                + //
                " | |  | |   /\\   |_   _|   \\ \\    / |_   _| \\ | |__   __/ __ \\    | |        /\\       |  __ \\ /\\   |  __ |__   __|_   _|__   __|/\\   | |\r\n"
                + //
                " | |__| |  /  \\    | |      \\ \\  / /  | | |  \\| |  | | | |  | |   | |       /  \\      | |__) /  \\  | |__) | | |    | |    | |  /  \\  | |\r\n"
                + //
                " |  __  | / /\\ \\   | |       \\ \\/ /   | | | . ` |  | | | |  | |   | |      / /\\ \\     |  ___/ /\\ \\ |  _  /  | |    | |    | | / /\\ \\ | |\r\n"
                + //
                " | |  | |/ ____ \\ _| |_       \\  /   _| |_| |\\  |  | | | |__| |   | |____ / ____ \\    | |  / ____ \\| | \\ \\  | |   _| |_   | |/ ____ \\|_|\r\n"
                + //
                " |_|  |_/_/    \\_|_____|       \\/   |_____|_| \\_|  |_|  \\____/    |______/_/    \\_\\   |_| /_/    \\_|_|  \\_\\ |_|  |_____|  |_/_/    \\_(_)\r\n"
                + //
                "                                                                                                                                        \r\n"
                + //
                "                                                                                                                                        \r\n"
                + //
                "", OutputUtils.ANSI_BRIGHT_YELLOW, OutputUtils.ANSI_BOLD);
        
        // Logging della vittoria
        FileServiceImpl.getInstance().writeLog("Il giocatore " + giocatore.getNome().toUpperCase() + " ha vinto la partita completando l'obiettivo: " + giocatore.getObiettivo().getDescrizione());
        FileServiceImpl.getInstance().writeLog("");
        gioco.setPartitaInCorso(false);
    }

    public boolean turnoGiocatore(Giocatore giocatore, Gioco gioco) {
        TurnoGioco turnState = gioco.getCurrentTurnState();

        // Resetta la variabile haRicevutoCartaBonus all'inizio del turno
        giocatore.setHaRicevutoCartaBonus(false);

        // Inizializza la fase corrente se non impostata
        if (turnState.getCurrentPhase() == null) {
            turnState.setCurrentPhase(TurnoGioco.TurnPhase.START_TURN);
        }

        // Se la partita è stata caricata, stampa il messaggio di turno
        if (gioco.isLoadedGame()) {
            AnsiConsole.out().print("\033[H\033[2J");
            AnsiConsole.out().flush();

            OutputUtils.printTurnHeader(giocatore);

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Reset del flag dopo la stampa
            gioco.setLoadedGame(false);
        }

        while (!turnState.isTurnoTerminato()) {
            switch (turnState.getCurrentPhase()) {
                case START_TURN:
                    // Inizializza il turno
                    giocatore.resetTerritoriConquistatiNelTurno();

                    // Logging dell'inizio del turno
                    FileServiceImpl.getInstance().writeLog("Inizio del turno di " + giocatore.getNome().toUpperCase());

                    // Calcolo delle armate da distribuire
                    int armateTerritori = Math.max(3, giocatore.getTerritori_controllati().size() / 3);
                    int armateContinenti = calcolaArmateContinenti(giocatore, gioco.getMappa().getContinenti());
                    int armateTotali = armateTerritori + armateContinenti;

                    // Calcolo del numero di territori e continenti controllati
                    int numeroTerritori = giocatore.getTerritori_controllati().size();
                    int numeroContinenti = calcolaNumeroContinentiControllati(giocatore, gioco.getMappa().getContinenti());

                    // Logging delle armate ricevute
                    FileServiceImpl.getInstance().writeLog("Giocatore " + giocatore.getNome().toUpperCase()
                        + " riceve " + armateTotali + " armate (Territori: " + numeroTerritori + ", Continenti: " + numeroContinenti + ").");
                    
                    turnState.setArmateDaDistribuire(armateTotali);
                    turnState.setArmateTotali(armateTotali);
                    turnState.setNumeroTerritori(numeroTerritori);
                    turnState.setNumeroContinenti(numeroContinenti);
                    turnState.setCurrentPhase(TurnoGioco.TurnPhase.DISTRIBUTE_ARMIES);
                    break;
                
                case DISTRIBUTE_ARMIES:
                    distribuzioneArmate(giocatore, turnState, gioco);
                    turnState.setCurrentPhase(TurnoGioco.TurnPhase.MENU);
                    break;

                case MENU:
                    boolean continueMenu = true;
                    while (continueMenu) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        AnsiConsole.out().print("\033[H\033[2J");
                        AnsiConsole.out().flush();

                        // Stampa del menu principale
                        OutputUtils.println("\n————————————————————————————————————————————————", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);
                        OutputUtils.println("                MENU' PRINCIPALE                 ", OutputUtils.ANSI_BRIGHT_GREEN, OutputUtils.ANSI_BOLD);
                        OutputUtils.println("————————————————————————————————————————————————", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);
                        OutputUtils.println("1) Visualizza Obiettivo", OutputUtils.ANSI_WHITE, OutputUtils.ANSI_BOLD, OutputUtils.ANSI_BLINK);
                        OutputUtils.println("2) Visualizza Territori e Continenti Controllati", OutputUtils.ANSI_WHITE, OutputUtils.ANSI_BOLD, OutputUtils.ANSI_BLINK);
                        OutputUtils.println("3) Visualizza Carte Collezionate", OutputUtils.ANSI_WHITE, OutputUtils.ANSI_BOLD, OutputUtils.ANSI_BLINK);
                        OutputUtils.println("4) Attacca", OutputUtils.ANSI_WHITE, OutputUtils.ANSI_BOLD, OutputUtils.ANSI_BLINK);
                        OutputUtils.println("5) Sposta Armate", OutputUtils.ANSI_WHITE, OutputUtils.ANSI_BOLD, OutputUtils.ANSI_BLINK);
                        OutputUtils.println("6) Termina Turno", OutputUtils.ANSI_WHITE, OutputUtils.ANSI_BOLD, OutputUtils.ANSI_BLINK);
                        OutputUtils.println("7) Salva ed Esci", OutputUtils.ANSI_WHITE, OutputUtils.ANSI_BOLD, OutputUtils.ANSI_BLINK);
                        OutputUtils.println("————————————————————————————————————————————————", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);

                        OutputUtils.print("\nScelta: ", OutputUtils.ANSI_BRIGHT_GREEN, OutputUtils.ANSI_BOLD);
                        Integer[] opzioniValide = { 1, 2, 3, 4, 5, 6, 7 };
                        int scelta = InputManagerSingleton.getInstance().readIntegerUntilPossibleValue(opzioniValide);

                        switch (scelta) {
                            case 1:
                                visualizzaObiettivo(giocatore);                                
                                break;
                            
                            case 2:
                                visualizzaTerritoriEContinentiControllati(giocatore, gioco);
                                break;

                            case 3:
                                visualizzaCarteCollezionate(giocatore);
                                break;

                            case 4:
                            	attaccoGiocatore(giocatore, gioco);
                                if (!gioco.isPartitaInCorso()) {
                                    return false; // Gioco terminato
                                }
                                break;
                            
                            case 5:
                            	try {
                                    spostamentoArmate(giocatore);
                                    turnState.setTurnoTerminato(true);
                                    continueMenu = false;
                                    OutputUtils.println("\nTurno di " + giocatore.getNome().toUpperCase()
                                        + " terminato dopo lo spostamento delle armate.", OutputUtils.ANSI_BRIGHT_YELLOW, OutputUtils.ANSI_BOLD);
                                    FileServiceImpl.getInstance().writeLog("Turno di " + giocatore.getNome().toUpperCase()
                                        + " terminato dopo lo spostamento delle armate.");
                                    FileServiceImpl.getInstance().writeLog(""); // Riga vuota per separare le azioni
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    AnsiConsole.out().print("\033[H\033[2J");
                                    AnsiConsole.out().flush();
                                } catch (Exception e) {
                                    System.out.println("Errore durante lo spostamento delle armate: " + e.getMessage());
                                }
                                if (!gioco.isPartitaInCorso()) {
                                    return false; // Game over
                                }
                                break;

                            case 6:
                                turnState.setTurnoTerminato(true);
                                continueMenu = false;

                                AnsiConsole.out().print("\033[H\033[2J");
                                AnsiConsole.out().flush();

                                OutputUtils.println("\n════════════════════════════", OutputUtils.ANSI_BRIGHT_RED, OutputUtils.ANSI_BOLD);
                                OutputUtils.println("TURNO DI " + giocatore.getNome().toUpperCase() + " TERMINATO.", OutputUtils.ANSI_BRIGHT_RED, OutputUtils.ANSI_BOLD);
                                OutputUtils.println("════════════════════════════", OutputUtils.ANSI_BRIGHT_RED, OutputUtils.ANSI_BOLD);
                                System.out.println();

                                // Logging del turno terminato
                                FileServiceImpl.getInstance().writeLog("Turno di " + giocatore.getNome().toUpperCase() + " terminato.");
                                FileServiceImpl.getInstance().writeLog(""); // Riga vuota per separare le azioni

                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                AnsiConsole.out().print("\033[H\033[2J");
                                AnsiConsole.out().flush();
                                break;

                            case 7:
                                salvaEEsci(gioco);
                                return false;
                        }
                    }
                    break;
                
                default:
                    // Se la fase non è riconosciuta, termina il turno
                    turnState.setTurnoTerminato(true);
                    break;
            }
            // Dopo ogni fase, verifica se il gioco è terminato
            if (!gioco.isPartitaInCorso()) {
                return false;
            }
        }

        // Resetta lo stato del turno per il prossimo giocatore
        gioco.setCurrentTurnState(new TurnoGioco());
        return true;
    }

    /**
     * Simula il lancio di un certo numero di dadi.
     *
     * @param numDadi il numero di dadi da lanciare.
     * @return una lista dei risultati ottenuti, ordinati in ordine decrescente.
     */
    private List<Integer> lanciaDadi(int numDadi) {
        Random random = new Random();
        List<Integer> risultati = new ArrayList<>();
        for (int i = 0; i < numDadi; i++) {
            risultati.add(random.nextInt(6) + 1); // Lancia un dado (valore da 1 a 6)
        }
        risultati.sort(Collections.reverseOrder()); // Ordina i risultati in ordine decrescente
        return risultati;
    }

    /**
     * Mostra all'utente il proprio obiettivo di gioco.
     *
     * @param giocatore il giocatore corrente.
     */
    private void visualizzaObiettivo(Giocatore giocatore) {
        AnsiConsole.out().print("\033[H\033[2J");
        AnsiConsole.out().flush();

        OutputUtils.println("\n————————————————", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);
        OutputUtils.println("IL TUO OBIETTIVO", OutputUtils.ANSI_BRIGHT_GREEN, OutputUtils.ANSI_BOLD);
        OutputUtils.println("————————————————\n", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);

        CartaObiettivo obiettivo = giocatore.getObiettivo();
        if (obiettivo != null) {
            OutputUtils.println(obiettivo.getDescrizione() + "\n", OutputUtils.ANSI_BOLD);
        } else {
            OutputUtils.println("Nessun obiettivo assegnato.\n", OutputUtils.ANSI_BOLD);
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Visualizza i territori e i continenti controllati dal giocatore.
     *
     * @param giocatore il giocatore corrente.
     * @param gioco     l'oggetto Gioco corrente.
     */
    private void visualizzaTerritoriEContinentiControllati(Giocatore giocatore, Gioco gioco) {
        AnsiConsole.out().print("\033[H\033[2J");
        AnsiConsole.out().flush();

        OutputUtils.println("\n———————————————————————————————————————————————", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);
        OutputUtils.println("TERRITORI E CONTINENTI CONTROLLATI DA " + giocatore.getNome().toUpperCase(), OutputUtils.ANSI_BRIGHT_GREEN, OutputUtils.ANSI_BOLD);
        OutputUtils.println("———————————————————————————————————————————————\n", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);

        // Visualizza i territori controllati
        OutputUtils.println("Territori Controllati:", OutputUtils.ANSI_BRIGHT_CYAN, OutputUtils.ANSI_BOLD);
        int indiceTerritorio = 1;
        for (Territorio territorio : giocatore.getTerritori_controllati()) {
            OutputUtils.println(indiceTerritorio + ") " + territorio.getNome() + " con " + territorio.getNumeroArmate() + " armate", OutputUtils.ANSI_BOLD);
            indiceTerritorio++;
        }

        // Calcola e visualizza i continenti controllati
        List<Continente> continentiControllati = getContinentiControllati(giocatore, gioco.getMappa().getContinenti());
        if (!continentiControllati.isEmpty()) {
            OutputUtils.println("\nContinenti Controllati:", OutputUtils.ANSI_BRIGHT_CYAN, OutputUtils.ANSI_BOLD);
            for (Continente continente : continentiControllati) {
                OutputUtils.println("- " + continente.getNome(), OutputUtils.ANSI_BOLD);
            }
        } else {
            OutputUtils.println("\nNon controlli completamente nessun continente.", OutputUtils.ANSI_BRIGHT_CYAN, OutputUtils.ANSI_BOLD);
        }

        System.out.println();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restituisce la lista dei continenti controllati completamente dal giocatore.
     *
     * @param giocatore  il giocatore da verificare.
     * @param continenti la lista dei continenti nella mappa.
     * @return una lista dei continenti controllati.
     */
    private List<Continente> getContinentiControllati(Giocatore giocatore, List<Continente> continenti) {
        List<Continente> continentiControllati = new ArrayList<>();
        for (Continente continente : continenti) {
            boolean possiedeTuttiITerritori = true;
            for (Territorio territorio : continente.getTerritori()) {
                if (!territorio.getGiocatore().equals(giocatore)) {
                    possiedeTuttiITerritori = false;
                    break;
                }
            }
            if (possiedeTuttiITerritori) {
                continentiControllati.add(continente);
            }
        }
        return continentiControllati;
    }

    /**
     * Mostra al giocatore le carte collezionate.
     *
     * @param giocatore il giocatore corrente.
     */
    private void visualizzaCarteCollezionate(Giocatore giocatore) {
        AnsiConsole.out().print("\033[H\033[2J");
        AnsiConsole.out().flush();

        OutputUtils.println("\n—————————————————————————", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);
        OutputUtils.println("LE TUE CARTE COLLEZIONATE", OutputUtils.ANSI_BRIGHT_GREEN, OutputUtils.ANSI_BOLD);
        OutputUtils.println("—————————————————————————\n", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);

        List<Carta> carte = giocatore.getCarte();
        if (carte.isEmpty()) {
            OutputUtils.println("Non hai nessuna carta al momento.\n", OutputUtils.ANSI_BOLD);
        } else {
            for (int i = 0; i < carte.size(); i++) {
                Carta carta = carte.get(i);
                OutputUtils.println((i + 1) + ") " + carta.getTipo() + (carta.getTerritorio() != null ? " - " + carta.getTerritorio().getNome() : ""), OutputUtils.ANSI_BOLD);
            }
            System.out.println();
        }

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Calcola il numero di continenti controllati completamente dal giocatore.
     *
     * @param giocatore  il giocatore da verificare.
     * @param continenti la lista dei continenti nella mappa.
     * @return il numero di continenti controllati.
     */
    private int calcolaNumeroContinentiControllati(Giocatore giocatore, List<Continente> continenti) {
        int numeroContinentiControllati = 0;
        for (Continente continente : continenti) {
            boolean possiedeTuttiITerritori = true;
            for (Territorio territorio : continente.getTerritori()) {
                if (!territorio.getGiocatore().equals(giocatore)) {
                    possiedeTuttiITerritori = false;
                    break;
                }
            }
            if (possiedeTuttiITerritori) {
                numeroContinentiControllati++;
            }
        }
        return numeroContinentiControllati;
    }

    /**
     * Calcola il numero di armate bonus per i continenti controllati.
     *
     * @param giocatore  il giocatore corrente.
     * @param continenti la lista dei continenti.
     * @return il numero totale di armate bonus.
     */
    private int calcolaArmateContinenti(Giocatore giocatore, List<Continente> continenti) {
        int armateBonus = 0;

        for (Continente continente : continenti) {
            boolean possiedeTuttiITerritori = true;
            for (Territorio territorio : continente.getTerritori()) {
                if (!territorio.getGiocatore().equals(giocatore)) {
                    possiedeTuttiITerritori = false;
                    break;
                }
            }

            if (possiedeTuttiITerritori) {
                switch (continente.getNome()) {
                    case "Oceania":
                        armateBonus += 2;
                        break;

                    case "Europa":
                        armateBonus += 5;
                        break;

                    case "America del Sud":
                        armateBonus += 2;
                        break;

                    case "America del Nord":
                        armateBonus += 5;
                        break;

                    case "Africa":
                        armateBonus += 3;
                        break;

                    case "Asia":
                        armateBonus += 7;
                        break;
                }
            }
        }
        return armateBonus;
    }

    /**
     * Gestisce la fase di distribuzione delle armate per il giocatore.
     *
     * @param giocatore il giocatore corrente.
     * @param turnState lo stato corrente del turno.
     * @param gioco     l'oggetto Gioco corrente.
     */
    private void distribuzioneArmate(Giocatore giocatore, TurnoGioco turnState, Gioco gioco) {
        int armateDaDistribuire = turnState.getArmateDaDistribuire();

        // Pulizia dello schermo
        AnsiConsole.out().print("\033[H\033[2J");
        AnsiConsole.out().flush();

        // Verifica delle combinazioni valide di carte
        boolean haCombinazioneValida = giocatoreService.possiedeAlmenoUnaCombinazioneValida(giocatore);

        // Questo solo se sono passati almeno 3 turni, poichè prima è impossibile che un giocatore abbia almeno 3 carte
        if (gioco.getRoundCount() > 3) {
            if(haCombinazioneValida) {
                OutputUtils.printTurnHeader(giocatore);
                OutputUtils.println("\nHai una combinazione di 3 carte. Vuoi scambiarle per ottenre armate aggiuntive? (S/N)", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);
                OutputUtils.print("\nScelta: ", OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BOLD);
                String rispostaScambio = InputManagerSingleton.getInstance().readString();
                if (rispostaScambio.equalsIgnoreCase("s")) {
                    int armateDaCarte = giocatoreService.scambiaCartePerArmate(giocatore, gioco);
                    armateDaDistribuire += armateDaCarte;
                    turnState.setArmateDaDistribuire(armateDaDistribuire);
                    turnState.setArmateTotali(turnState.getArmateTotali() + armateDaCarte);
                    // Logging dello scambio di carte
                    FileServiceImpl.getInstance().writeLog("Giocatore " + giocatore.getNome().toUpperCase() + "scambia carte e riceve altre " + armateDaCarte + "armate.");
                }
            } else if (giocatore.getCarte().size() >= 3) {
                OutputUtils.printTurnHeader(giocatore);
                OutputUtils.println("\nHai 3 o più carte, ma nessuna combinazione valida per lo scambio.", OutputUtils.ANSI_BRIGHT_YELLOW, OutputUtils.ANSI_BOLD);
                try {
                    Thread.sleep(3500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                OutputUtils.printTurnHeader(giocatore);
                OutputUtils.println("\nNon hai abbastanza carte collezionabili per lo scambio.\n", OutputUtils.ANSI_BRIGHT_YELLOW, OutputUtils.ANSI_BOLD);
                try {
                    Thread.sleep(3500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // Distribuzione delle armate
        while (armateDaDistribuire > 0) {
            // Pulizia dello schermo
            AnsiConsole.out().print("\033[H\033[2J");
            AnsiConsole.out().flush();
            
            // Stampa dei messaggi aggiornati
            OutputUtils.printTurnHeader(giocatore);
            System.out.println();

            // Stampa delle informazioni sulle armate
            OutputUtils.println(giocatore.getNome().toUpperCase() + " riceve " + turnState.getArmateTotali() 
                + " armate (Territori: " + turnState.getNumeroTerritori() + ", Continenti: "
                + turnState.getNumeroContinenti() + ").\n", OutputUtils.ANSI_BRIGHT_GREEN, OutputUtils.ANSI_BOLD);

            // Informazioni sulle armate da distribuire
            OutputUtils.print("Hai", OutputUtils.ANSI_BOLD);
            OutputUtils.print(" " + armateDaDistribuire, OutputUtils.ANSI_BRIGHT_CYAN, OutputUtils.ANSI_BOLD);
            OutputUtils.println(" armate da distribuire.", OutputUtils.ANSI_BOLD);

            // Elenco dei territori controllati
            for (int j = 0; j < giocatore.getTerritori_controllati().size(); j++) {
                Territorio territorio = giocatore.getTerritori_controllati().get(j);
                OutputUtils.print((j + 1) + ") " + territorio.getNome(), OutputUtils.ANSI_BOLD);
                OutputUtils.println(" (Armate attuali: " + territorio.getNumeroArmate() + ")", OutputUtils.ANSI_BRIGHT_CYAN, OutputUtils.ANSI_BOLD);
            }

            // Selezione del territorio per la distribuzione
            OutputUtils.print("\nScelta: ", OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BOLD);
            int indiceTerritorio = InputManagerSingleton.getInstance().readIntegerUntilPossibleValue(IntStream
                .rangeClosed(1, giocatore.getTerritori_controllati().size()).boxed().toArray(Integer[]::new)) - 1;
            System.out.println();

            if (indiceTerritorio >= 0 && indiceTerritorio < giocatore.getTerritori_controllati().size()) {
                Territorio territorioSelezionato = giocatore.getTerritori_controllati().get(indiceTerritorio);
                territorioSelezionato.aggiungiArmate(1);
                giocatore.incrementaTotaleArmate(1);
                FileServiceImpl.getInstance().writeLog("Giocatore " + giocatore.getNome().toUpperCase() + " ha posizionato 1 armata su " + territorioSelezionato.getNome());
                armateDaDistribuire--;
                turnState.setArmateDaDistribuire(armateDaDistribuire);
            } else {
                OutputUtils.println("\nTerritorio non valido. Riprovare", OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
            }
        }

        // Logging finale per separare le azioni
        FileServiceImpl.getInstance().writeLog("");
    }
    
    /**
     * Permette al giocatore di selezionare un territorio da cui attaccare.
     * Il territorio deve avere almeno 2 armate.
     *
     * @param giocatore il giocatore corrente.
     * @return il territorio selezionato, o null se non ci sono territori
     *         disponibili.
     */
    private Territorio selezionaTerritorioPerAttacco(Giocatore giocatore) {
        // Filtra i territori controllati con almeno 2 armate
        List<Territorio> territoriAttaccabili = giocatore.getTerritori_controllati().stream()
            .filter(t -> t.getNumeroArmate() >= 2).collect(Collectors.toList());

        if (territoriAttaccabili.isEmpty()) {
            OutputUtils.println("Non hai territori con abbastanza armate per attaccare.", OutputUtils.ANSI_BOLD);
            return null;
        }

        // Mostra la lista dei territori disponibili per l'attacco
        for (int i = 0; i < territoriAttaccabili.size(); i++) {
            OutputUtils.print(i + 1 + ") " + territoriAttaccabili.get(i).getNome(), OutputUtils.ANSI_BOLD);
            OutputUtils.println(" (" + territoriAttaccabili.get(i).getNumeroArmate() + " armate)", OutputUtils.ANSI_BRIGHT_CYAN, OutputUtils.ANSI_BOLD);
        }

        // Chiede al giocatore di selezionare un territorio
        OutputUtils.print("\nScelta: ", OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BOLD);
        int indiceTerritorio = InputManagerSingleton.getInstance().readIntegerUntilPossibleValue(
            IntStream.rangeClosed(1, territoriAttaccabili.size()).boxed().toArray(Integer[]::new)) - 1;

        return territoriAttaccabili.get(indiceTerritorio);
    }

    /**
     * Chiede al giocatore quante armate vuole spostare nel territorio conquistato.
     *
     * @param territorioAttaccante il territorio da cui spostare le armate.
     * @param armateMinime         il numero minimo di armate da spostare.
     * @return il numero di armate da spostare.
     */
    private int scegliQuanteArmateSpostare(Territorio territorioAttaccante, int armateMinime) {
        int armateMassime = territorioAttaccante.getNumeroArmate() - 1;

        OutputUtils.print("Quante armate vuoi spostare? (Minimo " + armateMinime + ", massimo " + armateMassime + "): ", OutputUtils.ANSI_BOLD);

        int armateDaSpostare;
        do {
            armateDaSpostare = InputManagerSingleton.getInstance().readInteger();
            if (armateDaSpostare < armateMinime || armateDaSpostare > armateMassime) {
                OutputUtils.print("\nNumero di armate non valido. Inserisci un numero tra ", OutputUtils.ANSI_BOLD);
                OutputUtils.print(armateMinime + " e " + armateMassime + ": ", OutputUtils.ANSI_BRIGHT_CYAN, OutputUtils.ANSI_BOLD);
            }
        } while (armateDaSpostare < armateMinime || armateDaSpostare > armateMassime);

        return armateDaSpostare;
    }
    
    /**
     * Gestisce la fase di attacco del giocatore.
     *
     * @param giocatore il giocatore corrente.
     * @param gioco     l'oggetto Gioco corrente.
     */
    private void attaccoGiocatore(Giocatore giocatore, Gioco gioco) {
        // Pulizia schermo
        AnsiConsole.out().print("\033[H\033[2J");
        AnsiConsole.out().flush();

        boolean attaccoTerminato = false;

        while (!attaccoTerminato) {
            // Seleziona territorio di partenza per l'attacco
            OutputUtils.println("\nSeleziona il territorio da cui vuoi attaccare:", OutputUtils.ANSI_BRIGHT_RED, OutputUtils.ANSI_BOLD);
            Territorio territorioAttaccante = selezionaTerritorioPerAttacco(giocatore);
            System.out.println();

            if (territorioAttaccante == null) {
                System.out.println("Non hai più territori con armate sufficienti per attaccare.");
                break;
            }

            // Logging dell'attacco
            FileServiceImpl.getInstance().writeLog("Giocatore " + giocatore.getNome().toUpperCase() + " attacca dal territorio " + territorioAttaccante.getNome());

            // Selezione del territorio da attaccare
            List<Territorio> territoriAttaccabili = territorioAttaccante.getTerritoriAdiacenti().stream()
                .filter(t -> !t.getGiocatore().equals(giocatore) && t.getNumeroArmate() > 0)
                .collect(Collectors.toList());

            if (territoriAttaccabili.isEmpty()) {
                OutputUtils.print("Non ci sono territori adiacenti attaccabili. Vuoi selezionare un altro territorio? (S/N): ", OutputUtils.ANSI_BOLD);
                String risposta = InputManagerSingleton.getInstance().readString();
                if (risposta.equalsIgnoreCase("n")) {
                    break; // Esce dalla fase di attacco se il giocatore decide di non selezionare un altro territorio
                } else {
                    continue; // Se vuole selezionare un altro territorio, continua il ciclo
                }
            }

            // Stampa dei territori attaccabili e selezione
            OutputUtils.println("Territori adiacenti di " + territorioAttaccante.getNome() + ":", OutputUtils.ANSI_BRIGHT_RED, OutputUtils.ANSI_BOLD);
            for (int i = 0; i < territoriAttaccabili.size(); i++) {
                Territorio t = territoriAttaccabili.get(i);
                OutputUtils.print((i + 1) + ") " + t.getNome(), OutputUtils.ANSI_BOLD);
                OutputUtils.println(" (Giocatore: " + t.getGiocatore().getNome() + ", Armate: " + t.getNumeroArmate() + ")", OutputUtils.ANSI_BRIGHT_CYAN, OutputUtils.ANSI_BOLD);
            }

            // Seleziona il territorio da attaccare
            OutputUtils.print("\nScelta: ", OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BOLD);
            int indiceTerritorioDifensore = InputManagerSingleton.getInstance().readIntegerUntilPossibleValue(
                IntStream.rangeClosed(1, territoriAttaccabili.size()).boxed().toArray(Integer[]::new)) - 1;
            System.out.println();
            Territorio territorioDifensore = territoriAttaccabili.get(indiceTerritorioDifensore);

            // Logging dell'attacco al territorio selezionato
            FileServiceImpl.getInstance().writeLog("Giocatore " + giocatore.getNome().toUpperCase() + " attacca il territorio "
                + territorioDifensore.getNome() + " posseduto da "
                + territorioDifensore.getGiocatore().getNome().toUpperCase());
            FileServiceImpl.getInstance().writeLog("");

            // Determinazione del numero di dadi per l'attacco e la difesa
            int maxDadiAttacco = Math.min(territorioAttaccante.getNumeroArmate() - 1, 3);
            OutputUtils.print("Attaccante, quante armate vuoi usare per attaccare? (1-" + maxDadiAttacco + "): ", OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
            int dadiAttacco = InputManagerSingleton.getInstance().readIntegerUntilPossibleValue(IntStream.rangeClosed(1, maxDadiAttacco).boxed().toArray(Integer[]::new));
            // Il difensore deve usare lo stesso numero di dadi, o il massimo possibile se ha meno armate
            int dadiDifesa = Math.min(dadiAttacco, territorioDifensore.getNumeroArmate());
            OutputUtils.println("Il difensore userà " + dadiDifesa + " dadi per difendersi.", OutputUtils.ANSI_BLUE, OutputUtils.ANSI_BOLD);

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    
     // Esecuzione dell'attacco
        boolean territorioConquistato = attacca(giocatore, gioco, territorioAttaccante, territorioDifensore,
                dadiAttacco, dadiDifesa);

        // Se il territorio è stato conquistato, gestisce lo spostamento delle armate
        if (territorioConquistato) {
            int armateMinimeDaSpostare = 1;
            int armateMassimeDaSpostare = territorioAttaccante.getNumeroArmate() - 1;
            armateMinimeDaSpostare = Math.min(armateMinimeDaSpostare, armateMassimeDaSpostare);

            int armateSpostate = scegliQuanteArmateSpostare(territorioAttaccante, armateMinimeDaSpostare);
            territorioAttaccante.rimuoviArmate(armateSpostate);
            territorioDifensore.setArmate(armateSpostate);

            // Logging dello spostamento delle armate nel territorio conquistato
            FileServiceImpl.getInstance()
                    .writeLog("Giocatore " + giocatore.getNome().toUpperCase() + " ha spostato " + armateSpostate
                            + " armate da " + territorioAttaccante.getNome() + " a " + territorioDifensore.getNome()
                            + " dopo averlo conquistato.");
            FileServiceImpl.getInstance().writeLog(""); // Riga vuota per separare le azioni

            // Verifica la vittoria
            if (verificaVittoria(giocatore, gioco)) {
                return; // Esce dal metodo se il gioco è finito
            }
        }

        // Chiede se il giocatore vuole continuare ad attaccare
        boolean inputValido = false;
        while (!inputValido) {
            OutputUtils.print("\nVuoi continuare ad attaccare? (S/N): ", OutputUtils.ANSI_CYAN,
                    OutputUtils.ANSI_BOLD);
            String risposta = InputManagerSingleton.getInstance().readString();
            if (risposta.equalsIgnoreCase("s")) {
                inputValido = true; // Continua ad attaccare
            } else if (risposta.equalsIgnoreCase("n")) {
                inputValido = true;
                attaccoTerminato = true;
            } else {
                OutputUtils.println("\nInput non valido. Inserisci 'S' per sì o 'N' per no.", OutputUtils.ANSI_RED,
                        OutputUtils.ANSI_BOLD);
            }
          }
       }
    }
    
    /**
     * Esegue l'attacco tra due territori.
     *
     * @param giocatore            il giocatore che attacca.
     * @param gioco                l'oggetto Gioco corrente.
     * @param territorioAttaccante il territorio da cui parte l'attacco.
     * @param territorioDifensore  il territorio che viene attaccato.
     * @param dadiAttacco          il numero di dadi usati dall'attaccante.
     * @param dadiDifesa           il numero di dadi usati dal difensore.
     * @return true se il territorio è stato conquistato, false altrimenti.
     */
    private boolean attacca(Giocatore giocatore, Gioco gioco, Territorio territorioAttaccante,
            Territorio territorioDifensore, int dadiAttacco, int dadiDifesa) {
        // Stampa lo stato iniziale
        System.out.println();
        OutputUtils.println("Attacco dal territorio " + territorioAttaccante.getNome() + " (armate: "
                + territorioAttaccante.getNumeroArmate() + ")", OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
        OutputUtils.println("Difesa del territorio " + territorioDifensore.getNome() + " (armate: "
                + territorioDifensore.getNumeroArmate() + ")", OutputUtils.ANSI_BLUE, OutputUtils.ANSI_BOLD);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // L'attaccante lancia i dadi
        List<Integer> risultatiAttacco = lanciaDadi(dadiAttacco);
        System.out.println();
        OutputUtils.println("L'attaccante ha lanciato i dadi: " + risultatiAttacco, OutputUtils.ANSI_RED,
                OutputUtils.ANSI_BOLD);

        // Il difensore lancia i dadi
        List<Integer> risultatiDifesa = lanciaDadi(dadiDifesa);
        OutputUtils.println("Il difensore ha lanciato i dadi: " + risultatiDifesa, OutputUtils.ANSI_BLUE,
                OutputUtils.ANSI_BOLD);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Logging dei risultati dei dadi
        FileServiceImpl.getInstance().writeLog("L'attaccante ha lanciato i dadi: " + risultatiAttacco);
        FileServiceImpl.getInstance().writeLog("Il difensore ha lanciato i dadi: " + risultatiDifesa);

        // Confronto dei dadi
        int armatePerseAttaccante = 0;
        int armatePerseDifensore = 0;

        for (int i = 0; i < Math.min(risultatiAttacco.size(), risultatiDifesa.size()); i++) {
            if (risultatiAttacco.get(i) > risultatiDifesa.get(i)) {
                armatePerseDifensore++;
            } else {
                armatePerseAttaccante++;
            }
        }
        
     // Aggiornamento delle armate sui territori
        territorioAttaccante.rimuoviArmate(armatePerseAttaccante);
        territorioDifensore.rimuoviArmate(armatePerseDifensore);

        OutputUtils.println(
                "\nRisultato dell'attacco: " + armatePerseAttaccante + " armate perse dall'attaccante, "
                        + armatePerseDifensore + " armate perse dal difensore.",
                OutputUtils.ANSI_BRIGHT_YELLOW, OutputUtils.ANSI_BOLD);

        // Logging del risultato dell'attacco
        FileServiceImpl.getInstance().writeLog("\nRisultato dell'attacco: " + armatePerseAttaccante
                + " armate perse dall'attaccante, " + armatePerseDifensore + " armate perse dal difensore.");
        FileServiceImpl.getInstance().writeLog("");

        // Se il difensore ha perso tutte le armate, il territorio è conquistato
        if (territorioDifensore.getNumeroArmate() == 0) {
            OutputUtils.println("\nIl territorio " + territorioDifensore.getNome() + " è stato conquistato!",
                    OutputUtils.ANSI_BRIGHT_YELLOW, OutputUtils.ANSI_BOLD);
            System.out.println();

            // Logging della conquista del territorio
            FileServiceImpl.getInstance()
                    .writeLog("Giocatore " + giocatore.getNome().toUpperCase() + " ha conquistato il territorio "
                            + territorioDifensore.getNome() + " da "
                            + territorioDifensore.getGiocatore().getNome().toUpperCase());

            Giocatore difensore = territorioDifensore.getGiocatore();
            difensore.rimuoviTerritorio(territorioDifensore);
            territorioDifensore.setGiocatore(giocatore); // Il giocatore conquista il territorio
            giocatore.aggiungiTerritorio(territorioDifensore);
            giocatore.incrementaTerritoriConquistatiNelTurno(); // Aggiunge il territorio conquistato ai territori del
                                                                // giocatore

            // Riga vuota per separare le azioni
            FileServiceImpl.getInstance().writeLog("");

            // Assegna la carta bonus SOLO SE il giocatore non l'ha già ricevuta
            if (!giocatore.getHaRicevutoCartaBonus()) {
                Carta nuovaCarta = gioco.getMazzoDiCarte().pescaCarta();
                if (nuovaCarta != null) {
                    giocatore.aggiungiCarta(nuovaCarta);
                    OutputUtils.println("Hai ricevuto una nuova carta: " + nuovaCarta.getTipo()
                            + (nuovaCarta.getTerritorio() != null ? " - " + nuovaCarta.getTerritorio().getNome() : ""),
                            OutputUtils.ANSI_GREEN, OutputUtils.ANSI_BOLD);
                    System.out.println();
                    // Log dell'evento
                    FileServiceImpl.getInstance().writeLog("Giocatore " + giocatore.getNome().toUpperCase()
                            + " ha ricevuto una carta: " + nuovaCarta.getTipo()
                            + (nuovaCarta.getTerritorio() != null ? " - " + nuovaCarta.getTerritorio().getNome() : ""));
                    FileServiceImpl.getInstance().writeLog("");
                } else {
                    OutputUtils.println("Il mazzo di carte è vuoto, non puoi ricevere una nuova carta.",
                            OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
                }
                // Imposta il flag a true per indicare che ha già ricevuto la carta bonus
                giocatore.setHaRicevutoCartaBonus(true);
            }
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Permette al giocatore di spostare armate tra territori adiacenti controllati
     * e terminare il turno.
     *
     * @param giocatore il giocatore corrente.
     */
    private void spostamentoArmate(Giocatore giocatore) {
        AnsiConsole.out().print("\033[H\033[2J");
        AnsiConsole.out().flush();

        // Mostra tutti i territori controllati dal giocatore
        List<Territorio> territoriControllati = giocatore.getTerritori_controllati();

        if (territoriControllati.isEmpty()) {
            System.out.println("Non controlli nessun territorio per spostare le armate.");
            return;
        }

        // Seleziona il territorio di partenza
        OutputUtils.println("\nSeleziona il territorio da cui vuoi spostare le armate:", OutputUtils.ANSI_BRIGHT_CYAN, OutputUtils.ANSI_BOLD);
        for (int i = 0; i < territoriControllati.size(); i++) {
            OutputUtils.print(i + 1 + ") " + territoriControllati.get(i).getNome(), OutputUtils.ANSI_BOLD);
            OutputUtils.println(" (" + territoriControllati.get(i).getNumeroArmate() + " armate)", OutputUtils.ANSI_BRIGHT_CYAN, OutputUtils.ANSI_BOLD);
        }

        OutputUtils.print("\nScelta: ", OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BOLD);
        int indicePartenza = InputManagerSingleton.getInstance().readIntegerUntilPossibleValue(
            IntStream.rangeClosed(1, territoriControllati.size()).boxed().toArray(Integer[]::new)) - 1;
        Territorio territorioPartenza = territoriControllati.get(indicePartenza);

        if (territorioPartenza.getNumeroArmate() <= 1) {
            OutputUtils.println("\nNon hai abbastanza armate per spostarle da questo territorio. Deve rimanere almeno una armata.", OutputUtils.ANSI_BRIGHT_RED, OutputUtils.ANSI_BOLD);
            return;
        }

        // Seleziona un territorio adiacente in cui spostare le armate (solo territori posseduti dal giocatore
        List<Territorio> territoriAdiacentiPosseduti = territorioPartenza.getTerritoriAdiacenti().stream()
            .filter(t -> t.getGiocatore().equals(giocatore)).collect(Collectors.toList());

        if (territoriAdiacentiPosseduti.isEmpty()) {
            OutputUtils.println("\nNon ci sono territori adiacenti controllati in cui spostare le armate.", OutputUtils.ANSI_BOLD);
            return;
        }

        OutputUtils.println("\nSeleziona il territorio di destinazione:", OutputUtils.ANSI_BRIGHT_CYAN, OutputUtils.ANSI_BOLD);
        for (int i = 0; i < territoriAdiacentiPosseduti.size(); i++) {
            OutputUtils.print(i + 1 + ") " + territoriAdiacentiPosseduti.get(i).getNome(), OutputUtils.ANSI_BOLD);
            OutputUtils.println(" (" + territoriAdiacentiPosseduti.get(i).getNumeroArmate() + " armate)", OutputUtils.ANSI_BRIGHT_CYAN, OutputUtils.ANSI_BOLD);
        }

        OutputUtils.print("\nScelta: ", OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BOLD);
        int indiceDestinazione = InputManagerSingleton.getInstance().readIntegerUntilPossibleValue(
            IntStream.rangeClosed(1, territoriAdiacentiPosseduti.size()).boxed().toArray(Integer[]::new)) - 1;
        Territorio territorioDestinazione = territoriAdiacentiPosseduti.get(indiceDestinazione);

        // Chiede quante armate spostare
        OutputUtils.print("\nQuante armate vuoi spostare? ", OutputUtils.ANSI_BOLD);
        OutputUtils.print("(Minimo 1, massimo " + (territorioPartenza.getNumeroArmate() - 1) + "): ", OutputUtils.ANSI_BRIGHT_CYAN, OutputUtils.ANSI_BOLD);
        int armateDaSpostare = InputManagerSingleton.getInstance().readIntegerUntilPossibleValue(
            IntStream.range(1, territorioPartenza.getNumeroArmate()).boxed().toArray(Integer[]::new));

        territorioPartenza.rimuoviArmate(armateDaSpostare);
        territorioDestinazione.aggiungiArmate(armateDaSpostare);

        OutputUtils.println("\nHai spostato " + armateDaSpostare + " armate da " + territorioPartenza.getNome() + " a " + territorioDestinazione.getNome() + ". Il tuo turno è concluso.", OutputUtils.ANSI_BOLD);

        // Logging dello spostamento
        FileServiceImpl.getInstance().writeLog("Giocatore " + giocatore.getNome().toUpperCase() + " ha spostato " + armateDaSpostare
            + " armate da " + territorioPartenza.getNome() + " a " + territorioDestinazione.getNome());
        FileServiceImpl.getInstance().writeLog(""); // Riga vuota per separare le azioni
    }

    /**
     * Salva lo stato corrente del gioco su un file e permette di uscire.
     *
     * @param gioco l'oggetto Gioco corrente.
     */
    private void salvaEEsci(Gioco gioco) {
        try {
            AnsiConsole.out().print("\033[H\033[2J"); 
            AnsiConsole.out().flush();

            OutputUtils.print("\nInserisci il nome del file per salvare la partita: ", OutputUtils.ANSI_BRIGHT_CYAN, OutputUtils.ANSI_BOLD);
            String filename = InputManagerSingleton.getInstance().readString();

            if (!filename.endsWith(".save")) {
                filename += ".save";
            }

            // Rinomina il file di log per corrispondere al nome del gioco salvato
            String newLogFileName = filename.replace(".save", ".log");
            String oldLogFileName = gioco.getLogFileName();
            gioco.setLogFileName(newLogFileName);
            FileServiceImpl.getInstance().renameLogFile(oldLogFileName, newLogFileName);
            FileServiceImpl.getInstance().setCurrentLogFileName(newLogFileName);

            // Salva il gioco con il nuovo nome del file di log
            FileServiceImpl.getInstance().salvaGioco(gioco, filename);

            OutputUtils.print("\n\r\n" + //
                    "  _____           _   _ _                    _            _                                                                  _ \r\n"
                    + //
                    " |  __ \\         | | (_) |                  | |          | |                                                                | |\r\n"
                    + //
                    " | |__) |_ _ _ __| |_ _| |_ __ _   ___  __ _| |_   ____ _| |_ __ _    ___ ___  _ __    ___ _   _  ___ ___ ___  ___ ___  ___ | |\r\n"
                    + //
                    " |  ___/ _` | '__| __| | __/ _` | / __|/ _` | \\ \\ / / _` | __/ _` |  / __/ _ \\| '_ \\  / __| | | |/ __/ __/ _ \\/ __/ __|/ _ \\| |\r\n"
                    + //
                    " | |  | (_| | |  | |_| | || (_| | \\__ \\ (_| | |\\ V / (_| | || (_| | | (_| (_) | | | | \\__ \\ |_| | (_| (_|  __/\\__ \\__ \\ (_) |_|\r\n"
                    + //
                    " |_|   \\__,_|_|   \\__|_|\\__\\__,_| |___/\\__,_|_| \\_/ \\__,_|\\__\\__,_|  \\___\\___/|_| |_| |___/\\__,_|\\___\\___\\___||___/___/\\___/(_)\r\n"
                    + //
                    "                                                                                                                               \r\n"
                    + //
                    "                                                                                                                               \r\n"
                    + //
                    "", OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BOLD);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gioco.setRitornaAlMenu(true);
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio della partita: " + e.getMessage());
        }
    }

}