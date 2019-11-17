package com.example.foodholic;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

public class exit extends AppCompatActivity {
    String myDatefrom=" / / ";
    DatePickerDialog.OnDateSetListener date_;
    private DatabaseReference root;
    FirebaseFirestore db ;
    TextView textView,textView2,textView9;
    Button button,showExit;
    EditText editText;
    Spinner spinner;
    String extDate="";
    String extTime="";
    String extDesc="";
    int lang=0;
    ArrayList<classExit> ex=new ArrayList<>();
    ArrayList<classExit>exInday=new ArrayList<>();
    int position;
    SharedPreferences shared2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);

        textView=findViewById(R.id.exitDate);

        textView9=findViewById(R.id.textView9);
        button=findViewById(R.id.addExit);
        showExit=findViewById(R.id.showExit);
        spinner=findViewById(R.id.spinner_time);
        editText=findViewById(R.id.exitdesc);
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        if(shared2.getString("language", "").equals("arabic")){


            ArrayList<String>array=new ArrayList<>();
            array.add("اختيار مدة الاجازه");
            array.add("ساعه");
            array.add("ساعتين");
            array.add("ثلاث ساعات");
            array.add("يوم كامل");
            ArrayAdapter<String>adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,array);
            spinner.setAdapter(adapter);
            button.setText("اضافة الاجازه");
            showExit.setText("الاجازات في هذا اليوم");
            editText.setHint("تفاصيل الاجازه");
            textView.setText("تاريخ الاجازه: يوم-شهر-سنه");
            lang=1;
        }

        position=getIntent().getIntExtra("pos",0);
        db=FirebaseFirestore.getInstance();
        db.collection("Res_1_exitRequest").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d : list){
                    ex.add(d.toObject(classExit.class));
                }
            }
        });

        showExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dayoff=0,hour1=0,hour2=0,hour3=0;
         if (myDatefrom.equals(" / / ")){
             if(shared2.getString("language", "").equals("arabic")) {
                 Toast.makeText(getApplicationContext(), "الرجاء اختيار التاريخ اولا", Toast.LENGTH_LONG).show();
             }
             else {Toast.makeText(getApplicationContext(), "please choose the date", Toast.LENGTH_LONG).show();}
             }
         else {
             for (classExit d : ex) {
                 if (d.date.equals(myDatefrom)) {
                     exInday.add(d);
                     if (d.time.equals("day off")) {
                         dayoff++;
                     } else if (d.time.equals("1")) {
                         hour1++;
                     } else if (d.time.equals("2")) {
                         hour2++;
                     } else if (d.time.equals("3")) {
                         hour3++;
                     }
                 }
             }

             if(shared2.getString("language", "").equals("arabic")) {
                 textView9.setText("الكل : "+exInday.size()+"\n"+
                         "يوم اجازه : "+dayoff +"\n"+"ساعه : "+hour1+
                         "\n"+"ساعتين : "+hour2+"\n"+
                         "ثلاث ساعات : "+hour3 );
             }
             else {
                 textView9.setText("all : "+exInday.size()+"\n"+
                         "day off : "+dayoff +"\n"+"one hour : "+hour1+
                         "\n"+"tow hours : "+hour2+"\n"+
                         "three hours : "+hour3 );
             }
             //////////////////////////////////////////////
         }


            }
        });
        extDesc=editText.getText().toString();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        extTime="1";
                        break;
                    case 2:
                        extTime="2";
                        break;
                    case 3 :
                        extTime="3";
                        break;
                    case 4 :
                        extTime="day off";
                        break ;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                textView.setText(myDatefrom);
                extDate=myDatefrom;

             /*   String []y=myDate.split("/");
                textView1.setText(y[1]+"-"+y[0]+"-"+y[2]);*/

            } };
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(exit.this, date_, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                //Toast.makeText(getApplicationContext(),"chose the dd/mm/yyyy in month you want add exit",Toast.LENGTH_LONG).show();
            }
        });

        db=FirebaseFirestore.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extDesc=editText.getText().toString();
                if (extDate.isEmpty()){
                    if (lang==1){Toast.makeText(getApplicationContext(),"الرجاء ادخال تاريخ الاجازه او المغادره",Toast.LENGTH_LONG).show();}
                    else { Toast.makeText(getApplicationContext(),"please enter exit date",Toast.LENGTH_LONG).show();}

                }
                else if (extTime.isEmpty()){
                    if (lang==1){Toast.makeText(getApplicationContext(),"الرجاء ادخال مدة الاجازه المغادره",Toast.LENGTH_LONG).show();}
                    else{Toast.makeText(getApplicationContext(),"please enter exit time",Toast.LENGTH_LONG).show();}

                }
                else if (extDesc.isEmpty()){
                    if (lang==1){Toast.makeText(getApplicationContext(),"الرجاء ادخال وصف الاجازه او المغادره",Toast.LENGTH_LONG).show();}
                    else{Toast.makeText(getApplicationContext(),"please enter the description of exit",Toast.LENGTH_LONG).show();}

                }
                else {

                    root = FirebaseDatabase.getInstance().getReference();
                    Map<String, Object> map = new HashMap<String, Object>();
                   String temp_key = root.push().getKey();

                    Map<String, Object> reservation = new HashMap<>();
                    reservation.put("date", extDate);
                    reservation.put("time", extTime);
                    reservation.put("email", employeeCreatInformation.emp.get(position).email);
                    reservation.put("description", extDesc);
                    reservation.put("extID", temp_key);

                    db.collection("Res_1_exitRequest").document(temp_key).set(reservation)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        if (lang==1){
                                            Toast.makeText(exit.this, "تمت اضافة الاجازه بنجاح", Toast.LENGTH_SHORT).show();

                                        }
                                        else{
                                            Toast.makeText(exit.this, "Operation Successful, Your you added exit ", Toast.LENGTH_SHORT).show();

                                        }
                                    onBackPressed();}
                                    else
                                    if (lang==1){
                                        Toast.makeText(exit.this, "حدث خطأ غير متوقع الرجاء اعادة المحاوله !", Toast.LENGTH_SHORT).show();

                                    }
                                    else{
                                        Toast.makeText(exit.this, "Error, Please Try To add exit Again !", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });


                }
            }
        });

    }
}
