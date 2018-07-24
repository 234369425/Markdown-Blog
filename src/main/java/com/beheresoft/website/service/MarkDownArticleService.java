package com.beheresoft.website.service;

import com.beheresoft.website.dict.SystemDict;
import com.vladsch.flexmark.ast.Document;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.springframework.stereotype.Service;

/**
 * @author Aladi
 */
@Service
public class MarkDownArticleService {

    private SystemDict systemDict;

    public MarkDownArticleService(SystemDict systemDict) {
        this.systemDict = systemDict;
    }

    public String search(long hashcode) {

        systemDict.findMetaDataByHashCode(hashcode);

    }

}
