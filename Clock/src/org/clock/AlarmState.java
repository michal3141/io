package org.clock;

import android.widget.TextView;

/**
 * Alarm state class representing state of alarm
 * This is mainly for concurrent access handling
 * @author Micha³
 */
public class AlarmState {
	
	/**
	 * Text view with alarm status
	 */
	private TextView alarmStateStatus;
	
	/**
	 * true if current batch of alarms (list) is playing, false otherwise
	 */
	private boolean isListPlaying;
	
	/**
	 * true if playing is blocked due to removing or adding alarms, false otherwise
	 */
	private boolean isBlocked;
	
	/**
	 * true if the alarm is playing now, false otherwise. Note that isListPlaying may be true when isPlaying is false
	 */
	private boolean isPlaying;
	
	/**
	 * Alarm State constructor
	 * @param alarmStateStatus text view object passed to constructor
	 */
	public AlarmState(TextView alarmStateStatus) {
		this.alarmStateStatus = alarmStateStatus;
		this.isListPlaying = false;
		this.isBlocked  = false;
		this.isPlaying = false;
	}


	public TextView getAlarmStateStatus() {
		return alarmStateStatus;
	}

	public void setAlarmStateStatus(TextView alarmStateStatus) {
		this.alarmStateStatus = alarmStateStatus;
	}

	public boolean isListPlaying() {
		return isListPlaying;
	}

	public void setListPlaying(boolean isListPlaying) {
		this.isListPlaying = isListPlaying;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	
	
}
