package com.example.foodholic;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;


public class statistics_chart extends AppCompatActivity {

    double sal;
    double pay;
    TextView textView;
    SharedPreferences shared2;
    classCurrencyAndTax currencyAndTax=Adminpage.currencyAndTax;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_chart);

        textView=findViewById(R.id.textView_profit);


        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        sal = getIntent().getDoubleExtra("sale",0);
        pay = getIntent().getDoubleExtra("pay",0);




        Pie pie = AnyChart.pie();

        List<DataEntry> data = new ArrayList<>();
        if(shared2.getString("language", "").equals("arabic")) {
            data.add(new ValueDataEntry("المبيعات", sal));
            data.add(new ValueDataEntry("التكاليف", pay));
            data.add(new ValueDataEntry("قيمة الضريبه", ((currencyAndTax.tax/100*sal))));
            textView.setText("مجموع الارباح : "+(sal-((currencyAndTax.tax/100*sal))-pay));
        }
        else {
            data.add(new ValueDataEntry("sales", sal));
            data.add(new ValueDataEntry("payment", pay));
            data.add(new ValueDataEntry("tax ammount", ((currencyAndTax.tax/100*sal))));
            textView.setText("the porfits is : "+(sal-((currencyAndTax.tax/100*sal))-pay));
        }
        pie.data(data);

        AnyChartView anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
        anyChartView.setChart(pie);
    }

}
