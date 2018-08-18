package com.lazytomatostudios.feedback;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    Button button_create, button_read, button_update, button_delete, button_csv;
    LayoutInflater inflater;
    View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        button_create = findViewById(R.id.button_create);
        button_read = findViewById(R.id.button_read);
        button_update = findViewById(R.id.button_update);
        button_delete = findViewById(R.id.button_delete);
        button_csv = findViewById(R.id.button_csv);

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.dialog_create, (ViewGroup) findViewById(R.id.admin_container));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_create:

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
