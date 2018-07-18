package com.beheresoft.website.dict.pojo;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Aladi
 */
@Setter
public class Catalog {

    @Getter
    private String name;
    private List<Catalog> subCatalogs = Lists.newArrayList();
    private List<MetaData> articles = Lists.newArrayList();

    public Catalog(String name) {
        this.name = name;
    }

    public void addCatalog(Catalog catalog) {
        this.subCatalogs.add(catalog);
    }

    public void addMetaData(MetaData metaData) {
        this.articles.add(metaData);
    }

    public int getArticleCount() {
        return articles.size();
    }

}
