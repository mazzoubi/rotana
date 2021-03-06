package com.example.rotanademo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashAct extends AppCompatActivity {

    TextView ivLogo, ivSubtitle, ivSubtitle2, ivBtn, ivBtn2;
    ImageView ivSplash;
    Animation smalltobig, fleft, fhelper;

    SharedPreferences shared;
    SharedPreferences shared2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        if ( ContextCompat.checkSelfPermission( this,
                Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        if ( ContextCompat.checkSelfPermission( this,
                Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        if ( ContextCompat.checkSelfPermission( this,
                Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED )
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, 3);
        if ( ContextCompat.checkSelfPermission( this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED )
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);
        if ( ContextCompat.checkSelfPermission( this,
                Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED )
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
        if ( ContextCompat.checkSelfPermission( this,
                Manifest.permission.ACCESS_NETWORK_STATE ) != PackageManager.PERMISSION_GRANTED )
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 6);
        if ( ContextCompat.checkSelfPermission( this,
                Manifest.permission.INTERNET ) != PackageManager.PERMISSION_GRANTED )
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET}, 7);

        shared = getSharedPreferences("lang", MODE_PRIVATE);

        if(!shared.getString("language", "").equals("")){

           // startActivity(new Intent(SplashAct.this, HomeAct.class));
           // finish();

        }

        smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        fleft = AnimationUtils.loadAnimation(this, R.anim.fleft);
        fhelper = AnimationUtils.loadAnimation(this, R.anim.fhelper);

        ivLogo =  findViewById(R.id.ivLogo);
        ivSubtitle =  findViewById(R.id.ivSubtitle);
        ivSubtitle2 =  findViewById(R.id.ivSubtitle2);
        ivBtn =  findViewById(R.id.ivBtn);
        ivBtn2 =  findViewById(R.id.ivBtn2);

        ivSplash =  findViewById(R.id.ivSplash);


        ivSplash.startAnimation(smalltobig);

        ivLogo.setTranslationX(400);
        ivSubtitle.setTranslationX(400);
        ivSubtitle2.setTranslationX(400);
        ivBtn.setTranslationX(400);
        ivBtn2.setTranslationX(400);

        ivLogo.setAlpha(0);
        ivSubtitle.setAlpha(0);
        ivSubtitle2.setAlpha(0);
        ivBtn.setAlpha(0);
        ivBtn2.setAlpha(0);

        ivLogo.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        ivSubtitle.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        ivSubtitle2.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        ivBtn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        ivBtn2.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();

        ivBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = shared.edit();
                editor.putString("language", "english");
                editor.apply();

                Intent ax = new Intent(SplashAct.this, HomeAct.class);
                startActivity(ax);
                overridePendingTransition(R.anim.fleft, R.anim.fhelper);
            }
        });

        ivBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = shared.edit();
                editor.putString("language", "arabic");
                editor.apply();

                Intent ax = new Intent(SplashAct.this, HomeAct.class);
                startActivity(ax);
                overridePendingTransition(R.anim.fleft, R.anim.fhelper);
            }
        });

    }

    public Boolean checker() {

        LocationManager lm = (LocationManager) SplashAct.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        return gps_enabled && network_enabled;

    }

}
