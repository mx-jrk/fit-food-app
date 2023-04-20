package com.example.fitfood.data.data_sources.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitfood.data.data_sources.room.entites.UserEntity;

@Dao
public interface UserDao {
    @Insert
    void insert(UserEntity user);

    @Delete
    void delete(UserEntity user);

    @Update
    void update(UserEntity user);

    @Query("SELECT * FROM user_table LIMIT 1 OFFSET 0")
    LiveData<UserEntity> getUser();

}
