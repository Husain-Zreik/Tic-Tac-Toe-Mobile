package com.example.tictactoe.data.local.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tictactoe.data.local.database.dao.GameDao;
import com.example.tictactoe.data.local.database.dao.UserDao;
import com.example.tictactoe.data.local.database.dao.ScoreDao;
import com.example.tictactoe.data.local.database.entities.User;
import com.example.tictactoe.data.local.database.entities.Score;
import com.example.tictactoe.data.local.database.entities.Game;

@Database(entities = {User.class, Score.class, Game.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract ScoreDao scoreDao();
    public abstract GameDao gameDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "tictactoe_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
