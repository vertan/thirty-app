package se.bitninja.thirtygame;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CheckScoreActivity extends AppCompatActivity {

    private Dice[] dice;
    private int comboSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_score);

        // Get dice from last activity
        Intent intent = getIntent();
        int[] savedDice = intent.getIntArrayExtra(DiceActivity.DICES);

        // Reconstruct the dice from last activity.
        comboSum = 0;
        dice = new Dice[DiceActivity.DICE_AMOUNT];
        for(int i = 0; i < savedDice.length; i++) {
            dice[i] = new Dice(DiceActivity.FACE_AMOUNT);
            dice[i].setNumber(savedDice[i]);
            String buttonID = "dice" + (i+1);
            int resID = getResources().getIdentifier(buttonID, "id", "se.bitninja.thirtygame");
            ImageButton b = (ImageButton) findViewById(resID);
            dice[i].setButton(b);
            b.setImageDrawable(DiceActivity.getFaceImage(b, DiceActivity.faceColor.WHITE, dice[i].getNumber()));
        }

        // The string representations of the different score methods.
        String[] scoreOptions = getResources().getStringArray(R.array.scoreOptions);

        // Create spinner to let user choose method.
        Spinner spinner = (Spinner) findViewById(R.id.scoretype_spinner);
        ArrayList<String> scoreOptionsList = new ArrayList<>();

        for(int i = 0; i < scoreOptions.length; i++) {
            if(!DiceActivity.usedSumTypes[i]) {
                scoreOptionsList.add(scoreOptions[i]);
            }

        }

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            chooseType(view);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // Implementing interface
                    }
                });

        // Populate spinner with remaining methods.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scoreOptionsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    /**
     * Converts the string representation to the
     * integer representation of the scoring methods.
     * @param text The text to convert to a integer representation.
     * @return The integer representation of a given method string.
     */
    public int getPositionFromText(String text) {
        // The string representations of the different score methods.
        Resources r = getResources();
        if(text.equals(r.getString(R.string.option_low))) {
            return 0;
        } else if(text.equals(r.getString(R.string.option_four))) {
            return 1;
        } else if(text.equals(r.getString(R.string.option_five))) {
            return 2;
        } else if(text.equals(r.getString(R.string.option_six))) {
            return 3;
        } else if(text.equals(r.getString(R.string.option_seven))) {
            return 4;
        } else if(text.equals(r.getString(R.string.option_eight))) {
            return 5;
        } else if(text.equals(r.getString(R.string.option_nine))) {
            return 6;
        } else if(text.equals(r.getString(R.string.option_ten))) {
            return 7;
        } else if(text.equals(r.getString(R.string.option_eleven))) {
            return 8;
        } else if(text.equals(r.getString(R.string.option_twelve))) {
            return 9;
        } else {
            // No options left
            return -1;
        }

    }

    /**
     * Checks the currently chosen dice combo and calculates the score
     * of that combo, or hints about any eventual errors in the combo.
     * @param view The view of the pressed button.
     */
    public void checkCombo(View view) {
        Spinner spinner = (Spinner)findViewById(R.id.scoretype_spinner);
        String chosenString = spinner.getSelectedItem().toString();
        int chosenType = getPositionFromText(chosenString) + 3;
        int sum = 0;

        String lowStr = getResources().getString(R.string.option_low);
        // Special case for the Low method.
        if(chosenString.equals(lowStr)) {
            for(int i = 0; i < dice.length; i++) {
                if(dice[i].isSaved()) {
                    if(dice[i].getNumber() <= 3) {
                        sum += dice[i].getNumber();
                    } else {
                        Context context = getApplicationContext();
                        CharSequence text = getResources().getString(R.string.wrong_value_toast);
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
            }

            // Make used dice red and unclickable
            // to discern from the rest.
            for(int i = 0; i < dice.length; i++) {
                if(dice[i].isSaved()) {
                    dice[i].setSaved(false);
                    ImageButton b = dice[i].getButton();
                    b.setEnabled(false);
                    b.setImageDrawable(DiceActivity.getFaceImage(b, DiceActivity.faceColor.RED, dice[i].getNumber()));
                }
            }
            // Update combo text
            comboSum += sum;
            TextView text2 = (TextView)findViewById(R.id.current_combo_score);
            text2.setText(String.format(getResources().getString(R.string.combo_score), comboSum));

        } else {
            // Count sum of current combo.
            for(int i = 0; i < dice.length; i++) {
                if(dice[i].isSaved()) {
                    sum += dice[i].getNumber();
                }
            }
            if(sum == chosenType) {
                // Make used dice red and unclickable
                // to discern from the rest.
                for(int i = 0; i < dice.length; i++) {
                    if(dice[i].isSaved()) {
                        dice[i].setSaved(false);
                        ImageButton b = dice[i].getButton();
                        b.setEnabled(false);
                        b.setImageDrawable(DiceActivity.getFaceImage(b, DiceActivity.faceColor.RED, dice[i].getNumber()));
                    }
                }
                comboSum += sum;
                int currCombos = comboSum / chosenType;
                TextView text = (TextView)findViewById(R.id.current_combo_sum);
                text.setText(String.format(getResources().getString(R.string.combo_count), currCombos, chosenString.toLowerCase()));
                TextView text2 = (TextView)findViewById(R.id.current_combo_score);
                text2.setText(String.format(getResources().getString(R.string.combo_score), comboSum));
            } else {
                // Show hints about errors in combo
                Context context = getApplicationContext();

                CharSequence text = getResources().getString(R.string.incorrect_combo);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

        }

    }

    /**
     * Saves the result for the chosen method, and then redirects
     * to the dice choosing activity.
     * @param view
     */
    public void saveCombos(View view) {
        Spinner spinner = (Spinner)findViewById(R.id.scoretype_spinner);
        String chosenString = spinner.getSelectedItem().toString();
        int chosenType = getPositionFromText(chosenString);
        DiceActivity.usedSumTypes[chosenType] = true;
        DiceActivity.scoreList[chosenType] = comboSum;

        // Check if game is over
        if(DiceActivity.currentRound == 10) {
            Intent intent = new Intent(this, ResultActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, DiceActivity.class);
            startActivity(intent);
        }

    }

    /**
     * Reacts to the user selecting a method in the spinner. It sets some text to
     * accomodate the choice made.
     * @param view
     */
    public void chooseType(View view) {
        Spinner spinner = (Spinner)findViewById(R.id.scoretype_spinner);
        String chosenType = spinner.getSelectedItem().toString();
        TextView text = (TextView)findViewById(R.id.current_combo_sum);
        if(chosenType.equals(getResources().getString(R.string.option_low))) {
            text.setText(getResources().getString(R.string.select_low_combo));
        } else {
            int currCombos = 0;
            text.setText(String.format(getResources().getString(R.string.combo_count), currCombos, chosenType.toLowerCase()));
        }
        TextView text2 = (TextView)findViewById(R.id.current_combo_score);
        String comboString = this.getResources().getString(R.string.current_combo_score);
        text2.setText(comboString);
    }

    /**
     * Toggle save state on clicked die.
     * @param view The view of the clicked button.
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
}