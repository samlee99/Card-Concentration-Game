/***************************************************************
 * file: Game4x4Activity.java
 * author: Sam Lee, Andrew Nipp, Joshua Ludwig, Steven Mai, Je'Don Carter
 * class: CS 245 – Programming Graphical User Interfaces
 *
 * assignment: Android Project
 * date last modified: 3/8/2017
 *
 * purpose: This file is for the actual game.  It contains the
 * logic and layout of the game.
 *
 ****************************************************************/
package com.example.samlee.cs245application;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.os.Handler;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;

public class Game4x4Activity extends AppCompatActivity implements View.OnClickListener{

    private int numberOfElements;

    private MemoryButton[] buttons;

    private int[] buttonGraphicLocations;
    private int[] buttonGraphics;

    private MemoryButton selectedButton1;
    private MemoryButton selectedButton2;

    private int numColumns;
    private int numRows;

    private int score;
    private String user;
    private String gridNumber = "4x5"; //should be changed to whatever user specified grid

    private boolean isBusy = false;

    private int paused;
    private MediaPlayer player;
    
    
    //method: onCreate
    //purpose: This creates the grid, gives the buttons the graphics
    //and manages audio
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game4x4);
        score = 0;
        MainMenuActivity main = new MainMenuActivity();
        numColumns = (int) main.numColumns;
        numRows = (int) main.numRows;
        GridLayout gridLayout = (GridLayout)findViewById(R.id.grid_layout_4x4);

        numberOfElements = numColumns * numRows;

        buttons = new MemoryButton[numberOfElements];

        buttonGraphics = new int[20 / 2];

        buttonGraphics[0] = R.drawable.morty_1;
        buttonGraphics[1] = R.drawable.morty_2;
        buttonGraphics[2] = R.drawable.morty_3;
        buttonGraphics[3] = R.drawable.morty_4;
        buttonGraphics[4] = R.drawable.morty_5;
        buttonGraphics[5] = R.drawable.morty_6;
        buttonGraphics[6] = R.drawable.morty_7;
        buttonGraphics[7] = R.drawable.morty_8;
        buttonGraphics[8] = R.drawable.morty_9;
        buttonGraphics[9] = R.drawable.morty_10;

        buttonGraphicLocations = new int [numberOfElements];

        shuffleButtonGraphics();

        for(int r = 0; r < numRows; r++)
        {
            int j = 0;
            for(int c = 0; c < numColumns; c++)
            {
                //try changing with j when it works
                MemoryButton tempButton = new MemoryButton(this, r, c, buttonGraphics[buttonGraphicLocations[r * numColumns + c]]);
                tempButton.setId(View.generateViewId());
                tempButton.setOnClickListener(this);
                buttons[r  * numColumns + c] = tempButton;
                gridLayout.addView(tempButton);
                j++;
            }
            j++;
        }

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
//method: shuffleButtonGraphics
    //purpose:  This method swaps locations of the cards
    protected void shuffleButtonGraphics()
    {
        Random rand = new Random();

        for(int i = 0; i < numberOfElements; i++)
        {
            buttonGraphicLocations[i] = i % (numberOfElements / 2);
        }

        for(int i = 0; i < numberOfElements; i++)
        {
            int temp = buttonGraphicLocations[i];
            //ADDED CHECK!!!!
            int swapLocation = rand.nextInt(numberOfElements);

            buttonGraphicLocations[i] = buttonGraphicLocations[swapLocation];

            buttonGraphicLocations[swapLocation] = temp;
        }
    }
    
    //method: onClick
    //purpose: This manages what happens when a card is clicked.
    //It checks to see if it is clicked and whether the cards are matching
    @Override
    public void onClick(View view) {
        if(isBusy)
            return;

        MemoryButton button =  (MemoryButton) view;

        if(button.isMatched)
            return;

        //clicked a valid button and am waiting for your next one
        if(selectedButton1 == null)
        {
            selectedButton1 = button;
            selectedButton1.flip();
            return;
        }
        //checks to see if user pressed the button twice
        if(selectedButton1.getId() == button.getId())
        {
            return;
        }
        //think button should be selected button 2
        if(selectedButton1.getFrontDrawableId() ==  button.getFrontDrawableId())
        {
            button.flip();

            button.setMatched(true);
            selectedButton1.setMatched(true);

            selectedButton1.setEnabled(false);
            button.setEnabled(false);

            selectedButton1 = null;

            MediaPlayer success = MediaPlayer.create(this,R.raw.success);
            success.setLooping(false);
            success.start();
            score += 2;
            return;
        }
        //two cards not the same
        else
        {
            selectedButton2 = button;
            selectedButton2.flip();
            isBusy = true;
            if(score > 0) score--;
//            final Handler handler = new Handler();
//
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                selectedButton2.flip();
//                selectedButton1.flip();
//                selectedButton1 = null;
//                selectedButton2 = null;
//                isBusy = false;
//                }
//            }, 500);
        }
    }
    
//method: tryAgainClicked
    //purpose: When called, it flips the chosen cards over.
    public void tryAgainClicked(View view){
        if(isBusy) {
            selectedButton1.flip();
            selectedButton2.flip();
            selectedButton1 = null;
            selectedButton2 = null;
            isBusy = false;
        }
    }
 //method: newGameClicked
    //purpose: When called it restarts the game
    public void newGameClicked(View view){
        startActivity(new Intent(this, Game4x4Activity.class));
        player.release();
        finish();
    }
//method: endGameClicked
    //purpose: This ends the game
    public void endGameClicked(View view){
        for (MemoryButton mb : buttons) {
            if(!mb.getIsFlipped()) {
                mb.flip();
                mb.setMatched(true);
                mb.setEnabled(false);
            }
            if(!mb.isMatched() && mb.getIsFlipped()){
                mb.setMatched(true);
                mb.setEnabled(false);
            }
        }
        promptScore();
    }
        //method: onBackPressed
    //purpose: This goes back to the main menu
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainMenuActivity.class));
        player.release();
        finish();
    }
//method: promptScore
    //purpose: this brings up a dialog box to enter your name for the scoring
    private void promptScore(){
        user = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your Score: " + score);
        builder.setMessage("Enter Username: ");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        final Context game = this;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user = input.getText().toString();
                //ATTENTION!! ATTENTION!! WE NEED TO MAKE SURE GRID NUMBER IS CHANGED
                writeScore("User:" + user + " - Game Type: " + gridNumber + " - Score: " + score + "\n", game);
                startActivity(new Intent(game, HighScoresActivity.class));
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
//method: writeScore
    //purpose: This writes the score information to a txt file
    private void writeScore(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("scores.txt", Context.MODE_APPEND));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
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
