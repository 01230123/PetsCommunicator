package com.example.petscommunicator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private MainScreen mainScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set full screen
        DisplayMetrics displayMetrics = getFullScreen();
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        //Create or open internal storage
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        contextWrapper.getDir(String.valueOf(R.string.internalPath), Context.MODE_PRIVATE);

        //Main screen
        mainScreen = new MainScreen(this, width, height);
        mainScreen.setKeepScreenOn(true);
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