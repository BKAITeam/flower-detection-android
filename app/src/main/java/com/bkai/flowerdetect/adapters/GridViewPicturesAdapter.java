package com.bkai.flowerdetect.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bkai.flowerdetect.R;

import java.util.ArrayList;

/**
 * Created by marsch on 4/17/17.
 */


public class GridViewPicturesAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList data = new ArrayList();

    public GridViewPicturesAdapter(Context context, int layoutResourceId, ArrayList data) {
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
            holder.image = (ImageView) row.findViewById(R.id.img_view);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Uri item = (Uri) data.get(position);
        holder.image.setImageURI(item);
        return row;
    }

    static class ViewHolder {
        ImageView image;
    }
}