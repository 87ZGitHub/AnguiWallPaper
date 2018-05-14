package com.greatwall.papervolly.activity;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.greatwall.papervolly.R;
import com.greatwall.papervolly.adapter.ViewPagerAdapter;
import com.greatwall.papervolly.model.Wallpaper;
import com.greatwall.papervolly.utils.DbUtils;
import com.greatwall.papervolly.utils.FormatUtil;
import com.greatwall.papervolly.utils.UrlUtils;
import com.greatwall.papervolly.view.CircleProgressView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ShowActivity extends BaseActivity {

    private ViewPager viewPager;
    private View maskView;
    private CircleProgressView progressView;

    public static final String SHOW_WHAT = "show_what";
    public static final String POSITION = "position";
    public static final int SHOW_ALL = 0;
    public static final int SHOW_MY = 1;
    public static final int SHOW_NATURE = 2;
    public static final int SHOW_PET = 3;
    public static final int SHOW_OTHERS = 4;
    private List<Wallpaper> wallpapers = new ArrayList<>();
    private int position;
    private Timer timer;
    private int progress;
    private Handler handler = new Handler();

    public static void showActivityStart(Context context, int judge,int position) {
        Intent intent = new Intent(context,ShowActivity.class);
        intent.putExtra(SHOW_WHAT,judge);
        intent.putExtra(POSITION,position);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent intent = getIntent();
        switch (intent.getIntExtra(SHOW_WHAT,SHOW_ALL)) {
            case SHOW_ALL:
                wallpapers = DbUtils.getAllWallPaper();
                break;
            case SHOW_MY:
                wallpapers = DbUtils.getStarWallPaper();
                break;
        }
        position = intent.getIntExtra(POSITION,0);
        viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(wallpapers);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

        viewPager.setOffscreenPageLimit(3);

        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                final float normalizedPostition = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalizedPostition / 2 + 0.6f);
                page.setScaleY(normalizedPostition / 2 + 0.6f);
            }
        });
        Button btnSet = findViewById(R.id.set_wallpaper);
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                position = viewPager.getCurrentItem();
                setWallpaper(position);
            }
        });

    }

    private void showProgress() {
        progressView = findViewById(R.id.progress);
        maskView = findViewById(R.id.mask_view);
        progressView.setVisibility(View.VISIBLE);
        maskView.setVisibility(View.VISIBLE);
        progress = 0;
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressView.setProgress(progress);
                        if(progress < 100){
                            progress++;
                        }
                    }
                });
            }
        };
        timer.schedule(task,0,180);
    }

    private void setWallpaper(int position) {

        Wallpaper wallpaper = wallpapers.get(position);

        String urlHD = UrlUtils.getWallpaperHDUrl(wallpaper.getUrlWallpaper());

        Glide.with(this).load(urlHD).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Toast.makeText(ShowActivity.this, "Fail!Please try again...", Toast.LENGTH_SHORT).show();
                progressView.setVisibility(View.GONE);
                maskView.setVisibility(View.GONE);
                if(timer!=null){
                    timer.cancel();
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Bitmap bitmap = FormatUtil.getInstance().drawable2Bitmap(resource);
                if(bitmap != null){
                    WallpaperManager manager = WallpaperManager.getInstance(ShowActivity.this);
                    try {
                        manager.setBitmap(bitmap);
                        Toast.makeText(ShowActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(ShowActivity.this,"Fail ! Please try again...",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } finally {
                        progressView.setVisibility(View.GONE);
                        maskView.setVisibility(View.GONE);
                        if(timer!=null){
                            timer.cancel();
                        }
                    }
                }
                return false;
            }
        }).preload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
        }
    }
}
