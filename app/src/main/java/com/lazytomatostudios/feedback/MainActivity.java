package com.lazytomatostudios.feedback;

import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lazytomatostudios.feedback.db.Database;

import org.jetbrains.annotations.NotNull;

import io.ghyeok.stickyswitch.widget.StickySwitch;

public class MainActivity extends AppCompatActivity {

    String TAG = "Manick";
    TransitionDrawable transition;
    ConstraintLayout layout;
    StickySwitch stickySwitch;
    Button button;
    Intent intent;

    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.container);
        transition = (TransitionDrawable) layout.getBackground();
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stickySwitch.getDirection() == StickySwitch.Direction.LEFT) {
                    Log.d(TAG, "LEFT");
                    intent = new Intent(MainActivity.this, WaiterActivity.class);
                } else {
                    Log.d(TAG, "RIGHT");
                    intent = new Intent(MainActivity.this, AdminActivity.class);
                }
                startActivity(intent);
            }
        });

        stickySwitch = findViewById(R.id.sticky_switch);
        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(@NotNull StickySwitch.Direction direction, @NotNull String text) {
                Log.d(TAG, "Now Selected : " + direction.name() + ", Current Text : " + text);
                if (direction.name() == "LEFT")
                    transition.startTransition(500);
                else
                    transition.reverseTransition(500);
            }
        });
    }
}
