package com.example.tickit;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class NewTaskAct extends AppCompatActivity {
    TextView titlepage, addtitle, adddesc, adddate;
    EditText titledoes, descdoes, datedoes;
    Button btnSaveTask, btnCancel;
    DatabaseReference reference;
    Integer doesNum = new Random().nextInt();
    String keydoes = Integer.toString(doesNum);
    String DeviceId= Build.MODEL.replaceAll("\\.", "");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        titlepage = findViewById(R.id.titlepage);

        addtitle = findViewById(R.id.addtitle);
        adddesc = findViewById(R.id.adddesc);
        adddate = findViewById(R.id.adddate);

        titledoes = findViewById(R.id.titledoes);
        descdoes = findViewById(R.id.descdoes);
        datedoes = findViewById(R.id.datedoes);

        btnSaveTask = findViewById(R.id.btnSaveUpdate);
        btnCancel = findViewById(R.id.btnDelete);

        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewTaskAct.this,"Adding task!!", Toast.LENGTH_LONG).show();
                reference= FirebaseDatabase.getInstance().getReference(DeviceId).child("DoesApp").
                        child("Does"+doesNum);
//                reference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        dataSnapshot.getRef().child("titledoes").setValue(titledoes.getText().toString());
//                        dataSnapshot.getRef().child("descdoes").setValue(descdoes.getText().toString());
//                        dataSnapshot.getRef().child("datedoes").setValue(datedoes.getText().toString());
//                        dataSnapshot.getRef().child("keydoes").setValue(keydoes);
//
//                        Intent a = new Intent(NewTaskAct.this,MainActivity.class);
//                        startActivity(a);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
                MyDoes does=new MyDoes();
                does.setTitledoes(titledoes.getText().toString());
                does.setDatedoes(datedoes.getText().toString());
                does.setDescdoes(descdoes.getText().toString());
                does.setKeydoes(keydoes);
                reference.setValue(does);
                Intent a =new Intent(NewTaskAct.this,MainActivity.class);
                startActivity(a);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a= new Intent(NewTaskAct.this,MainActivity.class);
                startActivity(a);
            }
        });
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // customize font
        titlepage.setTypeface(MMedium);

        addtitle.setTypeface(MLight);
        titledoes.setTypeface(MMedium);

        adddesc.setTypeface(MLight);
        descdoes.setTypeface(MMedium);

        adddate.setTypeface(MLight);
        datedoes.setTypeface(MMedium);

        btnSaveTask.setTypeface(MMedium);
        btnCancel.setTypeface(MLight);


    }
}
