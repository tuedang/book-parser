package com.twiki.bookstack;

import java.util.List;

public class BookStack {
    private String title;
    private String description;

    private Chapter primaryChapter;

    private List<Chapter> chapters;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Chapter getPrimaryChapter() {
        return primaryChapter;
    }

    public void setPrimaryChapter(Chapter primaryChapter) {
        this.primaryChapter = primaryChapter;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}
