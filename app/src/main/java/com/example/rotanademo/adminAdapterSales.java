package com.example.rotanademo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class adminAdapterSales extends ArrayAdapter<classSales> {

    classSales pay ;

    adminAdapterSales(Context context, int view , ArrayList<classSales> arrayList){
        super(context,view,arrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        View myView = layoutInflater.inflate(R.layout.row,parent,false);

        TextView textView =(TextView)myView.findViewById(R.id.ddd);
        TextView textView1 =(TextView)myView.findViewById(R.id.sss);

        pay=getItem(position);

        textView.setText(pay.date);
        textView1.setText(pay.sale+"");

        return myView ;
    }
}
