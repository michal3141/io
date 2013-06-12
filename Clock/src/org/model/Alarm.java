package org.model;

import java.util.Calendar;

/**
 * Simple POJO representing alarm object
 * @author Micha³
 *
 */
public class Alarm {

	/**
	 * name of the alarm given by the user
	 */
	private String name;
	
	/**
	 * uri for sound file (preferably mp3)
	 */
	private String uri;
	/**
	 * name of the game an alarm is designed for
	 */
	private String game;
	/**
	 * cycle of an alarm in minutes
	 */
	private long cycle;
	/**
	 * date of next alarm (including minutes and hours)
	 */
	private Calendar date;
	/**
	 * volume of an alarm from 0 to 100
	 */
	private int volume;
	
	public Alarm(String name, String uri, String game, long cycle, Calendar date, int volume) {
		super();
		this.name = name;
		this.uri = uri;
		this.game = game;
		this.cycle = cycle;
		this.date = date;
		this.volume = volume;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public long getCycle() {
		return cycle;
	}

	public void setCycle(long cycle) {
		this.cycle = cycle;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("-----------------------------\n");
		sb.append("Name: " + name + "\n");
		sb.append("URI: " + uri + "\n");
		sb.append("Game: " + game + "\n");
		sb.append("Cycle: " + cycle + "\n");
		sb.append("Volume: " + volume + "\n");
		sb.append("Year: " + date.get(Calendar.YEAR) + "\n");
		sb.append("Month: " + date.get(Calendar.MONTH) + "\n");
		sb.append("Day: " + date.get(Calendar.DAY_OF_MONTH) + "\n");
		sb.append("Hour: " + date.get(Calendar.HOUR_OF_DAY) + "\n");
		sb.append("Minute: " + date.get(Calendar.MINUTE) + "\n");
		sb.append("-----------------------------\n");
		return sb.toString();
	}
}
