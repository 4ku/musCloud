package com.example.vadim.muscloud.Entities;

public class Genre {
    private Song song;
    private String name;
    public Genre(String name, Song song){
        this.name=name;
        this.song=song;
    }
    public Song getSong(){return song;}
    public String getName(){return name;}
}