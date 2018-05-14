package com.greatwall.papervolly.utils;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.greatwall.papervolly.model.Wallpaper;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class DbUtils {
    public static final String NATURE = "nature";
    public static final String PET = "pet";
    public static final String OTHERS = "others";
    public static final int NATURE_LENGTH = 100;
    public static final int PET_LENGTH = 100;
    public static final int OTHERS_LENGTH = 100;
    public static final String URL = "http://128.199.191.25:8080/?sign=cpx&type=6";

    //check if database is existing
    public static boolean checkDatabase(){
        return DataSupport.isExist(Wallpaper.class);
    }

    //parse json arrarylist using gson
    private static List<Wallpaper> parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        List<Wallpaper> wallpapers = gson.fromJson(jsonData, new TypeToken<List<Wallpaper>>(){}.getType());
        return wallpapers;
    }

    //save all wallpaper data
    public static void saveAllWallPaper(List<Wallpaper> wallpapers) {
        DataSupport.saveAll(wallpapers);
    }

    //get all wallpaper data
    public static List<Wallpaper> getAllWallPaper() {
        List<Wallpaper> wallpapers = DataSupport.findAll(Wallpaper.class);
        return  wallpapers;
    }

    //find wallpaper where star =true
    public static List<Wallpaper> getStarWallPaper() {
        List<Wallpaper> wallpapers = getAllWallPaper();
        List<Wallpaper> starpapers = new ArrayList<>();
        for (Wallpaper wallpaper: wallpapers){
            if(wallpaper.isStar()) {
                starpapers.add(wallpaper);
            }
        }
        return starpapers;
    }

    //update star
    public static void updateStar(int id, boolean isMine) {
        ContentValues values = new ContentValues();
        values.put("star", isMine);
        DataSupport.update(Wallpaper.class, values, id);
    }

    //get all the url by volley request
    public static void requestAllWallPaper(Context context){
        RequestQueue mQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Wallpaper> wallpapers = parseJSONWithGSON(response.toString());
                        //EventBus notification
                        EventBus.getDefault().post(wallpapers);
                        Log.d("DB", "onResponse: ");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //EventBus get no message
                EventBus.getDefault().post(null);
                Log.d("DB", "onErrorResponse: ");
            }
        });
        mQueue.add(jsonArrayRequest);
    }


    //get specific classification of wallpaper
    public static List<Wallpaper> getClassificationWallpapers(String classification){
        List<Wallpaper> wallpapers = getAllWallPaper();
        List<Wallpaper> classpapers = new ArrayList<>();
        for (Wallpaper wallpaper:wallpapers) {
            if(wallpaper.getClassification().equals(classification)){
                classpapers.add(wallpaper);
            }
        }
        return classpapers;
    }
}
