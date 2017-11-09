package com.twiki.pdf;

import com.google.common.collect.Lists;
import com.twiki.processor.*;

import java.util.List;

public class PDFPipelineBookProcessor extends PipelineBookProcessor {
    public PDFPipelineBookProcessor() {
        super(createDefaultPipelineBookProcessors());
    }

    private static List<BookProcessor> createDefaultPipelineBookProcessors() {
        return Lists.newArrayList(
        );
    }
}
