package com.hfad.exploreshopping;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ForgotPasswordActivity extends AppCompatActivity {

    AppCompatButton btnResetPassword;
    EditText etResetEmailAddress;
    TextView tvSignInPageNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Integer code = getIntent().getIntExtra("EXTRA_CODE",0);

        btnResetPassword = findViewById(R.id.btnResetPassword);
        etResetEmailAddress = findViewById(R.id.etResetEmailAddress);
        tvSignInPageNavigator = findViewById(R.id.tvSignInPageNavigator);

        if (code == 1){
            tvSignInPageNavigator.setText("Finish");
        }

        tvSignInPageNavigator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code == 1){
                    finish();
                    return;
                }

                Intent intent = new Intent(ForgotPasswordActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = etResetEmailAddress.getText().toString();

                ParseUser.requestPasswordResetInBackground(emailAddress, new RequestPasswordResetCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            if (code == 1){
                                alertDisplayer("Password reset","Verification email sent + \nFollow instructions to reset the account password");
                                return;
                            }
                            alertDisplayer("Password reset","Verification email sent" +
                                    "\nNavigate back to the sign in page to log into your account");
                        } else{
                            alertDisplayer("Password reset",e.getMessage());
                        }
                    }
                });
            }
        });
    }



    private void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}