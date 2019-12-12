package com.example.foodholic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class employeeCreatInformation extends AppCompatActivity {

    ListView listView ;
    Button addEmp,deleteEmp;
    FirebaseFirestore db ;
    public static ArrayList<classEmployee> emp =new ArrayList<>();
    SharedPreferences shared2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_creat_information);

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.empList);
        addEmp=(Button)findViewById(R.id.addEmp);
        deleteEmp=(Button)findViewById(R.id.deleteEmp);


        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        if(shared2.getString("language", "").equals("arabic")) {
            addEmp.setText("اضافة موظف");
            deleteEmp.setText("حضور الموظفين");
        }

        db=FirebaseFirestore.getInstance();
        db.collection("Res_1_employee").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list =queryDocumentSnapshots.getDocuments();
                emp =new ArrayList<>();
                for(DocumentSnapshot d : list){
                    classEmployee e =d.toObject(classEmployee.class);
                    emp.add(e);
                }

                ArrayAdapter<classEmployee>adapter=new empAdapter(getApplicationContext(),R.layout.rowemp,emp);
                listView .setAdapter(adapter);
            }
        });

        addEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n=new Intent(getApplicationContext(),employeeAdd.class);
                startActivity(n);
            }
        });

        deleteEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent n=new Intent(getApplicationContext(),onWork.class);
                startActivity(n);

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent n =new Intent(getApplicationContext(),adminEmpAccess.class);
                n.putExtra("position",position);
                startActivity(n);
            }
        });

    }
}
