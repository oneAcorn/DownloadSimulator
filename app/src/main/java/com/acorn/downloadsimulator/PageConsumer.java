package com.acorn.downloadsimulator;

import com.acorn.downloadsimulator.bean.Page;
import com.acorn.downloadsimulator.blockConcurrent.Consumer;
import com.acorn.downloadsimulator.blockConcurrent.Storage;

import java.util.Random;

/**
 * Created by acorn on 2020/4/12.
 */
public class PageConsumer extends Consumer<Page> {
    private Random mRandom = new Random();

    public PageConsumer(Storage<Page> storage, IDispatcher<Page> dispatcher) {
        super(storage, dispatcher);
    }

    @Override
    protected void execute(Page page, IDispatcher<Page> dispatcher) throws InterruptedException {
        saveBitmap(downloadBitmap(page), dispatcher);
    }

    private Page downloadBitmap(Page page) throws InterruptedException {
        Thread.sleep(mRandom.nextInt(1500) + 100);
        page.setBitmap("我是图片," + page.getPageName());
        LogUtil.i("Consumer downloadBitmap " + page + " finished");
        return page;
    }

    private void saveBitmap(Page page, IDispatcher<Page> dispatcher) throws InterruptedException {
        Thread.sleep(mRandom.nextInt(2500) + 200);
        if (mRandom.nextInt(100) > -1) { //TODO Version2.0 添加重试机制
            LogUtil.i("Consumer saveBitmap " + page + " finished");
            page.setDownloaded(true);
            dispatcher.finish(page);
        } else if (page.getFailTimes().get() < 3) {
            LogUtil.i("Consumer saveBitmap " + page + " fail,FailTimes=" + page.getFailTimes().get());
            page.getFailTimes().incrementAndGet();

            //Error!!!!!!!,消费者不能从事生产,在失败率高的情况下,
            // 可能会导致所有消费者都在生产,没人消费,从而无限阻塞!!!!!!!!!
            //storage.put(page);
        } else {
            LogUtil.i("Consumer saveBitmap FailTimes>=3!!!!!!!!!!!");
            dispatcher.finish(page);
        }
    }
}
