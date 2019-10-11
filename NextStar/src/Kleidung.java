

public class Kleidung extends Asset {
	
	private String groeße;
	private String typ;
	
	public Kleidung(String groeße, String typ,String id){
		super(id);//Konstruktor der Klasse, von der geerbt wird, wird aufgerufen.
		this.groeße = groeße;
		this.typ = typ;
	}
	
	public String getGroeße(){
		return groeße;
	}
	
	public String getTyp(){
		return typ;
	}
}
