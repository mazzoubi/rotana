package com.example.foodholic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class statisticsMainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    ArrayList<classEmployee> emp = new ArrayList<>();
    ArrayList<String> empnames=new ArrayList<>();
    Button print;
    ListView listView;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_main);
        init();

    }

    void init(){
        print = findViewById(R.id.a2print);
        listView=findViewById(R.id.a2list);
        spinner =findViewById(R.id.a2spinner);
        loadEmp();
    }




    public void loadEmp(){
        db = FirebaseFirestore.getInstance();
        db.collection("Res_1_employee").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                empnames.add("select employee");
                for (DocumentSnapshot d : list) {
                    classEmployee a = d.toObject(classEmployee.class);
                    if (a.cashWork || a.delevery) {
                        emp.add(a);
                        empnames.add(a.Fname+" "+a.Lname+" "+a.empType);
                    }
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,empnames);
                spinner.setAdapter(adapter);


            }
        });


    }
}