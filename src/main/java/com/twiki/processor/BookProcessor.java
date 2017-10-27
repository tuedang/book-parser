package com.twiki.processor;

import com.twiki.bookstack.BookStack;

import java.io.IOException;

public interface BookProcessor {

    BookStack processBook(BookStack bookStack) throws IOException;
}
