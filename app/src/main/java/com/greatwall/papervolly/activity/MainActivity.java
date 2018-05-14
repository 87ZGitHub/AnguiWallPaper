package com.greatwall.papervolly.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.greatwall.papervolly.R;
import com.greatwall.papervolly.fragment.AllWallpapersFragment;
import com.greatwall.papervolly.fragment.MyWallpaperFragment;
import com.greatwall.papervolly.model.Wallpaper;
import com.greatwall.papervolly.utils.DbUtils;
import com.greatwall.papervolly.view.CircleProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends BaseActivity {


    private ViewPager mViewPager;
    private View maskView;
    private CircleProgressView cProgressView;

    private ArrayList<Fragment> mViewList = new ArrayList<>();
    private Timer timer;
    private int progress;
    private Handler handler = new Handler();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectListerner
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(1);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showProgress();
        if(!DbUtils.checkDatabase()){
            EventBus.getDefault().register(this);
            DbUtils.requestAllWallPaper(this);
        }else{
            initData();
        }

    }

    //处理EventBus获取到的壁纸数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(List<Wallpaper> wallpapers) {
        if(wallpapers!=null){
            Log.d("!!!!", "onEvent: " + wallpapers.toString());
            DbUtils.saveAllWallPaper(wallpapers);
            initData();
        }
    }

    private void initData() {
        View maskView = findViewById(R.id.mask_view);
        maskView.setVisibility(View.GONE);
        cProgressView.setVisibility(View.GONE);
        if(timer!=null){
            timer.cancel();
        }

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectListerner);

        mViewPager = findViewById(R.id.view_pager);
        if(mViewList.size() == 0){
            mViewList.add(new AllWallpapersFragment());
            mViewList.add(new MyWallpaperFragment());
        }
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mViewList.get(position);
            }

            @Override
            public int getCount() {
                return mViewList.size();
            }
        });
    }

    //显示进度条
    private void showProgress() {
        cProgressView = findViewById(R.id.progress);
        progress = 0;
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        cProgressView.setProgress(progress);
                        if(progress < 100){
                            progress++;
                        }
                    }
                });
            }
        };
        timer.schedule(task,0,50);
    }

    //双击退出程序
    private long firstTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis() - firstTime > 2000){
                Toast.makeText(MainActivity.this,"Press again to exit the program.",Toast.LENGTH_SHORT).show();
                firstTime=System.currentTimeMillis();
            }else{
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if(timer!=null){
            timer.cancel();
        }
    }
}
