package com.beheresoft.website.dict;

import com.beheresoft.website.config.WebSiteConfig;
import com.beheresoft.website.dict.pojo.MetaData;
import com.beheresoft.website.dict.pojo.MetaInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

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


    public SystemDict(WebSiteConfig systemConfig, MarkDownUtils markDownUtils) {
        this.websiteConfig = systemConfig;
        this.markDownUtils = markDownUtils;
    }

    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            markdownMetas = markDownUtils.listMarkDownFiles(websiteConfig);
            metaInfo = markDownUtils.loadCatalogInfo(websiteConfig);
            System.out.println("");
        } catch (IOException e) {
            log.error("file path: mark down dir[{}] or file index [{}] not exists , cause: {} "
                    , websiteConfig.getMarkdownDir(), websiteConfig.getIndexDir(), e.getMessage());

            System.exit(5005);
        }
        System.out.println(websiteConfig.getMarkdownDir());
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
}
