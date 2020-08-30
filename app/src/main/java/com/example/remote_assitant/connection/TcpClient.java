package com.example.remote_assitant.connection;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Observable;

public class TcpClient extends Observable {

    private String address;
    private Integer port;
    private Integer timeout = 2000;

    private TcpClientState state = TcpClientState.DISCONNECTED;
    private ITcpClientAsyncCallback mAsyncCallback;

    PrintWriter bufferOut;
    BufferedReader bufferIn;

    private Socket socket;

    public TcpClient() { port = 0; }


    protected void fireEvent(TcpEvent event) {
        setChanged();

        notifyObservers(event);

        if(mAsyncCallback != null)
            mAsyncCallback.ExecuteAction(event);

        clearChanged();
    }

    public void setPort(int port) {
        if(state == TcpClientState.CONNECTED) {
            throw new RuntimeException("Cannot change port while connected");
        }
        this.port = port;
    }

    public void setAddress(String address) {
        if(state == TcpClientState.CONNECTED) {
            throw new RuntimeException("Cannot change address while connected");
        }

        this.address = address;
    }

    public void setAsyncCallback(ITcpClientAsyncCallback callback) {
        mAsyncCallback = callback;
    }

    public boolean isClientConnected() {
        return this.state == TcpClientState.CONNECTED;
    }


    public void connect() {
        if(state == TcpClientState.DISCONNECTED || state == TcpClientState.FAILED) {
            if(address == null || port == null) {
                throw new RuntimeException("Address or port missing");
            }
            new ConnectThread().start();
        } else {
            throw new RuntimeException("This client is already connected or connecting");
        }
    }

    public void sendMessage(String message) {
        new SendMessageThread(message).start();
    }

    private class ConnectThread extends Thread {

        private  boolean hasInternetAccess() {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000);
                urlc.connect();
                return (urlc.getResponseCode() == 204 &&
                        urlc.getContentLength() == 0);
            } catch (IOException e) {
                fireEvent(new TcpEvent(TcpEventType.CONNECTION_FAILED, null));
            }

            return false;
        }

        @Override
        public void run() {
            try {
                state = TcpClientState.CONNECTING;
                socket = new Socket();
                socket.connect(new InetSocketAddress(InetAddress.getByName(address), port), timeout);

                bufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                bufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                state = TcpClientState.CONNECTED;
                fireEvent(new TcpEvent(TcpEventType.CONNECTION_ESTABLISHED, null));

                new ReceiveMessagesThread().start();

            } catch(Exception e) {
                fireEvent(new TcpEvent(TcpEventType.CONNECTION_FAILED, e));

                state = TcpClientState.FAILED;
            }
        }
    }

    private class ReceiveMessagesThread extends Thread {
        @Override
        public void run() {
            while(state == TcpClientState.CONNECTED) {
                try {
                    String message = bufferIn.readLine();
                    if(message != null) {
                        fireEvent(new TcpEvent(TcpEventType.MESSAGE_RECEIVED, message));
                    }
                } catch(IOException e) {
                    fireEvent(new TcpEvent(TcpEventType.CONNECTION_LOST, null));

                    try {
                        bufferOut.flush();
                        bufferOut.close();

                        bufferIn.close();

                        socket.close();
                    } catch (IOException er) {

                    }

                    state = TcpClientState.DISCONNECTED;
                }
            }
        }
    }

    private class SendMessageThread extends Thread {
        private String messageLine;

        public SendMessageThread(String message) {
            this.messageLine = message;
        }

        @Override
        public void run() {
            if(bufferOut.checkError()) {
                try {
                    bufferOut.flush();
                    bufferOut.close();

                    bufferIn.close();
                } catch(IOException e) {
                    fireEvent(new TcpEvent(TcpEventType.CONNECTION_FAILED, e.getMessage()));
                }
            } else {
                bufferOut.print(messageLine);
                bufferOut.flush();
                fireEvent(new TcpEvent(TcpEventType.MESSAGE_SENT, messageLine.toString()));
            }
        }
    }
}
