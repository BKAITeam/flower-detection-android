package com.bkai.flowerdetect.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bkai.flowerdetect.R;
import com.bkai.flowerdetect.models.Flower;

import java.util.List;

/**
 * Created by marsch on 4/14/17.
 */

class FlowerMyholder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView flower_name;
    TextView flower_description;
    FlowerRecyclerViewAdapter.OnItemClickListener mItemClickListener;

    public FlowerMyholder(View view, FlowerRecyclerViewAdapter.OnItemClickListener mItemClickListener){
        super(view);
        flower_name = (TextView) view.findViewById(R.id.flower_name);
        flower_description = (TextView) view.findViewById(R.id.flower_description);
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

    public FlowerRecyclerViewAdapter(Context context, List<Flower> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public FlowerMyholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.flower_card_view,parent,false);

        return new FlowerMyholder(item, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(FlowerMyholder holder, int position) {
        final Flower item = list.get(position);

        holder.flower_name.setText(item.getName());
        holder.flower_description.setText(Html.fromHtml(item.getDescription()));
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
