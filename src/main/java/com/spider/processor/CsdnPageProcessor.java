package com.spider.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import com.spider.entity.Blog;
import com.spider.entity.BlogEnum;
import com.spider.service.CsdnService;
import com.spider.util.BlogUtils;

public class CsdnPageProcessor implements PageProcessor {
	private CsdnService csdnService = new CsdnService();
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
	public Site getSite() {
		return site;
	}
	public void process(Page page) {
		//文章链接
		boolean flg = page.getUrl().regex("/article/details/\\d+").match();
		if(flg){
			//提取文章内容元素
			Blog blog = csdnService.getBlobByPage(page);
			blog.setType(BlogEnum.CSDN);
			//将符合规则的url添加到待爬队列中
			csdnService.addSeedTargetRequest(page);
			//编码blog url
			BlogUtils.encodeUrl(blog);
		}else{
			csdnService.addDetailTargetRequest(page);
		}
	}
	
	public static void main(String[] args) {
		System.out.println("【爬虫开始】请耐心等待一大波数据到你碗里来...");
		// 从用户博客首页开始抓，开启5个线程，启动爬虫
		Spider.create(new CsdnPageProcessor()).addUrl("http://blog.csdn.net/mynameishuangshuai").thread(5).run();
	}

}
