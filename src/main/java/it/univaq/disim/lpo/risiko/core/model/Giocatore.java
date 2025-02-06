package it.univaq.disim.lpo.risiko.core.model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;


public class Giocatore implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String nome;
	private int armate;
	private List<Territorio> territori_controllati;
	private CartaObiettivo obiettivo;
	private String colore;
	private int risultatoLancioDado;
	private int totaleArmate;
	private int territoriConquistatiNelTurno;
	private List<Carta> carte;
	private boolean haRicevutoCartaBonus;
	
	public Giocatore(String nome, int armate, List<Territorio> territori_controllati,int risultatoLancioDado, int totaleArmate) {
		
		this.nome = nome;
		this.armate = armate;
		this.territori_controllati = (territori_controllati != null) ? territori_controllati : new ArrayList<>();	
		this.risultatoLancioDado = risultatoLancioDado;
		this.colore = null;
		this.totaleArmate = totaleArmate;
		this.carte = new ArrayList<>();
	}

	public Giocatore(String nome, int armate, List<Territorio> territori_controllati, int totaleArmate ) {
		this(nome, armate, territori_controllati, 0, totaleArmate); // valore predefinito per risultatoLancioDado
	}
	
	
	public String getNome() {
		return this.nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public int getArmate() {
		return this.armate;
	}
	
	public void setArmate(int armate) {
		this.armate = armate;
	}
	
	public List<Territorio> getTerritori_controllati(){
		return this.territori_controllati;
	}
	
	public void setTerritori_controllati(List<Territorio> territori_controllati) {
		this.territori_controllati = territori_controllati;
	}	
	
	public CartaObiettivo getObiettivo() {
		return obiettivo;
	}
	
	public void setObiettivo(CartaObiettivo obiettivo) {
		this.obiettivo = obiettivo;
	}
	
	public String getColore() {
		return this.colore;
	}
	
	public void setColore(String colore) {
		this.colore = colore;
	}
	
	public int getRisultatoLancioDado() {
		return this.risultatoLancioDado;
	}
	
	public void setRisultatoLancioDado(int risultatoLancioDado) {
		this.risultatoLancioDado = risultatoLancioDado;
	}
	
	public int getTotaleArmate() {
		return totaleArmate;
	}
	
	public void setToataleArmate(int totaleArmate) {
		this.totaleArmate = totaleArmate;
	}
	
	public int getTerritoriConquistatiNelTurno() {
		return territoriConquistatiNelTurno;
	}
	
	public List<Carta> getCarte(){
		return carte;
	}
	
	public boolean getHaRicevutoCartaBonus() {
		return haRicevutoCartaBonus;
	}
	
	public void setHaRicevutoCartaBonus(boolean haRicevutoCartaBonus) {
		this.haRicevutoCartaBonus = haRicevutoCartaBonus;
	}
	
	//aggiungere una carta
	public void aggiungiCarta(Carta carta) {
		
		carte.add(carta);
	}
	
	//rimuove le carte
	public void rimuoviCarte(List<Carta> carteDaRimuovere) {
		
		carte.removeAll(carteDaRimuovere);
	}
	
	//incrementa territori conquistati durante il turno
	public void incrementaTerritoriConquistatiNelTurno() {
		
		this.territoriConquistatiNelTurno++;
	}
	
	//resetta i territori conquistati
	public void resetTerritoriConquistatiNelTurno() {
		
		this.territoriConquistatiNelTurno = 0;	
	}
	
	//incrementa toale armate
	public void incrementaTotaleArmate(int increment) {
		
		this.totaleArmate += increment;
	}
	
	//equals territorio 
	public Territorio getTerritorioByName(String nome) {
		
		for(Territorio territorio : territori_controllati) {
			if(territorio.getNome().equalsIgnoreCase(nome)) {
				return territorio;
			}
		}
		return null;
	}
	
	//aggiunge territorio
	public void aggiungiTerritorio(Territorio territorio) {
		
		if(!territori_controllati.contains(territorio)) {
			territori_controllati.add(territorio);
			territorio.setGiocatore(this);
		}
	}
	
	// rimuove territorio
	public void rimuoviTerritorio(Territorio territorio) {
		
		territori_controllati.remove(territorio);
	}
	
	//Equals e Hash
	public boolean equals(Object o) {
		
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Giocatore giocatore = (Giocatore) o;
		return nome != null && nome.equalsIgnoreCase(giocatore.nome);
		
	}
	
	public int hashCode() {
		
		return nome != null ? nome.toLowerCase().hashCode() : 0;
	}
	
}
	
	


