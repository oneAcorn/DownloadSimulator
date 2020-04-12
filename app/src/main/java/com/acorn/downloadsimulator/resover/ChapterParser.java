package com.acorn.downloadsimulator.resover;

/**
 * Created by acorn on 2020/4/12.
 */
public class ChapterParser<T, K> {
    private IParserStrategy<T, K> mStrategy;

    public ChapterParser(IParserStrategy<T, K> strategy) {
        mStrategy = strategy;
    }

    public void parse(T t, OnParseListener<K> listener) {
        mStrategy.parse(t, listener);
    }
}
