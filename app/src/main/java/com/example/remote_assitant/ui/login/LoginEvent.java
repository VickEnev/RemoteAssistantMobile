package com.example.remote_assitant.ui.login;

public class LoginEvent {

    private boolean mIsSuccessfulLogin = false;
    private int mErrorMessageID = -1;

    public boolean IsSuccessfulLogin() {
        return mIsSuccessfulLogin;
    }

    public void setIsSuccessfulLogin(boolean IsSuccessfulLogin) {
        this.mIsSuccessfulLogin = IsSuccessfulLogin;
    }

    public int getErrorMessageID() {
        return mErrorMessageID;
    }

    public void setErrorMessageID(int ErrorMessageID) {
        this.mErrorMessageID = ErrorMessageID;
    }

    public boolean isEmpty() {
        return !mIsSuccessfulLogin  && mErrorMessageID == -1;
    }
}
