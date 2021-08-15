package com.example.task_master;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insertOne(StatusItems statusItems);

    @Query("SELECT * FROM statusItems where task_name LIKE :name")
    StatusItems findByName(String name);

    @Query("SELECT * FROM statusItems")
    List<StatusItems> findAll();


    @Delete
    void delete(StatusItems statusItems);

}
