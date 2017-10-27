package com.twiki.processor;

import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Page;
import com.twiki.helper.BookStackTraversal;
import nl.siegmann.epublib.domain.Resource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;

public class ImageUrlBookProcessor implements BookProcessor {
    private static final Log log = LogFactory.getLog(ImageUrlBookProcessor.class);

    private String prePath;
    private File baseFolder;

    private static final String IMAGE_SRC = "src";
    private static final String IMAGE_TAG = "img";

    public ImageUrlBookProcessor(String baseGeneration, String prePath) {
        this.prePath = prePath;
        this.baseFolder = new File(baseGeneration);
    }

    @Override
    public BookStack processBook(BookStack bookStack) throws IOException {
        String imagePath = prePath + bookStack.getSlug() + "/";
        BookStackTraversal.visitPage(bookStack, (page) -> transformImageSource(page, imagePath));
        extractImage(bookStack, imagePath);
        return bookStack;
    }

    private void transformImageSource(Page page, String imagePath) {
        if (StringUtils.containsIgnoreCase(page.getHtmlContent(), IMAGE_TAG)) {
            Document document = Jsoup.parseBodyFragment(page.getHtmlContent());
            document.getElementsByTag(IMAGE_TAG).forEach(imgElement -> {
                imgElement.attr(IMAGE_SRC, imagePath + imgElement.attr(IMAGE_SRC));
                log.info(String.format("Image in page [%s#%s] ", page.getTitle(), imgElement.attr(IMAGE_SRC)));
            });
            page.setHtmlContent(document.outerHtml());

        }
    }

    private void extractImage(BookStack bookStack, String path) {
        bookStack.getOriginalBook().getResources().getResourceMap()
                .entrySet().stream()
                .filter(entry -> isImage(entry.getKey()))
                .forEach(e -> writeImage(path + "/" + e.getKey(), e.getValue()));
    }

    private boolean isImage(String file) {
        return file.endsWith(".png")
                || file.endsWith(".jpg")
                || file.endsWith(".jpeg");
    }

    private void writeImage(String fileout, Resource resource) {
        if (!baseFolder.exists()) {
            throw new UncheckedIOException(new FileNotFoundException(baseFolder.getPath() + "is not existed"));
        }
        try {
            File target = new File(baseFolder, fileout);
            target.getParentFile().mkdirs();
            FileUtils.writeByteArrayToFile(target, resource.getData());
        } catch (IOException e1) {
            throw new UncheckedIOException(e1);
        }
    }
}
