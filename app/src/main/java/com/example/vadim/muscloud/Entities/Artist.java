package com.example.vadim.muscloud.Entities;

public class Artist {
    private Song song;
    private String name;
    public Artist(String name, Song song){
        this.name=name;
        this.song=song;
    }
    public Song getSong(){return song;}
    public String getName(){return name;}
}