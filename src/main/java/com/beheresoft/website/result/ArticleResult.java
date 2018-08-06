package com.beheresoft.website.result;

import com.beheresoft.website.dict.pojo.MetaData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Aladi
 */
@NoArgsConstructor
@Getter
@Setter
public class ArticleResult {

    private MetaData meta;

    private MetaData prev;

    private MetaData next;

    private String html;

}
