package com.twiki.processor;

import com.google.common.collect.Lists;

import java.util.List;

public class DefaultPipelineBookProcessor extends PipelineBookProcessor {

    public DefaultPipelineBookProcessor() {
        super(createDefaultPipelineBookProcessors());
    }

    private static List<BookProcessor> createDefaultPipelineBookProcessors() {
        return Lists.newArrayList(
                new SlugBookProcessor(),
                new GroupIntroductionPageProcessor(),
                new TextContentChapterBookProcessor()
        );
    }
}
