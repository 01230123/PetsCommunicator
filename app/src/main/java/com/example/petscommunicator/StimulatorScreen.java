package com.example.petscommunicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
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

    private List<AnimationSprite> listSprite;
    private List<MySprite> clickArea;
    private List<TextView> emotionText;

    public StimulatorScreen(Context context, MainScreen mainScreen, float top, float left, int width, int height, int numberOfSprite) {
        super(context, top, left, width, height);
        this.numberOfSprite = numberOfSprite;
        this.currentAddedSprite = 0;
        this.mainScreen = mainScreen;
        this.mode = 0;
        this.currentPlaySprite = 0;

        listSprite = new ArrayList<>();
        clickArea = new ArrayList<>();
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
//        scaleSprite();
        setSprite();
        setClickArea();
    }

    private void setSprite() {
        int[][] offset = new int[][]{
                {9, 3, 5},
                {7, 2, 3},
                {10, 0, 4},
                {8, 6, 2},
                {7, 0, 4},
                {12, 7, 2}
        };
        int numberOfRow = 16;
        int numberOfCol = 9;
        int gridHeight = getHeight() / numberOfRow;
        int gridWidth = getWidth() / numberOfCol;

        for (int i = 0; i < numberOfSprite; i++)
        {
            AnimationSprite dog = new AnimationSprite(
                    getContext(),
                    mainScreen,
                    0,0,0,0
            );
            float dogTop = gridHeight * offset[i][0];
            float dogLeft = gridWidth * offset[i][1];
//            Log.d("@@@@", "Set dog " + i + " at " + dogLeft + ", " + dogTop);
            dog.setTop(dogTop);
            dog.setLeft(dogLeft);
            dog.setWidth(gridWidth * offset[i][2]);
            dog.setHeight(gridHeight * offset[i][2]);
            listSprite.add(dog);
        }
        listSprite.get(0).setTop(listSprite.get(0).getTop() - 65);
        listSprite.get(0).setLeft(listSprite.get(0).getLeft() + 60);

        listSprite.get(1).setWidth(listSprite.get(1).getWidth() + 100);

        listSprite.get(3).setTop(listSprite.get(3).getTop() - 70);
        listSprite.get(3).setLeft(listSprite.get(3).getLeft() + 70);

        listSprite.get(4).setLeft(listSprite.get(4).getLeft() - 80);
    }

    private void setClickArea(){
        int[][] offset = new int[][]{
                {10, 5, 2},
                {8, 3, 2},
                {11, 1, 3},
                {8, 7, 1},
                {9, 1, 2},
                {12, 7, 2}
        };
        int numberOfRow = 16;
        int numberOfCol = 9;
        int gridHeight = getHeight() / numberOfRow;
        int gridWidth = getWidth() / numberOfCol;
        for (int i = 0; i < numberOfSprite; i++)
        {
            MySprite area = new MySprite(
                    getContext(),
                    gridHeight * offset[i][0],
                    gridWidth * offset[i][1],
                    gridWidth * offset[i][2],
                    gridHeight * offset[i][2]
            );
            clickArea.add(area);
        }
    }

//    private void scaleSprite() {
//        int numberOfCol = 2;
//        int numberOfRow = (numberOfSprite + numberOfCol - 1) / numberOfCol;
//
//        int spriteWidth = getWidth() / (numberOfCol + 1);
//        int spriteHeight = getHeight() / (numberOfRow + 1);
//        for (int i = 0; i < numberOfSprite; i++)
//        {
//            AnimationSprite sprite = new AnimationSprite(getContext(), mainScreen, 0, 0, 0, 0);
//            sprite.setWidth(spriteWidth);
//            sprite.setHeight(spriteHeight);
//            int row = i / 2;
//            int column = i % 2;
//            sprite.setTop(spriteHeight * row + (float)spriteHeight / (numberOfRow + 1) * (row + 1));
//            sprite.setLeft(spriteWidth * column + (float)spriteWidth / (numberOfCol + 1) * (column + 1));
//            gridSprite.add(sprite);
//        }
//    }

    public void addSprite(int[] bmpIdList)
    {
        listSprite.get(currentAddedSprite).addBmp(bmpIdList);
        currentAddedSprite++;
        currentAddedSprite %= listSprite.size();
    }

    public void addSound(int spriteId, int[] soundIdList)
    {
        listSprite.get(spriteId % listSprite.size()).addSound(soundIdList);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for (AnimationSprite s : listSprite)
        {
//            Log.d("@@@@", "Draw at " + s.getLeft() + ", " + s.getTop() + " with bmp " + s.getBmpPos());
            s.draw(canvas);
        }

//        for (int i = 0; i < emotionText.size(); i++)
//        {
//            TextView tv = emotionText.get(i);
//            @SuppressLint("Range")
//            int widthSpec = View.MeasureSpec.makeMeasureSpec(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    View.MeasureSpec.UNSPECIFIED
//            );
//            int heightSpec = View.MeasureSpec.makeMeasureSpec(400,
//                    View.MeasureSpec.UNSPECIFIED
//            );
//            tv.measure(widthSpec, heightSpec);
//            tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());
//            canvas.save();
//            canvas.translate(
//                    listSprite.get(i).getLeft() + (float)(listSprite.get(i).getWidth() - tv.getWidth()) / 2,
//                    listSprite.get(i).getTop() + listSprite.get(i).getHeight()
//            );
//            tv.draw(canvas);
//            canvas.restore();
//        }
    }

    public void handleClick(float x, float y) {
        for (int i = 0; i < numberOfSprite; i++)
            if (clickArea.get(i).isSelected(x, y))
            {
                if (mode == 0)
                    if (i != currentPlaySprite && listSprite.get(currentPlaySprite).isTurnedOn)
                    {
                        listSprite.get(currentPlaySprite).isTurnedOn =
                                !listSprite.get(currentPlaySprite).isTurnedOn;
                        listSprite.get(currentPlaySprite).handleClick(getContext());
                    }
                listSprite.get(i).isTurnedOn = !listSprite.get(i).isTurnedOn;
                listSprite.get(i).handleClick(getContext());
                currentPlaySprite = i;
                break;
            }
    }

    public void setMode(int mode) {
        this.mode = mode;
        if (this.mode == 0)
            for (int i = 0; i < numberOfSprite; i++)
                if (i != currentPlaySprite)
                {
                    if (listSprite.get(i).isTurnedOn)
                    {
                        listSprite.get(i).isTurnedOn = false;
                        listSprite.get(i).handleClick(getContext());
                    }
                }
    }

//    public void muteSound()
//    {
//        for (AnimationSprite dog: listSprite)
//            dog.mute();
//    }
//
//    public void unmuteSound()
//    {
//        for (AnimationSprite dog: listSprite)
//            dog.unmute();
//    }

    public void destroyEverything()
    {
        for (AnimationSprite dog: listSprite){
            if (dog.isTurnedOn)
            {
                dog.isTurnedOn = false;
                dog.handleClick(getContext());
            }
            dog.destroyEverything();
        }
    }
}
