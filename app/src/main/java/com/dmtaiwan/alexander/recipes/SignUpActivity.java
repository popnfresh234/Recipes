package com.dmtaiwan.alexander.recipes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by Alexander on 4/7/2015.
 */
public class SignUpActivity extends ActionBarActivity implements View.OnClickListener {

    EditText mUsernameEditText;
    EditText mPasswordEditText;
    EditText mEmailEditText;
    Button mSignupButton;
    Button mCancelButton;
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mContext = this;
        mUsernameEditText = (EditText) findViewById(R.id.text_view_signup_username);
        mPasswordEditText = (EditText) findViewById(R.id.text_view_signup_password);
        mEmailEditText = (EditText) findViewById(R.id.text_view_signup_email);
        mSignupButton = (Button) findViewById(R.id.button_signup_signup);
        mCancelButton = (Button) findViewById(R.id.button_signup_cancel);

        mSignupButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_signup_signup:
                //Create new user
                ParseUser newUser = new ParseUser();

                //Trim strings from Edit Text Fields
                String usernameTrimmed = mUsernameEditText.getText().toString().trim();
                String passwordTrimmed = mPasswordEditText.getText().toString().trim();
                String emailTrimmed = mEmailEditText.getText().toString().trim();

                //Check if any fields are blank

                if (usernameTrimmed.isEmpty() | passwordTrimmed.isEmpty() || emailTrimmed.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show();
                } else {
                    newUser.setUsername(usernameTrimmed);
                    newUser.setPassword(passwordTrimmed);
                    newUser.setEmail(emailTrimmed);

                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                //Success
                                Intent i = new Intent(mContext, HomeActivity.class);
                                startActivity(i);

                            } else {
                                //failure
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                break;
            case R.id.button_signup_cancel:
                finish();
                break;
        }

    }
}
