package com.bkai.flowerdetect.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bkai.flowerdetect.R;
import com.bkai.flowerdetect.adapters.ViewPagerAdapter;
import com.bkai.flowerdetect.helpers.SlidingTabLayout;
import com.bkai.flowerdetect.models.Flower;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FlowerDetail extends AppCompatActivity implements TextToSpeech.OnInitListener{

    ImageView flowImage;
    ImageView flower_Speaker_Eng;
    ImageView flower_Speaker_Vi;
    TextView flower_name;
    TextView flower_eng_name;
    TextView flower_description;
    TextToSpeech speech;

    ViewPager pager;
    ViewPagerAdapter pagerAdapter;
    SlidingTabLayout tabs;
    CharSequence Titiles []={"Cổ tích","Khoa học"};
    TabLayout tabLayout ;

    Toolbar toolbar;

    Flower mFlower;

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

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_left);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void initComponent(){
        flowImage = (ImageView) findViewById(R.id.img_view);
        flower_Speaker_Eng = (ImageView) findViewById(R.id.img_speaker_eng);
        flower_Speaker_Vi = (ImageView) findViewById(R.id.img_speaker_vi);
//        flower_name = (TextView) findViewById(R.id.flower_name);
        flower_eng_name = (TextView) findViewById(R.id.flower_eng_name);
//        flower_description = (TextView) findViewById(R.id.flower_description);

        Intent intent = getIntent();
        mFlower = (Flower) intent.getBundleExtra("flower_package").getSerializable("flower");

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

//        flower_name.setText(mFlower.getName());
        flower_eng_name.setText(mFlower.getEngName());
//        flower_description.setText(Html.fromHtml(mFlower.getDescription()));
        speech = new TextToSpeech(this, this);
        flower_Speaker_Eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakOut();
            }
        });

        flower_Speaker_Vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakOutVi();
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.addTab(tabLayout.newTab().setText("Cổ tích"));
        tabLayout.addTab(tabLayout.newTab().setText("Khoa học"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        pager = (ViewPager) findViewById(R.id.pager);

        pagerAdapter = new ViewPagerAdapter((getSupportFragmentManager()), 2);

        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = speech.setLanguage(Locale.UK);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
//                flower_Speaker_Eng.setEnabled(true);
//                flower_Speaker_Vi.setEnabled(true);
//                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut() {

        speech.setLanguage(Locale.UK);

        String text = mFlower.getEngName();

        speech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void speakOutVi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            speech.setLanguage(Locale.forLanguageTag("vi"));

            String text = mFlower.getName();

            speech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }

    }

    @Override
    protected void onDestroy() {
        if(speech !=null){
            speech.stop();
            speech.shutdown();
        }
        super.onDestroy();
    }
}
