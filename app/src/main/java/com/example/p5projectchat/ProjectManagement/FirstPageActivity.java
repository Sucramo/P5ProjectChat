package com.example.p5projectchat.ProjectManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.p5projectchat.R;
import com.example.p5projectchat.UserSettings.EditUserActivity;
import com.example.p5projectchat.UserSettings.UserSettingsActivity;

import java.lang.Object;

public class FirstPageActivity extends AppCompatActivity {

    //AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder

    Button userSettings;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        userSettings = (Button) findViewById(R.id.user_settings_btn);


    }

    public void openUserSettings(View view){
        startActivity(new Intent(this, UserSettingsActivity.class));
    }


}
