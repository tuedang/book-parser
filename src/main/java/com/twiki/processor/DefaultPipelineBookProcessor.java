package com.twiki.processor;

import com.google.common.collect.Lists;
import com.twiki.adhoc.Spring5MicroserviceBookProcessor;

import java.util.List;

public class DefaultPipelineBookProcessor extends PipelineBookProcessor {

    public DefaultPipelineBookProcessor() {
        super(createDefaultPipelineBookProcessors());
    }

    private static List<BookProcessor> createDefaultPipelineBookProcessors() {
        return Lists.newArrayList(
                new CoverBookProcessor(),
                new GroupIntroductionPageProcessor(),
                new TextContentChapterBookProcessor(),
                new SlugBookProcessor(),
                new HeaderIdAssignBookProcessor(),
                new ChapterLinkingBookProcessor(),
                new IndexAssignmentBookProcessor(),

                //adhoc
                new Spring5MicroserviceBookProcessor()
        );
    }
}
