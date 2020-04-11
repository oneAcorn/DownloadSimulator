package com.acorn.downloadsimulator.bean;

import java.util.List;

/**
 * Created by acorn on 2020/4/11.
 */
public class Chapter {
    private String url;
    private String chapterName;
    private List<Page> pages;
    //是否已获取到全部pages
    private boolean resoved;

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
}
