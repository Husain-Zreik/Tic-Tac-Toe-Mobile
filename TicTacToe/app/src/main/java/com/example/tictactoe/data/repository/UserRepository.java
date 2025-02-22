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

    public void insert(User user) {
        userDao.insert(user);
    }

    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
}
