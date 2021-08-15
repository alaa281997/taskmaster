package com.example.task_master;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {StatusItems.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}