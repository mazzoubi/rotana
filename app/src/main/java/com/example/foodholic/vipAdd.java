package com.example.foodholic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class vipAdd extends AppCompatActivity {

    EditText name , email, phone;
    Button button;
    FirebaseFirestore db;
    SharedPreferences shared2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_add);
        init();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")){
                        Toast.makeText(vipAdd.this, "الاسم فارغ!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(vipAdd.this, "name is empty!", Toast.LENGTH_SHORT).show();
                    }

                }
                else if (phone.getText().toString().isEmpty()){

                    if(shared2.getString("language", "").equals("arabic")){
                        Toast.makeText(vipAdd.this, "رقم الهاتف فارغ!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(vipAdd.this, "phone is empty!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    upload();
                }
            }
        });
    }

    void init(){
        db =FirebaseFirestore.getInstance();
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone =findViewById(R.id.phone);
        button =findViewById(R.id.add);
        if(shared2.getString("language", "").equals("arabic")){
            name.setHint("اسم العميل");
            email.setHint("البريد الاكتروني");
            phone.setHint("رقم الهاتف");
            button.setText("اضافه");
        }

    }
    void upload(){
        final String id = System.currentTimeMillis()+"";
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", name.getText().toString());
        reservation.put("phone", phone.getText().toString());
        reservation.put("id", id);
        reservation.put("email", email.getText().toString()+"");
        reservation.put("desc", "");

        db.collection("Res_1_vipUsers").document(id).set(reservation)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(shared2.getString("language", "").equals("arabic")){
                                Toast.makeText(vipAdd.this, "تمت العمليه بنجاح", Toast.LENGTH_SHORT).show();
                                Intent n =new Intent(getApplicationContext(),addVipUserItem.class);
                                n.putExtra("name", name.getText().toString());
                                n.putExtra("phone",phone.getText().toString() );
                                n.putExtra("id", id);
                                n.putExtra("desc", "");
                                n.putExtra("email",email.getText().toString()+"" );
                                classVipUsers a=new classVipUsers();
                                a.desc="";
                                a.email=email.getText().toString()+"" ;
                                a.id=id;
                                a.name=name.getText().toString();
                                a.phone=phone.getText().toString();
                                vipActivity.vipUsers.add(a);
                                onBackPressed();
                                startActivity(n);
                            }
                            else{
                                Toast.makeText(vipAdd.this, "Operation Successful, Your add item", Toast.LENGTH_SHORT).show();
                                Intent n =new Intent(getApplicationContext(),addVipUserItem.class);
                                n.putExtra("name", name.getText().toString());
                                n.putExtra("phone",phone.getText().toString() );
                                n.putExtra("id", id);
                                n.putExtra("email",email.getText().toString()+"" );
                                onBackPressed();
                                startActivity(n);

                            }

                        }
                        else
                        if(shared2.getString("language", "").equals("arabic")){
                            Toast.makeText(vipAdd.this, "خطأ غير متوقع الرجاء اعادة المحاوله !", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(vipAdd.this, "Error, Please Try To add Again !", Toast.LENGTH_SHORT).show();

                    }
                });

    }

}
