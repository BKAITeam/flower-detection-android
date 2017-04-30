package com.bkai.flowerdetect.views;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.bkai.flowerdetect.R;
import com.bkai.flowerdetect.helpers.CameraView;
import com.bkai.flowerdetect.logic.Cluster;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.bkai.flowerdetect.R.id.takePicture;


public class CameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2, View.OnTouchListener {


    private static final String TAG = "OCVSample::Activity";
    private CameraView mOpenCvCameraView;
    private boolean mIsJavaCamera = true;
    private MenuItem mItemSwitchCamera = null;

    Mat mRgba;
    Mat mRgbaF;
    Mat mRgbaT;
    ProgressDialog _waiting;
    public static final int RGBA = 1;

    ImageButton takePicure;

    String mPictureFileName;

    private BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG,"OpenCV load successfully");
                    mOpenCvCameraView.enableView();

                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    private void checkPermission(){
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    123);
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);

        _waiting = new ProgressDialog( CameraActivity.this,  R.style.AppTheme_Transparent_Dialog);
        _waiting.setCancelable(false);

        mOpenCvCameraView = (CameraView) findViewById(R.id.CameraView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setFocusable(true);
        mOpenCvCameraView.setOnTouchListener(CameraActivity.this);

        takePicure = (ImageButton) findViewById(takePicture);
        takePicure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Cluster", "Begin");
//                new MyAsyncTakePicture().execute();
                takePicture_byOpencv(mRgba);

            }
        });
    }

    private void takePicture(Mat mRgba){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateandTime = sdf.format(new Date());
        String sdcard = Environment.getExternalStorageDirectory().getPath();
        File path = new File(sdcard+"/FlowerDetect/");
        path.mkdirs();
        mPictureFileName = sdcard + "/FlowerDetect/" + currentDateandTime + ".jpg";
        mOpenCvCameraView.takePicture(mPictureFileName, mPreviewCallBack);
    }

    class MyAsyncTakePicture extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] objects) {
            takePicture_byOpencv(mRgba);
            return null;
        }
    }

    private void takePicture_byOpencv(Mat mRgba){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateandTime = sdf.format(new Date());
        String sdcard = Environment.getExternalStorageDirectory().getPath();
        File path = new File(sdcard+"/FlowerDetect/");
        path.mkdirs();

        mPictureFileName = sdcard + "/FlowerDetect/" + currentDateandTime + "_1.jpg";

        Imgproc.cvtColor(mRgba, mRgbaT, Imgproc.COLOR_RGBA2RGB, 4);
        Imgproc.cvtColor(mRgbaT, mRgbaT, Imgproc.COLOR_RGB2BGR, 4);

        Imgcodecs.imwrite(mPictureFileName, mRgbaT);

        showPreview(mPictureFileName);
    }

    android.hardware.Camera.PreviewCallback  mPreviewCallBack = new android.hardware.Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] bytes, android.hardware.Camera camera) {
//            showPreview(mPictureFileName_1);
            Log.e(TAG, "mPreviewCallBack");
        }
    };

    public void showPreview(String img_path){
        Intent showPicture = new Intent(this, PicturePreview.class);

        File img_file = new File(img_path);
        Uri img_uri = Uri.fromFile(img_file);

//        showPicture.putExtra("img_uri", img_uri.toString());
        showPicture.putExtra("img_path", img_path);

        startActivity(showPicture);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC3);
        mRgbaF = new Mat(height, width, CvType.CV_8UC3);
        mRgbaT = new Mat(height, width, CvType.CV_8UC3);
//        List<Camera.Size> resolutions = mOpenCvCameraView.getResolutionList();
//        mOpenCvCameraView.setResolution(resolutions.get(resolutions.size()-1));
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        mRgba = inputFrame.rgba();

        switch (mOpenCvCameraView.getDisplay().getRotation()) {
            case Surface.ROTATION_0: // Vertical portrait
                Core.transpose(mRgba, mRgbaT);
                Imgproc.resize(mRgbaT, mRgbaF, mRgbaF.size(), 0, 0, 0);
                Core.flip(mRgbaF, mRgba, 2);
                break;
            case Surface.ROTATION_90: // 90° anti-clockwise
                break;
            case Surface.ROTATION_180: // Vertical anti-portrait
                Core.transpose(mRgba, mRgbaT);
                Imgproc.resize(mRgbaT, mRgbaF, mRgbaF.size(), 0, 0, 0);
                Core.flip(mRgbaF, mRgba, 0);
                break;
            case Surface.ROTATION_270: // 90° clockwise
                Imgproc.resize(mRgba, mRgbaF, mRgbaF.size(), 0, 0, 0);
                Core.flip(mRgbaF, mRgba, -1);
                break;
            default:
        }

//        previewProcess();
        return mRgba;
    }

    private final Handler _hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("CLUSTER", "DONE");
            _waiting.dismiss();
        }
    };

    void previewProcess(){
//        Imgproc.Canny(mRgba, mRgbaF, 80, 100);
//        Imgproc.cvtColor(mRgba, mRgbaT, Imgproc.COLOR_RGBA2GRAY, 4);

        Imgproc.cvtColor(mRgba, mRgbaT, Imgproc.COLOR_RGBA2RGB, 4);

//        Mat resizeimage = new Mat();

//        Size size = mRgbaT.size();
//        Imgproc.resize( mRgbaT, resizeimage, new Size(size.width/4, size.height/4));

        List<Mat> clusters = new ArrayList<Mat>();
//        clusters = new Cluster(_hander).cluster(mRgbaT, 3);
        Imgproc.cvtColor(clusters.get(0), mRgba, Imgproc.COLOR_RGB2BGRA, 4);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!OpenCVLoader.initDebug()){
            Log.e(TAG, "Internal OpenCv Library not found.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallBack);
        } else {
            Log.e(TAG, "OpenCv library found inside package.");
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mOpenCvCameraView != null){
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mOpenCvCameraView.focusOnTouch(motionEvent);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
