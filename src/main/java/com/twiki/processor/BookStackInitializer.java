package com.twiki.processor;

import com.twiki.ResourceHelper;
import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Chapter;
import com.twiki.bookstack.Page;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.util.CollectionUtil;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class BookStackInitializer implements BookProcessor{
    private Book book;
    private BookStack bookStack;

    public BookStackInitializer(Book book) {
        this.book = book;
        processBook(null);
    }

    public BookStack get() {
        return this.bookStack;
    }

    @Override
    public BookStack processBook(BookStack bookStackInit) {
        if (bookStackInit != null) {
            throw new IllegalArgumentException("This is init state, it must be null");
        }
        if (bookStack != null) {
            return bookStack;
        }
        this.bookStack = new BookStack(book.getTitle(), ResourceHelper.getData(book.getCoverPage()));
        handle(0, book.getTableOfContents().getTocReferences());
        return bookStack;
    }

    private void handle(Integer level, List<TOCReference> tocReferences)  {
        level++;
        for (TOCReference tocReference : tocReferences) {
            processTOC(level, tocReference);
            if (tocReference.getChildren() != null) {
                handle(level, tocReference.getChildren());
            }
        }
    }

    private void processTOC(int level, TOCReference tocReference) {
        Document document = Jsoup.parseBodyFragment(ResourceHelper.getData(tocReference.getResource()));

        String htmlContent;
        if (StringUtils.isNotEmpty(tocReference.getFragmentId())) {
            Element fragment = document.getElementById(tocReference.getFragmentId());
            fragment = closest(fragment, "div.sect1, section, body");
            fragment.select("div.titlepage").remove();
            for (TOCReference toc : tocReference.getChildren()) {
                Element f = fragment.getElementById(toc.getFragmentId());
                if (f != null) f.remove();
            }

            //transform sect2 to h2 (match with bookstack
            Elements elements = fragment.select("div.sect2");
            elements.forEach(element -> element.prepend("<h2>" + element.attr("title")));
            elements.unwrap();

            //adhoc?
            fragment.select("h1").remove();//object-oriented vs function programming

            htmlContent = fragment.html();

        } else {
            document.select("div.sect1, div.footnotes").remove();
            Element element = document.getElementById(tocReference.getResourceId().replace("id-", ""));
            if (element == null) {
                htmlContent = document.select("section").html();
            } else {
                htmlContent = element.html();
            }
        }

        switch (level) {
            case 1:
                if (CollectionUtil.isEmpty(tocReference.getChildren())) {
                    bookStack.addPage(new Page(tocReference.getTitle(), htmlContent));
                } else {
                    bookStack.addChapter(new Chapter(tocReference.getTitle(), htmlContent));
                }
                break;
            case 2:
                Page page = new Page(tocReference.getTitle(), htmlContent);
                bookStack.getLastChapter().addPage(page);
                break;
        }

    }

    private Element closest(Element element, String cssSelector) {
        while (!element.is(cssSelector)) {
            element = element.parent();
        }
        return element;
    }

}
