package com.hfad.exploreshopping;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hfad.exploreshopping.FragmentEditProfile;
import com.hfad.exploreshopping.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    TextView tvEditProfile;
    TextView tvUsername;
    TextView tvPhoneNumber;
    TextView tvEmailAddress;
    TextView tvResetPassword;
    ImageButton ibEditProfileInfo;
    TextView tvThemeName;
    Switch sbThemeSwitch;
    ImageView ivAccountPicture;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvEditProfile = view.findViewById(R.id.tvEditProfile);
        tvUsername = view.findViewById(R.id.etNameField);
        tvPhoneNumber = view.findViewById(R.id.etPhoneNumberField);
        tvEmailAddress = view.findViewById(R.id.etEmailAddressField);
        tvResetPassword = view.findViewById(R.id.tvResetPassword);
        tvThemeName = view.findViewById(R.id.tvThemeName);
        sbThemeSwitch = view.findViewById(R.id.sbThemeSwitch);
        ivAccountPicture = view.findViewById(R.id.ivAccountPIcture);
        ibEditProfileInfo = view.findViewById(R.id.ibEditProfileInfo);

        //

        populateUserInfo("userPersonalName", tvUsername);
        populateUserInfo("userPhoneNumber", tvPhoneNumber);
        populateUserInfo("userEmailAddress", tvEmailAddress);

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser == null) {
            return;
        }

        ParseFile parseFile = currentUser.getParseFile("ProfileImage");
        if (parseFile == null || parseFile.getUrl() == null) {
            Log.d(TAG, "Nothing was passed in here");
        } else {
            Glide.with(getContext()).load(parseFile.getUrl()).into(ivAccountPicture);
            Log.d(TAG, "This is the file's URL " + parseFile.getUrl());
            Log.d(TAG, "Is it not interesting how this piece of code manages to run all of the time");
        }

        tvResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ForgotPasswordActivity.class);
                intent.putExtra("EXTRA_CODE", 1);
                startActivity(intent);
            }
        });

        ibEditProfileInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContainer, new FragmentEditProfile()).commit();
            }
        });


        tvEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContainer, new FragmentEditProfile()).commit();
            }
        });

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            sbThemeSwitch.setChecked(false);
        } else {
            sbThemeSwitch.setChecked(true);
        }


        sbThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }
        });
    }

    private void populateUserInfo(String attribute, TextView view) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        String temp = currentUser.getString(attribute);
        if (temp != null) {
            view.setText(temp);
        }

    }
}