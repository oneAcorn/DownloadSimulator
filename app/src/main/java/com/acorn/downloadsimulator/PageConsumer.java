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

    public PageConsumer(Storage<Page> storage, IDispatcher dispatcher) {
        super(storage, dispatcher);
    }

    @Override
    protected void execute(Page page) throws InterruptedException {
        saveBitmap(downloadBitmap(page));
    }

    private Page downloadBitmap(Page page) throws InterruptedException {
        Thread.sleep(mRandom.nextInt(15) + 10);
        page.setBitmap("我是图片," + page.getPageName());
//        LogUtil.i("Consumer downloadBitmap " + page + " finished");
        return page;
    }

    private void saveBitmap(Page page) throws InterruptedException {
        Thread.sleep(mRandom.nextInt(250) + 20);
        if (mRandom.nextInt(100) > 70) { //TODO Version1.1 添加重试机制
            LogUtil.i("Consumer saveBitmap " + page + " finished");
            page.getState().set(1);
        } else {
            page.getFailTimes().incrementAndGet();
            page.getState().set(2);
            LogUtil.i("Consumer saveBitmap " + page + " fail,FailTimes=" + page.getFailTimes().get());

            //Error!!!!!!!,消费者不能从事生产,在失败率高的情况下,
            // 可能会导致所有消费者都在生产,没人消费,从而无限阻塞!!!!!!!!!
            //storage.put(page);
        }
    }
}
