package com.example.tickit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditTaskDesk extends AppCompatActivity {
EditText titlesDoes,descDoes,dateDoes;
Button btnSaveUpdate,btnDelete;
DatabaseReference reference;
    String DeviceId= Build.MODEL.replaceAll("\\.", "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task_desk);

        titlesDoes=findViewById(R.id.titledoes);
        descDoes=findViewById(R.id.descdoes);
        dateDoes=findViewById(R.id.datedoes);

        btnSaveUpdate=findViewById(R.id.btnSaveUpdate);
        btnDelete=findViewById(R.id.btnDelete);


        titlesDoes.setText(getIntent().getStringExtra("titledoes"));
        descDoes.setText(getIntent().getStringExtra("descdoes"));
        dateDoes.setText(getIntent().getStringExtra("datedoes"));

        final String KeyDoes=getIntent().getStringExtra("keydoes");


        btnSaveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference= FirebaseDatabase.getInstance().getReference(DeviceId).child("DoesApp").
                        child("Does"+KeyDoes);

                MyDoes does=new MyDoes();

                //                reference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        dataSnapshot.getRef().child("titledoes").setValue(titlesDoes.getText().toString());
//                        dataSnapshot.getRef().child("descdoes").setValue(descDoes.getText().toString());
//                        dataSnapshot.getRef().child("datedoes").setValue(dateDoes.getText().toString());
//                        dataSnapshot.getRef().child("keydoes").setValue(KeyDoes);
//
//                        Intent a=new Intent(EditTaskDesk.this,MainActivity.class);
//                        startActivity(a);
//                        finish();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
                    does.setTitledoes(titlesDoes.getText().toString());
                    does.setDatedoes(dateDoes.getText().toString());
                    does.setDescdoes(descDoes.getText().toString());
                    does.setKeydoes(KeyDoes);
                reference.setValue(does);
                Intent a =new Intent(EditTaskDesk.this,MainActivity.class);
                startActivity(a);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference=FirebaseDatabase.getInstance().getReference(DeviceId).child("DoesApp").child("Does"+KeyDoes);
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent a =new Intent(EditTaskDesk.this,MainActivity.class);
                            startActivity(a);
                            finish();
                        }
                        else{
                            Toast.makeText(EditTaskDesk.this,"ERROR",Toast.LENGTH_LONG);
                        }
                    }
                });
            }
        });
    }
}
