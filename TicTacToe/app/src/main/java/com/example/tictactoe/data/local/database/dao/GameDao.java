package com.example.tictactoe.data.local.database.dao;

import com.example.tictactoe.data.local.database.entities.Game;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface GameDao {
    @Insert
    void insert(Game game);

    @Query("SELECT * FROM games WHERE p1FK = :userId OR p2FK = :userId")
    List<Game> getGamesByUserId(int userId);
}
