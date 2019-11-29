package com.example.p5projectchat.UserSettings;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.p5projectchat.Database.User;
import com.example.p5projectchat.Login.CreateAccountActivity;
import com.example.p5projectchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;

public class EditUserActivity extends AppCompatActivity {

    String picturePath = null;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private EditText editPassword;
    private EditText repeatPassword;
    private EditText oldPasswordText;

    private Button saveBtn;
    private Button changePictureBtn;

    private ImageView profilePicture;
    private Bitmap loadedBitmap;
    User user;

    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference myDatabaseRef;
    private String profilePictureRef;
    private File profilePictureFile;
    public String databaseOldPassword;

    private static int RESULT_LOAD_IMAGE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        editFirstName = (EditText) findViewById(R.id.edit_first_name);
        editLastName = (EditText) findViewById(R.id.edit_last_name);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.editPassword);
        repeatPassword = (EditText) findViewById(R.id.repeatEditPassword);
        oldPasswordText = (EditText) findViewById(R.id.oldPassword);

        saveBtn = (Button) findViewById(R.id.saveButton);
        changePictureBtn = (Button) findViewById(R.id.change_profile_picture_button);

        FirebaseAuth.AuthStateListener mAuthListener;

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myDatabaseRef = database.getReference();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uid = firebaseUser.getUid();
                user = dataSnapshot.child(uid).getValue(User.class);
                if (firebaseUser != null){
                    if (user != null){
                        editFirstName.setText(user.getFirstName());
                        editLastName.setText(user.getLastName());
                        editEmail.setText(user.getEmail());
                        databaseOldPassword = user.getPassword();
                        profilePictureRef = user.getProfilePicture();
                        Log.i("Firebase", "Profile picture reference: " + profilePictureRef);
                    }
                    if (profilePictureRef != null){
                        profilePictureFile = new File(profilePictureRef);
                        Log.i("PROFILE PICTURE FILE", profilePictureFile.toString());
                        if (profilePictureFile.exists()){
                            profilePicture.setImageBitmap(BitmapFactory.decodeFile(profilePictureRef));
                            Log.i("PROFILE PICTURE FILE", profilePictureFile.toString());
                        } else {
                            Toast.makeText(EditUserActivity.this, "Couldn't load the profile picture... " + "Please change your profile picture", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void setSaveBtn (View view){
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPass = repeatPassword.getText().toString().trim();
        String oldPass = oldPasswordText.getText().toString().trim();

        if (!password.isEmpty() || !confirmPass.isEmpty() || !oldPass.isEmpty()){

            if (oldPass.equals(databaseOldPassword)){

                if (password.length() < 8){
                    Toast.makeText(getApplicationContext(), "The new password need to be 8 characters long", Toast.LENGTH_SHORT).show();

                } else if (!password.equals(confirmPass)){
                    Toast.makeText(getApplicationContext(), "The password is not matching", Toast.LENGTH_SHORT).show();

                } else if (!CreateAccountActivity.isValidEmail(email)){
                    Toast.makeText(getApplicationContext(), "The email is not valid", Toast.LENGTH_SHORT).show();

                } else {
                    myDatabaseRef.child("users").child(firebaseUser.getUid()).child("firstName").setValue(editFirstName.getText().toString().trim()); // Changing the value from edit profile in database
                    myDatabaseRef.child("users").child(firebaseUser.getUid()).child("lastName").setValue(editLastName.getText().toString().trim());
                    myDatabaseRef.child("users").child(firebaseUser.getUid()).child("email").setValue(editEmail.getText().toString().trim());
                    myDatabaseRef.child("users").child(firebaseUser.getUid()).child("password").setValue(editPassword.getText().toString().trim());
                    Toast.makeText(getApplicationContext(), "User info is saved", Toast.LENGTH_LONG).show();
                }
            } else{
                Toast.makeText(getApplicationContext(), "The old password does not fit", Toast.LENGTH_SHORT).show();

            }
        } else {
            myDatabaseRef.child("users").child(firebaseUser.getUid()).child("firstName").setValue(editFirstName.getText().toString().trim()); // Changing the value from edit profil in database
            myDatabaseRef.child("users").child(firebaseUser.getUid()).child("lastName").setValue(editLastName.getText().toString().trim());
            myDatabaseRef.child("users").child(firebaseUser.getUid()).child("email").setValue(editEmail.getText().toString().trim());
            //user.setProfilePicture(profilePictureRef);
            myDatabaseRef.child("users").child(firebaseUser.getUid()).child("profilePicture").setValue(picturePath);
            Log.i("PROFILE PICTURE REF", "REF "+ profilePictureRef);
            Toast.makeText(getApplicationContext(), "User info is saved", Toast.LENGTH_LONG).show();
        }
    }

    public void addProfilePictureBtnClicked(View view){
        if(checkGalleryPermissions()){
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees){
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private boolean checkGalleryPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                return false;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RESULT_LOAD_IMAGE);
                return true;
            }
        } else {
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);

            cursor.close();
            Log.i("PICTURE PATH: ", picturePath.toString());

            loadedBitmap = BitmapFactory.decodeFile(picturePath);
            Log.i("LOADED BITMAP: ", loadedBitmap.toString());


            ExifInterface exif = null;
            try {
                File pictureFile = new File(picturePath);
                exif = new ExifInterface(pictureFile.getAbsoluteFile());

            } catch (IOException e){
                e.printStackTrace();
            }

            int orientation = ExifInterface.ORIENTATION_NORMAL;

            if (exif != null){
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            }

            switch (orientation){

                case ExifInterface.ORIENTATION_ROTATE_90:
                    loadedBitmap = rotateBitmap(loadedBitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    loadedBitmap = rotateBitmap(loadedBitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    loadedBitmap = rotateBitmap(loadedBitmap, 270);
                    break;

            }

            profilePicture.setImageBitmap(loadedBitmap);
            user.setProfilePicture(loadedBitmap.toString());

            Log.i("PROFILE PICTURE PATH: ", profilePicture.toString());

        }
    }

}
