package se.bitninja.thirtygame;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class DiceActivity extends AppCompatActivity {

    public enum faceColor {WHITE, GREY, RED}
    private final String ROUNDS_LEFT = "roundsLeft";
    private final String CURRENT_ROUND = "currentRound";
    private final String SCORE_LIST = "scoreList";
    public final static String DICE_ARRAY = "diceArray";
    public final static String USED_SUM_TYPES = "usedSumTypes";

    private Dice[] dice;
    private int roundsLeft = 2;
    public static final int DICE_AMOUNT = 6;
    public static final int FACE_AMOUNT = 6;
    public final static String DICES = "se.bitninja.thirty.DICES";

    // Static values used between different activities.
    public static int currentRound = 0;
    public static int[] scoreList = new int[10];
    private  boolean[] usedSumTypes = new boolean[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            roundsLeft = savedInstanceState.getInt(ROUNDS_LEFT);
            currentRound = savedInstanceState.getInt(CURRENT_ROUND);
            dice = (Dice[]) savedInstanceState.getParcelableArray(DICE_ARRAY);
            scoreList = savedInstanceState.getIntArray(SCORE_LIST);
            usedSumTypes = savedInstanceState.getBooleanArray(USED_SUM_TYPES);
        } else {
            currentRound++;
            // Create new dice array and set the face pictures.
            dice = new Dice[DICE_AMOUNT];
            for(int i = 0; i < DICE_AMOUNT; i++) {
                dice[i] = new Dice(FACE_AMOUNT);
                String ID = "dice" + (i+1);
                dice[i].setID(ID);
            }
        }

        for(int i = 0; i < DICE_AMOUNT; i++) {
            View v = findViewById(android.R.id.content);
            toggleDiceImage(v, dice[i]);
        }

        Intent intent = getIntent();
        if(intent != null) {
            usedSumTypes = intent.getBooleanArrayExtra(DiceActivity.USED_SUM_TYPES);
        }

        TextView text = (TextView)findViewById(R.id.available_throws);
        text.setText(String.format(getResources().getString(R.string.rounds_left), roundsLeft));
    }

    public static ImageButton getButton(View v, String buttonID) {
        Resources res = v.getResources();
        int resID = res.getIdentifier(buttonID, "id", "se.bitninja.thirtygame");
        return (ImageButton) v.findViewById(resID);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(ROUNDS_LEFT, roundsLeft);
        savedInstanceState.putParcelableArray(DICE_ARRAY, dice);
        savedInstanceState.putInt(CURRENT_ROUND, currentRound);
        savedInstanceState.putIntArray(SCORE_LIST, scoreList);
        savedInstanceState.putBooleanArray(USED_SUM_TYPES, usedSumTypes);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundsLeft = savedInstanceState.getInt(ROUNDS_LEFT);
        dice = (Dice[]) savedInstanceState.getParcelableArray(DICE_ARRAY);
        currentRound = savedInstanceState.getInt(ROUNDS_LEFT);
        scoreList = savedInstanceState.getIntArray(SCORE_LIST);
        usedSumTypes = savedInstanceState.getBooleanArray(USED_SUM_TYPES);

    }

    public static Drawable getFaceImage(ImageButton button, faceColor color, int number) {
        TypedArray whiteFaces = button.getResources().obtainTypedArray(R.array.whiteFaces);
        TypedArray greyFaces = button.getResources().obtainTypedArray(R.array.greyFaces);
        TypedArray redFaces = button.getResources().obtainTypedArray(R.array.redFaces);

        Drawable faceImage = whiteFaces.getDrawable(0);
        switch(color) {
            case WHITE:
                faceImage = whiteFaces.getDrawable(number-1);
                break;
            case GREY:
                faceImage = greyFaces.getDrawable(number-1);
                break;
            case RED:
                faceImage = redFaces.getDrawable(number-1);
                break;
            default:
                getFaceImage(button, faceColor.WHITE, number);
        }
        whiteFaces.recycle();
        greyFaces.recycle();
        redFaces.recycle();
        return faceImage;

    }

    /**
     * Toggles the save state of the clicked die.
     * @param view The clicked view
     */
    public void saveDice(View view) {
        switch(view.getId()) {
            case R.id.dice1:
                dice[0].toggleSaved();
                toggleDiceImage(view, dice[0]);
                break;
            case R.id.dice2:
                dice[1].toggleSaved();
                toggleDiceImage(view, dice[1]);
                break;
            case R.id.dice3:
                dice[2].toggleSaved();
                toggleDiceImage(view, dice[2]);
                break;
            case R.id.dice4:
                dice[3].toggleSaved();
                toggleDiceImage(view, dice[3]);
                break;
            case R.id.dice5:
                dice[4].toggleSaved();
                toggleDiceImage(view, dice[4]);
                break;
            case R.id.dice6:
                dice[5].toggleSaved();
                toggleDiceImage(view, dice[5]);
                break;
        }
    }

    public static void toggleDiceImage(View view, Dice die) {
        ImageButton b = getButton(view, die.getID());
        if(die.isDisabled()){
            b.setImageDrawable(DiceActivity.getFaceImage(b, faceColor.RED, die.getNumber()));
        } else if(die.isSaved()) {
            b.setImageDrawable(DiceActivity.getFaceImage(b, faceColor.GREY, die.getNumber()));
        } else {
            b.setImageDrawable(DiceActivity.getFaceImage(b, faceColor.WHITE, die.getNumber()));
        }
    }

    /**
     * Rolls our die and updates the face pictures.
     * @param view The view from the clicked button
     */
    public void diceRoller(View view) {
        if(roundsLeft > 0) {
            for (int i = 0; i < DICE_AMOUNT; i++) {
                if (dice[i].isSaved()) {
                    continue;
                }
                dice[i].roll();
                View v = findViewById(android.R.id.content);
                ImageButton b = getButton(v, dice[i].getID());
                b.setImageDrawable(getFaceImage(b, faceColor.WHITE, dice[i].getNumber()));
            }
            roundsLeft--;
            TextView text = (TextView)findViewById(R.id.available_throws);
            text.setText(String.format(getResources().getString(R.string.rounds_left), roundsLeft));
        } else {
            Context context = getApplicationContext();
            CharSequence text = getResources().getString(R.string.no_rolls);
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

        int[] dicesInt = new int[DICE_AMOUNT];
        for(int i = 0; i < DICE_AMOUNT; i++) {
            dicesInt[i] = dice[i].getNumber();
        }

        intent.putExtra(USED_SUM_TYPES, usedSumTypes);
        intent.putExtra(DICES, dicesInt);
        startActivity(intent);
    }

}
