package com.example.foodholic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adminpage extends AppCompatActivity {

    public class CustomGridViewAdapter extends BaseAdapter {

        public Integer[] mThumbIds = {
                R.drawable.ic_person,
                R.drawable.ic_storage,
                R.drawable.ic_report,
                R.drawable.ic_pay,
                R.drawable.ic_cash,
                R.drawable.ic_z,
                R.drawable.ic_drive,
                R.drawable.ic_tab,
                R.drawable.ic_star,
                R.drawable.ic_point,
                R.drawable.ic_visa,
                R.drawable.ic_vip,
                R.drawable.ic_suppliers,
                R.drawable.ic_job,
                R.drawable.ic_noti,
                R.drawable.ic_z,
                R.drawable.ic_support,
                R.drawable.ic_adb
        };

        //public String[] mThumbNames = {"Human Resources", "Storage", "Reports", "Payments", "Cash Drawer", "Taxes", "Delivery Drivers", "Ratings" };
        public String[] mThumbNames = {"Human Resources","Storage" ,"Reports", "Payments","Cash Drawer",  "Taxes" ,"Delivery Drivers" , "Tables Management" , "Ratings", "Points", "VISA", "VIP", "Suppliers", "Job Req.","Alerts" , "tax change", "Contact Us", "Config" };
        //public String[] mThumbNames2 = {"تقارير جودة","سائقين التوصيل", "ضرائب", "صندوق الكاش","مصروفات", "تقارير", "مستودعات", "شؤون موظفين" };
        public String[] mThumbNames2 = {"شؤون موظفين","مستودعات", "تقارير", "مصروفات","صندوق الكاش", "ضرائب", "سائقين التوصيل", "تنظيم الصالة","تقارير جودة", "نقاط", "فيزا", "أهم العملاء", "موردين", "طلبات توظيف", "تنبيهات","تعديل الضريبه", "تواصل معنا" , "ضبط"};

        private Context mContext;

        public CustomGridViewAdapter(Context c) {
            mContext = c;
        }

        @Override
        public int getCount() {
            return mThumbIds.length;
        }

        @Override
        public Object getItem(int position) {
            return mThumbIds[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolderItem viewHolder;

            if (convertView == null) {

                LayoutInflater inflater = (Adminpage.this).getLayoutInflater();
                convertView = inflater.inflate(R.layout.row_grid, parent, false);

                viewHolder = new ViewHolderItem();
                viewHolder.textViewItem = convertView.findViewById(R.id.textView);
                viewHolder.imageViewItem = convertView.findViewById(R.id.imageView);

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        switch (position){

                            case 0:
                                Intent n=new Intent(getApplicationContext(),employeeCreatInformation.class);
                                startActivity(n);
                                break;
                            case 1:
                                Intent n1 =new Intent(getApplicationContext(),warehouse.class);
                                startActivity(n1);
                                break;
                            case 2:
                                Intent n2=new Intent(getApplicationContext(),statistics.class);
                                startActivity(n2);
                                break;
                            case 3:
                                Intent n3 =new Intent(getApplicationContext(),Payment.class);
                                n3.putExtra("emp",email);
                                startActivity(n3);
                                break;
                            case 4:
                                Intent n4=new Intent(getApplicationContext(),statisticsMainActivity.class);
                                startActivity(n4);
                                break;
                            case 5:
                                Intent n5=new Intent(getApplicationContext(),Zreport.class);
                                startActivity(n5);
                                break;
                            case 6:
                                Intent n7=new Intent(getApplicationContext(),driverReport.class);
                                startActivity(n7);
                                break;
                            case 7:
                                final EditText edt = new EditText(Adminpage.this);
                                if(shared2.getString("language", "").equals("arabic")) {
                                    edt.setHint("أدخل عدد الطاولات");
                                    new AlertDialog.Builder(Adminpage.this)
                                            .setTitle("تنظيم الصالة")
                                            .setView(edt)
                                            .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                            .setPositiveButton("إدخال", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    uploadTabelNum(edt.getText().toString());
                                                    dialogInterface.dismiss();
                                                }
                                            }).show();
                                }else{
                                        edt.setHint("Enter number of table");
                                        new AlertDialog.Builder(Adminpage.this)
                                                .setTitle("hall organization")
                                                .setView(edt)
                                                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                })
                                                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        uploadTabelNum(edt.getText().toString());
                                                        dialogInterface.dismiss();
                                                    }
                                                }).show();

                                }
                                break;
                            case 8:
                                Intent n6=new Intent(getApplicationContext(),ratingSystem.class);
                                startActivity(n6);
                                break;
                            case 9:
                                Intent n8=new Intent(getApplicationContext(),PointReport.class);
                                startActivity(n8);
                                break;
                            case 10:
                                Intent n9=new Intent(getApplicationContext(),VisaReport.class);
                                startActivity(n9);
                                break;
                            case 11:
                                Intent n10=new Intent(getApplicationContext(),vipActivity.class);
                                startActivity(n10);
                                break;
                            case 12:
                                Intent n11=new Intent(getApplicationContext(),suppliers.class);
                                startActivity(n11);
                                break;
                            case 13:
                                Intent n13=new Intent(getApplicationContext(),Job_Apps.class);
                                startActivity(n13);
                                break;
                            case 14:
                                Intent n14 =new Intent(getApplicationContext(),notificationActivity.class);
                                startActivity(n14);
                                break;
                            case 15:
                                Intent n15=new Intent(getApplicationContext(),taxChange.class);
                                startActivity(n15);
                                break;
                            case 16:
                                Intent n16=new Intent(getApplicationContext(),Contact.class);
                                startActivity(n16);
                                break;
                            case 17:
                                final EditText e = new EditText(Adminpage.this);
                                e.setHint("!!! كلمة السر !!!");
                                new AlertDialog.Builder(Adminpage.this)
                                        .setTitle("يرجى عدم العبث بهذا الخيار")
                                        .setView(e)
                                        .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .setPositiveButton("إدخال", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                if(e.getText().toString().equals("Animeotaku691996!!!")){
                                                    final EditText ed = new EditText(Adminpage.this);
                                                    ed.setHint("IP");
                                                    new AlertDialog.Builder(Adminpage.this)
                                                            .setTitle("Admin @OverRide")
                                                            .setView(ed)
                                                            .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    dialogInterface.dismiss();
                                                                }
                                                            })
                                                            .setPositiveButton("إدخال", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                                    SharedPreferences shasha = getSharedPreferences("IPS", MODE_PRIVATE);
                                                                    SharedPreferences.Editor editor = shasha.edit();

                                                                    String [] ipp = ed.getText().toString().split(",");

                                                                    editor.putString("ip", ipp[0]);
                                                                    editor.putInt("port", Integer.parseInt(ipp[1]));

                                                                    editor.apply();

                                                                    dialogInterface.dismiss();
                                                                }
                                                            }).show();
                                                }

                                            }
                                        }).show();
                                break;
                        }

                    }
                });

                convertView.setTag(viewHolder); }

            else
                viewHolder = (ViewHolderItem) convertView.getTag();

            if(shared2.getString("language", "").equals("arabic"))
                viewHolder.textViewItem.setText(mThumbNames2[position]);
            else
                viewHolder.textViewItem.setText(mThumbNames[position]);

            viewHolder.textViewItem.setTag(position);

            viewHolder.imageViewItem.setImageResource(mThumbIds[position]);
            viewHolder.imageViewItem.setBackground(getResources().getDrawable(R.drawable.shape5));


            return convertView; }

    }

    private void uploadTabelNum(String toString) {

        try{

            int x = Integer.parseInt(toString);

            FirebaseFirestore fb = FirebaseFirestore.getInstance();
            HashMap<String, Object> map = new HashMap<>();
            map.put("count", toString);
            fb.collection("Res_1_Table_Count")
                    .document("TC").set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                                Toast.makeText(Adminpage.this, "تمت الاضافة بنجاح", Toast.LENGTH_LONG).show();
                        }
                    });

        } catch (Exception ex){
            Toast.makeText(this, "الرجاء ادخال ارقام انجليزية صحيحة", Toast.LENGTH_LONG).show();
        }
    }

    static class ViewHolderItem {
        TextView textViewItem;
        ImageView imageViewItem; }

    FirebaseFirestore db ;
    FirebaseFirestore dbs;

    public static ArrayList<classSales> cSale=new ArrayList<>();
    public static ArrayList<classPayment> cPyment=new ArrayList<>();
    public static classCurrencyAndTax currencyAndTax=new classCurrencyAndTax();
    int noteCount=0;
    boolean a=false,b=false;
    ArrayAdapter<String> adapter;

    TextView textView;
    SharedPreferences shared2;

   public static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_adminpage_new);
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);

        textView = findViewById(R.id.textView6);

        if(shared2.getString("language", "").equals("arabic"))
            textView.setText("صفحة المدير");

        GridView gridView = findViewById(R.id.gridViewCustom);
        gridView.setAdapter(new CustomGridViewAdapter(this));

        db = FirebaseFirestore.getInstance();

        db.collection("Res_1_payment").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                cPyment=new ArrayList<>();
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                classPayment a=new classPayment();
                for(DocumentSnapshot d:list){
                    a=d.toObject(classPayment.class);
                   try{
                       if(a.emp.equals(null)){
                           a.emp="";
                       }
                   }catch (Exception e){
                       a.emp="";
                   }
                    cPyment.add(a);
                }

            }

        });

        dbs = FirebaseFirestore.getInstance();
        dbs.collection("Res_1_sales").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                cSale=new ArrayList<>() ;
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                classSales a =new classSales();
                for(DocumentSnapshot d:list){
                    try{
                        a=d.toObject(classSales.class);
                        if(a.empEmail.equals(null)){
                            a.empEmail="";
                        }
                        cSale.add(d.toObject(classSales.class));
                    }
                    catch(Exception e){}
                }
            }
        });

        db=FirebaseFirestore.getInstance();
        db.collection("Res_1_currencyAndTax").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list =queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d : list){
                    currencyAndTax=d.toObject(classCurrencyAndTax.class);
                }
            }
        });


    }


}
