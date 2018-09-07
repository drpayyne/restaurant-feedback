package com.lazytomatostudios.feedback;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lazytomatostudios.feedback.db.Database;
import com.lazytomatostudios.feedback.db.entity.Waiter;
import com.lazytomatostudios.feedback.helper.FullscreenBugWorkaround;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import fr.ganfra.materialspinner.MaterialSpinner;

public class WaiterActivity extends AppCompatActivity {

    String TAG = "Manick", date, time;

    MaterialEditText tabletext;
    Database database;
    String[] waiterArray;
    List<Waiter> waiterList;
    ArrayAdapter<String> waiterAdapter;
    MaterialSpinner waiterSpinner;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    Button button_open_date;
    Button button_open_time;
    FloatingActionButton button_feedback;
    TextView date_view, time_view;

    //DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    //SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        database = Database.getDatabase(this);

        date_view = findViewById(R.id.textview_date);
        time_view = findViewById(R.id.textview_time);
        tabletext = findViewById(R.id.edittext_table);
        button_open_date = findViewById(R.id.button_date);
        button_open_time = findViewById(R.id.button_time);
        button_feedback = findViewById(R.id.button_start);

        //date = dateFormat.format(new Date());
        //time = timeFormat.format(new Date());
        //Log.d(TAG, date + " " + time);

        date = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        date_view.setText(date);

        time = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
        time_view.setText(time);

        button_open_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        button_open_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });

       button_feedback.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(waiterSpinner.getSelectedItem() == null || tabletext.getText().toString() == "") {
                   Toasty.error(getApplicationContext(), "Please enter all fields!", Toast.LENGTH_SHORT, true).show();
               } else {
                   Intent intent = new Intent(WaiterActivity.this, FeedbackActivity.class);
                   intent.putExtra("waiter", waiterSpinner.getSelectedItem().toString());
                   intent.putExtra("table_no", tabletext.getText().toString());
                   intent.putExtra("date", date);
                   intent.putExtra("time", time);
                   startActivity(intent);
               }
           }
       });

        new Thread(new Runnable() {
            @Override
            public void run() {
                waiterList = database.waiterDao().readAll();
                waiterArray = new String[waiterList.size()];
                for(int i = 0; i < waiterList.size(); i++) {
                    waiterArray[i] = waiterList.get(i).getName();
                    Log.d(TAG, waiterArray[i]);
                }
                initWaiterAdapter();
            }
        }).start();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.d("amrutha", String.valueOf(year) + " " + String.valueOf(monthOfYear+1) + " " + String.valueOf(dayOfMonth));
                date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year);
                date_view.setText(date);
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minuteOfHour) {
                String hour = String.valueOf(hourOfDay);
                String minute = String.valueOf(minuteOfHour);
                if(hourOfDay < 10)
                    hour = "0" + hour;
                if(minuteOfHour < 10)
                    minute = "0" + minute;
                Log.d(TAG, String.valueOf(hour) + " " + String.valueOf(minute));
                time = String.valueOf(hour) + ":" + String.valueOf(minute);
                time_view.setText(time);

            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false);

    }

    private void initWaiterAdapter() {
        waiterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, waiterArray);
        waiterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waiterSpinner = findViewById(R.id.waiter_spinner);
        waiterSpinner.setAdapter(waiterAdapter);
    }
}