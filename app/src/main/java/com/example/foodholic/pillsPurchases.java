package com.example.foodholic;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
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

public class pillsPurchases extends AppCompatActivity {
    SharedPreferences shared2;
    ListView listView;
    FirebaseFirestore db;
    String supplier="";
    Button button;
    EditText editText;
    RadioButton day,month,year;
    TextView textView,pilljoin;
    String myDatefrom=" / / ";
    ArrayList<classPaymentCreatPill> paymentCreatPills;
    ArrayList<classPaymentCreatPill> paymentCreatPillsAfterformatting;
    public static classPaymentCreatPill pp =new classPaymentCreatPill();
    DatePickerDialog.OnDateSetListener date_;


    ArrayList<classSupplier> suppliers;
    ArrayList<String>supplierString;
    ArrayList<classWarehouseItem> item;
    ArrayList<String>itemString;

    Spinner supp,iteem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pills_purchases);

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
                textView.setText(myDatefrom);
            } };
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(pillsPurchases.this, date_, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                //Toast.makeText(getApplicationContext(),"chose the dd/mm/yyyy in month you want add exit",Toast.LENGTH_LONG).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().isEmpty()){
                    if (myDatefrom.equals(" / / ")){
                        if(shared2.getString("language", "").equals("arabic")) {
                            Toast.makeText(pillsPurchases.this, "الرجاء ادخال التاريخ", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(pillsPurchases.this, "pleas enter the date", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        if (year.isChecked()){
                            paymentCreatPillsAfterformatting=new ArrayList<>();
                            String [] date = myDatefrom.split("-");
                            for (int i=0 ; i<paymentCreatPills.size();i++){
                                if (paymentCreatPills.get(i).date.contains(date[2])){
                                    paymentCreatPillsAfterformatting.add(paymentCreatPills.get(i));
                                }
                            }
                            ArrayAdapter adapter=new adapterPurchases(getApplicationContext(),R.layout.row,paymentCreatPillsAfterformatting);
                            listView.setAdapter(adapter);
                        }
                        else if (month.isChecked()){
                            paymentCreatPillsAfterformatting=new ArrayList<>();
                            String [] date = myDatefrom.split("-");
                            String []s ;
                            String ss ="";
                            for (int i=0 ; i<paymentCreatPills.size();i++){
                                s=paymentCreatPills.get(i).date.split("-");
                                ss=s[1]+"-"+ s[2];
                                if (ss.equals(date[1]+"-"+date[2])){
                                    paymentCreatPillsAfterformatting.add(paymentCreatPills.get(i));
                                }
                            }
                            ArrayAdapter adapter=new adapterPurchases(getApplicationContext(),R.layout.row,paymentCreatPillsAfterformatting);
                            listView.setAdapter(adapter);

                        }
                        else if (day.isChecked()){
                            paymentCreatPillsAfterformatting=new ArrayList<>();
                            for (int i=0 ; i<paymentCreatPills.size();i++){
                                if (paymentCreatPills.get(i).date.equals(myDatefrom)){
                                    paymentCreatPillsAfterformatting.add(paymentCreatPills.get(i));
                                }
                            }
                            ArrayAdapter adapter=new adapterPurchases(getApplicationContext(),R.layout.row,paymentCreatPillsAfterformatting);
                            listView.setAdapter(adapter);
                        }
                        else {
                            if(shared2.getString("language", "").equals("arabic")) {
                                Toast.makeText(pillsPurchases.this, "الرجاء اختيار طريقة البحث", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(pillsPurchases.this, "pleas choose type of search", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }
                else {

                    if (editText.getText().toString().isEmpty()){

                    }
                    else {
                        paymentCreatPillsAfterformatting=new ArrayList<>();
                        for (int i=0 ; i<paymentCreatPills.size();i++){
                            if (paymentCreatPills.get(i).pillNumber.equals(editText.getText().toString())){
                                paymentCreatPillsAfterformatting.add(paymentCreatPills.get(i));
                            }
                        }
                        ArrayAdapter adapter=new adapterPurchases(getApplicationContext(),R.layout.row,paymentCreatPillsAfterformatting);
                        listView.setAdapter(adapter);

                    }

                        /*
                        if (year.isChecked()){
                            paymentCreatPillsAfterformatting=new ArrayList<>();
                            String [] date = myDatefrom.split("-");
                            for (int i=0 ; i<paymentCreatPills.size();i++){
                                if (paymentCreatPills.get(i).date.contains(date[2])||paymentCreatPills.get(i).pillNumber.equals(editText.getText().toString())){
                                    paymentCreatPillsAfterformatting.add(paymentCreatPills.get(i));
                                }
                            }
                            ArrayAdapter adapter=new adapterPurchases(getApplicationContext(),R.layout.row,paymentCreatPillsAfterformatting);
                            listView.setAdapter(adapter);
                        }
                        else if (month.isChecked()){
                            paymentCreatPillsAfterformatting=new ArrayList<>();
                            String [] date = myDatefrom.split("-");
                            String []s ;
                            String ss ="";
                            for (int i=0 ; i<paymentCreatPills.size();i++){
                                s=paymentCreatPills.get(i).date.split("-");
                                ss=s[1]+"-"+ s[2];
                                if (ss.equals(date[1]+"-"+date[2])||paymentCreatPills.get(i).pillNumber.equals(editText.getText().toString())){
                                    paymentCreatPillsAfterformatting.add(paymentCreatPills.get(i));
                                }
                            }
                            ArrayAdapter adapter=new adapterPurchases(getApplicationContext(),R.layout.row,paymentCreatPillsAfterformatting);
                            listView.setAdapter(adapter);

                        }
                        else if (day.isChecked()){
                            paymentCreatPillsAfterformatting=new ArrayList<>();
                            for (int i=0 ; i<paymentCreatPills.size();i++){
                                if (paymentCreatPills.get(i).date.equals(myDatefrom) ||paymentCreatPills.get(i).pillNumber.equals(editText.getText().toString()) ){
                                    paymentCreatPillsAfterformatting.add(paymentCreatPills.get(i));
                                }
                            }
                            ArrayAdapter adapter=new adapterPurchases(getApplicationContext(),R.layout.row,paymentCreatPillsAfterformatting);
                            listView.setAdapter(adapter);
                        }
                        else {
                            if(shared2.getString("language", "").equals("arabic")) {
                                Toast.makeText(suppliersItem.this, "الرجاء اختيار طريقة البحث", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(suppliersItem.this, "pleas choose type of search", Toast.LENGTH_SHORT).show();
                            }
                        }
                         */


                }
            }
        });

        pilljoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(pillsPurchases.this, "الرجاء ادخال رقم الفاتورة", Toast.LENGTH_SHORT).show();
                    }
                    else {Toast.makeText(pillsPurchases.this, "please enter the pill number", Toast.LENGTH_SHORT).show();}
                }
                else {
                    paymentCreatPillsAfterformatting=new ArrayList<>();
                    for (int i=0 ; i<paymentCreatPills.size();i++){
                        if (paymentCreatPills.get(i).pillNumber.equals(editText.getText().toString())){
                            paymentCreatPillsAfterformatting.add(paymentCreatPills.get(i));
                        }
                    }
                    ArrayAdapter adapter=new adapterPurchases(getApplicationContext(),R.layout.row,paymentCreatPillsAfterformatting);
                    listView.setAdapter(adapter);

                }
            }
        });
    }
    void init(){
        supplier=getIntent().getStringExtra("sup");
        db=FirebaseFirestore.getInstance();
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        button =findViewById(R.id.button7);
        day=findViewById(R.id.radioButton5);
        month=findViewById(R.id.radioButton4);
        year=findViewById(R.id.radioButton);
        textView=findViewById(R.id.textView4);
        editText=findViewById(R.id.editText3);
        supp=findViewById(R.id.supplier);
        iteem=findViewById(R.id.item);
        pilljoin=findViewById(R.id.textView5);
        if(shared2.getString("language", "").equals("arabic")) {
            day.setText("اليوم");
            month.setText("الشهر");
            year.setText("السنه");
            textView.setText("التاريخ");
            editText.setHint("رقم الفاتورة,(اختياري)");
            pilljoin.setText("دمج تقارير");
        }

        listView=findViewById(R.id.listView);
        loadpill();
        loaditems();
        loadSupplier();
    }

    void loadpill(){
        paymentCreatPills=new ArrayList<>();
        paymentCreatPillsAfterformatting=new ArrayList<>();
        db.collection("Res_1_purchases").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list){

                        paymentCreatPills.add(0,d.toObject(classPaymentCreatPill.class));
                        paymentCreatPillsAfterformatting.add(0,d.toObject(classPaymentCreatPill.class));

                }
                ArrayAdapter adapter=new adapterPurchases(getApplicationContext(),R.layout.row,paymentCreatPills);
                listView.setAdapter(adapter);
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
                supp.setAdapter(adapter);
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
                iteem.setAdapter(adapter);

            }
        });
    }
}
