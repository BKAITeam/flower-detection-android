package com.bkai.flowerdetect.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.bkai.flowerdetect.R;
import com.bkai.flowerdetect.adapters.GridViewPicturesAdapter;
import com.bkai.flowerdetect.database.DBHelper;
import com.bkai.flowerdetect.logic.Prediction;
import com.bkai.flowerdetect.models.Flower;

import java.io.File;
import java.util.ArrayList;

public class ChoosePictureActivity extends AppCompatActivity {

    GridViewPicturesAdapter mGridViewPictureAdapter;
    GridView gridView;
    Toolbar toolbar;
    ProgressDialog _waiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_picture);

        _waiting = new ProgressDialog( ChoosePictureActivity.this,  R.style.AppTheme_Dark_Dialog);

        gridView = (GridView) findViewById(R.id.gridview);
        mGridViewPictureAdapter = new GridViewPicturesAdapter(this, R.layout.grid_view_item_picture, getData());
        gridView.setAdapter(mGridViewPictureAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                _waiting.setIndeterminate(true);
                _waiting.setMessage("Detecting...");
                _waiting.show();
                Prediction prediction = new Prediction(_hander);
                prediction.start();
            }
        });

        setupToolBar();
    }

    private final Handler _hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String name = msg.getData().getString("result");
            DBHelper db = new DBHelper(getApplicationContext());

            Flower flower = db.getFlowerById(Integer.parseInt(name));
            Log.e("Prediction result", flower.getName());
            _waiting.dismiss();

            Intent flowerDetail = new Intent(getApplicationContext(), FlowerDetail.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("flower", flower);
            flowerDetail.putExtra("flower_package", bundle);
            startActivity(flowerDetail);

        }
    };

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
