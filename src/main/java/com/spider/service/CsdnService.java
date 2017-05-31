package com.spider.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
	
	private BloomFilter useFilter = new BloomFilter();
	
	public Blog getBlobByPage(Page page) {
		Blog blog = new Blog();
		// 博客分类
		String category = ListUtils.listToString(page.getHtml().xpath("//div[@class='category_r']/label/span/text()").all());
		blog.setCategory(category);
		// 评论人数
		int comments = Integer.parseInt(page.getHtml().xpath("//div[@class='article_r']/span[@class='link_comments']").regex("\\((\\d+)\\)").get());
		blog.setComments(comments);
		// 是否原创
		int copyright = page.getHtml().regex("bog_copyright").match() ? 1 : 0;
		blog.setCopyright(copyright);
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
		
		return blog;
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
		page.addTargetRequests(articleUrl);
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
	 * 过滤出推荐文章的博客入口
	 */
	private List<String> filterUserArticleList(List<String> seedLinks) {
		if(seedLinks == null || seedLinks.isEmpty()) 
			return Collections.emptyList();
		List<String> result = new ArrayList<String>(seedLinks.size());
		int last;
		for(String url : seedLinks){
			last = url.indexOf("article/details");
			String userArticleUrls = url.substring(0,last);
			boolean existUrl = useFilter.isExistUrl(userArticleUrls);
			if(!existUrl){
				result.add(userArticleUrls);
			}
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
