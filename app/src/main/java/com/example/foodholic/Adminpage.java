package com.example.foodholic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Adminpage extends AppCompatActivity {



    FirebaseFirestore db ;
    FirebaseFirestore dbs;

    public static ArrayList<classSales> cSale=new ArrayList<>();
    public static ArrayList<classPayment> cPyment=new ArrayList<>();

    Button emp,stati,addPayment,warehouse ,zreport,openClose;
    boolean a=false,b=false;
    ArrayAdapter<String> adapter;

    TextView textView;
    SharedPreferences shared2;

   public static String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage);



        final ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressBar);
        emp=(Button)findViewById(R.id.employee);
        stati=(Button)findViewById(R.id.statistics2);
        addPayment=(Button)findViewById(R.id.addPayment);
        warehouse=findViewById(R.id.warehouse);
        textView=findViewById(R.id.textView6);
        zreport=findViewById(R.id.warehouse2);
        openClose=findViewById(R.id.warehouse3);
        zreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n=new Intent(getApplicationContext(),Zreport.class);
                startActivity(n);
            }
        });

        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        if(shared2.getString("language", "").equals("arabic")){
            emp.setText("الموظفين");
            stati.setText("التقارير");
            addPayment.setText("اضافة التكاليف");
            warehouse.setText("المستودع");
            openClose.setText("فتح واغلاق الكاش");
            textView.setText("صفحة المدير");
        }


        warehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n =new Intent(getApplicationContext(),warehouse.class);
                startActivity(n);
            }
        });


        addPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n =new Intent(getApplicationContext(),Payment.class);
                n.putExtra("emp",email);
                startActivity(n);
            }
        });

        emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n=new Intent(getApplicationContext(),employeeCreatInformation.class);
                startActivity(n);
            }
        });

        stati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_SHORT);
                Intent n=new Intent(getApplicationContext(),statistics.class);
                startActivity(n);
            }
        });
        db = FirebaseFirestore.getInstance();

        db.collection("Res_1_payment").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                progressBar.setVisibility(View.GONE);

                cPyment=new ArrayList<>();
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    cPyment.add(d.toObject(classPayment.class));
                }
                emp.setEnabled(true);
                stati.setEnabled(true);

            }

        });

        dbs = FirebaseFirestore.getInstance();
        dbs.collection("Res_1_sales").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                progressBar.setVisibility(View.GONE);
                cSale=new ArrayList<>() ;
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    cSale.add(d.toObject(classSales.class));
                }
                emp.setEnabled(true);
                stati.setEnabled(true);
            }
        });




    }

}
