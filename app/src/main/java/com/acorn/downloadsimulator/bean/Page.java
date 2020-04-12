package com.acorn.downloadsimulator.bean;

import androidx.annotation.NonNull;

/**
 * Created by acorn on 2020/4/11.
 */
public class Page {
    private String url;
    private String pageName;
    private String chapterName;
    //假装自己是bitmap
    private String bitmap;

    public Page(String url, String pageName, String chapterName) {
        this.url = url;
        this.pageName = pageName;
        this.chapterName = chapterName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    @NonNull
    @Override
    public String toString() {
        return url;
    }
}
