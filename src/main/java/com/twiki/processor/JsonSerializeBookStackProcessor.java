package com.twiki.processor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.twiki.helper.ResourceHelper;
import com.twiki.bookstack.BookStack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class JsonSerializeBookStackProcessor implements BookProcessor {
    private File baseFolder;

    public JsonSerializeBookStackProcessor(String baseFolder) {
        this.baseFolder = new File(baseFolder);
    }

    @Override
    public BookStack processBook(BookStack bookStack) throws IOException {
        generateJson(bookStack, baseFolder);
        return bookStack;
    }

    private static void generateJson(BookStack bookStack, File baseFolder) throws IOException {
        if (!baseFolder.exists()) {
            throw new FileNotFoundException(baseFolder.getAbsolutePath());
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.writeValue(new File(baseFolder, ResourceHelper.escape(bookStack.getTitle()) + ".json"), bookStack);
    }

}
