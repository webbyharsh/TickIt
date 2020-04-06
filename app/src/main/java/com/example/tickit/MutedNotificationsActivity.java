package com.example.tickit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MutedNotificationsActivity extends AppCompatActivity {
    String DeviceId= Build.MODEL.replaceAll("\\.", "");
DatabaseReference muteref= FirebaseDatabase.getInstance().getReference(DeviceId);
ArrayList<SimpleNotificationMaker> list;
RecyclerView muted_rv;
MutedNotificationsAdapter myAdapter;
RelativeLayout warn_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muted_notifications);
        list=new ArrayList<SimpleNotificationMaker>();
        muted_rv=(RecyclerView)findViewById(R.id.muted_notifications_rv);
        muted_rv.setLayoutManager(new LinearLayoutManager(this));
        muteref.child("MUTED_NOTIFICATIONS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    SimpleNotificationMaker p = dataSnapshot1.getValue(SimpleNotificationMaker.class);
                    list.add(p);
                }
                if(list.size()==0){
                    show_dino_view();
                }else{
                    hide_dino_view();
                }
                myAdapter=new MutedNotificationsAdapter(MutedNotificationsActivity.this,list);
                muted_rv.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
                ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(muted_rv);
            }

            ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    final int position=viewHolder.getAdapterPosition();
                    SimpleNotificationMaker p=list.get(position);
                    list.remove(position);
                    myAdapter.notifyItemRemoved(position);
                    if(list.size()==0){
                        show_dino_view();
                    }else{
                        hide_dino_view();
                    }
                    String key=p.getFirebaseID();
                    DatabaseReference t=FirebaseDatabase.getInstance().getReference(DeviceId).child("MUTED_NOTIFICATIONS");
                    t.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MutedNotificationsActivity.this,"Notification removed!!",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            };



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void hide_dino_view(){
        warn_view=(RelativeLayout)findViewById(R.id.muted_warn_view);
        warn_view.setVisibility(View.GONE);
    }
    public void show_dino_view(){
        warn_view=(RelativeLayout)findViewById(R.id.muted_warn_view);
        warn_view.setVisibility(View.VISIBLE);
    }

}
