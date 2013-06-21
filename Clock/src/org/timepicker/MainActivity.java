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
package org.timepicker;

import org.clock.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.Display;
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

	int width,height;
	
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
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		FrameLayout main = (FrameLayout) findViewById(R.id.main_view);
		final TextView text= (TextView)findViewById(R.id.text1);

		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		int width2= (int)width/2;
		height = display.getHeight();
		int height2= (int)(height/2.7);
		
		// !!
		b=new Ball(this,width/2,height2,width/2,50,Color.GRAY);
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
				final float xs= width/2;
				final float ys= (float) (height/2.7);


				if((minutka== true) && (press==true)){
					FrameLayout flView = (FrameLayout) v;
					// !!
					b1= new Ball(flView.getContext(), xs,ys,300,300,Color.BLACK);
					flView.addView(b1);
					press= false;
				}


				float yp= (float)(height/16.5);
				float r= ys-yp;

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
