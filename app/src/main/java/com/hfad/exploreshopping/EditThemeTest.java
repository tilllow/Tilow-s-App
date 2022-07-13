package com.hfad.exploreshopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class EditThemeTest extends AppCompatActivity {

    Switch switchChangeTheme;
    SharedPreferences sharedPreferences = null;
    TextView tvThemeChange;
    TextView tvDone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_theme_test);
        switchChangeTheme = findViewById(R.id.switchChangeTheme);
        tvDone = findViewById(R.id.tvDone);
        sharedPreferences = getSharedPreferences("night",0);
        tvThemeChange = findViewById(R.id.tvThemeMode);
        Boolean booleanValue = sharedPreferences.getBoolean("night_mode",true);

        //
        if (booleanValue) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switchChangeTheme.setChecked(true);
        }

//        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//            switchChangeTheme.setChecked(false);
//        } else {
//            switchChangeTheme.setChecked(true);
//        }

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        switchChangeTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    switchChangeTheme.setChecked(true);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night_mode",true);
                    editor.commit();
                    reset();
                    //tvThemeChange.setText("Dark mode");
                    Toast.makeText(EditThemeTest.this,"Light mode is off",Toast.LENGTH_SHORT).show();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    switchChangeTheme.setChecked(false);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night_mode",false);
                    editor.commit();
                    reset();
                    Toast.makeText(EditThemeTest.this,"Dark mode is off",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void reset() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}