package org.timepicker;

// stary ta klasa sobie po prostu trzyma ten stan z zegarkow, ale nie wiem jak to tam masz w glownym temacie
// wiec najwyzej sobie ogarniesz co i jak
/**
 * Time class - see comment in code
 * @author Damian
 */
public class Time {

	/**
	 * Hour
	 */
	private static int godzina;
	public static int getGodzina() {
		return godzina;
	}
	public static void setGodzina(int godzina) {
		Time.godzina = godzina;
	}
	public static int getMinuta() {
		return minuta;
	}
	public static void setMinuta(int minuta) {
		Time.minuta = minuta;
	}
	
	/**
	 * Minute
	 */
	private static int minuta;
	
	public static int getOkres_godzin() {
		return okres_godzin;
	}
	public static void setOkres_godzin(int okres_godzin) {
		Time.okres_godzin = okres_godzin;
	}
	public static int getOkres_minut() {
		return okres_minut;
	}
	public static void setOkres_minut(int okres_minut) {
		Time.okres_minut = okres_minut;
	}
	
	/**
	 * Hour period
	 */
	private static int okres_godzin;
	
	/**
	 * Minute period
	 */
	private static int okres_minut;
}
