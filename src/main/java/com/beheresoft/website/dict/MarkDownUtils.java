package com.beheresoft.website.dict;

import com.beheresoft.website.config.SystemConfig;
import com.beheresoft.website.dict.pojo.Catalog;
import com.beheresoft.website.dict.pojo.MetaData;
import com.beheresoft.website.dict.pojo.MetaInfo;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.MoreFiles;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Aladi
 */
@Slf4j
public class MarkDownUtils {

    private final static String MARKDOWN_EXT = "md";

    private MarkDownUtils() {

    }

    /**
     * 枚举所有markdown file
     *
     * @param path
     * @param result
     * @throws IOException
     */
    public static void listMarkDownFiles(final Path path, final List<Path> result) throws IOException {
        List<Path> paths = MoreFiles.listFiles(path);
        for (Path p : paths) {
            String ext = Files.getFileExtension(p.getFileName().toString());
            if (p.toFile().isDirectory()) {
                listMarkDownFiles(p, result);
            } else if (ext.equals(MARKDOWN_EXT)) {
                result.add(p);
            }
        }
    }

    public static MetaInfo loadCatalogInfo(final SystemConfig systemConfig) throws IOException {
        final Path path = Paths.get(systemConfig.getMarkdownDir());
        final List<Path> markDowns = Lists.newArrayList();
        listMarkDownFiles(path, markDowns);
        MetaInfo metaInfo = null;
        File file = new File(path.toAbsolutePath().toString() + "/" + systemConfig.getMetaFile());
        if (file.exists()) {
            List<String> contents = Files.readLines(file, Charset.forName("UTF-8"));
            try {
                metaInfo = new Gson().fromJson(Joiner.on("").join(contents), MetaInfo.class);
            } catch (Exception e) {

            }
        } else {
            Files.touch(file);
        }

        if (metaInfo == null) {
            metaInfo = new MetaInfo();
        }

        int hashCode = calcHashCode(markDowns);
        //如果目录内的markdown文件没有变化
        if (metaInfo.getHashCode() == hashCode) {
            return metaInfo;
        }
        Catalog catalog = new Catalog("root");
        metaInfo.setCatalog(catalog);
        List<Path> list = MoreFiles.listFiles(path);
        for (Path p : list) {
            loadCatalogInfo(systemConfig, catalog, p);
        }
        metaInfo.setHashCode(hashCode);
        try {
            Files.write(new Gson().toJson(metaInfo).getBytes(), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return metaInfo;
    }

    private static void loadCatalogInfo(final SystemConfig systemConfig, final Catalog catalog, Path path) throws IOException {
        File file = path.toFile();
        if (file.isDirectory()) {
            Catalog ct = new Catalog(path.getFileName().toString());
            ct.setHashcode(path.hashCode());
            catalog.addCatalog(ct);
            List<Path> lists = MoreFiles.listFiles(path);
            for (Path p : lists) {
                File f = p.toFile();
                if (f.isDirectory()) {
                    loadCatalogInfo(systemConfig, ct, p);
                } else if (isMarkDownFile(p)) {
                    ct.addMetaData(genMetaData(p, systemConfig));
                }
            }
        } else if (isMarkDownFile(path)) {
            catalog.addMetaData(genMetaData(path, systemConfig));
        }
    }

    private static boolean isMarkDownFile(Path path) {
        return MARKDOWN_EXT.equals(Files.getFileExtension(path.getFileName().toString()));
    }

    private static MetaData genMetaData(final Path path, final SystemConfig systemConfig) {
        MetaData metaData = new MetaData(path);
        File file = path.toFile();
        List<String> lines;
        try {
            lines = Files.readLines(file, Charset.forName("UTF-8"));
        } catch (Exception e) {
            return null;
        }
        if (systemConfig.getArticleTitle() == SystemConfig.TITLE.FILE_NAME) {
            metaData.setTitle(Files.getNameWithoutExtension(path.getFileName().toString()));
        } else {
            metaData.setTitle(Iterables.getFirst(lines, path.getFileName().toString()));
        }
        if (!lines.isEmpty()) {
            int size = lines.size();
            String summary = Joiner.on("\n").join(lines.subList(1, systemConfig.getSummaryRows() > size ? size : systemConfig.getSummaryRows()));
            metaData.setSummary(summary.substring(1));
        }
        metaData.setLastModify(file.lastModified());
        return metaData;
    }

    private static int calcHashCode(List<Path> paths) {
        int hashCode = 0;
        for (Path p : paths) {
            if (hashCode == 0) {
                hashCode = p.hashCode();
            } else {
                hashCode = (p.hashCode()) ^ (hashCode >>> 16);
            }
        }
        return hashCode;
    }

}
