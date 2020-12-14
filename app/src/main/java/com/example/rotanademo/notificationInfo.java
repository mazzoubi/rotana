package com.example.rotanademo;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class notificationInfo extends AppCompatActivity {

    classNotification noti=new classNotification();
    TextView title,date,time,desc;
    FirebaseFirestore db ;
    SharedPreferences shared2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_info);
        init();

    }

    void init(){

        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        db=FirebaseFirestore.getInstance();


        noti.state=getIntent().getStringExtra("state");
        noti.desc=getIntent().getStringExtra("desc");
        noti.time=getIntent().getStringExtra("time");
        noti.date=getIntent().getStringExtra("date");
        noti.id=getIntent().getStringExtra("id");
        noti.title=getIntent().getStringExtra("title");


        title=findViewById(R.id.title);
        date=findViewById(R.id.date);
        time=findViewById(R.id.time);
        desc=findViewById(R.id.desc);

        title.setText(noti.title);
        date.setText(noti.date);
        time.setText(noti.time);
        desc.setText(noti.desc+"");

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("title", noti.title);
        reservation.put("date", noti.date);
        reservation.put("time", noti.time);
        reservation.put("desc", noti.desc);
        reservation.put("state", "0");
        reservation.put("id", noti.id);

        if(shared2.getString("language", "").equals("arabic")) {
            db.collection("Res_1_adminNotificationsAr").document(noti.id).set(reservation)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //     progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()) {

                            }
                        }
                    });
        }
        else {
            db.collection("Res_1_adminNotificationsEn").document(noti.id).set(reservation)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //     progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()){

                            }

                        }
                    });
        }



    }
}
