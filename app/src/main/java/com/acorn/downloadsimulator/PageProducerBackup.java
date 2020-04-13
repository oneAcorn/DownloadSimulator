package com.acorn.downloadsimulator;

import com.acorn.downloadsimulator.bean.Chapter;
import com.acorn.downloadsimulator.bean.Page;
import com.acorn.downloadsimulator.blockConcurrent.Producer;
import com.acorn.downloadsimulator.blockConcurrent.Storage;
import com.acorn.downloadsimulator.resover.ChapterParser;
import com.acorn.downloadsimulator.resover.IParserStrategy;
import com.acorn.downloadsimulator.resover.OnParseListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acorn on 2020/4/11.
 */
public class PageProducerBackup extends Producer<Page> {
    private final List<Chapter> mChapters;
    private final ChapterParser<Chapter, Page> mChapterParser;

    public PageProducerBackup(Storage<Page> storage, List<Chapter> chapters, IParserStrategy<Chapter, Page> strategy) {
        super(storage,null);
        mChapters = chapters;
        mChapterParser = new ChapterParser<>(strategy);
    }

    @Override
    protected void execute(final Storage<Page> storage) {
        for (final Chapter chapter : mChapters) {
            LogUtil.i("Producer chapter " + chapter);
            mChapterParser.parse(chapter, new OnParseListener<Page>() {
                @Override
                public void onParsed(Page page) {
                    if (chapter.getPages() == null)
                        chapter.setPages(new ArrayList<Page>());
                    chapter.getPages().add(page);
                    try {
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
