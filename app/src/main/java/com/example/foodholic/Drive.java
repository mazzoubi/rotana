package com.example.foodholic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Drive extends AppCompatActivity {

    FirebaseFirestore db;

    ListView list;
    ArrayAdapter<String> adapter;

    ArrayList<String> id;
    ArrayList<String> loc;
    ArrayList<String> info;
    ArrayList<String>latlng;

    String name="";
    String p="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drive);

        db = FirebaseFirestore.getInstance();

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);

        list=findViewById(R.id.list);

        id = new ArrayList<String>();
        loc = new ArrayList<String>();
        info = new ArrayList<String>();
        latlng = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, R.layout.items_row3, R.id.item, info);
        list.setAdapter(adapter);

        downloadDriverData();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                getLngLat();

                if (HomeAct.lang==1){
                    new AlertDialog.Builder(Drive.this)
                            .setMessage("هل ترغب بالأتصال أم الذهاب للموقع؟")
                            .setNegativeButton("عرض الموقع", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                            Uri.parse("http://maps.google.com/maps?daddr="+latlng.get(i)));
                                    startActivity(intent);
                                }
                            }).setPositiveButton("إتصال", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String str = info.get(i);
                            String num = str.substring(str.indexOf("الهاتف : ")+9, str.indexOf("قائمة : "));
                            num = RemoveSpace(num);
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", num, null)));
                        }
                    }).show();

                }
                else {
                    new AlertDialog.Builder(Drive.this)
                            .setMessage("Want to connect?")
                            .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                            Uri.parse("http://maps.google.com/maps?daddr="+latlng.get(i)));
                                    startActivity(intent);
                                }
                            }).setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String str = info.get(i);
                            String num = str.substring(str.indexOf("phone : ")+9, str.indexOf("Menu : "));
                            num = RemoveSpace(num);
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", num, null)));
                        }
                    }).show();

                }

            }
        });

    }

    private void getLngLat() {

        latlng.clear();

        db.collection("Res_1_Delivery")
                .document(name).collection("1")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot document : task.getResult())
                    latlng.add(document.get("lat").toString()+","+document.get("lng").toString());
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

    public void downloadDriverData(){

        String em = getIntent().getStringExtra("email");
        db.collection("Res_1_employee")
                .document(em)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){

                            String temp = "";
                            temp += task.getResult().get("Fname").toString();
                            temp += " "+task.getResult().get("Lname").toString();
                            AddDataName(temp);
                        }
                    }
                });

    }

    public void AddDataName(String temp){
        name = temp;
        downloadData();
    }

    public void downloadData(){

        info.clear();

        db.collection("Res_1_Delivery")
                .document(name).collection("1")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot document : task.getResult())
                    if (task.isSuccessful()) {
                        if (HomeAct.lang==1){
                            info.add("الأسم : "+document.get("user_name").toString()+"\n"
                                    +"الهاتف : "+document.get("user_mobile").toString()+"\n"
                                    +"قائمة : "+document.get("item_list").toString()+"\n"
                                    +"النقاط : "+document.get("point_sum").toString()+"\n"
                                    +"ملاحظات : "+document.get("user_desc").toString()+"\n"
                                    +"سعر توصيل : "+document.get("d_price").toString()+"\n"
                                    +"العنوان : "+"jordan"+"\n"
                                    +"المجموع : "+document.get("item_sum_price").toString()+"\n" );
                        }
                        else {
                            info.add("Name : "+document.get("user_name").toString()+"\n"
                                    +"Phone : "+document.get("user_mobile").toString()+"\n"
                                    +"Menu : "+document.get("item_list").toString()+"\n"
                                    +"Points : "+document.get("point_sum").toString()+"\n"
                                    +"Notes : "+document.get("user_desc").toString()+"\n"
                                    +"Delivery Price : "+document.get("d_price").toString()+"\n"
                                    +"Address : "+"Jordan"+"\n"
                                    +"Total : "+document.get("item_sum_price").toString()+"\n" );

                        }
                        adapter.notifyDataSetChanged();
                        if(info.isEmpty()){
                            if (HomeAct.lang==1){
                                Toast.makeText(Drive.this, "لايوجد طلبات دلفري", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Drive.this, "No delivery request", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

            }
        });
    }



}
