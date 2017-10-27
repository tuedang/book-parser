package com.twiki;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.Chapter;
import com.twiki.bookstack.ContentEntity;
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

        for (ContentEntity contentEntity : bookStack.getContents()) {
            int contentEntityIndex = bookStack.getContents().indexOf(contentEntity);

            if (contentEntity.getType().equals("page")) {
                FileUtils.writeStringToFile(
                        new File(bf, String.format("%s__%s.html", contentEntityIndex, escape(contentEntity.getTitle()))),
                        contentEntity.getHtmlContent());
            } else {
                Chapter chapter = (Chapter) contentEntity;
                File bfc = new File(bf, String.format("%s__%s", contentEntityIndex, escape(chapter.getTitle())));
                bfc.mkdirs();
                //chapter description
                FileUtils.writeStringToFile(
                        new File(bfc, String.format("%s__%s.htm", 0, escape(chapter.getTitle()))),
                        contentEntity.getHtmlContent());
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

    public static void generateJson(BookStack bookStack, File baseFolder) throws IOException {
        if (!baseFolder.exists()) {
            throw new FileNotFoundException(baseFolder.getAbsolutePath());
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.writeValue(new File(baseFolder, escape(bookStack.getTitle())+"-books.json"), bookStack);
    }

}
