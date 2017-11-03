package com.twiki.bookstack;

import com.google.common.collect.Lists;
import nl.siegmann.epublib.domain.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookMetaContextHolder {
    private Book book;
    private Map<String, ContentEntity> resourceMapping;
    private Map<String, Chapter> chapterMapping;

    public BookMetaContextHolder(Book book) {
        this.book = book;
        resourceMapping = new HashMap<>();
        chapterMapping = new HashMap<>();
    }

    public BookMetaContextHolder putResource(String href, ContentEntity contentEntity) {
        resourceMapping.put(href, contentEntity);
        if (contentEntity instanceof Chapter) {
            if (href.contains("#")) {
                chapterMapping.put(href.split("#")[0], (Chapter) contentEntity);
            } else {
                chapterMapping.put(href, (Chapter) contentEntity);
            }
        }
        return this;
    }

    public ContentEntity getResource(String href) {
        return resourceMapping.get(href);
    }

    public Chapter getChapter(String href) {
        return chapterMapping.get(href);
    }

    public List<String> getChapterResource() {
        return Lists.newArrayList(chapterMapping.keySet());
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Map<String, ContentEntity> getResourceMapping() {
        return resourceMapping;
    }

    public void setResourceMapping(Map<String, ContentEntity> resourceMapping) {
        this.resourceMapping = resourceMapping;
    }
}
