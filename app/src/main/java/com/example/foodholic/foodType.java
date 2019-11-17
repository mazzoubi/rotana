package com.example.foodholic;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class foodType extends AppCompatActivity {



    FirebaseFirestore db ;
    ArrayList<classFood> food=new ArrayList<>();
    ArrayList<classFood> all=new ArrayList<>();
    ArrayList<String> allmeals=new ArrayList<>();
    TextView textView ,textView2;
    Spinner spinnerN,spinnerT;


    String myDatefrom=" / / ";
    DatePickerDialog.OnDateSetListener date_;


    ListView name , type, info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_type);

        info = findViewById(R.id.listinfo);
        textView = findViewById(R.id.textView8);
        textView2 = findViewById(R.id.textView14);
        spinnerN =findViewById(R.id.spinnerName);
        spinnerT =findViewById(R.id.spinnerType);

        db=FirebaseFirestore.getInstance();




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
                myDatefrom=y[0]+"-"+y[2];
                textView2.setText(myDatefrom);

             /*   String []y=myDate.split("/");
                textView1.setText(y[1]+"-"+y[0]+"-"+y[2]);*/

            } };
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(foodType.this, date_, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                Toast.makeText(getApplicationContext(),"chose the dd/mm/yyyy in month you want add exit",Toast.LENGTH_LONG).show();

                food=new ArrayList<>();
                all=new ArrayList<>();
                allmeals=new ArrayList<>();
                db.collection("Res_1_Sales").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list= queryDocumentSnapshots.getDocuments();

                        db=FirebaseFirestore.getInstance();
                        for(DocumentSnapshot d  : list){

                            classFood a = d.toObject(classFood.class);
                            food.add(a);

                            if (allmeals.isEmpty()) {
                                allmeals.add(a.title);
                            }
                            else{
                                boolean x = false;
                                for(int i =0 ; i<allmeals.size();i++){
                                    if (allmeals.get(i).equalsIgnoreCase(a.title)){
                                        x=true;
                                        break;
                                    }
                                }
                                if (x){}
                                else {allmeals.add(a.title);}
                            }


                        }
                        String []nn=new String[allmeals.size()+1];
                        nn[0]="chose food";
                        for(int i=1;i<allmeals.size()+1;i++){
                            nn[i]=allmeals.get(i-1);
                        }
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,nn);
                        spinnerN.setAdapter(adapter);

                    }
                });

            }
        });

        spinnerT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String n =parent.getItemAtPosition(position).toString();
                ArrayList<classFood> arrayList = new ArrayList<>();
                ArrayList<String> arrayListstring = new ArrayList<>();
                double result =0 ;
                String da;
                for (int i=0 ; i<food.size();i++){
                    da=food.get(i).date;
                    String []z=da.split("-");
                    if (food.get(i).name.equalsIgnoreCase(n)&& myDatefrom.equals(z[1]+"-"+z[2])){
                        result+=Double.parseDouble(food.get(i).price);
                        arrayList.add(food.get(i));
                        arrayListstring.add(food.get(i).name);
                    }
                }
                ArrayAdapter<String>arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,arrayListstring);
                info.setAdapter(arrayAdapter);
                textView.setText("salse the "+n +"  is : "+result+"  JD");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String n=parent.getItemAtPosition(position).toString();
                ArrayList<String> ss=new ArrayList<>();
                for (int i=0 ; i<food.size();i++){
                    if (food.get(i).title.equalsIgnoreCase(n)){
                        ss.add(food.get(i).name);
                    }
                }

                String []array=new String[ss.size()+1];
                array[0]="chose";
                for(int i=1;i<ss.size()+1;i++){
                    array[i]=ss.get(i-1);
                }
                ArrayAdapter<String>rr=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,array);
                spinnerT.setAdapter(rr);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}
