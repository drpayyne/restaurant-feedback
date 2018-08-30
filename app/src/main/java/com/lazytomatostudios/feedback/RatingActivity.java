package com.lazytomatostudios.feedback;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

public class RatingActivity extends AppCompatActivity {

    RatingBar ratingBar;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        Log.d("Manick", getIntent().getExtras().getString("number") + "");
        Log.d("Manick", getIntent().getExtras().getString("waiter") + "");
        Log.d("Manick", getIntent().getExtras().getString("table_no") + "");
        Log.d("Manick", getIntent().getExtras().getString("date") + "");

        ratingBar = findViewById(R.id.r1);
        submitButton = findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Manick", "STARS " + String.valueOf(ratingBar.getRating()));
            }
        });
    }
}
