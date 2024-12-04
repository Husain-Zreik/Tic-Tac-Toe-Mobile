package com.example.tictactoe;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class GameActivity extends AppCompatActivity {
    private TextView currentPlayerTextView, scoreTextView;
    private Button[][] buttons = new Button[3][3];
    private boolean playerOneTurn = true;
    private int roundCount = 0;

    private int playerOneScore = 0;
    private int playerTwoScore = 0;
    private String playerOneName, playerTwoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        // Retrieve player names from intent
        playerOneName = getIntent().getStringExtra("PLAYER_ONE");
        playerTwoName = getIntent().getStringExtra("PLAYER_TWO");

        // Initialize UI components
        currentPlayerTextView = findViewById(R.id.currentPlayerTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        Button restartButton = findViewById(R.id.restartButton);
        Button exitButton = findViewById(R.id.exitButton);

        // Initialize the Tic Tac Toe board
        initializeBoard();

        // Set initial UI
        updatePlayerTurn();
        updateScore();

        restartButton.setOnClickListener(v -> resetScores());

        // Exit Button Click Listener
        exitButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);

                // Set click listeners for the buttons
                buttons[i][j].setOnClickListener(v -> onCellClick((Button) v));
            }
        }
    }

    private void onCellClick(Button button) {
        if (!button.getText().toString().equals("")) {
            return;
        }

        if (playerOneTurn) {
            button.setText("X");
            button.setTextColor(getResources().getColor(R.color.blue));
        } else {
            button.setText("O");
            button.setTextColor(getResources().getColor(R.color.red));
        }

        roundCount++;

        if (checkForWinner()) {
            if (playerOneTurn) {
                playerOneWins();
            } else {
                playerTwoWins();
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            // Switch turns
            playerOneTurn = !playerOneTurn;
            updatePlayerTurn();
        }
    }

    private boolean checkForWinner() {
        String[][] board = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            // Rows
            if (board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2]) && !board[i][0].equals("")) {
                return true;
            }
            // Columns
            if (board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i]) && !board[0][i].equals("")) {
                return true;
            }
        }
        // Diagonals
        if (board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2]) && !board[0][0].equals("")) {
            return true;
        }
        if (board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0]) && !board[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void playerOneWins() {
        playerOneScore++;
        savePlayerScore(playerOneName, playerOneScore); // Save the updated score
        showWinner(playerOneName + " wins!");
        resetBoard();
    }

    private void playerTwoWins() {
        playerTwoScore++;
        savePlayerScore(playerTwoName, playerTwoScore); // Save the updated score
        showWinner(playerTwoName + " wins!");
        resetBoard();
    }

    private void savePlayerScore(String playerName, int score) {
        // Get SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("TicTacToeScores", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Retrieve the old score (default to 0 if no score exists)
        int oldScore = sharedPreferences.getInt(playerName, 0);

        // Add 1 to the old score (as the win increments the score by 1)
        int updatedScore = oldScore + 1;

        // Save the updated score
        editor.putInt(playerName, updatedScore);
        editor.apply();
    }

    private void draw() {
        showWinner("It's a draw!");
        resetBoard();
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        roundCount = 0;
        playerOneTurn = true;
        updatePlayerTurn();
        updateScore();
    }

    private void updatePlayerTurn() {
        currentPlayerTextView.setText("Current Turn: " + (playerOneTurn ? playerOneName : playerTwoName));
    }

    private void updateScore() {
        scoreTextView.setText(playerOneName + ": " + playerOneScore + " | " + playerTwoName + ": " + playerTwoScore);
    }

    private void showWinner(String message) {
        int roundNumber = (playerOneScore + playerTwoScore + 1);
        new AlertDialog.Builder(this)
                .setTitle("Round " + roundNumber)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void resetScores() {
        playerOneScore = 0;
        playerTwoScore = 0;
        resetBoard();
        updateScore();
    }

}
