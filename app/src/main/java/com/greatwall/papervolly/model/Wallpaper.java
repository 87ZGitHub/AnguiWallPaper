package com.greatwall.papervolly.model;

import org.litepal.crud.DataSupport;

public class Wallpaper extends DataSupport {
    private int id;
    private String urlWallpaper;
    private boolean star;
    private String classification;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrlWallpaper() {
        return urlWallpaper;
    }

    public void setUrlWallpaper(String urlWallpaper) {
        this.urlWallpaper = urlWallpaper;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @Override
    public String toString() {
        return "Wallpaper{" +
                "id=" + id +
                ", urlWallpaper='" + urlWallpaper + '\'' +
                ", star=" + star +
                ", classification='" + classification + '\'' +
                '}';
    }
}
