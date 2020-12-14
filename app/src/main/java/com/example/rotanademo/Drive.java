package com.example.rotanademo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Drive extends AppCompatActivity {

    FirebaseFirestore db;

    ListView list;
    ArrayAdapter<String> adapter;

    ArrayList<String> id;
    ArrayList<String> loc;
    ArrayList<String> info;
    ArrayList<String>latlng;

    Toolbar toolbar;
    TextView t1,t2;
    String name="";
    String p="";
    int lang=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drive);
        Space(savedInstanceState);
        t1=findViewById(R.id.driverpro);
        t2=findViewById(R.id.thanks);
        toolbar=findViewById(R.id.tool);
        SharedPreferences shared2;
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        if(shared2.getString("language", "").equals("arabic")) {
            lang=1;

        }
        else {
            t1.setText("dear driver");
            t2.setText("thanks for your efforts");
            toolbar.setTitle("Aldakhil Corporation delivery");
        }
        db = FirebaseFirestore.getInstance();

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);

        list=findViewById(R.id.list);

        id = new ArrayList<String>();
        loc = new ArrayList<String>();
        info = new ArrayList<String>();
        latlng = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, R.layout.items_row3, R.id.item, info);
        list.setAdapter(adapter);

        downloadDriverData();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                getLngLat();

                if (lang==1){
                    new AlertDialog.Builder(Drive.this)
                            .setMessage("هل ترغب بالأتصال أم الذهاب للموقع؟")
                            .setNegativeButton("عرض موقع الزبون", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                            Uri.parse("http://maps.google.com/maps?daddr="+latlng.get(i)));
                                    startActivity(intent);
                                }
                            }).setPositiveButton("إتصال", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String str = info.get(i);
                            String num = str.substring(str.indexOf("الهاتف : ")+9, str.indexOf("قائمة : "));
                            num = RemoveSpace(num);
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", num, null)));
                        }
                    }).setNeutralButton("عرض موقع المطعم", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("http://maps.google.com/maps?daddr=32.532917,35.869199"));
                            startActivity(intent);

                        }
                    }).show();

                }
                else {
                    new AlertDialog.Builder(Drive.this)
                            .setMessage("Want to connect?")
                            .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                            Uri.parse("http://maps.google.com/maps?daddr="+latlng.get(i)));
                                    startActivity(intent);
                                }
                            }).setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String str = info.get(i);
                            String num = str.substring(str.indexOf("phone : ")+9, str.indexOf("Menu : "));
                            num = RemoveSpace(num);
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", num, null)));
                        }
                    }).show();

                }

            }
        });

    }

    private void getNoti() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Res_1_Delivery").document(name).collection("1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            showNotification();
                        }
                    } });
    }

    private void showNotification() {

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        SharedPreferences shared2;
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        if(shared2.getString("language", "").equals("arabic")) {
            lang=1;
        }
        if (lang==1){
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("إشعار")
                    .setContentText("لديك طلبية جديدة, تفقد البرنامج");


            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addNextIntent(new Intent(this, Main2Activity.class));
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            mBuilder.setContentIntent(resultPendingIntent);

            notificationManager.notify(notificationId, mBuilder.build());
        }
        else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("notification")
                    .setContentText("you have new order, see your app");


            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addNextIntent(new Intent(this, Main2Activity.class));
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            mBuilder.setContentIntent(resultPendingIntent);

            notificationManager.notify(notificationId, mBuilder.build());
        }

    }

    private void Space(Bundle savedInstanceState) {

        SpaceNavigationView spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_exit));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_support));
        spaceNavigationView.setSpaceItemIconSize(100);

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {

                final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Drive.this);
                LayoutInflater inflater = Drive.this.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.dialog_deliver3, null));
                final androidx.appcompat.app.AlertDialog dialog = builder.create();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;



                dialog.show();
                dialog.getWindow().setAttributes(lp);
                TextView t1 = dialog.findViewById(R.id.dnum);
                TextView t2 = dialog.findViewById(R.id.dprice);
                TextView t3 = dialog.findViewById(R.id.dsum);
                if(lang==1){
                    t1.setText("عدد الطلبيات : "+info.size()+" طلب");

                    t2.setText("مجموع مبلغ الوجبات : "+getRecieteSum()+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"));

                    t3.setText("مجموع مبلغ التوصيل : "+getpriceSum()+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " دينار"));
                }
                else {
                    t1.setText("orders count : "+info.size()+" order");

                    t2.setText("sum of meals amount : "+getRecieteSum()+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD"));

                    t3.setText("sum of delivery amount : "+getpriceSum()+getSharedPreferences("Finance", MODE_PRIVATE).getString("cur", " JOD"));
                }



            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch (itemIndex){

                    case 0:
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.signOut();

                        UploadDriverData();

                        startActivity(new Intent(Drive.this, Login.class));
                        finish();

                        break;

                    case 1:
                        if (lang==1){
                            new androidx.appcompat.app.AlertDialog.Builder(Drive.this)
                                    .setMessage("هل تود الأتصال على رقم المطعم ؟")
                                    .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent((Intent.ACTION_DIAL));
                                            intent.setData(Uri.parse("tel:+962792942040"));
                                            startActivity(intent);
                                        }
                                    }).setNegativeButton("لا", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                        }
                        else {
                            new androidx.appcompat.app.AlertDialog.Builder(Drive.this)
                                    .setMessage("do you want to call the restaurant?")
                                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent((Intent.ACTION_DIAL));
                                            intent.setData(Uri.parse("tel:+962792942040"));
                                            startActivity(intent);
                                        }
                                    }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                        }


                        break;
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                switch (itemIndex){

                    case 0:
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.signOut();

                        UploadDriverData();

                        startActivity(new Intent(Drive.this, Login.class));
                        finish();
                        break;

                    case 1:

                        if (lang==1 ){
                            new androidx.appcompat.app.AlertDialog.Builder(Drive.this)
                                    .setMessage("هل تود الأتصال على رقم المطعم ؟")
                                    .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent((Intent.ACTION_DIAL));
                                            intent.setData(Uri.parse("tel:+962792942040"));
                                            startActivity(intent);
                                        }
                                    }).setNegativeButton("لا", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                        }
                        else {
                            new androidx.appcompat.app.AlertDialog.Builder(Drive.this)
                                    .setMessage("do you want to call the restaurant?")
                                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent((Intent.ACTION_DIAL));
                                            intent.setData(Uri.parse("tel:+962792942040"));
                                            startActivity(intent);
                                        }
                                    }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                        }


                        break;
                }
            }
        });
        spaceNavigationView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UploadDriverData();
    }

    private void UploadDriverData() {

        FirebaseFirestore fb = FirebaseFirestore.getInstance();

        HashMap<String, String> map = new HashMap<>();
        map.put("date_time", new Date().toString());
        map.put("delivery_sum", getRecieteSum());
        map.put("order_sum", getpriceSum());
        map.put("order_num", info.size()+"");

        fb.collection("Res_1_Driver_Report").document(getIntent().getStringExtra("email")).collection("1").document(new Date().toString()).set(map);

    }

    private String getRecieteSum() {

        Double sum = 0.0;
        for(int i=0; i<info.size(); i++)
            sum+=Double.parseDouble(info.get(i).substring(info.get(i).indexOf("المجموع : ")+10));

        return sum+"";
    }

    private String getpriceSum() {

        Double sum = 0.0;
        for(int i=0; i<info.size(); i++)
            sum+=Double.parseDouble(info.get(i).substring(info.get(i).indexOf("سعر توصيل : ")+12, info.get(i).indexOf("العنوان : ")));

        return sum+"";
    }

    private void getLngLat() {

        latlng.clear();

        db.collection("Res_1_Delivery")
                .document(name).collection("1")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot document : task.getResult())
                    latlng.add(document.get("lat").toString()+","+document.get("lng").toString());
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

    public void downloadDriverData(){

        String em = getIntent().getStringExtra("email");
        db.collection("Res_1_employee")
                .document(em)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){

                            String temp = "";
                            temp += task.getResult().get("Fname").toString();
                            temp += " "+task.getResult().get("Lname").toString();
                            AddDataName(temp);
                        }
                    }
                });

    }

    public void AddDataName(String temp){
        name = temp;
        downloadData();
        getNoti();
    }

    public void downloadData(){

        info.clear();

        db.collection("Res_1_Delivery")
                .document(name).collection("1")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot document : task.getResult())
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
                        else {
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
                        if(info.isEmpty()){
                            if (lang==1){
                                Toast.makeText(Drive.this, "لايوجد طلبات دلفري", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Drive.this, "No delivery request", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

            }
        });
    }



}
