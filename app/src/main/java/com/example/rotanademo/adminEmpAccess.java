package com.example.rotanademo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class adminEmpAccess extends AppCompatActivity {

    String myDatefrom=" / / ";
    DatePickerDialog.OnDateSetListener date_;
    TextView dateExit,datetext,ref,empInfo;
    Button button,permissions,discount,loans,salPay;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_admin_emp_access);

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
        
        button =findViewById(R.id.addexite);
        permissions=findViewById(R.id.addpermissions);
        loans=findViewById(R.id.loans);
        discount=findViewById(R.id.discount);
        salPay=findViewById(R.id.salPay);

        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        position=getIntent().getIntExtra("position",0);
        sal=employeeCreatInformation.emp.get(position).sal;
        String salDate="";
        salDate=employeeCreatInformation.emp.get(position).regisetDate;
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
                    if(a.empEmail.equals(employeeCreatInformation.emp.get(position).email)){
                        dis.add(a);
                        disVal+=a.value;
                        sal=sal-a.value;
                        numDis++;
                    }
                }
                if(shared2.getString("language", "").equals("arabic")) {
                    String joptype=employeeCreatInformation.emp.get(position).jopType,type=employeeCreatInformation.emp.get(position).empType;
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
                    empInfo.setText("اسم الموظف :   "+employeeCreatInformation.emp.get(position).Fname+" "+employeeCreatInformation.emp.get(position).Lname
                            +"\n\n"+"الهاتف :   "+employeeCreatInformation.emp.get(position).empPhone
                            +"\n\n"+"العنوان : "+employeeCreatInformation.emp.get(position).address+"\n\n"
                            +"نوع الموظف :   "+s2
                            +"\n\n"+"المسمى الوظيفي : "+s1
                            +"\n\n"+"البريد الالكتروني :   "+employeeCreatInformation.emp.get(position).email+"\n\n"
                            +"تاريخ التوظيف :   "+employeeCreatInformation.emp.get(position).regisetDate
                            +"\n\n"+"الراتب :   "+employeeCreatInformation.emp.get(position).sal+" د.ا"+"\n\n"
                            +"\n\n"+"عدد الحسومات :   "+numDis+"\n\n"+
                            "قيمة الحسومات :   "+disVal+"\n\n"+
                            "عدد السلف :   "+numloan+"\n\n"+
                            "قيمة السلف :   "+disLoan+"\n\n"+
                            "الراتب بعد الحسم والسلف :   "+sal+" د.ا"+"\n\n"+
                            "تاريخ استحقاق الراتب :   "+datesOfSal);

                }
                else {
                    empInfo.setText("employee name :   "+employeeCreatInformation.emp.get(position).Fname+" "+employeeCreatInformation.emp.get(position).Lname
                            +"\n\n"+"employee phone :   "+employeeCreatInformation.emp.get(position).empPhone
                            +"\n\n"+"employee address : "+employeeCreatInformation.emp.get(position).address+"\n\n"
                            +"employee type : "+employeeCreatInformation.emp.get(position).empType
                            +"\n\n"+"jop type : "+employeeCreatInformation.emp.get(position).jopType
                            +"\n\n"+"employee email :   "+employeeCreatInformation.emp.get(position).email+"\n\n"
                            +"employee register date :  "+employeeCreatInformation.emp.get(position).regisetDate+"\n\n"+
                            "employee salary : "+employeeCreatInformation.emp.get(position).sal+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD")
                            +"\n\n"+"num of discount :   "+numDis+"\n\n"+
                            "discount value :   "+disVal+"\n\n"+
                            "num of loans :   "+numloan+"\n\n"+
                            "loans value :   "+disLoan+"\n\n"+
                            "salary after discount & load :   "+sal+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD")+"\n\n"+
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
                    if(a.empEmail.equals(employeeCreatInformation.emp.get(position).email)){
                        loan.add(a);
                        disLoan+=a.loan;
                        sal=sal-a.repyment;
                        numloan++;
                    }
                }
                if(shared2.getString("language", "").equals("arabic")) {
                    String joptype=employeeCreatInformation.emp.get(position).jopType,type=employeeCreatInformation.emp.get(position).empType;
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
                    empInfo.setText("اسم الموظف :   "+employeeCreatInformation.emp.get(position).Fname+" "+employeeCreatInformation.emp.get(position).Lname
                            +"\n\n"+"الهاتف :   "+employeeCreatInformation.emp.get(position).empPhone
                            +"\n\n"+"العنوان : "+employeeCreatInformation.emp.get(position).address+"\n\n"
                            +"نوع الموظف :   "+s2
                            +"\n\n"+"المسمى الوظيفي : "+s1
                            +"\n\n"+"البريد الالكتروني :   "+employeeCreatInformation.emp.get(position).email+"\n\n"
                            +"تاريخ التوظيف :   "+employeeCreatInformation.emp.get(position).regisetDate
                            +"\n\n"+"الراتب :   "+employeeCreatInformation.emp.get(position).sal+" د.ا"+"\n\n"
                            +"\n\n"+"عدد الحسومات :   "+numDis+"\n\n"+
                            "قيمة الحسومات :   "+disVal+"\n\n"+
                            "عدد السلف :   "+numloan+"\n\n"+
                            "قيمة السلف :   "+disLoan+"\n\n"+
                            "الراتب بعد الحسم والسلف :   "+sal+" د.ا"+"\n\n"+
                            "تاريخ استحقاق الراتب :   "+datesOfSal);

                }
                else {
                    empInfo.setText("employee name :   "+employeeCreatInformation.emp.get(position).Fname+" "+employeeCreatInformation.emp.get(position).Lname
                            +"\n\n"+"employee phone :   "+employeeCreatInformation.emp.get(position).empPhone
                            +"\n\n"+"employee address : "+employeeCreatInformation.emp.get(position).address+"\n\n"
                            +"employee type : "+employeeCreatInformation.emp.get(position).empType
                            +"\n\n"+"jop type : "+employeeCreatInformation.emp.get(position).jopType
                            +"\n\n"+"employee email :   "+employeeCreatInformation.emp.get(position).email+"\n\n"
                            +"employee register date :  "+employeeCreatInformation.emp.get(position).regisetDate+"\n\n"+
                            "employee salary : "+employeeCreatInformation.emp.get(position).sal+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD")
                            +"\n\n"+"num of discount :   "+numDis+"\n\n"+
                            "discount value :   "+disVal+"\n\n"+
                            "num of loans :   "+numloan+"\n\n"+
                            "loans value :   "+disLoan+"\n\n"+
                            "salary after discount & load :   "+sal+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD")+"\n\n"+
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
                    if (a.email.equals(employeeCreatInformation.emp.get(position).email)&&myDatefrom.equals(s[1]+"-"+s[2])){
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


        empInfo.setText("employee name :   "+employeeCreatInformation.emp.get(position).Fname+" "+employeeCreatInformation.emp.get(position).Lname
                +"\n\n"+"employee phone :   "+employeeCreatInformation.emp.get(position).empPhone
                +"\n\n"+"employee address : "+employeeCreatInformation.emp.get(position).address+"\n\n"
                +"employee type : "+employeeCreatInformation.emp.get(position).empType
                +"\n\n"+"jop type : "+employeeCreatInformation.emp.get(position).jopType
                +"\n\n"+"employee email :   "+employeeCreatInformation.emp.get(position).email+"\n\n"
                +"employee register date :  "+employeeCreatInformation.emp.get(position).regisetDate+"\n\n"+
                "employee salary : "+employeeCreatInformation.emp.get(position).sal+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD")
                +"\n\n"+"num of discount :   "+numDis+"\n\n"+
                "discount value :   "+disVal+"\n\n"+
                "num of loans :   "+numloan+"\n\n"+
                "loans value :   "+disLoan+"\n\n"+
                "dates of sal payment :   "+datesOfSal
        );

        if(shared2.getString("language", "").equals("arabic")) {
            button.setText("اضافة اجازه");
            permissions.setText("الصلاحيات");
            loans.setText("السلف");
            discount.setText("الحسومات");
            salPay.setText("دفع الراتب");

            String joptype=employeeCreatInformation.emp.get(position).jopType,type=employeeCreatInformation.emp.get(position).empType;
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

            empInfo.setText("اسم الموظف :   "+employeeCreatInformation.emp.get(position).Fname+" "+employeeCreatInformation.emp.get(position).Lname
                    +"\n\n"+"الهاتف :   "+employeeCreatInformation.emp.get(position).empPhone
                    +"\n\n"+"العنوان : "+employeeCreatInformation.emp.get(position).address+"\n\n"
                    +"نوع الموظف :   "+s2
                    +"\n\n"+"المسمى الوظيفي : "+s1
                    +"\n\n"+"البريد الالكتروني :   "+employeeCreatInformation.emp.get(position).email+"\n\n"
                    +"تاريخ التوظيف :   "+employeeCreatInformation.emp.get(position).regisetDate
                    +"\n\n"+"الراتب :   "+employeeCreatInformation.emp.get(position).sal+" د.ا"+"\n\n"
                    +"\n\n"+"عدد الحسومات :   "+numDis+"\n\n"+
                    "قيمة الحسومات :   "+disVal+"\n\n"+
                    "عدد السلف :   "+numloan+"\n\n"+
                    "قيمة السلف :   "+disLoan+"\n\n"+
                    "تاريخ استحقاق الراتب :   "+datesOfSal);

        }

        salPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<dis.size();i++){
                    if (dis.get(i).type==2){
                        db.collection("Res_1_salDiscount").document(dis.get(i).disId).delete();
                    }
                }
                try{
                    double disss=0,lo=loan.get(0).loan,py=loan.get(0).repyment,loanREsult=0;
                    loanREsult=lo-py;
                    if(loanREsult<=0){
                        db.collection("Res_1_loans").document(loan.get(0).loanId).delete();
                    }
                    else {
                        Map<String, Object> reservation = new HashMap<>();
                        reservation.put("totelLoan", loan.get(0).totelLoan);
                        reservation.put("loan", loanREsult);
                        reservation.put("repyment", loan.get(0).repyment);
                        reservation.put("empEmail", loan.get(0).empEmail);
                        reservation.put("loanId", loan.get(0).loanId);



                        db.collection("Res_1_loans").document(loan.get(0).loanId).set(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){

                                }
                            }
                        });
                    }

                    String dr=new Date()+"";
                    String []time=dr.split(" ");
                    Map<String, Object> reservation = new HashMap<>();
                    reservation.put("date", date);
                    reservation.put("pay", sal);
                    reservation.put("description", "راتب salary");
                    reservation.put("time", time[3]);
                    reservation.put("emp", Adminpage.email);


                    db.collection("Res_1_payment").document(""+new Date()).set(reservation)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //     progressBar.setVisibility(View.GONE);
                                    if(task.isSuccessful()){
                                        if(shared2.getString("language", "").equals("arabic")) {
                                            Toast.makeText(getApplicationContext(), "تم دفع الراتب وعمليات الخصم بنجاح", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Operation Successful", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(), "Error, Please Try Again !", Toast.LENGTH_SHORT).show();
                                }
                            });
                }catch (Exception e){
                    String dr=new Date()+"";
                    String []time=dr.split(" ");
                    Map<String, Object> reservation = new HashMap<>();
                    reservation.put("date", date);
                    reservation.put("pay", sal);
                    reservation.put("description", "راتب salary");
                    reservation.put("time", time[3]);
                    reservation.put("emp", Adminpage.email);


                    db.collection("Res_1_payment").document(""+new Date()).set(reservation)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //     progressBar.setVisibility(View.GONE);
                                    if(task.isSuccessful()){
                                        if(shared2.getString("language", "").equals("arabic")) {
                                            Toast.makeText(getApplicationContext(), "تم دفع الراتب وعمليات الخصم بنجاح", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Operation Successful", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(), "Error, Please Try Again !", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        loans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n=new Intent(getApplicationContext(),employeeLoans.class);
                n.putExtra("pos",position);
                startActivity(n);
            }
        });
        discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n=new Intent(getApplicationContext(),employeeSalaryDiscount.class);
                n.putExtra("pos",position);
                startActivity(n);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n =new Intent(getApplicationContext(), exit.class);
                n.putExtra("pos",position);
                startActivity(n);
            }
        });

        permissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n =new Intent(getApplicationContext(),empPermission.class);
                n.putExtra("sition",position);
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
                Intent n =new Intent(getApplicationContext(), adminEmpAccess.class);
                n.putExtra("position",position);
                onBackPressed();
                startActivity(n);
            }
        });

    }
}
