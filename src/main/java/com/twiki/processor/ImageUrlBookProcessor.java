package com.twiki.processor;

import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Page;
import com.twiki.helper.BookStackTraversal;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ImageUrlBookProcessor implements BookProcessor {
    private String prePath;
    private static final String IMAGE_SRC = "src";

    public ImageUrlBookProcessor(String prePath) {
        this.prePath = prePath;
    }

    @Override
    public BookStack processBook(BookStack bookStack) throws IOException {
        String imagePath = prePath + bookStack.getSlug() + "/";
        BookStackTraversal.visitPage(bookStack, (page, index) -> transformImageSource(page, imagePath));
        return bookStack;
    }

    private void transformImageSource(Page page, String imagePath) {
        Document document = Jsoup.parseBodyFragment(page.getHtmlContent());
        Elements images = document.getElementsByTag("img");
        if (!images.isEmpty()) {
            images.attr(IMAGE_SRC, imagePath + images.attr(IMAGE_SRC));
            page.setHtmlContent(document.outerHtml());
        }
    }
}
