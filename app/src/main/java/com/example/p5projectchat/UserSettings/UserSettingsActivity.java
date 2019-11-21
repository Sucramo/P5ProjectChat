package com.example.p5projectchat.UserSettings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        final TextView fname = (TextView) findViewById(R.id.get_first_name);
        final TextView lname = (TextView) findViewById(R.id.get_last_name);
        final TextView email = (TextView) findViewById(R.id.get_email);
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

    public void editProfileButton(View view){
        startActivity(new Intent(this, EditUserActivity.class));
        finish();
    }

}
