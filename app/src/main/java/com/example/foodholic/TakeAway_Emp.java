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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TakeAway_Emp extends AppCompatActivity {

    FirebaseFirestore db;

    ListView list;
    ArrayAdapter<String> adapter;

    ArrayList<String> id;
    ArrayList<String> info;
    ArrayList<String>dname;

    ArrayAdapter<String> adapterSpin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_away__emp);

        db = FirebaseFirestore.getInstance();

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list=findViewById(R.id.list);

        id = new ArrayList<String>();
        info = new ArrayList<String>();
        dname = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, info);
        list.setAdapter(adapter);

        final Spinner sp = findViewById(R.id.type);
        adapterSpin = new ArrayAdapter<String>(TakeAway_Emp.this, android.R.layout.simple_spinner_item, dname);
        sp.setAdapter(adapterSpin);

        getDriver();

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadData();
            }
        });

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                downloadData(sp.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //downloadData();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long ido) {

                new AlertDialog.Builder(TakeAway_Emp.this)
                        .setMessage("تأكيد أم حذف الطلب ؟")
                        .setNegativeButton("حذف", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeData(sp.getSelectedItem().toString()); }
                        })
                        .setPositiveButton("تأكيد", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddSale( position);
                        removeData(sp.getSelectedItem().toString());
                         }
                }).setCancelable(false).create().show();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(TakeAway_Emp.this)
                        .setMessage("هل ترغب بالأتصال ؟")
                        .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton("أتصال", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = info.get(position);
                        String num = str.substring(str.indexOf("الهاتف : ")+9, str.indexOf("العنوان : "));
                        num = RemoveSpace(num);
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", num, null)));
                    }
                }).setCancelable(false).show();

                return true;
            }
        });

    }

    public void downloadData(){

        info.clear();

        db.collection("Res_1_TakeAway")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for(QueryDocumentSnapshot document : task.getResult()){

                        info.add("الأسم : "+document.get("user_name").toString()+"\n"
                                +"الهاتف : "+document.get("user_mobile").toString()+"\n"
                                +"العنوان : "+document.get("user_loc").toString()+"\n"
                                +"قائمة : "+document.get("item_list").toString()+"\n"
                                +"مجموع : "+document.get("item_sum_price").toString()+"\n" );

                    }


                    adapter.notifyDataSetChanged();
                    if(info.isEmpty())
                        Toast.makeText(TakeAway_Emp.this, "لايوجد طلبات سفري", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void downloadData(String path){

        info.clear();

        db.collection("Res_1_TakeAway")
                .document(path).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                        info.add("الأسم : "+task.getResult().get("user_name").toString()+"\n"
                                +"الهاتف : "+task.getResult().get("user_mobile").toString()+"\n"
                                +"العنوان : "+task.getResult().get("user_loc").toString()+"\n"
                                +"قائمة : "+task.getResult().get("item_list").toString()+"\n"
                                +"مجموع : "+task.getResult().get("item_sum_price").toString()+"\n" );

                    adapter.notifyDataSetChanged();
                    if(info.isEmpty())
                        Toast.makeText(TakeAway_Emp.this, "لايوجد طلبات سفري", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void removeData(String path){

        db.collection("Res_1_TakeAway").document(path).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        info.clear();
                        adapter.clear();

                        adapter.notifyDataSetChanged();
                        Toast.makeText(TakeAway_Emp.this, "تم", Toast.LENGTH_SHORT).show();
                        recreate(); } });

    }

    public String RemoveSpace(String str){

        char [] arr = str.toCharArray();
        String temp = "";

        for(int i=0; i<arr.length; i++)
            if(Character.isDigit(arr[i]) || arr[i] == '.')
                temp+=arr[i];

        return temp;
    }

    public void getDriver(){

        dname.clear();

        db.collection("Res_1_TakeAway")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful())
                            for (QueryDocumentSnapshot document : task.getResult())
                                addData(document.getId());

                    } });

    }

    public void addData(String name){

        dname.add(name);
        adapterSpin.notifyDataSetChanged();

    }

    public void AddSale(int pos){

        String [] temp = info.get(pos)
                .substring(info.get(pos).indexOf("قائمة : ")+8, info.get(pos).indexOf("مجموع : ")).split(" , ");

        String [] temp2 = new String [temp.length];

        for(int i=0; i<temp2.length; i++)
            temp2[i] = temp[i].substring(temp[i].indexOf(":")+1);

        for(int i=0; i<temp.length; i++){

            try{
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
            Date dateee = new Date();
            String date = dateFormat.format(dateee);

            String day = date.substring(0, date.indexOf(" "));
            String time = date.substring(date.indexOf(" ")+1);

            Map<String, Object> sale = new HashMap<>();
            sale.put("date", day);
            sale.put("time", time);
            sale.put("item", "");
            sale.put("subItem", temp[i].substring(0, temp[i].indexOf("=")));
            sale.put("empEmail", getIntent().getStringExtra("empemail"));
            sale.put("sale", temp2[i]);

            db.collection("Res_1_sales").document()
                    .set(sale)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            recreate();
                        }
                    });
            }
            catch(Exception ex){}


        }









    }

}
