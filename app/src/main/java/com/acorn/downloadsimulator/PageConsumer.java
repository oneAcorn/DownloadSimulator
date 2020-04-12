package com.acorn.downloadsimulator;

import com.acorn.downloadsimulator.bean.Page;
import com.acorn.downloadsimulator.blockConcurrent.Consumer;
import com.acorn.downloadsimulator.blockConcurrent.Storage;

import java.util.Random;

/**
 * Created by acorn on 2020/4/12.
 */
public class PageConsumer extends Consumer<Page> {
    public PageConsumer(Storage<Page> storage, IDispatcher<Page> dispatcher) {
        super(storage, dispatcher);
    }

    @Override
    protected void execute(Page page, IDispatcher<Page> dispatcher) throws InterruptedException {
        saveBitmap(downloadBitmap(page), dispatcher);
    }

    private Page downloadBitmap(Page page) throws InterruptedException {
        Thread.sleep(new Random().nextInt(1500) + 100);
        page.setBitmap("我是图片," + page.getPageName());
        LogUtil.i("Consumer downloadBitmap " + page.getUrl() + " finished");
        return page;
    }

    private void saveBitmap(Page page, IDispatcher<Page> dispatcher) throws InterruptedException {
        Thread.sleep(new Random().nextInt(2500) + 200);
        LogUtil.i("Consumer saveBitmap " + page.getUrl() + " finished");
        dispatcher.finish(page);
    }
}
