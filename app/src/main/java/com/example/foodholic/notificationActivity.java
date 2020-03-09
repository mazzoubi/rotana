package com.example.foodholic;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class notificationActivity extends AppCompatActivity {
    FirebaseFirestore db ;
    ListView listView;
    Button button;
    SharedPreferences shared2;
    public static ArrayList<classNotification> notifications;
    public static classNotification noti=new classNotification();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_notification);
        init();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0 ; i< notifications.size();i++){
                   try{
                       db.collection("Res_1_adminNotificationsAr").document(notifications.get(i).id).delete();
                       db.collection("Res_1_adminNotificationsEn").document(notifications.get(i).id).delete();
                   }catch (Exception e ){}
                }
                notifications=new ArrayList<>();
                notifications.add(noti);
                ArrayAdapter<classNotification> adapter=new notificationAdapter(getApplicationContext(),R.layout.row_noti,notifications);
                listView.setAdapter(adapter);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent n =new Intent(getApplicationContext(),notificationInfo.class);
                n.putExtra("title",notifications.get(position).title);
                n.putExtra("desc",notifications.get(position).desc);
                n.putExtra("time",notifications.get(position).time);
                n.putExtra("date",notifications.get(position).date);
                n.putExtra("id",notifications.get(position).id);
                n.putExtra("state",notifications.get(position).state);
                startActivity(n);
            }
        });
    }
    void init(){
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        db=FirebaseFirestore.getInstance();
        button=findViewById(R.id.button6);
        listView =findViewById(R.id.listView);
        if(shared2.getString("language", "").equals("arabic")){
            loadNoteArabic();
            button.setText("حذف جميع الاشعارات");
        }
        else {
            loadNoteEng();
        }

    }

    void loadNoteArabic(){
        notifications=new ArrayList<>();
        db.collection("Res_1_adminNotificationsAr").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d:list){
                    notifications.add(0,d.toObject(classNotification.class));
                }
                ArrayAdapter<classNotification> adapter=new notificationAdapter(getApplicationContext(),R.layout.row_noti,notifications);
                listView.setAdapter(adapter);
            }
        });

    }
    void loadNoteEng(){
        notifications=new ArrayList<>();
        db.collection("Res_1_adminNotificationsEn").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d:list){
                    notifications.add(0,d.toObject(classNotification.class));
                }
                ArrayAdapter<classNotification> adapter=new notificationAdapter(getApplicationContext(),R.layout.row_noti,notifications);
                listView.setAdapter(adapter);
            }
        });
    }

}
