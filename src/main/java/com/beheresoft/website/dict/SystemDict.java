package com.beheresoft.website.dict;

import com.beheresoft.website.config.WebSiteConfig;
import com.beheresoft.website.dict.pojo.MetaData;
import com.beheresoft.website.dict.pojo.MetaInfo;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.io.IOException;
import java.util.List;

/**
 * @author Aladi
 */
@Component
@Slf4j
public class SystemDict implements CommandLineRunner {

    private final WebSiteConfig websiteConfig;
    private MetaInfo metaInfo;
    private List<MetaData> markdownMetas;
    private MarkDownUtils markDownUtils;
    private ThymeleafViewResolver viewResolver;


    public SystemDict(WebSiteConfig systemConfig, MarkDownUtils markDownUtils, ThymeleafViewResolver viewResolver) {
        this.websiteConfig = systemConfig;
        this.markDownUtils = markDownUtils;
        this.viewResolver = viewResolver;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            metaInfo = markDownUtils.loadCatalogInfo();
            markdownMetas = markDownUtils.listMarkDownFiles(metaInfo.getCatalog());
            System.out.println("");
        } catch (IOException e) {

            System.exit(5005);
        }
        //顺便设置静态导航栏
        List<String> folders = this.metaInfo.getCatalog().catalogs();
        int lastIndex = 10 > markdownMetas.size() ? markdownMetas.size() : 10;
        viewResolver.setStaticVariables(ImmutableMap.of("navMenu", folders,
                "newest", markdownMetas.subList(0, lastIndex)));
        ;
    }

    public Page<MetaData> listMetas(Pageable pageable) {
        int number = pageable.getPageNumber();
        int size = pageable.getPageSize();
        int first = number * size;
        int maxIndex = markdownMetas.size() - 1;
        first = first >= maxIndex ? maxIndex : first;
        int last = maxIndex < first + size ? maxIndex + 1 : first + size;
        return new PageImpl<>(markdownMetas.subList(first, last), pageable, markdownMetas.size());
    }

    /**
     * 根据文章hash查找文章
     * @param hashcode
     * @return
     */
    public MetaData findMetaDataByHashCode(final long hashcode) {
        for (MetaData metaData : markdownMetas) {
            if (metaData.getHash() == hashcode) {
                return metaData;
            }
        }
        return null;
    }
}
