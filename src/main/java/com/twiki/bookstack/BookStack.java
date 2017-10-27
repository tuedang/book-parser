package com.twiki.bookstack;

import nl.siegmann.epublib.domain.Book;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

public class BookStack extends ContentEntity {
    private Book originalBook;

    private List<ContentEntity> contents;

    public BookStack(String title, String htmlContent) {
        super(title, htmlContent);
        this.type = "book";
        contents = new ArrayList<>();
    }

    public BookStack addPage(Page page) {
        contents.add(page);
        return this;
    }

    public BookStack addChapter(Chapter chapter) {
        contents.add(chapter);
        return this;
    }

    public List<ContentEntity> getContents() {
        return contents;
    }

    @Transient
    public Chapter getLastChapter() {
        ContentEntity contentEntity = contents.get(contents.size() - 1);
        if(!(contentEntity instanceof Chapter)) {
            throw new RuntimeException("Wrong content type");
        }
        return (Chapter) contentEntity;
    }

    @Transient
    public Book getOriginalBook() {
        return originalBook;
    }

    public void setOriginalBook(Book originalBook) {
        this.originalBook = originalBook;
    }
}
