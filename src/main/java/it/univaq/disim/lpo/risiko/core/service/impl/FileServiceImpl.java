package it.univaq.disim.lpo.risiko.core.service.impl;

import java.io.*;
import java.nio.file.*;
import it.univaq.disim.lpo.risiko.core.service.FileService;
import it.univaq.disim.lpo.risiko.core.model.Gioco;
import it.univaq.disim.lpo.risiko.core.utils.OutputUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe sngleton che implementa l'interfaccia FileService.
 * Gestisce il salvataggio/caricamento delle partite e la registrazione dei log.
 */
public class FileServiceImpl implements FileService {

    private static FileServiceImpl instance;
    private static final String SAVE_FOLDER = "saves/";
    private static final String LOG_FOLDER = "logs/";
    private String currentLogFileName;

    /**
     * Costruttore privato per il pattern sigleton.
     * Garantisce che le cartelle 'logs/' e 'save/' esistano.
     */
    private FileServiceImpl() {
        // Assicura che la directory 'logs/' esista
        Path logDir = Paths.get(LOG_FOLDER);
        if (!Files.exists(logDir)) {
            try {
                Files.createDirectories(logDir);
            } catch (IOException e) {
                System.out.println("Errore durante la creazione della cartella dei log: " + e.getMessage());

            }
        }
        // Assicura che la directory 'save/' esista
        Path saveDir = Paths.get(SAVE_FOLDER);
        if (!Files.exists(saveDir)) {
            try {
                Files.createDirectories(saveDir);
            } catch (IOException e) {
                System.out.println("Errore durante la creazione della cartella dei salvataggi:" + e.getMessage());

            }
        }
    }
    /**
     * Ottiene il prossimo numero di partita per un'identificazione univoca.
     * 
     * @return il prossimo numero di partita.
     */
    public static FileServiceImpl getInstance() {
        if (instance == null) {
            synchronized (FileServiceImpl.class) {
                if (instance == null) {
                    instance = new FileServiceImpl();

                }
            }
        }
        return instance;
    }
    /**
     * Salva lo stato del gioco su un file.
     * 
     * @param gioco l'oggetto gioco da salvare.
     * @param fileName il nome del file su cui salvare.
     * @throws IOException se si verifica un errore di I/O.
     */

    public void salvaGioco(Gioco gioco, String fileName) throws IOException {
        Path path = Paths.get(SAVE_FOLDER + fileName);
        try (FileOutputStream fileOut = new FileOutputStream(path.toFile());
                ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(gioco);
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio della partita: " + e.getMessage());
            throw e;
        }

    }
    /**
     * Carica una partita salvata da un file.
     * 
     * @param fileName il nome del file da cui caricare.
     * @return l'oggetto gioco caricato.
     * @throws IOException se si verifica un errore di I/O.
     * @throws ClassNotFoundException se la classe Gioco non viene trovata.
     */

    public Gioco caricaGioco(String fileName) throws IOException, ClassNotFoundException {
        Path path = Paths.get(SAVE_FOLDER + fileName);
        try (FileInputStream fileIn = new FileInputStream(path.toFile());
                ObjectInputStream in = new ObjectInputStream(fileIn)) {
            Gioco gioco = (Gioco) in.readObject();
            OutputUtils.println("Partita caricata da " + path.toAbsolutePath(), OutputUtils.ANSI_BRIGHT_GREEN, OutputUtils.ANSI_BOLD);
            return gioco;
        } catch (IOException | ClassNotFoundException e) {
            OutputUtils.println("Errore durante il caricamento della partita: " + e.getMessage(), OutputUtils.ANSI_BRIGHT_RED, OutputUtils.ANSI_BOLD);
            throw e;
        }
    }
    /**
     * Legge dati da un file.
     * 
     * @param fileName il nome del file da leggere.
     * @return il contenuto del file come stringa.
     */

    public String readData(String fileName) {
        Path path = Paths.get(fileName);
        try {
            return Files.readString(path);
        } catch (IOException e) {
            System.out.println("Errore durante la lettura del file: " + e.getMessage());
            return "";
        }
    }
    /**
     * Scrive dati su un file.
     * 
     * @param fileName il nome del file su cui scrivere.
     * @param data i dati da scrivere.
     */

    public void writeData(String fileName, String data) {
        Path path = Paths.get(fileName);
        try {
            Files.writeString(path, data);
            System.out.println("Dati scritti su " + path.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Errore durante la scrittura del file: " + e.getMessage());
        }
    }

    /**
     * Ottiene il prossimo numero di partita per un'identificazione univoca.
     * 
     * @return il prossimo numero di partita.
     */

    public int getNextGameNumber() {
        Path counterFile = Paths.get(LOG_FOLDER, "game_counter.txt");
        int gameNumber = 1; //Default a 1 se il file non esiste.
        if (Files.exists(counterFile)) {
            try {
                String content = Files.readString(counterFile);
                gameNumber = Integer.parseInt(content.trim()) + 1;
            } catch (IOException | NumberFormatException e) {
                System.out.println("Errore durante la lettura del contatotre delle partite: " + e.getMessage());
            }
        }
        //Scrive il nuovo numero di partita nel file.
        try {
            Files.writeString(counterFile, Integer.toString(gameNumber));
        } catch (IOException e ) {
            System.out.println("Errore durante la scrittura del contatore delle partite: " + e.getMessage());
         }
         return gameNumber;
    }
    /**
     * Imposta il nome del file di log corrente.
     * 
     * @param logFileName il nome del file di log.
     */
        
    public void  setCurrentLogFileName(String logFileName) {
        this.currentLogFileName = logFileName;
    }
    /**
     * Rinomina un file di log.
     * 
     * @param oldFileName il nome attuale del file.
     * @param newFileName il nuovo nome per il file.
     */

    public void  renameLogFile(String oldFileName, String newFileName) {
        Path oldFilePath = Paths.get(LOG_FOLDER + oldFileName);
        Path newFilePath = Paths.get(LOG_FOLDER + newFileName);

        try {
            Files.move(oldFilePath, newFilePath);
            OutputUtils.print("\nLog file rinominato in: ", OutputUtils.ANSI_BRIGHT_CYAN, OutputUtils.ANSI_BOLD);
            System.out.println(newFileName);
        } catch (IOException e) {
            System.out.println("Errore durante la rinomina del file di log: " + e.getMessage());
        }
    }
    /**
     * Scrive un'entrata nel log corrente.
     * 
     * @param data i dati da scrivere nel log.
     */

    public void writeLog(String data) {
        if (currentLogFileName == null) {
            System.out.println("Errore: nessun file di log impostato per la scrittura.");
            return;
        }
        Path logFilePath = Paths.get(LOG_FOLDER + currentLogFileName);
        try (FileWriter writer = new FileWriter(logFilePath.toFile(), true);
        BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String[] lines = data.split("\\r?\\n");
            for (String line : lines) {
                bufferedWriter.write("[" + timeStamp + "] " + line);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            System.out.println("Errore durante la scrittura del file di log: " + e.getMessage());
        }
    }


}


