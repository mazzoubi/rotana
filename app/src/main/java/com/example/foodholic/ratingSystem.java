package com.example.foodholic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ratingSystem extends AppCompatActivity {

    public static ArrayList<classRating> delevery;
    public static ArrayList<classRating> emp;
    public static ArrayList<classRating> res;
    public static ArrayList<classRating> meal;

    FirebaseFirestore db;

    Button bRes,bDel,bEmp,bMeal;
    TextView result,sumt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_system);

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        LoadData();

        bRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resl="";
                double sum=0;
                int count=0;

                for (classRating d : res){
                    resl+="----------------------------------------------\n" +
                            "التقييم : "+d.rate+" نقطة\n" +
                            "ملاحظة: "+d.note+"\n" +
                            "اسم الخدمة او الموظف: "+d.name+"\n" +
                            "----------------------------------------------\n";
                    sum+=Double.parseDouble(d.rate);
                    count++;
                }
                result.setText(resl);
                sumt.setText("The rate AVG= "+(sum/count)+" Point");
            }
        });
        bDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resl="";
                double sum=0;
                int count=0;

                for (classRating d : delevery){
                    resl+="----------------------------------------------\n" +
                            "التقييم : "+d.rate+" نقطة\n" +
                            "ملاحظة: "+d.note+"\n" +
                            "اسم الخدمة او الموظف: "+d.name+"\n" +
                            "----------------------------------------------\n";
                    sum+=Double.parseDouble(d.rate);
                    count++;
                }
                result.setText(resl);
                sumt.setText("The rate AVG= "+(sum/count)+" Point");
            }
        });
        bEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resl="";
                double sum=0;
                int count=0;

                for (classRating d : emp){
                    resl+="----------------------------------------------\n" +
                            "التقييم : "+d.rate+" نقطة\n" +
                            "ملاحظة: "+d.note+"\n" +
                            "اسم الخدمة او الموظف: "+d.name+"\n" +
                            "----------------------------------------------\n";
                    sum+=Double.parseDouble(d.rate);
                    count++;
                }
                result.setText(resl);
                sumt.setText("The rate AVG= "+(sum/count)+" Point");
            }
        });
        bMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resl="";
                double sum=0;
                int count=0;

                for (classRating d : meal){
                    resl+="----------------------------------------------\n" +
                            "التقييم : "+d.rate+" نقطة\n" +
                            "ملاحظة: "+d.note+"\n" +
                            "اسم الخدمة او الموظف: "+d.name+"\n" +
                            "----------------------------------------------\n";
                    sum+=Double.parseDouble(d.rate);
                    count++;
                }
                result.setText(resl);
                sumt.setText("The rate AVG= "+(sum/count)+" Point");
            }
        });

    }


    void init (){
        db = FirebaseFirestore.getInstance();
        bRes=findViewById(R.id.rat_res);
        bDel=findViewById(R.id.rat_delv);
        bEmp=findViewById(R.id.rat_emp);
        bMeal=findViewById(R.id.rat_meal);
        result=findViewById(R.id.rat_textResult);
        sumt=findViewById(R.id.rat_textSumRate);

    }
    void LoadData(){
        delevery =new ArrayList<>();
        res=new ArrayList<>();
        emp=new ArrayList<>();
        meal=new ArrayList<>();

        db.collection("Res_1_Rating").document("توصيل").collection("1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d :list){
                    delevery.add(d.toObject(classRating.class));
                }
            }
        });
        db.collection("Res_1_Rating").document("مطعم").collection("1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d :list){
                    res.add(d.toObject(classRating.class));
                }
            }
        });
        db.collection("Res_1_Rating").document("موظف").collection("1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d :list){
                    emp.add(d.toObject(classRating.class));
                }
            }
        });
        db.collection("Res_1_Rating").document("وجبة").collection("1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d :list){
                    meal.add(d.toObject(classRating.class));
                }
            }
        });
    }
}
