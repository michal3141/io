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
