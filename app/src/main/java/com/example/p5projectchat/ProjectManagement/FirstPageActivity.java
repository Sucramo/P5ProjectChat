package com.example.p5projectchat.ProjectManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import com.example.p5projectchat.R;
import com.example.p5projectchat.Tabs.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.lang.Object;

public class FirstPageActivity extends AppCompatActivity {

    //AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}
