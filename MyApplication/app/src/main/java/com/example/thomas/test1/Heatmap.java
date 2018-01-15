package com.example.thomas.test1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.altbeacon.beacon.BeaconConsumer;

public class Heatmap extends AppCompatActivity implements BeaconConsumer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heatmap);

        setContentView(new SampleView(this));
    }

    @Override
    public void onBeaconServiceConnect() {

    }

    private static class SampleView extends View {

        // CONSTRUCTOR
        public SampleView(Context context) {
            super(context);
            setFocusable(true);

        }
        @Override
        protected void onDraw(Canvas canvas) {
            Paint paint = new Paint();

            int[][] Heatmap = new int[5][6];
            //dummy data
            for (int i = 0; i<5;i++) {
                for (int j = 0; j<6; j++) {
                    Heatmap[i][j]=200;
                }
            }
            Heatmap[3][2]=100;
            Heatmap[3][3]=75;
            Heatmap[2][2]=50;
            Heatmap[0][2]=180;

            Heatmap[1][1]=140;
            Heatmap[0][0]=140;

            canvas.drawColor(Color.WHITE);

           // Bitmap b = Bitmap.createBitmap(2000, 2000, Bitmap.Config.ALPHA_8);
           // Canvas c = new Canvas(b);
           //c.drawRect(500, 500, 200, 200, paint);

           // paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            boolean firstTime = true;
            for (int i = 0; i<5;i++) {
                for (int j = 0; j<6; j++) {
                    if(firstTime) {
                        paint.setColor(Color.rgb(Heatmap[i][j], Heatmap[i][j], Heatmap[i][j]));
                        canvas.drawRect(30 + i * 200, 30 + j * 200, 230 + i * 200, 230 + j * 200, paint);
                       // firstTime= false;
                    }
                    else {
                        paint.setColor(Color.rgb(Heatmap[i][j], Heatmap[i][j], Heatmap[i][j]));
                        canvas.drawRect(10 + i * 200, 0 + j * 200, 200 + i * 200, 200 + j * 200, paint);
                    }
                }
            }
            paint.setColor(Color.RED);
            canvas.drawCircle(30, 30 , 50, paint);
            canvas.drawCircle(1030, 30 , 50, paint);
            canvas.drawCircle(30, 1230 , 50, paint);
            canvas.drawCircle(1030, 1230 , 50, paint);




         //   canvas.drawBitmap(b, 10,10, paint);
        }

    }
}
