package com.bkai.flowerdetect.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bkai.flowerdetect.R;
import com.bkai.flowerdetect.adapters.FlowerRecyclerViewAdapter;
import com.bkai.flowerdetect.database.DBHelper;
import com.bkai.flowerdetect.models.Flower;
import com.github.clans.fab.FloatingActionButton;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static {
        if(!OpenCVLoader.initDebug())
        {
            Log.e("OpenCv","Init Fail");
        } else {
            Log.e("OpenCv","Init Successful");
        }
    }
    FloatingActionButton openCamera;
    private List<Flower> mFlowerList;
    private RecyclerView mRecyclerView;
    private FlowerRecyclerViewAdapter mAdapter;
    Toolbar toolbar;
    DBHelper db;
    private static int IMG_RESULT = 1;

    FloatingActionButton fab_gallery;
    FloatingActionButton fab_camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        fab_camera = (FloatingActionButton) findViewById(R.id.fab_camera);
        fab_gallery = (FloatingActionButton) findViewById(R.id.fab_gallery);
        fab_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openCamera = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(openCamera);
            }
        });

        fab_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), IMG_RESULT);

//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, IMG_RESULT);
            }
        });
        setupToolBar();
        initComponent();
        checkPermission();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == IMG_RESULT && resultCode == RESULT_OK
                    && null != data) {
                Uri uri = data.getData();

                Intent showPicture = new Intent(this, PicturePreview.class);
                String file_path = getPath(uri);
                showPicture.putExtra("img_path", file_path);
                showPicture.putExtra("img_uri", uri.toString());

                startActivity(showPicture);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public String getPathFromUri(Uri uri)
    {
        String[] projection = {  };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public String getPath(Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    String file_path = cursor.getString(column_index);
                    return file_path;
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    void setupToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(4);
        }
        setSupportActionBar(toolbar);
    }
    private void checkPermission(){
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    123);
            return;
        }
    }
    void initComponent(){

        db = new DBHelper(getApplicationContext());

        mFlowerList = db.getAllFowers();

        mRecyclerView = (RecyclerView) findViewById(R.id.listFlower);
        mAdapter = new FlowerRecyclerViewAdapter(this,mFlowerList, null);

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
