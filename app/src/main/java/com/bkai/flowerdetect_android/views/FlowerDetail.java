package com.bkai.flowerdetect_android.views;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bkai.flowerdetect_android.R;
import com.bkai.flowerdetect_android.models.Flower;

public class FlowerDetail extends AppCompatActivity {

    ImageView flowImage;
    TextView flower_name;
    TextView flower_description;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_detail);

        setupToolBar();

        initComponent();
    }
    void setupToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        //        toolbar.setBackgroundColor(Color.parseColor("#873FB5A9"));
        //        toolbar.setTitleTextColor(Color.BLACK);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    void initComponent(){
        flower_name = (TextView) findViewById(R.id.flower_name);
        flower_description = (TextView) findViewById(R.id.flower_description);

        Intent intent = getIntent();
        Flower mFlower = (Flower) intent.getBundleExtra("flower_package").getSerializable("flower");

        getSupportActionBar().setTitle(mFlower.getName());

        flower_name.setText(mFlower.getName());
        flower_description.setText(mFlower.getDescription());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
