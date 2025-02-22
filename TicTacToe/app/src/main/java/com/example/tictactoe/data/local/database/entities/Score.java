package com.example.tictactoe.data.local.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
@Entity(tableName = "scores",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "usernameFK", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Game.class, parentColumns = "id", childColumns = "gameFK", onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("usernameFK"), @Index("gameFK")}
)
public class Score {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "usernameFK")
    private int usernameFK;

    @ColumnInfo(name = "gameFK")
    private int gameFK;

    @ColumnInfo(name = "score")
    private int score;

    @ColumnInfo(name = "result")
    private String result; // "win", "loss", "draw"

    // Constructor
    public Score(int usernameFK, int gameFK, int score, String result) {
        this.usernameFK = usernameFK;
        this.gameFK = gameFK;
        this.score = score;
        this.result = result;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsernameFK() { return usernameFK; }
    public void setUsernameFK(int usernameFK) { this.usernameFK = usernameFK; }

    public int getGameFK() { return gameFK; }
    public void setGameFK(int gameFK) { this.gameFK = gameFK; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}
