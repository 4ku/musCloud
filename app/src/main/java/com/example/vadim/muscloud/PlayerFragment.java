package com.example.vadim.muscloud;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vadim.muscloud.Entities.Playlist;

import java.io.IOException;

public class PlayerFragment extends Fragment implements MediaPlayer.OnPreparedListener{
    private static Playlist curPlaylist = null;
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    private Button rand, prev, play, next, loop;
    private static int curIndex;

    //private static Boolean play_ = false;

    //----------------------------------------------------------------------------------

//    public static PlayerFragment newInstance() {
//        Bundle args = new Bundle();
//        PlayerFragment fragment = new PlayerFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fr_player, container, false);
        curIndex = 0;
        rand = v.findViewById(R.id.btn_random);
        prev = v.findViewById(R.id.btn_prev);
        play = v.findViewById(R.id.btn_play);
        next = v.findViewById(R.id.btn_next);
        loop = v.findViewById(R.id.btn_loop);

        rand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                releaseMP();
                curIndex -= 1;
                if (curIndex < 0) {
                    curIndex = curPlaylist.size() - 1;
                }
                try {
                    if (curPlaylist != null) {
                        mediaPlayer.setDataSource(curPlaylist.getSongs().get(curIndex).getSongPath());
                        mediaPlayer.prepare();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    play.setBackground(getResources().getDrawable(android.R.drawable.ic_media_play));
                } else {
                    mediaPlayer.start();
                    play.setBackground(getResources().getDrawable(android.R.drawable.ic_media_pause));
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseMP();
                curIndex += 1;
                if (curPlaylist.size() <= curIndex) {
                    curIndex = 0;
                }
                try {
                    if (curPlaylist != null) {
                        mediaPlayer.setDataSource(curPlaylist.getSongs().get(curIndex).getSongPath());
                        mediaPlayer.prepare();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isLooping()) {
                        mediaPlayer.setLooping(false);
                    } else {
                        mediaPlayer.setLooping(true);
                    }
                }
            }
        });
        mediaPlayer = new MediaPlayer();
        //mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        return v;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMP();
    }

    public static void setPlaylist(Playlist playlist, int index) {
        releaseMP();
        curPlaylist = playlist;
        try {
            if (curPlaylist != null) {
                curIndex = index;
                if (curPlaylist.size() <= curIndex) {
                    curIndex = 0;
                }
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        curIndex += 1;
                        if (curPlaylist.size() <= curIndex) {
                            curIndex = 0;
                        }
                        try {
                            if (curPlaylist != null) {
                                mediaPlayer.setDataSource(curPlaylist.getSongs().get(curIndex).getSongPath());
                                mediaPlayer.prepare();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(curPlaylist.getSongs().get(curIndex).getSongPath());
                mediaPlayer.prepare();
                //mediaPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

