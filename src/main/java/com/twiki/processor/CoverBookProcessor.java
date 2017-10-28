package com.twiki.processor;

import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Chapter;
import com.twiki.bookstack.Page;

import java.io.IOException;

public class CoverBookProcessor implements BookProcessor {
    @Override
    public BookStack processBook(BookStack bookStack) throws IOException {
        Page coverPage = new Page(bookStack.getTitle(), bookStack.getHtmlContent());
        if (bookStack.getContents().get(0) instanceof Chapter) {
            ((Chapter) bookStack.getContents().get(0)).getPages().add(0, coverPage);
        } else {
            bookStack.getContents().add(0, coverPage);
        }
        return bookStack;
    }
}
