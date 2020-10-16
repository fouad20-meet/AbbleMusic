package com.example.abblemusic;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Comparable<Album>, Serializable {
    private String name;
    private String artist;
    private boolean the;
    private String image;
    private ArrayList<Song> songs;
    public Album(String name,String artist,boolean the,String image){
        this.artist=artist;
        this.image=image;
        this.name=name;
        this.the=the;
        songs = new ArrayList<Song>();
    }
    public void addSong(String name,int raw){
        songs.add(new Song(name,this.artist,this.the,raw,this.image));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public boolean isThe() {
        return the;
    }

    public void setThe(boolean the) {
        this.the = the;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    @Override
    public String toString() {
        return "Album{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", the=" + the +
                ", image='" + image + '\'' +
                ", songs=" + songs +
                '}';
    }

    @Override
    public int compareTo(Album o) {
        int i = 0;
        while (i<this.artist.length()&&i<o.getArtist().length()){
            if(this.artist.charAt(i)>o.getArtist().charAt(i))
                return 1;
            else if(this.artist.charAt(i)<o.getArtist().charAt(i))
                return -1;
            i++;
        }
        return 0;
    }
}
