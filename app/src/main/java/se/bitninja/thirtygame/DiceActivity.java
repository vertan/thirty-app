package se.bitninja.thirtygame;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class DiceActivity extends AppCompatActivity {

    Dice[] dice;
    int roundsLeft = 2;
    public final static String DICES = "se.bitninja.thirty.DICES";

    // Static values used between different activities.
    public static int currentRound = 0;
    public static int[] scoreList = new int[10];
    public static boolean[] usedSumTypes = new boolean[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentRound++;

        // Create new dice array and set the face pictures.
        dice = new Dice[6];
        for(int i = 0; i < 6; i++) {
            int rand = (int) Math.floor((Math.random() * 6) + 1);
            dice[i] = new Dice(rand);
            String buttonID = "dice" + (i+1);
            int resID = getResources().getIdentifier(buttonID, "id", "se.bitninja.thirtygame");
            dice[i].setButton((ImageButton) findViewById(resID));
            dice[i].getButton().setImageResource(dice[i].whiteFaces[rand-1]);
        }

    }

    /**
     * Toggles the save state of the clicked die.
     * @param view The clicked view
     */
    public void saveDice(View view) {
        switch(view.getId()) {
            case R.id.dice1:
                dice[0].toggleSaved();
                break;
            case R.id.dice2:
                dice[1].toggleSaved();
                break;
            case R.id.dice3:
                dice[2].toggleSaved();
                break;
            case R.id.dice4:
                dice[3].toggleSaved();
                break;
            case R.id.dice5:
                dice[4].toggleSaved();
                break;
            case R.id.dice6:
                dice[5].toggleSaved();
                break;
        }
    }

    // Rolls our dice

    /**
     * Rolls our dice and updates the face pictures.
     * @param view The view from the clicked button
     */
    public void diceRoller(View view) {
        if(roundsLeft > 0) {
            for (int i = 0; i < 6; i++) {
                if (dice[i].isSaved()) {
                    continue;
                }
                int rand = (int) Math.floor((Math.random() * 6) + 1);
                dice[i].setNumber(rand);
                dice[i].setFace(rand);
                dice[i].getButton().setImageResource(dice[i].whiteFaces[rand-1]);
            }
            roundsLeft--;
            TextView text = (TextView)findViewById(R.id.available_throws);
            text.setText("Available throws: " + roundsLeft);
        } else {
            Context context = getApplicationContext();
            CharSequence text = "No rolls left!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    /**
     * Redirects the user to the score checking activity.
     * @param view The view of the clicked button
     */
    public void checkScore(View view) {
        Intent intent = new Intent(this, CheckScoreActivity.class);

        int[] dicesInt = new int[6];
        for(int i = 0; i < dice.length; i++) {
            dicesInt[i] = dice[i].getNumber();
        }

        intent.putExtra(DICES, dicesInt);
        startActivity(intent);
    }

}