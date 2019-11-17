package com.example.foodholic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class Online extends AppCompatActivity {

    FirebaseFirestore db;

    ListView list;
    ArrayAdapter<String> adapter;

    ArrayList<String> id;
    ArrayList<String> info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online);

        db = FirebaseFirestore.getInstance();

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list=findViewById(R.id.list);

        id = new ArrayList<String>();
        info = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, info);
        list.setAdapter(adapter);

        downloadData();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long ido) {

                new AlertDialog.Builder(Online.this)
                        .setTitle("Please Take Action !")
                        .setMessage("Remove Or Call This Order ?")
                        .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeData(id.get(position)); }
                        }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String str = info.get(position);
                        String num = str.substring(str.indexOf("Mobile : ")+8, str.indexOf("Time : ")-1);
                        num = RemoveSpace(num);
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", num, null)));

                         }
                }).setCancelable(false).create().show();
            }
        });


    }

    public void downloadData(){

        db.collection("Res_1_Reservation")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                info.add("Name : "+document.get("user_name").toString()+"\n"
                                +"Mobile : "+document.get("user_mobile").toString()+"\n"
                                +"Time : "+document.get("time").toString()+"\n"
                                +"Date : "+document.get("date").toString()+"\n"
                                +"People : "+document.get("people").toString()+"\n"
                                +"Reservation Id : "+i);

                                id.add(document.getId());

                                i++; }

                            adapter.notifyDataSetChanged();
                            if(info.isEmpty())
                                Toast.makeText(Online.this, "You Have No Take Aways !", Toast.LENGTH_SHORT).show(); }


                    } });
    }

    public void removeData(String path){

        db.collection("Res_1_Reservation").document(path).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        info.clear();
                        id.clear();
                        adapter.clear();

                        adapter.notifyDataSetChanged();
                        Toast.makeText(Online.this, "Reservation Removed !", Toast.LENGTH_SHORT).show();
                        downloadData(); } });

    }

    public String RemoveSpace(String str){

        char [] arr = str.toCharArray();
        String temp = "";

        for(int i=0; i<arr.length; i++)
            if(Character.isDigit(arr[i]) || arr[i] == '.')
                temp+=arr[i];

        return temp;
    }

}
