package com.example.foodholic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class HomeAct extends AppCompatActivity {

    public static String resName= "demo res";

    Button guest,login;

    LinearLayout ivItemOne, ivItemTwo;

    ImageView ivIlls;
    Animation smalltobig, stb2;

    SharedPreferences shared;
    public static int lang=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        shared = getSharedPreferences("lang", MODE_PRIVATE);

        smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        stb2 = AnimationUtils.loadAnimation(this, R.anim.stb2);

        ivItemOne =  findViewById(R.id.ivItemOne);
        ivItemTwo =  findViewById(R.id.ivItemTwo);
        ivIlls =  findViewById(R.id.ivIlls);
        guest = findViewById(R.id.guest1);
        login = findViewById(R.id.login1);

        ivItemOne.setTranslationX(800);
        ivItemTwo.setTranslationX(800);
        ivIlls.setTranslationX(800);

        ivItemOne.setAlpha(0);
        ivItemTwo.setAlpha(0);
        ivIlls.setAlpha(0);

        ivIlls.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(200).start();
        ivItemOne.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        ivItemTwo.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(400).start();

        ivIlls.startAnimation(stb2);

        if (shared.getString("language", "").equals("arabic")){
            guest.setText("زبون");
            login.setText("تسجيل دخول");
        lang =1 ;
        }

        else{
            guest.setText("Guest");
            lang =0 ;
            login.setText("Log In"); }


        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAct.this,Main2Activity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAct.this, Login.class);
                startActivity(intent);
            }
        });

    }
}
