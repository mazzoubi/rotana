package com.example.rotanademo;

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
            neme.setText(Sales.subItemName+"");
            count.setText(Sales.count+"");
            uprice.setText(Sales.unitPrice+"");
            sum.setText(Sales.sumPrice+"");
        }
        else {
            neme.setText(Sales.subItemName+"");
            count.setText(Sales.count+"");
            uprice.setText(Sales.unitPrice+"");
            sum.setText(Sales.sumPrice+"");
        }

        return myView ;
    }
}
