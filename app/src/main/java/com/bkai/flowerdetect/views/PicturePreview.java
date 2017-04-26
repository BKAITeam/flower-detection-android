package com.bkai.flowerdetect.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bkai.flowerdetect.R;
import com.bkai.flowerdetect.logic.Cluster;

import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;

public class PicturePreview extends AppCompatActivity {

    ImageView img_view;
    Toolbar toolbar;
    public static byte img_binary[];
    ProgressDialog _waiting;

    ImageButton btn_next;
    ImageButton btn_back;

    String mPictureFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_picture_preview);

        _waiting = new ProgressDialog( PicturePreview.this,  R.style.AppTheme_Transparent_Dialog);
        _waiting.setCancelable(false);

        btn_next = (ImageButton) findViewById(R.id.btn_next);
        btn_back = (ImageButton) findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cluster();
            }
        });

        img_view = (ImageView) findViewById(R.id.img_picture_preview);
        show_image();
    }

    void setupToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void cluster(){
        Intent showPicture = new Intent(this, KmeanView.class);
        showPicture.putExtra("img_path", mPictureFileName);
        startActivity(showPicture);
    }

    private void show_image(){
        Intent intent = getIntent();
        mPictureFileName = intent.getStringExtra("img_path");

        Log.e("File", mPictureFileName);
        File img_file_1 = new File(mPictureFileName);

        Uri uri_1 = Uri.fromFile(img_file_1);

        img_view.setImageURI(uri_1);
    }

    private final Handler _hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("CLUSTER", "DONE");
            _waiting.dismiss();
        }
    };

     @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
