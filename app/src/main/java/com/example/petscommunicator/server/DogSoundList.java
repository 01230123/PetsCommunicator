package com.example.petscommunicator.server;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DogSoundList {


    @SerializedName("dogSounds")
    private List<List<String>> dogSounds;


    public List<List<String>> getDogSounds() {
        return dogSounds;
    }
}
