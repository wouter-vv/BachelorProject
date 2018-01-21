package com.example.thomas.erasmusproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import static java.lang.Math.pow;

/**
 * Created by Walter on 21/01/2018.
 */

public class Location extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new SampleView(this));
    }

    private static class SampleView extends View {
        int roomWidth = 5;
        int roomLength = 6;
        int[][] room = new int[roomWidth][roomLength];
        Point beacon1 = new Point(30,30);
        Point beacon2 = new Point(30+roomWidth*200,30);
        Point beacon3 = new Point(30,30+roomLength*200);
        Point beacon4 = new Point(30+roomWidth*200,30+roomLength*200);



        // CONSTRUCTOR
        public SampleView(Context context) {
            super(context);
            setFocusable(true);

        }
        @Override
        protected void onDraw(Canvas canvas) {
            for (int i = 0; i<roomWidth; i++ ) {
                for (int j = 0; j<roomLength; j++ ) {
                    room[i][j] =220;
                }
            }
            room[3][5] =100;
            Point location = trilateration();
            Paint paint = new Paint();
            for (int i = 0; i<roomWidth; i++ ) {
                for (int j = 0; j<roomLength; j++ ) {
                    Point currentRectTopLeft = new Point(30 + i*200, 30+j*200);
                    Point currentRectBottomRight = new Point(230+i*200, 230+j*200);
                    if (location.x > currentRectTopLeft.x && location.y > currentRectTopLeft.y) {
                        if (location.x <= currentRectBottomRight.x && location.y <= currentRectBottomRight.y) {
                            room[i][j] -= 100;
                        }
                    }
                    paint.setColor(Color.rgb(room[i][j], room[i][j], room[i][j]));
                    canvas.drawRect(30 + i * 200, 30 + j * 200, 230 + i * 200, 230 + j * 200, paint);
                }
            }
            paint.setColor(Color.RED);
            canvas.drawCircle(beacon1.x, beacon1.y ,50,paint);
            canvas.drawCircle(beacon2.x ,beacon2.y ,50,paint);
            canvas.drawCircle(beacon3.x ,beacon3.y ,50,paint);
            canvas.drawCircle(beacon4.x ,beacon4.y ,50,paint);


        }
        public Point trilateration() {
            float xa = beacon1.x;
            float ya = beacon1.y;
            float xb = beacon2.x;
            float yb = beacon2.y;
            float xc = beacon3.x;
            float yc = beacon3.y;
            float ra = 100;
            float rb = 800;
            float rc = 800;

            double S = (pow(xc, 2.) - pow(xb, 2.) + pow(yc, 2.) - pow(yb, 2.) + pow(rb, 2.) - pow(rc, 2.)) / 2.0;
            double T = (pow(xa, 2.) - pow(xb, 2.) + pow(ya, 2.) - pow(yb, 2.) + pow(rb, 2.) - pow(ra, 2.)) / 2.0;
            double y = ((T * (xb - xc)) - (S * (xb - xa))) / (((ya - yb) * (xb - xc)) - ((yc - yb) * (xb - xa)));
            double x = ((y * (ya - yb)) - T) / (xb - xa);

            Log.d("a", x +"   " +y);
            return new Point((int)x,(int)y);
        }

    }
}