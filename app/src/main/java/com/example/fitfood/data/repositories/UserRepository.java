package com.example.fitfood.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitfood.data.data_sources.room.dao.ProductDAO;
import com.example.fitfood.data.data_sources.room.dao.UserDao;
import com.example.fitfood.data.data_sources.room.entites.ProductEntity;
import com.example.fitfood.data.data_sources.room.entites.UserEntity;
import com.example.fitfood.data.data_sources.room.root.ProductDatabase;
import com.example.fitfood.data.data_sources.room.root.UserDatabase;

import java.util.List;

public class UserRepository {
    private final UserDao userDao;
    private final LiveData<UserEntity> user;

    public UserRepository(Application application){
        UserDatabase db = UserDatabase.getDatabase(application);
        userDao = db.userDAO();
        user = userDao.getUser();
    }

    public void insert(UserEntity user){
        userDao.insert(user);
    }

    public void delete(UserEntity user){
        UserDatabase.databaseWriteExecutor.execute(()-> userDao.delete(user));
    }

    public void update(UserEntity user){
        UserDatabase.databaseWriteExecutor.execute(()-> userDao.update(user));
    }

    public LiveData<UserEntity> getUser(){
        return user;
    }

}
