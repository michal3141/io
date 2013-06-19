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
