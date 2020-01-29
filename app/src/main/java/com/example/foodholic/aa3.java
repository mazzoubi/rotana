package com.example.foodholic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class aa3 extends AppCompatActivity {

    public static ArrayList<classItem> item;
    public static ArrayList<classSubItem> subItems;
    FirebaseFirestore db;
    Button button;
    ListView listView;
    Spinner spinner;
    int lang=0;

    public static boolean eng=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_aa3);

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        RadioGroup rg = findViewById(R.id.rg);
        final RadioButton rb1 = findViewById(R.id.radioButtonAR);
        final RadioButton rb2 = findViewById(R.id.radioButtonEN);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if(rb1.isChecked())
                    eng = false;
                if(rb2.isChecked())
                    eng = true;

                if(!eng)
                    loadItem2();
                else
                    loadItem();

                if (eng){
                    button.setText(" + add item + ");
                    getSupportActionBar().setTitle("Go Back");
                }
                else{
                    button.setText(" + أضافة مادة + ");
                    getSupportActionBar().setTitle("الرجوع");
                }



                ArrayAdapter<classSubItem>adapter=new mainGuestAdapter(getApplicationContext(),R.layout.row_guest,subItems);
                listView.setAdapter(adapter);

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s=adapterView.getItemAtPosition(i).toString();
                if(eng)
                    loadSubItem(s);
                else
                    loadSubItem2(s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int pos=i;
                if(lang==1) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(aa3.this)
                            .setMessage("هل تريد حذف هذا العنصر؟")
                            .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        db.collection("Res_1_subItem").document(subItems.get(pos).itemId).delete();
                                        Toast.makeText(getApplicationContext(),"تم الاجراء بنجاح",Toast.LENGTH_LONG).show();
                                        recreate();
                                    }catch (Exception e){Toast.makeText(getApplicationContext(),"!!لايمكن تنفيذ هذا الاجراء",Toast.LENGTH_LONG).show();}
                                }
                            })
                            .setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
//                                    Intent n=new Intent(getApplicationContext(),cashAddItemAR.class);
//                                    n.putExtra("ppos",pos);
//                                    startActivity(n);
                                }
                            });
                    AlertDialog alert=builder.create();
                    alert.setTitle(subItems.get(pos).subItem);
                    alert.show();


                }
                else {
                    AlertDialog.Builder builder=new AlertDialog.Builder(aa3.this)
                            .setMessage("do you want delete this sub item")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        db.collection("Res_1_subItem").document(subItems.get(pos).itemId).delete();
                                        Toast.makeText(getApplicationContext(),"this operation is done",Toast.LENGTH_LONG).show();
                                        recreate();
                                    }catch (Exception e){Toast.makeText(getApplicationContext(),"This procedure cannot be performed",Toast.LENGTH_LONG).show();}
                                }
                            })
                            .setNegativeButton("add others language ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent n=new Intent(getApplicationContext(),cashAddItemAR.class);
                                    n.putExtra("ppos",pos);
                                    startActivity(n);
                                }
                            });
                    AlertDialog alert=builder.create();
                    alert.setTitle(subItems.get(pos).subItem);
                    alert.show();
                    //sas

                }


            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(aa3.this, cashAddItem.class));
            }
        });
    }

    void init (){
        SharedPreferences shared2;
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        if(shared2.getString("language", "").equals("arabic")) {
            lang=1;
        }
        db=FirebaseFirestore.getInstance();
        button=findViewById(R.id.button911);
        listView=findViewById(R.id.listView911);
        spinner=findViewById(R.id.spinner911);
        if (eng){
            button.setText(" + add item + ");
        }
        else{
            button.setText(" + أضافة مادة + ");
        }
        loadItem();

    }

    void loadSubItem(final String s){
        subItems=new ArrayList<>();
        db.collection("Res_1_subItem").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d :list){
                    if (d.toObject(classSubItem.class).item.equals(s)) {
                        subItems.add(d.toObject(classSubItem.class));
                    }
                }
                ArrayAdapter<classSubItem>adapter=new mainGuestAdapter(getApplicationContext(),R.layout.row_guest,subItems);
                listView.setAdapter(adapter);
            }
        });
    }

    void loadSubItem2(final String s){
        subItems=new ArrayList<>();
        db.collection("Res_1_subItem").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d :list){
                    if (d.toObject(classSubItem.class).Ar_item.equals(s)) {
                        subItems.add(d.toObject(classSubItem.class));
                    }
                }
                ArrayAdapter<classSubItem>adapter=new mainGuestAdapter(getApplicationContext(),R.layout.row_guest,subItems);
                listView.setAdapter(adapter);
            }
        });
    }

    void loadItem2(){
        item=new ArrayList<>();
        final ArrayList<String> ii=new ArrayList<>();
        db.collection("Res_1_Ar_Items").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d :list){
                    item.add(d.toObject(classItem.class));
                    ii.add(d.toObject(classItem.class).itemName);
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),R.layout.items_row3, R.id.item,ii);
                spinner.setAdapter(adapter);
            }
        });
    }

    void loadItem(){
        item=new ArrayList<>();
        final ArrayList<String> ii=new ArrayList<>();
        db.collection("Res_1_items").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d :list){
                    item.add(d.toObject(classItem.class));
                    ii.add(d.toObject(classItem.class).itemName);
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),R.layout.items_row3, R.id.item,ii);
                spinner.setAdapter(adapter);
            }
        });
    }
}
