package com.beheresoft.website.result;

import com.beheresoft.website.dict.pojo.MetaData;
import lombok.Getter;

/**
 * @author Aladi
 */
public class ArticleResult {

    @Getter
    public MetaData meta;

    @Getter
    private String html;

    public ArticleResult(MetaData metaData, String html) {
        this.meta = metaData;
        this.html = html;
    }

}
