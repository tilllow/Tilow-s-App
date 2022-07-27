package com.hfad.exploreshopping;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.io.File;

public class FragmentEditProfile extends Fragment {

    public static final String TAG = "FragmentEditProfile";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    TextView tvDone;
    EditText etNameField;
    EditText etPhoneNumberField;
    EditText etEmailAddressField;
    ImageButton ibTakePhoto;
    ImageView ivAccountPicture;
    TextView tvResetPasswordEditProfile;
    TextView tvLogoutEditProfile;
    ImageButton ibEditProfile;
    public String photoFilename = "photo.jpg";
    private File photoFile;

    public FragmentEditProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvDone = view.findViewById(R.id.tvDone);
        etNameField = view.findViewById(R.id.etNameField);
        etPhoneNumberField = view.findViewById(R.id.etPhoneNumberField);
        etEmailAddressField = view.findViewById(R.id.etEmailAddressField);
        ibTakePhoto = view.findViewById(R.id.ibTakePhoto);
        ivAccountPicture = view.findViewById(R.id.ivEditAccountPIcture);

        tvResetPasswordEditProfile = view.findViewById(R.id.tvResetPasswordEditProfile);
        ibEditProfile = view.findViewById(R.id.ibEditProfile);
        tvLogoutEditProfile = view.findViewById(R.id.tvLogoutEditProfile);

        populateUserInfo("userPersonalName", etNameField);
        populateUserInfo("userPhoneNumber", etPhoneNumberField);
        populateUserInfo("userEmailAddress", etEmailAddressField);


        ParseUser currentUser = ParseUser.getCurrentUser();
        etNameField.setText(currentUser.getString("userPersonalName"));
        etEmailAddressField.setText(currentUser.getString("userEmailAddress"));
        etPhoneNumberField.setText(currentUser.getString("userPhoneNumber"));
        ParseFile parseFile = currentUser.getParseFile("ProfileImage");

        if (parseFile != null) {
            Glide.with(getContext()).load(parseFile.getUrl()).into(ivAccountPicture);
        } else {
            ivAccountPicture.setImageResource(R.drawable.ic_baseline_person_24);
        }

        tvLogoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground();
                Intent intent = new Intent(getContext(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });


        ibEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isEnabled = etNameField.isEnabled();

                if (isEnabled){
                    ibEditProfile.setImageResource(R.drawable.ic_edit);
                    etNameField.setEnabled(false);
                    etPhoneNumberField.setEnabled(false);
                    etEmailAddressField.setEnabled(false);

                    String updatedName = etNameField.getText().toString();
                    String updatedPhoneNumber = etPhoneNumberField.getText().toString();
                    String updatedEmail = etEmailAddressField.getText().toString();

                    ParseUser currentUser = ParseUser.getCurrentUser();
                    currentUser.put("userPersonalName",updatedName);
                    currentUser.put("userPhoneNumber",updatedPhoneNumber);
                    currentUser.put("userEmailAddress",updatedEmail);
                    currentUser.saveInBackground();
                } else{
                    ibEditProfile.setImageResource(R.drawable.ic_save_changes);
                    etNameField.setEnabled(true);
                    etPhoneNumberField.setEnabled(true);
                    etEmailAddressField.setEnabled(true);
                }


            }
        });

        tvResetPasswordEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ForgotPasswordActivity.class);
                intent.putExtra("EXTRA_CODE", 1);
                startActivity(intent);
            }
        });


        ibTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
                ParseFile file = new ParseFile(photoFile);
                //Log.d("This is the file ")
                //currentUser.put("ProfileImage",file);
                //currentUser.saveInBackground();
            }
        });

    }

    private void launchCamera() {
        Log.i(TAG, "launchCamera: ");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a file reference to access to future access
        photoFile = getPhotoFileUri(photoFilename);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                Matrix matrix = new Matrix();
                matrix.setRotate(90);
                takenImage = Bitmap.createBitmap(takenImage, 0, 0, takenImage.getWidth(), takenImage.getHeight(), matrix, true);
                Log.d(TAG,"This is the taken image : " + takenImage);
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                //ImageView ivPreview = (ImageView) findViewById(R.id.ivPost);

                ivAccountPicture.setImageBitmap(takenImage);
                ParseUser currentUser = ParseUser.getCurrentUser();
                currentUser.put("ProfileImage",new ParseFile(photoFile));
                currentUser.saveInBackground();
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void populateUserInfo(String attribute, EditText view) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        String temp = currentUser.getString(attribute);
        if (temp != null) {
            view.setText(temp);
        }

    }

}