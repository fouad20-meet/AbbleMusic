package com.example.abblemusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.Collections;

public class AlbumActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener, MediaPlayer.OnCompletionListener {
    private ImageView cover;
    private TextView name,artist;
    private ListView listView;//display
    private ArrayList<Song> songs;//DATA
    private ArrayAdapter<Song> arrayAdapter;//Adapter
    private MediaPlayer player;
    private Song playing;
    private Button playPause,shuffle;
    private Album album;
    private int index;
    private ImageButton prev,next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        setUpImageLoader();
        Intent intent = getIntent();
        album = (Album) intent.getSerializableExtra("album");
        int defaultImage = this.getResources().getIdentifier(album.getImage(),null, this.getPackageName());
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();
        ImageView imageView = findViewById(R.id.albumphoto);
        imageLoader.displayImage(album.getImage(),imageView,options);
        name = findViewById(R.id.albumname);
        name.setText(album.getName());
        artist = findViewById(R.id.albumartist);
        artist.setText(album.getArtist());
        listView = findViewById(R.id.listViewsongs);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        songs = album.getSongs();
        Collections.sort(songs);
        arrayAdapter = new AlbumSongsArrayAdapter(this,R.layout.custum_album_row, songs);
        listView.setAdapter(arrayAdapter);
        playPause=findViewById(R.id.playpause);
        playPause.setOnClickListener(this);
        shuffle = findViewById(R.id.shufflealbum);
        shuffle.setOnClickListener(this);
        prev = findViewById(R.id.prevalbum);
        prev.setOnClickListener(this);
        next = findViewById(R.id.nextalbum);
        next.setOnClickListener(this);
    }

    private void setUpImageLoader(){
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Song song = (Song)listView.getItemAtPosition(position);
        if (playing!=null){
            stopPlayer(playing);
        }
        playing = song;
        player = MediaPlayer.create(this,song.getId());
        player.setOnCompletionListener(this);
        index = position;
        player.start();
        playPause.setText("Pause");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Song song = (Song)listView.getItemAtPosition(position);
        if (song == playing){
            stopPlayer(song);
        }
        return true;
    }

    private void stopPlayer(Song song){
        if (player!=null){
            player.release();
            player = null;
            playing = null;
            playPause.setText("Play");
        }
    }

    @Override
    public void onClick(View v) {
        if (v==playPause) {
            if (playing == null) {
                player = MediaPlayer.create(this, songs.get(0).getId());
                player.setOnCompletionListener(this);
                index = 0;
                player.start();
                playPause.setText("Pause");
                playing = songs.get(0);
            } else if (playing != null && player.isPlaying()) {
                player.pause();
                playPause.setText("Play");
            } else {
                player.start();
                playPause.setText("Pause");
            }
        }
        else if (v==prev){
            if (index>0){
                stopPlayer(playing);
                playPause.setText("Pause");
                Song song = songs.get(index-1);
                player = MediaPlayer.create(this,song.getId());
                index--;
                playing = song;
                player.start();
            }
        }
        else if (v==next){
            if (index<songs.size()-1){
                stopPlayer(playing);
                playPause.setText("Pause");
                Song song = songs.get(index+1);
                player = MediaPlayer.create(this,song.getId());
                index++;
                playing = song;
                player.start();
            }
        }
        else if (v==shuffle){
            int length = songs.size();
            int order = (int) ((int)length*(Math.random()));
            if (playing!=null)
                stopPlayer(playing);
            player = MediaPlayer.create(this,songs.get(order).getId());
            player.start();
            index = order;
            playing = songs.get(order);
            playPause.setText("Pause");
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (index<songs.size()-1) {
            Song song = songs.get(index + 1);
            mp = MediaPlayer.create(this, song.getId());
            player = mp;
            index++;
            playing = song;
            player.start();
        }
        else {
            Song song = songs.get(0);
            player = MediaPlayer.create(this, song.getId());
            index=0;
            playing = song;
            player.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.myAccount){
            Intent intent = new Intent(this, AccountActivity.class);
            stopPlayer(playing);
            startActivity(intent);
        }
        else if (id == R.id.signout){
            Intent intent = new Intent(this,SignInActivity.class);
            stopPlayer(playing);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.backmenu){
            Intent intent = new Intent(this,MainActivity.class);
            stopPlayer(playing);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}