package com.example.harpreet.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.textclassifier.TextClassification;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.harpreet.myapplication.Interface.iFirebaseLoadDone;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class Match extends AppCompatActivity implements com.example.harpreet.myapplication.Interface.iFirebaseLoadDone {

    SearchableSpinner searchableSpinner2,searchableSpinner1;

    DatabaseReference databaseReference;
    iFirebaseLoadDone iFirebaseLoadDone;
    List<Data> List;
    String firstId="";
    String SecondId="";
    EditText points1;
    EditText points2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.setParserFactory(new JacksonParserFactory());

        //Spinner Property
        searchableSpinner1 = findViewById(R.id.spinner1);
        searchableSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Data item = List.get(position);
                firstId = item.getUser_id();
                Toast.makeText(Match.this,"Selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchableSpinner2 = findViewById(R.id.spinner2);
        searchableSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Data item = List.get(position);
                SecondId = item.getUser_id();
                    Toast.makeText(Match.this,"Selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Spinner Area ended

        points1 = findViewById(R.id.editText);
        points2 = findViewById(R.id.editText2);

        Button submit = findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String P1 = points1.getText().toString();
                String P2 = points2.getText().toString();
                if(P1.isEmpty()||P2.isEmpty()||firstId.isEmpty()||SecondId.isEmpty()){
                    Toast.makeText(Match.this, "Please Enter the Details", Toast.LENGTH_SHORT).show();
                }else{
                    if(firstId.equals(SecondId)){
                        Toast.makeText(Match.this, "You Cannot Select Same Player", Toast.LENGTH_SHORT).show();
                    }else{
                        //Make Http Call to the function
                        AndroidNetworking.get("https://us-central1-badmintion-55f94.cloudfunctions.net/DataUpdate?point1="+P1+"&id1="+firstId+"&point2="+P2+"&id2="+SecondId)
                                .setTag("Match")
                                .setPriority(Priority.LOW)
                                .build()
                                .getAsString(new StringRequestListener() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(Match.this, "Data Added", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        Toast.makeText(Match.this, anError.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                        //Toast.makeText(Match.this, P1+" - "+P2, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("Score");
        iFirebaseLoadDone = this;
    //getData
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Data> list = new ArrayList<>();
                for(DataSnapshot DataSnap:dataSnapshot.getChildren()){
                    list.add(DataSnap.getValue(Data.class));
                }
                iFirebaseLoadDone.onFirebaseLoadSuccess(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFirebaseLoadDone.onFirebaseLoadFailed((databaseError.getMessage()));
            }
        });
    }

    @Override
    public void onFirebaseLoadSuccess(List<Data> list) {
        List = list;
        List<String> name_list = new ArrayList<>();
        for(Data data:list){
            name_list.add(data.getName());
            //Creating Adapter and setting it for spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,name_list);
            searchableSpinner1.setAdapter(adapter);
            searchableSpinner2.setAdapter(adapter);
        }
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
