package com.example.remote_assitant.connection;

public enum TcpEventType {
    CONNECTION_STARTED,
    CONNECTION_ESTABLISHED,
    CONNECTION_FAILED,
    CONNECTION_LOST,
    MESSAGE_RECEIVED,
    MESSAGE_SENT,
    DISCONNECTED;

    @Override
    public String toString(){
        switch (this) {
            case CONNECTION_STARTED:        return "CONNECTION_STARTED";
            case CONNECTION_ESTABLISHED:    return "CONNECTION_ESTABLISHED";
            case CONNECTION_FAILED:         return "CONNECTION_FAILED";
            case CONNECTION_LOST:           return "CONNECTION_LOST";
            case MESSAGE_RECEIVED:          return "MESSAGE_RECEIVED";
            case MESSAGE_SENT:              return "MESSAGE_SENT";
            case DISCONNECTED:              return "DISCONNECTED";
            default: throw new IllegalArgumentException();
        }
    }
}
