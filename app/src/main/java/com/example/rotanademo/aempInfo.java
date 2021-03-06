package com.example.rotanademo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class aempInfo extends AppCompatActivity {

    String myDatefrom=" / / ";
    DatePickerDialog.OnDateSetListener date_;
    TextView dateExit,datetext,ref,empInfo;
    Button permissions;
    int position;
    SharedPreferences shared2;
    public static ArrayList<classExit> ex=new ArrayList<>();
    public static ArrayList<classLoans> loan=new ArrayList<>();
    public static ArrayList<classSalDiscount> dis=new ArrayList<>();
    FirebaseFirestore db ,db1,db2;
    int dayoff=0;
    int hourex=0;

    double disVal=0;
    double disLoan=0;
    int numDis=0;
    int numloan=0;
    double sal=0;
    String datesOfSal="";
    String date;
    
    FirebaseFirestore fb1;
    ArrayList<classEmployee> sss=aempLogin.sss;
    String email="";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_aemp_info);


        email=getIntent().getStringExtra("empemail");


        for (int i =0 ; i<sss.size();i++){
            if (sss.get(i).email.equals(email)){
                position=i;
                break;
            }
        }

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Date a=new Date();
        String aa=a+"";
        String[] aaa=aa.split(" ");
        String day =(a.getDate())+"";
        String month =(a.getMonth()+1)+"";
        date=day+"-"+month+"-"+aaa[5];
        myDatefrom=month+"-"+aaa[5];
        if (month.length()==1){
            month="0"+month;
            myDatefrom=month+"-"+aaa[5];
            date=day+"-"+month+"-"+aaa[5];
        }
        if(day.length()==1){
            day="0"+day;
            myDatefrom=month+"-"+aaa[5];
            date=day+"-"+month+"-"+aaa[5];
        }

        datetext=findViewById(R.id.textView_date);
        datetext.setText(date);
        ref=findViewById(R.id.ref);
        empInfo=findViewById(R.id.textView13);


        permissions=findViewById(R.id.addpermissions);

        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
       

        String salDate="";
        salDate=sss.get(position).regisetDate;
        String[]mm=salDate.split("-");
        if (Integer.parseInt(day)<=Integer.parseInt(mm[0])){
            datesOfSal=mm[0]+"-"+month+"-"+aaa[5];
        }
        else {
            int x=(Integer.parseInt(month))+1;
            int y=(Integer.parseInt(aaa[5]));
            if (x==13){x=1;y=y+1;}
            datesOfSal=mm[0]+"-"+x+"-"+y;
        }


        


        db=FirebaseFirestore.getInstance();
        db1=FirebaseFirestore.getInstance();
        db2=FirebaseFirestore.getInstance();
        ////////////////////////////////////////////////////////////////////
        db1.collection("Res_1_salDiscount").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                dis=new ArrayList<>();
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    classSalDiscount a=d.toObject(classSalDiscount.class);
                    if(a.empEmail.equals(sss.get(position).email)){
                        dis.add(a);
                        disVal+=a.value;
                        sal=sal-a.value;
                        numDis++;
                    }
                }
                if(shared2.getString("language", "").equals("arabic")) {
                    String joptype=sss.get(position).jopType,type=sss.get(position).empType;
                    String s1="",s2="";
                    if (joptype.equals("admin")){
                        s1="مدير";
                    }else if(joptype.equals("casher")){
                        s1="كاشير";
                    }else if(joptype.equals("chef")){
                        s1="شيف";
                    }else if (joptype.equals("chef assistant")){
                        s1="مساعد شيف";
                    }else if(joptype.equals("driver")){
                        s1="سائق";
                    }else if (joptype.equals("floor worker")){
                        s1="عامل صاله";
                    }else if(joptype.equals("worker")){
                        s1="عامل";
                    }

                    if (type.equals("ad")){
                        s2="مدير";
                    }else if(joptype.equals("emp")){
                        s2="موظف";
                    }else if(joptype.equals("dr")) {
                        s2 = "سائق";
                    }
                    empInfo.setText("اسم الموظف :   "+sss.get(position).Fname+" "+sss.get(position).Lname
                            +"\n\n"+"الهاتف :   "+sss.get(position).empPhone
                            +"\n\n"+"العنوان : "+sss.get(position).address+"\n\n"
                            +"نوع الموظف :   "+s2
                            +"\n\n"+"المسمى الوظيفي : "+s1
                            +"\n\n"+"البريد الالكتروني :   "+sss.get(position).email+"\n\n"
                            +"تاريخ التوظيف :   "+sss.get(position).regisetDate
                            +"\n\n"+"الراتب :   "+sss.get(position).sal+" د.ا"+"\n\n"
                            +"\n\n"+"عدد الحسومات :   "+numDis+"\n\n"+
                            "قيمة الحسومات :   "+disVal+"\n\n"+
                            "عدد السلف :   "+numloan+"\n\n"+
                            "قيمة السلف :   "+disLoan+"\n\n"+
                            "تاريخ استحقاق الراتب :   "+datesOfSal);

                }
                else {
                    empInfo.setText("employee name :   "+sss.get(position).Fname+" "+sss.get(position).Lname
                            +"\n\n"+"employee phone :   "+sss.get(position).empPhone
                            +"\n\n"+"employee address : "+sss.get(position).address+"\n\n"
                            +"employee type : "+sss.get(position).empType
                            +"\n\n"+"jop type : "+sss.get(position).jopType
                            +"\n\n"+"employee email :   "+sss.get(position).email+"\n\n"
                            +"employee register date :  "+sss.get(position).regisetDate+"\n\n"+
                            "employee salary : "+sss.get(position).sal+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD")
                            +"\n\n"+"num of discount :   "+numDis+"\n\n"+
                            "discount value :   "+disVal+"\n\n"+
                            "num of loans :   "+numloan+"\n\n"+
                            "loans value :   "+disLoan+"\n\n"+
                            "dates of sal payment :   "+datesOfSal
                    );
                }
            }
        });
        ////////////////////////////////////////////////////////////////////

        db2.collection("Res_1_loans").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                loan=new ArrayList<>();
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    classLoans a=d.toObject(classLoans.class);
                    if(a.empEmail.equals(sss.get(position).email)){
                        loan.add(a);
                        disLoan+=a.loan;
                        sal=sal-a.repyment;
                        numloan++;
                    }
                }
                if(shared2.getString("language", "").equals("arabic")) {
                    String joptype=sss.get(position).jopType,type=sss.get(position).empType;
                    String s1="",s2="";
                    if (joptype.equals("admin")){
                        s1="مدير";
                    }else if(joptype.equals("casher")){
                        s1="كاشير";
                    }else if(joptype.equals("chef")){
                        s1="شيف";
                    }else if (joptype.equals("chef assistant")){
                        s1="مساعد شيف";
                    }else if(joptype.equals("driver")){
                        s1="سائق";
                    }else if (joptype.equals("floor worker")){
                        s1="عامل صاله";
                    }else if(joptype.equals("worker")){
                        s1="عامل";
                    }

                    if (type.equals("ad")){
                        s2="مدير";
                    }else if(joptype.equals("emp")){
                        s2="موظف";
                    }else if(joptype.equals("dr")) {
                        s2 = "سائق";
                    }
                    empInfo.setText("اسم الموظف :   "+sss.get(position).Fname+" "+sss.get(position).Lname
                            +"\n\n"+"الهاتف :   "+sss.get(position).empPhone
                            +"\n\n"+"العنوان : "+sss.get(position).address+"\n\n"
                            +"نوع الموظف :   "+s2
                            +"\n\n"+"المسمى الوظيفي : "+s1
                            +"\n\n"+"البريد الالكتروني :   "+sss.get(position).email+"\n\n"
                            +"تاريخ التوظيف :   "+sss.get(position).regisetDate
                            +"\n\n"+"الراتب :   "+sss.get(position).sal+" د.ا"+"\n\n"
                            +"\n\n"+"عدد الحسومات :   "+numDis+"\n\n"+
                            "قيمة الحسومات :   "+disVal+"\n\n"+
                            "عدد السلف :   "+numloan+"\n\n"+
                            "قيمة السلف :   "+disLoan+"\n\n"+
                            "تاريخ استحقاق الراتب :   "+datesOfSal);

                }
                else {
                    empInfo.setText("employee name :   "+sss.get(position).Fname+" "+sss.get(position).Lname
                            +"\n\n"+"employee phone :   "+sss.get(position).empPhone
                            +"\n\n"+"employee address : "+sss.get(position).address+"\n\n"
                            +"employee type : "+sss.get(position).empType
                            +"\n\n"+"jop type : "+sss.get(position).jopType
                            +"\n\n"+"employee email :   "+sss.get(position).email+"\n\n"
                            +"employee register date :  "+sss.get(position).regisetDate+"\n\n"+
                            "employee salary : "+sss.get(position).sal+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD")
                            +"\n\n"+"num of discount :   "+numDis+"\n\n"+
                            "discount value :   "+disVal+"\n\n"+
                            "num of loans :   "+numloan+"\n\n"+
                            "loans value :   "+disLoan+"\n\n"+
                            "dates of sal payment :   "+datesOfSal
                    );
                }
            }
        });

///////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////
        db.collection("Res_1_exitRequest").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                ex=new ArrayList<>();

                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    classExit a=d.toObject(classExit.class);
                    String []s=a.date.split("-");
                    if (a.email.equals(sss.get(position).email)&&myDatefrom.equals(s[1]+"-"+s[2])){
                        ex.add(a);
                        if (a.time.equals("day off")){
                            dayoff++;
                        }
                        else if (a.time.equals("1")){
                            hourex++;
                        }
                        else if (a.time.equals("2")){
                            hourex+=2;
                        }
                        else if (a.time.equals("3")){
                            hourex+=3;
                        }
                        else{}
                    }


                    if(shared2.getString("language", "").equals("arabic")) {
                        dateExit.setText("المغادرات : "+hourex+"\n"+"الغيابات : "+dayoff);
                    }
                    else dateExit.setText("hours exit : "+hourex+"\n"+"day off : "+dayoff);
                }


            }
        });





        ////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////

        dateExit =findViewById(R.id.exitDate);

        permissions=findViewById(R.id.addpermissions);


        empInfo.setText("employee name :   "+sss.get(position).Fname+" "+sss.get(position).Lname
                +"\n\n"+"employee phone :   "+sss.get(position).empPhone
                +"\n\n"+"employee address : "+sss.get(position).address+"\n\n"
                +"employee type : "+sss.get(position).empType
                +"\n\n"+"jop type : "+sss.get(position).jopType
                +"\n\n"+"employee email :   "+sss.get(position).email+"\n\n"
                +"employee register date :  "+sss.get(position).regisetDate+"\n\n"+
                "employee salary : "+sss.get(position).sal+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD")
                +"\n\n"+"num of discount :   "+numDis+"\n\n"+
                "discount value :   "+disVal+"\n\n"+
                "num of loans :   "+numloan+"\n\n"+
                "loans value :   "+disLoan+"\n\n"+
                "dates of sal payment :   "+datesOfSal
        );

        if(shared2.getString("language", "").equals("arabic")) {
            permissions.setText("الصلاحيات");


            String joptype=sss.get(position).jopType,type=sss.get(position).empType;
            String s1="",s2="";
            if (joptype.equals("admin")){
                s1="مدير";
            }else if(joptype.equals("casher")){
                s1="كاشير";
            }else if(joptype.equals("chef")){
                s1="شيف";
            }else if (joptype.equals("chef assistant")){
                s1="مساعد شيف";
            }else if(joptype.equals("driver")){
                s1="سائق";
            }else if (joptype.equals("floor worker")){
                s1="عامل صاله";
            }else if(joptype.equals("worker")){
                s1="عامل";
            }

            if (type.equals("ad")){
                s2="مدير";
            }else if(joptype.equals("emp")){
                s2="موظف";
            }else if(joptype.equals("dr")) {
                s2 = "سائق";
            }

            empInfo.setText("اسم الموظف :   "+sss.get(position).Fname+" "+sss.get(position).Lname
                    +"\n\n"+"الهاتف :   "+sss.get(position).empPhone
                    +"\n\n"+"العنوان : "+sss.get(position).address+"\n\n"
                    +"نوع الموظف :   "+s2
                    +"\n\n"+"المسمى الوظيفي : "+s1
                    +"\n\n"+"البريد الالكتروني :   "+sss.get(position).email+"\n\n"
                    +"تاريخ التوظيف :   "+sss.get(position).regisetDate
                    +"\n\n"+"الراتب :   "+sss.get(position).sal+" د.ا"+"\n\n"
                    +"\n\n"+"عدد الحسومات :   "+numDis+"\n\n"+
                    "قيمة الحسومات :   "+disVal+"\n\n"+
                    "عدد السلف :   "+numloan+"\n\n"+
                    "قيمة السلف :   "+disLoan+"\n\n"+
                    "تاريخ استحقاق الراتب :   "+datesOfSal);

        }

        permissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n =new Intent(getApplicationContext(),empPermission.class);
                employeeCreatInformation.emp=sss;
                n.putExtra("sition",position);
                n.putExtra("112",1);
                startActivity(n);
            }
        });

        dateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n =new Intent(getApplicationContext(),exitInformation.class);
                startActivity(n);
            }
        });

        ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
