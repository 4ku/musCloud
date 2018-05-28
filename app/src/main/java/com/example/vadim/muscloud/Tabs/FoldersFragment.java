package com.example.vadim.muscloud.Tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.vadim.muscloud.Authentification.SharedPreferencesHelper;
import com.example.vadim.muscloud.Entities.Playlist;
import com.example.vadim.muscloud.Entities.Song;
import com.example.vadim.muscloud.Extra.FolderListAdapter;
import com.example.vadim.muscloud.Extra.MusicListAdapter;
import com.example.vadim.muscloud.MusicActivity;
import com.example.vadim.muscloud.PlayerFragment;
import com.example.vadim.muscloud.R;
import com.example.vadim.muscloud.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FoldersFragment extends Fragment implements OnBackPressedListener {
    private Playlist curPlaylist;
    private ArrayList<Song> curSongs;
    private ArrayList<String> directories;
    private SharedPreferencesHelper mSharedPreferencesHelper;
    boolean inSongs;
    private ListView folderListView;
    Map<String,ArrayList<Song>> map;
    private int SEARCH_DEPTH=1;


    private AdapterView.OnItemClickListener adapterViewClick=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(inSongs){
                PlayerFragment.setPlaylist(curPlaylist, position);
            } else{
                File clickedFile = new File(directories.get(position));
                if (clickedFile != null) {
                    curSongs=map.get(directories.get(position));
                    curPlaylist = new Playlist("FoldersSong", curSongs);
                    folderListView.setAdapter(new MusicListAdapter(getActivity(), curSongs));
                }
                inSongs=true;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_folders, container, false);
        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity());
        curSongs=new ArrayList<Song>();
        directories =new ArrayList<String>();
        inSongs=false;

        //find Folders
        map=new HashMap<String,ArrayList<Song>>();
        for(Song s: mSharedPreferencesHelper.getSongs()){
            File file=new File(s.getSongPath());
            for(int i = 0; i<SEARCH_DEPTH &&
                    !file.getAbsolutePath().equals(MusicActivity.MAIN_DIR.getAbsolutePath());i++) {
                file=file.getParentFile();
            }

            String songFolder=file.getAbsolutePath();
            if(map.get(songFolder)==null){
                ArrayList<Song> ar=new ArrayList<Song>();
                ar.add(s);
                map.put(songFolder,ar);
            }else{
                ArrayList<Song> ar=map.get(songFolder);
                ar.add(s);
                map.put(songFolder,ar);
            }
        }

        directories.addAll(map.keySet());

        folderListView = v.findViewById(R.id.arList_folders);
        folderListView.setOnItemClickListener(adapterViewClick);
        folderListView.setAdapter(new FolderListAdapter(getActivity(), directories));
        return v;
    }

    @Override
    public void onBackPressed() {
        inSongs=false;
        folderListView.setAdapter(new FolderListAdapter(getActivity(), directories));
    }

}
