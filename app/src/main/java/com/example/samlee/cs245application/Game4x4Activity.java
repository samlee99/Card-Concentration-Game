package com.example.samlee.cs245application;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.os.Handler;
import java.util.Random;

public class Game4x4Activity extends AppCompatActivity implements View.OnClickListener{

    private int numberOfElements;

    private MemoryButton[] buttons;

    private int[] buttonGraphicLocations;
    private int[] buttonGraphics;

    private MemoryButton selectedButton1;
    private MemoryButton selectedButton2;

    private boolean isBusy = false;

    private int paused;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game4x4);

        GridLayout gridLayout = (GridLayout)findViewById(R.id.grid_layout_4x4);

        int numColumns = gridLayout.getColumnCount();
        int numRows = gridLayout.getRowCount();

        numberOfElements = numColumns * numRows;

        buttons = new MemoryButton[numberOfElements];

        buttonGraphics = new int[numberOfElements / 2];

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

            return;
        }
        //two cards not the same
        else
        {
            selectedButton2 = button;
            selectedButton2.flip();
            isBusy = true;

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

    public void tryAgainClicked(View view){
        selectedButton1.flip();
        selectedButton2.flip();
        selectedButton1 = null;
        selectedButton2 = null;
        isBusy = false;
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
