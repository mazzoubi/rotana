package com.example.foodholic;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.print.pdf.PrintedPdfDocument;
import android.printservice.PrintDocument;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.Table;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Zreport extends AppCompatActivity {

    Spinner spinnerYear,spinnerMonth;
    Button monthreport,yearReport ,print;
    ListView listView;
    SharedPreferences shared2;
    String year="";
    String month ="";
    FirebaseFirestore db;

    ArrayList<classSales>cSale;
   ArrayList<String> smonth;
   ArrayList<String> syear;

   classCurrencyAndTax currencyAndTax;
   ArrayAdapter<String> adapter;
   private static final int STORAGE_CODE=1000;
   String pdfTex="";

   int m=0;
   int y=0;

   int flag=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
                flag=1;
                if (year.isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(Zreport.this, "الرجاء اختيار السنه", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Zreport.this, "Please choose year", Toast.LENGTH_SHORT).show();
                    }

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
                    syear.add("\n\n\t\t\t\t[-- yearly report --]" +
                            "\nRes : "+HomeAct.resName+"\n" +
                            "pill date : "+new Date()+"\n" +
                            "\t________________________________________________\n" +
                            "\n");
                    double z=0;
                    int y=0;
                    int i=1;
                    String []s;
                    String aaa;
                    for(;i<=12;i++){
                        for(int a=0 ; a<list.size();a++){
                            s=list.get(a).date.split("-");
                            aaa=s[1]+"-"+s[2];
                            if (i+"".length()==1){
                                if (aaa.equals("0"+i+"-"+year)){
                                   z+=list.get(a).sale;
                                   y=1;

                                }
                            }else{
                                if (aaa.equals(""+i+"-"+year)){
                                    z+=list.get(a).sale;
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
                    syear.add("\n\t----------------------------------------------\n");
                    syear.add("total at : "+year+"="+bb+" "+currencyAndTax.currency +"\n" +
                            "tax : "+currencyAndTax.tax+"%\n"+
                            "tax ammount : "+((bb*currencyAndTax.tax/100))+" "+currencyAndTax.currency +"\n"+
                            "\n\t________________________________________________\n");
                    syear.add("\t**** Aldahkeel +962798056383 ****\t");

                    adapter=new zReportAdapter(getApplicationContext(),R.layout.row_zreport,syear);
                    listView.setAdapter(adapter);
                }
            }
        });

        monthreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flag=2;
               // cSalepdf=new ArrayList<>();
               // csale1=new ArrayList<>();
                if (year.isEmpty()){
                    if(HomeAct.lang==1){
                        Toast.makeText(Zreport.this, "الرجاء اختيار السنه", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Zreport.this, "Please choose year", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (month.isEmpty()){
                    if(HomeAct.lang==1){
                        Toast.makeText(Zreport.this, "الرجاء اختيار الشهر", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Zreport.this, "Please choose month", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    m=1;
                    y=0;
                    double bb=0;
                    smonth=new ArrayList<>();
                    smonth.add("\n\n\t\t\t\t[-- monthly report --]" +
                            "\nRes : "+HomeAct.resName+"\n" +
                            "pill dat : "+new Date()+"\n" +
                            "\t________________________________________________\n" +
                            "\n\n");
                    yearReport.setBackgroundColor(Color.GRAY);
                    monthreport.setBackgroundColor(Color.GREEN);
                    for (int i=0 ; i < cSale.size();i++ ){
                        if (cSale.get(i).date.contains(month+"-"+year)){
                            bb+=cSale.get(i).sale;
                        }
                    }
                    smonth.add("total at : "+month+"-"+year+"="+bb+" "+currencyAndTax.currency +"\n" +
                            "tax : "+currencyAndTax.tax+"%\n"+
                            "tax ammount : "+((bb*currencyAndTax.tax/100))+" "+currencyAndTax.currency +"\n"
                            +"\n________________________________________________\n");
                    smonth.add("\t**** Aldahkeel +962798056383 ****\t");

                    adapter=new zReportAdapter(getApplicationContext(),R.layout.row_zreport,smonth);
                    listView.setAdapter(adapter);
                }
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (m==0&&y==0){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(Zreport.this, "الرجاء اختيار نوع التقرير", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Zreport.this, "please choose the type of report", Toast.LENGTH_SHORT).show();
                    }

                }
                else if (y==1&&m==0){
                    String s="";
                    for (int i=0; i< syear.size();i++){
                        s+=syear.get(i)+"\n";
                    }
                    savePDF();
                }
                else if (y==0&& m==1){
                    String s="";
                    for (int i=0; i< smonth.size();i++){
                        s+=smonth.get(i)+"\n";
                    }
                    pdfTex=s;
                    savePDF();
                }
            }
        });

    }

    void init(){
        db=FirebaseFirestore.getInstance();
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        spinnerMonth=findViewById(R.id.zspinnerMonth);
        spinnerYear=findViewById(R.id.zspinnerYear);
        yearReport = findViewById(R.id.yearReport);
        monthreport = findViewById(R.id.monthReport);
        print=findViewById(R.id.button_printReport);
        listView=findViewById(R.id.reportList);

        currencyAndTax=new classCurrencyAndTax();

        db.collection("Res_1_currencyAndTax").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list =queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d : list){
                    currencyAndTax=d.toObject(classCurrencyAndTax.class);
                }
            }
        });

        if(shared2.getString("language", "").equals("arabic")) {
            yearReport.setText("تقرير سنوي");
            monthreport.setText("تقرير شهري");
        }
        else {

        }

        db.collection("Res_1_sales").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                cSale=new ArrayList<>() ;
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    try{cSale.add(d.toObject(classSales.class));}catch (Exception e){}
                }
            }
        });
    }

    void savePDF(){
        Document mDoc=new Document();
       // PdfDocument mDoc=new PdfDocument();
        String mfileName= "Zreport"+new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        String mfilepath = Environment.getExternalStorageDirectory()+"/"+mfileName+".pdf";

        double sum=0;
        double []arr12=new double[12];
        double []arr31=new double[31];
        for (int i=0;i<31;i++){
            if (i<12){
                arr12[i]=0;
            }
            arr31[i]=0;
        }
        PdfPTable pdfPTable=new PdfPTable(3);
        PdfPCell pdfPCell=new PdfPCell(new Paragraph("date"));
        PdfPCell pdfPCell2=new PdfPCell(new Paragraph("sales"));
        PdfPCell pdfPCell3=new PdfPCell(new Paragraph("currency"));

        pdfPTable.addCell(pdfPCell);
        pdfPTable.addCell(pdfPCell2);
        pdfPTable.addCell(pdfPCell3);

        if (flag==1){
        for (classSales d : cSale){
            if (d.date.contains(year)){
                if (d.date.contains("01-"+year)){
                    arr12[0]+=d.sale;
                }
                else if (d.date.contains("02-"+year)){
                    arr12[1]+=d.sale;
                }else if (d.date.contains("03-"+year)){
                    arr12[2]+=d.sale;
                }else if (d.date.contains("04-"+year)){
                    arr12[3]+=d.sale;
                }else if (d.date.contains("05-"+year)){
                    arr12[4]+=d.sale;
                }else if (d.date.contains("06-"+year)){
                    arr12[5]+=d.sale;
                }else if (d.date.contains("07-"+year)){
                    arr12[6]+=d.sale;
                }else if (d.date.contains("08-"+year)){
                    arr12[7]+=d.sale;
                }else if (d.date.contains("09-"+year)){
                    arr12[8]+=d.sale;
                }else if (d.date.contains("10-"+year)){
                    arr12[9]+=d.sale;
                }else if (d.date.contains("11-"+year)){
                    arr12[10]+=d.sale;
                }else if (d.date.contains("12-"+year)){
                    arr12[11]+=d.sale;
                }

            }
        }
        for (int i=0;i<12;i++){
            sum+=arr12[i];
            pdfPTable.addCell(new Paragraph((i+1)+"-"+year));
            pdfPTable.addCell(new Paragraph(arr12[i]+""));
            pdfPTable.addCell(currencyAndTax.currency);

        }
        }
        else if (flag==2){
            for (classSales d : cSale){
                if (d.date.contains(month+"-"+year)){
                     if (d.date.contains("01-"+month+"-"+year)){
                         arr31[0]+=d.sale;
                    }else if (d.date.contains("02-"+month+"-"+year)){
                         arr31[1]+=d.sale;
                    }else if (d.date.contains("03-"+month+"-"+year)){
                         arr31[2]+=d.sale;
                    }else if (d.date.contains("04-"+month+"-"+year)){
                         arr31[3]+=d.sale;
                    }else if (d.date.contains("05-"+month+"-"+year)){
                         arr31[4]+=d.sale;
                    }else if (d.date.contains("06-"+month+"-"+year)){
                         arr31[5]+=d.sale;
                    }else if (d.date.contains("07-"+month+"-"+year)){
                         arr31[6]+=d.sale;
                    }else if (d.date.contains("08-"+month+"-"+year)){
                         arr31[7]+=d.sale;
                    }else if (d.date.contains("09-"+month+"-"+year)){
                         arr31[8]+=d.sale;
                    }else if (d.date.contains("10-"+month+"-"+year)){
                         arr31[9]+=d.sale;
                    }else if (d.date.contains("11-"+month+"-"+year)){
                         arr31[10]+=d.sale;
                    }else if (d.date.contains("12-"+month+"-"+year)){
                         arr31[11]+=d.sale;
                    }else if (d.date.contains("13-"+month+"-"+year)){
                         arr31[12]+=d.sale;
                    }else if (d.date.contains("14-"+month+"-"+year)){
                         arr31[13]+=d.sale;
                    }else if (d.date.contains("15-"+month+"-"+year)){
                         arr31[14]+=d.sale;
                    }else if (d.date.contains("16-"+month+"-"+year)){
                         arr31[15]+=d.sale;
                    }else if (d.date.contains("17-"+month+"-"+year)){
                         arr31[16]+=d.sale;
                    }else if (d.date.contains("18-"+month+"-"+year)){
                         arr31[17]+=d.sale;
                    }else if (d.date.contains("19-"+month+"-"+year)){
                         arr31[18]+=d.sale;
                    }else if (d.date.contains("20-"+month+"-"+year)){
                         arr31[19]+=d.sale;
                    }else if (d.date.contains("21-"+month+"-"+year)){
                         arr31[20]+=d.sale;
                    }else if (d.date.contains("22-"+month+"-"+year)){
                         arr31[21]+=d.sale;
                    }else if (d.date.contains("23-"+month+"-"+year)){
                         arr31[22]+=d.sale;
                    }else if (d.date.contains("24-"+month+"-"+year)){
                         arr31[23]+=d.sale;
                    }else if (d.date.contains("25-"+month+"-"+year)){
                         arr31[24]+=d.sale;
                    }else if (d.date.contains("26-"+month+"-"+year)){
                         arr31[25]+=d.sale;
                    }else if (d.date.contains("27-"+month+"-"+year)){
                         arr31[26]+=d.sale;
                    }else if (d.date.contains("28-"+month+"-"+year)){
                         arr31[27]+=d.sale;
                    }else if (d.date.contains("29-"+month+"-"+year)){
                         arr31[28]+=d.sale;
                    }else if (d.date.contains("30-"+month+"-"+year)){
                         arr31[29]+=d.sale;
                    }else if (d.date.contains("31-"+month+"-"+year)){
                         arr31[30]+=d.sale;
                    }
                }
            }
            for (int i=0;i<31;i++){
                sum+=arr31[i];
                pdfPTable.addCell(new Paragraph((i+1)+"-"+month+"-"+year));
                pdfPTable.addCell(new Paragraph(arr31[i]+""));
                pdfPTable.addCell(currencyAndTax.currency);

            }
        }

        try{

            PdfWriter.getInstance(mDoc, new FileOutputStream(mfilepath));
            mDoc.open();
            mDoc.add(new Paragraph("Tax Report\n\n"+"Res Name:  "+HomeAct.resName+"       Operation Date:   "+new Date()+"\n\n\n"));
            mDoc.add(pdfPTable);
            mDoc.add(new Paragraph("\nTax: "+currencyAndTax.tax+"%"));
            mDoc.add(new Paragraph("\nTax Amount: "+(sum*currencyAndTax.tax/100)+" "+currencyAndTax.currency));

            if (flag==1){
                mDoc.add(new Paragraph("\nTotal Sales At  "+year+" : "+sum+" "+currencyAndTax.currency));
            }else if (flag==2){
                mDoc.add(new Paragraph("\nTotal Sales At  "+month+"-"+year+" : "+sum+" "+currencyAndTax.currency));
            }


            mDoc.close();
            Toast.makeText(this, mfileName+".pdf file is saved\n"+mfilepath, Toast.LENGTH_SHORT).show();

        }catch (Exception e){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_CODE:{
                if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    savePDF();
                }
                else {

                }
            }

        }
    }
}
