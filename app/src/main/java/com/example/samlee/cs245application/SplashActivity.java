package com.example.samlee.cs245application;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;

/**
 * Created by PC on 2/20/2017.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
