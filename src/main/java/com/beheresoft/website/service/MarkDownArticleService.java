package com.beheresoft.website.service;

import com.beheresoft.website.dict.MarkDownUtils;
import com.beheresoft.website.dict.SystemDict;
import com.beheresoft.website.exception.ArticleNotFoundException;
import com.beheresoft.website.result.ArticleResult;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Aladi
 */
@Service
public class MarkDownArticleService {

    private SystemDict systemDict;
    private MarkDownUtils markDownUtils;

    public MarkDownArticleService(SystemDict systemDict, MarkDownUtils markDownUtils) {
        this.systemDict = systemDict;
        this.markDownUtils = markDownUtils;
    }

    public ArticleResult search(long hashcode) throws ArticleNotFoundException {
        ArticleResult result = systemDict.findMetaDataByHashCode(hashcode);
        try {
            String html = this.markDownUtils.parse(result.getMeta());
            result.setHtml(html);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ArticleNotFoundException(hashcode);
        }
    }

    public String getAbout() throws ArticleNotFoundException {
        try {
            return this.markDownUtils.parse(systemDict.getAbout());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ArticleNotFoundException(-1);
        }
    }

}
