package com.moutamid.musicapp.Model;

import java.io.Serializable;

public class Song implements Serializable {
    private String name;
    private String description;
    private int musicResourceId;

    public Song() {
    }

    public Song(String description, String name, int musicResourceId) {
        this.name = name;
        this.description = description;
        this.musicResourceId = musicResourceId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setMusicResourceId(int musicResourceId) {
        this.musicResourceId = musicResourceId;
    }

    public int getMusicResourceId() {
        return musicResourceId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }
}
