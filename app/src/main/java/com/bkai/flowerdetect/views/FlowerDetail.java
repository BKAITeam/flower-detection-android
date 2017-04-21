package com.bkai.flowerdetect.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bkai.flowerdetect.R;
import com.bkai.flowerdetect.models.Flower;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class FlowerDetail extends AppCompatActivity {

    ImageView flowImage;
    TextView flower_name;
    TextView flower_science_name;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(4);
        }
        setSupportActionBar(toolbar);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void initComponent(){
        flowImage = (ImageView) findViewById(R.id.img_view);
        flower_name = (TextView) findViewById(R.id.flower_name);
        flower_science_name = (TextView) findViewById(R.id.flower_science_name);
        flower_description = (TextView) findViewById(R.id.flower_description);

        Intent intent = getIntent();
        Flower mFlower = (Flower) intent.getBundleExtra("flower_package").getSerializable("flower");

        getSupportActionBar().setTitle(mFlower.getName());

        String name = null;

        try {
            name = getImage(getApplicationContext()).get(mFlower.getId() == 10 ? 0 : mFlower.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }

        name = "flowers/" + name;

        Log.e("File name", name);
        Bitmap bitmap = getBitmapFromAsset(getApplicationContext(), name);

        flowImage.setImageBitmap(bitmap);

        flower_name.setText(mFlower.getName());
        flower_science_name.setText(mFlower.getScienceName());
        flower_description.setText(Html.fromHtml(mFlower.getDescription()));
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private List<String> getImage(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();

        String[] files = assetManager.list("flowers");
        List<String> listImage = Arrays.asList(files);

        return listImage;
    }
}
