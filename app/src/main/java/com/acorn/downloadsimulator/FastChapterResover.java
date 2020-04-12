package com.acorn.downloadsimulator;

import com.acorn.downloadsimulator.bean.Chapter;
import com.acorn.downloadsimulator.bean.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by acorn on 2020/4/11.
 */
public class FastChapterResover {

    public static void resoveChapter(Chapter chapter, OnChapterResoveListener listener) {
        Random random = new Random();
        int pageCount = random.nextInt(13) + 17; //随机生成每章节页数,在17-29页间
        List<Page> pages = new CopyOnWriteArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            try {
                Thread.sleep(random.nextInt(200) + 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pages.add(new Page(chapter.getUrl() + i, String.valueOf(i), chapter.getChapterName()));
        }
        listener.onChapterResoved(pages);
        chapter.setResoved(true);
        chapter.setPageCount(pageCount);
    }

    public interface OnChapterResoveListener {
        void onChapterResoved(List<Page> pages);
    }
}
