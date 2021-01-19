package com.example.petscommunicator;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.CountDownTimer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnimationSprite extends MySprite {
    public boolean isTurnedOn;
    private MainScreen mainScreen;
    private MediaPlayer media;

    private List<Integer> soundList;

    public AnimationSprite(Context context, MainScreen mainScreen, int top, int left, int width, int height) {
        super(context, top, left, width, height);
        this.mainScreen = mainScreen;
        isTurnedOn = false;
        soundList = new ArrayList<>();
        media = new MediaPlayer();
    }

    public void addSound(int[] soundIdList)
    {
        for (int id : soundIdList)
            soundList.add(id);
    }

    private int getRandomSound()
    {
        if (soundList.isEmpty()) return R.raw.happy_sound_1;
        int id = (int)(Math.random() * Math.random() * 123456) % soundList.size();
        return soundList.get(id);
    }

    public CountDownTimer timer = new CountDownTimer(5000,100 ) {
        @Override
        public void onTick (long millisUntilFinished) {
            update();
            mainScreen.postInvalidate();
            if(!media.isPlaying())
            {
                media.reset();
                stopAnimation();
                isTurnedOn = false;
            }
        }

        @Override
        public void onFinish() {
            start();
        }
    };

    public void startAnimation() {
        timer.start();
    }

    public void stopAnimation() {
        timer.cancel();
        update(0);
    }

    public void handleClick(Context context) {
        if(this.isTurnedOn){
            if (!media.isPlaying())
                try {
                    openMedia();
                } catch (IOException e) {
                    stopAnimation();
                }
            startAnimation();
        }
        else{
            if(media.isPlaying())
            {
                stopAnimation();
                media.stop();
                media.reset();
            }
        }
    }

    public void mute()
    {
        media.setVolume(0.0f, 0.0f);
    }

    public void unmute()
    {
        media.setVolume(1.0f, 1.0f);
    }

    private void openMedia() throws IOException {
        AssetFileDescriptor afd = getContext().getResources().openRawResourceFd(getRandomSound());
        if (afd == null) return;
        media.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        afd.close();
        media.prepare();
        media.start();
    }

    public void destroyEverything()
    {
        media.release();
    }
}
