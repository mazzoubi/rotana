package com.example.foodholic;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class About extends AppCompatActivity {

    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        shared = getSharedPreferences("lang", MODE_PRIVATE);

        Toolbar bar = findViewById(R.id.tool);

        if(shared.getString("language", "").equals("arabic"))
            bar.setTitle("الرجوع");

        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView t = findViewById(R.id.title);
        t.setText("N.O.V.A Solutions is a Nested Operational Virtual Agency, we are a tech company that specializes in software developing, but our core vision is to achieve dreams, everyone has their own dream or ideas, we provide the necessary tools and methods to bring such ideas to life, to contact us you may call us directly or send us an email. \\n\\n Thank you For Using Or Application !");

        Button call = findViewById(R.id.call);
        Button email = findViewById(R.id.email);

        if(shared.getString("language", "").equals("arabic")){

            t.setText("فريق تقني متخصص في تطوير البرمجيات, لمعلومات الاتصال بنا اختر من الأسفل");
            call.setText("إتصل");
            email.setText("بريد الكتروني");
        }



        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "0792942040", null)));

            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Our Email Is Copied", "alalmahdawi15@cit.just.edu.jo");
                clipboard.setPrimaryClip(clip);

                if(shared.getString("language", "").equals("arabic"))
                    Toast.makeText(About.this, "alalmahdawi15@cit.just.edu.jo Has Been Copied To Your Clipboard, You Can Now Paste It !", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(About.this, "alalmahdawi15@cit.just.edu.jo تم نسخ الايميل و بامكانك استخدامه", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
