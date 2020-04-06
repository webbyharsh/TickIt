package com.example.tickit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tickit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView titlepage,subtitlepage,endpage;
    Button btnAddNew,btnNotAna;
    DatabaseReference reference;
    RecyclerView ourdoes;
    ArrayList<MyDoes> list;
    DoesAdapter doesAdapter;
    String DeviceId= Build.MODEL.replaceAll("\\.", "");
    RelativeLayout cute_dino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titlepage=findViewById(R.id.titlepage);
        subtitlepage=findViewById(R.id.subtitlepage);
        endpage=findViewById(R.id.endpage);
        btnAddNew=findViewById(R.id.btnAddNew);
        btnNotAna=findViewById(R.id.btnNotAna);
        Typeface MLight=Typeface.createFromAsset(getAssets(),"fonts/ML.ttf");
        Typeface MMedium=Typeface.createFromAsset(getAssets(),"fonts/MM.ttf");

        titlepage.setTypeface(MMedium);
        subtitlepage.setTypeface(MLight);
        endpage.setTypeface(MLight);

        btnAddNew.setTypeface(MLight);

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(MainActivity.this,NewTaskAct.class);
                startActivity(a);
                finish();
            }
        });


        ourdoes=findViewById(R.id.ourdoes);
        ourdoes.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<MyDoes>();
        final Intent i=new Intent(MainActivity.this,NotifyMe.class);




        ///STARTING SERVICE////////
        startService(i);
        //STARTING SERVICE


        reference= FirebaseDatabase.getInstance().getReference(DeviceId).child("DoesApp");

        btnNotAna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent g=new Intent(MainActivity.this,NotificationAnalytics.class);
                startActivity(g);
            }
        });
        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    MyDoes p = dataSnapshot1.getValue(MyDoes.class);
                    list.add(p);
                }
                if(list.size()==0){
                    show_dino_view();
                }else{
                    hide_dino_view();
                }
                doesAdapter = new DoesAdapter(MainActivity.this, list);
                ourdoes.setAdapter(doesAdapter);
                doesAdapter.notifyDataSetChanged();
                ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(ourdoes);
            }
            ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    final int position=viewHolder.getAdapterPosition();
                    final MyDoes deletedTask;
                    MyDoes d=list.get(position);
                    deletedTask=list.get(position);
                    list.remove(position);
                    doesAdapter.notifyItemRemoved(position);
                    if(list.size()==0){
                        show_dino_view();
                    }else{
                        hide_dino_view();
                    }
                    final DatabaseReference ref=FirebaseDatabase.getInstance().getReference(DeviceId);
                    ref.child("DoesApp").child("Does"+d.getKeydoes()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this,"Task removed",Toast.LENGTH_SHORT).show();
                        }
                    });
                    Snackbar.make(ourdoes,"Task Removed",Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                   list.add(position,deletedTask);
                                   doesAdapter.notifyItemInserted(position);
                                    ref.child("DoesApp").child("Does"+deletedTask.getKeydoes()).setValue(deletedTask);
                                    if(list.size()==0){
                                        show_dino_view();
                                    }else{
                                        hide_dino_view();
                                    }
                                }
                            }).show();
                    //Toast.makeText(MainActivity.this,"Swiped",Toast.LENGTH_SHORT).show();
                }
            };

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();

            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {

                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference(DeviceId);
                ref.child("Firebase token").setValue(token);
                        // Log and toast
                        //Toast.makeText(MainActivity.this, "New Token generated", Toast.LENGTH_SHORT).show();
                    }
                });
        createNotificationChannel("myFirebaseNotification");
        createNotificationChannel("myFirebaseMuteNotification");
    }
    public void createNotificationChannel(String nameofNot){
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CustomNotificationChannel";
            String description = "This is it";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(nameofNot, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public void hide_dino_view(){
        cute_dino=(RelativeLayout)findViewById(R.id.dino_view);
        cute_dino.setVisibility(View.GONE);
    }
    public void show_dino_view(){
        cute_dino=(RelativeLayout)findViewById(R.id.dino_view);
        cute_dino.setVisibility(View.VISIBLE);
    }
}
