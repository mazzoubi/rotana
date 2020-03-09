package com.example.foodholic;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Delivery_Emp extends AppCompatActivity {

    FirebaseFirestore db;

    ListView list;
    ArrayAdapter<String> adapter;

    ArrayList<String> id;
    ArrayList<String> loc;
    ArrayList<String> info;
    ArrayList<String>dname;
    ArrayList<String>latlng;

    ArrayAdapter<String> adapterSpin;
    ArrayAdapter<String> adapterSpin2;

    String p="";
    int lang=0 ;
    SharedPreferences shared3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_delivery__emp);

        try {
            DatabaseReference dil = FirebaseDatabase.getInstance().getReference().child("notification").child("notificationDelivery");
            dil.removeValue();
        }catch (Exception e ){
        }

        SharedPreferences shared2;
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        if(shared2.getString("language", "").equals("arabic")) {
            lang=1;
        }
        db = FirebaseFirestore.getInstance();

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        shared3 = getSharedPreferences("cash", MODE_PRIVATE);
        if(HomeAct.lang == 1)
            getSupportActionBar().setTitle("الرجوع");
        else
            getSupportActionBar().setTitle("Go Back");

        list=findViewById(R.id.list);

        id = new ArrayList<String>();
        loc = new ArrayList<String>();
        info = new ArrayList<String>();
        latlng = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, R.layout.items_row3, R.id.item, info);
        list.setAdapter(adapter);
        dname = new ArrayList<String>();
        final Spinner sp = findViewById(R.id.type);
        adapterSpin = new ArrayAdapter<String>(Delivery_Emp.this, R.layout.items_row3, R.id.item, dname);
        sp.setAdapter(adapterSpin);

        getDriver();

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                downloadData(sp.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long ido) {
                if (lang==1){

                    final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Delivery_Emp.this);
                    LayoutInflater inflater = Delivery_Emp.this.getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.dialog_redirect, null));
                    final androidx.appcompat.app.AlertDialog dialog = builder.create();
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    dialog.show();
                    dialog.getWindow().setAttributes(lp);

                    Button b1, b2, b3, b4, b5, b6;

                    b1 = dialog.findViewById(R.id.call);
                    b2 = dialog.findViewById(R.id.go);
                    b3 = dialog.findViewById(R.id.redirect);
                    b4 = dialog.findViewById(R.id.confirm);
                    b6 = dialog.findViewById(R.id.confirm2);
                    b5 = dialog.findViewById(R.id.remove);

                    b6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            PrintDriverAr(sp.getSelectedItem().toString(), position);

                        }
                    });

                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String str = info.get(position);
                            String num = str.substring(str.indexOf("الهاتف : ")+9, str.indexOf("قائمة : "));
                            num = RemoveSpace(num);
                            Intent intent = new Intent((Intent.ACTION_DIAL));
                            intent.setData(Uri.parse("tel:"+num));
                            startActivity(intent);

                        }
                    });

                    b2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(!latlng.isEmpty()){

                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                        Uri.parse(latlng.get(position)));
                                startActivity(intent);
                            }
                            else{

                                Toast.makeText(Delivery_Emp.this, "لا يوجد عنوان !", Toast.LENGTH_LONG).show();
                            }


                        }
                    });

                    b4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AddSale(sp.getSelectedItem().toString(), position);

                        }
                    });

                    b5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            removeData(sp.getSelectedItem().toString(), position);

                        }
                    });

                    b3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final androidx.appcompat.app.AlertDialog.Builder builder2 = new androidx.appcompat.app.AlertDialog.Builder(Delivery_Emp.this);
                            LayoutInflater inflater2 = Delivery_Emp.this.getLayoutInflater();
                            builder2.setView(inflater2.inflate(R.layout.dialog_redirect2, null));
                            final androidx.appcompat.app.AlertDialog dialog2 = builder2.create();
                            WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                            lp2.copyFrom(dialog2.getWindow().getAttributes());
                            lp2.width = WindowManager.LayoutParams.WRAP_CONTENT;
                            lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;

                            dialog2.show();
                            dialog2.getWindow().setAttributes(lp2);

                            final Spinner sp2 = dialog2.findViewById(R.id.type2);
                            adapterSpin = new ArrayAdapter<String>(Delivery_Emp.this, R.layout.items_row3, R.id.item, dname);
                            sp2.setAdapter(adapterSpin);

                            getLngLat();

                            Button bbb = dialog2.findViewById(R.id.call);
                            bbb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String ll = "";
                                    if(!latlng.isEmpty())
                                        ll = latlng.get(position).substring(latlng.get(position).indexOf("daddr=")+6);

                                    String temp = info.get(position);
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("user_name", temp.substring(temp.indexOf("الأسم : ")+8, temp.indexOf("الهاتف : ")).replace("\n", ""));
                                    map.put("user_mobile", temp.substring(temp.indexOf("الهاتف : ")+9, temp.indexOf("قائمة : ")).replace("\n", ""));
                                    map.put("user_desc", temp.substring(temp.indexOf("ملاحظات : ")+10, temp.indexOf("وقت الإستلام : ")).replace("\n", ""));
                                    map.put("time", temp.substring(temp.indexOf("وقت الإستلام : ")+15, temp.indexOf("سعر توصيل : ")).replace("\n", ""));
                                    map.put("item_list",temp.substring(temp.indexOf("قائمة : ")+8, temp.indexOf("النقاط : ")).replace("\n", ""));
                                    map.put("item_sum_price", temp.substring(temp.indexOf("المجموع : ")+10).replace("\n", ""));
                                    map.put("point_sum", temp.substring(temp.indexOf("النقاط : ")+9, temp.indexOf("ملاحظات : ")).replace("\n", ""));
                                    map.put("d_price", temp.substring(temp.indexOf("سعر توصيل : ")+12, temp.indexOf("العنوان : ")).replace("\n", ""));
                                    map.put("email", "");
                                    try{map.put("lat", ll.substring(0, ll.indexOf(",")));
                                        map.put("lng", ll.substring(ll.indexOf(",")+1));}
                                    catch (Exception e){}

                                    db.collection("Res_1_Delivery")
                                            .document(sp2.getSelectedItem().toString())
                                            .collection("1")
                                            .document(map.get("user_name")).set(map)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(Delivery_Emp.this, "تم نقل الطلب", Toast.LENGTH_SHORT).show();
                                                        removeData(sp.getSelectedItem().toString(), position); }
                                                }
                                            });

                                }
                            });


                        }
                    });

                }
                else {
                    final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Delivery_Emp.this);
                    LayoutInflater inflater = Delivery_Emp.this.getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.dialog_redirect, null));
                    final androidx.appcompat.app.AlertDialog dialog = builder.create();
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    dialog.show();
                    dialog.getWindow().setAttributes(lp);

                    Button b1, b2, b3, b4, b5, b6;
                    TextView t = dialog.findViewById(R.id.title);

                    b1 = dialog.findViewById(R.id.call);
                    b2 = dialog.findViewById(R.id.go);
                    b3 = dialog.findViewById(R.id.redirect);
                    b4 = dialog.findViewById(R.id.confirm);
                    b6 = dialog.findViewById(R.id.confirm2);
                    b5 = dialog.findViewById(R.id.remove);

                    t.setText("You Can Pick To...");
                    b1.setText("contact The customer");
                    b2.setText("show customer address");
                    b3.setText("transfer the order to another driver");
                    b4.setText("print receipt (For Driver)");
                    b6.setText("receipt confirmation (For Customer)");
                    b5.setText("remove order");

                    b6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            PrintDriverEn(sp.getSelectedItem().toString(), position);

                        }
                    });

                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String str = info.get(position);
                            String num = str.substring(str.indexOf("Phone : ")+8, str.indexOf("Menu : "));
                            num = RemoveSpace(num);
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", num, null)));

                        }
                    });

                    b2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(!latlng.isEmpty()){

                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                        Uri.parse(latlng.get(position)));
                                startActivity(intent);
                            }
                            else{
                                getLngLat();
                                Toast.makeText(Delivery_Emp.this, "No Address !", Toast.LENGTH_LONG).show();
                            }



                        }
                    });

                    b4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AddSale2(sp.getSelectedItem().toString(), position);

                        }
                    });

                    b5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            removeData(sp.getSelectedItem().toString(), position);

                        }
                    });

                    b3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final androidx.appcompat.app.AlertDialog.Builder builder2 = new androidx.appcompat.app.AlertDialog.Builder(Delivery_Emp.this);
                            LayoutInflater inflater2 = Delivery_Emp.this.getLayoutInflater();
                            builder2.setView(inflater2.inflate(R.layout.dialog_redirect2, null));
                            final androidx.appcompat.app.AlertDialog dialog2 = builder2.create();
                            WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
                            lp2.copyFrom(dialog2.getWindow().getAttributes());
                            lp2.width = WindowManager.LayoutParams.WRAP_CONTENT;
                            lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;

                            dialog2.show();
                            dialog2.getWindow().setAttributes(lp2);

                            final Spinner sp2 = dialog2.findViewById(R.id.type2);
                            adapterSpin = new ArrayAdapter<String>(Delivery_Emp.this, R.layout.items_row3, R.id.item, dname);
                            sp2.setAdapter(adapterSpin);

                            Button bbb = dialog2.findViewById(R.id.call);
                            TextView t = dialog2.findViewById(R.id.title);
                            t.setText("Transfer Order to Another Driver");
                            bbb.setText("Transfer");

                            bbb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String ll = "";
                                    if(!latlng.isEmpty())
                                        ll = latlng.get(position).substring(latlng.get(position).indexOf("daddr=")+6);

                                    String temp = info.get(position);
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("user_name", temp.substring(temp.indexOf("Name : ")+7, temp.indexOf("Phone : ")).replace("\n", ""));
                                    map.put("user_mobile", temp.substring(temp.indexOf("Phone : ")+8, temp.indexOf("Menu : ")).replace("\n", ""));
                                    map.put("user_desc", temp.substring(temp.indexOf("Notes : ")+8, temp.indexOf("Pickup Time")).replace("\n", ""));
                                    map.put("time", temp.substring(temp.indexOf("Pickup Time : ")+14, temp.indexOf("Delivery Price")).replace("\n", ""));
                                    map.put("item_list",temp.substring(temp.indexOf("Menu : ")+7, temp.indexOf("Points : ")).replace("\n", ""));
                                    map.put("item_sum_price", temp.substring(temp.indexOf("Total : ")+8).replace("\n", ""));
                                    map.put("point_sum", temp.substring(temp.indexOf("Points : ")+9, temp.indexOf("Notes : ")).replace("\n", ""));
                                    map.put("d_price", temp.substring(temp.indexOf("Delivery Price : ")+17, temp.indexOf("Address : ")).replace("\n", ""));
                                    map.put("email", "");
                                    try{map.put("lat", ll.substring(0, ll.indexOf(",")));
                                        map.put("lng", ll.substring(ll.indexOf(",")+1));}
                                    catch (Exception e){}

                                    db.collection("Res_1_Delivery")
                                            .document(sp2.getSelectedItem().toString())
                                            .collection("1")
                                            .document(map.get("user_name")).set(map)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(Delivery_Emp.this, "Done", Toast.LENGTH_SHORT).show();
                                                        removeData(sp.getSelectedItem().toString(), position); }
                                                }
                                            });

                                }
                            });


                        }
                    });
                }
            }
        });

    }

    private void getLngLat() {

        latlng.clear();

        db.collection("Res_1_Delivery")
                .document(p).collection("1")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot document : task.getResult())
                    AddCoar(document.get("lat").toString()+","+document.get("lng").toString());

            }
        });



    }

    private void AddCoar(String add) {

        latlng.add("http://maps.google.com/maps?daddr="+add);

    }

    public void downloadData(String path){

        info.clear();
        adapter.notifyDataSetChanged();
        p = path;

        db.collection("Res_1_Delivery")
                .document(path).collection("1")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for(QueryDocumentSnapshot document : task.getResult())

                {

                    try {
                        if (task.isSuccessful()) {
                            if (lang==1){

                                info.add("الأسم : "+document.get("user_name").toString()+"\n"
                                        +"الهاتف : "+document.get("user_mobile").toString()+"\n"
                                        +"قائمة : "+document.get("item_list").toString()+"\n"
                                        +"النقاط : "+document.get("point_sum").toString()+"\n"
                                        +"ملاحظات : "+document.get("user_desc").toString()+"\n"
                                        +"وقت الإستلام : "+document.get("time").toString()+"\n"
                                        +"سعر توصيل : "+document.get("d_price").toString()+"\n"
                                        +"العنوان : "+"jordan"+"\n"
                                        +"المجموع : "+document.get("item_sum_price").toString()+"\n" );
                            }
                            else{
                                info.add("Name : "+document.get("user_name").toString()+"\n"
                                        +"Phone : "+document.get("user_mobile").toString()+"\n"
                                        +"Menu : "+document.get("item_list").toString()+"\n"
                                        +"Points : "+document.get("point_sum").toString()+"\n"
                                        +"Notes : "+document.get("user_desc").toString()+"\n"
                                        +"Pickup Time : "+document.get("time").toString()+"\n"
                                        +"Delivery Price : "+document.get("d_price").toString()+"\n"
                                        +"Address : "+"Jordan"+"\n"
                                        +"Total : "+document.get("item_sum_price").toString()+"\n" );
                            }

                            adapter.notifyDataSetChanged();

                            if(info.isEmpty())
                                if (lang==1){
                                    Toast.makeText(Delivery_Emp.this, "لايوجد طلبات دلفري", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(Delivery_Emp.this, "No delivery request", Toast.LENGTH_SHORT).show();
                                }
                        }
                    } catch(Exception e){new AlertDialog.Builder(Delivery_Emp.this).setMessage(e.getMessage()).show();}

                }

            }
        });

        getLngLat(); }

    public void removeData(String path, int pos){

        String temp="";

        if(HomeAct.lang != 1)
            temp = info.get(pos).substring(info.get(pos).indexOf(" : ")+3, info.get(pos).indexOf("Phone : "));
        else
            temp = info.get(pos).substring(info.get(pos).indexOf(" : ")+3, info.get(pos).indexOf("الهاتف : "));

        db.collection("Res_1_Delivery").document(path).collection("1").document(temp.replaceAll("\n","")).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            loc.clear();
                            info.clear();
                            adapter.clear();

                            adapter.notifyDataSetChanged();

                            if (HomeAct.lang==1){
                                Toast.makeText(Delivery_Emp.this, "تم", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Delivery_Emp.this, "Done", Toast.LENGTH_SHORT).show();
                            }

                            recreate();
                        }
                    }
                });

    }

    public String RemoveSpace(String str){

        char [] arr = str.toCharArray();
        String temp = "";

        for(int i=0; i<arr.length; i++)
            if(Character.isDigit(arr[i]) || arr[i] == '.')
                temp+=arr[i];

        return temp;
    }

    public void PrintDriverAr(final String path, final int pos){
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
                .indexOf("قائمة : ")+8, info.get(pos).indexOf("المجموع :"))
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

        bill+="\n"+"مرحبا بك في مطعم شاورما هايبرد"+",";
        bill+="\n"+"نوع الفاتورة : فاتورة توصيل"+",";
        bill+="\n\n"+",";
        bill+="تاريخ : "+day+"\n"+",";
        bill+="وقت : "+time+"\n"+",";
        bill+="__________________________________________\n\n\n"+",";

        for(int i=0; i<temp.length-1; i++)
            bill+="\nالمادة : "+temp2[i];

        bill+=",";

        double taxValue = Double.parseDouble(getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0"))*0.01;
        double billval = Double.parseDouble(info.get(pos).substring(info.get(pos).indexOf("المجموع : ")+10).replace(getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"), ""));
        double su = billval-(billval*taxValue);

        bill+="\n,\nمجموع الفاتورة : "+(su)+",";
        bill+="\n\nقيمة الضريبة : "+getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0")+" %"+"\n"+",";
        bill+="\n\nمجموع كلي : "+info.get(pos).substring(info.get(pos).indexOf("المجموع : ")+10)+"\n"+",";
        bill+="\n\n\nأهلا و سهلا زبائننا الكرام\n\n\n,";

        PrintUsingServer(bill);

    }

    public void PrintDriverEn(final String path, final int pos){

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
                .indexOf("Menu : ")+7, info.get(pos).indexOf("Points :"))
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

        bill+="\n"+"Welcome To Hybrid Shawarma Restaurant"+",";
        bill+="\n"+"Bill Type : Delivery Bill"+",";
        bill+="\n\n"+",";
        bill+="Date : "+day+"\n"+",";
        bill+="Time : "+time+"\n"+",";
        bill+="__________________________________________\n\n\n"+",";

        for(int i=0; i<temp.length; i++)
            bill+="\nItem : "+temp2[i];

        double taxValue = Double.parseDouble(getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0"))*0.01;
        double billval = Double.parseDouble(info.get(pos).substring(info.get(pos).indexOf("Total : ")+8).replace(getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"), ""));
        double su = billval-(billval*taxValue);

        bill+=",";

        bill+="\n,\nBill Value : "+(su)+",";
        bill+="\n\nTax Value: "+getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0")+" %"+"\n"+",";
        bill+="\n\nTotal Bill : "+info.get(pos).substring(info.get(pos).indexOf("Total : ")+8)+"\n"+",";
        bill+="\n\n\nCome Again Soon !\n\n\n,";

        PrintUsingServer(bill);

        //test on printers _______________________________________________****************************

    }

    public void AddSale(final String path, final int pos){
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
                .indexOf("قائمة : ")+8, info.get(pos).indexOf("المجموع :"))
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

        bill+="\n"+"مرحبا بك في مطعم شاورما هايبرد"+",";
        bill+="\n"+"نوع الفاتورة : فاتورة توصيل"+",";
        bill+="\n\n"+",";
        bill+="تاريخ : "+day+"\n"+",";
        bill+="وقت : "+time+"\n"+",";
        bill+="__________________________________________\n\n\n"+",";

        Map<String, Object> sale = new HashMap<>();

        for(int i=0; i<temp.length-1; i++){

            sale.put("date", day);
            sale.put("time", time);
            sale.put("subItem", temp[i].substring(0, temp[i].indexOf(" X")));
            sale.put("item", "");
            sale.put("empEmail", auth.getCurrentUser().getEmail());

            double p = Double.parseDouble(temp[i].substring(temp[i].indexOf("السعر : ")+8));
            int c = Integer.parseInt(temp[i].substring(temp[i].indexOf("X")+1, temp[i].indexOf(" السعر : ")));
            sale.put("sale", p*c);

            bill+="\nالمادة : "+temp2[i];
            db.collection("Res_1_sales").document().set(sale);
        }

        double taxValue = Double.parseDouble(getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0"))*0.01;
        double billval = Double.parseDouble(info.get(pos).substring(info.get(pos).indexOf("المجموع : ")+10).replace(getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"), ""));
        double su = billval-(billval*taxValue);

        bill+=",";

        bill+="\n,\nمجموع الفاتورة : "+(su)+",";
        bill+="\n\nقيمة الضريبة : "+getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0")+" %"+"\n"+",";
        bill+="\n\nمجموع كلي : "+info.get(pos).substring(info.get(pos).indexOf("المجموع : ")+10)+"\n"+",";
        bill+="\n\n\nأهلا و سهلا زبائننا الكرام\n\n\n,";

        removeData(path, pos);
        PrintUsingServer(bill);

    }

    public void AddSale2(final String path, final int pos){

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
                .indexOf("Menu : ")+7, info.get(pos).indexOf("Points :"))
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

        bill+="\n"+"Welcome To Hybrid Shawarma Restaurant"+",";
        bill+="\n"+"Bill Type : Delivery Bill"+",";
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

        bill+="\n,\nBill Value : "+(su)+",";
        bill+="\n\nTax Value: "+getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0")+" %"+"\n"+",";
        bill+="\n\nTotal Bill : "+info.get(pos).substring(info.get(pos).indexOf("Total : ")+8)+"\n"+",";
        bill+="\n\n\nCome Again Soon !\n\n\n,";

        removeData(path, pos);
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

            SocketAddress socketAddress = new InetSocketAddress("192.168.123.1", 9100);
            Socket socket = new Socket();

            socket.connect(socketAddress, 5000);

            OutputStreamWriter clientSocketWriter = (new OutputStreamWriter(socket.getOutputStream(), "UTF8")); //optional encoding
            clientSocketWriter.write(s);
            clientSocketWriter.close();
            socket.close();


        }
        catch(Exception e){
            if(HomeAct.lang == 1)
                Toast.makeText(this, "لا يوجد طابعة !!!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "No Printer Attached !!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void Print(String str) {

        try {

            SocketAddress socketAddress = new InetSocketAddress("192.168.1.3", 9100);
            Socket socket = new Socket();

            socket.connect(socketAddress, 2000);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8")); //optional encoding
            writer.write(str);
            writer.write("\n\n\n\f");
            writer.close();
            socket.close();

        }
        catch(Exception e){
            if(HomeAct.lang == 1)
                Toast.makeText(this, "لا يوجد طابعة !!!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "No Printer Attached !!!", Toast.LENGTH_LONG).show();
        }

    }

    public void getDriver(){

        dname.clear();

        db.collection("Res_1_employee")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful())
                            for (QueryDocumentSnapshot document : task.getResult())
                                if(document.getId().contains(".del"))
                                    addData(document.get("Fname").toString()+" "+document.get("Lname").toString());

                        addData("برنامج");
                    } });

    }

    public void addData(String name){

        dname.add(name);
        adapterSpin.notifyDataSetChanged();

    }


}
