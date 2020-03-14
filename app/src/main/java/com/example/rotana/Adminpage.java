package com.example.rotana;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                R.drawable.aaareport,
                R.drawable.ic_job,
                R.drawable.ic_noti,
                R.drawable.ic_z,
                R.drawable.ic_support,
                R.drawable.ic_adb,
                R.drawable.ic_fingerprint_black_24dp,
                R.drawable.ic_exit
        };

        //public String[] mThumbNames = {"Human Resources", "Storage", "Reports", "Payments", "Cash Drawer", "Taxes", "Delivery Drivers", "Ratings" };
        public String[] mThumbNames = {"Human Resources","Storage" ,"Reports", "Payments","Cash Drawer",  "Taxes" ,"Delivery Drivers" , "Tables Management" , "Ratings", "Points", "VISA", "VIP", "Suppliers","purchases report", "Job Req.","Alerts" , "tax change", "Contact Us", "Config", "Finger Print", "Exit" };
        //public String[] mThumbNames2 = {"تقارير جودة","سائقين التوصيل", "ضرائب", "صندوق الكاش","مصروفات", "تقارير", "مستودعات", "شؤون موظفين" };
        public String[] mThumbNames2 = {"شؤون موظفين","مستودعات", "تقارير", "مصروفات","صندوق الكاش", "ضرائب", "سائقين التوصيل", "تنظيم الصالة","تقارير جودة", "نقاط", "فيزا", "أهم العملاء", "موردين","تقارير المشتريات", "طلبات توظيف", "تنبيهات","تعديل الضريبه", "تواصل معنا" ,"ضبط", "تفعيل بصمة", "خروج"};

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
        public View getView(final int position, View convertView, final ViewGroup parent) {

            ViewHolderItem viewHolder;



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
                            Intent n13=new Intent(getApplicationContext(),pillsPurchases.class);
                            startActivity(n13);
                            break;
                        case 14:
                            Intent n143=new Intent(getApplicationContext(),Job_Apps.class);
                            startActivity(n143);
                            break;
                        case 15:
                            Intent n154 =new Intent(getApplicationContext(),notificationActivity.class);
                            startActivity(n154);
                            break;
                        case 16:
                            Intent n165=new Intent(getApplicationContext(),taxChange.class);
                            startActivity(n165);
                            break;
                        case 17:
                            Intent n176=new Intent(getApplicationContext(),Contact.class);
                            startActivity(n176);
                            break;
                        case 18:
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
                        case 19 :

                            FingerprintIdentify mFingerprintIdentify = new FingerprintIdentify(Adminpage.this);
                            mFingerprintIdentify.setSupportAndroidL(true);
                            mFingerprintIdentify.init();

                            if(!mFingerprintIdentify.isFingerprintEnable() ||
                                    !mFingerprintIdentify.isHardwareEnable()){

                                if(HomeAct.lang == 1){

                                    new AlertDialog.Builder(Adminpage.this)
                                            .setTitle("عذرا")
                                            .setMessage("جهازك لا يدعم البصمة")
                                            .setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();

                                }
                                else{

                                    new AlertDialog.Builder(Adminpage.this)
                                            .setTitle("Sorry !")
                                            .setMessage("Your Device Does not support FingerPrint")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();

                                }

                            }

                            else{

                                final AlertDialog AD = ReturnAlert(parent);

                                mFingerprintIdentify.startIdentify(5, new BaseFingerprint.IdentifyListener() {
                                    @Override
                                    public void onSucceed() {
                                        AD.dismiss();
                                        if(HomeAct.lang == 1)
                                            Toast.makeText(Adminpage.this, "تم تفعيل البصمة بنجاح", Toast.LENGTH_LONG).show();
                                        else
                                            Toast.makeText(Adminpage.this, "Finger Print Activated", Toast.LENGTH_LONG).show();

                                        SharedPreferences sha = getSharedPreferences("lang", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sha.edit();
                                        editor.putString("finem", getIntent().getStringExtra("empemail"));
                                        editor.putString("finpa", getIntent().getStringExtra("emppass"));
                                        editor.apply();

                                    }

                                    @Override
                                    public void onNotMatch(int availableTimes) {
                                        AD.dismiss();
                                        if(HomeAct.lang == 1)
                                            Toast.makeText(Adminpage.this, "هذه البصمة غير معروفة", Toast.LENGTH_LONG).show();
                                        else
                                            Toast.makeText(Adminpage.this, "Cannot Recognize This Finger Print", Toast.LENGTH_LONG).show();



                                    }

                                    @Override
                                    public void onFailed(boolean isDeviceLocked) {

                                    }

                                    @Override
                                    public void onStartFailedByDeviceLocked() {

                                    }
                                });

                            }


                            break;

                        case 20 : finishAffinity(); System.exit(0); break;
                    }

                }
            });

            if(shared2.getString("language", "").equals("arabic"))
                viewHolder.textViewItem.setText(mThumbNames2[position]);
            else
                viewHolder.textViewItem.setText(mThumbNames[position]);

            viewHolder.textViewItem.setTag(position);

            viewHolder.imageViewItem.setImageResource(mThumbIds[position]);
            viewHolder.imageViewItem.setBackground(getResources().getDrawable(R.drawable.shape5));


            return convertView; }

    }

    private AlertDialog ReturnAlert(ViewGroup parent) {

        AlertDialog Alert=null;

        if(HomeAct.lang == 1)
            Alert = new AlertDialog.Builder(Adminpage.this)
                    .setTitle("ألرجاء لمس ماسح البصمة")
                    .setView(getLayoutInflater().inflate(R.layout.finger_print, parent, false)).show();
        else
            Alert = new AlertDialog.Builder(Adminpage.this)
                    .setTitle("Touch Finger Print Sensor")
                    .setView(getLayoutInflater().inflate(R.layout.finger_print, parent, false)).show();

        return Alert;
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
