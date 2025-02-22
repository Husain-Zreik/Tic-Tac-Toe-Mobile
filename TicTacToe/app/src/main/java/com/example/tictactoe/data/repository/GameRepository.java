package com.example.tictactoe.data.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.example.tictactoe.data.local.database.AppDatabase;
import com.example.tictactoe.data.local.database.dao.GameDao;
import com.example.tictactoe.data.local.database.entities.Game;
import java.util.List;

public class GameRepository {
    private GameDao gameDao;

    public GameRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        gameDao = db.gameDao();
    }

    public void insert(Game game) {
        gameDao.insert(game);
    }

    public List<Game> getGamesByUserId(int userId) {
        return gameDao.getGamesByUserId(userId);
    }
}
