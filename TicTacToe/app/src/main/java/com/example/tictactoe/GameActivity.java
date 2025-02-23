package com.example.tictactoe;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tictactoe.data.local.database.entities.Game;
import com.example.tictactoe.data.local.database.entities.Score;
import com.example.tictactoe.data.local.database.entities.User;
import com.example.tictactoe.data.repository.GameRepository;
import com.example.tictactoe.data.repository.ScoreRepository;
import com.example.tictactoe.data.repository.UserRepository;

public class GameActivity extends AppCompatActivity {
    private TextView currentPlayerTextView, scoreTextView;
    private Button[][] buttons = new Button[3][3];
    private boolean playerOneTurn = true;
    private int roundCount = 0;

    private Game currentGame;
    private int playerOneScore = 0;
    private int playerTwoScore = 0;

    private Score score1;
    private Score score2;
    private String playerOneName, playerTwoName;
    private int playerOneID, playerTwoID;
    private ScoreRepository scoreRepository;

    private UserRepository userRepository;

    private GameRepository gameRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize repositories
        gameRepository = new GameRepository(this);
        scoreRepository = new ScoreRepository(this);
        userRepository = new UserRepository(this);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.TRANSPARENT);

        // Retrieve player names from intent
        playerOneName = getIntent().getStringExtra("PLAYER_ONE");
        playerOneID = getIntent().getIntExtra("PLAYER_ONE_ID",0);

        playerTwoName = getIntent().getStringExtra("PLAYER_TWO");
        playerTwoID = getIntent().getIntExtra("PLAYER_TWO_ID",0);

        // Initialize UI components
        currentPlayerTextView = findViewById(R.id.currentPlayerTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        Button restartButton = findViewById(R.id.restartButton);
        Button exitButton = findViewById(R.id.exitButton);

        // Initialize the Tic Tac Toe board
        initializeBoard();
        insertInitialPlayers();

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
    private void insertInitialPlayers() {
        new Thread(() -> {
            try {
                Log.d("GameActivity", "Inserted players ID: " + playerOneID +" "+ playerTwoID); // Log gameId
                // Insert the game and get the generated game ID
                gameRepository.insert(new Game(playerOneID, playerTwoID));
                currentGame = gameRepository.getGameByPlayers(playerOneID, playerTwoID);
                int gameId = currentGame.getId();
                Log.d("GameActivity", "Inserted Game ID: " + gameId); // Log gameId

                // Insert initial scores for both players with the game ID
                score1 = new Score(playerOneID, (int) gameId, 0, "draw");
                score2 = new Score(playerTwoID, (int) gameId, 0, "draw");

                scoreRepository.insertScore(score1);
                scoreRepository.insertScore(score2);

                score1 = scoreRepository.getScoreByUserIdAndGameId(playerOneID,gameId);
                score2 = scoreRepository.getScoreByUserIdAndGameId(playerTwoID,gameId);

                Log.d("GameActivity", "Inserting Score for Player 1: " + score1); // Log Player 1 score
                Log.d("GameActivity", "Inserting Score for Player 2: " + score2); // Log Player 2 score

            } catch (Exception e) {
                Log.e("GameActivity", "Database insert failed", e); // Log any exception
            }
        }).start();
    }

    private void updateGameWinner() {
        new Thread(() -> {
            try {
                if (currentGame != null) {
                    if (playerOneScore > playerTwoScore) {
                        currentGame.setWinner(playerOneID);
                        if (score1 != null && score2 != null) {
                            score1.setResult("win");
                            score2.setResult("loss");
                        } else {
                            Log.e("updateGameWinner", "One of the score objects is null.");
                        }
                    } else if (playerOneScore < playerTwoScore) {
                        currentGame.setWinner(playerTwoID);
                        if (score1 != null && score2 != null) {
                            score2.setResult("win");
                            score1.setResult("loss");
                        } else {
                            Log.e("updateGameWinner", "One of the score objects is null.");
                        }
                    } else {
                        currentGame.setWinner(null);
                        if (score1 != null && score2 != null) {
                            score1.setResult("draw");
                            score2.setResult("draw");
                        } else {
                            Log.e("updateGameWinner", "One of the score objects is null.");
                        }
                    }

                    if (score1 != null) {
                        scoreRepository.updateScore(score1);
                    } else {
                        Log.e("updateGameWinner", "score1 is null. Cannot update score.");
                    }

                    if (score2 != null) {
                        scoreRepository.updateScore(score2);
                    } else {
                        Log.e("updateGameWinner", "score2 is null. Cannot update score.");
                    }

                    gameRepository.updateGame(currentGame);
                } else {
                    Log.e("updateGameWinner", "currentGame is null. Cannot update game winner.");
                }
            } catch (Exception e) {
                Log.e("updateGameWinner", "An error occurred while updating the game winner.", e);
            }
        }).start();
    }


    private void updatePlayerScore(Score score, int scoreValue, int user) {
        new Thread(() -> {
            try {
                if (score != null) {
                    Log.e("updatePlayerScore", "Score object is not null. score Id: " + score.getId() + " old score: " + score.getScore() + " new value: " + scoreValue);
                    score.setScore(scoreValue);
                    scoreRepository.updateScore(score);

                    // Log before fetching the current user
                    Log.e("updatePlayerScore", "Fetching current user with userId: " + user);
                    User currentUser = userRepository.getUserByUserId(user);

                    if (currentUser != null) {
                        Log.e("updatePlayerScore", "User found. Current total score: " + currentUser.getTotalScore());
                        userRepository.updateTotalScore(user, currentUser.getTotalScore() + 1);
                        Log.e("updatePlayerScore", "Total score updated for userId: " + user + " new total: " + (currentUser.getTotalScore() + scoreValue));
                    } else {
                        Log.e("updatePlayerScore", "User not found for userId: " + user);
                    }
                } else {
                    Log.e("updatePlayerScore", "Score object is null. Cannot update score.");
                }
            } catch (Exception e) {
                Log.e("updatePlayerScore", "An error occurred while updating the score. Error: ", e);
            }
        }).start();
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
        if (!button.getText().toString().isEmpty()) {
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
        // Switch turns
        playerOneTurn = !playerOneTurn;
        updatePlayerTurn();

        if (checkForWinner()) {
            updateGameWinner();
            if (playerOneTurn) {
                playerOneWins();
            } else {
                playerTwoWins();
            }
        } else if (roundCount == 9) {
            updateGameWinner();
            draw();
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
            if (board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2]) && !board[i][0].isEmpty()) {
                return true;
            }
            // Columns
            if (board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i]) && !board[0][i].isEmpty()) {
                return true;
            }
        }
        // Diagonals
        if (board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2]) && !board[0][0].isEmpty()) {
            return true;
        }
        if (board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0]) && !board[0][2].isEmpty()) {
            return true;
        }

        return false;
    }

    private void playerOneWins() {
        playerOneScore++;
        updatePlayerScore(score1,playerOneScore,playerOneID);
        savePlayerScore(playerOneName, playerOneScore); // Save the updated score
        showWinner(playerOneName + " wins!");
        resetBoard();
    }

    private void playerTwoWins() {
        playerTwoScore++;
        updatePlayerScore(score2,playerTwoScore,playerTwoID);
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
