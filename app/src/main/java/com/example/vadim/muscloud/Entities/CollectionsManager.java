package com.example.vadim.muscloud.Entities;


import java.util.ArrayList;
import java.util.HashMap;

public class CollectionsManager {
    HashMap<String,Artist> artists;
    HashMap<String,Album> albums;
    HashMap<String,Genre> genres;
    public CollectionsManager(){
        artists =new  HashMap<String,Artist> ();
        albums =new HashMap<String,Album>();
        genres =new HashMap<String,Genre>();
    }
    public void putAlbum(Album album){
        albums.put(album.getName(),album);
    }
    public void putArtist(Artist artist){
        artists.put(artist.getName(),artist);
    }
    public void putGenre(Genre genre){
        genres.put(genre.getName(),genre);
    }
    public Album getAlbum(String name){
        return albums.get(name);
    }
    public Artist getArtist(String name){
        return artists.get(name);
    }
    public Genre getGenre(String name){
        return genres.get(name);
    }

}
