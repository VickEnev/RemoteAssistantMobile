package com.example.remote_assitant.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserSession.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserSessionDAO userSessionDao();
}
