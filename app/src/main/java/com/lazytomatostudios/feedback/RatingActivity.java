package com.lazytomatostudios.feedback;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.lazytomatostudios.feedback.db.Database;
import com.lazytomatostudios.feedback.db.entity.Feedback;
import com.rengwuxian.materialedittext.MaterialEditText;

import es.dmoral.toasty.Toasty;

public class RatingActivity extends AppCompatActivity {

    String TAG="Steve", phone, table, date, waiter, comments;
    float q1_rating, q2_rating, q3_rating, q4_rating, q5_rating;
    RatingBar ratingBar1, ratingBar2, ratingBar3, ratingBar4, ratingBar5;
    MaterialEditText commentEditText;
    Feedback feedback;
    Button submitButton;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        database = Database.getDatabase(this);

        phone= getIntent().getExtras().getString("number");
        waiter= getIntent().getExtras().getString("waiter");
        table=getIntent().getExtras().getString("table_no");
        date= getIntent().getExtras().getString("date");

        ratingBar1 = findViewById(R.id.r1);
        ratingBar2 = findViewById(R.id.r2);
        ratingBar3 = findViewById(R.id.r3);
        ratingBar4 = findViewById(R.id.r4);
        ratingBar5 = findViewById(R.id.r5);
        commentEditText = findViewById(R.id.comments_edit_text);

        submitButton = findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            q1_rating= ratingBar1.getRating();
            q2_rating= ratingBar2.getRating();
            q3_rating= ratingBar3.getRating();
            q4_rating= ratingBar4.getRating();
            q5_rating= ratingBar5.getRating();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    feedback = new Feedback();
                    feedback.setDate(date);
                    feedback.setPhone(phone);
                    feedback.setTable(table);
                    feedback.setWaiter(waiter);
                    feedback.setQ1_rating(q1_rating);
                    feedback.setQ2_rating(q2_rating);
                    feedback.setQ3_rating(q3_rating);
                    feedback.setQ4_rating(q4_rating);
                    feedback.setQ5_rating(q5_rating);
                    database.feedbackDao().create(feedback);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toasty.success(getApplicationContext(), "Feedback submitted!", Toast.LENGTH_SHORT, true).show();
                            Intent intent = new Intent(RatingActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                }
            }).start();
            }
        });
    }
}
