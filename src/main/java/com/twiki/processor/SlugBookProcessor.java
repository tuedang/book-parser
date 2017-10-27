package com.twiki.processor;

import com.github.slugify.Slugify;
import com.twiki.bookstack.BookStack;

import java.io.IOException;

public class SlugBookProcessor implements BookProcessor {
    private Slugify slugify = new Slugify();

    @Override
    public BookStack processBook(BookStack bookStack) throws IOException {
        String slug = slugify.slugify(bookStack.getTitle());
        bookStack.setSlug(slug);
        return bookStack;
    }
}
