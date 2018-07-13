package com.beheresoft.website.service;

import com.vladsch.flexmark.ast.Document;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.springframework.stereotype.Service;

/**
 * @author Aladi
 */
@Service
public class MarkDownArticleService {

    private HtmlRenderer renderer;
    private Parser parser;

    public MarkDownArticleService(HtmlRenderer renderer, Parser parser) {
        this.renderer = renderer;
        this.parser = parser;
    }

    private String parse(String markdown) {
        Document document = parser.parse(markdown);
        return renderer.render(document);
    }

}
