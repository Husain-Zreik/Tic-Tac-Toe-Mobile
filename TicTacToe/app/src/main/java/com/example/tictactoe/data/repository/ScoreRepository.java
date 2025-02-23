package com.example.tictactoe.data.repository;

import android.content.Context;

import com.example.tictactoe.data.local.database.AppDatabase;
import com.example.tictactoe.data.local.database.dao.ScoreDao;
import com.example.tictactoe.data.local.database.entities.Score;

import java.util.List;

public class ScoreRepository {

    private ScoreDao scoreDao;

    public ScoreRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context); // Getting the database instance
        scoreDao = db.scoreDao();
    }

    // Insert a new score
    public void insertScore(Score score) {
        scoreDao.insert(score);
    }

    // Update an existing score
    public void updateScore(Score score) {
        scoreDao.update(score);
    }

    // Delete a score
    public void deleteScore(Score score) {
        scoreDao.delete(score);
    }

    // Get score by user ID and game ID
    public Score getScoreByUserIdAndGameId(int userId, int gameId) {
        return scoreDao.getScoreByUserIdAndGameId(userId, gameId);
    }

    // Get all scores from database
    public List<Score> getAllScores() {
        return scoreDao.getAllScores();
    }

    // Delete all scores
    public void deleteAllScores() {
        new Thread(scoreDao::deleteAllScores).start();
    }
}
