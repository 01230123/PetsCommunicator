package com.example.petscommunicator;

import android.content.Context;

public class SettingIconSprite extends MySprite{
    private int length;
    public boolean isTurnedOn;

    public SettingIconSprite(Context context, float top, float left, int length) {
        super(context, top, left, length, length);
        this.length = length;
        this.isTurnedOn = false;
    }

    public int getLength() {
        return this.length;
    }
}
