package com.greatwall.papervolly.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greatwall.papervolly.R;
import com.greatwall.papervolly.adapter.WallpapersAdapter;
import com.greatwall.papervolly.model.Wallpaper;
import com.greatwall.papervolly.utils.DbUtils;

import java.util.List;

import static com.greatwall.papervolly.activity.ShowActivity.SHOW_ALL;

public class AllWallpapersFragment extends Fragment {

    private View rootView;
    private RecyclerView rvPics;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_all_wallpapers, container, false);
        initUI();
        return rootView;
    }

    private void initUI() {
        rvPics = rootView.findViewById(R.id.rv_pics);
        rvPics.setLayoutManager(new GridLayoutManager(getContext(),3));
        List<Wallpaper> wallpapers = DbUtils.getAllWallPaper();
        WallpapersAdapter adapter = new WallpapersAdapter(wallpapers,SHOW_ALL);
        rvPics.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
