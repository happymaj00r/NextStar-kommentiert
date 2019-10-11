


public class Asset {
	
	private String id;
	private String farbe;
	
	public Asset(String id){
		this.id = id;
	}
	
	public String getFarbe(){
		return farbe;
	}
	
	public String getId(){
		return id;
	}
	
	public void setFarbe(String farbe){
		this.farbe = farbe;
	}
}
