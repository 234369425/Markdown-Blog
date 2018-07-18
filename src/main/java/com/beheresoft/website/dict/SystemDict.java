package com.beheresoft.website.dict;

import com.beheresoft.website.config.SystemConfig;
import com.beheresoft.website.dict.pojo.MetaInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Aladi
 */
@Component
@Slf4j
public class SystemDict implements CommandLineRunner {

    private final SystemConfig systemConfig;
    private MetaInfo metaInfo;

    public SystemDict(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
    }

    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            Path markDownRootPath = Paths.get(systemConfig.getMarkdownDir());
            List<Path> paths = Lists.newArrayList();
            MarkDownUtils.listMarkDownFiles(markDownRootPath, paths);
            for (Path p : paths) {
                log.info("File Name : {} ,absolute:{},hash:{}", p.getFileName(), p.isAbsolute(), p.hashCode());
            }
            metaInfo = MarkDownUtils.loadCatalogInfo(systemConfig);
            System.out.println("");
        } catch (IOException e) {
            log.error("file path: mark down dir[{}] or file index [{}] not exists , cause: {} "
                    , systemConfig.getMarkdownDir(), systemConfig.getIndexDir(), e.getMessage());

            System.exit(5005);
        }
        System.out.println(systemConfig.getMarkdownDir());
    }
}
