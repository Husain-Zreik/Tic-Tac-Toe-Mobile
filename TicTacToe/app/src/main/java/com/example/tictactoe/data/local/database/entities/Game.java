package com.example.tictactoe.data.local.database.entities;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
@Entity(tableName = "games",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "p1FK"),
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "p2FK"),
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "winner", onDelete = ForeignKey.SET_NULL) // Allowing null for winner on delete
        },
        indices = {@Index("p1FK"), @Index("p2FK"), @Index("winner")}
)
public class Game {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "p1FK")
    private int p1FK;

    @ColumnInfo(name = "p2FK")
    private int p2FK;

    @ColumnInfo(name = "winner")
    private Integer winner; // Allowing null for winner (nullable)

    @ColumnInfo(name = "date")
    private long date;

    public Game(int p1FK, int p2FK) {
        this.p1FK = p1FK;
        this.p2FK = p2FK;
        this.winner = null; // Winner is null initially (for a draw or pending result)
        this.date = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getP1FK() { return p1FK; }
    public void setP1FK(int p1FK) { this.p1FK = p1FK; }

    public int getP2FK() { return p2FK; }
    public void setP2FK(int p2FK) { this.p2FK = p2FK; }

    public Integer getWinner() { return winner; } // Get winner (nullable)
    public void setWinner(Integer winner) { this.winner = winner; }

    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
}
