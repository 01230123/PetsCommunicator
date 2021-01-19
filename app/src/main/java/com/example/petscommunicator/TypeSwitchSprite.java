package com.example.petscommunicator;

import android.content.Context;
import android.graphics.Canvas;

public class TypeSwitchSprite extends MySprite{
    private int numberOfType;
    private int currentType;

    private MySprite[] typeIcon;
    private int typeWidth;

    public TypeSwitchSprite(Context context, int width, int top, int numberOfType)
    {
        super(context, top, 0, width, 0);

        this.numberOfType = numberOfType;
        currentType = 0;
        typeIcon = new MySprite[numberOfType];
        typeWidth = width / numberOfType;
    }

    @Override
    public void addBmp(int[] bmpIdList) {
        if (getBmpPos() == -1)
        {
            scaleBitmap(bmpIdList[0], getWidth(), (int)getTop());
            setTop(getTop() - getHeight());
            createType();
        }
        super.addBmp(bmpIdList);
    }

    private void createType() {
        for (int i = 0; i < numberOfType; i++)
            typeIcon[i] = new MySprite(
                    getContext(),
                    getTop(), typeWidth * i,
                    typeWidth, getHeight()
            );
    }

    public void addTypeIcon(int[] bmpIdList)
    {
        for (int i = 0; i < numberOfType; i++)
            typeIcon[i].addBmp(new int[] {bmpIdList[i * 2], bmpIdList[i * 2 + 1]});
        typeIcon[currentType].update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for (MySprite t : typeIcon)
            t.draw(canvas);
    }

    public void handleButtonClick(float x, float y)
    {
        update(0);
        typeIcon[currentType].update();
        for (int i = 0; i < numberOfType; i++)
            if (typeWidth * i <= x && x <= typeWidth * (i + 1))
            {
                typeIcon[i].update();
                currentType = i;
                break;
            }
    }

    public void handleSelected(float x, float y)
    {
        for (int i = 0; i < numberOfType; i++)
            if (typeWidth * i <= x && x <= typeWidth * (i + 1))
            {
                update(i + 1);
                break;
            }
    }

    public int getCurrentType()
    {
        return currentType;
    }

    public void setCurrentType(int t) {
        typeIcon[this.currentType].update();
        this.currentType = t;
        typeIcon[this.currentType].update();
    }
}
