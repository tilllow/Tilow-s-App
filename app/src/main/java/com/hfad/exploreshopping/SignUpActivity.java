package com.hfad.exploreshopping;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "SignUpActivity";

    EditText etUsername;
    EditText etPassword;
    EditText etUserEmail;
    EditText etConfirmPassword;
    Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etInputUserPassword);
        etConfirmPassword = findViewById(R.id.etInputConfirmPassword);
        btnCreateAccount = findViewById(R.id.btnSignUp);
        etUserEmail = findViewById(R.id.etUserEmail);
        TextView tvHasAccount = findViewById(R.id.tvHasAccount);

        tvHasAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(i);
            }
        });

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

                    ParseUser user = new ParseUser();
                    user.setUsername(etUsername.getText().toString());
                    user.setPassword(initialPassword);
                    user.setEmail(etUserEmail.getText().toString());

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){
                                Log.d(TAG,"The account has been successfully created");
                                ParseUser.logOut();
                                alertDisplayer("Account Created Successfully!","Please verify your email before login",false);
                            } else{
                                ParseUser.logOut();
                                alertDisplayer("Error Account Creation failed","Account could not be created" + " :" + e.getMessage(),true);
                                Log.d(TAG,"Unable to create account");
                                // TODO : Display something to the user so that he or she can create a brand new account.
                            }
                        }
                    });

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    private void alertDisplayer(String title,String message, final boolean error){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (!error){
                            Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            launchSignInPage();
                        }
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    private void launchSignInPage(){
        Intent i = new Intent(this,SignInActivity.class);
        startActivity(i);
        //finish();
    }


}