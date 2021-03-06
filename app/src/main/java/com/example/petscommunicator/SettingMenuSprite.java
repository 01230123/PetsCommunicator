package com.example.petscommunicator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class SettingMenuSprite extends MySprite{
    private int itemWidth;
    private int itemHeight;

//    private MySprite menuBmp;
    private MySprite menuTextBmp;
    private MySprite modeButtonBmp;
//    private MySprite soundButtonBmp;
    private MySprite downloadButtonBmp;
    private MySprite creditButtonBmp;

    public boolean creditOn;
    private MySprite creditBmp;

    public SettingMenuSprite(Context context, float top, float left, int width, int height)
    {
        super(context, top, left, width, height);
        creditOn = false;

        itemWidth = (int)(getWidth() / 1.5);
        itemHeight = (int)(getHeight() / 5);
        creditBmp = new MySprite(
                getContext(),
                getTop() - 150,
                getLeft() - 75,
                getWidth() + 150,
                getHeight() + 300);
        creditBmp.addBmp(new int[]{R.drawable.creditpage});

    }

    public void setMenuBmp(int bmpId)
    {
        addBmp(new int[]{bmpId});
//        menuBmp = new MySprite(getContext(), getTop(), getLeft(), getWidth(), getHeight());
//        menuBmp.addBmp(new int[]{bmpId});
    }

    public void setMenuTextBmp(int[] bmpId)
    {
        menuTextBmp = new MenuItemSprite(getContext(), itemWidth, itemHeight);
        menuTextBmp.addBmp(bmpId);
        menuTextBmp.setTop(getTop() + (float)itemHeight / 5);
        menuTextBmp.setLeft(getLeft() + ((float)(getWidth() - menuTextBmp.getWidth()) / 2));
    }

    public void setModeButtonBmp(int[] bmpId)
    {
        modeButtonBmp = new MenuItemSprite(getContext(), itemWidth, itemHeight);
        modeButtonBmp.addBmp(bmpId);
        modeButtonBmp.setTop(getTop() + itemHeight + 2 * (float)itemHeight / 5);
        modeButtonBmp.setLeft(getLeft() + ((float)(getWidth() - modeButtonBmp.getWidth()) / 2));
    }

//    public void setSoundButtonBmp(int[] bmpId)
//    {
//        soundButtonBmp = new MenuItemSprite(getContext(), itemWidth, itemHeight);
//        soundButtonBmp.addBmp(bmpId);
//        soundButtonBmp.setTop(getTop() + itemHeight * 2 + 3 * (float)itemHeight / 5);
//        soundButtonBmp.setLeft(getLeft() + ((float)(getWidth() - soundButtonBmp.getWidth()) / 2));
//    }

    public void setDownloadButtonBmp(int[] bmpId)
    {
        downloadButtonBmp = new MenuItemSprite(getContext(), itemWidth, itemHeight);
        downloadButtonBmp.addBmp(bmpId);
        downloadButtonBmp.setTop(getTop() + itemHeight * 2 + 3 * (float)itemHeight / 5);
        downloadButtonBmp.setLeft(getLeft() + ((float)(getWidth() - downloadButtonBmp.getWidth()) / 2));
    }

    public void setCreditButtonBmp(int[] bmpId)
    {
        creditButtonBmp = new MenuItemSprite(getContext(), itemWidth, itemHeight);
        creditButtonBmp.addBmp(bmpId);
        creditButtonBmp.setTop(getTop() + itemHeight * 3 + 4 * (float)itemHeight / 5);
        creditButtonBmp.setLeft(getLeft() + ((float)(getWidth() - creditButtonBmp.getWidth()) / 2));
    }

    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawARGB(150, 0, 0, 0);
        if (!creditOn)
        {
            super.draw(canvas);
            menuTextBmp.draw(canvas);
            modeButtonBmp.draw(canvas);
//            soundButtonBmp.draw(canvas);
            downloadButtonBmp.draw(canvas);
            creditButtonBmp.draw(canvas);
        }
        else
            creditBmp.draw(canvas);
    }

    public int getCurrentMode()
    {
        return modeButtonBmp.getBmpPos();
    }

//    public int isMute()
//    {
//        return (soundButtonBmp.getBmpPos() + 1) % 2;
//    }

    public void handleButtonsClick(float x, float y) {
        if (modeButtonBmp.isSelected(x, y))
            modeButtonBmp.update();
//        else if (soundButtonBmp.isSelected(x, y))
//            soundButtonBmp.update();
        else if (downloadButtonBmp.isSelected(x, y))
            handleDownloadIntent();
        else if (creditButtonBmp.isSelected(x, y))
            creditOn = true;
    }

    private void handleDownloadIntent() {
        getWritePermission();
        if (checkWritePermission())
        {
            Log.d("@@@@", "Ready to intent");
            Intent intent = new Intent(getContext(), DownloadActivity.class);
            getContext().startActivity(intent);
        }
        else
            return;
    }

    public void handleCredit(float x, float y) {
        if (x >= creditBmp.getLeft() + creditBmp.getWidth() - 200 &&
            x <= creditBmp.getLeft() + creditBmp.getWidth() &&
            y >= creditBmp.getTop() &&
            y <= creditBmp.getTop() + 200)
            creditOn = false;
    }

    private boolean checkWritePermission()
    {
        int permission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    private void getWritePermission() {
        // Check for permissions
        if (!checkWritePermission()) {
            String[] PERMISSIONS_STORAGE = {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            int REQUEST_EXTERNAL_STORAGE = 1;

            ActivityCompat.requestPermissions(
                    (Activity)getContext(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
