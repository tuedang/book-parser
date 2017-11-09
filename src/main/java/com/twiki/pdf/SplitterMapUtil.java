package com.twiki.pdf;

import com.twiki.bookstack.BookStack;
import com.twiki.bookstack.ContentEntity;
import com.twiki.helper.BookStackTraversal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplitterMapUtil {
    public static Map<Integer, String> createSplitterMap(BookStack pdfBookStack) {
        Map<Integer, String> spliterMap = new HashMap<>();

        List<ContentEntity> contentEntities = new ArrayList<>();
        BookStackTraversal.visitContent(pdfBookStack, (contentEntity, level) -> {
            if (level == BookStackTraversal.FIRST_LEVEL) {
                contentEntities.add(contentEntity);
            }
        });
        for (int i = 0; i < contentEntities.size()-1; i++) {
            spliterMap.put(Integer.valueOf(contentEntities.get(i + 1).getHtmlContent())-1, contentEntities.get(i).getTitle());
        }
        spliterMap.put(437, "END");
        return spliterMap;
    }
}
