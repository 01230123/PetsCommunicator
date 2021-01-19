package com.example.petscommunicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StimulatorScreen extends MySprite{
    private MainScreen mainScreen;
    private int numberOfSprite;
    private int currentAddedSprite;

    private int mode;
    private int currentPlaySprite;

    private List<AnimationSprite> gridSprite;
    private List<TextView> emotionText;

    public StimulatorScreen(Context context, MainScreen mainScreen, float top, float left, int width, int height, int numberOfSprite) {
        super(context, top, left, width, height);
        this.numberOfSprite = numberOfSprite;
        this.currentAddedSprite = 0;
        this.mainScreen = mainScreen;
        this.mode = 0;
        this.currentPlaySprite = 0;

        gridSprite = new ArrayList<>();
        emotionText = new ArrayList<>();

        String[] emotionName = getContext().getResources().getStringArray(R.array.emotion);
        for (String s : emotionName)
        {
            TextView tv = new TextView(getContext());
            tv.setText(s);
            tv.setTextSize(25);
            tv.setTextColor(Color.parseColor("#8e5768"));
            emotionText.add(tv);
        }
        scaleSprite();
    }

    private void scaleSprite() {
        int numberOfCol = 2;
        int numberOfRow = (numberOfSprite + numberOfCol - 1) / numberOfCol;

        int spriteWidth = getWidth() / (numberOfCol + 1);
        int spriteHeight = getHeight() / (numberOfRow + 1);
        for (int i = 0; i < numberOfSprite; i++)
        {
            AnimationSprite sprite = new AnimationSprite(getContext(), mainScreen, 0, 0, 0, 0);
            sprite.setWidth(spriteWidth);
            sprite.setHeight(spriteHeight);
            int row = i / 2;
            int column = i % 2;
            sprite.setTop(spriteHeight * row + (float)spriteHeight / (numberOfRow + 1) * (row + 1));
            sprite.setLeft(spriteWidth * column + (float)spriteWidth / (numberOfCol + 1) * (column + 1));
            gridSprite.add(sprite);
        }
    }

    public void addSprite(int[] bmpIdList)
    {
        gridSprite.get(currentAddedSprite).addBmp(bmpIdList);
        currentAddedSprite++;
        currentAddedSprite %= gridSprite.size();
    }

    public void addSound(int spriteId, int[] soundIdList)
    {
        gridSprite.get(spriteId % gridSprite.size()).addSound(soundIdList);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for (AnimationSprite s : gridSprite)
            s.draw(canvas);

        for (int i = 0; i < emotionText.size(); i++)
        {
            TextView tv = emotionText.get(i);
            @SuppressLint("Range")
            int widthSpec = View.MeasureSpec.makeMeasureSpec(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    View.MeasureSpec.UNSPECIFIED
            );
            int heightSpec = View.MeasureSpec.makeMeasureSpec(400,
                    View.MeasureSpec.UNSPECIFIED
            );
            tv.measure(widthSpec, heightSpec);
            tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());
            canvas.save();
            canvas.translate(
                    gridSprite.get(i).getLeft() + (float)(gridSprite.get(i).getWidth() - tv.getWidth()) / 2,
                    gridSprite.get(i).getTop() + gridSprite.get(i).getHeight()
            );
            tv.draw(canvas);
            canvas.restore();
        }
    }

    public void handleClick(float x, float y) {
        for (int i = 0; i < numberOfSprite; i++)
            if (gridSprite.get(i).isSelected(x, y))
            {
                if (mode == 0)
                    if (i != currentPlaySprite && gridSprite.get(currentPlaySprite).isTurnedOn)
                    {
                        gridSprite.get(currentPlaySprite).isTurnedOn =
                                !gridSprite.get(currentPlaySprite).isTurnedOn;
                        gridSprite.get(currentPlaySprite).handleClick(getContext());
                    }
                gridSprite.get(i).isTurnedOn = !gridSprite.get(i).isTurnedOn;
                gridSprite.get(i).handleClick(getContext());
                currentPlaySprite = i;
            }
    }

    public void setMode(int mode) {
        this.mode = mode;
        if (this.mode == 0)
            for (int i = 0; i < numberOfSprite; i++)
                if (i != currentPlaySprite)
                {
                    if (gridSprite.get(i).isTurnedOn)
                    {
                        gridSprite.get(i).isTurnedOn = false;
                        gridSprite.get(i).handleClick(getContext());
                    }
                }
    }

    public void muteSound()
    {
        for (AnimationSprite dog: gridSprite)
            dog.mute();
    }

    public void unmuteSound()
    {
        for (AnimationSprite dog: gridSprite)
            dog.unmute();
    }

    public void destroyEverything()
    {
        for (AnimationSprite dog: gridSprite){
            if (dog.isTurnedOn)
            {
                dog.isTurnedOn = false;
                dog.handleClick(getContext());
            }
            dog.destroyEverything();
        }
    }
}
