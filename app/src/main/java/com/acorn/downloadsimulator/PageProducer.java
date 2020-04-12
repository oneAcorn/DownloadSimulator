package com.acorn.downloadsimulator;

import com.acorn.downloadsimulator.bean.Chapter;
import com.acorn.downloadsimulator.bean.Page;
import com.acorn.downloadsimulator.blockConcurrent.Producer;
import com.acorn.downloadsimulator.blockConcurrent.Storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

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
            LogUtil.i("Producer chapter " + chapter);
            FastChapterResover.resoveChapter(chapter, new FastChapterResover.OnChapterResoveListener() {
                @Override
                public void onChapterResoved(List<Page> pages) {
                    try {
                        chapter.setPages(pages);
                        LogUtil.i("Producer resove " + chapter);
                        for (Page page : pages) {
                            storage.put(page);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        LogUtil.e("Producer error:" + e.getMessage());
                    }
                }
            });
        }
    }
}
