package com.example.tickit;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


public class NotifyMe extends NotificationListenerService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.tickit.action.FOO";
    private static final String ACTION_BAZ = "com.example.tickit.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.tickit.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.tickit.extra.PARAM2";


    String MobileName = Build.BRAND;
    String DeviceId= Build.MODEL.replaceAll("\\.", "");
    NotificationMaker myNotificaton;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("NotifyMeService");
    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference(DeviceId);
    DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference(DeviceId);


    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, NotifyMe.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }


    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, NotifyMe.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }


    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }


    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String text, title;
        text = "";
        title = "";
        super.onNotificationPosted(sbn);
        // cancelAllNotifications();
        long postTime = sbn.getPostTime();
        String packageName = sbn.getPackageName();
        CharSequence tickerText = sbn.getNotification().tickerText;

        Bundle extras = sbn.getNotification().extras;
        if (extras != null) {
            title = extras.getString("android.title");
            if(extras.getCharSequence("android.text")!=null){
            text = extras.getCharSequence("android.text").toString();}
        }
        myNotificaton = new NotificationMaker(sbn.getPackageName(), sbn.getNotification().tickerText, Integer.toString(sbn.getId()), text, title);

        if (sbn.isClearable()) {
            String id = myRef.child(MobileName.replaceAll("\\.+$", "")).push().getKey();
            myRef.child(DeviceId).child(id).setValue(myNotificaton);

            updateNotificationCount(sbn.getPackageName().replaceAll("\\.", ""), sbn);
            SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
            Boolean t = sharedPreferences.getBoolean("NOTIFICATIONS_MUTED", false);
            if (t) {
                cancelAllNotifications();
                String k = ref3.child("MUTED_NOTIFICATIONS").push().getKey();
                if (tickerText != null) {
                    SimpleNotificationMaker s = new SimpleNotificationMaker(tickerText.toString(), packageName, postTime, "", text, title, k);
                    ref3.child("MUTED_NOTIFICATIONS").child(k).setValue(s);
                } else {
                    SimpleNotificationMaker s = new SimpleNotificationMaker("", packageName, postTime, "", text, title, k);
                    ref3.child("MUTED_NOTIFICATIONS").child(k).setValue(s);
                }

            }
            //Toast.makeText(NotifyMe.this,"NOTIFICATIONS ARE CANCELED",Toast.LENGTH_LONG).show();

    }


    }




    //IMPORTANT FUNCTION TO COUNT NOTIFICATIONS

        public void updateNotificationCount(final String packageName,final StatusBarNotification n){
            ref2.child("NotificationAnalytics").child(packageName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    MyNots d=dataSnapshot.getValue(MyNots.class);
                    if(d==null){
                        String title=n.getNotification().extras.getString("android.title");
                        MyNots t=new MyNots(title,n.getPackageName(),"1",n.getKey());
                        ref2.child("NotificationAnalytics").child(packageName).setValue(t);
                    }else {
                        String Count = d.getCount();
                        int i = Integer.parseInt(Count);
                        i = i + 1;
                        //Toast.makeText(NotifyMe.this, i + " and " + Count, Toast.LENGTH_SHORT).show();
                        Count = Integer.toString(i);
                        MyNots newV = new MyNots(d.title, d.packageName, Count, d.key);
                        ref2.child("NotificationAnalytics").child(packageName).setValue(newV);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(NotifyMe.this,"Data dont exist make one",Toast.LENGTH_LONG).show();
                }
            });



    }


    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        //Toast.makeText(NotifyMe.this,"Notification remvoed",Toast.LENGTH_LONG).show();
    }
//

}
