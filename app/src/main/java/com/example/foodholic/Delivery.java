package com.example.foodholic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Delivery extends AppCompatActivity {

    FirebaseFirestore db;
    SharedPreferences shared;
    SharedPreferences shared2;

    ListView list;
    ArrayAdapter<String> adapter;

    ArrayList<String> user_name;
    ArrayList<String> user_mobile;
    ArrayList<String> user_desc;
    ArrayList<String> item_name;
    ArrayList<String> item_desc;
    ArrayList<String> item_price;
    ArrayList<String> id;

    ArrayList<String> info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shared = getSharedPreferences("delivery", MODE_PRIVATE);
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);

        if(shared2.getString("language", "").equals("arabic"))
            setContentView(R.layout.activity_delivery2);
        else
            setContentView(R.layout.activity_delivery);

        db = FirebaseFirestore.getInstance();

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list=findViewById(R.id.list);

        user_name = new ArrayList<String>();
        user_mobile = new ArrayList<String>();
        user_desc = new ArrayList<String>();
        item_name = new ArrayList<String>();
        item_desc = new ArrayList<String>();
        item_price = new ArrayList<String>();
        id = new ArrayList<String>();

        info = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, info);
        list.setAdapter(adapter);

        downloadData();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ido) {

                String temp = info.get(position);
                int pos = temp.indexOf("Order Id : ");
                String str = temp.substring(pos+11);
                final int iddd = Integer.parseInt(str);

                if(shared2.getString("language", "").equals("arabic")){
                    new AlertDialog.Builder(Delivery.this)
                            .setTitle("تنبيه !")
                            .setMessage("هل ترغب ب الغاء هذا الطلب ؟")
                            .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss(); }
                            }).setPositiveButton("الغاء", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            removeData(id.get(iddd)); }
                    }).setCancelable(false).create().show();
                }
                else{

                    new AlertDialog.Builder(Delivery.this)
                            .setTitle("Alert !")
                            .setMessage("Do You Want To Remove This Order ?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss(); }
                            }).setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            removeData(id.get(iddd)); }
                    }).setCancelable(false).create().show();

                }
            }
        });


    }

    public void downloadData(){

        db.collection("Res_1_Delivery")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                user_name.add(document.get("user_name").toString());
                                user_mobile.add(document.get("user_mobile").toString());
                                user_desc.add(document.get("user_desc").toString());
                                item_name.add(document.get("item_name").toString());
                                item_desc.add(document.get("item_desc").toString());
                                item_price.add(document.get("item_price").toString());
                                id.add(document.getId());}

                            sortData(); }
                    } });
    }

    public void sortData(){

        for(int i=0; i<user_mobile.size(); i++)
            if(user_mobile.get(i).equals(shared.getString("mid", "")))
                info.add("Name : "+user_name.get(i)+"\n"+"Mobile : "+user_mobile.get(i)+"\n"+
                        "Notes : "+user_desc.get(i)+"\n"+ "Food : "+item_name.get(i)+"\n"+
                        "Description : "+item_desc.get(i)+"\n"+"Price : "+item_price.get(i)+"\n"+
                        "Order Time : "+id.get(i)+"\n"+ "Order Id : "+i);

        adapter.notifyDataSetChanged();

        if(info.isEmpty()){
            if (HomeAct.lang==1){
                Toast.makeText(this, "لا يوجد طلبات سفري !", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "You Have No Take Aways !", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void removeData(String path){

        db.collection("Res_1_Delivery").document(path).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        user_name.clear();
                        user_mobile.clear();
                        user_desc.clear();
                        item_name.clear();
                        item_desc.clear();
                        item_price.clear();
                        id.clear();
                        info.clear();
                        adapter.clear();

                        adapter.notifyDataSetChanged();
                        if(HomeAct.lang==1){
                            Toast.makeText(Delivery.this, "تم حذف الطلب !", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Delivery.this, "Order Removed !", Toast.LENGTH_SHORT).show();
                        }
                        downloadData(); } });

    }

}
