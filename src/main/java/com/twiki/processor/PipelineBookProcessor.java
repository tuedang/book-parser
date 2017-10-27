package com.twiki.processor;

import com.twiki.bookstack.BookStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PipelineBookProcessor implements BookProcessor {
    protected List<BookProcessor> bookProcessors;

    public PipelineBookProcessor() {
        this.bookProcessors = null;
    }

    public PipelineBookProcessor(List<BookProcessor> bookProcessors) {
        this.bookProcessors = bookProcessors;
    }

    public PipelineBookProcessor addBookProcessor(BookProcessor bookProcessor) {
        if (this.bookProcessors == null) {
            this.bookProcessors = new ArrayList<>();
        }
        this.bookProcessors.add(bookProcessor);
        return this;
    }

    @Override
    public BookStack processBook(BookStack bookStack) throws IOException {
        if (bookProcessors == null) {
            return bookStack;
        }
        for (BookProcessor bookProcessor : bookProcessors) {
            bookStack = bookProcessor.processBook(bookStack);
        }
        return bookStack;
    }
}
