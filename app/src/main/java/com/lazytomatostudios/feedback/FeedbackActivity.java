package com.lazytomatostudios.feedback;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lazytomatostudios.feedback.db.Database;
import com.lazytomatostudios.feedback.db.entity.User;
import com.lazytomatostudios.feedback.db.entity.Waiter;
import com.lazytomatostudios.feedback.helper.FullscreenBugWorkaround;

import es.dmoral.toasty.Toasty;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class FeedbackActivity extends AppCompatActivity{

    String TAG = "Steve", user_name, user_phone, user_mail, user_dob, user_doa, input;
    Database database;
    User user;
    Button button;
    LayoutInflater inflater;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    View layout;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        editText = findViewById(R.id.edittext_table);

        database = Database.getDatabase(this);
        button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input=editText.getText().toString();
                Log.d(TAG, input + "********");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        user = database.userDao().read(input);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(user == null) {
                                    Log.d("Steve", "not found");
                                    inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                    layout = inflater.inflate(R.layout.dialogue_create_user, null);
                                    final EditText name = layout.findViewById(R.id.user_name);
                                    final EditText phone = layout.findViewById(R.id.user_phone);
                                    final EditText email = layout.findViewById(R.id.user_mail);
                                    final EditText dob = layout.findViewById(R.id.user_dob);
                                    final EditText doa = layout.findViewById(R.id.user_doa);
                                    phone.setText(input);
                                    //Building dialog
                                    builder = new AlertDialog.Builder(FeedbackActivity.this);
                                    builder.setView(layout);
                                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            user_name = name.getText().toString().trim();
                                            user_phone = phone.getText().toString().trim();
                                            user_mail = email.getText().toString().trim();
                                            user_dob = dob.getText().toString().trim();
                                            user_doa = doa.getText().toString().trim();
                                            Log.d(TAG, "User details : ");
                                            Log.d(TAG, String.valueOf(user_name == null));
                                            Log.d(TAG, user_phone + "~");
                                            Log.d(TAG, user_mail + "~");
                                            Log.d(TAG, user_dob + "~");
                                            Log.d(TAG, user_doa + "~");
                                            if (user_name == "" || user_phone == "" || user_mail == "" || user_dob == "") {
                                                Toasty.error(getApplicationContext(), "Enter all details!", Toast.LENGTH_SHORT, true).show();
                                            } else {
                                                Log.d(TAG, "Creating thread for database query");
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Log.d(TAG, "Initiating database query");
                                                        user = new User();
                                                        user.setName(user_name);
                                                        user.setPhone_number(user_phone);
                                                        user.setDoa(user_doa);
                                                        user.setDob(user_dob);
                                                        user.setMail(user_mail);

                                                        database.userDao().create(user);
                                                        Log.d(TAG, "Database query completed");
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toasty.success(getApplicationContext(), "Created new customer", Toast.LENGTH_SHORT, true).show();
                                                                Intent intent = new Intent(FeedbackActivity.this, RatingActivity.class);
                                                                intent.putExtra("waiter", getIntent().getExtras().getString("waiter"));
                                                                intent.putExtra("table_no",getIntent().getExtras().getString("table_no"));
                                                                intent.putExtra("date",getIntent().getExtras().getString("date"));
                                                                intent.putExtra("number", user_phone);
                                                                startActivity(intent);
                                                            }
                                                        });
                                                    }
                                                }).start();
                                                Log.d(TAG, "Dismissing dialog");
                                                alertDialog.dismiss();
                                            }
                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            alertDialog.dismiss();
                                        }
                                    });
                                    alertDialog = builder.create();
                                    alertDialog.show();
                                } else {
                                    Log.d("Steve",user.getPhone_number() + user.getName() + user.getMail() + user.getDoa() + user.getDob());
                                    Intent intent = new Intent(FeedbackActivity.this, RatingActivity.class);
                                    intent.putExtra("waiter", getIntent().getExtras().getString("waiter"));
                                    intent.putExtra("table_no",getIntent().getExtras().getString("table_no"));
                                    intent.putExtra("date",getIntent().getExtras().getString("date"));
                                    intent.putExtra("number", user.getPhone_number());
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }

}
