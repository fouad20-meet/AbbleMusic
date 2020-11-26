package com.example.abblemusic;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private Bitmap image;
    private String name;
    private String email;
    private String pass;
    private ArrayList<Song> playlist;
    public User(Bitmap image, String name, String email, String pass){
        this.image = image;
        this.name = name;
        this.email = email;
        this.pass = pass;
        playlist = new ArrayList<Song>();
    }

    public User(String name, String email, String pass){
        this.image = null;
        this.name = name;
        this.email = email;
        this.pass = pass;
        playlist = new ArrayList<Song>();
    }

    public void addSong(Song song){
        playlist.add(new Song(song.getName(),song.getArtist(),song.isThe(),song.getId(),song.getImage()));
    }

    @NonNull
    public Bitmap getImage() {
        return image;
    }

    public void setImage(@NonNull Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public ArrayList<Song> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(ArrayList<Song> playlist) {
        this.playlist = playlist;
    }

    @Override
    public String toString() {
        return "User{" +
                "image=" + image +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", pass='" + pass + '\'' +
                ", playlist=" + playlist +
                '}';
    }
}
