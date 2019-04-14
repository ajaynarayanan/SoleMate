package com.example.android.SoleMate;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class history_graph extends AppCompatActivity {

    private DatabaseReference RootRef;
    private JSONObject mainObject;
    private LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_graph);

        RootRef = FirebaseDatabase.getInstance().getReference().child("cg");
        RootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String val = dataSnapshot.getValue(String.class);
                try{
                    mainObject = new JSONObject(val);
                    JSONArray keys = mainObject.names();
                    ArrayList<DataPoint> d = new ArrayList<DataPoint>();
                    for (int i = 1; i <= keys.length()/2; ++i) {
                        d.add(new DataPoint(mainObject.getDouble("x" + i), mainObject.getDouble("y" + i)));
                    }
                    Collections.sort(d, new Comparator<DataPoint>() {
                        @Override
                        public int compare(DataPoint o1, DataPoint o2) {
                            if(o1.getX() > o2.getX())
                                    return 1;
                            else
                                return -1;
                        }
                    });
                    DataPoint[] dn = new DataPoint[d.size()];
                    d.toArray(dn);
                    series.resetData(dn);
                }catch (final Exception e) {
                    Log.e("Json parsing error: ", e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.addSeries(series);

    }
}
