package com.beheresoft.website.config;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aladi
 */
@Configuration
public class VladschConfig {

    @Bean
    public MutableDataSet mutableDataSet() {
        MutableDataSet mutableDataSet = new MutableDataSet();
        return mutableDataSet;
    }

    @Bean
    public HtmlRenderer htmlRenderer(MutableDataSet mutableDataSet) {
        return HtmlRenderer.builder(mutableDataSet).build();
    }

    @Bean
    public Parser parser(MutableDataSet mutableDataSet) {
        return Parser.builder(mutableDataSet).build();
    }

}
