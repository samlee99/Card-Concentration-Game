/***************************************************************
 * file: MainMenuActivity.java
 * author: Sam Lee, Andrew Nipp, Joshua Ludwig, Steven Mai, Je'Don Carter
 * class: CS 245 – Programming Graphical User Interfaces
 *
 * assignment: Android Project
 * date last modified: 3/8/2017
 *
 * purpose: This file is for the main menu.  Contains the
 * buttons for other options.
 *
 ****************************************************************/
package com.example.samlee.cs245application;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.io.IOException;


public class MainMenuActivity extends AppCompatActivity {

    private int paused;
    private MediaPlayer player;
    private PopupWindow popupWindow;
    private LayoutInflater layoutInflater;
    private RelativeLayout relativeLayout;

    private EditText col;
    private EditText row;

    public static int numColumns;
    public static int numRows;
//method: onCreate
    //purpose: This method controls the music and layout of main menu.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        player = MediaPlayer.create(this, R.raw.backgroundmusic);
        player.setLooping(true);
        player.start();

        relativeLayout = (RelativeLayout) findViewById(R.id.activity_main_menu);
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        CheckBox checkbox = (CheckBox) findViewById(R.id.checkBox);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(compoundButton.isChecked()) {
                    paused = player.getCurrentPosition();
                    player.pause();
                } else {
                    player.seekTo(paused);
                    player.start();
                }
            }
        });
    }
//method: creditsButton
    //purpose: Creates the credits button
    public void creditsButton(View view) {
        ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.activity_credits,null);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int)(dm.widthPixels * 0.8);
        int height = (int)(dm.heightPixels * 0.6);
        popupWindow = new PopupWindow(container, width, height, true);
        popupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);

        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
    //method: highScoresButton
     //purpose: Creates the highscore button
    public void highScoresButton(View view) {
        Intent intent = new Intent(this, HighScoresActivity.class);
        startActivity(intent);

        /*ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.activity_high_score,null);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int)(dm.widthPixels * 0.8);
        int height = (int)(dm.heightPixels * 0.6);
        popupWindow = new PopupWindow(container, width, height, true);
        popupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);

        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                return true;
            }
        });*/
    }
    //method: playButton
    //purpose: Starts the concentration game
    public void playButton(View view) {
        col = (EditText) findViewById(R.id.columnText);
        row = (EditText) findViewById(R.id.rowText);
        String co = col.getText().toString();
        String ro = row.getText().toString();
        MediaPlayer mp = MediaPlayer.create(this, R.raw.show_me_what_you_got);
        mp.start();
        AlertDialog.Builder error = new AlertDialog.Builder(this);
        error.setTitle("Error!");
        try {
            numColumns = Integer.parseInt(co);
            numRows = Integer.parseInt(ro);
        }catch(NumberFormatException e){
            error.setMessage("Please enter a number");
            error.show();
            return;
        }

        if(numColumns*numRows>20){
            error.setMessage("Please enter a row and column that is less than 20");
            error.show();
            return;
        }

        if(numColumns == 5 && numRows == 4){
            numColumns = 4;
            numRows = 5;
        }

        Intent intent = new Intent(this, Game4x4Activity.class);
        startActivity(intent);
    }
//method: onPause
    //purpose: Pause functionality
    @Override
    protected void onPause() {
        super.onPause();
        player.release();
        finish();
    }
}
