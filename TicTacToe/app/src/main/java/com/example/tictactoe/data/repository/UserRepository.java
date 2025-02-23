package com.example.tictactoe.data.repository;

import android.content.Context;

import com.example.tictactoe.data.local.database.AppDatabase;
import com.example.tictactoe.data.local.database.dao.UserDao;
import com.example.tictactoe.data.local.database.entities.User;

import java.util.List;

public class UserRepository {

    private UserDao userDao;

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context); // Getting the database instance
        userDao = db.userDao();
    }

    // Insert a new user
    public void insertUser(User user) {
        userDao.insert(user);
    }

    // Update an existing user
    public void updateUser(User user) {
        userDao.update(user);
    }

    // Delete a user
    public void deleteUser(User user) {
        userDao.delete(user);
    }

    // Get user by username
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public User getUserByUserId(int userId) {
        return userDao.getUserByUserId(userId);
    }

    public int insertOrGetUser(User user) {
        User existingUser = userDao.getUserByUsername(user.getUsername());
        if (existingUser == null) {
            userDao.insert(user);
            existingUser = userDao.getUserByUsername(user.getUsername());
        }
        return existingUser.getId();
    }

    // Get all users
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    // Update total score for a specific user
    public void updateTotalScore(int userId, int totalScore) {
        userDao.updateTotalScore(userId, totalScore);
    }

    // Retrieve the total score for a specific user (optional method)
    public int getTotalScore(String userId) {
        User user = userDao.getUserByUsername(userId);
        return (user != null) ? user.getTotalScore() : 0;
    }

    // Reset total scores for all users
    public void resetTotalScores() {
        userDao.resetTotalScores();
    }
}
