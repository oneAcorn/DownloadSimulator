package com.acorn.downloadsimulator.resover;

/**
 * 解析完成回调
 * Created by acorn on 2020/4/12.
 */
public interface OnParseListener<T> {
    void onParsed(T t);
}
