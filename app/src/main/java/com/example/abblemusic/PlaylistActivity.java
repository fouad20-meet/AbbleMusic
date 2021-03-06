package com.example.abblemusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;

public class PlaylistActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener, MediaPlayer.OnCompletionListener, PlayingFragment.PlayingFragmentListener, SeekBar.OnSeekBarChangeListener {
    private ListView listView;//display
    private ArrayList<Song> songs;//DATA
    private ArrayAdapter<Song> arrayAdapter;//Adapter
    private MediaPlayer player;
    private Song playing;
    private Button playPause,shuffle;
    private int index;
    private SeekBar seekBar;
    private TextView place,duration;
    private PlayingFragment fragment;
    private Handler mHandler = new Handler();
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        songs = new ArrayList<Song>();
        songs.add(new Song("Killing Me Softly With His Song","Fugees",true,R.raw.fugees,"@drawable/fugees"));
        songs.add(new Song("Tears Dry On Their Own","Amy Winehouse",false,R.raw.amytearsdry,"@drawable/amy"));
        songs.add(new Song("The Man Who Sold the World","Nirvana",false,R.raw.nirvanasoldworld,"@drawable/nirvana"));
        songs.add(new Song("Maybe","Janis Joplin",false,R.raw.janismaybe,"@drawable/janis"));
        songs.add(new Song("No. 1 Party Anthem","Arctic Monkeys",false, R.raw.arcticno1,"@drawable/arcticmonkeys"));
        songs.add(new Song("Something","Beatles",true,R.raw.thebeatlessomething,"@drawable/beatles"));
        songs.add(new Song("Can't Take My Eyes Off of You","Lauryn Hill",false,R.raw.lauryneyesoffyou,"@drawable/lauryn"));
        songs.add(new Song("Time","Pink Floyd",false,R.raw.pinktime,"@drawable/pinkfloyd"));
        songs.add(new Song("Fu-gee-la","Fugees",true,R.raw.fugeesfugeela,"@drawable/fugees"));
        Collections.sort(songs);
        arrayAdapter = new SongArrayAdapter(this,R.layout.custom_row, songs);
        listView.setAdapter(arrayAdapter);
        playPause=findViewById(R.id.playPause);
        playPause.setOnClickListener(this);
        shuffle = findViewById(R.id.shuffle);
        seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(this);
        place = findViewById(R.id.place);
        duration = findViewById(R.id.duration);
        //Make sure you update Seekbar on UI thread
        PlaylistActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(player != null){
                    int mCurrentPosition = player.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    if (mCurrentPosition%60<10)
                        place.setText(((mCurrentPosition/60))+":0"+(mCurrentPosition%60));
                    else
                        place.setText(((mCurrentPosition/60))+":"+(mCurrentPosition%60));
                }
                mHandler.postDelayed(this, 1000);
            }
        });
        shuffle.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        fragment = (PlayingFragment) getSupportFragmentManager().findFragmentById(R.id.frPlaying);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Song song = (Song)listView.getItemAtPosition(position);
        if (playing!=null){
            stopPlayer(playing);
        }
        playing = song;
        player = MediaPlayer.create(this,song.getId());
        seekBar.setMax(player.getDuration()/1000);
        if ((player.getDuration()/1000)%60<10)
            duration.setText((player.getDuration()/1000)/60+ ":0" + (player.getDuration()/1000)%60);
        else
            duration.setText((player.getDuration()/1000)/60+ ":" + (player.getDuration()/1000)%60);
        place.setText("0:00");
        seekBar.setProgress(0);
        fragment.setSong(playing.getImage(),playing.getName());
        index = position;
        player.setOnCompletionListener(this);
        player.start();
        playPause.setText("Pause");
        fragment.changeIcon(true);
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
        if (player!=null && playing!=null){
            player.release();
            playing = null;
            player = null;
            playPause.setText("Play");
            fragment.changeIcon(false);
            fragment.setSong("@drawable/nosong","Not Playing");
        }
    }

    @Override
    public void onClick(View v) {
        if (v==playPause){
            if (playing==null) {
                player = MediaPlayer.create(this, songs.get(0).getId());
                seekBar.setMax(player.getDuration()/1000);
                if ((player.getDuration()/1000)%60<10)
                    duration.setText((player.getDuration()/1000)/60+ ":0" + (player.getDuration()/1000)%60);
                else
                    duration.setText((player.getDuration()/1000)/60+ ":" + (player.getDuration()/1000)%60);
                place.setText("0:00");
                seekBar.setProgress(0);
                index = 0;
                player.setOnCompletionListener(this);
                player.start();
                playPause.setText("Pause");
                fragment.changeIcon(true);
                playing = songs.get(0);
                fragment.setSong(playing.getImage(),playing.getName());
            }
            else if (player!=null && player.isPlaying()) {
                player.pause();
                playPause.setText("Play");
                fragment.changeIcon(false);
            }
            else {
                player.start();
                playPause.setText("Pause");
                fragment.changeIcon(true);
            }
        }
        else if (v==shuffle){
            int length = songs.size();
            int order = (int) ((int)length*(Math.random()));
            if (playing!=null)
                stopPlayer(playing);
            player = MediaPlayer.create(this,songs.get(order).getId());
            seekBar.setMax(player.getDuration()/1000);
            if ((player.getDuration()/1000)%60<10)
                duration.setText((player.getDuration()/1000)/60+ ":0" + (player.getDuration()/1000)%60);
            else
                duration.setText((player.getDuration()/1000)/60+ ":" + (player.getDuration()/1000)%60);
            place.setText("0:00");
            seekBar.setProgress(0);
            index = order;
            player.setOnCompletionListener(this);
            player.start();
            playing = songs.get(order);
            fragment.setSong(playing.getImage(),playing.getName());
            playPause.setText("Pause");
            fragment.changeIcon(true);
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
            mAuth.signOut();
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
    public void onCompletion(MediaPlayer mp) {
        stopPlayer(playing);
        if (index<songs.size()-1) {
            Song song = songs.get(index + 1);
            player = MediaPlayer.create(this, song.getId());
            seekBar.setMax(player.getDuration()/1000);
            if ((player.getDuration()/1000)%60<10)
                duration.setText((player.getDuration()/1000)/60+ ":0" + (player.getDuration()/1000)%60);
            else
                duration.setText((player.getDuration()/1000)/60+ ":" + (player.getDuration()/1000)%60);
            place.setText("0:00");
            seekBar.setProgress(0);
            index++;
            playing = song;
            fragment.setSong(playing.getImage(),playing.getName());
            player.start();
            fragment.changeIcon(true);
            playPause.setText("Pause");
        }
        else {
            Song song = songs.get(0);
            player = MediaPlayer.create(this, song.getId());
            seekBar.setMax(player.getDuration()/1000);
            if ((player.getDuration()/1000)%60<10)
                duration.setText((player.getDuration()/1000)/60+ ":0" + (player.getDuration()/1000)%60);
            else
                duration.setText((player.getDuration()/1000)/60+ ":" + (player.getDuration()/1000)%60);
            place.setText("0:00");
            seekBar.setProgress(0);
            index=0;
            playing = song;
            fragment.setSong(playing.getImage(),playing.getName());
            player.start();
            fragment.changeIcon(true);
            playPause.setText("Pause");
        }
    }

    public void next(){
        if (index<songs.size()){
            if (playing!=null)
                stopPlayer(playing);
            index++;
            Song song = songs.get(index);
            player = MediaPlayer.create(this, song.getId());
            seekBar.setMax(player.getDuration()/1000);
            if ((player.getDuration()/1000)%60<10)
                duration.setText((player.getDuration()/1000)/60+ ":0" + (player.getDuration()/1000)%60);
            else
                duration.setText((player.getDuration()/1000)/60+ ":" + (player.getDuration()/1000)%60);
            place.setText("0:00");
            seekBar.setProgress(0);
            playing = song;
            fragment.setSong(playing.getImage(),playing.getName());
            player.start();
            fragment.changeIcon(true);
            playPause.setText("Pause");
        }
        else {
            if (playing!=null)
                stopPlayer(playing);
            Song song = songs.get(0);
            player = MediaPlayer.create(this, song.getId());
            seekBar.setMax(player.getDuration()/1000);
            if ((player.getDuration()/1000)%60<10)
                duration.setText((player.getDuration()/1000)/60+ ":0" + (player.getDuration()/1000)%60);
            else
                duration.setText((player.getDuration()/1000)/60+ ":" + (player.getDuration()/1000)%60);
            place.setText("0:00");
            seekBar.setProgress(0);
            index=0;
            playing = song;
            fragment.setSong(playing.getImage(),playing.getName());
            player.start();
            fragment.changeIcon(true);
            playPause.setText("Pause");
        }
    }

    public void prev(){
        if (index>0){
            if (playing!=null)
                stopPlayer(playing);
            index--;
            Song song = songs.get(index);
            player = MediaPlayer.create(this, song.getId());
            seekBar.setMax(player.getDuration()/1000);
            if ((player.getDuration()/1000)%60<10)
                duration.setText((player.getDuration()/1000)/60+ ":0" + (player.getDuration()/1000)%60);
            else
                duration.setText((player.getDuration()/1000)/60+ ":" + (player.getDuration()/1000)%60);
            place.setText("0:00");
            seekBar.setProgress(0);
            playing = song;
            player.start();
            fragment.setSong(playing.getImage(),playing.getName());
            fragment.changeIcon(true);
            playPause.setText("Pause");
        }
        else {
            if (playing!=null)
                stopPlayer(playing);
            Song song = songs.get(songs.size()-1);
            player = MediaPlayer.create(this, song.getId());
            seekBar.setMax(player.getDuration()/1000);
            if ((player.getDuration()/1000)%60<10)
                duration.setText((player.getDuration()/1000)/60+ ":0" + (player.getDuration()/1000)%60);
            else
                duration.setText((player.getDuration()/1000)/60+ ":" + (player.getDuration()/1000)%60);
            place.setText("0:00");
            seekBar.setProgress(0);
            index=songs.size()-1;
            playing = song;
            player.start();
            fragment.setSong(playing.getImage(),playing.getName());
            fragment.changeIcon(true);
            playPause.setText("Pause");
        }
    }

    public void playPause(){
        if (playing==null) {
            player = MediaPlayer.create(this, songs.get(0).getId());
            seekBar.setMax(player.getDuration()/1000);
            if ((player.getDuration()/1000)%60<10)
                duration.setText((player.getDuration()/1000)/60+ ":0" + (player.getDuration()/1000)%60);
            else
                duration.setText((player.getDuration()/1000)/60+ ":" + (player.getDuration()/1000)%60);
            place.setText("0:00");
            seekBar.setProgress(0);
            index = 0;
            player.setOnCompletionListener(this);
            player.start();
            playPause.setText("Pause");
            fragment.changeIcon(true);
            playing = songs.get(0);
            fragment.setSong(playing.getImage(),playing.getName());
        }
        if (player!= null && player.isPlaying()){
            player.pause();
            playPause.setText("Play");
            fragment.changeIcon(false);
        }
        else {
            player.start();
            playPause.setText("Pause");
            fragment.changeIcon(true);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(player != null && fromUser){
            player.seekTo(progress * 1000);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}