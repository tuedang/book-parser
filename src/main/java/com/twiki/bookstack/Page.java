package com.twiki.bookstack;

public class Page extends ContentEntity {
    public Page(String title, String htmlContent) {
        super(title, htmlContent);
        this.type = "page";
    }

}
