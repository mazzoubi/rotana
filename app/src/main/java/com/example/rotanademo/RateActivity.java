package com.example.rotanademo;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

public class RateActivity extends AppCompatActivity {

    Button b1, b2, b3, b4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        Toolbar toolbar = findViewById(R.id.tool);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        b1 = findViewById(R.id.resrate);
        b2 = findViewById(R.id.foorate);
        b3 = findViewById(R.id.emprate);
        b4 = findViewById(R.id.delrate);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(RateActivity.this);
                LayoutInflater inflater = RateActivity.this.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.dialog_rate, null));
                final AlertDialog dialog = builder.create();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.show();
                dialog.getWindow().setAttributes(lp);

                final EditText et1 = dialog.findViewById(R.id.name);
                final EditText et2 = dialog.findViewById(R.id.desc);

                final RatingBar rb = dialog.findViewById(R.id.rating);
                Button btn = dialog.findViewById(R.id.deliv);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        upRate("مطعم", et1.getText().toString(),
                                et2.getText().toString(), rb.getRating()+"");
                        dialog.dismiss(); } }); } });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(RateActivity.this);
                LayoutInflater inflater = RateActivity.this.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.dialog_rate, null));
                final AlertDialog dialog = builder.create();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.show();
                dialog.getWindow().setAttributes(lp);

                final EditText et1 = dialog.findViewById(R.id.name);
                et1.setHint("أسم الوجبة");
                final EditText et2 = dialog.findViewById(R.id.desc);

                final RatingBar rb = dialog.findViewById(R.id.rating);
                Button btn = dialog.findViewById(R.id.deliv);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        upRate("وجبة", et1.getText().toString(),
                                et2.getText().toString(), rb.getRating()+"");
                        dialog.dismiss(); } }); } });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(RateActivity.this);
                LayoutInflater inflater = RateActivity.this.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.dialog_rate, null));
                final AlertDialog dialog = builder.create();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.show();
                dialog.getWindow().setAttributes(lp);

                final EditText et1 = dialog.findViewById(R.id.name);
                et1.setHint("أسم الموظف");
                final EditText et2 = dialog.findViewById(R.id.desc);

                final RatingBar rb = dialog.findViewById(R.id.rating);
                Button btn = dialog.findViewById(R.id.deliv);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        upRate("موظف", et1.getText().toString(),
                                et2.getText().toString(), rb.getRating()+"");
                        dialog.dismiss(); } }); } });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(RateActivity.this);
                LayoutInflater inflater = RateActivity.this.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.dialog_rate, null));
                final AlertDialog dialog = builder.create();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.show();
                dialog.getWindow().setAttributes(lp);

                final EditText et1 = dialog.findViewById(R.id.name);
                et1.setHint("أسم موظف التوصيل");
                final EditText et2 = dialog.findViewById(R.id.desc);

                final RatingBar rb = dialog.findViewById(R.id.rating);
                Button btn = dialog.findViewById(R.id.deliv);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        upRate("توصيل", et1.getText().toString(),
                                et2.getText().toString(), rb.getRating()+"");
                        dialog.dismiss(); } }); } });

    }

    public void upRate(String path, String name, String note, String rate){

        HashMap<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("note", note);
        map.put("rate", rate);

        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection("Res_1_Rating").document(path)
                .collection("1").document(new Date().toString()).set(map)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Toast.makeText(RateActivity.this, "شكرا لوقتكم, ناخذ رأيكم بأهمية بالغة !", Toast.LENGTH_SHORT).show();
            }
        });





    }

}
