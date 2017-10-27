package com.twiki.processor;

import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Chapter;
import com.twiki.helper.BookStackTraversal;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class TextContentChapterBookProcessor implements BookProcessor {

    @Override
    public BookStack processBook(BookStack bookStack) throws IOException {
        BookStackTraversal.visitChapter(bookStack, this::transformImageSource);
        return bookStack;
    }

    private void transformImageSource(Chapter chapter) {
        Document document = Jsoup.parseBodyFragment(chapter.getHtmlContent());
        chapter.setTextContent(document.text());
    }
}
