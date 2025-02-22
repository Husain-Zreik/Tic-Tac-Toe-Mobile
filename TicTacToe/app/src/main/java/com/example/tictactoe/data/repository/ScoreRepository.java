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

    public void insertScore(Score score) {
        scoreDao.insert(score);
    }
    public List<Score> getScoresByGameId(int gameId) {
        return scoreDao.getScoresByGameId(gameId);
    }

    public List<Score> getScoresByUserId(int userId) {
        return scoreDao.getScoresByUserId(userId);
    }
}
