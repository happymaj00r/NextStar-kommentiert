import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class MenuHandler {
	
	private static ProduktionsManager produktionsManager; //Unser ProduktionsManager worueber wir an die Informationen unserer Objekte kommen.
	private static String[] menuEintraege = new String[10]; //Unser Array in dem unsere Menueintraege gespeichert sind
	
	public static void main(String[] args) {
		
		produktionsManager = new ProduktionsManager();
		
		/*
		 * Die Datei, die die Testdatensätze beinhaltet, könnte nicht gefunden werden, in diesem Fall
		 * soll das Programm einfach ohne Testdatensatz weitermachen und wie gehabt funktionieren.
		 */
		try {
			produktionsManager.speiseTestdatensätzeEin();
		} catch (IOException e) {
			System.out.println("[ERROR] Kein Testdatensatz gefunden!");
		}
		
		/*
		 * Zeige vordefinierte Produktionennamen und UUIDS
		 */	
		System.out.println("Vordefinierte Produktionen:");
		for(int i = 0; i < produktionsManager.getProduktionen().size(); i++){
			Produktion produktion = produktionsManager.getProduktionen().get(i);
			System.out.println("Titel: " + produktion.getTitel() + " UUID: " + produktion.getUUID());
		}	
		
		initialisiereMenüeinträge();	
		handle();
	}
	
	/*
	 * Das Array 'menüEinträge' wird mit den, im Menü genutzten Einträgen gefüllt.
	 * Wichtig ist das für die Funktion 'zeigeMenü', die dieses Array über die Konsole ausgibt.
	 */
	private static void initialisiereMenüeinträge () {
		menuEintraege[0] = "(01)   Produktion anlegen";
		menuEintraege[1] = "(02)   Szene anlegen und Produktion zuordnen";
		menuEintraege[2] = "(03)   Set anlegen und Szene zuordnen";
		menuEintraege[3] = "(04)   Asset anlegen";
		menuEintraege[4] = "(05)   Alle Assets einer Produktion anzeigen";
		menuEintraege[5] = "(06)   Alle Szenen und Set einer Produktion anzeigen";
		menuEintraege[6] = "(07)   Alle Informationen einer Produktion anzeigen";
		menuEintraege[7] = "(08)   Assets einer Produktion nach Star suchen";
		menuEintraege[8] = "(09)   Alle Szenen wo ein Asset genutzt wurde";
		menuEintraege[9] = "(10)   Beenden";
	}
	
	/*
	 * Konsolenroutine
	 */
	private static void handle(){
		while(true){ //Wiederholt die Programmroutine unendlich oft, bis das Programm über die Funktion 'beendeProgramm' beendet wird.
			System.out.println("-------------------------------------------------"); //Zur Verbesserung der Übersichtlichkeit
			zeigeMenü(); //Zeigt die Menüeinträge in der Konsole an
			System.out.println("Bitte Menueintrag auswaehlen (1-10):");
			int auswahl = warteAufEintragAuswahl(); //Wartet auf die Auswahl eines gültigen Menüeintrags.
			bearbeiteAuswahl(auswahl); //Bearbeitet die Auswahl
		}
	}
	
	
	/*
	 * Gibt das Menu in der Konsole wieder, welches im Array menuEintraege definiert ist.
	 */
	private static void zeigeMenü(){
		for(int i = 0; i < menuEintraege.length; i++){
			System.out.println(menuEintraege[i]);
		}
	}
	
	
	/*
	 * Wartet bis der Nutzer einen Menu Eintrag ausgewaehlt hat und gibt den eingegeben Wert zurück.
	 */
	private static int warteAufEintragAuswahl(){
		Scanner scanner = new Scanner(System.in);
		String nutzerEingabe = scanner.nextLine();
		int eintrag;
		try {
		    eintrag = Integer.parseInt(nutzerEingabe);
		} catch (NumberFormatException e) { //Nutzer gab keine Zahl an
			System.out.println("Fehlerhafte Eingabe: Gib eine Zahl an!");
			return -1; //
		}
		return eintrag;
	}
	
	/*
	 * Fügt die übergebende Zeichenkette dem System Clipboard hinzu.
	 * Kleine Hilfsfunktion, um die Navigation durch das Programm etwas zu vereinfachen, da
	 * UUIDS nun immer bei Erstellung eines neuen Objektes in das Clipboard gespeichert werden und 
	 * von dort beliebig irgendwo eingefügt werden können. 
	 */
	private static void addTextToClipboard(String s){
		StringSelection stringSelection = new StringSelection(s);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}
	
	
	/*
	 * Bearbeitet, den vom Benutzer ausgewählten Menüeintrag.
	 * int eintrag : den ausgewählten eintrag des Benutzers.
	 */

	private static void bearbeiteAuswahl(int eintrag){
		eintrag-=1; //Wichtig, da die Indexierung der Menueintraege, die dem Nutzer sichtbar sind, bei 1 anfangen und nicht wie im Programm bei 0.
		if(eintrag < 0) return; //Bei einer falschen Menüeintragauswahl, wird -1 zurückgegeben und hier deswegen die weitere Abarbeitung unterbrochen
		System.out.println(menuEintraege[eintrag]); //Gibt den vom Menüeintrag zugehörigen Text aus.
		Scanner scanner = new Scanner(System.in); // Scanner wird initialisiert, womit wir die Nutzereingaben auslesen werden.
		switch(eintrag){
		case 0: //Menüeintrag 1
			System.out.println("Name der Produktion:");
			String eingabe = scanner.nextLine(); //Benutzereingabe
			System.out.println("Wähle aus: 'Film' oder 'Serie':");
			String filmOderSerie = scanner.nextLine();
			Produktion produktion = null;
			/*
			 * Als nächstes wird geschaut, auf Basis der Nutzereingabe, welche Child-Klasse der Produktions-Klasse
			 * benötigt9 wird.
			 */
			if(filmOderSerie.equalsIgnoreCase("Film")){ 
				System.out.println("Dauer des Films in Minuten:");
				String dauer = scanner.nextLine();
				produktion = new Film(dauer,eingabe);//Dauer,Titel
				System.out.println("Film:" + eingabe + ":" + produktion.getUUID() + " erstellt!");
			} else if (filmOderSerie.equalsIgnoreCase("Serie")){
				System.out.println("Episodenanzahl der Serie:");
				String episodenAnzahl = scanner.nextLine();
				produktion = new Serie(episodenAnzahl,eingabe); //Episodenanzahl,Titel
				System.out.println("Serie:" + eingabe + ":" + produktion.getUUID() + " erstellt!");
			} else {
				System.out.println("Bitte wähle nur zwischen 'Film' oder 'Serie'!");
				break;
			}
			produktionsManager.getProduktionen().add(produktion);
			addTextToClipboard(produktion.getUUID()); //Wir kopieren die UUID vom eben erstellten Objekt in das
														   //Clipboard, um die Nutzerfreundlichkeit zu erhöhen.
			break;
		case 1: 
			System.out.println("Ort der Szene:");
			String ort = scanner.nextLine(); //Benutzereingabe - Ort der Szene
			System.out.println("Zeitdauer der Szene in Minuten:");
			String zeit = scanner.nextLine(); //Benutzereingabe - Zeit der Szene
			Szene szene = new Szene(ort,zeit); //Neue Szene mit denen vom Benutzer eingegebenen Parameter
			produktion = getProduktionDurchBenutzer(); //Benutzer wird aufgefordert eine Produktion zu nennen.
			if(produktion != null){ //Exisitert eine Produktion mit der UUID?
				produktion.addSzene(szene);
				System.out.println("Szene:" + szene.getUUID() + " wurde Produktion zugewiesen!");
				addTextToClipboard(szene.getUUID());
			}
			break;
		case 2:
			System.out.println("Star des Sets:");
			String star = scanner.nextLine(); //Nutzereingabe - Star des Sets
			Set set = new Set(star); //Neues Set Objekt wird erstellt
			System.out.println("Szene, welche das Set zugeordnet werden soll:");
			String uuid = scanner.nextLine(); // UUID der Szene, die das Set hinzugefügt werden soll.
			if(produktionsManager.existiertSzene(uuid)){
				szene = produktionsManager.getSzene(uuid);
				szene.addSet(set);
				addTextToClipboard(set.getUUID());
				System.out.println("Set:"+set.getUUID() + " erfolgreich zur Szene:" + szene.getUUID() + " hinzugefügt!");
			} else {
				System.out.println("Szene existiert nicht!");
			}
			break;
		case 3:
			System.out.println("Farbe:");
			String farbe = scanner.nextLine();//Nutzereingabe - Farbe des Assets
			System.out.println("Typen: 'Schmuck' 'Kleidung' 'Accessoires' ");
			String typ = scanner.nextLine();//Nutzereingabe - Typ des Assets
			System.out.println("ID:");
			String id = scanner.nextLine();
			Asset asset = null;
			
			/*
			 * Überprüfung, welcher Typ, also welche Child-Klasse der Asset-Klasse verwendet werden soll
			 */
			
			if(typ.equalsIgnoreCase("Schmuck")){ 
				System.out.println("Schmuckart:");
				String schmuckart = scanner.nextLine();
				asset = new Schmuck(schmuckart,id);
			} else if (typ.equalsIgnoreCase("Kleidung")){
				System.out.println("Größe(cm):");
				String größe = scanner.nextLine();
				System.out.println("Typ:");
				typ = scanner.nextLine();
				asset = new Kleidung(größe,typ,id);
			} else if (typ.equalsIgnoreCase("Accessoires")){
				System.out.println("Accessoireart:");
				String accessoireart = scanner.nextLine();
				asset = new Accessoires(accessoireart,id);
			} else {
				System.out.println("Typ existiert nicht!");
				break;
			}
			
			System.out.println("Set, welches das Asset zugewiesen wird:");
			uuid = scanner.nextLine();
			if(produktionsManager.existiertSet(uuid)){
				set = produktionsManager.getSet(uuid);
				asset.setFarbe(farbe);
				set.addAsset(asset);
				System.out.println("Asset:" + asset.getId() + " erstellt und zugewiesen");
			} else {
				System.out.println("Set existiert nicht!");
			}
			break;
			
		case 4:
			produktion = getProduktionDurchBenutzer(); //Der Nutzer wird aufgefordert eine Produktion zu wählen
			if(produktion == null) return; //Funktion kann null zurückgeben, falls wohlmöglich falsche Angaben getätigt wurden
			ArrayList<Asset> assetListe = produktionsManager.getAssetsEinerProduktion(produktion);//Alle Assets einer Produktion
			printList(produktionsManager.getAssetInformation(assetListe,""));//Zeige alle Assetinformationen an
			break;
		case 5:
			produktion = getProduktionDurchBenutzer(); //Der Nutzer wird aufgefordert eine Produktion zu wählen
			if(produktion == null) return;
			printList(produktionsManager.getAlleInformationenVonSzenenUndSetsEinerProduktion(produktion));
			break;
		case 6:
			produktion = getProduktionDurchBenutzer();//Der Nutzer wird aufgefordert eine Produktion zu wählen
			if(produktion == null) return;
			printList(produktionsManager.getAlleInformationenEinerProduktion(produktion));
			break;
		case 7:
			produktion = getProduktionDurchBenutzer();//Der Nutzer wird aufgefordert eine Produktion zu wählen
			if(produktion == null) return;
			System.out.println("Wähle Star, dessen Assets gesucht werden:");
			star = scanner.nextLine();
			assetListe = produktionsManager.getAssetsMitStarAmSet(produktion, star);
			printList(produktionsManager.getAssetInformation(assetListe,""));
			break;
		case 8:
			System.out.println("Wähle Asset (ID):");
			eingabe = scanner.nextLine();
			if(produktionsManager.existiertAsset(eingabe)){
				ArrayList<Szene> szenenListe = produktionsManager.getSzenenMitAsset(eingabe);
				zeigeSzenenInformationen(szenenListe);				
			} else {
				System.out.println("Asset existiert nicht!");
			}
			break;
		case 9:
			programmBeenden();
			break;
		}
	}
	
	private static void programmBeenden(){
		System.exit(0);
	}
	
	
	/*
	 * Gibt eine String Liste in der Konsole wieder.
	 */
	private static void printList(ArrayList<String> liste){
		for(String s : liste){
			System.out.println(s);
		}
	}
	
	
	/*
	 * Fuehrt den Konsolenbenutzer durch die Erfassung einer Produktion. 
	 * Produktionen koennen hierbei mit ihrer UUID oder ihrem Namen angesteuert werden.
	 * Wird eine Produktionen mit einem mehr als einmal vorkommenen Namen angesteuert, gibt
	 * die Methode dem Nutzer die Wahl zwischen diesen mit Ausgabe der UUIDS.
	 */
	
	private static Produktion getProduktionDurchBenutzer(){
		Scanner scanner = new Scanner(System.in); //Scanner für die Nutzereingabe
		System.out.println("Bitte nennen Sie den Produktionstitel oder UUID:");
		String eingabe = scanner.nextLine(); //Nutzereingabe - UUID oder NAME
		ArrayList<Produktion> produktionen = produktionsManager.getProduktionenMitName(eingabe);
		if(!produktionen.isEmpty()){ // Wenn die Liste leer ist, wurde keine Produktion mit dem Titel gefunden
			if(produktionen.size() == 1){
				return produktionen.get(0); // Es wurde nur eine Produktion gefunden
			} else { // Es wurden mehrere gefunden
					//Also lassen wir den Nutzer die richtige mit der UUID wählen.
				System.out.println("Mehrere Produktionen mit diesem Titel gefunden!");
				System.out.println("Wähle eine mit folgenden UUID´s aus:");
				for(Produktion produktion : produktionen){
					System.out.println("Produktion:" + produktion.getUUID());
				}
				System.out.println("Wähle (UUID):");
				eingabe = scanner.nextLine();
				if(produktionsManager.existiertProduktion(eingabe)) return produktionsManager.getProduktion(eingabe); //Die neu eingegeben UUID existiert und konstituiert ein Atribut einer Produktion, also wird ein Produktions Objekt wiedergegeben
			}
		} else if (produktionsManager.existiertProduktion(eingabe)) { //Es wird geschaut ob eine Produktion mit der UUID existiert, die identisch der Eingabe des Benutzers ist.
			Produktion produktion = produktionsManager.getProduktion(eingabe);
			return produktion;
		}
		System.out.println("Produktion nicht gefunden!");
		return null; // Kann null zurück geben, daher wichtig, dass darauf geachtet wird, sobald mit der Funktion gearbeitet wird.
	}
	
	
	
	/*
	 * Gibt alle Informationen der in der Liste enthaltenen Szenen wieder.
	 */
	
	private static void zeigeSzenenInformationen(ArrayList<Szene> szenenListe){
		for(Szene szene : szenenListe){ //Geht durch jedes Element in der Liste
			System.out.println("SZENE");
			System.out.println("UUID:" + szene.getUUID());
			System.out.println("Ort:" + szene.getOrt());
			System.out.println("Zeitdauer:" + szene.getZeitdauer());
		}
	}
}
