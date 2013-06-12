package org.timepicker;

import org.clock.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Class for Main drawing activity
 * @author Damian
 *
 */
public class MainActivity extends Activity{

	/**
	 * First ball
	 */
	Ball b=null;

	/**
	 * Second ball
	 */
	Ball b1= null;

	/**
	 * Minutka - if minutka is active...
	 */
	boolean minutka=false;

	/**
	 * If button is pressed
	 */
	boolean press= true;
	
	/**
	 * Minute which is set
	 */
	int minuta=0;
	
	/**
	 * Hour which is set
	 */
	int godzina=0;
	
	/**
	 * If post meridian
	 */
	boolean pm= false;

	/**
	 * What happens when this activity is stopped
	 */
	protected void onStop() {
		super.onStop();
		String alarmName = getIntent().getStringExtra("alarmName");
		int hour = Time.getGodzina();
		int minute = Time.getMinuta();
		int hourPeriod = Time.getOkres_godzin();
		int minutePeriod = Time.getOkres_minut();
		System.out.println("Stopping main activity...");
		System.out.println("Minute: " + minute);
		System.out.println("Hour: " + hour);
		System.out.println("Minute Period: " + minutePeriod);
		System.out.println("Hour Period: " + hourPeriod);
		System.out.println("Alarm Name: " + alarmName);

		StateMap.state.put(alarmName, new int[]{hour, minute, hourPeriod, minutePeriod});
		// Reseting period setups in global Time
		Time.setOkres_godzin(0);
		Time.setOkres_minut(0);
	}

	/**
	 * What happens when this activity is created
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		FrameLayout main = (FrameLayout) findViewById(R.id.main_view);
		final TextView text= (TextView)findViewById(R.id.text1);

		// !!
		b=new Ball(this,160,177,160,50,Color.GRAY);
		main.addView(b);


		Button b2 = (Button) findViewById(R.id.minutowa1);
		b2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				minutka=!minutka;

				if ((b1 != null) && (minutka== true)){
					b1.setVisibility (View.VISIBLE);

				}

				else if((b1 != null) && (minutka== false))
					b1.setVisibility (View.INVISIBLE);
			}
		});

		main.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent e) {

				final float x = e.getX();
				final float y = e.getY();

				// srodek okregu rozny dla urzadzen.. zakomentowane dla telefonu..
				final float xs= 160;
				final float ys= 177;


				if((minutka== true) && (press==true)){
					FrameLayout flView = (FrameLayout) v;
					// !!
					b1= new Ball(flView.getContext(), xs,ys,300,300,Color.BLACK);
					flView.addView(b1);
					press= false;
				}


				float yp= 29;
				float r= 148;

				float b= FloatMath.sqrt((x-xs)*(x-xs)+(ys-y)*(ys-y));
				float c= FloatMath.sqrt((x-xs)*(x-xs)+(y-yp)*(y-yp));
				double kat;
				if(x>=xs)
					kat= Math.acos((r*r+b*b-c*c)/(2*r*b))*(180/Math.PI);
				else
					kat= 360-Math.acos((r*r+b*b-c*c)/(2*r*b))*(180/Math.PI);


				if(minutka==false){
					godzina= Math.round((float)(kat)/30);
					if(godzina==0){
						godzina=12;
						pm= !pm;
					}
				}
				else{
					minuta= Math.round((float)(kat)/6);
				}
				if(pm)
					if(minuta<10)
						text.setText(godzina+" : 0"+minuta+" pm");
					else
						text.setText(godzina+" : "+minuta+" pm");
				else
					if(minuta<10)
						text.setText(godzina+" : 0"+minuta+" am");
					else
						text.setText(godzina+" : "+minuta+" am");
				String txt = text.getText().toString();
				if (txt.contains("am")) {
					if (godzina == 12) {
						Time.setGodzina(0);
					} else {
						Time.setGodzina(godzina);
					}
				} else {
					if (godzina == 12) {
						Time.setGodzina(12);
					} else {
						Time.setGodzina(godzina + 12);
					}
				}
				Time.setMinuta(minuta);
				if(minutka==false){
					move(x,y);
				}
				else{
					move2(x,y);
				}

				return true;
			}
		});

	}
	
	/**
	 * Moving first ball
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public void move(float x,float y){
		b.changePos(x,y);
	}
	
	/**
	 * Moving second ball
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public void move2(float x,float y){
		b1.changePos(x,y);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * When selecting options item
	 * @param item - menuItem
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		String nrElement = "";

		switch (item.getItemId()) {

		case R.id.item1:
			nrElement = "Set Perdiod for alarm";
			Intent intent = new Intent(this, PeriodActivity.class);
			startActivity(intent);
			break;
		default:
			nrElement = "None";	 
		}

		Toast.makeText(getApplicationContext(), "Element: " + nrElement,
				Toast.LENGTH_LONG).show();

		return true;
	}


}

