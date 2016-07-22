package se.bitninja.thirtygame;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CheckScoreActivity extends AppCompatActivity {

    Dice[] dice;
    int comboSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_score);

        // Get dice from last activity
        Intent intent = getIntent();
        int[] savedDice = intent.getIntArrayExtra(DiceActivity.DICES);

        // Reconstruct the dice from last activity.
        comboSum = 0;
        dice = new Dice[6];
        for(int i = 0; i < savedDice.length; i++) {
            dice[i] = new Dice(savedDice[i]);
            String buttonID = "dice" + (i+1);
            int resID = getResources().getIdentifier(buttonID, "id", "se.bitninja.thirtygame");
            dice[i].setButton((ImageButton) findViewById(resID));
            dice[i].getButton().setImageResource(dice[i].whiteFaces[dice[i].getNumber()-1]);
        }

        // The string representations of the different score methods.
        String[] scoreOptions = {
            "Low", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve"
        };

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
        switch(text) {
            case "Low":
                return 0;
            case "Four":
                return 1;
            case "Five":
                return 2;
            case "Six":
                return 3;
            case "Seven":
                return 4;
            case "Eight":
                return 5;
            case "Nine":
                return 6;
            case "Ten":
                return 7;
            case "Eleven":
                return 8;
            case "Twelve":
                return 9;
        }

        return 0;

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

        // Special case for the Low method.
        if(chosenString.equals("Low")) {
            for(int i = 0; i < dice.length; i++) {
                if(dice[i].isSaved()) {
                    if(dice[i].getNumber() <= 3) {
                        sum += dice[i].getNumber();
                    } else {
                        Context context = getApplicationContext();
                        CharSequence text = "Dice with value over three not added to sum!";
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
                    dice[i].getButton().setEnabled(false);
                    dice[i].getButton().setImageResource(dice[i].redFaces[dice[i].getFace()]);
                }
            }
            // Update combo text
            comboSum += sum;
            TextView text2 = (TextView)findViewById(R.id.current_combo_score);
            text2.setText("Combo score: " + comboSum);

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
                        dice[i].getButton().setEnabled(false);
                        dice[i].getButton().setImageResource(dice[i].redFaces[dice[i].getFace()]);
                    }
                }
                comboSum += sum;
                int currCombos = comboSum / chosenType;
                TextView text = (TextView)findViewById(R.id.current_combo_sum);
                text.setText(currCombos + " combinations of " + chosenString.toLowerCase() + ".");
                TextView text2 = (TextView)findViewById(R.id.current_combo_score);
                text2.setText("Combo score: " + comboSum);
            } else {
                // Show hints about errors in combo
                Context context = getApplicationContext();
                CharSequence text = "Incorrect combo, try again!";
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
        if(chosenType.equals("Low")) {
            text.setText("Select all dice with a max value of three.");
        } else {
            text.setText("0 combinations of " + chosenType.toLowerCase() + ".");
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