package com.acorn.downloadsimulator.blockConcurrent;

import com.acorn.downloadsimulator.LogUtil;

/**
 * Created by acorn on 2020/4/11.
 */
public abstract class Consumer<T> implements Runnable {
    private final Storage<T> mStorage;
    private final IDispatcher<T> mDispatcher;

    public Consumer(Storage<T> storage, IDispatcher<T> dispatcher) {
        mStorage = storage;
        mDispatcher = dispatcher;
    }

    @Override
    public void run() {
        try {
            while (!mDispatcher.isFinished()) {
                T t = mStorage.take();
                execute(t, mDispatcher);
            }
        } catch (InterruptedException e) {
//            e.printStackTrace();
            LogUtil.e("线程终止");
        }
    }

    protected abstract void execute(T t, IDispatcher<T> dispatcher) throws InterruptedException;


    public interface IDispatcher<T> {
        void finish(T t);

        boolean isFinished();
    }
}
