package com.example.foodholic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Contact extends AppCompatActivity {

    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        try{
            if(shared.getString("language", "").equals("arabic"))
                setContentView(R.layout.activity_contact2);
            else
                setContentView(R.layout.activity_contact);
        }catch (Exception e){
            setContentView(R.layout.activity_contact2);
        }

        shared = getSharedPreferences("lang", MODE_PRIVATE);

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button rest = findViewById(R.id.rest);
        Button mng = findViewById(R.id.mng);
        Button tch = findViewById(R.id.tch);

        rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "0788166999", null)));
            } });

        mng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "0772044546", null)));
            } });

        tch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "0792942040", null)));
            } });

    }
}
