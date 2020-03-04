package com.example.foodholic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class MyService extends Service {

    public static boolean isServiceRunning = false;
    String str;
    DatabaseReference takeAway;
    DatabaseReference table;
    DatabaseReference delivery;
    NotificationManager manager;

    public static boolean take=false,tabl=false,del=false;
    int id =1,id2=1,id3;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        isServiceRunning = false;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        takeAway = FirebaseDatabase.getInstance().getReference().child("notification").child("notificationTakeAway");
        delivery = FirebaseDatabase.getInstance().getReference().child("notification").child("notificationDelivery");
        table = FirebaseDatabase.getInstance().getReference().child("notification").child("notificationTable");

        takeAway.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                showNotification("طلب سفري جديد");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        delivery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                showNotification("طلب ديلفري جديد");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                showNotification("حجز طاوله جديد");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return START_STICKY;
    }

    private void showNotification(String str) {

        isServiceRunning = true;
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("إشعار")
                .setContentText(str);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addNextIntent(new Intent(this, Main2Activity.class));
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_ONE_SHOT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }

}
/* to Delet notification

 try {
            takeAway = FirebaseDatabase.getInstance().getReference().child("notification").child("notificationTakeAway");
            takeAway.removeValue();
        }catch (Exception e ){
        }
 */

/*to add notification

                        Map<String, Object> map5 = new HashMap<String, Object>();
                        delivery.updateChildren(map5);
                        DatabaseReference message_root1 = delivery.child(temp_key);
                        Map<String, Object> map6 = new HashMap<String, Object>();
                        map6.put("name", "any thing");
                        message_root1.updateChildren(map6);
 */