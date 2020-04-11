package com.acorn.downloadsimulator;

import com.acorn.downloadsimulator.bean.Chapter;
import com.acorn.downloadsimulator.bean.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by acorn on 2020/4/11.
 */
public class DownloadDispatcher {
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(30);
    private CopyOnWriteArrayList<Chapter> mChapters;
    public boolean isFinished = false;

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
        mChapters = new CopyOnWriteArrayList<>(chapters);
        PageStorage storage = new PageStorage();
        mExecutorService.execute(new PageProducer(storage, chapters));
        for (int i = 0; i < 30; i++) {
            mExecutorService.execute(new PageConsumer(storage, this));
        }
    }

    public void finish(Page page) {
        synchronized (this) {
            for (Chapter chapter : mChapters) {
                if (chapter.getChapterName().equals(page.getChapterName())) {
                    if (!chapter.getPages().remove(page)) throw new AssertionError("没这个?!");
                }
                if (chapter.isResoved() && chapter.getPages().size() == 0) { //此章节下载完成
                    mChapters.remove(chapter);
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
}
