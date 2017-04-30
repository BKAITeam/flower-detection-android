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

import com.bkai.flowerdetect.R;
import com.bkai.flowerdetect.adapters.GridViewPicturesAdapter;
import com.bkai.flowerdetect.database.DBHelper;
import com.bkai.flowerdetect.logic.Cluster;
import com.bkai.flowerdetect.logic.Prediction;
import com.bkai.flowerdetect.models.Flower;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;

public class KmeanView extends AppCompatActivity {

    GridViewPicturesAdapter mGridViewPictureAdapter;
    GridView gridView;
    Toolbar toolbar;
    ProgressDialog _waiting;

    ArrayList<Bitmap> listKmeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kmean_view);

        _waiting = new ProgressDialog(KmeanView.this,  R.style.AppTheme_Dark_Dialog);
        _waiting.setCancelable(false);

        _waiting.setIndeterminate(true);
        _waiting.setMessage("Please wait ...");

        setUGridView();
        setupToolBar();
    }

    void setUGridView(){
        _waiting.show();

        gridView = (GridView) findViewById(R.id.gridview);

        listKmeans = getData();

        mGridViewPictureAdapter = new GridViewPicturesAdapter(this, R.layout.grid_cluster_view, listKmeans);
        gridView.setAdapter(mGridViewPictureAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                _waiting.setIndeterminate(true);
//                _waiting.setMessage("Detecting...");
//                _waiting.show();
                Prediction prediction = new Prediction(getApplicationContext(),_handerPrediction, listKmeans.get(i));
                prediction.start();
            }
        });
    }

    private final Handler _handerPrediction = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String name = msg.getData().getString("result");
            DBHelper db = new DBHelper(getApplicationContext());

            Flower flower = db.getFlowerById((int)Float.parseFloat(name));
            _waiting.dismiss();

            Intent flowerDetail = new Intent(getApplicationContext(), FlowerDetail.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("flower", flower);
            flowerDetail.putExtra("flower_package", bundle);
            startActivity(flowerDetail);
        }
    };

    private final Handler _hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("CLUSTER", "DONE");
            _waiting.dismiss();
        }
    };

    void setupToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private ArrayList<Bitmap> getData(){
        ArrayList result = new ArrayList();
        Intent intent = getIntent();
        String img_src = intent.getStringExtra("img_path");
        Log.e("FILE", img_src);

        Mat mRbga = Imgcodecs.imread(img_src);
        Mat mRgbaF = new Mat(mRbga.size(), CvType.CV_8UC3);

        Imgproc.cvtColor(mRbga, mRbga, Imgproc.COLOR_RGB2BGR, 4);

        Cluster cluster = new Cluster(_hander, mRbga, 3);
        cluster.run();

        mRgbaF = cluster.getClusters().get(0);
        Bitmap bmp = Bitmap.createBitmap(mRgbaF.cols(), mRgbaF.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mRgbaF, bmp);
        result.add(bmp);

        mRgbaF = cluster.getClusters().get(1);
        bmp = Bitmap.createBitmap(mRgbaF.cols(), mRgbaF.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mRgbaF, bmp);
        result.add(bmp);

        mRgbaF = cluster.getClusters().get(2);
        bmp = Bitmap.createBitmap(mRgbaF.cols(), mRgbaF.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mRgbaF, bmp);
        result.add(bmp);

        return  result;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
