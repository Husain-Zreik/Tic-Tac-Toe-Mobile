package com.example.tictactoe.data.local.database.dao;

import com.example.tictactoe.data.local.database.entities.Game;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface GameDao {
    @Insert
    void insert(Game game);

    @Update
    void update(Game game);

    @Query("SELECT * FROM games WHERE (p1FK = :playerOneId AND p2FK = :playerTwoId) OR (p1FK = :playerTwoId AND p2FK = :playerOneId) ORDER BY date DESC LIMIT 1")
    Game getGameByPlayers(int playerOneId, int playerTwoId);

    @Query("SELECT * FROM games WHERE p1FK = :userId OR p2FK = :userId")
    List<Game> getGamesByUserId(int userId);
}
