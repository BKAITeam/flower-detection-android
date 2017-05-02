package com.bkai.flowerdetect.helpers;

import android.graphics.Bitmap;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * Created by marsch on 5/2/17.
 */

public class MyHelper {
    public static Mat bitmapToMat(Bitmap img){
        Mat mat = new Mat(img.getWidth(), img.getHeight(), CvType.CV_8UC3);
        org.opencv.android.Utils.bitmapToMat(img, mat);
        return mat;
    }
}
