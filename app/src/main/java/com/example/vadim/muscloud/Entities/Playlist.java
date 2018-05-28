package com.example.vadim.muscloud.Entities;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Playlist implements Serializable {
    private String playlistName;
    private ArrayList<Song> songs;

    public Playlist() {
        playlistName = "";
        songs = new ArrayList<Song>();
    }

    public Playlist(String name, ArrayList<Song> songs) {
        this.playlistName = name;
        this.songs = songs;
    }

    public String getName() {
        return playlistName;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public Song getSong(int index) {
        return songs.get(index);
    }

    public void setName(String name) {
        playlistName = name;
    }

    public void setSongs(ArrayList<Song> songs) {
        songs.clear();
        songs.addAll(songs);
    }

    public void addSong(String path) {
        try {
            songs.add(0,new Song(path));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void addSong(Song song) {
        songs.add(0,song);
    }

    public void deleteSong(String path) {
        try {
            Song s = new Song(path);
            if (songs.contains(s)) {
                songs.remove(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int size() {
        return songs.size();
    }
}
