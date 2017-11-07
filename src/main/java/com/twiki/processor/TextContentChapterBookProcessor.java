package com.twiki.processor;

import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Chapter;
import com.twiki.bookstack.Page;
import com.twiki.helper.BookStackTraversal;
import net.htmlparser.jericho.Source;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class TextContentChapterBookProcessor implements BookProcessor {
    private static final int SHORT_LENGTH_CHARACTER_LIMIT = 1000;

    @Override
    public BookStack processBook(BookStack bookStack) throws IOException {
        BookStackTraversal.visitChapter(bookStack, this::transformChapterText);
        return bookStack;
    }

    private void transformChapterText(Chapter chapter) {
        if (chapter.getHtmlContent().length() < SHORT_LENGTH_CHARACTER_LIMIT) {
            chapter.setTextContent(htmlToText(chapter.getHtmlContent(), -1));
        } else {
            Page bringChapterToPage = new Page("Chapter: " + chapter.getTitle(), chapter.getHtmlContent());
            chapter.getPages().add(0, bringChapterToPage);
            chapter.setTextContent(htmlToText(chapter.getHtmlContent(), SHORT_LENGTH_CHARACTER_LIMIT / 2));
        }
    }

    private String htmlToText(String html, int limit) {
        Source source = new Source(html);
        String renderedText = source.getRenderer()
                .setMaxLineLength(2000)
                .toString();
        if (limit <= -1) {
            return renderedText;
        }
        return StringUtils.abbreviate(renderedText, limit);
    }

}
