package com.thumper.thumper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DriveActivity extends AppCompatActivity {
    private Communication communication;
    private int id = 1;
    private String ip ="";
    private String port ="";
    private int Left_speed = 0;
    private  int Right_speed = 0;
    private boolean touchIsDown = false;
    private DrawingClass painter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);
        painter = findViewById(R.id.simpleDrawingView1);
        SharedPreferences share = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip = share.getString(SettingsActivity.NODEJS_IP,"10.0.0.100");
        port = share.getString(SettingsActivity.NODEJS_PORT,"3000");
        String url = "http://" + ip + ":" + port +"/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        communication = retrofit.create(Communication.class);
        final View touchView = findViewById(R.id.touchView);

        final Handler handler = new Handler();
        // Define the code block to be executed
        final Runnable runnableCode = new Runnable() {
            @Override
            public void run() {

                Log.i("Ride", "left_speed" + Left_speed + " Right_speed:" + Right_speed);
                Log.d("Handlers", "Called on main thread");
                drive(Left_speed,Right_speed);
                handler.postDelayed(this, 50);
            }
        };


        touchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchIsDown = true;


                    handler.post(runnableCode);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    touchIsDown = false;
                    Left_speed = 0;
                    Right_speed = 0;
                    handler.removeCallbacks(runnableCode);
                    drive(0,0);
                    Log.i("Ride", "Should stop");

                }

                if(touchIsDown) {
                    //Canvas canvas = (DrawingClass)(findViewById(R.id.simpleDrawingView1)).
                    painter.setxCoord((int)event.getX());
                    painter.setyCoord((int)event.getY());
                    painter.invalidate();

                    if ((int)event.getX() > 350) {

                        Left_speed = ((int)event.getY() - 500)/2 - ((int)event.getX() - 350) ;
                        Right_speed = ((int)event.getY() - 500)/2 + ((int)event.getX() - 350) ;

                    }
                    else{
                        Left_speed = ((int)event.getY() - 500)/2 - ((int)event.getX() - 350) ;
                        Right_speed = ((int)event.getY() - 500)/2 + ((int)event.getX() - 350) ;
                    }

                    if (Left_speed > 250) {
                        Left_speed = 250;
                    }
                    if (Right_speed > 250) {
                        Right_speed = 250;
                    }

                    if (Left_speed >= Right_speed) {
                        ((TextView) findViewById(R.id.speedValue)).setText(Math.abs(Left_speed)/10 + "");
                    }
                    else {
                        ((TextView) findViewById(R.id.speedValue)).setText(Math.abs(Right_speed)/10 + "");
                    }

                }
                return true;
            }
        });
    }

    protected void drive (int left_speed, int right_speed){
        Call<NeoPixels> go = communication.go(left_speed,right_speed);
        go.enqueue(new Callback<NeoPixels>() {
            @Override
            public void onResponse(Call<NeoPixels> call, Response<NeoPixels> response) {
                Log.i("POST", "GO" + response.body().toString());
            }

            @Override
            public void onFailure(Call<NeoPixels> call, Throwable t) {
                Log.e("POST", "Cannot go");
            }
        });
    }
}
