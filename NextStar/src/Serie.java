

public class Serie extends Produktion {
	private String episoden; //Für die anzahl der Episoden würde sich natürlich ein Integer als
							// Datentyp eignen, nur gibt es bei dem derzeitigen Umfang des Programms,
							// der gefordert wurde, keine Nötigkeit für Rechenoperationen, die mit dieser
							// Variable durchgeführt werden müssten und dehalb zur Vereinfachung der Arbeit
							// mit den Nutzereingaben stattdessen ein String gewählt wurde.
	
	public Serie(String episoden,String titel){
		super(titel);
		this.episoden = episoden;
	}
	
	public String getEpisoden(){
		return episoden;
	}
}
