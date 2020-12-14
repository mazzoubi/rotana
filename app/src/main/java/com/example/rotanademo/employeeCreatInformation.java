package com.example.rotanademo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class employeeCreatInformation extends AppCompatActivity {

    ListView listView ;
    Button addEmp,deleteEmp;
    FirebaseFirestore db ;
    public static ArrayList<classEmployee> emp =new ArrayList<>();
    SharedPreferences shared2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_employee_creat_information);

        Toolbar bar = findViewById(R.id.tool);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.empList);
        addEmp=(Button)findViewById(R.id.addEmp);
        deleteEmp=(Button)findViewById(R.id.deleteEmp);


        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        if(shared2.getString("language", "").equals("arabic")) {
            addEmp.setText("اضافة موظف");
            deleteEmp.setText("حضور الموظفين");
        }

        db=FirebaseFirestore.getInstance();
        db.collection("Res_1_employee").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list =queryDocumentSnapshots.getDocuments();
                emp =new ArrayList<>();
                for(DocumentSnapshot d : list){
                    classEmployee e =d.toObject(classEmployee.class);
                    emp.add(e);
                }

                ArrayAdapter<classEmployee>adapter=new empAdapter(getApplicationContext(),R.layout.rowemp,emp);
                listView .setAdapter(adapter);
            }
        });

        addEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n=new Intent(getApplicationContext(),employeeAdd.class);
                startActivity(n);
            }
        });

        deleteEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent n=new Intent(getApplicationContext(),onWork.class);
                startActivity(n);

            }
        });



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int xx =i;
                if(shared2.getString("language", "").equals("arabic")) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(employeeCreatInformation.this)
                            .setMessage("هل تريد حذف هذا الموظف؟")
                            .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        db.collection("Res_1_employee").document(emp.get(xx).email).delete();
                                        Map<String, Object> reservation = new HashMap<>();
                                        reservation.put("Fname", emp.get(xx).Fname);
                                        reservation.put("Lname", emp.get(xx).Lname);
                                        reservation.put("regisetDate", emp.get(xx).regisetDate);
                                        reservation.put("email", emp.get(xx).email);
                                        reservation.put("empType", emp.get(xx).empType);
                                        reservation.put("empPhone", emp.get(xx).empPhone); //
                                        reservation.put("sal",emp.get(xx).sal);
                                        reservation.put("address",emp.get(xx).address);
                                        reservation.put("jopType", emp.get(xx).jopType);


                                        db.collection("Res_1_employeeDeleted").document(emp.get(xx).email).set(reservation)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                    }
                                                });

                                        Toast.makeText(getApplicationContext(),"تم الاجراء بنجاح",Toast.LENGTH_LONG).show();
                                    }catch (Exception e){Toast.makeText(getApplicationContext(),"!!لايمكن تنفيذ هذا الاجراء",Toast.LENGTH_LONG).show();}
                                }
                            })
                            .setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert=builder.create();
                    alert.setTitle("حذف موظف");
                    alert.show();


                }
                else {
                    AlertDialog.Builder builder=new AlertDialog.Builder(employeeCreatInformation.this)
                            .setMessage("do you want delete this employee?")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        db.collection("Res_1_employee").document(emp.get(xx).email).delete();
                                        db.collection("Res_1_employee").document(emp.get(xx).email).delete();
                                        Map<String, Object> reservation = new HashMap<>();
                                        reservation.put("Fname", emp.get(xx).Fname);
                                        reservation.put("Lname", emp.get(xx).Lname);
                                        reservation.put("regisetDate", emp.get(xx).regisetDate);
                                        reservation.put("email", emp.get(xx).email);
                                        reservation.put("empType", emp.get(xx).empType);
                                        reservation.put("empPhone", emp.get(xx).empPhone); //
                                        reservation.put("sal",emp.get(xx).sal);
                                        reservation.put("address",emp.get(xx).address);
                                        reservation.put("jopType", emp.get(xx).jopType);


                                        db.collection("Res_1_employeeDeleted").document(emp.get(xx).email).set(reservation)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                    }
                                                });

                                        Toast.makeText(getApplicationContext(),"this operation is done",Toast.LENGTH_LONG).show();

                                    }catch (Exception e){Toast.makeText(getApplicationContext(),"This procedure cannot be performed",Toast.LENGTH_LONG).show();}
                                }
                            })
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert=builder.create();
                    alert.setTitle("Delete Employee");
                    alert.show();


                }


                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent n =new Intent(getApplicationContext(),adminEmpAccess.class);
                n.putExtra("position",position);
                startActivity(n);
            }

        });

    }
}
