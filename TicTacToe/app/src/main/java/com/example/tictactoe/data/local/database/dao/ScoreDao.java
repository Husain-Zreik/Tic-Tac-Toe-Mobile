package com.example.tictactoe.data.local.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tictactoe.data.local.database.entities.Score;

import java.util.List;

@Dao
public interface ScoreDao {

    // Create (Insert) a new score
    @Insert
    void insert(Score score);

    // Update score and result
    @Update
    void update(Score score);

    // Delete a score
    @Delete
    void delete(Score score);

    // Get score by user ID
    @Query("SELECT * FROM scores WHERE usernameFK = :userId")
    List<Score> getScoresByUserId(int userId);

    @Query("SELECT * FROM scores WHERE usernameFK = :userId AND gameFK = :gameId LIMIT 1")
    Score getScoreByUserIdAndGameId(int userId, int gameId);

    @Query("SELECT * FROM scores ORDER BY score DESC") // Sort scores in descending order
    List<Score> getAllScores();

    @Query("DELETE FROM scores")
    void deleteAllScores();
}
