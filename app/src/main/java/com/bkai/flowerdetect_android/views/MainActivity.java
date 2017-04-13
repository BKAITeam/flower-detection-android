package com.bkai.flowerdetect_android.views;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.bkai.flowerdetect_android.R;
import com.bkai.flowerdetect_android.adapters.FlowerRecyclerViewAdapter;
import com.bkai.flowerdetect_android.models.Flower;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton openCamera;
    private List<Flower> mFlowerList;
    private RecyclerView mRecyclerView;
    private FlowerRecyclerViewAdapter mAdapter;
    Toolbar toolbar;

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

        initComponent();
    }

    void initComponent(){
        mFlowerList = new ArrayList<Flower>(Arrays.asList(
                new Flower("Hoa Thiên Điểu", "Đây là hoa thiên điểu"),
                new Flower("Hoa Mào gà", "Đây là hoa mào gà"),
                new Flower("Hoa Sen", "Đây là hoa sen")));
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
