package com.acorn.downloadsimulator.resover;

import com.acorn.downloadsimulator.bean.Chapter;
import com.acorn.downloadsimulator.bean.Page;

import java.util.Random;

/**
 * 隔一段时间直接生产出一堆
 * Created by acorn on 2020/4/12.
 */
public class BurstChapterParseStrategy implements IParserStrategy<Chapter, Page> {
    @Override
    public void parse(Chapter chapter, OnParseListener<Page> listener) {
        Random random = new Random();
        int pageCount = random.nextInt(13) + 17; //随机生成每章节页数,在17-29页间
        try {
            Thread.sleep(pageCount * (random.nextInt(1) + 1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < pageCount; i++) {
            listener.onParsed(new Page(chapter.getUrl() + i, String.valueOf(i), chapter.getChapterName()));
        }
        chapter.setResoved(true);
        chapter.setPageCount(pageCount);
    }
}
