
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.annotation.Resource;


public class ProduktionsManager {
	
	private ArrayList<Produktion> produktionenListe = new ArrayList<Produktion>(); //Liste unserer Produktionsobjekte
	
	public ProduktionsManager() {}
	
	
	
	/*
	 * Speist vordefinierten Datensatz aus Textdokument in Datensatz ein
	 */

	public void speiseTestdatensätzeEin() throws IOException{
		/*
		 * Der BufferedReader ermoeglicht es uns die Testdatensatz-datei auszulesen.
		 */
		InputStream input = getClass().getResourceAsStream("/Testdatensatz");
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		
		/*
		 * Wir lassen die Schleife so oft laufen, wie die Anzahl der Zeilen in unserer Textdatei.
		 */
		String line;
		while((line = reader.readLine()) != null){ // Wenn die nächste zeile nicht existiert, also gleich null ist, dann unterbricht die schleife 
			String[] objektInformationen = line.split(":"); //Informationen(Attribute,Klassenname etc.) sind durch Doppelpunkte voneinander getrennt
			erstelleObjekteMitObjektinformationen(objektInformationen); //Erstellt das entsprechende Objekt aus den Informationen
		}
	}
	
	
	
	/*
	 * Gibt die Liste aller Produktionen wieder
	 */
	public ArrayList<Produktion> getProduktionen(){
		return this.produktionenListe;
	}
	
	/*
	 * Erstellt Objekte aus den Informationen, die in dem Testdatensatz enthalten sind
	 * und weist sie gleichzeitig den richtigen "Traeger"-Objekten zu. Beispiel: Assets werden von Sets getragen und
	 * Sets werden von Szenen getragen etc.
	 */
	private void erstelleObjekteMitObjektinformationen(String[] objektinformationen){
		/*
		 * Da der Testdatensatz nur fuer Test- sowie Veranschaulichungszwecken dienen soll, wurde
		 * hier nicht auf die mögliche falsche Eingabe oder Formatierung der Daten geachtet.
		 * Die Methode nimmt an, dass der Testdatensatz korrekt formatiert und besetzt ist.
		 */
		String clazz = objektinformationen[0]; //Der erste Eintrag gibt immer die Klasse des Objektes an
		switch(clazz){
		case "Produktion": 
			String titel = objektinformationen[1]; 
			String typ = objektinformationen[2]; 
			/*
			 * Der "Typ" entscheidet ueber die zu verwendende Child-Klasse der einer Produktion.
			 */
			if(typ.equalsIgnoreCase("Film")){
				String dauer = objektinformationen[3];
				Produktion produktion = new Film(dauer,titel);
				this.produktionenListe.add(produktion);
			} else {
				String episoden = objektinformationen[3];
				Produktion produktion = new Serie(episoden,titel);
				this.produktionenListe.add(produktion);
			}
			break;
		case "Szene": // Klasse ist Szene
			String ort = objektinformationen[1];
			String dauer = objektinformationen[2];
			Szene szene = new Szene(ort,dauer);
			this.produktionenListe.get(this.produktionenListe.size() - 1).addSzene(szene); //Fuegt der zuletzt hinzugefuegten Produktion die neue Szene hinzu
			break;
		case "Set": // Klasse ist Set
			String star = objektinformationen[1];
			Set set = new Set(star);
			Produktion produktion = this.produktionenListe.get(this.produktionenListe.size()-1); //Gibt die zu letzt hinzugefuegte Produktion zurueck
			produktion.getSzenen().get(produktion.getSzenen().size()-1).addSet(set); //Fuegt der zuletzt hinzugefuegten Szene das neue Set hinzu
			break;
		case "Asset": // Klasse ist Asset
			String farbe = objektinformationen[1];
			typ = objektinformationen[2];
			String id = objektinformationen[3];
			Asset asset;
			/*
			 * Hier entscheiden wir wieder daruber , welche Child-Klasse eines Assets verwendet werden soll.
			 */
			if(typ.equalsIgnoreCase("Kleidung")){
				String größe = objektinformationen[4];
				String art = objektinformationen[5];
				asset = new Kleidung(größe,art,id);
			} else if (typ.equalsIgnoreCase("Schmuck")){
				String art = objektinformationen[4];
				asset = new Schmuck(art,id);
			} else {
				String art = objektinformationen[4];
				asset = new Accessoires(art,id);
			}
			asset.setFarbe(farbe);
			produktion = this.produktionenListe.get(this.produktionenListe.size()-1);//Gibt die zu letzt hinzugefuegte Produktion zurueck
			szene = produktion.getSzenen().get(produktion.getSzenen().size()-1);//Gibt die zu letzt hinzugefuegte Szene zurueck
			szene.getSets().get(szene.getSets().size()-1).addAsset(asset); //Fuegt dem zu letzt hinzugefuegtem Set das neue Asset hinzu
			break;	
		}
	}
	
	
	/*
	 * Sucht Set mit der genannten UUID und gibt es anschließend zurück.
	 */
	/**
	 * @param uuid Die UUID von einem Set
	 * @return
	 */
	public Set getSet(String uuid){
		/*
		 * Objekte vom Typ Set werden von Objekten vom Typ Szene getragen.
		 * Objekte vom Typ Szene werden von Objekten vom Typ Produktion getragen.
		 * Deshalb wird erst durch die Liste aller Produktionen gegangen, dann 
		 * durch die Szenenlisten dieser Produktionen und anschließend durch die
		 * Setliste dieser Szenen, bei der die einzelnen Elemente auf eine Übereinstimmung
		 * der UUID getested werden.
		 */
		for(Produktion produktion : produktionenListe){
			for(Szene szene : produktion.getSzenen()){  
				for(Set set : szene.getSets()){
					if(set.getUUID().equals(uuid)) return set;
				}
			}
		}
		return null;
	}
	
	
	
	/**
	 * Prüft ob ein Set mit der genannten UUID exisitert.
	 * Gleiche Funktionsweise wie @see {@link #getSet(String)} , 
	 * nur dass ein Wahrheitswert zurückgegeben wird.
	 * 
	 * @param uuid
	 * @return
	 */
	public boolean existiertSet(String uuid){
		for(Produktion produktion : produktionenListe){
			for(Szene szene : produktion.getSzenen()){
				for(Set set : szene.getSets()){
					if(set.getUUID().equals(uuid)) return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * @param uuid
	 * @return
	 */
	public Szene getSzene(String uuid){
		/*
		 * Objekte vom Typ Szene werden von Objekten vom Typ Produktion getragen.
		 * Deshalb wird erst durch die Liste aller Produktionen gegangen, dann 
		 * durch die Szenenlisten dieser Produktionen und anschließend werden die einzelnen Elemente auf eine Übereinstimmung
		 * der UUID getestet.
		 */		
		for(Produktion produktion : produktionenListe){
			for(Szene szene : produktion.getSzenen()){
				if(szene.getUUID().equals(uuid)) return szene;
			}
		}
		return null;
	}
	
	
	
	/*
	 * Gibt eine Liste aller Assets einer Produktion wieder.
	 * Die Entscheidung für ein Parameter vom Typ Produktion und nicht
	 * wie im restlichen Programm die Übergabe eines UUID Strings wurde
	 * deshalb in diesem Fall getroffen, da 2 Möglichkeiten bereitstehen
	 * eine Produktion zu erhalten @see getProduktionenMitName u. @see getProduktion
	 */
	
	/**
	 * @param produktion 
	 * @return
	 */
	public ArrayList<Asset> getAssetsEinerProduktion(Produktion produktion){ 
		ArrayList<Asset> assetListe = new ArrayList<Asset>();
		for(Szene szene : produktion.getSzenen()){
			for(Set set : szene.getSets()){
				assetListe.addAll(set.getAssets());
			}
		}
		return assetListe;
	}
	
	
	
	/**
	 * Prüft ob ein Set mit der genannten UUID exisitert.
	 * Gleiche Funktionsweise wie @see {@link #getSzene(String)} , 
	 * nur dass ein Wahrheitswert zurückgegeben wird.
	 * 
	 * @param uuid
	 * @return
	 */
	
	public boolean existiertSzene(String uuid){
		for(Produktion produktion : produktionenListe){
			for(Szene szene : produktion.getSzenen()){
				if(szene.getUUID().equals(uuid)) return true;
			}
		}
		return false;
	}
	
	
	
	
	/*
	 * Prüft ob Asset mit ID existiert.
	 */
	
	public boolean existiertAsset(String id){
		for(Produktion produktion : produktionenListe){ 
			for(Szene szene : produktion.getSzenen()){
				for(Set set : szene.getSets()){
					for(Asset asset : set.getAssets()){
						if(asset.getId().equals(id)) return true;
					}
				}
			}
		}
		return false;
	}
	
	
	
	/**
	 * @param produktion Produktion, in der nach den richtigen Assets gesucht wird. 
	 * @param star Star des Sets
	 * @return Liste mit Assets, die in der angegebenen Produktion und im Set mit gefragtem Star
	 * enthalten sind. 
	 */
	public ArrayList<Asset> getAssetsMitStarAmSet(Produktion produktion, String star){
		/*
		 * Als erstes gehen wir durch die entsprechende Objekthierarchie hindurch, um 
		 * an die AssetListe aus denen, in der Produktion enthaltenen Sets zu kommen und
		 * anschließend zu prüfen, ob der Star des Sets aus der Liste mit dem gefragtem Star
		 * übereinstimmt. 
		 * 
		 */
		ArrayList<Asset> assetListe = new ArrayList<Asset>();
		for(Szene szene : produktion.getSzenen()){
			for(Set set : szene.getSets()){
				if(set.getStar().equals(star)){
					assetListe.addAll(set.getAssets());
				}
			}
		}
		return assetListe;
	}
	
	
	
	/**
	 * 
	 * @param asset Asset, nach dem gesucht wird.
	 * @return
	 */
	public ArrayList<Szene> getSzenenMitAsset(String id){
		ArrayList<Szene> szenenListe = new ArrayList<Szene>();
		for(Produktion produktion : produktionenListe){
			for(Szene szene : produktion.getSzenen()){		
				for(Set set : szene.getSets()){			
					for(Asset assetElement : set.getAssets()){
						if(assetElement.getId().equals(id)) szenenListe.add(szene);
					}
				}
			}
		}
		return szenenListe;
	}

	
	/*
	 * Gibt Alle Informationen von Szenen und Sets einer Produktionen wieder.
	 *
	 */
	
	public ArrayList<String> getAlleInformationenVonSzenenUndSetsEinerProduktion(Produktion produktion){
		String indent = "";	
		ArrayList<String> informationsListe = new ArrayList<String>();
		for(Szene szene : produktion.getSzenen()){ //Wir gehen durch die Szenenliste unserer Produktion hindurch
			indent = "";
			String currentIndex = "[" + produktion.getSzenen().indexOf(szene) + "] ";
			//Informationen werden in die Liste abgespeichert
			informationsListe.add(indent + currentIndex + "SZENE");
			informationsListe.add(indent + currentIndex + "UUID:" + szene.getUUID());
			informationsListe.add(indent + currentIndex + "Ort:" + szene.getOrt());
			informationsListe.add(indent + currentIndex + "Zeitdauer(min):" + szene.getZeitdauer());
			
			for(Set set : szene.getSets()){ //Wir gehen durch die Setliste des Sets hindurch
				currentIndex = "[" + szene.getSets().indexOf(set) + "] ";
				indent = "   ";
				//Informationen werden in die Liste abgespeichert
				informationsListe.add(indent + currentIndex + "SET"); 
				informationsListe.add(indent + currentIndex + "UUID:" + set.getUUID());
				informationsListe.add(indent + currentIndex + "Star:" + set.getStar());
			}
		}
		return informationsListe;
	}
	
	/*
	 * Eine große Informationsliste aller Elemente einer Produktion wird angelegt und wieder zurückgegeben.
	 */
	public ArrayList<String> getAlleInformationenEinerProduktion(Produktion produktion){
		String indent = ""; //Sorgt für die Übesichtlichkeit und stellt Hierarchien zwischen den Objekten in der Konsole dar. 
							//Die Leerzeichen werden mit jeder zusätzlich geschachtelten Schleife um 3 weitere erhöht.
		ArrayList<String> informationsListe = new ArrayList<String>(); //Die Liste mit den gefragten Informationen
		String serieOderFilm = produktion instanceof Film ? "Film" : "Serie"; //Tenärer Operator, ist das Objekt eine Instanz von einem Film, wird die Variable zu 'Film' ansonsten wird sie zu 'Serie'
		informationsListe.add(indent + "PRODUKTIONSINFORMATIONEN");
		informationsListe.add(indent + "Typ:" + serieOderFilm);
		informationsListe.add(indent + "Titel:" + produktion.getTitel() + "  UUID:" + produktion.getUUID());
		informationsListe.add(indent + "Anzahl der Szenen:" + produktion.getSzenen().size());
		
		if(serieOderFilm.equalsIgnoreCase("Film")){
			informationsListe.add(indent + "Dauer(min):" + ((Film)produktion).getZeitdauer());
		} else {
			informationsListe.add(indent + "Episoden:" + ((Serie)produktion).getEpisoden());
		}
		
		for(Szene szene : produktion.getSzenen()){ //Wir gehen durch alle Produktionen hindurch
			indent = "   ";
			String currentIndex = "[" + produktion.getSzenen().indexOf(szene) + "] "; //Gibt den Index aus der Arraylist des aktuellen Elements wieder 
			informationsListe.add(indent + currentIndex + "SZENE");
			informationsListe.add(indent + currentIndex + "UUID:" + szene.getUUID());
			informationsListe.add(indent + currentIndex + "Ort:" + szene.getOrt());
			informationsListe.add(indent + currentIndex + "Zeitdauer(min):" + szene.getZeitdauer());
			
			for(Set set : szene.getSets()){ // Wir gehen durch alle Szenen der einzelnen Produktionen durch
				currentIndex = "[" + szene.getSets().indexOf(set) + "] ";
				indent = "      ";
				informationsListe.add(indent + currentIndex + "SET");
				informationsListe.add(indent + currentIndex + "UUID:" + set.getUUID());
				informationsListe.add(indent + currentIndex + "Star:" + set.getStar());
				
				indent = "         ";
				informationsListe.addAll(getAssetInformation(set.getAssets(),indent));
			}
		}
		return informationsListe;
	}
	
	/*
	 * 
	 */

	/**
	 * Gibt alle Informationen eines Assets wieder. Die Entscheidung, hierfür eine eigene Funktion
	 * zu erstellen, fußt auf die Notwendigkeit mehrere Typecasts anwenden zu müssen und dies
	 * die Übersichtlichkeit an anderer Stelle groß einschränken könnte.
	 * 
	 * @param assetList Eine Liste von Assets
	 * @param indent Besteht aus Leerzeichen und ist für die Einrückung von Text in der Konsole notwendig.
	 * @return Eine Liste aus Informationen von den Attributen der Assets aus der assetList.
	 */
	public ArrayList<String> getAssetInformation(ArrayList<Asset> assetList,String indent){
		ArrayList<String> informationsListe = new ArrayList<String>(); //Unser späterer Rückgabewert
		for(Asset asset : assetList){ //Wir gehen durch jedes Element aus der Liste hindurch
			String currentIndex = "[" + assetList.indexOf(asset) + "] ";
			//Tenärer Operator
			//Wenn Asset eine Schmuck Instanz ist, dann "Schmuck".
			//Wenn Asset eine Kleidungs Instanz ist, dann "Kleidung".
			//Ansonsten ist es ein Accessoires.
			String typ = asset instanceof Schmuck ? "Schmuck" : asset instanceof Kleidung ? "Kleidung" : "Accessoires";

			informationsListe.add(indent + currentIndex + "ASSET:" + typ);
			informationsListe.add(indent + currentIndex + "ID:" + asset.getId());
			informationsListe.add(indent + currentIndex + "Farbe:" + asset.getFarbe());
			if(asset instanceof Schmuck){ //Asset wird zu einer der erbenden Klassen gecastet, um auf die zusätzliche Funktionalität und Information zugreifen zu können!
				Schmuck schmuck = (Schmuck) asset;
				informationsListe.add(indent + currentIndex + "Schmuckart:" + schmuck.getSchmuckart());
			} else if (asset instanceof Kleidung){ //Siehe oben
				Kleidung kleidung = (Kleidung) asset;
				informationsListe.add(indent + currentIndex + "Größe(cm):" + kleidung.getGroeße());
				informationsListe.add(indent + currentIndex + "Typ:" + kleidung.getTyp());
			} else if (asset instanceof Accessoires){ //Siehe oben
				Accessoires accessoires = (Accessoires) asset;
				informationsListe.add(indent + currentIndex + "AccessoireArt:" + accessoires.getAccessoireArt());
			}
		}
		return informationsListe;
	}
	
	
	/*
	 * Schaut nach einer Produktion mit angegebenen Titel und gibt eine Liste, da mehrere 
	 * Produktionen mit gleichem Namen bestehen können, wieder zurück.
	 */
	
	public ArrayList<Produktion> getProduktionenMitName(String name){
		ArrayList<Produktion> liste = new ArrayList<Produktion>();
		for(Produktion produktion : produktionenListe){
			if(produktion.getTitel().equals(name)){
				liste.add(produktion);
			}
		}
		return liste;
	}
	
	/*
	 * Gibt ein Produktions Object mit angegebener UUID wieder.
	 */
	
	public Produktion getProduktion(String uuid){
		for(Produktion produktion : produktionenListe){
			if(produktion.getUUID().equals(uuid)){ //Abgleich der UUID mit der über die Parameter übergebenen, wenn identich = wiedergeben.
				return produktion;
			}
		}
		return null;
	}
	
	/*
	 * Prüft ob eine Produktion mit der angegebenen UUID existiert.
	 */
	
	public boolean existiertProduktion(String uuid){
		for(Produktion produktion : produktionenListe){
			if(produktion.getUUID().equals(uuid)) return true; //Abgleich der UUID mit der über die Parameter übergebenen, wenn identich = wiedergeben.
		}
		return false;
	}
	
}
