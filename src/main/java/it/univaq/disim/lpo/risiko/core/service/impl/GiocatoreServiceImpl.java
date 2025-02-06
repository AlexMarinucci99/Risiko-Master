package it.univaq.disim.lpo.risiko.core.service.impl;

import java.util.*;
import java.util.stream.Collectors;
import org.fusesource.jansi.AnsiConsole;

import it.univaq.disim.lpo.risiko.core.InputManagerSingleton;
import it.univaq.disim.lpo.risiko.core.model.Carta;
import it.univaq.disim.lpo.risiko.core.model.Giocatore;
import it.univaq.disim.lpo.risiko.core.model.Gioco;
import it.univaq.disim.lpo.risiko.core.model.Mappa;
import it.univaq.disim.lpo.risiko.core.model.Territorio;
import it.univaq.disim.lpo.risiko.core.service.GiocatoreService;
import it.univaq.disim.lpo.risiko.core.utils.OutputUtils;

/**
 * Implementazione dei servizi relativi ai giocatori.
 */
public class GiocatoreServiceImpl implements GiocatoreService {

    private final Random random;
    private List<String> coloriDisponibili = new ArrayList<>(Arrays.asList("rosso", "blu", "verde", "giallo", "nero", "bianco"));

    /**
     * Costruttore della classe.
     * Inizializza l'oggetto Random per l'utilizzo nei metodi.
     */
    public GiocatoreServiceImpl() {
        this.random = new Random();
    }

    /**
     * Crea una lista di giocatori chiedendo i nomi agli utenti.
     *
     * @param numeroGiocatori il numero di giocatori da creare.
     * @return una lista di giocatori creati.
     */
    @Override
    public List<Giocatore> creaGiocatori(int numeroGiocatori) {
        List<Giocatore> giocatori = new ArrayList<>();

        for (int i = 1; i <= numeroGiocatori; i++) {
            OutputUtils.print("\nGiocatore " + i + ", ", OutputUtils.ANSI_BOLD);
            OutputUtils.print("inserisci il tuo nome: ", OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BOLD);
            String nome = InputManagerSingleton.getInstance().readString();

            while (nome == null || nome.trim().isEmpty()) {
                OutputUtils.println("Nome non valido. Per favore, inserisci un nome valido: " + i + ",", OutputUtils.ANSI_BOLD);
                nome = InputManagerSingleton.getInstance().readString();
            }

            // Crea un nuovo giocatore con il nome inserito
            Giocatore giocatore = new Giocatore(nome, numeroGiocatori, new ArrayList<>(), numeroGiocatori);
            giocatori.add(giocatore);
        }

        return giocatori;
    }

    /**
     * Effettua un lancio di dado simulando un numero tra 1 e 6.
     *
     * @return il risultato del lancio del dado.
     */
    private int lancioDado() {
        return random.nextInt(6) + 1;
    }

    /**
     * Determina l'ordine di gioco lanciando i dadi per ciascun giocatore.
     *
     * @param giocatori la lista dei giocatori.
     * @return una lista di giocatori ordinata in base ai risultati dei dadi.
     */
    @Override
    public List<Giocatore> lancioDadiPerPrimoGiocatore(List<Giocatore> giocatori) {
        // Pulizia dello schermo
        AnsiConsole.out().print("\033[H\033[2J");
        AnsiConsole.out().flush();

        OutputUtils.println("\n———————————————————————————————————", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);
        OutputUtils.println("DETERMINAZIONE DELL'ORDINE DI GIOCO", OutputUtils.ANSI_BOLD);
        OutputUtils.println("———————————————————————————————————", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);
        System.out.println();

        // Effettua il lancio del dado per ogni giocatore
        for (Giocatore giocatore : giocatori) {
            int risultatoDado = lancioDado();
            giocatore.setRisultatoLancioDado(risultatoDado);
            OutputUtils.println(giocatore.getNome().toUpperCase() + " ha ottenuto: " + risultatoDado, OutputUtils.ANSI_BOLD);
            try {
                Thread.sleep(1000); // Pausa per migliorare l'esperienza utente
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Determina il risultato massimo e gestisce i pareggi
        List<Giocatore> vincitori = new ArrayList<>();
        int numeroMassimo = Integer.MIN_VALUE;

        // Trova il numero massimo e individua i vincitori
        for (Giocatore giocatore : giocatori) {
            int risultatoDado = giocatore.getRisultatoLancioDado();
            if (risultatoDado > numeroMassimo) {
                numeroMassimo = risultatoDado;
                vincitori.clear();
                vincitori.add(giocatore);
            } else if (risultatoDado == numeroMassimo) {
                vincitori.add(giocatore);
            }
        }

        // Gestione dei pareggi
        while (vincitori.size() > 1) {
            try {
                Thread.sleep(1500); // Pausa
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            OutputUtils.println("\nOps... c'è stato un pareggio, ripetiamo i lanci!", OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
            System.out.println();

            try {
                Thread.sleep(1500); // Pausa
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            numeroMassimo = Integer.MIN_VALUE;
            List<Giocatore> nuoviVincitori = new ArrayList<>();

            for (Giocatore vincitore : vincitori) {
                int risultatoDado = lancioDado();
                vincitore.setRisultatoLancioDado(risultatoDado);
                OutputUtils.println(vincitore.getNome().toUpperCase() + " ha ottenuto: " + risultatoDado, OutputUtils.ANSI_BOLD);

                if (risultatoDado > numeroMassimo) {
                    numeroMassimo = risultatoDado;
                    nuoviVincitori.clear();
                    nuoviVincitori.add(vincitore);
                } else if (risultatoDado == numeroMassimo) {
                    nuoviVincitori.add(vincitore);
                }
            }
            vincitori = nuoviVincitori;
        }

        // Ordina i giocatori in base al risultato del dado in ordine decrescente
        List<Giocatore> ordineGiocatori = new ArrayList<>(giocatori);
        ordineGiocatori.sort((g1, g2) -> Integer.compare(g2.getRisultatoLancioDado(), g1.getRisultatoLancioDado()));

        OutputUtils.println("\nL'ordine dei giocatori è: " + ordineGiocatori.stream().map(Giocatore::getNome).collect(Collectors.joining(", ")).toUpperCase(), OutputUtils.ANSI_BOLD);

        try {
            Thread.sleep(1500); // Pausa per migliorare l'esperienza utente
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ordineGiocatori;
    }
    
    /**
     * Aggiunge un territorio alla lista dei territori controllati dal giocatore.
     *
     * @param giocatore  il giocatore a cui aggiungere il territorio.
     * @param territorio il territorio da aggiungere.
     */
    @Override
    public void aggiungiTerritorio(Giocatore giocatore, Territorio territorio) {
        if (!giocatore.getTerritori_controllati().contains(territorio)) {
            giocatore.getTerritori_controllati().add(territorio);
            territorio.setGiocatore(giocatore); // Aggiorna il riferimento al giocatore nel territorio
        }
    }

    /**
     * Rimuove un territorio dalla lista dei territori controllati dal giocatore.
     *
     * @param giocatore  il giocatore da cui rimuovere il territorio.
     * @param territorio il territorio da rimuovere.
     */
    @Override
    public void rimuoviTerritorio(Giocatore giocatore, Territorio territorio) {
        if (giocatore.getTerritori_controllati().contains(territorio)) {
            giocatore.getTerritori_controllati().remove(territorio);
            territorio.setGiocatore(null); // Rimuove il riferimento al giocatore nel territorio
        }
    }
    
    /**
     * Aggiunge armate al giocatore.
     *
     * @param giocatore il giocatore a cui aggiungere le armate.
     * @param armate    il numero di armate da aggiungere.
     */
    @Override
    public void aggiungiArmate(Giocatore giocatore, int armate) {
        giocatore.setArmate(giocatore.getArmate() + armate);
    }

    /**
     * Rimuove armate dal giocatore.
     *
     * @param giocatore il giocatore da cui rimuovere le armate.
     * @param armate    il numero di armate da rimuovere.
     */
    @Override
    public void rimuoviArmate(Giocatore giocatore, int armate) {
        giocatore.setArmate(giocatore.getArmate() - armate);
    }
    
    /**
     * Distribuisce i territori tra i giocatori in modo casuale.
     *
     * @param giocatori la lista dei giocatori.
     * @param mappa     la mappa del gioco contenente i territori.
     */
    @Override
    public void distribuzioneTerritori(List<Giocatore> giocatori, Mappa mappa) {
        // Estrai tutti i territori dai continenti
        List<Territorio> tuttiTerritori = mappa.getContinenti().stream()
            .flatMap(continente -> continente.getTerritori().stream())
            .collect(Collectors.toList());

        // Mischia casualmente i territori
        Collections.shuffle(tuttiTerritori);

        int numeroGiocatori = giocatori.size();
        int numeroTerritori = tuttiTerritori.size();
        int territoriPerGiocatore = numeroTerritori / numeroGiocatori;
        int restantiTerritori = numeroTerritori % numeroGiocatori;

        int indiceTerritorio = 0;

        // Assegna i territori in modo uniforme tra i giocatori
        for (Giocatore giocatore : giocatori) {
            List<Territorio> territoriAssegnati = new ArrayList<>();
            for (int i = 0; i < territoriPerGiocatore; i++) {
                Territorio territorio = tuttiTerritori.get(indiceTerritorio++);
                territoriAssegnati.add(territorio);
                territorio.setGiocatore(giocatore);
            }
            giocatore.setTerritori_controllati(territoriAssegnati);
        }
        
        // Assegna i restanti territori in modo sequenziale ai giocatori
        for (int i = 0; i < restantiTerritori; i++) {
            Territorio territorio = tuttiTerritori.get(indiceTerritorio++);
            Giocatore giocatore = giocatori.get(i % numeroGiocatori);
            giocatore.aggiungiTerritorio(territorio);
            territorio.setGiocatore(giocatore);
        }
    }

    /**
     * Consente ai giocatori di scegliere i colori delle loro armate.
     *
     * @param giocatori la lista dei giocatori.
     */
    @Override
    public void scegliColoriGiocatori(List<Giocatore> giocatori) {
        try {
            Thread.sleep(5000); // Pausa per migliorare l'esperienza utente
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Pulizia dello schermo
        AnsiConsole.out().print("\033[H\033[2J");
        AnsiConsole.out().flush();
        
        OutputUtils.println("\n——————————————————————————————", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);
        OutputUtils.println("SCELTA DEL COLORE DELLE ARMATE", OutputUtils.ANSI_BOLD);
        OutputUtils.println("——————————————————————————————", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);

        Set<String> coloriScelti = new HashSet<>();

        for (Giocatore giocatore : giocatori) {
            scegliColore(giocatore, coloriScelti);
            mostraObiettivoConTimer(giocatore);
        }
    }

    /**
     * Consente a un giocatore di scegliere il colore delle sue armate.
     *
     * @param giocatore    il giocatore che deve scegliere il colore.
     * @param coloriScelti l'insieme dei colori già scelti dagli altri giocatori.
     */
    private void scegliColore(Giocatore giocatore, Set<String> coloriScelti) {
        OutputUtils.println("\n" + giocatore.getNome().toUpperCase() + ", scegli un colore per le tue armate:", OutputUtils.ANSI_BOLD);
        for (String colore : coloriDisponibili) {
            if (!coloriScelti.contains(colore.toLowerCase())) {
                String ansiColor = getAnsiColor(colore);
                OutputUtils.println("- " + capitalize(colore), ansiColor, OutputUtils.ANSI_BOLD);
            }
        }

        OutputUtils.print("\nColore scelto: ", OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BOLD);

        // Leggi la scelta del colore
        String coloreScelto = InputManagerSingleton.getInstance().readString().trim().toLowerCase();

        // Verifica la validità del colore scelto
        while (coloriScelti.contains(coloreScelto) || !coloriDisponibili.contains(coloreScelto)) {
            OutputUtils.println("\nIl colore scelto non è valido o è già stato preso. Scegli un altro colore:", OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
            OutputUtils.print("\nColore scelto: ", OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BOLD);
            coloreScelto = InputManagerSingleton.getInstance().readString().trim().toLowerCase();
        }

        // Imposta il colore scelto
        giocatore.setColore(capitalize(coloreScelto));
        coloriScelti.add(coloreScelto);
        String ansiColor = getAnsiColor(coloreScelto);
        OutputUtils.println("\n" + giocatore.getNome().toUpperCase() + " ha scelto il colore " + capitalize(coloreScelto) + ".", ansiColor, OutputUtils.ANSI_BOLD);

        // Logging della scelta del colore
        FileServiceImpl.getInstance().writeLog("Giocatore " + giocatore.getNome().toUpperCase() + " ha scelto il colore " + capitalize(coloreScelto) + ".");
        FileServiceImpl.getInstance().writeLog(""); // Riga vuota per separare le azioni
    }

    /**
     * Mostra l'obiettivo al giocatore con un timer per impedire agli altri di
     * guardare.
     *
     * @param giocatore il giocatore a cui mostrare l'obiettivo.
     */
    private void mostraObiettivoConTimer(Giocatore giocatore) {
        // Messaggio per gli altri giocatori di non guardare
        OutputUtils.println(
                "\n╔══════════════════════════════════════════════════════════════════════════════════════════╗\r\n" +
                        "║ ATTENZIONE! GLI ALTRI GIOCATORI NON DEVONO GUARDARE LO SCHERMO PER I PROSSIMI 10 SECONDI ║\r\n"
                        +
                        "╚══════════════════════════════════════════════════════════════════════════════════════════╝", OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
        
        try {
            Thread.sleep(5000); // Pausa di 5 secondi
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Messaggio di visualizzazione dell'obiettivo
        OutputUtils.print("\n" + giocatore.getNome().toUpperCase() + ", il tuo obiettivo è: ", OutputUtils.ANSI_BOLD);
        // Visualizzazione dell'obiettivo
        OutputUtils.println(giocatore.getObiettivo().getDescrizione(), OutputUtils.ANSI_BRIGHT_GREEN, OutputUtils.ANSI_BOLD);
        System.out.println();
        System.out.println();

        try {
            Thread.sleep(5000); // Pausa di 5 secondi
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Pulizia dello schermo
        AnsiConsole.out().print("\033[H\033[2J");
        AnsiConsole.out().flush();
    }
    
    /**
     * Ottiene il codice ANSI per il colore specificato.
     *
     * @param colore il nome del colore.
     * @return il codice ANSI corrispondente.
     */
    private String getAnsiColor(String colore) {
        switch (colore.toLowerCase()) {
            case "rosso":
                return OutputUtils.ANSI_RED;
            case "blu":
                return OutputUtils.ANSI_BLUE;
            case "verde":
                return OutputUtils.ANSI_GREEN;
            case "giallo":
                return OutputUtils.ANSI_YELLOW;
            case "nero":
                return OutputUtils.ANSI_BLACK;
            case "bianco":
                return OutputUtils.ANSI_WHITE;
            default:
                return OutputUtils.ANSI_RESET;
        }
    }

    /**
     * Capitalizza la prima lettera di una stringa.
     *
     * @param input la stringa da capitalizzare.
     * @return la stringa con la prima lettera maiuscola.
     */
    private String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    /**
     * Calcola il numero di armate iniziali per ciascun giocatore in base al numero
     * totale di giocatori.
     *
     * @param numeroGiocatori il numero totale di giocatori.
     * @return il numero di armate per giocatore.
     */
    @Override
    public int calcolaArmatePerGiocatore(int numeroGiocatori) {
        switch (numeroGiocatori) {
            case 2:
                return 40;
            case 3:
                return 35;
            case 4:
                return 30;
            case 5:
                return 25;
            case 6:
                return 20;
            default:
                throw new IllegalArgumentException("Numero di giocatori non valido.");
        }
    }
    
    /**
     * Verifica se il giocatore possiede almeno una combinazione valida di carte per
     * effettuare uno scambio.
     *
     * @param giocatore il giocatore da verificare.
     * @return true se possiede una combinazione valida, false altrimenti.
     */
    @Override
    public boolean possiedeAlmenoUnaCombinazioneValida(Giocatore giocatore) {
        List<Carta> carte = giocatore.getCarte();
        if (carte.size() < 3)
            return false;

        // Conta i tipi di carte
        Map<Carta.TipoCarta, Integer> conteggio = new HashMap<>();
        for (Carta carta : carte) {
            conteggio.put(carta.getTipo(), conteggio.getOrDefault(carta.getTipo(), 0) + 1);
        }

        boolean treStessoTipo = conteggio.values().stream().anyMatch(c -> c >= 3);
        boolean unoDiOgniTipo = conteggio.getOrDefault(Carta.TipoCarta.CANNONE, 0) >= 1
            && conteggio.getOrDefault(Carta.TipoCarta.FANTE, 0) >= 1
            && conteggio.getOrDefault(Carta.TipoCarta.CAVALIERE, 0) >= 1;
        boolean jollyPiuDueUguali = conteggio.getOrDefault(Carta.TipoCarta.JOLLY, 0) >= 1
            && conteggio.entrySet().stream().anyMatch(e -> e.getKey() != Carta.TipoCarta.JOLLY && e.getValue() >= 2);

        return treStessoTipo || unoDiOgniTipo || jollyPiuDueUguali;
    }

    /**
     * Verifica se una specifica combinazione di carte è valida per lo scambio.
     *
     * @param carteSelezionate le carte selezionate dal giocatore.
     * @return true se la combinazione è valida, false altrimenti.
     */
    public boolean isCombinazioneSpecificataValida(List<Carta> carteSelezionate) {
        if (carteSelezionate.size() != 3) {
            return false;
        }

        Map<Carta.TipoCarta, Long> conteggio = carteSelezionate.stream().collect(Collectors.groupingBy(Carta::getTipo, Collectors.counting()));
        
        // Controllo per 3 dello stesso tipo
        if (conteggio.containsValue(3L)) {
            return true;
        }

        // Controllo per 1 di ogni tipo
        if (conteggio.size() == 3
            && conteggio.containsKey(Carta.TipoCarta.CANNONE)
            && conteggio.containsKey(Carta.TipoCarta.FANTE)
            && conteggio.containsKey(Carta.TipoCarta.CAVALIERE)) {
            return true;
        }

        // Controllo per Jolly + 2 dello stesso tipo
        if (conteggio.getOrDefault(Carta.TipoCarta.JOLLY, 0L) == 1) {
            for (Map.Entry<Carta.TipoCarta, Long> entry : conteggio.entrySet()) {
                if (entry.getKey() != Carta.TipoCarta.JOLLY && entry.getValue() == 2L) {
                    return true;
                }
            }
        }

        return false;    
    }

    /**
     * Permette al giocatore di scambiare carte per ottenere armate aggiuntive.
     *
     * @param giocatore il giocatore che vuole scambiare le carte.
     * @param gioco     l'oggetto Gioco corrente.
     * @return il numero di armate ottenute dallo scambio.
     */
    @Override
    public int scambiaCartePerArmate(Giocatore giocatore, Gioco gioco) {
        List<Carta> carteGiocatore = giocatore.getCarte();
        if (carteGiocatore.size() < 3) {
            System.out.println("Non hai abbastanza carte per effettuare uno scambio.");
            return 0;
        }

        // Visualizzazione delle combinazioni valide
        OutputUtils.println("\nCombinazioni valide per lo scambio:", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);
        OutputUtils.println("- 3 Cannoni ➤ 4 armate", OutputUtils.ANSI_BOLD);
        OutputUtils.println("- 3 Fanti ➤ 6 armate", OutputUtils.ANSI_BOLD);
        OutputUtils.println("- 3 Cavalieri ➤ 8 armate", OutputUtils.ANSI_BOLD);
        OutputUtils.println("- 1 Cannone, 1 Fante, 1 Cavaliere ➤ 10 armate", OutputUtils.ANSI_BOLD);
        OutputUtils.println("- Jolly + 2 carte uguali ➤ 12 armate", OutputUtils.ANSI_BOLD);
        OutputUtils.println("\nInoltre riceverai 2 armate extra per ogni carta che raffigura un tuo territorio.", OutputUtils.ANSI_BRIGHT_GREEN, OutputUtils.ANSI_BOLD);

        // Visualizzazione delle carte del giocatore
        OutputUtils.println("\nLe tue carte:", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);
        for (int i = 0; i < carteGiocatore.size(); i++) {
            Carta carta = carteGiocatore.get(i);
            OutputUtils.println((i + 1) + ") " + carta.getTipo() + (carta.getTerritorio() != null ? " - " + carta.getTerritorio().getNome() : ""), OutputUtils.ANSI_BOLD);
        }

        while (true) {
            // Selezione delle carte da scambiare
            OutputUtils.println("\nSeleziona le carte da scambiare (inserisci gli indici separati da spazi)", OutputUtils.ANSI_BOLD);
            OutputUtils.print("\nScelta: ", OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BOLD);
            String input = InputManagerSingleton.getInstance().readString();
            String[] indiciString = input.trim().split("\\s+");

            if (indiciString.length != 3) {
                OutputUtils.print("\nDevi selezionare esattamente 3 carte.", OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
                System.out.println();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue; // Ripete il ciclo per un nuovo tentativo
            }
            
            List<Carta> carteSelezionate = new ArrayList<>();
            Set<Integer> uniqueIndices = new HashSet<>();

            boolean inputValido = true;

            for (String indiceStr : indiciString) {
                try {
                    int indice = Integer.parseInt(indiceStr) - 1; // Adeguamento dell'indice
                    if (indice < 0 || indice >= carteGiocatore.size()) {
                        OutputUtils.println("Indice non valido: " + indiceStr, OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
                        inputValido = false;
                        break; // Esce dal loop degli indici
                    }
                    if (!uniqueIndices.add(indice)) {
                        OutputUtils.print("\nHai selezionato la stessa carta più volte.", OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
                        inputValido = false;
                        break; // Esce dal loop degli indici
                    }
                    carteSelezionate.add(carteGiocatore.get(indice));
                } catch (NumberFormatException e) {
                    OutputUtils.println("Input non valido: " + indiceStr, OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
                    inputValido = false;
                    break; // Esce dal loop degli indici
                }
            }

            if (!inputValido) {
                System.out.println();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue; // Ripete il ciclo per un nuovo tentativo 
            }
      
            // Verifica se la combinazione è valida
            if (!isCombinazioneSpecificataValida(carteSelezionate)) {
                OutputUtils.println("\nLa combinazione di carte selezionata non è valida.", OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
                continue; // Ripete il ciclo per un nuovo tentativo
            }

            // Calcola le armate ottenute dalla combinazione valida
            int armateOttenute = calcolaArmateDaCarte(carteSelezionate, giocatore);
            if (armateOttenute > 0) {
                // Rimuovi le carte scambiate dal giocatore
                giocatore.rimuoviCarte(carteSelezionate);
                // Restituisci le carte al mazzo
                gioco.getMazzoDiCarte().restituisciCarte(carteSelezionate);
                OutputUtils.println("\nHai ottenuto " + armateOttenute + " armate dal cambio di carte.", OutputUtils.ANSI_BRIGHT_GREEN, OutputUtils.ANSI_BOLD);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Logga l'evento
                FileServiceImpl.getInstance().writeLog("Giocatore " + giocatore.getNome().toUpperCase() + " ha scambiato carte per " + armateOttenute + " armate.");
                return armateOttenute;
            } else {
                OutputUtils.println("\nLa combinazione di carte selezionata non è valida.", OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue; // Ripete il ciclo per un nuovo tentativo
            }
        }
    }
   
    /**
     * Calcola il numero di armate ottenute dallo scambio di carte.
     *
     * @param carteSelezionate le carte selezionate per lo scambio.
     * @param giocatore        il giocatore che effettua lo scambio.
     * @return il numero di armate ottenute.
     */
    private int calcolaArmateDaCarte(List<Carta> carteSelezionate, Giocatore giocatore) {
        if (!isCombinazioneSpecificataValida(carteSelezionate)) {
            return 0;
        }

        Map<Carta.TipoCarta, Integer> conteggioTipi = new HashMap<>();
        boolean haJolly = false;
        for (Carta carta : carteSelezionate) {
            if (carta.getTipo() == Carta.TipoCarta.JOLLY) {
                haJolly = true;
            } else {
                conteggioTipi.put(carta.getTipo(), conteggioTipi.getOrDefault(carta.getTipo(), 0) + 1);
            }
        }

        int armate = 0;
        if (haJolly) {
            // Jolly + due carte uguali
            for (Carta.TipoCarta tipo : Carta.TipoCarta.values()) {
                if (tipo != Carta.TipoCarta.JOLLY && conteggioTipi.getOrDefault(tipo, 0) == 2) {
                    armate = 12;
                    break;
                }
            }
        } else if (conteggioTipi.size() == 1) {
            // Tre carte dello stesso tipo
            Carta.TipoCarta tipo = conteggioTipi.keySet().iterator().next();
            int count = conteggioTipi.get(tipo);
            if (count == 3) {
                switch (tipo) {
                    case CANNONE:
                        armate = 4;
                        break;
                    case FANTE:
                        armate = 6;
                        break;
                    case CAVALIERE:
                        armate = 8;
                        break;
                    default:
                        armate = 0;
                        break;
                }
            }            
        } else if (conteggioTipi.size() == 3 && !haJolly) {
            // Una per ogni tipo
            if (conteggioTipi.containsKey(Carta.TipoCarta.CANNONE)
                && conteggioTipi.containsKey(Carta.TipoCarta.FANTE)
                && conteggioTipi.containsKey(Carta.TipoCarta.CAVALIERE)) {
                armate = 10;
            }            
        }

        // Aggiungi 2 armate per ogni carta che raffigura un territorio posseduto
        int bonusTerritori = 0;
        for (Carta carta : carteSelezionate) {
            Territorio territorio = carta.getTerritorio();
            if (territorio != null && territorio.getGiocatore().equals(giocatore)) {
                bonusTerritori += 2;
            }
        }

        armate += bonusTerritori;
        return armate;
    }
    
    /**
     * Distribuisce le armate iniziali tra i giocatori.
     *
     * @param giocatori          la lista dei giocatori.
     * @param armatePerGiocatore il numero di armate che ogni giocatore deve distribuire.
     */
    @Override
    public void distribuzioneInizialeArmate(List<Giocatore> giocatori, int armatePerGiocatore) {
        // Prima fase: Ogni giocatore posiziona una armata su ogni territorio
        for (Giocatore giocatore : giocatori) {
            for (Territorio territorio : giocatore.getTerritori_controllati()) {
                territorio.aggiungiArmate(1);
                giocatore.incrementaTotaleArmate(1);
            }
        }

        // Seconda fase: Distribuire le armate rimanenti
        for (Giocatore giocatore : giocatori) {
            int armateRimanenti = armatePerGiocatore - giocatore.getTerritori_controllati().size();
            
            while (armateRimanenti > 0) {
            	// Pulizia dello schermo
                AnsiConsole.out().print("\033[H\033[2J");
                AnsiConsole.out().flush();

                // Ristampa del titolo
                OutputUtils.println("\n———————————————————————————————————", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);
                OutputUtils.println("DISTRIBUZIONE DELLE ARMATE INIZIALI", OutputUtils.ANSI_BOLD);
                OutputUtils.println("———————————————————————————————————", OutputUtils.ANSI_BRIGHT_PURPLE, OutputUtils.ANSI_BOLD);

                OutputUtils.print("\n" + giocatore.getNome().toUpperCase() + ", hai", OutputUtils.ANSI_BOLD);
                OutputUtils.print(" " + armateRimanenti, OutputUtils.ANSI_BRIGHT_CYAN, OutputUtils.ANSI_BOLD);
                OutputUtils.println(" armate da distribuire.", OutputUtils.ANSI_BOLD);
                OutputUtils.println("\nSeleziona il territorio dove posizionare un'armata:", OutputUtils.ANSI_BOLD);

                for (int j = 0; j < giocatore.getTerritori_controllati().size(); j++) {
                    Territorio territorio = giocatore.getTerritori_controllati().get(j);
                    OutputUtils.print((j + 1) + ") " + territorio.getNome(), OutputUtils.ANSI_BOLD);
                    OutputUtils.println(" (Armate attuali: " + territorio.getNumeroArmate() + ")", OutputUtils.ANSI_BRIGHT_CYAN, OutputUtils.ANSI_BOLD);
                }

                int indiceTerritorio = -1;
                boolean territorioValido = false;

                while (!territorioValido) {
                    try {
                        OutputUtils.print("\nScelta: ", OutputUtils.ANSI_CYAN, OutputUtils.ANSI_BOLD);
                        indiceTerritorio = InputManagerSingleton.getInstance().readInteger();
                        System.out.println();

                        // Adeguamento per indice a partire da 1
                        indiceTerritorio = indiceTerritorio - 1;

                        if (indiceTerritorio >= 0 && indiceTerritorio < giocatore.getTerritori_controllati().size()) {
                            territorioValido = true;
                        } else {
                            OutputUtils.println("Territorio non valido. Riprovare", OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
                        }
                    } catch (Exception e) {
                        OutputUtils.println("Errore nella selezione del territorio. Riprovare", OutputUtils.ANSI_RED, OutputUtils.ANSI_BOLD);
                    }
                }

                Territorio territorioSelezionato = giocatore.getTerritori_controllati().get(indiceTerritorio);
                territorioSelezionato.aggiungiArmate(1);
                giocatore.incrementaTotaleArmate(1);
                armateRimanenti--;

                // Logging del posizionamento dell'armata
                FileServiceImpl.getInstance().writeLog("Giocatore " + giocatore.getNome().toUpperCase() + " posiziona 1 armata su " + territorioSelezionato.getNome());
            }
            FileServiceImpl.getInstance().writeLog(""); // Riga vuota per separare le azioni
        } 
    } 
     
}