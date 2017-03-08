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
    //Starts the concentration game
    public void playButton(View view) {
        col = (EditText) findViewById(R.id.columnText);
        row = (EditText) findViewById(R.id.rowText);
        String co = col.getText().toString();
        String ro = row.getText().toString();

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

    @Override
    protected void onPause() {
        super.onPause();
        player.release();
        finish();
    }
}
