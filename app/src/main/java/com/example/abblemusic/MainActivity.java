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

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private User user;
    private Button playlist;
    private GridView gridView;//display
    private ArrayList<Album> albums;//DATA
    private ArrayAdapter<Album> arrayAdapter;//Adapter
    private MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        playlist = findViewById(R.id.playlist);
        playlist.setOnClickListener(this);
        gridView = findViewById(R.id.simpleGridView);
        gridView.setOnItemClickListener(this);
        albums = new ArrayList<Album>();
        Album a1 = new Album("The Score","Fugees",true,"@drawable/fugees");
        a1.addSong("Red Intro",R.raw.fugeesintro);
        a1.addSong("How Many Mics",R.raw.fugeesmics);
        a1.addSong("Ready or Not",R.raw.fugeesready);
        a1.addSong("Zealots",R.raw.fugeeszealots);
        a1.addSong("The Beast",R.raw.fugeesbeast);
        a1.addSong("Fu-gee-la",R.raw.fugeesfugeela);
        a1.addSong("Family Buisness",R.raw.fugeesfamily);
        a1.addSong("Killing Me Softly With His Song",R.raw.fugees);
        a1.addSong("The Score",R.raw.fugeesscore);
        a1.addSong("The Mask",R.raw.fugeesmask);
        a1.addSong("Cowboys",R.raw.fugeescowboys);
        a1.addSong("No Woman, No Cry",R.raw.fugeeswoman);
        a1.addSong("Manifest/Outro",R.raw.fugeesmanifest);
        Album a2 = new Album("The Dark Side of the Moon","Pink Floyd",false,"@drawable/pinkfloyd");
        a2.addSong("Speak to Me",R.raw.pinkspeak);
        a2.addSong("Breathe (In the Air)",R.raw.pinkbreathe);
        a2.addSong("On the Run",R.raw.pinkrun);
        a2.addSong("Time",R.raw.pinktime);
        a2.addSong("The Great Gig In the Sky",R.raw.pinkgiginsky);
        a2.addSong("Money",R.raw.pinkmoney);
        a2.addSong("Us and Them",R.raw.pinkusthem);
        a2.addSong("Any Colour You Like",R.raw.pinkcolour);
        a2.addSong("Brain Damage",R.raw.pinkdamage);
        a2.addSong("Eclipse",R.raw.pinkeclipse);
        Album a3 = new Album("Greatest Hits","Janis Joplin",false,"@drawable/janis");
        a3.addSong("Piece of My Heart",R.raw.janisheart);
        a3.addSong("Summertime",R.raw.janissummertime);
        a3.addSong("Try (Just a Little Bit Harder)",R.raw.janistry);
        a3.addSong("Cry Baby",R.raw.janiscrybaby);
        a3.addSong("Me and Bobby McGee",R.raw.janisbobby);
        a3.addSong("Down On Me",R.raw.janisdown);
        a3.addSong("Get It While You Can",R.raw.janiswhileyoucan);
        a3.addSong("Bye, Bye, Baby",R.raw.janusbye);
        
        a3.addSong("Maybe",R.raw.janismaybe);
        Album a4 = new Album("MTV Unplugged","Nirvana",false,"@drawable/nirvana");
        a4.addSong("The Man Who Sold the World",R.raw.nirvanasoldworld);
        Album a5 = new Album("Abbey Road","Beatles",true,"@drawable/beatles");
        a5.addSong("Something",R.raw.thebeatlessomething);
        Album a6 = new Album("Back To Black","Amy Winehouse",false,"@drawable/amy");
        a6.addSong("Rehab",R.raw.amyrehab);
        a6.addSong("You Know I'm No Good",R.raw.amynogood);
        a6.addSong("Me & Mr. Jones",R.raw.amyjones);
        a6.addSong("Just Friends",R.raw.amyjustfriends);
        a6.addSong("Back to Black",R.raw.amybacktoblack);
        a6.addSong("Love is a Losing Game", R.raw.amylosinggame);
        a6.addSong("Tears Dry On Their Own",R.raw.amytearsdry);
        a6.addSong("Wake Up Alone",R.raw.amyalone);
        a6.addSong("Some Unholy War",R.raw.amyunholywar);
        a6.addSong("He Can Only Hold Her", R.raw.amyholdher);
        a6.addSong("Addicted",R.raw.amyaddicted);
        Album a7 = new Album("A.M.","Arctic Monkeys",false,"@drawable/arcticmonkeys");
        a7.addSong("Do I Wanna Know?",R.raw.arcticwannaknow);
        a7.addSong("R U Mine?",R.raw.arcticrumine);
        a7.addSong("One for the Road",R.raw.arcticforroad);
        a7.addSong("Arabella",R.raw.arcticarabella);
        a7.addSong("I Want It All",R.raw.arcticitall);
        a7.addSong("No. 1 Party Anthem",R.raw.arcticno1);
        a7.addSong("Mad Sounds",R.raw.arcticmadsounds);
        a7.addSong("Fireside",R.raw.arcticfireside);
        a7.addSong("Why'd You Only Call Me When You're High?", R.raw.arctichigh);
        a7.addSong("Snap Out of It",R.raw.arcticsnap);
        a7.addSong("Knee Socks",R.raw.arctickneesocks);
        a7.addSong("I Wanna Be Yours",R.raw.arcticwanna);
        Album a8 = new Album("The Miseducation of Lauryn Hill","Lauryn Hill",false,"@drawable/lauryn");
        a8.addSong("Can't Take My Eyes Off of You",R.raw.lauryneyesoffyou);
        albums.add(a1);
        albums.add(a2);
        albums.add(a3);
        albums.add(a4);
        albums.add(a5);
        albums.add(a6);
        albums.add(a7);
        albums.add(a8);
        Collections.sort(albums);
        arrayAdapter = new AlbumArrayAdapter(this,R.layout.custom_grid, albums);
        gridView.setAdapter(arrayAdapter);
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