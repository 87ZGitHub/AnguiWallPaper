package com.greatwall.papervolly.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greatwall.papervolly.R;
import com.greatwall.papervolly.adapter.WallpapersAdapter;
import com.greatwall.papervolly.model.Wallpaper;
import com.greatwall.papervolly.utils.DbUtils;

import java.util.List;

import static com.greatwall.papervolly.activity.ShowActivity.SHOW_MY;

public class MyWallpaperFragment extends android.support.v4.app.Fragment{

    private View rootView;
    private RecyclerView rvPics;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_wallpaper,container,false);
        return rootView;
    }

    @Override
    public void onResume() {
        initUi();
        super.onResume();
    }

    private void initUi() {
        TextView empty = rootView.findViewById(R.id.empty);
        rvPics = rootView.findViewById(R.id.rv_pics);
        List<Wallpaper> myWallpapers = DbUtils.getStarWallPaper();
        if(myWallpapers.size() == 0){
            empty.setVisibility(View.VISIBLE);
            rvPics.setVisibility(View.GONE);
        }else{
            rvPics.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
            rvPics.setLayoutManager(new GridLayoutManager(getContext(),3));
            WallpapersAdapter adapter = new WallpapersAdapter(myWallpapers,SHOW_MY);
            rvPics.setAdapter(adapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
