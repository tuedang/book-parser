package com.twiki.bookstack;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

public class BookStack extends ContentEntity {

    private List<ContentEntity> contents;

    public BookStack(String title, String htmlContent) {
        super(title, htmlContent, true);
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
        ContentEntity contentEntity = contents.get(contents.size() - 1);//.getPages().add(page);
        if(!(contentEntity instanceof Chapter)) {
            throw new RuntimeException("Wrong content type");
        }
        return (Chapter) contentEntity;
    }

}
