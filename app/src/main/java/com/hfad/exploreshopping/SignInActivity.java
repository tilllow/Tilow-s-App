package com.hfad.exploreshopping;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class SignInActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    AppCompatButton btnLogin;
    AppCompatButton btnSignUp;
    public static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        if(ParseUser.getCurrentUser() != null){
            launchMainActivity();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username,password);
            }
        });
        
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignUpPage();
            }
        });

    }

    private void launchSignUpPage() {
        Intent i = new Intent(this,SignUpActivity.class);
        startActivity(i);
    }

    private void loginUser(String username, String password){
        Log.i(TAG,"Attempting to login user " + username);
        //Toast.makeText(SignInActivity.this,"Hi, I am a toast",Toast.LENGTH_SHORT).show();
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null){
                    Log.i(TAG,"Issue with login",e);
                    return;
                }
                // TODO: navigate to the main activity if the user has signed in properly
                Log.i(TAG,"Login wasn't successful");
                Toast.makeText(SignInActivity.this,"Success!",Toast.LENGTH_SHORT).show();
                launchMainActivity();
            }
        });

    }



    private void launchMainActivity() {
        Intent i = new Intent(SignInActivity.this,MainActivity.class);
        startActivity(i);
    }
}