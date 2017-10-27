package com.twiki.bookstack;

public abstract class ContentEntity {
    protected String title;
    protected String type;
    protected String htmlContent;
    protected String textContent;
    private String slug;

    public ContentEntity(String title, String htmlContent) {
        this.title = title;
        this.htmlContent = htmlContent;
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

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getType() {
        return type;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
