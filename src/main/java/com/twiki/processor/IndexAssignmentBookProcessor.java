package com.twiki.processor;

import com.twiki.bookstack.BookStack;
import com.twiki.helper.BookStackTraversal;

import java.io.IOException;

public class IndexAssignmentBookProcessor implements BookProcessor {
    @Override
    public BookStack processBook(BookStack bookStack) throws IOException {
        BookStackTraversal.visitBook(bookStack, (contentEntity, type, index) -> contentEntity.setPriority(index));
        return bookStack;
    }
}
