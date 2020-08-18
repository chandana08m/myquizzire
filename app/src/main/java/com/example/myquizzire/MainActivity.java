package com.example.myquizzire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaActionSound;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
private static int time=6000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MediaPlayer oursound=MediaPlayer.create(this,R.raw.goes);
        oursound.setLooping(true);
        oursound.start();

         new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {
                 Intent score_intent=new Intent(MainActivity.this,score.class);
                 startActivity(score_intent);
                 finish();
             }
         },time);
    }
}
