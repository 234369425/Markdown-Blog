package com.beheresoft.website.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;

/**
 * @author Aladi
 */
@Configuration
@ConfigurationProperties("website")
@Getter
@Setter
public class WebSiteConfig {

    private String markdownDir;
    private String indexDir;
    private String metaFile = "metaInfo.json";
    private String charset = "UTF-8";
    private String about = "About.md";
    private TITLE articleTitle = TITLE.FILE_NAME;
    private Page page;
    private int summaryRows = 4;
    private GitTalk gitTalk;

    public int getSummaryFrom() {
        if (articleTitle == TITLE.FILE_NAME) {
            return 1;
        }
        return 0;
    }

    public int getSummaryTo() {
        if (articleTitle == TITLE.FILE_NAME) {
            return summaryRows;
        }
        return summaryRows + 1;
    }

    public String getCharsetName() {
        return charset;
    }

    public Charset getCharset() {
        return Charset.forName(charset);
    }

    public enum TITLE {
        /**
         * 文件名称
         */
        FILE_NAME,
        /**
         * 第一行
         */
        FIRST_ROW
    }

    @Getter
    @Setter
    public static class GitTalk {
        private String owner;
        private String repo;
        private Oauth oauth;
    }

    @Getter
    @Setter
    public static class Oauth {
        private String clientId;
        private String secret;
    }

    @Getter
    @Setter
    public static class Page {
        private String logo;
        private String footer;
        private String author;
    }

}