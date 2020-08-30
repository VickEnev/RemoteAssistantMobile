package com.example.remote_assitant.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserSession {
    @PrimaryKey(autoGenerate = true)
    public int primaryKey;

    @ColumnInfo(name = "UUID")
    public String base64UUID;

    @ColumnInfo(name = "IP_ADDRESS")
    public String serverIpAddress;
}
