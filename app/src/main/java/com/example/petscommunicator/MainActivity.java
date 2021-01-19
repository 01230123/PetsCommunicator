package com.example.petscommunicator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private MainScreen mainScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWritePermission();
//        getRecordPermission();

        DisplayMetrics displayMetrics = getFullScreen();
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        //Loading screen

        //Main screen
        mainScreen = new MainScreen(this, width, height);
        mainScreen.setKeepScreenOn(true);
    }

    private void getWritePermission() {
        // Check for permissions
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // If we don't have permissions, ask user for permissions
        if (permission != PackageManager.PERMISSION_GRANTED) {
            String[] PERMISSIONS_STORAGE = {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            int REQUEST_EXTERNAL_STORAGE = 1;

            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void getRecordPermission()
    {
        // Check for permissions
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        // If we don't have permissions, ask user for permissions
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    0
            );
        }
    }

    private DisplayMetrics getFullScreen() {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(mainScreen);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainScreen.destroyEverything();
    }

    private boolean exitFlag = false;

    private CountDownTimer exitTimer = new CountDownTimer(2000, 2000) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            exitFlag = false;
            exitTimer.cancel();
        }
    };

    @Override
    public void onBackPressed() {
        if (exitFlag)
        {
            super.onBackPressed();
            return;
        }
        exitFlag = true;
        exitTimer.start();
        final Toast msg = Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT);
        msg.show();
    }
}