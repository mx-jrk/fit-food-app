package com.example.fitfood.data.data_sources.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitfood.data.data_sources.room.entites.PlanEntity;

import java.util.List;

@Dao
public interface PlanDAO {
    @Insert
    void insert(PlanEntity plan);

    @Query("SELECT * FROM plan_table")
    LiveData<List<PlanEntity>> getAllPlans();
}
