package com.example.foodholic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Drive extends AppCompatActivity {

    FirebaseFirestore db;

    ListView list;
    ArrayAdapter<String> adapter;

    ArrayList<String> id;
    ArrayList<String> loc;
    ArrayList<String> info;

    String name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drive);

        db = FirebaseFirestore.getInstance();

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);

        list=findViewById(R.id.list);

        id = new ArrayList<String>();
        loc = new ArrayList<String>();
        info = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, info);
        list.setAdapter(adapter);

        downloadDriverData();

    }

    public void downloadDriverData(){

        String em = getIntent().getStringExtra("email");
        db.collection("Res_1_employee")
                .document(em)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){

                            String temp = "";
                            temp += task.getResult().get("Fname").toString();
                            temp += " "+task.getResult().get("Lname").toString();
                            AddDataName(temp);
                        }
                    }
                });

    }

    public void AddDataName(String temp){
        name = temp;
        downloadData();
    }

    public void downloadData(){

        info.clear();

        db.collection("Res_1_Delivery")
                .document(name).collection("1")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot document : task.getResult())
                    if (task.isSuccessful()) {
                        if (HomeAct.lang==1){
                            info.add("الأسم : "+document.get("name").toString()+"\n"
                                    +"الهاتف : "+document.get("mobile").toString()+"\n"
                                    +"العنوان : "+document.get("address").toString()+"\n"
                                    +"المجموع : "+document.get("sum").toString()+"\n" );
                        }
                        else {
                            info.add("Name : "+document.get("name").toString()+"\n"
                                    +"Phone : "+document.get("mobile").toString()+"\n"
                                    +"Address : "+document.get("address").toString()+"\n"
                                    +"Total : "+document.get("sum").toString()+"\n" );

                        }
                        adapter.notifyDataSetChanged();
                        if(info.isEmpty()){
                            if (HomeAct.lang==1){
                                Toast.makeText(Drive.this, "لايوجد طلبات دلفري", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Drive.this, "No delivery request", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

            }
        });
    }



}
