

public class Schmuck extends Asset {
	private String schmuckArt;
	
	public Schmuck(String schmuckArt,String id){
		super(id); //Konstruktor der Klasse, von der geerbt wird, wird aufgerufen.
		this.schmuckArt = schmuckArt;
	}
	
	public String getSchmuckart(){
		return schmuckArt;
	}
}
