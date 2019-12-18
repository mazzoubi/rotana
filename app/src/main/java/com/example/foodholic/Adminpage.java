package com.example.foodholic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
                R.drawable.ic_job,
                R.drawable.ic_support};

        //public String[] mThumbNames = {"Human Resources", "Storage", "Reports", "Payments", "Cash Drawer", "Taxes", "Delivery Drivers", "Ratings" };
        public String[] mThumbNames = {"Human Resources","Storage" ,"Reports", "Payments","Cash Drawer",  "Taxes" ,"Delivery Drivers" , "Tables Management" , "Ratings", "Points", "VISA", "VIP", "Suppliers", "Job Req.", "Contact Us" };
        //public String[] mThumbNames2 = {"تقارير جودة","سائقين التوصيل", "ضرائب", "صندوق الكاش","مصروفات", "تقارير", "مستودعات", "شؤون موظفين" };
        public String[] mThumbNames2 = {"شؤون موظفين","مستودعات", "تقارير", "مصروفات","صندوق الكاش", "ضرائب", "سائقين التوصيل", "تنظيم الصالة","تقارير جودة", "نقاط", "فيزا", "أهم العملاء", "موردين", "طلبات توظيف", "تواصل معنا" };

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
                                Toast.makeText(getApplicationContext(), "قريبا !!!", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getApplicationContext(), "قريبا !!!", Toast.LENGTH_SHORT).show();
                                break;
                            case 12:
                                Toast.makeText(getApplicationContext(), "قريبا !!!", Toast.LENGTH_SHORT).show();
                                break;
                            case 13:
                                Toast.makeText(getApplicationContext(), "قريبا !!!", Toast.LENGTH_SHORT).show();
                                break;
                            case 14:
                                Toast.makeText(getApplicationContext(), "قريبا !!!", Toast.LENGTH_SHORT).show();
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


            return convertView; }

    }

    static class ViewHolderItem {
        TextView textViewItem;
        ImageView imageViewItem; }

    FirebaseFirestore db ;
    FirebaseFirestore dbs;

    public static ArrayList<classSales> cSale=new ArrayList<>();
    public static ArrayList<classPayment> cPyment=new ArrayList<>();

    boolean a=false,b=false;
    ArrayAdapter<String> adapter;

    TextView textView;
    SharedPreferences shared2;

   public static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage_new);

        final ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressBar);
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
                progressBar.setVisibility(View.GONE);

                cPyment=new ArrayList<>();
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    cPyment.add(d.toObject(classPayment.class));
                }

            }

        });

        dbs = FirebaseFirestore.getInstance();
        dbs.collection("Res_1_sales").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                progressBar.setVisibility(View.GONE);
                cSale=new ArrayList<>() ;
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    try{cSale.add(d.toObject(classSales.class)); }
                    catch(Exception e){}
                }
            }
        });




    }

}
