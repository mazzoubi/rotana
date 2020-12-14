package com.example.rotanademo;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VisaReport extends AppCompatActivity {
    Button button;
    TextView result,date;
    ListView listView ;
    FirebaseFirestore db ;
    ArrayList<classPayment> payment;

    RadioButton day , month ,year ;

    DatePickerDialog.OnDateSetListener date_;
    String myDatefrom=" / / ";
    SharedPreferences shared2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_visa_report);



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

             /*   String []y=myDate.split("/");
                textView1.setText(y[1]+"-"+y[0]+"-"+y[2]);*/

            } };
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(VisaReport.this, date_, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                //   Toast.makeText(getApplicationContext(),"chose the dd/mm/yyyy in month you want show statistics",Toast.LENGTH_LONG).show();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myDatefrom.equals(" / / ")){
                    if(shared2.getString("language", "").equals("arabic")) {
                        Toast.makeText(VisaReport.this, "الرجاء اختيار التاريخ", Toast.LENGTH_SHORT).show();
                    }
                    else
                    Toast.makeText(VisaReport.this, "Please select the date", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (day.isChecked()){
                        paymentByDay(myDatefrom);
                    }
                    else if (month.isChecked()){
                        String[] s=myDatefrom.split("-");
                        paymentByMonth(s[1]+"-"+s[2]);
                    }
                    else if (year.isChecked()){
                        String[] s=myDatefrom.split("-");
                        paymentByYear(s[2]);
                    }
                    else {
                        if(shared2.getString("language", "").equals("arabic")) {
                            Toast.makeText(VisaReport.this, "الرجاء اختيار نوع البحث", Toast.LENGTH_SHORT).show();
                        }
                        else
                        Toast.makeText(VisaReport.this, "Please select the type of search", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    void init(){
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        db= FirebaseFirestore.getInstance();
        listView=findViewById(R.id.listView);
        button =findViewById(R.id.search);
        result =findViewById(R.id.resulte);
        date =findViewById(R.id.date);
        day =findViewById(R.id.radioButtonDay);
        month =findViewById(R.id.radioButtonmonth);
        year = findViewById(R.id.radioButtonyear);
        Date d =new Date();
        payment=loadPayments();
        if(shared2.getString("language", "").equals("arabic")) {
            day.setText("حسب اليوم");
            year.setText("حسب السنه");
            month.setText("حسب الشهر");
            date.setText("حدد التاريخ");
            result.setText(" ");
        }
        else {
            result.setText(" ");
        }
    }
    ArrayList<classPayment> loadPayments(){
        final ArrayList<classPayment> li=new ArrayList<>();
        db.collection("Res_1_visa").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d: list){
                    li.add(d.toObject(classPayment.class));
                }
            }
        });
        return li;
    }

    void paymentByDay(String date){
        double sum=0;
        ArrayList<classPayment>list=new ArrayList<>();
        for(classPayment d :payment){
            if (d.date.equals(date)){
                list.add(d);
                sum+=d.pay;
            }
        }
        result.setText("   visa sales= "+sum+" JOD   ");
        ArrayAdapter<classPayment> adapter=new adminAdapterPayment(getApplicationContext(),R.layout.row,list);
        listView.setAdapter(adapter);
    }
    void paymentByMonth(String date){
        double sum=0;
        ArrayList<classPayment>list=new ArrayList<>();
        String []s;
        for(classPayment d :payment){
            s=d.date.split("-");
            if (date.equals(s[1]+"-"+s[2])){
                list.add(d);
                sum+=d.pay;
            }
        }
        result.setText("   visa sales= "+sum+" JOD   ");
        ArrayAdapter<classPayment> adapter=new adminAdapterPayment(getApplicationContext(),R.layout.row,list);
        listView.setAdapter(adapter);
    }
    void paymentByYear(String date){
        double sum=0;
        ArrayList<classPayment>list=new ArrayList<>();


        for(classPayment d :payment){
            if (d.date.contains(date)){
                list.add(d);
                sum+=d.pay;
            }
        }
        if(shared2.getString("language", "").equals("arabic")) {
            result.setText("   مبيعات الفيزا= "+sum+Adminpage.currencyAndTax.currency +"   \n" +
                    "الضريبه= "+Adminpage.currencyAndTax.tax+"%" +
                    "\nقيمة الضريبه= "+(Adminpage.currencyAndTax.tax/100*sum)
                    +"\nالمجموع مع الضريبه = "+(sum-(Adminpage.currencyAndTax.tax/100*sum)));
        }
        else {
            result.setText("   visa sales= "+sum+Adminpage.currencyAndTax.currency +"   \n" +
                    "tax= "+Adminpage.currencyAndTax.tax+"%" +
                    "\ntax ammount= "+(Adminpage.currencyAndTax.tax/100*sum)
                    +"\nsum with taxs = "+(sum-(Adminpage.currencyAndTax.tax/100*sum)));
        }
        ArrayAdapter<classPayment> adapter=new adminAdapterPayment(getApplicationContext(),R.layout.row,list);
        listView.setAdapter(adapter);
    }
}
