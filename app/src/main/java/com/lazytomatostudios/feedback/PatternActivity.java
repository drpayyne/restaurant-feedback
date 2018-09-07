package com.lazytomatostudios.feedback;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

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

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        sharedPreferences = getApplicationContext().getSharedPreferences("feedback", 0);
        editor = sharedPreferences.edit();
        editor.putString("admin_pattern", "01247");
        editor.apply();

        patternLockView = findViewById(R.id.pattern_lock_view);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {
                Log.d(TAG, "Pattern started");
            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {
                Log.d(TAG, PatternLockUtils.patternToString(patternLockView, progressPattern));
            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String myPattern = PatternLockUtils.patternToString(patternLockView, pattern);
                Log.d(TAG, myPattern);
                Log.d(TAG, sharedPreferences.getString("admin_pattern", ""));
                if(sharedPreferences.getString("admin_pattern", "").equals(myPattern)) {
                    Log.d(TAG, "MATCHED");
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                    Intent intent = new Intent(PatternActivity.this, AdminActivity.class);
                    patternLockView.removePatternLockListener(this);
                    startActivity(intent);
                } else {
                    Log.d(TAG, "MISMATCHED");
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            // Block this thread for 2 seconds.
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.getLocalizedMessage();
                            }

                            // After sleep finished blocking, create a Runnable to run on the UI Thread.
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    patternLockView.clearPattern();
                                }
                            });
                        }
                    };
                    thread.start();
                }
            }

            @Override
            public void onCleared() {
                Log.d(TAG, "Pattern cleared");
            }
        });
    }
}
