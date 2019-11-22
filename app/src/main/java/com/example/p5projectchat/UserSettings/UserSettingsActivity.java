package com.example.p5projectchat.UserSettings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.p5projectchat.Database.User;
import com.example.p5projectchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserSettingsActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;

    private static int RESULT_LOAD_IMAGE = 1;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        final TextView fname = (TextView) findViewById(R.id.get_first_name);
        final TextView lname = (TextView) findViewById(R.id.get_last_name);
        final TextView email = (TextView) findViewById(R.id.get_email);
        ImageButton profilePic = (ImageButton) findViewById(R.id.user_image_button);
        Button editProfile = (Button) findViewById(R.id.edit_user_button);

        database = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uid = firebaseUser.getUid();
                User user = dataSnapshot.child(uid).getValue(User.class);
                fname.setText(user.getFirstName());
                lname.setText(user.getLastName());
                email.setText(user.getEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void editProfilePicture(View view){
        if (checkGalleryPermissions()){
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RESULT_LOAD_IMAGE);
        } else {
            Toast failureToast = Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT);
            failureToast.show();
        }
    }

    public void editProfileButton(View view){
        startActivity(new Intent(this, EditUserActivity.class));
        finish();
    }

    public void changeProfilePicture(View view){

    }

    private boolean checkGalleryPermissions(){
        // Here, this is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                return false;
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        RESULT_LOAD_IMAGE);
                return true;
            }
        } else {
            // Permission has already been granted
            return true;
        }
    }

}
