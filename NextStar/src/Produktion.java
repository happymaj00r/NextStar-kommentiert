import java.util.ArrayList;
import java.util.UUID;


public class Produktion {
	private String uuid;
	private String titel;
	private String produktionsDatum;
	private ArrayList<Szene> szenenListe = new ArrayList<Szene>();
	
	public Produktion (String titel){
		this.uuid = UUID.randomUUID().toString().split("-")[4];
		this.titel = titel;
	}
	
	public String getTitel(){
		return titel;
	}
	
	public String getUUID(){
		return uuid;
	}
	
	/*
	 * Prüft ob Szene mit der UUID existiert
	 */
	public boolean hatSzene(String s){
		for(Szene szene : szenenListe){
			if(szene.getUUID() == s) return true;
		}
		return false;
	}
	
	public void addSzene(Szene szene){
		szenenListe.add(szene);
	}
	
	public ArrayList<Szene> getSzenen(){
		return szenenListe;
	}
}
