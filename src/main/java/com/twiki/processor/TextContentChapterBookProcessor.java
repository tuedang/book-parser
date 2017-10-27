package com.twiki.processor;

import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Chapter;
import com.twiki.helper.BookStackTraversal;
import net.htmlparser.jericho.Source;

import java.io.IOException;

public class TextContentChapterBookProcessor implements BookProcessor {

    @Override
    public BookStack processBook(BookStack bookStack) throws IOException {
        BookStackTraversal.visitChapter(bookStack, this::transformImageSource);
        return bookStack;
    }

    public void transformImageSource(Chapter chapter) {
        Source source = new Source(chapter.getHtmlContent());
        String renderedText = source.getRenderer()
                .setMaxLineLength(2000)
                .toString();
        chapter.setTextContent(renderedText);
    }

}
