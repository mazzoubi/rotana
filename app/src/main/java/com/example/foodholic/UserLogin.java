package com.example.foodholic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserLogin extends AppCompatActivity {

    EditText eemail,ppass;
    Button llogin;
    CheckBox ch;

    SharedPreferences shared;
    SharedPreferences shared2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        shared = getSharedPreferences("lang", MODE_PRIVATE);
        shared2 = getSharedPreferences("stay", MODE_PRIVATE);

        eemail = findViewById(R.id.email);
        ppass = findViewById(R.id.pass);
        llogin = findViewById(R.id.loginn);
        ch = findViewById(R.id.check);

        if(shared.getString("language", "").equals("arabic")){

            TextView t = findViewById(R.id.textView2);
            t.setText("طعم يجب عليك ان تجربه");
            eemail.setHint("بريد الكتروني");
            ppass.setHint("كلمة السر");
            llogin.setText("تسجيل دخول");
            ch.setText("البقاء قيد الدخول"); }

        llogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!eemail.getText().toString().isEmpty() && !ppass.getText().toString().isEmpty()){

                    FirebaseAuth fire = FirebaseAuth.getInstance();
                    fire.signInWithEmailAndPassword(eemail.getText().toString(), ppass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        if(ch.isChecked()){
                                            SharedPreferences.Editor editor = shared2.edit();
                                            editor.putBoolean("log?", true);
                                            editor.putBoolean("in?", true);
                                            editor.apply();
                                        }

                                        Toast.makeText(UserLogin.this, "Account created, please login !", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(UserLogin.this, Profile.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                            });

                }
                else
                    Toast.makeText(UserLogin.this, "some fields are empty !", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
