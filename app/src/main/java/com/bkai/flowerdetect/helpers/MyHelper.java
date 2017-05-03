package com.bkai.flowerdetect.helpers;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by marsch on 5/2/17.
 */

public class MyHelper {
    public static Mat bitmapToMat(Bitmap img){
        Mat mat = new Mat(img.getWidth(), img.getHeight(), CvType.CV_8UC3);
        org.opencv.android.Utils.bitmapToMat(img, mat);
        return mat;
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }
}
