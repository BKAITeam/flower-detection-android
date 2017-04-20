package com.bkai.flowerdetect.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bkai.flowerdetect.R;

import java.io.File;

public class ShowPicture extends AppCompatActivity {

    ImageView img_view_1;
    ImageView img_view_2;
    ImageView img_view_3;
    Toolbar toolbar;
    public static byte img_binary[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_show_picture);

        setupToolBar();

        img_view_1 = (ImageView) findViewById(R.id.img_view_1);
        img_view_2 = (ImageView) findViewById(R.id.img_view_2);
        img_view_3 = (ImageView) findViewById(R.id.img_view_3);
        show_image();
//        show_image_from_bytes();

    }
    void setupToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

    //        toolbar.setBackgroundColor(Color.parseColor("#873FB5A9"));
    //        toolbar.setTitleTextColor(Color.BLACK);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private void show_image(){
        Intent intent = getIntent();
        String img_src_1 = intent.getStringExtra("img_path_1");
        String img_src_2 = intent.getStringExtra("img_path_2");
        String img_src_3 = intent.getStringExtra("img_path_3");

        Log.e("File", img_src_1);
        File img_file_1 = new File(img_src_1);
        File img_file_2 = new File(img_src_2);
        File img_file_3 = new File(img_src_3);

        Uri uri_1 = Uri.fromFile(img_file_1);
        Uri uri_2 = Uri.fromFile(img_file_2);
        Uri uri_3 = Uri.fromFile(img_file_3);

        img_view_1.setImageURI(uri_1);
        img_view_2.setImageURI(uri_2);
        img_view_3.setImageURI(uri_3);
    }

    private void show_image_from_bytes(){
        byte[] img_binary = ShowPicture.img_binary;

        Bitmap bmp = BitmapFactory.decodeByteArray(img_binary, 0, img_binary.length);

        img_view_1.setImageBitmap(bmp);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
