package com.example.samlee.cs245application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by PC on 2/21/2017.
 */

public class HighScoresActivity extends Activity {
    TextView score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        score = (TextView) findViewById(R.id.scores);
        score.setText("");
        readScore(this);
        score.setMovementMethod(new ScrollingMovementMethod());
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainMenuActivity.class));
        finish();
    }

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

    private String readScore(Context context) {
        String ret = "";
        HashMap<String,ArrayList<String[]>> highScoreList =  new HashMap<>();
        ArrayList<String[]> scoreList = new ArrayList<>();
        try {
            InputStream inputStream = context.openFileInput("scores.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    //stringBuilder.append(receiveString);
                   // score.append(receiveString+"\n\n");
                    //User, GameType, Score
                    String[] recSplit = receiveString.split("-");
                    String gameType = recSplit[1];
                    String[] userScore = {recSplit[0],recSplit[2]};
                    if(!highScoreList.containsKey(gameType) || highScoreList.get(gameType).size() < 3){
                        scoreList.add(userScore);
                        highScoreList.put(gameType,scoreList);
                    }else{
                        ArrayList<String[]> scoreList2 = highScoreList.get(gameType);
                        for(int i = 0; i < scoreList2.size();++i){
                            int savedScore = Integer.parseInt(scoreList2.get(i)[1].split(":")[1]);
                            int currScore = Integer.parseInt(userScore[1].split(":")[1]);
                            if(currScore > savedScore){
                                scoreList2.remove(i);
                                scoreList2.add(i,userScore);
                                break;
                            }
                        }
                    }
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (IOException e) {
            return "";
        }

        Object[] keyList = highScoreList.keySet().toArray();
        String[] keys = new String[keyList.length];
        for(int i = 0; i < keyList.length; ++i){
            keys[i] = (String)keyList[i];
        }


        for(int i = 0; i < highScoreList.size(); ++i) {
            score.append("Game Type: " + keys[i] + "\n--------------------\n");
            ArrayList<String[]> list = highScoreList.get(keys[i]);

            for (int j = 0; j < list.size(); ++j) {
                score.append(list.get(j)[0] + " --- " + list.get(j)[1] + "\n\n");
            }
//            for(int j = 0; j < highScoreList.get(i).size(); ++j){
            //              score.append(highScoreList.get(i).get(j)[0] + " --- " + highScoreList.get(i).get(j)[1] + "\n\n");
            //        }
        }
        return ret;
    }
}
