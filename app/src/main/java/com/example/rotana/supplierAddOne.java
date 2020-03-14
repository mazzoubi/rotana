package com.example.rotana;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class supplierAddOne extends AppCompatActivity {

    EditText name, phone , email ;
    Button button;
    FirebaseFirestore db;
    SharedPreferences shared2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_add_one);
        init();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(supplierAddOne.this, "اسم المورد فارغ", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(supplierAddOne.this, "supplier name is empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (phone.getText().toString().isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(supplierAddOne.this, "رقم هاتف المورد فارغ", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(supplierAddOne.this, "phone is empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(email.getText().toString().isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(supplierAddOne.this, "البريد الالكتروني فارغ", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(supplierAddOne.this, "email is empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    upload();
                }
            }
        });

    }
    void init (){
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();
        button=findViewById(R.id.aaAdd);
        name=findViewById(R.id.aaItem);
        phone=findViewById(R.id.aaquantity);
        email=findViewById(R.id.aaqantitytype);
        if(shared2.getString("language", "").equals("arabic")) {
            button.setText("اضافة المورد");
            name.setHint("اسم المورد");
            phone.setHint("رقم الهاتف");
            email.setHint("البريد الالكتروني");
        }
    }
    void upload(){

        String id =System.currentTimeMillis()+"";
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", name.getText().toString());
        reservation.put("email", email.getText().toString());
        reservation.put("phone", phone.getText().toString());
        reservation.put("id", id);




        db.collection("Res_1_suppliers").document(id).set(reservation)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(shared2.getString("language", "").equals("arabic")) {
                                Toast.makeText(getApplicationContext(), "تمت العمليه بنجاح", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Operation Successful", Toast.LENGTH_SHORT).show();
                            onBackPressed();}
                        else{
                            if(shared2.getString("language", "").equals("arabic")) {
                                Toast.makeText(getApplicationContext(), "خطأ غير متوقع, الرجاء اعادة المحاوله", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Error, Please Try Again !", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}
