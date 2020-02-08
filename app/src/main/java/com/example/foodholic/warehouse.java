package com.example.foodholic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class warehouse extends AppCompatActivity {

    ListView listView;
    Button button;
    classWarehouseItem objectItem=new classWarehouseItem();
    public static ArrayList<classWarehouseItem> item=new ArrayList<>();

    FirebaseFirestore db ;

    SharedPreferences shared2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_warehouse);

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView=findViewById(R.id.warehouseList);
        button=findViewById(R.id.warehouseAdd);


        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        if(shared2.getString("language", "").equals("arabic")){
            button.setText("+ اضافة جديد +");
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n =new Intent(getApplicationContext(),aa1.class);
                startActivity(n);
            }
        });

        db=FirebaseFirestore.getInstance();

        db.collection("Res_1_warehouse").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                item=new ArrayList<>();
                for(DocumentSnapshot d : list){
                    classWarehouseItem a = d.toObject(classWarehouseItem.class);
                    item.add(a);
                }

                if (item.isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")){
                        Toast.makeText(getApplicationContext(),"لا يوجد اي عناصر في المستودع",Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(),"you not have any item in your warehouse",Toast.LENGTH_LONG).show();
                }
                else {
                    ArrayAdapter<classWarehouseItem> adapter = new adapterWarehouse(getApplicationContext(), R.layout.row, item);
                    listView.setAdapter(adapter);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent n =new Intent(getApplicationContext(),aa2.class);
                n.putExtra("aa",position);
                objectItem=item.get(position);
                startActivity(n);
            }
        });


    }
}
