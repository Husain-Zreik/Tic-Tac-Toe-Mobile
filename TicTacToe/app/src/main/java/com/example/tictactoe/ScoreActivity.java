package com.example.tictactoe;

import static com.example.tictactoe.R.*;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.example.tictactoe.data.repository.UserRepository;
import com.example.tictactoe.data.local.database.entities.User;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScoreActivity extends AppCompatActivity {
    private TableLayout scoreTable;
    private UserRepository userRepository;
    private ExecutorService executorService;
    private CardView scoresCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_score);

        // Apply window insets to the main view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Get system insets (status and navigation bars)
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Set padding based on system insets to avoid overlapping system bars
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            // Return the insets to be applied
            return insets;
        });


        // Initialize components
        userRepository = new UserRepository(this);
        executorService = Executors.newSingleThreadExecutor();

        // Find views
        scoreTable = findViewById(R.id.scoreTable);
        scoresCard = findViewById(R.id.scoresCard);
        MaterialButton resetScoresButton = findViewById(R.id.resetScoresButton);

        // Load scores
        loadScores();

        // Set up reset button
        resetScoresButton.setOnClickListener(v -> {
            try {
                resetTotalScores();
                Snackbar.make(v, "Scores have been reset", Snackbar.LENGTH_SHORT).show();
            } catch (Exception e) {
                Snackbar.make(v, "Failed to reset scores", Snackbar.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    private void loadScores() {
        executorService.submit(new Callable<Void>() {
            @Override
            public Void call() {
                try {
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
                    runOnUiThread(() -> Snackbar.make(scoresCard,
                            "Error loading scores", Snackbar.LENGTH_SHORT).show());
                }
                return null;
            }
        });
    }

    private void displayScores(List<User> users) {
        scoreTable.removeAllViews();
        Collections.sort(users, (user1, user2) ->
                Integer.compare(user2.getTotalScore(), user1.getTotalScore()));

        // Add header row
        addHeaderRow();

        // Add player rows
        for (int i = 0; i < users.size(); i++) {
            addPlayerRow(users.get(i), i);
        }
    }

    private void addHeaderRow() {
        TableRow headerRow = new TableRow(this);

        // Create headers
        String[] headers = {"Rank", "Player", "Score"};
        for (String header : headers) {
            TextView headerView = new TextView(this);
            headerView.setText(header);
            headerView.setTextSize(16);
            headerView.setTextColor(getResources().getColor(R.color.primary_blue));
            headerView.setTypeface(null, Typeface.BOLD);
            headerView.setPadding(16, 16, 16, 16);
            headerView.setGravity(Gravity.CENTER);

            TableRow.LayoutParams params = new TableRow.LayoutParams(0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f);
            headerView.setLayoutParams(params);
            headerRow.addView(headerView);
        }

        scoreTable.addView(headerRow);
        addDivider();
    }

    private void addPlayerRow(User user, int position) {
        TableRow row = new TableRow(this);
        row.setPadding(8, 8, 8, 8);

        // Rank number
        TextView rankView = new TextView(this);
        rankView.setText(String.valueOf(position + 1));
        rankView.setTextSize(16);
        rankView.setTextColor(getResources().getColor(R.color.primary_blue));
        rankView.setGravity(Gravity.CENTER);
        rankView.setLayoutParams(new TableRow.LayoutParams(0,
                TableRow.LayoutParams.WRAP_CONTENT, 1f));

        // Player name
        TextView nameView = new TextView(this);
        nameView.setText(user.getUsername());
        nameView.setTextSize(16);
        nameView.setTextColor(Color.BLACK);
        nameView.setGravity(Gravity.CENTER);
        nameView.setLayoutParams(new TableRow.LayoutParams(0,
                TableRow.LayoutParams.WRAP_CONTENT, 1f));

        // Score
        TextView scoreView = new TextView(this);
        scoreView.setText(user.getTotalScore() + " pts");
        scoreView.setTextSize(16);
        scoreView.setTextColor(getResources().getColor(R.color.primary_blue));
        scoreView.setTypeface(null, Typeface.BOLD);
        scoreView.setGravity(Gravity.CENTER);
        scoreView.setLayoutParams(new TableRow.LayoutParams(0,
                TableRow.LayoutParams.WRAP_CONTENT, 1f));

        // Add views to row
        row.addView(rankView);
        row.addView(nameView);
        row.addView(scoreView);

        // Highlight top 3 positions
        if (position < 3) {
            int backgroundColor = Color.TRANSPARENT;
            switch (position) {
                case 0:
                    backgroundColor = Color.argb(20, 255, 215, 0); // Gold
                    break;
                case 1:
                    backgroundColor = Color.argb(20, 192, 192, 192); // Silver
                    break;
                case 2:
                    backgroundColor = Color.argb(20, 205, 127, 50); // Bronze
                    break;
            }
            row.setBackgroundColor(backgroundColor);
        }

        scoreTable.addView(row);
        addDivider();
    }

    private void addDivider() {
        View divider = new View(this);
        divider.setBackgroundColor(Color.LTGRAY);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, 1);
        divider.setLayoutParams(params);
        scoreTable.addView(divider);
    }

    private void displayEmptyState() {
        scoreTable.removeAllViews();

        TextView emptyStateText = new TextView(this);
        emptyStateText.setText("No players yet!\nStart a game to see scores here.");
        emptyStateText.setTextSize(18);
        emptyStateText.setTextColor(Color.GRAY);
        emptyStateText.setGravity(Gravity.CENTER);
        emptyStateText.setPadding(16, 32, 16, 32);

        TableRow emptyRow = new TableRow(this);
        emptyRow.addView(emptyStateText);
        scoreTable.addView(emptyRow);
    }

    private void resetTotalScores() {
        executorService.submit(new Callable<Void>() {
            @Override
            public Void call() {
                try {
                    List<User> users = userRepository.getAllUsers();
                    for (User user : users) {
                        userRepository.updateTotalScore(user.getId(), 0);
                    }
                    runOnUiThread(() -> loadScores());
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
            executorService.shutdown();
        }
    }
}