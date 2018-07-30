package com.beheresoft.website.extension.sitemap;

import com.beheresoft.website.dict.SystemDict;
import com.beheresoft.website.dict.pojo.MetaData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author Aladi
 */
@Controller
public class SiteMapController {

    private SystemDict systemDict;

    public SiteMapController(SystemDict systemDict) {
        this.systemDict = systemDict;
    }

    @RequestMapping(value = "/sitemap.xml", produces = {"application/xml"})
    @ResponseBody
    public SiteMap siteMap(HttpServletRequest request) {
        String domain = request.getRequestURL().substring(0, request.getRequestURL().length() - request.getRequestURI().length() + 1);
        SiteMap siteMap = new SiteMap();
        siteMap.setDomain(domain);
        siteMap.addUrl("about.html", new Date());

        List<MetaData> metas = systemDict.getMarkdownMetas();
        for (MetaData meta : metas) {
            siteMap.addUrl("article/md/get/" + meta.getHash(), new Date(meta.getLastModify()));
        }

        return siteMap;
    }
}
