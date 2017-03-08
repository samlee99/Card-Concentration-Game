/***************************************************************
 * file: MemoryButton.java
 * author: Sam Lee, Andrew Nipp, Joshua Ludwig, Steven Mai, Je'Don Carter
 * class: CS 245 – Programming Graphical User Interfaces
 *
 * assignment: Android Project
 * date last modified: 3/8/2017
 *
 * purpose: This file deals with the button functionality.
 *
 ****************************************************************/
package com.example.samlee.cs245application;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatDrawableManager;
import android.widget.Button;
import android.widget.GridLayout;

/**
 * Created by Je'Don Carter on 3/3/2017.
 */

public class MemoryButton extends AppCompatButton {

    protected int row;
    protected int column;
    protected int frontDrawableId;

    protected boolean isFlipped = false;
    protected boolean isMatched = false;

    protected Drawable front;
    protected Drawable back;

    int sdk = android.os.Build.VERSION.SDK_INT;

    //method: MemoryButton
    //purpose: This method applies the graphics to the cards, front and back
    public MemoryButton(Context context, int r, int c, int frontImageDrawableId)
    {
        super(context);

        row = r;
        column = c;
        frontDrawableId =  frontImageDrawableId;

        front = AppCompatDrawableManager.get().getDrawable(context, frontImageDrawableId);
        back = AppCompatDrawableManager.get().getDrawable(context, R.drawable.rick_and_morty_icon);

        //Cheecks sdk version and fixes error (setBackground dosent work for older phones and the other is deprecated
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(back);
        } else {
            setBackground(back);
        }

        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(r), GridLayout.spec(c));

        tempParams.width = (int) getResources().getDisplayMetrics().density * 50;
        tempParams.height = (int) getResources().getDisplayMetrics().density * 50;

        setLayoutParams(tempParams);
    }
    
//method: isMatched
    //purpose: returns whether there is a match
    public boolean isMatched() {
        return isMatched;
    }
    //method: setMatched
    //purpose: sets the cards to be matched
    public void setMatched(boolean matched) {
        isMatched = matched;
    }
    //method: getFrontDrawableId
    //purpose: this gets the number for the id
    public int getFrontDrawableId() {
        return frontDrawableId;
    }
    //method: getIsFlipped
    //purpose: returns that the card is flipped.
    public boolean getIsFlipped() {
        return isFlipped;
    }
    //method: flip
    //purpose: sets the cards whether they are flipped or not
    public void flip()
    {
        if(isMatched)
        {
            return;
        }

        if(isFlipped)
        {
            //Cheecks sdk version and fixes error (setBackground dosent work for older phones and the other is deprecated
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                setBackgroundDrawable(back);
            } else {
                setBackground(back);
            }

            isFlipped = false;
        }

        else
        {
            setBackground(front);
            isFlipped = true;
        }
    }
}
