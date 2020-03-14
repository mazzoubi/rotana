package com.example.rotana;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class employeeAdd extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    Button button;
    EditText email,pass , fname,lname,phone,address,sal;
    Spinner spinner,spinnerJop;

    String type="",joptype="";
    SharedPreferences shared2;

    String date="";
    classEmployee emp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_add);

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        shared2 = getSharedPreferences("lang", MODE_PRIVATE);

        Date a=new Date();
        String aa=a+"";
        String[] aaa=aa.split(" ");
        String day =(a.getDay()+1)+"";
        String month =(a.getMonth()+1)+"";
         date=day+"-"+month+"-"+aaa[5];


        button =(Button)findViewById(R.id.addPayment);
        email =(EditText)findViewById(R.id.emailE);
        pass =(EditText)findViewById(R.id.passE);

        address =(EditText)findViewById(R.id.address);
        sal =(EditText)findViewById(R.id.salary);
        fname =(EditText)findViewById(R.id.fName);
        lname =(EditText)findViewById(R.id.lName);
        phone =(EditText)findViewById(R.id.phoneN);
        spinner=(Spinner)findViewById(R.id.spinner_time);
        spinnerJop=(Spinner)findViewById(R.id.spinner_time2);
        // final ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressBar3);




        if(shared2.getString("language", "").equals("arabic")) {



            button.setText("تم");
            email.setHint("اسم المستخدم");
            pass.setHint("كلمة المرور");

            address.setHint("عنوان الموظف");
            sal.setHint("الراتب د.أ");
            fname.setHint("الاسم الاول");
            lname.setHint("الاسم الاخير");
            phone.setHint("رقم الهاتف");

            ArrayList<String> arrayList=new ArrayList<>();
            arrayList.add("نوع الموظف");
            arrayList.add("مدير");
            arrayList.add("موظف");
            arrayList.add("سائق");
            arrayList.add("كابتن اوردر");
            ArrayAdapter<String>adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList);
            spinner.setAdapter(adapter);


            ArrayList<String> arrayList1=new ArrayList<>();
            arrayList1.add("الاسم الوظيفي");
            arrayList1.add("مدير");
            arrayList1.add("كاشير");
            arrayList1.add("شيف");
            arrayList1.add("مساعد شيف");
            arrayList1.add("سائق");
            arrayList1.add("عامل صاله");
            arrayList1.add("عامل");


            ArrayAdapter<String>adapter1=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList1);
            spinnerJop.setAdapter(adapter1);
        }


        db=FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        spinnerJop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        joptype="admin";
                    break;

                    case 2:
                        joptype="casher";
                    break;

                    case 3:
                        joptype="chef";
                    break;

                    case 4:
                        joptype="chef assistant";
                    break;

                    case 5:
                        joptype="driver";
                    break;

                    case 6:
                        joptype="floor worker";
                    break;

                    case 7:
                        joptype="worker";
                    break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        type="ad";
                        break;
                    case 2:
                        type="emp";
                        break;
                    case 3:
                        type="dr";
                        break;
                    case 4:
                        type="cap";
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        emp=new classEmployee();

        button .setOnClickListener(new View.OnClickListener() {
            String emaill = "";
            @Override
            public void onClick(View v) {

                try {

                    emp.regisetDate=date;

                    emp.empType=type;
                    emp.jopType=joptype;
                    emp.Fname=fname.getText().toString();
                    emp.Lname=lname.getText().toString();
                    emp.empPhone=phone.getText().toString();
                    emp.address=address.getText().toString();
                    emp.sal=Double.parseDouble(sal.getText().toString());



                    String e = email.getText().toString();
                    String p = pass.getText().toString();


                    if (type.equals("ad")) {
                        emaill = e+".ad@gmail.com" ;
                        emp.email=emaill;
                    } else if (type.equals("emp")) {
                        emaill = e+ ".emp@gmail.com" ;
                        emp.email=emaill;
                    } else if (type.equals("dr")) {
                        emaill = e+ ".del@gmail.com" ;
                        emp.email=emaill;
                    }else if (type.equals("cap")) {
                        emaill = e+ ".cap@gmail.com" ;
                        emp.email=emaill;
                    }


                    if (type.equals("")) {
                        if(shared2.getString("language", "").equals("arabic")) {
                            Toast.makeText(getApplicationContext(), "الرجاء اختيار نوع الموظف", Toast.LENGTH_LONG).show();
                        }
                        else Toast.makeText(getApplicationContext(), "please choose the employee type", Toast.LENGTH_LONG).show();
                    } if (joptype.equals("")) {
                        if(shared2.getString("language", "").equals("arabic")) {
                            Toast.makeText(getApplicationContext(), "الرجاء اختيار المسمى الوظيفي", Toast.LENGTH_LONG).show();
                        }
                        else Toast.makeText(getApplicationContext(), "please choose the jop type", Toast.LENGTH_LONG).show();
                    } else if (e.isEmpty()||(!e.matches("[a-z]*"))) {
                        if(shared2.getString("language", "").equals("arabic")) {
                            Toast.makeText(getApplicationContext(), "خطأ في ادخال اسم المستخدم", Toast.LENGTH_LONG).show();
                        }
                        else Toast.makeText(getApplicationContext(), "wrong in the user name ", Toast.LENGTH_LONG).show();
                    } else if (p.isEmpty()) {
                        if(shared2.getString("language", "").equals("arabic")) {
                            Toast.makeText(getApplicationContext(), "الرجاء ادخال كلمة المرور", Toast.LENGTH_LONG).show();
                        }
                        else Toast.makeText(getApplicationContext(), "please enter the password", Toast.LENGTH_LONG).show();
                    } else {


                        mAuth.createUserWithEmailAndPassword(emaill, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if (task.isSuccessful()) {
                                    if(shared2.getString("language", "").equals("arabic")) {
                                        Toast.makeText(getApplicationContext(), "تم التسجيل", Toast.LENGTH_LONG).show();
                                    }
                                    else Toast.makeText(getApplicationContext(), "successful register", Toast.LENGTH_LONG).show();

                                    Map<String, Object> reservation = new HashMap<>();
                                    reservation.put("Fname", emp.Fname);
                                    reservation.put("Lname", emp.Lname);
                                    reservation.put("regisetDate", emp.regisetDate);
                                    reservation.put("email", emp.email);
                                    reservation.put("empType", emp.empType);
                                    reservation.put("address", emp.address);
                                    reservation.put("sal", emp.sal);
                                    reservation.put("empPhone", emp.empPhone); //
                                    reservation.put("jopType", emp.jopType);
                                    reservation.put("warehousea", false);
                                    reservation.put("onlineReservation", false);
                                    reservation.put("tableResev", false);
                                    reservation.put("takeAway", false);
                                    reservation.put("delevery", false);
                                    reservation.put("addpayment", false);
                                    reservation.put("cashWork", false);
                                    reservation.put("empExt", false);

                                    db.collection("Res_1_employee").document(emaill).set(reservation)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){

                                                        if(shared2.getString("language", "").equals("arabic")) {

                                                            AlertDialog.Builder builder=new AlertDialog.Builder(employeeAdd.this)
                                                                    .setMessage("هل تريد اضافة الصلاحيات الان؟")
                                                                    .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                            Intent n=new Intent(getApplicationContext(),empPermission.class);
                                                                            n.putExtra("Fname", emp.Fname);
                                                                            n.putExtra("Lname", emp.Lname);
                                                                            n.putExtra("address", emp.address);
                                                                            n.putExtra("regisetDate", emp.regisetDate);
                                                                            n.putExtra("email", emp.email);
                                                                            n.putExtra("empType", emp.empType);
                                                                            n.putExtra("sal", emp.sal);
                                                                            n.putExtra("empPhone", emp.empPhone); //
                                                                            n.putExtra("jopType", emp.jopType); //
                                                                            n.putExtra("test",1);
                                                                            n.putExtra("warehousea", false);
                                                                            n.putExtra("onlineReservation", false);
                                                                            n.putExtra("tableResev", false);
                                                                            n.putExtra("takeAway", false);
                                                                            n.putExtra("delevery", false);
                                                                            n.putExtra("addpayment", false);
                                                                            n.putExtra("cashWork", false);
                                                                            n.putExtra("empExt", false);

                                                                            startActivity(n);

                                                                        }
                                                                    })
                                                                    .setNegativeButton("ليس الان..", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                        }
                                                                    });
                                                            AlertDialog alert=builder.create();
                                                            alert.setTitle("اضافة الصلاحيات... ");
                                                            alert.show();
                                                            ///////////////////////////////////////////////////////////////
                                                            /////////////////////////////////////////////////////////////////
                                                        }
                                                        else {
                                                            AlertDialog.Builder builder=new AlertDialog.Builder(employeeAdd.this)
                                                                    .setMessage("do you want add permissions now?")
                                                                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                            Intent n=new Intent(getApplicationContext(),empPermission.class);
                                                                            n.putExtra("email", emp.email);
                                                                            n.putExtra("warehousea", false);
                                                                            n.putExtra("onlineReservation", false);
                                                                            n.putExtra("tableResev", false);
                                                                            n.putExtra("takeAway", false);
                                                                            n.putExtra("delevery", false);
                                                                            n.putExtra("addpayment", false);
                                                                            n.putExtra("cashWork", false);
                                                                            n.putExtra("empExt", false);
                                                                            startActivity(n);

                                                                        }
                                                                    })
                                                                    .setNegativeButton("not now..", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                        }
                                                                    });
                                                            AlertDialog alert=builder.create();
                                                            alert.setTitle("add permissions... ");
                                                            alert.show();
                                                        }

                                                    }
                                                    else;

                                                }
                                            });

                                } else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                    }
                }catch (Exception e){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(getApplicationContext(), "اسم مستخدم غير صحيح الرجاء المحاوله مره اخرى", Toast.LENGTH_LONG).show();
                    }
                    else Toast.makeText(getApplicationContext(), "incorrect e-mail please return in another e-mail", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
