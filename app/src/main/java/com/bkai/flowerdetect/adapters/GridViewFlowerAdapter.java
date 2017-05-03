package com.bkai.flowerdetect.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bkai.flowerdetect.R;
import com.bkai.flowerdetect.helpers.MyHelper;
import com.bkai.flowerdetect.models.Flower;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by marsch on 4/17/17.
 */


public class GridViewFlowerAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private List<Flower> data;

    public GridViewFlowerAdapter(Context context, int layoutResourceId, List<Flower> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.flower_image = (ImageView) row.findViewById(R.id.img_view);
            holder.flower_name = (TextView) row.findViewById(R.id.flower_name);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Flower item = (Flower) data.get(position);
        holder.flower_name.setText(item.getName());

        String name = null;

        try {
            name = getImage(context.getApplicationContext()).get(item.getId() == 10 ? 0 : item.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }

        name = "flowers/" + name;

        Bitmap bitmap = MyHelper.getBitmapFromAsset(context.getApplicationContext(), name);

        holder.flower_image.setImageBitmap(bitmap);
        return row;
    }

    static class ViewHolder {
        ImageView flower_image;
        TextView flower_name;
    }

    private List<String> getImage(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();

        String[] files = assetManager.list("flowers");
        List<String> listImage = Arrays.asList(files);

        return listImage;
    }
}