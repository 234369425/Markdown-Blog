package com.beheresoft.website.dict;

import com.beheresoft.website.config.WebSiteConfig;
import com.beheresoft.website.dict.pojo.Catalog;
import com.beheresoft.website.dict.pojo.MetaData;
import com.beheresoft.website.dict.pojo.MetaInfo;
import com.beheresoft.website.exception.ArticleNotFoundException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Aladi
 */
@Component
@Slf4j
public class SystemDict implements CommandLineRunner {

    private MetaInfo metaInfo;
    @Getter
    private List<MetaData> markdownMetas;
    private MarkDownUtils markDownUtils;
    private ThymeleafViewResolver viewResolver;
    private transient ApplicationContext applicationContext;
    private transient WebSiteConfig.Page page;
    private transient WebSiteConfig.GiTalk giTalk;
    private Path about;


    public SystemDict(MarkDownUtils markDownUtils,
                      WebSiteConfig webSiteConfig,
                      ThymeleafViewResolver viewResolver, ApplicationContext applicationContext) {
        this.markDownUtils = markDownUtils;
        this.viewResolver = viewResolver;
        this.applicationContext = applicationContext;
        this.page = webSiteConfig.getPage();
        this.giTalk = webSiteConfig.getGiTalk();
    }

    @Override
    public void run(String... args) throws Exception {
        initDict();
    }

    /**
     * 每小时刷新列表载入新文章
     */
    @Scheduled(fixedDelay = 60 * 60 * 1000)
    void initDict() {
        try {
            metaInfo = markDownUtils.loadCatalogInfo();
            markdownMetas = markDownUtils.listMarkDownFiles(metaInfo.getCatalog());
            about = markDownUtils.filterAbout(markdownMetas);
            resolverStaticVariables();
        } catch (IOException e) {
            SpringApplication.exit(applicationContext);
        }
    }

    /**
     * 设置thymeleaf全局变量
     */
    private void resolverStaticVariables() {
        //顺便设置静态导航栏
        int lastIndex = page.getNewestSize() > markdownMetas.size() ? markdownMetas.size() : page.getNewestSize();
        viewResolver.setStaticVariables(ImmutableMap.of("navMenu", this.metaInfo.getCatalog(),
                "pageInfo", page,
                "giTalk", giTalk,
                "newest", markdownMetas.subList(0, lastIndex)));
    }

    public Page<MetaData> listCatalogMetas(Long hashCode, Pageable pageable) {
        Catalog catalog = search(this.metaInfo.getCatalog(), hashCode);
        List<MetaData> metaData = catalog.getArticles();
        return PageIndex.of(pageable, page.getPageSize(), metaData).toPage();
    }

    private Catalog search(Catalog catalog, Long hashCode) {
        if (catalog.getHashcode() == hashCode) {
            return catalog;
        }

        while (catalog.getSubCatalogs() != null && !catalog.getSubCatalogs().isEmpty()) {
            for (Catalog c : catalog.getSubCatalogs()) {
                Catalog cr = search(c, hashCode);
                if (cr != null) {
                    return cr;
                }
            }
        }
        return null;
    }

    public Page<MetaData> listMetas(Pageable pageable) {
        return PageIndex.of(pageable, page.getPageSize(), markdownMetas).toPage();
    }

    /**
     * 根据文章hash查找文章
     *
     * @param hashcode 文件的hashcode
     * @return 找到的MetaData
     */
    public MetaData findMetaDataByHashCode(final long hashcode) throws ArticleNotFoundException {
        for (MetaData metaData : markdownMetas) {
            if (metaData.getHash() == hashcode) {
                return metaData;
            }
        }
        throw new ArticleNotFoundException(hashcode);
    }

    public Path getAbout() {
        return about;
    }

    private static class PageIndex {
        private int first;
        private int last;
        private int number;
        private int size;
        private int rowCount;
        private List<MetaData> dataList;

        private static final int EMPTY_INDEX = -1;
        private static final Page<MetaData> EMPTY = new PageImpl<>(Lists.newArrayList(), PageRequest.of(0, 10), 0);

        private PageIndex(Pageable pageable, int pageSize, List<MetaData> list) {
            if (list == null || list.isEmpty()) {
                first = EMPTY_INDEX;
                return;
            }
            number = pageable.getPageNumber();
            first = number * pageSize;
            dataList = list;
            rowCount = list.size();
            int maxIndex = rowCount - 1;
            first = first >= maxIndex ? maxIndex : first;
            size = pageSize;
            last = maxIndex < first + pageSize ? maxIndex + 1 : first + pageSize;
        }

        static PageIndex of(Pageable pageable, int pageSize, List<MetaData> list) {
            return new PageIndex(pageable, pageSize, list);
        }

        Page<MetaData> toPage() {
            if (first == EMPTY_INDEX || dataList == null || dataList.isEmpty()) {
                return EMPTY;
            }
            return new PageImpl<>(dataList.subList(first, last),
                    PageRequest.of(number, size),
                    rowCount);
        }
    }
}
