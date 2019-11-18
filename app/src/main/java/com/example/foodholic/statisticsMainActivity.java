package com.example.foodholic;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class statisticsMainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    ArrayList<classEmployee> emp = new ArrayList<>();
    ArrayList<String> empnames=new ArrayList<>();
    ArrayList<classCloseOpenCash> closeOpen;
    ArrayList<classCloseOpenCash> allCloseOpen;
    ArrayList<classCloseOpenCash> closeOpenAtDate;
    Button print;
    ListView listView;
    Spinner spinner;
    Button button;
    TextView textView;
    RadioButton year,month;
    DatePickerDialog.OnDateSetListener date_;
    String myDatefrom=" / / ";

    String empName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_main);
        init();

        final Calendar myCalendar = Calendar.getInstance();
        date_ = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                String []y=sdf.format(myCalendar.getTime()).split("/");
                myDatefrom=y[1]+"-"+y[0]+"-"+y[2];
                textView.setText(myDatefrom);

             /*   String []y=myDate.split("/");
                textView1.setText(y[1]+"-"+y[0]+"-"+y[2]);*/

            } };
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(statisticsMainActivity.this, date_, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                //   Toast.makeText(getApplicationContext(),"chose the dd/mm/yyyy in month you want show statistics",Toast.LENGTH_LONG).show();

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){

                }
                else {
                    empName=adapterView.getItemAtPosition(i).toString();
                    String ss="";
                    ArrayList<String> ls=new ArrayList<>();
                    allCloseOpen=new ArrayList<>();
                    for (int y=0 ; y<closeOpen.size();y++){
                        if (emp.get(i-1).email.equals(closeOpen.get(y).empEmail)){
                            allCloseOpen.add(closeOpen.get(y));
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeOpenAtDate=new ArrayList<>();
                ArrayList<String> arrayList=new ArrayList<>();
                double floor=0;
                double sale =0 ;
                double total=0;
                String we="";
                if (myDatefrom.equals(" / / ")){
                    Toast.makeText(statisticsMainActivity.this, "please enter the date", Toast.LENGTH_SHORT).show();
                }
                else if (year.isChecked()){
                    String []s =myDatefrom.split("-");
                    String ss=s[2];
                    for (int i=0 ; i< allCloseOpen.size();i++){
                        if (allCloseOpen.get(i).dateOpen.contains(ss)){
                            floor+=allCloseOpen.get(i).floor;
                            sale+=allCloseOpen.get(i).total-allCloseOpen.get(i).floor;
                            total+=allCloseOpen.get(i).total;
                            closeOpenAtDate.add(allCloseOpen.get(i));
                            arrayList.add("emp name: "+empName+"" +
                                    "\nemp email: "+allCloseOpen.get(i).empEmail+"" +
                                    "\n\n date: "+allCloseOpen.get(i).dateOpen+"\n" +
                                    "time open: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                                    "time close: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                                    "floor   = "+allCloseOpen.get(i).floor+"\n" +
                                    "sale    = "+(allCloseOpen.get(i).total-allCloseOpen.get(i).floor)+"\n" +
                                    "total   = "+allCloseOpen.get(i).total);
                        }
                    }
                    arrayList.add("\n-------------------------------\n" +
                            "floor   = "+floor+"\n" +
                            "sale    = "+sale+"\n" +
                            "total   = "+total);
                    ArrayAdapter<String> adapter=new zReportAdapter(getApplicationContext(),R.layout.row_zreport,arrayList);
                    listView.setAdapter(adapter);
                }
                else if (month.isChecked()){
                    String []s =myDatefrom.split("-");
                    String ss=s[1]+"-"+s[2];
                    for (int i=0 ; i< allCloseOpen.size();i++){
                        if (allCloseOpen.get(i).dateOpen.contains(ss)){
                            floor+=allCloseOpen.get(i).floor;
                            sale+=allCloseOpen.get(i).total-allCloseOpen.get(i).floor;
                            total+=allCloseOpen.get(i).total;
                            closeOpenAtDate.add(allCloseOpen.get(i));
                            arrayList.add("emp name: "+empName+"" +
                                    "\nemp email: "+allCloseOpen.get(i).empEmail+"" +
                                    "\n\n date: "+allCloseOpen.get(i).dateOpen+"\n" +
                                    "time open: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                                    "time close: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                                    "floor   = "+allCloseOpen.get(i).floor+"\n" +
                                    "sale    = "+(allCloseOpen.get(i).total-allCloseOpen.get(i).floor)+"\n" +
                                    "total   = "+allCloseOpen.get(i).total);
                        }
                    }
                    arrayList.add("\n-------------------------------\n" +
                            "floor   = "+floor+"\n" +
                            "sale    = "+sale+"\n" +
                            "total   = "+total);
                    ArrayAdapter<String> adapter=new zReportAdapter(getApplicationContext(),R.layout.row_zreport,arrayList);
                    listView.setAdapter(adapter);
                }
            }
        });

    }

    void init(){
        db = FirebaseFirestore.getInstance();
        print = findViewById(R.id.a2print);
        listView=findViewById(R.id.a2list);
        spinner =findViewById(R.id.a2spinner);
        textView=findViewById(R.id.a2date);
        button=findViewById(R.id.a2search);
        year=findViewById(R.id.a2yearRadio);
        month=findViewById(R.id.a2monthRadio);
        year.setChecked(true);
        loadEmp();
        loadCloseOpen();
    }




    public void loadEmp(){

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
    void loadCloseOpen(){
        closeOpen=new ArrayList<>();
        db.collection("closeOpenCash").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list){
                    closeOpen.add(d.toObject(classCloseOpenCash.class));
                }
            }
        });
    }

    void print(){

    }
}