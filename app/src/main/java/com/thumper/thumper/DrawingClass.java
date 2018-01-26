package com.thumper.thumper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Ivan on 11/10/2017.
 */

public class DrawingClass extends View {

    // setup initial color
    private final int paintColor = Color.BLACK;
    // defines paint and canvas
    private Paint drawPaint;
    private int xCoord = 500;
    private int yCoord = 700;

    public DrawingClass(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }



    public int getxCoord(){
        return xCoord;
    }

    public int getyCoord(){
        return yCoord;
    }

    public void setxCoord (int x) {
        this.xCoord = x;
    }

    public void setyCoord (int y) {
        this.yCoord = y;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.fillArrow(canvas , 350,500 , this.xCoord, this.yCoord);
    }


    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void fillArrow(Canvas canvas, float x0, float y0, float x1, float y1) {



        float deltaX = x1 - x0;
        float deltaY = y1 - y0;
        float frac = (float) 0.1;

        float point_x_1 = x0 + (float) ((1 - frac) * deltaX + frac * deltaY);
        float point_y_1 = y0 + (float) ((1 - frac) * deltaY - frac * deltaX);

        float point_x_2 = x1;
        float point_y_2 = y1;

        float point_x_3 = x0 + (float) ((1 - frac) * deltaX - frac * deltaY);
        float point_y_3 = y0 + (float) ((1 - frac) * deltaY + frac * deltaX);

        canvas.drawLine(x0,y0,point_x_2,point_y_2,drawPaint);

        Path path = new Path();
        drawPaint.setStyle(Paint.Style.FILL);
        path.setFillType(Path.FillType.EVEN_ODD);

        path.moveTo(x0,y0);
        path.lineTo(x1,y1);
        path.moveTo(point_x_1, point_y_1);
        path.lineTo(point_x_2, point_y_2);
        path.lineTo(point_x_3, point_y_3);
        path.lineTo(point_x_1, point_y_1);
        path.lineTo(point_x_1, point_y_1);

        path.close();


        canvas.drawPath(path, drawPaint);

    }
}
