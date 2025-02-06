package it.univaq.disim.lpo.risiko.core.service.impl;

import java.util.*;
import it.univaq.disim.lpo.risiko.core.model.Continente;
import it.univaq.disim.lpo.risiko.core.model.Mappa;
import it.univaq.disim.lpo.risiko.core.model.Territorio;
import it.univaq.disim.lpo.risiko.core.service.InizializzaPartitaException;
import it.univaq.disim.lpo.risiko.core.service.MappaService;

/**
 * Implementazione dell'interfaccia MappaService.
 * Gestisce l'inizializzazione della mappa, dei continenti, dei territori e delle loro adiacenze.
 */
public class MappaServiceImpl implements MappaService {

	private Mappa mappa;
	private List<Continente> continenti;
	
	/**
     * Costruttore che inizializza i continenti, i territori e le loro adiacenze.
     *
     * @throws InizializzaPartitaException se si verifica un errore durante l'inizializzazione.
     */
	public MappaServiceImpl() throws InizializzaPartitaException {
		this.continenti = inizializzaContinentiETerritori();
		this.mappa = new Mappa(continenti);
		impostaAdiacenze();  // Imposta le adiacenze tra i territori da sviluppare
		
		if ( continenti == null || continenti.isEmpty()) {
			throw new InizializzaPartitaException("Errore nell'inizializzazione: i continenti non possono essere null o vuoti. ");
		}
	}
  
	/**
     * Restituisce la mappa di gioco.
     *
     * @return la mappa di gioco.
     */
	@Override
	public Mappa getMappa() {
		return this.mappa;	
	}
	
	/**
     * Inizializza la mappa con i continenti forniti.
     *
     * @param continenti la lista dei continenti per inizializzare la mappa.
     * @return la mappa inizializzata.
     * @throws InizializzaPartitaException se la lista dei continenti è null o vuota.
     */
	@Override
	public Mappa inizializzaMappa(List<Continente> continenti) throws InizializzaPartitaException {
		if (continenti == null || continenti.isEmpty()) {
			throw new InizializzaPartitaException("La lista dei continenti è vuota o nulla.");
		}
		mappa.setContinenti(continenti);;
		return mappa;
	}

    /**
     * Restituisce la lista dei continenti nella mappa.
     *
     * @return la lista dei continenti.
     */
    public List<Continente> getContinenti() {
        return mappa.getContinenti();
    }

	/**
     * Restituisce tutti i territori di un continente specificato.
     *
     * @param nomeContinente il nome del continente.
     * @return la lista dei territori nel continente.
     */
	@Override
	public List<Territorio> getTuttiITerritori(String nomeContinente) {
	
		Continente continente = mappa.getContinente(nomeContinente);
		if(continente != null) {
			return continente.getTerritori();
		}
		return new ArrayList<>(); // Restituisce una lista vuota se il continente non esiste.
	}
	
	/**
     * Inizializza i continenti e i rispettivi territori.
     *
     * @return una lista di continenti inizializzati.
     */	
	@Override
	public List<Continente> inizializzaContinentiETerritori() {
		// Creazione dei continenti e dei territori.
		List<Continente> continenti = new ArrayList<>();
		
		// America Del Nord
        List<Territorio> territoriAmericaDelNord = Arrays.asList(
            new Territorio("Alaska"), new Territorio("Alberta"), new Territorio("America Centrale"),
            new Territorio("Groenlandia"), new Territorio("Territori del Nord-Ovest"),
            new Territorio("Ontario"), new Territorio("Quebec"), new Territorio("Stati Uniti Orientali"),
            new Territorio("Stati Uniti Occidentali"));
        Continente americaDelNord = new Continente("America del Nord", territoriAmericaDelNord);
        continenti.add(americaDelNord);
			
        // America Del Sud
        List<Territorio> territoriSudAmerica = Arrays.asList(
            new Territorio("Argentina"), new Territorio("Brasile"),
            new Territorio("Perù"), new Territorio("Venezuela"));
        Continente americaDelSud = new Continente("America del Sud", territoriSudAmerica);
        continenti.add(americaDelSud);

        // Europa
        List<Territorio> territoriEuropa = Arrays.asList(
            new Territorio("Islanda"), new Territorio("Scandinavia"), new Territorio("Gran Bretagna"),
            new Territorio("Europa Settentrionale"), new Territorio("Europa Occidentale"), new Territorio("Europa Meridionale"),
            new Territorio("Ucraina"));
        Continente europa = new Continente("Europa", territoriEuropa);
        continenti.add(europa);

        // Africa
        List<Territorio> territoriAfrica = Arrays.asList(
            new Territorio("Africa del Nord"), new Territorio("Egitto"), new Territorio("Congo"),
            new Territorio("Africa Orientale"), new Territorio("Africa del Sud"),
            new Territorio("Madagascar"));
        Continente africa = new Continente("Africa", territoriAfrica);
        continenti.add(africa);

        // Asia
        List<Territorio> territoriAsia = Arrays.asList(
            new Territorio("Urali"), new Territorio("Siberia"), new Territorio("Jacuzia"), new Territorio("Čita"),
            new Territorio("Kamchatka"), new Territorio("Giappone"), new Territorio("Mongolia"), new Territorio("Cina"),
            new Territorio("Medio Oriente"), new Territorio("India"), new Territorio("Siam"), new Territorio("Afghanistan"));
        Continente asia = new Continente("Asia", territoriAsia);
        continenti.add(asia);

        // Oceania
        List<Territorio> territoriOceania = Arrays.asList(
            new Territorio("Indonesia"), new Territorio("Nuova Guinea"),
            new Territorio("Australia Occidentale"), new Territorio("Australia Orientale"));
        Continente oceania = new Continente("Oceania", territoriOceania);
        continenti.add(oceania);
		
        // Associazione dei territori ai continenti
        for(Continente continente : continenti) {
        	for( Territorio territorio : continente.getTerritori()) {
        		territorio.setContinente(continente);
        	}     	
        }     
		return continenti;
	}

    /**
     * Imposta le adiacenze bidirezionali tra i territori.
     */
    private void impostaAdiacenze() {
        // Creazione di una mappa per accedere rapidamente ai territori per nome
        Map<String, Territorio> territorioMap = new HashMap<>();
        for (Continente continente : continenti) {
            for (Territorio territorio : continente.getTerritori()) {
                territorioMap.put(territorio.getNome(), territorio);
            }
        }
        
        // America del Nord
        aggiungiAdiacenzeBidirezionali(territorioMap, "Alaska", "Alberta");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Alaska", "Kamchatka");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Alaska", "Territori del Nord-Ovest");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Alberta", "Territori del Nord-Ovest");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Alberta", "Ontario");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Alberta", "Stati Uniti Occidentali");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Territori del Nord-Ovest", "Ontario");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Territori del Nord-Ovest", "Groenlandia");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Ontario", "Quebec");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Ontario", "Stati Uniti Orientali");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Ontario", "Stati Uniti Occidentali");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Ontario", "Groenlandia");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Quebec", "Stati Uniti Orientali");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Quebec", "Groenlandia");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Groenlandia", "Islanda");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Stati Uniti Occidentali", "Stati Uniti Orientali");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Stati Uniti Occidentali", "America Centrale");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Stati Uniti Orientali", "America Centrale");
        aggiungiAdiacenzeBidirezionali(territorioMap, "America Centrale", "Venezuela");

        // America del Sud
        aggiungiAdiacenzeBidirezionali(territorioMap, "Venezuela", "Brasile");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Venezuela", "Perù");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Perù", "Brasile");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Perù", "Argentina");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Brasile", "Argentina");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Brasile", "Africa del Nord");

        // Africa
        aggiungiAdiacenzeBidirezionali(territorioMap, "Africa del Nord", "Egitto");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Africa del Nord", "Congo");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Africa del Nord", "Africa Orientale");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Africa del Nord", "Europa Occidentale");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Africa del Nord", "Europa Meridionale");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Egitto", "Africa Orientale");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Egitto", "Medio Oriente");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Congo", "Africa Orientale");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Congo", "Africa del Sud");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Africa Orientale", "Madagascar");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Africa Orientale", "Africa del Sud");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Madagascar", "Africa del Sud");

        // Europa
        aggiungiAdiacenzeBidirezionali(territorioMap, "Europa Occidentale", "Europa Meridionale");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Europa Occidentale", "Europa Settentrionale");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Europa Occidentale", "Gran Bretagna");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Europa Meridionale", "Europa Settentrionale");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Europa Meridionale", "Ucraina");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Europa Meridionale", "Medio Oriente");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Europa Meridionale", "Egitto");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Europa Settentrionale", "Ucraina");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Europa Settentrionale", "Scandinavia");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Europa Settentrionale", "Gran Bretagna");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Gran Bretagna", "Islanda");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Gran Bretagna", "Scandinavia");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Scandinavia", "Islanda");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Scandinavia", "Ucraina");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Ucraina", "Urali");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Ucraina", "Afghanistan");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Ucraina", "Medio Oriente");

        // Asia
        aggiungiAdiacenzeBidirezionali(territorioMap, "Urali", "Siberia");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Urali", "Cina");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Urali", "Afghanistan");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Siberia", "Jacuzia");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Siberia", "Čita");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Siberia", "Mongolia");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Siberia", "Cina");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Jacuzia", "Čita");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Jacuzia", "Kamchatka");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Kamchatka", "Giappone");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Kamchatka", "Alaska");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Kamchatka", "Čita");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Kamchatka", "Mongolia");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Čita", "Mongolia");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Mongolia", "Giappone");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Mongolia", "Cina");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Cina", "Siam");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Cina", "India");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Cina", "Afghanistan");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Cina", "Medio Oriente");
        aggiungiAdiacenzeBidirezionali(territorioMap, "India", "Siam");
        aggiungiAdiacenzeBidirezionali(territorioMap, "India", "Medio Oriente");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Medio Oriente", "Afghanistan");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Siam", "Indonesia");

        // Oceania
        aggiungiAdiacenzeBidirezionali(territorioMap, "Indonesia", "Nuova Guinea");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Indonesia", "Australia Occidentale");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Nuova Guinea", "Australia Orientale");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Nuova Guinea", "Australia Occidentale");
        aggiungiAdiacenzeBidirezionali(territorioMap, "Australia Orientale", "Australia Occidentale");
    }

    /**
     * Metodo di supporto per aggiungere adiacenze bidirezionali tra due territori.
     *
     * @param territorioMap    la mappa contenente i nomi dei territori e i loro oggetti.
     * @param nomeTerritorio1  il nome del primo territorio.
     * @param nomeTerritorio2  il nome del secondo territorio.
     */
    private void aggiungiAdiacenzeBidirezionali(Map<String, Territorio> territorioMap, String nomeTerritorio1, String nomeTerritorio2) {
        Territorio territorio1 = territorioMap.get(nomeTerritorio1);
        Territorio territorio2 = territorioMap.get(nomeTerritorio2);

        if (territorio1 != null && territorio2 != null) {
            territorio1.aggiungiTerritorioAdiacente(territorio2);
            territorio2.aggiungiTerritorioAdiacente(territorio1);
        } else {
            System.out.println("Errore: Territori non trovati per l'adiacenza tra " + nomeTerritorio1 + " e " + nomeTerritorio2);
        }
    }

}