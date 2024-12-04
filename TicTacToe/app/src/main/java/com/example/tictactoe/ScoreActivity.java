package com.example.tictactoe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class ScoreActivity extends AppCompatActivity {
    private TableLayout scoreTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scoreTable = findViewById(R.id.scoreTable);
        displayScores();
    }

    private void displayScores() {
        // Get the SharedPreferences object
        SharedPreferences sharedPreferences = getSharedPreferences("TicTacToeScores", MODE_PRIVATE);
        Map<String, ?> allScores = sharedPreferences.getAll(); // Get all stored scores

        // Clear any previous rows (if any) in the table
        scoreTable.removeAllViews();

        // Add header row for better UI
        TableRow headerRow = new TableRow(this);

        // Player Name Header
        TextView headerName = new TextView(this);
        headerName.setText("Player Name");
        headerName.setTextSize(18);
        headerName.setTextColor(getResources().getColor(R.color.white));
        headerName.setBackgroundColor(getResources().getColor(R.color.blue)); // Background color for header
        headerName.setPadding(16, 16, 16, 16);
        TableRow.LayoutParams nameParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        headerName.setLayoutParams(nameParams);
        headerRow.addView(headerName);

        // Score Header
        TextView headerScore = new TextView(this);
        headerScore.setText("Score");
        headerScore.setTextSize(18);
        headerScore.setTextColor(getResources().getColor(R.color.white));
        headerScore.setBackgroundColor(getResources().getColor(R.color.blue)); // Background color for header
        headerScore.setPadding(16, 16, 16, 16);
        TableRow.LayoutParams scoreParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        headerScore.setLayoutParams(scoreParams);
        headerRow.addView(headerScore);

        // Add header row to the table
        scoreTable.addView(headerRow);

        // Add rows for each player score from SharedPreferences
        for (Map.Entry<String, ?> entry : allScores.entrySet()) {
            TableRow row = new TableRow(this);

            // Player Name Column
            TextView playerName = new TextView(this);
            playerName.setText(entry.getKey());
            playerName.setTextSize(16);
            playerName.setTextColor(getResources().getColor(R.color.black));
            playerName.setPadding(8, 8, 8, 8);
            TableRow.LayoutParams nameColumnParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            playerName.setLayoutParams(nameColumnParams);

            // Score Column
            TextView playerScore = new TextView(this);
            playerScore.setText(entry.getValue().toString());
            playerScore.setTextSize(16);
            playerScore.setTextColor(getResources().getColor(R.color.black));
            playerScore.setPadding(8, 8, 8, 8);
            TableRow.LayoutParams scoreColumnParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            playerScore.setLayoutParams(scoreColumnParams);

            // Add columns to the row
            row.addView(playerName);
            row.addView(playerScore);

            // Add the row to the table
            scoreTable.addView(row);
        }
    }
}
