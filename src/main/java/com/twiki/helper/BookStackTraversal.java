package com.twiki.helper;

import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Chapter;
import com.twiki.bookstack.ContentEntity;
import com.twiki.bookstack.Page;

public class BookStackTraversal {
    @FunctionalInterface
    public interface OrderedContentVisitor {
        void visit(ContentEntity contentEntity, String type, int index);
    }

    @FunctionalInterface
    public interface PageVisitor {
        void visit(Page page, int index);
    }

    public static void visitBook(BookStack bookStack, OrderedContentVisitor callback) {
        for (ContentEntity contentEntity : bookStack.getContents()) {
            callback.visit(contentEntity, contentEntity.getType(), bookStack.getContents().indexOf(contentEntity));
            if (contentEntity instanceof Chapter) {
                Chapter chapter = (Chapter) contentEntity;
                for (Page page : chapter.getPages()) {
                    callback.visit(page, page.getType(), chapter.getPages().indexOf(page));
                }
            }
        }
    }

    public static void visitPage(BookStack bookStack, PageVisitor pageVisitor) {
        for (ContentEntity contentEntity : bookStack.getContents()) {
            if (contentEntity instanceof Page) {
                pageVisitor.visit((Page) contentEntity, bookStack.getContents().indexOf(contentEntity));
            } else {
                Chapter chapter = (Chapter) contentEntity;
                for (Page page : chapter.getPages()) {
                    pageVisitor.visit(page, chapter.getPages().indexOf(page));
                }
            }
        }
    }
}
