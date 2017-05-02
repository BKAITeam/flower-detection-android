package com.bkai.flowerdetect.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.bkai.flowerdetect.helpers.MyHelper;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.ml.SVM;

import java.io.File;
import java.util.Random;

import static org.opencv.core.CvType.CV_32F;

/**
 * Created by marsch on 4/20/17.
 */

public class Prediction extends Thread {
    Context _context;
    Handler _handler;
    float _result;
    Bitmap _img;

    public Prediction(Context context, Handler handler, Bitmap img){
        this._context = context;
        this._handler = handler;
        this._img = img;
    }

    @Override
    public void run() {
        super.run();
        predict();
        Bundle bundle = new Bundle();
        bundle.putString("result", String.valueOf(this._result));
        Message msg = new Message();
        msg.setData(bundle);
        this._handler.sendMessage(msg);
    }

    public void predict(){
        SVM svm = SVM.create();
        svm.setGamma(0.05);
        svm.setC(20);
        svm.setKernel(SVM.RBF);
        svm.setType(SVM.C_SVC);

        String svm_dir = this._context.getApplicationInfo().dataDir + "/" + "svm.yml";

        svm = SVM.load(svm_dir);

//        Mat mat = new Mat(this._img.getWidth(), this._img.getHeight(), CvType.CV_8UC3);
//        MyHelper.bitmapToMat(this._img, mat);

        Mat mat = MyHelper.bitmapToMat(this._img);

        Moments momento = new Moments();
        Mat hu = new Mat(1,7, CV_32F);

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY, 4);

        momento = Imgproc.moments(mat, false); // ERROR LINE

        Imgproc.HuMoments(momento, hu);
        hu = hu.reshape(1,7);
        hu.convertTo(hu, CV_32F);

        Mat hus = new Mat(1,7, CV_32F);

        hus.put(0,0, Math.log10( Math.abs( hu.get(0,0)[0])) );
        hus.put(0,1, Math.log10( Math.abs( hu.get(1,0)[0])) );
        hus.put(0,2, Math.log10( Math.abs( hu.get(2,0)[0])) );
        hus.put(0,3, Math.log10( Math.abs( hu.get(3,0)[0])) );
        hus.put(0,4, Math.log10( Math.abs( hu.get(4,0)[0])) );
        hus.put(0,5, Math.log10( Math.abs( hu.get(5,0)[0])) );
        hus.put(0,6, Math.log10( Math.abs( hu.get(6,0)[0])) );

        int labels[] = new int[15];

        labels[0] = 7;
        labels[2] = 3;
        labels[4] = 1;
//        labels[6] = 4;
//        labels[8] = 5;
        labels[10] = 10;
        labels[11] = 9;
        labels[12] = 6;
        labels[13] = 2;
        labels[14] = 8;

        this._result = labels[(int)svm.predict(hus)];
    }
}
