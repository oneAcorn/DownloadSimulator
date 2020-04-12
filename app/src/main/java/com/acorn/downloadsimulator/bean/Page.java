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
    private boolean isDownloaded;
    //失败次数
    private final AtomicInteger failTimes = new AtomicInteger();

    public Page(String url, String pageName, String chapterName) {
        this.url = url;
        this.pageName = pageName;
        this.chapterName = chapterName;
    }

    public synchronized boolean isDownloaded() {
        return isDownloaded;
    }

    public synchronized void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
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
        return String.format(Locale.CHINA, "%s_%s,%s", chapterName, pageName, isDownloaded);
    }
}
