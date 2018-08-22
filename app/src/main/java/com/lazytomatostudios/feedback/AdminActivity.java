package com.lazytomatostudios.feedback;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "Manick";
    Button button_create, button_read, button_update, button_delete, button_csv;
    View layout;
    EditText waiter_name, waiter_id;
    String name, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

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

        waiter_name = findViewById(R.id.waiter_name);
        waiter_id = findViewById(R.id.waiter_id);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_create:
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.dialog_create, null);
                final EditText name = (EditText) layout.findViewById(R.id.waiter_name);
                final EditText id = (EditText) layout.findViewById(R.id.waiter_id);

                //Building dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                builder.setView(layout);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, name.getText().toString() + " " + id.getText().toString());
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.button_read:
                break;
            case R.id.button_update:
                break;
            case R.id.button_delete:
                break;
            case R.id.button_csv:
                break;
        }
    }
}
