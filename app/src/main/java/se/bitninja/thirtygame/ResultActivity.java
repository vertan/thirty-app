package se.bitninja.thirtygame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ListView resultListView = (ListView)findViewById(R.id.result_list);
        String[] scoreOptions = {
                "Low", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve"
        };
        int finalSum = 0;
        ArrayList resultList = new ArrayList<>();
        for(int i = 0; i < DiceActivity.scoreList.length; i++) {
            finalSum += DiceActivity.scoreList[i];
            resultList.add(scoreOptions[i] + " - " + DiceActivity.scoreList[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resultList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        resultListView.setAdapter(adapter);

        TextView finalSumView = (TextView) findViewById(R.id.final_sum);
        finalSumView.setText("Final sum: " + finalSum);
    }

    public void backToMainMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
