package com.example.foodholic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class driverReport extends AppCompatActivity {

    ArrayList<classEmployee> eee;
    ArrayList<String> emp;
    ArrayList<classDelReport> delRepoet;
    ArrayAdapter<String> empAdapter;
    ArrayAdapter<String> reportAdapter;
    FirebaseFirestore db;
    Spinner spinner,year,month;
    String y="",m="";
    ListView listView;
    Button button;

    String empName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_report);

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        loadEmp();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){

                }
                else {
                    empName=emp.get(i);
                    loadReport(eee.get(i-1).email);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    y="";
                }
                else {
                    y=adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    m="";
                }
                else {
                    m=adapterView.getItemAtPosition(i).toString();
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
        year=findViewById(R.id.rep_spinnerYear);
        month=findViewById(R.id.rep_spinnerMonth);
        listView=findViewById(R.id.rep_list);
        button=findViewById(R.id.rep_button);
    }
    void loadEmp(){
        db.collection("Res_1_employee").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                eee=new ArrayList<>();
                emp=new ArrayList<>();
                emp.add("choose driver");
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d: list){
                    if(d.toObject(classEmployee.class).email.contains("del")){
                        eee.add(d.toObject(classEmployee.class));
                        emp.add(d.toObject(classEmployee.class).Fname+" "+d.toObject(classEmployee.class).Lname);
                    }
                }
                empAdapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,emp);
                spinner.setAdapter(empAdapter);
            }
        });
    }

    void loadReport(String path){
        db.collection("Res_1_Driver_Report").document(path).collection("1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                delRepoet=new ArrayList<>();
                ArrayList<String> aa =new ArrayList<>();
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d : list){
                    delRepoet.add(d.toObject(classDelReport.class));
                    aa.add( "التاريخ : "+d.toObject(classDelReport.class).date_time+"\n" +
                            "مجموع الطلبيات للمطعم: "+d.toObject(classDelReport.class).delivery_sum+" دينار"+"\n" +
                            "مجموع التوصيل   : "+d.toObject(classDelReport.class).order_sum+" دينار"+"\n"+
                            "عدد الطلبيات   : "+d.toObject(classDelReport.class).order_num+"\n");
                }
                ArrayAdapter arrayAdapter=new zReportAdapter(getApplicationContext(),R.layout.row_zreport,aa);
                listView.setAdapter(arrayAdapter);
            }
        });
    }
}
