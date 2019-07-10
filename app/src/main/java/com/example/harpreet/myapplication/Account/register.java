package com.example.harpreet.myapplication.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.harpreet.myapplication.MainActivity;
import com.example.harpreet.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class register extends AppCompatActivity {

    private Button reg_signup;
    private Button reg_already_acc;
    private EditText reg_email;
    private  EditText reg_password;
    private EditText reg_name;
    private EditText reg_confirm_password;
    private FirebaseAuth mauth;
    private ProgressBar pp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mauth=FirebaseAuth.getInstance();
        reg_email=findViewById(R.id.signupemail);
        reg_password=findViewById(R.id.signuppassword);
        reg_confirm_password=findViewById(R.id.confirmpassword);
        reg_signup=findViewById(R.id.createbtn);
        reg_already_acc=findViewById(R.id.haveaccbtn);
        reg_name = findViewById(R.id.user_name);
        pp=findViewById(R.id.progressBar2);

        reg_already_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        reg_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=reg_email.getText().toString();
                String Password=reg_password.getText().toString();
                String confirm_Password=reg_confirm_password.getText().toString();
                String Name = reg_name.getText().toString();

                if(!TextUtils.isEmpty(mail)&&!TextUtils.isEmpty(Password)&&!TextUtils.isEmpty(confirm_Password)&&!TextUtils.isEmpty(Name))
                {

                    if(Password.equals(confirm_Password))
                    {

                        pp.setVisibility(View.VISIBLE);
                        mauth.createUserWithEmailAndPassword(mail,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    //Now the User is Successfully registered So adding it to Score Board
                                    FirebaseUser user=mauth.getCurrentUser();
                                    String id=user.getUid();
                                    //Add user to DashBoard with Name id matches=0 and points=0
                                    //Make a call to Nodejs code

                                    startActivity(new Intent(register.this,MainActivity.class));
                                    finish();
                                }
                                else
                                {
                                    String error=task.getException().getMessage();
                                    Toast.makeText(register.this,"Error:"+error,Toast.LENGTH_LONG).show();
                                }
                                pp.setVisibility(View.INVISIBLE);
                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(register.this,"Password don't match",Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    Toast.makeText(register.this,"Please Fill the Above Details",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mauth.getCurrentUser();
        if(user!=null)
        {
            startActivity(new Intent(register.this,MainActivity.class));
            finish();
        }
    }
}
