package com.example.petscommunicator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.petscommunicator.R;

public class DownloadItem {
    private Bitmap icon;
    private String title;

    public DownloadItem(String title, Context context)
    {
        this.title = title;
        icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.download_icon);
        icon = Bitmap.createScaledBitmap(icon, 200, 200, false);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }
}
