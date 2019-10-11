import java.util.ArrayList;
import java.util.UUID;


public class Szene {
	
	private String uuid;
	private String ort;
	private String zeit;//Zeitdauer in minuten
	private ArrayList<Set> setList = new ArrayList<Set>();
	
	public Szene (String ort, String zeit){
		uuid = UUID.randomUUID().toString().split("-")[4];
		this.ort = ort;
		this.zeit = zeit;
	}
	
	public String getZeitdauer(){
		return zeit;
	}
	
	public String getOrt(){
		return ort;
	}
	
	public void addSet(Set set){
		this.setList.add(set);
	}
	
	
	/*
	 * Prüft ob ein Set mit angegebenen Star existiert
	 */
	
	public boolean existiertSetMitStar(String star){
		for(Set set : setList){
			if(set.getStar().equals(star)) return true;
		}
		return false;
	}
	
	public ArrayList<Set> getSets(){
		return setList;
	}
	
	public Set getSet(String star){
		for(Set set : setList){
			if(set.getStar().equals(star)) return set;
		}
		return null;
	}
	
	public String getUUID(){
		return uuid;
	}
}
