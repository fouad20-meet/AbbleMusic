package com.example.abblemusic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etCode;
    private Button confirm;
    private Dialog d;
    private String code,email;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        etCode = findViewById(R.id.code);
        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        code = intent.getStringExtra("code");
        tv = findViewById(R.id.textView4);
        tv.setText(tv.getText()+email);
        confirm = findViewById(R.id.confirm);
    }

    public void sendAgain(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set a title for alert dialog
        builder.setTitle("Send Code Again?");

        // Set the alert dialog yes button click listener
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                code = "";
                for (int i = 0; i<6; i++){
                    int rnd = (int)(Math.random()*10);
                    code += rnd;
                }
                String subject = "Please Confirm Your Email - Abble Music";
                String message = code;
                //Creating SendMail object
                SendMail sm = new SendMail(ConfirmActivity.this, email, subject, message);
                sm.execute();
            }
        });

        // Set the alert dialog no button click listener
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == confirm){
            if(etCode.getText().toString().equals(code)){
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(this,"Code Incorrect",Toast.LENGTH_LONG).show();
                
                

            }
        }
    }
}