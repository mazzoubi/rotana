package com.example.foodholic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Job_Apps extends AppCompatActivity {

    FirebaseFirestore db;

    ListView list;
    ArrayAdapter<String> adapter;

    ArrayList<String> info;
    ArrayList<String> time;

    ArrayAdapter<String> adapterSpin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job__apps);

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        list=findViewById(R.id.list);

        info = new ArrayList<String>();
        time = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, R.layout.items_row3, R.id.item, info);
        list.setAdapter(adapter);

        final Spinner sp = findViewById(R.id.type);
        adapterSpin = new ArrayAdapter<String>(Job_Apps.this, R.layout.items_row3, R.id.item, time);
        sp.setAdapter(adapterSpin);

        getTime();

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                downloadData(sp.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadAllData();
            }
        });

    }

    public void downloadData(String path){

        info.clear();

        db.collection("Res_1_Job_Applications")
                .whereEqualTo("date", path)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        info.add("الأسم : " + document.get("name").toString() + "\n"
                                + "الهاتف : " + document.get("mobile").toString() + "\n"
                                + "البريد : " + document.get("email").toString() + "\n"
                                + "الوظيفة : " + document.get("job").toString() + "\n"
                                + "ملاحظات : " + document.get("desc").toString() + "\n" );


                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    public void downloadAllData(){

        info.clear();

        db.collection("Res_1_Job_Applications")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        info.add("الأسم : " + document.get("name").toString() + "\n"
                                + "الهاتف : " + document.get("mobile").toString() + "\n"
                                + "البريد : " + document.get("email").toString() + "\n"
                                + "الوظيفة : " + document.get("job").toString() + "\n"
                                + "ملاحظات : " + document.get("desc").toString() + "\n" ); }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    public void getTime(){

        time.clear();

        db.collection("Res_1_Job_Applications")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful())
                            for (QueryDocumentSnapshot document : task.getResult())
                                addData(document.get("date").toString());

                    } });

    }

    public void addData(String name){

        if(!time.contains(name))
            time.add(name);
        adapterSpin.notifyDataSetChanged();

    }

}
