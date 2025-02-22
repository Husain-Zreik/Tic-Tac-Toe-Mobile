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

    public void updateGame(Game game) {
        gameDao.update(game);
    }

    public List<Game> getGamesByUserId(int userId) {
        return gameDao.getGamesByUserId(userId);
    }

    public Game getGameByPlayers(int playerOneId, int playerTwoId) {
        Game game = gameDao.getGameByPlayers(playerOneId, playerTwoId);
        return game;
    }
//    public int getGameIdByPlayers(int playerOneId, int playerTwoId) {
//        Game game = gameDao.getGameByPlayers(playerOneId, playerTwoId);
//        if (game != null) {
//            return game.getId(); // Return the ID of the found game
//        }
//        return -1; // Return -1 if no game was found
//    }
}
