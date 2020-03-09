package com.example.foodholic;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class pymentsCreatPill extends AppCompatActivity {

    TextView date;
    EditText pillNumber,note,coast,numberOfElement;
    Spinner empemail,supplier,pillitem;
    Button button;
    FirebaseFirestore db ;
    SharedPreferences shared2;
    ArrayList<classEmployee> emp;
    ArrayList<String> empString;
    ArrayList<classSupplier> suppliers;
    ArrayList<String>supplierString;
    ArrayList<classWarehouseItem> item;
    ArrayList<String>itemString;
    String myDatefrom=" / / ";
    DatePickerDialog.OnDateSetListener date_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pyments_creat_pill);
        init();

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
                date.setText(myDatefrom);
            } };
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(pymentsCreatPill.this, date_, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                //Toast.makeText(getApplicationContext(),"chose the dd/mm/yyyy in month you want add exit",Toast.LENGTH_LONG).show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (empemail.getSelectedItemId()==0){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(pymentsCreatPill.this, "الرجاء تحديد موظف", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(pymentsCreatPill.this, "please select employee", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (supplier.getSelectedItemId()==0){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(pymentsCreatPill.this, "الرجاء تحديد مورد", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(pymentsCreatPill.this, "please select supplier", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (pillitem.getSelectedItemId()==0){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(pymentsCreatPill.this, "الرجاء تحديد عنصر", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(pymentsCreatPill.this, "please select item", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (pillNumber.getText().toString().isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")) {

                        Toast.makeText(pymentsCreatPill.this, "الرجاء ادخال رقم الفاتورة", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(pymentsCreatPill.this, "please enter pill number", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(coast.getText().toString().isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(pymentsCreatPill.this, "الرجاء ادخال سعر التكلفه", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(pymentsCreatPill.this, "please enter the coast", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(numberOfElement.getText().toString().isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(pymentsCreatPill.this, "الرجاء ادخال عدد العناصر", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(pymentsCreatPill.this, "please enter the umber of element", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(myDatefrom.equals(" / / ")){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(pymentsCreatPill.this, "الرجاء ادخال التاريخ", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(pymentsCreatPill.this, "please enter the date", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    uploadPill();
                }
            }
        });
    }
    void init (){
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        db=FirebaseFirestore.getInstance();
        empemail=findViewById(R.id.empemail);
        pillNumber=findViewById(R.id.pillnumber);
        pillitem=findViewById(R.id.pillitem);
        note=findViewById(R.id.notes);
        supplier=findViewById(R.id.supplier);
        coast=findViewById(R.id.coast);
        date = findViewById(R.id.date);
        button =findViewById(R.id.save);
        numberOfElement=findViewById(R.id.coast2);
        loadEmp();
        loaditems();
        loadSupplier();
        if(shared2.getString("language", "").equals("arabic")) {
            //empemail.setHint("ايميل الموظف");
            pillNumber.setHint("رقم الفاتوره");
           // pillitem.setHint("عناصر الفاتوره");
            note.setHint("ملاحظات");
            //supplier.setHint("المورد");
            coast.setHint("سعر التكلفه");
            date.setText("التاريخ");
            numberOfElement.setHint("عدد العناصر");
            button.setText("حفظ");
        }
    }

    void uploadPill(){
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("empEmail", empemail.getSelectedItem().toString());
        reservation.put("pillNumber", pillNumber.getText().toString());
        reservation.put("items", pillitem.getSelectedItem().toString());
        reservation.put("supplier", supplier.getSelectedItem().toString());
        reservation.put("date", myDatefrom);
        reservation.put("coast", coast.getText().toString());
        reservation.put("desc", note.getText().toString());
        reservation.put("numberOfElement", numberOfElement.getText().toString());



        db.collection("Res_1_purchases").document(System.currentTimeMillis()+"").set(reservation)
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
    void loadSupplier(){
        supplierString=new ArrayList<>();
        suppliers = new ArrayList<>();
        if(shared2.getString("language", "").equals("arabic")) {
            supplierString.add("اختر مورد");
        }
        else {
            supplierString.add("select supplier");
        }
        db.collection("Res_1_suppliers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list){
                    suppliers.add(d.toObject(classSupplier.class));
                    supplierString.add(d.toObject(classSupplier.class).name);
                }
                ArrayAdapter<String>adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,supplierString);
                supplier.setAdapter(adapter);
            }

        });
    }
    void loadEmp(){
        emp=new ArrayList<>();
        empString =new ArrayList<>();
        if(shared2.getString("language", "").equals("arabic")) {
            empString.add("اختر موظف");
        }
        else {
            empString.add("select employee");
        }
        db.collection("Res_1_employee").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list =queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d : list){
                    classEmployee e =d.toObject(classEmployee.class);
                    if (e.warehousea||e.cashWork){
                        emp.add(e);
                        empString.add(e.email);
                    }

                }

                ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,empString);
                empemail.setAdapter(adapter);
                //ArrayAdapter<classEmployee> adapter=new empAdapter(getApplicationContext(),R.layout.rowemp,emp);
                //listView .setAdapter(adapter);
            }
        });
    }
    void loaditems(){
        item=new ArrayList<>();
        itemString=new ArrayList<>();
        if(shared2.getString("language", "").equals("arabic")) {
            itemString.add("اختر عنصر");
        }
        else {
            itemString.add("select item");
        }
        db.collection("Res_1_warehouse").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d : list){
                    classWarehouseItem a = d.toObject(classWarehouseItem.class);
                    item.add(a);
                    itemString.add(a.item);
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,itemString);
                pillitem.setAdapter(adapter);

            }
        });
    }
}
