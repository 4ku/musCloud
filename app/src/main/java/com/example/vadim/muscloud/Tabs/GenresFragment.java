package com.example.vadim.muscloud.Tabs;

import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vadim.muscloud.Entities.Album;
import com.example.vadim.muscloud.Entities.Genre;
import com.example.vadim.muscloud.Entities.Playlist;
import com.example.vadim.muscloud.Entities.Song;
import com.example.vadim.muscloud.R;

import java.util.ArrayList;

public class GenresFragment extends Fragment {
    private Playlist curPlaylist;
    private ArrayList<Song> curSongs;
    private Genre curGenre;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab, container, false);
        v.setBackgroundColor(Color.parseColor("#f24772"));

        return v;
    }

}
