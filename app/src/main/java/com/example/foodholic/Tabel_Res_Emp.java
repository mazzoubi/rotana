package com.example.foodholic;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Tabel_Res_Emp extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener date_;
    ArrayList<String> tabels = new ArrayList<String>();
    public static String[] sites = {
            "1", "2", "3", "4", "5",
            "6", "7", "8","9","10", "11","12",
            "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22","23","24", "25","26", "27", "28", "29", "30" };

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

            Holder holder=new Holder();
            final View rowView;

            rowView = inflater.inflate(R.layout.sample_gridlayout, null);
            holder.os_text =(TextView) rowView.findViewById(R.id.os_texts);
            holder.os_text.setText(result[position]);

            if(!tabels.get(position).contains("Table Number : ") && checkDateRes(tabels.get(position))){

                if (shared.getInt("pos", 0) == position)
                    rowView.setBackgroundColor(getResources().getColor(R.color.colorPick));
                else
                    rowView.setBackgroundColor(getResources().getColor(R.color.colorPrimary)); }


            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    getResData(position+1);

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

                    final TextView date, time;
                    final Button reserve, cancle;
                    final EditText name, mobile, people;

                    date = dialog.findViewById(R.id.date);
                    time = dialog.findViewById(R.id.time);
                    reserve = dialog.findViewById(R.id.reserv);
                    cancle = dialog.findViewById(R.id.cancle);
                    name = dialog.findViewById(R.id.name);
                    mobile = dialog.findViewById(R.id.mobile);
                    people = dialog.findViewById(R.id.people);

                    final Calendar myCalendar = Calendar.getInstance();
                    date_ = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            String myFormat = "dd/MM/yy"; //In which you need put here
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                            date.setText(sdf.format(myCalendar.getTime())); } };

                    date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            new DatePickerDialog(Tabel_Res_Emp.this, date_, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                        }
                    });

                    time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final Calendar c = Calendar.getInstance();
                            int hour = c.get(Calendar.HOUR_OF_DAY);
                            int minute = c.get(Calendar.MINUTE);

                            TimePickerDialog timePickerDialog = new TimePickerDialog(Tabel_Res_Emp.this,
                                    new TimePickerDialog.OnTimeSetListener() {

                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                            time.setText(hourOfDay + ":" + minute);
                                        }
                                    }, hour, minute, false);
                            timePickerDialog.show();

                        }
                    });

                    reserve.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ReserveClick(position, rowView, name.getText().toString(), mobile.getText().toString(),
                                    people.getText().toString(), date.getText().toString(), time.getText().toString());
                            dialog.dismiss();
                            recreate();
                        }
                    });
                    cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences.Editor editor = shared.edit();
                            editor.putInt("pos", 0);
                            editor.apply();

                            new AlertDialog.Builder(Tabel_Res_Emp.this)
                                    .setTitle("الفاتورة").setMessage(info.get(position+1))
                                    .setPositiveButton("طباعة", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            String bill="";
                                            FirebaseAuth auth = FirebaseAuth.getInstance();

                                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
                                            Date dateee = new Date();
                                            String date = dateFormat.format(dateee);

                                            String day = date.substring(0, date.indexOf(" "));
                                            String time = date.substring(date.indexOf(" ")+1);

                                            String [] temp = info.get(position+1).substring(info.get(position+1)
                                                    .indexOf("الطلب : ")+8, info.get(position+1).indexOf("مجموع"))
                                                    .replaceAll("= ", "X").replaceAll(":", "Price : ").replaceAll("\n", "")
                                                    .split(",");

                                            if(temp[temp.length-1].equals(" ")){
                                                String [] temp2 = temp;
                                                temp = new String[temp2.length-1];
                                                for(int d=0; d<temp.length; d++)
                                                    temp[d] = temp2[d]; }

                                            bill+="WELCOME TO HYBRID RESTAURANT\n";
                                            bill+="Bill Type : Cash\n";
                                            bill+="\n\n";
                                            bill+="Date : "+day+"\n";
                                            bill+="Time : "+time+"\n";
                                            bill+="__________________________________________\n\n\n";

                                            Map<String, Object> sale = new HashMap<>();

                                            for(int ii=0; ii<temp.length; ii++){

                                                sale.put("date", day);
                                                sale.put("time", time);
                                                sale.put("subItem", temp[ii].substring(0, temp[ii].indexOf(" X")));
                                                sale.put("item", "");
                                                sale.put("empEmail", auth.getCurrentUser().getEmail());

                                                double p = Double.parseDouble(temp[ii].substring(temp[ii].indexOf("Price : ")+8));
                                                int c = Integer.parseInt(temp[ii].substring(temp[ii].indexOf("X")+1, temp[ii].indexOf(" Price : ")));
                                                sale.put("sale", p*c);

                                                bill+="\nItem : "+temp[ii]+"\n";
                                                db.collection("Res_1_sales").document().set(sale);
                                            }

                                            bill+="\nBill Value : "+info.get(position+1).substring(info.get(position+1).indexOf("مجموع : ")+8)+"\n";
                                            bill+="\n\n\nTHANK YOU FOR YOUR PURCHASE, COME AGAIN !\n\n\n";

                                            removeData(position+1);
                                            PrintUsingServer(bill);

                                        }
                                    }).setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialog.dismiss();
                                }
                            }).show();
//view the fatoorah and print it ____________________________________________

                        }
                    });


                }
            });

            return rowView;
        }

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

    private void removeData(int i) {

        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection("Res_1_Table_Res_").document(i+"").delete();

    }

    private void getResData(final int pos) {

        info.clear();

        for(int i=0; i<tabels.size(); i++)
            info.add("لا يوجد فاتورة");

        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection("Res_1_Table_Res_").document(""+pos)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    try{info.set(pos,
                            "تاريخ : "+task.getResult().get("date").toString()+"\n"+
                                    "وقت : "+task.getResult().get("time").toString()+"\n"+
                                    "الاسم : "+task.getResult().get("name").toString()+"\n"+
                                    "هاتف : "+task.getResult().get("mobile").toString()+"\n"+
                                    "اشخاص : "+task.getResult().get("people").toString()+"\n"+
                                    "الطلب : "+task.getResult().get("order").toString()+"\n"+
                                    "مجموع : "+task.getResult().get("sum").toString()+" دينار\n"

                    );}catch (Exception e){}

                }

            }
        });

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

    GridView gridview;
    FirebaseFirestore db;
    SharedPreferences shared, shared2;
    String da, ta;
    String temp = "";
    ArrayList<String> info = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tabel__res__emp);

        Toolbar toolbar = findViewById(R.id.tool);
        toolbar.setTitle("حجز الطاولة");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        shared = getSharedPreferences("order", MODE_PRIVATE);
        shared2 = getSharedPreferences("color", MODE_PRIVATE);

        db = FirebaseFirestore.getInstance();

        Button b = findViewById(R.id.btn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDateTime();
            }
        });

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

    private void AddTables(String count) {

        tabels.clear();
        int c = Integer.parseInt(count);

        for(int i=0; i<c; i++)
            tabels.add("Table Number : "+i);

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

        final Calendar cal = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener lsnr = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                da = sdf.format(cal.getTime()); CheckTabels(); } };

        new DatePickerDialog(Tabel_Res_Emp.this, lsnr, cal
                .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show();

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog lsn = new TimePickerDialog(Tabel_Res_Emp.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        ta = hourOfDay + ":" + minute;

                    }
                }, hour, minute, false);
        lsn.show();


    }

    private void ReserveClick(final int p, final View vie, String name, String mobile, String people,
                              String date, String time){

//        if(shared.getInt("pos", 0) == 0){
//            vie.setBackgroundColor(getResources().getColor(R.color.colorPick));
//            SharedPreferences.Editor editor = shared.edit();
//            editor.putInt("pos", p);
//            editor.apply();

            if(date.contains("تاريخ") || time.contains("وقت"))
                Toast.makeText(Tabel_Res_Emp.this, "اختر وقت و تاريخ رجائا", Toast.LENGTH_SHORT).show();
            else{
                if(getIntent().getStringExtra("order") != null){
                    if(!getIntent().getStringExtra("order").equals("")){

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
                                        if(task.isSuccessful())
                                            Toast.makeText(Tabel_Res_Emp.this, "تم اضافة الحجز", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(Tabel_Res_Emp.this, "حاول مجددا", Toast.LENGTH_SHORT).show();
                                    }
                                });
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
                                    if(task.isSuccessful())
                                        Toast.makeText(Tabel_Res_Emp.this, "تم اضافة الحجز", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(Tabel_Res_Emp.this, "حاول مجددا", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }

      //  }
       // else
        //    Toast.makeText(Tabel_Res_Emp.this, "لديك حجز مسبق", Toast.LENGTH_LONG).show();

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
