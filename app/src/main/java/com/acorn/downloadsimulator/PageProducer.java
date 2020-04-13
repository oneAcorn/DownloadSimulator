package com.acorn.downloadsimulator;

import com.acorn.downloadsimulator.bean.Chapter;
import com.acorn.downloadsimulator.bean.Page;
import com.acorn.downloadsimulator.blockConcurrent.Node;
import com.acorn.downloadsimulator.blockConcurrent.Producer;
import com.acorn.downloadsimulator.blockConcurrent.Storage;
import com.acorn.downloadsimulator.resover.BurstChapterParseStrategy;
import com.acorn.downloadsimulator.resover.ChapterParser;
import com.acorn.downloadsimulator.resover.IParserStrategy;
import com.acorn.downloadsimulator.resover.OnParseListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acorn on 2020/4/11.
 */
public class PageProducer extends Producer<Page> {
    private final List<Chapter> mChapters;
    private final ChapterParser<Chapter, Page> mChapterParser;
    private final int maxFailTimes = 3;

    public PageProducer(Storage<Page> storage, List<Chapter> chapters,
                        IParserStrategy<Chapter, Page> strategy, OnProduceListener listener) {
        super(storage, listener);
        mChapters = chapters;
        mChapterParser = new ChapterParser<>(strategy);
    }

    public static void main(String[] args) {
        PageProducer producer = new PageProducer(null, DownloadDispatcher.generateTestChapters(),
                new BurstChapterParseStrategy(), null);
        producer.execute(null);
    }

    @Override
    protected void execute(final Storage<Page> storage) {
        //把所有章节解析为页
        for (final Chapter chapter : mChapters) {
            LogUtil.i("Producer chapter " + chapter);
            mChapterParser.parse(chapter, new OnParseListener<Page>() {
                @Override
                public void onParsed(Page page) {
                    if (chapter.getPages() == null)
                        chapter.setPages(new ArrayList<Page>());
                    chapter.getPages().add(page);
                    try {
                        storage.put(page);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        LogUtil.e("Producer error:" + e.getMessage());
                    }

                }
            });
        }

        //轮询下载失败的环链表,重新放入仓库
        Node<Page> node = getCircleNode();
        while (node != null) {
            if (node.data.getState().get() == 1 || node.data.getFailTimes().get() > maxFailTimes) {
                if (node.prev == null) { //最后一个了
                    node.next = null;
                } else if (node.prev == node.next) { //最后2个
                    node.next.prev = null;
                } else {
                    node.prev.next = node.next;
                    node.next.prev = node.prev;
                }
            } else if (node.data.getState().get() == 2) {
                try {
                    node.data.getState().set(0);
                    storage.put(node.data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            node = node.next;
        }
        LogUtil.i("Producer 完工");
    }

    /**
     * 创建没有下载成功的环链表
     *
     * @return
     */
    private Node<Page> getCircleNode() {
        Node<Page> head = null;
        Node<Page> tmp = new Node<>();
        for (Chapter chapter : mChapters) {
            for (Page page : chapter.getPages()) {
                if (page.getState().get() != 1) {
                    tmp.data = page;
                    tmp.next = new Node<>();
                    if (head == null) {
                        head = tmp;
                    }
                    tmp.next.prev = tmp;
                    tmp = tmp.next;
                }
            }
        }
        tmp = tmp.prev;
        if (head != null) {
            if (head != tmp) {
                head.prev = tmp;
                tmp.next = head;
            } else
                head.next = null;
        }
        return head;
    }
}
