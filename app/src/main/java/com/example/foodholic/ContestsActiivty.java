package com.example.foodholic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ContestsActiivty extends AppCompatActivity {

    FirebaseFirestore db;
    ListView listView;

    ArrayAdapter<String> adapter;
    ArrayList<String> data;
    ArrayList<String> arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(HomeAct.lang == 1)
            setContentView(R.layout.activity_contests_actiivty);
        else
            setContentView(R.layout.activity_contests_actiivty2);

        db = FirebaseFirestore.getInstance();

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.list);
        data = new ArrayList<>();
        arr = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.items_row3, R.id.item, arr);
        listView.setAdapter(adapter);

        getData();

        Button add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText edt = new EditText(ContestsActiivty.this);
                edt.setGravity(Gravity.CENTER);
                edt.setTextSize(16);
                edt.setTextColor(Color.parseColor("#000000"));

                if(HomeAct.lang == 1){
                    new AlertDialog.Builder(ContestsActiivty.this)
                            .setTitle("يرجى كتابة موضوع المسابقة مع القواعد والشرح المراد عرضه للزبائن")
                            .setView(edt)
                            .setPositiveButton("نشر", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    UploadData(edt.getText().toString());
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
                else{
                    new AlertDialog.Builder(ContestsActiivty.this)
                            .setTitle("Fill Information For The Customers To Read, Include Dates If Possible")
                            .setView(edt)
                            .setPositiveButton("Share", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    UploadData(edt.getText().toString());
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                if(HomeAct.lang == 1){
                    new AlertDialog.Builder(ContestsActiivty.this)
                            .setTitle("يرجى التأكيد")
                            .setMessage("هل ترغب بحذف هذه المسابقة ؟ ")
                            .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setPositiveButton("حذف", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String s = data.get(position);

                            db.collection("Res_1_Contest")
                                    .document(s.substring(s.indexOf("@@@")+3))
                                    .delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                Toast.makeText(ContestsActiivty.this, "تم الحذف بنجاح !", Toast.LENGTH_LONG).show();
                                                recreate();
                                            }

                                        }
                                    });

                        }
                    }).show();
                }
                else{
                    new AlertDialog.Builder(ContestsActiivty.this)
                            .setTitle("Alert !")
                            .setMessage("Delete This Contest/Info ?")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String s = data.get(position);

                            db.collection("Res_1_Contest")
                                    .document(s.substring(s.indexOf("@@@")+3))
                                    .delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                Toast.makeText(ContestsActiivty.this, "Removed !", Toast.LENGTH_LONG).show();
                                                recreate();
                                            }

                                        }
                                    });

                        }
                    }).show();
                }

            }
        });

    }

    private void UploadData(String str) {

        String da = getIntent().getStringExtra("date");
        Map<String, Object> map = new HashMap<>();
        map.put("date", da);
        map.put("msg", str);

        db.collection("Res_1_Contest")
                .add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()) {
                    if(HomeAct.lang == 1)
                    Toast.makeText(ContestsActiivty.this, "تم النشر بنجاح !", Toast.LENGTH_LONG).show();
                   else
                    Toast.makeText(ContestsActiivty.this, "Done !", Toast.LENGTH_LONG).show();
                    recreate(); }
            }
        });

    }

    private void getData() {

        data.clear();
        arr.clear();
        adapter.notifyDataSetChanged();

        db.collection("Res_1_Contest").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful())
                            for(QueryDocumentSnapshot document : task.getResult())
                                AddData(document.get("msg").toString()+"@@@"+document.getId());
                    }
                });

    }

    private void AddData(String msg) {

        data.add(msg);
        arr.add(msg.substring(0, msg.indexOf("@@@")));
        adapter.notifyDataSetChanged(); }


}
