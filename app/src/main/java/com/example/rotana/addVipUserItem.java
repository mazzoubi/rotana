package com.example.rotana;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class addVipUserItem extends AppCompatActivity {

    FirebaseFirestore db ;
    ArrayList<classItem>items;
    ArrayList<classSubItem>subItems;
    ArrayList<classSubItem>subItemsAfterFilering;
    String salesString="";

    ArrayList<String>subItemToShow;
    ArrayList<String>itemToShow;
    ListView itemList,subList,resList;
    Button ref,add,pay, save;

    String name ="",phone = "",id ="",email="";

    SharedPreferences shared2;
    // هضول الاوبجيكت واليست عشان عملية حساب مجموع السعر والامور الثانيه تحت موضحه الامور اكثر ///////////
    classCashSale sale;
    ArrayList<classCashSale> saleList=new ArrayList<>();
    ArrayAdapter<classCashSale> adp;
    /////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vip_user_item);

        name =getIntent().getStringExtra("name");
        phone =getIntent().getStringExtra("phone");
        id =getIntent().getStringExtra("id");
        email =getIntent().getStringExtra("email");
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        db= FirebaseFirestore.getInstance();


        itemList=findViewById(R.id.cashItemList);
        subList=findViewById(R.id.cashSubitem);
        resList=findViewById(R.id.cashResultList);


        save=findViewById(R.id.button2);
        if(shared2.getString("language", "").equals("arabic")){
            save.setText("حفظ");
        }

        //add=findViewById(R.id.cahAddItem);
        //pay=findViewById(R.id.cashPayment);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });



//هسا من هون لعند الكومنت الي مكتوب فيه (النهايه)بس تحميل بيانات واذا كبس ع الايتم بظهرلك السب ايتم
/////////////////loads
        loadItem();
        loadSubItem();
/////////////////end loads

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getSubItem(items.get(position).itemName);
            }
        });//النهايه





        ///////////////////////////////////////////////////////////////////////////////////////////////

        //هاض اليسنر لعند الكومنت الي فيه( /**/ ) هو ع السب ايتم ليست عشان عمليات جمع السعر والامور الثانيه بس انا حالياً مش عامل غير جمع السعر
        /*
        classCashSale sale;        هضول معرفات قلوبال لانه اعترض عليهن هون
        ArrayList<classCashSale> saleList=new ArrayList<>();*/

        subList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sale=new classCashSale();

                boolean cheack=false;
                int loopPosition=0;

                if (saleList.isEmpty()){
                    sale.count=1;
                    sale.subItemName=subItemsAfterFilering.get(position).subItem;
                    sale.sumPrice=subItemsAfterFilering.get(position).price;
                    sale.unitPrice=subItemsAfterFilering.get(position).price;
                    saleList.add(sale);

                }
                else{
                    for(int i=0 ; i<saleList.size();i++){
                        if(subItemsAfterFilering.get(position).subItem.equals(saleList.get(i).subItemName)){
                            saleList.get(i).sumPrice+=subItemsAfterFilering.get(position).price;
                            saleList.get(i).count++;
                            cheack=true;
                            break;
                        }

                    }
                    if(cheack){
                    }
                    else{
                        sale.count=1;
                        sale.subItemName=subItemsAfterFilering.get(position).subItem;
                        sale.sumPrice=subItemsAfterFilering.get(position).price;
                        sale.unitPrice=subItemsAfterFilering.get(position).price;
                        saleList.add(sale);
                    }

                }

                double sumPrice=0;
                for (classCashSale d : saleList){
                    salesString+=d.subItemName+"\n"
                            +d.count+"\n"
                            +d.sumPrice+" " +vipActivity.currencyAndTax.currency+"\n"
                            +"------------------------------\n";
                    sumPrice+=d.sumPrice;
                }


                salesString+="------------------------------\n" +
                        sumPrice+" "+vipActivity.currencyAndTax.currency;

                ArrayAdapter<classCashSale> adapter=new cashAdapterResultScreen(getApplication(),android.R.layout.simple_list_item_1,saleList);
                resList.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });
        // /**/

    }





    public void loadSubItem(){
        db.collection("Res_1_subItem").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                subItems=new ArrayList<>();
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    classSubItem a=d.toObject(classSubItem.class);
                    subItems.add(a);
                }
            }
        });
    }




    public void loadItem(){
        db.collection("Res_1_items").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                items=new ArrayList<>();
                itemToShow=new ArrayList<>();
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    classItem a=d.toObject(classItem.class);
                    items.add(a);
                    itemToShow.add(a.itemName);
                }
                ArrayAdapter<String>adapter=new ArrayAdapter<>(getApplication(),android.R.layout.simple_list_item_1,itemToShow);
                itemList.setAdapter(adapter);

            }
        });
    }





    public void getSubItem(String item){
        subItemToShow=new ArrayList<>();
        subItemsAfterFilering=new ArrayList<>();
        for(classSubItem d:subItems){
            if (d.item.equals(item)){
                subItemToShow.add(d.subItem);
                subItemsAfterFilering.add(d);
            }
        }
        ArrayAdapter<String>adapter=new ArrayAdapter<>(getApplication(),android.R.layout.simple_list_item_1,subItemToShow);
        subList.setAdapter(adapter);
    }
    void upload(){
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", name);
        reservation.put("phone", phone);
        reservation.put("id", id);
        reservation.put("email", email);
        reservation.put("desc", salesString);

        vipInfo.tdesc.setText(salesString);
        vipInfo.tphone.setText(phone);
        db.collection("Res_1_vipUsers").document(id).set(reservation)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(shared2.getString("language", "").equals("arabic")){
                                Toast.makeText(addVipUserItem.this, "تمت العمليه بنجاح", Toast.LENGTH_SHORT).show();

                                onBackPressed();

                            }
                            else{
                                Toast.makeText(addVipUserItem.this, "Operation Successful, Your add item", Toast.LENGTH_SHORT).show();
                                onBackPressed();

                            }

                        }
                        else
                        if(shared2.getString("language", "").equals("arabic")){
                            Toast.makeText(addVipUserItem.this, "خطأ غير متوقع الرجاء اعادة المحاوله !", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(addVipUserItem.this, "Error, Please Try To add Again !", Toast.LENGTH_SHORT).show();

                    }
                });

    }
}
