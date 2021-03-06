package com.example.rotanademo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class aa1 extends AppCompatActivity {


    EditText item , qant , quantType;
    Button button;
    Spinner spinner;
    FirebaseFirestore db ;
    //
    SharedPreferences shared2;
    String suppl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_aa1);



        button =findViewById(R.id.aaAdd);
        item =findViewById(R.id.aaItem);
        qant =findViewById(R.id.aaquantity);
        quantType =findViewById(R.id.aaqantitytype);
        spinner=findViewById(R.id.spinner);

        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        if(shared2.getString("language", "").equals("arabic")){
            button.setText("اضافه على المستودع");
            item.setHint("العنصر");
            quantType.setHint("نوع الكميه");
            qant.setHint("الكميه");

        }


        db=FirebaseFirestore.getInstance();
        loadSupplier();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){

                }
                else {
                    suppl=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (item.getText().toString().isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")){
                        Toast.makeText(getApplicationContext(),"حقل العنصر فارغ",Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(),"item feald is impty",Toast.LENGTH_LONG).show();
                }
                else if (qant.getText().toString().isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")){
                        Toast.makeText(getApplicationContext(),"حقل الكميه فارغ",Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(),"quantity feald is impty",Toast.LENGTH_LONG).show();
                }
                else if (quantType.getText().toString().isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")){
                        Toast.makeText(getApplicationContext(),"حقل نوع الكميه فارغ",Toast.LENGTH_LONG).show();

                    }
                    else
                        Toast.makeText(getApplicationContext(),"quantity type feald is impty",Toast.LENGTH_LONG).show();
                }
                else {

                    Map<String, Object> reservation = new HashMap<>();
                    reservation.put("item", item.getText().toString());
                    reservation.put("quantity", qant.getText().toString());
                    reservation.put("quantityType", quantType.getText().toString());
                    reservation.put("supplier", suppl);

                    boolean ch=false;

                    for (int i=0 ; i<warehouse.item.size();i++){
                        if (warehouse.item.get(i).item.equals( item.getText().toString())){
                            ch=true;
                            break;
                        }
                    }

                    if (ch){
                        if(shared2.getString("language", "").equals("arabic")){
                            Toast.makeText(aa1.this, "هذا العنصر موجود في المستودع الرجاء اعادة الادخال", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(aa1.this, "this item already exist , please return input", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        db.collection("Res_1_warehouse").document(item.getText().toString()).set(reservation)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            if(shared2.getString("language", "").equals("arabic")){
                                                Toast.makeText(aa1.this, "تمت العمليه بنجاح", Toast.LENGTH_SHORT).show();
                                                Intent n =new Intent(getApplicationContext(),warehouse.class);
                                                addNotification(suppl,item.getText().toString(),qant.getText().toString(),quantType.getText().toString());
                                                onBackPressed();
                                                startActivity(n);
                                            }
                                            else{
                                                Toast.makeText(aa1.this, "Operation Successful, Your added new item", Toast.LENGTH_SHORT).show();
                                                Intent n =new Intent(getApplicationContext(),warehouse.class);
                                                onBackPressed();
                                                startActivity(n);
                                            }

                                        }
                                        else
                                        if(shared2.getString("language", "").equals("arabic")){
                                            Toast.makeText(aa1.this, "خطأ غير متوقع الرجاء اعادة الادخال !", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                            Toast.makeText(aa1.this, "Error, Please Try To add Again !", Toast.LENGTH_SHORT).show();

                                    }
                                });


                    }
                }
            }
        });



    }
    void loadSupplier(){
        db.collection("Res_1_suppliers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //ArrayList<classSupplier> suppliers=new ArrayList<>();
                ArrayList<String> ss =new ArrayList<>();
                if(shared2.getString("language", "").equals("arabic")){
                    ss.add("اختار مورد");
                }
                else {
                    ss.add("select supplier");
                }
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d : list){
                    ss.add(d.toObject(classSupplier.class).name);
                }
                ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,ss);
                spinner.setAdapter(adapter);
            }
        });
    }
    void addNotification(String sup,String item,String cuant,String cuantt){
        String id =System.currentTimeMillis()+"";
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("title","اشعارات نظام المستودع");
        reservation.put("date", "");
        reservation.put("time", new Date()+"");
        reservation.put("desc", "تمت اضافة عنصر جديد على المستودع : "+"\nالعنصر: "+item+"\nالكميه: "+cuant+" "+cuantt+"\nالمورد: "+sup);
        reservation.put("state", "1");
        reservation.put("id", id);

        Map<String, Object> reservation1 = new HashMap<>();
        reservation1.put("title","Stock System notification");
        reservation1.put("date", "");
        reservation1.put("time", new Date()+"");
        reservation1.put("desc", "new item added in the stock : "+"\nthe item : "+item+"\n quantity: "+cuant+" "+cuantt+"\nsupplier: "+sup);
        reservation1.put("state", "1");
        reservation1.put("id", id);

        db.collection("Res_1_adminNotificationsAr").document(id).set(reservation)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //     progressBar.setVisibility(View.GONE);
                    }
                });
        db.collection("Res_1_adminNotificationsEn").document(id).set(reservation1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //     progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
