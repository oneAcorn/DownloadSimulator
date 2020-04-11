package com.acorn.downloadsimulator;

import com.acorn.downloadsimulator.bean.Chapter;
import com.acorn.downloadsimulator.bean.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acorn on 2020/4/11.
 */
public class PageProducer implements Runnable {
    private final List<Chapter> mChapters;
    private final PageStorage mStorage;

    public PageProducer(PageStorage storage, List<Chapter> chapters) {
        mChapters = chapters;
        mStorage = storage;
    }

    @Override
    public void run() {
        for (final Chapter chapter : mChapters) {
            ChapterResover.resoveChapter(chapter, new ChapterResover.OnChapterResoveListener() {
                @Override
                public void onChapterResoved(Page page) {
                    try {
                        LogUtil.i("Producer put " + page.getUrl());
                        if (null == chapter.getPages()) {
                            chapter.setPages(new ArrayList<Page>());
                        }
                        chapter.getPages().add(page);
                        mStorage.put(page);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        LogUtil.e("Producer error:" + e.getMessage());
                    }
                }
            });
        }
    }
}
