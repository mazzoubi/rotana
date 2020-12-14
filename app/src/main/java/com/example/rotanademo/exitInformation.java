package com.example.rotanademo;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class exitInformation extends AppCompatActivity {
    String myDatefrom=" / / ";
    DatePickerDialog.OnDateSetListener date_;
    ListView listView;
    FirebaseFirestore db;
    ArrayList<String>arrayList=new ArrayList<>();
    TextView textView;
    int alldayoff=0;
    int dayoff=0;
    int dayoffnoshow=0;
    int hourext1=0;
    int hourext2=0;
    int hourext3=0;
    SharedPreferences shared2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_exit_information);
        listView=findViewById(R.id.listViewExitInformatio);

        db=FirebaseFirestore.getInstance();

        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        textView=findViewById(R.id.exitInfoResult);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                if(shared2.getString("language", "").equals("arabic")) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(exitInformation.this)
                            .setMessage("هل تريد حذف هذا الغياب؟")
                            .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        db.collection("Res_1_exitRequest").document(adminEmpAccess.ex.get(position).extID).delete();
                                        Toast.makeText(getApplicationContext(),"تم الاجراء بنجاح",Toast.LENGTH_LONG).show();
                                    }catch (Exception e){Toast.makeText(getApplicationContext(),"!!لايمكن تنفيذ هذا الاجراء",Toast.LENGTH_LONG).show();}
                                }
                            })
                            .setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert=builder.create();
                    alert.setTitle("حذف غياب");
                    alert.show();


                }
                else {
                    AlertDialog.Builder builder=new AlertDialog.Builder(exitInformation.this)
                            .setMessage("do you want delete this exit")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        db.collection("Res_1_exitRequest").document(adminEmpAccess.ex.get(position).extID).delete();
                                        Toast.makeText(getApplicationContext(),"this operation is done",Toast.LENGTH_LONG).show();
                                    }catch (Exception e){Toast.makeText(getApplicationContext(),"This procedure cannot be performed",Toast.LENGTH_LONG).show();}
                                }
                            })
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert=builder.create();
                    alert.setTitle("Delete Exit");
                    alert.show();


                }





            }
        });
        //////////////////////////////////
        //////////////////////////////////
        for(int i = 0; i< adminEmpAccess.ex.size(); i++){
            arrayList.add(adminEmpAccess.ex.get(i).email+"\n"+ adminEmpAccess.ex.get(i).date
                    +"\n"+"time of exit: "+ adminEmpAccess.ex.get(i).time+" hours"+"\n"+ adminEmpAccess.ex.get(i).description);
            if (adminEmpAccess.ex.get(i).time.equals("day off")){
                alldayoff++;
                if(adminEmpAccess.ex.get(i).description.equals("no show ,no call")){
                    dayoffnoshow++;
                }
                else dayoff++;
            }
            else if(adminEmpAccess.ex.get(i).time.equals("1")){  hourext1++;}
            else if(adminEmpAccess.ex.get(i).time.equals("2")){hourext2+=2;}
            else  if(adminEmpAccess.ex.get(i).time.equals("3")){hourext3+=3;}
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);
        //////////////////////////////////
        //////////////////////////////////

        Pie pie = AnyChart.pie();

        List<DataEntry> data = new ArrayList<>();

        if(shared2.getString("language", "").equals("arabic")) {
            textView.setText("| الغياب بعذر: "+dayoff+"| الغياب بدون عذر: "+dayoffnoshow+"| المغادرات : "+(hourext1+hourext2+hourext3));
            data.add(new ValueDataEntry("غياب بعذر", dayoff));
            data.add(new ValueDataEntry("غياب بدون عذر", dayoffnoshow));
            data.add(new ValueDataEntry("مغادره 1 ساعه", hourext1));
            data.add(new ValueDataEntry("مغادره 2 ساعه", hourext2));
            data.add(new ValueDataEntry("مغادره 3 ساعه", hourext3));
        }
        else {
            textView.setText("day off: "+dayoff+"| no show: "+dayoffnoshow+"| hours ext: "+(hourext1+hourext2+hourext3));
            data.add(new ValueDataEntry("day off", dayoff));
            data.add(new ValueDataEntry("no show,no call", dayoffnoshow));
            data.add(new ValueDataEntry("1 hour", hourext1));
            data.add(new ValueDataEntry("2 hours", hourext2));
            data.add(new ValueDataEntry("3 hours", hourext3));
        }
        pie.data(data);

        AnyChartView anyChartView = (AnyChartView) findViewById(R.id.any_chart_view11);
        anyChartView.setChart(pie);


    }
}
