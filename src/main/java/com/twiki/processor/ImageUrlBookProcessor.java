package com.twiki.processor;

import com.twiki.bookstack.BookStack;

import java.io.IOException;

public class ImageUrlBookProcessor implements BookProcessor{
    @Override
    public BookStack processBook(BookStack bookStack) throws IOException {
        return bookStack;
    }
}
