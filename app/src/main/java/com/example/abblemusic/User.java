package com.example.abblemusic;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
@IgnoreExtraProperties
public class User implements Serializable {
    private String key;
    private Bitmap image;
    private String name;
    private String email;
    private String pass;
    private ArrayList<Song> playlist;
    public User(String key,Bitmap image, String name, String email, String pass){
        this.key = key;
        this.image = image;
        this.name = name;
        this.email = email;
        this.pass = pass;
        playlist = new ArrayList<Song>();
    }

    public User(String key,String name, String email, String pass){
        this.key = key;
        this.image = null;
        this.name = name;
        this.email = email;
        this.pass = pass;
        playlist = new ArrayList<Song>();
    }

    public void addSong(Song song){
        playlist.add(new Song(song.getName(),song.getArtist(),song.isThe(),song.getId(),song.getImage()));
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
