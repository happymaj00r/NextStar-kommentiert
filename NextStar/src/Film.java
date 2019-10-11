

public class Film extends Produktion {
	private String zeitdauer; 
	
	public Film (String zeitdauer, String titel){
		super(titel);
		this.zeitdauer = zeitdauer;
	}
	
	public String getZeitdauer(){
		return zeitdauer;
	}
	
}
