package com.example.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tictactoe.data.local.database.entities.User;
import com.example.tictactoe.data.repository.UserRepository;

public class PlayerNameActivity extends AppCompatActivity {
    private EditText playerOneEditText, playerTwoEditText;
    private Button startGameButton;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player_name);

        // Initialize the UserRepository
        userRepository = new UserRepository(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        playerOneEditText = findViewById(R.id.playerOneEditText);
        playerTwoEditText = findViewById(R.id.playerTwoEditText);
        startGameButton = findViewById(R.id.startGameButton);

        startGameButton.setOnClickListener(v -> {
            String playerOneName = playerOneEditText.getText().toString().trim();
            String playerTwoName = playerTwoEditText.getText().toString().trim();

            // Validate names
            if (playerOneName.isEmpty()) {
                Toast.makeText(this, "Please enter Player 1's name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (playerTwoName.isEmpty()) {
                Toast.makeText(this, "Please enter Player 2's name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert players into the database
            User playerOne = new User(playerOneName, false); // Assuming false for isBot (human player)
            User playerTwo = new User(playerTwoName, false); // Assuming false for isBot (human player)

            // Insert the users into the database
            new Thread(() -> {
                userRepository.insert(playerOne);
                userRepository.insert(playerTwo);
            }).start();

            // Proceed to GameActivity
            Intent intent = new Intent(PlayerNameActivity.this, GameActivity.class);
            intent.putExtra("PLAYER_ONE", playerOneName);
            intent.putExtra("PLAYER_TWO", playerTwoName);
            startActivity(intent);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Clear the input fields when the activity is stopped
        playerOneEditText.setText("");
        playerTwoEditText.setText("");
    }
}
