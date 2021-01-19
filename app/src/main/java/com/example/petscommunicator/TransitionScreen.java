package com.example.petscommunicator;

import android.content.Context;
import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.util.Log;

public class TransitionScreen extends MySprite{
    private MySprite[] screenList;
    private MainScreen mainScreen;

    private int curScreen;
    private int transitionWidth;
    private int direction;
    private int curTransition = 0;
    private int transitionSpeed;
    private boolean transitionFlag;

    public TransitionScreen(Context context, MainScreen mainScreen, MySprite[] screenList)
    {
        super(context, 0, 0, 0, 0);
        this.mainScreen = mainScreen;
        this.screenList = screenList;
        transitionFlag = false;
    }

    public void startTransition(int transitionWidth, int curScreen, int direction)
    {
        this.transitionWidth = transitionWidth;
        this.curScreen = curScreen;
        this.direction = direction;
        this.transitionSpeed = this.transitionWidth / 10;

        transitionFlag = true;
        timer.start();
    }

    private CountDownTimer timer = new CountDownTimer(1000, 15) {
        @Override
        public void onTick(long millisUntilFinished) {
            curTransition += transitionSpeed;
            transitionWidth -= transitionSpeed;
            if (transitionWidth * transitionSpeed <= 0)
            {
                transitionFlag = false;
                curTransition = 0;
                transitionWidth = 0;
                timer.cancel();
            }
            mainScreen.postInvalidate();
        }

        @Override
        public void onFinish() {
            timer.start();
        }
    };

    @Override
    public void draw(Canvas canvas) {
        int totalWidth = 0;
        for (int i = curScreen; i >= 0 && i < screenList.length; i += direction)
        {
            canvas.save();
            canvas.translate(direction * (totalWidth - Math.abs(curTransition)), 0);
            screenList[i].draw(canvas);
            canvas.restore();
            totalWidth += screenList[i].getWidth();
        }
    }

    public boolean isRunning()
    {
        return transitionFlag || dragFlag;
    }

    private boolean dragFlag = false;

    public void startDragTransition(int transitionWidth, int curScreen)
    {
        dragFlag = true;
        this.curTransition += transitionWidth;
        if (this.curTransition < 0)
            this.direction = 1;
        else
            this.direction = -1;
        this.curScreen = curScreen;
        mainScreen.postInvalidate();
    }

    public int releaseDragTransition(int force)
    {
        int totalWidth = -screenList[0].getWidth() * curScreen;
        int pos = curScreen;
        int val = 0;
        for (int i = 0; i < screenList.length; i++)
        {
            int x = Math.max(0, totalWidth + curTransition);
            int y = Math.min(
                    screenList[i].getWidth(),
                    totalWidth + curTransition + screenList[i].getWidth()
            );
            if (y - x > val)
            {
                pos = i;
                val = y - x;
            }
            totalWidth += screenList[i].getWidth();
        }
        if (force >= 0) pos = force;
        totalWidth = -screenList[0].getWidth() * curScreen;
        for (int i = 0; i <screenList.length; i++)
        {
            if (i == pos)
            {
                int x = totalWidth + curTransition;
                curTransition = x;
                if (x > 0)
                    startTransition(-x, i, -1);
                else
                    startTransition(-x, i, 1);
            }
            totalWidth += screenList[i].getWidth();
        }
        dragFlag = false;
        return pos;
    }
}
