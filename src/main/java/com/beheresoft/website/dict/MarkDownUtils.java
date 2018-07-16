package com.beheresoft.website.dict;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;

/**
 * @author Aladi
 */
@Slf4j
public class MarkDownUtils {

    private final static String MARKDOWN_EXT = "md";

    private MarkDownUtils() {

    }

    public static List<String> list(ImmutableList<Path> paths) {
        List<String> result = Lists.newLinkedList();
        for (Path path : paths) {
            String ext = Files.getFileExtension(path.getFileName().toString());
            log.info(ext);
        }
        return result;
    }
}
