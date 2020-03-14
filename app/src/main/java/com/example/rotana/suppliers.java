package com.example.rotana;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class suppliers extends AppCompatActivity {

    FirebaseFirestore db ;
    ListView listView;
    Button button;
    ArrayList<classSupplier> suppliers;
    SharedPreferences shared2;
    boolean flag =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_suppliers);
        init();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n =new Intent(getApplicationContext(),supplierAddOne.class);
                startActivity(n);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent n =new Intent(getApplicationContext(),suppliersItem.class);
                n.putExtra("sup", suppliers.get(position).name);
                startActivity(n);
            }
        });
    }

    void init(){
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        db=FirebaseFirestore.getInstance();
        button=findViewById(R.id.warehouseAdd);
        listView=findViewById(R.id.warehouseList);
        if(shared2.getString("language", "").equals("arabic")) {
            button.setText("اضافة مورد جديد");
        }
        else {}
        loadSuppliers();
    }
    void loadSuppliers(){
        suppliers=new ArrayList<>();

        db.collection("Res_1_suppliers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<String> li =new ArrayList<>();

                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list){
                    suppliers.add(d.toObject(classSupplier.class));
                    li.add(d.toObject(classSupplier.class).name+"\n" +
                            d.toObject(classSupplier.class).email
                    +"\n"+d.toObject(classSupplier.class).phone);
                }
                if (li.isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")) {
                        li.add("لا يوجد اي موردين حالياً,  \n" +
                                "هل تريد اضافة مودر جديد؟؟");
                    }
                    else {
                        li.add("Do not have any supplier now \n" +
                                "do you want add one??");
                    }

                    flag=true;

                }
                ArrayAdapter<String> adapter=new zReportAdapter(getApplicationContext(),R.layout.row_zreport,li);
                listView.setAdapter(adapter);
            }
        });
    }

}
