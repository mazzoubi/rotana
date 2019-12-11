package com.example.foodholic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class driverReport extends AppCompatActivity {

    ArrayList<String> emp;
    ArrayList<classDelReport> delRepoet;
    ArrayAdapter<String> empAdapter;
    FirebaseFirestore db;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_report);
        init();
        loadEmp();
        empAdapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(empAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){

                }
                else {
                    loadReport(emp.get(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    void init(){
        db=FirebaseFirestore.getInstance();
        spinner=findViewById(R.id.rep_spinner);
    }
    void loadEmp(){
        db.collection("Res_1_employee").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                emp=new ArrayList<>();
                emp.add("choose driver");
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d: list){
                    if(d.toObject(classEmployee.class).email.contains("del")){
                        emp.add(d.toObject(classEmployee.class).Fname+" "+d.toObject(classEmployee.class).Lname);
                    }
                }
            }
        });
    }
    void loadReport(String path){
        db.collection("Res_1_Driver_Report").document(path).collection("1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                delRepoet=new ArrayList<>();
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d : list){
                    delRepoet.add(d.toObject(classDelReport.class));
                }
            }
        });
    }
}
