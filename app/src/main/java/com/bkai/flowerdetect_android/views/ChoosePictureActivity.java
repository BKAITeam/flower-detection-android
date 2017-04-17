package com.bkai.flowerdetect_android.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.bkai.flowerdetect_android.R;
import com.bkai.flowerdetect_android.adapters.GridViewPicturesAdapter;

import java.io.File;
import java.util.ArrayList;

public class ChoosePictureActivity extends AppCompatActivity {

    GridViewPicturesAdapter mGridViewPictureAdapter;
    GridView gridView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_picture);

        gridView = (GridView) findViewById(R.id.gridview);
        mGridViewPictureAdapter = new GridViewPicturesAdapter(this, R.layout.grid_view_item_picture, getData());
        gridView.setAdapter(mGridViewPictureAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Click " + String.valueOf(i), Toast.LENGTH_SHORT).show();
            }
        });
        setupToolBar();
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
    private ArrayList<Bitmap> getData(){
        ArrayList result = new ArrayList();
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

        result.add(uri_1);
        result.add(uri_2);
        result.add(uri_3);

        return  result;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
