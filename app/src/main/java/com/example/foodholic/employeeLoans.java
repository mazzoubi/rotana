package com.example.foodholic;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class employeeLoans extends AppCompatActivity {

    FirebaseFirestore db;
    TextView dash,title,ll;
    Button add;
    EditText value,repayment;
    int pos=0;
    classEmployee emp;
    SharedPreferences shared2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_employee_loans);

        db=FirebaseFirestore.getInstance();
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        dash=findViewById(R.id.loanDash);
        title=findViewById(R.id.textView39);
        ll=findViewById(R.id.textView41);
        add=findViewById(R.id.button_addloan);
        value=findViewById(R.id.loanValue);
        repayment=findViewById(R.id.loanRepayment);

        if (adminEmpAccess.loan.isEmpty()){
            dash.setText("this employee do not have\n any loans ");
        }
        else {
            dash.setText("total loan          : "+adminEmpAccess.loan.get(0).totelLoan+"\n\n"+
                    "remaining amount    : "+adminEmpAccess.loan.get(0).loan+"\n\n"+
                    "repayment           : "+adminEmpAccess.loan.get(0).repyment+"\n\n"+
                    "------------------------------");
        }

        if(shared2.getString("language", "").equals("arabic")) {
            title.setText("صفحة السلف");
            ll.setText("اضافة سلفه");
            add.setText("اضافه");
            value.setHint("قيمة القرض");
            repayment.setHint("الدفعات الشهريه");

            if (adminEmpAccess.loan.isEmpty()){
                dash.setText("هذا الموظف لا يملك\n اي سلف حالياً ");
            }
            else {
                dash.setText("قيمة السلفه          : "+adminEmpAccess.loan.get(0).totelLoan+"\n\n"+
                        "المبلغ المتبقي       : "+adminEmpAccess.loan.get(0).loan+"\n\n"+
                        "الدفعات الشهريه      : "+adminEmpAccess.loan.get(0).repyment+"\n\n"+
                        "------------------------------");
            }
        }
        pos=getIntent().getIntExtra("pos",0);
        emp=employeeCreatInformation.emp.get(pos);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!adminEmpAccess.loan.isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(getApplicationContext(),"هذا الموظف لديه سلفه حالياً, لا يمكن اكمال الاجراء .....",Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(),"this emp have a loan you cant compleat.....",Toast.LENGTH_LONG).show();
                }
                else if (value.getText().toString().isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(getApplicationContext(),"حقل قيمة السلفه فارغ",Toast.LENGTH_LONG).show();
                    }
                    else Toast.makeText(getApplicationContext(),"value field is empty ",Toast.LENGTH_LONG).show();
                }
                else if (repayment.getText().toString().isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(getApplicationContext(),"حقل قيمة السداد الشهريه فارغ",Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(),"repayment field is empty",Toast.LENGTH_LONG).show();
                }
                else {
                    Map<String, Object> reservation = new HashMap<>();
                    String id=new Date()+"";
                    reservation.put("totelLoan", Double.parseDouble(value.getText().toString()));
                    reservation.put("loan", Double.parseDouble(value.getText().toString()));
                    reservation.put("repyment", Double.parseDouble(repayment.getText().toString()));
                    reservation.put("empEmail", emp.email);
                    reservation.put("loanId", id);



                    db.collection("Res_1_loans").document(id).set(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                if(shared2.getString("language", "").equals("arabic")) {
                                    Toast.makeText(getApplicationContext(),"لقد تمت اضافة السلفه بنجاح ..",Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"successful loan add ..",Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                }
                            }
                        }
                    });
                }
            }
        });




    }
}
