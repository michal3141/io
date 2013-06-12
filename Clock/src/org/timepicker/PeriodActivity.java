package org.timepicker;

import org.clock.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Period activity class
 * @author Damian
 *
 */
public class PeriodActivity extends Activity{

	/**
	 * Ball
	 */
	Ball b=null;
	
	/**
	 * Hand of the clock
	 */
	int wskazowka= 0;
	
	/**
	 * How much hours
	 */
	int godzin=0;
	
	/**
	 * Current angle
	 */
	double lastKat=0;
	
	/**
	 * If minutka is active (minutka means minutes hand)
	 */
	boolean minutka=false;
	
	/**
	 * How much minutes
	 */
	int minut=0;
	
	/**
	 * Number of cycles around
	 */
	int przejscie=0;
	
	/**
	 * On stop of this activity
	 */
	protected void onStop() {
		super.onStop();
		System.out.println("Stopping period activity...");
		System.out.println("Minutes: " + Time.getOkres_minut());
		System.out.println("Hours: " + Time.getOkres_godzin());
	}
	
	/**
	 * Creating this activity
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period);
        FrameLayout main = (FrameLayout) findViewById(R.id.period_view);
		final TextView text= (TextView)findViewById(R.id.text2);
		
		// !!
		b=new Ball(this,160,177,170,50,Color.BLUE);
		main.addView(b);
		
		
		main.setOnTouchListener(new View.OnTouchListener() {
		    public boolean onTouch(View v, MotionEvent e) {
		    	
		    	final float x = e.getX();
		    	final float y = e.getY();
		    	
		    	// srodek okregu rozny dla urzadzen..
		    	final float xs= 160;
		    	final float ys= 177;			

		    	float yp= 29;
		    	float r= 148;
		    	if((x-xs)*(x-xs)+(y-ys)*(y-ys)<r*r*0.6){
		    		text.setText("godzina");
		    		minutka=false;
		    	}
		    	else{
		    		minutka= true;
		    	}
		    	float b= FloatMath.sqrt((x-xs)*(x-xs)+(ys-y)*(ys-y));
		    	float c= FloatMath.sqrt((x-xs)*(x-xs)+(y-yp)*(y-yp));
		    	double kat;
		    	if(x>=xs)
		    		kat= Math.acos((r*r+b*b-c*c)/(2*r*b))*(180/Math.PI);
		    	else
		    		kat= 360-Math.acos((r*r+b*b-c*c)/(2*r*b))*(180/Math.PI);
		    	
		    	if(minutka){
		    		wskazowka= Math.round((float)(kat)/6);
		    		text.setText(" okres godzin: "+(przejscie+godzin)+" minut: "+wskazowka);
		    		minut= wskazowka;
		    	}
		    	else{
		    		wskazowka= Math.round((float)(kat)/30);
		    		godzin=wskazowka;
			    	if(lastKat>352 && lastKat<360 && kat<10)
			    		przejscie+=12;
			    	if(lastKat<10 && kat>352 && kat<360)
			    		if(przejscie>0)
			    			przejscie-=12;;		    			    			    	
			    	
			    	lastKat=kat;
		    		text.setText(" okres godzin: "+(przejscie+godzin)+" minut: "+minut);
		    	}
		    	Time.setOkres_godzin(przejscie+godzin);
		    	Time.setOkres_minut(minut);
		    	move(x,y);
		    	
				return true;		    	
		    }
		});
	}
	
	/**
	 * Moving ball
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public void move(float x,float y){
		b.changePos(x,y);
	}
	
	/**
	 * Happening when backButton is clicked
	 * @param button backButton
	 */
	public void onBackClick(View button){
		finish();
    }
	
}

