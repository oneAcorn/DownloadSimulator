package com.acorn.downloadsimulator;

import com.acorn.downloadsimulator.bean.Chapter;
import com.acorn.downloadsimulator.bean.Page;
import com.acorn.downloadsimulator.blockConcurrent.Consumer;
import com.acorn.downloadsimulator.blockConcurrent.Producer;
import com.acorn.downloadsimulator.blockConcurrent.Storage;
import com.acorn.downloadsimulator.resover.NormalChapterParseStrategy;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by acorn on 2020/4/11.
 */
public class DownloadDispatcher implements Consumer.IDispatcher, Producer.OnProduceListener {
    //总线程数
    private final int threadCount = 8;
    //生产者线程数
    private int producerCount = 2;
    private AtomicInteger remainingProducerCount = new AtomicInteger();
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(threadCount);
    private List<Chapter> mChapters;
    private boolean isFinished = false;
    private Storage<Page> storage;

    public static void main(String[] args) {
        DownloadDispatcher dispatcher = new DownloadDispatcher();
        dispatcher.execute(generateTestChapters());
    }

    public static List<Chapter> generateTestChapters() {
        List<Chapter> chapters = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chapters.add(new Chapter("url/" + i, "chapter:" + i));
        }
        return chapters;
    }

    public void execute(List<Chapter> chapters) {
        mChapters = chapters;
        storage = new Storage<>(30);

        int chapterSize = chapters.size();
        if (producerCount >= chapterSize) {
            producerCount = chapterSize;
        }
        if (producerCount >= threadCount) {
            throw new IllegalArgumentException("生产者的线程数大于等于总线程数");
        }

        int firstIndex = 0;
        int step = chapterSize / producerCount;
        int lastIndex = step;
        for (int i = 0; i < producerCount; i++) {
            if (i == producerCount - 1) {
                lastIndex = chapterSize;
            }
            List<Chapter> chapterForProducer = new CopyOnWriteArrayList<>();
            for (int j = firstIndex; j < lastIndex; j++) {
                chapterForProducer.add(chapters.get(j));
            }
            mExecutorService.execute(new PageProducer(storage, chapterForProducer, new NormalChapterParseStrategy(), this));
            remainingProducerCount.incrementAndGet();
            firstIndex = lastIndex;

            lastIndex += step;
        }

        for (int i = 0; i < threadCount; i++) {
            mExecutorService.execute(new PageConsumer(storage, this));
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void onProduceFinished() {
        int remainingCount = remainingProducerCount.decrementAndGet();
        if (remainingCount == 0 && storage.isEmpty()) {//全部下载完成
            int titleDownload = 0, titleCount = 0;
            for (Chapter chapter : mChapters) {
                LogUtil.i(chapter.toString());
                titleCount += chapter.getPageCount();
                StringBuilder sb = new StringBuilder();
                for (Page p : chapter.getPages()) {
                    titleDownload += p.getState().get() == 1 ? 1 : 0;
                    sb.append(p.toString());
                    sb.append(" ");
                }
                LogUtil.i(sb.toString());
            }
            DecimalFormat df = new DecimalFormat("0.##");
            LogUtil.i(String.format(Locale.CHINA, "成功率:%s%%", df.format((float) titleDownload / (float) titleCount * 100f)));

            isFinished = true;
            mExecutorService.shutdownNow();
        }

    }
}
