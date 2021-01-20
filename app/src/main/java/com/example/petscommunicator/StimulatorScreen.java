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
    private AddEverything adder;

    public StimulatorScreen(Context context, MainScreen mainScreen, float top, float left, int width, int height, int numberOfSprite) {
        super(context, top, left, width, height);
        this.numberOfSprite = numberOfSprite;
        this.currentAddedSprite = 0;
        this.mainScreen = mainScreen;
        this.mode = 0;
        this.currentPlaySprite = 0;
        adder = new AddEverything();

        listSprite = new ArrayList<>();
        clickArea = new ArrayList<>();
        emotionText = new ArrayList<>();

        String[] emotionName = getContext().getResources().getStringArray(R.array.emotion);
        for (String s : emotionName)
        {
            TextView tv = new TextView(getContext());
            tv.setText(s);
            tv.setTextSize(15);
            tv.setTextColor(Color.parseColor("#ede7f6"));
            emotionText.add(tv);
        }
//        scaleSprite();
        setSprite();
        setClickArea();
    }

    private void setSprite() {
        int[][] offset = new int[][]{
                {10, 3, 5},
                {7, 3, 3},
                {11, 0, 4},
                {8, 6, 2},
                {8, 0, 4},
                {13, 7, 2}
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
//<<<<<<< HEAD
            Log.d("@@@@", "Set dog " + getWidth() + " at " + dogLeft + ", " + dogTop);
//=======
//            Log.d("@@@@", "Set dog " + i + " at " + dogLeft + ", " + dogTop);
//>>>>>>> 84e25b9cbbbce94810c1f93739d844285f85c80d
            dog.setTop(dogTop);
            dog.setLeft(dogLeft);
            dog.setWidth(gridWidth * offset[i][2]);
            dog.setHeight(gridHeight * offset[i][2]);
            listSprite.add(dog);
        }

        adder.setSpritePosition(listSprite,gridWidth);
    }

    private void setClickArea(){
        int[][] offset = new int[][]{
                {11, 5, 2},
                {8, 4, 2},
                {12, 1, 3},
                {8, 7, 1},
                {9, 1, 2},
                {13, 7, 2}
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
            if(i==0){
                canvas.translate(
                        listSprite.get(i).getLeft() + (float)(listSprite.get(i).getWidth() - tv.getWidth()) / 2,
                        listSprite.get(i).getTop() + (float)(listSprite.get(i).getHeight() - 1.8 * getHeight()/16)
                );
            }
            else {
                canvas.translate(
                        listSprite.get(i).getLeft() + (float)(listSprite.get(i).getWidth() - tv.getWidth()) / 2,
                        listSprite.get(i).getTop() + listSprite.get(i).getHeight()
                );
            }
            tv.draw(canvas);
            canvas.restore();
        }

        for (AnimationSprite s : listSprite) {
//            Log.d("@@@@", "Draw at " + s.getLeft() + ", " + s.getTop() + " with bmp " + s.getBmpPos());
            s.draw(canvas);
        }
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
