package com.example.foodholic;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class adapterPurchases extends ArrayAdapter<classPaymentCreatPill> {

    classPaymentCreatPill warehouseItem ;

    adapterPurchases(Context context, int view , ArrayList<classPaymentCreatPill> arrayList){
        super(context,view,arrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        View myView = layoutInflater.inflate(R.layout.row_purchases,parent,false);

        TextView item =(TextView)myView.findViewById(R.id.item);
        TextView date =(TextView)myView.findViewById(R.id.date);
        TextView supplier =(TextView)myView.findViewById(R.id.suppliername);
        TextView pillnumber =(TextView)myView.findViewById(R.id.pillnumber);
        warehouseItem=getItem(position);

        if (HomeAct.lang==1){
            item.setText(warehouseItem.items);
            date.setText(warehouseItem.date);
            supplier.setText("المورد: "+warehouseItem.supplier);
            pillnumber.setText("رقم الفاتورة: "+warehouseItem.pillNumber);
        }
        else {
            item.setText(warehouseItem.items);
            date.setText(warehouseItem.date);
            supplier.setText("supplier: "+warehouseItem.supplier);
            pillnumber.setText("pill no#: "+warehouseItem.pillNumber);
        }


        return myView ;
    }
}
