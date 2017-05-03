package com.bkai.flowerdetect.adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bkai.flowerdetect.R;
import com.bkai.flowerdetect.helpers.MyHelper;
import com.bkai.flowerdetect.models.Flower;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by marsch on 4/14/17.
 */

class FlowerMyholder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView flower_name;
    TextView flower_description;
    ImageView flower_image;
    FlowerRecyclerViewAdapter.OnItemClickListener mItemClickListener;

    public FlowerMyholder(View view, FlowerRecyclerViewAdapter.OnItemClickListener mItemClickListener){
        super(view);
        flower_name = (TextView) view.findViewById(R.id.flower_name);
        flower_description = (TextView) view.findViewById(R.id.flower_description);
        flower_image = (ImageView) view.findViewById(R.id.flower_image);
        this.mItemClickListener = mItemClickListener;

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(v, getPosition());
        }
    }
}

public class FlowerRecyclerViewAdapter extends RecyclerView.Adapter<FlowerMyholder>{
    Context context;
    List<Flower> list;

    OnItemClickListener mItemClickListener;

    public FlowerRecyclerViewAdapter(Context context, List<Flower> list, List img){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public FlowerMyholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_flower,parent,false);

        return new FlowerMyholder(item, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(FlowerMyholder holder, int position) {
        final Flower item = list.get(position);

        holder.flower_name.setText(item.getName());
        holder.flower_description.setText(Html.fromHtml(item.getDescription()));

        String name = null;

        try {
            name = getImage(context.getApplicationContext()).get(item.getId() == 10 ? 0 : item.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }

        name = "flowers/" + name;

        Bitmap bitmap = MyHelper.getBitmapFromAsset(context.getApplicationContext(), name);

        holder.flower_image.setImageBitmap(bitmap);
    }


    private List<String> getImage(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();

        String[] files = assetManager.list("flowers");
        List<String> listImage = Arrays.asList(files);

        return listImage;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
