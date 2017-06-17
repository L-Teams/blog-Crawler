package com.spider.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import us.codecraft.webmagic.Page;

import com.spider.entity.BlogEnum;
import com.spider.entity.Common;
import com.spider.util.ListUtils;
/**
 * csdn爬虫服务类
 * @author 孙洪亮
 *
 */
public class CsdnService {
	
	private static Logger logger = Logger.getLogger(CsdnService.class);
	
	public void putBlogField(Page page) {
		try {
			// 判断是否是转载文章
			String copyright = page.getHtml().xpath("//span[@ico ico_type_Repost]").get();
			// 博客分类
			String category = ListUtils.listToString(page.getHtml().xpath("//div[@class='category_r']/label/span/text()").all());
			page.putField(Common.CATEGORY, category);
			// 评论人数
			String commentsStr = page.getHtml().xpath("//div[@class='article_r']/span[@class='link_comments']").regex("\\((\\d+)\\)").get();
			int comments = Integer.parseInt(commentsStr != null ? commentsStr.trim() : "0");
			page.putField(Common.COMMENTS, comments);
			if(copyright == null || "".equals(copyright.trim())){
				page.putField(Common.COPYRIGHT, 0);
			}else{
				page.putField(Common.COPYRIGHT, 1);
			}
			// 日期
			String date = page.getHtml().xpath("//div[@class='article_r']/span[@class='link_postdate']/text()").get();
			page.putField(Common.DATE, date != null ? date.trim() : null);
			// 标签
			String tags = ListUtils.listToString(page.getHtml().xpath("//div[@class='article_l']/span[@class='link_categories']/a/allText()").all());
			page.putField(Common.TAGS, tags != null ? tags.trim() : null);
			// 标题
			String title = page.getHtml().xpath("//div[@class='article_title']//span[@class='link_title']/a/text()").get();
			page.putField(Common.TITLE, title != null ? title.trim() : null);
			// 阅读人数
			String viewStr = page.getHtml().xpath("//div[@class='article_r']/span[@class='link_view']").regex("(\\d+)人阅读").get();
			int view = Integer.parseInt(viewStr != null ? viewStr.trim() : "0");
			page.putField(Common.VIEW, view);
			// 摘要
			String summery = page.getHtml().xpath("//meta[@name='description']/@content").get();
			page.putField(Common.SUMMERY, summery != null ? summery.trim() : null);
			// 顶的人数
			String diggStr = page.getHtml().xpath("//dl[@id='btnDigg']/dd/text()").get();
			int digg = Integer.parseInt(diggStr != null ? diggStr.trim() : "0");
			page.putField(Common.DIGG, digg);
			// 踩的人数
			String buryStr = page.getHtml().xpath("//dl[@id='btnBury']/dd/text()").get();
			int bury = Integer.parseInt(buryStr != null ? buryStr.trim() : "0");
			page.putField(Common.BURY, bury);
			// 博客的url
			String url = page.getResultItems().getRequest().getUrl();
			page.putField(Common.URL, url != null ? url.trim() : null);
			// author
			String author = page.getUrl().regex("http://blog.csdn.net/(\\w+)/article/details/").get();
			page.putField(Common.AUTHOR, author);
			// 博客内容
			String content = page.getHtml().xpath("//div[@id='article_content']").get();
			if(StringUtils.isEmpty(content))
				page.setSkip(true);
			page.putField(Common.CONTENT, content);
		} catch (Exception e) {
			page.setSkip(true);
			logger.error("元素内容提取出现异常", e);
		}
	}

	/**
	 * 获取所有文章中的日期最后的一篇文章作为种子也
	 */
	public void addDetailTargetRequest(Page page) {
		//推荐文章最上面的一篇
		String recommendArticle = page.getHtml().xpath("//div[@id='article_toplist']").links().get();
		//普通文章最新一个
		String commonArticle = page.getHtml().xpath("//div[@id='article_list']").links().get();
		if(!StringUtils.isEmpty(commonArticle)){
			page.addTargetRequest(commonArticle);
		}else{
			page.addTargetRequest(recommendArticle);
		}
	}

	/**
	 * 添加下子爬取的种子页面
	 */
	public void addSeedTargetRequest(Page page){
		//获取推荐区域
		List<String> seedLinks = page.getHtml().xpath("//div[@class='similar_wrap tracking-ad']").regex("/\\w+/article/details/\\d+").all();
		//从推荐区域中获取推荐文章的作者所有博客入口
		List<String> userArticleUrls = filterUserArticleList(seedLinks);
		page.addTargetRequests(completionUrl(userArticleUrls));
		//获取上一页连接
		String preUrl = completionUrl(page.getHtml().xpath("//li[@class='prev_article']").links().get());
		page.addTargetRequest(preUrl);
	}
	
	/**
	 * 添加推荐文章的博客入口
	 */
	private List<String> filterUserArticleList(List<String> seedLinks) {
		if(seedLinks == null || seedLinks.isEmpty()) 
			return Collections.emptyList();
		List<String> result = new ArrayList<String>(seedLinks.size());
		int last;
		for(String url : seedLinks){
			last = url.indexOf("article/details");
			result.add(url.substring(0,last));
		}
		return result;
	}

	/**
	 * 补全相对url
	 */
	private String completionUrl(String url){
		if(StringUtils.isEmpty(url)){
			return null;
		}
		String host = BlogEnum.CSDN.getHost();
		if(!url.startsWith(host)){
			url = host + url;
		}
		return url;
	}
	
	private List<String> completionUrl(List<String> urls){
		List<String> result = new ArrayList<String>();
		for(String url : urls){
			result.add(completionUrl(url));
		}
		return result;
	}
	
}
