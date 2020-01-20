package com.example.foodholic;

import android.content.SharedPreferences;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class suppliersItem extends AppCompatActivity {
    SharedPreferences shared2;
    ListView listView;
    FirebaseFirestore db;
    String supplier="";
    ArrayList<classWarehouseItem> warehouseItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppliers_item);
        init();
    }
    void init(){
        supplier=getIntent().getStringExtra("sup");
        db=FirebaseFirestore.getInstance();
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        listView=findViewById(R.id.listView);
        loaditem();
    }

    void loaditem(){
        warehouseItems=new ArrayList<>();
        db.collection("Res_1_warehouse").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list){
                    if (d.toObject(classWarehouseItem.class).supplier.equals(supplier)){
                        warehouseItems.add(d.toObject(classWarehouseItem.class));
                    }
                }
                ArrayAdapter adapter=new adapterWarehouse(getApplicationContext(),R.layout.row,warehouseItems);
                listView.setAdapter(adapter);
            }
        });
    }
}
