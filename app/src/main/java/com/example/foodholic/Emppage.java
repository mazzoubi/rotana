package com.example.foodholic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

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
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Emppage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseFirestore db;

    int Count = 0;

    SharedPreferences shared;
    SharedPreferences shared2;
    SharedPreferences shared3;

    public static String empEmail;
    public static classEmployee empObj;

    ArrayList<classItem>items;
    ArrayList<classSubItem>subItems;
    ArrayList<classSubItem>subItemsAfterFilering;

    ArrayAdapter<String> adapterSpin;

    ArrayList<String>subItemToShow;
    ArrayList<String>itemToShow;
    ListView itemList,subList,resList;
    Button ref,add,pay;

    classCashSale sale;
    ArrayList<classCashSale> saleList=new ArrayList<>();
    ArrayAdapter<classCashSale> adp;

    double sum=0;
    EditText ee;
    Map<String, Object> cash;
    ArrayList<String>cname;
    ArrayList<String>dname;
    ArrayList<String>cmobile;
    ArrayList<String>caddress;
    ArrayAdapter<String> adapter;
    ArrayList<String> temp;

    public static classCloseOpenCash closeOpenCash = new classCloseOpenCash();

    GridView gridview;
    String da, ta;


    ArrayList<String> tabels = new ArrayList<String>();
    public static String[] sites = new String[250];
//    public static String[] sites = {
//            "1", "2", "3", "4", "5",
//            "6", "7", "8","9","10", "11","12",
//            "13", "14", "15", "16", "17", "18", "19",
//            "20", "21", "22","23","24", "25","26", "27", "28", "29", "30" };

    public class CustomAdapter extends BaseAdapter {

        String [] result;
        Context context;

        private LayoutInflater inflater=null;

        public CustomAdapter(Emppage table, String[] site) {

            for(int i=0; i<250; i++) sites[i] = (i+1)+"";

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

            Emppage.CustomAdapter.Holder holder=new Emppage.CustomAdapter.Holder();
            final View rowView;

            rowView = inflater.inflate(R.layout.sample_gridlayout, null);
            holder.os_text =(TextView) rowView.findViewById(R.id.os_texts);
            holder.os_text.setText(result[position]);

            if(!tabels.get(position).contains("Table Number : ") && checkDateRes(tabels.get(position))){

                if (shared.getInt("pos", 0) == position)
                    rowView.setBackgroundColor(getResources().getColor(R.color.colorPick));
                else
                    rowView.setBackgroundColor(getResources().getColor(R.color.colorPrimary)); }

            return rowView;
        }

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
        catch (ParseException e) { return false; }

        if(d2.compareTo(d1) == 0)
            return true;
        else
            return false; }

    String UID="";
    String EMAIL="";
    String PO="";

    final Map<String, Object> tempMap = new HashMap<>();

    boolean falafel = false;
    boolean restart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        restart = false;

        Intent intent = new Intent(this, MyService.class);
        startService(intent);

        if(HomeAct.lang == 1)
            setContentView(R.layout.activity_del_emp);
        else
            setContentView(R.layout.activity_del_emp2);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        da = sdf.format(new Date());

        String myFormat2 = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2, Locale.US);
        ta = sdf2.format(new Date());

        cname = new ArrayList<String>();
        dname = new ArrayList<String>();
        cmobile = new ArrayList<String>();
        caddress = new ArrayList<String>();

        empEmail=getIntent().getStringExtra("empemail");
        empLoadInfo(empEmail);
        ee = new EditText(Emppage.this);
        if (HomeAct.lang==1){
            ee.setHint("أدخل مقدار الكاش");
        }
        else {
            ee.setHint("Enter the amount of cash");
        }

        ee.setInputType(InputType.TYPE_CLASS_NUMBER);

        cash = new HashMap<String, Object>();

        shared = getSharedPreferences("delivery", MODE_PRIVATE);
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        shared3 = getSharedPreferences("cash", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = shared.getString("MyObject", "");
        if(!json.equals(""))
            closeOpenCash = gson.fromJson(json, classCloseOpenCash.class);

        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (shared2.getString("language", "").equals("arabic"))
            toolbar.setTitle("مؤسسة الدخيل");
        else
            toolbar.setTitle("Welcome Employee");

        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        if(shared2.getString("language", "").equals("arabic")){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_del_emp_drawer2); }
        else{
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_del_emp_drawer); }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        db=FirebaseFirestore.getInstance();

        itemList=findViewById(R.id.cashItemList);
        subList=findViewById(R.id.cashSubitem);
        resList=findViewById(R.id.cashResultList);

        Button close, btn2, btn3, btn4, btn5, btn6, btn7, re;
        re = findViewById(R.id.btn);
        close = findViewById(R.id.btn9);
        btn2 = findViewById(R.id.btn5);
        btn3 = findViewById(R.id.btn2);
        btn4 = findViewById(R.id.btn3);
        btn5 = findViewById(R.id.btn4);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);

        Button tcount = findViewById(R.id.counter);
        if(HomeAct.lang == 1)
            tcount.setText("رقم الفاتورة : "+shared3.getInt("count", 0)+"");
        else
            tcount.setText("Bill ID : "+shared3.getInt("count", 0)+"");

        Button sup = findViewById(R.id.btnnew4);
        sup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent n11=new Intent(getApplicationContext(),suppliers.class);
                startActivity(n11);

            }
        });

        Button vip = findViewById(R.id.btnnew1);
        vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent n11=new Intent(getApplicationContext(),vipActivity.class);
                startActivity(n11);

            }
        });

        Button not = findViewById(R.id.btnnew2);
        not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent n11=new Intent(getApplicationContext(), notificationActivity.class);
                startActivity(n11);

            }
        });

        Button zeroC = findViewById(R.id.btnnew3);
        zeroC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(HomeAct.lang == 1){

                    new AlertDialog.Builder(Emppage.this)
                            .setTitle("تنبيه !!!")
                            .setMessage("أنت على وشك تصفير الدور الى 0, هل أنت متأكد ؟")
                            .setCancelable(false)
                            .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences.Editor editor = shared3.edit();
                                    editor.putInt("count" , 0);
                                    editor.apply();

                                    Button tcount = findViewById(R.id.counter);
                                    tcount.setText("رقم الفاتورة : 0");
                                }
                            }).setNegativeButton("لا", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }

                else{

                    new AlertDialog.Builder(Emppage.this)
                            .setTitle("Alert !!!")
                            .setMessage("You Are About To Zero The Bill ID Count, Are You sure ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences.Editor editor = shared3.edit();
                                    editor.putInt("count" , 0);
                                    editor.apply();

                                    Button tcount = findViewById(R.id.counter);

                                    tcount.setText("Bill ID : 0");
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }

            }
        });

        Button serv = findViewById(R.id.btn51996);
        serv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(HomeAct.lang == 1)
                    Toast.makeText(Emppage.this, "قريبا !!!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(Emppage.this, "Coming Soon !!!", Toast.LENGTH_LONG).show();

//                Intent n11=new Intent(getApplicationContext(), DriveCustom.class);
//                startActivity(n11);

            }
        });

        try{
            if(getIntent().getStringExtra("empemail").contains(".cap")){
                btn2.setVisibility(View.GONE);
                btn3.setVisibility(View.GONE);
                btn4.setVisibility(View.GONE);
                btn6.setVisibility(View.GONE);
                btn7.setVisibility(View.GONE);
                close.setVisibility(View.GONE);
                re.setVisibility(View.GONE);

                sup.setVisibility(View.GONE);
                vip.setVisibility(View.GONE);
                not.setVisibility(View.GONE);
                zeroC.setVisibility(View.GONE);
                serv.setVisibility(View.GONE);
                btn5.setWidth(2500); }
        } catch (Exception e){}

        SharedPreferences.Editor editor = shared.edit();
        editor.putString("cash", "");
        editor.apply();



        if(shared2.getString("cash", "").equals("")){
            if (HomeAct.lang==1 && (!getIntent().getStringExtra("empemail").contains(".cap"))){

                if(ee.getParent()!=null)
                    ((ViewGroup)ee.getParent()).removeView(ee);

                new AlertDialog.Builder(Emppage.this)
                        .setTitle("الرجاء فتح الكاش")
                        .setView(ee).setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Emppage.this.finish();
                    }
                }).setPositiveButton("ادخال", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        if(!ee.getText().toString().equals("")){

                            SharedPreferences.Editor editor = shared2.edit();
                            editor.putString("cash", ee.getText().toString());
                            editor.apply();

                            cash.put("cash", ee.getText().toString());
                            closeOpenCash.floor = Double.parseDouble(ee.getText().toString());


                            String form = "dd-MM-yy";
                            String form2 = "HH:mm dd-MM-yy";
                            SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                            SimpleDateFormat sdf2 = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

                            try {

                                Date parsedDate = sdf.parse(new Date().toString());
                                Date parsedDate2 = sdf2.parse(new Date().toString());

                                SimpleDateFormat print = new SimpleDateFormat(form);
                                SimpleDateFormat print2 = new SimpleDateFormat(form2);

                                closeOpenCash.dateOpen = print.format(parsedDate);
                                closeOpenCash.dateAndTimeOpen = print2.format(parsedDate2);

                            }
                            catch (ParseException e) {
                                new AlertDialog.Builder(Emppage.this).setMessage(e+"").show();
                            }

                            closeOpenCash.empEmail = getIntent().getStringExtra("empemail");



                        }
                        else{
                            dialog.dismiss();
                            Emppage.this.finish();
                        }


                    }
                }).setCancelable(false).show();

            }
            else {

                if((!getIntent().getStringExtra("empemail").contains(".cap"))){

                    if(ee.getParent()!=null)
                        ((ViewGroup)ee.getParent()).removeView(ee);

                    new AlertDialog.Builder(Emppage.this)
                            .setTitle("Please open cash")
                            .setView(ee).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Emppage.this.finish();
                        }
                    }).setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {

                            if(!ee.getText().toString().equals("")){

                                SharedPreferences.Editor editor = shared2.edit();
                                editor.putString("cash", ee.getText().toString());
                                editor.apply();

                                cash.put("cash", ee.getText().toString());
                                closeOpenCash.floor = Double.parseDouble(ee.getText().toString());


                                String form = "dd-MM-yy";
                                String form2 = "HH:mm dd-MM-yy";
                                SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                                SimpleDateFormat sdf2 = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

                                try {

                                    Date parsedDate = sdf.parse(new Date().toString());
                                    Date parsedDate2 = sdf2.parse(new Date().toString());

                                    SimpleDateFormat print = new SimpleDateFormat(form);
                                    SimpleDateFormat print2 = new SimpleDateFormat(form2);

                                    closeOpenCash.dateOpen = print.format(parsedDate);
                                    closeOpenCash.dateAndTimeOpen = print2.format(parsedDate2);

                                }
                                catch (ParseException e) {
                                    new AlertDialog.Builder(Emppage.this).setMessage(e+"").show();
                                }

                                closeOpenCash.empEmail = getIntent().getStringExtra("empemail");



                            }
                            else{
                                dialog.dismiss();
                                Emppage.this.finish();
                            }


                        }
                    }).setCancelable(false).show();

                }
            }
        }

        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (empObj.cashWork){

                    final AlertDialog.Builder builder2 = new AlertDialog.Builder(Emppage.this);
                LayoutInflater inflater2 = Emppage.this.getLayoutInflater();
                builder2.setView(inflater2.inflate(R.layout.calulator, null));
                final AlertDialog dialog2 = builder2.create();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                dialog2.show();

                TextView t = dialog2.findViewById(R.id.title);
                String s = String.valueOf(sum);

                EditText p = dialog2.findViewById(R.id.pay);
                TextView c = dialog2.findViewById(R.id.change);

                Button po = dialog2.findViewById(R.id.poin);
                Button dee = dialog2.findViewById(R.id.deliv);
                Button vii = dialog2.findViewById(R.id.visa);

                if(HomeAct.lang != 1){
                    p.setHint("Paid Amount");
                    c.setText("Change : 0.0 JOD");
                    po.setText("Point Pay");
                    dee.setText("Cash Pay");
                    vii.setText("Visa Pay"); }

                try{
                    if (s.length() > 10){
                    if(shared2.getString("language", "").equals("arabic"))
                        t.setText("مجموع الفاتورة : " + s.substring(s.indexOf("."), s.indexOf(".") + 3) + getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " ``"));
                    else
                        t.setText("Total Bill : " + s.substring(s.indexOf("."), s.indexOf(".") + 3) + getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD ")); }
                else{
                    if(shared2.getString("language", "").equals("arabic"))
                        t.setText("مجموع الفاتورة : " + sum + getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"));
                    else
                        t.setText("Total Bill : " + sum + getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD ")); } }
                catch (Exception e){}

                closeOpenCash.total += sum;

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
                            String temp = String.valueOf((Double.parseDouble(e.getText().toString()) - sum));
                            try {
                                if(shared2.getString("language", "").equals("arabic")){
                                    t2.setText("الباقي : " + temp.substring(0, temp.indexOf(".") + 3) + getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"));
                                } else {
                                    t2.setText("The rest : " + temp.substring(0, temp.indexOf(".") + 3) + "  ");
                                }
                            } catch (Exception ex) {
                                if(shared2.getString("language", "").equals("arabic")){
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

                Button b2 = dialog2.findViewById(R.id.poin);
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final AlertDialog.Builder builder3 = new AlertDialog.Builder(Emppage.this);
                        LayoutInflater inflater3 = Emppage.this.getLayoutInflater();
                        builder3.setView(inflater3.inflate(R.layout.calulator2, null));
                        final AlertDialog dialog3 = builder3.create();
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                        dialog3.show();

                        TextView t3 = dialog3.findViewById(R.id.title);
                        String s = String.valueOf(sum * 10);
                        if(shared2.getString("language", "").equals("arabic")){
                            t3.setText("مجموع الفاتورة بالنقاط : " + s + " نقطة ");
                        }
                        else {
                            t3.setText("total bill at points : " + s + " point ");
                        }

                        AutoCompleteTextView aa1 = dialog3.findViewById(R.id.email);
                        TextView aa2 = dialog3.findViewById(R.id.po);
                        TextView aa3 = dialog3.findViewById(R.id.change);
                        EditText aa4 = dialog3.findViewById(R.id.pay);
                        Button aa5 = dialog3.findViewById(R.id.sea);
                        Button aa6 = dialog3.findViewById(R.id.poinaa);
                        Button aa7 = dialog3.findViewById(R.id.delivaa);

                        if(HomeAct.lang != 1){

                            aa1.setHint("Customer Email");
                            aa2.setHint("Customer Points");
                            aa3.setText("Change : ");
                            aa4.setHint("Paid Amount");
                            aa5.setText("Search Account");
                            aa6.setText("Point Pay");
                            aa7.setText("Cash Pay");

                        }

                        final TextView t2 = dialog3.findViewById(R.id.change);
                        final EditText e = dialog3.findViewById(R.id.pay);
                        e.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if (s.toString().equals(""))
                                    e.setText("0");
                                else {
                                    String temp = String.valueOf((Double.parseDouble(e.getText().toString()) - sum * 10));
                                    try {
                                        if(shared2.getString("language", "").equals("arabic")){
                                            t2.setText("الباقي : " + temp.substring(0, temp.indexOf(".") + 3) + " نقطة ");
                                        } else {
                                            t2.setText("The rest : " + temp.substring(0, temp.indexOf(".") + 3) + "  ");
                                        }
                                    } catch (Exception ex) {
                                        if(shared2.getString("language", "").equals("arabic")){
                                            t2.setText("الباقي : " + temp + " نقطة ");
                                        } else {
                                            t2.setText("The rest : " + temp + " point ");
                                        }

                                    }
                                }

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });

                        Button b = dialog3.findViewById(R.id.delivaa);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog3.dismiss();
                            }
                        });
                        Button b2 = dialog3.findViewById(R.id.poinaa);
                        b2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                takePoint(String.valueOf(sum * 10));
                                dialog3.dismiss();
                                dialog2.dismiss();
                            }
                        });

                        temp = new ArrayList<String>();

                        adapter = new ArrayAdapter<String>(Emppage.this, android.R.layout.simple_dropdown_item_1line, temp);
                        final AutoCompleteTextView et1 = dialog3.findViewById(R.id.email);
                        et1.setAdapter(adapter);

                        db.collection("Res_1_Customer_Acc")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        if (task.isSuccessful())
                                            for (QueryDocumentSnapshot document : task.getResult())
                                                addCustomData(document.get("email").toString());

                                    }
                                });

                        Button bbb = dialog3.findViewById(R.id.sea);
                        bbb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                getPoint(et1.getText().toString());

                                TextView t = dialog3.findViewById(R.id.po);
                                t.setText(PO);

                                EditText ete = dialog3.findViewById(R.id.pay);
                                ete.setText(String.valueOf(PO));

                            }
                        });

                    }
                });


                Button b = dialog2.findViewById(R.id.deliv);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(e.getText().toString().equals(""))
                            e.setText("0");

                            if ((Double.parseDouble(e.getText().toString()) - sum) >= 0 && !e.getText().toString().equals("")) {

                            if(HomeAct.lang == 1)
                                AddSaleAra(Double.parseDouble(e.getText().toString()), (Double.parseDouble(e.getText().toString()) - sum), sum);
                            else
                                AddSale(Double.parseDouble(e.getText().toString()), (Double.parseDouble(e.getText().toString()) - sum), sum);
                            dialog2.dismiss();
                            recreate();
                        } else {
                            if(shared2.getString("language", "").equals("arabic")){
                                Toast.makeText(Emppage.this, "الرجاء ادخال مبلغ صحيح", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Emppage.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                            }
                        }




                    }
                });

                    Button vbtn = dialog2.findViewById(R.id.visa);
                    vbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if ((Double.parseDouble(e.getText().toString()) - sum) >= 0 && !e.getText().toString().equals("")) {

                                if(HomeAct.lang == 1)
                                    AddVisaSaleAra(Double.parseDouble(e.getText().toString()), (Double.parseDouble(e.getText().toString()) - sum), sum);
                                else
                                    AddVisaSale(Double.parseDouble(e.getText().toString()), (Double.parseDouble(e.getText().toString()) - sum), sum);
                                dialog2.dismiss();
                                recreate();
                            } else {
                                if(shared2.getString("language", "").equals("arabic")){
                                    Toast.makeText(Emppage.this, "الرجاء ادخال مبلغ صحيح", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Emppage.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });

            } else {
                    if(shared2.getString("language", "").equals("arabic"))
                        Toast.makeText(Emppage.this, "انت لا تمتلك صلاحيات الدخول الى هذا الاجراء", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(Emppage.this, "you don't have permission enter", Toast.LENGTH_SHORT).show();
                }

                SharedPreferences.Editor prefsEditor = shared.edit();
                Gson gson = new Gson();
                String json = gson.toJson(closeOpenCash);
                prefsEditor.putString("MyObject", json);
                prefsEditor.apply();

            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(empObj.delevery){

                    if (saleList.isEmpty())
                        startActivity(new Intent(Emppage.this, Delivery_Emp.class));
                    else{

                        if(HomeAct.lang == 1){

                            final AlertDialog.Builder builder = new AlertDialog.Builder(Emppage.this);
                            LayoutInflater inflater = Emppage.this.getLayoutInflater();
                            builder.setView(inflater.inflate(R.layout.addcustom2, null));
                            final AlertDialog dialog = builder.create();
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialog.getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                            dialog.show();
                            dialog.getWindow().setAttributes(lp);

                            getDriver();

                            final Spinner sp = dialog.findViewById(R.id.type);

                            adapterSpin = new ArrayAdapter<String>
                                    (Emppage.this, android.R.layout.simple_spinner_item, dname);
                            sp.setAdapter(adapterSpin);

                            final EditText name2 = dialog.findViewById(R.id.name);
                            final EditText mobile2 = dialog.findViewById(R.id.mobile);
                            final EditText desc2 = dialog.findViewById(R.id.desc);

                            Button d = dialog.findViewById(R.id.deliv);
                            d.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    final EditText name, mobile, desc, dprice;
                                    name = dialog.findViewById(R.id.name);
                                    mobile = dialog.findViewById(R.id.mobile);
                                    desc = dialog.findViewById(R.id.desc);
                                    dprice = dialog.findViewById(R.id.deliva);

                                    if(!cname.contains(name.getText().toString())) {
                                        if (HomeAct.lang==1){
                                            new AlertDialog.Builder(Emppage.this)
                                                    .setMessage("هل ترغب بتسجيل العميل ؟")
                                                    .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            AddClient(name.getText().toString(),
                                                                    mobile.getText().toString(),
                                                                    desc.getText().toString());

                                                            dialog.dismiss();
                                                        }
                                                    }).setNegativeButton("لا", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialog.dismiss();
                                                    recreate();
                                                }
                                            }).show();

                                            Map<String, Object> order = new HashMap<>();
                                            order.put("user_name", name.getText().toString());
                                            order.put("user_mobile", mobile.getText().toString());
                                            order.put("user_desc", desc.getText().toString());
                                            order.put("item_list", MenuSum());
                                            order.put("item_sum_price", sum+"");
                                            order.put("point_sum", "0");
                                            order.put("d_price", dprice.getText().toString());
                                            order.put("email", "");
                                            order.put("lat", "");
                                            order.put("time", "");
                                            order.put("lng", "");

                                            db.collection("Res_1_Delivery")
                                                    .document(sp.getSelectedItem().toString())
                                                    .collection("1")
                                                    .document(name.getText().toString()).set(order)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful())
                                                                Toast.makeText(Emppage.this, "تم اضافة طلبك", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                        }
                                        else {
                                            new AlertDialog.Builder(Emppage.this)
                                                    .setMessage("Do you want to register the client?")
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            AddClient(name.getText().toString(),
                                                                    mobile.getText().toString(),
                                                                    desc.getText().toString());

                                                            dialog.dismiss();
                                                        }
                                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialog.dismiss();
                                                }
                                            }).show();

                                            Map<String, Object> order = new HashMap<>();
                                            order.put("user_name", name.getText().toString());
                                            order.put("user_mobile", mobile.getText().toString());
                                            order.put("user_desc", desc.getText().toString());
                                            order.put("item_list", MenuSum());
                                            order.put("item_sum_price", sum+"");
                                            order.put("point_sum", "0");
                                            order.put("d_price", dprice.getText().toString());
                                            order.put("email", "");
                                            order.put("lat", "");
                                            order.put("time", "");
                                            order.put("lng", "");

                                            db.collection("Res_1_Delivery")
                                                    .document(sp.getSelectedItem().toString())
                                                    .collection("1")
                                                    .document(name.getText().toString()).set(order)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful())
                                                                Toast.makeText(Emppage.this, "تم إضافة طلبك", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                        }
                                    }



                                }
                            });

                        }
                        else{

                            final AlertDialog.Builder builder = new AlertDialog.Builder(Emppage.this);
                            LayoutInflater inflater = Emppage.this.getLayoutInflater();
                            builder.setView(inflater.inflate(R.layout.addcustom2a, null));
                            final AlertDialog dialog = builder.create();
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialog.getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                            dialog.show();
                            dialog.getWindow().setAttributes(lp);

                            getDriver();

                            final Spinner sp = dialog.findViewById(R.id.type);

                            adapterSpin = new ArrayAdapter<String>
                                    (Emppage.this, android.R.layout.simple_spinner_item, dname);
                            sp.setAdapter(adapterSpin);

                            final EditText name2 = dialog.findViewById(R.id.name);
                            final EditText mobile2 = dialog.findViewById(R.id.mobile);
                            final EditText desc2 = dialog.findViewById(R.id.desc);

                            Button d = dialog.findViewById(R.id.deliv);
                            d.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    final EditText name, mobile, desc, dprice;
                                    name = dialog.findViewById(R.id.name);
                                    mobile = dialog.findViewById(R.id.mobile);
                                    desc = dialog.findViewById(R.id.desc);
                                    dprice = dialog.findViewById(R.id.deliva);

                                    if(!cname.contains(name.getText().toString())) {
                                            new AlertDialog.Builder(Emppage.this)
                                                    .setMessage("Do you want to register the client?")
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            AddClient(name.getText().toString(),
                                                                    mobile.getText().toString(),
                                                                    desc.getText().toString());

                                                            dialog.dismiss();
                                                        }
                                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialog.dismiss();
                                                }
                                            }).show();

                                            Map<String, Object> order = new HashMap<>();
                                            order.put("user_name", name.getText().toString());
                                            order.put("user_mobile", mobile.getText().toString());
                                            order.put("user_desc", desc.getText().toString());
                                            order.put("item_list", MenuSum());
                                            order.put("item_sum_price", sum+"");
                                            order.put("point_sum", "0");
                                            order.put("d_price", dprice.getText().toString());
                                            order.put("email", "");
                                            order.put("lat", "");
                                        order.put("time", "");
                                            order.put("lng", "");

                                            db.collection("Res_1_Delivery")
                                                    .document(sp.getSelectedItem().toString())
                                                    .collection("1")
                                                    .document(name.getText().toString()).set(order)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful())
                                                                Toast.makeText(Emppage.this, "Done", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });


                                    }



                                }
                            });

                        }
                    }


            } else {
                if(HomeAct.lang == 1)
                    Toast.makeText(Emppage.this, "انت لا تمتلك صلاحيات الدخول الى هذا الاجراء", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Emppage.this, "you don't have permission enter", Toast.LENGTH_SHORT).show();
            } }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(empObj.takeAway){

                    if (saleList.isEmpty()){
                        Intent ax = new Intent(Emppage.this, TakeAway_Emp.class);
                        ax.putExtra("empemail",getIntent().getStringExtra("empemail"));
                        startActivity(ax);
                    }

                    else{
                        if(HomeAct.lang == 1){
                            final AlertDialog.Builder builder = new AlertDialog.Builder(Emppage.this);
                            LayoutInflater inflater = Emppage.this.getLayoutInflater();
                            builder.setView(inflater.inflate(R.layout.addcustom, null));
                            final AlertDialog dialog = builder.create();
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialog.getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                            dialog.show();
                            dialog.getWindow().setAttributes(lp);

                            getClient();

                            Spinner sp = dialog.findViewById(R.id.type);
                            adapterSpin = new ArrayAdapter<String>
                                    (Emppage.this, android.R.layout.simple_spinner_item, cname);
                            sp.setAdapter(adapterSpin);

                            final EditText name2 = dialog.findViewById(R.id.name);
                            final EditText mobile2 = dialog.findViewById(R.id.mobile);
                            final EditText desc2 = dialog.findViewById(R.id.desc);

                            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    name2.setText(cname.get(position));
                                    mobile2.setText(cmobile.get(position));
                                    desc2.setText(caddress.get(position));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            Button d = dialog.findViewById(R.id.deliv);
                            d.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    final EditText name, mobile, desc;
                                    name = dialog.findViewById(R.id.name);
                                    mobile = dialog.findViewById(R.id.mobile);
                                    desc = dialog.findViewById(R.id.desc);

                                    new AlertDialog.Builder(Emppage.this)
                                            .setMessage("هل ترغب بتسجيل العميل ؟")
                                            .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    AddClient(name.getText().toString(),
                                                            mobile.getText().toString(),
                                                            desc.getText().toString());
                                                    recreate();
                                                    dialog.dismiss();
                                                }
                                            }).setNegativeButton("لا", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            recreate();
                                            dialog.dismiss();
                                        }
                                    }).show();

                                    String temp = "";
                                    double s = 0;

                                    for(int i=0; i<saleList.size(); i++){
                                        temp+=saleList.get(i).subItemName+" = "+saleList.get(i).count+" : "+saleList.get(i).sumPrice+" , ";
                                        s+=saleList.get(i).sumPrice;

                                        Map<String, Object> order = new HashMap<>();
                                        order.put("user_name", name2.getText().toString());
                                        order.put("user_mobile", mobile2.getText().toString());
                                        order.put("user_loc", "");
                                        order.put("user_desc", desc2.getText().toString());
                                        order.put("item_list", MenuSum());
                                        order.put("item_sum_price", sum+"");
                                        order.put("point_sum", "0");
                                        order.put("d_price", "");
                                        order.put("time", "");
                                        order.put("email", "");

                                        db.collection("Res_1_TakeAway").document(new Date().toString()).set(order)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            Toast.makeText(Emppage.this, "تمت اضافة الطلب", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                    }
                                }
                            });
                        }
                        else {

                            final AlertDialog.Builder builder = new AlertDialog.Builder(Emppage.this);
                            LayoutInflater inflater = Emppage.this.getLayoutInflater();
                            builder.setView(inflater.inflate(R.layout.addcustom3, null));
                            final AlertDialog dialog = builder.create();
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialog.getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                            dialog.show();
                            dialog.getWindow().setAttributes(lp);

                            getClient();

                            Spinner sp = dialog.findViewById(R.id.type);
                            adapterSpin = new ArrayAdapter<String>
                                    (Emppage.this, android.R.layout.simple_spinner_item, cname);
                            sp.setAdapter(adapterSpin);

                            final EditText name2 = dialog.findViewById(R.id.name);
                            final EditText mobile2 = dialog.findViewById(R.id.mobile);
                            final EditText desc2 = dialog.findViewById(R.id.desc);

                            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    name2.setText(cname.get(position));
                                    mobile2.setText(cmobile.get(position));
                                    desc2.setText(caddress.get(position));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            Button d = dialog.findViewById(R.id.deliv);
                            d.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    final EditText name, mobile, desc;
                                    name = dialog.findViewById(R.id.name);
                                    mobile = dialog.findViewById(R.id.mobile);
                                    desc = dialog.findViewById(R.id.desc);

                                    new AlertDialog.Builder(Emppage.this)
                                            .setMessage("Do you want to register the client?")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    AddClient(name.getText().toString(),
                                                            mobile.getText().toString(),
                                                            desc.getText().toString());
                                                    recreate();
                                                    dialog.dismiss();
                                                }
                                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            recreate();
                                            dialog.dismiss();
                                        }
                                    }).show();

                                    String temp = "";
                                    double s = 0;

                                    for(int i=0; i<saleList.size(); i++){
                                        temp+=saleList.get(i).subItemName+" = "+saleList.get(i).count+" : "+saleList.get(i).sumPrice+" , ";
                                        s+=saleList.get(i).sumPrice;

                                        Map<String, Object> order = new HashMap<>();
                                        order.put("user_name", name2.getText().toString());
                                        order.put("user_mobile", mobile2.getText().toString());
                                        order.put("user_loc", "");
                                        order.put("user_desc", desc2.getText().toString());
                                        order.put("item_list", MenuSum());
                                        order.put("item_sum_price", sum+"");
                                        order.put("point_sum", "0");
                                        order.put("d_price", "");
                                        order.put("time", "");
                                        order.put("email", "");

                                        db.collection("Res_1_TakeAway").document(new Date().toString()).set(order)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            Toast.makeText(Emppage.this, "Your request is added", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                    }
                                }
                            });

                             }
                    }

            } else {
                if(HomeAct.lang == 1)
                    Toast.makeText(Emppage.this, "انت لا تمتلك صلاحيات الدخول الى هذا الاجراء", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Emppage.this, "you don't have permission enter", Toast.LENGTH_SHORT).show();
            }}
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(empObj.onlineReservation){

                if (saleList.isEmpty())
                    startActivity(new Intent(Emppage.this, Tabel_Res_Emp.class)
                    .putExtra("empemail", getIntent().getStringExtra("empemail")));
                else{
                    if (HomeAct.lang==1){
                        Toast.makeText(Emppage.this, "اختر الطاولة الان", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(Emppage.this, "Choose the table now", Toast.LENGTH_LONG).show();
                    }
                    String temp = "";
                    double s = 0;

                    for(int i=0; i<saleList.size(); i++){
                        temp+=saleList.get(i).subItemName+" = "+saleList.get(i).count+" : "+saleList.get(i).sumPrice+" , ";
                        s+=saleList.get(i).sumPrice;}

                    Intent intent = new Intent(Emppage.this, Tabel_Res_Emp.class);
                    intent.putExtra("order", temp);
                    intent.putExtra("empemail", getIntent().getStringExtra("empemail"));
                    intent.putExtra("orderSum", s+"");
                    startActivity(intent);
                }

            } else {
                if(shared2.getString("language", "").equals("arabic"))
                    Toast.makeText(Emppage.this, "انت لا تمتلك صلاحيات الدخول الى هذا الاجراء", Toast.LENGTH_SHORT).show();
                else Toast.makeText(Emppage.this, "you don't have permission enter", Toast.LENGTH_SHORT).show();
            }}
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ee.getParent()!=null)
                    ((ViewGroup)ee.getParent()).removeView(ee);

                ee.setText("");

                if (HomeAct.lang==1){

                    final AlertDialog.Builder builder = new AlertDialog.Builder(Emppage.this);
                    LayoutInflater inflater = Emppage.this.getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.dialog_close, null));
                    final AlertDialog dialog = builder.create();
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    dialog.show();
                    dialog.getWindow().setAttributes(lp);

                    final EditText et1, et2, et3, et4, et5;
                    final Button b1, b2;

                    et1 = dialog.findViewById(R.id.name);
                    et2 = dialog.findViewById(R.id.mobile);
                    et3 = dialog.findViewById(R.id.pric);
                    et4 = dialog.findViewById(R.id.desc);
                    et5 = dialog.findViewById(R.id.tot);

                    b1 = dialog.findViewById(R.id.takeaway);
                    b2 = dialog.findViewById(R.id.deliv);

                    closeOpenCash.paid = Double.parseDouble(shared3.getString("pay", "0.0"));
                    final Double nu = ((closeOpenCash.total+closeOpenCash.floor)-closeOpenCash.paid);

                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            et1.setText(closeOpenCash.empEmail);
                            et2.setText(closeOpenCash.dateAndTimeOpen);
                                et5.setText("مبيعات : "+(closeOpenCash.total)+"   ارضية : "+closeOpenCash.floor+
                                        "   مصروفات : "+ closeOpenCash.paid+"\n");
                                et5.setText(et5.getText().toString()+"   مجموع : "+nu);

                            String form = "HH:mm dd-MM-yy";
                            SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

                            try {
                                Date parsedDate = sdf.parse(new Date().toString());
                                SimpleDateFormat print = new SimpleDateFormat(form);
                                closeOpenCash.dateAndTimeClose = print.format(parsedDate);
                                et3.setText(closeOpenCash.dateAndTimeClose); }
                            catch (Exception e){}
                        }
                    });

                    b2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String s1, s2, s3, s4, s5;
                            s1 = et1.getText().toString();
                            s2 = et2.getText().toString();
                            s3 = et3.getText().toString();
                            s4 = et4.getText().toString();
                            s5 = et5.getText().toString();

                            closeAppUpload(s1, s2, s3, s4, s5);

                            dialog.dismiss();
//                            new AlertDialog.Builder(Emppage.this)
//                                    .setMessage("هل ترغب بالخروج ؟")
//                                    .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            dialogInterface.dismiss();
//                                            dialog.dismiss();
//                                        }
//                                    }).setPositiveButton("خروج", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//
//                                    Count = 1;
//                                    onBackPressed();
//
//                                }
//                            }).show();

                        }
                    });
                }
                else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Emppage.this);
                    LayoutInflater inflater = Emppage.this.getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.dialog_close2, null));
                    final AlertDialog dialog = builder.create();
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    dialog.show();
                    dialog.getWindow().setAttributes(lp);

                    final EditText et1, et2, et3, et4, et5;
                    final Button b1, b2;

                    et1 = dialog.findViewById(R.id.name);
                    et2 = dialog.findViewById(R.id.mobile);
                    et3 = dialog.findViewById(R.id.pric);
                    et4 = dialog.findViewById(R.id.desc);
                    et5 = dialog.findViewById(R.id.tot);

                    b1 = dialog.findViewById(R.id.takeaway);
                    b2 = dialog.findViewById(R.id.deliv);

                    closeOpenCash.paid = Double.parseDouble(shared3.getString("pay", "0.0"));
                    final Double nu = ((closeOpenCash.total+closeOpenCash.floor)-closeOpenCash.paid);

                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            et1.setText(closeOpenCash.empEmail);
                            et2.setText(closeOpenCash.dateAndTimeOpen);
                            et5.setText("Sale : "+(closeOpenCash.total)+"   Floor : "+closeOpenCash.floor+
                                    "   Payment : "+ closeOpenCash.paid+"\n");
                            et5.setText(et5.getText().toString()+"   Total : "+nu);

                            String form = "HH:mm dd-MM-yy";
                            SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

                            try {
                                Date parsedDate = sdf.parse(new Date().toString());
                                SimpleDateFormat print = new SimpleDateFormat(form);
                                closeOpenCash.dateAndTimeClose = print.format(parsedDate);
                                et3.setText(closeOpenCash.dateAndTimeClose); }
                            catch (Exception e){}
                        }
                    });

                    b2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            closeAppUpload(et1.getText().toString(), et2.getText().toString(),
                                    et3.getText().toString(), et4.getText().toString(),
                                    et5.getText().toString());

                            dialog.dismiss();

//                            new AlertDialog.Builder(Emppage.this)
//                                    .setMessage("Do You Want To Exit ?")
//                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            dialogInterface.dismiss();
//                                            dialog.dismiss();
//                                        }
//                                    }).setPositiveButton("Exit", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//
//                                    Count = 1;
//                                    onBackPressed();
//
//                                }
//                            }).show();

                        }
                    });
                }
            } });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (empObj.cashWork||empObj.warehousea)
                startActivity(new Intent(Emppage.this, aa3.class));
                else {
                    if(shared2.getString("language", "").equals("arabic"))
                        Toast.makeText(Emppage.this, "انت لا تمتلك صلاحيات الدخول الى هذا الاجراء", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(Emppage.this, "you don't have permission enter", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (empObj.addpayment){
                Intent n=new Intent(getApplicationContext(), Payment.class);
                n.putExtra("emp",empEmail);
                startActivity(n);}
                else {
                    if(shared2.getString("language", "").equals("arabic"))
                        Toast.makeText(Emppage.this, "انت لا تمتلك صلاحيات الدخول الى هذا الاجراء", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(Emppage.this, "you don't have permission enter", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadSubItem();

        if(HomeAct.lang == 1)
            loadItem2();
        else
            loadItem();


        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(HomeAct.lang == 1)
                    getSubItem2(items.get(position).itemName);
                else
                    getSubItem(items.get(position).itemName);
            }
        });

        subList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sale=new classCashSale();

                if(falafel)
                    Redo();

                boolean cheack=false;
                int loopPosition=0;

                if (saleList.isEmpty()){
                    sale.count=1;
                    if(HomeAct.lang == 1)
                        sale.subItemName=subItemsAfterFilering.get(position).Ar_subItem;
                    else
                        sale.subItemName=subItemsAfterFilering.get(position).subItem;
                    sale.sumPrice=subItemsAfterFilering.get(position).price;
                    sale.unitPrice=subItemsAfterFilering.get(position).price;
                    sale.kitchen=subItemsAfterFilering.get(position).kitchen;
                    saleList.add(sale);

                }
                else{

                    if(HomeAct.lang == 1){
                        for(int i=0 ; i<saleList.size();i++){
                            if(subItemsAfterFilering.get(position).Ar_subItem.equals(saleList.get(i).subItemName)){
                                saleList.get(i).sumPrice+=subItemsAfterFilering.get(position).price;
                                saleList.get(i).count++;
                                cheack=true;
                                break;
                            }

                        }
                        if(cheack){
                        }
                        else{
                            sale.count=1;
                            sale.subItemName=subItemsAfterFilering.get(position).Ar_subItem;
                            sale.sumPrice=subItemsAfterFilering.get(position).price;
                            sale.unitPrice=subItemsAfterFilering.get(position).price;
                            sale.kitchen=subItemsAfterFilering.get(position).kitchen;
                            saleList.add(sale);
                        }
                    }
                    else{
                        for(int i=0 ; i<saleList.size();i++){
                            if(subItemsAfterFilering.get(position).subItem.equals(saleList.get(i).subItemName)){
                                saleList.get(i).sumPrice+=subItemsAfterFilering.get(position).price;
                                saleList.get(i).count++;
                                cheack=true;
                                break;
                            }

                        }
                        if(cheack){
                        }
                        else{
                            sale.count=1;
                            sale.subItemName=subItemsAfterFilering.get(position).subItem;
                            sale.sumPrice=subItemsAfterFilering.get(position).price;
                            sale.unitPrice=subItemsAfterFilering.get(position).price;
                            sale.kitchen=subItemsAfterFilering.get(position).kitchen;
                            saleList.add(sale);
                        }
                    }


                }


                final ArrayAdapter<classCashSale>adapter=new cashAdapterResultScreen(getApplication(), R.layout.items_row, saleList);
                resList.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int pos, long l) {

                        final EditText e = new EditText(Emppage.this);
                        ee.setInputType(InputType.TYPE_CLASS_NUMBER);

                        if(shared2.getString("language", "").equals("arabic")){
                            new AlertDialog.Builder(Emppage.this)
                                    .setTitle("تعديل على الكمية")
                                    .setView(e).setPositiveButton("تعديل", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    final classCashSale sal=new classCashSale();
                                    sal.subItemName = saleList.get(pos).subItemName;
                                    sal.unitPrice = saleList.get(pos).unitPrice;
                                    sal.count = saleList.get(pos).count;
                                    sal.sumPrice = saleList.get(pos).sumPrice;

                                    if(Integer.parseInt(e.getText().toString()) > 0){
                                        sal.count = Integer.parseInt(e.getText().toString());
                                        sal.sumPrice = sal.count*sal.unitPrice;
                                    }

                                    saleList.set(pos, sal);
                                    dialogInterface.dismiss();
                                    adapter.notifyDataSetChanged();

                                    sum = 0;
                                    for (int j =0; j<saleList.size(); j++)
                                        sum+=saleList.get(j).sumPrice;

                                    String t = "";
                                    t=String.valueOf(sum);
                                    TextView s = findViewById(R.id.tot);

                                    try{
                                        if(t.length() > 10)
                                            s.setText("مجموع الفاتورة : "+t.substring(0, t.indexOf(".")+3)+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"));
                                        else
                                            s.setText("مجموع الفاتورة : "+sum+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار")); }
                                    catch (Exception e) {s.setText("مجموع الفاتورة : "+sum+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"));}

                                }
                            }).setNegativeButton("حذف", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    saleList.remove(pos);
                                    adapter.notifyDataSetChanged();

                                    sum = 0;
                                    for (int j =0; j<saleList.size(); j++)
                                        sum+=saleList.get(j).sumPrice;

                                    String t = "";
                                    t=String.valueOf(sum);
                                    TextView s = findViewById(R.id.tot);

                                    try{
                                        if(t.length() > 10)
                                        s.setText("مجموع الفاتورة : "+t.substring(0, t.indexOf(".")+3)+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"));
                                    else
                                        s.setText("مجموع الفاتورة : "+sum+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار")); }
                                    catch (Exception e){s.setText("مجموع الفاتورة : "+sum+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"));}

                                    dialogInterface.dismiss();
                                }
                            }).show();
                        }
                        else {
                            new AlertDialog.Builder(Emppage.this)
                                    .setTitle("Modification on quantity")
                                    .setView(e).setPositiveButton("Modification", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    final classCashSale sal=new classCashSale();
                                    sal.subItemName = saleList.get(pos).subItemName;
                                    sal.unitPrice = saleList.get(pos).unitPrice;
                                    sal.count = saleList.get(pos).count;
                                    sal.sumPrice = saleList.get(pos).sumPrice;

                                    if(Integer.parseInt(e.getText().toString()) > 0){
                                        sal.count = Integer.parseInt(e.getText().toString());
                                        sal.sumPrice = sal.count*sal.unitPrice;
                                    }

                                    saleList.set(pos, sal);
                                    dialogInterface.dismiss();
                                    adapter.notifyDataSetChanged();

                                    sum = 0;
                                    for (int j =0; j<saleList.size(); j++)
                                        sum+=saleList.get(j).sumPrice;

                                    String t = "";
                                    t=String.valueOf(sum);
                                    TextView s = findViewById(R.id.tot);

                                    try{
                                        if(t.length() > 10)
                                        s.setText("Bill Sum : "+t.substring(0, t.indexOf(".")+3)+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD "));
                                    else
                                        s.setText("Bill Sum : "+sum+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD ")); }
                                    catch (Exception e){s.setText("Bill Sum : "+sum+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD "));}

                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    saleList.remove(pos);
                                    adapter.notifyDataSetChanged();

                                    sum = 0;
                                    for (int j =0; j<saleList.size(); j++)
                                        sum+=saleList.get(j).sumPrice;

                                    String t = "";
                                    t=String.valueOf(sum);
                                    TextView s = findViewById(R.id.tot);

                                    try{
                                        if(t.length() > 10)
                                        s.setText("Bill Sum : "+t.substring(0, t.indexOf(".")+3)+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD "));
                                    else
                                        s.setText("Bill Sum : "+sum+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD ")); }
                                    catch (Exception e) {s.setText("Bill Sum : "+sum+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD "));}
                                    dialogInterface.dismiss();
                                }
                            }).show();
                        }

                    }
                });

                sum = 0;
                for (int i =0; i<saleList.size(); i++)
                    sum+=saleList.get(i).sumPrice;

                String t = "";
                t=String.valueOf(sum);
                TextView s = findViewById(R.id.tot);

                try{
                    if(t.length() > 10){
                        try {
                            if(shared2.getString("language", "").equals("arabic")){
                                s.setText("مجموع الفاتورة : "+t.substring(0, t.indexOf(".")+3)+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"));
                            }
                            else {
                                s.setText("Bill Sum : "+t.substring(0, t.indexOf(".")+3)+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD "));
                            }
                        } catch (Exception e){
                            if(HomeAct.lang == 1){
                                s.setText("مجموع الفاتورة : "+sum+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"));
                            }
                            else {
                                s.setText("Bill Sum : "+sum+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD "));
                            }                        }
                    }
                    else{
                        if (HomeAct.lang==1){
                            s.setText("مجموع الفاتورة : "+sum+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"));
                        }
                        else {
                            s.setText("Bill Sum : "+sum+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD "));
                        }
                    }
                } catch(Exception e){}
            }
        });
        GetTabelCount();



    }

    private void Redo() {



    }

    private void PrintUsingServer(String s) {

        int c = shared3.getInt("count", 0);
        SharedPreferences.Editor editor = shared3.edit();
        editor.putInt("count" ,++c);
        editor.apply();

        if(!s.contains("فاتورة اغلاق") && !s.contains("Close Cash")){
            String temp="[";
            for(int i=0; i<saleList.size(); i++)
                temp+=saleList.get(i).kitchen+",";
            temp+="]";

            s = s+temp;

            if(HomeAct.lang == 1)
                s = ("رقم الفاتورة : "+shared3.getInt("count", 0)+"")+","+s;
            else
                s = ("Bill ID : "+shared3.getInt("count", 0)+"")+","+s;
        }

        try {

            String ip = getSharedPreferences("IPS", MODE_PRIVATE).getString("ip", "192.168.1.3");
            int port = getSharedPreferences("IPS", MODE_PRIVATE).getInt("port", 9100);

            SocketAddress socketAddress = new InetSocketAddress("192.168.123.1", port);
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
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            SharedPreferences.Editor prefsEditor = shared.edit();
            Gson gson = new Gson();
            String json = gson.toJson(closeOpenCash);
            prefsEditor.putString("MyObject", json);
            prefsEditor.apply();
        }
    }

    @Override
    protected void onDestroy() {

//        String str = getIntent().getStringExtra("empemail");
//
//        if(str != null)
//            if((!str.contains(".cap")))
//                closeAppUpload("", "", "", "", "");

        SharedPreferences.Editor prefsEditor = shared.edit();
        Gson gson = new Gson();
        String json = gson.toJson(closeOpenCash);
        prefsEditor.putString("MyObject", json);
        prefsEditor.apply();

        super.onDestroy(); }

    public void closeAppUpload(final String s1, final String s2,
                               final String s3, final String s4,
                               final String s5){
        try{
            String form2 = "HH:mm dd-MM-yy";
            SimpleDateFormat sdf2 = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            Date parsedDate2 = sdf2.parse(new Date().toString());
            SimpleDateFormat print2 = new SimpleDateFormat(form2);
            closeOpenCash.dateAndTimeClose = print2.format(parsedDate2);
            closeOpenCash.note = s4;

        }
        catch(Exception e){}

        restart = true;

        closeOpenCash.sold=closeOpenCash.total;
        closeOpenCash.total=(closeOpenCash.total+closeOpenCash.floor-closeOpenCash.paid);

        db.collection("closeOpenCash").document().set(closeOpenCash)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(restart) {
                            if (task.isSuccessful()) {
                                if (HomeAct.lang == 1) {
                                    returnBill(s1, s2, s3, s4, s5);

                                    SharedPreferences.Editor prefsEditor = shared.edit();
                                    Gson gson = new Gson();
                                    closeOpenCash.total = 0;
                                    String json = gson.toJson(closeOpenCash);
                                    prefsEditor.putString("MyObject", json);
                                    prefsEditor.apply();

                                    prefsEditor = shared.edit();
                                    prefsEditor.remove("MyObject");
                                    prefsEditor.apply();


                                } else {
                                    returnBillEn(s1, s2, s3, s4, s5);

                                    SharedPreferences.Editor prefsEditor = shared.edit();
                                    Gson gson = new Gson();
                                    closeOpenCash.total = 0;
                                    String json = gson.toJson(closeOpenCash);
                                    prefsEditor.putString("MyObject", json);
                                    prefsEditor.apply();

                                    prefsEditor = shared.edit();
                                    prefsEditor.remove("MyObject");
                                    prefsEditor.apply();

                                }


                            } else if (shared2.getString("language", "").equals("arabic")) {
                                Toast.makeText(Emppage.this, "تعذر التخزين !", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Emppage.this, "Error !", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                });  }

    private void returnBill(String s1, String s2,String s3,String s4,String s5) {

        String bill="";

        bill+="\n"+"نوع الفاتورة : فاتورة اغلاق كاش"+",";
        bill+="\n"+",";
        bill+="__________________________________________\n\n\n"+",";

        bill+="\n\nأسم الموظف : "+closeOpenCash.empEmail+"\n"+",";
        bill+="\n\nتاريخ\\وقت فتح الكاش : "+closeOpenCash.dateAndTimeOpen+"\n"+",";
        bill+="\n\nتاريخ\\وقت إغلاق الكاش : "+closeOpenCash.dateAndTimeClose+"\n"+",";
        bill+="\n\nأرضية الكاش : "+closeOpenCash.floor+"\n"+",";
        bill+="\n\nبيع الكاش : "+closeOpenCash.sold+"\n"+",";
        bill+="\n\nمصروفات الكاش : "+closeOpenCash.paid+"\n"+",";
        bill+="\n\nمجموع الكاش : "+closeOpenCash.total+"\n"+",";
        bill+="\n\nملاحظات أخرى : "+closeOpenCash.note+"\n"+",";

        PrintUsingServer(bill);

    }

    private void returnBillEn(String s1, String s2,String s3,String s4,String s5) {

        String bill="";

        bill+="\n"+"Bill Type : Close Cash"+",";
        bill+="\n"+",";
        bill+="__________________________________________\n\n\n"+",";

        bill+="\n\nEmployee Name : "+closeOpenCash.empEmail+"\n"+",";
        bill+="\n\nCash Open Date\\Time : "+closeOpenCash.dateAndTimeOpen+"\n"+",";
        bill+="\n\nCash Close Date\\Time : "+closeOpenCash.dateAndTimeClose+"\n"+",";
        bill+="\n\nCash Floor : "+closeOpenCash.floor+"\n"+",";
        bill+="\n\nSold Amound : "+closeOpenCash.sold+"\n"+",";
        bill+="\n\nCash Payments : "+closeOpenCash.paid+"\n"+",";
        bill+="\n\nCash Total : "+closeOpenCash.total+"\n"+",";
        bill+="\n\nExtra Notes : "+closeOpenCash.note+"\n"+",";

        PrintUsingServer(bill);

    }

    private void AddTables(String count) {

        tabels.clear();
        int c = Integer.parseInt(count);

        for(int i=0; i<c; i++)
            tabels.add("Table Number : "+i);

        FillReserve();

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

    private String MenuSum() {

        String str="";
        for(int i=0; i<saleList.size(); i++)
            str+=saleList.get(i).subItemName+" = "+saleList.get(i).count+" : "+saleList.get(i).unitPrice+",";
        return str;
    }

    public void addCustomData(String str){

        temp.add(str);
        adapter.notifyDataSetChanged();

    }

    private void FillReserve() {

        gridview = (GridView) findViewById(R.id.customgrid);

        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection("Res_1_Table_Res_").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                try{
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult())
                            tabels.set(Integer.parseInt(document.getId())-1, document.get("time").toString()+"@"+document.get("date"));

                        gridview.setAdapter(new Emppage.CustomAdapter(Emppage.this, sites)); }

                } catch(Exception ex){

                    gridview.setAdapter(new Emppage.CustomAdapter(Emppage.this, tabels.toArray(new String[tabels.size()]) ));
                }

            } });

    }

    private void getPoint(final String e) {

        db = FirebaseFirestore.getInstance();

        db.collection("Res_1_Customer_Acc").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful())
                            for (QueryDocumentSnapshot document : task.getResult())
                                if(document.get("email").toString().equals(e)){
                                    EMAIL = document.get("email").toString();
                                    PO = document.get("points").toString();
                                    UID = document.getId();

                                    tempMap.put("email", document.get("email"));
                                    tempMap.put("mobile", document.get("mobile"));
                                    tempMap.put("name", document.get("name"));
                                    tempMap.put("pass", document.get("pass"));
                                    tempMap.put("points", document.get("points")); }
                    }
                });



    }

    private void takePoint(String newP) {

        String points = String.valueOf(Double.parseDouble(tempMap.get("points").toString()) - Double.parseDouble(newP));
        tempMap.put("points", points);
        db.collection("Res_1_Customer_Acc").document(UID).set(tempMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(HomeAct.lang == 1)
                            AddSale2();
                        else
                            AddSale2Eng();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(++Count == 2){
                restart = true;
                super.onBackPressed(); }
            else
                if(HomeAct.lang == 1)
                    Toast.makeText(this, "إضغط مرة أخرى للخروج", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Press Back Again To Exit", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(shared2.getString("language", "").equals("arabic"))
            getMenuInflater().inflate(R.menu.del_emp2, menu);
        else
            getMenuInflater().inflate(R.menu.del_emp, menu);
        return true; }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.out) {

//           FirebaseAuth auth = FirebaseAuth.getInstance();
//           auth.signOut();

            SharedPreferences.Editor editor = shared2.edit();
            editor.putString("cash", "");
            editor.apply();

            editor = shared3.edit();
            editor.putString("pay", "0.0");
            editor.apply();

            SharedPreferences.Editor prefsEditor = shared.edit();
            Gson gson = new Gson();
            closeOpenCash.total = 0;
            String json = gson.toJson(closeOpenCash);
            prefsEditor.putString("MyObject", json);
            prefsEditor.apply();

            prefsEditor = shared.edit();
            prefsEditor.remove("MyObject");
            prefsEditor.apply();

           startActivity(new Intent(Emppage.this, Login.class));
           finish();

        }

        else if (id == R.id.offer){

            startActivity(new Intent(Emppage.this, offer.class));

        }

        else if (id == R.id.cont){

            TextClock tc = findViewById(R.id.tc);
            String da = tc.getText().toString();

            startActivity(new Intent(Emppage.this, ContestsActiivty.class)
            .putExtra("date", da));

        }

        else if(id == R.id.noti){

            if(HomeAct.lang == 1){
                final EditText et = new EditText(Emppage.this);
                final Map<String, Object> evnt = new HashMap<String, Object>();

                new AlertDialog.Builder(Emppage.this)
                        .setTitle("إرسال إشعار لزبائن المطعم ؟")
                        .setView(et).setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("إرسال", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        evnt.put("msg", et.getText().toString());

                        db.collection("Res_1_Announcment")
                                .document("Notification")
                                .set(evnt).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Emppage.this, "تم الأرسال بنجاح", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).setCancelable(false).create().show();
            }
            else{
                final EditText et = new EditText(Emppage.this);
                final Map<String, Object> evnt = new HashMap<String, Object>();

                new AlertDialog.Builder(Emppage.this)
                        .setTitle("Send Notification To All Customers ?")
                        .setView(et).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        evnt.put("msg", et.getText().toString());

                        db.collection("Res_1_Announcment")
                                .document("Notification")
                                .set(evnt).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Emppage.this, "Notification Sent !", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).setCancelable(false).create().show();
            }



        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_menu:break;
            case R.id.nav_table:
                if (empObj.tableResev)
                startActivity(new Intent(this, Tabel_Res_Emp.class));
                else {
                    if(shared2.getString("language", "").equals("arabic"))
                        Toast.makeText(this, "انت لا تمتلك صلاحيات الدخول الى هذا الاجراء", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(this, "you don't have permission enter", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_online:
                if (empObj.onlineReservation)
                startActivity(new Intent(this, Online.class));
                else {
                    if(shared2.getString("language", "").equals("arabic"))
                        Toast.makeText(this, "انت لا تمتلك صلاحيات الدخول الى هذا الاجراء", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(this, "you don't have permission enter", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_take:
                if (empObj.takeAway)
                startActivity(new Intent(this, TakeAway_Emp.class));
                else {
                    if(shared2.getString("language", "").equals("arabic"))
                        Toast.makeText(this, "انت لا تمتلك صلاحيات الدخول الى هذا الاجراء", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(this, "you don't have permission enter", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.nav_delivery:
                if (empObj.delevery)
                startActivity(new Intent(this, Delivery_Emp.class));
                else {
                    if(shared2.getString("language", "").equals("arabic"))
                        Toast.makeText(this, "انت لا تمتلك صلاحيات الدخول الى هذا الاجراء", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(this, "you don't have permission enter", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_add:
                if (empObj.warehousea||empObj.cashWork)
                startActivity(new Intent(this, Add_Emp.class));
                else {
                    if(shared2.getString("language", "").equals("arabic"))
                        Toast.makeText(this, "انت لا تمتلك صلاحيات الدخول الى هذا الاجراء", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(this, "you don't have permission enter", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_pay:
                if (empObj.addpayment) {
                    Intent n = new Intent(getApplicationContext(), Payment.class);
                    n.putExtra("emp", empEmail);
                    startActivity(n);
                }
                else {
                    if(shared2.getString("language", "").equals("arabic"))
                        Toast.makeText(this, "انت لا تمتلك صلاحيات الدخول الى هذا الاجراء", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(this, "you don't have permission enter", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_contact:
                startActivity(new Intent(this, Contact.class));
            break;
            case R.id.nav_about:
                startActivity(new Intent(this, About.class));
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadSubItem(){
        db.collection("Res_1_subItem").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                subItems=new ArrayList<>();
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    classSubItem a=d.toObject(classSubItem.class);
                    subItems.add(a);
                }
            }
        });
    }

    public void loadItem(){
        db.collection("Res_1_items").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                items=new ArrayList<>();
                itemToShow=new ArrayList<>();
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    classItem a=d.toObject(classItem.class);
                    items.add(a);
                    itemToShow.add(a.itemName);
                }
                ArrayAdapter<String>adapter=new ArrayAdapter<>(getApplication(),R.layout.items_row, R.id.item, itemToShow);
                itemList.setAdapter(adapter);

            }
        });
    }

    public void loadItem2(){
        db.collection("Res_1_Ar_Items").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                items=new ArrayList<>();
                itemToShow=new ArrayList<>();
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    classItem a=d.toObject(classItem.class);
                    items.add(a);
                    itemToShow.add(a.itemName);
                }
                ArrayAdapter<String>adapter=new ArrayAdapter<>(getApplication(),R.layout.items_row, R.id.item, itemToShow);
                itemList.setAdapter(adapter);

            }
        });
    }

    public void getSubItem(String item){
        subItemToShow=new ArrayList<>();
        subItemsAfterFilering=new ArrayList<>();
        for(classSubItem d:subItems){
            if (d.item.equals(item)){
                subItemToShow.add(d.subItem);
                subItemsAfterFilering.add(d);
            }
        }
        ArrayAdapter<String>adapter=new ArrayAdapter<>(getApplication(),R.layout.items_row, R.id.item, subItemToShow);
        subList.setAdapter(adapter);
    }

    public void getSubItem2(String item){
        subItemToShow=new ArrayList<>();
        subItemsAfterFilering=new ArrayList<>();
        for(classSubItem d:subItems){
            if (d.Ar_item.equals(item)){
                subItemToShow.add(d.Ar_subItem);
                subItemsAfterFilering.add(d);
            }
        }
        ArrayAdapter<String>adapter=new ArrayAdapter<>(getApplication(),R.layout.items_row, R.id.item, subItemToShow);
        subList.setAdapter(adapter);
    }

    public void getClient(){

        dname.clear();
        cname.clear();
        cmobile.clear();
        caddress.clear();

        db.collection("Res_1_client")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful())
                            for (QueryDocumentSnapshot document : task.getResult())
                                addData(document.getId(), document.get("mobile").toString(), document.get("address").toString());

                    } });

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

    public void AddClient(String name, String mobile, String address){

        Map<String, Object> clie = new HashMap<>();
        clie.put("mobile", mobile);
        clie.put("address", address);

        db.collection("Res_1_client").document(name)
                .set(clie)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (HomeAct.lang==1){
                            Toast.makeText(Emppage.this, "تم تسجيل العميل", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Emppage.this, "The client is registered", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    public void addData(String name, String mobile, String address){

        cname.add(name);
        cmobile.add(mobile);
        caddress.add(address);
        adapterSpin.notifyDataSetChanged();



    }

    public void addData(String name){

        dname.add(name);
        adapterSpin.notifyDataSetChanged();

    }

    public void AddSale2(){

        String bill="";

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        Date dateee = new Date();
        String date = dateFormat.format(dateee);

        String day = date.substring(0, date.indexOf(" "));
        String time = date.substring(date.indexOf(" ")+1);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        bill+="\n"+"نوع الفاتورة : فاتورة كاش نقاط"+",";
        bill+="\n"+",";
        bill+="تاريخ : "+day+"\n"+",";
        bill+="وقت : "+time+"\n"+",";
        bill+="__________________________________________\n\n\n"+",";

        for(int i=0; i<saleList.size(); i++){

            Map<String, Object> sale = new HashMap<>();
            sale.put("date", day);
            sale.put("time", time);
            sale.put("subItem", saleList.get(i).subItemName);
            sale.put("item", itemToShow.get(i));
            sale.put("empEmail", auth.getCurrentUser().getEmail());
            sale.put("sale", saleList.get(i).sumPrice);

            String f, s, t;
            f = "العنصر:"+saleList.get(i).subItemName+" ";
            s = " السعر:"+saleList.get(i).sumPrice+" ";
            t = "X"+saleList.get(i).count+" ";
            bill+= s+t+f+",";

            db.collection("Res_1_point_sales").document()

                    .set(sale)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Emppage.this, "تمت العملية بنجاح", Toast.LENGTH_SHORT).show();
                            recreate();
                        }
                    });

        }

        double taxValue = Double.parseDouble(getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0"));
        bill+="\n\nمجموع الفاتورة : "+(sum-(sum*taxValue*0.01))+"\n"+",";
        bill+= "\n\nقيمة الضريبة : "+" %"+getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0")+"\n"+",";
        bill+="\n\nمجموع كلي : "+sum+"\n"+",";
        bill+="\n\n\n\nاهلا وسهلا زبائننا الكرام\n\n\n"+",";

        PrintUsingServer(bill);

    }

    public void AddSale2Eng(){

        String bill="";

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        Date dateee = new Date();
        String date = dateFormat.format(dateee);

        String day = date.substring(0, date.indexOf(" "));
        String time = date.substring(date.indexOf(" ")+1);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        bill+="\n"+"Welcome To Hybrid Shawarma"+",";
        bill+="\n"+"Bill Type : Cash Bill Points"+",";
        bill+="\n"+",";
        bill+="Date : "+day+"\n"+",";
        bill+="Time : "+time+"\n"+",";
        bill+="__________________________________________\n\n\n"+",";

        for(int i=0; i<saleList.size(); i++){

            Map<String, Object> sale = new HashMap<>();
            sale.put("date", day);
            sale.put("time", time);
            sale.put("subItem", saleList.get(i).subItemName);
            sale.put("item", itemToShow.get(i));
            sale.put("empEmail", auth.getCurrentUser().getEmail());
            sale.put("sale", saleList.get(i).sumPrice);

            bill+="\nItem : "+saleList.get(i).subItemName+" X"+saleList.get(i).count+" Price : "+saleList.get(i).sumPrice+"\n"+",";


            db.collection("Res_1_point_sales").document()

                    .set(sale)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Emppage.this, "Done", Toast.LENGTH_SHORT).show();
                            recreate();
                        }
                    });

        }
        double taxValue = Double.parseDouble(getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0"));
        bill+="\n\nBill value : "+(sum-(sum*taxValue*0.01))+"\n"+",";
        bill+="\n\nTax Value : "+getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0")+" %"+"\n"+",";
        bill+="\n\nTotal Value : "+sum+"\n"+",";
        bill+="\n\nCome Again Soon !\n\n\n"+",";

        PrintUsingServer(bill);

    }

    public void AddSaleAra(double paid, double change, double sum){

        String bill="";

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        Date dateee = new Date();
        String date = dateFormat.format(dateee);

        String day = date.substring(0, date.indexOf(" "));
        String time = date.substring(date.indexOf(" ")+1);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        bill+="\n"+"نوع الفاتورة : فاتورة كاش"+",";
        bill+="\n"+",";
        bill+="تاريخ : "+day+"\n"+",";
        bill+="وقت : "+time+"\n"+",";
        bill+="__________________________________________\n\n\n"+",";

        for(int i=0; i<saleList.size(); i++){

            Map<String, Object> sale = new HashMap<>();
            sale.put("date", day);
            sale.put("time", time);
            sale.put("subItem", saleList.get(i).subItemName);
            sale.put("item", itemToShow.get(i));
            sale.put("empEmail", auth.getCurrentUser().getEmail());
            sale.put("sale", saleList.get(i).sumPrice);

            String f, s, t;
            f = "العنصر:"+saleList.get(i).subItemName+" ";
            s = " السعر:"+saleList.get(i).sumPrice+" ";
            t = "X"+saleList.get(i).count+" ";
            bill+= s+t+f+",";

            db.collection("Res_1_sales").document()

                    .set(sale)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Emppage.this, "تم", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        double taxValue = Double.parseDouble(getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0"));
        bill+="\n\nمجموع الفاتورة : "+(sum-(sum*taxValue*0.01))+"\n"+",";
        bill+= "\n\nقيمة الضريبة : "+" %"+getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0")+"\n"+",";
        bill+="\n\nمجموع كلي : "+sum+"\n"+",";
        bill+="\n\n\n\nاهلا وسهلا زبائننا الكرام\n\n\n"+",";

        PrintUsingServer(bill);

    }

    public void AddVisaSaleAra(double paid, double change, double sum){

        String bill="";

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        Date dateee = new Date();
        String date = dateFormat.format(dateee);

        String day = date.substring(0, date.indexOf(" "));
        String time = date.substring(date.indexOf(" ")+1);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        bill+="\n"+"نوع الفاتورة : فاتورة كاش فيزا"+",";
        bill+="\n"+",";
        bill+="تاريخ : "+day+"\n"+",";
        bill+="وقت : "+time+"\n"+",";
        bill+="__________________________________________\n\n\n"+",";

        for(int i=0; i<saleList.size(); i++){

            Map<String, Object> sale = new HashMap<>();
            sale.put("date", day);
            sale.put("time", time);
            sale.put("description", saleList.get(i).subItemName);
            sale.put("emp", auth.getCurrentUser().getEmail());
            sale.put("pay", saleList.get(i).sumPrice);

            String f, s, t;
            f = "العنصر:"+saleList.get(i).subItemName+" ";
            s = " السعر:"+saleList.get(i).sumPrice+" ";
            t = "X"+saleList.get(i).count+" ";
            bill+= s+t+f+",";

            db.collection("Res_1_visa").document()

                    .set(sale)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Emppage.this, "تم", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        double taxValue = Double.parseDouble(getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0"));
        bill+="\n\nمجموع الفاتورة : "+(sum-(sum*taxValue*0.01))+"\n"+",";
        bill+= "\n\nقيمة الضريبة : "+" %"+getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0")+"\n"+",";
        bill+="\n\n\n\nمدفوع : "+paid+"\n"+",";
        bill+="\n\n\n\nباقي : "+change+"\n"+",";
        bill+="\n\n\n\nاهلا وسهلا زبائننا الكرام\n\n\n"+",";

        PrintUsingServer(bill);

    }

    public void AddVisaSale(double paid, double change, double sum){

        String bill="";

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        Date dateee = new Date();
        String date = dateFormat.format(dateee);

        String day = date.substring(0, date.indexOf(" "));
        String time = date.substring(date.indexOf(" ")+1);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        bill+="\n"+"Bill Type : Cash Bill Visa"+",";
        bill+="\n"+",";
        bill+="Date : "+day+"\n"+",";
        bill+="Time : "+time+"\n"+",";
        bill+="__________________________________________\n\n\n"+",";

        for(int i=0; i<saleList.size(); i++){

            Map<String, Object> sale = new HashMap<>();
            sale.put("date", day);
            sale.put("time", time);
            sale.put("subItem", saleList.get(i).subItemName);
            sale.put("item", itemToShow.get(i));
            sale.put("empEmail", auth.getCurrentUser().getEmail());
            sale.put("sale", saleList.get(i).sumPrice);

            bill+="\nitem : "+saleList.get(i).subItemName+" X"+saleList.get(i).count+" price : "+saleList.get(i).sumPrice+"\n"+",";

            db.collection("Res_1_sales").document()

                    .set(sale)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            recreate();
                        }
                    });
        }

        double taxValue = Double.parseDouble(getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0"));
        bill+="\n\nBill Sum : "+(sum-(sum*taxValue*0.01))+"\n"+",";
        bill+="\n\nTax Value : "+getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0")+" %"+"\n"+",";
        bill+="\n\nBill Total : "+sum+"\n"+",";
        bill+="\n\nPaid Amount : "+paid+"\n"+",";
        bill+="\n\nChange Amount : "+change+"\n"+",";
        bill+="\n\nCome Again Soon !\n\n\n"+",";

        PrintUsingServer(bill);

    }

    public void AddSale(double paid, double change, double sum){

        String bill="";

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        Date dateee = new Date();
        String date = dateFormat.format(dateee);

        String day = date.substring(0, date.indexOf(" "));
        String time = date.substring(date.indexOf(" ")+1);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        bill+="\n"+"Bill Type : Cash Bill"+",";
        bill+="\n"+",";
        bill+="Date : "+day+"\n"+",";
        bill+="Time : "+time+"\n"+",";
        bill+="__________________________________________\n\n\n"+",";

        for(int i=0; i<saleList.size(); i++){

            Map<String, Object> sale = new HashMap<>();
            sale.put("date", day);
            sale.put("time", time);
            sale.put("subItem", saleList.get(i).subItemName);
            sale.put("item", itemToShow.get(i));
            sale.put("empEmail", auth.getCurrentUser().getEmail());
            sale.put("sale", saleList.get(i).sumPrice);

            bill+="\nitem : "+saleList.get(i).subItemName+" X"+saleList.get(i).count+" price : "+saleList.get(i).sumPrice+"\n"+",";

            db.collection("Res_1_sales").document()

                    .set(sale)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Emppage.this, "Done", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        double taxValue = Double.parseDouble(getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0"));
        bill+="\n\nBill Sum : "+(sum-(sum*taxValue*0.01))+"\n"+",";
        bill+="\n\nTax Value : "+getSharedPreferences("Finance", MODE_PRIVATE).getString("tax", "0")+" %"+"\n"+",";
        bill+="\n\nBill Total : "+sum+"\n"+",";
        bill+="\n\nPaid Amount : "+paid+"\n"+",";
        bill+="\n\nChange Amount : "+change+"\n"+",";
        bill+="\n\nCome Again Soon !\n\n\n"+",";

        PrintUsingServer(bill);

    }

    void empLoadInfo(final String e){
        FirebaseFirestore db =FirebaseFirestore.getInstance();
        db.collection("Res_1_employee").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d : list){
                    if (d.toObject(classEmployee.class).email.equals(e)){
                        empObj=d.toObject(classEmployee.class);
                    }
                }
            }
        });
    }
}
