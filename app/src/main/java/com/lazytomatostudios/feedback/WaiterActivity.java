package com.lazytomatostudios.feedback;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.lazytomatostudios.feedback.db.Database;
import com.lazytomatostudios.feedback.db.entity.Waiter;

import java.util.Calendar;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class WaiterActivity extends AppCompatActivity /*implements DatePickerDialog.OnDateSetListener*/ {

    String TAG = "Manick";

    Database database;
    String[] waiterArray;
    List<Waiter> waiterList;
    ArrayAdapter<String> waiterAdapter;
    MaterialSpinner waiterSpinner;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter);

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

        //datePickerDialog = new DatePickerDialog(this, WaiterActivity.this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    private void initWaiterAdapter() {
        waiterAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, waiterArray);
        waiterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waiterSpinner = findViewById(R.id.waiter_spinner);
        waiterSpinner.setAdapter(waiterAdapter);
    }
}
