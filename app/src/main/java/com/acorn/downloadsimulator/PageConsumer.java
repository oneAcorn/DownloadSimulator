package com.acorn.downloadsimulator;

import android.util.Log;

import com.acorn.downloadsimulator.bean.Page;

import java.util.Random;

/**
 * Created by acorn on 2020/4/11.
 */
public class PageConsumer implements Runnable {
    private final PageStorage mStorage;
    private final DownloadDispatcher mDispatcher;

    public PageConsumer(PageStorage storage, DownloadDispatcher dispatcher) {
        mStorage = storage;
        mDispatcher = dispatcher;
    }

    @Override
    public void run() {
        try {
            while (!mDispatcher.isFinished) {
                LogUtil.i("Consumer take");
                Page page = mStorage.take();
                saveBitmap(downloadBitmap(page));
            }
        } catch (InterruptedException e) {
//            e.printStackTrace();
            LogUtil.e("线程" + Thread.currentThread().getName() + "终止");
        }
    }

    private Page downloadBitmap(Page page) throws InterruptedException {
        Thread.sleep(new Random().nextInt(1500) + 100);
        page.setBitmap("我是图片," + page.getPageName());
        LogUtil.i("Consumer downloadBitmap " + page.getUrl() + " finished");
        return page;
    }

    private void saveBitmap(Page page) throws InterruptedException {
        Thread.sleep(new Random().nextInt(2500) + 200);
        LogUtil.i("Consumer saveBitmap " + page.getUrl() + " finished");
        mDispatcher.finish(page);
    }
}
