package com.lazytomatostudios.feedback;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;

import java.util.List;

public class PatternActivity extends AppCompatActivity {

    String TAG = "Manick";
    PatternLockView patternLockView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern);

        sharedPreferences = getApplicationContext().getSharedPreferences("feedback", 0);
        editor = sharedPreferences.edit();
        editor.putString("admin_pattern", "(Row = 0, Col = 0)(Row = 0, Col = 1)(Row = 0, Col = 2)(Row = 1, Col = 1)(Row = 2, Col = 1)");
        editor.apply();

        patternLockView = findViewById(R.id.pattern_lock_view);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {
                Log.d(TAG, "Pattern started");
            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {
                Log.d(TAG, progressPattern.toArray().toString());
            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                Log.d(TAG, pattern.toArray().toString());
                String myPattern = "";
                for (int i = 0; i < pattern.size(); i++) {
                    myPattern = myPattern.concat(pattern.get(i).toString());
                }
                Log.d(TAG, myPattern);
                Log.d(TAG, sharedPreferences.getString("admin_pattern", null));
                if(sharedPreferences.getString("admin_pattern", "").equals(myPattern)) {
                    Log.d(TAG, "MATCHED");
                    Intent intent = new Intent(PatternActivity.this, AdminActivity.class);
                    startActivity(intent);
                } else {
                    Log.d(TAG, "MISMATCHED");
                    patternLockView.clearPattern();
                }
            }

            @Override
            public void onCleared() {
                Log.d(TAG, "Pattern cleared");
            }
        });
    }
}
