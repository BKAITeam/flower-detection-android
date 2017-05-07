package com.bkai.flowerdetect.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bkai.flowerdetect.R;
import com.bkai.flowerdetect.models.Flower;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Result_Dialog extends AppCompatActivity {
    TextView tv_result;
    Flower mFlower;
    CircleImageView flowerImage;

    Button btn_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_dialog);

        tv_result = (TextView) findViewById(R.id.tv_result);
        flowerImage = (CircleImageView) findViewById(R.id.flowerImage);
        btn_detail = (Button) findViewById(R.id.btn_detail);



        Intent intent = getIntent();
        mFlower = (Flower) intent.getBundleExtra("flower_package").getSerializable("flower");

        if (getResources().getConfiguration().locale.toString().equals("vi")){
            tv_result.setText(mFlower.getName());
        } else {
            tv_result.setText(mFlower.getEngName());
        }

        btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent flowerDetail = new Intent(getApplicationContext(), FlowerDetail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("flower", mFlower);
                flowerDetail.putExtra("flower_package", bundle);
                startActivity(flowerDetail);
            }
        });

        String name = null;

        try {
            name = getImage(getApplicationContext()).get(mFlower.getId() == 10 ? 0 : mFlower.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }

        name = "flowers/" + name;

        Log.e("File name", name);
        Bitmap bitmap = getBitmapFromAsset(getApplicationContext(), name);

        flowerImage.setImageBitmap(bitmap);
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

    private List<String> getImage(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();

        String[] files = assetManager.list("flowers");
        List<String> listImage = Arrays.asList(files);

        return listImage;
    }
}
