package com.twiki.helper;

import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Chapter;
import com.twiki.bookstack.ContentEntity;
import com.twiki.bookstack.Page;
import org.apache.commons.lang3.StringUtils;

public class BookStackTraversal {
    public static final int FIRST_LEVEL = 0;
    public static final int SUB_LEVEL = 1;

    @FunctionalInterface
    public interface OrderedContentVisitor {
        void visit(ContentEntity contentEntity, String type, int index);
    }

    @FunctionalInterface
    public interface PageVisitor {
        void visit(Page page);
    }

    @FunctionalInterface
    public interface ChapterVisitor {
        void visit(Chapter chapter);
    }

    @FunctionalInterface
    public interface ContentVisitor {
        void visit(ContentEntity contentEntity, int level);
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

    public static void visitContent(BookStack bookStack, ContentVisitor callback) {
        for (ContentEntity contentEntity : bookStack.getContents()) {
            callback.visit(contentEntity, FIRST_LEVEL);
            if (contentEntity instanceof Chapter) {
                Chapter chapter = (Chapter) contentEntity;
                for (Page page : chapter.getPages()) {
                    callback.visit(page, SUB_LEVEL);
                }
            }
        }
    }

    public static void print(BookStack bookStack) {
        System.out.println(bookStack.getTitle());
        for (ContentEntity contentEntity : bookStack.getContents()) {
            if (contentEntity instanceof Chapter) {
                Chapter chapter = (Chapter) contentEntity;
                print("├── ", chapter);
                for (Page page : chapter.getPages()) {
                    print("    └── ", page);
                }
            } else {
                print("└── ", contentEntity);
            }
        }
    }

    private static void print(String prefix, ContentEntity contentEntity) {
        System.out.println(String.format("%s%s      (%s)", prefix, contentEntity.getTitle(),
                StringUtils.abbreviate(contentEntity.getHtmlContent(), 20)));
    }

    public static void visitPage(BookStack bookStack, PageVisitor pageVisitor) {
        for (ContentEntity contentEntity : bookStack.getContents()) {
            if (contentEntity instanceof Page) {
                pageVisitor.visit((Page) contentEntity);
            } else {
                Chapter chapter = (Chapter) contentEntity;
                for (Page page : chapter.getPages()) {
                    pageVisitor.visit(page);
                }
            }
        }
    }

    public static void visitChapter(BookStack bookStack, ChapterVisitor visitor) {
        for (ContentEntity contentEntity : bookStack.getContents()) {
            if (contentEntity instanceof Chapter) {
                visitor.visit((Chapter) contentEntity);
            }
        }
    }
}
