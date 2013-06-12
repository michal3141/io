package org.clock;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ShowAlarmsActivity class - shows alarms names in pretty list 
 * and when user clicks the list he/she is enabled to change period, hour
 * or minute of an alarm
 * @author Micha³
 *
 */
public class ShowAlarmsActivity extends ListActivity {
	
	/**
	 * Setting result for calling activity
	 */
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("Show Alarms Activity DONE...");
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent); 
		finish();
	}
	
	/**
	 * @param savedInstanceState - Android Bundle object
	 * Displays alarms and waits for clicks on list
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 
		Bundle b = getIntent().getExtras();
		String[] alarms = b.getStringArray("alarms");
 
		setListAdapter(new ArrayAdapter<String>(this, R.layout.show_alarm, alarms));
 
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
 
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
			    Toast.makeText(getApplicationContext(),
				((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			    
			    String alarmName = ((TextView) view).getText().toString();
			    Intent intent = new Intent(ShowAlarmsActivity.this, org.timepicker.MainActivity.class);
			    intent.putExtra("alarmName", alarmName);
				startActivity(intent);
			}
		});
 
	}
}
