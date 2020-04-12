package com.acorn.downloadsimulator.bean;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;

/**
 * Created by acorn on 2020/4/11.
 */
public class Chapter {
    private String url;
    private String chapterName;
    private List<Page> pages;
    //是否已获取到全部pages
    private boolean resoved;
    private int pageCount;
    private AtomicInteger downloadedCount = new AtomicInteger();

    public Chapter(String url, String chapterName) {
        this.url = url;
        this.chapterName = chapterName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public boolean isResoved() {
        return resoved;
    }

    public void setResoved(boolean resoved) {
        this.resoved = resoved;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public AtomicInteger getDownloadedCount() {
        return downloadedCount;
    }

    public void setDownloadedCount(AtomicInteger downloadedCount) {
        this.downloadedCount = downloadedCount;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.CHINA, "%s,%d/%d", chapterName, downloadedCount.get(), pageCount);
    }
}
