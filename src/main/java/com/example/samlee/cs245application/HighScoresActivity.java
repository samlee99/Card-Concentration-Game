
/***************************************************************
 * file: HighScoresActivity.java
 * author: Sam Lee, Andrew Nipp, Joshua Ludwig, Steven Mai, Je'Don Carter
 * class: CS 245 – Programming Graphical User Interfaces
 *
 * assignment: Android Project
 * date last modified: 3/8/2017
 *
 * purpose: This file manages the high scores for the game.
 *
 ****************************************************************/
package com.example.samlee.cs245application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by PC on 2/21/2017.
 */

public class HighScoresActivity extends Activity {
    TextView score;
    @Override
    //method: onCreate
    //purpose: This displays the scores.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        score = (TextView) findViewById(R.id.scores);
        score.setText("");
        readScore(this);
    }
    //method: onBackPressed
    //purpose: This brings you back to the main menu
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainMenuActivity.class));
        finish();
    }
//method: resetScore
    //purpose: This method resets the scores file.
    public void resetScore(View view) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput("scores.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write("");
            outputStreamWriter.close();
            score.setText("");
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
//method:readScore
    //purpose: This opens the scores text and combines them into a string.
    private String readScore(Context context) {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("scores.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    //stringBuilder.append(receiveString);
                    score.append(receiveString+"\n\n");
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (IOException e) {
            return "";
        }

        return ret;
    }
}
