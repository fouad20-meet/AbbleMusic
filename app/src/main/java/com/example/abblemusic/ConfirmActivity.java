package com.example.abblemusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfirmActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etCode;
    private Button confirm;
    private Dialog d;
    private String code,email,pass,name;
    private TextView tv;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        etCode = findViewById(R.id.code);
        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        Intent intent = getIntent();
        firebaseDatabase = FirebaseDatabase.getInstance();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        pass = intent.getStringExtra("pass");
        code = intent.getStringExtra("code");
        mAuth = FirebaseAuth.getInstance();
        tv = findViewById(R.id.textView4);
        tv.setText(tv.getText()+email);
        confirm = findViewById(R.id.confirm);
    }

    public void createUser(String email,String pass){
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(ConfirmActivity.this, "Authentication worked.",
                                    Toast.LENGTH_SHORT).show();
                           // signIn(email,pass);
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            User user = new User(name,email,pass);
                            signIn(email,pass);
                            firebaseDatabase.getReference("Users").child(uid).setValue(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(ConfirmActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
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
            if(etCode.getText().toString().equals("555")){
                createUser(email,pass);
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(this,"Code Incorrect",Toast.LENGTH_LONG).show();

            }
        }
    }
    public void signIn(String email,String pass){
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(ConfirmActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}