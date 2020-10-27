package com.example.abblemusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class PlaylistActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener, MediaPlayer.OnCompletionListener {
    private ListView listView;//display
    private ArrayList<Song> songs;//DATA
    private ArrayAdapter<Song> arrayAdapter;//Adapter
    private MediaPlayer player;
    private Song playing;
    private Button playPause,shuffle;
    private ImageButton prev,next;
    private int index;
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
        shuffle.setOnClickListener(this);
        prev = findViewById(R.id.prev);
        prev.setOnClickListener(this);
        next = findViewById(R.id.next);
        next.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Song song = (Song)listView.getItemAtPosition(position);
        if (playing!=null){
            stopPlayer(playing);
        }
        playing = song;
        player = MediaPlayer.create(this,song.getId());
        index = position;
        player.setOnCompletionListener(this);
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
            playing = null;
            player = null;
            playPause.setText("Play");
        }
    }

    @Override
    public void onClick(View v) {
        if (v==playPause){
            if (playing==null) {
                player = MediaPlayer.create(this, songs.get(0).getId());
                index = 0;
                player.setOnCompletionListener(this);
                player.start();
                playPause.setText("Pause");
                playing = songs.get(0);
            }
            else if (player!=null && player.isPlaying()) {
                player.pause();
                playPause.setText("Play");
            }
            else {
                player.start();
                playPause.setText("Pause");
            }
        }
        else if (v==shuffle){
            int length = songs.size();
            int order = (int) ((int)length*(Math.random()));
            if (playing!=null)
                stopPlayer(playing);
            player = MediaPlayer.create(this,songs.get(order).getId());
            index = order;
            player.setOnCompletionListener(this);
            player.start();
            playing = songs.get(order);
            playPause.setText("Pause");
        }
        else if (v==prev){
            if (index>0){
                stopPlayer(playing);
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
                Song song = songs.get(index+1);
                player = MediaPlayer.create(this,song.getId());
                index++;
                playing = song;
                player.start();
            }
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
    public void onCompletion(MediaPlayer mp) {
        if (index<songs.size()-1) {
            Song song = songs.get(index + 1);
            player = MediaPlayer.create(this, song.getId());
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
    public static void setListViewHeightBasedOnChildren
            (ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) view.setLayoutParams(new
                    ViewGroup.LayoutParams(desiredWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter.getCount()-1));

        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}