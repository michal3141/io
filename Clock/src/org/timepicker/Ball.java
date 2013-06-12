package org.timepicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Ball class - representing ball which is in fact a sloppy line 
 * with circle on top of it
 * @author Damian
 *
 */
@SuppressLint("ViewConstructor")
public class Ball extends View{
	
	/**
	 * Center x coordinate
	 */
	private final float x;
	
	/**
	 * Center y coordinate
	 */
    private final float y;
    
    /**
     * Ball x coordinate
     */
    private float x1;
    
    /**
     * Ball y coordinate
     */
    private float y1;
    
    /**
     * Canvas
     */
    Canvas can;

    /**
     * Something to paint
     */
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    
    /**
     * Ball constructor
     * @param context drawing context
     * @param x Center x coordinate
     * @param y Center y coordinate
     * @param x1 Ball x coordinate
     * @param y1 Ball y coordinate
     * @param c color
     */
    public Ball(Context context, float x, float y, float x1, float y1, int c) {
        super(context);
        mPaint.setColor(c);
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1= y1;
        
    }
    
    /**
     * Drawing line and circle on Canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //!!
        canvas.drawCircle(160, 177, 15, mPaint);
        canvas.drawLine(x, y, x1, y1, mPaint);
    }
    
    /**
     * Changing position
     * @param dx deltaX
     * @param dy deltaY
     */
    public void changePos(float dx, float dy)
    {
        x1 = dx;
        y1 = dy;
        this.invalidate ();
    } // end changePos

}
