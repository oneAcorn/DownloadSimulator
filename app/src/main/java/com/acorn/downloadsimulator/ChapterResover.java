package com.acorn.downloadsimulator;

import com.acorn.downloadsimulator.bean.Chapter;
import com.acorn.downloadsimulator.bean.Page;

import java.util.Random;

/**
 * Created by acorn on 2020/4/11.
 */
public class ChapterResover {

    public static void resoveChapter(Chapter chapter, OnChapterResoveListener listener) {
        Random random = new Random();
        int pageCount = random.nextInt(10) > 2 ? (random.nextInt(13) + 1) :
                (random.nextInt(20) + 5); //随机生成每章节页数,大概率在17-29页间,小概率在120-319页间
        for (int i = 0; i < pageCount; i++) {
            try {
                Thread.sleep(random.nextInt(200) + 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            listener.onChapterResoved(new Page(chapter.getUrl() + i, chapter.getChapterName() + i, chapter.getChapterName()));
        }
        chapter.setResoved(true);
    }

    public interface OnChapterResoveListener {
        void onChapterResoved(Page page);
    }
}
