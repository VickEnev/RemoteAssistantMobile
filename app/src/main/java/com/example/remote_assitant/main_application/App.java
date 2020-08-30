package com.example.remote_assitant.main_application;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import com.example.remote_assitant.connection.TcpClient;
import com.example.remote_assitant.connection.TcpEvent;
import com.example.remote_assitant.database.AppDatabase;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class App extends Application implements Observer {
    private final GlobalRepository mGlobalRepository = new GlobalRepository();
    private final String databaseName = "remoteAssistantDatabase";
    private AppDatabase appDatabase;
    private TcpClient mTcpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, databaseName).build();
        mTcpClient = new TcpClient();
        this.mTcpClient.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        Object payload = ((TcpEvent)arg).getPayload();

        Log.d("TcpState", ((TcpEvent)arg).getTcpEventTypeString());
        Log.d("Payload", payload != null? payload.toString() : "");
    }

    public TcpClient getTcpClient() {
        return mTcpClient;
    }
    public GlobalRepository getGlobalRepository() {
        return mGlobalRepository;
    }
    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
