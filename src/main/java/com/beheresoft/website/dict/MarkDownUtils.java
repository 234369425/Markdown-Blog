package com.beheresoft.website.dict;

import com.beheresoft.website.config.WebSiteConfig;
import com.beheresoft.website.dict.pojo.Catalog;
import com.beheresoft.website.dict.pojo.MetaData;
import com.beheresoft.website.dict.pojo.MetaInfo;
import com.beheresoft.website.exception.ArticleNotFoundException;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.MoreFiles;
import com.google.gson.Gson;
import com.vladsch.flexmark.ast.Document;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aladi
 */
@Slf4j
@Component
public class MarkDownUtils {

    private final static String MARKDOWN_EXT = "md";
    private HtmlRenderer renderer;
    private Parser parser;
    private WebSiteConfig webSiteConfig;

    private MarkDownUtils(WebSiteConfig webSiteConfig, HtmlRenderer renderer, Parser parser) {
        this.renderer = renderer;
        this.parser = parser;
        this.webSiteConfig = webSiteConfig;
    }

    public Path filterAbout(List<MetaData> metaDataList) {
        int index = -1;
        for (int i = 0; i < metaDataList.size(); i++) {
            if (getFileName(metaDataList.get(i).getPath()).equalsIgnoreCase(webSiteConfig.getAbout())) {
                index = i;
                break;
            }
        }

        Path path = null;
        if (index >= 0) {
            path = Paths.get(metaDataList.get(index).getAbsolutePath());
            metaDataList.remove(index);
        }
        return path;

    }

    /**
     * 按创建时间排序所有文章
     *
     * @param catalog 根目录
     * @return List<MetaData> 所有文章列表
     */
    public List<MetaData> listMarkDownFiles(final Catalog catalog) {
        List<MetaData> result = Lists.newArrayList();
        addMetaDatas(catalog, result);
        return result.stream().
                sorted(Comparator.comparingLong(MetaData::getLastModify).reversed())
                .collect(Collectors.toList());
    }

    private void addMetaDatas(Catalog catalog, List<MetaData> result) {
        if (catalog == null) {
            return;
        }
        result.addAll(catalog.getArticles());
        for (int i = 0; catalog.getSubCatalogs() != null && i < catalog.getSubCatalogs().size(); i++) {
            addMetaDatas(catalog.getSubCatalogs().get(i), result);
        }
    }

    /**
     * 枚举所有markdown file
     *
     * @param path   路径
     * @param result 返回结果ref
     * @throws IOException 抛出异常
     */
    private void listMarkDownFiles(final Path path, final List<Path> result) throws IOException {
        List<Path> paths = MoreFiles.listFiles(path);
        for (Path p : paths) {
            String ext = Files.getFileExtension(getFileName(p));
            if (p.toFile().isDirectory()) {
                listMarkDownFiles(p, result);
            } else if (ext.equals(MARKDOWN_EXT)) {
                result.add(p);
            }
        }
    }

    public MetaInfo loadCatalogInfo() throws IOException {
        log.info("file path: mark down dir[{}] or file index [{}] "
                , webSiteConfig.getMarkdownDir(), webSiteConfig.getIndexDir());
        final Path path = Paths.get(webSiteConfig.getMarkdownDir());
        final List<Path> markDowns = Lists.newArrayList();
        listMarkDownFiles(path, markDowns);
        MetaInfo metaInfo = null;
        File file = new File(path.toAbsolutePath().toString() + "/" + webSiteConfig.getMetaFile());
        if (file.exists()) {
            List<String> contents = Files.readLines(file, webSiteConfig.getCharset());
            try {
                metaInfo = new Gson().fromJson(Joiner.on("").join(contents), MetaInfo.class);
            } catch (Exception e) {
                log.info("读取缓存文件错误{}", contents);
            }
        } else {
            Files.touch(file);
        }

        if (metaInfo == null) {
            metaInfo = new MetaInfo();
        }

        int hashCode = calcFolderHashCode(markDowns);
        //如果目录内的markdown文件没有变化
        if (metaInfo.getHashCode() == hashCode) {
            return metaInfo;
        }
        Catalog catalog = new Catalog("root", 17);
        metaInfo.setCatalog(catalog);
        List<Path> list = MoreFiles.listFiles(path);
        for (Path p : list) {
            loadCatalogInfo(catalog, p);
        }
        metaInfo.setHashCode(hashCode);
        try {
            Files.write(new Gson().toJson(metaInfo).getBytes(webSiteConfig.getCharsetName()), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return metaInfo;
    }

    private String getFileName(Path path) {
        if (path == null) {
            return "";
        }
        path = path.getFileName();
        if (path == null) {
            return "";
        }
        return path.toString();
    }

    private void loadCatalogInfo(final Catalog catalog, Path path) throws IOException {
        File file = path.toFile();
        if (file.isDirectory()) {
            Catalog ct = new Catalog(getFileName(path), catalog.getHashcode());
            catalog.addCatalog(ct);
            List<Path> lists = MoreFiles.listFiles(path);
            for (Path p : lists) {
                File f = p.toFile();
                if (f.isDirectory()) {
                    loadCatalogInfo(ct, p);
                } else if (isMarkDownFile(p)) {
                    ct.addMetaData(genMetaData(p, ct.getHashcode()));
                }
            }
        } else if (isMarkDownFile(path)) {
            catalog.addMetaData(genMetaData(path, catalog.getHashcode()));
        }
    }

    private boolean isMarkDownFile(Path path) {
        return MARKDOWN_EXT.equals(Files.getFileExtension(getFileName(path)));
    }

    private MetaData genMetaData(final Path path, final long parentHashCode) {
        MetaData metaData = new MetaData(path, parentHashCode);
        File file = path.toFile();
        List<String> lines;
        try {
            lines = Files.readLines(file, webSiteConfig.getCharset());
        } catch (Exception e) {
            return null;
        }
        if (webSiteConfig.getArticleTitle() == WebSiteConfig.TITLE.FILE_NAME) {
            metaData.setTitle(Files.getNameWithoutExtension(getFileName(path)));
        } else {
            metaData.setTitle(Iterables.getFirst(lines, getFileName(path)));
        }
        if (!lines.isEmpty()) {
            int size = lines.size();
            int summaryTo = webSiteConfig.getSummaryTo() > size ? size : webSiteConfig.getSummaryTo();
            String summary = Joiner.on("\n").join(lines.subList(webSiteConfig.getSummaryFrom(), summaryTo));
            metaData.setSummary(summary);
            metaData.setSummary(parse(metaData.getSummary()));

        }
        metaData.setLastModify(file.lastModified());
        return metaData;
    }

    public String parse(MetaData metaData) throws IOException {
        if (metaData == null) {
            return "";
        }
        return parse(metaData.getPath());
    }

    public String parse(Path path) throws IOException {
        if (path == null) {
            return "";
        }
        File markDown = path.toFile();
        String content = Joiner.on("\n").join(Files.readLines(markDown, webSiteConfig.getCharset()));
        return parse(content);
    }

    private String parse(String markdown) {
        if (markdown == null) {
            return "";
        }
        Document document = parser.parse(markdown);
        return renderer.render(document);
    }

    private int calcFolderHashCode(List<Path> paths) {
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
