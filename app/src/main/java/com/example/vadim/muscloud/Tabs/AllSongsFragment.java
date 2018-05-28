package com.example.vadim.muscloud.Tabs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.vadim.muscloud.Authentification.SharedPreferencesHelper;
import com.example.vadim.muscloud.Entities.Playlist;
import com.example.vadim.muscloud.Extra.MusicListAdapter;
import com.example.vadim.muscloud.OnBackPressedListener;
import com.example.vadim.muscloud.PlayerFragment;
import com.example.vadim.muscloud.R;
import com.example.vadim.muscloud.Entities.Song;

import java.io.File;
import java.util.ArrayList;


public class AllSongsFragment extends Fragment implements OnBackPressedListener {
    private Playlist curPlaylist;
    private ArrayList<Song> curSongs;
    private SharedPreferencesHelper mSharedPreferencesHelper;
    private ListView songsListView;
//    public static int color= Color.parseColor("#FFFF4444");

    private AdapterView.OnItemClickListener adapterViewClick=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String selectedFileString = curSongs.get(position).getSongPath();
            File clickedFile = new File(selectedFileString);
            if (clickedFile != null) {
                PlayerFragment.setPlaylist(curPlaylist, position);
            }
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_all_songs, container, false);
//        v.getBackground().

        songsListView = v.findViewById(R.id.arList_all_song);
        songsListView.setOnItemClickListener(adapterViewClick);

        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity());

        if(getArguments()!=null && getArguments().containsKey("Playlist")){
            curPlaylist=(Playlist) getArguments().get("Playlist");
            curSongs=curPlaylist.getSongs();
            v.setBackgroundColor(getResources().getColor(R.color.yellow));
        }else{
            curSongs=mSharedPreferencesHelper.getSongs();
            curPlaylist = new Playlist("Default", curSongs);
        }
        songsListView.setAdapter(new MusicListAdapter(getActivity(), curSongs));
        return v;
    }
    @Override
    public void onBackPressed() {
        if(getArguments()!=null && getArguments().containsKey("Playlist")) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.playListFragment,new PlayListFragment())
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    }
}

