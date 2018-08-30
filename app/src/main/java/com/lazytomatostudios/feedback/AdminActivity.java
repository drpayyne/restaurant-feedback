package com.lazytomatostudios.feedback;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.lazytomatostudios.feedback.db.Database;
import com.lazytomatostudios.feedback.db.entity.Feedback;
import com.lazytomatostudios.feedback.db.entity.Waiter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.siegmar.fastcsv.writer.CsvAppender;
import de.siegmar.fastcsv.writer.CsvWriter;
import es.dmoral.toasty.Toasty;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "Manick", waiter_name;
    Button button_create, button_read, button_update, button_delete, button_csv;
    View layout;
    int waiter_id;
    Database database;
    Waiter waiter;
    List<Waiter> waiterList;
    List<Feedback> feedbackList;
    LayoutInflater inflater;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        database = Database.getDatabase(this);

        Dexter.withActivity(AdminActivity.this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                Toasty.success(AdminActivity.this, "Permission granted!", Toast.LENGTH_SHORT, true).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toasty.error(AdminActivity.this, "You must grant access to storage!", Toast.LENGTH_SHORT, true).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check();

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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        feedbackList = database.feedbackDao().readAll();
                        parseFeedback(feedbackList);
                    }
                }).start();
                break;
        }
    }

    public void parseFeedback(List<Feedback> feedbackList) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
        Date date = new Date();
        File folder = new File(Environment.getExternalStorageDirectory() + "/Feedback");
        if(!folder.exists())
            folder.mkdir();
        String fileName = folder.toString() + "/" + dateFormat.format(date) + ".csv";
        File file = new File(fileName);
        CsvWriter csvWriter = new CsvWriter();
        try (CsvAppender csvAppender = csvWriter.append(file, StandardCharsets.UTF_8)) {
            csvAppender.appendLine("index", "phone", "rating1", "rating2", "rating3", "rating4", "rating5", "comments", "date", "waiter", "table");
            for (int i = 0; i < feedbackList.size(); i++) {
                csvAppender.appendLine(
                        String.valueOf(feedbackList.get(i).getIndex()),
                        feedbackList.get(i).getPhone(),
                        String.valueOf(feedbackList.get(i).getQ1_rating()),
                        String.valueOf(feedbackList.get(i).getQ2_rating()),
                        String.valueOf(feedbackList.get(i).getQ3_rating()),
                        String.valueOf(feedbackList.get(i).getQ4_rating()),
                        String.valueOf(feedbackList.get(i).getQ5_rating()),
                        feedbackList.get(i).getComments(),
                        feedbackList.get(i).getDate(),
                        feedbackList.get(i).getWaiter(),
                        feedbackList.get(i).getTable()
                        );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Number of feedbacks : " + feedbackList.size());
        for(int i = 0; i <feedbackList.size(); i++) {
            Log.d(TAG, String.valueOf(feedbackList.get(i).getIndex()));
        }
    }
}
