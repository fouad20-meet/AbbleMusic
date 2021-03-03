package com.example.abblemusic;

import java.io.Serializable;

public class Song implements Comparable<Song>, Serializable {
    private String name;
    private String artist;
    private boolean the;
    private int id;
    private String key;
    private String image;
    public Song(){
    }
    public Song(String name,String artist,boolean the,int id,String image){
        this.name=name;
        this.artist=artist;
        this.the=the;
        this.id=id;
        this.image=image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isThe() {
        return the;
    }

    public void setThe(boolean the) {
        this.the = the;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Song{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", id=" + id +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public int compareTo(Song o) {
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
