package com.example.petscommunicator;

import android.content.Context;

public class MenuItemSprite extends MySprite {

    public MenuItemSprite(Context context, int width, int height) {
        super(context, 0, 0, width, height);
    }

    @Override
    public void addBmp(int[] bmpIdList) {
        if (getBmpPos() == -1)
            scaleBitmap(bmpIdList[0], getWidth(), getHeight());
        super.addBmp(bmpIdList);
    }
}
