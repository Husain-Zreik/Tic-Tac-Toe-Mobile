package com.example.tictactoe.data.local.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tictactoe.data.local.database.entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    User getUserByUserId(int userId);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    // Update total score for a specific user
    @Query("UPDATE users SET total_score = :totalScore WHERE id = :userId")
    void updateTotalScore(int userId, int totalScore);

    // Reset total scores for all users
    @Query("UPDATE users SET total_score = 0")
    void resetTotalScores();
}
