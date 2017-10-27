package com.twiki.processor;

import com.github.slugify.Slugify;
import com.twiki.bookstack.BookStack;
import com.twiki.helper.BookStackTraversal;

import java.io.IOException;

public class SlugBookProcessor implements BookProcessor {
    private Slugify slugify = new Slugify();

    @Override
    public BookStack processBook(BookStack bookStack) throws IOException {
        bookStack.setSlug(slugify.slugify(bookStack.getTitle()));
        BookStackTraversal.visitContent(bookStack, (contentEntity) ->
                contentEntity.setSlug(slugify.slugify(contentEntity.getTitle())));
        return bookStack;
    }
}
