package com.lazytomatostudios.feedback;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.lazytomatostudios.feedback.db.Database;
import com.lazytomatostudios.feedback.db.entity.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import es.dmoral.toasty.Toasty;

public class FeedbackActivity extends AppCompatActivity{

    String TAG = "Steve", user_name, user_phone, user_mail, user_dob, user_doa, input;
    Database database;
    User user;
    FloatingActionButton button;
    LayoutInflater inflater;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    View layout;
    MaterialEditText editText;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        database = Database.getDatabase(this);
        sharedPreferences = getApplicationContext().getSharedPreferences("feedback", MODE_PRIVATE);

        editText = findViewById(R.id.edittext_phone);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = editText.getText().toString().trim();
                if (input.length() != 10) {
                    editText.setError("Enter a valid phone number");
                } else {
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
                                        final MaterialEditText name = layout.findViewById(R.id.user_name);
                                        final MaterialEditText phone = layout.findViewById(R.id.user_phone);
                                        final MaterialEditText email = layout.findViewById(R.id.user_mail);
                                        final MaterialEditText dob = layout.findViewById(R.id.user_dob);
                                        final MaterialEditText doa = layout.findViewById(R.id.user_doa);
                                        final FloatingActionButton birthday = layout.findViewById(R.id.button_birthday);
                                        final FloatingActionButton anniversary = layout.findViewById(R.id.button_anniversary);

                                        birthday.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                new DatePickerDialog(FeedbackActivity.this, new DatePickerDialog.OnDateSetListener() {
                                                    @Override
                                                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                                        String text = i2 + "/" + (i1 + 1) + "/" + i;
                                                        dob.setText(text);
                                                    }
                                                }, 1990, 1, 1).show();
                                            }
                                        });

                                        anniversary.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                new DatePickerDialog(FeedbackActivity.this, new DatePickerDialog.OnDateSetListener() {
                                                    @Override
                                                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                                        String text = i2 + "/" + (i1 + 1) + "/" + i;
                                                        doa.setText(text);
                                                    }
                                                }, 1990, 1, 1).show();
                                            }
                                        });

                                        phone.setText(input);
                                        //Building dialog
                                        builder = new AlertDialog.Builder(FeedbackActivity.this);
                                        builder.setView(layout);
                                        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //OVERRIDDEN LATER
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
                                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                user_name = name.getText().toString().trim();
                                                user_phone = phone.getText().toString().trim();
                                                user_mail = email.getText().toString().trim();
                                                user_dob = dob.getText().toString().trim();
                                                user_doa = doa.getText().toString().trim();
                                                Log.d(TAG, "User details : ");
                                                Log.d(TAG, String.valueOf(user_name.equals("")));
                                                Log.d(TAG, user_phone + "~");
                                                Log.d(TAG, user_mail + "~");
                                                Log.d(TAG, user_dob + "~");
                                                Log.d(TAG, user_doa + "~");
                                                if(user_name.equals("")) {
                                                    name.setError("Please enter your name");
                                                }
                                                if(user_phone.equals("")) {
                                                    phone.setError("Please enter your name");
                                                }
                                                if(user_mail.equals("")) {
                                                    email.setError("Please enter your name");
                                                }
                                                if(user_dob.equals("")) {
                                                    dob.setError("Please enter your name");
                                                }
                                                if (user_name.equals("") || user_phone.equals("")  || user_mail.equals("")  || user_dob.equals("")) {
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
                                                            editor = sharedPreferences.edit();
                                                            editor.putInt("new_customers", sharedPreferences.getInt("new_customers", 0) + 1);
                                                            editor.apply();
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Toasty.success(getApplicationContext(), "Created new customer", Toast.LENGTH_SHORT, true).show();
                                                                    Intent intent = new Intent(FeedbackActivity.this, RatingActivity.class);
                                                                    intent.putExtra("waiter", getIntent().getExtras().getString("waiter"));
                                                                    intent.putExtra("table_no",getIntent().getExtras().getString("table_no"));
                                                                    intent.putExtra("date",getIntent().getExtras().getString("date"));
                                                                    intent.putExtra("time", getIntent().getExtras().getString("time"));
                                                                    intent.putExtra("number", user_phone);
                                                                    intent.putExtra("customer", user_name);
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
            }
        });
    }

}
