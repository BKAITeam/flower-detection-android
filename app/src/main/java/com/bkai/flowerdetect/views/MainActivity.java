package com.bkai.flowerdetect.views;

import android.content.Intent;
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

        initComponent();
    }

    void initComponent(){

        db = new DBHelper(getApplicationContext());

        Log.e("Size of Flower", String.valueOf(db.getAllFowers().size()));

        mFlowerList = db.getAllFowers();

//        mFlowerList = new ArrayList<Flower>(Arrays.asList(
//                new Flower("Hoa Thiên Điểu", "Đây là hoa Thiên Điểu"),
//                new Flower("Hoa Hướng Dương", "Đây là Hoa Hướng Dương"),
//                new Flower("Hoa Lyly", "Đây là Hoa Lyly"),
//                new Flower("Hoa Cẩm Tú Cầu", "Đây là Hoa Cẩm Tú Cầu"),
//                new Flower("Hoa Cúc Trắng", "Đây là Hoa Cúc Trắng"),
//                new Flower("Hoa Mào Gà", "Đây là Hoa Mào Gà"),
//                new Flower("Hoa Rum", "Đây là Hoa Rum"),
//                new Flower("Hoa Vạn Thọ", "Đây là Hoa Vạn Thọ"),
//                new Flower("Hoa Thược Dược", "Đây là Hoa Thược Dược"),
//                new Flower("Hoa Sen", "Đây là Hoa Sen")));
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
