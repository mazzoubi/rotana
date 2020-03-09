package com.example.foodholic;

import android.content.Context;
import android.graphics.Color;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class notificationAdapter extends ArrayAdapter<classNotification> {

    classNotification noti ;

    notificationAdapter(Context context, int view , ArrayList<classNotification> arrayList){
        super(context,view,arrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        View myView = layoutInflater.inflate(R.layout.row_noti,parent,false);

        TextView textView =(TextView)myView.findViewById(R.id.textView57);
        TextView textView1 =(TextView)myView.findViewById(R.id.textView58);
        TextView textView2 =(TextView)myView.findViewById(R.id.textView59);
        ConstraintLayout c =myView.findViewById(R.id.cc);
        c.setBackgroundColor(Color.WHITE);
        noti=getItem(position);
        if (noti.state.equals("1")){
            c.setBackgroundColor(Color.GRAY);
        }

        textView.setText(noti.title);
        textView1.setText(noti.date+" "+noti.time);
        try {
            textView2.setText(noti.desc.substring(0,25)+"....");
        }catch (Exception e){
            textView2.setText(noti.desc);
        }


        return myView ;
    }
}
