package com.example.foodholic;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ContestsActiivty extends AppCompatActivity {

    FirebaseFirestore db;
    ListView listView;

    ArrayAdapter<String> adapter;
    ArrayList<String> data;
    ArrayList<String> arr;

    String da="", ta="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(HomeAct.lang == 1)
            setContentView(R.layout.activity_contests_actiivty);
        else
            setContentView(R.layout.activity_contests_actiivty2);

        db = FirebaseFirestore.getInstance();

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.list);
        data = new ArrayList<>();
        arr = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.items_row3, R.id.item, arr);
        listView.setAdapter(adapter);

        getData();

        Button add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(ContestsActiivty.this);
                LayoutInflater inflater2 = ContestsActiivty.this.getLayoutInflater();
                builder2.setView(inflater2.inflate(R.layout.cont, null));
                final android.support.v7.app.AlertDialog dialog2 = builder2.create();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                dialog2.show();

                final EditText et1, et3;
                final TextView et2;
                Button send;

                et1 = dialog2.findViewById(R.id.pay);
                et2 = dialog2.findViewById(R.id.pay2);
                et3 = dialog2.findViewById(R.id.pay3);
                send = dialog2.findViewById(R.id.visa);

                et2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getDateTime(dialog2);

                    }
                });

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(HomeAct.lang == 1){
                            UploadData("عنوان الإيفينت : "+et1.getText().toString()+"\n\n"+
                                    "موعد الإيفينت : "+et2.getText().toString()+"\n\n"+
                                    "تفاصيل الإيفينت : "+et3.getText().toString()+"\n\n");
                        }
                        else{
                            UploadData("Event Title : "+et1.getText().toString()+"\n\n"+
                                    "Event Date : "+et2.getText().toString()+"\n\n"+
                                    "Event Details : "+et3.getText().toString()+"\n\n");
                        }
                        dialog2.dismiss();

                    }
                });
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                if(HomeAct.lang == 1){
                    new AlertDialog.Builder(ContestsActiivty.this)
                            .setTitle("يرجى التأكيد")
                            .setMessage("هل ترغب بحذف هذه المسابقة ؟ ")
                            .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setPositiveButton("حذف", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String s = data.get(position);

                            db.collection("Res_1_Contest")
                                    .document(s.substring(s.indexOf("@@@")+3))
                                    .delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                Toast.makeText(ContestsActiivty.this, "تم الحذف بنجاح !", Toast.LENGTH_LONG).show();
                                                recreate();
                                            }

                                        }
                                    });

                        }
                    }).show();
                }
                else{
                    new AlertDialog.Builder(ContestsActiivty.this)
                            .setTitle("Alert !")
                            .setMessage("Delete This Contest/Info ?")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String s = data.get(position);

                            db.collection("Res_1_Contest")
                                    .document(s.substring(s.indexOf("@@@")+3))
                                    .delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                Toast.makeText(ContestsActiivty.this, "Removed !", Toast.LENGTH_LONG).show();
                                                recreate();
                                            }

                                        }
                                    });

                        }
                    }).show();
                }

            }
        });

    }

    public void getDateTime(final android.support.v7.app.AlertDialog dialog2) {

        final Calendar cal = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener lsnr = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                da = sdf.format(cal.getTime());

                TextView et2 = dialog2.findViewById(R.id.pay2);
                et2.setText(da+"   "+ta);

            } };

        new DatePickerDialog(ContestsActiivty.this, lsnr, cal
                .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show();

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog lsn = new TimePickerDialog(ContestsActiivty.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        ta = hourOfDay + ":" + minute;

                    }
                }, hour, minute, false);
        lsn.show();


    }

    private void UploadData(String str) {

        String da = getIntent().getStringExtra("date");
        Map<String, Object> map = new HashMap<>();
        map.put("date", da);
        map.put("msg", str);

        db.collection("Res_1_Contest")
                .add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()) {
                    if(HomeAct.lang == 1)
                    Toast.makeText(ContestsActiivty.this, "تم النشر بنجاح !", Toast.LENGTH_LONG).show();
                   else
                    Toast.makeText(ContestsActiivty.this, "Done !", Toast.LENGTH_LONG).show();
                    recreate(); }
            }
        });

    }

    private void getData() {

        data.clear();
        arr.clear();
        adapter.notifyDataSetChanged();

        db.collection("Res_1_Contest").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful())
                            for(QueryDocumentSnapshot document : task.getResult())
                                AddData(document.get("msg").toString()+"@@@"+document.getId());
                    }
                });

    }

    private void AddData(String msg) {

        data.add(msg);
        arr.add(msg.substring(0, msg.indexOf("@@@")));
        adapter.notifyDataSetChanged(); }


}
