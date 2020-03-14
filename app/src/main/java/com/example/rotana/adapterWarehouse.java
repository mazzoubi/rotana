package com.example.rotana;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class adapterWarehouse extends ArrayAdapter<classWarehouseItem> {

    classWarehouseItem warehouseItem ;

    adapterWarehouse(Context context, int view , ArrayList<classWarehouseItem> arrayList){
        super(context,view,arrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        View myView = layoutInflater.inflate(R.layout.row,parent,false);

        TextView textView =(TextView)myView.findViewById(R.id.ddd);
        TextView textView1 =(TextView)myView.findViewById(R.id.sss);

        warehouseItem=getItem(position);

        textView.setText(warehouseItem.item);
        textView1.setText(warehouseItem.quantity+" "+warehouseItem.quantityType);

        return myView ;
    }
}
