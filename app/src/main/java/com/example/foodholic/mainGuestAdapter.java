package com.example.foodholic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class mainGuestAdapter extends ArrayAdapter<classSubItem> {

    classSubItem Sales ;
    Context context;

    mainGuestAdapter(Context context, int view , ArrayList<classSubItem> arrayList){
        super(context,view,arrayList);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        View myView = layoutInflater.inflate(R.layout.row_guest,parent,false);

        TextView neme =(TextView)myView.findViewById(R.id.guestSubItem);
        TextView point =(TextView)myView.findViewById(R.id.guestPoint);
        TextView priceAfter =(TextView)myView.findViewById(R.id.guestPriceAftertax);
        ImageView imageView=myView.findViewById(R.id.imageView2);
        TextView des=myView.findViewById(R.id.des);

        Sales=getItem(position);

        if (HomeAct.lang==1 && !aa3.eng){
            neme.setText(Sales.Ar_subItem+"");
            des.setText(Sales.Ar_description+"");
            point.setText("النقاط : "+Sales.point);
            priceAfter.setText("السعر : "+Sales.price+" دينار ");
        }

        else {
            neme.setText(Sales.subItem+"");
            des.setText(Sales.description+"");
            point.setText("Point : "+Sales.point);
            priceAfter.setText("Price : "+Sales.price+" JOD ");
        }

        Glide.with(context).load(Sales.image).into(imageView);


        return myView ;
    }
}
