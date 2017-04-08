package com.bkai.flowerdetect_android;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

public class ShowPicture extends AppCompatActivity {

    ImageView img_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);
        img_view = (ImageView) findViewById(R.id.img_view);
        show_image();
    }

    private void show_image(){
        Intent intent = getIntent();
        String img_src = intent.getStringExtra("img_path");
        Log.e("File", img_src);
        File img_file = new File("/storage/emulated/0/FlowerDetect/2017-04-09_00-57-48.jpg");
        Uri uri = Uri.fromFile(img_file);

        img_view.setImageURI(uri);
    }
}
