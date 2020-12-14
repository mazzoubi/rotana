package com.example.rotanademo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class aempLogin extends AppCompatActivity {
    EditText ppass;
    AutoCompleteTextView et1;
    Button llogin;

    SharedPreferences shared;

    ArrayAdapter<String> adapter;
    ArrayList<String> temp;

    FirebaseFirestore db,fb1;
    public static ArrayList<classEmployee> sss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_aemp_login);

        shared = getSharedPreferences("lang", MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();
        temp = new ArrayList<String>();


        adapter = new ArrayAdapter<String>(aempLogin.this, android.R.layout.simple_dropdown_item_1line, temp);
        et1 = findViewById(R.id.email);
        et1.setAdapter(adapter);

        getAccountList();

        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().endsWith(".emp@yahoo.com") ||
                        charSequence.toString().endsWith(".emp@gmail.com") ||
                        charSequence.toString().endsWith(".com") ||
                        charSequence.toString().endsWith(".ad@yahoo.com") ||
                        charSequence.toString().endsWith(".ad@gmail.com") ||
                        charSequence.toString().endsWith(".del@yahoo.com") ||
                        charSequence.toString().endsWith(".del@gmail.com"))
                    et1.setTextColor(Color.GREEN);
                else
                    et1.setTextColor(Color.RED);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ppass = findViewById(R.id.pass);
        llogin = findViewById(R.id.loginn);

        llogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Email = et1.getText().toString();
                final String Password = ppass.getText().toString();
                FirebaseAuth auth = FirebaseAuth.getInstance();

                if(!Email.equals("") && !Password.equals("")){

                    auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {



                                fb1=FirebaseFirestore.getInstance();

                                fb1.collection("Res_1_employee").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                                        sss=new ArrayList<>();

                                        for (DocumentSnapshot d: list){
                                            sss.add(d.toObject(classEmployee.class));
                                        }
                                        Intent ax = new Intent(aempLogin.this, aempInfo.class);
                                        ax.putExtra("empemail",Email);
                                        ax.putExtra("112",1);
                                        startActivity(ax);

                                    }
                                });


                                /*
                                 if (Email.contains(".ad")){

                                    Intent ax = new Intent(aempLogin.this, Adminpage.class);
                                ax.putExtra("empemail",Email);
                                startActivity(ax);
                                finish(); }

                            else if (Email.contains(".del")){

                                Intent ax = new Intent(aempLogin.this, Drive.class);
                                ax.putExtra("email", Email);
                                startActivity(ax);
                                finish(); }

                            else if (Email.contains(".emp")){

                                Intent ax = new Intent(aempLogin.this, Emppage.class);
                                ax.putExtra("empemail",Email);
                                startActivity(ax);
                                finish(); }

                            else if (Email.contains(".cap")){

                                Intent ax = new Intent(aempLogin.this, Emppage.class);
                                ax.putExtra("empemail",Email);
                                startActivity(ax);
                                finish(); }
                                */
                            } else
                                Toast.makeText(aempLogin.this, "Sign in error, Account not found !", Toast.LENGTH_SHORT).show();
                        }
                    });
                }



            }

        });


        if (shared.getString("language", "").equals("arabic")){
            et1.setHint("البريد الالكتروني");
            ppass.setHint("كلمة السر");
            llogin.setText("تسجيل دخول"); }

        GetCurrency();

    }

    private void GetCurrency(){

        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection("Res_1_currencyAndTax")
                .document("onlyOne").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful())
                            AddData(task.getResult().get("currency").toString(),
                                    task.getResult().get("currencyAr").toString(),
                                    task.getResult().get("tax").toString());

                    }
                });

    }

    private void AddData(String currency, String currencyAr, String tax) {

        SharedPreferences sha = getSharedPreferences("Finance", MODE_PRIVATE);
        SharedPreferences.Editor editor = sha.edit();

        if(HomeAct.lang == 1)
            editor.putString("cur", currencyAr);
        else
            editor.putString("cur", currency);

        editor.putString("tax", tax);
        editor.apply();

    }

    public void getAccountList(){

        db.collection("Res_1_employee")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful())
                            for (QueryDocumentSnapshot document : task.getResult())
                                addData(document.getId());

                    } });

    }

    public void addData(String str){

        temp.add(str);
        adapter.notifyDataSetChanged();

    }

}
