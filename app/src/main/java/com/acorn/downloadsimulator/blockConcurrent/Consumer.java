package com.acorn.downloadsimulator.blockConcurrent;

import com.acorn.downloadsimulator.LogUtil;

/**
 * Created by acorn on 2020/4/11.
 */
public abstract class Consumer<T> implements Runnable {
    private final Storage<T> mStorage;
    private final IDispatcher mDispatcher;

    public Consumer(Storage<T> storage, IDispatcher dispatcher) {
        mStorage = storage;
        mDispatcher = dispatcher;
    }

    @Override
    public void run() {
        try {
            while (!mDispatcher.isFinished()) {
                T t = mStorage.take();
                execute(t);
            }
        } catch (InterruptedException e) {
//            e.printStackTrace();
            LogUtil.e("线程终止");
        }
    }

    protected abstract void execute(T t) throws InterruptedException;


    public interface IDispatcher {
        boolean isFinished();
    }
}
