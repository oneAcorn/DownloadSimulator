package com.acorn.downloadsimulator;

import com.acorn.downloadsimulator.bean.Chapter;
import com.acorn.downloadsimulator.bean.Page;
import com.acorn.downloadsimulator.blockConcurrent.Consumer;
import com.acorn.downloadsimulator.blockConcurrent.Storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by acorn on 2020/4/11.
 */
public class DownloadDispatcher implements Consumer.IDispatcher<Page> {
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(30);
    private List<Chapter> mChapters;
    private boolean isFinished = false;

    public static void main(String[] args) {
        DownloadDispatcher dispatcher = new DownloadDispatcher();
        dispatcher.execute(generateTestChapters());
    }

    public static List<Chapter> generateTestChapters() {
        List<Chapter> chapters = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            chapters.add(new Chapter("url/" + i, "chapter:" + i));
        }
        return chapters;
    }

    public void execute(List<Chapter> chapters) {
        mChapters = chapters;
        Storage<Page> storage = new Storage<>(30);
        mExecutorService.execute(new PageProducer(storage, chapters));
        for (int i = 0; i < 30; i++) {
            mExecutorService.execute(new PageConsumer(storage, this));
        }
    }

    @Override
    public void finish(Page page) {
        synchronized (this) {
            Iterator<Chapter> iterator = mChapters.iterator();
            while (iterator.hasNext()) {
                Chapter chapter = iterator.next();
                if (chapter.getChapterName().equals(page.getChapterName())) {
                    if (!chapter.getPages().remove(page)) throw new AssertionError("没这个?!");
                }
                if (chapter.isResoved() && chapter.getPages().size() == 0) { //此章节下载完成
                    //不要使用mChapters.remove(chapter),会导致ConcurrentModificationException
                    iterator.remove();
                    LogUtil.i(chapter.getChapterName() + " 下载完成");
                }
            }
        }
        if (mChapters.size() == 0) {
            isFinished = true;
            mExecutorService.shutdownNow();
            LogUtil.i("结束");
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
