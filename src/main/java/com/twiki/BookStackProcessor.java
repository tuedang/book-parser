package com.twiki;

import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Chapter;
import com.twiki.bookstack.Page;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

public class BookStackProcessor {
    private Book book;
    private BookStack bookStack;

    public BookStackProcessor(Book book) {
        this.book = book;
        this.bookStack = new BookStack();
        try {
            process();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public BookStack get() {
        return this.bookStack;
    }

    private BookStack process() throws IOException {
        bookStack.setTitle(book.getTitle());
        bookStack.setDescription(new String(book.getCoverPage().getData()));
        bookStack.setChapters(new ArrayList<>());

        handle(0, book.getTableOfContents().getTocReferences());
        return bookStack;

    }

    private void handle(Integer level, List<TOCReference> tocReferences) throws IOException {
        level++;
        for (TOCReference tocReference : tocReferences) {
            processTOC(level, tocReference);
            if (tocReference.getChildren() != null) {
                handle(level, tocReference.getChildren());
            }
        }
    }

    private void processTOC(int level, TOCReference tocReference) throws IOException {
        String data = new String(tocReference.getResource().getData());
        Document document = Jsoup.parseBodyFragment(data);

        String htmlContent;
        if (StringUtils.isNotEmpty(tocReference.getFragmentId())) {
            Element fragment = document.select(String.format("div.sect1:has(#%s)",
                    tocReference.getFragmentId())).first();
            if (fragment == null) {
                fragment = document.select(String.format("section:has(#%s)",
                        tocReference.getFragmentId())).first();
            }
            fragment.select("div.titlepage").remove();

            Elements elements = fragment.select("div.sect2");
            elements.forEach(element -> {
                element.prepend("<h2>" + element.attr("title"));
            });
            elements.unwrap();

            htmlContent = fragment.outerHtml();
        } else {
            document.select("div.sect1, div.footnotes").remove();
            Element element = document.getElementById(tocReference.getResourceId().replace("id-", ""));//.html();
            if (element == null) {
                htmlContent = document.select("body").outerHtml();
            } else {
                htmlContent = element.outerHtml();
            }
        }

        switch (level) {
            case 1:
                Chapter chapter = new Chapter();
                chapter.setTitle(tocReference.getTitle());
                chapter.setDescription(htmlContent);
                chapter.setPages(new ArrayList<>());
                bookStack.getChapters().add(chapter);
                break;
            case 2:
                Page page = new Page();
                page.setTitle(tocReference.getTitle());
                page.setHtmlContent(htmlContent);
                List<Chapter> chapters = bookStack.getChapters();
                chapters.get(chapters.size() - 1).getPages().add(page);
        }

    }
}
