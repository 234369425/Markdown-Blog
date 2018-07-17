package com.beheresoft.website.pojo;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

/**
 * @author Aladi
 */
@Getter
public class MetaData {

    public MetaData(Path path) {
        this.hash = path.hashCode();
        this.absolutePath = path.toAbsolutePath().toString();
        this.path = path;
    }

    private int hash;
    @Setter
    private String title;
    @Setter
    private String summary;
    private String absolutePath;
    @Setter
    private long lastModify;
    private transient Path path;

}
