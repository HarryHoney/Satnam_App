package com.example.harpreet.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.harpreet.myapplication.Account.login;
import com.example.harpreet.myapplication.Account.setup;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth mauth;
    //for adapter
    FirebaseFirestore firebaseFirestore =FirebaseFirestore.getInstance();
    public FirebaseAdapter adapter;

    public static int count=0;
    int[] drawablearray=new int[]{R.drawable.lee,R.drawable.lin,R.drawable.tau,R.drawable.mom};
    Timer _t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setTitle("Satnam Academy");

        mauth=FirebaseAuth.getInstance();
        if(mauth.getCurrentUser()==null) {
            startActivity(new Intent(MainActivity.this,login.class));
        }


        //dynamically changing the background
        final CoordinatorLayout lnMain = findViewById(R.id.back_pic);
        _t = new Timer();
        _t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() // run on ui thread
                {
                    public void run() {
                        if (count < drawablearray.length) {
                            Drawable d=ContextCompat.getDrawable(MainActivity.this, drawablearray[count]);
                            lnMain.setBackground(d);
                            count = (count + 1) % drawablearray.length;
                        }
                    }
                });
            }
        }, 5000, 5000);


        //Code for floating action button
        FloatingActionButton floatingActionButton=findViewById(R.id.floating_button);
        if(mauth.getUid().equals("gS9G6duEBuaN9FRw2lURB2iZ1mr2")){
            floatingActionButton.show();
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create an instant for new match

                startActivity(new Intent(MainActivity.this,Match.class));

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
             case(R.id.settings):
                Toast.makeText(this, "Setting selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,setup.class));
                //do something
                return true;
            case (R.id.about_us):
                //do something
                Toast.makeText(MainActivity.this,"About us Selected",Toast.LENGTH_LONG).show();

                return true;
            case (R.id.sign_out):
            {
                mauth.signOut();
                startActivity(new Intent(this,login.class));
            }
            default:
                return false;
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user==null)
        {
            //go to sign in page
            sendtoLogin();
        }
        else
        {
            FirebaseFirestore firebaseFirestore;
            firebaseFirestore=FirebaseFirestore.getInstance();//to access database
            firebaseFirestore.collection("Users").document("Score").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        if (!task.getResult().exists()) {
                            // startActivity(new Intent(MainActivity.this,setup.class));
                        }
                        setupRecyclerView();
                        adapter.startListening();
                    }
                }

            });
        }
    }

    private void setupRecyclerView() {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        CollectionReference reference = firebaseFirestore.collection("/Score");
        Query query = reference
                .limit(50). orderBy("order",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Data> options = new FirestoreRecyclerOptions.Builder<Data>().
                setQuery(query,Data.class)
                .build();
        adapter = new FirebaseAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
    private void sendtoLogin() {
        Intent intent=new Intent(MainActivity.this,login.class);
        startActivity(intent);
        finish();
    }

}
