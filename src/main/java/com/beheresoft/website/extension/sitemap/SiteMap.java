package com.beheresoft.website.extension.sitemap;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Aladi
 */
@XmlRootElement(name = "urlset")
public class SiteMap {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @XmlElement(name = "url")
    List<Url> urls;

    @Setter
    private String domain;

    public SiteMap() {
        urls = Lists.newArrayList();
    }

    public void addUrl(String path, Date lastModify) {
        urls.add(new Url(domain + path, dateFormat.format(lastModify)));
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @XmlRootElement(name = "url")
    public static class Url {

        @XmlElement
        String loc;
        @XmlElement(name = "lastmod")
        String lastModify;
    }

}
