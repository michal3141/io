package org.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Vector;

/**
 * Representing collection of alarms and exposing some utilities on this collection
 * @author Micha³
 *
 */
public class AlarmList implements Serializable {
	/**
	 * 1L
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Vector of alarms
	 */
	private Collection<Alarm> alarms = new Vector<Alarm>();
	
	/**
	 * AlarmList constructor
	 * @param alarms collection of alarms objects
	 */
	public AlarmList(Collection<Alarm> alarms) {
		this.alarms = alarms;
	}
	
	/**
	 * Adding alarm to alarms list
	 * @param alarm alarm
	 */
	public void add(Alarm alarm) {
		alarms.add(alarm);
	}
	
	/**
	 * Removing alarm from alarms list
	 * @param alarm alarm
	 */
	public void remove(Alarm alarm) {
		alarms.remove(alarm);
	}
	
	/**
	 * Removing all alarms from list. Making it clean and neat.
	 */
	public void removeAll() {
		alarms = new Vector<Alarm>();
	}
	
	/**
	 * Printing alarms to LogCat for debugging reasons
	 */
	public void printAlarms() {
		System.out.println("Currently defined alarms: ");
		for (Alarm alarm: alarms) {
			System.out.println(alarm.toString());		
		}
	}

	/**
	 * Finding alarm by name
	 * @param name name of an alarm
	 * @return alarm object which was found
	 */
	public Alarm findAlarmByName(String name) {
		for (Alarm alarm : alarms) {
			if (alarm.getName().equals(name))
				return alarm;
		}
		return null;
	}
	
	/**
	 * Finding collections of alarms by game
	 * @param game game
	 * @return collections of alarms for that particular game
	 */
	public Collection<Alarm> findAlarmByGame(String game) {
		Collection<Alarm> result = new Vector<Alarm>();
		for (Alarm alarm: alarms) {
			if (alarm.getGame().equals(game)) {
				result.add(alarm);
			}
		}
		return result;
	}

	public Collection<Alarm> getAlarms() {
		return alarms;
	}

	public void setAlarms(Collection<Alarm> alarms) {
		this.alarms = alarms;
	}
	
	/**
	 * Getting names of alarms
	 * @return
	 */
	public Collection<String> getNames() {
		Collection<String> result = new ArrayList<String>();
		for (Alarm alarm: alarms) {
			result.add(alarm.getName());
		}
		return result;
	}

	/**
	 * Looping for all alarms and getting them up to date. Main synchronization
	 * mechanism - important stuff. Don't touch if not necessary
	 */
	public Collection<Alarm> synchronizeAlarms() {
		Collection<Alarm> result = new Vector<Alarm>();
		Calendar now = new GregorianCalendar();
		for (Alarm alarm : alarms) {		
			long diff = minDiff(now, alarm.getDate());
			if (diff == 0) {
				result.add(alarm);
			} else if (diff > 0) {
				// Need for synchronization, because alarm time is outdated
				alarm.getDate().add(Calendar.MINUTE, (int) (alarm.getCycle() * (diff / alarm.getCycle() + 1)));
				if (diff % alarm.getCycle() == 0) {
					alarm.getDate().add(Calendar.MINUTE, (int) (-diff));
					result.add(alarm);
				}
			}			
		}
		
		for (Alarm alarm : result) {
			alarm.getDate().add(Calendar.MINUTE, (int) alarm.getCycle());
		}
		return result;
	}
	
	/**
	 * @param c1 first calendar
	 * @param c2 second calendar
	 * @return returning difference in minutes between given calendars 
	 */
	public long minDiff(Calendar c1, Calendar c2) {
		return (c1.getTimeInMillis() - c2.getTimeInMillis()) / 60000;
	}
}
