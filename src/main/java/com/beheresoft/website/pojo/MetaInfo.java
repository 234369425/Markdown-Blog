package com.beheresoft.website.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aladi
 */
@Getter
@Setter
@NoArgsConstructor
public class MetaInfo {

    private int hashCode;
    private Boolean locked = Boolean.FALSE;
    private List<MetaData> metas = new ArrayList<>();

    public void addMetaData(MetaData metaData) {
        metas.add(metaData);
    }

}
