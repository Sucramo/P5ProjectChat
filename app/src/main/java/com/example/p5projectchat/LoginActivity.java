package com.example.p5projectchat;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {


    EditText username;
    EditText password;

    Button loginBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        loginBtn = (Button)findViewById(R.id.login);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                    Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
                    // set activity redirection

                } else{
                    Toast.makeText(getApplicationContext(), "Wrong credentials", Toast.LENGTH_SHORT).show();

                    username.setVisibility(View.VISIBLE);
                    username.setBackgroundColor(Color.RED);

                    password.setVisibility(View.VISIBLE);
                    password.setBackgroundColor(Color.RED);

                }
            }
        });
    }

    public void login(View view){
        if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
            //correct password
        } else{
            //wrong password
        }
    }



}
