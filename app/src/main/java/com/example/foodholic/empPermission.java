
package com.example.foodholic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class empPermission extends AppCompatActivity {
    CheckBox warehousea,onlineReservation,tableResev,takeAway,delevery,addpayment,cashWork,empExt;
    classEmployee emp;
    classEmployee ee;
    SharedPreferences shared2;
    FirebaseFirestore db;
    Button button;
    TextView textView;
    int position;
    int test=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_permission);

        shared2 = getSharedPreferences("lang", MODE_PRIVATE);


        test=getIntent().getIntExtra("test",0);



        db=FirebaseFirestore.getInstance();
        textView=findViewById(R.id.textView20);
        button=findViewById(R.id.persave);
        warehousea=findViewById(R.id.wareHOUSE1);
        onlineReservation=findViewById(R.id.onlineRESEV1);
        tableResev=findViewById(R.id.tableRESEV1);
        takeAway=findViewById(R.id.takeAWAY1);
        delevery=findViewById(R.id.delEVARY1);
        addpayment=findViewById(R.id.addPAYMENT1);
        cashWork=findViewById(R.id.cashWORK1);
        empExt=findViewById(R.id.empEXT1);



        if(shared2.getString("language", "").equals("arabic")) {
            textView.setText("صلاحيات الموظف");
            warehousea.setText("صلاحيات مستودع");
            onlineReservation.setText("حجوزات اونلاين");
            tableResev.setText("حجوزات الطاولات");
            takeAway.setText("حجوزات سفري مسبقه");
            delevery.setText("حجوزات الديلفري");
            addpayment.setText("اظافة وسحب التكاليف");
            cashWork.setText("عامل كاش");
            empExt.setText("اضافة الاجازات");
            button.setText("حفظ");

        }

        if(test==1){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ee=new classEmployee();
                    ee.email=getIntent().getStringExtra("email");
                    ee.warehousea=getIntent().getBooleanExtra("warehousea", false);
                    ee.onlineReservation=getIntent().getBooleanExtra("onlineReservation", false);
                    ee.tableResev=getIntent().getBooleanExtra("tableResev", false);
                    ee.takeAway=getIntent().getBooleanExtra("takeAway", false);
                    ee.delevery=getIntent().getBooleanExtra("delevery", false);
                    ee.addpayment=getIntent().getBooleanExtra("addpayment", false);
                    ee.cashWork=getIntent().getBooleanExtra("cashWork", false);
                    ee.empExt=getIntent().getBooleanExtra("empExt", false);
                    ee.Fname=getIntent().getStringExtra("Fname");
                    ee.Lname=getIntent().getStringExtra("Lname");
                    ee.regisetDate=getIntent().getStringExtra("regisetDate");
                    ee.empType=getIntent().getStringExtra("empType");
                    ee.sal=getIntent().getDoubleExtra("sal",0);
                    ee.empPhone=getIntent().getStringExtra("empPhone");
                    ee.address=getIntent().getStringExtra("address");
                    ee.jopType=getIntent().getStringExtra("jopType");
                    Map<String, Object> reservation = new HashMap<>();
                    reservation.put("Fname", ee.Fname);
                    reservation.put("Lname", ee.Lname);
                    reservation.put("regisetDate", ee.regisetDate);
                    reservation.put("email", ee.email);
                    reservation.put("empType", ee.empType);
                    reservation.put("empPhone", ee.empPhone); //
                    reservation.put("sal",ee.sal);
                    reservation.put("address",ee.address);
                    reservation.put("jopType", ee.jopType);
                    reservation.put("warehousea", warehousea.isChecked());
                    reservation.put("onlineReservation", onlineReservation.isChecked());
                    reservation.put("tableResev", tableResev.isChecked());
                    reservation.put("takeAway", takeAway.isChecked());
                    reservation.put("delevery", delevery.isChecked());
                    reservation.put("addpayment", addpayment.isChecked());
                    reservation.put("cashWork", cashWork.isChecked());
                    reservation.put("empExt", empExt.isChecked());

                    db.collection("Res_1_employee").document(ee.email).set(reservation)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        if (HomeAct.lang==1){
                                            Toast.makeText(getApplicationContext(),"تم الحفظ بنجاح ",Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(),"the saved is successful ",Toast.LENGTH_LONG).show();
                                        }

                                        onBackPressed();
                                        Intent n =new Intent(getApplicationContext(),Adminpage.class);
                                        startActivity(n);
                                    }
                                    else;

                                }
                            });
                }
            });
        }
        else {
            position =getIntent().getIntExtra("sition",0);

            emp =employeeCreatInformation.emp.get(position);
            warehousea.setChecked(emp.warehousea);
            onlineReservation.setChecked(emp.onlineReservation);
            tableResev.setChecked(emp.tableResev);
            takeAway.setChecked(emp.takeAway);
            delevery.setChecked(emp.delevery);
            addpayment.setChecked(emp.addpayment);
            cashWork.setChecked(emp.cashWork);
            empExt.setChecked(emp.empExt);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Map<String, Object> reservation = new HashMap<>();
                    reservation.put("Fname", emp.Fname);
                    reservation.put("Lname", emp.Lname);
                    reservation.put("regisetDate", emp.regisetDate);
                    reservation.put("email", emp.email);
                    reservation.put("empType", emp.empType);
                    reservation.put("empPhone", emp.empPhone); //
                    reservation.put("sal",emp.sal);
                    reservation.put("address",emp.address);
                    reservation.put("jopType", emp.jopType);
                    reservation.put("warehousea", warehousea.isChecked());
                    reservation.put("onlineReservation", onlineReservation.isChecked());
                    reservation.put("tableResev", tableResev.isChecked());
                    reservation.put("takeAway", takeAway.isChecked());
                    reservation.put("delevery", delevery.isChecked());
                    reservation.put("addpayment", addpayment.isChecked());
                    reservation.put("cashWork", cashWork.isChecked());
                    reservation.put("empExt", empExt.isChecked());

                    db.collection("Res_1_employee").document(emp.email).set(reservation)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        if(shared2.getString("language", "").equals("arabic")) {
                                            Toast.makeText(getApplicationContext(), "تم الحفظ بنجاح", Toast.LENGTH_LONG).show();
                                            onBackPressed();
                                            Intent n = new Intent(getApplicationContext(), Adminpage.class);
                                            startActivity(n);
                                        }else{
                                            Toast.makeText(getApplicationContext(), "the saved is successful ", Toast.LENGTH_LONG).show();
                                            onBackPressed();
                                            Intent n = new Intent(getApplicationContext(), Adminpage.class);
                                            startActivity(n);
                                        }
                                    }
                                    else;

                                }
                            });
                }
            });
        }


    }
}
