package com.example.tictactoe;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tictactoe.data.repository.UserRepository;
import com.example.tictactoe.data.local.database.entities.User;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ScoreActivity extends AppCompatActivity {
    private TableLayout scoreTable;
    private UserRepository userRepository;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Set the status bar to be transparent
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.TRANSPARENT);

        // Initialize the UserRepository for accessing the database
        userRepository = new UserRepository(this);

        // Initialize ExecutorService for background tasks
        executorService = Executors.newSingleThreadExecutor();

        scoreTable = findViewById(R.id.scoreTable);
        Button resetScoresButton = findViewById(R.id.resetScoresButton);

        // Load the scores from the database asynchronously
        loadScores();

        // Reset scores functionality
        resetScoresButton.setOnClickListener(v -> {
            try {
                resetTotalScores(); // Reset the total scores in the database
                loadScores();       // Refresh the table
            } catch (Exception e) {
                e.printStackTrace();
                // Handle the exception (e.g., show a Toast or Snackbar)
            }
        });
    }

    // Method to load and display the scores asynchronously
    private void loadScores() {
        // Execute the task in the background thread
        executorService.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    // Fetch all users from the database
                    List<User> users = userRepository.getAllUsers();
                    runOnUiThread(() -> {
                        if (users == null || users.isEmpty()) {
                            displayEmptyState();
                        } else {
                            displayScores(users);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    // Method to display the scores table
    private void displayScores(List<User> users) {
        // Clear any previous rows in the table
        scoreTable.removeAllViews();

        // Sort users by total score in descending order
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return Integer.compare(user2.getTotalScore(), user1.getTotalScore());
            }
        });

        // Add header row for Player Name and Score
        TableRow headerRow = new TableRow(this);

        TextView headerName = new TextView(this);
        headerName.setText("Player Name");
        headerName.setTextSize(18);
        headerName.setTextColor(getResources().getColor(R.color.white));
        headerName.setBackgroundColor(android.graphics.Color.parseColor("#152C3C"));
        headerName.setPadding(16, 16, 16, 16);
        headerName.setGravity(Gravity.CENTER);
        TableRow.LayoutParams nameParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        headerName.setLayoutParams(nameParams);
        headerRow.addView(headerName);

        TextView headerScore = new TextView(this);
        headerScore.setText("Total Score");
        headerScore.setTextSize(18);
        headerScore.setTextColor(getResources().getColor(R.color.white));
        headerScore.setBackgroundColor(android.graphics.Color.parseColor("#152C3C"));
        headerScore.setPadding(16, 16, 16, 16);
        headerScore.setGravity(Gravity.CENTER);
        TableRow.LayoutParams scoreParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        headerScore.setLayoutParams(scoreParams);
        headerRow.addView(headerScore);

        scoreTable.addView(headerRow);

        // Add rows for each user and their total score
        for (User user : users) {
            TableRow row = new TableRow(this);

            // Player Name Column
            TextView playerName = new TextView(this);
            playerName.setText(user.getUsername());
            playerName.setTextSize(16);
            playerName.setTextColor(getResources().getColor(R.color.black));
            playerName.setPadding(8, 8, 8, 8);
            playerName.setGravity(Gravity.CENTER);
            TableRow.LayoutParams nameColumnParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            playerName.setLayoutParams(nameColumnParams);

            // Total Score Column
            TextView playerScore = new TextView(this);
            playerScore.setText(String.valueOf(user.getTotalScore())); // Display the total score
            playerScore.setTextSize(16);
            playerScore.setTextColor(getResources().getColor(R.color.black));
            playerScore.setPadding(8, 8, 8, 8);
            playerScore.setGravity(Gravity.CENTER);
            TableRow.LayoutParams scoreColumnParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            playerScore.setLayoutParams(scoreColumnParams);

            // Add columns to the row
            row.addView(playerName);
            row.addView(playerScore);

            // Add the row to the table
            scoreTable.addView(row);
        }
    }

    // Method to display the empty state message
    private void displayEmptyState() {
        TextView emptyStateText = new TextView(this);
        emptyStateText.setText("No users available");
        emptyStateText.setTextSize(18);
        emptyStateText.setTextColor(getResources().getColor(R.color.black));
        emptyStateText.setPadding(16, 16, 16, 16);
        emptyStateText.setGravity(Gravity.CENTER);

        // Add the empty state message to the table
        TableRow emptyRow = new TableRow(this);
        emptyRow.addView(emptyStateText);
        scoreTable.addView(emptyRow);
    }

    // Method to reset the total scores for all users
    private void resetTotalScores() {
        executorService.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    List<User> users = userRepository.getAllUsers();
                    for (User user : users) {
                        // Reset the total score for each user
                        userRepository.updateTotalScore(user.getId(), 0);
                    }
                    runOnUiThread(() -> loadScores()); // Refresh the scores after resetting
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown(); // Shutdown the ExecutorService when the activity is destroyed
        }
    }
}
