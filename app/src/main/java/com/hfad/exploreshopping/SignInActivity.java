package com.hfad.exploreshopping;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.CollectionMapper;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.facebook.ParseFacebookUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;

public class SignInActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    AppCompatButton btnLogin;
    TextView tvSignUp;
    AppCompatButton btnFacebook;
    public static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

//        if(ParseUser.getCurrentUser() != null){
//            launchMainActivity();
//        }



        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etUserPassword);
        btnLogin = findViewById(R.id.btnLoginToShop);
        tvSignUp = findViewById(R.id.tvSignUp);
        btnFacebook = findViewById(R.id.btnFacebook);

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collection<String> permissions = Arrays.asList("public_profile","email");
                ParseFacebookUtils.logInWithReadPermissionsInBackground(SignInActivity.this, permissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e != null){
                            ParseUser.logOut();
                            Log.e(TAG,"Error",e);
                        }

                        if (user == null){
                            ParseUser.logOut();
                            Toast.makeText(SignInActivity.this,"The user cancelled the Facebook Login",Toast.LENGTH_SHORT).show();
                        } else if (user.isNew()){
                            Toast.makeText(SignInActivity.this,"User signed up and logged in through Facebook",Toast.LENGTH_SHORT).show();
//                            launchMainActivity();
                            getUserDetailFromFB();

                        } else{
                            Toast.makeText(SignInActivity.this,"User logged in through Facebook",Toast.LENGTH_SHORT).show();
//                            launchMainActivity();
                            getUserDetailFromParse();
                        }
                    }
                });
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username,password);
            }
        });
        
//        btnSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                launchSignUpPage();
//            }
//        });

    }

    private void getUserDetailFromParse() {
        ParseUser user = ParseUser.getCurrentUser();
        String title = "Welcome Back";
        String message = user.getUsername() + "\n";
        alertDisplayer(title,message);
    }

    private void alertDisplayer(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                launchMainActivity();
            }
        });
        builder.show();
    }

    private void getUserDetailFromFB() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                ParseUser user = ParseUser.getCurrentUser();
                try {
                    user.setUsername(jsonObject.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    user.setEmail(jsonObject.getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        alertDisplayer("First time Login","Welcome!");
                    }
                });
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","name,email");
        request.setParameters(parameters);
        request.executeAsync();

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
                Log.i(TAG,"Login was successful");
                Toast.makeText(SignInActivity.this,"Success!",Toast.LENGTH_SHORT).show();
                launchMainActivity();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode,resultCode,data);
    }

    private void launchMainActivity() {
        Log.d(TAG,"I am in the launch MainActivity section");
        Intent i = new Intent(SignInActivity.this,MainActivity.class);
        startActivity(i);
    }
}