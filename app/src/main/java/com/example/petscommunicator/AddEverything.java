package com.example.petscommunicator;

public class AddEverything {
    public void addStimulatorScreen(StimulatorScreen stimulatorScreen)
    {
        stimulatorScreen.addSprite(new int[]{
                R.drawable.happy1,
                R.drawable.happy2,
                R.drawable.happy3,
                R.drawable.happy4,
                R.drawable.happy5,
                R.drawable.happy6,
                R.drawable.happy7,
                R.drawable.happy8,
                R.drawable.happy9,
                R.drawable.happy10
        });
        stimulatorScreen.addSound(0, new int[]{
                R.raw.happy_sound_1,
                R.raw.happy_sound_2,
                R.raw.happy_sound_3,
                R.raw.happy_sound_4,
                R.raw.happy_sound_5
        });

        stimulatorScreen.addSprite(new int[]{
                R.drawable.angry1,
                R.drawable.angry2,
                R.drawable.angry3,
                R.drawable.angry4,
                R.drawable.angry5,
                R.drawable.angry6,
                R.drawable.angry7,
                R.drawable.angry8,
                R.drawable.angry9,
                R.drawable.angry10
        });
        stimulatorScreen.addSound(1, new int[]{
                R.raw.angry_sound_1,
                R.raw.angry_sound_2,
                R.raw.angry_sound_3
        });

        stimulatorScreen.addSprite(new int[]{
                R.drawable.makefriend0,
                R.drawable.makefriend1,
                R.drawable.makefriend2,
                R.drawable.makefriend3,
                R.drawable.makefriend4,
                R.drawable.makefriend5,
                R.drawable.makefriend6,
                R.drawable.makefriend7
        });
        stimulatorScreen.addSound(2, new int[]{
                R.raw.friend_sound_1,
                R.raw.friend_sound_2,
                R.raw.friend_sound_3
        });

        stimulatorScreen.addSprite(new int[]{
                R.drawable.upset1,
                R.drawable.upset2,
                R.drawable.upset3,
                R.drawable.upset4,
                R.drawable.upset5,
                R.drawable.upset6,
                R.drawable.upset7,
                R.drawable.upset8,
                R.drawable.upset9,
                R.drawable.upset10
        });
        stimulatorScreen.addSound(3, new int[]{
                R.raw.upset_sound_1,
                R.raw.upset_sound_2,
                R.raw.upset_sound_3
        });

        stimulatorScreen.addSprite(new int[]{
                R.drawable.baby1,
                R.drawable.baby2,
                R.drawable.baby3,
                R.drawable.baby4,
                R.drawable.baby5,
                R.drawable.baby6,
                R.drawable.baby7,
                R.drawable.baby8,
                R.drawable.baby9,
                R.drawable.baby10
        });
        stimulatorScreen.addSound(4, new int[]{
                R.raw.baby_sound_1,
                R.raw.baby_sound_2,
                R.raw.baby_sound_3
        });

        stimulatorScreen.addSprite(new int[]{
                R.drawable.terrorized0,
                R.drawable.terrorized1,
                R.drawable.terrorized2,
                R.drawable.terrorized3,
                R.drawable.terrorized4
        });
        stimulatorScreen.addSound(5, new int[]{
                R.raw.scary_sound_1,
                R.raw.scary_sound_2,
                R.raw.scary_sound_3,
                R.raw.scary_sound_4
        });
    }

    public void addSettingMenu(SettingMenuSprite settingMenuSprite)
    {
        settingMenuSprite.setMenuBmp(R.drawable.menu_background);
        settingMenuSprite.setMenuTextBmp(new int[] {
                R.drawable.setting_text
        });
        settingMenuSprite.setModeButtonBmp(new int[]{
                R.drawable.mode_1,
                R.drawable.mode_2
        });
        settingMenuSprite.setSoundButtonBmp(new int[]{
                R.drawable.sound_1
        });
        settingMenuSprite.setCreditButtonBmp(new int[]{
                R.drawable.credit_1
        });
    }

    public void addSettingIcon(SettingIconSprite settingIconSprite)
    {
        settingIconSprite.addBmp(new int[] {
                R.drawable.setting_icon
        });
    }

    public void addSwitchType(TypeSwitchSprite typeSwitchSprite)
    {
        typeSwitchSprite.addBmp(new int[]{
                R.drawable.type_0,
                R.drawable.type_1,
                R.drawable.type_2
        });

        typeSwitchSprite.addTypeIcon(new int[]{
                R.drawable.type_icon_1_1, R.drawable.type_icon_1_2,
                R.drawable.type_icon_2_1, R.drawable.type_icon_2_2
        });
    }
}
