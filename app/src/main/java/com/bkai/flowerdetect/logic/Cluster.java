package com.bkai.flowerdetect.logic;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marsch on 4/13/17.
 */

public class Cluster extends Thread {
    private Handler _handler;
    private List<Mat> clusters;
    private Mat cutout;
    private int k;

    public Cluster(Handler handler, Mat cutout, int k){
        this._handler = handler;
        this.cutout = cutout;
        this. k = k;

    }

    public List<Mat> getClusters() {
        return clusters;
    }

    @Override
    public void run() {
        super.run();
        this.clusters = cluster(this.cutout, this.k);
        Bundle bundle = new Bundle();
        bundle.putString("cluster", "done");
        Message msg = new Message();
        msg.setData(bundle);

        this._handler.sendMessage(msg);
    }

    public List<Mat> cluster(Mat cutout, int k) {
        Imgproc.resize(cutout, cutout, new Size( (int)(cutout.cols()*0.2), (int)(cutout.rows()*0.2) ));

        Mat samples = cutout.reshape(1, cutout.cols() * cutout.rows());
        Mat samples32f = new Mat();
        samples.convertTo(samples32f, CvType.CV_32F, 1.0 / 255.0);

        Mat labels = new Mat();
        TermCriteria criteria = new TermCriteria(TermCriteria.COUNT, 100, 1);
        Mat centers = new Mat();
        Core.kmeans(samples32f, k, labels, criteria, 1, Core.KMEANS_PP_CENTERS, centers);

        return showClusters(cutout, labels, centers);
    }

    private List<Mat> showClusters (Mat cutout, Mat labels, Mat centers) {
        centers.convertTo(centers, CvType.CV_8UC1, 255.0);
        centers.reshape(3);

        List<Mat> clusters = new ArrayList<Mat>();
        for(int i = 0; i < centers.rows(); i++) {
            clusters.add(Mat.zeros(cutout.size(), cutout.type()));
        }

        Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
        for(int i = 0; i < centers.rows(); i++) counts.put(i, 0);

        int rows = 0;
        for(int y = 0; y < cutout.rows(); y++) {
            for(int x = 0; x < cutout.cols(); x++) {
                int label = (int)labels.get(rows, 0)[0];
                int r = (int)centers.get(label, 2)[0];
                int g = (int)centers.get(label, 1)[0];
                int b = (int)centers.get(label, 0)[0];
                counts.put(label, counts.get(label) + 1);
                clusters.get(label).put(y, x, cutout.get(y,x));
//                clusters.get(label).put(y, x, r, g, b);
                rows++;
            }
        }
        return clusters;
    }
}
