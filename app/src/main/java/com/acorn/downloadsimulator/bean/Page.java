package com.acorn.downloadsimulator.bean;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

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

    //失败次数
    private final AtomicInteger failTimes = new AtomicInteger();
    //0 未知,1 成功,2 失败
    private final AtomicInteger state = new AtomicInteger(0);

    public Page(String url, String pageName, String chapterName) {
        this.url = url;
        this.pageName = pageName;
        this.chapterName = chapterName;
    }

    public AtomicInteger getState() {
        return state;
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

    public AtomicInteger getFailTimes() {
        return failTimes;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.CHINA, "%s_%s", chapterName, pageName);
    }
}
