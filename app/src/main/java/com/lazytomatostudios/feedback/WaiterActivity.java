package com.lazytomatostudios.feedback;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
<<<<<<< HEAD
=======
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
>>>>>>> amad

import com.lazytomatostudios.feedback.db.Database;
import com.lazytomatostudios.feedback.db.entity.Waiter;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class WaiterActivity extends AppCompatActivity {

    String TAG = "Manick";

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

        tabletext = findViewById(R.id.edittext_table);

        button_open_date = (Button) findViewById(R.id.button_date);
        button_open_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

       button_feedback = (Button) findViewById(R.id.button_start);
       button_feedback.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
        //       Intent intent = new Intent(this, FeedbackActivity.class);
      //         intent.putExtra("waiter", waiterSpinner.getSelectedItem().toString());
    //           intent.putExtra("table_no",tabletext.getText());
  //             intent.putExtra("date",date_view.getText());
//               startActivity(intent);
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
                date_view.setText(String.valueOf(year) + " " + String.valueOf(monthOfYear+1) + " " + String.valueOf(dayOfMonth));
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

    }

    private void initWaiterAdapter() {
        waiterAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, waiterArray);
        waiterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waiterSpinner = findViewById(R.id.waiter_spinner);
        waiterSpinner.setAdapter(waiterAdapter);
    }
}