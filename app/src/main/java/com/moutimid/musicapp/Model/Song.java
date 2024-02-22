package com.moutimid.musicapp.Model;

import java.io.Serializable;

public class Song implements Serializable {
    private String name;
    private String description;
    private int musicResourceId;

    String artist;

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

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setName(String name) {
        this.name = name;
    }
}
