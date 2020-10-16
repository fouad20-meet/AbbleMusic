package com.example.abblemusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView imageView;
    private TextView change;
    private EditText name, email, pass;
    private Button back, update;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        //user = (User)intent.getSerializableExtra("user");
        imageView = findViewById(R.id.profileimage);
        change =findViewById(R.id.change);
        name = findViewById(R.id.name);
        //Log.d("name",user.getName());
        //String n = user.getName();
        //name.setText(n);
        email = findViewById(R.id.email);
        //email.setText(user.getEmail());
        pass = findViewById(R.id.pass);
        //pass.setText(user.getPass());
        back = findViewById(R.id.back);
        update = findViewById(R.id.update);
        back.setOnClickListener(this);
        update.setOnClickListener(this);
//        if (user.getImage()!=null)
//            imageView.setImageBitmap(user.getImage());
    }

    public void changeProfile(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = (Bitmap)data.getExtras().get("data");
        //user.setImage(bitmap);
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
//            if (email.getText().toString().equals("") && pass.getText().toString().equals("") && name.getText().toString().equals(""))
//                Toast.makeText(this,"Error! Empty Fields...",Toast.LENGTH_LONG).show();
//            else{
//                if(email.getText().toString().contains("@") && email.getText().toString().contains(".com")) {
                    Intent intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
//                }
//                else
//                    Toast.makeText(this,"Not A Valid Email!",Toast.LENGTH_LONG).show();
//            }
        }
    }
}