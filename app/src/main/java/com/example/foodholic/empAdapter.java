package com.example.foodholic;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class empAdapter extends ArrayAdapter<classEmployee> {

    classEmployee emp ;

    String  Fname;
    String  Lname;
    String  regisetDate;
    String  email;
    String empPhone;
    String  empType;



    empAdapter(Context context, int view , ArrayList<classEmployee> arrayList){
        super(context,view,arrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        View myView = layoutInflater.inflate(R.layout.rowemp,parent,false);

        TextView t1=myView.findViewById(R.id.name111);
        TextView t2=myView.findViewById(R.id.email111);
        LinearLayout aa=myView.findViewById(R.id.linear);
        emp=getItem(position);

        if(position%2==0){
            aa.setBackgroundColor(Color.parseColor("#FF9800"));
        }

        Fname=emp.Fname;
        Lname=emp.Lname;
        regisetDate=emp.regisetDate;
        email=emp.email;
        empPhone=emp.empPhone;
        empType=emp.empType;

        t1.setText(Fname+" "+Lname);
        t2.setText(email);

        return myView ;
    }
}

