package com.example.rotana;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class statisticsByDay extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener date_;

    String myDatefrom=" / / ";
    String myDateto=" / / ";
    Button search,searchyear,refresh, moredetale,chart;

    double sale ;
    double pay ;
    double salCost=0;
    double payCost=0;
    ArrayList<classPayment>cpay=new ArrayList<>();
    ArrayList<classSales>csal=new ArrayList<>();

    ArrayAdapter<classPayment> payadpt;
    ArrayAdapter<classSales>salAdapt;
    ListView listsale,listpay;
    SharedPreferences shared2;
    TextView t1,t2,monyResult;
    boolean ch=false;
    classCurrencyAndTax currencyAndTax=Adminpage.currencyAndTax;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_by_day);



        listpay=(ListView)findViewById(R.id.listpayday);
        listsale=(ListView)findViewById(R.id.listsaleday);


        final TextView textView =(TextView)findViewById(R.id.dddaday);
        // final TextView textView2 =(TextView)findViewById(R.id.ddda3day);
        monyResult =(TextView)findViewById(R.id.moneyResultday);

        search=(Button)findViewById(R.id.button_searchday);
        moredetale=(Button)findViewById(R.id.button_search3day);
        // searchyear=(Button)findViewById(R.id.button_search2day);
        refresh=(Button)findViewById(R.id.button_refreshday);
        chart=findViewById(R.id.button_chartday);
        t1=findViewById(R.id.ddda7day);
        t2=findViewById(R.id.ddda5day);

        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        if(shared2.getString("language", "").equals("arabic")) {
            // textView2.setText("عرض في السنه");
            moredetale.setText("تفاصيل اكثر");
            t1.setText("المبيعات");
            t2.setText("التكاليف");
            monyResult.setText("التكاليف: " + 0 + " "+currencyAndTax.currency +"      | " + "المبيعات: " + 0 + " "+currencyAndTax.currency +"     \n\n" + "     الارباح: " + 0 + " "+currencyAndTax.currency +"     "); }


        moredetale .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(getApplicationContext(),foodType.class);
                startActivity(n);
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

             /*   String []y=myDate.split("/");
                textView1.setText(y[1]+"-"+y[0]+"-"+y[2]);*/

            } };

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(statisticsByDay.this, date_, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                Toast.makeText(getApplicationContext(),"chose the dd/mm/yyyy in month you want show statistics",Toast.LENGTH_LONG).show();



            }
        });



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
                        if ((Adminpage.cSale.get(i).date).equals(myDatefrom)) {
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
                        if ((Adminpage.cPyment.get(i).date).equals(myDatefrom)) {
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
                            "%"+"    قيمة الضريبه: "+(salCost-(currencyAndTax.tax/100*salCost)) + "\n     الارباح: " +
                            (salCost - payCost) + " "+currencyAndTax.currency +"     ");
                }
                else {
                    monyResult.setText("costs: " + payCost +" "+currencyAndTax.currency + "      | " + "sales: " + salCost + " "+
                            currencyAndTax.currency +"     \n\n"+"tax: "+currencyAndTax.tax+
                            "%"+"    tax ammount: "+(salCost-(currencyAndTax.tax/100*salCost)) + "\n     profit: " +
                            (salCost - payCost) + " "+currencyAndTax.currency +"     ");
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
                    Toast.makeText(getApplicationContext(),"الرجاء البحث من خلال السنه و الشهر واليوم لعرض المخطط",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"please filtering the data py year,month or day",Toast.LENGTH_LONG).show();
                }
                }
            }
        });

    }
}
