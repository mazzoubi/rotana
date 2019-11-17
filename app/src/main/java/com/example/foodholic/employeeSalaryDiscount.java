package com.example.foodholic;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class employeeSalaryDiscount extends AppCompatActivity {

    FirebaseFirestore db;

    Spinner spinner;
    TextView title,result,tt;
    Button add;
    EditText value,desc;
    SharedPreferences shared2;
    int selected=0;
    int pos=0;
    String resultText="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_salary_discount);

        db=FirebaseFirestore.getInstance();
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        spinner =findViewById(R.id.disSpinner);
        title=findViewById(R.id.disTitle);
        tt=findViewById(R.id.textView44);
        result=findViewById(R.id.result);
        add=findViewById(R.id.disButton);
        value=findViewById(R.id.disValue);
        desc=findViewById(R.id.disDescription);

        if (adminEmpAccess.dis.isEmpty()){
            result.setText("this employee do not have\nany salary discount ......\n\n\n\n\n\n\n ");
        }
        else{
            int count=0;
            for (int i=0;i<adminEmpAccess.dis.size();i++){
                resultText+=
                            "value of sal discount   : "+adminEmpAccess.dis.get(i).value+"\n\n"+
                            "description of discount : "+adminEmpAccess.dis.get(i).description+"\n"+
                            "------------------------------------\n";
                count+=adminEmpAccess.dis.get(i).value;
            }
            resultText+="number of sal discount  : "+adminEmpAccess.dis.size()+"\n\n";
            resultText+="total discount : "+count +" JOD\n";
            result.setText(resultText);
        }
        pos=getIntent().getIntExtra("pos",0);
        String [] sp={"discount type","always","one time"};
        String [] spAr={"نوع الحسم","دائم","لمره واحده"};
        ArrayAdapter<String>adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,sp);
        spinner.setAdapter(adapter);

        if(shared2.getString("language", "").equals("arabic")) {
            if (adminEmpAccess.dis.isEmpty()){
                result.setText("هذا الموظف لايملك \nحسومات على الراتب ......\n\n\n\n\n\n\n ");
            }
            else{
                int count=0;
                for (int i=0;i<adminEmpAccess.dis.size();i++){
                    resultText="";
                    resultText+=
                            "قيمة الحسم : "+adminEmpAccess.dis.get(i).value+"\n\n"+
                            "وصف الحسم  : "+adminEmpAccess.dis.get(i).description+"\n"+
                            "--------------------------------\n";
                    count+=adminEmpAccess.dis.get(i).value;
                }
                resultText+="عدد الحسومات : "+adminEmpAccess.dis.size()+"\n\n";
                resultText+="القيمه الكليه للحسومات : "+count +" د.ا\n";
                result.setText(resultText);
            }
            adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,spAr);
            spinner.setAdapter(adapter);
            title.setText("حسومات الراتب");
            add.setText("اضافه");
            value.setHint("قيمة الحسم");
            desc.setHint("الوصف");
            tt.setText("اضافة حسم");
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        selected=0;
                        break;
                    case 1:

                        selected=1;
                        break;
                    case 2:

                        selected=2;
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value.getText().toString().isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(getApplicationContext(),"الرجاء ادخال قيمة الحسم",Toast.LENGTH_LONG).show();
                    }
                    else
                    Toast.makeText(getApplicationContext(),"please enter th value of descount ",Toast.LENGTH_LONG).show();
                }else if(desc.getText().toString().isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(getApplicationContext(),"الرجاء ادخال وصف الحسم",Toast.LENGTH_LONG).show();
                    }
                    else
                    Toast.makeText(getApplicationContext(),"please enter th description of descount",Toast.LENGTH_LONG).show();
                }else if (selected==0){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(getApplicationContext(),"الجاء اختيار نوع الحسم",Toast.LENGTH_LONG).show();
                    }
                    else
                    Toast.makeText(getApplicationContext(),"please choose th type of descount",Toast.LENGTH_LONG).show();
                }
                else{
                    Map<String, Object> reservation = new HashMap<>();
                    String id=new Date()+"";
                    reservation.put("description", desc.getText().toString());
                    reservation.put("disId", id);
                    reservation.put("type", selected);
                    reservation.put("value", Double.parseDouble(value.getText().toString()));
                    reservation.put("empEmail", employeeCreatInformation.emp.get(pos).email);



                    db.collection("Res_1_salDiscount").document(id).set(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                if(shared2.getString("language", "").equals("arabic")) {
                                    Toast.makeText(getApplicationContext(),"تم اضافة الحسم بنجاح ..",Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                }else {
                                Toast.makeText(getApplicationContext(),"successful salary discount add ..",Toast.LENGTH_LONG).show();
                                onBackPressed();}
                            }
                        }
                    });
                }
            }
        });

    }
}
