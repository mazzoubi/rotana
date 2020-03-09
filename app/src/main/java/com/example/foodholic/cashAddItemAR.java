package com.example.foodholic;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class cashAddItemAR extends AppCompatActivity {

    RadioButton ar,en;
    Button button;
    EditText subItem,desic,point,cost,price,ketch;
    classSubItem ss;
    FirebaseFirestore db;
    SharedPreferences shared2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_add_item_ar);
        init();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ar.isChecked()){
                    if (subItem.getText().toString().isEmpty()){
                        if(shared2.getString("language", "").equals("arabic")) {
                            Toast.makeText(cashAddItemAR.this, "الرجاء ادخال اسم العنصر", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(cashAddItemAR.this, "please enter item name", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else if(desic.getText().toString().isEmpty())  {
                        if(shared2.getString("language", "").equals("arabic")) {
                            Toast.makeText(cashAddItemAR.this, "الرجاء ادخال الوصف", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(cashAddItemAR.this, "please enter description", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        addar();
                    }
                }
                else if (en.isChecked()){
                    if (subItem.getText().toString().isEmpty()){
                        if(shared2.getString("language", "").equals("arabic")) {
                            Toast.makeText(cashAddItemAR.this, "الرجاء ادخال اسم العنصر", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(cashAddItemAR.this, "please enter item name", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else if(desic.getText().toString().isEmpty())  {
                        if(shared2.getString("language", "").equals("arabic")) {
                            Toast.makeText(cashAddItemAR.this, "الرجاء ادخال الوصف", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(cashAddItemAR.this, "please enter description", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        adden();
                    }
                }
                else {
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(cashAddItemAR.this, "الرجاء اختيار اللغه", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(cashAddItemAR.this, "please choose language", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
    void init(){
        ss=aa3.subItems.get(getIntent().getIntExtra("ppos",-1));
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        db=FirebaseFirestore.getInstance();
        subItem=findViewById(R.id.itemSubItem);
        desic=findViewById(R.id.descr);
        point=findViewById(R.id.itemPoint);
        price=findViewById(R.id.itemPrice);
        cost=findViewById(R.id.itemCost);
        ketch=findViewById(R.id.kitchenNo);
        button=findViewById(R.id.buttonItemAdd);
        ar=findViewById(R.id.radioButtonAR);
        en=findViewById(R.id.radioButtonEN);
        if(shared2.getString("language", "").equals("arabic")) {
            button.setText("اضافه");
        }
        point.setText(ss.point);
        price.setText(ss.price+"");
        cost.setText(ss.cost);
        ketch.setText(ss.kitchen);
    }

    void addar(){
        Map<String, Object> reservation = new HashMap<>();
        String id=new Date()+"";
        reservation.put("item", ss.item);
        reservation.put("subItem", ss.subItem );
        reservation.put("image", ss.image);
        reservation.put("description", ss.description);
        reservation.put("Ar_description", desic.getText().toString());
        reservation.put("Ar_item", "");
        reservation.put("Ar_subItem", subItem.getText().toString());
        reservation.put("point", ss.point);
        reservation.put("kitchen", ss.kitchen);
        reservation.put("price",  ss.price);
        reservation.put("cost",  ss.cost);
        reservation.put("itemId", ss.itemId);



        db.collection("Res_1_subItem").document(ss.itemId).set(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(getApplicationContext(),"لقد تمت الاضافة بنجاح ..",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(cashAddItemAR.this, Emppage.class)); finish();}
                    else{
                        Toast.makeText(getApplicationContext(),"successful add ..",Toast.LENGTH_LONG).show();
                    } }  } });
    }

    void adden(){
        Map<String, Object> reservation = new HashMap<>();
        String id=new Date()+"";
        reservation.put("item", ss.item);
        reservation.put("subItem", subItem.getText().toString() );
        reservation.put("image", ss.image);
        reservation.put("description", desic.getText().toString());
        reservation.put("Ar_description", ss.Ar_description);
        reservation.put("Ar_item", "");
        reservation.put("Ar_subItem", ss.Ar_subItem);
        reservation.put("point", ss.point);
        reservation.put("kitchen", ss.kitchen);
        reservation.put("price",  ss.price);
        reservation.put("cost",  ss.cost);
        reservation.put("itemId", ss.itemId);



        db.collection("Res_1_subItem").document(ss.itemId).set(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(getApplicationContext(),"لقد تمت الاضافة بنجاح ..",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(cashAddItemAR.this, Emppage.class)); finish();}
                    else{
                        Toast.makeText(getApplicationContext(),"successful add ..",Toast.LENGTH_LONG).show();
                    } }  } });
    }
}
