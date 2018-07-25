package com.beheresoft.website.exception;

import lombok.Getter;

/**
 * @author Aladi
 */
public class ArticleNotFoundException extends Exception {

    @Getter
    private long hashcode;

    public ArticleNotFoundException(long hashcode) {
        this.hashcode = hashcode;
    }

}
