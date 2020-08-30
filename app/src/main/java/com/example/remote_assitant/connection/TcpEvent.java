package com.example.remote_assitant.connection;

enum TcpClientState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    CONNECTION_STARTED,
    FAILED
}

public class TcpEvent {
    private TcpEventType eventType;
    private Object payload;

    public TcpEvent(TcpEventType eventType, Object payload) {
        this.eventType = eventType;
        this.payload = payload;
    }

    public TcpEventType getTcpEventType() {
        return this.eventType;
    }

    public String getTcpEventTypeString() {
       return this.eventType.toString();
    }

    public Object getPayload() {
        return this.payload;
    }
}
