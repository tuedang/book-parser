package com.twiki.processor;

import com.twiki.bookstack.BookStack;
import com.twiki.helper.AppStringUtils;
import com.twiki.helper.BookStackTraversal;

import java.io.IOException;

public class SlugBookProcessor implements BookProcessor {

    @Override
    public BookStack processBook(BookStack bookStack) throws IOException {
        bookStack.setSlug(AppStringUtils.slugify(bookStack.getTitle()));
        BookStackTraversal.visitContent(bookStack, (contentEntity) ->
                contentEntity.setSlug(AppStringUtils.slugify(contentEntity.getTitle())));
        return bookStack;
    }
}
