package com.beheresoft.website.dict;

import com.beheresoft.website.config.SystemConfig;
import com.google.common.collect.ImmutableList;
import com.google.common.io.MoreFiles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Aladi
 */
@Component
@Slf4j
public class DictInit implements CommandLineRunner {

    private final SystemConfig systemConfig;

    public DictInit(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            Path path = Paths.get(systemConfig.getMarkdownDir());
            ImmutableList<Path> paths = MoreFiles.listFiles(path);
            MarkDownUtils.list(paths);
            for (Path p : paths) {
                log.info("File Name : {} ,absolute:{},hash:{}", p.getFileName(), p.isAbsolute(), p.hashCode());
            }
        } catch (IOException e) {
            log.error("file path: mark down dir[{}] or file index [{}] not exists , cause: {} "
                    , systemConfig.getMarkdownDir(), systemConfig.getIndexDir(), e.getMessage());

            System.exit(5005);
        }
        System.out.println(systemConfig.getMarkdownDir());
    }
}
