package com.twiki.bookstack;

import java.util.ArrayList;
import java.util.List;

public class Chapter extends ContentEntity {
    private List<Page> pages;

    public Chapter(String title, String htmlContent) {
        super(title, htmlContent, true);
        this.type = "chapter";
        pages = new ArrayList<>();
    }

    public Chapter addPage(Page page) {
        pages.add(page);
        return this;
    }

    public List<Page> getPages() {
        return pages;
    }
}
