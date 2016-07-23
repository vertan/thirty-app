package se.bitninja.thirtygame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    /**
     * Resets eventual old values from the DiceActivity class and
     * starts the game from scratch.
     * @param view
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
