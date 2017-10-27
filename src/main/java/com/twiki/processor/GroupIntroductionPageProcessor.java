package com.twiki.processor;

import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Chapter;
import com.twiki.bookstack.Page;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GroupIntroductionPageProcessor implements BookProcessor {
    @Override
    public BookStack processBook(BookStack bookStack) throws IOException {
        List<Page> introductionPages = bookStack.getContents().stream()
                .filter(c -> c instanceof Page)
                .map(c -> (Page) c)
                .collect(Collectors.toList());

        if (introductionPages.isEmpty()) {
            return bookStack;
        }

        bookStack.getContents().removeAll(introductionPages);

        Chapter introductionChapter = new Chapter(bookStack.getTitle() + " - Introduction", "Introduction pages");
        introductionChapter.setSlug(bookStack.getSlug());
        introductionChapter.getPages().addAll(introductionPages);
        bookStack.getContents().add(0, introductionChapter);

        return bookStack;
    }
}
