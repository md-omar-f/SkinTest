package com.example.skintest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    private CardView cv, helpCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cv = findViewById(R.id.scanCard);
        helpCV = findViewById(R.id.helpCard);

        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        Intent intentToHelp = new Intent(HomeActivity.this, HelpActivity.class);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

        helpCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentToHelp);
            }
        });
    }
}