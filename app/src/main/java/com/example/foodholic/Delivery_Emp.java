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

public class Delivery_Emp extends AppCompatActivity {

    FirebaseFirestore db;

    ListView list;
    ArrayAdapter<String> adapter;

    ArrayList<String> id;
    ArrayList<String> loc;
    ArrayList<String> info;
    ArrayList<String>dname;

    ArrayAdapter<String> adapterSpin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery__emp);

        db = FirebaseFirestore.getInstance();

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list=findViewById(R.id.list);

        id = new ArrayList<String>();
        loc = new ArrayList<String>();
        info = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, info);
        list.setAdapter(adapter);
        dname = new ArrayList<String>();
        final Spinner sp = findViewById(R.id.type);
        adapterSpin = new ArrayAdapter<String>(Delivery_Emp.this, android.R.layout.simple_spinner_item, dname);
        sp.setAdapter(adapterSpin);

        getDriver();

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                downloadData(sp.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long ido) {
                if (HomeAct.lang==1){

                    new AlertDialog.Builder(Delivery_Emp.this)
                            .setMessage("تأكيد أم حذف الطلب ؟")
                            .setNegativeButton("حذف", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    removeData(sp.getSelectedItem().toString(), position); }
                            })
                            .setPositiveButton("تأكيد", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    removeData(sp.getSelectedItem().toString(), position);
                                }
                            }).setCancelable(false).create().show();
                }
                else {

                    new AlertDialog.Builder(Delivery_Emp.this)
                            .setMessage("Confirm or delete request?")
                            .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    removeData(sp.getSelectedItem().toString(), position); }
                            })
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    removeData(sp.getSelectedItem().toString(), position);
                                }
                            }).setCancelable(false).create().show();
                }
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                if (HomeAct.lang==1){
                    new AlertDialog.Builder(Delivery_Emp.this)
                            .setMessage("هل ترغب بالأتصال ؟")
                            .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setPositiveButton("إتصال", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String str = info.get(position);
                            String num = str.substring(str.indexOf("الهاتف : ")+9, str.indexOf("العنوان : "));
                            num = RemoveSpace(num);
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", num, null)));
                        }
                    }).setCancelable(false).show();

                }
                else {
                    new AlertDialog.Builder(Delivery_Emp.this)
                            .setMessage("Want to connect?")
                            .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String str = info.get(position);
                            String num = str.substring(str.indexOf("phone : ")+9, str.indexOf("Address : "));
                            num = RemoveSpace(num);
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", num, null)));
                        }
                    }).setCancelable(false).show();

                }
                return true;
            }
        });


    }

    public void downloadData(String path){

        info.clear();

        db.collection("Res_1_Delivery")
                .document(path).collection("1")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for(QueryDocumentSnapshot document : task.getResult())

                    if (task.isSuccessful()) {
                        if (HomeAct.lang==1){
                            info.add("الأسم : "+document.get("name").toString()+"\n"
                                    +"الهاتف : "+document.get("mobile").toString()+"\n"
                                    +"العنوان : "+document.get("address").toString()+"\n"
                                    +"المجموع : "+document.get("sum").toString()+"\n" );
                        }
                        else{
                            info.add("Name : "+document.get("name").toString()+"\n"
                                    +"Phone : "+document.get("mobile").toString()+"\n"
                                    +"Address : "+document.get("address").toString()+"\n"
                                    +"Total : "+document.get("sum").toString()+"\n" );
                        }

                        adapter.notifyDataSetChanged();
                        if(info.isEmpty())
                           if (HomeAct.lang==1){
                               Toast.makeText(Delivery_Emp.this, "لايوجد طلبات دلفري", Toast.LENGTH_SHORT).show();
                           }
                        else {
                               Toast.makeText(Delivery_Emp.this, "No delivery request", Toast.LENGTH_SHORT).show();
                           }
                    }

            }
        });

    }

    public void removeData(String path, int pos){

        String temp = info.get(pos).substring(info.get(pos).indexOf(" : ")+3, info.get(pos).indexOf("الهاتف : "));
        db.collection("Res_1_Delivery").document(path).collection("1").document(temp.replaceAll("\n","")).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            loc.clear();
                            info.clear();
                            adapter.clear();

                            adapter.notifyDataSetChanged();

                            if (HomeAct.lang==1){
                                Toast.makeText(Delivery_Emp.this, "تم", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Delivery_Emp.this, "Done", Toast.LENGTH_SHORT).show();
                            }

                            recreate();
                        }
                    }
                });

    }

    public void ConfirmData(int id){

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr="+loc.get(id)+","+loc.get(id+1)));
        startActivity(intent);

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

        db.collection("Res_1_driver")
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


}
