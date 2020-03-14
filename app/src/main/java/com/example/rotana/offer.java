package com.example.rotana;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class offer extends AppCompatActivity {

    public class OfferClass {

        public String description="";
        public String uid="";
        public String link=""; }
        int lang=0;

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

                    if (lang==1){
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
                    else {
                        new AlertDialog.Builder(context)
                                .setTitle("note")
                                .setMessage("do you want to delete the offer ?")
                                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).setPositiveButton("yes", new DialogInterface.OnClickListener() {
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_offer);
        SharedPreferences shared2;
        shared2 = getSharedPreferences("lang", MODE_PRIVATE);
        if(shared2.getString("language", "").equals("arabic")) {
            lang=1;
        }
        Toolbar toolbar = findViewById(R.id.tool);
        if (lang==1){
            toolbar.setTitle("إعدادات العروض");
        }
        else {
            toolbar.setTitle("offer settings");
        }

        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView = findViewById(R.id.list);
        adapter=new OfferAdapter(offer.this,R.layout.offer, items);
        listView.setAdapter(adapter);

        getOffers();

        Button btn = findViewById(R.id.btn);
        if (HomeAct.lang != 1){
            btn.setText("show all");
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(offer.this);
                LayoutInflater inflater = offer.this.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.offer_dio, null));
                final androidx.appcompat.app.AlertDialog dialog = builder.create();
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
                if(HomeAct.lang != 1)
                    et.setHint("Offer Description");
                Button btn = dialog.findViewById(R.id.add);
                if(HomeAct.lang != 1)
                    btn.setText("Add Offer");
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
                if (lang==1){
                    Toast.makeText(offer.this, "تمت العملية بنجاح", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(offer.this, "the operation is done", Toast.LENGTH_SHORT).show();
                }

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
