/***************************************************************
 * file: SplashActivity.java
 * author: Sam Lee, Andrew Nipp, Joshua Ludwig, Steven Mai, Je'Don Carter
 * class: CS 245 – Programming Graphical User Interfaces
 *
 * assignment: Android Project
 * date last modified: 2/28/2017
 *
 * purpose: This file launches the splash screen for the app.
 *
 ****************************************************************/
package com.example.samlee.cs245application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    //method: onCreate
    // purpose: Create a splash thread and runs it for 3 seconds.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        Thread splashThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        splashThread.start();
    }
}
