package com.twiki.processor;

import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Chapter;
import com.twiki.bookstack.ContentEntity;
import com.twiki.bookstack.Page;
import com.twiki.helper.ResourceHelper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HtmlSplitterBookStackProcessor implements BookProcessor {
    private File baseFolder;

    public HtmlSplitterBookStackProcessor(String baseFolder) {
        this.baseFolder = new File(baseFolder);
    }

    @Override
    public BookStack processBook(BookStack bookStack) throws IOException {
        generateFolderHtml(bookStack, baseFolder);
        return bookStack;
    }

    private void generateFolderHtml(BookStack bookStack, File baseFolder) throws IOException {
        if (!baseFolder.exists()) {
            throw new FileNotFoundException(baseFolder.getAbsolutePath());
        }
        File bookBaseFolder = new File(baseFolder, ResourceHelper.escape(bookStack.getTitle()));
        bookBaseFolder.mkdirs();

        for (ContentEntity contentEntity : bookStack.getContents()) {
            int contentEntityIndex = bookStack.getContents().indexOf(contentEntity);

            if (contentEntity.getType().equals("page")) {
                writeToFile(bookBaseFolder, contentEntityIndex, contentEntity);
            } else {
                Chapter chapter = (Chapter) contentEntity;
                File chapterFolder = new File(bookBaseFolder, String.format("%s__%s", contentEntityIndex, ResourceHelper.escape(chapter.getTitle())));
                chapterFolder.mkdirs();
                //chapter description
                writeToFile(chapterFolder, 0, chapter);
                for (Page page : chapter.getPages()) {
                    writeToFile(chapterFolder, chapter.getPages().indexOf(page) + 1, page);
                }
            }
        }
    }

    private static void writeToFile(File base, int index, ContentEntity contentEntity) throws IOException {
        FileUtils.writeStringToFile(
                new File(base, String.format("%s__%s.html", index, ResourceHelper.escape(contentEntity.getTitle()))),
                contentEntity.getHtmlContent());
    }

}
