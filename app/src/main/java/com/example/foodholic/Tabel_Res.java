package com.example.foodholic;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Tabel_Res extends AppCompatActivity {

    FirebaseFirestore db;
    SharedPreferences shared;
    SharedPreferences shared2;

    TextView date, time;
    Button reserve, check;
    EditText name, mobile, people;

    DatePickerDialog.OnDateSetListener date_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shared = getSharedPreferences("delivery", MODE_PRIVATE);
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);

        if(shared2.getString("language", "").equals("arabic"))
            setContentView(R.layout.activity_tabel__res2);
        else
            setContentView(R.layout.activity_tabel__res);

        db = FirebaseFirestore.getInstance();

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        reserve = findViewById(R.id.reserv);
        check = findViewById(R.id.check);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        people = findViewById(R.id.people);

        final Calendar myCalendar = Calendar.getInstance();
        date_ = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                date.setText(sdf.format(myCalendar.getTime())); } };

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(Tabel_Res.this, date_, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(Tabel_Res.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                time.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();

            }
        });

        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("Res_1_Table").document("tabel")
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String temp = task.getResult().get("count").toString();

                        int count = Integer.parseInt(temp);

                        if(count > 0){
                            Map<String, Object> table = new HashMap<>();
                            table.put("count", --count);
                            db.collection("Res_1_Table").document("tabel").set(table)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    uploadData(name.getText().toString(), mobile.getText().toString(), date.getText().toString(),
                                            time.getText().toString(), people.getText().toString());
                                }
                            });
                        }

                        else
                            Toast.makeText(Tabel_Res.this, "No Tabels Available, Please Try Again later", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(shared2.getString("language", "").equals("arabic"))
                    downloadData();
                else
                    downloadData2();

            }
        });



    }

    public void uploadData(String user_name, String user_mobile, String date,
                           String time, String people){

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("user_name", user_name);
        reservation.put("user_mobile", user_mobile);
        reservation.put("date", date);
        reservation.put("time", time);
        reservation.put("people", people);

        if(shared.getString("mid", "").equals("")){
            SharedPreferences.Editor editor = shared.edit();

            editor.putString("mid", user_mobile);
            editor.apply();

            db.collection("Res_1_Reservation").document(shared.getString("mid", "")).set(reservation)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                Toast.makeText(Tabel_Res.this, "Operation Successful, Your Reservation Is Pending !", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(Tabel_Res.this, "Error, Please Try To Reserve Again Again !", Toast.LENGTH_SHORT).show();

                        }
                    });

        }
        else{
            db.collection("Res_1_Reservation").document(shared.getString("mid", "")).set(reservation)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                Toast.makeText(Tabel_Res.this, "Operation Successful, Your Reservation Is Pending !", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(Tabel_Res.this, "Error, Please Try To Reserve Again !", Toast.LENGTH_SHORT).show();

                        }
                    });
        }

    }

    public void downloadData(){

        db.collection("Res_1_Reservation").document(shared.getString("mid", "")).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        new AlertDialog.Builder(Tabel_Res.this)
                                .setTitle("Remove Your Reservation ?")
                                .setMessage("Name :"+task.getResult().get("user_name").toString()+"\n"+
                                        "Mobile : "+task.getResult().get("user_mobile").toString()+"\n"+
                                        "Date : "+task.getResult().get("date").toString()+"\n"+
                                        "Time : "+ task.getResult().get("time").toString()+"\n"+
                                        "Number Of People : "+task.getResult().get("people").toString())
                                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        db.collection("Res_1_Reservation")
                                                .document(shared.getString("mid", "")).delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                db.collection("Res_1_Table").document("tabel")
                                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        String temp = task.getResult().get("count").toString();
                                                        int count = Integer.parseInt(temp);

                                                        Map<String, Object> table = new HashMap<>();
                                                        table.put("count", ++count);
                                                        db.collection("Res_1_Table").document("tabel").set(table)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        Toast.makeText(Tabel_Res.this, "Reservation Removed !", Toast.LENGTH_LONG).show();
                                                                    }
                                                                });
                                                        } });
                                            } });



                                        Toast.makeText(Tabel_Res.this, "Reservation Removed !", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setCancelable(false).create().show();
                    }
                });

    }

    public void downloadData2(){

        db.collection("Res_1_Reservation").document(shared.getString("mid", "")).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        new AlertDialog.Builder(Tabel_Res.this)
                                .setTitle("الغاء الحجز ؟")
                                .setMessage("الأسم :"+task.getResult().get("user_name").toString()+"\n"+
                                        "الهاتف : "+task.getResult().get("user_mobile").toString()+"\n"+
                                        "التاريخ : "+task.getResult().get("date").toString()+"\n"+
                                        "الوقت  : "+ task.getResult().get("time").toString()+"\n"+
                                        "عدد الأشخاص : "+task.getResult().get("people").toString())
                                .setPositiveButton("الغاء", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        db.collection("Res_1_Reservation")
                                                .document(shared.getString("mid", "")).delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        db.collection("Res_1_Table").document("tabel")
                                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                String temp = task.getResult().get("count").toString();
                                                                int count = Integer.parseInt(temp);

                                                                Map<String, Object> table = new HashMap<>();
                                                                table.put("count", ++count);
                                                                db.collection("Res_1_Table").document("tabel").set(table)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                Toast.makeText(Tabel_Res.this, "تم الألغاء بنجاح", Toast.LENGTH_LONG).show();
                                                                            }
                                                                        });
                                                            } });
                                                    } });
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("لا", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setCancelable(false).create().show();
                    }
                });

    }

}
