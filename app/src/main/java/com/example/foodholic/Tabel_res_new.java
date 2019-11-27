package com.example.foodholic;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Tabel_res_new extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener date_;
    public static String[] sites = {
            "1", "2", "3", "4", "5",
            "6", "7", "8","9","10",
            "11","12","13","14","15" };

    ArrayList<String> tabels = new ArrayList<String>();

    public class CustomAdapter extends BaseAdapter{

        String [] result;
        Context context;

        private LayoutInflater inflater=null;

        public CustomAdapter(Tabel_res_new table, String[] site) {

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

                    final AlertDialog.Builder builder = new AlertDialog.Builder(Tabel_res_new.this);
                    LayoutInflater inflater = Tabel_res_new.this.getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.activity_tabel__res2, null));
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

                            new DatePickerDialog(Tabel_res_new.this, date_, myCalendar
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

                            TimePickerDialog timePickerDialog = new TimePickerDialog(Tabel_res_new.this,
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

                            db.collection("Res_1_Table_Res_").document(""+(position+1)).delete();
                            rowView.setBackgroundColor(Color.WHITE);
                            dialog.dismiss();
                        }
                    });


                }
            });

            return rowView;
        }

    }

    GridView gridview;
    FirebaseFirestore db;
    SharedPreferences shared, shared2;

    String da, ta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabel__res__emp);

        Toolbar toolbar = findViewById(R.id.tool);
        toolbar.setTitle("حجز الطاولة");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        shared = getSharedPreferences("order", MODE_PRIVATE);
        shared2 = getSharedPreferences("color", MODE_PRIVATE);

        getDateTime();

        db = FirebaseFirestore.getInstance();

        tabels.add("Table Number : 1"); tabels.add("Table Number : 2"); tabels.add("Table Number : 3");
        tabels.add("Table Number : 4"); tabels.add("Table Number : 5"); tabels.add("Table Number : 6");
        tabels.add("Table Number : 7"); tabels.add("Table Number : 8"); tabels.add("Table Number : 9");
        tabels.add("Table Number : 10"); tabels.add("Table Number : 11"); tabels.add("Table Number : 12");
        tabels.add("Table Number : 13"); tabels.add("Table Number : 14"); tabels.add("Table Number : 15");

        gridview = (GridView) findViewById(R.id.customgrid); }

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

        new DatePickerDialog(Tabel_res_new.this, lsnr, cal
                .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show();

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog lsn = new TimePickerDialog(Tabel_res_new.this,
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

        if(shared.getInt("pos", 0) == 0){
            vie.setBackgroundColor(getResources().getColor(R.color.colorPick));
            SharedPreferences.Editor editor = shared.edit();
            editor.putInt("pos", p);
            editor.apply();

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
                                Toast.makeText(Tabel_res_new.this, "تم اضافة الحجز", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(Tabel_res_new.this, "حاول مجددا", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else
            Toast.makeText(Tabel_res_new.this, "لديك حجز مسبق", Toast.LENGTH_LONG).show();

    }

    public void CheckTabels(){

        db.collection("Res_1_Table_Res_").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                try{
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult())
                            tabels.set(Integer.parseInt(document.getId())-1, document.get("time").toString()+"@"+document.get("date"));

                        gridview.setAdapter(new CustomAdapter(Tabel_res_new.this, sites)); }

                } catch(Exception ex){

                    gridview.setAdapter(new CustomAdapter(Tabel_res_new.this, tabels.toArray(new String[tabels.size()]) ));
                }

            } });
    }

}
