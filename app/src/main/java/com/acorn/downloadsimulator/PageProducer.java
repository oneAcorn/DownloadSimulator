package com.acorn.downloadsimulator;

import com.acorn.downloadsimulator.bean.Chapter;
import com.acorn.downloadsimulator.bean.Page;
import com.acorn.downloadsimulator.blockConcurrent.Producer;
import com.acorn.downloadsimulator.blockConcurrent.Storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acorn on 2020/4/11.
 */
public class PageProducer extends Producer<Page> {
    private final List<Chapter> mChapters;

    public PageProducer(Storage<Page> storage, List<Chapter> chapters) {
        super(storage);
        mChapters = chapters;
    }

    @Override
    protected void execute(final Storage<Page> storage) {
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
                        storage.put(page);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        LogUtil.e("Producer error:" + e.getMessage());
                    }
                }
            });
        }
    }
}
