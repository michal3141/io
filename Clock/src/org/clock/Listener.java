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

/**
 * This is a class responsible for playing alarm using MediaPlayer
 * @author Micha³
 */
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import org.model.Alarm;
import org.model.AlarmList;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class Listener extends Activity {
	
	/**
	 * List of alarms wrapper
	 */
	private AlarmList alarmList;
	
	/**
	 * State of alarm
	 */
	private AlarmState alarmState;
	
	/**
	 * Context object to make Media Player happy
	 */
	private Context context;
	
	/**
	 * Media Player object - Android stuff...
	 */
	private MediaPlayer player;
	
	/**
	 * Listener constructor
	 * @param alarmList list of alarms
	 * @param context context to make Media Player happy
	 * @param alarmState state of alarm
	 */
	public Listener(AlarmList alarmList, Context context, AlarmState alarmState) {
		this.alarmList = alarmList;
		this.context = context;
		this.alarmState = alarmState;
		this.player = new MediaPlayer();
	}
	
	
	/**
	 * To clean up some resources
	 */
	public void release() {
		player.release();
	}
	
	/**
	 * Executing scheduled list of alarms algorithm
	 * More in pdf documentations
	 */
	public void execute() {
		Collection<Alarm> alarms = alarmList.synchronizeAlarms();
		if (!alarms.isEmpty() && !alarmState.isBlocked()) {
			//int period = 50 / alarms.size();
			//System.out.println("Period: " + period);
			alarmState.setListPlaying(true);
			synchronized(alarmList) {
				for (final Alarm alarm : alarms) {
					if (compareToMinutes(alarm.getDate(), new GregorianCalendar()) >= 0) {
						// Playing 'non-expired' alarms (with time not older than current minute)
						System.out.println(alarm.getUri());	
						
						runOnUiThread(new Runnable() {
							public void run() {
								displayToast(context, alarm);
							}
						});				
					
						playSound(context, Uri.parse(alarm.getUri()),
								alarm.getVolume() / 100.0, true);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								alarmState.getAlarmStateStatus().setText(
										"Currently played alarm: "
												+ alarm.getName());
							}
						});
						while (alarmState.isPlaying()) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						playSound(context, Uri.parse(alarm.getUri()),
								alarm.getVolume() / 100.0, false);
					}
				}
			}
			alarmState.setListPlaying(false);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					alarmState.getAlarmStateStatus().setText("No alarm is running.");
				}					
			});
		}
	}


	/**
	 * Displays Toast representing alarm
	 * @param context - applicationContext
	 * @param alarm - alarm object
	 */
	private void displayToast(Context context, final Alarm alarm) {
		String name = alarm.getName();
		String game = alarm.getGame();
		Toast.makeText(context, 
				"Playing alarm: " + name + " game: " + game,
				Toast.LENGTH_LONG).show();
	}
	
	/**
	 * Utility to compare two dates with respect to minutes
	 * @param date passed calendar object representing data of alarm
	 * @param gregorianCalendar current date
	 * @return -1 if date is smaller than gregorianCalendar with respect to minutes, 0 if equal, 1 otherwise
	 */
	private int compareToMinutes(Calendar date, GregorianCalendar gregorianCalendar) {
		gregorianCalendar.set(Calendar.SECOND, 0);
		gregorianCalendar.set(Calendar.MILLISECOND, 0);
		
		return date.compareTo(gregorianCalendar);
	}

	/**
	 * Plays sound calling Media Player API
	 * @param context context to make Media Player happy
	 * @param alert URI to alarm
	 * @param volume volume from 0 to 100
	 * @param alarm if true than starting playing, finishing playing otherwise
	 */
	private void playSound(Context context, Uri alert, double volume, Boolean alarm) {
	    try {
	        final AudioManager audioManager = (AudioManager) context
	                .getSystemService(Context.AUDIO_SERVICE);
	        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0
	                && alarm == true) {
				player.reset();
				player.setAudioStreamType(AudioManager.STREAM_ALARM);
	            player.setDataSource(context, alert);
	            player.setLooping(true);
	            float fVolume = (float) volume;
	            player.setVolume(fVolume, fVolume);
	            player.prepare();
	            player.start();
	            
	            alarmState.setPlaying(true);
	        } else {
	            player.stop();
	            alarmState.setPlaying(false);
	        }
	    } catch (Exception e) {
	        System.out.println("OOPS Something went terribly wrong");
	    }
	}

	public AlarmState getAlarmState() {
		return alarmState;
	}

	public void setAlarmState(AlarmState alarmState) {
		this.alarmState = alarmState;
	}
	
	
}
