package com.twiki.bookstack;

import org.jsoup.Jsoup;

public abstract class ContentEntity {
    protected String title;
    protected String type;
    protected String htmlContent;
    protected String textContent;

    public ContentEntity(String title, String htmlContent, boolean parseTextContent) {
        this.title = title;
        this.htmlContent = htmlContent;
        if (htmlContent != null && parseTextContent) {
            this.textContent = Jsoup.parseBodyFragment(htmlContent).text();
        }
    }

    public ContentEntity(String title, String htmlContent) {
        this(title, htmlContent, false);
    }

    public String getTitle() {
        return title;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getTextContent() {
        return textContent;
    }

    public String getType() {
        return type;
    }
}
