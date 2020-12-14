package com.example.rotanademo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.rotanademo.Emppage.closeOpenCash;

public class Tabel_Res_Emp extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener date_;
    ArrayList<String> tabels = new ArrayList<String>();
    public static String[] sites = {
            "1", "2", "3", "4", "5",
            "6", "7", "8","9","10", "11","12",
            "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22","23","24", "25","26", "27", "28", "29", "30",
            "31","32", "33", "34", "35", "36"};

    public class CustomAdapter extends BaseAdapter{

        String [] result;
        Context context;

        private LayoutInflater inflater=null;

        public CustomAdapter(Tabel_Res_Emp table, String[] site) {

            result=sites;
            context=table;

            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); }

        @Override
        public int getCount() { return tabels.size(); }

        @Override
        public Object getItem(int position) { return position; }

        @Override
        public long getItemId(int position) { return position; }

        public class Holder { TextView os_text; }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {

            getResData(position+1);

            Holder holder=new Holder();
            final View rowView;

            rowView = inflater.inflate(R.layout.sample_gridlayout, null);
            holder.os_text =(TextView) rowView.findViewById(R.id.os_texts);
            holder.os_text.setText(result[position]);

            if(!tabels.get(position).contains("Table Number : ")){

                if (shared.getInt("pos", 0) == position)
                    rowView.setBackgroundColor(getResources().getColor(R.color.colorPick));
                else
                    rowView.setBackgroundColor(getResources().getColor(R.color.colorPrimary)); }


            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

//                    if(getIntent().getStringExtra("empemail").contains(".cap")){
//
//                        final AlertDialog.Builder builder = new AlertDialog.Builder(Tabel_Res_Emp.this);
//                        LayoutInflater inflater = Tabel_Res_Emp.this.getLayoutInflater();
//                        builder.setView(inflater.inflate(R.layout.activity_tabel_order, null));
//                        final AlertDialog dialog = builder.create();
//                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                        lp.copyFrom(dialog.getWindow().getAttributes());
//                        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//                        dialog.show();
//                        dialog.getWindow().setAttributes(lp);
//
//                        final Button reserve, cancle;
//
//                        reserve = dialog.findViewById(R.id.reserv);
//                        cancle = dialog.findViewById(R.id.cancle);
//
//                        if(HomeAct.lang != 1){
//                            reserve.setText("Reserve");
//                            cancle.setText("View Bill");
//                        }
//
//                        reserve.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                String myFormat = "dd/MM/yy"; //In which you need put here
//                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//                                da = sdf.format(new Date());
//
//                                myFormat = "HH:mm";
//                                sdf = new SimpleDateFormat(myFormat, Locale.US);
//                                ta = sdf.format(new Date());
//
//                                ReserveClick(position, v, "", "", "", da, ta);
//                                dialog.dismiss();
//                                recreate();
//
//                                dialog.dismiss();
//                                recreate();
//                            }
//                        });
//                        cancle.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                getInsta(position+1);
//
//                            }
//                        });
//
//                    }
//
//                    else{

                        final AlertDialog.Builder builder = new AlertDialog.Builder(Tabel_Res_Emp.this);
                        LayoutInflater inflater = Tabel_Res_Emp.this.getLayoutInflater();
                        builder.setView(inflater.inflate(R.layout.activity_tabel__resss2, null));
                        final AlertDialog dialog = builder.create();
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                        dialog.show();
                        dialog.getWindow().setAttributes(lp);

                        final TextView name;
                        final Button reserve, cancle, cancle2;
                        final Toolbar tool;

                        tool = dialog.findViewById(R.id.tool);
                        reserve = dialog.findViewById(R.id.reserv);
                        cancle = dialog.findViewById(R.id.cancle);
                        cancle2 = dialog.findViewById(R.id.cancle2);
                        name = dialog.findViewById(R.id.name);

                        name.setText(info.get(position+1));

                        if(HomeAct.lang != 1){
                            tool.setTitle("Reservation Information");
                            reserve.setText("Reserve");
                            cancle.setText("View Bill");
                            name.setHint("Customer Name");
                        }

                        if(getIntent().getStringExtra("order") != null){
                            if(!getIntent().getStringExtra("order").equals("")){

                                ReserveClick(position, rowView, "", "", "", "", "");
                                dialog.dismiss(); } }

                        cancle2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                removeData(position+1);
                                recreate();

                            }
                        });

                        reserve.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                PrintT(position+1);

                            }
                        });

                        cancle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final AlertDialog.Builder builder2 = new AlertDialog.Builder(Tabel_Res_Emp.this);
                                LayoutInflater inflater2 = Tabel_Res_Emp.this.getLayoutInflater();
                                builder2.setView(inflater2.inflate(R.layout.calulator3, null));
                                final AlertDialog dialog2 = builder2.create();
                                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                                dialog2.show();

                                TextView t = dialog2.findViewById(R.id.title);
                                String sts = info.get(position+1);
                                final String ss = sts.substring(sts.indexOf("مجموع : ")+8).replace(getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"), "").replace("\n", "").replaceAll(" ", "");

                                EditText p = dialog2.findViewById(R.id.pay);
                                TextView c = dialog2.findViewById(R.id.change);

                                if(HomeAct.lang == 1)
                                    t.setText("مجموع الفاتورة : " + ss + getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"));
                                else
                                    t.setText("Total Bill : " + ss + getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD "));

                                closeOpenCash.total += Double.parseDouble(ss);

                                final TextView t2 = dialog2.findViewById(R.id.change);

                                final EditText e = dialog2.findViewById(R.id.pay);
                                e.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        if (s.toString().equals(""))
                                            e.setText("0");
                                        else {
                                            String temp = String.valueOf((Double.parseDouble(e.getText().toString()) - Double.parseDouble(ss)));
                                            try {
                                                if(HomeAct.lang == 1){
                                                    t2.setText("الباقي : " + temp.substring(0, temp.indexOf(".") + 3) + getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"));
                                                } else {
                                                    t2.setText("The rest : " + temp.substring(0, temp.indexOf(".") + 3) + "  ");
                                                }
                                            } catch (Exception ex) {
                                                if(HomeAct.lang == 1){
                                                    t2.setText("الباقي : " + temp + getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"));
                                                } else {
                                                    t2.setText("The rest : " + temp + getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD "));
                                                }

                                            }
                                        }

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                                Button b = dialog2.findViewById(R.id.deliv);
                                b.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if(e.getText().toString().equals(""))
                                            e.setText("0");

                                        if ((Double.parseDouble(e.getText().toString()) - Double.parseDouble(ss)) >= 0 && !e.getText().toString().equals("")) {

                                            AddSale(position+1);

                                            dialog2.dismiss();
                                            recreate();
                                        } else {
                                            if(HomeAct.lang == 1){
                                                Toast.makeText(Tabel_Res_Emp.this, "الرجاء ادخال مبلغ صحيح", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(Tabel_Res_Emp.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                                            }
                                        }




                                    }
                                });

                            }
                        });

//                        cancle.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                SharedPreferences.Editor editor = shared.edit();
//                                editor.putInt("pos", 0);
//                                editor.apply();
//
//                                String s1, s2, s3;
//                                if(HomeAct.lang == 1){
//                                    s1="الفاتورة";
//                                    s3="إلغاء";
//                                    s2="طباعة"; }
//                                else{
//                                    s1="Bill";
//                                    s3="Cancel";
//                                    s2="Print"; }
//
//                                new AlertDialog.Builder(Tabel_Res_Emp.this)
//                                        .setTitle(s1).setMessage(info.get(position+1))
//                                        .setPositiveButton(s2, new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                                                if(HomeAct.lang == 1)
//                                                    AddSale(position+1);
//                                                else
//                                                    AddSaleEn(position+1);
//
//                                            }
//                                        }).setNegativeButton(s3, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        dialog.dismiss();
//                                    }
//                                }).show();
////view the fatoorah and print it ____________________________________________
//
//                            }
//                        });
                    }


                //}
            });

            return rowView;
        }

    }

    private void getInsta(final int psn) {

        SharedPreferences.Editor editor = shared.edit();
        editor.putInt("pos", 0);
        editor.apply();

        String s1, s2, s3, s4;
        if(HomeAct.lang == 1){
            s1="الفاتورة";
            s3="إلغاء";
            s4="حجز";
            s2="طباعة"; }
        else{
            s1="Bill";
            s3="Cancel";
            s4="Reserve";
            s2="Print"; }

        new AlertDialog.Builder(Tabel_Res_Emp.this)
                .setTitle(s1).setMessage(info.get(psn))
                .setPositiveButton(s2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

//                        if(HomeAct.lang == 1)
//                            AddSale(psn);
//                        else
//                            AddSaleEn(psn);

                        recreate();

                    }
                }).setNegativeButton(s3, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss(); }

                }).show();

    }

    public void AddSaleEn(final int pos){

        String bill="";
        FirebaseAuth auth = FirebaseAuth.getInstance();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        Date dateee = new Date();
        String date = dateFormat.format(dateee);

        String day = date.substring(0, date.indexOf(" "));
        String time = date.substring(date.indexOf(" ")+1);

        ArrayList<String> ttt = info;

        String [] temp, temp2;
        temp = info.get(pos).substring(info.get(pos)
                .indexOf("Menu : ")+7, info.get(pos).indexOf("Total :"))
                .replaceAll("= ", "X").replaceAll(":", "Price : ").replaceAll("\n", "")
                .split(",");
        temp2 = temp;
        if(temp[temp.length-1].equals(" ")){
            temp2 = temp;
            temp = new String[temp2.length-1];
            for(int i=0; i<temp.length; i++) {
                temp[i] = temp2[i];
                temp2[i] = temp[i]+"\n,";
            }
        }

        bill+="\n"+"Welcome To Hybrid Shawarma"+",";
        bill+="\n"+"Bill Type : Table"+",";
        bill+="\n"+"Table Num : "+pos+",";
        bill+="\n\n"+",";
        bill+="Date : "+day+"\n"+",";
        bill+="Time : "+time+"\n"+",";
        bill+="__________________________________________\n\n\n"+",";

        Map<String, Object> sale = new HashMap<>();

        for(int i=0; i<temp.length; i++){

            sale.put("date", day);
            sale.put("time", time);
            sale.put("subItem", temp[i].substring(0, temp[i].indexOf(" X")));
            sale.put("item", "");
            sale.put("empEmail", auth.getCurrentUser().getEmail());

            double p = Double.parseDouble(temp[i].substring(temp[i].indexOf("Price : ")+8));
            int c = Integer.parseInt(temp[i].substring(temp[i].indexOf("X")+1, temp[i].indexOf(" Price : ")));
            sale.put("sale", p*c);

            bill+="\nItem : "+temp2[i];
            db.collection("Res_1_sales").document().set(sale);
        }

        double taxValue = Double.parseDouble(getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0"))*0.01;
        double billval = Double.parseDouble(info.get(pos).substring(info.get(pos).indexOf("Total : ")+8).replace(getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"), ""));
        double su = billval-(billval*taxValue);

        bill+=",";

        bill+="\n\nBill Sum : "+(su)+",";
        bill+="\n\nTax Value : "+getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0")+" %"+"\n"+",";
        bill+="\n\nBill Total : "+info.get(pos).substring(info.get(pos).indexOf("Total : ")+8)+"\n"+",";
        bill+="\n\n\nCome Again Soon !\n\n\n";

        removeData(pos);
        PrintUsingServer(bill);

    }

    public void AddSale(final int pos){

        String bill="";
        FirebaseAuth auth = FirebaseAuth.getInstance();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        Date dateee = new Date();
        String date = dateFormat.format(dateee);

        String day = date.substring(0, date.indexOf(" "));
        String time = date.substring(date.indexOf(" ")+1);

        ArrayList<String> ttt = info;

        String [] temp, temp2;
        temp = info.get(pos).substring(info.get(pos)
                .indexOf("الطلب : ")+8, info.get(pos).indexOf("مجموع :"))
                .replaceAll("= ", "X").replaceAll(":", "السعر : ").replaceAll("\n", "")
                .split(",");
        temp2 = temp;
        if(temp[temp.length-1].equals(" ")){
            temp2 = temp;
            temp = new String[temp2.length-1];
            for(int i=0; i<temp.length; i++) {
                temp[i] = temp2[i];
                temp2[i] = temp[i]+"\n,";
            }
        }

        bill+="\n"+"مرحبا بك في كوفي شوب جيفان"+",";
        bill+="\n"+"نوع الفاتورة : فاتورة طاولة"+",";
        bill+="\n"+"رقم الطاولة : "+pos+",";
        bill+="\n\n"+",";
        bill+="تاريخ : "+day+"\n"+",";
        bill+="وقت : "+time+"\n"+",";
        bill+="__________________________________________\n\n\n"+",";

        Map<String, Object> sale = new HashMap<>();

        for(int i=0; i<temp.length; i++){

            sale.put("date", day);
            sale.put("time", time);
            sale.put("subItem", temp[i].substring(0, temp[i].indexOf("X")));
            sale.put("item", "");
            sale.put("empEmail", auth.getCurrentUser().getEmail());

            double p = Double.parseDouble(temp[i].substring(temp[i].indexOf("السعر : ")+8));
            int c = Integer.parseInt(temp[i].substring(temp[i].indexOf("X")+1, temp[i].indexOf(" السعر : ")));
            sale.put("sale", p*c);

            bill+="\nالعنصر : "+temp2[i];
            db.collection("Res_1_sales").document().set(sale);
        }

        double taxValue = Double.parseDouble(getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0"))*0.01;
        double billval = Double.parseDouble(info.get(pos).substring(info.get(pos).indexOf("مجموع : ")+8).replace(getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"), ""));
        double su = billval-(billval*taxValue);

        bill+=",";

        bill+="\n\nمجموع الفاتورة : "+(su)+",";
        bill+="\n\nقيمة الضريبة : "+getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0")+" %"+"\n"+",";
        bill+="\n\nمجموع كلي : "+info.get(pos).substring(info.get(pos).indexOf("مجموع : ")+8)+"\n"+",";
        bill+="\n\n\nأهلا و سهلا زبائننا الكرام\n\n\n";

        removeData(pos);
        PrintUsingServer(bill);

    }

    private void PrintUsingServer(String s) {

        int c = shared3.getInt("count", 0);
        SharedPreferences.Editor editor = shared3.edit();
        editor.putInt("count" ,++c);
        editor.apply();

        try {

            if(HomeAct.lang == 1)
                s = ("رقم الفاتورة : "+shared3.getInt("count", 0)+"")+","+s;
            else
                s = ("Bill ID : "+shared3.getInt("count", 0)+"")+","+s;

            String ip = getSharedPreferences("IPS", MODE_PRIVATE).getString("ip", "192.168.1.1");
            int port = getSharedPreferences("IPS", MODE_PRIVATE).getInt("port", 9100);

            SocketAddress socketAddress = new InetSocketAddress("192.168.1.100", 9100);
            Socket socket = new Socket();

            socket.connect(socketAddress, 5000);

            OutputStreamWriter clientSocketWriter = (new OutputStreamWriter(socket.getOutputStream(), "UTF8")); //optional encoding
            clientSocketWriter.write(s);
            clientSocketWriter.close();
            socket.close();

        }
        catch(Exception e){
            if(HomeAct.lang == 1)
                Toast.makeText(Tabel_Res_Emp.this, "لا يوجد طابعة !!!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(Tabel_Res_Emp.this, "No Printer Attached !!!", Toast.LENGTH_SHORT).show();
        }
      }

    private void removeData(int i) {

        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection("Res_1_Table_Res_").document(i+"").delete();

    }

    private void getResData(final int pos) {

        info.clear();

        for(int i=0; i<tabels.size(); i++){
            if(HomeAct.lang == 1)
            info.add("لا يوجد فاتورة");
            else
            info.add("No Bill");}

        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection("Res_1_Table_Res_").document(""+pos)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                   if(HomeAct.lang == 1){
                       try{info.set(pos,
                                       "الطلب : "+task.getResult().get("order").toString()+"\n"+
                                       "مجموع : "+task.getResult().get("sum").toString()+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار")+" \n"

                       );}catch (Exception e){}
                   }
                   else{
                       try{info.set(pos,
                                       "Menu : "+task.getResult().get("order").toString()+"\n"+
                                       "Total : "+task.getResult().get("sum").toString()+" JOD\n"

                       );}catch (Exception e){}
                   }

                }

            }
        });

    }

    private void Print(String str) {

        try {

            String ip = getSharedPreferences("IPS", MODE_PRIVATE).getString("ip", "192.168.1.1");
            int port = getSharedPreferences("IPS", MODE_PRIVATE).getInt("port", 9100);

            SocketAddress socketAddress = new InetSocketAddress("192.168.1.100", 9100);
            Socket socket = new Socket();

            socket.connect(socketAddress, 2000);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8")); //optional encoding
            writer.write(str);
            writer.write("\n\n\n\f");
            writer.close();
            socket.close();

        }
        catch(Exception e){
            Toast.makeText(Tabel_Res_Emp.this, "لا يوجد طابعة !!!", Toast.LENGTH_SHORT).show();
        }

    }

    GridView gridview;
    FirebaseFirestore db;
    SharedPreferences shared, shared2;
    String da, ta;
    String temp = "";
    ArrayList<String> info = new ArrayList<>();
    SharedPreferences shared3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tabel__res__emp);

        try {
            DatabaseReference tablelelele = FirebaseDatabase.getInstance().getReference().child("notification").child("notificationTable");
            tablelelele.removeValue();
        }catch (Exception e ){
        }

        Toolbar toolbar = findViewById(R.id.tool);
        TextView tt = findViewById(R.id.back);

        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView refr = findViewById(R.id.ref);
        if(HomeAct.lang == 1)
            tt.setText("الرجوع");
        else
            tt.setText("Go Back");

        refr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        shared3 = getSharedPreferences("cash", MODE_PRIVATE);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        shared = getSharedPreferences("order", MODE_PRIVATE);
        shared2 = getSharedPreferences("color", MODE_PRIVATE);

        db = FirebaseFirestore.getInstance();

        TextView t1, t2;
        t1 = findViewById(R.id.not);
        t2 = findViewById(R.id.yes);

        if(HomeAct.lang != 1){
            t1.setText("Not Reserved");
            t2.setText("Reserved"); }
        else{
            t1.setText("غير محجوزة");
            t2.setText("محجوزة"); }

        db = FirebaseFirestore.getInstance();
        gridview = (GridView) findViewById(R.id.customgrid);

        GetTabelCount();

         }

    private void GetTabelCount() {

        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection("Res_1_Table_Count").document("TC")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                    AddTables(task.getResult().getData().get("count").toString());
            }
        });



    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(Tabel_Res_Emp.this, Emppage.class)
                .putExtra("empemail", getIntent().getStringExtra("empemail")));

        finish();

    }

    private void AddTables(String count) {

        tabels.clear();
        int c = Integer.parseInt(count);

        for(int i=0; i<c; i++)
            tabels.add("Table Number : "+i);

        CheckTabels();
    }

    public boolean checkDateRes(String str) {

        String str2 = str.substring(0, str.indexOf("@"));
        str = str.substring(str.indexOf("@")+1);

        String form = "dd/MM/yy HH";
        SimpleDateFormat sdf = new SimpleDateFormat(form, Locale.US);

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = sdf.parse(str+" "+str2);
            d2 = sdf.parse(da+" "+ta); }
        catch (ParseException e) {}

        if(d2.compareTo(d1) == 0)
            return true;
        else
            return false; }

    public void getDateTime() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        da = sdf.format(new Date());

        myFormat = "hh:mm"; //In which you need put here
        sdf = new SimpleDateFormat(myFormat, Locale.US);
        ta = sdf.format(new Date());


    }

    private void ReserveClick(final int p, final View vie, String name, String mobile, String people,
                              String date, String time){

                if(getIntent().getStringExtra("order") != null){
                    if(!getIntent().getStringExtra("order").equals("")){
                        if(info.get(p+1).equals("لا يوجد فاتورة")){
                        Map<String, Object> order = new HashMap<>();
                        order.put("name", name);
                        order.put("mobile", mobile);
                        order.put("people", people);
                        order.put("date", date);
                        order.put("time", time);
                        order.put("order", getIntent().getStringExtra("order"));
                        order.put("sum", getIntent().getStringExtra("orderSum"));

                        db.collection("Res_1_Table_Res_").document(""+(p+1)).set(order)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(HomeAct.lang == 1){
                                            if(task.isSuccessful())
                                                Toast.makeText(Tabel_Res_Emp.this, "تم اضافة الحجز", Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(Tabel_Res_Emp.this, "حاول مجددا", Toast.LENGTH_SHORT).show();
                                            Sendk(p+1);
                                            onBackPressed();
                                        }
                                        else{
                                            if(task.isSuccessful())
                                                Toast.makeText(Tabel_Res_Emp.this, "Reserved Successfully !", Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(Tabel_Res_Emp.this, "Try Again Please !", Toast.LENGTH_SHORT).show();
                                            onBackPressed(); }
                                    }
                                });
                    }
                    else{

                        String str = info.get(p+1);

                        String str2 = (Double.parseDouble(str.substring(str.indexOf("مجموع : ")+8).replace(getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"), ""))+Double.parseDouble(getIntent().getStringExtra("orderSum")))+"";

                            str = str.replace("الطلب : ", "")
                                    .substring(0, str.indexOf("مجموع : "))
                                    .replace("\n", "")
                            .replace("مجموع :","");

                        String all =str+getIntent().getStringExtra("order");
                            Map<String, Object> order = new HashMap<>();
                            order.put("name", name);
                            order.put("mobile", mobile);
                            order.put("people", people);
                            order.put("date", date);
                            order.put("time", time);
                            order.put("order", all);
                            order.put("sum", str2);

                            db.collection("Res_1_Table_Res_").document(""+(p+1)).set(order)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(HomeAct.lang == 1){
                                                if(task.isSuccessful())
                                                    Toast.makeText(Tabel_Res_Emp.this, "تم الأضافة على الحجز", Toast.LENGTH_SHORT).show();
                                                else
                                                    Toast.makeText(Tabel_Res_Emp.this, "حاول مجددا", Toast.LENGTH_SHORT).show();
                                                Sendk(p+1);
                                                onBackPressed();
                                            }
                                            else{
                                                if(task.isSuccessful())
                                                    Toast.makeText(Tabel_Res_Emp.this, "Reserved Successfully !", Toast.LENGTH_SHORT).show();
                                                else
                                                    Toast.makeText(Tabel_Res_Emp.this, "Try Again Please !", Toast.LENGTH_SHORT).show();
                                                onBackPressed(); }
                                        }
                                    });

                        }
                    }
                }
                else{
                    Map<String, Object> order = new HashMap<>();
                    order.put("name", name);
                    order.put("mobile", mobile);
                    order.put("people", people);
                    order.put("date", date);
                    order.put("time", time);
                    order.put("order", "");
                    order.put("sum", "");

                    db.collection("Res_1_Table_Res_").document(""+(p+1)).set(order)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(HomeAct.lang == 1){
                                        if(task.isSuccessful())
                                            Toast.makeText(Tabel_Res_Emp.this, "تم اضافة الحجز", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(Tabel_Res_Emp.this, "حاول مجددا", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        if(task.isSuccessful())
                                            Toast.makeText(Tabel_Res_Emp.this, "Reserved Successfully !", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(Tabel_Res_Emp.this, "Try Again Please !", Toast.LENGTH_SHORT).show();
                                    }

                                        }
                            });

                }


      //  }
       // else
        //    Toast.makeText(Tabel_Res_Emp.this, "لديك حجز مسبق", Toast.LENGTH_LONG).show();

    }

    public void PrintT(final int pos){

        String bill="";
        FirebaseAuth auth = FirebaseAuth.getInstance();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        Date dateee = new Date();
        String date = dateFormat.format(dateee);

        String day = date.substring(0, date.indexOf(" "));
        String time = date.substring(date.indexOf(" ")+1);

        ArrayList<String> ttt = info;

        String [] temp, temp2;
        temp = info.get(pos).substring(info.get(pos)
                .indexOf("الطلب : ")+8, info.get(pos).indexOf("مجموع :"))
                .replaceAll("= ", "X").replaceAll(":", "السعر : ").replaceAll("\n", "")
                .split(",");
        temp2 = temp;
        if(temp[temp.length-1].equals(" ")){
            temp2 = temp;
            temp = new String[temp2.length-1];
            for(int i=0; i<temp.length; i++) {
                temp[i] = temp2[i];
                temp2[i] = temp[i]+"\n,";
            }
        }

        bill+="\n"+"مرحبا بك في كوفي شوب جيفان"+",";
        bill+="\n"+"نوع الفاتورة : فاتورة طاولة"+",";
        bill+="\n"+"رقم الطاولة : "+pos+",";
        bill+="\n\n"+",";
        bill+="تاريخ : "+day+"\n"+",";
        bill+="وقت : "+time+"\n"+",";
        bill+="__________________________________________\n\n\n"+",";

        Map<String, Object> sale = new HashMap<>();

        for(int i=0; i<temp.length; i++){

            sale.put("date", day);
            sale.put("time", time);
            sale.put("subItem", temp[i].substring(0, temp[i].indexOf("X")));
            sale.put("item", "");
            sale.put("empEmail", auth.getCurrentUser().getEmail());

            double p = Double.parseDouble(temp[i].substring(temp[i].indexOf("السعر : ")+8));
            int c = Integer.parseInt(temp[i].substring(temp[i].indexOf("X")+1, temp[i].indexOf(" السعر : ")));
            sale.put("sale", p*c);

            bill+="\nالعنصر : "+temp2[i];
        }

        double taxValue = Double.parseDouble(getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0"))*0.01;
        double billval = Double.parseDouble(info.get(pos).substring(info.get(pos).indexOf("مجموع : ")+8).replace(getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"), ""));
        double su = billval-(billval*taxValue);

        bill+=",";

        bill+="\n\nمجموع الفاتورة : "+(su)+",";
        bill+="\n\nقيمة الضريبة : "+getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0")+" %"+"\n"+",";
        bill+="\n\nمجموع كلي : "+info.get(pos).substring(info.get(pos).indexOf("مجموع : ")+8)+"\n"+",";
        bill+="\n\n\nأهلا و سهلا زبائننا الكرام\n\n\n";

        PrintUsingServer(bill);

    }
    public void Sendk(final int pos){

        String bill="";
        FirebaseAuth auth = FirebaseAuth.getInstance();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        Date dateee = new Date();
        String date = dateFormat.format(dateee);

        String day = date.substring(0, date.indexOf(" "));
        String time = date.substring(date.indexOf(" ")+1);

        String [] temp, temp2;

        String inf = getIntent().getStringExtra("order");
        temp = inf.replaceAll("= ", "X")
                .replaceAll(":", "السعر : ")
                .replaceAll("\n", "")
        .split(",");

        bill+="\n"+"مرحبا بك في كوفي شوب جيفان"+",";
        bill+="\n"+"نوع الفاتورة : فاتورة طاولة"+",";
        bill+="\n"+"رقم الطاولة : "+pos+",";
        bill+="\n\n"+",";
        bill+="تاريخ : "+day+"\n"+",";
        bill+="وقت : "+time+"\n"+",";
        bill+="__________________________________________\n\n\n"+",";

        Map<String, Object> sale = new HashMap<>();

        for(int i=0; i<temp.length-1; i++){

            sale.put("date", day);
            sale.put("time", time);
            sale.put("subItem", temp[i].substring(0, temp[i].indexOf("X")));
            sale.put("item", "");
            sale.put("empEmail", auth.getCurrentUser().getEmail());

            double p = Double.parseDouble(temp[i].substring(temp[i].indexOf("السعر : ")+8));
            int c = Integer.parseInt(temp[i].substring(temp[i].indexOf("X")+1, temp[i].indexOf(" السعر : ")));
            sale.put("sale", p*c);

            bill+="\nالعنصر : "+temp[i]+",";
        }

        String inf2 = getIntent().getStringExtra("orderSum");

        double taxValue = Double.parseDouble(getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0"))*0.01;
        double billval = Double.parseDouble(inf2);
        double su = billval-(billval*taxValue);

        bill+="\n\nمجموع الفاتورة : "+(su)+",";
        bill+="\n\nقيمة الضريبة : "+getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0")+" %"+"\n"+",";
        bill+="\n\nمجموع كلي : "+inf2+"\n"+",";
        bill+="\n\n\nأهلا و سهلا زبائننا الكرام\n\n\n";


            String st=",[";
            for(int i=0; i<temp.length-1; i++)
                st+="1,";
            st+="]";

            bill += st;
            bill += "SendToKitchen";
            PrintUsingServer(bill);

    }

    public void CheckTabels(){

        db.collection("Res_1_Table_Res_").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                try{
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult())
                            tabels.set(Integer.parseInt(document.getId())-1, document.get("time").toString()+"@"+document.get("date"));

                        gridview.setAdapter(new Tabel_Res_Emp.CustomAdapter(Tabel_Res_Emp.this, sites)); }

                } catch(Exception ex){

                    gridview.setAdapter(new Tabel_Res_Emp.CustomAdapter(Tabel_Res_Emp.this, tabels.toArray(new String[tabels.size()]) ));
                }

            } });
    }

}
