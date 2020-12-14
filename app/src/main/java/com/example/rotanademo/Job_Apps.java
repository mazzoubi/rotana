package com.example.rotanademo;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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
    ArrayList<String> id;
    ArrayList<String> time;

    ArrayAdapter<String> adapterSpin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_job__apps);

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        list=findViewById(R.id.list);

        info = new ArrayList<String>();
        time = new ArrayList<String>();
        id = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, R.layout.items_row3, R.id.item, info);
        list.setAdapter(adapter);

        final Spinner sp = findViewById(R.id.type);
        adapterSpin = new ArrayAdapter<String>(Job_Apps.this, R.layout.items_row3, R.id.item, time);
        sp.setAdapter(adapterSpin);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                new AlertDialog.Builder(Job_Apps.this)
                        .setTitle("تأكيد").setMessage("هل ترغب بحذف هذا الطلب ؟")
                        .setPositiveButton("حذف", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int k) {
                                FirebaseFirestore fb = FirebaseFirestore.getInstance();
                                fb.collection("Res_1_Job_Applications").document(""+id.get(i))
                                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        info.remove(i);
                                        id.remove(i);
                                        adapter.notifyDataSetChanged();

                                        Toast.makeText(Job_Apps.this, "تم الحذف بنجاح", Toast.LENGTH_LONG).show();
                                    }
                                });
                                dialogInterface.dismiss(); }
                        }).setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();

            }
        });

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
        id.clear();

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
                        id.add(document.getId());

                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    public void downloadAllData(){

        info.clear();
        id.clear();

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
                                + "ملاحظات : " + document.get("desc").toString() + "\n" );
                        id.add(document.getId()); }
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
