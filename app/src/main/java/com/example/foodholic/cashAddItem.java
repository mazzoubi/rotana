package com.example.foodholic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class cashAddItem extends AppCompatActivity {

    FirebaseFirestore db;
    EditText itemName,itemPrice,itemCost,itemPoint,newItemName,kitchen;
    CheckBox checkBox;
    ImageView imageView;
    Button add;
    Spinner currentItem;
    SharedPreferences shared2;
    ArrayList<classItem>itemList;
    ArrayList<classSubItem> subItems;

    ArrayList<String>spinerIten;

    String itemItem="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_add_item);

        db=FirebaseFirestore.getInstance();


        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        itemName=findViewById(R.id.itemSubItem);
        itemPrice=findViewById(R.id.itemPrice);
        itemCost=findViewById(R.id.itemCost);
        itemPoint=findViewById(R.id.itemPoint);
        newItemName=findViewById(R.id.itemNewItem);


        checkBox=findViewById(R.id.itemCheckBox);


        add=findViewById(R.id.buttonItemAdd);

        kitchen=findViewById(R.id.kitchenNo);
        currentItem=findViewById(R.id.spinner_correntItem);

        checkBox.setChecked(true);

        if (HomeAct.lang==1){
            itemName.setText("اسم الماده");
            itemPrice.setText("سعر الماده");
            itemCost.setText("سعر التكلفه");
            itemPoint.setText("النقاط");
            newItemName.setText("اسم التصنيف");
            checkBox.setText("تصنيف جديد؟");
            kitchen.setText("رقم المطبخ");
            add.setText("اضافه");
        }

        db.collection("Res_1_items").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                itemList=new ArrayList<>();
                spinerIten=new ArrayList<>();
                if (HomeAct.lang==1){
                    spinerIten.add("اختر عنصر");
                }
                else{
                spinerIten.add("choose item");}
                for(DocumentSnapshot d:list){
                    classItem a=d.toObject(classItem.class);
                    itemList.add(a);
                    spinerIten.add(a.itemName);
                }

                ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,spinerIten);
                currentItem.setAdapter(adapter);
                if (itemList.isEmpty()){
                    checkBox.setChecked(true);
                    newItemName.setEnabled(true);
                    currentItem.setEnabled(false);
                }
            }
        });

        currentItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){}
                else {
                    itemItem=itemList.get(position-1).itemName;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        db.collection("Res_1_items").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                subItems=new ArrayList<>();
                for (DocumentSnapshot d:list){
                    classSubItem a=d.toObject(classSubItem.class);
                    subItems.add(a);
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    currentItem.setEnabled(!isChecked);
                    newItemName.setEnabled(isChecked);
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (itemName.getText().toString().isEmpty()){
                   if (HomeAct.lang==1){
                       Toast.makeText(getApplicationContext(),"الرجاء ادخال اسم العنصر",Toast.LENGTH_LONG).show();
                   }
                   else {
                       Toast.makeText(getApplicationContext(),"please enter item name",Toast.LENGTH_LONG).show();
                   }
                }
                else if (itemPrice.getText().toString().isEmpty()){
                    if (HomeAct.lang==1){
                        Toast.makeText(getApplicationContext(),"الرجاء ادخال سعر العنصر",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"please enter item price",Toast.LENGTH_LONG).show();
                    }
                }
                else if (itemCost.getText().toString().isEmpty()){
                    if (HomeAct.lang==1){
                        Toast.makeText(getApplicationContext(),"الرجاء ادخال سعر النكلفه للعنصر",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"please enter item cost",Toast.LENGTH_LONG).show();
                    }
                }
                else if (itemPoint.getText().toString().isEmpty()){
                    if (HomeAct.lang==1){
                        Toast.makeText(getApplicationContext(),"الرجاء ادخال النقاط",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"please enter item point",Toast.LENGTH_LONG).show();
                    }
                }

                else{
                    if (checkBox.isChecked()){
                         if (newItemName.getText().toString().isEmpty()){
                             if (HomeAct.lang==1){
                                 Toast.makeText(getApplicationContext(),"الرجاء اختيار اسم العنصر الجديد",Toast.LENGTH_LONG).show();
                             }
                             else {
                                 Toast.makeText(getApplicationContext(),"please enter new item name",Toast.LENGTH_LONG).show();
                             }
                        }
                         else{
                             boolean x=false;
                             for(int i=0;i<itemList.size();i++){
                                 if (itemList.get(i).itemName.equals(newItemName.getText().toString())){
                                     x=true;
                                     break;
                                 }
                             }
                             if (newItemName.getText().toString().isEmpty()){
                                 if (HomeAct.lang==1){
                                     Toast.makeText(getApplicationContext(),"الرجاء اختيار اسم العنصر الجديد",Toast.LENGTH_LONG).show();
                                 }
                                 else {
                                     Toast.makeText(getApplicationContext(),"please enter new item name",Toast.LENGTH_LONG).show();
                                 }
                             }
                             else if (x){
                                 if (HomeAct.lang==1){
                                     Toast.makeText(getApplicationContext(),"العنصر موجود فعلاً بالقائمه الرجاء اختياره من القائمه",Toast.LENGTH_LONG).show();
                                 }
                                 else {
                                     Toast.makeText(getApplicationContext(),"this item found in list please choose it from list",Toast.LENGTH_LONG).show();
                                 }
                             }
                             else{
                                 Map<String, Object> reservation = new HashMap<>();
                                 String id=new Date()+"";
                                 reservation.put("itemName", newItemName.getText().toString());


                                 db.collection("Res_1_items").document(id).set(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                         if (task.isSuccessful()){

                                             if(shared2.getString("language", "").equals("arabic")) {
                                                 Toast.makeText(getApplicationContext(),"لقد تمت اضافة العنصر بنجاح ..",Toast.LENGTH_LONG).show();
                                             }
                                             else{
                                                 Toast.makeText(getApplicationContext(),"successful add ..",Toast.LENGTH_LONG).show();
                                             }
                                             Map<String, Object> reservation = new HashMap<>();
                                             String id=new Date()+"";
                                             reservation.put("item", newItemName.getText().toString());
                                             reservation.put("subItem", itemName.getText().toString() );
                                             reservation.put("image", "");
                                             reservation.put("point", itemPoint.getText().toString());
                                             reservation.put("kitchen", kitchen.getText().toString());
                                             reservation.put("price",  Double.parseDouble(itemPrice.getText().toString()));
                                             reservation.put("cost",  Double.parseDouble(itemCost.getText().toString()));
                                             reservation.put("itemId", id);



                                             db.collection("Res_1_subItem").document(id).set(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<Void> task) {
                                                     if (task.isSuccessful()){
                                                         if(shared2.getString("language", "").equals("arabic")) {
                                                             Toast.makeText(getApplicationContext(),"لقد تمت الاضافة بنجاح ..",Toast.LENGTH_LONG).show();
                                                             startActivity(new Intent(cashAddItem.this, Emppage.class));
                                                         }
                                                         else{
                                                             Toast.makeText(getApplicationContext(),"successful add ..",Toast.LENGTH_LONG).show();
                                                         }
                                                     }
                                                 }
                                             });
                                         }
                                     }
                                 });
                             }
                         }
                    }
                    else{
                        Map<String, Object> reservation = new HashMap<>();
                        String id=new Date()+"";
                        reservation.put("item", itemItem);
                        reservation.put("subItem", itemName.getText().toString() );
                        reservation.put("image", "");
                        reservation.put("point", itemPoint.getText().toString());
                        reservation.put("kitchen", kitchen.getText().toString());
                        reservation.put("price", Double.parseDouble(itemPrice.getText().toString()));
                        reservation.put("itemId", id);



                        db.collection("Res_1_subItem").document(id).set(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    if(shared2.getString("language", "").equals("arabic")) {
                                        Toast.makeText(getApplicationContext(),"لقد تمت اضافة العنصر بنجاح ..",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(cashAddItem.this, Emppage.class));
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"successful item add ..",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
