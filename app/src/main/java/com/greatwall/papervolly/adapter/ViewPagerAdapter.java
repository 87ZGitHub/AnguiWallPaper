package com.greatwall.papervolly.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.greatwall.papervolly.R;
import com.greatwall.papervolly.model.Wallpaper;
import com.greatwall.papervolly.utils.DbUtils;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ViewPagerAdapter extends android.support.v4.view.PagerAdapter {
    private List<Wallpaper> wallpapers = new ArrayList<>();
    private Context context;
    private RequestOptions options;

    public ViewPagerAdapter(List<Wallpaper> wallpapers){
        this.wallpapers = wallpapers;
        this.options = new RequestOptions().centerCrop().transform(new RoundedCornersTransformation(20,0)).placeholder(R.mipmap.loading);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        context = container.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_set_wallpaper,null);
        ImageView ivWallpaper = view.findViewById(R.id.iv_wallpaper);
        final ImageView ivLike = view.findViewById(R.id.iv_like);
        final boolean isMine = wallpapers.get(position).isStar();

        if(isMine){
            ivLike.setImageResource(R.mipmap.ic_liked);
        }
        Glide.with(context)
                .load(wallpapers.get(position).getUrlWallpaper())
                .apply(options)
                .into(ivWallpaper);

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Wallpaper wallpaper = wallpapers.get(position);
                boolean isMine = wallpaper.isStar();
                if(isMine){
                    ivLike.setImageResource(R.mipmap.ic_like);
                }else{
                    ivLike.setImageResource(R.mipmap.ic_liked);
                }
                wallpapers.get(position).setStar(!isMine);
                DbUtils.updateStar(wallpaper.getId(),!isMine);
            }
        });
        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public float getPageWidth(int position) {
        return 0.85f;
    }

    @Override
    public int getCount() {
        return wallpapers.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
