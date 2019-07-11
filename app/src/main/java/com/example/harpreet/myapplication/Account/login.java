package com.example.harpreet.myapplication.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.harpreet.myapplication.MainActivity;
import com.example.harpreet.myapplication.Match;
import com.example.harpreet.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jacksonandroidnetworking.JacksonParserFactory;

public class login extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private ProgressBar progressBar;
    private Button login;
    private Button signup;
    FirebaseAuth mauth;
    private Toolbar login_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.cloud);
        mauth=FirebaseAuth.getInstance();
        email=findViewById(R.id.signupemail);
        password=findViewById(R.id.signuppassword);
        progressBar=findViewById(R.id.progressBar);
        login=findViewById(R.id.createbtn);
        signup=findViewById(R.id.signupbutton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFuction();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this,register.class));
            }
        });
    }

    private void loginFuction() {

        String Email=email.getText().toString();
        String Password=password.getText().toString();

        if(!TextUtils.isEmpty(Email)&&!TextUtils.isEmpty(Password))
        {
            progressBar.setVisibility(View.VISIBLE);
            mauth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        startActivity(new Intent(login.this,MainActivity.class));
                    }
                    else
                    {
                        String error=task.getException().getMessage();
                        Toast.makeText(login.this,error,Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
        else
        {
            Toast.makeText(login.this,"Please Enter Both Email & Password",Toast.LENGTH_SHORT).show();
        }

        progressBar.setVisibility(View.INVISIBLE);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            //go to sign in page
            startActivity(new Intent(login.this,MainActivity.class));
            finish();
        }
    }
}
