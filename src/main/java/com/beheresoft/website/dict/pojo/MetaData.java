package com.beheresoft.website.dict.pojo;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Aladi
 */
@Getter
public class MetaData {

    public MetaData(Path path, long parentHash) {
        this.absolutePath = path.toAbsolutePath().toString();
        this.path = path;
        this.hash = HashCode.asLong(path.toFile().getName(), parentHash);
    }

    private long hash;
    @Setter
    private String title;
    @Setter
    private String summary;
    private String absolutePath;
    @Setter
    private long lastModify;
    private transient Path path;

    public Path getPath() {
        if (path == null) {
            path = Paths.get(absolutePath);
        }
        return path;
    }

}
