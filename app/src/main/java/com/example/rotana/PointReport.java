package com.example.rotana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.WindowManager;
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

public class PointReport extends AppCompatActivity {

    FirebaseFirestore db;

    ListView list;
    ArrayAdapter<String> adapter;

    ArrayList<String> info;
    ArrayList<String> time;

    ArrayAdapter<String> adapterSpin;
    double tot = 0;
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_point_report);

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        list=findViewById(R.id.list);
        t = findViewById(R.id.tot);

        info = new ArrayList<String>();
        time = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, R.layout.items_row3, R.id.item, info);
        list.setAdapter(adapter);

        final Spinner sp = findViewById(R.id.type);
        adapterSpin = new ArrayAdapter<String>(PointReport.this, R.layout.items_row3, R.id.item, time);
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
        tot = 0;

        db.collection("Res_1_point_sales")
                .whereEqualTo("date", path)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        info.add("الصنف : " + document.get("item").toString() + "\n"
                                + "المادة : " + document.get("subItem").toString() + "\n"
                                + "النقاط : " + (Double.parseDouble(document.get("sale").toString()) * 10) + "\n"
                                + "الوقت : " + document.get("time").toString() + "\n");

                        tot+=Double.parseDouble(document.get("sale").toString())*10;

                    }
                    adapter.notifyDataSetChanged();
                    t.setText("مجموع النقاط : "+tot+" نقطة");
                }
            }
        });
    }
    public void downloadAllData(){

        info.clear();
        tot = 0;

        db.collection("Res_1_point_sales")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        info.add("الصنف : " + document.get("item").toString() + "\n"
                                + "المادة : " + document.get("subItem").toString() + "\n"
                                + "النقاط : " + (Double.parseDouble(document.get("sale").toString()) * 10) + "\n"
                                + "الوقت : " + document.get("time").toString() + "\n");
                        tot+=Double.parseDouble(document.get("sale").toString())*10; }
                    adapter.notifyDataSetChanged();
                    t.setText("مجموع النقاط : "+tot+" نقطة");
                }
            }
        });
    }
    public void getTime(){

        time.clear();

        db.collection("Res_1_point_sales")
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
