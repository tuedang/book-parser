package com.twiki.processor;

import com.google.common.collect.Lists;

import java.util.List;

public class DefaultPipelineBookProcessor extends PipelineBookProcessor {
    public DefaultPipelineBookProcessor(BookStackInitializer bookStackInitializer) {
        super(createDefaultPipelineBookProcessors());
        this.bookProcessors.add(0, bookStackInitializer);
    }

    public DefaultPipelineBookProcessor() {
        super(createDefaultPipelineBookProcessors());
    }

    private static List<BookProcessor> createDefaultPipelineBookProcessors() {
        return Lists.newArrayList(
                new SlugBookProcessor(),
                new TextContentChapterBookProcessor(),
                new ImageUrlBookProcessor("/Data/books/books_json", "/book_res/"),
                new TextContentChapterBookProcessor()
        );
    }
}
