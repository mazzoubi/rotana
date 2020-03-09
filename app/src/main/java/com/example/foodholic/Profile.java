package com.example.foodholic;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

  FirebaseFirestore db;
  SharedPreferences shared;
  SharedPreferences shared2;
  SharedPreferences shared3;

  FirebaseAuth fire;
  StorageReference ref;
  String path="";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    shared = getSharedPreferences("lang", MODE_PRIVATE);
    shared2 = getSharedPreferences("stay", MODE_PRIVATE);
    shared3 = getSharedPreferences("info", MODE_PRIVATE);
    db = FirebaseFirestore.getInstance();
    fire = FirebaseAuth.getInstance();

    setContentView(R.layout.activity_profile2);

    Toolbar toolbar = findViewById(R.id.tool);
    toolbar.setTitleTextColor(Color.WHITE);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    CircleImageView im = findViewById(R.id.im);
    im.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1996);

      } });

    CheckLog();

    Button log = findViewById(R.id.log);
    log.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();

        onBackPressed();

      }
    });

  }



  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK){
      if (requestCode == 1996){

        Uri filePath = data.getData();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        if(filePath != null){}

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("لحظات من فضلك");
        progressDialog.show();

        final CircleImageView im = findViewById(R.id.im);
        Glide.with(Profile.this).load(filePath).into(im);

        ref = storageReference.child("images/"+ UUID.randomUUID().toString());
        ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                  @Override
                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                      @Override
                      public void onSuccess(Uri uri) {
                        SharedPreferences.Editor editor = shared3.edit();
                        editor.putString("path", uri.toString());
                        editor.apply();

                        progressDialog.dismiss();
                        Toast.makeText(Profile.this, "تم", Toast.LENGTH_SHORT).show();

                      }
                    });
                  }
                })
                .addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Profile.this, "حاول مجددا", Toast.LENGTH_SHORT).show();
                  }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                  @Override
                  public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage((int)progress+"%");
                  }
                });

      } } }

  public void CheckLog(){

    FirebaseAuth auth = FirebaseAuth.getInstance();
    String uid="";
    try{
      uid = auth.getCurrentUser().getUid();
    }
    catch(Exception ex){}

    if (!shared2.getBoolean("in?", false) || uid.equals("")){

      new AlertDialog.Builder(this)
              .setTitle("خيار")
              .setMessage("يجب ان يكون لديك حساب للمتابعة في هذه الصفحة")
              .setNegativeButton("تسجيل دخول", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  Intent intent = new Intent(Profile.this, UserLogin.class);
                  startActivity(intent); } })
              .setPositiveButton("إنشاء حساب", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  Intent intent = new Intent(Profile.this, UserRegister.class);
                  startActivity(intent); } })
              .create().show(); }
    else
      DownloadData();

  }

  private void DownloadData() {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    String uid="";
    try{ uid = auth.getCurrentUser().getUid(); }
    catch(Exception ex){}

    if(!uid.equals("")){

      final CircleImageView im = findViewById(R.id.im);
      final TextView name = findViewById(R.id.name);
      final TextView email = findViewById(R.id.email);
      final TextView mobile = findViewById(R.id.mobile);
      final TextView points = findViewById(R.id.points);

      try{

        db.collection("Res_1_Customer_Acc").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
          @Override
          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            name.setText("الاسم : "+task.getResult().get("name").toString());
            email.setText("الايميل : "+task.getResult().get("email").toString());
            mobile.setText("هاتف : "+task.getResult().get("mobile").toString());
            points.setText("نقاط : "+task.getResult().get("points").toString());
            Glide.with(Profile.this).load(shared3.getString("path", "")).into(im);
          }
        });
      } catch (Exception ex) {}}







  }

}
