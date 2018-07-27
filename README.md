## ![](https://github.com/234369425/Markdown-Blog/blob/master/doc/img/bug32.png?raw=true)MarkDown Blog ![](https://github.com/234369425/Markdown-Blog/blob/master/doc/img/cat48.png?raw=true) ##

一个java MarkDown Blog系统，功能简单，送给喜欢使用markdown写文档的人.

## 功能说明 ##

1. 按照目录结构分档列表
2. 显示文档点击次数

## 配置文件application.yaml ##

    website:
	  markdownDir: 							#MarkDown文件根目录
	  indexDir: 生成的文件目录，				#不配置使用markdownDir
	  articleTitle: FILE_NAME | FIRST_ROW	#enum
	  about: About.md						#About页面markdown名称
	  page:		
	    pageSize: 10						#分页大小
	    newesetSize: 5						#最新文档大小
	    author: Aladi						#显示作者
	    logo: ''							#Logo，支持HTML
	    keywords: ''						#meta增加的keyword
	    titleSuffix: ''						#浏览器标题后缀
	
	  gittalk:
	    owner:
	    repo:
	    oauth:
	      clientId:
	      secret:
	server:
	  port: 9999							#启动端口


