package com.example.remote_assitant.ui.login;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.remote_assitant.R;
import com.example.remote_assitant.connection.ITcpClientAsyncCallback;
import com.example.remote_assitant.connection.TcpClient;
import com.example.remote_assitant.connection.TcpEvent;
import com.example.remote_assitant.database.AppDatabase;
import com.example.remote_assitant.database.UserSession;
import com.example.remote_assitant.main_application.GlobalRepository;
import com.example.remote_assitant.utilities.DeviceInfo;
import com.example.remote_assitant.utilities.IActionCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginManager implements ITcpClientAsyncCallback {

    private class HeavyLifter extends AsyncTask<AppDatabase, Void, UserSession> {

        @Override
        protected UserSession doInBackground(AppDatabase... databases) {
            return databases[0].userSessionDao().GetUUID();
        }

        @Override
        protected void onPostExecute(UserSession result) {
            LoginEvent event = new LoginEvent();

            if(result == null)
            {
                event.setErrorMessageID(R.string.an_error_has_occurred);
                mCallback.OnAction(event);
                return;
            }

            if(result.base64UUID.equals(""))
            {
                event.setErrorMessageID(R.string.an_error_has_occurred);
                mCallback.OnAction(event);
                return;
            }

            if(!validateIPAddress(result.serverIpAddress))
            {
                event.setErrorMessageID(R.string.an_error_has_occurred);
                mCallback.OnAction(event);
                return;
            }

            mIpAddress = result.serverIpAddress;
            mBase64UUID = result.base64UUID;

            InitConnection();
        }

        @Override
        protected void onPreExecute() {
        }
    }

    enum LoginType {
        LoginWithPassword,
        LoginWithUUID
    }

    private Context mContext;
    private TcpClient mClient;
    private IActionCallback mCallback;
    private AppDatabase mDatabase;

    private String mBase64UUID;
    private String mPassword;
    private String mIpAddress;
    private LoginType mLoginType;

    LoginManager(Context context, TcpClient client, AppDatabase database , IActionCallback callback) {
        mContext = context;
        mClient = client;
        mCallback = callback;
        mDatabase = database;

        mClient.setAsyncCallback(this);
    }

    void setLoginCredentials(String ipAddress, String password) {
        mIpAddress = ipAddress;
        mPassword = password;
    }

    boolean loginWithCredentials() {
        if(!validateIPAddress(mIpAddress) || !validatePassword(mPassword))
            return false;

        mLoginType = LoginType.LoginWithPassword;

        if(mClient.isClientConnected()) {
            sendLoginCredentials();
        } else {
            InitConnection();
        }

        return true;
    }

    void loginWithUUID() {
        mLoginType = LoginType.LoginWithUUID;
        new HeavyLifter().execute(mDatabase);
    }

    private boolean validateIPAddress(String ipAddress) {
        if(ipAddress == null || ipAddress.equals(""))  {
            Toast.makeText(mContext.getApplicationContext(),  R.string.empty_ip_address, Toast.LENGTH_SHORT)
                    .show();

            return false;
        }

        final String pattern = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(ipAddress);
        if (!m.find( )) {

            Toast.makeText(mContext.getApplicationContext(),  R.string.invalid_ip_address, Toast.LENGTH_SHORT)
                    .show();

            return false;
        }

        return true;
    }

    private boolean validatePassword(String password) {
        if(password == null || password.equals("") || password.length() < 5) {
            Toast.makeText(mContext.getApplicationContext(),  R.string.invalid_password, Toast.LENGTH_LONG)
                    .show();

            return false;
        }

        return true;
    }

    private void InitConnection() {
        mClient.setAddress(mIpAddress);
        mClient.setPort(8888);

        mClient.connect();
    }

    private void sendLoginCredentials() {
        if(mLoginType == LoginType.LoginWithPassword)
            mClient.sendMessage(GlobalRepository.Actions.getLoginCredentialsCommandText(mPassword, new DeviceInfo()));
        else if(mLoginType == LoginType.LoginWithUUID)
            mClient.sendMessage(GlobalRepository.Actions.getLoginWithUUIDCommandText(mBase64UUID));
    }

    @Override
    public void ExecuteAction(TcpEvent event) {
        LoginEvent loginEvent = new LoginEvent();

        switch(event.getTcpEventType()) {
            case CONNECTION_ESTABLISHED:
                sendLoginCredentials();
                break;
            case MESSAGE_RECEIVED:
                final String payload = event.getPayload().toString();
                final String[] commands = payload.split(":");

                final char commandChar = commands[0].charAt(0);

                if( commandChar == GlobalRepository.Actions._LoginSuccessful)  {
                    loginEvent.setIsSuccessfulLogin(true);

                    if(mLoginType == LoginType.LoginWithPassword)
                    {
                        mDatabase.userSessionDao().deleteAll();

                        UserSession session = new UserSession();
                        session.base64UUID =  commands[1];
                        session.serverIpAddress = mIpAddress;

                        mDatabase.userSessionDao().insertAll(session);
                    }
                }
                else if( commandChar == GlobalRepository.Actions._LoginFailed) {
                    loginEvent.setErrorMessageID(R.string.login_failed);
                }
                else {
                    loginEvent.setErrorMessageID(R.string.an_error_has_occurred);
                }
                break;
            case CONNECTION_FAILED:
                loginEvent.setErrorMessageID(R.string.Connection_failed);
                break;
            case CONNECTION_LOST:
                loginEvent.setErrorMessageID(R.string.connection_lost);
                break;
        }

        if(!loginEvent.isEmpty())
            mCallback.OnAction(loginEvent);
    }
}
