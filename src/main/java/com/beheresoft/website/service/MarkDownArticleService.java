package com.beheresoft.website.service;

import com.vladsch.flexmark.ast.Document;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.springframework.stereotype.Service;

/**
 * @author Aladi
 */
@Service
public class MarkDownArticleService {

    private HtmlRenderer renderer;
    private Parser parser;

    public MarkDownArticleService(HtmlRenderer renderer, Parser parser) {
        this.renderer = renderer;
        this.parser = parser;
    }

    public String get(){
        return parse("# KoDi简易遥控器 #\n" +
                "  KoDi原有界面操作过于复杂，不适合老人、孩子使用，基于\n" +
                "\t[KoDi RPC API v8](https://kodi.wiki/view/JSON-RPC_API/v9)\n" +
                "\t实现了一个简易遥控器\n" +
                "\n" +
                "- 支持配置地址\n" +
                "- 支持音量控制\n" +
                "- PRV频道直接选择\n" +
                "- KoDi pvr地址: http://kc.eastapple.com/playlist.m3u8\n" +
                "\n" +
                "\n" +
                "[跳转试用地址](http://kc.eastapple.com \"在线试用地址\")\n" +
                "\n" +
                "![遥控器展示](https://raw.githubusercontent.com/234369425/KoDiRemoteControl/master/doc/img/control.png)\n" +
                "\n" +
                "## 下载地址 ##\n" +
                "包含树莓派，windows等下载\n" +
                "\n" +
                "[Kodi官网下载地址](https://kodi.tv/download)\n" +
                "\n" +
                "[安卓TV 16.1 apk\n" +
                "安装包](http://pb47lh0dj.bkt.clouddn.com//file/kodi-16.1.apk \"KoDi 16.1 TV版下载\")");
    }

    private String parse(String markdown) {
        Document document = parser.parse(markdown);
        return renderer.render(document);
    }

}
