package com.example.abblemusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        songs = new ArrayList<Song>();
        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference("Users/"+mAuth.getUid()+"/PlayList");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data:snapshot.getChildren()){
                    Song song=data.getValue(Song.class);
                    song.setKey(data.getKey());
                    songs.add(song);
                }
                Collections.sort(songs);
                arrayAdapter = new SongArrayAdapter(PlaylistActivity.this,R.layout.custom_row, songs);
                listView.setAdapter(arrayAdapter);
                if(songs.isEmpty()){
                    Intent notification = new Intent(PlaylistActivity.this, MyReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(PlaylistActivity.this, 1, notification, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) PlaylistActivity.this.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.RTC, (System.currentTimeMillis()+(1000)), 1000 * 60 * 60 * 24, pendingIntent);
                    Intent intentS = new Intent(PlaylistActivity.this,IntentService.class);
                    startService(intentS);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference df = FirebaseDatabase.getInstance().getReference("Users/"+mAuth.getUid()+"/PlayList/"+song.getKey());
        df.removeValue();
        songs.clear();
        arrayAdapter.notifyDataSetChanged();
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
        if (index<(songs.size()-2)){
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
            fragment.changeIcon(false);
            playPause.setText("Play");
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
            fragment.changeIcon(false);
            playPause.setText("Play");
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

    @Override
    public void onBackPressed() {
        stopPlayer(playing);
        super.onBackPressed();
    }
}