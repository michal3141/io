/*
Copyright (c) 2013, Damian Kudas & Micha³ Mrowczyk
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies, 
either expressed or implied, of the FreeBSD Project.
*/
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
