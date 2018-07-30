package com.beheresoft.website.dict.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Aladi
 */
@Getter
@Setter
@NoArgsConstructor
public class MetaInfo {

    private int hashCode;
    private Boolean locked = Boolean.FALSE;
    private Catalog catalog = null;

    public Catalog getCatalog() {
        if (catalog == null) {
            return new Catalog("root", 17);
        }
        return catalog;
    }

}
