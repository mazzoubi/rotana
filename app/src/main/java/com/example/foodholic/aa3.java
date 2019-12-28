package com.example.foodholic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class aa3 extends AppCompatActivity {

    ArrayList<classItem> item;
    ArrayList<classSubItem> subItems;
    FirebaseFirestore db;
    Button button;
    ListView listView;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aa3);
        init();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s=adapterView.getItemAtPosition(i).toString();
                loadSubItem(s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int pos=i;
                if(HomeAct.lang==1) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(aa3.this)
                            .setMessage("هل تريد حذف هذا العنصر؟")
                            .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        db.collection("Res_1_subItem").document(subItems.get(pos).itemId).delete();
                                        Toast.makeText(getApplicationContext(),"تم الاجراء بنجاح",Toast.LENGTH_LONG).show();
                                    }catch (Exception e){Toast.makeText(getApplicationContext(),"!!لايمكن تنفيذ هذا الاجراء",Toast.LENGTH_LONG).show();}
                                }
                            })
                            .setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
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
                                    }catch (Exception e){Toast.makeText(getApplicationContext(),"This procedure cannot be performed",Toast.LENGTH_LONG).show();}
                                }
                            })
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert=builder.create();
                    alert.setTitle(subItems.get(pos).subItem);
                    alert.show();


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
        db=FirebaseFirestore.getInstance();
        button=findViewById(R.id.button911);
        listView=findViewById(R.id.listView911);
        spinner=findViewById(R.id.spinner911);
        if (HomeAct.lang==1){
            button.setText("اضافة عنصر");
        }
        else{
            button.setText("add item");
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
                ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,ii);
                spinner.setAdapter(adapter);
            }
        });
    }
}