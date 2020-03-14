package com.example.rotana;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class zCloaseOpenAdapter extends ArrayAdapter<classCloseOpenCash> {

    classCloseOpenCash ss ;

    zCloaseOpenAdapter(Context context, int view , ArrayList<classCloseOpenCash> arrayList){
        super(context,view,arrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        View myView = layoutInflater.inflate(R.layout.row_cloaseopencash,parent,false);

        TextView textView =(TextView)myView.findViewById(R.id.a1);
        TextView textView1 =(TextView)myView.findViewById(R.id.a2);
        TextView textView2=(TextView)myView.findViewById(R.id.a3);
        TextView textView3 =(TextView)myView.findViewById(R.id.a4);
        TextView textView4 =(TextView)myView.findViewById(R.id.a5);
        TextView textView5 =(TextView)myView.findViewById(R.id.a6);
        TextView textView6 =(TextView)myView.findViewById(R.id.a7);
        TextView textView7 =(TextView)myView.findViewById(R.id.a8);



        ss=getItem(position);

        if (HomeAct.lang==1){
            textView.setText("تاريخ العمليه: "+ss.dateOpen);
            textView1.setText("وقت فتح الصندوق: "+ss.dateAndTimeOpen);
            textView2.setText("وقت اغلاق الصندوق: "+ss.dateAndTimeClose);
            textView3.setText("ملاحظات: \n "+ss.note);
            textView4.setText("الأرضيه: "+ss.floor+"");
            textView5.setText("المصروفات: "+ss.paid+"");
            textView6.setText("المبيعات: "+ss.sold+"");
            textView7.setText("المجموع: "+ss.total+"");
        }else {
            textView.setText("operation date: "+ss.dateOpen);
            textView1.setText("time open: "+ss.dateAndTimeOpen);
            textView2.setText("time close: "+ss.dateAndTimeClose);
            textView3.setText("note: \n"+ss.note);
            textView4.setText("floor: "+ss.floor+"");
            textView5.setText("paid: "+ss.paid+"");
            textView6.setText("sold: "+ss.sold+"");
            textView7.setText("total: "+ss.total+"");
        }


        if(ss.total<ss.floor){
            textView7.setBackgroundColor(Color.RED);
        }

        return myView ;
    }
}
