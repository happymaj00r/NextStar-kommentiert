import java.util.ArrayList;
import java.util.UUID;


public class Set {
	private String star;
	private ArrayList<Asset> assetListe = new ArrayList<Asset>();
	private String uuid;
	
	public Set(String star){
		this.uuid = UUID.randomUUID().toString().split("-")[4];
		this.star = star;
	}
	
	public String getUUID(){
		return this.uuid;
	}
	
	public ArrayList<Asset> getAssets(){
		return assetListe;
	}
	
	public String getStar(){
		return star;
	}
	
	public void addAsset(Asset asset){
		assetListe.add(asset);
	}
}
