package com.beheresoft.website.dict;

import com.beheresoft.website.config.WebSiteConfig;
import com.google.common.base.Joiner;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Aladi
 */
@Component
@Slf4j
public class ViewerCounter {

    private Map<Long, AtomicInteger> counter;
    private final String filePath;
    private final String charset;

    public ViewerCounter(WebSiteConfig webSiteConfig) {
        filePath = webSiteConfig.getIndexDir() + "/" + webSiteConfig.getCounterFile();
        charset = webSiteConfig.getCharsetName();
        Path path = Paths.get(filePath);
        File file = path.toFile();
        if (file.exists()) {
            try {
                Type type = new TypeToken<Map<Long, AtomicInteger>>() {
                }.getType();
                counter = new Gson().fromJson(Joiner.on("").join(Files.readLines(file, webSiteConfig.getCharset())), type);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Files.createParentDirs(file);
                Files.touch(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            counter = new ConcurrentHashMap<>();
        }

        Runtime.getRuntime().addShutdownHook(new Flush());
    }

    public int add(Long hash) {
        return counter.computeIfAbsent(hash, o -> new AtomicInteger(0)).incrementAndGet();
    }

    @Scheduled(fixedDelay = 2 * 60 * 1000)
    void flush() {
        log.info("simple task flush data ~");
        try {
            File file = Paths.get(filePath).toFile();
            Files.write(new Gson().toJson(counter).getBytes(charset), file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Flush extends Thread {
        @Override
        public void run() {
            log.info("shut down hook, flush data to disk ~~~ ");
            flush();
        }
    }


}
