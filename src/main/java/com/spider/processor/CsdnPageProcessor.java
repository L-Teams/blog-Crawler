package com.spider.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import com.spider.entity.Blog;
import com.spider.service.CsdnService;

public class CsdnPageProcessor implements PageProcessor {
	private CsdnService csdnService = new CsdnService();
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
	private static int size = 0;// 共抓取到的文章数量
	public Site getSite() {
		return site;
	}
	public void process(Page page) {
		//文章链接
		boolean flg = page.getUrl().regex("/article/details/\\d+").match();
		if(flg){
			size ++;
			Blog blog = csdnService.getBlobByPage(page);
			System.out.println(blog);
			csdnService.addSeedTargetRequest(page);
		}else{
			csdnService.addDetailTargetRequest(page);
		}
	}
	
	public static void main(String[] args) {
		long startTime, endTime;
		System.out.println("【爬虫开始】请耐心等待一大波数据到你碗里来...");
		startTime = System.currentTimeMillis();
		// 从用户博客首页开始抓，开启5个线程，启动爬虫
		Spider.create(new CsdnPageProcessor()).addUrl("http://blog.csdn.net/mynameishuangshuai").thread(5).run();
		endTime = System.currentTimeMillis();
		System.out.println("【爬虫结束】共抓取" + size + "篇文章，耗时约" + ((endTime - startTime) / 1000) + "秒，已保存到数据库，请查收！");
	}

}
