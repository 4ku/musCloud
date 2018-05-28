package com.example.vadim.muscloud.Entities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlaylistManager {

    private SharedPreferences mSharedPreferences;
    private Gson mGson = new Gson();

    public static final String SHARED_PREF_NAME = "SHARED_PREF_NAME";
    public static final String PLAYLIST_KEY = "PLAYLIST_KEY";
    public static final Type PLAYLIST_TYPE = new TypeToken<List<Playlist>>() {
    }.getType();


    public PlaylistManager(Context context){
        mSharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

    }

    public ArrayList<Playlist> getPlaylists() {
        ArrayList<Playlist> plays = mGson.fromJson(mSharedPreferences.getString(PLAYLIST_KEY, ""), PLAYLIST_TYPE);
        return plays == null ? new ArrayList<Playlist>() : plays;
    }
    public boolean addPlaylist(Playlist playlist) {
        List<Playlist> plays = getPlaylists();
        for (Playlist p : plays) {
            if (p.getName().equalsIgnoreCase(playlist.getName())) {
                return false;
            }
        }
        plays.add(playlist);
        mSharedPreferences.edit().putString(PLAYLIST_KEY, mGson.toJson(plays, PLAYLIST_TYPE)).apply();
        return true;
    }
    public void addPlaylists(ArrayList<Playlist> playlists){
        deleteAllPlaylists();
        for(Playlist x:playlists){
            addPlaylist(x);
        }
    }
    public void deleteAllPlaylists() {
        mSharedPreferences.edit().putString(PLAYLIST_KEY, mGson.toJson(new ArrayList<Playlist>(), PLAYLIST_TYPE)).apply();
    }

}
