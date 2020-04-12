package com.acorn.downloadsimulator.blockConcurrent;

/**
 * Created by acorn on 2020/4/11.
 */
public abstract class Producer<T> implements Runnable {
    private final Storage<T> mStorage;

    public Producer(Storage<T> storage) {
        mStorage = storage;
    }

    @Override
    public void run() {
        execute(mStorage);
    }

    protected abstract void execute(Storage<T> storage);

}
