package com.bkai.flowerdetect.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.bkai.flowerdetect.R;
import com.bkai.flowerdetect.models.Flower;

import java.util.Locale;

/**
 * Created by marsch on 5/4/17.
 */

public class CoTichFragment extends Fragment {
    TextView tc_cotich;
    WebView wv_cotich;
    Locale currentLocate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentLocate = getResources().getConfiguration().locale;
        View root = inflater.inflate(R.layout.cotich_fragment, container, false);
        InitComponent(root);
        return root;
    }

    private void InitComponent(View root){
        Intent intent = getActivity().getIntent();
        Flower mFlower = (Flower) intent.getBundleExtra("flower_package").getSerializable("flower");
        wv_cotich = (WebView) root.findViewById(R.id.webview_cotich);

        if (currentLocate.toString().equals("vi")){
            wv_cotich.loadDataWithBaseURL(null, mFlower.getCotich(),"text/html", "UTF-8", null);
        } else {
            wv_cotich.loadDataWithBaseURL(null, mFlower.getCotich_eng(),"text/html", "UTF-8", null);
        }
    }
}
