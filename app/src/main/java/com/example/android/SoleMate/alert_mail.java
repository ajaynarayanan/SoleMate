package com.example.android.SoleMate;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class alert_mail extends AppCompatActivity {

    private DatabaseReference RootRef;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PressureDetails> myDataset = new ArrayList<PressureDetails>();
    private JSONObject mainObject;
    private Double avg, peak;

    public static final long maxpeak = 150;
    public static final String CHANNNEL_ID = "SoleMate";
    public static final String CHANNEL_NAME = "SoleMate";
    public static final String CHANNEL_DESC = "This is a channel for SoleMate Notifications";

    public static final String KEY_INTENT_MORE = "keyintentmore";
    public static final String KEY_INTENT_HELP = "keyintenthelp";

    public static final int REQUEST_CODE_MORE = 100;
    public static final int REQUEST_CODE_HELP = 101;
    public static final int NOTIFICATION_ID = 200;
    public static final Double MPEAK = 1.1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_mail);

        RootRef = FirebaseDatabase.getInstance().getReference().child("sensor");

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);


        mAdapter.notifyDataSetChanged();
        RootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String val = dataSnapshot.getValue(String.class);
                try{
                    Double temp1 = 0.0, temp2 = 0.0, temp3 = 0.0;
                    mainObject = new JSONObject(val);
                    JSONArray keys = mainObject.names();
                    ArrayList<PressureDetails> d = new ArrayList<PressureDetails>();
                    for(int i = 1; i <= keys.length(); ++i) {

                        temp1 = Double.parseDouble(mainObject.getString("V" + i));
                        temp2 = temp1 + temp2;
                        if(temp1 > temp3)
                            temp3 = temp1;
                        d.add(new PressureDetails("V" + i, mainObject.getString("V" + i)));

                    }
                    avg = temp2/keys.length();
                    peak = temp3;
                    if(peak > maxpeak)//avg*MPEAK)
                        displayNotification();
                    myDataset.clear();
                    myDataset.addAll(d);
                    mAdapter.notifyDataSetChanged();
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNNEL_ID, CHANNEL_NAME, importance);
            mChannel.setDescription(CHANNEL_DESC);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }
    /*
        RootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    long val = dataSnapshot.getValue(Long.class);
                    String ke = dataSnapshot.getKey();
                    myDataset.add(new PressureDetails(ke, String.valueOf(val)));
                    mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                long val = dataSnapshot.getValue(Long.class);
                String ke = dataSnapshot.getKey();
                for(int i = 0; i < myDataset.size(); i++)
                {
                    if(ke.equals(myDataset.get(i).key))
                    {
                        myDataset.set(i, new PressureDetails(ke, String.valueOf(val)));
                        mAdapter.notifyItemChanged(i);
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }
        public void sendMail(View v)
        {
            Intent i = new Intent(Intent.ACTION_SENDTO);
            i.setData(Uri.parse("mailto:"));
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "MAXIMUM Pressure Peak ");
            i.putExtra(Intent.EXTRA_TEXT, "Dear Doctor Strange, " +
                    "\nYour patient Sherlock (Patient ID:IIITDM_0001) has recorded a maximum peak pressure of " +
                    "100N" + ", and has been notified too. This is for your kind information.");
            try {
                startActivity(Intent.createChooser(i, "Send Mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(alert_mail.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }

    public void displayNotification() {

        //Pending intent for a notification button named More
        PendingIntent morePendingIntent = PendingIntent.getBroadcast(
                alert_mail.this,
                REQUEST_CODE_MORE,
                new Intent(alert_mail.this, NotificationReceiver.class)
                        .putExtra(KEY_INTENT_MORE, REQUEST_CODE_MORE),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        //Pending intent for a notification button help
        PendingIntent helpPendingIntent = PendingIntent.getBroadcast(
                alert_mail.this,
                REQUEST_CODE_HELP,
                new Intent(alert_mail.this, NotificationReceiver.class)
                        .putExtra(KEY_INTENT_HELP, REQUEST_CODE_HELP),
                PendingIntent.FLAG_UPDATE_CURRENT
        );


        //Creating the notifiction builder object
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Maximum peak pressure reached")
                .setContentText("Do you want to send a mail to your Doctor?")
                .setAutoCancel(true)
                .setContentIntent(helpPendingIntent)
                .addAction(android.R.drawable.ic_menu_compass, "Yes", morePendingIntent)
                .addAction(android.R.drawable.ic_menu_directions, "No", helpPendingIntent);


        //finally displaying the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
        /*

            <ListView
        android:id="@+id/prlist"
        android:layout_weight="0.1"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
         />




        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification.Builder
                (getApplicationContext()).setContentTitle("Title").setContentText("body").
                setContentTitle("subject").setSmallIcon(R.drawable.ic_launcher_background).build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);
        */


// displayNotification();

//final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pval);
//arrayAdapter = new customAdapter(this, pval1);
//plist.setAdapter(arrayAdapter);
/*
        rootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                long value = dataSnapshot.getValue(Long.class);
                String key = dataSnapshot.getKey();
                pval.add(String.valueOf(value));
                kval.add(key);
                //PressureDetails temp = new PressureDetails(key, String.valueOf(value));
                //pval1.add(temp);

                Log.i("val =", String.valueOf(value));
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                long value = dataSnapshot.getValue(Long.class);
                String key = dataSnapshot.getKey();

                for (int i = 0; i < pval.size(); i++)
                {
                    if(kval.get(i) == key)
                    {
                        pval.set(i, String.valueOf(value));
                    }

                }

                //arrayAdapter.clear();
                //arrayAdapter.addAll(pval);
                arrayAdapter.notifyDataSetChanged();

                /*
                long value = dataSnapshot.getValue(Long.class);
                String key = dataSnapshot.getKey();

                int j = pval1.size()-1;

                Log.i("val =", String.valueOf(value));
                for (int i = 0; i < pval1.size(); i++)
                {

                    PressureDetails item = new PressureDetails(pval1.get(i).key, pval1.get(i).value);
                    if(item.key == key)
                    {
                        item.value =  String.valueOf(value);
                        j = i;
                        pval1.set(i, item);
                    }

                }
             //   pval1.remove(j);
             //   pval1.add(j, new PressureDetails(key, String.valueOf(value)));

                //arrayAdapter.updateval(key, String.valueOf(value));

                arrayAdapter.refreshEvents(pval1);
                //arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
*/



/*
public class alert_mail extends AppCompatActivity {

    private DatabaseReference V1Ref, V2Ref, V3Ref, V4Ref;
    TextView v1, v2, v3, v4, risk;
    private long s1, s2, s3, s4;
    Button b1;
    //private ListView plist;
    //private ArrayList<PressureDetails> pval1 = new ArrayList<>();
    //private customAdapter arrayAdapter;
    //private ArrayList<String> pval = new ArrayList<>();
    //private ArrayList<String> kval = new ArrayList<>();

    public static final long peak = 150;
    public static final String CHANNNEL_ID = "SoleMate";
    public static final String CHANNEL_NAME = "SoleMate";
    public static final String CHANNEL_DESC = "This is a channel for SoleMate Notifications";

    public static final String KEY_INTENT_MORE = "keyintentmore";
    public static final String KEY_INTENT_HELP = "keyintenthelp";

    public static final int REQUEST_CODE_MORE = 100;
    public static final int REQUEST_CODE_HELP = 101;
    public static final int NOTIFICATION_ID = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_mail);

        V1Ref = FirebaseDatabase.getInstance().getReference().child("V1");
        V2Ref = FirebaseDatabase.getInstance().getReference().child("V2");
        V3Ref = FirebaseDatabase.getInstance().getReference().child("V3");
        V4Ref = FirebaseDatabase.getInstance().getReference().child("V4");

        v1 = (TextView) findViewById(R.id.v1);
        v2 = (TextView) findViewById(R.id.v2);
        v3 = (TextView) findViewById(R.id.v3);
        v4 = (TextView) findViewById(R.id.v4);
        risk = (TextView) findViewById(R.id.trisk);
        b1 = (Button) findViewById(R.id.smail);

        V1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    s1 = dataSnapshot.getValue(Long.class);
                    String kv = "Velostat reading 1 : " + "\t" + String.valueOf(s1);
                    v1.setText(kv);
                if( s1 > peak || s2 > peak || s3 > peak || s4 > peak )
                {
                    risk.setText("HIGH RISK, RECOMMENDED ACTION : SEND MAIL TO DOCOTR ");
                    b1.setVisibility(View.VISIBLE);
                    displayNotification();
                }
                else
                {
                    risk.setText("RISK FREE");
                    b1.setVisibility(View.GONE);
                }
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        V2Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                s2 = dataSnapshot.getValue(Long.class);
                String kv = "Velostat reading 2 : " + "\t"+ String.valueOf(s2);
                v2.setText(kv);
                if( s1 > peak || s2 > peak || s3 > peak || s4 > peak )
                {
                    risk.setText("HIGH RISK, RECOMMENDED ACTION : SEND MAIL TO DOCOTR ");
                    b1.setVisibility(View.VISIBLE);
                    displayNotification();
                }
                else
                {
                    risk.setText("RISK FREE");
                    b1.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        V3Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                s3 = dataSnapshot.getValue(Long.class);
                String kv = "Velostat reading 3 : " + "\t" + String.valueOf(s3);
                v3.setText(kv);
                if( s1 > peak || s2 > peak || s3 > peak || s4 > peak )
                {
                    risk.setText("HIGH RISK, RECOMMENDED ACTION : SEND MAIL TO DOCOTR ");
                    b1.setVisibility(View.VISIBLE);
                    displayNotification();
                }
                else
                {
                    risk.setText("RISK FREE");
                    b1.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        V4Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                s4 = dataSnapshot.getValue(Long.class);
                String kv = "Velostat reading 4 : " + "\t" +  String.valueOf(s4);
                v4.setText(kv);
                if( s1 > peak || s2 > peak || s3 > peak || s4 > peak )
                {
                    risk.setText("HIGH RISK, RECOMMENDED ACTION : SEND MAIL TO DOCOTR ");
                    b1.setVisibility(View.VISIBLE);
                    displayNotification();
                }
                else
                {
                    risk.setText("RISK FREE");
                    b1.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNNEL_ID, CHANNEL_NAME, importance);
            mChannel.setDescription(CHANNEL_DESC);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }

     //   plist = (ListView) findViewById(R.id.prlist);
    }




        public void sendMail(View v)
        {
            Intent i = new Intent(Intent.ACTION_SENDTO);
            i.setData(Uri.parse("mailto:"));
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "MAXIMUM Pressure Peak ");
            i.putExtra(Intent.EXTRA_TEXT, "Dear Doctor Strange, " +
                    "\nYour patient Sherlock (Patient ID:IIITDM_0001) has recorded a maximum peak pressure of " +
                    "100N" + ", and has been notified too. This is for your kind information.");
            try {
                startActivity(Intent.createChooser(i, "Send Mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(alert_mail.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }

        public void displayNotification() {

            //Pending intent for a notification button named More
            PendingIntent morePendingIntent = PendingIntent.getBroadcast(
                    alert_mail.this,
                    REQUEST_CODE_MORE,
                    new Intent(alert_mail.this, NotificationReceiver.class)
                            .putExtra(KEY_INTENT_MORE, REQUEST_CODE_MORE),
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            //Pending intent for a notification button help
            PendingIntent helpPendingIntent = PendingIntent.getBroadcast(
                    alert_mail.this,
                    REQUEST_CODE_HELP,
                    new Intent(alert_mail.this, NotificationReceiver.class)
                            .putExtra(KEY_INTENT_HELP, REQUEST_CODE_HELP),
                    PendingIntent.FLAG_UPDATE_CURRENT
            );


            //Creating the notifiction builder object
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_email)
                    .setContentTitle("Maximum peak pressure reached")
                    .setContentText("Do you want to send a mail to your Doctor?")
                    .setAutoCancel(true)
                    .setContentIntent(helpPendingIntent)
                    .addAction(android.R.drawable.ic_menu_compass, "Yes", morePendingIntent)
                    .addAction(android.R.drawable.ic_menu_directions, "No", helpPendingIntent);


            //finally displaying the notification
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
}


 */


/*
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".alert_mail">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/v1"/>
    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/v2"/>
    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/v3"/>
    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/v4"/>
    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/trisk"/>


    <Button
        android:id="@+id/smail"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:onClick="sendMail"
        android:text="Send Mail"
        android:textAllCaps="false"
        android:visibility="gone"/>

</LinearLayout>

 */