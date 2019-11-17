package com.example.foodholic;

import android.graphics.Color;
import android.printservice.PrintService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Zreport extends AppCompatActivity {

    Spinner spinnerYear,spinnerMonth;
    Button monthreport,yearReport ,print;
    ListView listView;

    String year="";
    String month ="";
    FirebaseFirestore db;

   ArrayList<classSales>cSale;

   ArrayList<String> smonth;
   ArrayList<String> syear;

   ArrayAdapter<String> adapter;

   int m=0;
   int y=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zreport);
        init();

        monthreport.setBackgroundColor(Color.GRAY);
        yearReport.setBackgroundColor(Color.GRAY);
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    month="";
                }
                else{
                    month=adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    year="";
                }
                else{
                    year=adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        yearReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (year.isEmpty()){
                    Toast.makeText(Zreport.this, "Please choose year", Toast.LENGTH_SHORT).show();
                }
                else {
                    y=1;
                    m=0;
                    double bb=0;
                    syear=new ArrayList<>();
                    ArrayList<classSales> list= new ArrayList<>();
                    yearReport.setBackgroundColor(Color.GREEN);
                    monthreport.setBackgroundColor(Color.GRAY);

                    for (int i=0 ; i < cSale.size();i++ ){
                        if (cSale.get(i).date.contains(year)){
                            bb+=cSale.get(i).sale;
                           list.add(cSale.get(i));
                        }
                    }
                    syear.add("\n\n\n\n\t\t\t\tmonthly reprt " +
                            "\nRes : "+HomeAct.resName+"\n" +
                            "pill dat : "+new Date()+"\n" +
                            "\t-----------------------\n" +
                            "\n\n");
                    double z=0;
                    int y=0;
                    int i=1;
                    for(;i<=12;i++){
                        for(int a=0 ; a<list.size();a++){
                            if (i+"".length()==1){
                                if (list.get(a).date.contains("0"+i+"-"+year)){
                                   z+=list.get(a).sale;
                                   list.remove(a);
                                   y=1;
                                }
                            }else{
                                if (list.get(a).date.contains(i+"-"+year)){
                                    z+=list.get(a).sale;
                                    list.remove(a);
                                    y=1;
                                }
                            }
                        }
                        if (y==1){
                            syear.add("Sales at : "+i+"-"+year+"="+z);
                        }
                        y=0;
                        z=0;
                    }
                    syear.add("\n\t-----------------------\n");
                    syear.add("total at : "+year+"="+bb+"\n" +
                            "\n\t-----------------------\n");
                    syear.add("\t**** Aldahkeel 0798056383 ****\t");

                    adapter=new zReportAdapter(getApplicationContext(),R.layout.row_zreport,syear);
                    listView.setAdapter(adapter);
                }
            }
        });

        monthreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (year.isEmpty()){
                    Toast.makeText(Zreport.this, "Please choose year", Toast.LENGTH_SHORT).show();
                }
                else if (month.isEmpty()){
                    Toast.makeText(Zreport.this, "Please choose month", Toast.LENGTH_SHORT).show();
                }
                else {
                    m=1;
                    y=0;
                    double bb=0;
                    smonth=new ArrayList<>();
                    smonth.add("\n\n\n\n\t\t\t\tmonthly reprt " +
                            "\nRes : "+HomeAct.resName+"\n" +
                            "pill dat : "+new Date()+"\n" +
                            "\t-----------------------\n" +
                            "\n\n");
                    yearReport.setBackgroundColor(Color.GRAY);
                    monthreport.setBackgroundColor(Color.GREEN);
                    for (int i=0 ; i < cSale.size();i++ ){
                        if (cSale.get(i).date.contains(month+"-"+year)){
                            bb+=cSale.get(i).sale;
                        }
                    }
                    smonth.add("total at : "+month+"-"+year+"="+bb+"\n" +
                            "\n-----------------------\n");
                    smonth.add("\t**** Aldahkeel 0798056383 ****\t");

                    adapter=new zReportAdapter(getApplicationContext(),R.layout.row_zreport,smonth);
                    listView.setAdapter(adapter);
                }
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (m==0&&y==0){
                    Toast.makeText(Zreport.this, "please choose the type of report", Toast.LENGTH_SHORT).show();
                }
                else if (y==1&&m==0){
                    String s="";
                    for (int i=0; i< syear.size();i++){
                        s+=syear.get(i)+"\n";
                    }
                    printYear(s);
                }
                else if (y==0&& m==1){
                    String s="";
                    for (int i=0; i< smonth.size();i++){
                        s+=smonth.get(i)+"\n";
                    }
                    printMonth(s);
                }
            }
        });

    }

    void init(){
        db=FirebaseFirestore.getInstance();
        spinnerMonth=findViewById(R.id.zspinnerMonth);
        spinnerYear=findViewById(R.id.zspinnerYear);
        yearReport = findViewById(R.id.yearReport);
        monthreport = findViewById(R.id.monthReport);
        print=findViewById(R.id.button_printReport);
        listView=findViewById(R.id.reportList);

        db.collection("Res_1_sales").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                cSale=new ArrayList<>() ;
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    cSale.add(d.toObject(classSales.class));
                }
            }
        });
    }

    void printMonth(String s){


    }
    void printYear(String s){
        Toast.makeText(this, "year report", Toast.LENGTH_SHORT).show();
    }

}
