package com.hfad.exploreshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "SignUpActivity";

    EditText etUsername;
    EditText etPassword;
    EditText etConfirmPassword;
    Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etUsername = findViewById(R.id.etNewUsername);
        etPassword = findViewById(R.id.etNewUserPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String initialPassword = etPassword.getText().toString();
                String finalPassword = etConfirmPassword.getText().toString();

                if (!initialPassword.equals(finalPassword)){
                    // TODO : Display something to the uer when the two passwords do not match.
                    return;
                }

                try{
                    String username = etUsername.getText().toString();
                    String password = initialPassword;
                    ParseUser user = new ParseUser();
                    user.setUsername(username);
                    user.setPassword(password);

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){
                                Toast.makeText(SignUpActivity.this,"Hooray, your new account has been set up successfully",Toast.LENGTH_SHORT)
                                        .show();
                                launchSignInPage();
                            } else{
                                // TODO : Display something to the user so that he or she can create a brand new account.
                            }
                        }
                    });

                } catch (Exception e){
                    Log.e(TAG,"Login wasn't successful : ", e);
                }
            }
        });
    }

    private void launchSignInPage(){
        Intent i = new Intent(this,SignInActivity.class);
        startActivity(i);
        //finish();
    }


}