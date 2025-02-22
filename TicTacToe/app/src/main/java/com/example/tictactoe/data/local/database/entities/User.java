package com.example.tictactoe.data.local.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "isBot")
    private boolean isBot;

    @ColumnInfo(name = "total_wins")
    private int totalWins;

    @ColumnInfo(name = "total_losses")
    private int totalLosses;

    @ColumnInfo(name = "total_draws")
    private int totalDraws;

    // Constructor, getters, and setters
    public User(String username, boolean isBot) {
        this.username = username;
        this.isBot = isBot;
        this.totalWins = 0;
        this.totalLosses = 0;
        this.totalDraws = 0;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public boolean isBot() { return isBot; }
    public void setBot(boolean bot) { isBot = bot; }

    public int getTotalWins() { return totalWins; }
    public void setTotalWins(int totalWins) { this.totalWins = totalWins; }

    public int getTotalLosses() { return totalLosses; }
    public void setTotalLosses(int totalLosses) { this.totalLosses = totalLosses; }

    public int getTotalDraws() { return totalDraws; }
    public void setTotalDraws(int totalDraws) { this.totalDraws = totalDraws; }
}
