package org.clock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

/**
 * Notifier class is basically a poor man way of doing something
 * which should be done using java Timer
 * @author Micha³
 *
 */
public class Notifier extends Thread {
	
	/**
	 * Holding minute value to be compared with current minute
	 */
	private int minute;
	
	/**
	 * Collection of listening listeners
	 */
	private Collection<Listener> listeners = new ArrayList<Listener>();
	
	/**
	 * Notifier constructor
	 * @param minute value of minute
	 */
	public Notifier(int minute) {
		this.minute = minute;
	}
	
	/**
	 * Registering listeners responsible for playing beautiful alarms
	 * @param t listener
	 */
	public void register(Listener t) {
		listeners.add(t);
	}
	
	/**
	 * Unregistering listeners responsible for playing beautiful alarms
	 * @param t listener
	 */
	public void unregister(Listener t) {
		listeners.remove(t);
	}
	
	/**
	 * Firing events for listeners
	 */
	public void fireEvent() {
		for (Listener listener : listeners) {
			listener.execute();
		}
	}
	
	/**
	 * Running thread which basically boils down to firing events
	 * when minute indicated by system clock is changing
	 */
	public void run() {
		while (true) {
			int minuteNow = new GregorianCalendar().get(Calendar.MINUTE);
			if (minuteNow != minute) {
				minute = minuteNow;
				fireEvent();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
