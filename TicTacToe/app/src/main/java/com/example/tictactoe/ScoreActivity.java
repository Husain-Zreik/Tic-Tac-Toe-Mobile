package com.example.tictactoe;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ScoreActivity extends AppCompatActivity {
    private TableLayout scoreTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Set the status bar to be transparent
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.TRANSPARENT);


        scoreTable = findViewById(R.id.scoreTable);
        Button resetScoresButton = findViewById(R.id.resetScoresButton);

        displayScores();

        // Reset scores functionality
        resetScoresButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("TicTacToeScores", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Clear all data
            editor.apply();
            displayScores(); // Refresh the table
        });
    }

    private void displayScores() {
        // Get the SharedPreferences object
        SharedPreferences sharedPreferences = getSharedPreferences("TicTacToeScores", MODE_PRIVATE);
        Map<String, ?> allScores = sharedPreferences.getAll(); // Get all stored scores

        // Clear any previous rows (if any) in the table
        scoreTable.removeAllViews();

        // Check if there are any scores
        if (allScores.isEmpty()) {
            // Show empty state message
            TextView emptyStateText = new TextView(this);
            emptyStateText.setText("No scores available");
            emptyStateText.setTextSize(18);
            emptyStateText.setTextColor(getResources().getColor(R.color.black));
            emptyStateText.setPadding(16, 16, 16, 16);
            emptyStateText.setGravity(android.view.Gravity.CENTER);

            // Add the empty state message to the table
            TableRow emptyRow = new TableRow(this);
            emptyRow.addView(emptyStateText);
            scoreTable.addView(emptyRow);
            return; // Stop further processing
        }

        // Convert and sort scores in descending order
        List<Map.Entry<String, Integer>> sortedScores = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allScores.entrySet()) {
            try {
                sortedScores.add(Map.entry(entry.getKey(), Integer.parseInt(entry.getValue().toString())));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(sortedScores, (a, b) -> b.getValue() - a.getValue()); // Sort by score descending

        // Add header row
        TableRow headerRow = new TableRow(this);

        TextView headerName = new TextView(this);
        headerName.setText("Player Name");
        headerName.setTextSize(18);
        headerName.setTextColor(getResources().getColor(R.color.white));
        headerName.setBackgroundColor(android.graphics.Color.parseColor("#152C3C")); // Background color
        headerName.setPadding(16, 16, 16, 16);
        headerName.setGravity(android.view.Gravity.CENTER); // Center the text
        TableRow.LayoutParams nameParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        headerName.setLayoutParams(nameParams);
        headerRow.addView(headerName);

        TextView headerScore = new TextView(this);
        headerScore.setText("Score");
        headerScore.setTextSize(18);
        headerScore.setTextColor(getResources().getColor(R.color.white));
        headerScore.setBackgroundColor(android.graphics.Color.parseColor("#152C3C")); // Background color
        headerScore.setPadding(16, 16, 16, 16);
        headerScore.setGravity(android.view.Gravity.CENTER); // Center the text
        TableRow.LayoutParams scoreParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        headerScore.setLayoutParams(scoreParams);
        headerRow.addView(headerScore);

        scoreTable.addView(headerRow);

        // Add rows for each player score
        for (Map.Entry<String, Integer> entry : sortedScores) {
            TableRow row = new TableRow(this);

            // Player Name Column
            TextView playerName = new TextView(this);
            playerName.setText(entry.getKey());
            playerName.setTextSize(16);
            playerName.setTextColor(getResources().getColor(R.color.black));
            playerName.setPadding(8, 8, 8, 8);
            playerName.setGravity(android.view.Gravity.CENTER); // Center the text
            TableRow.LayoutParams nameColumnParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            playerName.setLayoutParams(nameColumnParams);

            // Score Column
            TextView playerScore = new TextView(this);
            playerScore.setText(String.valueOf(entry.getValue()));
            playerScore.setTextSize(16);
            playerScore.setTextColor(getResources().getColor(R.color.black));
            playerScore.setPadding(8, 8, 8, 8);
            playerScore.setGravity(android.view.Gravity.CENTER); // Center the text
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
