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
    private TITLE articleTitle;
    private int summaryRows = 4;
    private GitTalk gitTalk;

    public Charset getCharset() {
        return Charset.forName(charset);
    }

    public enum TITLE {
        FILE_NAME,
        FIRST_ROW
    }

    @Getter
    @Setter
    public static class GitTalk {
        private String owner;
        private String repo;
        private OAuth oauth;
    }

    @Getter
    @Setter
    public static class OAuth {
        private String clientId;
        private String secret;
    }

}
