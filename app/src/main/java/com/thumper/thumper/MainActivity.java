package com.thumper.thumper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

public class MainActivity extends AppCompatActivity {
    private Communication communication;
    private int id = 1;
    private String ip ="";
    private String port ="";


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences share = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip = share.getString(SettingsActivity.NODEJS_IP,"10.0.0.100");
        port = share.getString(SettingsActivity.NODEJS_PORT,"3000");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences share = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
         ip = share.getString(SettingsActivity.NODEJS_IP,"10.0.0.100");
         port = share.getString(SettingsActivity.NODEJS_PORT,"3000");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Call<NeoPixels> call = communication.pixels(id);
                call.enqueue(new Callback<NeoPixels>() {
                    @Override
                    public void onResponse(Call<NeoPixels> call, Response<NeoPixels> response) {
                        //This executes on your GUI thread
                        if(response.body() != null){
                            String output = "";

                                output += "id is " + response.body().getNumber_of_pixels() + "\n";

                            //Display output
                            ((TextView) findViewById(R.id.textView1)).setText(output);
                        }else {
                            Log.e("REST", "Response has no body");
                        }
                    }

                    @Override
                    public void onFailure(Call<NeoPixels> call, Throwable t) {
                        Log.e("REST","Could not make request: " + t);
                    }
                });
            }
        });
        String url = "http://" + ip + ":" + port +"/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        communication = retrofit.create(Communication.class);

        Button postBtn = (Button) findViewById(R.id.PostButton);

        postBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int red = 255 * ((SeekBar) findViewById(R.id.seekBarR)).getProgress() / 100;
                int blue = 255 * ((SeekBar) findViewById(R.id.seekBarB)).getProgress() / 100;
                int green = 255 * ((SeekBar) findViewById(R.id.seekBarG)).getProgress() / 100;
                Call<NeoPixels> call = communication.pixels(id,red,green,blue);
                call.enqueue(new Callback<NeoPixels>() {
                    @Override
                    public void onResponse(Call<NeoPixels> call, Response<NeoPixels> response) {
                        if(response.isSuccessful()) {
                             Log.i("POST", "post submitted to API." + response.body().toString());
                             }
                    }

                    @Override
                    public void onFailure(Call<NeoPixels> call, Throwable t) {
                            Log.e("POST", "Unable to submit post to API.");
                    }
                });

            }

        });

        Button goBtn = (Button) findViewById(R.id.goBtn);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent aboutIntent = new Intent(getApplicationContext(), DriveActivity.class);
                startActivity(aboutIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
