package se.bitninja.thirtygame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * The menu activity of the Thirty app, which allows the user to create a new game
 * @author Filip Hedman
 * @version 1.0 Jul 25, 2016
 */
public class MenuActivity extends AppCompatActivity {

    /**
     * Called on creation of this activity
     * @param savedInstanceState instance of state before destruction of activity, if available
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    /**
     * Resets potential old values from the DiceActivity class and
     * starts the game from scratch by launching the next activity
     * @param view view of clicked button
     */
    public void startGame(View view) {
        DiceActivity.currentRound = 0;
        DiceActivity.scoreList = new int[10];
        boolean[] usedSumTypes = new boolean[10];
        Intent intent = new Intent(this, DiceActivity.class);
        intent.putExtra(DiceActivity.USED_SUM_TYPES, usedSumTypes);
        startActivity(intent);
    }
}
