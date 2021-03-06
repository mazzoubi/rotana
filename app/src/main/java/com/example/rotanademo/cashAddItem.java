package com.example.rotanademo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class cashAddItem extends AppCompatActivity {

    FirebaseFirestore db;
    EditText itemName,itemName2, itemPrice,itemCost,itemPoint,newItemName,newItemName2, kitchen, descr, descr2;
    CheckBox checkBox;
    ImageView imageView;
    Button add, addimg;
    Spinner currentItem, currentItem2;
    SharedPreferences shared2;
    ArrayList<classItem>itemList, itemList2;
    ArrayList<classSubItem> subItems;
    ArrayList<String>spinerIten, spinerIten2;
    String itemItem="";
    String itemItem2="";
    Uri ImageUri=Uri.parse("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_cash_add_item);

        db=FirebaseFirestore.getInstance();

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        itemName=findViewById(R.id.itemSubItem);
        itemName2=findViewById(R.id.itemSubItem2);
        itemPrice=findViewById(R.id.itemPrice);
        itemCost=findViewById(R.id.itemCost);
        itemPoint=findViewById(R.id.itemPoint);
        newItemName=findViewById(R.id.itemNewItem);
        newItemName2=findViewById(R.id.itemNewItem2);
        descr=findViewById(R.id.descr);
        descr2=findViewById(R.id.descr2);
        addimg=findViewById(R.id.addimg);

        checkBox=findViewById(R.id.itemCheckBox);

        add=findViewById(R.id.buttonItemAdd);

        kitchen=findViewById(R.id.kitchenNo);
        currentItem=findViewById(R.id.spinner_correntItem);
        currentItem2=findViewById(R.id.spinner_correntItem2);

       // checkBox.setChecked(true);
        if (checkBox.isChecked()==false){
            newItemName.setEnabled(false);
            newItemName2.setEnabled(false);
            currentItem.setEnabled(true);
        }
        if (HomeAct.lang==1){
            itemName.setHint("إسم المادة بالانجليزي");
            itemName2.setHint("إسم المادة بالعربي");
            itemPrice.setHint("سعر المادة");
            itemCost.setHint("سعر التكلفة");
            itemPoint.setHint("النقاط");
            newItemName.setHint("إسم التصنيف بالانجليزي");
            newItemName2.setHint("إسم التصنيف بالعربي");
            checkBox.setText("تصنيف جديد؟");
            kitchen.setHint("رقم المطبخ");
            descr.setHint("وصف المادة بالانجليزي");
            descr2.setHint("وصف المادة بالعربي");
            add.setText("+ إضافة +");
            addimg.setText("+ إضافة صورة +");
            bar.setTitle("الرجوع");
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

                ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(), R.layout.items_row3, R.id.item,spinerIten);
                currentItem.setAdapter(adapter);
                if (itemList.isEmpty()){
                    checkBox.setChecked(true);
                    newItemName.setEnabled(true);
                    newItemName2.setEnabled(true);
                    currentItem.setEnabled(false);
                }
            }
        });

        db.collection("Res_1_Ar_Items").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                itemList2=new ArrayList<>();
                spinerIten2=new ArrayList<>();
                spinerIten2.add("اختر عنصر");
                for(DocumentSnapshot d:list){
                    classItem a=d.toObject(classItem.class);
                    itemList2.add(a);
                    spinerIten2.add(a.itemName);
                }

                ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(), R.layout.items_row3, R.id.item,spinerIten2);
                currentItem2.setAdapter(adapter);
                if (itemList2.isEmpty()){
                    checkBox.setChecked(true);
                    newItemName.setEnabled(true);
                    newItemName2.setEnabled(true);
                    currentItem2.setEnabled(false);
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

        currentItem2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){}
                else {
                    itemItem2=itemList2.get(position-1).itemName;
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
                    currentItem2.setEnabled(!isChecked);
                    newItemName.setEnabled(isChecked);
                    newItemName2.setEnabled(isChecked);
            }
        });

        addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 1996);

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adden();
            }
        });
    }

    void adden(){

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
                    for(int i=0;i<itemList2.size();i++){
                        if (itemList2.get(i).itemName.equals(newItemName2.getText().toString())){
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
                        final Map<String, Object> reservation = new HashMap<>();
                        final String id=new Date()+"";
                        reservation.put("itemName", newItemName.getText().toString());

                        final Uri resultUri = ImageUri;

                        if(!resultUri.toString().equals("")){
                            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                            final StorageReference ref = mStorageRef.child("Offers/"+ UUID.randomUUID().toString());
                            ref.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(final Uri uri) {

                                            if(!newItemName2.getText().toString().equals("")){
                                                final Map<String, Object> rrr = new HashMap<>();
                                                rrr.put("itemName", newItemName2.getText().toString());
                                                db.collection("Res_1_Ar_Items").document().set(rrr); }

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
                                                        reservation.put("image", uri.toString());
                                                        reservation.put("description", descr.getText().toString());
                                                        reservation.put("Ar_description", descr2.getText().toString());
                                                        reservation.put("Ar_item", newItemName2.getText().toString());
                                                        reservation.put("Ar_subItem", itemName2.getText().toString());
                                                        reservation.put("point", itemPoint.getText().toString());
                                                        reservation.put("kitchen", kitchen.getText().toString());
                                                        reservation.put("price",   Double.parseDouble(itemPrice.getText().toString()));
                                                        reservation.put("cost",  itemCost.getText().toString());
                                                        reservation.put("itemId", id);

                                                        db.collection("Res_1_subItem").document(id).set(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    if(shared2.getString("language", "").equals("arabic")) {
                                                                        Toast.makeText(getApplicationContext(),"لقد تمت الاضافة بنجاح ..",Toast.LENGTH_LONG).show();
                                                                        startActivity(new Intent(cashAddItem.this, Emppage.class)); finish();}
                                                                    else{
                                                                        Toast.makeText(getApplicationContext(),"successful add ..",Toast.LENGTH_LONG).show();
                                                                    }

                                                                     }  } });
                                                    }  }


                                            });
                                        }
                                    });
                                }
                            }); }
                        else{

                            if(!newItemName2.getText().toString().equals("")){
                                final Map<String, Object> rrr = new HashMap<>();
                                rrr.put("itemName", newItemName2.getText().toString());
                                db.collection("Res_1_Ar_Items").document().set(rrr); }

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
                                        reservation.put("Ar_description", descr2.getText().toString());
                                        reservation.put("Ar_item", newItemName2.getText().toString());
                                        reservation.put("Ar_subItem", itemName2.getText().toString());
                                        reservation.put("description", descr.getText().toString());
                                        reservation.put("point", itemPoint.getText().toString());
                                        reservation.put("kitchen", kitchen.getText().toString());
                                        reservation.put("price",  Double.parseDouble(itemPrice.getText().toString()));
                                        reservation.put("cost",  itemCost.getText().toString());
                                        reservation.put("itemId", id);



                                        db.collection("Res_1_subItem").document(id).set(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    if(shared2.getString("language", "").equals("arabic")) {
                                                        Toast.makeText(getApplicationContext(),"لقد تمت الاضافة بنجاح ..",Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(cashAddItem.this, Emppage.class)); finish();}
                                                    else{
                                                        Toast.makeText(getApplicationContext(),"successful add ..",Toast.LENGTH_LONG).show();
                                                    }


                                                     }  } });
                                    }  } });

                        }

                    }


                } }
            else{

                final Uri resultUri = ImageUri;

                if(!resultUri.toString().equals("")) {

                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                    final StorageReference ref = mStorageRef.child("Offers/" + UUID.randomUUID().toString());
                    ref.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Map<String, Object> reservation = new HashMap<>();
                                    String id = new Date() + "";
                                    reservation.put("item", itemItem);
                                    reservation.put("subItem", itemName.getText().toString());
                                    reservation.put("image", uri.toString());
                                    reservation.put("Ar_description", descr2.getText().toString());
                                    reservation.put("Ar_item", itemItem2);
                                    reservation.put("Ar_subItem", itemName2.getText().toString());
                                    reservation.put("description", descr.getText().toString());
                                    reservation.put("point", itemPoint.getText().toString());
                                    reservation.put("kitchen", kitchen.getText().toString());
                                    reservation.put("price",  Double.parseDouble(itemPrice.getText().toString()));
                                    reservation.put("itemId", id);

                                    db.collection("Res_1_subItem").document(id).set(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                if (shared2.getString("language", "").equals("arabic")) {
                                                    Toast.makeText(getApplicationContext(), "لقد تمت اضافة العنصر بنجاح ..", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(cashAddItem.this, Emppage.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "successful item add ..", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                else{

                    Map<String, Object> reservation = new HashMap<>();
                    String id = new Date() + "";
                    reservation.put("item", itemItem);
                    reservation.put("subItem", itemName.getText().toString());
                    reservation.put("image", "");
                    reservation.put("Ar_description", descr2.getText().toString());
                    reservation.put("Ar_item", itemItem2);
                    reservation.put("Ar_subItem", itemName2.getText().toString());
                    reservation.put("description", descr.getText().toString());
                    reservation.put("point", itemPoint.getText().toString());
                    reservation.put("kitchen", kitchen.getText().toString());
                    reservation.put("price", Double.parseDouble(itemPrice.getText().toString()));
                    reservation.put("itemId", id);

                    db.collection("Res_1_subItem").document(id).set(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (shared2.getString("language", "").equals("arabic")) {
                                    Toast.makeText(getApplicationContext(), "لقد تمت اضافة العنصر بنجاح ..", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(cashAddItem.this, Emppage.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "successful item add ..", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });

                }



            }
        }
    }

    void addar(){

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
                    for(int i=0;i<itemList2.size();i++){
                        if (itemList2.get(i).itemName.equals(newItemName2.getText().toString())){
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
                        final Map<String, Object> reservation = new HashMap<>();
                        final String id=new Date()+"";
                        reservation.put("itemName", newItemName.getText().toString());

                        final Uri resultUri = ImageUri;

                        if(!resultUri.toString().equals("")){
                            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                            final StorageReference ref = mStorageRef.child("Offers/"+ UUID.randomUUID().toString());
                            ref.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(final Uri uri) {

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
                                                        reservation.put("subItem", "" );
                                                        reservation.put("image", uri.toString());
                                                        reservation.put("description", "");
                                                        reservation.put("Ar_description", descr.getText().toString());
                                                        reservation.put("Ar_item", "");
                                                        reservation.put("Ar_subItem", itemName.getText().toString());
                                                        reservation.put("point", itemPoint.getText().toString());
                                                        reservation.put("kitchen", kitchen.getText().toString());
                                                        reservation.put("price",  Double.parseDouble(itemPrice.getText().toString()));
                                                        reservation.put("cost",  itemCost.getText().toString());
                                                        reservation.put("itemId", id);



                                                        db.collection("Res_1_subItem").document(id).set(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    if(shared2.getString("language", "").equals("arabic")) {
                                                                        Toast.makeText(getApplicationContext(),"لقد تمت الاضافة بنجاح ..",Toast.LENGTH_LONG).show();
                                                                        startActivity(new Intent(cashAddItem.this, Emppage.class)); finish();}
                                                                    else{
                                                                        Toast.makeText(getApplicationContext(),"successful add ..",Toast.LENGTH_LONG).show();
                                                                    } }  } });
                                                    }  } });
                                        }
                                    });
                                }
                            }); }
                        else{

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
                                        reservation.put("subItem", "");
                                        reservation.put("image", "");
                                        reservation.put("Ar_description", descr.getText().toString());
                                        reservation.put("Ar_item", "");
                                        reservation.put("Ar_subItem",  itemName.getText().toString());
                                        reservation.put("description", "");
                                        reservation.put("point", itemPoint.getText().toString());
                                        reservation.put("kitchen", kitchen.getText().toString());
                                        reservation.put("price",  Double.parseDouble(itemPrice.getText().toString()));
                                        reservation.put("cost", itemCost.getText().toString());
                                        reservation.put("itemId", id);



                                        db.collection("Res_1_subItem").document(id).set(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    if(shared2.getString("language", "").equals("arabic")) {
                                                        Toast.makeText(getApplicationContext(),"لقد تمت الاضافة بنجاح ..",Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(cashAddItem.this, Emppage.class)); finish();}
                                                    else{
                                                        Toast.makeText(getApplicationContext(),"successful add ..",Toast.LENGTH_LONG).show();
                                                    } }  } });
                                    }  } });

                        }

                    }


                } }
            else{

                final Uri resultUri = ImageUri;

                if(!resultUri.toString().equals("")) {

                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                    final StorageReference ref = mStorageRef.child("Offers/" + UUID.randomUUID().toString());
                    ref.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Map<String, Object> reservation = new HashMap<>();
                                    String id = new Date() + "";
                                    reservation.put("item", itemItem);
                                    reservation.put("subItem", "");
                                    reservation.put("image", uri.toString());
                                    reservation.put("Ar_description", descr.getText().toString());
                                    reservation.put("Ar_item", "");
                                    reservation.put("Ar_subItem", itemName.getText().toString());
                                    reservation.put("description", "");
                                    reservation.put("point", itemPoint.getText().toString());
                                    reservation.put("kitchen", kitchen.getText().toString());
                                    reservation.put("price", Double.parseDouble(itemPrice.getText().toString()));
                                    reservation.put("itemId", id);

                                    db.collection("Res_1_subItem").document(id).set(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                if (shared2.getString("language", "").equals("arabic")) {
                                                    Toast.makeText(getApplicationContext(), "لقد تمت اضافة العنصر بنجاح ..", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(cashAddItem.this, Emppage.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "successful item add ..", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                else{

                    Map<String, Object> reservation = new HashMap<>();
                    String id = new Date() + "";
                    reservation.put("item", itemItem);
                    reservation.put("subItem", "");
                    reservation.put("image", "");
                    reservation.put("Ar_description", descr.getText().toString());
                    reservation.put("Ar_item", "");
                    reservation.put("Ar_subItem", itemName.getText().toString());
                    reservation.put("description", "");
                    reservation.put("point", itemPoint.getText().toString());
                    reservation.put("kitchen", kitchen.getText().toString());
                    reservation.put("price", Double.parseDouble(itemPrice.getText().toString()));
                    reservation.put("itemId", id);

                    db.collection("Res_1_subItem").document(id).set(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (shared2.getString("language", "").equals("arabic")) {
                                    Toast.makeText(getApplicationContext(), "لقد تمت اضافة العنصر بنجاح ..", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(cashAddItem.this, Emppage.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "successful item add ..", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });

                }



            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1996 && resultCode == RESULT_OK && data != null)
            ImageUri = data.getData();
        Toast.makeText(cashAddItem.this, "تم إضافة الصورة بنجاح", Toast.LENGTH_SHORT).show();}


}
