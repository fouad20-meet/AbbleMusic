package com.example.abblemusic;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText siEmail,siPass,suEmail,suPass,name;
    private Button signin,signup;
    private SharedPreferences sp;
    private Dialog d,confirm;
    private TextView createAccount;
    private CheckBox save;
    private CircleImageView imageView;
    private User user;
    private String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        siEmail = findViewById(R.id.etEmail);
        siPass = findViewById(R.id.etPass);
        signin = findViewById(R.id.signin);
        createAccount = findViewById(R.id.tvSignin);
        save = findViewById(R.id.save);
        imageView = findViewById(R.id.profile_image);
        sp = getApplicationContext().getSharedPreferences("sp",0);
        String email = sp.getString("email",null);
        String pass = sp.getString("pass",null);
        if (email!=null && pass!=null){
            siEmail.setText(email);
            siPass.setText(pass);
        }
        signin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==signin){
            if (siEmail.getText().toString().equals("") && siPass.getText().toString().equals(""))
                Toast.makeText(this,"Error! Empty Fields...",Toast.LENGTH_LONG).show();
            else{
                if(siEmail.getText().toString().contains("@") && siEmail.getText().toString().contains(".com")) {
                    if (save.isChecked()) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("email", siEmail.getText().toString());
                        editor.putString("pass", siPass.getText().toString());
                        editor.apply();
                    }
                    Intent intent = new Intent(this, MainActivity.class);
                    //intent.putExtra("user",user);
                    startActivity(intent);
                    finish();
                }
                else
                    Toast.makeText(this,"Not A Valid Email!",Toast.LENGTH_LONG).show();
            }
        }
        else if (v==signup){
            if (suEmail.getText().toString().equals("") && suPass.getText().toString().equals("") && name.getText().toString().equals(""))
                Toast.makeText(this,"Error! Empty Fields...",Toast.LENGTH_LONG).show();
            else{
                if(suEmail.getText().toString().contains("@") && suEmail.getText().toString().contains(".com")) {
                    siEmail.setText(suEmail.getText());
                    siPass.setText(suPass.getText());
                    code = "";
                    for (int i = 0; i<6; i++){
                        int rnd = (int)(Math.random()*10);
                        code += rnd;
                    }
                    d.dismiss();
                    String email = suEmail.getText().toString().trim();
                    String subject = "Please Confirm Your Email - Abble Music";
                    String message = code;
                    //Creating SendMail object
                    SendMail sm = new SendMail(this, email, subject, message);
                    //Executing sendmail to send email
                    sm.execute();
                    //Intent
                    //user = new User(name.getText().toString(),suEmail.getText().toString(),suPass.getText().toString());
                    //Log.d("user",user.toString());
                }
                else
                    Toast.makeText(this,"Not A Valid Email!",Toast.LENGTH_LONG).show();
            }
        }
    }
    public void createAccountD(View v){
        d = new Dialog(this);
        d.setCancelable(true);
        d.setContentView(R.layout.custom_layout);
        suEmail = d.findViewById(R.id.suEmail);
        suPass = d.findViewById(R.id.suPass);
        name = d.findViewById(R.id.name);
        signup = d.findViewById(R.id.signup);
        signup.setOnClickListener(this);
        d.show();
    }
}