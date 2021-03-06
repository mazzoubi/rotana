package com.example.rotanademo;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class taxChange extends AppCompatActivity {

    FirebaseFirestore db;
    Button button;
    EditText editText;
    TextView taxdes,currentTax;
    Spinner currency;
    SharedPreferences shared2;
    String curr="";
    double tax=0;
    classCurrencyAndTax currencyAndTax=Adminpage.currencyAndTax;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_tax_change);
    init();
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (editText.getText().toString().isEmpty()){
                tax=0;
            }
            else {
                tax = Double.parseDouble(editText.getText().toString());
            }
            upload();
        }
    });

    currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            curr=parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });

    }

    void init(){
        db=FirebaseFirestore.getInstance();
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        button =findViewById(R.id.button);
        editText=findViewById(R.id.editText);
        currentTax=findViewById(R.id.currentTax);
        taxdes=findViewById(R.id.textView46);
        currency=findViewById(R.id.spinner2);
        if(shared2.getString("language", "").equals("arabic")){
            button.setText("حفظ");
            currentTax.setText("الضريبه الحالية = "+currencyAndTax.tax+"%");
            taxdes.setText("قيمة الضريبه");

        }
    }
    void upload (){

        String [] temp = curr.split(",");

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("tax", tax);
        reservation.put("currencyAr",temp[1]);
        reservation.put("currency",temp[0]);

        Adminpage.currencyAndTax.tax=tax;
        Adminpage.currencyAndTax.currency=curr;


        db.collection("Res_1_currencyAndTax").document("onlyOne").set(reservation)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(shared2.getString("language", "").equals("arabic")) {
                                Toast.makeText(getApplicationContext(), "تمت العمليه بنجاح", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Operation Successful", Toast.LENGTH_SHORT).show();
                            onBackPressed();}
                        else{
                            if(shared2.getString("language", "").equals("arabic")) {
                                Toast.makeText(getApplicationContext(), "خطأ غير متوقع, الرجاء اعادة المحاوله", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Error, Please Try Again !", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }


}
