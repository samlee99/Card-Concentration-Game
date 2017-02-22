package com.example.samlee.cs245application;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {

    private int paused;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player = MediaPlayer.create(this, R.raw.get_schwifty);
        player.setLooping(true);
        player.start();

        CheckBox checkbox = (CheckBox) findViewById(R.id.pauseCheckBox);
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

    public void tryAgainClicked(View view){



    }



    public void newGameClicked(View view){

    

    }


    
    public void endGameClicked(View view){
        startActivity(new Intent(this, MainMenuActivity.class));
        player.release();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.release();
        finish();
    }
}
