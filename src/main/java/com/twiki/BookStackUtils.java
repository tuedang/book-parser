package com.twiki;

import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Chapter;
import com.twiki.bookstack.Page;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BookStackUtils {
    public static void generateFolderHtml(BookStack bookStack, File baseFolder) throws IOException {
        if (!baseFolder.exists()) {
            throw new FileNotFoundException(baseFolder.getAbsolutePath());
        }
        File bf = new File(baseFolder, escape(bookStack.getTitle()));
        bf.mkdirs();

        for (Chapter chapter : bookStack.getChapters()) {
            int chapterIndex = bookStack.getChapters().indexOf(chapter);

            if (chapter.getPages().isEmpty()) {
                FileUtils.writeStringToFile(
                        new File(bf, String.format("%s__%s.html", chapterIndex, escape(chapter.getTitle()))),
                        chapter.getDescription());
            } else {
                File bfc = new File(bf, String.format("%s__%s", chapterIndex, escape(chapter.getTitle())));
                bfc.mkdirs();
                //chapter description
                FileUtils.writeStringToFile(
                        new File(bfc, String.format("%s__%s.htm", 0, escape(chapter.getTitle()))),
                        chapter.getDescription());
                for (Page page : chapter.getPages()) {
                    FileUtils.writeStringToFile(
                            new File(bfc, String.format("%s__%s.html", chapter.getPages().indexOf(page) + 1, escape(page.getTitle()))),
                            page.getHtmlContent());
                }
            }

        }
    }
    private static String escape(String folder) {
        return folder
                .replace("/","_")
                .replace("?", "")
                .replace(":", "");
    }
}
