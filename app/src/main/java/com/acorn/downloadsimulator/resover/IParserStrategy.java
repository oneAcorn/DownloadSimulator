package com.acorn.downloadsimulator.resover;

/**
 * 解析策略
 * Created by acorn on 2020/4/12.
 */
public interface IParserStrategy<T, K> {

    void parse(T t, OnParseListener<K> listener);
}
