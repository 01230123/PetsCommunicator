package com.example.petscommunicator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MySprite {
    private Context mainScreen;

    private float top, left;
    private int width, height;

    private List<Bitmap> bmpList;
    private int bmpPos;

    public MySprite(Context context, float top, float left, int width, int height) {
        this.top = top;
        this.left = left;
        this.width = width == 0 ? 1 : width;
        this.height = height == 0 ? 1 : height;
        this.mainScreen = context;

        bmpList = new ArrayList<>();
        bmpPos = -1;
    }

    public void scaleBitmap(int bitmapId, int desWidth, int desHeight)
    {
        Bitmap bmp = BitmapFactory.decodeResource(mainScreen.getResources(), bitmapId);
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float scale = (float)desWidth / width;
        if (height * scale > desHeight)
            scale = (float)desHeight / height;
        setWidth((int)(bmp.getWidth() * scale));
        setHeight((int)(bmp.getHeight() * scale));
    }

    public void addBmp(int[] bmpIdList)
    {
        if (bmpPos == -1) bmpPos = 0;
        for (int id : bmpIdList)
        {
            Log.d("@@@@", "ID: " + id);
            Bitmap bmp = BitmapFactory.decodeResource(mainScreen.getResources(), id);
            Log.d("@@@@", "Decode Success");
            bmp = Bitmap.createScaledBitmap(bmp, width, height, false);
            bmpList.add(bmp);
        }
    }

    public boolean isSelected(float x, float y)
    {
        return (left <= x && x <= left + width - 1) && (top <= y && y <= top + height - 1);
    }

    public void update()
    {
        if (bmpList.isEmpty()) return;
        bmpPos = (bmpPos + 1) % bmpList.size();
    }

    public void update(int pos)
    {
        if (bmpList.isEmpty()) return;
        bmpPos = pos % bmpList.size();
    }

    public void draw(Canvas canvas)
    {
        if (bmpList.isEmpty()) return;
        canvas.drawBitmap(bmpList.get(bmpPos), left, top, null);
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Context getContext() {
        return mainScreen;
    }

    public int getBmpPos() {
        return bmpPos;
    }
}
