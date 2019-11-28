package com.example.foodholic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class aa2 extends AppCompatActivity {

    int position;

    Button button, button2;
    TextView textView;
    EditText editText;

    double quantity;
    double give;
    FirebaseFirestore db ;
    SharedPreferences shared2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aa2);
        position=getIntent().getIntExtra("aa",0);

        textView=findViewById(R.id.aaShowItemDetail);
        button=findViewById(R.id.aaGet);
        button2=findViewById(R.id.aaAdd);
        editText=findViewById(R.id.aachosequant);


        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        if(shared2.getString("language", "").equals("arabic")){


            button.setText("سحب");
            button2.setText("اضافه");
            editText.setHint("الكميه");
            textView.setText("العنصر : "+warehouse.item.get(position).item
                    +"\n"+"الكميه : "+warehouse.item.get(position).quantity+" "+warehouse.item.get(position).quantityType);
        }
        else{
            textView.setText("The item is : "+warehouse.item.get(position).item
                    +"\n"+"Quantity is : "+warehouse.item.get(position).quantity+" "+warehouse.item.get(position).quantityType); }



        quantity=Double.parseDouble(warehouse.item.get(position).quantity);


        db=FirebaseFirestore.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try{
                    give=Double.parseDouble(editText.getText().toString());
                    if(editText.getText().toString().isEmpty()){
                        if(shared2.getString("language", "").equals("arabic")){}
                        else
                            Toast.makeText(getApplicationContext(),"the quantity filed is empty",Toast.LENGTH_LONG).show();
                    }

                    else if (quantity-give<0){
                        if(shared2.getString("language", "").equals("arabic")){}
                        else
                            Toast.makeText(getApplicationContext(),"you not have this quantity in your warehouse, please return input",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Map<String, Object> reservation = new HashMap<>();
                        reservation.put("item", warehouse.item.get(position).item);
                        reservation.put("quantity", quantity-give+"");
                        reservation.put("quantityType", warehouse.item.get(position).quantityType);

                        db.collection("Res_1_warehouse").document(warehouse.item.get(position).item).set(reservation)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            if(shared2.getString("language", "").equals("arabic")){
                                                Toast.makeText(aa2.this, "تمت العمليه بنجاح", Toast.LENGTH_SHORT).show();
                                                Intent n =new Intent(getApplicationContext(),warehouse.class);
                                                onBackPressed();
                                                startActivity(n);
                                            }
                                            else{
                                                Toast.makeText(aa2.this, "Operation Successful, Your get item", Toast.LENGTH_SHORT).show();
                                                Intent n =new Intent(getApplicationContext(),warehouse.class);
                                                onBackPressed();
                                                startActivity(n);
                                            }

                                        }
                                        else
                                        if(shared2.getString("language", "").equals("arabic")){
                                            Toast.makeText(aa2.this, "خطأ غير متوقع الرجاء اعادة المحاوله !", Toast.LENGTH_SHORT).show();

                                        }
                                        else
                                            Toast.makeText(aa2.this, "Error, Please Try To get Again !", Toast.LENGTH_SHORT).show();

                                    }
                                });


                    }}catch (Exception e){
                    if(shared2.getString("language", "").equals("arabic")){
                        Toast.makeText(getApplicationContext(),"حقل ادخال الكميه فارغ",Toast.LENGTH_LONG).show();

                    }
                    else
                        Toast.makeText(getApplicationContext(),"the quantity filed is empty",Toast.LENGTH_LONG).show();
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try{
                    give=Double.parseDouble(editText.getText().toString());
                    if(editText.getText().toString().isEmpty()){
                        if(shared2.getString("language", "").equals("arabic")){
                            Toast.makeText(getApplicationContext(),"حقل ادخال الكميه فارغ",Toast.LENGTH_LONG).show();

                        }
                        else
                            Toast.makeText(getApplicationContext(),"the quantity filed is empty",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Map<String, Object> reservation = new HashMap<>();
                        reservation.put("item", warehouse.item.get(position).item);
                        reservation.put("quantity", quantity+give+"");
                        reservation.put("quantityType", warehouse.item.get(position).quantityType);

                        db.collection("Res_1_warehouse").document(warehouse.item.get(position).item).set(reservation)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            if(shared2.getString("language", "").equals("arabic")){
                                                Toast.makeText(aa2.this, "تمت العمليه بنجاح", Toast.LENGTH_SHORT).show();
                                                Intent n =new Intent(getApplicationContext(),warehouse.class);
                                                onBackPressed();
                                                startActivity(n);
                                            }
                                            else{
                                                Toast.makeText(aa2.this, "Operation Successful, Your add item", Toast.LENGTH_SHORT).show();
                                                Intent n =new Intent(getApplicationContext(),warehouse.class);
                                                onBackPressed();
                                                startActivity(n);
                                            }

                                        }
                                        else
                                        if(shared2.getString("language", "").equals("arabic")){
                                            Toast.makeText(aa2.this, "خطأ غير متوقع الرجاء اعادة المحاوله !", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                            Toast.makeText(aa2.this, "Error, Please Try To add Again !", Toast.LENGTH_SHORT).show();

                                    }
                                });


                    }
                }catch (Exception e){
                    if(shared2.getString("language", "").equals("arabic")){
                        Toast.makeText(getApplicationContext(),"حقل ادخال الكميه فارغ",Toast.LENGTH_LONG).show();

                    }
                    else
                        Toast.makeText(getApplicationContext(),"the quantity filed is empty",Toast.LENGTH_LONG).show();

                }}
        });


    }
}
