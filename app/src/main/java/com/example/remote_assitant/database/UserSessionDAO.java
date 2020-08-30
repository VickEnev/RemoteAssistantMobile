package com.example.remote_assitant.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserSessionDAO {
    @Query("SELECT * FROM usersession")
    UserSession GetUUID();

    @Insert
    void insertAll(UserSession... users);

    @Query("DELETE FROM usersession")
    void deleteAll();
}
