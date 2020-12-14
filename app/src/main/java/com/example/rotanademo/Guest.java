package com.example.rotanademo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Guest extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static class MenuModel {

        private String name, desc, price, image;

        public MenuModel() {
        }

        public String getName() {
            return name;
        }

        public String getDesc() {
            return desc;
        }

        public String getPrice() {
            return price;
        }

        public String getImage() {
            return image;
        }

    }

    private class MenuModelViewHolder extends RecyclerView.ViewHolder {

        private View view;

        MenuModelViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        void setInfo(final String na, final String de, final String pr, final String im) {

            TextView n = view.findViewById(R.id.name);
            TextView d = view.findViewById(R.id.title);
            TextView p = view.findViewById(R.id.company);
            CircleImageView img = view.findViewById(R.id.image);

            n.setText(na);
            d.setText(de);
            p.setText(pr);
            Glide.with(getApplicationContext()).load(im).into(img);

            CardView lay = view.findViewById(R.id.lay);
            lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(Guest.this);
                    LayoutInflater inflater = Guest.this.getLayoutInflater();

                    if(shared2.getString("language", "").equals("arabic"))
                        builder.setView(inflater.inflate(R.layout.dialog_deliver2, null));
                    else
                        builder.setView(inflater.inflate(R.layout.dialog_deliver, null));

                    final AlertDialog dialog = builder.create();
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    dialog.show();
                    dialog.getWindow().setAttributes(lp);

                    TextView t = dialog.findViewById(R.id.type);
                    t.setText(na);

                    Button deliv = dialog.findViewById(R.id.deliv);
                    deliv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            EditText a = dialog.findViewById(R.id.name);
                            EditText b = dialog.findViewById(R.id.mobile);
                            EditText c = dialog.findViewById(R.id.desc);

                            uploadDelivery(a.getText().toString(), b.getText().toString(), c.getText().toString(),
                                    na, de, pr, lat, lng);
                        }
                    });

                    Button tak = dialog.findViewById(R.id.takeaway);
                    tak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            EditText a = dialog.findViewById(R.id.name);
                            EditText b = dialog.findViewById(R.id.mobile);
                            EditText c = dialog.findViewById(R.id.desc);

                            uploadTakeAway(a.getText().toString(), b.getText().toString(), c.getText().toString(),
                                    na, de, pr);
                        }
                    });

                }
            });


        }


    }

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

    RecyclerView MenuList;

    FirebaseFirestore db;
    FirestoreRecyclerAdapter adapter;
    FirestoreRecyclerOptions<MenuModel> options;

    int Count = 0;

    SharedPreferences shared;
    SharedPreferences shared2;

    double lng, lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest2);

        Intent intent = new Intent(this, MyService.class);
        startService(intent);

        shared = getSharedPreferences("delivery", MODE_PRIVATE);
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (shared2.getString("language", "").equals("arabic"))
            toolbar.setTitle("أهلا و سهلا بك");
        else
            toolbar.setTitle("Welcome Customer");

        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        if(shared2.getString("language", "").equals("arabic")){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_guest2_drawer2); }
        else{
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_guest2_drawer); }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; }

        try{

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                    10, mLocationListener);
        }catch (Exception e){}

        GetMenuitems(); }

    public void GetMenuitems(){

        MenuList = findViewById(R.id.list);
        MenuList.setLayoutManager(new LinearLayoutManager(Guest.this));

        Query query = db.collection("Res_1_Menu").orderBy("price", Query.Direction.ASCENDING);

        options = new FirestoreRecyclerOptions.Builder<MenuModel>().setQuery(query, MenuModel.class).build();
        adapter = new FirestoreRecyclerAdapter<MenuModel, MenuModelViewHolder>(options){

            @Override
            protected void onBindViewHolder(@NonNull MenuModelViewHolder holder, int position, @NonNull MenuModel model) {

                holder.setInfo(model.getName(), model.getDesc(), model.getPrice(), model.getImage()); }

            @NonNull
            @Override
            public MenuModelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
                return new MenuModelViewHolder(view);
            }
        };
        MenuList.setAdapter(adapter);

    }

    public void uploadDelivery(String user_name, String user_mobile, String user_desc,
                           String item_name, String item_desc, String item_price,
                           double la, double lo){

        Map<String, Object> order = new HashMap<>();
        order.put("user_name", user_name);
        order.put("user_mobile", user_mobile);
        order.put("user_desc", user_desc);
        order.put("lat", la);
        order.put("lng", lo);
        order.put("item_name", item_name);
        order.put("item_desc", item_desc);
        order.put("item_price", item_price);

        if(shared.getString("mid", "").equals("")){
            SharedPreferences.Editor editor = shared.edit();


            editor.putString("mid", user_mobile);
            editor.apply();

            db.collection("Res_1_Delivery").document((new Date()).toString()).set(order)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                Toast.makeText(Guest.this, "Operation Successful, Your Order Is Pending !", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(Guest.this, "Error, Please Try Ordering Again !", Toast.LENGTH_SHORT).show();

                        }
                    });

        }
        else{
            db.collection("Res_1_Delivery").document((new Date()).toString()).set(order)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                Toast.makeText(Guest.this, "Operation Successful, Your Order Is Pending !", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(Guest.this, "Error, Please Try Ordering Again !", Toast.LENGTH_SHORT).show();

                        }
                    });
        }

    }

    public void uploadTakeAway(String user_name, String user_mobile, String user_desc,
                               String item_name, String item_desc, String item_price){

        Map<String, Object> order = new HashMap<>();
        order.put("user_name", user_name);
        order.put("user_mobile", user_mobile);
        order.put("user_desc", user_desc);
        order.put("item_name", item_name);
        order.put("item_desc", item_desc);
        order.put("item_price", item_price);

        if(shared.getString("mid", "").equals("")){
            SharedPreferences.Editor editor = shared.edit();


            editor.putString("mid", user_mobile);
            editor.apply();

            db.collection("Res_1_TakeAway").document((new Date()).toString()).set(order)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                Toast.makeText(Guest.this, "Operation Successful, Your Order Is Pending !", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(Guest.this, "Error, Please Try Ordering Again !", Toast.LENGTH_SHORT).show();

                        }
                    });

        }
        else{
            db.collection("Res_1_TakeAway").document((new Date()).toString()).set(order)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                Toast.makeText(Guest.this, "Operation Successful, Your Order Is Pending !", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(Guest.this, "Error, Please Try Ordering Again !", Toast.LENGTH_SHORT).show();

                        }
                    });
        }

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
            getMenuInflater().inflate(R.menu.guest2, menu);
        else
            getMenuInflater().inflate(R.menu.guest, menu);

        return true; }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){

            case R.id.menu:
                if(shared2.getString("language", "").equals("arabic")){
                    new AlertDialog.Builder(this)
                            .setTitle("ما هي قائمة الطعام ؟")
                            .setMessage("بأمكانك هنا ان ترى قائمة طعام هذا المطعم و عند الضغط على احدى الوجبات, يمكن ان تطلبها سفري او توصيل الى مكانك")
                            .setPositiveButton("أوك", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show(); }
                else{
                    new AlertDialog.Builder(this)
                            .setTitle("What is the food menu ?")
                            .setMessage("You can see here all the resturant regular menu, their description and their prices. You can also press any item and ask for it to be delivered to your location by default !")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show(); }
            break;

            case R.id.table:
                if(shared2.getString("language", "").equals("arabic")){
                    new AlertDialog.Builder(this)
                            .setTitle("ما هو حجز الطاولة ؟")
                            .setMessage("هنا بأمكانك ان تحجز طاولتك عبر الانترنت, قم باختيار الموعد و وقت الحجز و عدد الأشخاص ليتم حجز طاولة لك بعد التاكد من وجود شواغر")
                            .setPositiveButton("أوك", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show(); }
                else{
                    new AlertDialog.Builder(this)
                            .setTitle("What is the table reservation ?")
                            .setMessage("Here you can see how much tables are available and if you like reserve a table at a specific time and people. You will receive a confirmation call for your reservation once its made !")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show(); }
            break;

            case R.id.take:
                if(shared2.getString("language", "").equals("arabic")){
                    new AlertDialog.Builder(this)
                            .setTitle("ما هو طلب السفري")
                            .setMessage("عند الضغط على احدى الوجبات و اختيار طلب سفري, سترى جميع طلباتك هنا و سيتم تحضير الوجبة قبل ان تصل لتستلمها عند وصولك مباشرة")
                            .setPositiveButton("أوك", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show(); }
                else{
                    new AlertDialog.Builder(this)
                            .setTitle("What is take away ?")
                            .setMessage("You can order a take away and view all your take away's here. Use this page to order food and claim it when you pass by the resturent !")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show(); }
                break;

            case R.id.delivery:
                if(shared2.getString("language", "").equals("arabic")){
                    new AlertDialog.Builder(this)
                            .setTitle("ما هو طلب التوصيل ؟")
                            .setMessage("يمكنك ان تطلب وجبة او عدة وجبات و سيتم عندها اخذ معلوماتك و موقعك و توصيلها لهذا الموقع لضمان السرعة والجودة")
                            .setPositiveButton("أوك", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show(); }
                else{
                    new AlertDialog.Builder(this)
                            .setTitle("What is your delivery ?")
                            .setMessage("Once you press on an item from the food menu, a form will pop up for you to fill and after its complete the delivery order will be sent to the resturant !")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show(); }
            break;

            case R.id.location:
                if(shared2.getString("language", "").equals("arabic")){
                    new AlertDialog.Builder(this)
                            .setTitle("ما هو موقعنا ؟")
                            .setMessage("تمكنك هذه الصفحة من العثور على موقعنا و توجهك ب اسرع طرق خالية من الزحام مع الوقت المقرر للوصول, لضمان السرعة و الراحة")
                            .setPositiveButton("أوك", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show(); }
                else{
                    new AlertDialog.Builder(this)
                            .setTitle("What is our location ?")
                            .setMessage("This tab allows you to navigate from your current location with specific instruction to the resturents location with other features such as estimated arrival time, best shortest route and trafic information !")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show(); }
            break;

            case R.id.contact:
                if(shared2.getString("language", "").equals("arabic")){
                    new AlertDialog.Builder(this)
                            .setTitle("ما هو التواصل معنا ؟")
                            .setMessage("من هنا يمكنك ان تعرض هواتف جميع الجهات المسؤؤولة عن المطعم والبرنامج ليتم مساعدتك باسرع وقت ممكن")
                            .setPositiveButton("أوك", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show(); }
                else{
                    new AlertDialog.Builder(this)
                            .setTitle("What is the contact us ?")
                            .setMessage("Here you can view the resturents customer services mobile number and the manager mobile number. And both numbers are for the best customer support possible !")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show(); }
            break;

            case R.id.about:
                if(shared2.getString("language", "").equals("arabic")){
                    new AlertDialog.Builder(this)
                            .setTitle("ما هو عن البرنامج ؟")
                            .setMessage("جميع المعلوات عن مبرمجي البرنامج و كيفية التواصل معهم")
                            .setPositiveButton("أوك", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show(); }
                else{
                    new AlertDialog.Builder(this)
                            .setTitle("What is about the app ?")
                            .setMessage("A brief description about the creators of the application with all the information neccessery to contact us, provided by email or phone number !")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); } })
                            .setCancelable(false).create().show(); }
            break;

        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.nav_menu:
                Toast.makeText(this, "Your Already Here !", Toast.LENGTH_SHORT).show(); break;
            case R.id.nav_table: startActivity(new Intent(this, Tabel_res_new.class)); break;
            case R.id.nav_take: startActivity(new Intent(this, TakeAway.class)); break;
            case R.id.nav_delivery: startActivity(new Intent(this, Delivery.class)); break;
            case R.id.nav_pro: startActivity(new Intent(this, Profile.class)); break;
            case R.id.nav_location: startActivity(new Intent(this, Loc.class)); break;
            case R.id.nav_contact: startActivity(new Intent(this, Contact.class)); break;
            case R.id.nav_about: startActivity(new Intent(this, About.class)); break; }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
