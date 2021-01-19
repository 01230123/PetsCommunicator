package com.example.petscommunicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainScreen extends View {
    private StimulatorScreen stimulatorScreen;
    private TranslatorScreen translatorScreen;
    private TransitionScreen transitionScreen;

    private SettingIconSprite settingIconSprite;
    private SettingMenuSprite settingMenuSprite;
    private TypeSwitchSprite typeSwitchSprite;
    private MySprite background;

    private int screenWidth, screenHeight;

    private AddEverything adder;

    private float xDown, yDown;
    private float singleClickArea = 44;

    public MainScreen(Context context, int width, int height) {
        super(context);
        this.screenWidth = width;
        this.screenHeight = height;

        adder = new AddEverything();


        createSettingIcon();
        createSettingMenu();
        createSwitchTypeButton();
        createStimulatorScreen();
        createTranslatorScreen();
        createTransitionScreen();
        createBackground();

    }

    private void createBackground() {
        Log.d("@@@@", "Background loading...");
        background = new MySprite(
                getContext(),
                0,
                0,
                screenWidth,
                screenHeight - typeSwitchSprite.getHeight()
        );
        background.addBmp(new int[]{R.drawable.bgdog});
    }

    private void createTranslatorScreen() {
        Log.d("@@@@", "Translator Screen loading...");
        translatorScreen = new TranslatorScreen(
                getContext(),
                this,
                0,
                0,
                screenWidth,
                screenHeight - typeSwitchSprite.getHeight());

        translatorScreen.addBmp(new int[]{R.drawable.bgrec});
    }

    private void createStimulatorScreen() {
        Log.d("@@@@", "Stimulator Screen loading...");
        stimulatorScreen = new StimulatorScreen(
                getContext(),
                this,
                0,
                0,
                screenWidth,
                screenHeight - typeSwitchSprite.getHeight(),
                6);

        stimulatorScreen.addBmp(new int[]{R.drawable.bgdog});
        adder.addStimulatorScreen(stimulatorScreen);

    }

    private void createTransitionScreen() {
        Log.d("@@@@", "Transition Screen loading...");
        transitionScreen = new TransitionScreen(
                getContext(),
                this,
                new MySprite[]{
                        stimulatorScreen,
                        translatorScreen
                }
        );
    }

    private void createSettingIcon() {
        Log.d("@@@@", "Setting Icon loading...");
        int length = screenWidth / 7;
        settingIconSprite = new SettingIconSprite(
                getContext(),
                0,
                screenWidth - length,
                length
        );

        adder.addSettingIcon(settingIconSprite);
    }

    private void createSettingMenu() {
        Log.d("@@@@", "Setting Menu loading...");
        int width = (int)(screenWidth / 1.2);
        int height = screenHeight - settingIconSprite.getLength() * 2;
        settingMenuSprite = new SettingMenuSprite(
                getContext(),
                (float)(screenHeight - height) / 2,
                (float)(screenWidth - width) / 2,
                width, height
        );

        adder.addSettingMenu(settingMenuSprite);
    }

    private void createSwitchTypeButton() {
        Log.d("@@@@", "Switch Type loading...");
        typeSwitchSprite = new TypeSwitchSprite(
                getContext(),
                screenWidth,
                screenHeight,
                2
        );

        adder.addSwitchType(typeSwitchSprite);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        background.draw(canvas);
        if (!transitionScreen.isRunning())
        {
            if (typeSwitchSprite.getCurrentType() == 0)
                stimulatorScreen.draw(canvas);
            else
                translatorScreen.draw(canvas);
        }
        else
            transitionScreen.draw(canvas);
        typeSwitchSprite.draw(canvas);
        settingIconSprite.draw(canvas);
        if (settingIconSprite.isTurnedOn)
        {
            settingMenuSprite.draw(canvas);
        }
    }

    private boolean singleClickFlag = true;
    private float xCur, yCur;
    private boolean swipeFlag = false;

    private CountDownTimer swipeTimer = new CountDownTimer(50, 50) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            swipeFlag = false;
            xDown = xCur;
            yDown = yCur;
            swipeTimer.cancel();
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int eventAction = event.getActionMasked();
        switch (eventAction)
        {
            case MotionEvent.ACTION_DOWN:
                setupTouchPointer(x, y);
                if (!settingIconSprite.isTurnedOn)
                    handleTypeSelect();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isSingleClick(x, y))
                {
                    singleClickFlag = false;
                    typeSwitchSprite.update(0);
                    invalidate();
                }
                if (!singleClickFlag && !settingIconSprite.isTurnedOn)
                    handleSwipeEvent(x, y);
                break;
            case MotionEvent.ACTION_UP:
                if (singleClickFlag)
                    handleSingleClick(x, y);
                else if (!settingIconSprite.isTurnedOn)
                    handleSwipeRelease(x, y);
                break;
        }
        return true;
    }

    private void setupTouchPointer(float x, float y)
    {
        singleClickFlag = true;
        swipeFlag = false;
        xDown = x;
        yDown = y;
        xCur = x;
        yCur = y;
    }

    private void handleSwipeEvent(float x, float y)
    {
        transitionScreen.startDragTransition(
                (int)(x - xCur),
                typeSwitchSprite.getCurrentType()
        );
        xCur = x;
        yCur = y;

        if (swipeFlag)
            swipeTimer.cancel();
        swipeFlag = true;
        swipeTimer.start();
    }

    private void handleSwipeRelease(float x, float y)
    {
        if (swipeFlag)
        {
            if (Math.abs(x - xDown) >= 150)
            {
                int temp = typeSwitchSprite.getCurrentType();
                if (x < xDown)
                    temp = Math.min(temp + 1, 1);
                else
                    temp = Math.max(temp - 1, 0);
                typeSwitchSprite.setCurrentType(
                        transitionScreen.releaseDragTransition(temp)
                );
            }
            else
                typeSwitchSprite.setCurrentType(
                        transitionScreen.releaseDragTransition(-1)
                );
        }
        else
        {
            typeSwitchSprite.setCurrentType(
                    transitionScreen.releaseDragTransition(-1)
            );
        }
        if (typeSwitchSprite.getCurrentType() == 0 && translatorScreen.rec)
            translatorScreen.doRecord();
    }

    private void handleTypeSelect() {
        if (typeSwitchSprite.isSelected(xDown, yDown))
        {
            typeSwitchSprite.handleSelected(xDown, yDown);
            invalidate();
        }
    }

    private void handleSingleClick(float x, float y) {
        Log.d("@@@@", "Screen " + typeSwitchSprite.getCurrentType() + " at " + x + ", " + y);
        if (settingIconSprite.isTurnedOn)
            handleSettingMenuClick(x, y);
        else if (settingIconSprite.isSelected(x, y))
            settingIconSprite.isTurnedOn = true;
        else if (typeSwitchSprite.isSelected(x, y))
        {
            int temp = typeSwitchSprite.getCurrentType();
            typeSwitchSprite.handleButtonClick(x, y);
            if (temp != typeSwitchSprite.getCurrentType())
                transitionScreen.startTransition(
                        screenWidth,
                        temp,
                        typeSwitchSprite.getCurrentType() - temp
                );
            if (typeSwitchSprite.getCurrentType() == 0 && translatorScreen.rec)
                translatorScreen.doRecord();
        }
        else if (typeSwitchSprite.getCurrentType() == 0)
            stimulatorScreen.handleClick(x, y);
        else if (typeSwitchSprite.getCurrentType() == 1)
            translatorScreen.handleClick(x, y);
        else
            return;
        invalidate();
    }

    private void handleSettingMenuClick(float x, float y) {
        if (settingMenuSprite.creditOn)
            settingMenuSprite.handleCredit(x, y);
        else if (!settingMenuSprite.isSelected(x, y))
            settingIconSprite.isTurnedOn = false;
        else
            settingMenuSprite.handleButtonsClick(x, y);
        stimulatorScreen.setMode(settingMenuSprite.getCurrentMode());
//        if (settingMenuSprite.isMute() == 0)
//            stimulatorScreen.muteSound();
//        else
//            stimulatorScreen.unmuteSound();
    }

    private boolean isSingleClick(float x, float y) {
        return Math.abs(x - xDown) <= singleClickArea && Math.abs(y - yDown) <= singleClickArea;
    }

    public void destroyEverything()
    {
        if (translatorScreen.rec)
            translatorScreen.doRecord();
        stimulatorScreen.destroyEverything();
    }
}
