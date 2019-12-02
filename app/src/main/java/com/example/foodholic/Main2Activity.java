package com.example.foodholic;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    ArrayList<classItem>items;
    ArrayList<String>itemToShow;
    FirebaseFirestore db;

    Spinner spinner;
    ListView listView;
    ArrayList<classSubItem>subItems=new ArrayList<>();
    ArrayList<String>subItemToShow;
    public ArrayList<String>order;
    ArrayList<classSubItem>subItemsAfterFilering;

    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            lng = location.getLongitude();
            lat = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    double lng, lat;

    double sum = 0;
    double point = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest2);
        Space(savedInstanceState);

        Intent intent = new Intent(this, MyService.class);
        startService(intent);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (HomeAct.lang==1){
            toolbar.setTitle("مؤسسة الدخيل");
        }
        else {
            toolbar.setTitle("welcome");
        }

        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.inflateMenu(R.menu.activity_guest2_drawer2);

        order = new ArrayList<String>();
        db=FirebaseFirestore.getInstance();

        loadSubItem();
        loadItem();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(Main2Activity.this);

        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; }

        try{

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                    10, mLocationListener);
        }catch (Exception e){}

        spinner =findViewById(R.id.guestSpinner);
        listView=findViewById(R.id.guestList);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSubItem(items.get(position).itemName);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                for (int j=0; j<subItems.size(); j++){
                    if(subItemToShow.get(i).equals(subItems.get(j).subItem)){
                        order.add(subItems.get(j).subItem + " = 1 : "+subItems.get(j).price+", ");
                        sum+=Double.parseDouble(subItems.get(j).price+"");
                        point+=Double.parseDouble(subItems.get(j).point+"");
                    }
                }
                if (HomeAct.lang==1){
                    Toast.makeText(Main2Activity.this, "تمت الاضافة", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Main2Activity.this, "Done", Toast.LENGTH_SHORT).show();
                }

            }
        });
        getOffers();
    }

    private void getOffers() {

        final ArrayList<String> temp1, temp2;
        temp1 = new ArrayList<>();
        temp2 = new ArrayList<>();

        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection("Res_1_Offer")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot snaps : task.getResult()){
                        temp1.add(snaps.get("description").toString());
                        temp2.add(snaps.get("link").toString()); }
                slider(temp1, temp2); }
            }
        });

    }

    public void slider(ArrayList<String> desc, ArrayList<String> link){

        final SliderLayout mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        HashMap<String,String> url_maps = new HashMap<String, String>();

        for(int i=0; i<link.size(); i++)
            url_maps.put(desc.get(i), link.get(i));

        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);

            textSliderView.description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView); }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.setPresetTransformer("ZoomOut");

    }

    private void Space(Bundle savedInstanceState){

        SpaceNavigationView spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_menu));
        spaceNavigationView.addSpaceItem(new SpaceItem("",R.drawable.ic_support));
        spaceNavigationView.setSpaceItemIconSize(100);

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {

                ListView list = new ListView(Main2Activity.this);

                ArrayAdapter<String> arr = new ArrayAdapter<String>(Main2Activity.this, android.R.layout.simple_spinner_item,
                        order);
                list.setAdapter(arr);

                if (HomeAct.lang==1){
                    new AlertDialog.Builder(Main2Activity.this).setTitle("مجموع الفاتورة : "+sum+" دينار")
                            .setMessage("عند تاكيد طلبك يرجى تعبائة بعض المعلومات...").setPositiveButton("طلب", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, int i) {

                            final AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                            LayoutInflater inflater = Main2Activity.this.getLayoutInflater();
                            builder.setView(inflater.inflate(R.layout.dialog_deliver2, null));
                            final AlertDialog dialog = builder.create();
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialog.getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                            dialog.show();
                            dialog.getWindow().setAttributes(lp);

                            final TextView time = dialog.findViewById(R.id.time);
                            time.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    final Calendar c = Calendar.getInstance();
                                    int hour = c.get(Calendar.HOUR_OF_DAY);
                                    int minute = c.get(Calendar.MINUTE);

                                    TimePickerDialog timePickerDialog = new TimePickerDialog(Main2Activity.this,
                                            new TimePickerDialog.OnTimeSetListener() {

                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                                    time.setText(hourOfDay + ":" + minute);
                                                }
                                            }, hour, minute, false);
                                    timePickerDialog.show();

                                }
                            });

                            EditText a = dialog.findViewById(R.id.name);
                            EditText b = dialog.findViewById(R.id.mobile);
                            a.setText(getSharedPreferences("stay", MODE_PRIVATE).getString("n", ""));
                            b.setText(getSharedPreferences("stay", MODE_PRIVATE).getString("m", ""));

                            Button deliv = dialog.findViewById(R.id.deliv);
                            deliv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    EditText a = dialog.findViewById(R.id.name);
                                    EditText b = dialog.findViewById(R.id.mobile);
                                    EditText c = dialog.findViewById(R.id.desc);

                                    uploadDelivery(a.getText().toString(), b.getText().toString(), c.getText().toString(), lat, lng);
                                    dialog.dismiss();
                                }
                            });

                            Button tak = dialog.findViewById(R.id.takeaway);
                            tak.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    EditText a = dialog.findViewById(R.id.name);
                                    EditText b = dialog.findViewById(R.id.mobile);
                                    EditText c = dialog.findViewById(R.id.desc);

                                    uploadTakeAway(a.getText().toString(), b.getText().toString(), c.getText().toString());
                                    dialog.dismiss();
                                }
                            });


                        }
                    }).setNegativeButton("الغاء الطلب", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            order.clear();
                            sum=0;
                            dialogInterface.dismiss();
                        }
                    }).show();
                }
                else{
                    new AlertDialog.Builder(Main2Activity.this).setTitle("Total : "+sum+" JOD")
                            .setPositiveButton("Request", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, int i) {

                            final AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                            LayoutInflater inflater = Main2Activity.this.getLayoutInflater();
                            builder.setView(inflater.inflate(R.layout.dialog_deliver2, null));
                            final AlertDialog dialog = builder.create();
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialog.getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                            dialog.show();
                            dialog.getWindow().setAttributes(lp);

                            Button deliv = dialog.findViewById(R.id.deliv);
                            deliv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    EditText a = dialog.findViewById(R.id.name);
                                    EditText b = dialog.findViewById(R.id.mobile);
                                    EditText c = dialog.findViewById(R.id.desc);

                                    uploadDelivery(a.getText().toString(), b.getText().toString(), c.getText().toString(), lat, lng);
                                    dialogInterface.dismiss();
                                }
                            });

                            Button tak = dialog.findViewById(R.id.takeaway);
                            tak.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    EditText a = dialog.findViewById(R.id.name);
                                    EditText b = dialog.findViewById(R.id.mobile);
                                    EditText c = dialog.findViewById(R.id.desc);

                                    uploadTakeAway(a.getText().toString(), b.getText().toString(), c.getText().toString());
                                    dialogInterface.dismiss();
                                }
                            });

                            Toast.makeText(Main2Activity.this, "Request is done", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            order.clear();
                            sum=0;
                            dialogInterface.dismiss();
                        }
                    }).show();

                    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) list.getLayoutParams();
                    p.setMargins(50, 50, 50, 50);
                    list.requestLayout();
                }
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch (itemIndex){

                    case 0:
                        ListView list = new ListView(Main2Activity.this);

                        ArrayAdapter<String> arr = new ArrayAdapter<String>(Main2Activity.this, android.R.layout.simple_spinner_item,
                                order);
                        list.setAdapter(arr);

                        if (HomeAct.lang==1){
                            new AlertDialog.Builder(Main2Activity.this).setTitle("مجموع الفاتورة : "+sum+" دينار")
                                    .setView(list).show();

                            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) list.getLayoutParams();
                            p.setMargins(50, 50, 50, 50);
                            list.requestLayout();
                        }
                        else{
                            new AlertDialog.Builder(Main2Activity.this).setTitle("Total : "+sum+" JOD")
                                    .setView(list).show();

                            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) list.getLayoutParams();
                            p.setMargins(50, 50, 50, 50);
                            list.requestLayout();
                        }
                        break;

                    case 1:
                        new AlertDialog.Builder(Main2Activity.this)
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
                        break;
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                switch (itemIndex){

                    case 0:
                        ListView list = new ListView(Main2Activity.this);

                        ArrayAdapter<String> arr = new ArrayAdapter<String>(Main2Activity.this, android.R.layout.simple_spinner_item,
                                order);
                        list.setAdapter(arr);

                        if (HomeAct.lang==1){
                            new AlertDialog.Builder(Main2Activity.this).setTitle("المجموع : "+sum+" دينار")
                                    .setView(list).show();

                            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) list.getLayoutParams();
                            p.setMargins(50, 50, 50, 50);
                            list.requestLayout();
                        }
                        else{
                            new AlertDialog.Builder(Main2Activity.this).setTitle("Total : "+sum+" JOD")
                                    .setView(list).show();

                            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) list.getLayoutParams();
                            p.setMargins(50, 50, 50, 50);
                            list.requestLayout();
                        }
                        break;

                    case 1:

                        break;
                }
            }
        });
        spaceNavigationView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }

    public void uploadDelivery(String user_name, String user_mobile, String user_desc, double la, double lo){

        String temp = "";
        for(int i=0; i<order.size(); i++)
            temp+=order.get(i);

        FirebaseAuth a = FirebaseAuth.getInstance();

        Map<String, Object> order = new HashMap<>();
        order.put("user_name", user_name);
        order.put("user_mobile", user_mobile);
        order.put("user_desc", user_desc);
        order.put("item_list", temp);
        order.put("item_sum_price", sum+"");
        order.put("point_sum", point+"");
        order.put("lat", la);
        order.put("lng", lo);

        if(a.getCurrentUser() != null)
            order.put("email", a.getCurrentUser().getEmail());
        else
            order.put("email", "");

            db.collection("Res_1_Delivery").document("برنامج")
                    .collection("1").document(user_name).set(order)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                if (HomeAct.lang==1){
                                    Toast.makeText(Main2Activity.this, "تمت اضافة طلبك", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(Main2Activity.this, "The request is added", Toast.LENGTH_SHORT).show();
                                }
                            }

                            else{
                                if (HomeAct.lang==1){
                                    Toast.makeText(Main2Activity.this, "الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(Main2Activity.this, "Please try again", Toast.LENGTH_SHORT).show();
                                }
                            }


                        }
                    });

    }

    public void uploadTakeAway(String user_name, String user_mobile, String user_desc){

        String temp = "";
        for(int i=0; i<order.size(); i++)
            temp+=order.get(i);

        FirebaseAuth a = FirebaseAuth.getInstance();

        Map<String, Object> order = new HashMap<>();
        order.put("user_name", user_name);
        order.put("user_mobile", user_mobile);
        order.put("user_desc", user_desc);
        order.put("item_list", temp);
        order.put("item_sum_price", sum+"");
        order.put("point_sum", point+"");
        order.put("user_loc", "");

        if(a.getCurrentUser() != null)
            order.put("email", a.getCurrentUser().getEmail());
        else
            order.put("email", "");

        db.collection("Res_1_TakeAway").document((new Date()).toString()).set(order)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if (HomeAct.lang==1){
                                Toast.makeText(Main2Activity.this, "تمت اضافة طلبك", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Main2Activity.this, "The request is added", Toast.LENGTH_SHORT).show();
                            }
                        }

                        else{
                            if (HomeAct.lang==1){
                                Toast.makeText(Main2Activity.this, "الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Main2Activity.this, "Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        }

    public void loadSubItem(){
        db.collection("Res_1_subItem").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                subItems=new ArrayList<>();
                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
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
                ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplication(),android.R.layout.simple_list_item_1,itemToShow);
                adapter.setDropDownViewResource(R.layout.spinner_item);
                spinner.setAdapter(adapter);
                spinner.setSelection(0);

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
        ArrayAdapter<classSubItem>adapter=new mainGuestAdapter(getApplication(),R.layout.row_guest,subItemsAfterFilering);
        listView.setAdapter(adapter);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.guest2, menu);

        return true; }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (HomeAct.lang==1){
            switch (id){

                case R.id.menu:
                    new AlertDialog.Builder(this)
                            .setTitle("ما هي قائمة الطعام ؟")
                            .setMessage("بأمكانك هنا ان ترى قائمة طعام هذا المطعم و عند الضغط على احدى الوجبات, يمكن ان تطلبها سفري او توصيل الى مكانك")
                            .setPositiveButton("أوك", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show();
                    break;

                case R.id.table:
                    new AlertDialog.Builder(this)
                            .setTitle("ما هو حجز الطاولة ؟")
                            .setMessage("هنا بأمكانك ان تحجز طاولتك عبر الانترنت, قم باختيار الموعد و وقت الحجز و عدد الأشخاص ليتم حجز طاولة لك بعد التاكد من وجود شواغر")
                            .setPositiveButton("أوك", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show();
                    break;

                case R.id.take:
                    new AlertDialog.Builder(this)
                            .setTitle("ما هو طلب السفري؟")
                            .setMessage("عند الضغط على احدى الوجبات و اختيار طلب سفري, سترى جميع طلباتك هنا و سيتم تحضير الوجبة قبل ان تصل لتستلمها عند وصولك مباشرة")
                            .setPositiveButton("أوك", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show();
                    break;

                case R.id.delivery:
                    new AlertDialog.Builder(this)
                            .setTitle("ما هو طلب التوصيل ؟")
                            .setMessage("يمكنك ان تطلب وجبة او عدة وجبات و سيتم عندها اخذ معلوماتك و موقعك و توصيلها لهذا الموقع لضمان السرعة والجودة")
                            .setPositiveButton("أوك", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show();
                    break;

                case R.id.location:
                    new AlertDialog.Builder(this)
                            .setTitle("ما هو موقعنا ؟")
                            .setMessage("تمكنك هذه الصفحة من العثور على موقعنا و توجهك ب اسرع طرق خالية من الزحام مع الوقت المقرر للوصول, لضمان السرعة و الراحة")
                            .setPositiveButton("أوك", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show();
                    break;

                case R.id.contact:
                    new AlertDialog.Builder(this)
                            .setTitle("ما هو التواصل معنا ؟")
                            .setMessage("من هنا يمكنك ان تعرض هواتف جميع الجهات المسؤؤولة عن المطعم والبرنامج ليتم مساعدتك باسرع وقت ممكن")
                            .setPositiveButton("أوك", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show();
                    break;

                case R.id.about:
                    new AlertDialog.Builder(this)
                            .setTitle("ما هو عن البرنامج ؟")
                            .setMessage("جميع المعلوات عن مبرمجي البرنامج و كيفية التواصل معهم")
                            .setPositiveButton("أوك", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show();
                    break;

            }
        }
        else {
            switch (id){

                case R.id.menu:
                    new AlertDialog.Builder(this)
                            .setTitle("What is the menu?")
                            .setMessage("Here you can see the menu of this restaurant and when you click on a meal, you can order it or travel to your place.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show();
                    break;

                case R.id.table:
                    new AlertDialog.Builder(this)
                            .setTitle("What is table reservation?")
                            .setMessage("Here you can book your table online, choose the date and time of booking and the number of people to be booked table for you after making sure of vacancies")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show();
                    break;

                case R.id.take:
                    new AlertDialog.Builder(this)
                            .setTitle("What is my travel request?")
                            .setMessage("When you click on a meal and choose a travel request, you will see all your requests here and the meal will be prepared before you arrive to receive it when you arrive directly")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show();
                    break;

                case R.id.delivery:
                    new AlertDialog.Builder(this)
                            .setTitle("What is the delivery request?")
                            .setMessage("You can order one or several meals and your information and location will be taken and delivered to this site to ensure speed and quality")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show();
                    break;

                case R.id.location:
                    new AlertDialog.Builder(this)
                            .setTitle("What is our location?")
                            .setMessage("This page enables you to find our location and direct you in the fastest traffic-free routes with the scheduled arrival time, to ensure speed and convenience")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show();
                    break;

                case R.id.contact:
                    new AlertDialog.Builder(this)
                            .setTitle("What is communication with us?")
                            .setMessage("From here you can show the phones of all those responsible for the restaurant and the program to help you as soon as possible")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show();
                    break;

                case R.id.about:
                    new AlertDialog.Builder(this)
                            .setTitle("What is it about the program?")
                            .setMessage("All information about programmers and how to communicate with them")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show();
                    break;

            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.nav_menu:
                if(HomeAct.lang==1){
                    Toast.makeText(this, "أنت في هذه الصفحة حاليا", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "You are currently on this page", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.nav_table: startActivity(new Intent(this, Tabel_res_new.class)); break;
            case R.id.nav_pro: startActivity(new Intent(this, Profile.class)); break;
            case R.id.nav_location: startActivity(new Intent(this, Loc.class)); break;
            case R.id.nav_contact: startActivity(new Intent(this, Contact.class)); break;
            case R.id.nav_about: startActivity(new Intent(this, About.class)); break; }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
