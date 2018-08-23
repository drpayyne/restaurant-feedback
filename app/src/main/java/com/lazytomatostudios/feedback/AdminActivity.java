package com.lazytomatostudios.feedback;

import android.content.DialogInterface;
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
import com.lazytomatostudios.feedback.db.entity.Waiter;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "Manick";
    Button button_create, button_read, button_update, button_delete, button_csv;
    View layout;
    String waiter_name;
    int waiter_id;
    Database database;
    Waiter waiter;
    List<Waiter> waiterList;
    LayoutInflater inflater;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        database = Database.getDatabase(this);

        button_create = findViewById(R.id.button_create);
        button_read = findViewById(R.id.button_read);
        button_update = findViewById(R.id.button_update);
        button_delete = findViewById(R.id.button_delete);
        button_csv = findViewById(R.id.button_csv);

        button_create.setOnClickListener(this);
        button_read.setOnClickListener(this);
        button_update.setOnClickListener(this);
        button_delete.setOnClickListener(this);
        button_csv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_create:
                inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                layout = inflater.inflate(R.layout.dialog_create, null);
                final EditText name = layout.findViewById(R.id.waiter_name);

                //Building dialog
                builder = new AlertDialog.Builder(AdminActivity.this);
                builder.setView(layout);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, name.getText().toString());
                        waiter_name = name.getText().toString();
                        Log.d(TAG, "Creating thread for database query");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "Initiating database query");
                                waiter = new Waiter();
                                waiter.setName(waiter_name);
                                database.waiterDao().create(waiter);
                                Log.d(TAG, "Database query completed");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toasty.success(getApplicationContext(), "Created new waiter", Toast.LENGTH_SHORT, true).show();
                                    }
                                });
                            }
                        }).start();
                        Log.d(TAG, "Dismissing dialog");
                        alertDialog.dismiss();
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
                break;
            case R.id.button_read:
                Log.d(TAG, "Creating thread for database query");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Initiating database query");
                        waiterList = database.waiterDao().readAll();
                        Log.d(TAG, "Database query completed");
                        for(int i = 0; i < waiterList.size(); i++)
                            Log.d(TAG, waiterList.get(i).getId() + " " + waiterList.get(i).getName());
                    }
                }).start();
                break;
            case R.id.button_update:
                break;
            case R.id.button_delete:
                inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                layout = inflater.inflate(R.layout.dialog_delete, null);
                final EditText id = layout.findViewById(R.id.waiter_id);

                //Building dialog
                builder = new AlertDialog.Builder(AdminActivity.this);
                builder.setView(layout);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, id.getText().toString());
                        waiter_id = Integer.valueOf(id.getText().toString());
                        Log.d(TAG, "Creating thread for database query");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "Initiating database query");
                                waiter = new Waiter();
                                waiter.setId(waiter_id);
                                database.waiterDao().delete(waiter);
                                Log.d(TAG, "Database query completed");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toasty.success(getApplicationContext(), "Deleted waiter : " + waiter_id, Toast.LENGTH_SHORT, true).show();
                                    }
                                });
                            }
                        }).start();
                        Log.d(TAG, "Dismissing dialog");
                        alertDialog.dismiss();
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
                break;
            case R.id.button_csv:
                break;
        }
    }
}
