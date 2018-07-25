package com.beheresoft.website.service;

import com.beheresoft.website.dict.MarkDownUtils;
import com.beheresoft.website.dict.SystemDict;
import com.beheresoft.website.dict.pojo.MetaData;
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

    public String search(long hashcode) {
        MetaData metaData = systemDict.findMetaDataByHashCode(hashcode);
        try {
            return this.markDownUtils.parse(metaData);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
