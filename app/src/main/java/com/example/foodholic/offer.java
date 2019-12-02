package com.example.foodholic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class offer extends AppCompatActivity {

    public class OfferClass {

        public String description="";
        public String uid="";
        public String link=""; }

    public class OfferAdapter extends ArrayAdapter<OfferClass> {

        OfferClass offer ;
        Context context;

        public OfferAdapter(Context context, int view , ArrayList<OfferClass> arrayList) {
            super(context,view,arrayList);
            this.context = context; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            LayoutInflater layoutInflater=LayoutInflater.from(getContext());
            View myView = layoutInflater.inflate(R.layout.offer,parent,false);

            TextView desc = myView.findViewById(R.id.desc);
            ImageView image = myView.findViewById(R.id.image);

            offer=getItem(position);

            desc.setText(offer.description);
            Glide.with(context).load(offer.link).into(image);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(context)
                            .setTitle("تنبيه")
                            .setMessage("هل ترغب بحذف العرض ؟")
                            .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseFirestore fb = FirebaseFirestore.getInstance();
                            fb.collection("Res_1_Offer").document(offer.uid)
                                    .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                        recreate();
                                }
                            });
                        }
                    }).show();

                }
            });

            return myView ; }

    }

    ArrayList<OfferClass> items = new ArrayList<>();
    ArrayAdapter<OfferClass>adapter;
    ImageView im;
    Uri ImageUri;
    byte[] thumb_byte_data;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        Toolbar toolbar = findViewById(R.id.tool);
        toolbar.setTitle("إعدادات العروض");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView = findViewById(R.id.list);
        adapter=new OfferAdapter(offer.this,R.layout.offer, items);
        listView.setAdapter(adapter);

        getOffers();

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(offer.this);
                LayoutInflater inflater = offer.this.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.offer_dio, null));
                final android.support.v7.app.AlertDialog dialog = builder.create();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.show();
                dialog.getWindow().setAttributes(lp);

                im = dialog.findViewById(R.id.image);
                im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent galleryIntent = new Intent();
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, 1996); } });

                final EditText et = dialog.findViewById(R.id.desc);

                Button btn = dialog.findViewById(R.id.add);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Uri resultUri = ImageUri;

                        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                        final StorageReference ref = mStorageRef.child("Offers/"+ UUID.randomUUID().toString());
                        ref.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        offerUpload(et.getText().toString(), uri.toString());
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });

                    }
                });

            }
        });

    }

    private void offerUpload(String desc, String link) {

        OfferClass o = new OfferClass();
        o.description = desc;
        o.link = link;

        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection("Res_1_Offer").document()
                .set(o).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(offer.this, "تمت العملية بنجاح !", Toast.LENGTH_SHORT).show();
                recreate();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1996 && resultCode == RESULT_OK && data != null){
            ImageUri = data.getData();
            im.setImageURI(ImageUri); }


    }

    private void getOffers() {

        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection("Res_1_Offer")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot snaps : task.getResult()){
                        OfferClass o = new OfferClass();
                        o.description = snaps.get("description").toString();
                        o.link = snaps.get("link").toString();
                        o.uid = snaps.getId();
                        items.add(o);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }
}