package com.example.foodholic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    EditText ppass;
    AutoCompleteTextView et1;
    Button llogin;

    SharedPreferences shared;

    ArrayAdapter<String> adapter;
    ArrayList<String> temp;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        shared = getSharedPreferences("lang", MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();
        temp = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(Login.this, android.R.layout.simple_dropdown_item_1line, temp);
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
                                if (Email.contains(".ad")){

                                    Intent ax = new Intent(Login.this, Adminpage.class);
                                    ax.putExtra("empemail",Email);
                                    startActivity(ax);
                                    finish(); }

                                else if (Email.contains(".del")){

                                    Intent ax = new Intent(Login.this, Drive.class);
                                    ax.putExtra("email", Email);
                                    startActivity(ax);
                                    finish(); }

                                else if (Email.contains(".emp")){

                                    Intent ax = new Intent(Login.this, Emppage.class);
                                    ax.putExtra("empemail",Email);
                                    startActivity(ax);
                                    finish(); }

                                else if (Email.contains(".cap")){

                                    Intent ax = new Intent(Login.this, Emppage.class);
                                    ax.putExtra("empemail",Email);
                                    startActivity(ax);
                                    finish(); }

                            } else
                                Toast.makeText(Login.this, "Sign in error, Account not found !", Toast.LENGTH_SHORT).show();
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
                                    task.getResult().get("tax").toString());

                    }
                });

    }

    private void AddData(String currency, String tax) {

        SharedPreferences sha = getSharedPreferences("Finance", MODE_PRIVATE);
        SharedPreferences.Editor editor = sha.edit();

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
