package com.greatwall.papervolly.utils;

import static com.greatwall.papervolly.utils.DbUtils.NATURE;
import static com.greatwall.papervolly.utils.DbUtils.PET;
import static com.greatwall.papervolly.utils.DbUtils.OTHERS;
import static com.greatwall.papervolly.utils.DbUtils.NATURE_LENGTH;
import static com.greatwall.papervolly.utils.DbUtils.PET_LENGTH;
import static com.greatwall.papervolly.utils.DbUtils.getAllWallPaper;

public class UrlUtils {

    public static String getWallpapaerUrl(String classification, int position){
        if(getAllWallPaper().isEmpty()){
            return "";
        }
        int i = 0;
        switch (classification){
            case NATURE:
                break;
            case PET:
                i = NATURE_LENGTH;
                break;
            case OTHERS:
                i = NATURE_LENGTH+PET_LENGTH;
                break;
        }
        i += position+1;
        return getAllWallPaper().get(i).getUrlWallpaper();
    }

    public static String getWallpaperHDUrl(String wallpaperUrl){
        int last = wallpaperUrl.lastIndexOf('/');
        String HDUrl = wallpaperUrl.substring(0,last) + "_hd" + wallpaperUrl.substring(last);
        return HDUrl;
    }
}
