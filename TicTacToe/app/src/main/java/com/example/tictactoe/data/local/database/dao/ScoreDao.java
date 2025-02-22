package com.example.tictactoe.data.local.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.tictactoe.data.local.database.entities.Score;

import java.util.List;

@Dao
public interface ScoreDao {
    @Insert
    void insert(Score score);

    @Query("SELECT * FROM scores WHERE gameFK = :gameId")
    List<Score> getScoresByGameId(int gameId);

    @Query("SELECT * FROM scores WHERE usernameFK = :userId")
    List<Score> getScoresByUserId(int userId);
}
