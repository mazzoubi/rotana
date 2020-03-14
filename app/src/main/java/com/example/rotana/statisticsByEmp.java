package com.example.rotana;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class statisticsByEmp extends AppCompatActivity {

    FirebaseFirestore db;

    ArrayList<classEmployee> emp = new ArrayList<>();
    ArrayList<String> empnames=new ArrayList<>();
    ArrayList<classPayment> payments = new ArrayList<>();
    ArrayList<classSales> sales = new ArrayList<>();

    String dateyear="",datemonth="",email="";

    Button search , chart;
    TextView s,p,comp,resa;
    Spinner month,year,employee;
    ListView payList,saleList;

    ArrayAdapter<classPayment>payadpt;
    ArrayAdapter<classSales>salAdapt;
    SharedPreferences shared;
    int lang=0;
    double payCost=0;
    double salCost=0;
    int monthCheck=0;
    boolean ch=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_statistics_by_emp);

        shared = getSharedPreferences("lang", MODE_PRIVATE);

        search=findViewById(R.id.empStatSearchButton);
        chart=findViewById(R.id.empStatStatistics);
        resa=findViewById(R.id.textView322);
        s=findViewById(R.id.empStatSaleText);
        p=findViewById(R.id.empStatPayText);
        comp=findViewById(R.id.empStatText);

        month=findViewById(R.id.empStatSpinner_month);
        year=findViewById(R.id.empStatSpinner_year);
        employee=findViewById(R.id.empStatSpinner_empname);

        payList=findViewById(R.id.empStatPayList);
        saleList=findViewById(R.id.empStatSaleList);

        if (shared.getString("language", "").equals("arabic")){
            search.setText("بحث");
            s.setText("المبيعات");
            p.setText("التكاليف");
            comp.setText("مؤسسة الدخيل \nN.O.V.A  فريق المطورين ");
            lang=1;
        }

        loadEmp();

        employee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){ch=false;}else {

                    ch=true;
                    comp.setText(emp.get(position - 1).Fname + " " + emp.get(position - 1).Lname + "\n" +
                            emp.get(position - 1).empPhone + "\n" +
                            emp.get(position - 1).email + "\n" +
                            emp.get(position - 1).empType);
                    email = emp.get(position - 1).email;
                }
                if (lang==1){
                    Toast.makeText(getApplicationContext(),"تم, الرجاء اختيار التاريخ الان", Toast.LENGTH_LONG).show();
                }
                else {Toast.makeText(getApplicationContext(),"ok, please choose the date now", Toast.LENGTH_LONG).show();}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){

                    case 0:
                        datemonth="";
                        ch=false;
                        break;

                    case 1:
                        datemonth="01";
                        ch=true;
                        break;

                    case 2:
                        datemonth="02";
                        ch=true;
                        break;

                    case 3:
                        datemonth="03";
                        ch=true;
                        break;

                    case 4:
                        datemonth="04";
                        ch=true;
                        break;

                    case 5:
                        datemonth="05";
                        ch=true;
                        break;

                    case 6:
                        datemonth="06";
                        ch=true;
                        break;

                    case 7:
                        datemonth="07";
                        ch=true;
                        break;

                    case 8:
                        datemonth="08";
                        ch=true;
                        break;

                    case 9:
                        datemonth="09";
                        ch=true;
                        break;

                    case 10:
                        datemonth="10";
                        ch=true;
                        break;

                    case 11:
                        datemonth="11";
                        ch=true;
                        break;

                    case 12:
                        datemonth="12";
                        ch=true;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){dateyear="";}
                else {
                    dateyear=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.isEmpty()){
                    if (lang==1) {
                        Toast.makeText(getApplicationContext(), "الرجاء اختيار اسم الموظف", Toast.LENGTH_LONG).show();
                    }
                    else {Toast.makeText(getApplicationContext(), "please choose the employee name", Toast.LENGTH_LONG).show();}
                }
                else if (dateyear.isEmpty()){
                    if (lang==1) {
                        Toast.makeText(getApplicationContext(), "الرجاء تحديد السنه", Toast.LENGTH_LONG).show();
                    }else{Toast.makeText(getApplicationContext(), "please select a year", Toast.LENGTH_LONG).show();}
                }
                else if (datemonth.isEmpty()){
                    if (lang==1) {
                        Toast.makeText(getApplicationContext(), "الرجاء تحديد الشهر", Toast.LENGTH_LONG).show();
                    }else{Toast.makeText(getApplicationContext(), "please select a month", Toast.LENGTH_LONG).show();}

                }
                else {

                    loadSale(email,datemonth,dateyear);
                    loadPayment(email,datemonth,dateyear);


                }

            }
        });

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ch) {
                    Intent n = new Intent(getApplicationContext(), statistics_chart.class);
                    n.putExtra("sale", salCost);
                    n.putExtra("pay", payCost);
                    startActivity(n);
                }
                else { if(lang==1) {
                    Toast.makeText(getApplicationContext(),"الرجاء البحث من خلال السنه او الشهر لعرض المخطط",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"please filtering the data py year or month",Toast.LENGTH_LONG).show();
                }
                }
            }
        });

    }


    public void loadSale( String email1,  String month1,  String year1){
        // Toast.makeText(getApplicationContext(), "sale ok", Toast.LENGTH_LONG).show();
        sales = new ArrayList<>();
        salCost=0;
        String []s ;
        String aaa;
        for(int i=0;i<Adminpage.cSale.size();i++) {
            s = Adminpage.cSale.get(i).date.split("-");
            aaa = s[1] + "-" + s[2];
            if (Adminpage.cSale.get(i).empEmail.contains(email1) && aaa.equals(month1 + "-" + year1)) {
                sales.add(Adminpage.cSale.get(i));
                salCost += Adminpage.cSale.get(i).sale;
            }
        }
        salAdapt = new adminAdapterSales(getApplicationContext(), R.layout.row, sales);
        saleList.setAdapter(salAdapt);

    }
    public void loadPayment( String email1,  String month1,  String year1) {

        // Toast.makeText(getApplicationContext(), "payment ok", Toast.LENGTH_LONG).show();
        payments = new ArrayList<>();
        payCost=0;
        String []s ;
        String aaa;
        for (int i=0 ; i<Adminpage.cPyment.size();i++) {
            s=Adminpage.cPyment.get(i).date.split("-");
            aaa=s[1]+"-"+s[2];
            if (Adminpage.cPyment.get(i).emp.equals(email1) && aaa.equals(month1 + "-" + year1)) {
                payments.add(Adminpage.cPyment.get(i));
                payCost+=Adminpage.cPyment.get(i).pay;
            }
        }
        payadpt = new adminAdapterPayment(getApplicationContext(), R.layout.row, payments);
        payList.setAdapter(payadpt);
        loadSale(email1,month1,year1);
    }
    public void loadEmp(){
        db = FirebaseFirestore.getInstance();
        db.collection("Res_1_employee").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                if (lang==1){
                    empnames.add("اختيار موظف");
                }
                else {empnames.add("select employee");}
                for (DocumentSnapshot d : list) {
                    classEmployee a = d.toObject(classEmployee.class);
                    if (a.cashWork || a.delevery) {
                        if(a.email.equals(null)){
                            a.email="";
                        }
                        emp.add(a);
                        empnames.add(a.Fname+" "+a.Lname+" "+a.empType);
                    }
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,empnames);
                employee.setAdapter(adapter);


            }
        });


    }
}
