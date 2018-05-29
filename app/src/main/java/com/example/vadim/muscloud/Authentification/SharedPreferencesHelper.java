package com.example.vadim.muscloud.Authentification;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.vadim.muscloud.Entities.User;
import com.example.vadim.muscloud.Entities.Playlist;
import com.example.vadim.muscloud.Entities.Song;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SharedPreferencesHelper {
    public static final String SHARED_PREF_NAME = "SHARED_PREF_NAME";
    public static final String USERS_KEY = "USERS_KEY";
    public static final String SONG_KEY = "SONG_KEY";

    public static final Type USERS_TYPE = new TypeToken<List<User>>() {
    }.getType();
    public static final Type SONG_TYPE = new TypeToken<ArrayList<Song>>() {
    }.getType();

    protected SharedPreferences mSharedPreferences;
    protected Gson mGson = new Gson();

    public SharedPreferencesHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }



    public List<User> getUsers() {
        List<User> users = mGson.fromJson(mSharedPreferences.getString(USERS_KEY, ""), USERS_TYPE);
        return users == null ? new ArrayList<User>() : users;
    }

    public boolean addUser(User user) {
        List<User> users = getUsers();
        for (User u : users) {
            if (u.getLogin().equalsIgnoreCase(user.getLogin())) {
                return false;
            }
        }
        users.add(user);
        mSharedPreferences.edit().putString(USERS_KEY, mGson.toJson(users, USERS_TYPE)).apply();
        return true;
    }

    public List<String> getSuccessLogins() {
        List<String> successLogins = new ArrayList<>();
        List<User> allUsers = getUsers();
        for (User user : allUsers) {
            if (user.hasSuccessLogin()) {
                successLogins.add(user.getLogin());
            }
        }
        return successLogins;
    }

    public User login(String login, String password) {
        List<User> users = getUsers();
        for (User u : users) {
            if (login.equalsIgnoreCase(u.getLogin())
                    && password.equals(u.getPassword())) {
                u.setHasSuccessLogin(true);
                mSharedPreferences.edit().putString(USERS_KEY, mGson.toJson(users, USERS_TYPE)).apply();
                return u;
            }
        }
        return null;
    }



    public ArrayList<Song> getSongs() {
        ArrayList<Song> songs = mGson.fromJson(mSharedPreferences.getString(SONG_KEY, ""), SONG_TYPE);
        return songs == null ? new ArrayList<Song>() : songs;
    }

    public boolean addSong(String songPath) {
        List<Song> songs = getSongs();
        for (Song u : songs) {
            if (u.getSongPath().equalsIgnoreCase(songPath)) {
                return false;
            }
        }
        try {
            Song s = new Song(songPath);
            songs.add(s);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSharedPreferences.edit().putString(SONG_KEY, mGson.toJson(songs, SONG_TYPE)).apply();
        return true;
    }

    public boolean addSong(Song song) {
        List<Song> songs = getSongs();
        for (Song u : songs) {
            if (u.getSongPath().equalsIgnoreCase(song.getSongPath())) {
                return false;
            }
        }
        try {
            songs.add(song);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSharedPreferences.edit().putString(SONG_KEY, mGson.toJson(songs, SONG_TYPE)).apply();
        return true;
    }

    public void addSongs(ArrayList<Song> songs){
        for(Song x : songs)addSong(x);
    }

    public void deleteSong(Song song){
        List<Song> songs = getSongs();
        songs.remove(song);
        mSharedPreferences.edit().putString(SONG_KEY, mGson.toJson(songs, SONG_TYPE)).apply();
    }
    public void deleteAllSongs() {
        mSharedPreferences.edit().putString(SONG_KEY, mGson.toJson(new ArrayList<Song>(), SONG_TYPE)).apply();
    }



}

