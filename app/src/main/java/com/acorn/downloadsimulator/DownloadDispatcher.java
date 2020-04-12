package com.acorn.downloadsimulator;

import com.acorn.downloadsimulator.bean.Chapter;
import com.acorn.downloadsimulator.bean.Page;
import com.acorn.downloadsimulator.blockConcurrent.Consumer;
import com.acorn.downloadsimulator.blockConcurrent.Storage;
import com.acorn.downloadsimulator.resover.BurstChapterParseStrategy;
import com.acorn.downloadsimulator.resover.NormalChapterParseStrategy;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by acorn on 2020/4/11.
 */
public class DownloadDispatcher implements Consumer.IDispatcher<Page> {
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(8);
    private List<Chapter> mChapters;
    private List<Chapter> removedChapters;
    private boolean isFinished = false;

    public static void main(String[] args) {
        DownloadDispatcher dispatcher = new DownloadDispatcher();
        dispatcher.execute(generateTestChapters());
    }

    public static List<Chapter> generateTestChapters() {
        List<Chapter> chapters = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            chapters.add(new Chapter("url/" + i, "chapter:" + i));
        }
        return chapters;
    }

    public void execute(List<Chapter> chapters) {
        mChapters = chapters;
        removedChapters = new Vector<>();
        Storage<Page> storage = new Storage<>(30);
        mExecutorService.execute(new PageProducer(storage, chapters, new NormalChapterParseStrategy()));
        for (int i = 0; i < 30; i++) {
            mExecutorService.execute(new PageConsumer(storage, this));
        }
    }

    @Override
    public void finish(Page page) {
        for (Chapter chapter : mChapters) {
            int downloadCount = 0;
            if (chapter.getChapterName().equals(page.getChapterName())) {
                //下载完成一页,已下载数原子自增1
                downloadCount = chapter.getDownloadedCount().incrementAndGet();
            }
            if (chapter.isResoved() && chapter.getPageCount() == downloadCount) { //此章节下载完成
                //不要使用mChapters.remove(chapter),会导致ConcurrentModificationException,使用CopyOnWriteArrayList也可以
                removedChapters.add(chapter);
                LogUtil.i(chapter + " 下载完成");
            }
        }
        if (mChapters.size() == removedChapters.size()) {
            isFinished = true;
            mExecutorService.shutdownNow();
            LogUtil.i("---------------------------下载结果-------------------------------");
            int titleDownload = 0, titleCount = 0;
            for (Chapter chapter : mChapters) {
                LogUtil.i(chapter.toString());
                titleCount += chapter.getPageCount();
                StringBuilder sb = new StringBuilder();
                for (Page p : chapter.getPages()) {
                    titleDownload += p.isDownloaded() ? 1 : 0;
                    sb.append(p.toString());
                    sb.append(" ");
                }
                LogUtil.i(sb.toString());
            }
            DecimalFormat df = new DecimalFormat("0.##");
            LogUtil.i(String.format(Locale.CHINA, "成功率:%s%%", df.format((float) titleDownload / (float) titleCount * 100f)));
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
