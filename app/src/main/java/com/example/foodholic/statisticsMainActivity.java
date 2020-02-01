package com.example.foodholic;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
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
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
  RadioButton year,month,day;
  DatePickerDialog.OnDateSetListener date_;
  String myDatefrom=" / / ";
  SharedPreferences shared2;
  String toPrint="";
  String empName="";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_statistics_main);

    Toolbar bar = findViewById(R.id.tool);
    setSupportActionBar(bar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    init();

    print.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(toPrint.equals("")){
        }
        else{
          print(toPrint);
        }
      }
    });
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
        myDatefrom=y[1]+"-"+y[0]+"-"+(Integer.parseInt(y[2])-2000);
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
          if(HomeAct.lang == 1){
          try{
            closeOpenAtDate=new ArrayList<>();
            ArrayList<String> arrayList=new ArrayList<>();
            double floor=0;
            double sale =0 ;
            double total=0;
            String we="";
            if (myDatefrom.equals(" / / ")){
              Toast.makeText(statisticsMainActivity.this, "الرجاء ادخال التاريخ", Toast.LENGTH_SHORT).show();
            }
            else if (year.isChecked()){
              toPrint="";
              String []s =myDatefrom.split("-");
              String ss=s[2];
              arrayList.add("اسم الموظف: "+empName+"" +
                      "\nالبريد الالكتروني: "+allCloseOpen.get(0).empEmail+"\n" +
                      "تاريخ هذه العمليه: "+new Date()+"" +
                      "\n\n\n");
              toPrint+="\nاسم الموظف: "+empName+"" +
                      "\nالبريد الالكتروني: "+allCloseOpen.get(0).empEmail+"\n" +
                      "تاريخ هذه العمليه: "+new Date()+"" +
                      "\n\n\n";
              for (int i=0 ; i< allCloseOpen.size();i++){
                if (allCloseOpen.get(i).dateOpen.contains(ss)){
                  floor+=allCloseOpen.get(i).floor;
                  sale+=allCloseOpen.get(i).total-allCloseOpen.get(i).floor;
                  total+=allCloseOpen.get(i).total;
                  closeOpenAtDate.add(allCloseOpen.get(i));

                  if ((allCloseOpen.get(i).total)<(allCloseOpen.get(i).floor)){
                    arrayList.add("\n\n *************************************\n التاريخ: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "وقت فتح صندوق: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "وقت اغلاق صندوق: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "قيمة فتح الصندوق   = "+allCloseOpen.get(i).floor+"\n" +
                            "المبيعات    = "+(allCloseOpen.get(i).total-allCloseOpen.get(i).floor)+"\n" +
                            "المجموع الكلي   = "+allCloseOpen.get(i).total+"\n*************************************\n");
                    toPrint+="\n\n*************************************";
                    toPrint+="\n التارخ: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "وقت فتح صندوق: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "وقت اغلاق صندوق: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "قيمة فتح الصندوق   = "+allCloseOpen.get(i).floor+"\n" +
                            "المبيعات    = "+(allCloseOpen.get(i).total-allCloseOpen.get(i).floor)+"\n" +
                            "المجموع الكلي   = "+allCloseOpen.get(i).total;
                    toPrint+="\n*************************************\n";
                  }
                  else {
                    arrayList.add("\n\n التاريخ: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "وقت فتح صندوق: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "وقت اغلاق صندوق: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "قيمة فتح الصندوق   = "+allCloseOpen.get(i).floor+"\n" +
                            "المبيعات    = "+(allCloseOpen.get(i).total-allCloseOpen.get(i).floor)+"\n" +
                            "المجموع الكلي   = "+allCloseOpen.get(i).total);
                    toPrint+="\n\n التارخ: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "وقت فتح صندوق: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "وقت اغلاق صندوق: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "قيمة فتح الصندوق   = "+allCloseOpen.get(i).floor+"\n" +
                            "المبيعات    = "+(allCloseOpen.get(i).total-allCloseOpen.get(i).floor)+"\n" +
                            "المجموع الكلي   = "+allCloseOpen.get(i).total+"\n";
                  }

                }
              }
              arrayList.add("\n\n\n" +
                      "مجموع فتح صندوق   = "+floor+"\n" +
                      "مجموع المبيعات    = "+sale+"\n" +
                      "المجموع الكلي   = "+total);
              toPrint+="\n\n\n" +
                      "مجموع فتح صندوق   = "+floor+"\n" +
                      "مجموع المبيعات    = "+sale+"\n" +
                      "المجموع الكلي   = "+total;
              ArrayAdapter<String> adapter=new zReportAdapter(getApplicationContext(),R.layout.row_zreport,arrayList);
              listView.setAdapter(adapter);
            }
            else if (month.isChecked()){
              toPrint="";
              String []s =myDatefrom.split("-");
              String ss=s[1]+"-"+s[2];
              arrayList.add("اسم الموظف: "+empName+"" +
                      "\nالبريد الالكتروني: "+allCloseOpen.get(0).empEmail+"\n" +
                      "تاريخ هذه العمليه: "+new Date()+"" +
                      "\n\n");
              toPrint+="\nاسم الموظف: "+empName+"" +
                      "\nالبريد الالكتروني: "+allCloseOpen.get(0).empEmail+"\n" +
                      "تاريخ هذه العمليه: "+new Date()+"" +
                      "\n\n";
              for (int i=0 ; i< allCloseOpen.size();i++){
                if (allCloseOpen.get(i).dateOpen.contains(ss)){
                  floor+=allCloseOpen.get(i).floor;
                  sale+=allCloseOpen.get(i).total-allCloseOpen.get(i).floor;
                  total+=allCloseOpen.get(i).total;
                  closeOpenAtDate.add(allCloseOpen.get(i));

                  if ((allCloseOpen.get(i).total)<(allCloseOpen.get(i).floor)){
                    arrayList.add("\n\n *************************************\n التاريخ: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "وقت فتح صندوق: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "وقت اغلاق صندوق: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "قيمة فتح الصندوق   = "+allCloseOpen.get(i).floor+"\n" +
                            "المبيعات    = "+(allCloseOpen.get(i).total-allCloseOpen.get(i).floor)+"\n" +
                            "المجموع الكلي   = "+allCloseOpen.get(i).total+"\n*************************************\n");
                    toPrint+="\n\n*************************************";
                    toPrint+="\n التارخ: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "وقت فتح صندوق: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "وقت اغلاق صندوق: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "قيمة فتح الصندوق   = "+allCloseOpen.get(i).floor+"\n" +
                            "المبيعات    = "+(allCloseOpen.get(i).total-allCloseOpen.get(i).floor)+"\n" +
                            "المجموع الكلي   = "+allCloseOpen.get(i).total;
                    toPrint+="\n*************************************\n";
                  }
                  else {
                    arrayList.add("\n\n التاريخ: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "وقت فتح صندوق: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "وقت اغلاق صندوق: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "قيمة فتح الصندوق   = "+allCloseOpen.get(i).floor+"\n" +
                            "المبيعات    = "+(allCloseOpen.get(i).total-allCloseOpen.get(i).floor)+"\n" +
                            "المجموع الكلي   = "+allCloseOpen.get(i).total);
                    toPrint+="\n\n التارخ: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "وقت فتح صندوق: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "وقت اغلاق صندوق: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "قيمة فتح الصندوق   = "+allCloseOpen.get(i).floor+"\n" +
                            "المبيعات    = "+(allCloseOpen.get(i).total-allCloseOpen.get(i).floor)+"\n" +
                            "المجموع الكلي   = "+allCloseOpen.get(i).total+"\n";
                  }
                }
              }
              arrayList.add("\n\n\n" +
                      "مجموع فتح صندوق   = "+floor+"\n" +
                      "مجموع المبيعات    = "+sale+"\n" +
                      "المجموع الكلي   = "+total);
              toPrint+="\n\n\n" +
                      "مجموع فتح صندوق   = "+floor+"\n" +
                      "مجموع المبيعات    = "+sale+"\n" +
                      "المجموع الكلي   = "+total;
              ArrayAdapter<String> adapter=new zReportAdapter(getApplicationContext(),R.layout.row_zreport,arrayList);
              listView.setAdapter(adapter);
            }
            else if (day.isChecked()){
              toPrint="";
              arrayList.add("اسم الموظف: "+empName+"" +
                      "\nالبريد الالكتروني: "+allCloseOpen.get(0).empEmail+"\n" +
                      "تاريخ هذه العمليه: "+new Date()+"" +
                      "\n\n");
              toPrint+="\nاسم الموظف: "+empName+"" +
                      "\nالبريد الالكتروني: "+allCloseOpen.get(0).empEmail+"\n" +
                      "تاريخ هذه العمليه: "+new Date()+"" +
                      "\n\n";
              for (int i=0 ; i< allCloseOpen.size();i++){
                if (allCloseOpen.get(i).dateOpen.contains(myDatefrom)){
                  floor+=allCloseOpen.get(i).floor;
                  sale+=allCloseOpen.get(i).total-allCloseOpen.get(i).floor;
                  total+=allCloseOpen.get(i).total;
                  closeOpenAtDate.add(allCloseOpen.get(i));
                  if ((allCloseOpen.get(i).total)<(allCloseOpen.get(i).floor)){
                    arrayList.add("\n\n *************************************\n التاريخ: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "وقت فتح صندوق: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "وقت اغلاق صندوق: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "قيمة فتح الصندوق   = "+allCloseOpen.get(i).floor+"\n" +
                            "المبيعات    = "+(allCloseOpen.get(i).total-allCloseOpen.get(i).floor)+"\n" +
                            "المجموع الكلي   = "+allCloseOpen.get(i).total+"\n*************************************\n");
                    toPrint+="\n\n*************************************";
                    toPrint+="\n التارخ: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "وقت فتح صندوق: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "وقت اغلاق صندوق: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "قيمة فتح الصندوق   = "+allCloseOpen.get(i).floor+"\n" +
                            "المبيعات    = "+(allCloseOpen.get(i).total-allCloseOpen.get(i).floor)+"\n" +
                            "المجموع الكلي   = "+allCloseOpen.get(i).total;
                    toPrint+="\n*************************************\n";
                  }
                  else {
                    arrayList.add("\n\n التاريخ: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "وقت فتح صندوق: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "وقت اغلاق صندوق: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "قيمة فتح الصندوق   = "+allCloseOpen.get(i).floor+"\n" +
                            "المبيعات    = "+(allCloseOpen.get(i).total-allCloseOpen.get(i).floor)+"\n" +
                            "المجموع الكلي   = "+allCloseOpen.get(i).total);
                    toPrint+="\n\n التارخ: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "وقت فتح صندوق: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "وقت اغلاق صندوق: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "قيمة فتح الصندوق   = "+allCloseOpen.get(i).floor+"\n" +
                            "المبيعات    = "+(allCloseOpen.get(i).total-allCloseOpen.get(i).floor)+"\n" +
                            "المجموع الكلي   = "+allCloseOpen.get(i).total+"\n";
                  }

                }
              }
              arrayList.add("\n\n\n" +
                      "مجموع فتح صندوق   = "+floor+"\n" +
                      "مجموع المبيعات    = "+sale+"\n" +
                      "المجموع الكلي   = "+total);
              toPrint+="\n\n\n" +
                      "مجموع فتح صندوق   = "+floor+"\n" +
                      "مجموع المبيعات    = "+sale+"\n" +
                      "المجموع الكلي   = "+total;
              ArrayAdapter<String> adapter=new zReportAdapter(getApplicationContext(),R.layout.row_zreport,arrayList);
              listView.setAdapter(adapter);
            }
          }catch (IndexOutOfBoundsException e){
            Toast.makeText(statisticsMainActivity.this, "هذا الموظف لايمتلك مبيعات حالياً", Toast.LENGTH_SHORT).show();
          }
          catch (Exception e ){
            Toast.makeText(statisticsMainActivity.this, "الرجاء اختيار موظف", Toast.LENGTH_LONG).show();
          }
        }
        ////////////////////////
        ///////////////////////
        else{
          try{
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
              toPrint="";
              String []s =myDatefrom.split("-");
              String ss=s[2];
              arrayList.add("emp name: "+empName+"" +
                      "\nemp email: "+allCloseOpen.get(0).empEmail+"\n" +
                      "date of this opreation: "+new Date()+"" +
                      "\n\n");
              toPrint+="\nemp name: "+empName+"" +
                      "\nemp email: "+allCloseOpen.get(0).empEmail+"\n" +
                      "date of this opreation: "+new Date()+"" +
                      "\n\n";
              for (int i=0 ; i< allCloseOpen.size();i++){
                if (allCloseOpen.get(i).dateOpen.contains(ss)){
                  floor+=allCloseOpen.get(i).floor;
                  sale+=allCloseOpen.get(i).total-allCloseOpen.get(i).floor;
                  total+=allCloseOpen.get(i).total;
                  closeOpenAtDate.add(allCloseOpen.get(i));


                  if ((allCloseOpen.get(i).total)<(allCloseOpen.get(i).floor)){
                    arrayList.add("\n\n *************************************\n date: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "time open: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "time close: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "floor   = "+allCloseOpen.get(i).floor+"\n" +
                            "sale    = "+(allCloseOpen.get(i).total)+"\n" +
                            "total   = "+allCloseOpen.get(i).total+allCloseOpen.get(i).floor+"\n*************************************\n");
                    toPrint+="\n\n*************************************";
                    toPrint+="\n\n date: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "time open: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "time close: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "floor   = "+allCloseOpen.get(i).floor+"\n" +
                            "sale    = "+(allCloseOpen.get(i).total)+"\n" +
                            "total   = "+(allCloseOpen.get(i).total+allCloseOpen.get(i).floor);
                    toPrint+="\n*************************************\n";
                  }
                  else {
                    arrayList.add("\n\n date: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "time open: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "time close: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "floor   = "+allCloseOpen.get(i).floor+"\n" +
                            "sale    = "+(allCloseOpen.get(i).total)+"\n" +
                            "total   = "+allCloseOpen.get(i).total+allCloseOpen.get(i).floor);
                    toPrint+="\n\n date: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "time open: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "time close: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "floor   = "+allCloseOpen.get(i).floor+"\n" +
                            "sale    = "+(allCloseOpen.get(i).total)+"\n" +
                            "total   = "+(allCloseOpen.get(i).total+allCloseOpen.get(i).floor);
                  }
                }
              }
              arrayList.add("\n\n\n" +
                      "floor   = "+floor+"\n" +
                      "sale    = "+sale+"\n" +
                      "total   = "+total);
              toPrint+="\n\n\n" +
                      "floor   = "+floor+"\n" +
                      "sale    = "+sale+"\n" +
                      "total   = "+total;
              ArrayAdapter<String> adapter=new zReportAdapter(getApplicationContext(),R.layout.row_zreport,arrayList);
              listView.setAdapter(adapter);
            }
            else if (month.isChecked()){
              toPrint="";
              String []s =myDatefrom.split("-");
              String ss=s[1]+"-"+s[2];
              arrayList.add("emp name: "+empName+"" +
                      "\nemp email: "+allCloseOpen.get(0).empEmail+"\n" +
                      "date of this opreation: "+new Date()+"" +
                      "\n\n");
              toPrint+="\nemp name: "+empName+"" +
                      "\nemp email: "+allCloseOpen.get(0).empEmail+"\n" +
                      "date of this opreation: "+new Date()+"" +
                      "\n\n";
              for (int i=0 ; i< allCloseOpen.size();i++){
                if (allCloseOpen.get(i).dateOpen.contains(ss)){
                  floor+=allCloseOpen.get(i).floor;
                  sale+=allCloseOpen.get(i).total-allCloseOpen.get(i).floor;
                  total+=allCloseOpen.get(i).total;
                  closeOpenAtDate.add(allCloseOpen.get(i));
                  if ((allCloseOpen.get(i).total)<(allCloseOpen.get(i).floor)){
                    arrayList.add("\n\n *************************************\n date: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "time open: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "time close: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "floor   = "+allCloseOpen.get(i).floor+"\n" +
                            "sale    = "+(allCloseOpen.get(i).total)+"\n" +
                            "total   = "+allCloseOpen.get(i).total+allCloseOpen.get(i).floor+"\n*************************************\n");
                    toPrint+="\n\n*************************************";
                    toPrint+="\n\n date: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "time open: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "time close: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "floor   = "+allCloseOpen.get(i).floor+"\n" +
                            "sale    = "+(allCloseOpen.get(i).total)+"\n" +
                            "total   = "+(allCloseOpen.get(i).total+allCloseOpen.get(i).floor);
                    toPrint+="\n*************************************\n";
                  }
                  else {
                    arrayList.add("\n\n date: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "time open: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "time close: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "floor   = "+allCloseOpen.get(i).floor+"\n" +
                            "sale    = "+(allCloseOpen.get(i).total)+"\n" +
                            "total   = "+allCloseOpen.get(i).total+allCloseOpen.get(i).floor);
                    toPrint+="\n\n date: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "time open: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "time close: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "floor   = "+allCloseOpen.get(i).floor+"\n" +
                            "sale    = "+(allCloseOpen.get(i).total)+"\n" +
                            "total   = "+(allCloseOpen.get(i).total+allCloseOpen.get(i).floor);
                  }
                }
              }
              arrayList.add("\n\n\n" +
                      "floor   = "+floor+"\n" +
                      "sale    = "+sale+"\n" +
                      "total   = "+total);
              toPrint+="\n\n\n" +
                      "floor   = "+floor+"\n" +
                      "sale    = "+sale+"\n" +
                      "total   = "+total;
              ArrayAdapter<String> adapter=new zReportAdapter(getApplicationContext(),R.layout.row_zreport,arrayList);
              listView.setAdapter(adapter);
            }
            else if (day.isChecked()){
              toPrint="";
              arrayList.add("emp name: "+empName+"" +
                      "\nemp email: "+allCloseOpen.get(0).empEmail+"\n" +
                      "date of this opreation: "+new Date()+"" +
                      "\n\n");
              toPrint+="\nemp name: "+empName+"" +
                      "\nemp email: "+allCloseOpen.get(0).empEmail+"\n" +
                      "date of this opreation: "+new Date()+"" +
                      "\n\n";
              for (int i=0 ; i< allCloseOpen.size();i++){
                if (allCloseOpen.get(i).dateOpen.contains(myDatefrom)){
                  floor+=allCloseOpen.get(i).floor;
                  sale+=allCloseOpen.get(i).total-allCloseOpen.get(i).floor;
                  total+=allCloseOpen.get(i).total;
                  closeOpenAtDate.add(allCloseOpen.get(i));
                  if ((allCloseOpen.get(i).total)<(allCloseOpen.get(i).floor)){
                    arrayList.add("\n\n *************************************\n date: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "time open: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "time close: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "floor   = "+allCloseOpen.get(i).floor+"\n" +
                            "sale    = "+(allCloseOpen.get(i).total)+"\n" +
                            "total   = "+allCloseOpen.get(i).total+allCloseOpen.get(i).floor+"\n*************************************\n");
                    toPrint+="\n\n*************************************";
                    toPrint+="\n\n date: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "time open: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "time close: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "floor   = "+allCloseOpen.get(i).floor+"\n" +
                            "sale    = "+(allCloseOpen.get(i).total)+"\n" +
                            "total   = "+(allCloseOpen.get(i).total+allCloseOpen.get(i).floor);
                    toPrint+="\n*************************************\n";
                  }
                  else {
                    arrayList.add("\n\n date: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "time open: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "time close: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "floor   = "+allCloseOpen.get(i).floor+"\n" +
                            "sale    = "+(allCloseOpen.get(i).total)+"\n" +
                            "total   = "+allCloseOpen.get(i).total+allCloseOpen.get(i).floor);
                    toPrint+="\n\n date: "+allCloseOpen.get(i).dateOpen+"\n" +
                            "time open: "+allCloseOpen.get(i).dateAndTimeOpen+"\n" +
                            "time close: "+allCloseOpen.get(i).dateAndTimeClose+"\n" +
                            "floor   = "+allCloseOpen.get(i).floor+"\n" +
                            "sale    = "+(allCloseOpen.get(i).total)+"\n" +
                            "total   = "+(allCloseOpen.get(i).total+allCloseOpen.get(i).floor);
                  }

                }
              }
              arrayList.add("\n\n\n" +
                      "floor   = "+floor+"\n" +
                      "sale    = "+sale+"\n" +
                      "total   = "+total);
              toPrint+="\n\n\n" +
                      "floor   = "+floor+"\n" +
                      "sale    = "+sale+"\n" +
                      "total   = "+total;
              ArrayAdapter<String> adapter=new zReportAdapter(getApplicationContext(),R.layout.row_zreport,arrayList);
              listView.setAdapter(adapter);
            }
          }catch (IndexOutOfBoundsException e){
            Toast.makeText(statisticsMainActivity.this, "this employee has no sales", Toast.LENGTH_SHORT).show();
          }
          catch (Exception e ){
            Toast.makeText(statisticsMainActivity.this, "please choose the employee", Toast.LENGTH_LONG).show();
          }
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
    day=findViewById(R.id.a2dayRadio);
    year.setChecked(true);
    if(HomeAct.lang==1){
      year.setText("تقرير سنوي");
      month.setText("تقرير شهري");
      day.setText("تقرير يومي");
      button.setText("بحث");
      textView.setText("ادخل التاريخ");
    }
    else {
      button.setText("search");
    }
    loadEmp();
    loadCloseOpen();
  }




  public void loadEmp(){

    db.collection("Res_1_employee").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
      @Override
      public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
        if(HomeAct.lang==1){
          empnames.add("اختر موظف");
        }
        else {
          empnames.add("select employee");
        }

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

  void print(String s){
    Document mDoc=new Document();
    String mfileName= "cash_drawer_"+System.currentTimeMillis();
    String mfilepath = Environment.getExternalStorageDirectory()+"/"+mfileName+".pdf";
    try{
      PdfWriter writer= PdfWriter.getInstance(mDoc, new FileOutputStream(mfilepath));
      mDoc.open();

      mDoc.add(new Paragraph(s));

      mDoc.close();
      writer.close();
      Toast.makeText(this, mfileName+".pdf file is saved\n"+mfilepath, Toast.LENGTH_SHORT).show();

    }catch (Exception e){
      Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    //Toast.makeText(this, s, Toast.LENGTH_LONG).show();
  }

}