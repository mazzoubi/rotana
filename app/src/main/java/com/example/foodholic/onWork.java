package com.example.foodholic;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class onWork extends AppCompatActivity {

    FirebaseFirestore db;
    Button button;
    TextView textView;
    ListView listView;
    private DatabaseReference root;
    ArrayList<classEmployee> emp=new ArrayList<>();
    ArrayAdapter<classEmployee> adapter;
    int pos=-1;
    SharedPreferences shared2;
    String myDatefrom=" / / ";
    DatePickerDialog.OnDateSetListener date_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_work);

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ///////////////////////////////////////////////////
        ///////////////////////////////////////////////////
        final Calendar myCalendar = Calendar.getInstance();
        date_ = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                String[] y = sdf.format(myCalendar.getTime()).split("/");
                myDatefrom = y[1] + "-" + y[0] + "-" + y[2];
            }};
        new DatePickerDialog(onWork.this, date_, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        // Toast.makeText(getApplicationContext(), "chose the dd/mm/yyyy in month you want show information", Toast.LENGTH_LONG).show();


        //////////////////////////////////////////////////
        //////////////////////////////////////////////////


        button = findViewById(R.id.addwork);
        textView = findViewById(R.id.showname);
        listView = findViewById(R.id.showemp);


        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        if(shared2.getString("language", "").equals("arabic")) {
            button.setText("غياب");
        }

        db = FirebaseFirestore.getInstance();
        db.collection("Res_1_employee").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                for (DocumentSnapshot d : list) {
                    classEmployee a = d.toObject(classEmployee.class);
                    emp.add(a);
                }
                adapter = new empAdapter(getApplicationContext(), R.layout.rowemp, emp);
                listView.setAdapter(adapter);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                textView.setText(emp.get(position).Fname + " " + emp.get(position).Lname);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myDatefrom.equals(" / / ")){
                    new DatePickerDialog(onWork.this, date_, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    //  Toast.makeText(getApplicationContext(), "chose the dd/mm/yyyy in month you want show information", Toast.LENGTH_LONG).show();

                }
                else{


                    if (textView.getText().toString().isEmpty()){
                        if(shared2.getString("language", "").equals("arabic")) {
                            Toast.makeText(getApplicationContext(), "لم يتم تحديد اي موظف ", Toast.LENGTH_LONG).show();

                        }
                        else
                            Toast.makeText(getApplicationContext(), "no emp selected return select ", Toast.LENGTH_LONG).show();
                    }
                    else {
                        root = FirebaseDatabase.getInstance().getReference();

                        Map<String, Object> map = new HashMap<String, Object>();
                        String temp_key = root.push().getKey();

                        Map<String, Object> reservation = new HashMap<>();
                        reservation.put("date", myDatefrom);
                        reservation.put("time", "day off");
                        reservation.put("email", emp.get(pos).email);
                        reservation.put("description", "no show ,no call");
                        reservation.put("extID", temp_key);
                        db.collection("Res_1_exitRequest").document(temp_key).set(reservation)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            if(shared2.getString("language", "").equals("arabic")) {
                                                Toast.makeText(onWork.this, "تمت اضافة الغيابات بنجاح", Toast.LENGTH_SHORT).show();

                                            }
                                            else
                                                Toast.makeText(onWork.this, "Operation Successful, Your you added exit !", Toast.LENGTH_SHORT).show();

                                        }
                                        else{
                                            if(shared2.getString("language", "").equals("arabic")) {
                                                Toast.makeText(onWork.this, "خطأ غير متوقع الرجاء اعادة المحاوله !", Toast.LENGTH_SHORT).show();

                                            }
                                            else
                                                Toast.makeText(onWork.this, "Error, Please Try To add exit Again !", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });
                    }}}
        });

    }
}
