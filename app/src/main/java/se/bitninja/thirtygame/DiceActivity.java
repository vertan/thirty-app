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

    private static int[] whiteFaces = {
            R.drawable.white1,
            R.drawable.white2,
            R.drawable.white3,
            R.drawable.white4,
            R.drawable.white5,
            R.drawable.white6
    };
    private static int[] greyFaces = {
            R.drawable.grey1,
            R.drawable.grey2,
            R.drawable.grey3,
            R.drawable.grey4,
            R.drawable.grey5,
            R.drawable.grey6
    };
    private static int[] redFaces = {
            R.drawable.red1,
            R.drawable.red2,
            R.drawable.red3,
            R.drawable.red4,
            R.drawable.red5,
            R.drawable.red6
    };

    public enum faceColor {WHITE, GREY, RED}

    private Dice[] dice;
    private int roundsLeft = 2;
    public static final int DICE_AMOUNT = 6;
    public static final int FACE_AMOUNT = 6;
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
        dice = new Dice[DICE_AMOUNT];
        for(int i = 0; i < DICE_AMOUNT; i++) {
            dice[i] = new Dice(FACE_AMOUNT);
            String buttonID = "dice" + (i+1);
            int resID = getResources().getIdentifier(buttonID, "id", "se.bitninja.thirtygame");
            dice[i].setButton((ImageButton) findViewById(resID));
            dice[i].getButton().setImageResource(getFaceImage(faceColor.WHITE, dice[i].getNumber()));
        }

    }

    public static int getFaceImage(faceColor color, int number) {
        int faceImage = 0;
        switch(color) {
            case WHITE:
                faceImage = whiteFaces[number-1];
                break;
            case GREY:
                faceImage = greyFaces[number-1];
                break;
            case RED:
                faceImage = redFaces[number-1];
                break;
            default:
                getFaceImage(faceColor.WHITE, number);
        }

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
                dice[i].getButton().setImageResource(getFaceImage(faceColor.WHITE, dice[i].getNumber()));
            }
            roundsLeft--;
            TextView text = (TextView)findViewById(R.id.available_throws);
            // TODO Replace text literal
            text.setText("Available throws: " + roundsLeft);
        } else {
            Context context = getApplicationContext();
            // TODO Replace text literal
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

        int[] dicesInt = new int[DICE_AMOUNT];
        for(int i = 0; i < DICE_AMOUNT; i++) {
            dicesInt[i] = dice[i].getNumber();
        }

        intent.putExtra(DICES, dicesInt);
        startActivity(intent);
    }

}
