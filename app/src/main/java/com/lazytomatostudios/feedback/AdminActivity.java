package com.lazytomatostudios.feedback;

import android.Manifest;
import android.app.ActionBar;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.lazytomatostudios.feedback.db.Database;
import com.lazytomatostudios.feedback.db.entity.Feedback;
import com.lazytomatostudios.feedback.db.entity.Waiter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.siegmar.fastcsv.writer.CsvAppender;
import de.siegmar.fastcsv.writer.CsvWriter;
import es.dmoral.toasty.Toasty;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "Manick", waiter_name;
    Button button_create, button_read, button_delete, button_csv;
    View layout;
    int waiter_id;
    Database database;
    Waiter waiter;
    List<Waiter> waiterList;
    List<Feedback> feedbackList;
    LayoutInflater inflater;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    TextView textView, newFeedbackView;
    String waiters = "";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        database = Database.getDatabase(this);
        sharedPreferences = getApplicationContext().getSharedPreferences("feedback", 0);

        newFeedbackView = findViewById(R.id.new_feedbacks_text);
        newFeedbackView.setText(getString(R.string.test_resource) + String.valueOf(sharedPreferences.getInt("new_feedbacks", 0)));

        Dexter.withActivity(AdminActivity.this).withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if(!report.areAllPermissionsGranted()) {
                    Toasty.error(AdminActivity.this, "You must grant access to all permissions!", Toast.LENGTH_SHORT, true).show();
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();

        button_create = findViewById(R.id.button_create);
        button_read = findViewById(R.id.button_read);
        button_delete = findViewById(R.id.button_delete);
        button_csv = findViewById(R.id.button_csv);

        button_create.setOnClickListener(this);
        button_read.setOnClickListener(this);
        button_delete.setOnClickListener(this);
        button_csv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_create:
                inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                layout = inflater.inflate(R.layout.dialog_create, null);
                final EditText name = layout.findViewById(R.id.waiter_name);

                //Building dialog
                new AlertDialog
                        .Builder(AdminActivity.this)
                        .setView(layout)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
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
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.button_read:
                Log.d(TAG, "Creating thread for database query");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Initiating database query");
                        waiterList = database.waiterDao().readAll();
                        Log.d(TAG, "Database query completed");
                        waiters = "";
                        for(int i = 0; i < waiterList.size(); i++) {
                            Log.d(TAG, waiterList.get(i).getId() + " " + waiterList.get(i).getName());
                            waiters = waiters.concat(waiterList.get(i).getId() + " : " + waiterList.get(i).getName() + "\n");
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                layout = inflater.inflate(R.layout.dialog_view_waiter, null);
                                new AlertDialog
                                        .Builder(AdminActivity.this)
                                        .setView(layout)
                                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        }).show();
                                textView = layout.findViewById(R.id.textview_waiters);
                                textView.setText(waiters);
                            }
                        });
                    }
                }).start();
                break;
                /*try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("+919500132964", null, "hi", null, null);
                    smsManager.sendTextMessage("+919500132964", null, "hi", null, null);
                    smsManager.sendTextMessage("+919500132964", null, "hi", null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
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
                        parseFeedback();
                    }
                }).start();
                break;
        }
    }

    public void parseFeedback() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
        Date date = new Date();
        Log.d(TAG, Environment.getExternalStorageDirectory().toString());
        File folder = new File(Environment.getExternalStorageDirectory() + "/Feedback");
        if(!folder.exists()) {
            Log.d(TAG, "Creating folder");
            boolean folderCreated = folder.mkdir();
            Log.d(TAG, "Folder status : " + String.valueOf(folderCreated));
        }
        String fileName = folder.toString() + "/" + dateFormat.format(date) + ".csv";
        Log.d(TAG, fileName);
        File file = new File(fileName);
        CsvWriter csvWriter = new CsvWriter();
        CsvAppender csvAppender;
        try {
            csvAppender = csvWriter.append(new FileWriter(file));
            csvAppender.appendLine("index", "phone", "rating1", "rating2", "rating3", "rating4", "rating5", "comments", "date", "waiter", "table");
            for (int i = 0; i < feedbackList.size(); i++) {
                Log.d(TAG, "Exporting feedback : " + i);
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
            csvAppender.close();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toasty.success(getApplicationContext(), "Exported to CSV. Count : " + feedbackList.size(), Toast.LENGTH_SHORT, true).show();
                }
            });
        } catch (IOException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toasty.error(getApplicationContext(), "Unable to export! Try again." + feedbackList.size(), Toast.LENGTH_SHORT, true).show();
                }
            });
            e.printStackTrace();
        }
        Log.d(TAG, file.getAbsolutePath());
        Log.d(TAG, "Number of feedbacks : " + feedbackList.size());
        for(int i = 0; i <feedbackList.size(); i++) {
            Log.d(TAG, String.valueOf(feedbackList.get(i).getIndex()));
        }
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("feedback", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("new_feedbacks", 0);
        editor.apply();
        newFeedbackView.setText(getString(R.string.test_resource) + String.valueOf(sharedPreferences.getInt("new_feedbacks", 0)));
    }
}
