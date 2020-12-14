package com.example.rotanademo;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserRegister extends AppCompatActivity {

    EditText eemail,ppass, mobile, name;
    Button reg;

    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        shared = getSharedPreferences("lang", MODE_PRIVATE);

        eemail = findViewById(R.id.email);
        name = findViewById(R.id.name);
        ppass = findViewById(R.id.pass);
        mobile = findViewById(R.id.mobile);
        reg = findViewById(R.id.reg);

        if(shared.getString("language", "").equals("arabic")){

            TextView t = findViewById(R.id.textView2);
            t.setText("التسوق الالكتروني يجعل حياتنا اسهل");
            eemail.setHint("بريد الكتروني");
            name.setHint("الأسم");
            ppass.setHint("كلمة السر");
            mobile.setHint("هاتف");
            reg.setText("إنشاء حساب"); }

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!eemail.getText().toString().isEmpty() && !ppass.getText().toString().isEmpty()
                        && !mobile.getText().toString().isEmpty() && !name.getText().toString().isEmpty()){

                    final FirebaseAuth fire = FirebaseAuth.getInstance();
                    fire.createUserWithEmailAndPassword(eemail.getText().toString(), ppass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        String id = fire.getUid();

                                        Map<String, Object> map = new HashMap<>();
                                        map.put("name", name.getText().toString());
                                        map.put("email", eemail.getText().toString());
                                        map.put("mobile", mobile.getText().toString());
                                        map.put("pass", ppass.getText().toString());
                                        map.put("points", "0");

                                        db.collection("Res_1_Customer_Acc")
                                                .document(id).set(map)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> tas) {
                                                        if(tas.isSuccessful()){

                                                            Intent intent = new Intent(UserRegister.this, Profile.class);
                                                            startActivity(intent);
                                                            finish();

                                                        }
                                                    }
                                                });



                                    }
                                }
                            });

                }
                else
                    Toast.makeText(UserRegister.this, "some fields are empty !", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
