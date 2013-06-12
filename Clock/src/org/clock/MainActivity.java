package org.clock;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.model.Alarm;
import org.model.AlarmList;
import org.parser.AlarmParser;
import org.timepicker.StateMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * Main class creating GUI, calling parsing utilities and 
 * invoking different activities
 * @author Micha³
 *
 */
public class MainActivity extends Activity {
	
	/**
	 * To switch between views
	 */
	private ViewFlipper switcher;
	
	/**
	 * For historical reasons, could be deleted as well
	 */
	private static final int REFRESH_SCREEN = 1;
	
	/**
	 * list of alarms
	 */
	public AlarmList alarmList;
	
	/**
	 * parser object using to parse goodies - I mean alarms
	 */
	public AlarmParser parser;
	
	/**
	 * button clicked to add alarm
	 */
	private Button addAlarmButton;
	
	/**
	 * button clicked to remove alarm
	 */
	private Button removeAlarmButton;
	
	/**
	 * button clicked to stop alarm
	 */
	private Button stopAlarmButton;
	
	/**
	 * button clicked to confirm adding alarm
	 */
	private Button confirmAddButton;
	
	/**
	 * button clicked to confirm removing alarm
	 */
	private Button confirmRemoveButton;
	
	/**
	 * button clicked to show list of alarms
	 */
	private Button printAlarmButton;
	
	/**
	 * button clicked to fill defaults when adding alarms
	 */
	private Button addAlarmDefault;
	
	/**
	 * button clicked to abort adding alarm
	 */
	private Button addAlarmAbort;
	
	/**
	 * button clicked to abort removing alarm
	 */
	private Button removeAlarmAbort;
	
	/**
	 * textEdit with name of alarm
	 */
	private EditText nameEdit;
	
	/**
	 * textEdit with name of game of alarm
	 */
	private EditText gameEdit;
	
	/**
	 * textEdit with URI (Uniform Resource Identifier) of alarm
	 */
	private EditText uriEdit;
	
	/**
	 * textEdit with cycle of alarm (in minutes)
	 */
	private EditText cycleEdit;
	
	/**
	 * textEdit with volume of alarm (from 0 to 100)
	 */
	private EditText volumeEdit;
	
	/**
	 * textEdit with name of alarm to be removed
	 */
	private EditText removeEdit;
	
	/**
	 * textView with status of alarm adding
	 */
	private TextView addAlarmStatus;
	
	/**
	 * textView with status of alarm removing
	 */
	private TextView removeAlarmStatus;
	
	/**
	 * textView with status of alarm: namely if there is alarm currently playing or not
	 */
	private TextView alarmStateStatus;
	
	
	//private MediaPlayer player;
	
	/**
	 * notifier object - notifies about playing alarm - synchronization mechanism
	 */
	private Notifier notifier;
	
	/**
	 * listener object - listens for changes and is responsible for playing alarms
	 */
	private Listener listener;
	
	/**
	 * state of an alarm - hold some concurrency related infos
	 */
	private AlarmState alarmState;
	
	/**
	 * Variable needed by notifier to send message to player thread that minute has changed
	 */
	private int currMin = new GregorianCalendar().get(Calendar.MINUTE);
	
	/**
	 * Creating flipper (switcher) object, initializing GUI, and executing our cute app
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    	switcher = (ViewFlipper) findViewById(R.id.profileSwitcher);
    	//player = new MediaPlayer();
    	//switcher.addView(View.inflate(this, R.layout.add_alarm, null), 0);
    	//switcher.addView(View.inflate(this, R.layout.remove_alarm, null), 1);
   
    	//switcher.setDisplayedChild(0);
        initGUI();
        execute();
    }

    /**
     * On stop there is nothing interesting so comment is for joke
     */
    @Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//listener.release();
	}
    
    /**
     * When destroying main activity there is media player which needs to be released.
     */
    protected void onDestroy() {
    	super.onDestroy();
    	listener.release();
    }

    /**
     * This method is invoked when period or hour/minute for some alarms has been changed manually by user
     * @param requestCode requestCode for this started activity
     * @param resultCode resultCode returned from activity
     * @param data data passed as intent
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	  if (requestCode == 1) {
    	     if (resultCode == RESULT_CANCELED) {    
    	         System.out.println("Main APP received confirmation of result..");
    	         for (String alarmName : StateMap.state.keySet()) {
    	        	 System.out.println("Changing datetime params for: " + alarmName);
    	        	 
    	        	 Alarm alarm = alarmList.findAlarmByName(alarmName);
    	        	 int[] params = StateMap.state.get(alarmName);
    	        	 
    	        	 // Setting cycle only if it is not 0...
    	        	 if (params[2] != 0 || params[3] != 0)
    	        		 alarm.setCycle(params[2] * 60 + params[3]);
    	        	 
    	        	 // Setting up date
    	        	 Calendar cal = new GregorianCalendar();
    	        	 cal.set(Calendar.HOUR_OF_DAY, params[0]);
    	        	 cal.set(Calendar.MINUTE, params[1]);
    	        	 alarm.setDate(cal);
    	        	 
    	        	 System.out.println(alarm.toString());
    	         }
    	         // clearing state
    	         StateMap.state = new HashMap<String, int[]>();
    	         // saving updated alarms
    	         parser.saveAlarms(alarmList.getAlarms(), getFilesDir() + "/alarms.xml");
    	     }
    	  }
    }//onActivityResult

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
	/**
	 * Parsing xml with alarms, setting up notifier and listener
	 */
    public void execute() {
    	Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				parser = new AlarmParser();	
		       	alarmList = new AlarmList(parser.parseAlarms(getFilesDir() + "/alarms.xml"));
		       	//alarmList.removeAll();
		       	/*alarmList.add(new Alarm("alarm1", getFilesDir() + "/circus.mp3", "WOW", 2, 
		       							new GregorianCalendar(2013, 4, 24, 12, 51), 80));
		       	alarmList.add(new Alarm("alarm2", getFilesDir() + "/adams.mp3", "TitanQuest", 2, 
		       							new GregorianCalendar(2013, 4, 24, 12, 53), 70));
		       	alarmList.printAlarms();
		       	
		       	parser.saveAlarms(alarmList.getAlarms(), getFilesDir() + "/alarms.xml");*/
			}	
			
    	});
    	t.start();
    	try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	alarmState = new AlarmState(alarmStateStatus);
    	notifier = new Notifier(currMin);
    	listener = new Listener(alarmList, getApplicationContext(), alarmState);
    	notifier.register(listener);
    	notifier.setDaemon(true);
    	notifier.start();

    }
   
	/**
	 * Initializing GUI - all stuff goes here
	 */
	private void initGUI() {
		
		addAlarmStatus = (TextView) findViewById(R.id.addAlarmStatus);
		removeAlarmStatus = (TextView) findViewById(R.id.removeAlarmStatus);
		alarmStateStatus = (TextView) findViewById(R.id.alarmStateStatus);
		
		nameEdit = (EditText) findViewById(R.id.nameEdit);
		gameEdit = (EditText) findViewById(R.id.gameEdit);
		uriEdit = (EditText) findViewById(R.id.uriEdit);
		cycleEdit = (EditText) findViewById(R.id.cycleEdit);
		volumeEdit = (EditText) findViewById(R.id.volumeEdit);
		removeEdit = (EditText) findViewById(R.id.removeEdit);
		
		confirmAddButton = (Button) findViewById(R.id.confirmAddButton);
		confirmAddButton.setOnClickListener(new View.OnClickListener() {				
			@Override
			public void onClick(View v) {
				System.out.println("Validating inputs...");
				boolean result = validateInput();
				if (result) {
					parser.saveAlarms(alarmList.getAlarms(), getFilesDir() + "/alarms.xml");
					switcher.showPrevious();
					alarmState.setBlocked(false);
				} 
			}
		});
		
		addAlarmButton = (Button) findViewById(R.id.addAlarmButton);
		addAlarmButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				if (!alarmState.isListPlaying()) {
					switcher.showNext();
					alarmState.setBlocked(true);
				} else {
					alarmStateStatus.setText("Stop current alarm before modification");
				}
			}
		});
		
		addAlarmAbort = (Button) findViewById(R.id.addAlarmAbort);
		addAlarmAbort.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				switcher.showPrevious();				
				alarmState.setBlocked(false);
			}
		});
		
		addAlarmDefault = (Button) findViewById(R.id.addAlarmDefault);
		addAlarmDefault.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				int i = 1;
				while (alarmList.getNames().contains("alarm" + i)) 
					i++;
				nameEdit.setText("alarm" + i);
				gameEdit.setText("game");
				uriEdit.setText("/sdcard/DCIM");
				cycleEdit.setText("1000");
				volumeEdit.setText("100");
			}
		});
		
		printAlarmButton = (Button) findViewById(R.id.printAlarmButton);
		printAlarmButton.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				if (!alarmState.isListPlaying()) {
					alarmState.setBlocked(true);
					alarmList.printAlarms();	
					Intent intent = new Intent(MainActivity.this, ShowAlarmsActivity.class);
					Bundle b = new Bundle();
					Collection<String> names = alarmList.getNames();
					b.putStringArray("alarms", names.toArray(new String[names.size()]));
					intent.putExtras(b);
					startActivityForResult(intent, 1);
					System.out.println("After activity");
					alarmState.setBlocked(false);
				} else {
					alarmStateStatus.setText("Stop current alarm before modification");
				}
				
			}
		});
		
		removeAlarmButton = (Button) findViewById(R.id.removeAlarmButton);
		removeAlarmButton.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				if (!alarmState.isListPlaying()) {
					alarmState.setBlocked(true);
					switcher.showNext();
					switcher.showNext();
				} else {
					alarmStateStatus.setText("Stop current alarm before modification");
				}
			}
		});
		
		confirmRemoveButton = (Button) findViewById(R.id.confirmRemoveButton);
		confirmRemoveButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				String alarmName = removeEdit.getText().toString();
				synchronized(alarmList) {
					for (Alarm alarm : alarmList.getAlarms()) {
						if (alarm.getName().equals(alarmName)) {
							alarmList.getAlarms().remove(alarm);
							parser.saveAlarms(alarmList.getAlarms(), getFilesDir() + "/alarms.xml");
							alarmState.setBlocked(false);
							switcher.showPrevious();
							switcher.showPrevious();					
						}
					}
				}
				removeAlarmStatus.setText("There is no alarm for given name to remove.");
				
			}
		});
		
		removeAlarmAbort = (Button) findViewById(R.id.removeAlarmAbort);
		removeAlarmAbort.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				alarmState.setBlocked(false);
				switcher.showPrevious();
				switcher.showPrevious();
			}
		});
		
		stopAlarmButton = (Button) findViewById(R.id.stopAlarmButton);
		stopAlarmButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				alarmState.setPlaying(false);
			}
		});
	}
	
	/**
	 * Validate user defined alarm input
	 * @return returns true if user defined alarm can be created, false otherwise
	 */
	private boolean validateInput() {
		if (!emptyField()) {
			addAlarmStatus.setText("There is at least one empty field. Please fill it.");
			return false;
		}
		String name = nameEdit.getText().toString();
		for (String alarmName : alarmList.getNames()) {
			if (name.equals(alarmName)) {
				addAlarmStatus.setText("There is already defined alarm with this name.");
				return false;
			}
		}
		String uri = uriEdit.getText().toString();
		if (!uri.endsWith(".mp3") && !uri.endsWith(".ogg") && !uri.endsWith(".wav") && !uri.endsWith(".flac")) {
			addAlarmStatus.setText("Unsupported file format. Alarm sound must be either mp3 or wav or flac or ogg file.");
			return false;
		}
		
		File f = new File(uri);
		if (!f.exists()) {
			addAlarmStatus.setText("Specified alarm sound file does not exist.");
			return false;
		}
	
		
		String game = gameEdit.getText().toString();
		long cycle = 0;
		try {
			cycle = Long.parseLong(cycleEdit.getText().toString());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			addAlarmStatus.setText("Cycle must be whole number.");
			return false;
		}
		
		if (cycle <= 0) {
			addAlarmStatus.setText("Invalid cycle for an alarm.");
			return false;
		}
		
		int volume = 0;
		try {
			volume = Integer.parseInt(volumeEdit.getText().toString());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			addAlarmStatus.setText("Volume must be whole number.");
			return false;
		}
		if (volume < 0 || volume > 100) {
			addAlarmStatus.setText("Invalid volume. Should be between 0 and 100");
			return false;
		}
		
		// If so far so good then we need to actually create an alarm
		Alarm alarm = new Alarm(name, uri, game, cycle, new GregorianCalendar(), volume);
		alarmList.add(alarm);
		
		return true;
	}
	
	/**
	 * Checking for correctness of user input - if no empty field is left
	 * @return true if no field is empty, false otherwise
	 */
	private boolean emptyField() {
		if (nameEdit.getText().toString().equals("")) return false;
		if (gameEdit.getText().toString().equals("")) return false;
		if (uriEdit.getText().toString().equals("")) return false;
		if (cycleEdit.getText().toString().equals("")) return false;
		if (volumeEdit.getText().toString().equals("")) return false;
		return true;
	}
    
}
