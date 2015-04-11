package com.dmtaiwan.alexander.recipes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Alexander on 4/7/2015.
 */
public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    EditText mUsernameEditText;
    EditText mPasswordEditText;
    Button mLoginButton;
    Button mFacebookButton;
    TextView mSignUpText;
    TextView mForgotPasswordText;
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;


        mUsernameEditText = (EditText) findViewById(R.id.text_view_login_username);
        mPasswordEditText = (EditText) findViewById(R.id.text_view_login_password);
        mLoginButton = (Button) findViewById(R.id.button_login_login);
        mSignUpText = (TextView) findViewById(R.id.text_view_login_signup);
        mForgotPasswordText = (TextView) findViewById(R.id.text_view_login_forgot_passowrd);

        //TODO facebook integration
//        mFacebookButton = (Button) findViewById(R.id.button_login_facebook);
//        mFacebookButton.setOnClickListener(this);

        mLoginButton.setOnClickListener(this);
        mSignUpText.setOnClickListener(this);
        mForgotPasswordText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_login_login:
                String username = mUsernameEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null) {
                            Intent i = new Intent(mContext, HomeActivity.class);
                            startActivity(i);
                        }else {
                            Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
            case R.id.text_view_login_signup:
                Intent i = new Intent(this, SignUpActivity.class);
                startActivity(i);
                break;
            case R.id.text_view_login_forgot_passowrd:
                break;
        }
    }
}
