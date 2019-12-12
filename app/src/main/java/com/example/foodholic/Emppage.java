package com.example.foodholic;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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


    public static String[] sites = {
            "1", "2", "3", "4", "5",
            "6", "7", "8","9","10",
            "11","12","13","14","15" };

    ArrayList<String> tabels = new ArrayList<String>();

    public class CustomAdapter extends BaseAdapter {

        String [] result;
        Context context;

        private LayoutInflater inflater=null;

        public CustomAdapter(Emppage table, String[] site) {

            result=sites;
            context=table;

            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); }

        @Override
        public int getCount() { return result.length; }

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
        catch (ParseException e) {}

        if(d2.compareTo(d1) == 0)
            return true;
        else
            return false; }

    String UID="";
    String EMAIL="";
    String PO="";

    final Map<String, Object> tempMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_emp);

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

        try{
            if(getIntent().getStringExtra("empemail").contains(".cap")){
                btn2.setVisibility(View.GONE);
                btn3.setVisibility(View.GONE);
                btn4.setVisibility(View.GONE);
                btn6.setVisibility(View.GONE);
                btn7.setVisibility(View.GONE);
                close.setVisibility(View.GONE);
                re.setVisibility(View.GONE);

                btn5.setWidth(2500); }
        } catch (Exception e){}

        SharedPreferences.Editor editor = shared.edit();
        editor.putString("cash", "");
        editor.apply();

        if(shared2.getString("cash", "").equals("")){
            if (HomeAct.lang==1){

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

                            cash.put("cash", ee.getText().toString());

                            db.collection("Res_1_cash")
                                    .document(""+new Date()).set(cash).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        SharedPreferences.Editor editor = shared2.edit();
                                        editor.putString("cash", ee.getText().toString());
                                        editor.apply();

                                        dialog.dismiss();

                                    }

                                }
                            });

                        }
                        else{
                            dialog.dismiss();
                            Emppage.this.finish();
                        }


                    }
                }).setCancelable(false).show();

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

                final AlertDialog.Builder builder2 = new AlertDialog.Builder(Emppage.this);
                LayoutInflater inflater2 = Emppage.this.getLayoutInflater();
                builder2.setView(inflater2.inflate(R.layout.calulator, null));
                final AlertDialog dialog2 = builder2.create();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                dialog2.show();

                TextView t = dialog2.findViewById(R.id.title);
                String s=String.valueOf(sum);

                if(String.valueOf(sum).length() > 10)
                    t.setText("مجموع الفاتورة : "+s.substring(s.indexOf("."), s.indexOf(".")+3)+" دينار ");
                else
                    t.setText("مجموع الفاتورة : "+sum+" دينار ");

                closeOpenCash.total+=sum;

                final TextView t2 = dialog2.findViewById(R.id.change);

                final EditText e = dialog2.findViewById(R.id.pay);
                e.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(s.toString().equals(""))
                            e.setText("0");
                        else{
                            String temp = String.valueOf((Double.parseDouble(e.getText().toString())-sum));
                            try{
                                if(HomeAct.lang==1){
                                    t2.setText("الباقي : "+temp.substring(0, temp.indexOf(".")+3)+" دينار ");
                                }
                                else {
                                    t2.setText("The rest : "+temp.substring(0, temp.indexOf(".")+3)+"  ");
                                }
                            }
                            catch(Exception ex){
                                if(HomeAct.lang==1){
                                    t2.setText("الباقي : "+temp+" دينار ");
                                }
                                else {
                                    t2.setText("The rest : "+temp+" JOD ");
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
                        String s=String.valueOf(sum*10);
                        t3.setText("مجموع الفاتورة بالنقاط : "+s+" نقطة ");

                        final TextView t2 = dialog3.findViewById(R.id.change);
                        final EditText e = dialog3.findViewById(R.id.pay);
                        e.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if(s.toString().equals(""))
                                    e.setText("0");
                                else{
                                    String temp = String.valueOf((Double.parseDouble(e.getText().toString())-sum*10));
                                    try{
                                        if(HomeAct.lang==1){
                                            t2.setText("الباقي : "+temp.substring(0, temp.indexOf(".")+3)+" نقطة ");
                                        }
                                        else {
                                            t2.setText("The rest : "+temp.substring(0, temp.indexOf(".")+3)+"  ");
                                        }
                                    }
                                    catch(Exception ex){
                                        if(HomeAct.lang==1){
                                            t2.setText("الباقي : "+temp+" نقطة ");
                                        }
                                        else {
                                            t2.setText("The rest : "+temp+" JOD ");
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
                                takePoint(String.valueOf(sum*10));
                                print("مجموع الخصم : "+sum*10+"\n\n\n"+"**********Aldakheel**********\n\n\n");
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

                                    } });

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

                        if((Double.parseDouble(e.getText().toString())-sum)>=0 && !e.getText().toString().equals("")){
                            AddSale();
                            dialog2.dismiss();
                            recreate();
                        }
                        else{
                            if (HomeAct.lang==1){
                                Toast.makeText(Emppage.this, "الرجاء ادخال مبلغ صحيح", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Emppage.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                });

            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(Emppage.this)
                        .setTitle("الرجاء الاختيار")
                        .setMessage("هل ترغب بفتح طلبات التطبيق ام الكاش ؟")
                        .setPositiveButton("تطبيق", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (saleList.isEmpty())
                                    startActivity(new Intent(Emppage.this, Delivery_Emp.class));
                                else{
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
                                                    order.put("lng", "");

                                                    db.collection("Res_1_Delivery")
                                                            .document(sp.getSelectedItem().toString())
                                                            .collection("1")
                                                            .document(name.getText().toString()).set(order)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                        Toast.makeText(Emppage.this, "Your request is added", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });

                                                }
                                            }



                                        }
                                    });
                                }
                            }
                        }).setNegativeButton("كاش", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (saleList.isEmpty())
                            startActivity(new Intent(Emppage.this, Delivery_Emp.class));
                        else{
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
                                            order.put("lng", "");

                                            db.collection("Res_1_Delivery")
                                                    .document(sp.getSelectedItem().toString())
                                                    .collection("1")
                                                    .document(name.getText().toString()).set(order)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful())
                                                                Toast.makeText(Emppage.this, "Your request is added", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                        }
                                    }



                                }
                            });
                        }
                    }
                }).show();


            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(Emppage.this)
                        .setTitle("الرجاء الاختيار")
                        .setMessage("هل ترغب بفتح طلبات التطبيق ام الكاش ؟")
                        .setPositiveButton("تطبيق", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (saleList.isEmpty()){
                                    Intent ax = new Intent(Emppage.this, TakeAway_Emp.class);
                                    ax.putExtra("empemail",getIntent().getStringExtra("empemail"));
                                    startActivity(ax);
                                }

                                else{
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

                                            if (HomeAct.lang==1){

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
                                            }

                                            String temp = "";
                                            double s = 0;

                                            for(int i=0; i<saleList.size(); i++){
                                                temp+=saleList.get(i).subItemName+" = "+saleList.get(i).count+" : "+saleList.get(i).sumPrice+" , ";
                                                s+=saleList.get(i).sumPrice;

                                                Map<String, Object> order = new HashMap<>();
                                                order.put("user_name", name2.getText().toString());
                                                order.put("user_mobile", mobile2.getText().toString());
                                                order.put("user_loc", desc2.getText().toString());
                                                order.put("item_list", MenuSum());
                                                order.put("item_sum_price", sum+"");
                                                order.put("point_sum", "0");
                                                order.put("d_price", "");
                                                order.put("email", "");

                                                db.collection("Res_1_TakeAway").document(new Date().toString()).set(order)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                    Toast.makeText(Emppage.this, "Your request is added", Toast.LENGTH_SHORT).show();

                                                            }
                                                        });

                                            }
                                        }
                                    });
                                }
                            }
                        }).setNegativeButton("كاش", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (saleList.isEmpty()){
                            Intent ax = new Intent(Emppage.this, TakeAway_Emp.class);
                            ax.putExtra("empemail",getIntent().getStringExtra("empemail"));
                            startActivity(ax);
                        }

                        else{
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

                                    if (HomeAct.lang==1){

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
                                    }

                                    String temp = "";
                                    double s = 0;

                                    for(int i=0; i<saleList.size(); i++){
                                        temp+=saleList.get(i).subItemName+" = "+saleList.get(i).count+" : "+saleList.get(i).sumPrice+" , ";
                                        s+=saleList.get(i).sumPrice;

                                        Map<String, Object> order = new HashMap<>();
                                        order.put("user_name", name2.getText().toString());
                                        order.put("user_mobile", mobile2.getText().toString());
                                        order.put("user_loc", desc2.getText().toString());
                                        order.put("item_list", MenuSum());
                                        order.put("item_sum_price", sum+"");
                                        order.put("point_sum", "0");
                                        order.put("d_price", "");
                                        order.put("email", "");

                                        db.collection("Res_1_TakeAway").document(new Date().toString()).set(order)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                            Toast.makeText(Emppage.this, "Your request is added", Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    }
                }).show();

            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (saleList.isEmpty())
                    startActivity(new Intent(Emppage.this, Tabel_Res_Emp.class));
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
                    intent.putExtra("orderSum", s+"");
                    startActivity(intent);
                }

            }
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

                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Double nu = ((closeOpenCash.total+closeOpenCash.floor)-Double.parseDouble(shared3.getString("pay", "0.0")));

                            et1.setText(closeOpenCash.empEmail);
                            et2.setText(closeOpenCash.dateAndTimeOpen);
                            et5.setText("مبيعات : "+closeOpenCash.total+"   ارضية : "+closeOpenCash.floor+
                                    "   مصروفات : "+ shared3.getString("pay", "0.0")+"\n");
                            et5.setText(et5.getText().toString()+"   مجموع : "+nu);

                            closeOpenCash.total = nu;

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

                            db.collection("closeOpenCash").document().set(closeOpenCash)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                                auth.signOut();

                                                Toast.makeText(Emppage.this, "تسجيل خروج بنجاح", Toast.LENGTH_SHORT).show();

                                                SharedPreferences.Editor editor = shared2.edit();
                                                editor.putString("cash", "");
                                                editor.apply();

                                                editor = shared3.edit();
                                                editor.putString("pay", "0.0");
                                                editor.apply();

                                                closeOpenCash = new classCloseOpenCash();

                                                dialog.dismiss();
                                                Emppage.this.finish();
                                            }
                                            else
                                                Toast.makeText(Emppage.this, "تعذر التخزين !", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                        }
                    });
                }
                else {
                    new AlertDialog.Builder(Emppage.this)
                            .setTitle("Please close the cache")
                            .setView(ee).setCancelable(false)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Emppage.this.finish(); }})
                            .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {

                                    if(!ee.getText().toString().equals("")){

                                        cash.put("cash", ee.getText().toString());

                                        db.collection("Res_1_cash")
                                                .document(""+new Date()).set(cash).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){

                                                    int prev = Integer.parseInt(shared2.getString("cash", "0"));
                                                    int curr = Integer.parseInt(ee.getText().toString());

                                                    if(curr >= prev){

                                                        FirebaseAuth auth = FirebaseAuth.getInstance();
                                                        auth.signOut();

                                                        Toast.makeText(Emppage.this, "Your logout is successful", Toast.LENGTH_SHORT).show();


                                                        dialog.dismiss();
                                                        Emppage.this.finish();

                                                    }
                                                    else{
                                                        Toast.makeText(Emppage.this, "Existing values do not match sales, please check your cache again", Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss(); }
                                                } } }); }
                                    else
                                        dialog.dismiss();

                                } }).show();
                }
            } });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Emppage.this, cashAddItem.class));

            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n=new Intent(getApplicationContext(), Payment.class);
                n.putExtra("emp",empEmail);
                startActivity(n);
            }
        });

        loadItem();
        loadSubItem();

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getSubItem(items.get(position).itemName);
            }
        });

        subList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sale=new classCashSale();

                boolean cheack=false;
                int loopPosition=0;

                if (saleList.isEmpty()){
                    sale.count=1;
                    sale.subItemName=subItemsAfterFilering.get(position).subItem;
                    sale.sumPrice=subItemsAfterFilering.get(position).price;
                    sale.unitPrice=subItemsAfterFilering.get(position).price;
                    saleList.add(sale);

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
                        saleList.add(sale);
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

                        if (HomeAct.lang==1){
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
                                    TextView s = findViewById(R.id.tot);

                                    if(String.valueOf(sum).length() > 10)
                                        s.setText("مجموع الفاتورة : "+t.substring(t.indexOf("."), t.indexOf(".")+3)+" دينار ");
                                    else
                                        s.setText("مجموع الفاتورة : "+sum+" دينار ");

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
                                    TextView s = findViewById(R.id.tot);

                                    if(String.valueOf(sum).length() > 10)
                                        s.setText("مجموع الفاتورة : "+t.substring(t.indexOf("."), t.indexOf(".")+3)+" دينار ");
                                    else
                                        s.setText("مجموع الفاتورة : "+sum+" دينار ");

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
                                    TextView s = findViewById(R.id.tot);

                                    if(String.valueOf(sum).length() > 10)
                                        s.setText("Total invoice : "+t.substring(t.indexOf("."), t.indexOf(".")+3)+" JOD ");
                                    else
                                        s.setText("Total invoice : "+sum+" JOD ");

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
                                    TextView s = findViewById(R.id.tot);

                                    if(String.valueOf(sum).length() > 10)
                                        s.setText("Total invoice : "+t.substring(t.indexOf("."), t.indexOf(".")+3)+" JOD ");
                                    else
                                        s.setText("Total invoice : "+sum+" JOD ");
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
                TextView s = findViewById(R.id.tot);

                if(String.valueOf(sum).length() > 10){
                    if (HomeAct.lang==1){
                        s.setText("مجموع الفاتورة : "+t.substring(t.indexOf("."), t.indexOf(".")+3)+" دينار ");
                    }
                    else {
                        s.setText("Total invoice : "+t.substring(t.indexOf("."), t.indexOf(".")+3)+" JOD ");
                    }
                }
                else{
                    if (HomeAct.lang==1){
                        s.setText("مجموع الفاتورة : "+sum+" دينار ");
                    }
                    else {
                        s.setText("Total invoice : "+sum+" JOD ");
                    }
                }
            }
        });

        FillReserve();

        //Print();
    }

    private void Print() {

        try {
            Socket socket = new Socket("127.0.0.1", 5554);
            OutputStream out = socket.getOutputStream();
            out.write("hi my name is ahmad".getBytes());
        } catch(Exception e){}

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



        db.collection("Res_1_Table_Res_").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

        db = FirebaseFirestore.getInstance();

        tabels.add("Table Number : 1"); tabels.add("Table Number : 2"); tabels.add("Table Number : 3");
        tabels.add("Table Number : 4"); tabels.add("Table Number : 5"); tabels.add("Table Number : 6");
        tabels.add("Table Number : 7"); tabels.add("Table Number : 8"); tabels.add("Table Number : 9");
        tabels.add("Table Number : 10"); tabels.add("Table Number : 11"); tabels.add("Table Number : 12");
        tabels.add("Table Number : 13"); tabels.add("Table Number : 14"); tabels.add("Table Number : 15");

        gridview = (GridView) findViewById(R.id.customgrid);

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
                        Toast.makeText(Emppage.this, "تمت العملية بنجاح", Toast.LENGTH_SHORT).show();
                        recreate();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(++Count == 2)
                super.onBackPressed();
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

            if(ee.getParent()!=null)
                ((ViewGroup)ee.getParent()).removeView(ee);

            ee.setText("");
            if(HomeAct.lang==1){
                new AlertDialog.Builder(Emppage.this)
                        .setTitle("الرجاء إغلاق درج الكاش")
                        .setView(ee).setCancelable(false)
                        .setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Emppage.this.finish(); }})
                        .setPositiveButton("ادخال", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {

                                if(!ee.getText().toString().equals("")){

                                    cash.put("cash", ee.getText().toString());

                                    db.collection("Res_1_cash")
                                            .document(""+new Date()).set(cash).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                int prev = Integer.parseInt(shared2.getString("cash", "0"));
                                                int curr = Integer.parseInt(ee.getText().toString());

                                                if(curr >= prev){

                                                    SharedPreferences.Editor editor = shared2.edit();
                                                    editor.putString("cash", "");
                                                    editor.apply();

                                                    dialog.dismiss();
                                                    Emppage.this.finish();

                                                }
                                                else{
                                                    Toast.makeText(Emppage.this, "القيم الموجودة غير مطابقة للمبيعات, الرجاء التأكد من الكاش مرة أخرى", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss(); }
                                            } } }); }
                                else
                                    dialog.dismiss();

                            } }).show();

                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();

                Toast.makeText(this, "تسجيل خروج بنجاح", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Emppage.this, Login.class);
                startActivity(intent);
                finish();
            }
            else {
                new AlertDialog.Builder(Emppage.this)
                        .setTitle("Please close the cash drawer")
                        .setView(ee).setCancelable(false)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Emppage.this.finish(); }})
                        .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {

                                if(!ee.getText().toString().equals("")){

                                    cash.put("cash", ee.getText().toString());

                                    db.collection("Res_1_cash")
                                            .document(""+new Date()).set(cash).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                int prev = Integer.parseInt(shared2.getString("cash", "0"));
                                                int curr = Integer.parseInt(ee.getText().toString());

                                                if(curr >= prev){

                                                    SharedPreferences.Editor editor = shared2.edit();
                                                    editor.putString("cash", "");
                                                    editor.apply();

                                                    dialog.dismiss();
                                                    Emppage.this.finish();

                                                }
                                                else{
                                                    Toast.makeText(Emppage.this, "Existing values do not match sales, please check your cache again", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss(); }
                                            } } }); }
                                else
                                    dialog.dismiss();

                            } }).show();

                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();

                Toast.makeText(this, "Your logout is successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Emppage.this, Login.class);
                startActivity(intent);
                finish();
            }

        }

        else if (id == R.id.offer){

            startActivity(new Intent(Emppage.this, offer.class));

        }

        else if(id == R.id.noti){

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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_menu:break;
            case R.id.nav_table: startActivity(new Intent(this, Tabel_Res_Emp.class)); break;
            case R.id.nav_online: startActivity(new Intent(this, Online.class)); break;
            case R.id.nav_take: startActivity(new Intent(this, TakeAway_Emp.class)); break;
            case R.id.nav_delivery: startActivity(new Intent(this, Delivery_Emp.class)); break;
            case R.id.nav_add: startActivity(new Intent(this, Add_Emp.class)); break;
            case R.id.nav_pay:
                Intent n =new Intent(getApplicationContext(),Payment.class);
                n.putExtra("emp",empEmail);
                startActivity(n);
                break;
            case R.id.nav_contact: startActivity(new Intent(this, Contact.class)); break;
            case R.id.nav_about: startActivity(new Intent(this, About.class)); break; }

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

        db.collection("Res_1_driver")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful())
                            for (QueryDocumentSnapshot document : task.getResult())
                                addData(document.getId());

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

    public void AddSale(){

        String bill="";
        int sum=0;

        for(int i=0; i<saleList.size(); i++){

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
            Date dateee = new Date();
            String date = dateFormat.format(dateee);

            String day = date.substring(0, date.indexOf(" "));
            String time = date.substring(date.indexOf(" ")+1);

            Map<String, Object> sale = new HashMap<>();
            sale.put("date", day);
            sale.put("time", time);
            sale.put("subItem", saleList.get(i).subItemName);
            sale.put("item", itemToShow.get(i));
            sale.put("empEmail", getIntent().getStringExtra("empemail"));
            sale.put("sale", saleList.get(i).sumPrice);

            sum+=saleList.get(i).sumPrice;
            bill+="date: "+day+"\n" +
                    "time: "+time + "\n" +
                    "sub item: "+saleList.get(i).subItemName+"\n" +
                    "sum price: "+ saleList.get(i).sumPrice+"\n" +
                    "-------------------------------------------\n" ;

            db.collection("Res_1_sales").document()
                    
                    .set(sale)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            recreate();
                        }
                    });

        }
        bill+="total: "+sum+"\n\n\n"+"**********Aldakheel**********\n\n\n";
        print(bill);


    }
    void print(String ss ){
        Toast.makeText(this, ss, Toast.LENGTH_LONG).show();
    }
}
