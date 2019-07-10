package com.example.harpreet.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.harpreet.myapplication.Interface.iFirebaseLoadDone;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.w3c.dom.Text;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

public class Match extends AppCompatActivity implements com.example.harpreet.myapplication.Interface.iFirebaseLoadDone {

    SearchableSpinner searchableSpinner2,searchableSpinner1;

    DatabaseReference databaseReference;
    iFirebaseLoadDone iFirebaseLoadDone;
    List<Data> List;
    boolean First1=true;
    boolean First2=true;
    String firstId="";
    String SecondId="";
    EditText points1;
    EditText points2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        //Spinner Property
        searchableSpinner1 = findViewById(R.id.spinner1);
        searchableSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Data item = List.get(position);
                firstId = item.getUser_id();
                //Toast.makeText(Match.this, "here", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchableSpinner2 = findViewById(R.id.spinner2);
        searchableSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!First1){
                Data item = List.get(position);
                SecondId = item.getUser_id();}else{
                    First1=false;
                }
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
                        Toast.makeText(Match.this, P1+" - "+P2, Toast.LENGTH_SHORT).show();
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
