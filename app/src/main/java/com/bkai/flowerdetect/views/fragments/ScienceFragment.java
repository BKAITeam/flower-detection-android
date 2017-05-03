package com.bkai.flowerdetect.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bkai.flowerdetect.R;
import com.bkai.flowerdetect.models.Flower;

/**
 * Created by marsch on 5/4/17.
 */

public class ScienceFragment extends Fragment {
    TextView tv_science;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.science_fragment, container, false);
        InitComponent(root);
        return root;
    }

    private void InitComponent(View root){
        Intent intent = getActivity().getIntent();
        Flower mFlower = (Flower) intent.getBundleExtra("flower_package").getSerializable("flower");
        tv_science = (TextView) root.findViewById(R.id.tv_science);
        tv_science.setText(Html.fromHtml(mFlower.getDescription()));
    }
}