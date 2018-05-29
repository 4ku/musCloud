package com.example.vadim.muscloud.Entities;

import android.media.MediaMetadataRetriever;
import android.util.Pair;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

enum AudioFileFormat {
    mp3, wav
}

public class Song implements Serializable {

    private String title;
    private AudioFileFormat type;
    private String genre;
    private String songPath;
    private String albumName;
    private String albumArtist;
    private String artist;
    private String author;
    private String bitRate;
    private String composer;
    private String duration;

    private String year;

    public Song(String path) {

        songPath = path;

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(songPath);
        albumName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        albumArtist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
        artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        author = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);
        bitRate = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
        composer = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER);
        genre = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
        title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        year = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

        int duration = 0;
        String dur = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        if (dur != null) {
            duration = Integer.parseInt(dur)/1000;
        }
        long h = duration / 3600;
        long m = (duration - h * 3600) / 60;
        long s = duration - (h * 3600 + m * 60);
        if(h!=0){
            this.duration=String.format("%d:%02d:%02d",h,m,s);
        }else{
            this.duration=String.format("%d:%02d", m, s);
        }
    }

    public String getSongSize(){
        File file = new File(songPath);
        long Filesize=file.length()/1024;//call function and convert bytes into Kb
        if(Filesize>=1024)
            return Filesize/1024+" Mb";
        else
            return Filesize+" Kb";
    }
    public String getSongPath() {
        return songPath;
    }
    public String getAlbumName() {
        return albumName;
    }
    public String getAlbumArtist() {
        return albumArtist;
    }
    public String getArtist() {
        return artist;
    }
    public String getAuthor() {
        return author;
    }
    public String getBitRate() {
        return bitRate;
    }
    public String getComposer() {
        return composer;
    }
    public String getDuration() {
        return duration;
    }
    public String getGenre() {
        return genre;
    }
    public String getTitle() {
        return title;
    }
    public String getYear() {
        return year;
    }

    public  List<Pair<String,String>> getInfo() {
        List<Pair<String,String>> properties = new ArrayList<Pair<String, String>>();
        properties.add(new Pair<String, String>("Song path", songPath));
        properties.add(new Pair<String, String>("File size", getSongSize()));
        properties.add(new Pair<String, String>("Duration", this.duration));
        properties.add(new Pair<String, String>("Bit rate", bitRate));
        properties.add(new Pair<String, String>("Album name", albumName));
        properties.add(new Pair<String, String>("Album artist", albumArtist));
        properties.add(new Pair<String, String>("Artist", artist));
        properties.add(new Pair<String, String>("Author", author));
        properties.add(new Pair<String, String>("Composer", composer));
        properties.add(new Pair<String, String>("Genre", genre));
        properties.add(new Pair<String, String>("Title", title));
        properties.add(new Pair<String, String>("Year", year));
//        item_music_properties.put("File size", getSongSize());
//        item_music_properties.put("Duration",this.duration);
//        item_music_properties.put("Bit rate",bitRate);
//        item_music_properties.put("Album name", albumName);
//        item_music_properties.put("Album artist", albumArtist);
//        item_music_properties.put("Artist", artist);
//        item_music_properties.put("Author",author);
//        item_music_properties.put("Composer",composer);
//        item_music_properties.put("Genre",genre);
//        item_music_properties.put("Title",title);
//        item_music_properties.put("Year",year);

        return properties;
    }
    public void edit(){
        //TODO
    }
}
