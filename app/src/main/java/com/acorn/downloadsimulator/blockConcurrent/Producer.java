package com.acorn.downloadsimulator.blockConcurrent;

/**
 * Created by acorn on 2020/4/11.
 */
public abstract class Producer<T> implements Runnable {
    private final Storage<T> mStorage;
    private final OnProduceListener mListener;

    public Producer(Storage<T> storage, OnProduceListener listener) {
        mStorage = storage;
        mListener = listener;
    }

    @Override
    public void run() {
        execute(mStorage);
        if (null != mListener)
            mListener.onProduceFinished();
    }

    protected abstract void execute(Storage<T> storage);

    public interface OnProduceListener {
        void onProduceFinished();
    }
}
