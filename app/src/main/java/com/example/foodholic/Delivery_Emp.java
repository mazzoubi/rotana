package com.example.foodholic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.google.firebase.firestore.FirebaseFirestore;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery__emp);
        SharedPreferences shared2;
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        if(shared2.getString("language", "").equals("arabic")) {
            lang=1;
        }
        db = FirebaseFirestore.getInstance();

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Delivery_Emp.this);
                    LayoutInflater inflater = Delivery_Emp.this.getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.dialog_redirect, null));
                    final android.support.v7.app.AlertDialog dialog = builder.create();
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    dialog.show();
                    dialog.getWindow().setAttributes(lp);

                    Button b1, b2, b3, b4, b5;

                    b1 = dialog.findViewById(R.id.call);
                    b2 = dialog.findViewById(R.id.go);
                    b3 = dialog.findViewById(R.id.redirect);
                    b4 = dialog.findViewById(R.id.confirm);
                    b5 = dialog.findViewById(R.id.remove);

                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String str = info.get(position);
                            String num = str.substring(str.indexOf("الهاتف : ")+9, str.indexOf("قائمة : "));
                            num = RemoveSpace(num);
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", num, null)));

                        }
                    });

                    b2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            getLngLat();

                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("http://maps.google.com/maps?daddr="+latlng.get(position)));
                            startActivity(intent);

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

                            final android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(Delivery_Emp.this);
                            LayoutInflater inflater2 = Delivery_Emp.this.getLayoutInflater();
                            builder2.setView(inflater2.inflate(R.layout.dialog_redirect2, null));
                            final android.support.v7.app.AlertDialog dialog2 = builder2.create();
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
                            bbb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String temp = info.get(position);
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("user_name", temp.substring(temp.indexOf("الأسم : ")+8, temp.indexOf("الهاتف : ")).replace("\n", ""));
                                    map.put("user_mobile", temp.substring(temp.indexOf("الهاتف : ")+9, temp.indexOf("قائمة : ")).replace("\n", ""));
                                    map.put("user_desc", temp.substring(temp.indexOf("ملاحظات : ")+10, temp.indexOf("سعر توصيل : ")).replace("\n", ""));
                                    map.put("item_list",temp.substring(temp.indexOf("قائمة : ")+8, temp.indexOf("النقاط : ")).replace("\n", ""));
                                    map.put("item_sum_price", temp.substring(temp.indexOf("المجموع : ")+10).replace("\n", ""));
                                    map.put("point_sum", temp.substring(temp.indexOf("النقاط : ")+9, temp.indexOf("ملاحظات : ")).replace("\n", ""));
                                    map.put("d_price", temp.substring(temp.indexOf("سعر توصيل : ")+12, temp.indexOf("العنوان : ")).replace("\n", ""));
                                    map.put("email", "");
                                    map.put("lat", "");
                                    map.put("lng", "");

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
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Delivery_Emp.this);
                    LayoutInflater inflater = Delivery_Emp.this.getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.dialog_redirect, null));
                    final android.support.v7.app.AlertDialog dialog = builder.create();
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    dialog.show();
                    dialog.getWindow().setAttributes(lp);

                    Button b1, b2, b3, b4, b5;

                    b1 = dialog.findViewById(R.id.call);
                    b2 = dialog.findViewById(R.id.go);
                    b3 = dialog.findViewById(R.id.redirect);
                    b4 = dialog.findViewById(R.id.confirm);
                    b5 = dialog.findViewById(R.id.remove);

                    b1.setText("contact with customer");
                    b2.setText("show customer address");
                    b3.setText("transfer the order to another driver");
                    b4.setText("receipt confirmation");
                    b5.setText("remove order");
                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String str = info.get(position);
                            String num = str.substring(str.indexOf("phone : ")+9, str.indexOf("menu : "));
                            num = RemoveSpace(num);
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", num, null)));

                        }
                    });

                    b2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            getLngLat();

                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("http://maps.google.com/maps?daddr="+latlng.get(position)));
                            startActivity(intent);

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

                            final android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(Delivery_Emp.this);
                            LayoutInflater inflater2 = Delivery_Emp.this.getLayoutInflater();
                            builder2.setView(inflater2.inflate(R.layout.dialog_redirect2, null));
                            final android.support.v7.app.AlertDialog dialog2 = builder2.create();
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
                            bbb.setText("contact with customer");
                            bbb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String temp = info.get(position);
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("user_name", temp.substring(temp.indexOf("الأسم : ")+8, temp.indexOf("الهاتف : ")).replace("\n", ""));
                                    map.put("user_mobile", temp.substring(temp.indexOf("الهاتف : ")+9, temp.indexOf("قائمة : ")).replace("\n", ""));
                                    map.put("user_desc", temp.substring(temp.indexOf("ملاحظات : ")+10, temp.indexOf("سعر توصيل : ")).replace("\n", ""));
                                    map.put("item_list",temp.substring(temp.indexOf("قائمة : ")+8, temp.indexOf("النقاط : ")).replace("\n", ""));
                                    map.put("item_sum_price", temp.substring(temp.indexOf("المجموع : ")+10).replace("\n", ""));
                                    map.put("point_sum", temp.substring(temp.indexOf("النقاط : ")+9, temp.indexOf("ملاحظات : ")).replace("\n", ""));
                                    map.put("d_price", temp.substring(temp.indexOf("سعر توصيل : ")+12, temp.indexOf("العنوان : ")).replace("\n", ""));
                                    map.put("email", "");
                                    map.put("lat", "");
                                    map.put("lng", "");

                                    db.collection("Res_1_Delivery")
                                            .document(sp2.getSelectedItem().toString())
                                            .collection("1")
                                            .document(map.get("user_name")).set(map)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(Delivery_Emp.this, "delivery is done", Toast.LENGTH_SHORT).show();
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
                    latlng.add(document.get("lat").toString()+","+document.get("lng").toString());
            }
        });

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

    }

    public void removeData(String path, int pos){

        String temp = info.get(pos).substring(info.get(pos).indexOf(" : ")+3, info.get(pos).indexOf("الهاتف : "));
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

    public void AddSale(final String path, final int pos){

        String bill="";
        FirebaseAuth auth = FirebaseAuth.getInstance();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        Date dateee = new Date();
        String date = dateFormat.format(dateee);

        String day = date.substring(0, date.indexOf(" "));
        String time = date.substring(date.indexOf(" ")+1);

        String [] temp = info.get(pos).substring(info.get(pos)
                .indexOf("قائمة : ")+8, info.get(pos).indexOf("النقاط"))
                .replaceAll("= ", "X").replaceAll(":", "Single Price : ").replaceAll("\n", "")
                .split(",");

        bill+="WELCOME TO HYBRID RESTAURANT\n";
        bill+="Bill Type : Delivery\n";
        bill+="\n\n";
        bill+="Date : "+day+"\n";
        bill+="Time : "+time+"\n";
        bill+="__________________________________________\n\n\n";

        Map<String, Object> sale = new HashMap<>();

        for(int i=0; i<temp.length; i++){

            sale.put("date", day);
            sale.put("time", time);
            sale.put("subItem", temp[i].substring(0, temp[i].indexOf(" X")));
            sale.put("item", "");
            sale.put("empEmail", auth.getCurrentUser().getEmail());

            double p = Double.parseDouble(temp[i].substring(temp[i].indexOf("Single Price : ")+14));
            int c = Integer.parseInt(temp[i].substring(temp[i].indexOf("X")+1, temp[i].indexOf(" Single Price : ")));
            sale.put("sale", p*c);

            bill+="\nItem : "+temp[i]+"\n";
            db.collection("Res_1_sales").document().set(sale);
        }

        bill+="\nBill Value : "+info.get(pos).substring(info.get(pos).indexOf("المجموع : ")+10)+"\n";
        bill+="\n\n\nTHANK YOU FOR YOUR PURCHASE, COME AGAIN !\n\n\n";

        removeData(path, pos);
        PrintUsingServer(bill);

    }

    private void PrintUsingServer(String s) {

        try {

            SocketAddress socketAddress = new InetSocketAddress("192.168.14.54", 9100);
            Socket socket = new Socket();

            socket.connect(socketAddress, 5000);

            OutputStreamWriter clientSocketWriter = (new OutputStreamWriter(socket.getOutputStream(), "UTF8")); //optional encoding
            clientSocketWriter.write(s);
            clientSocketWriter.close();
            socket.close();

        }
        catch(Exception e){
            Toast.makeText(this, "لا يوجد طابعة !!!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "لا يوجد طابعة !!!", Toast.LENGTH_SHORT).show();
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
                    } });

    }

    public void addData(String name){

        dname.add(name);
        adapterSpin.notifyDataSetChanged();

    }


}
