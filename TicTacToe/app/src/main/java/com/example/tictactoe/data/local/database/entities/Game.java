package com.example.tictactoe.data.local.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "games",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "p1FK"),
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "p2FK"),
                @ForeignKey(entity = Score.class, parentColumns = "id", childColumns = "S1FK"),
                @ForeignKey(entity = Score.class, parentColumns = "id", childColumns = "S2FK")
        })
public class Game {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "p1FK")
    private int p1FK;

    @ColumnInfo(name = "p2FK")
    private int p2FK;

    @ColumnInfo(name = "S1FK")
    private int S1FK;

    @ColumnInfo(name = "S2FK")
    private int S2FK;

    @ColumnInfo(name = "date")
    private long date; // Timestamp of when the game was played

    // Constructor, getters, and setters
    public Game(int p1FK, int p2FK, int S1FK, int S2FK, long date) {
        this.p1FK = p1FK;
        this.p2FK = p2FK;
        this.S1FK = S1FK;
        this.S2FK = S2FK;
        this.date = date;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getP1FK() { return p1FK; }
    public void setP1FK(int p1FK) { this.p1FK = p1FK; }

    public int getP2FK() { return p2FK; }
    public void setP2FK(int p2FK) { this.p2FK = p2FK; }

    public int getS1FK() { return S1FK; }
    public void setS1FK(int S1FK) { this.S1FK = S1FK; }

    public int getS2FK() { return S2FK; }
    public void setS2FK(int S2FK) { this.S2FK = S2FK; }

    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
}
