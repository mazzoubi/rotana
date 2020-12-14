package com.example.rotanademo;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class vipInfo extends AppCompatActivity {
    SharedPreferences shared2;
    public static TextView tname,temail,tdesc;
    public static EditText tphone;
    Button button;

    String name="",phone="",email="",desc="",id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_vip_info);
        init();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tphone.getText().toString().isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")){
                        Toast.makeText(vipInfo.this, "رقم الهاتف فارغ", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(vipInfo.this, "phone number is empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Intent n =new Intent(getApplicationContext(),addVipUserItem.class);
                    n.putExtra("name", name);
                    n.putExtra("phone",tphone.getText().toString());
                    n.putExtra("id", id);
                    n.putExtra("desc", desc);
                    n.putExtra("email",email );
                    startActivity(n);
                }

            }
        });
    }
    void init(){
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        tname=findViewById(R.id.name);
        temail=findViewById(R.id.email);
        tdesc=findViewById(R.id.desc);
        tphone=findViewById(R.id.phone);
        button=findViewById(R.id.button);

        name = vipActivity.users.name;
        phone= vipActivity.users.phone;
        email=vipActivity.users.email;
        id=vipActivity.users.id;
        desc=vipActivity.users.desc;

        tname.setText(name);
        temail.setText(email);
        tdesc.setText(desc);
        tphone.setText(phone);

        if(shared2.getString("language", "").equals("arabic")){
            button.setText("تعديل");
        }
    }
}
