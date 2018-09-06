package com.lazytomatostudios.feedback;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.lazytomatostudios.feedback.db.Database;
import com.lazytomatostudios.feedback.db.entity.Feedback;
import com.lazytomatostudios.feedback.helper.FullscreenBugWorkaround;
import com.rengwuxian.materialedittext.MaterialEditText;

import es.dmoral.toasty.Toasty;
import fr.ganfra.materialspinner.MaterialSpinner;

public class RatingActivity extends AppCompatActivity {

    String TAanG="Steve", phone, table, date, waiter, comments, frequency, waitTime;
    float q1_rating, q2_rating, q3_rating, q4_rating, q5_rating;
    RatingBar ratingBar1, ratingBar2, ratingBar3, ratingBar4, ratingBar5;
    MaterialEditText commentEditText;
    Feedback feedback;
    Button submitButton;
    Database database;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    MaterialSpinner frequencySpinner, waitTimeSpinner;
    ArrayAdapter<String> frequencyAdapter, waitTimeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        FullscreenBugWorkaround.assistActivity(this);

        database = Database.getDatabase(this);
        sharedPreferences = getApplicationContext().getSharedPreferences("feedback", 0);

        phone = getIntent().getExtras().getString("number");
        waiter = getIntent().getExtras().getString("waiter");
        table = getIntent().getExtras().getString("table_no");
        date = getIntent().getExtras().getString("date");

        ratingBar1 = findViewById(R.id.r1);
        ratingBar2 = findViewById(R.id.r2);
        ratingBar3 = findViewById(R.id.r3);
        ratingBar4 = findViewById(R.id.r4);
        ratingBar5 = findViewById(R.id.r5);
        commentEditText = findViewById(R.id.comments_edit_text);
        frequencySpinner = findViewById(R.id.frequency_spinner);
        waitTimeSpinner = findViewById(R.id.wait_time_spinner);

        frequencyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.frequency_of_visit));
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner = findViewById(R.id.frequency_spinner);
        frequencySpinner.setAdapter(frequencyAdapter);

        waitTimeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.expected_wait_time));
        waitTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waitTimeSpinner = findViewById(R.id.wait_time_spinner);
        waitTimeSpinner.setAdapter(waitTimeAdapter);

        submitButton = findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q1_rating = ratingBar1.getRating();
                q2_rating = ratingBar2.getRating();
                q3_rating = ratingBar3.getRating();
                q4_rating = ratingBar4.getRating();
                q5_rating = ratingBar5.getRating();
                comments = commentEditText.getText().toString();
                //frequency = frequencySpinner.getSelectedItem().toString();
                //waitTime = waitTimeSpinner.getSelectedItem().toString();

                if (q1_rating == 0.0 || q2_rating == 0.0 || q3_rating == 0.0 || q4_rating == 0.0 || q5_rating == 0.0 || frequencySpinner.getSelectedItem() == null || waitTimeSpinner.getSelectedItem() == null) {
                    Toasty.error(getApplicationContext(), "Please enter all details", Toast.LENGTH_SHORT, true).show();
                } else {
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
                            feedback.setComments(comments);
                            feedback.setFrequency(frequency);
                            feedback.setWait_time(waitTime);
                            database.feedbackDao().create(feedback);
                            editor = sharedPreferences.edit();
                            editor.putInt("new_feedbacks", sharedPreferences.getInt("new_feedbacks", 0) + 1);
                            editor.apply();
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
            }
        });
    }
}
