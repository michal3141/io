package org.timepicker;

import java.util.HashMap;
import java.util.Map;

/**
 * Simply contains global state of settings with both time and period
 * for given name of alarm
 * @author Micha³
 *
 */
public class StateMap {
	
	/**
	 * Temporary settings. Format: name --> [hour, minute, hourPeriod, minutePeriod]
	 */
	public static Map<String, int[]> state = new HashMap<String, int[]>();
}
