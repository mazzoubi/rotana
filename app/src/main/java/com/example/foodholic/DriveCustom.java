package com.example.foodholic;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DriveCustom extends AppCompatActivity {

    FirebaseFirestore db;
    ListView listView;

    ArrayAdapter<String> adapter;
    ArrayList<String> DataToShow;
    ArrayList<String> DataToGo;
    ArrayList<String> DataId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_drive_custom);

        db = FirebaseFirestore.getInstance();

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.list);
        DataToShow = new ArrayList<>();
        DataToGo = new ArrayList<>();
        DataId = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.items_row3, R.id.item, DataToShow);
        listView.setAdapter(adapter);

        getData();

    }

    private void getData() {

        DataToShow.clear();
        DataToGo.clear();
        DataId.clear();
        adapter.notifyDataSetChanged();

        db.collection("Res_1_CustomerDrive").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful())
                            for(QueryDocumentSnapshot document : task.getResult())
                                AddData(document.get("lat").toString(),
                                        document.get("lng").toString(),
                                        document.get("name").toString(),
                                        document.get("mobile").toString(),
                                        document.get("time").toString(),
                                        document.get("date").toString(),
                                        document.get("people").toString(),
                                        document.getId());
                    }
                });

    }

    private void AddData(String str, String str2, String str3,
                         String str4, String str5, String str6,
                         String str7, String str8) {

        DataToShow.add("الأسم : "+str3+"\n"  +  "الهاتف : "+str4+"\n"  +  "الوقت : "
                +str5+"\n"  +  "التاريخ : "+str6+"\n"  +  "العدد : "+str7+"\n");

        DataToGo.add("http://maps.google.com/maps?daddr="+str+","+str2);

        DataId.add(str8);

        adapter.notifyDataSetChanged(); }

}
