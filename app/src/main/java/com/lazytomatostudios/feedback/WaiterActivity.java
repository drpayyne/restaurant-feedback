package com.lazytomatostudios.feedback;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lazytomatostudios.feedback.db.Database;
import com.lazytomatostudios.feedback.db.entity.Waiter;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;
import fr.ganfra.materialspinner.MaterialSpinner;

public class WaiterActivity extends AppCompatActivity {

    String TAG = "Manick", date;

    MaterialEditText tabletext;
    Database database;
    String[] waiterArray;
    List<Waiter> waiterList;
    ArrayAdapter<String> waiterAdapter;
    MaterialSpinner waiterSpinner;
    DatePickerDialog datePickerDialog;

    Button button_open_date;
    Button button_feedback;
    TextView date_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter);
        date_view = findViewById(R.id.textview_date);

        date = "Date : " + String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        date_view.setText(date);
        tabletext = findViewById(R.id.edittext_table);


        button_open_date = (Button) findViewById(R.id.button_date);
        button_open_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissKeyboard(WaiterActivity.this);
                datePickerDialog.show();
            }
        });

       button_feedback = (Button) findViewById(R.id.button_start);
       button_feedback.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(waiterSpinner.getSelectedItem() == null || tabletext.getText().toString() == "" || date_view.getText().toString() == "") {
                   Toasty.error(getApplicationContext(), "Please enter all fields!", Toast.LENGTH_SHORT, true).show();
               } else {
                   Intent intent = new Intent(WaiterActivity.this, FeedbackActivity.class);
                   intent.putExtra("waiter", waiterSpinner.getSelectedItem().toString());
                   intent.putExtra("table_no",tabletext.getText().toString());
                   intent.putExtra("date",date_view.getText().toString());
                   startActivity(intent);
               }
           }
       });

        database = Database.getDatabase(this);

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
                date = "Date : " + String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year);
                date_view.setText(date);
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

    }

    private void initWaiterAdapter() {
        waiterAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, waiterArray);
        waiterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waiterSpinner = findViewById(R.id.waiter_spinner);
        waiterSpinner.setAdapter(waiterAdapter);
    }

    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }
}