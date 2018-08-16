package com.filippoints.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.filippoints.R;

/**
 * Created by hlib on 8/16/18.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String LOGIN_ACTIVITY_TAG = "LoginActivity";
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile currentProfile = Profile.getCurrentProfile();
                if (currentProfile != null) {
                    Log.i(LOGIN_ACTIVITY_TAG, String.format("%s %s logged in successfully!",
                            currentProfile.getFirstName(), currentProfile.getLastName()));
                }
                Intent intent = ChoosePointsActivity.buildIntent(LoginActivity.this);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                // App code
                Log.w(LOGIN_ACTIVITY_TAG, "Facebook login cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.w(LOGIN_ACTIVITY_TAG, "Facebook login failed");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
