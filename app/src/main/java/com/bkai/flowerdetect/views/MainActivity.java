package com.bkai.flowerdetect.views;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.bkai.flowerdetect.R;
import com.bkai.flowerdetect.adapters.FlowerRecyclerViewAdapter;
import com.bkai.flowerdetect.database.DBHelper;
import com.bkai.flowerdetect.models.Flower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton openCamera;
    private List<Flower> mFlowerList;
    private RecyclerView mRecyclerView;
    private FlowerRecyclerViewAdapter mAdapter;
    Toolbar toolbar;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        openCamera = (FloatingActionButton) findViewById(R.id.openCamera);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openCamera = new Intent(getApplicationContext(), DetectActivity.class);
                startActivity(openCamera);
            }
        });
        setupToolBar();
        initComponent();
    }
    void setupToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(4);
        }
        setSupportActionBar(toolbar);
    }
    void initComponent(){

        db = new DBHelper(getApplicationContext());

        mFlowerList = db.getAllFowers();

        mRecyclerView = (RecyclerView) findViewById(R.id.listFlower);
        mAdapter = new FlowerRecyclerViewAdapter(this,mFlowerList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new FlowerRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent flowerDetail = new Intent(getApplicationContext(), FlowerDetail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("flower", mFlowerList.get(position));
                flowerDetail.putExtra("flower_package", bundle);
                startActivity(flowerDetail);
            }
        });
    }
}
