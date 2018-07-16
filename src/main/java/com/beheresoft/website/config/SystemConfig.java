package com.beheresoft.website.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aladi
 */
@Configuration
@ConfigurationProperties("website")
@Getter
@Setter
public class SystemConfig {

    private String markdownDir;
    private String indexDir;
    private String metaFile;

}
