package com.example.foodholic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class cashAdapterResultScreen extends ArrayAdapter<classCashSale> {

    classCashSale Sales ;

    cashAdapterResultScreen(Context context, int view , ArrayList<classCashSale> arrayList){
        super(context,view,arrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        View myView = layoutInflater.inflate(R.layout.row_cashresultscreen,parent,false);

        TextView neme =(TextView)myView.findViewById(R.id.rname);
        TextView count =(TextView)myView.findViewById(R.id.rcount);
        TextView uprice =(TextView)myView.findViewById(R.id.ruprice);
        TextView sum =(TextView)myView.findViewById(R.id.rsum);

        Sales=getItem(position);

        if (HomeAct.lang==1) {
            neme.setText("أسم المادة : " + Sales.subItemName);
            count.setText("العدد : " + Sales.count);
            uprice.setText("السعر الأفرادي : " + Sales.unitPrice);
            sum.setText("السعر الكامل : " + Sales.sumPrice);
        }
        else {
            neme.setText("Subject Name : " + Sales.subItemName);
            count.setText("no# : " + Sales.count);
            uprice.setText("Item price : " + Sales.unitPrice);
            sum.setText("total : " + Sales.sumPrice);
        }

        return myView ;
    }
}
