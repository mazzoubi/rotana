package com.example.foodholic;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DriveCustom.this);
                LayoutInflater inflater = DriveCustom.this.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.dialog_redirect4, null));
                final android.support.v7.app.AlertDialog dialog = builder.create();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.show();
                dialog.getWindow().setAttributes(lp);

                Button b1, b2, b4, b5;

                b1 = dialog.findViewById(R.id.call);
                b2 = dialog.findViewById(R.id.go);
                b4 = dialog.findViewById(R.id.confirm);
                b5 = dialog.findViewById(R.id.remove);

                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String str = DataToShow.get(position);
                        String num = str.substring(str.indexOf("الهاتف : ")+9, str.indexOf("الوقت : "));
                        num = RemoveSpace(num);
                        Intent intent = new Intent((Intent.ACTION_DIAL));
                        intent.setData(Uri.parse("tel:"+num));
                        startActivity(intent);
                    } });

                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse(DataToGo.get(position)));
                        startActivity(intent);
                    } });

                b4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        removeData(DataId.get(position));

                    }
                });

                b5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        removeData(DataId.get(position));

                    }
                });

            }
        });

    }

    public void removeData(String path){

        db.collection("Res_1_CustomerDrive").document(path)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(DriveCustom.this, "تمت العملية بنجاح !", Toast.LENGTH_LONG).show();
                recreate();
            }
        });

    }

    public String RemoveSpace(String str){

        char [] arr = str.toCharArray();
        String temp = "";

        for(int i=0; i<arr.length; i++)
            if(Character.isDigit(arr[i]) || arr[i] == '.')
                temp+=arr[i];

        return temp;
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
