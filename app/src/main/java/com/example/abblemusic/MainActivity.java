package com.example.abblemusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Button playlist;
    private GridView gridView;//display
    private ArrayList<Album> albums;//DATA
    private ArrayAdapter<Album> arrayAdapter;//Adapter
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        playlist = findViewById(R.id.playlist);
        playlist.setOnClickListener(this);
        gridView = findViewById(R.id.simpleGridView);
        gridView.setOnItemClickListener(this);
        database=FirebaseDatabase.getInstance().getReference("Albums");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                albums = new ArrayList<Album>();
                for (DataSnapshot data:snapshot.getChildren()){
                    Album album=data.getValue(Album.class);
                    albums.add(album);
                }
                Collections.sort(albums);
                arrayAdapter = new AlbumArrayAdapter(MainActivity.this,R.layout.custom_grid, albums);
                gridView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mAuth = FirebaseAuth.getInstance();
        //user = (User)intent.getSerializableExtra("user");
        //Log.d("user",user.toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.myAccount){
            Intent intent = new Intent(this, AccountActivity.class);
            //intent.putExtra("user",user);
            startActivity(intent);
        }
        if (id == R.id.signout){
            Intent intent = new Intent(this,SignInActivity.class);
            mAuth.signOut();
            startActivity(intent);
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v==playlist){
            Intent intent = new Intent(this,PlaylistActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,AlbumActivity.class);
        intent.putExtra("album",(Album)albums.get(position));
        startActivity(intent);
    }
}