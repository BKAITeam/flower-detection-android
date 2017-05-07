package com.bkai.flowerdetect.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.bkai.flowerdetect.R;
import com.bkai.flowerdetect.adapters.FlowerRecyclerViewAdapter;
import com.bkai.flowerdetect.adapters.GridViewFlowerAdapter;
import com.bkai.flowerdetect.adapters.GridViewKmeanAdapter;
import com.bkai.flowerdetect.database.DBHelper;
import com.bkai.flowerdetect.logic.Prediction;
import com.bkai.flowerdetect.models.Flower;
import com.github.clans.fab.FloatingActionButton;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

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
    MultiStateToggleButton lang_switch;

    FloatingActionButton fab_gallery;
    FloatingActionButton fab_camera;

    GridViewFlowerAdapter mGridViewPictureAdapter;
    GridView gridView;

    Locale currentLocate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Locale locale = new Locale("vi");
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        getResources().updateConfiguration(config,getResources().getDisplayMetrics());

        currentLocate = getResources().getConfiguration().locale;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(4);
        }
    }

    void setUGridView(){

        gridView = (GridView) findViewById(R.id.grid_flower_list);

        mGridViewPictureAdapter = new GridViewFlowerAdapter(this, R.layout.grid_flower_view, mFlowerList);
        gridView.setAdapter(mGridViewPictureAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent flowerDetail = new Intent(getApplicationContext(), FlowerDetail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("flower", mFlowerList.get(position));
                flowerDetail.putExtra("flower_package", bundle);
                startActivity(flowerDetail);
            }
        });
    }

    private void checkPermission(){
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET},
                    123);
            return;
        }
    }

    void initComponent(){
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
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), IMG_RESULT);
            }
        });
        db = new DBHelper(getApplicationContext());

        mFlowerList = db.getAllFowers();

        setUGridView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lang, menu);
        lang_switch = (MultiStateToggleButton) menu.findItem(R.id.switch_lang_menu_item).getActionView().findViewById(R.id.mstb_lang);

        if (currentLocate.toString().equals("vi")){
            lang_switch.setValue(0);
        } else {
            lang_switch.setValue(1);
        }

        lang_switch.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                String lang[] = new String[2];
                lang[0] ="vi";
                lang[1] ="en";

                Locale locale = new Locale(lang[value]);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getResources().updateConfiguration(config,getResources().getDisplayMetrics());
                RestartActivity();
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.switch_lang_menu_item:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void RestartActivity(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
