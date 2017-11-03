package com.twiki.processor;

import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Chapter;
import com.twiki.bookstack.ContentEntity;
import com.twiki.bookstack.Page;
import com.twiki.helper.BookStackTraversal;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ChapterLinkingBookProcessor implements BookProcessor {
    private static Log LOG = LogFactory.getLog(ChapterLinkingBookProcessor.class);

    @Override
    public BookStack processBook(BookStack bookStack) throws IOException {
        List<String> chapterResources = bookStack.getBookMetaContextHolder().getChapterResource();
        if (chapterResources.isEmpty()) {
            return bookStack;
        }
        String cssChapterSelector = chapterResources.stream()
                .map(s -> String.format("a[href*='%s']", s))
                .collect(Collectors.joining(","));

        BookStackTraversal.visitPage(bookStack, (page -> {
            Document pageDocument = Jsoup.parseBodyFragment(page.getHtmlContent());

            Elements link2Chapters = pageDocument.select(cssChapterSelector);
            link2Chapters.forEach(l -> {
                String lChapter = l.attr("href").split("#")[0];
                Chapter chapterLink = bookStack.getBookMetaContextHolder().getChapter(lChapter);

                if (l.attr("href").equalsIgnoreCase(lChapter)) { //link to chapter
                    String link = makeLink(bookStack, chapterLink, null);
                    l.attr("href", link);
                    LOG.debug(chapterLink.getTitle() + "->" + link);
                } else { // link to page
                    String pLink = l.attr("href").split("#")[1];
                    Page pageLink = chapterLink.getPages().stream()
                            .filter(p -> Jsoup.parseBodyFragment(p.getHtmlContent()).getElementById(pLink) != null)
                            .findFirst()
                            .orElse(null); //Null when it belong to chapter description

                    if (pageLink != null) {
                        String link = makeLink(bookStack, pageLink, pLink);
                        l.attr("href", link);
                        LOG.debug(page.getTitle() + "->" + link);
                    } else {
                        String link = makeLink(bookStack, chapterLink, pLink);
                        LOG.debug(chapterLink.getTitle() + "->" + link);
                        l.attr("href", link);
                    }
                }
            });
            page.setHtmlContent(pageDocument.outerHtml());
        }));
        return bookStack;
    }

    private String makeLink(BookStack bookStack, ContentEntity contentEntity, String hrefId) {
        String link;
        if (contentEntity instanceof Chapter) {
            link = "../chapter/" + contentEntity.getSlug();
        } else {
            link = "/books/" + bookStack.getSlug() + "/page/" + contentEntity.getSlug();
        }
        if (StringUtils.isNotEmpty(hrefId)) {
            link += "#" + hrefId;
        }
        return link;
    }
}
