package com.spider.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import com.spider.entity.Blog;
import com.spider.util.BloomFilter;
import com.spider.util.ListUtils;
/**
 * csdn爬虫服务类
 * @author 孙洪亮
 *
 */
public class CsdnService {
	
	private BloomFilter bloomFilter = new BloomFilter();
	
	private static Logger logger = Logger.getLogger(CsdnService.class);
	
	public Blog getBlobByPage(Page page) {
		try {
			// 判断是否是转载文章
			String copyright = page.getHtml().xpath("//span[@ico ico_type_Repost]").get();
			Blog blog = new Blog();
			// 博客分类
			String category = ListUtils.listToString(page.getHtml().xpath("//div[@class='category_r']/label/span/text()").all());
			blog.setCategory(category);
			// 评论人数
			int comments = Integer.parseInt(page.getHtml().xpath("//div[@class='article_r']/span[@class='link_comments']").regex("\\((\\d+)\\)").get());
			blog.setComments(comments);
			if(copyright == null || "".equals(copyright.trim())){
				blog.setCopyright(0);
			}else{
				blog.setCopyright(1);
			}
			// 日期
			String date = page.getHtml().xpath("//div[@class='article_r']/span[@class='link_postdate']/text()").get();
			blog.setDate(date);
			// 标签
			String tags = ListUtils.listToString(page.getHtml().xpath("//div[@class='article_l']/span[@class='link_categories']/a/allText()").all());
			blog.setTags(tags);
			// 标题
			String title = page.getHtml().xpath("//div[@class='article_title']//span[@class='link_title']/a/text()").get();
			blog.setTitle(title);
			// 阅读人数
			int view = Integer.parseInt(page.getHtml().xpath("//div[@class='article_r']/span[@class='link_view']").regex("(\\d+)人阅读").get());
			blog.setView(view);
			// 摘要
			String summery = page.getHtml().xpath("//meta[@name='description']/@content").get();
			blog.setSummery(summery);
			// 顶的人数
			int digg = Integer.parseInt(page.getHtml().xpath("//dl[@id='btnDigg']/dd/text()").get());
			blog.setDigg(digg);
			// 踩的人数
			int bury = Integer.parseInt(page.getHtml().xpath("//dl[@id='btnBury']/dd/text()").get());
			blog.setBury(bury);
			// 博客的url
			String url = page.getResultItems().getRequest().getUrl();
			blog.setUrl(url);
			// author
			String author = page.getHtml().xpath("//div[@id='blog_userface']").links().regex("http://my.csdn.net/(\\w+)").get();
			blog.setAuthor(author);
			// 博客内容
			String content = page.getHtml().xpath("//div[@id='article_content']").get();
			blog.setContent(content);
			return blog;
		} catch (Exception e) {
			logger.error("元素内容提取出现异常", e);
		}
		return null;
	}

	/**
	 * 过滤出下次爬寻的页面url
	 */
	public void addDetailTargetRequest(Page page) {
		//过滤选择区域
		Selectable links = page.getHtml().xpath("//div[@class='list_item_new']").links();
		//获取分页链接
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<String> listUrl = (ArrayList)links.regex("/\\w+/article/list/\\d+").all().stream().distinct().collect(Collectors.toList());
		//文章内容连接
		List<String> articleUrl = links.regex("/\\w+/article/details/\\d+").all();
		page.addTargetRequests(listUrl);
		page.addTargetRequests(filterUrl(articleUrl));
	}
	
	private List<String> filterUrl(List<String> ourl){
		if(ourl == null){
			return Collections.emptyList();
		}
		List<String> result = new ArrayList<String>(ourl.size());
		for(String url : ourl){
			if(!bloomFilter.isExistUrl(url)){
				result.add(url);
			}
		}
		return result;
	}

	/**
	 * 添加下子爬取的种子页面
	 */
	public void addSeedTargetRequest(Page page){
		//获取推荐区域
		List<String> seedLinks = page.getHtml().xpath("//div[@class='similar_wrap tracking-ad']").regex("/\\w+/article/details/\\d+").all();
		//从推荐区域中获取推荐文章的作者所有博客入口
		List<String> userArticleUrls = filterUserArticleList(seedLinks);
		page.addTargetRequests(userArticleUrls);
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
			String userArticleUrls = url.substring(0,last);
			result.add(userArticleUrls);
		}
		return result;
	}

	/**
	 * 持久化blog数据
	 */
	public boolean persistenceDate(Blog blog){
		System.out.println(blog);
		return true;
	}
}
