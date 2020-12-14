package com.example.rotanademo;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


public class supplierPillinfo extends AppCompatActivity {
    SharedPreferences shared2;
    TextView supplier,emp,date,pillnumber,body,price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_pillinfo);
        init();
    }
    void init(){
        supplier=findViewById(R.id.supplier);
        emp=findViewById(R.id.emp);
        date=findViewById(R.id.date);
        pillnumber=findViewById(R.id.pillnumber);
        body=findViewById(R.id.body);
        price=findViewById(R.id.price);

        supplier.setText("");
        emp.setText("");
        date.setText("");
        pillnumber.setText("");
        body.setText("");
        price.setText("");
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        int flag=getIntent().getIntExtra("flag",0);

        if(shared2.getString("language", "").equals("arabic")) {
            if (flag==1){
                String ss ="";
                double x =0;
                try{

                    for (int i=0;i<pillsPurchases.yy.size();i++){
                        ss+=(i+1)+"   "+pillsPurchases.yy.get(i).body+" \n";
                        x+= Double.parseDouble(pillsPurchases.yy.get(i).price);
                    }


                    supplier.setText(pillsPurchases.yy.get(0).head);
                    emp.setText(pillsPurchases.yy.get(0).head1);
                    date.setText(pillsPurchases.yy.get(0).date);
                    pillnumber.setText(pillsPurchases.yy.get(0).head3);
                    body.setText(ss);
                    price.setText("سعر الفاتورة: "+x);
                }catch (Exception e ){
                    Toast.makeText(this, "خطأ في رقم الفاتورة, او المورد", Toast.LENGTH_LONG).show();
                }


            }
            else if (flag==2){
                try{

                    supplier.setText("المورد: "+pillsPurchases.pp.supplier);
                    emp.setText("الموظف الذي ادخل الفاتورة: "+pillsPurchases.pp.empEmail);
                    date.setText("تاريخ الفاتورة: "+pillsPurchases.pp.date);
                    pillnumber.setText(""+pillsPurchases.pp.pillNumber);
                    body.setText("العنصر: "+pillsPurchases.pp.items+"   العدد: "+pillsPurchases.pp.numberOfElement+"\n\nالتفاصيل: \n"+pillsPurchases.pp.desc);
                    price.setText("السعر: "+pillsPurchases.pp.coast);
                }catch (Exception e ){
                    Toast.makeText(this, "خطأ في رقم الفاتورة, او المورد", Toast.LENGTH_LONG).show();
                }

            }
            else if (flag==3){
                try{
                    supplier.setText("المورد: "+suppliersItem.pp.supplier);
                    emp.setText("الموظف الذي ادخل الفاتورة: "+suppliersItem.pp.empEmail);
                    date.setText("تاريخ الفاتورة: "+suppliersItem.pp.date);
                    pillnumber.setText(""+suppliersItem.pp.pillNumber);
                    body.setText("العنصر: "+suppliersItem.pp.items+"   العدد: "+suppliersItem.pp.numberOfElement+"\n\nالتفاصيل: \n"+suppliersItem.pp.desc);
                    price.setText("السعر: "+suppliersItem.pp.coast);
                }catch (Exception e ){
                    Toast.makeText(this, "خطأ في رقم الفاتورة, او المورد", Toast.LENGTH_LONG).show();
                }


            }
        }
        else {
            if (flag==1){
                String ss ="";
                double x =0;
                try{

                    for (int i=0;i<pillsPurchases.yy.size();i++){
                        ss+=(i+1)+"   "+pillsPurchases.yy.get(i).body+" \n";
                        x+= Double.parseDouble(pillsPurchases.yy.get(i).price);
                    }

                    supplier.setText(pillsPurchases.yy.get(0).head);
                    emp.setText(pillsPurchases.yy.get(0).head1);
                    date.setText(pillsPurchases.yy.get(0).date);
                    pillnumber.setText(pillsPurchases.yy.get(0).head3);
                    body.setText(ss);
                    price.setText("pill price: "+x);
                }catch (Exception e ){
                    Toast.makeText(this, "invalid bill number, or supplier ", Toast.LENGTH_LONG).show();
                }


            }
            else if (flag==2){
                try{
                    supplier.setText("the supplier: "+pillsPurchases.pp.supplier);
                    emp.setText("the employee who enter the pill: "+pillsPurchases.pp.empEmail);
                    date.setText("pill number: "+pillsPurchases.pp.date);
                    pillnumber.setText("pill date: "+pillsPurchases.pp.pillNumber);
                    body.setText("item: "+pillsPurchases.pp.items+"   number of item: "+pillsPurchases.pp.numberOfElement+"\n\nthe details: \n"+pillsPurchases.pp.desc);
                    price.setText("price: "+pillsPurchases.pp.coast);
                }catch (Exception e ){
                    Toast.makeText(this, "invalid bill number, or supplier", Toast.LENGTH_LONG).show();
                }


            }
            else if (flag==3){
                try{
                    supplier.setText("the supplier: "+suppliersItem.pp.supplier);
                    emp.setText("the employee who enter the pill: "+suppliersItem.pp.empEmail);
                    date.setText("pill number: "+suppliersItem.pp.date);
                    pillnumber.setText("pill date: "+suppliersItem.pp.pillNumber);
                    body.setText("item: "+suppliersItem.pp.items+"   number of item: "+suppliersItem.pp.numberOfElement+"\n\nthe details: \n"+suppliersItem.pp.desc);
                    price.setText("price: "+suppliersItem.pp.coast);
                }catch (Exception e ){
                    Toast.makeText(this, "invalid bill number, or supplier", Toast.LENGTH_LONG).show();
                }


            }
        }


    }
}
