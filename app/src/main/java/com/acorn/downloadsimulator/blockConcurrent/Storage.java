package com.acorn.downloadsimulator.blockConcurrent;

import com.acorn.downloadsimulator.utils.LogUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by acorn on 2020/4/11.
 */
public class Storage<T> {
    private final BlockingQueue<T> mBlockingQueue;

    public Storage(int capacity) {
        mBlockingQueue = new ArrayBlockingQueue<>(capacity);
    }

    public T take() throws InterruptedException {
        T t = mBlockingQueue.take();
        LogUtil.output("Storage take:" + t.toString() + ",Capacity:" + mBlockingQueue.remainingCapacity());
        return t;
    }

    public void put(T t) throws InterruptedException {
        mBlockingQueue.put(t);
        LogUtil.output("Storage put:" + t.toString() + ",Capacity:" + mBlockingQueue.remainingCapacity());
    }

    public int remainingCapacity() {
        return mBlockingQueue.remainingCapacity();
    }

    public boolean isEmpty() {
        return mBlockingQueue.isEmpty();
    }
}
