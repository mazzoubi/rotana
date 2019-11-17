package com.example.foodholic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class zReportAdapter extends ArrayAdapter<String> {

    String warehouseItem ;

    zReportAdapter(Context context, int view , ArrayList<String> arrayList){
        super(context,view,arrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        View myView = layoutInflater.inflate(R.layout.row_zreport,parent,false);

        TextView textView =(TextView)myView.findViewById(R.id.textView25);


        warehouseItem=getItem(position);

        textView.setText(warehouseItem);


        return myView ;
    }
}
