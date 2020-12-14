package com.example.rotanademo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class zzStringToBitmap extends AppCompatActivity {
    ImageView imageView;
    Button button;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zz_string_to_bitmap);

        init();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s=editText.getText().toString();
                imageView.setImageBitmap(stringTobitMap(s));
            }
        });

    }
    void init(){
        imageView=findViewById(R.id.imageView999);
        button=findViewById(R.id.button999);
        editText=findViewById(R.id.editText999);
    }
    public Bitmap stringTobitMap(String s){
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(25);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseLine= - paint.ascent();
        int width=(int)(paint.measureText(s)+0.5f);
        int high=(int)(baseLine+paint.descent()+0.5f);
        Bitmap image=Bitmap.createBitmap(width,high,Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(image);
        canvas.drawText(s,0,baseLine,paint);
        return image;
    }
}
