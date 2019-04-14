package com.example.android.SoleMate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    Button hgraph, pgraph, mail, hhgrp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);


        hgraph = (Button) findViewById(R.id.btnhgraph);
        pgraph = (Button) findViewById(R.id.btnpgraph);
        mail = (Button) findViewById(R.id.btnmail);
        hhgrp = (Button) findViewById(R.id.btnhist1);

        hgraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, history_graph.class);
                startActivity(intent);
            }
        });

        pgraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, PBG.class);
                startActivity(intent);
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(MainActivity.this, alert_mail.class);
                startActivity(intent);

            }
        });
        hhgrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, phgraph.class);
                startActivity(intent);
            }
        });
    }
}
/*
public class MainActivity extends AppCompatActivity {

    EditText editText, editText1;
    Button submit, fetch;
    DatabaseReference rootRef, demoRef;
    TextView demoValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        editText = (EditText) findViewById(R.id.etValue);
        editText1 = (EditText) findViewById(R.id.etValue1);
        demoValue = (TextView) findViewById(R.id.tvValue);
        submit = (Button) findViewById(R.id.btnSubmit);
        fetch = (Button) findViewById(R.id.btnFetch);

        //database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();

        //database reference pointing to New node
        demoRef = rootRef.child("New");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = editText.getText().toString();
                String key = editText1.getText().toString();
                //push creates a unique id in database
                demoRef.child(key).setValue(value);
            }
        });

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> dataSnapshotIterable = dataSnapshot.getChildren();
                        for (DataSnapshot p : dataSnapshotIterable) {
                            String value = p.getValue(String.class);
                            String key = p.getKey();
                            String kv = key + "\n" + value;
                            demoValue.setText(kv);
                            Log.i("myinfo", kv);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }
}

<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:padding="16dp">

    <EditText
        android:id="@+id/etValue1"
        android:layout_marginBottom="24dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <EditText
        android:id="@+id/etValue"
        android:layout_marginTop="24dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <Button
        android:id="@+id/btnSubmit"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@+id/etValue"
        android:layout_marginTop="24dp"
        android:text="Submit" />

    <Button
        android:id="@+id/btnFetch"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@+id/btnSubmit"
        android:text="Fetch" />

    <TextView
        android:id="@+id/tvValue"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@+id/btnFetch"
        android:gravity="center_horizontal"
        android:textSize="16dp" />

</RelativeLayout>
*/