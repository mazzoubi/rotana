package com.example.rotanademo;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class statisticsByItem extends AppCompatActivity {

    ArrayList<classSales> list;
    ArrayList<classItem> listItem;
    ArrayList<classSubItem>listSubItem;
    SharedPreferences shared2;
    TextView result ;
    Spinner month,year,item,subitem;
    Button button;
    String m="",y="",it="",s="";
    classCurrencyAndTax currencyAndTax=Adminpage.currencyAndTax;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_statistics_by_item);
        item=findViewById(R.id.a1itemspinner);
        init();


        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){

                }
                else {
                    m=adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){

                }
                else {
                    y=adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> ss =new ArrayList<>();
                if(shared2.getString("language", "").equals("arabic")){
                  ss.add("اختر عنصر");
                  ArrayAdapter<String>adapter;
                  if (i==0){
                      subitem.setEnabled(false);
                      year.setEnabled(false);
                      month.setEnabled(false);
                      result.setText("\n\n\n\n\n\t\t\t\t\tالرجاء اختيار عنصر");
                  }
                  else {
                      it=adapterView.getItemAtPosition(i).toString();


                      for(classSubItem d: listSubItem){
                          if (d.item.equals(it)){
                              ss.add(d.subItem);
                          }
                      }
                      year.setEnabled(true);
                      month.setEnabled(true);
                      subitem.setEnabled(true);
                      result.setText("\n\n\n\n\n\t\t\t\t\t");

                  }
                  adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,ss);
                  subitem.setAdapter(adapter);
              }
              else {
                  ss.add("choose sub item");
                  ArrayAdapter<String>adapter;
                  if (i==0){
                      subitem.setEnabled(false);
                      year.setEnabled(false);
                      month.setEnabled(false);
                      result.setText("\n\n\n\n\n\t\t\t\t\tplease choose item");
                  }
                  else {
                      it=adapterView.getItemAtPosition(i).toString();


                      for(classSubItem d: listSubItem){
                          if (d.item.equals(it)){
                              ss.add(d.subItem);
                          }
                      }
                      year.setEnabled(true);
                      month.setEnabled(true);
                      subitem.setEnabled(true);
                      result.setText("\n\n\n\n\n\t\t\t\t\t");

                  }
                  adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,ss);
                  subitem.setAdapter(adapter);
              }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        subitem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){

                }
                else {
                    s=adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double cost=0,sale=0;
                ArrayList<classSales> ss=new ArrayList<>();
                if(shared2.getString("language", "").equals("arabic")){
                  if (m.isEmpty()&&s.isEmpty()){
                      for (classSales d : list){
                          if (d.item.equals(it)&&d.date.contains(y)){
                              cost+=d.cost;
                              sale+=d.sale;
                              ss.add(d);
                          }
                      }
                      result.setText("\n\n\n\n العنصر : "+it+"\n" +
                              "العنصر الفرعي : "+s+"\n\n" +
                              "التكلفه = "+cost+"\n" +
                              "الضريبه = "+currencyAndTax.tax+"%"+"\n"+
                              "قيمة الضريبه = "+ (sale-(currencyAndTax.tax/100*sale))+" "+currencyAndTax.currency +"\n"+
                              "الربح = "+(sale-(sale-(currencyAndTax.tax/100*sale))-cost)+"\n" +"\n"+
                              "المجموع = "+sale);
                  }
                  else if (!m.isEmpty()&&s.isEmpty()){
                      for (classSales d : list){
                          if (d.item.equals(it)&&d.date.contains(m+"-"+y)){
                              cost+=d.cost;
                              sale+=d.sale;
                              ss.add(d);
                          }
                      }
                      result.setText("\n\n\n\n العنصر : "+it+"\n" +
                              "العنصر الفرعي : "+s+"\n\n" +
                              "التكلفه = "+cost+"\n" +
                              "الضريبه = "+currencyAndTax.tax+"%"+"\n"+
                              "قيمة الضريبه = "+ (sale-(currencyAndTax.tax/100*sale))+" "+currencyAndTax.currency +"\n"+
                              "الربح = "+(sale-(sale-(currencyAndTax.tax/100*sale))-cost)+"\n" +"\n"+
                              "المجموع = "+sale);
                  }
                  else if (m.isEmpty()&&!s.isEmpty()){
                      for (classSales d : list){
                          if (d.subItem.equals(s)&&d.date.contains(y)){
                              cost+=d.cost;
                              sale+=d.sale;
                              ss.add(d);
                          }
                      }
                      result.setText("\n\n\n\n العنصر : "+it+"\n" +
                              "العنصر الفرعي : "+s+"\n\n" +
                              "التكلفه = "+cost+"\n" +
                              "الضريبه = "+currencyAndTax.tax+"%"+"\n"+
                              "قيمة الضريبه = "+ (sale-(currencyAndTax.tax/100*sale))+" "+currencyAndTax.currency +"\n"+
                              "الربح = "+(sale-(sale-(currencyAndTax.tax/100*sale))-cost)+"\n" +"\n"+
                              "المجموع = "+sale);
                  }
                  else if (!m.isEmpty()&&!s.isEmpty()){
                      for (classSales d : list){
                          if (d.subItem.equals(s)&&d.date.contains(m+"-"+y)){
                              cost+=d.cost;
                              sale+=d.sale;
                              ss.add(d);
                          }
                      }
                      result.setText("\n\n\n\n العنصر : "+it+"\n" +
                              "العنصر الفرعي : "+s+"\n\n" +
                              "التكلفه = "+cost+"\n" +
                              "الضريبه = "+currencyAndTax.tax+"%"+"\n"+
                              "قيمة الضريبه = "+ (sale-(currencyAndTax.tax/100*sale))+" "+currencyAndTax.currency +"\n"+
                              "الربح = "+(sale-(sale-(currencyAndTax.tax/100*sale))-cost)+"\n" +"\n"+
                              "المجموع = "+sale);
                  }
              }
              else {
                  if (m.isEmpty()&&s.isEmpty()){
                      for (classSales d : list){
                          if (d.item.equals(it)&&d.date.contains(y)){
                              cost+=d.cost;
                              sale+=d.sale;
                              ss.add(d);
                          }
                      }
                      result.setText("\n\n\n\n Item : "+it+"\n" +
                              "Sub Item : "+s+"\n\n" +
                              "Costs = "+cost+"\n" +
                              "tax = "+currencyAndTax.tax+"%"+"\n"+
                              "tax Ammount = "+ (sale-(currencyAndTax.tax/100*sale))+" "+currencyAndTax.currency +"\n"+
                              "profit = "+(sale-(sale-(currencyAndTax.tax/100*sale))-cost)+"\n" +"\n"+
                              "Total = "+sale);
                  }
                  else if (!m.isEmpty()&&s.isEmpty()){
                      for (classSales d : list){
                          if (d.item.equals(it)&&d.date.contains(m+"-"+y)){
                              cost+=d.cost;
                              sale+=d.sale;
                              ss.add(d);
                          }
                      }
                      result.setText("\n\n\n\n Item : "+it+"\n" +
                              "Sub Item : "+s+"\n\n" +
                              "Costs = "+cost+"\n" +
                              "tax = "+currencyAndTax.tax+"%"+"\n"+
                              "tax Ammount = "+ (sale-(currencyAndTax.tax/100*sale))+" "+currencyAndTax.currency +"\n"+
                              "profit = "+(sale-(sale-(currencyAndTax.tax/100*sale))-cost)+"\n" +"\n"+
                              "Total = "+sale);
                  }
                  else if (m.isEmpty()&&!s.isEmpty()){
                      for (classSales d : list){
                          if (d.subItem.equals(s)&&d.date.contains(y)){
                              cost+=d.cost;
                              sale+=d.sale;
                              ss.add(d);
                          }
                      }
                      result.setText("\n\n\n\n Item : "+it+"\n" +
                              "Sub Item : "+s+"\n\n" +
                              "Costs = "+cost+"\n" +
                              "tax = "+currencyAndTax.tax+"%"+"\n"+
                              "tax Ammount = "+ (sale-(currencyAndTax.tax/100*sale))+" "+currencyAndTax.currency +"\n"+
                              "profit = "+(sale-(sale-(currencyAndTax.tax/100*sale))-cost)+"\n" +"\n"+
                              "Total = "+sale);
                  }
                  else if (!m.isEmpty()&&!s.isEmpty()){
                      for (classSales d : list){
                          if (d.subItem.equals(s)&&d.date.contains(m+"-"+y)){
                              cost+=d.cost;
                              sale+=d.sale;
                              ss.add(d);
                          }
                      }
                      result.setText("\n\n\n\n Item : "+it+"\n" +
                              "Sub Item : "+s+"\n\n" +
                              "Costs = "+cost+"\n" +
                              "tax = "+currencyAndTax.tax+"%"+"\n"+
                              "tax Ammount = "+ (sale-(currencyAndTax.tax/100*sale))+" "+currencyAndTax.currency +"\n"+
                              "profit = "+(sale-(sale-(currencyAndTax.tax/100*sale))-cost)+"\n" +"\n"+
                              "Total = "+sale);
                  }
              }
            }
        });


    }

    void init(){
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        list=Adminpage.cSale;
        listItem();
        listSubItem();
        result=findViewById(R.id.a1textresult);
        month=findViewById(R.id.a1monthspinner);
        year=findViewById(R.id.a1yearspinner);
        subitem=findViewById(R.id.a1sunitemspinner);
        button=findViewById(R.id.a1button);

        if(shared2.getString("language", "").equals("arabic")){
            button.setText("بحث");
        }
    }
    void listItem(){
        FirebaseFirestore db=FirebaseFirestore.getInstance();

        db.collection("Res_1_items").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            listItem=new ArrayList<>();
                ArrayAdapter<String> itemAdapter;
                ArrayList<String> zz=new ArrayList<>();
                if(shared2.getString("language", "").equals("arabic")){
                    zz.add("اختر عنصر");
                }
                else {
                    zz.add("choose item");
                }
            List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d : list){
                    listItem.add(d.toObject(classItem.class));
                    zz.add(d.toObject(classItem.class).itemName);
                }
                itemAdapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,zz);
                item.setAdapter(itemAdapter);
            }
        });
    }
    void listSubItem(){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("Res_1_subItem").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                listSubItem=new ArrayList<>();
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d : list){
                    listSubItem.add(d.toObject(classSubItem.class));
                }
            }
        });
    }

}
