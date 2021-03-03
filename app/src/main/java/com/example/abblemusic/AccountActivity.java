package com.example.abblemusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView imageView;
    private TextView change,email;
    private EditText name, pass;
    private Button back, update;
    private Bitmap bitmap;
    private FirebaseAuth mAuth;
    private DatabaseReference database,profile;
    private String DEmail,DName,DPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference("Users/"+mAuth.getUid());
        profile = FirebaseDatabase.getInstance().getReference("Users/"+mAuth.getUid()+"/profile");
        setContentView(R.layout.activity_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        imageView = findViewById(R.id.profileimage);
        change =findViewById(R.id.change);
        name = findViewById(R.id.etName);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DEmail = snapshot.child("email").getValue(String.class);
                email.setText("email: "+DEmail);
                DName = snapshot.child("name").getValue(String.class);
                name.setText(DName);
                DPass = snapshot.child("pass").getValue(String.class);
                pass.setText(DPass);
                if (snapshot.child("profile").getValue(String.class)!=null)
                    imageView.setImageBitmap(StringToBitMap(snapshot.child("profile").getValue(String.class)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        back = findViewById(R.id.back);
        update = findViewById(R.id.update);
        back.setOnClickListener(this);
        update.setOnClickListener(this);
    }

    public void changeProfile(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = (Bitmap)data.getExtras().get("data");
        String imagef = BitMapToString(bitmap);
        profile.setValue(imagef);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.myAccount) {
            Toast.makeText(this,"Already in My Account...",Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.signout){
            Intent intent = new Intent(this,SignInActivity.class);
            mAuth.signOut();
            startActivity(intent);
            finish();
        }
        else if (id == R.id.backmenu){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (back==v){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (update==v){
            database.child("name").setValue(name.getText().toString());
            database.child("pass").setValue(pass.getText().toString());
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }
}