package com.example.petscommunicator.server;

import com.google.gson.annotations.SerializedName;

public class UploadAudio {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
