package com.bkai.flowerdetect_android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;

public class ShowPicture extends AppCompatActivity {

    ImageView img_view;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_show_picture);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(Color.parseColor("#0DBCBCBC"));
        toolbar.setTitleTextColor(Color.BLACK);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        img_view = (ImageView) findViewById(R.id.img_view);
        show_image();

    }



    private void show_image(){
        Intent intent = getIntent();
        String img_src = intent.getStringExtra("img_path");
//        byte[] img_binary = intent.getByteArrayExtra("img_binary");

//        Bitmap bmp = BitmapFactory.decodeByteArray(img_binary, 0, img_binary.length);


        Log.e("File", img_src);
        File img_file = new File(img_src);



        Uri uri = Uri.fromFile(img_file);

        img_view.setImageURI(uri);
//        img_view.setImageBitmap(bmp);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
