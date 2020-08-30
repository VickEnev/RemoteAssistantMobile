package com.example.remote_assitant.ui.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.remote_assitant.MainActivity;
import com.example.remote_assitant.R;
import com.example.remote_assitant.main_application.App;
import com.example.remote_assitant.utilities.IActionCallback;

public class LoginActivity
        extends AppCompatActivity
        implements IActionCallback  {

    private ProgressBar loadingProgressBar;
    private LoginManager mLoginManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText ipAddressEditText = findViewById(R.id.ipAddressTextField);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        mLoginManager = new LoginManager(this
                , ((App)getApplication()).getTcpClient()
                , ((App)getApplication()).getAppDatabase()
                , this );

        loadingProgressBar.setVisibility(View.VISIBLE);
        enableControls(false);

        mLoginManager.loginWithUUID();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                enableControls(false);

                mLoginManager.setLoginCredentials(ipAddressEditText.getText().toString()
                        , passwordEditText.getText().toString());

                if(!mLoginManager.loginWithCredentials())
                {
                    loadingProgressBar.setVisibility(View.GONE);
                    enableControls(true);
                }
            }
        });
    }

   private void startMainActivity() {
       Intent mainActivityIntent = new Intent(this, MainActivity.class);
       startActivity(mainActivityIntent);
   }

   private void enableControls(boolean enable) {
       final EditText ipAddressEditText = findViewById(R.id.ipAddressTextField);
       final EditText passwordEditText = findViewById(R.id.password);
       final Button loginButton = findViewById(R.id.login);

       ipAddressEditText.setEnabled(enable);
       passwordEditText.setEnabled(enable);
       loginButton.setEnabled(enable);
   }

    @Override
    public void OnAction(final Object data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingProgressBar.setVisibility(View.GONE);

                if(data instanceof LoginEvent) {
                    LoginEvent loginEvent = (LoginEvent)data;

                    if(!loginEvent.IsSuccessfulLogin()) {
                        Toast.makeText(getApplication().getApplicationContext(), loginEvent.getErrorMessageID(), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        startMainActivity();
                        finish();
                    }
                }

                enableControls(true);
            }
        });
    }
}
