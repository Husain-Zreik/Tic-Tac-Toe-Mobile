package com.example.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button letsGoButton = findViewById(R.id.playButton);
        Button scoreButton = findViewById(R.id.scoreButton);
        Button exitButton = findViewById(R.id.exitButton);

        letsGoButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlayerNameActivity.class);
            startActivity(intent);
        });

        // Handle exit button click
        exitButton.setOnClickListener(v -> {
            finish(); // This will close the application
        });

        scoreButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
            startActivity(intent);
        });

    }


}