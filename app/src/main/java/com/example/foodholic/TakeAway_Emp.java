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

public class TakeAway_Emp extends AppCompatActivity {

    FirebaseFirestore db;

    ListView list;
    ArrayAdapter<String> adapter;

    ArrayList<String> id;
    ArrayList<String> info;
    ArrayList<String>dname;

    ArrayAdapter<String> adapterSpin;

    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_take_away__emp);

        db = FirebaseFirestore.getInstance();

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button bt = findViewById(R.id.btn);
        if(HomeAct.lang == 1) {
            getSupportActionBar().setTitle("الرجوع");
            bt.setText("عرض جميع الطلبات");
        }
        else {
            getSupportActionBar().setTitle("Go Back");
            bt.setText("View All Orders");
        }

        list=findViewById(R.id.list);

        id = new ArrayList<String>();
        info = new ArrayList<String>();
        dname = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, R.layout.items_row3, R.id.item, info);
        list.setAdapter(adapter);

        final Spinner sp = findViewById(R.id.type);
        adapterSpin = new ArrayAdapter<String>(TakeAway_Emp.this, R.layout.items_row3, R.id.item, dname);
        sp.setAdapter(adapterSpin);

        getDriver();

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(HomeAct.lang == 1)
                    downloadData();
                else
                    downloadDataAr();

                flag = false;
            }
        });

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(HomeAct.lang == 1)
                    downloadData(sp.getSelectedItem().toString());
                else
                    downloadDataAr(sp.getSelectedItem().toString());

                flag = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //downloadData();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long ido) {

                if(HomeAct.lang == 1){
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(TakeAway_Emp.this);
                    LayoutInflater inflater = TakeAway_Emp.this.getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.dialog_redirect3, null));
                    final android.support.v7.app.AlertDialog dialog = builder.create();
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    dialog.show();
                    dialog.getWindow().setAttributes(lp);

                    Button b1, b4, b5;

                    b1 = dialog.findViewById(R.id.call);
                    b4 = dialog.findViewById(R.id.confirm);
                    b5 = dialog.findViewById(R.id.remove);

                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String str = info.get(position);
                            String num = str.substring(str.indexOf("الهاتف : ")+9, str.indexOf("الملاحظات : "));
                            num = RemoveSpace(num);
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", num, null)));

                        }
                    });

                    b4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(flag || dname.size() == 1)
                                AddSale(sp.getSelectedItem().toString(), position);
                            else
                                Toast.makeText(TakeAway_Emp.this, "الرجاء الاختيار من لائحة الوقت .", Toast.LENGTH_LONG).show();

                        }
                    });

                    b5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(flag)
                                removeData(sp.getSelectedItem().toString());
                            else
                                Toast.makeText(TakeAway_Emp.this, "الرجاء الاختيار من لائحة الوقت .", Toast.LENGTH_LONG).show();

                        }
                    });
                }
                else{
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(TakeAway_Emp.this);
                    LayoutInflater inflater = TakeAway_Emp.this.getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.dialog_redirect3a, null));
                    final android.support.v7.app.AlertDialog dialog = builder.create();
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    dialog.show();
                    dialog.getWindow().setAttributes(lp);

                    Button b1, b4, b5;

                    b1 = dialog.findViewById(R.id.call);
                    b4 = dialog.findViewById(R.id.confirm);
                    b5 = dialog.findViewById(R.id.remove);

                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String str = info.get(position);
                            String num = str.substring(str.indexOf("Mobile : ")+9, str.indexOf("Notes : "));
                            num = RemoveSpace(num);
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", num, null)));

                        }
                    });

                    b4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(flag || dname.size() == 1)
                                AddSale(sp.getSelectedItem().toString(), position);
                            else
                                Toast.makeText(TakeAway_Emp.this, "Please Pick A Time From List", Toast.LENGTH_LONG).show();

                        }
                    });

                    b5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(flag)
                                removeData(sp.getSelectedItem().toString());
                            else
                                Toast.makeText(TakeAway_Emp.this, "Please Pick A Time From List", Toast.LENGTH_LONG).show();

                        }
                    });
                }


            }
        });

    }

    public void downloadData(){

        info.clear();
        adapter.notifyDataSetChanged();

        db.collection("Res_1_TakeAway")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for(QueryDocumentSnapshot document : task.getResult()){

                        info.add("الأسم : "+document.get("user_name").toString()+"\n"
                                +"الهاتف : "+document.get("user_mobile").toString()+"\n"
                                +"بريد : "+document.get("email").toString()+"\n"
                                +"الملاحظات : "+document.get("user_loc").toString()+"\n"
                                +"الطلب : "+document.get("item_list").toString()+"\n"
                                +"مجموع النقاط : "+document.get("point_sum").toString()+" نقطة"+"\n"
                                +"مجموع المبلغ : "+document.get("item_sum_price").toString()+" دينار"+"\n" );

                    }


                    adapter.notifyDataSetChanged();
                    if(info.isEmpty())
                        Toast.makeText(TakeAway_Emp.this, "لايوجد طلبات سفري", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void downloadDataAr(){

        info.clear();
        adapter.notifyDataSetChanged();

        db.collection("Res_1_TakeAway")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for(QueryDocumentSnapshot document : task.getResult()){

                        info.add("Name : "+document.get("user_name").toString()+"\n"
                                +"Mobile : "+document.get("user_mobile").toString()+"\n"
                                +"Email : "+document.get("email").toString()+"\n"
                                +"Notes : "+document.get("user_loc").toString()+"\n"
                                +"Order : "+document.get("item_list").toString()+"\n"
                                +"Point Sum : "+document.get("point_sum").toString()+" Point"+"\n"
                                +"Bill Sum : "+document.get("item_sum_price").toString()+" JOD"+"\n" );

                    }


                    adapter.notifyDataSetChanged();
                    if(info.isEmpty())
                        Toast.makeText(TakeAway_Emp.this, "You Have No TakeAway's", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void downloadData(String path){

        info.clear();
        adapter.notifyDataSetChanged();

        db.collection("Res_1_TakeAway")
                .document(path).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    info.add("الأسم : "+task.getResult().get("user_name").toString()+"\n"
                            +"الهاتف : "+task.getResult().get("user_mobile").toString()+"\n"
                            +"بريد : "+task.getResult().get("email").toString()+"\n"
                            +"الملاحظات : "+task.getResult().get("user_loc").toString()+"\n"
                            +"الطلب : "+task.getResult().get("item_list").toString()+"\n"
                            +"مجموع النقاط : "+task.getResult().get("point_sum").toString()+" نقطة"+"\n"
                            +"مجموع المبلغ : "+task.getResult().get("item_sum_price").toString()+" دينار"+"\n" );

                    adapter.notifyDataSetChanged();
                    if(info.isEmpty())
                        Toast.makeText(TakeAway_Emp.this, "لايوجد طلبات سفري", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void downloadDataAr(String path){

        info.clear();
        adapter.notifyDataSetChanged();

        db.collection("Res_1_TakeAway")
                .document(path).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    info.add("Name : "+task.getResult().get("user_name").toString()+"\n"
                            +"Mobile : "+task.getResult().get("user_mobile").toString()+"\n"
                            +"Email : "+task.getResult().get("email").toString()+"\n"
                            +"Notes : "+task.getResult().get("user_loc").toString()+"\n"
                            +"Order : "+task.getResult().get("item_list").toString()+"\n"
                            +"Point Sum : "+task.getResult().get("point_sum").toString()+" Point"+"\n"
                            +"Bill Sum : "+task.getResult().get("item_sum_price").toString()+" JOD"+"\n" );

                    adapter.notifyDataSetChanged();
                    if(info.isEmpty())
                        Toast.makeText(TakeAway_Emp.this, "You Have No TakeAway's", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void removeData(String path){

            db.collection("Res_1_TakeAway").document(path).delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            info.clear();
                            adapter.clear();

                            adapter.notifyDataSetChanged();
                            if(HomeAct.lang == 1)
                                Toast.makeText(TakeAway_Emp.this, "تم", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(TakeAway_Emp.this, "Done", Toast.LENGTH_SHORT).show();
                            recreate(); } });
    }

    public String RemoveSpace(String str){

        char [] arr = str.toCharArray();
        String temp = "";

        for(int i=0; i<arr.length; i++)
            if(Character.isDigit(arr[i]) || arr[i] == '.')
                temp+=arr[i];

        return temp;
    }

    public void getDriver(){

        dname.clear();

        db.collection("Res_1_TakeAway")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful())
                            for (QueryDocumentSnapshot document : task.getResult())
                                addData(document.getId());

                    } });

    }

    public void addData(String name){

        dname.add(name);
        adapterSpin.notifyDataSetChanged();

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

        String [] temp;

        if(HomeAct.lang == 1){
            temp = info.get(pos).substring(info.get(pos)
                    .indexOf("الطلب : ")+8, info.get(pos).indexOf("مجموع النقاط :"))
                    .replaceAll("= ", "X").replaceAll(":", "Price : ").replaceAll("\n", "")
                    .split(","); }
        else{
            temp = info.get(pos).substring(info.get(pos)
                    .indexOf("Order : ")+8, info.get(pos).indexOf("Point Sum :"))
                    .replaceAll("= ", "X").replaceAll(":", "Price : ").replaceAll("\n", "")
                    .split(","); }

        if(temp[temp.length-1].equals(" ")){
            String [] temp2 = temp;
            temp = new String[temp2.length-1];
            for(int i=0; i<temp.length; i++)
                temp[i] = temp2[i]; }

        bill+="WELCOME TO HYBRID RESTAURANT\n";
        bill+="Bill Type : Take Away\n";
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

            double p = Double.parseDouble(temp[i].substring(temp[i].indexOf("Price : ")+8));
            int c = Integer.parseInt(temp[i].substring(temp[i].indexOf("X")+1, temp[i].indexOf(" Price : ")));
            sale.put("sale", p*c);

            bill+="\nItem : "+temp[i]+"\n";
            db.collection("Res_1_sales").document().set(sale);
        }

        bill+="\nBill Value : "+info.get(pos).substring(info.get(pos).indexOf("المجموع : ")+10)+"\n";
        bill+="\n\n\nTHANK YOU FOR YOUR PURCHASE, COME AGAIN !\n\n\n";


        //getPoint(e.replace("\n", ""), t);
        removeData(path);
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

    private void getPoint(final String e, final String t) {

        final Map<String, Object> tempMap = new HashMap<>();

        db = FirebaseFirestore.getInstance();

        db.collection("Res_1_Customer_Acc").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful())
                            for (QueryDocumentSnapshot document : task.getResult())
                                if(document.get("email").toString().equals(e)){
                                    tempMap.put("email", document.get("email"));
                                    tempMap.put("mobile", document.get("mobile"));
                                    tempMap.put("name", document.get("name"));
                                    tempMap.put("pass", document.get("pass"));
                                    tempMap.put("points", document.get("points"));
                                    addPoint(document.getId(), tempMap, t); }
                    }
                });

    }

    private void addPoint(String id, Map<String, Object> map, String newP) {

        String points = String.valueOf(Double.parseDouble(map.get("points").toString()) + Double.parseDouble(newP));
        map.put("points", points);
        db.collection("Res_1_Customer_Acc").document(id).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(TakeAway_Emp.this, "تمت العملية بنجاح", Toast.LENGTH_SHORT).show();
                        recreate();
                    }
                });

    }

}
