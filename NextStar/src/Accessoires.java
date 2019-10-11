

public class Accessoires extends Asset {
	private String accessoireArt;
	
	public Accessoires (String accessoireArt,String id){
		super(id);//Konstruktor der Klasse, von der geerbt wird, wird aufgerufen.
		this.accessoireArt = accessoireArt;
	}
	
	public String getAccessoireArt(){
		return accessoireArt;
	}
}
