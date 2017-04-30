package com.bkai.flowerdetect.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.ml.SVM;

import java.io.File;
import java.util.Random;

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
        long sum = 0;

//        for(int i=0;i< 700000000; i++){
//            sum += 1;
//        }

//        Random rand = new Random();
//        this.result = String.valueOf(rand.nextInt(10)+1);
        File svm_file = new File("/data/user/0/com.bkai.flowerdetect/databases/svm.dat");
        SVM svm = SVM.load("/data/user/0/com.bkai.flowerdetect/databases/svm.dat");

        Mat mat = new Mat(this._img.getWidth(), this._img.getHeight(), CvType.CV_8SC3);
        Utils.bitmapToMat(this._img, mat);

        this._result = svm.predict(mat);
    }
}
