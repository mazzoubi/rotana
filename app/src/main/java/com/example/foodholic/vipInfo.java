package com.example.foodholic;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class vipInfo extends AppCompatActivity {
    SharedPreferences shared2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_info);
    }
    void init(){
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);

        if(shared2.getString("language", "").equals("arabic")){

        }
    }
}
