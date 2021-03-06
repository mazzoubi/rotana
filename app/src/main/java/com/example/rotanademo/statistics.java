package com.example.rotanademo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class statistics extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener date_;
    DatePickerDialog.OnDateSetListener date_to;
    String myDatefrom=" / / ";
    String myDateto=" / / ";
    String myDatetoDay=" / / ";
    Button search,searchyear,refresh, moredetale,chart;

    double sale ;
    double pay ;
    double salCost=0;
    double payCost=0;
    ArrayList<classPayment>cpay=new ArrayList<>();
    ArrayList<classSales>csal=new ArrayList<>();

    ArrayAdapter<classPayment>payadpt;
    ArrayAdapter<classSales>salAdapt;
    ListView listsale,listpay;
    SharedPreferences shared2;
    TextView t1,t2,monyResult;
    Spinner spinner;
    int spinnerpos=0;
    boolean ch=false;
    classCurrencyAndTax currencyAndTax=Adminpage.currencyAndTax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_statistics);

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        csal=new ArrayList<>();
        cpay=new ArrayList<>();

        spinner=findViewById(R.id.spinner_typeSearch);
        listpay=(ListView)findViewById(R.id.listpay);
        listsale=(ListView)findViewById(R.id.listsale);

        String []n={"search type","search by year","search by month","search by day","search by emp","search by item"};
        String []m={"طريقة البحث","حسب السنه","حسب الشهر","حسب اليوم","حسب الموظف","حسب العنصر"};

        ArrayAdapter<String> adda=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,n);
        spinner.setAdapter(adda);

        final TextView textView =(TextView)findViewById(R.id.ddda);
        //final TextView textView2 =(TextView)findViewById(R.id.ddda3);
        monyResult =(TextView)findViewById(R.id.moneyResult);

        search=(Button)findViewById(R.id.button_search);
       // moredetale=(Button)findViewById(R.id.button_search3);
       //  searchyear=(Button)findViewById(R.id.button_search2);
        refresh=(Button)findViewById(R.id.button_refresh);
        chart=findViewById(R.id.button_chart);
        t1=findViewById(R.id.ddda7);
        t2=findViewById(R.id.ddda5);

        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        if(shared2.getString("language", "").equals("arabic")) {
           // textView2.setText("عرض في السنه");
            textView.setText("عرض في الشهر");
//            moredetale.setText("تفاصيل اكثر");
            t1.setText("المبيعات");
            t2.setText("التكاليف");
            monyResult.setText("التكاليف : " + 0 + " " + " | المبيعات : " + 0 + " " + " | الارباح : " + 0 + " ");
            adda=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,m);
            spinner.setAdapter(adda);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerpos=position;
                switch (position){
                    case 4:
                        Intent n=new Intent(getApplicationContext(),statisticsByEmp.class);
                        startActivity(n);
                        break;
                    case 5:
                        Intent r=new Intent(getApplicationContext(),statisticsByItem.class);
                        startActivity(r);
                        break;
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
                myDatefrom=y[0]+"-"+y[2];
                myDateto=y[2];
                myDatetoDay=y[1]+"-"+y[0]+"-"+y[2];
                textView.setText(myDatefrom);

             /*   String []y=myDate.split("/");
                textView1.setText(y[1]+"-"+y[0]+"-"+y[2]);*/

            } };

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(statistics.this, date_, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();




            }
        });



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (spinnerpos == 0) {
                    if(shared2.getString("language", "").equals("arabic")) {
                        monyResult.setText("الرجاء اختيار نوع البحث");
                    }
                    else
                        Toast.makeText(getApplicationContext(), "please select the search type", Toast.LENGTH_LONG).show();
                } else if (spinnerpos == 1) {
                    ch=true;
                    csal=new ArrayList<>();
                    cpay=new ArrayList<>();
                    double proft=0;
                    salCost=0;
                    payCost=0;
                    ///////////////////////////////////////////////////////
                    //////////////////////////////////////////////////////
                    {
                        for (int i = 0; i < Adminpage.cSale.size(); i++) {
                            String[] a = (Adminpage.cSale.get(i).date).split("-");
                            if (( a[2]).equals(myDateto)) {
                                csal.add(Adminpage.cSale.get(i));
                                salCost+=Adminpage.cSale.get(i).sale;
                            }

                        }
                        salAdapt = new adminAdapterSales(getApplicationContext(), R.layout.row, csal);
                        listsale.setAdapter(salAdapt);
                    }
                    ///////////////////////////////////////////////////////
                    ///////////////////////////////////////////////////////

                    {
                        for (int i = 0; i < Adminpage.cPyment.size(); i++) {
                            String[] a = (Adminpage.cPyment.get(i).date).split("-");
                            if (( a[2]).equals(myDateto)) {
                                cpay.add(Adminpage.cPyment.get(i));
                                payCost+=Adminpage.cPyment.get(i).pay;
                            }

                        }
                        payadpt = new adminAdapterPayment(getApplicationContext(), R.layout.row, cpay);
                        listpay.setAdapter(payadpt);
                    }

                    if(shared2.getString("language", "").equals("arabic")) {
                        monyResult.setText("التكاليف: " + payCost +" "+currencyAndTax.currency + "      | " + "المبيعات: " + salCost + " "+
                                currencyAndTax.currency +"     \n\n"+"الضريبه: "+currencyAndTax.tax+
                                "%"+"    قيمة الضريبه: "+((currencyAndTax.tax/100*salCost)) + "\n     الارباح: " +
                                (salCost - payCost-((currencyAndTax.tax/100*salCost))) + " "+currencyAndTax.currency +"     ");
                    }
                    else {
                        monyResult.setText("costs: " + payCost +" "+currencyAndTax.currency + "      | " + "sales: " + salCost + " "+
                                currencyAndTax.currency +"     \n\n"+"tax: "+currencyAndTax.tax+
                                "%"+"    tax ammount: "+((currencyAndTax.tax/100*salCost)) + "\n     profit: " +
                                (salCost - payCost-((currencyAndTax.tax/100*salCost))) + " "+currencyAndTax.currency +"     ");
                    }
                } else if (spinnerpos == 2) {
                    ch = true;
                    csal = new ArrayList<>();
                    cpay = new ArrayList<>();
                    double proft = 0;
                    salCost = 0;
                    payCost = 0;
                    ///////////////////////////////////////////////////////
                    //////////////////////////////////////////////////////
                    {
                        for (int i = 0; i < Adminpage.cSale.size(); i++) {
                            String[] a = (Adminpage.cSale.get(i).date).split("-");
                            if ((a[1] + "-" + a[2]).equals(myDatefrom)) {
                                csal.add(Adminpage.cSale.get(i));
                                salCost += Adminpage.cSale.get(i).sale;
                            }

                        }
                        salAdapt = new adminAdapterSales(getApplicationContext(), R.layout.row, csal);
                        listsale.setAdapter(salAdapt);
                    }
                    ///////////////////////////////////////////////////////
                    ///////////////////////////////////////////////////////

                    {
                        for (int i = 0; i < Adminpage.cPyment.size(); i++) {
                            String[] a = (Adminpage.cPyment.get(i).date).split("-");
                            if ((a[1] + "-" + a[2]).equals(myDatefrom)) {
                                cpay.add(Adminpage.cPyment.get(i));
                                payCost += Adminpage.cPyment.get(i).pay;
                            }

                        }
                        payadpt = new adminAdapterPayment(getApplicationContext(), R.layout.row, cpay);
                        listpay.setAdapter(payadpt);
                    }

                    if (shared2.getString("language", "").equals("arabic")) {
                        monyResult.setText("التكاليف: " + payCost +" "+currencyAndTax.currency + "      | " + "المبيعات: " + salCost + " "+
                                currencyAndTax.currency +"     \n\n"+"الضريبه: "+currencyAndTax.tax+
                                "%"+"    قيمة الضريبه: "+((currencyAndTax.tax/100*salCost)) + "\n     الارباح: " +
                                (salCost - payCost-((currencyAndTax.tax/100*salCost))) + " "+currencyAndTax.currency +"     ");
                    } else {
                        monyResult.setText("costs: " + payCost +" "+currencyAndTax.currency + "      | " + "sales: " + salCost + " "+
                                currencyAndTax.currency +"     \n\n"+"tax: "+currencyAndTax.tax+
                                "%"+"    tax ammount: "+((currencyAndTax.tax/100*salCost)) + "\n     profit: " +
                                (salCost - payCost-((currencyAndTax.tax/100*salCost))) + " "+currencyAndTax.currency +"     ");
                    }
                }else if (spinnerpos==3){

                    ch=true;
                    csal=new ArrayList<>();
                    cpay=new ArrayList<>();
                    double proft=0;
                    salCost=0;
                    payCost=0;
                    ///////////////////////////////////////////////////////
                    //////////////////////////////////////////////////////
                    {
                        for (int i = 0; i < Adminpage.cSale.size(); i++) {
                            if ((Adminpage.cSale.get(i).date).equals(myDatetoDay)) {
                                csal.add(Adminpage.cSale.get(i));
                                salCost+=Adminpage.cSale.get(i).sale;
                            }

                        }
                        salAdapt = new adminAdapterSales(getApplicationContext(), R.layout.row, csal);
                        listsale.setAdapter(salAdapt);
                    }
                    ///////////////////////////////////////////////////////
                    ///////////////////////////////////////////////////////

                    {
                        for (int i = 0; i < Adminpage.cPyment.size(); i++) {
                            if ((Adminpage.cPyment.get(i).date).equals(myDatetoDay)) {
                                cpay.add(Adminpage.cPyment.get(i));
                                payCost+=Adminpage.cPyment.get(i).pay;
                            }

                        }
                        payadpt = new adminAdapterPayment(getApplicationContext(), R.layout.row, cpay);
                        listpay.setAdapter(payadpt);
                    }

                    if(shared2.getString("language", "").equals("arabic")) {
                        monyResult.setText("التكاليف: " + payCost +" "+currencyAndTax.currency + "      | " + "المبيعات: " + salCost + " "+
                                currencyAndTax.currency +"     \n\n"+"الضريبه: "+currencyAndTax.tax+
                                "%"+"    قيمة الضريبه: "+((currencyAndTax.tax/100*salCost)) + "\n     الارباح: " +
                                (salCost - payCost-((currencyAndTax.tax/100*salCost))) + " "+currencyAndTax.currency +"     ");
                    }
                    else {
                        monyResult.setText("costs: " + payCost +" "+currencyAndTax.currency + "      | " + "sales: " + salCost + " "+
                                currencyAndTax.currency +"     \n\n"+"tax: "+currencyAndTax.tax+
                                "%"+"    tax ammount: "+((currencyAndTax.tax/100*salCost)) + "\n     profit: " +
                                (salCost - payCost-((currencyAndTax.tax/100*salCost))) + " "+currencyAndTax.currency +"     ");
                    }
                }else if(spinnerpos==4){
                    if(shared2.getString("language", "").equals("arabic")) {
                        monyResult.setText("الرجاء اختيار نوع البحث");
                    }
                    else
                        Toast.makeText(getApplicationContext(), "please select the search type", Toast.LENGTH_LONG).show();                }
                else if(spinnerpos== 5){
                    if(shared2.getString("language", "").equals("arabic")) {
                        monyResult.setText("الرجاء اختيار نوع البحث");
                    }
                    else
                        Toast.makeText(getApplicationContext(), "please select the search type", Toast.LENGTH_LONG).show();
                }
            }

        });


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               recreate();

            }
        });

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ch) {
                    Intent n = new Intent(getApplicationContext(), statistics_chart.class);
                    n.putExtra("sale", salCost);
                    n.putExtra("pay", payCost);
                    startActivity(n);
                }
                else { if(shared2.getString("language", "").equals("arabic")) {
                    Toast.makeText(getApplicationContext(),"الرجاء البحث من خلال السنه او الشهر لعرض المخطط",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"please filtering the data py year or month",Toast.LENGTH_LONG).show();
                }
                }
            }
        });

    }

}