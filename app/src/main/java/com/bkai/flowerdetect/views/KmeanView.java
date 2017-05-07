package com.bkai.flowerdetect.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.bkai.flowerdetect.R;
import com.bkai.flowerdetect.adapters.GridViewKmeanAdapter;
import com.bkai.flowerdetect.database.DBHelper;
import com.bkai.flowerdetect.helpers.MyHelper;
import com.bkai.flowerdetect.logic.Cluster;
import com.bkai.flowerdetect.logic.Prediction;
import com.bkai.flowerdetect.models.Flower;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class KmeanView extends AppCompatActivity {

    GridViewKmeanAdapter mGridViewPictureAdapter;
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
        _waiting.setMessage(getResources().getString(R.string.loading));

        setUGridView(this);
        setupToolBar();
    }

    void setUGridView(Activity activity){
        AsyncTaskGridView prepare = new AsyncTaskGridView(activity);
        prepare.execute();
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Prediction prediction = new Prediction(getApplicationContext(),_handerPrediction, listKmeans.get(i));
                prediction.start();
            }
        });
    }

    class AsyncTaskGridView extends AsyncTask{
        Activity activity;

        public AsyncTaskGridView(Activity activity){
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            _waiting.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            listKmeans = getData();
            return listKmeans;
        }

        @Override
        protected void onPostExecute(Object o) {
            _waiting.dismiss();
            mGridViewPictureAdapter = new GridViewKmeanAdapter(activity, R.layout.grid_cluster_view, listKmeans);
            gridView.setAdapter(mGridViewPictureAdapter);
            super.onPostExecute(o);
        }
    }


    private final Handler _handerPrediction = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String name = msg.getData().getString("result");
            DBHelper db = new DBHelper(getApplicationContext());

            Flower flower = db.getFlowerById((int)Float.parseFloat(name));

            Intent flowerDetail = new Intent(getApplicationContext(), Result_Dialog.class);
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

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_left);

        if (getResources().getConfiguration().locale.toString().equals("vi")){
            getSupportActionBar().setTitle("Đố bé chọn đúng bông hoa?");
        } else {
            getSupportActionBar().setTitle("Can you pick the right flower ?");
        }

        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private ArrayList<Bitmap> getData(){
        ArrayList result = new ArrayList();
        Intent intent = getIntent();
        String img_src = intent.getStringExtra("img_path");

        Uri _uri = Uri.parse(intent.getStringExtra("img_uri"));
        Bitmap bitmap = null;
        try {
            InputStream image_stream = getContentResolver().openInputStream(_uri);
            bitmap = BitmapFactory.decodeStream(image_stream );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        Mat mRbga = Imgcodecs.imread(img_src);
        Mat mRbga = MyHelper.bitmapToMat(bitmap);
        Mat mRgbaF = new Mat(mRbga.size(), CvType.CV_8UC3);

        Imgproc.cvtColor(mRbga, mRbga, Imgproc.COLOR_RGBA2BGR, 3);

        Cluster cluster = new Cluster(_hander, mRbga, 3);
        cluster.run();

        mRgbaF = cluster.getClusters().get(0);
        mRgbaF = preProcess(mRgbaF);


        Bitmap bmp = Bitmap.createBitmap(mRgbaF.cols(), mRgbaF.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mRgbaF, bmp);
        result.add(bmp);
//        result.add(bitmap);

        mRgbaF = cluster.getClusters().get(1);
        mRgbaF = preProcess(mRgbaF);
        bmp = Bitmap.createBitmap(mRgbaF.cols(), mRgbaF.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mRgbaF, bmp);
        result.add(bmp);
//        result.add(bitmap);

        mRgbaF = cluster.getClusters().get(2);
        mRgbaF = preProcess(mRgbaF);
        bmp = Bitmap.createBitmap(mRgbaF.cols(), mRgbaF.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mRgbaF, bmp);
        result.add(bmp);
//        result.add(bitmap);
        return  result;
    }


    public Mat preProcess(final Mat src) {

//        final Mat dst = new Mat(src.rows(), src.cols(), src.type());
//        src.copyTo(dst);
//
//        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_BGR2GRAY);
//
//        final List<MatOfPoint> points = new ArrayList<>();
//        final Mat hierarchy = new Mat();
//        Imgproc.findContours(dst, points, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
//
//        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_GRAY2BGR);

        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2RGBA, 4);

        return src;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
