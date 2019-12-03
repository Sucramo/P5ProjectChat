package com.example.p5projectchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.p5projectchat.Login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();


    }

    public void signIn(View view){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        // check if the user is signed in and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

}
