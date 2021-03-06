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

public class AudioPlayer extends Fragment implements MediaPlayer.OnPreparedListener{
    private static Playlist curPlaylist = null;
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    private static Button prev, play, next;
    private static int curIndex;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_player, container, false);
        curIndex = 0;
        prev = v.findViewById(R.id.btn_prev);
        play = v.findViewById(R.id.btn_play);
        next = v.findViewById(R.id.btn_next);


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

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        return v;
    }


    public static int getAudioSessionId(){
        return mediaPlayer.getAudioSessionId();
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
    public static void play_pause(){
        play.callOnClick();
    }
    public static void next(){
        next.callOnClick();
    }
    public static void prev(){
        prev.callOnClick();
    }

    private static void releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

