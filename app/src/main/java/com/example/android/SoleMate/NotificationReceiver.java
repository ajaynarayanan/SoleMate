package com.example.android.SoleMate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {

    private Context context;
/*
    public NotificationReceiver(Context context) {
        this.context = context;
    }*/
    @Override
    public void onReceive(Context context, Intent intent) {

        //if help button is clicked
        if (intent.getIntExtra(alert_mail.KEY_INTENT_HELP, -1) == alert_mail.REQUEST_CODE_HELP) {
            Toast.makeText(context, "Mail not sent", Toast.LENGTH_LONG).show();
        }

        //if more button is clicked
        if (intent.getIntExtra(alert_mail.KEY_INTENT_MORE, -1) == alert_mail.REQUEST_CODE_MORE) {
            Toast.makeText(context, "Mail sent", Toast.LENGTH_LONG).show();
            Intent i = new Intent(Intent.ACTION_SENDTO);
            i.setData(Uri.parse("mailto:"));
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"edm16b016@iiitdm.ac.in"});
            i.putExtra(Intent.EXTRA_SUBJECT, "MAXIMUM Pressure Peak ");
            i.putExtra(Intent.EXTRA_TEXT, "Dear Doctor Strange, " +
                    "\nYour patient Sherlock (Patient ID:IIITDM_0001) has recorded a maximum peak pressure of " +
                    "100N" + ", and has been notified too. This is for your kind information.");
            //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(Intent.createChooser(i, "Send Mail...").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

}