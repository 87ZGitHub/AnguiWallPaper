package com.greatwall.papervolly.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.greatwall.papervolly.R;
import com.greatwall.papervolly.activity.ShowActivity;
import com.greatwall.papervolly.model.Wallpaper;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class WallpapersAdapter extends RecyclerView.Adapter<WallpapersAdapter.mViewHolder> {
    private List<Wallpaper> wallpapers = new ArrayList<>();
    private Context context;
    private RequestOptions options;
    private int judge;

    public WallpapersAdapter(List<Wallpaper> wallpapers, int judge){
        this.wallpapers = wallpapers;
        this.options = new RequestOptions().transform(new RoundedCornersTransformation(20,0)).placeholder(R.mipmap.loading);
        this.judge = judge;
    }

    public class mViewHolder extends RecyclerView.ViewHolder {
        ImageView ivWallpaper;
        public mViewHolder(View itemView) {
            super(itemView);
            ivWallpaper = itemView.findViewById(R.id.iv_wallpaper);
        }
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_wallpaper,parent,false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, final int position) {
        Glide.with(context).load(wallpapers.get(position).getUrlWallpaper()).apply(options).into(holder.ivWallpaper);
        holder.ivWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity.showActivityStart(context, judge, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return wallpapers.size();
    }
}
