package com.beheresoft.website.dict;

import com.beheresoft.website.config.SystemConfig;
import com.beheresoft.website.pojo.MetaData;
import com.beheresoft.website.pojo.MetaInfo;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Aladi
 */
@Slf4j
public class MarkDownUtils {

    private final static String MARKDOWN_EXT = "md";

    private MarkDownUtils() {

    }

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

    public static MetaInfo generateMetaInfo(final Path path, final SystemConfig systemConfig, final List<Path> markDowns) {
        MetaInfo metaInfo = null;
        File file = new File(path.toAbsolutePath().toString() + "/" + systemConfig.getMetaFile());
        if (file.exists()) {
            List<String> contents = Lists.newArrayList();
            try {
                contents = Files.readLines(file, Charset.forName("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                metaInfo = new Gson().fromJson(Joiner.on("").join(contents), MetaInfo.class);
            } catch (Exception e) {

            }
        } else {
            metaInfo = new MetaInfo();
            generateMetaInfo(markDowns, metaInfo, systemConfig);
        }

        int hashCode = calcHashCode(markDowns);
        if (metaInfo.getHashCode() == hashCode) {
            return metaInfo;
        }

        return metaInfo;
    }

    private static void generateMetaInfo(List<Path> paths, MetaInfo metaInfo, final SystemConfig systemConfig) {
        if (Boolean.TRUE.equals(metaInfo.getHashCode())) {
            return;
        }
        boolean useFileName = systemConfig.getArticleTitle() == SystemConfig.TITLE.FILE_NAME;
        for (Path path : paths) {
            MetaData metaData = new MetaData(path);
            File file = path.toFile();
            List<String> lines = null;
            try {
                lines = Files.readLines(file, Charset.forName("UTF-8"));
            } catch (Exception e) {
                continue;
            }
            if (useFileName) {
                metaData.setTitle(path.getFileName().toString());
            } else {
                metaData.setTitle(Iterables.getFirst(lines, path.getFileName().toString()));
            }
            if (!lines.isEmpty()) {
                int size = lines.size();
                String summary = Joiner.on("").join(lines.subList(1, systemConfig.getSummaryRows() > size ? size : systemConfig.getSummaryRows()));
                metaData.setSummary(summary);
            }
            metaInfo.addMetaData(metaData);
        }
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
