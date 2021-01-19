package com.example.petscommunicator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.petscommunicator.R;

public class DownloadItem {
    private Bitmap icon;
    private String title;
    private String path;

    public DownloadItem(String path, Context context)
    {
        this.path = path;
        this.title = path.substring(path.indexOf('/') + 1);
        icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.download_icon);
        icon = Bitmap.createScaledBitmap(icon, 200, 200, false);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }
}
