package com.example.tickit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationAnalytics extends AppCompatActivity {
    RelativeLayout layout;
    Button btnNA;
    FloatingActionButton muteNotScreen;
    RecyclerView ournots;
    ArrayList<MyNots> list;
    NotificationAnalAdapter notsAdapter;
    String DeviceId= Build.MODEL.replaceAll("\\.", "");
    DatabaseReference notsref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_analytics);
        layout=(RelativeLayout)findViewById(R.id.MainDisguise);
        btnNA=(Button)findViewById(R.id.allownot);
        TextView t=(TextView)findViewById(R.id.explain_title);
        TextView p=(TextView)findViewById(R.id.explain_desc);
        ournots=(RecyclerView)findViewById(R.id.notification_rv);
        ournots.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<MyNots>();

        if(isNotificationServiceEnabled()){
            //Toast.makeText(this,"Notification Listener Service enabled",Toast.LENGTH_LONG).show();
            layout.setVisibility(View.GONE);
            ournots.setVisibility(View.VISIBLE);
        }else{
            //Toast.makeText(this,"Notification Listener Service not enabled",Toast.LENGTH_LONG).show();
            //startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            layout.setVisibility(View.VISIBLE);
            ournots.setVisibility(View.GONE);

        }

        //ADD VALUES IN LIST

//        MyNots pi=new MyNots("Whatsapp","this is test","12","fe23df");
//        list.add(pi);
//        MyNots pi=new MyNots("Whatsapp","com.whatsapp","12","fe23df");
//        list.add(pi);
        notsref=FirebaseDatabase.getInstance().getReference(DeviceId).child("NotificationAnalytics");
        notsref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                notsAdapter=new NotificationAnalAdapter(NotificationAnalytics.this,list);
                ournots.setAdapter(notsAdapter);
                notsAdapter.notifyDataSetChanged();
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    MyNots p=d.getValue(MyNots.class);
                    list.add(p);
                    notsAdapter=new NotificationAnalAdapter(NotificationAnalytics.this,list);
                    ournots.setAdapter(notsAdapter);
                    notsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //ADD VALUES IN LIST





        btnNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            }
        });

        muteNotScreen=(FloatingActionButton)findViewById(R.id.notification_snooze);
        muteNotScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(NotificationAnalytics.this,MuteNotificationsScreen.class);
                startActivity(a);
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        layout=(RelativeLayout)findViewById(R.id.MainDisguise);
        btnNA=(Button)findViewById(R.id.allownot);
        ournots=(RecyclerView)findViewById(R.id.notification_rv);
        if(isNotificationServiceEnabled()){
            //Toast.makeText(this,"Notification Listener Service enabled",Toast.LENGTH_LONG).show();
            layout.setVisibility(View.GONE);
            ournots.setVisibility(View.VISIBLE);

        }else{
            //Toast.makeText(this,"Notification Listener Service not enabled",Toast.LENGTH_LONG).show();
            //startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            layout.setVisibility(View.VISIBLE);
            ournots.setVisibility(View.GONE);
        }

    }


    private boolean isNotificationServiceEnabled(){
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
