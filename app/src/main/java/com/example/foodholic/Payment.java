package com.example.foodholic;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Payment extends AppCompatActivity {

    EditText   payment ,description ;
    TextView pydate ;
    Button done;
    FirebaseFirestore db;
    DatePickerDialog.OnDateSetListener date_;
    String myDatefrom=" / / ";
    Double pay=0.0;
    SharedPreferences shared2;
    SharedPreferences shared3;

    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_payment);

        email=getIntent().getStringExtra("emp");

        payment=(EditText)findViewById(R.id.payments);
        description=(EditText)findViewById(R.id.paymentsDesc);
        pydate=(TextView) findViewById(R.id.dateToday);
        done =(Button)findViewById(R.id.apply);

        Toolbar toolbar = findViewById(R.id.tool);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        shared3 = getSharedPreferences("cash", MODE_PRIVATE);

        if(shared2.getString("language", "").equals("arabic")) {
            payment.setHint("المبلغ المصروف");
            description.setHint("تفاصيل المصروف");
            pydate.setText("تاريخ الصرف");
            done.setText("+ إضافة المصروف +");
            toolbar.setTitle("الرجوع");
        }


        db = FirebaseFirestore.getInstance();

        final Calendar myCalendar = Calendar.getInstance();
        date_ = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                String []y=sdf.format(myCalendar.getTime()).split("/");
                myDatefrom=y[1]+"-"+y[0]+"-"+y[2];
                pydate.setText(myDatefrom);

             /*   String []y=myDate.split("/");
                textView1.setText(y[1]+"-"+y[0]+"-"+y[2]);*/

            } };


        pydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Payment.this, date_, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
             //   Toast.makeText(getApplicationContext(),"chose the dd/mm/yyyy in month you want show statistics",Toast.LENGTH_LONG).show();

            }
        });




        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {

                   if (myDatefrom.equals(" / / ")){
                       if(shared2.getString("language", "").equals("arabic")) {
                           Toast.makeText(getApplicationContext(), "حقل التاريخ فارغ الرجاء اعادة الادخال", Toast.LENGTH_LONG).show();
                       }
                       else
                       Toast.makeText(getApplicationContext(), "the date field is empty please return input", Toast.LENGTH_LONG).show();
                   }
                   else {
                   pay = 0.0;
                   pay += Double.parseDouble(payment.getText().toString());
                   db.collection("Res_1_payment").document(myDatefrom)
                           .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                           try {
                               pay += Double.parseDouble(task.getResult().get("pay").toString());
                           }catch (Exception e){};
                           uploadData(myDatefrom, pay,description.getText().toString());

                       }
                   });
               } } catch(Exception e){
                   if(shared2.getString("language", "").equals("arabic")) {
                       Toast.makeText(getApplicationContext(), "حقل المبلغ فارغ الرجاء اعادة الادخال", Toast.LENGTH_LONG).show();
                   }
                   else
                   Toast.makeText(getApplicationContext(), "the payment field is empty please return input", Toast.LENGTH_LONG).show();
               }

            }
        });

    }
    public void uploadData(String date, double pay,String desc){

        double paySum = Double.parseDouble(shared3.getString("pay", "0.0"));
        paySum+=pay;

        SharedPreferences.Editor editor = shared3.edit();
        editor.putString("pay", paySum+"");
        editor.apply();

        String dd=new Date()+"";
        String []time=dd.split(" ");
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", date);
        reservation.put("pay", pay);
        reservation.put("description", desc);
        reservation.put("time", time[3]);
        reservation.put("emp", email+"");


        db.collection("Res_1_payment").document(""+new Date()).set(reservation)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //     progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Operation Successful", Toast.LENGTH_SHORT).show();
                            onBackPressed();}
                        else
                            Toast.makeText(getApplicationContext(), "Error, Please Try Again !", Toast.LENGTH_SHORT).show();
                    }
                });



        }

}
