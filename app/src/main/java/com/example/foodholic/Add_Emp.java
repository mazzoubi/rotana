package com.example.foodholic;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public class Add_Emp extends AppCompatActivity {

    Button pic;
    static final int gallary_pick = 9;
    StorageReference mImageStorage;
    FirebaseFirestore db;
    String download_url;
    SharedPreferences shared2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__emp);

        pic = findViewById(R.id.table1);

        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        if (shared2.getString("language", "").equals("arabic")) {
            pic.setText("اضف وجبه");
        }


        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                shared2 = getSharedPreferences("lang", MODE_PRIVATE);
                if (shared2.getString("language", "").equals("arabic")) {
                    Intent gallary = new Intent();
                    gallary.setType("image/*");
                    gallary.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(gallary, "أختر صورة"), gallary_pick);
                } else {
                    Intent gallary = new Intent();
                    gallary.setType("image/*");
                    gallary.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(gallary, "choose image"), gallary_pick);
                }
            }

        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
