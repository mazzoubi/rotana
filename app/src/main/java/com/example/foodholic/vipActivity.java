package com.example.foodholic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class vipActivity extends AppCompatActivity {
    FirebaseFirestore db ;
    SharedPreferences shared2;
    public static ArrayList<classVipUsers> vipUsers=new ArrayList<>();
    public static ArrayList<classVipUsers> vipUsersafter=new ArrayList<>();
    public static classCurrencyAndTax currencyAndTax=new classCurrencyAndTax();
    ListView listView;
    public static classVipUsers users=new classVipUsers();
    Button search,add,ref;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);
        init();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n =new Intent(getApplicationContext(),vipAdd.class);
                startActivity(n);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=editText.getText().toString();
                final ArrayList<String>ss=new ArrayList<>();
                vipUsersafter=new ArrayList<>();
                for (int i=0;i<vipUsers.size();i++){
                    if (vipUsers.get(i).phone.contains(s)||
                            vipUsers.get(i).phone.contains(" "+s)||
                            vipUsers.get(i).phone.contains("  "+s)||
                            vipUsers.get(i).phone.contains("   "+s)||
                            vipUsers.get(i).phone.contains(s+" ")||
                            vipUsers.get(i).phone.contains(s+"  ")||
                            vipUsers.get(i).phone.contains(s+"   ")||vipUsers.get(i).name.contains(s)||
                            vipUsers.get(i).name.contains(" "+s)||
                            vipUsers.get(i).name.contains("  "+s)||
                            vipUsers.get(i).name.contains("   "+s)||
                            vipUsers.get(i).name.contains(s+" ")||
                            vipUsers.get(i).name.contains(s+"  ")||
                            vipUsers.get(i).name.contains(s+"   ")){
                        vipUsersafter.add(vipUsers.get(i));
                        ss.add(vipUsers.get(i).name+"\n"+
                                vipUsers.get(i).phone);
                    }
                }
                if (ss.isEmpty()){
                    if(shared2.getString("language", "").equals("arabic")){
                        Toast.makeText(vipActivity.this, "هذا الزبون غير موجود", Toast.LENGTH_SHORT).show();
                    }
                    else
                    Toast.makeText(vipActivity.this, "customer not found", Toast.LENGTH_SHORT).show();
                }
                ArrayAdapter<String> adapter=new zReportAdapter(getApplicationContext(),R.layout.row_zreport,ss);
                listView.setAdapter(adapter);
                editText.setText("");

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                users=vipUsersafter.get(position);
                Intent n =new Intent(getApplicationContext(),vipInfo.class);
                startActivity(n);
            }
        });
        ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

    }
    void init (){
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        db=FirebaseFirestore.getInstance();
        listView=findViewById(R.id.listView);
        search=findViewById(R.id.button4);
        add=findViewById(R.id.button3);
        ref=findViewById(R.id.button5);
        editText=findViewById(R.id.editText2);
        if(shared2.getString("language", "").equals("arabic")){
            editText.setHint("بحث");
            add.setText("اضافة");
        }
        loadCurrency();
        loadUsers();
    }
    void loadUsers(){
        vipUsers=new ArrayList<>();
        final ArrayList<String>ss=new ArrayList<>();

        db.collection("Res_1_vipUsers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
           List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
           for (DocumentSnapshot d : list){
               vipUsers.add(d.toObject(classVipUsers.class));
               vipUsersafter.add(d.toObject(classVipUsers.class));
               ss.add(d.toObject(classVipUsers.class).name+"\n"+
                       d.toObject(classVipUsers.class).phone);
           }
                ArrayAdapter<String> adapter=new zReportAdapter(getApplicationContext(),R.layout.row_zreport,ss);
           listView.setAdapter(adapter);
            }
        });
    }
    void loadCurrency (){
        db.collection("Res_1_currencyAndTax").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list =queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d : list){
                    currencyAndTax=d.toObject(classCurrencyAndTax.class);
                }
            }
        });
    }
}
