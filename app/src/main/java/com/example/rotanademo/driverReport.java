package com.example.rotanademo;

import android.content.SharedPreferences;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
    int lang=0;
    String empName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        SharedPreferences shared2;
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        if(shared2.getString("language", "").equals("arabic")) {
            lang=1;
        }
    }
    void loadEmp(){
        db.collection("Res_1_employee").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                eee=new ArrayList<>();
                emp=new ArrayList<>();
                emp.add("أختر السائق");
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
                    if (lang == 1) {
                        aa.add( "التاريخ : "+d.toObject(classDelReport.class).date_time+"\n" +
                                "مجموع الطلبيات للمطعم: "+d.toObject(classDelReport.class).delivery_sum+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار")+"\n" +
                                "مجموع التوصيل   : "+d.toObject(classDelReport.class).order_sum+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار")+"\n"+
                                "عدد الطلبيات   : "+d.toObject(classDelReport.class).order_num+"\n");
                    }
                    else {
                        aa.add( "date : "+d.toObject(classDelReport.class).date_time+"\n" +
                                "sum of restaurant order: "+d.toObject(classDelReport.class).delivery_sum+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD")+"\n" +
                                "sum of delivery   : "+d.toObject(classDelReport.class).order_sum+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD")+"\n"+
                                "sum of order   : "+d.toObject(classDelReport.class).order_num+"\n");
                    }

                }
                ArrayAdapter arrayAdapter=new zReportAdapter(getApplicationContext(),R.layout.row_zreport,aa);
                listView.setAdapter(arrayAdapter);
            }
        });
    }
}
